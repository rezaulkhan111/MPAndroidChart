package com.github.mikephil.charting.charts

import android.content.Context
import android.graphics.RectF
import android.util.AttributeSet
import android.util.Log
import com.github.mikephil.charting.components.AxisBase.calculate
import com.github.mikephil.charting.components.YAxis
import com.github.mikephil.charting.components.YAxis.calculate
import com.github.mikephil.charting.data.BarData
import com.github.mikephil.charting.data.BarEntry
import com.github.mikephil.charting.highlight.*
import com.github.mikephil.charting.interfaces.dataprovider.BarDataProvider
import com.github.mikephil.charting.renderer.BarChartRenderer

/**
 * Chart that draws bars.
 *
 * @author Philipp Jahoda
 */
open class BarChart : BarLineChartBase<BarData?>, BarDataProvider {

    /**
     * flag that indicates whether the highlight should be full-bar oriented, or single-value?
     */
    protected var mHighlightFullBarEnabled = false

    /**
     * if set to true, all values are drawn above their bars, instead of below their top
     */
    private var mDrawValueAboveBar = true

    /**
     * if set to true, a grey area is drawn behind each bar that indicates the maximum value
     */
    private var mDrawBarShadow = false

    private var mFitBars = false

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

    protected override fun init() {
        super.init()
        mRenderer = BarChartRenderer(this, mAnimator, mViewPortHandler)
        highlighter = BarHighlighter(this)
        xAxis!!.setSpaceMin(0.5f)
        xAxis!!.setSpaceMax(0.5f)
    }

    protected override fun calcMinMax() {
        if (mFitBars) {
            mXAxis.calculate(
                mData!!.xMin - mData!!.getBarWidth() / 2f,
                mData!!.xMax + mData!!.getBarWidth() / 2f
            )
        } else {
            mXAxis.calculate(mData!!.xMin, mData!!.xMax)
        }

        // calculate axis range (min / max) according to provided data
        mAxisLeft.calculate(
            mData!!.getYMin(YAxis.AxisDependency.LEFT),
            mData!!.getYMax(YAxis.AxisDependency.LEFT)
        )
        mAxisRight.calculate(
            mData!!.getYMin(YAxis.AxisDependency.RIGHT),
            mData!!.getYMax(YAxis.AxisDependency.RIGHT)
        )
    }

    /**
     * Returns the Highlight object (contains x-index and DataSet index) of the selected value at the given touch
     * point
     * inside the BarChart.
     *
     * @param x
     * @param y
     * @return
     */
    override fun getHighlightByTouchPoint(x: Float, y: Float): Highlight? {
        return if (mData == null) {
            Log.e(LOG_TAG, "Can't select by touch. No data set.")
            null
        } else {
            val h = highlighter.getHighlight(x, y)
            if (h == null || !isHighlightFullBarEnabled()) h else Highlight(
                h.x, h.y,
                h.xPx, h.yPx,
                h.dataSetIndex, -1, h.axis
            )

            // For isHighlightFullBarEnabled, remove stackIndex
        }
    }

    /**
     * Returns the bounding box of the specified Entry in the specified DataSet. Returns null if the Entry could not be
     * found in the charts data.  Performance-intensive code should use void getBarBounds(BarEntry, RectF) instead.
     *
     * @param e
     * @return
     */
    open fun getBarBounds(e: BarEntry): RectF? {
        val bounds = RectF()
        getBarBounds(e, bounds)
        return bounds
    }

    /**
     * The passed outputRect will be assigned the values of the bounding box of the specified Entry in the specified DataSet.
     * The rect will be assigned Float.MIN_VALUE in all locations if the Entry could not be found in the charts data.
     *
     * @param e
     * @return
     */
    open fun getBarBounds(e: BarEntry, outputRect: RectF) {
        val set = mData!!.getDataSetForEntry(e)
        if (set == null) {
            outputRect[Float.MIN_VALUE, Float.MIN_VALUE, Float.MIN_VALUE] =
                Float.MIN_VALUE
            return
        }
        val y = e.getY()
        val x = e.getX()
        val barWidth = mData!!.getBarWidth()
        val left = x - barWidth / 2f
        val right = x + barWidth / 2f
        val top: Float = if (y >= 0) y else 0f
        val bottom: Float = if (y <= 0) y else 0f
        outputRect[left, top, right] = bottom
        getTransformer(set.getAxisDependency())!!.rectValueToPixel(outputRect)
    }

    /**
     * If set to true, all values are drawn above their bars, instead of below their top.
     *
     * @param enabled
     */
    open fun setDrawValueAboveBar(enabled: Boolean) {
        mDrawValueAboveBar = enabled
    }

    /**
     * returns true if drawing values above bars is enabled, false if not
     *
     * @return
     */
    override fun isDrawValueAboveBarEnabled(): Boolean {
        return mDrawValueAboveBar
    }

    /**
     * If set to true, a grey area is drawn behind each bar that indicates the maximum value. Enabling his will reduce
     * performance by about 50%.
     *
     * @param enabled
     */
    open fun setDrawBarShadow(enabled: Boolean) {
        mDrawBarShadow = enabled
    }

    /**
     * returns true if drawing shadows (maxvalue) for each bar is enabled, false if not
     *
     * @return
     */
    override fun isDrawBarShadowEnabled(): Boolean {
        return mDrawBarShadow
    }

    /**
     * Set this to true to make the highlight operation full-bar oriented, false to make it highlight single values (relevant
     * only for stacked). If enabled, highlighting operations will highlight the whole bar, even if only a single stack entry
     * was tapped.
     * Default: false
     *
     * @param enabled
     */
    open fun setHighlightFullBarEnabled(enabled: Boolean) {
        mHighlightFullBarEnabled = enabled
    }

    /**
     * @return true the highlight operation is be full-bar oriented, false if single-value
     */
    override fun isHighlightFullBarEnabled(): Boolean {
        return mHighlightFullBarEnabled
    }

    /**
     * Highlights the value at the given x-value in the given DataSet. Provide
     * -1 as the dataSetIndex to undo all highlighting.
     *
     * @param x
     * @param dataSetIndex
     * @param stackIndex   the index inside the stack - only relevant for stacked entries
     */
    override fun highlightValue(x: Float, dataSetIndex: Int, stackIndex: Int) {
        highlightValue(Highlight(x, dataSetIndex, stackIndex), false)
    }

    override fun getBarData(): BarData? {
        return mData
    }

    /**
     * Adds half of the bar width to each side of the x-axis range in order to allow the bars of the barchart to be
     * fully displayed.
     * Default: false
     *
     * @param enabled
     */
    open fun setFitBars(enabled: Boolean) {
        mFitBars = enabled
    }

    /**
     * Groups all BarDataSet objects this data object holds together by modifying the x-value of their entries.
     * Previously set x-values of entries will be overwritten. Leaves space between bars and groups as specified
     * by the parameters.
     * Calls notifyDataSetChanged() afterwards.
     *
     * @param fromX      the starting point on the x-axis where the grouping should begin
     * @param groupSpace the space between groups of bars in values (not pixels) e.g. 0.8f for bar width 1f
     * @param barSpace   the space between individual bars in values (not pixels) e.g. 0.1f for bar width 1f
     */
    open fun groupBars(fromX: Float, groupSpace: Float, barSpace: Float) {
        if (getBarData() == null) {
            throw RuntimeException("You need to set data for the chart before grouping bars.")
        } else {
            getBarData()!!.groupBars(fromX, groupSpace, barSpace)
            notifyDataSetChanged()
        }
    }
}