package com.github.mikephil.charting.charts

import android.animation.ObjectAnimator
import android.annotation.SuppressLint
import android.content.Context
import android.util.AttributeSet
import android.util.Log
import android.view.MotionEvent
import com.github.mikephil.charting.animation.Easing.EasingFunction
import com.github.mikephil.charting.components.Legend.*
import com.github.mikephil.charting.data.*
import com.github.mikephil.charting.interfaces.datasets.IDataSet
import com.github.mikephil.charting.listener.PieRadarChartTouchListener
import com.github.mikephil.charting.utils.MPPointF
import com.github.mikephil.charting.utils.MPPointF.Companion.getInstance
import com.github.mikephil.charting.utils.MPPointF.Companion.recycleInstance
import com.github.mikephil.charting.utils.Utils.convertDpToPixel
import com.github.mikephil.charting.utils.Utils.getNormalizedAngle

/**
 * Baseclass of PieChart and RadarChart.
 *
 * @author Philipp Jahoda
 */
abstract class PieRadarChartBase<T : ChartData<out IDataSet<out Entry?>?>?> : Chart<T> {
    /**
     * holds the normalized version of the current rotation angle of the chart
     */
    private var mRotationAngle = 270f

    /**
     * holds the raw version of the current rotation angle of the chart
     */
    private var mRawRotationAngle = 270f

    /**
     * flag that indicates if rotation is enabled or not
     */
    protected var mRotateEnabled = true

    /**
     * Sets the minimum offset (padding) around the chart, defaults to 0.f
     */
    protected var mMinOffset = 0f

    constructor(context: Context?) : super(context) {}
    constructor(context: Context?, attrs: AttributeSet?) : super(context, attrs) {}
    constructor(context: Context?, attrs: AttributeSet?, defStyle: Int) : super(
        context,
        attrs,
        defStyle
    ) {
    }

    override fun init() {
        super.init()
        mChartTouchListener = PieRadarChartTouchListener(this)
    }

    override fun calcMinMax() {
        //mXAxis.mAxisRange = mData.getXVals().size() - 1;
    }

    override fun getMaxVisibleCount(): Int {
        return mData!!.getEntryCount()
    }

    override fun onTouchEvent(event: MotionEvent?): Boolean {
        // use the pie- and radarchart listener own listener
        return if (mTouchEnabled && mChartTouchListener != null) mChartTouchListener!!.onTouch(
            this,
            event
        ) else super.onTouchEvent(event)
    }

    override fun computeScroll() {
        if (mChartTouchListener is PieRadarChartTouchListener) (mChartTouchListener as PieRadarChartTouchListener).computeScroll()
    }

    override fun notifyDataSetChanged() {
        if (mData == null) return
        calcMinMax()
        if (mLegend != null) mLegendRenderer!!.computeLegend(mData!!)
        calculateOffsets()
    }

    override fun calculateOffsets() {
        var legendLeft = 0f
        var legendRight = 0f
        var legendBottom = 0f
        var legendTop = 0f
        if (mLegend != null && mLegend!!.isEnabled() && !mLegend!!.isDrawInsideEnabled()) {
            val fullLegendWidth = Math.min(
                mLegend!!.mNeededWidth,
                mViewPortHandler.chartWidth * mLegend!!.getMaxSizePercent()
            )
            when (mLegend!!.getOrientation()) {
                LegendOrientation.VERTICAL -> {
                    var xLegendOffset = 0f
                    if (mLegend!!.getHorizontalAlignment() === LegendHorizontalAlignment.LEFT
                        || mLegend!!.getHorizontalAlignment() === LegendHorizontalAlignment.RIGHT
                    ) {
                        if (mLegend!!.getVerticalAlignment() === LegendVerticalAlignment.CENTER) {
                            // this is the space between the legend and the chart
                            val spacing = convertDpToPixel(13f)
                            xLegendOffset = fullLegendWidth + spacing
                        } else {
                            // this is the space between the legend and the chart
                            val spacing = convertDpToPixel(8f)
                            val legendWidth = fullLegendWidth + spacing
                            val legendHeight = mLegend!!.mNeededHeight + mLegend!!.mTextHeightMax
                            val center = getCenter()
                            val bottomX = if (mLegend!!.getHorizontalAlignment() ===
                                LegendHorizontalAlignment.RIGHT
                            ) width - legendWidth + 15f else legendWidth - 15f
                            val bottomY = legendHeight + 15f
                            val distLegend = distanceToCenter(bottomX, bottomY)
                            val reference = getPosition(
                                center, getRadius(),
                                getAngleForPoint(bottomX, bottomY)
                            )
                            val distReference = distanceToCenter(reference.x, reference.y)
                            val minOffset = convertDpToPixel(5f)
                            if (bottomY >= center.y && height - legendWidth > width) {
                                xLegendOffset = legendWidth
                            } else if (distLegend < distReference) {
                                val diff = distReference - distLegend
                                xLegendOffset = minOffset + diff
                            }
                            recycleInstance(center)
                            recycleInstance(reference)
                        }
                    }
                    when (mLegend!!.getHorizontalAlignment()) {
                        LegendHorizontalAlignment.LEFT -> legendLeft = xLegendOffset
                        LegendHorizontalAlignment.RIGHT -> legendRight = xLegendOffset
                        LegendHorizontalAlignment.CENTER -> when (mLegend!!.getVerticalAlignment()) {
                            LegendVerticalAlignment.TOP -> legendTop = Math.min(
                                mLegend!!.mNeededHeight,
                                mViewPortHandler.chartHeight * mLegend!!.getMaxSizePercent()
                            )
                            LegendVerticalAlignment.BOTTOM -> legendBottom = Math.min(
                                mLegend!!.mNeededHeight,
                                mViewPortHandler.chartHeight * mLegend!!.getMaxSizePercent()
                            )
                            else -> {}
                        }
                    }
                }
                LegendOrientation.HORIZONTAL -> {
                    var yLegendOffset = 0f
                    if (mLegend!!.getVerticalAlignment() === LegendVerticalAlignment.TOP ||
                        mLegend!!.getVerticalAlignment() === LegendVerticalAlignment.BOTTOM
                    ) {

                        // It's possible that we do not need this offset anymore as it
                        //   is available through the extraOffsets, but changing it can mean
                        //   changing default visibility for existing apps.
                        val yOffset = getRequiredLegendOffset()
                        yLegendOffset = Math.min(
                            mLegend!!.mNeededHeight + yOffset,
                            mViewPortHandler.chartHeight * mLegend!!.getMaxSizePercent()
                        )
                        when (mLegend!!.getVerticalAlignment()) {
                            LegendVerticalAlignment.TOP -> legendTop = yLegendOffset
                            LegendVerticalAlignment.BOTTOM -> legendBottom = yLegendOffset
                            else -> {}
                        }
                    }
                }
            }
            legendLeft += getRequiredBaseOffset()
            legendRight += getRequiredBaseOffset()
            legendTop += getRequiredBaseOffset()
            legendBottom += getRequiredBaseOffset()
        }
        var minOffset = convertDpToPixel(mMinOffset)
        if (this is RadarChart) {
            val x = getXAxis()
            if (x!!.isEnabled() && x.isDrawLabelsEnabled()) {
                minOffset = Math.max(minOffset, x.mLabelRotatedWidth)
            }
        }
        legendTop += getExtraTopOffset()
        legendRight += getExtraRightOffset()
        legendBottom += getExtraBottomOffset()
        legendLeft += getExtraLeftOffset()
        val offsetLeft = Math.max(minOffset, legendLeft)
        val offsetTop = Math.max(minOffset, legendTop)
        val offsetRight = Math.max(minOffset, legendRight)
        val offsetBottom = Math.max(minOffset, Math.max(getRequiredBaseOffset(), legendBottom))
        mViewPortHandler.restrainViewPort(offsetLeft, offsetTop, offsetRight, offsetBottom)
        if (mLogEnabled) Log.i(
            LOG_TAG, "offsetLeft: " + offsetLeft + ", offsetTop: " + offsetTop
                    + ", offsetRight: " + offsetRight + ", offsetBottom: " + offsetBottom
        )
    }

    /**
     * returns the angle relative to the chart center for the given point on the
     * chart in degrees. The angle is always between 0 and 360°, 0° is NORTH,
     * 90° is EAST, ...
     *
     * @param x
     * @param y
     * @return
     */
    open fun getAngleForPoint(x: Float, y: Float): Float {
        val c = getCenterOffsets()
        val tx = (x - c!!.x).toDouble()
        val ty = (y - c.y).toDouble()
        val length = Math.sqrt(tx * tx + ty * ty)
        val r = Math.acos(ty / length)
        var angle = Math.toDegrees(r).toFloat()
        if (x > c.x) angle = 360f - angle

        // add 90° because chart starts EAST
        angle = angle + 90f

        // neutralize overflow
        if (angle > 360f) angle = angle - 360f
        recycleInstance(c)
        return angle
    }

    /**
     * Returns a recyclable MPPointF instance.
     * Calculates the position around a center point, depending on the distance
     * from the center, and the angle of the position around the center.
     *
     * @param center
     * @param dist
     * @param angle  in degrees, converted to radians internally
     * @return
     */
    open fun getPosition(center: MPPointF, dist: Float, angle: Float): MPPointF {
        val p = getInstance(0f, 0f)
        getPosition(center, dist, angle, p)
        return p
    }

    open fun getPosition(center: MPPointF, dist: Float, angle: Float, outputPoint: MPPointF) {
        outputPoint.x = (center.x + dist * Math.cos(Math.toRadians(angle.toDouble()))).toFloat()
        outputPoint.y = (center.y + dist * Math.sin(Math.toRadians(angle.toDouble()))).toFloat()
    }

    /**
     * Returns the distance of a certain point on the chart to the center of the
     * chart.
     *
     * @param x
     * @param y
     * @return
     */
    open fun distanceToCenter(x: Float, y: Float): Float {
        val c = getCenterOffsets()
        var dist = 0f
        var xDist = 0f
        var yDist = 0f
        xDist = if (x > c!!.x) {
            x - c.x
        } else {
            c.x - x
        }
        yDist = if (y > c.y) {
            y - c.y
        } else {
            c.y - y
        }

        // pythagoras
        dist =
            Math.sqrt(Math.pow(xDist.toDouble(), 2.0) + Math.pow(yDist.toDouble(), 2.0)).toFloat()
        recycleInstance(c)
        return dist
    }

    /**
     * Returns the xIndex for the given angle around the center of the chart.
     * Returns -1 if not found / outofbounds.
     *
     * @param angle
     * @return
     */
    abstract fun getIndexForAngle(angle: Float): Int

    /**
     * Set an offset for the rotation of the RadarChart in degrees. Default 270f
     * --> top (NORTH)
     *
     * @param angle
     */
    open fun setRotationAngle(angle: Float) {
        mRawRotationAngle = angle
        mRotationAngle = getNormalizedAngle(mRawRotationAngle)
    }

    /**
     * gets the raw version of the current rotation angle of the pie chart the
     * returned value could be any value, negative or positive, outside of the
     * 360 degrees. this is used when working with rotation direction, mainly by
     * gestures and animations.
     *
     * @return
     */
    open fun getRawRotationAngle(): Float {
        return mRawRotationAngle
    }

    /**
     * gets a normalized version of the current rotation angle of the pie chart,
     * which will always be between 0.0 < 360.0
     *
     * @return
     */
    open fun getRotationAngle(): Float {
        return mRotationAngle
    }

    /**
     * Set this to true to enable the rotation / spinning of the chart by touch.
     * Set it to false to disable it. Default: true
     *
     * @param enabled
     */
    open fun setRotationEnabled(enabled: Boolean) {
        mRotateEnabled = enabled
    }

    /**
     * Returns true if rotation of the chart by touch is enabled, false if not.
     *
     * @return
     */
    open fun isRotationEnabled(): Boolean {
        return mRotateEnabled
    }

    /**
     * Gets the minimum offset (padding) around the chart, defaults to 0.f
     */
    open fun getMinOffset(): Float {
        return mMinOffset
    }

    /**
     * Sets the minimum offset (padding) around the chart, defaults to 0.f
     */
    open fun setMinOffset(minOffset: Float) {
        mMinOffset = minOffset
    }

    /**
     * returns the diameter of the pie- or radar-chart
     *
     * @return
     */
    open fun getDiameter(): Float {
        val content = mViewPortHandler.contentRect
        content.left += getExtraLeftOffset()
        content.top += getExtraTopOffset()
        content.right -= getExtraRightOffset()
        content.bottom -= getExtraBottomOffset()
        return Math.min(content.width(), content.height())
    }

    /**
     * Returns the radius of the chart in pixels.
     *
     * @return
     */
    abstract fun getRadius(): Float

    /**
     * Returns the required offset for the chart legend.
     *
     * @return
     */
    protected abstract fun getRequiredLegendOffset(): Float

    /**
     * Returns the base offset needed for the chart without calculating the
     * legend size.
     *
     * @return
     */
    protected abstract fun getRequiredBaseOffset(): Float

    override fun getYChartMax(): Float {
        // TODO Auto-generated method stub
        return 0f
    }

    override fun getYChartMin(): Float {
        // TODO Auto-generated method stub
        return 0f
    }

    /**
     * ################ ################ ################ ################
     */
    /** CODE BELOW THIS RELATED TO ANIMATION */

    /**
     * ################ ################ ################ ################
     */
    /** CODE BELOW THIS RELATED TO ANIMATION  */
    /**
     * Applys a spin animation to the Chart.
     *
     * @param durationmillis
     * @param fromangle
     * @param toangle
     */
    @SuppressLint("NewApi")
    open fun spin(durationmillis: Int, fromangle: Float, toangle: Float, easing: EasingFunction?) {
        setRotationAngle(fromangle)
        val spinAnimator = ObjectAnimator.ofFloat(
            this, "rotationAngle", fromangle,
            toangle
        )
        spinAnimator.duration = durationmillis.toLong()
        spinAnimator.interpolator = easing
        spinAnimator.addUpdateListener { postInvalidate() }
        spinAnimator.start()
    }
}