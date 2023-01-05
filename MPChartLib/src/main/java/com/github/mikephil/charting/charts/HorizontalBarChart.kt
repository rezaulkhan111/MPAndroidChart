package com.github.mikephil.charting.charts

import android.content.Context
import android.graphics.RectF
import android.util.AttributeSet
import android.util.Log
import com.github.mikephil.charting.components.Legend.*
import com.github.mikephil.charting.components.XAxis.XAxisPosition
import com.github.mikephil.charting.components.YAxis.AxisDependency
import com.github.mikephil.charting.data.BarEntry
import com.github.mikephil.charting.data.Entry
import com.github.mikephil.charting.highlight.Highlight
import com.github.mikephil.charting.highlight.HorizontalBarHighlighter
import com.github.mikephil.charting.renderer.HorizontalBarChartRenderer
import com.github.mikephil.charting.renderer.XAxisRendererHorizontalBarChart
import com.github.mikephil.charting.renderer.YAxisRendererHorizontalBarChart
import com.github.mikephil.charting.utils.HorizontalViewPortHandler
import com.github.mikephil.charting.utils.MPPointF
import com.github.mikephil.charting.utils.TransformerHorizontalBarChart
import com.github.mikephil.charting.utils.Utils

/**
 * BarChart with horizontal bar orientation. In this implementation, x- and y-axis are switched, meaning the YAxis class
 * represents the horizontal values and the XAxis class represents the vertical values.
 *
 * @author Philipp Jahoda
 */
class HorizontalBarChart : BarChart {

    constructor(context: Context?) : super(context) {}
    constructor(context: Context?, attrs: AttributeSet?) : super(context, attrs) {}
    constructor(context: Context?, attrs: AttributeSet?, defStyle: Int) : super(
        context,
        attrs,
        defStyle
    ) {
    }

    override fun init() {
        mViewPortHandler = HorizontalViewPortHandler()
        super.init()
        mLeftAxisTransformer = TransformerHorizontalBarChart(mViewPortHandler)
        mRightAxisTransformer = TransformerHorizontalBarChart(mViewPortHandler)
        mRenderer = HorizontalBarChartRenderer(this, mAnimator!!, mViewPortHandler)
        setHighlighter(HorizontalBarHighlighter(this))
        mAxisRendererLeft = YAxisRendererHorizontalBarChart(
            mViewPortHandler,
            mAxisLeft!!, mLeftAxisTransformer
        )
        mAxisRendererRight = YAxisRendererHorizontalBarChart(
            mViewPortHandler,
            mAxisRight!!, mRightAxisTransformer
        )
        mXAxisRenderer = XAxisRendererHorizontalBarChart(
            mViewPortHandler,
            mXAxis!!, mLeftAxisTransformer, this
        )
    }

    private val mOffsetsBuffer = RectF()

    override fun calculateLegendOffsets(offsets: RectF) {
        offsets.left = 0f
        offsets.right = 0f
        offsets.top = 0f
        offsets.bottom = 0f
        if (mLegend == null || !mLegend!!.isEnabled() || mLegend!!.isDrawInsideEnabled()) return

        when (mLegend!!.getOrientation()) {
            LegendOrientation.VERTICAL -> when (mLegend!!.getHorizontalAlignment()) {
                LegendHorizontalAlignment.LEFT -> offsets.left += (Math.min(
                    mLegend!!.mNeededWidth,
                    mViewPortHandler.getChartWidth() * mLegend!!.getMaxSizePercent()
                )
                        + mLegend!!.getXOffset())
                LegendHorizontalAlignment.RIGHT -> offsets.right += (Math.min(
                    mLegend!!.mNeededWidth,
                    mViewPortHandler.getChartWidth() * mLegend!!.getMaxSizePercent()
                )
                        + mLegend!!.getXOffset())
                LegendHorizontalAlignment.CENTER -> when (mLegend!!.getVerticalAlignment()) {
                    LegendVerticalAlignment.TOP -> offsets.top += (Math.min(
                        mLegend!!.mNeededHeight,
                        mViewPortHandler.getChartHeight() * mLegend!!.getMaxSizePercent()
                    )
                            + mLegend!!.getYOffset())
                    LegendVerticalAlignment.BOTTOM -> offsets.bottom += (Math.min(
                        mLegend!!.mNeededHeight,
                        mViewPortHandler.getChartHeight() * mLegend!!.getMaxSizePercent()
                    )
                            + mLegend!!.getYOffset())
                    else -> {}
                }
                else -> {}
            }
            LegendOrientation.HORIZONTAL -> when (mLegend!!.getVerticalAlignment()) {
                LegendVerticalAlignment.TOP -> {
                    offsets.top += (Math.min(
                        mLegend!!.mNeededHeight,
                        mViewPortHandler.getChartHeight() * mLegend!!.getMaxSizePercent()
                    )
                            + mLegend!!.getYOffset())
                    if (mAxisLeft!!.isEnabled() && mAxisLeft!!.isDrawLabelsEnabled()) offsets.top += mAxisLeft!!.getRequiredHeightSpace(
                        mAxisRendererLeft!!.getPaintAxisLabels()!!
                    )
                }
                LegendVerticalAlignment.BOTTOM -> {
                    offsets.bottom += (Math.min(
                        mLegend!!.mNeededHeight,
                        mViewPortHandler.getChartHeight() * mLegend!!.getMaxSizePercent()
                    )
                            + mLegend!!.getYOffset())
                    if (mAxisRight!!.isEnabled() && mAxisRight!!.isDrawLabelsEnabled()) offsets.bottom += mAxisRight!!.getRequiredHeightSpace(
                        mAxisRendererRight!!.getPaintAxisLabels()!!
                    )
                }
                else -> {}
            }
            else -> {}
        }
    }

    override fun calculateOffsets() {
        var offsetLeft = 0f
        var offsetRight = 0f
        var offsetTop = 0f
        var offsetBottom = 0f
        calculateLegendOffsets(mOffsetsBuffer)
        offsetLeft += mOffsetsBuffer.left
        offsetTop += mOffsetsBuffer.top
        offsetRight += mOffsetsBuffer.right
        offsetBottom += mOffsetsBuffer.bottom

        // offsets for y-labels
        if (mAxisLeft!!.needsOffset()) {
            offsetTop += mAxisLeft!!.getRequiredHeightSpace(mAxisRendererLeft!!.getPaintAxisLabels()!!)
        }
        if (mAxisRight!!.needsOffset()) {
            offsetBottom += mAxisRight!!.getRequiredHeightSpace(mAxisRendererRight!!.getPaintAxisLabels()!!)
        }
        val xlabelwidth = mXAxis!!.mLabelRotatedWidth.toFloat()
        if (mXAxis!!.isEnabled()) {

            // offsets for x-labels
            if (mXAxis!!.getPosition() === XAxisPosition.BOTTOM) {
                offsetLeft += xlabelwidth
            } else if (mXAxis!!.getPosition() === XAxisPosition.TOP) {
                offsetRight += xlabelwidth
            } else if (mXAxis!!.getPosition() === XAxisPosition.BOTH_SIDED) {
                offsetLeft += xlabelwidth
                offsetRight += xlabelwidth
            }
        }
        offsetTop += getExtraTopOffset()
        offsetRight += getExtraRightOffset()
        offsetBottom += getExtraBottomOffset()
        offsetLeft += getExtraLeftOffset()
        val minOffset = Utils.convertDpToPixel(mMinOffset)
        mViewPortHandler.restrainViewPort(
            Math.max(minOffset, offsetLeft),
            Math.max(minOffset, offsetTop),
            Math.max(minOffset, offsetRight),
            Math.max(minOffset, offsetBottom)
        )
        if (mLogEnabled) {
            Log.i(
                LOG_TAG,
                "offsetLeft: " + offsetLeft + ", offsetTop: " + offsetTop + ", offsetRight: " +
                        offsetRight + ", offsetBottom: "
                        + offsetBottom
            )
            Log.i(LOG_TAG, "Content: " + mViewPortHandler.getContentRect().toString())
        }
        prepareOffsetMatrix()
        prepareValuePxMatrix()
    }

    override fun prepareValuePxMatrix() {
        mRightAxisTransformer!!.prepareMatrixValuePx(
            mAxisRight!!.mAxisMinimum, mAxisRight!!.mAxisRange, mXAxis!!.mAxisRange,
            mXAxis!!.mAxisMinimum
        )
        mLeftAxisTransformer!!.prepareMatrixValuePx(
            mAxisLeft!!.mAxisMinimum, mAxisLeft!!.mAxisRange, mXAxis!!.mAxisRange,
            mXAxis!!.mAxisMinimum
        )
    }

    override fun getMarkerPosition(high: Highlight?): FloatArray {
        return floatArrayOf(high!!.getDrawY(), high.getDrawX())
    }

    override fun getBarBounds(e: BarEntry, outputRect: RectF) {
        val set = mData!!.getDataSetForEntry(e)
        if (set == null) {
            outputRect[Float.MIN_VALUE, Float.MIN_VALUE, Float.MIN_VALUE] =
                Float.MIN_VALUE
            return
        }
        val y = e.getY()
        val x = e.getX()
        val barWidth = mData!!.getBarWidth()
        val top = x - barWidth / 2f
        val bottom = x + barWidth / 2f
        val left: Float = if (y >= 0) y else 0f
        val right: Float = if (y <= 0) y else 0f
        outputRect[left, top, right] = bottom
        getTransformer(set.getAxisDependency())!!.rectValueToPixel(outputRect)
    }

//    override var mGetPositionBuffer = FloatArray(2)

    /**
     * Returns a recyclable MPPointF instance.
     *
     * @param e
     * @param axis
     * @return
     */
    override fun getPosition(e: Entry?, axis: AxisDependency): MPPointF? {
        val mGetPositionBuffer = FloatArray(2)
        if (e == null) return null
        val vals = mGetPositionBuffer
        vals[0] = e.getY()
        vals[1] = e.getX()
        getTransformer(axis).pointValuesToPixel(vals)
        return MPPointF.getInstance(vals[0], vals[1])
    }

    /**
     * Returns the Highlight object (contains x-index and DataSet index) of the selected value at the given touch point
     * inside the BarChart.
     *
     * @param x
     * @param y
     * @return
     */
    override fun getHighlightByTouchPoint(x: Float, y: Float): Highlight? {
        return if (mData == null) {
            if (mLogEnabled) Log.e(LOG_TAG, "Can't select by touch. No data set.")
            null
        } else getHighlighter()!!.getHighlight(y, x) // switch x and y
    }

    override fun getLowestVisibleX(): Float {
        getTransformer(AxisDependency.LEFT).getValuesByTouchPoint(
            mViewPortHandler.contentLeft(),
            mViewPortHandler.contentBottom(), posForGetLowestVisibleX
        )
        return Math.max(mXAxis!!.mAxisMinimum, posForGetLowestVisibleX.y.toFloat())
    }

    override fun getHighestVisibleX(): Float {
        getTransformer(AxisDependency.LEFT).getValuesByTouchPoint(
            mViewPortHandler.contentLeft(),
            mViewPortHandler.contentTop(), posForGetHighestVisibleX
        )
        return Math.min(mXAxis!!.mAxisMaximum, posForGetHighestVisibleX.y.toFloat())
    }

    /**
     * ###### VIEWPORT METHODS BELOW THIS ######
     */
    override fun setVisibleXRangeMaximum(maxXRange: Float) {
        val xScale = mXAxis!!.mAxisRange / maxXRange
        mViewPortHandler.setMinimumScaleY(xScale)
    }

    override fun setVisibleXRangeMinimum(minXRange: Float) {
        val xScale = mXAxis!!.mAxisRange / minXRange
        mViewPortHandler.setMaximumScaleY(xScale)
    }

    override fun setVisibleXRange(minXRange: Float, maxXRange: Float) {
        val minScale = mXAxis!!.mAxisRange / minXRange
        val maxScale = mXAxis!!.mAxisRange / maxXRange
        mViewPortHandler.setMinMaxScaleY(minScale, maxScale)
    }

    override fun setVisibleYRangeMaximum(maxYRange: Float, axis: AxisDependency) {
        val yScale = getAxisRange(axis) / maxYRange
        mViewPortHandler.setMinimumScaleX(yScale)
    }

    override fun setVisibleYRangeMinimum(minYRange: Float, axis: AxisDependency) {
        val yScale = getAxisRange(axis) / minYRange
        mViewPortHandler.setMaximumScaleX(yScale)
    }

    override fun setVisibleYRange(minYRange: Float, maxYRange: Float, axis: AxisDependency) {
        val minScale = getAxisRange(axis) / minYRange
        val maxScale = getAxisRange(axis) / maxYRange
        mViewPortHandler.setMinMaxScaleX(minScale, maxScale)
    }
}