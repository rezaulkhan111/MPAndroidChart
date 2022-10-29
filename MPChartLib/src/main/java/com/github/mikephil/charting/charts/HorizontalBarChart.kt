package com.github.mikephil.charting.charts

import android.content.Context
import android.graphics.RectF
import android.util.AttributeSet
import android.util.Log
import com.github.mikephil.charting.components.YAxis
import com.github.mikephil.charting.data.BarEntry
import com.github.mikephil.charting.data.Entry
import com.github.mikephil.charting.highlight.Highlight
import com.github.mikephil.charting.interfaces.datasets.IBarDataSet
import com.github.mikephil.charting.renderer.HorizontalBarChartRenderer
import com.github.mikephil.charting.utils.MPPointF
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
        mRenderer = HorizontalBarChartRenderer(this, mAnimator, mViewPortHandler)
        setHighlighter(HorizontalBarHighlighter(this))
        mAxisRendererLeft =
            YAxisRendererHorizontalBarChart(mViewPortHandler, mAxisLeft, mLeftAxisTransformer)
        mAxisRendererRight =
            YAxisRendererHorizontalBarChart(mViewPortHandler, mAxisRight, mRightAxisTransformer)
        mXAxisRenderer =
            XAxisRendererHorizontalBarChart(mViewPortHandler, mXAxis, mLeftAxisTransformer, this)
    }

    private val mOffsetsBuffer: RectF = RectF()
    override fun calculateLegendOffsets(offsets: RectF) {
        offsets.left = 0f
        offsets.right = 0f
        offsets.top = 0f
        offsets.bottom = 0f
        if (mLegend == null || !mLegend.isEnabled || mLegend.isDrawInsideEnabled) return
        when (mLegend.orientation) {
            LegendOrientation.VERTICAL -> when (mLegend.horizontalAlignment) {
                LegendHorizontalAlignment.LEFT -> offsets.left += (Math.min(
                    mLegend.mNeededWidth,
                    mViewPortHandler.chartWidth * mLegend.maxSizePercent
                )
                        + mLegend.xOffset)
                LegendHorizontalAlignment.RIGHT -> offsets.right += (Math.min(
                    mLegend.mNeededWidth,
                    mViewPortHandler.chartWidth * mLegend.maxSizePercent
                )
                        + mLegend.xOffset)
                LegendHorizontalAlignment.CENTER -> when (mLegend.verticalAlignment) {
                    LegendVerticalAlignment.TOP -> offsets.top += (Math.min(
                        mLegend.mNeededHeight,
                        mViewPortHandler.chartHeight * mLegend.maxSizePercent
                    )
                            + mLegend.yOffset)
                    LegendVerticalAlignment.BOTTOM -> offsets.bottom += (Math.min(
                        mLegend.mNeededHeight,
                        mViewPortHandler.chartHeight * mLegend.maxSizePercent
                    )
                            + mLegend.yOffset)
                    else -> {}
                }
            }
            LegendOrientation.HORIZONTAL -> when (mLegend.verticalAlignment) {
                LegendVerticalAlignment.TOP -> {
                    offsets.top += (Math.min(
                        mLegend.mNeededHeight,
                        mViewPortHandler.chartHeight * mLegend.maxSizePercent
                    )
                            + mLegend.yOffset)
                    if (mAxisLeft.isEnabled && mAxisLeft.isDrawLabelsEnabled) offsets.top += mAxisLeft.getRequiredHeightSpace(
                        mAxisRendererLeft.paintAxisLabels
                    )
                }
                LegendVerticalAlignment.BOTTOM -> {
                    offsets.bottom += (Math.min(
                        mLegend.mNeededHeight,
                        mViewPortHandler.chartHeight * mLegend.maxSizePercent
                    )
                            + mLegend.yOffset)
                    if (mAxisRight.isEnabled && mAxisRight.isDrawLabelsEnabled) offsets.bottom += mAxisRight.getRequiredHeightSpace(
                        mAxisRendererRight.paintAxisLabels
                    )
                }
                else -> {}
            }
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
        if (mAxisLeft.needsOffset()) {
            offsetTop += mAxisLeft.getRequiredHeightSpace(mAxisRendererLeft.paintAxisLabels)
        }
        if (mAxisRight.needsOffset()) {
            offsetBottom += mAxisRight.getRequiredHeightSpace(mAxisRendererRight.paintAxisLabels)
        }
        val xlabelwidth: Float = mXAxis.mLabelRotatedWidth
        if (mXAxis.isEnabled) {

            // offsets for x-labels
            if (mXAxis.position === XAxisPosition.BOTTOM) {
                offsetLeft += xlabelwidth
            } else if (mXAxis.position === XAxisPosition.TOP) {
                offsetRight += xlabelwidth
            } else if (mXAxis.position === XAxisPosition.BOTH_SIDED) {
                offsetLeft += xlabelwidth
                offsetRight += xlabelwidth
            }
        }
        offsetTop += extraTopOffset
        offsetRight += extraRightOffset
        offsetBottom += extraBottomOffset
        offsetLeft += extraLeftOffset
        val minOffset = Utils.convertDpToPixel(mMinOffset)
        mViewPortHandler.restrainViewPort(
            Math.max(minOffset, offsetLeft),
            Math.max(minOffset, offsetTop),
            Math.max(minOffset, offsetRight),
            Math.max(minOffset, offsetBottom)
        )
        if (mLogEnabled) {
            Log.i(
                Chart.Companion.LOG_TAG,
                "offsetLeft: " + offsetLeft + ", offsetTop: " + offsetTop + ", offsetRight: " +
                        offsetRight + ", offsetBottom: "
                        + offsetBottom
            )
            Log.i(Chart.Companion.LOG_TAG, "Content: " + mViewPortHandler.contentRect.toString())
        }
        prepareOffsetMatrix()
        prepareValuePxMatrix()
    }

    override fun prepareValuePxMatrix() {
        mRightAxisTransformer.prepareMatrixValuePx(
            mAxisRight.mAxisMinimum, mAxisRight.mAxisRange, mXAxis.mAxisRange,
            mXAxis.mAxisMinimum
        )
        mLeftAxisTransformer.prepareMatrixValuePx(
            mAxisLeft.mAxisMinimum, mAxisLeft.mAxisRange, mXAxis.mAxisRange,
            mXAxis.mAxisMinimum
        )
    }

    override fun getMarkerPosition(high: Highlight?): FloatArray {
        return floatArrayOf(high!!.drawY, high.drawX)
    }

    override fun getBarBounds(e: BarEntry, outputRect: RectF) {
        val bounds: RectF = outputRect
        val set: IBarDataSet? = mData!!.getDataSetForEntry(e)
        if (set == null) {
            outputRect.set(Float.MIN_VALUE, Float.MIN_VALUE, Float.MIN_VALUE, Float.MIN_VALUE)
            return
        }
        val y: Float = e.y
        val x: Float = e.x
        val barWidth = mData!!.barWidth
        val top = x - barWidth / 2f
        val bottom = x + barWidth / 2f
        val left: Float = if (y >= 0) y else 0F
        val right: Float = if (y <= 0) y else 0F
        bounds.set(left, top, right, bottom)
        getTransformer(set.axisDependency)!!.rectValueToPixel(bounds)
    }

    override var mGetPositionBuffer = FloatArray(2)

    /**
     * Returns a recyclable MPPointF instance.
     *
     * @param e
     * @param axis
     * @return
     */
    override fun getPosition(e: Entry?, axis: YAxis.AxisDependency?): MPPointF? {
        if (e == null) return null
        val vals = mGetPositionBuffer
        vals[0] = e.y
        vals[1] = e.x
        getTransformer(axis)!!.pointValuesToPixel(vals)
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
            if (mLogEnabled) Log.e(
                Chart.Companion.LOG_TAG,
                "Can't select by touch. No data set."
            )
            null
        } else highlighter.getHighlight(y, x) // switch x and y
    }

    override val lowestVisibleX: Float
        get() {
            getTransformer(YAxis.AxisDependency.LEFT)!!.getValuesByTouchPoint(
                mViewPortHandler.contentLeft(),
                mViewPortHandler.contentBottom(), posForGetLowestVisibleX
            )
            return Math.max(mXAxis.mAxisMinimum, posForGetLowestVisibleX.y.toFloat())
        }
    override val highestVisibleX: Float
        get() {
            getTransformer(YAxis.AxisDependency.LEFT)!!.getValuesByTouchPoint(
                mViewPortHandler.contentLeft(),
                mViewPortHandler.contentTop(), posForGetHighestVisibleX
            )
            return Math.min(mXAxis.mAxisMaximum, posForGetHighestVisibleX.y.toFloat())
        }

    /**
     * ###### VIEWPORT METHODS BELOW THIS ######
     */
    override fun setVisibleXRangeMaximum(maxXRange: Float) {
        val xScale = mXAxis.mAxisRange / maxXRange
        mViewPortHandler.setMinimumScaleY(xScale)
    }

    override fun setVisibleXRangeMinimum(minXRange: Float) {
        val xScale = mXAxis.mAxisRange / minXRange
        mViewPortHandler.setMaximumScaleY(xScale)
    }

    override fun setVisibleXRange(minXRange: Float, maxXRange: Float) {
        val minScale = mXAxis.mAxisRange / minXRange
        val maxScale = mXAxis.mAxisRange / maxXRange
        mViewPortHandler.setMinMaxScaleY(minScale, maxScale)
    }

    override fun setVisibleYRangeMaximum(maxYRange: Float, axis: YAxis.AxisDependency) {
        val yScale = getAxisRange(axis) / maxYRange
        mViewPortHandler.setMinimumScaleX(yScale)
    }

    override fun setVisibleYRangeMinimum(minYRange: Float, axis: YAxis.AxisDependency) {
        val yScale = getAxisRange(axis) / minYRange
        mViewPortHandler.setMaximumScaleX(yScale)
    }

    override fun setVisibleYRange(minYRange: Float, maxYRange: Float, axis: YAxis.AxisDependency) {
        val minScale = getAxisRange(axis) / minYRange
        val maxScale = getAxisRange(axis) / maxYRange
        mViewPortHandler.setMinMaxScaleX(minScale, maxScale)
    }
}