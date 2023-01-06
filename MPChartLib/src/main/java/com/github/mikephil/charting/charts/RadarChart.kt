package com.github.mikephil.charting.charts

import android.content.Context
import android.graphics.Canvas
import android.graphics.Color
import android.util.AttributeSet
import com.github.mikephil.charting.components.YAxis
import com.github.mikephil.charting.components.YAxis.AxisDependency
import com.github.mikephil.charting.data.RadarData
import com.github.mikephil.charting.highlight.RadarHighlighter
import com.github.mikephil.charting.renderer.RadarChartRenderer
import com.github.mikephil.charting.renderer.XAxisRendererRadarChart
import com.github.mikephil.charting.renderer.YAxisRendererRadarChart
import com.github.mikephil.charting.utils.Utils.convertDpToPixel
import com.github.mikephil.charting.utils.Utils.getNormalizedAngle

/**
 * Implementation of the RadarChart, a "spidernet"-like chart. It works best
 * when displaying 5-10 entries per DataSet.
 *
 * @author Philipp Jahoda
 */
class RadarChart : PieRadarChartBase<RadarData?> {
    /**
     * width of the main web lines
     */
    private var mWebLineWidth = 2.5f

    /**
     * width of the inner web lines
     */
    private var mInnerWebLineWidth = 1.5f

    /**
     * color for the main web lines
     */
    private var mWebColor = Color.rgb(122, 122, 122)

    /**
     * color for the inner web
     */
    private var mWebColorInner = Color.rgb(122, 122, 122)

    /**
     * transparency the grid is drawn with (0-255)
     */
    private var mWebAlpha = 150

    /**
     * flag indicating if the web lines should be drawn or not
     */
    private var mDrawWeb = true

    /**
     * modulus that determines how many labels and web-lines are skipped before the next is drawn
     */
    private var mSkipWebLineCount = 0

    /**
     * the object reprsenting the y-axis labels
     */
    private var mYAxis: YAxis? = null

    protected var mYAxisRenderer: YAxisRendererRadarChart? = null
    protected var mXAxisRenderer: XAxisRendererRadarChart? = null

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
        mYAxis = YAxis(AxisDependency.LEFT)
        mYAxis!!.setLabelXOffset(10f)
        mWebLineWidth = convertDpToPixel(1.5f)
        mInnerWebLineWidth = convertDpToPixel(0.75f)
        mRenderer = RadarChartRenderer(this, mAnimator!!, mViewPortHandler)
        mYAxisRenderer = YAxisRendererRadarChart(mViewPortHandler, mYAxis!!, this)
        mXAxisRenderer = XAxisRendererRadarChart(mViewPortHandler, mXAxis!!, this)
        mHighlighter = RadarHighlighter(this)
    }

    override fun calcMinMax() {
        super.calcMinMax()
        mYAxis!!.calculate(
            mData!!.getYMin(AxisDependency.LEFT),
            mData!!.getYMax(AxisDependency.LEFT)
        )
        mXAxis!!.calculate(0f, mData!!.getMaxEntryCountSet()!!.getEntryCount().toFloat())
    }

    override fun notifyDataSetChanged() {
        if (mData == null) return
        calcMinMax()
        mYAxisRenderer!!.computeAxis(
            mYAxis!!.mAxisMinimum,
            mYAxis!!.mAxisMaximum,
            mYAxis!!.isInverted()
        )
        mXAxisRenderer!!.computeAxis(mXAxis!!.mAxisMinimum, mXAxis!!.mAxisMaximum, false)
        if (mLegend != null && !mLegend!!.isLegendCustom()) mLegendRenderer!!.computeLegend(mData!!)
        calculateOffsets()
    }

    override fun onDraw(canvas: Canvas) {
        super.onDraw(canvas!!)
        if (mData == null) return

//        if (mYAxis.isEnabled())
//            mYAxisRenderer.computeAxis(mYAxis.mAxisMinimum, mYAxis.mAxisMaximum, mYAxis.isInverted());
        if (mXAxis!!.isEnabled()) mXAxisRenderer!!.computeAxis(
            mXAxis!!.mAxisMinimum,
            mXAxis!!.mAxisMaximum,
            false
        )
        mXAxisRenderer!!.renderAxisLabels(canvas)
        if (mDrawWeb) mRenderer!!.drawExtras(canvas)
        if (mYAxis!!.isEnabled() && mYAxis!!.isDrawLimitLinesBehindDataEnabled()) mYAxisRenderer!!.renderLimitLines(
            canvas
        )
        mRenderer!!.drawData(canvas)
        if (valuesToHighlight()) mRenderer!!.drawHighlighted(canvas, mIndicesToHighlight)
        if (mYAxis!!.isEnabled() && !mYAxis!!.isDrawLimitLinesBehindDataEnabled()) mYAxisRenderer!!.renderLimitLines(
            canvas
        )
        mYAxisRenderer!!.renderAxisLabels(canvas)
        mRenderer!!.drawValues(canvas)
        mLegendRenderer!!.renderLegend(canvas)
        drawDescription(canvas)
        drawMarkers(canvas)
    }

    /**
     * Returns the factor that is needed to transform values into pixels.
     *
     * @return
     */
    fun getFactor(): Float {
        val content = mViewPortHandler.getContentRect()
        return Math.min(content!!.width() / 2f, content.height() / 2f) / mYAxis!!.mAxisRange
    }

    /**
     * Returns the angle that each slice in the radar chart occupies.
     *
     * @return
     */
    fun getSliceAngle(): Float {
        return 360f / mData!!.getMaxEntryCountSet()!!.getEntryCount().toFloat()
    }

    override fun getIndexForAngle(angle: Float): Int {

        // take the current angle of the chart into consideration
        val a = getNormalizedAngle(angle - getRotationAngle())
        val sliceangle = getSliceAngle()
        val max: Int = mData!!.getMaxEntryCountSet()!!.getEntryCount()
        var index = 0
        for (i in 0 until max) {
            val referenceAngle = sliceangle * (i + 1) - sliceangle / 2f
            if (referenceAngle > a) {
                index = i
                break
            }
        }
        return index
    }

    /**
     * Returns the object that represents all y-labels of the RadarChart.
     *
     * @return
     */
    fun getYAxis(): YAxis? {
        return mYAxis
    }

    /**
     * Sets the width of the web lines that come from the center.
     *
     * @param width
     */
    fun setWebLineWidth(width: Float) {
        mWebLineWidth = convertDpToPixel(width)
    }

    fun getWebLineWidth(): Float {
        return mWebLineWidth
    }

    /**
     * Sets the width of the web lines that are in between the lines coming from
     * the center.
     *
     * @param width
     */
    fun setWebLineWidthInner(width: Float) {
        mInnerWebLineWidth = convertDpToPixel(width)
    }

    fun getWebLineWidthInner(): Float {
        return mInnerWebLineWidth
    }

    /**
     * Sets the transparency (alpha) value for all web lines, default: 150, 255
     * = 100% opaque, 0 = 100% transparent
     *
     * @param alpha
     */
    fun setWebAlpha(alpha: Int) {
        mWebAlpha = alpha
    }

    /**
     * Returns the alpha value for all web lines.
     *
     * @return
     */
    fun getWebAlpha(): Int {
        return mWebAlpha
    }

    /**
     * Sets the color for the web lines that come from the center. Don't forget
     * to use getResources().getColor(...) when loading a color from the
     * resources. Default: Color.rgb(122, 122, 122)
     *
     * @param color
     */
    fun setWebColor(color: Int) {
        mWebColor = color
    }

    fun getWebColor(): Int {
        return mWebColor
    }

    /**
     * Sets the color for the web lines in between the lines that come from the
     * center. Don't forget to use getResources().getColor(...) when loading a
     * color from the resources. Default: Color.rgb(122, 122, 122)
     *
     * @param color
     */
    fun setWebColorInner(color: Int) {
        mWebColorInner = color
    }

    fun getWebColorInner(): Int {
        return mWebColorInner
    }

    /**
     * If set to true, drawing the web is enabled, if set to false, drawing the
     * whole web is disabled. Default: true
     *
     * @param enabled
     */
    fun setDrawWeb(enabled: Boolean) {
        mDrawWeb = enabled
    }

    /**
     * Sets the number of web-lines that should be skipped on chart web before the
     * next one is drawn. This targets the lines that come from the center of the RadarChart.
     *
     * @param count if count = 1 -> 1 line is skipped in between
     */
    fun setSkipWebLineCount(count: Int) {
        mSkipWebLineCount = Math.max(0, count)
    }

    /**
     * Returns the modulus that is used for skipping web-lines.
     *
     * @return
     */
    fun getSkipWebLineCount(): Int {
        return mSkipWebLineCount
    }

    override fun getRequiredLegendOffset(): Float {
        return mLegendRenderer!!.getLabelPaint()!!.textSize * 4f
    }

    override fun getRequiredBaseOffset(): Float {
        return if (mXAxis!!.isEnabled() && mXAxis!!.isDrawLabelsEnabled()) mXAxis!!.mLabelRotatedWidth.toFloat() else convertDpToPixel(
            10f
        )
    }

    override fun getRadius(): Float {
        val content = mViewPortHandler.getContentRect()
        return Math.min(content!!.width() / 2f, content.height() / 2f)
    }

    /**
     * Returns the maximum value this chart can display on it's y-axis.
     */
    override fun getYChartMax(): Float {
        return mYAxis!!.mAxisMaximum
    }

    /**
     * Returns the minimum value this chart can display on it's y-axis.
     */
    override fun getYChartMin(): Float {
        return mYAxis!!.mAxisMinimum
    }

    /**
     * Returns the range of y-values this chart can display.
     *
     * @return
     */
    fun getYRange(): Float {
        return mYAxis!!.mAxisRange
    }
}