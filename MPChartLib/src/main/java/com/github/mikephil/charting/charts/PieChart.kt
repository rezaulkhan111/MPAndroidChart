package com.github.mikephil.charting.charts

import android.content.Context
import android.graphics.Canvas
import android.graphics.RectF
import android.graphics.Typeface
import android.util.AttributeSet
import com.github.mikephil.charting.components.XAxis
import com.github.mikephil.charting.data.PieData
import com.github.mikephil.charting.highlight.Highlight
import com.github.mikephil.charting.highlight.PieHighlighter
import com.github.mikephil.charting.interfaces.datasets.IPieDataSet
import com.github.mikephil.charting.renderer.PieChartRenderer
import com.github.mikephil.charting.utils.MPPointF
import com.github.mikephil.charting.utils.MPPointF.Companion.getInstance
import com.github.mikephil.charting.utils.MPPointF.Companion.recycleInstance
import com.github.mikephil.charting.utils.Utils.convertDpToPixel
import com.github.mikephil.charting.utils.Utils.getNormalizedAngle

/**
 * View that represents a pie chart. Draws cake like slices.
 *
 * @author Philipp Jahoda
 */
open class PieChart : PieRadarChartBase<PieData?> {
    /**
     * rect object that represents the bounds of the piechart, needed for
     * drawing the circle
     */
    private val mCircleBox: RectF = RectF()

    /**
     * flag indicating if entry labels should be drawn or not
     */
    private var mDrawEntryLabels = true

    /**
     * array that holds the width of each pie-slice in degrees
     */
    private var mDrawAngles = FloatArray(1)

    /**
     * array that holds the absolute angle in degrees of each slice
     */
    private var mAbsoluteAngles = FloatArray(1)

    /**
     * if true, the white hole inside the chart will be drawn
     */
    private var mDrawHole = true

    /**
     * if true, the hole will see-through to the inner tips of the slices
     */
    private var mDrawSlicesUnderHole = false

    /**
     * if true, the values inside the piechart are drawn as percent values
     */
    private var mUsePercentValues = false

    /**
     * if true, the slices of the piechart are rounded
     */
    private var mDrawRoundedSlices = false

    /**
     * variable for the text that is drawn in the center of the pie-chart
     */
    private var mCenterText: CharSequence = ""

    private val mCenterTextOffset = getInstance(0f, 0f)

    /**
     * indicates the size of the hole in the center of the piechart, default:
     * radius / 2
     */
    private var mHoleRadiusPercent = 50f

    /**
     * the radius of the transparent circle next to the chart-hole in the center
     */
    protected var mTransparentCircleRadiusPercent = 55f

    /**
     * if enabled, centertext is drawn
     */
    private var mDrawCenterText = true

    private var mCenterTextRadiusPercent = 100f

    protected var mMaxAngle = 360f

    /**
     * Minimum angle to draw slices, this only works if there is enough room for all slices to have
     * the minimum angle, default 0f.
     */
    private var mMinAngleForSlices = 0f

    constructor(context: Context?) : super(context) {

    }

    constructor(context: Context?, attrs: AttributeSet?) : super(context, attrs) {

    }

    constructor(context: Context?, attrs: AttributeSet?, defStyle: Int) : super(
        context,
        attrs,
        defStyle
    ) {
    }

    override fun init() {
        super.init()
        mRenderer = PieChartRenderer(this, mAnimator!!, mViewPortHandler)
        mXAxis = null
        mHighlighter = PieHighlighter(this)
    }

    override fun onDraw(canvas: Canvas) {
        super.onDraw(canvas)
        if (mData == null) return
        mRenderer!!.drawData(canvas)
        if (valuesToHighlight()) mRenderer!!.drawHighlighted(canvas, mIndicesToHighlight)
        mRenderer!!.drawExtras(canvas)
        mRenderer!!.drawValues(canvas)
        mLegendRenderer!!.renderLegend(canvas)
        drawDescription(canvas)
        drawMarkers(canvas)
    }

    override fun calculateOffsets() {
        super.calculateOffsets()

        // prevent nullpointer when no data set
        if (mData == null) return
        val diameter = getDiameter();
        val radius = diameter / 2f
        val c = getCenterOffsets()
        val shift = mData!!.getDataSet().getSelectionShift()

        // create the circle box that will contain the pie-chart (the bounds of
        // the pie-chart)
        mCircleBox!![c.x - radius + shift, c.y - radius + shift, c.x + radius - shift] =
            c.y + radius - shift
        recycleInstance(c)
    }

    override fun calcMinMax() {
        calcAngles()
    }

    override fun getMarkerPosition(highlight: Highlight?): FloatArray {
        val center = getCenterCircleBox()
        var r = getRadius()
        var off = r / 10f * 3.6f
        if (isDrawHoleEnabled()) {
            off = (r - r / 100f * getHoleRadius()) / 2f
        }
        r -= off // offset to keep things inside the chart
        val rotationAngle = getRotationAngle();
        val entryIndex = highlight!!.getX().toInt()

        // offset needed to center the drawn text in the slice
        val offset = mDrawAngles[entryIndex] / 2

        // calculate the text position
        val x = (r
                * Math.cos(
            Math.toRadians(
                ((rotationAngle + mAbsoluteAngles[entryIndex] - offset)
                        * mAnimator!!.getPhaseY()).toDouble()
            )
        ) + center.x).toFloat()
        val y = (r
                * Math.sin(
            Math.toRadians(
                ((rotationAngle + mAbsoluteAngles[entryIndex] - offset)
                        * mAnimator!!.getPhaseY()).toDouble()
            )
        ) + center.y).toFloat()
        recycleInstance(center)
        return floatArrayOf(x, y)
    }

    /**
     * calculates the needed angles for the chart slices
     */
    private fun calcAngles() {
        val entryCount = mData!!.getEntryCount()
        if (mDrawAngles.size != entryCount) {
            mDrawAngles = FloatArray(entryCount)
        } else {
            for (i in 0 until entryCount) {
                mDrawAngles[i] = 0f
            }
        }
        if (mAbsoluteAngles.size != entryCount) {
            mAbsoluteAngles = FloatArray(entryCount)
        } else {
            for (i in 0 until entryCount) {
                mAbsoluteAngles[i] = 0f
            }
        }
        val yValueSum = mData!!.getYValueSum()
        val dataSets = mData!!.getDataSets();
        val hasMinAngle = mMinAngleForSlices != 0f && entryCount * mMinAngleForSlices <= mMaxAngle
        val minAngles = FloatArray(entryCount)
        var cnt = 0
        var offset = 0f
        var diff = 0f
        for (i in 0 until mData!!.getDataSetCount()) {
            val set = dataSets[i]
            for (j in 0 until set!!.getEntryCount()) {
                val drawAngle = calcAngle(Math.abs(set.getEntryForIndex(j)!!.getY()), yValueSum)
                if (hasMinAngle) {
                    val temp = drawAngle - mMinAngleForSlices
                    if (temp <= 0) {
                        minAngles[cnt] = mMinAngleForSlices
                        offset += -temp
                    } else {
                        minAngles[cnt] = drawAngle
                        diff += temp
                    }
                }
                mDrawAngles[cnt] = drawAngle
                if (cnt == 0) {
                    mAbsoluteAngles[cnt] = mDrawAngles[cnt]
                } else {
                    mAbsoluteAngles[cnt] = mAbsoluteAngles[cnt - 1] + mDrawAngles[cnt]
                }
                cnt++
            }
        }
        if (hasMinAngle) {
            // Correct bigger slices by relatively reducing their angles based on the total angle needed to subtract
            // This requires that `entryCount * mMinAngleForSlices <= mMaxAngle` be true to properly work!
            for (i in 0 until entryCount) {
                minAngles[i] -= (minAngles[i] - mMinAngleForSlices) / diff * offset
                if (i == 0) {
                    mAbsoluteAngles[0] = minAngles[0]
                } else {
                    mAbsoluteAngles[i] = mAbsoluteAngles[i - 1] + minAngles[i]
                }
            }
            mDrawAngles = minAngles
        }
    }

    /**
     * Checks if the given index is set to be highlighted.
     *
     * @param index
     * @return
     */
    fun needsHighlight(index: Int): Boolean {

        // no highlight
        if (!valuesToHighlight()) return false
        for (i in 0 until mIndicesToHighlight!!.size)  // check if the xvalue for the given dataset needs highlight
            if (mIndicesToHighlight!![i].getX().toInt() == index) return true
        return false
    }

    /**
     * calculates the needed angle for a given value
     *
     * @param value
     * @return
     */
    private fun calcAngle(value: Float): Float {
        return calcAngle(value, mData!!.getYValueSum())
    }

    /**
     * calculates the needed angle for a given value
     *
     * @param value
     * @param yValueSum
     * @return
     */
    private fun calcAngle(value: Float, yValueSum: Float): Float {
        return value / yValueSum * mMaxAngle
    }

    /**
     * This will throw an exception, PieChart has no XAxis object.
     *
     * @return
     */
    @Deprecated("")
    override fun getXAxis(): XAxis? {
        throw RuntimeException("PieChart has no XAxis")
    }

    override fun getIndexForAngle(angle: Float): Int {

        // take the current angle of the chart into consideration
        val a = getNormalizedAngle(angle - getRotationAngle())
        for (i in mAbsoluteAngles.indices) {
            if (mAbsoluteAngles[i] > a) return i
        }
        return -1 // return -1 if no index found
    }

    /**
     * Returns the index of the DataSet this x-index belongs to.
     *
     * @param xIndex
     * @return
     */
    fun getDataSetIndexForIndex(xIndex: Int): Int {
        val dataSets = mData!!.getDataSets()
        for (i in dataSets.indices) {
            if (dataSets[i]!!.getEntryForXValue(xIndex.toFloat(), Float.NaN) != null) return i
        }
        return -1
    }

    /**
     * returns an integer array of all the different angles the chart slices
     * have the angles in the returned array determine how much space (of 360°)
     * each slice takes
     *
     * @return
     */
    fun getDrawAngles(): FloatArray? {
        return mDrawAngles
    }

    /**
     * returns the absolute angles of the different chart slices (where the
     * slices end)
     *
     * @return
     */
    fun getAbsoluteAngles(): FloatArray? {
        return mAbsoluteAngles
    }

    /**
     * Sets the color for the hole that is drawn in the center of the PieChart
     * (if enabled).
     *
     * @param color
     */
    fun setHoleColor(color: Int) {
        (mRenderer as PieChartRenderer).getPaintHole()!!.color = color
    }

    /**
     * Enable or disable the visibility of the inner tips of the slices behind the hole
     */
    fun setDrawSlicesUnderHole(enable: Boolean) {
        mDrawSlicesUnderHole = enable
    }

    /**
     * Returns true if the inner tips of the slices are visible behind the hole,
     * false if not.
     *
     * @return true if slices are visible behind the hole.
     */
    fun isDrawSlicesUnderHoleEnabled(): Boolean {
        return mDrawSlicesUnderHole
    }

    /**
     * set this to true to draw the pie center empty
     *
     * @param enabled
     */
    fun setDrawHoleEnabled(enabled: Boolean) {
        mDrawHole = enabled
    }

    /**
     * returns true if the hole in the center of the pie-chart is set to be
     * visible, false if not
     *
     * @return
     */
    fun isDrawHoleEnabled(): Boolean {
        return mDrawHole
    }

    /**
     * Sets the text String that is displayed in the center of the PieChart.
     *
     * @param text
     */
    fun setCenterText(text: CharSequence?) {
        mCenterText = text ?: ""
    }

    /**
     * returns the text that is drawn in the center of the pie-chart
     *
     * @return
     */
    fun getCenterText(): CharSequence? {
        return mCenterText
    }

    /**
     * set this to true to draw the text that is displayed in the center of the
     * pie chart
     *
     * @param enabled
     */
    fun setDrawCenterText(enabled: Boolean) {
        mDrawCenterText = enabled
    }

    /**
     * returns true if drawing the center text is enabled
     *
     * @return
     */
    fun isDrawCenterTextEnabled(): Boolean {
        return mDrawCenterText
    }

    override fun getRequiredLegendOffset(): Float {
        return mLegendRenderer!!.getLabelPaint()!!.textSize * 2f
    }

    override fun getRequiredBaseOffset(): Float {
        return 0f
    }

    override fun getRadius(): Float {
        return if (mCircleBox == null) 0f else Math.min(
            mCircleBox.width() / 2f,
            mCircleBox.height() / 2f
        )
    }

    /**
     * returns the circlebox, the boundingbox of the pie-chart slices
     *
     * @return
     */
    fun getCircleBox(): RectF? {
        return mCircleBox
    }

    /**
     * returns the center of the circlebox
     *
     * @return
     */
    fun getCenterCircleBox(): MPPointF {
        return getInstance(mCircleBox!!.centerX(), mCircleBox.centerY())
    }

    /**
     * sets the typeface for the center-text paint
     *
     * @param t
     */
    fun setCenterTextTypeface(t: Typeface?) {
        (mRenderer as PieChartRenderer).getPaintCenterText()!!.typeface = t
    }

    /**
     * Sets the size of the center text of the PieChart in dp.
     *
     * @param sizeDp
     */
    fun setCenterTextSize(sizeDp: Float) {
        (mRenderer as PieChartRenderer).getPaintCenterText()!!.textSize = convertDpToPixel(sizeDp)
    }

    /**
     * Sets the size of the center text of the PieChart in pixels.
     *
     * @param sizePixels
     */
    fun setCenterTextSizePixels(sizePixels: Float) {
        (mRenderer as PieChartRenderer).getPaintCenterText()!!.textSize = sizePixels
    }

    /**
     * Sets the offset the center text should have from it's original position in dp. Default x = 0, y = 0
     *
     * @param x
     * @param y
     */
    fun setCenterTextOffset(x: Float, y: Float) {
        mCenterTextOffset.x = convertDpToPixel(x)
        mCenterTextOffset.y = convertDpToPixel(y)
    }

    /**
     * Returns the offset on the x- and y-axis the center text has in dp.
     *
     * @return
     */
    fun getCenterTextOffset(): MPPointF {
        return getInstance(mCenterTextOffset.x, mCenterTextOffset.y)
    }

    /**
     * Sets the color of the center text of the PieChart.
     *
     * @param color
     */
    fun setCenterTextColor(color: Int) {
        (mRenderer as PieChartRenderer).getPaintCenterText()!!.color = color
    }

    /**
     * sets the radius of the hole in the center of the piechart in percent of
     * the maximum radius (max = the radius of the whole chart), default 50%
     *
     * @param percent
     */
    fun setHoleRadius(percent: Float) {
        mHoleRadiusPercent = percent
    }

    /**
     * Returns the size of the hole radius in percent of the total radius.
     *
     * @return
     */
    fun getHoleRadius(): Float {
        return mHoleRadiusPercent
    }

    /**
     * Sets the color the transparent-circle should have.
     *
     * @param color
     */
    fun setTransparentCircleColor(color: Int) {
        val p = (mRenderer as PieChartRenderer).getPaintTransparentCircle()
        val alpha = p!!.alpha
        p.color = color
        p.alpha = alpha
    }

    /**
     * sets the radius of the transparent circle that is drawn next to the hole
     * in the piechart in percent of the maximum radius (max = the radius of the
     * whole chart), default 55% -> means 5% larger than the center-hole by
     * default
     *
     * @param percent
     */
    fun setTransparentCircleRadius(percent: Float) {
        mTransparentCircleRadiusPercent = percent
    }

    fun getTransparentCircleRadius(): Float {
        return mTransparentCircleRadiusPercent
    }

    /**
     * Sets the amount of transparency the transparent circle should have 0 = fully transparent,
     * 255 = fully opaque.
     * Default value is 100.
     *
     * @param alpha 0-255
     */
    fun setTransparentCircleAlpha(alpha: Int) {
        (mRenderer as PieChartRenderer).getPaintTransparentCircle()!!.alpha = alpha
    }

    /**
     * Set this to true to draw the entry labels into the pie slices (Provided by the getLabel() method of the PieEntry class).
     * Deprecated -> use setDrawEntryLabels(...) instead.
     *
     * @param enabled
     */
    @Deprecated("")
    fun setDrawSliceText(enabled: Boolean) {
        mDrawEntryLabels = enabled
    }

    /**
     * Set this to true to draw the entry labels into the pie slices (Provided by the getLabel() method of the PieEntry class).
     *
     * @param enabled
     */
    fun setDrawEntryLabels(enabled: Boolean) {
        mDrawEntryLabels = enabled
    }

    /**
     * Returns true if drawing the entry labels is enabled, false if not.
     *
     * @return
     */
    fun isDrawEntryLabelsEnabled(): Boolean {
        return mDrawEntryLabels
    }

    /**
     * Sets the color the entry labels are drawn with.
     *
     * @param color
     */
    fun setEntryLabelColor(color: Int) {
        (mRenderer as PieChartRenderer).getPaintEntryLabels()!!.color = color
    }

    /**
     * Sets a custom Typeface for the drawing of the entry labels.
     *
     * @param tf
     */
    fun setEntryLabelTypeface(tf: Typeface?) {
        (mRenderer as PieChartRenderer).getPaintEntryLabels()!!.typeface = tf
    }

    /**
     * Sets the size of the entry labels in dp. Default: 13dp
     *
     * @param size
     */
    fun setEntryLabelTextSize(size: Float) {
        (mRenderer as PieChartRenderer).getPaintEntryLabels()!!.textSize = convertDpToPixel(size)
    }

    /**
     * Sets whether to draw slices in a curved fashion, only works if drawing the hole is enabled
     * and if the slices are not drawn under the hole.
     *
     * @param enabled draw curved ends of slices
     */
    fun setDrawRoundedSlices(enabled: Boolean) {
        mDrawRoundedSlices = enabled
    }

    /**
     * Returns true if the chart is set to draw each end of a pie-slice
     * "rounded".
     *
     * @return
     */
    fun isDrawRoundedSlicesEnabled(): Boolean {
        return mDrawRoundedSlices
    }

    /**
     * If this is enabled, values inside the PieChart are drawn in percent and
     * not with their original value. Values provided for the IValueFormatter to
     * format are then provided in percent.
     *
     * @param enabled
     */
    fun setUsePercentValues(enabled: Boolean) {
        mUsePercentValues = enabled
    }

    /**
     * Returns true if using percentage values is enabled for the chart.
     *
     * @return
     */
    fun isUsePercentValuesEnabled(): Boolean {
        return mUsePercentValues
    }

    /**
     * the rectangular radius of the bounding box for the center text, as a percentage of the pie
     * hole
     * default 1.f (100%)
     */
    fun setCenterTextRadiusPercent(percent: Float) {
        mCenterTextRadiusPercent = percent
    }

    /**
     * the rectangular radius of the bounding box for the center text, as a percentage of the pie
     * hole
     * default 1.f (100%)
     */
    fun getCenterTextRadiusPercent(): Float {
        return mCenterTextRadiusPercent
    }

    fun getMaxAngle(): Float {
        return mMaxAngle
    }

    /**
     * Sets the max angle that is used for calculating the pie-circle. 360f means
     * it's a full PieChart, 180f results in a half-pie-chart. Default: 360f
     *
     * @param maxangle min 90, max 360
     */
    fun setMaxAngle(maxangle: Float) {
        var maxangle = maxangle
        if (maxangle > 360) maxangle = 360f
        if (maxangle < 90) maxangle = 90f
        mMaxAngle = maxangle
    }

    /**
     * The minimum angle slices on the chart are rendered with, default is 0f.
     *
     * @return minimum angle for slices
     */
    fun getMinAngleForSlices(): Float {
        return mMinAngleForSlices
    }

    /**
     * Set the angle to set minimum size for slices, you must call [.notifyDataSetChanged]
     * and [.invalidate] when changing this, only works if there is enough room for all
     * slices to have the minimum angle.
     *
     * @param minAngle minimum 0, maximum is half of [.setMaxAngle]
     */
    fun setMinAngleForSlices(minAngle: Float) {
        var minAngle = minAngle
        if (minAngle > mMaxAngle / 2f) minAngle = mMaxAngle / 2f else if (minAngle < 0) minAngle =
            0f
        mMinAngleForSlices = minAngle
    }

    override fun onDetachedFromWindow() {
        // releases the bitmap in the renderer to avoid oom error
        if (mRenderer != null && mRenderer is PieChartRenderer) {
            (mRenderer as PieChartRenderer).releaseBitmap()
        }
        super.onDetachedFromWindow()
    }
}