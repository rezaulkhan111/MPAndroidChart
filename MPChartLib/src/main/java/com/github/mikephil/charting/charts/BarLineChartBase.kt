package com.github.mikephil.charting.charts

import android.annotation.SuppressLint
import android.annotation.TargetApi
import android.content.Context
import android.graphics.*
import android.util.AttributeSet
import android.util.Log
import android.view.MotionEvent
import com.github.mikephil.charting.components.AxisBase.calculate
import com.github.mikephil.charting.components.AxisBase.isDrawGridLinesBehindDataEnabled
import com.github.mikephil.charting.components.AxisBase.isDrawLabelsEnabled
import com.github.mikephil.charting.components.AxisBase.isDrawLimitLinesBehindDataEnabled
import com.github.mikephil.charting.components.ComponentBase.isEnabled
import com.github.mikephil.charting.components.ComponentBase.xOffset
import com.github.mikephil.charting.components.ComponentBase.yOffset
import com.github.mikephil.charting.components.Description.position
import com.github.mikephil.charting.components.Legend.*
import com.github.mikephil.charting.components.Legend.horizontalAlignment
import com.github.mikephil.charting.components.Legend.isDrawInsideEnabled
import com.github.mikephil.charting.components.Legend.maxSizePercent
import com.github.mikephil.charting.components.Legend.orientation
import com.github.mikephil.charting.components.Legend.verticalAlignment
import com.github.mikephil.charting.components.XAxis.XAxisPosition
import com.github.mikephil.charting.components.XAxis.position
import com.github.mikephil.charting.components.YAxis
import com.github.mikephil.charting.components.YAxis.AxisDependency
import com.github.mikephil.charting.components.YAxis.calculate
import com.github.mikephil.charting.data.*
import com.github.mikephil.charting.highlight.ChartHighlighter
import com.github.mikephil.charting.highlight.Highlight.toString
import com.github.mikephil.charting.interfaces.dataprovider.BarLineScatterCandleBubbleDataProvider
import com.github.mikephil.charting.interfaces.datasets.IBarLineScatterCandleBubbleDataSet
import com.github.mikephil.charting.jobs.AnimatedMoveViewJob.Companion.getInstance
import com.github.mikephil.charting.jobs.AnimatedZoomJob.Companion.getInstance
import com.github.mikephil.charting.jobs.MoveViewJob.Companion.getInstance
import com.github.mikephil.charting.jobs.ZoomJob.Companion.getInstance
import com.github.mikephil.charting.listener.*
import com.github.mikephil.charting.renderer.LegendRenderer.computeLegend
import com.github.mikephil.charting.renderer.LegendRenderer.renderLegend
import com.github.mikephil.charting.renderer.XAxisRenderer
import com.github.mikephil.charting.renderer.YAxisRenderer
import com.github.mikephil.charting.utils.*
import com.github.mikephil.charting.utils.MPPointD.Companion.getInstance
import com.github.mikephil.charting.utils.MPPointD.Companion.recycleInstance
import com.github.mikephil.charting.utils.MPPointF.Companion.recycleInstance
import com.github.mikephil.charting.utils.Utils.convertDpToPixel
import com.github.mikephil.charting.utils.ViewPortHandler.centerViewPort
import com.github.mikephil.charting.utils.ViewPortHandler.chartHeight
import com.github.mikephil.charting.utils.ViewPortHandler.chartWidth
import com.github.mikephil.charting.utils.ViewPortHandler.contentBottom
import com.github.mikephil.charting.utils.ViewPortHandler.contentLeft
import com.github.mikephil.charting.utils.ViewPortHandler.contentRect
import com.github.mikephil.charting.utils.ViewPortHandler.contentRight
import com.github.mikephil.charting.utils.ViewPortHandler.contentTop
import com.github.mikephil.charting.utils.ViewPortHandler.fitScreen
import com.github.mikephil.charting.utils.ViewPortHandler.hasNoDragOffset
import com.github.mikephil.charting.utils.ViewPortHandler.isFullyZoomedOut
import com.github.mikephil.charting.utils.ViewPortHandler.matrixTouch
import com.github.mikephil.charting.utils.ViewPortHandler.refresh
import com.github.mikephil.charting.utils.ViewPortHandler.resetZoom
import com.github.mikephil.charting.utils.ViewPortHandler.restrainViewPort
import com.github.mikephil.charting.utils.ViewPortHandler.scaleX
import com.github.mikephil.charting.utils.ViewPortHandler.scaleY
import com.github.mikephil.charting.utils.ViewPortHandler.setDragOffsetX
import com.github.mikephil.charting.utils.ViewPortHandler.setDragOffsetY
import com.github.mikephil.charting.utils.ViewPortHandler.setMaximumScaleX
import com.github.mikephil.charting.utils.ViewPortHandler.setMaximumScaleY
import com.github.mikephil.charting.utils.ViewPortHandler.setMinMaxScaleX
import com.github.mikephil.charting.utils.ViewPortHandler.setMinMaxScaleY
import com.github.mikephil.charting.utils.ViewPortHandler.setMinimumScaleX
import com.github.mikephil.charting.utils.ViewPortHandler.setMinimumScaleY
import com.github.mikephil.charting.utils.ViewPortHandler.zoom
import com.github.mikephil.charting.utils.ViewPortHandler.zoomIn
import com.github.mikephil.charting.utils.ViewPortHandler.zoomOut

/**
 * Base-class of LineChart, BarChart, ScatterChart and CandleStickChart.
 *
 * @author Philipp Jahoda
 */
@SuppressLint("RtlHardcoded")
abstract class BarLineChartBase<T : BarLineScatterCandleBubbleData<out IBarLineScatterCandleBubbleDataSet<out Entry?>?>?> :
    Chart<T>, BarLineScatterCandleBubbleDataProvider {
    /**
     * the maximum number of entries to which values will be drawn
     * (entry numbers greater than this value will cause value-labels to disappear)
     */
    override var maxVisibleCount = 100
        protected set
    /**
     * @return true if auto scaling on the y axis is enabled.
     * @default false
     */
    /**
     * Flag that indicates if auto scaling on the y axis is enabled. This is
     * especially interesting for charts displaying financial data.
     *
     * @param enabled the y axis automatically adjusts to the min and max y
     * values of the current x axis range whenever the viewport
     * changes
     */
    /**
     * flag that indicates if auto scaling on the y axis is enabled
     */
    var isAutoScaleMinMaxEnabled = false
    /**
     * returns true if pinch-zoom is enabled, false if not
     *
     * @return
     */
    /**
     * flag that indicates if pinch-zoom is enabled. if true, both x and y axis
     * can be scaled with 2 fingers, if false, x and y axis can be scaled
     * separately
     */
    var isPinchZoomEnabled = false
        protected set
    /**
     * Returns true if zooming via double-tap is enabled false if not.
     *
     * @return
     */
    /**
     * Set this to true to enable zooming in by double-tap on the chart.
     * Default: enabled
     *
     * @param enabled
     */
    /**
     * flag that indicates if double tap zoom is enabled or not
     */
    var isDoubleTapToZoomEnabled = true
    /**
     * Set this to true to allow highlighting per dragging over the chart
     * surface when it is fully zoomed out. Default: true
     *
     * @param enabled
     */
    /**
     * flag that indicates if highlighting per dragging over a fully zoomed out
     * chart is enabled
     */
    var isHighlightPerDragEnabled = true
    /**
     * Returns true if dragging on the X axis is enabled for the chart, false if not.
     *
     * @return
     */
    /**
     * Set this to true to enable dragging on the X axis
     *
     * @param enabled
     */
    /**
     * if true, dragging is enabled for the chart
     */
    var isDragXEnabled = true
    /**
     * Returns true if dragging on the Y axis is enabled for the chart, false if not.
     *
     * @return
     */
    /**
     * Set this to true to enable dragging on the Y axis
     *
     * @param enabled
     */
    var isDragYEnabled = true
    var isScaleXEnabled = true
    var isScaleYEnabled = true

    /**
     * paint object for the (by default) lightgrey background of the grid
     */
    protected var mGridBackgroundPaint: Paint? = null
    protected var mBorderPaint: Paint? = null

    /**
     * flag indicating if the grid background should be drawn or not
     */
    protected var mDrawGridBackground = false

    /**
     * When enabled, the borders rectangle will be rendered.
     * If this is enabled, there is no point drawing the axis-lines of x- and y-axis.
     *
     * @return
     */
    var isDrawBordersEnabled = false
        protected set

    /**
     * When enabled, the values will be clipped to contentRect,
     * otherwise they can bleed outside the content rect.
     *
     * @return
     */
    var isClipValuesToContentEnabled = false
        protected set

    /**
     * When disabled, the data and/or highlights will not be clipped to contentRect. Disabling this option can
     * be useful, when the data lies fully within the content rect, but is drawn in such a way (such as thick lines)
     * that there is unwanted clipping.
     *
     * @return
     */
    var isClipDataToContentEnabled = true
        protected set
    /**
     * Gets the minimum offset (padding) around the chart, defaults to 15.f
     */
    /**
     * Sets the minimum offset (padding) around the chart, defaults to 15.f
     */
    /**
     * Sets the minimum offset (padding) around the chart, defaults to 15
     */
    var minOffset = 15f
    /**
     * Returns true if keeping the position on rotation is enabled and false if not.
     */
    /**
     * Sets whether the chart should keep its position (zoom / scroll) after a rotation (orientation change)
     */
    /**
     * flag indicating if the chart should stay at the same position after a rotation. Default is false.
     */
    var isKeepPositionOnRotation = false
    /**
     * Gets the OnDrawListener. May be null.
     *
     * @return
     */
    /**
     * the listener for user drawing on the chart
     */
    var drawListener: OnDrawListener? = null
        protected set
    /**
     * Returns the left y-axis object. In the horizontal bar-chart, this is the
     * top axis.
     *
     * @return
     */
    /**
     * the object representing the labels on the left y-axis
     */
    var axisLeft: YAxis? = null
        protected set
    /**
     * Returns the right y-axis object. In the horizontal bar-chart, this is the
     * bottom axis.
     *
     * @return
     */
    /**
     * the object representing the labels on the right y-axis
     */
    var axisRight: YAxis? = null
        protected set

    /**
     * Sets a custom axis renderer for the left axis and overwrites the existing one.
     *
     * @param rendererLeftYAxis
     */
    var rendererLeftYAxis: YAxisRenderer? = null

    /**
     * Sets a custom axis renderer for the right acis and overwrites the existing one.
     *
     * @param rendererRightYAxis
     */
    var rendererRightYAxis: YAxisRenderer? = null
    protected var mLeftAxisTransformer: Transformer? = null
    protected var mRightAxisTransformer: Transformer? = null
    var rendererXAxis: XAxisRenderer? = null
        protected set

    // /** the approximator object used for data filtering */
    // private Approximator mApproximator;
    constructor(context: Context?, attrs: AttributeSet?, defStyle: Int) : super(
        context,
        attrs,
        defStyle
    ) {
    }

    constructor(context: Context?, attrs: AttributeSet?) : super(context, attrs) {}
    constructor(context: Context?) : super(context) {}

    override fun init() {
        super.init()
        axisLeft = YAxis(AxisDependency.LEFT)
        axisRight = YAxis(AxisDependency.RIGHT)
        mLeftAxisTransformer = Transformer(mViewPortHandler)
        mRightAxisTransformer = Transformer(mViewPortHandler)
        rendererLeftYAxis = YAxisRenderer(mViewPortHandler, axisLeft!!, mLeftAxisTransformer)
        rendererRightYAxis = YAxisRenderer(mViewPortHandler, axisRight!!, mRightAxisTransformer)
        rendererXAxis = XAxisRenderer(mViewPortHandler, mXAxis, mLeftAxisTransformer)
        setHighlighter(ChartHighlighter<Any?>(this))
        mChartTouchListener = BarLineChartTouchListener(this, mViewPortHandler.matrixTouch, 3f)
        mGridBackgroundPaint = Paint()
        mGridBackgroundPaint!!.style = Paint.Style.FILL
        // mGridBackgroundPaint.setColor(Color.WHITE);
        mGridBackgroundPaint!!.color = Color.rgb(240, 240, 240) // light
        // grey
        mBorderPaint = Paint()
        mBorderPaint!!.style = Paint.Style.STROKE
        mBorderPaint!!.color = Color.BLACK
        mBorderPaint!!.strokeWidth = convertDpToPixel(1f)
    }

    // for performance tracking
    private var totalTime: Long = 0
    private var drawCycles: Long = 0
    override fun onDraw(canvas: Canvas) {
        super.onDraw(canvas)
        if (mData == null) return
        val starttime = System.currentTimeMillis()

        // execute all drawing commands
        drawGridBackground(canvas)
        if (isAutoScaleMinMaxEnabled) {
            autoScale()
        }
        if (axisLeft!!.isEnabled) rendererLeftYAxis!!.computeAxis(
            axisLeft!!.mAxisMinimum,
            axisLeft!!.mAxisMaximum,
            axisLeft!!.isInverted
        )
        if (axisRight!!.isEnabled) rendererRightYAxis!!.computeAxis(
            axisRight!!.mAxisMinimum,
            axisRight!!.mAxisMaximum,
            axisRight!!.isInverted
        )
        if (mXAxis.isEnabled) rendererXAxis!!.computeAxis(
            mXAxis.mAxisMinimum,
            mXAxis.mAxisMaximum,
            false
        )
        rendererXAxis!!.renderAxisLine(canvas)
        rendererLeftYAxis!!.renderAxisLine(canvas)
        rendererRightYAxis!!.renderAxisLine(canvas)
        if (mXAxis.isDrawGridLinesBehindDataEnabled) rendererXAxis!!.renderGridLines(canvas)
        if (axisLeft!!.isDrawGridLinesBehindDataEnabled) rendererLeftYAxis!!.renderGridLines(canvas)
        if (axisRight!!.isDrawGridLinesBehindDataEnabled) rendererRightYAxis!!.renderGridLines(
            canvas
        )
        if (mXAxis.isEnabled && mXAxis.isDrawLimitLinesBehindDataEnabled) rendererXAxis!!.renderLimitLines(
            canvas
        )
        if (axisLeft!!.isEnabled && axisLeft!!.isDrawLimitLinesBehindDataEnabled) rendererLeftYAxis!!.renderLimitLines(
            canvas
        )
        if (axisRight!!.isEnabled && axisRight!!.isDrawLimitLinesBehindDataEnabled) rendererRightYAxis!!.renderLimitLines(
            canvas
        )
        var clipRestoreCount = canvas.save()
        if (isClipDataToContentEnabled) {
            // make sure the data cannot be drawn outside the content-rect
            canvas.clipRect(mViewPortHandler.contentRect)
        }
        mRenderer.drawData(canvas)
        if (!mXAxis.isDrawGridLinesBehindDataEnabled) rendererXAxis!!.renderGridLines(canvas)
        if (!axisLeft!!.isDrawGridLinesBehindDataEnabled) rendererLeftYAxis!!.renderGridLines(canvas)
        if (!axisRight!!.isDrawGridLinesBehindDataEnabled) rendererRightYAxis!!.renderGridLines(
            canvas
        )

        // if highlighting is enabled
        if (valuesToHighlight()) mRenderer.drawHighlighted(canvas, mIndicesToHighlight)

        // Removes clipping rectangle
        canvas.restoreToCount(clipRestoreCount)
        mRenderer.drawExtras(canvas)
        if (mXAxis.isEnabled && !mXAxis.isDrawLimitLinesBehindDataEnabled) rendererXAxis!!.renderLimitLines(
            canvas
        )
        if (axisLeft!!.isEnabled && !axisLeft!!.isDrawLimitLinesBehindDataEnabled) rendererLeftYAxis!!.renderLimitLines(
            canvas
        )
        if (axisRight!!.isEnabled && !axisRight!!.isDrawLimitLinesBehindDataEnabled) rendererRightYAxis!!.renderLimitLines(
            canvas
        )
        rendererXAxis!!.renderAxisLabels(canvas)
        rendererLeftYAxis!!.renderAxisLabels(canvas)
        rendererRightYAxis!!.renderAxisLabels(canvas)
        if (isClipValuesToContentEnabled) {
            clipRestoreCount = canvas.save()
            canvas.clipRect(mViewPortHandler.contentRect)
            mRenderer.drawValues(canvas)
            canvas.restoreToCount(clipRestoreCount)
        } else {
            mRenderer.drawValues(canvas)
        }
        mLegendRenderer.renderLegend(canvas)
        drawDescription(canvas)
        drawMarkers(canvas)
        if (mLogEnabled) {
            val drawtime = System.currentTimeMillis() - starttime
            totalTime += drawtime
            drawCycles += 1
            val average = totalTime / drawCycles
            Log.i(
                Chart.Companion.LOG_TAG,
                "Drawtime: " + drawtime + " ms, average: " + average + " ms, cycles: "
                        + drawCycles
            )
        }
    }

    /**
     * RESET PERFORMANCE TRACKING FIELDS
     */
    fun resetTracking() {
        totalTime = 0
        drawCycles = 0
    }

    protected open fun prepareValuePxMatrix() {
        if (mLogEnabled) Log.i(
            Chart.Companion.LOG_TAG,
            "Preparing Value-Px Matrix, xmin: " + mXAxis.mAxisMinimum + ", xmax: "
                    + mXAxis.mAxisMaximum + ", xdelta: " + mXAxis.mAxisRange
        )
        mRightAxisTransformer!!.prepareMatrixValuePx(
            mXAxis.mAxisMinimum,
            mXAxis.mAxisRange,
            axisRight!!.mAxisRange,
            axisRight!!.mAxisMinimum
        )
        mLeftAxisTransformer!!.prepareMatrixValuePx(
            mXAxis.mAxisMinimum,
            mXAxis.mAxisRange,
            axisLeft!!.mAxisRange,
            axisLeft!!.mAxisMinimum
        )
    }

    protected fun prepareOffsetMatrix() {
        mRightAxisTransformer!!.prepareMatrixOffset(axisRight!!.isInverted)
        mLeftAxisTransformer!!.prepareMatrixOffset(axisLeft!!.isInverted)
    }

    override fun notifyDataSetChanged() {
        if (mData == null) {
            if (mLogEnabled) Log.i(Chart.Companion.LOG_TAG, "Preparing... DATA NOT SET.")
            return
        } else {
            if (mLogEnabled) Log.i(Chart.Companion.LOG_TAG, "Preparing...")
        }
        if (mRenderer != null) mRenderer.initBuffers()
        calcMinMax()
        rendererLeftYAxis!!.computeAxis(
            axisLeft!!.mAxisMinimum,
            axisLeft!!.mAxisMaximum,
            axisLeft!!.isInverted
        )
        rendererRightYAxis!!.computeAxis(
            axisRight!!.mAxisMinimum,
            axisRight!!.mAxisMaximum,
            axisRight!!.isInverted
        )
        rendererXAxis!!.computeAxis(mXAxis.mAxisMinimum, mXAxis.mAxisMaximum, false)
        if (mLegend != null) mLegendRenderer.computeLegend(mData)
        calculateOffsets()
    }

    /**
     * Performs auto scaling of the axis by recalculating the minimum and maximum y-values based on the entries currently in view.
     */
    protected fun autoScale() {
        val fromX = lowestVisibleX
        val toX = highestVisibleX
        mData!!.calcMinMaxY(fromX, toX)
        mXAxis.calculate(mData!!.xMin, mData!!.xMax)

        // calculate axis range (min / max) according to provided data
        if (axisLeft!!.isEnabled) axisLeft!!.calculate(
            mData!!.getYMin(AxisDependency.LEFT),
            mData!!.getYMax(AxisDependency.LEFT)
        )
        if (axisRight!!.isEnabled) axisRight!!.calculate(
            mData!!.getYMin(AxisDependency.RIGHT),
            mData!!.getYMax(AxisDependency.RIGHT)
        )
        calculateOffsets()
    }

    override fun calcMinMax() {
        mXAxis.calculate(mData!!.xMin, mData!!.xMax)

        // calculate axis range (min / max) according to provided data
        axisLeft!!.calculate(
            mData!!.getYMin(AxisDependency.LEFT),
            mData!!.getYMax(AxisDependency.LEFT)
        )
        axisRight!!.calculate(
            mData!!.getYMin(AxisDependency.RIGHT),
            mData!!.getYMax(AxisDependency.RIGHT)
        )
    }

    protected open fun calculateLegendOffsets(offsets: RectF) {
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
    }

    private val mOffsetsBuffer = RectF()
    public override fun calculateOffsets() {
        if (!mCustomViewPortEnabled) {
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
            if (axisLeft!!.needsOffset()) {
                offsetLeft += axisLeft!!.getRequiredWidthSpace(
                    rendererLeftYAxis
                        .paintAxisLabels!!
                )
            }
            if (axisRight!!.needsOffset()) {
                offsetRight += axisRight!!.getRequiredWidthSpace(
                    rendererRightYAxis
                        .paintAxisLabels!!
                )
            }
            if (mXAxis.isEnabled && mXAxis.isDrawLabelsEnabled) {
                val xLabelHeight = mXAxis.mLabelRotatedHeight + mXAxis.yOffset

                // offsets for x-labels
                if (mXAxis.position === XAxisPosition.BOTTOM) {
                    offsetBottom += xLabelHeight
                } else if (mXAxis.position === XAxisPosition.TOP) {
                    offsetTop += xLabelHeight
                } else if (mXAxis.position === XAxisPosition.BOTH_SIDED) {
                    offsetBottom += xLabelHeight
                    offsetTop += xLabelHeight
                }
            }
            offsetTop += extraTopOffset
            offsetRight += extraRightOffset
            offsetBottom += extraBottomOffset
            offsetLeft += extraLeftOffset
            val minOffset = convertDpToPixel(
                minOffset
            )
            mViewPortHandler.restrainViewPort(
                Math.max(minOffset, offsetLeft),
                Math.max(minOffset, offsetTop),
                Math.max(minOffset, offsetRight),
                Math.max(minOffset, offsetBottom)
            )
            if (mLogEnabled) {
                Log.i(
                    Chart.Companion.LOG_TAG,
                    "offsetLeft: " + offsetLeft + ", offsetTop: " + offsetTop
                            + ", offsetRight: " + offsetRight + ", offsetBottom: " + offsetBottom
                )
                Log.i(
                    Chart.Companion.LOG_TAG,
                    "Content: " + mViewPortHandler.contentRect.toString()
                )
            }
        }
        prepareOffsetMatrix()
        prepareValuePxMatrix()
    }

    /**
     * draws the grid background
     */
    protected fun drawGridBackground(c: Canvas) {
        if (mDrawGridBackground) {

            // draw the grid background
            c.drawRect(mViewPortHandler.contentRect, mGridBackgroundPaint)
        }
        if (isDrawBordersEnabled) {
            c.drawRect(mViewPortHandler.contentRect, mBorderPaint)
        }
    }

    /**
     * Returns the Transformer class that contains all matrices and is
     * responsible for transforming values into pixels on the screen and
     * backwards.
     *
     * @return
     */
    override fun getTransformer(which: AxisDependency?): Transformer? {
        return if (which === AxisDependency.LEFT) mLeftAxisTransformer else mRightAxisTransformer
    }

    override fun onTouchEvent(event: MotionEvent): Boolean {
        super.onTouchEvent(event)
        if (mChartTouchListener == null || mData == null) return false

        // check if touch gestures are enabled
        return if (!mTouchEnabled) false else mChartTouchListener.onTouch(this, event)
    }

    override fun computeScroll() {
        if (mChartTouchListener is BarLineChartTouchListener) (mChartTouchListener as BarLineChartTouchListener).computeScroll()
    }
    /**
     * ################ ################ ################ ################
     */
    /**
     * CODE BELOW THIS RELATED TO SCALING AND GESTURES AND MODIFICATION OF THE
     * VIEWPORT
     */
    protected var mZoomMatrixBuffer = Matrix()

    /**
     * Zooms in by 1.4f, into the charts center.
     */
    fun zoomIn() {
        val center: MPPointF = mViewPortHandler.getContentCenter()
        mViewPortHandler.zoomIn(center.x, -center.y, mZoomMatrixBuffer)
        mViewPortHandler.refresh(mZoomMatrixBuffer, this, false)
        recycleInstance(center)

        // Range might have changed, which means that Y-axis labels
        // could have changed in size, affecting Y-axis size.
        // So we need to recalculate offsets.
        calculateOffsets()
        postInvalidate()
    }

    /**
     * Zooms out by 0.7f, from the charts center.
     */
    fun zoomOut() {
        val center: MPPointF = mViewPortHandler.getContentCenter()
        mViewPortHandler.zoomOut(center.x, -center.y, mZoomMatrixBuffer)
        mViewPortHandler.refresh(mZoomMatrixBuffer, this, false)
        recycleInstance(center)

        // Range might have changed, which means that Y-axis labels
        // could have changed in size, affecting Y-axis size.
        // So we need to recalculate offsets.
        calculateOffsets()
        postInvalidate()
    }

    /**
     * Zooms out to original size.
     */
    fun resetZoom() {
        mViewPortHandler.resetZoom(mZoomMatrixBuffer)
        mViewPortHandler.refresh(mZoomMatrixBuffer, this, false)

        // Range might have changed, which means that Y-axis labels
        // could have changed in size, affecting Y-axis size.
        // So we need to recalculate offsets.
        calculateOffsets()
        postInvalidate()
    }

    /**
     * Zooms in or out by the given scale factor. x and y are the coordinates
     * (in pixels) of the zoom center.
     *
     * @param scaleX if < 1f --> zoom out, if > 1f --> zoom in
     * @param scaleY if < 1f --> zoom out, if > 1f --> zoom in
     * @param x
     * @param y
     */
    fun zoom(scaleX: Float, scaleY: Float, x: Float, y: Float) {
        mViewPortHandler.zoom(scaleX, scaleY, x, -y, mZoomMatrixBuffer)
        mViewPortHandler.refresh(mZoomMatrixBuffer, this, false)

        // Range might have changed, which means that Y-axis labels
        // could have changed in size, affecting Y-axis size.
        // So we need to recalculate offsets.
        calculateOffsets()
        postInvalidate()
    }

    /**
     * Zooms in or out by the given scale factor.
     * x and y are the values (NOT PIXELS) of the zoom center..
     *
     * @param scaleX
     * @param scaleY
     * @param xValue
     * @param yValue
     * @param axis   the axis relative to which the zoom should take place
     */
    fun zoom(scaleX: Float, scaleY: Float, xValue: Float, yValue: Float, axis: AxisDependency?) {
        val job: Runnable? = getInstance(
            mViewPortHandler,
            scaleX,
            scaleY,
            xValue,
            yValue,
            getTransformer(axis),
            axis,
            this
        )
        addViewportJob(job!!)
    }

    /**
     * Zooms to the center of the chart with the given scale factor.
     *
     * @param scaleX
     * @param scaleY
     */
    fun zoomToCenter(scaleX: Float, scaleY: Float) {
        val center: MPPointF = getCenterOffsets()
        val save = mZoomMatrixBuffer
        mViewPortHandler.zoom(scaleX, scaleY, center.x, -center.y, save)
        mViewPortHandler.refresh(save, this, false)
    }

    /**
     * Zooms by the specified scale factor to the specified values on the specified axis.
     *
     * @param scaleX
     * @param scaleY
     * @param xValue
     * @param yValue
     * @param axis
     * @param duration
     */
    @TargetApi(11)
    fun zoomAndCenterAnimated(
        scaleX: Float, scaleY: Float, xValue: Float, yValue: Float, axis: AxisDependency?,
        duration: Long
    ) {
        val origin = getValuesByTouchPoint(
            mViewPortHandler.contentLeft(),
            mViewPortHandler.contentTop(),
            axis
        )
        val job: Runnable? = getInstance(
            mViewPortHandler,
            this,
            getTransformer(axis),
            getAxis(axis),
            mXAxis.mAxisRange,
            scaleX,
            scaleY,
            mViewPortHandler.scaleX,
            mViewPortHandler.scaleY,
            xValue,
            yValue,
            origin.x.toFloat(),
            origin.y.toFloat(),
            duration
        )
        addViewportJob(job!!)
        recycleInstance(origin)
    }

    protected var mFitScreenMatrixBuffer = Matrix()

    /**
     * Resets all zooming and dragging and makes the chart fit exactly it's
     * bounds.
     */
    fun fitScreen() {
        val save = mFitScreenMatrixBuffer
        mViewPortHandler.fitScreen(save)
        mViewPortHandler.refresh(save, this, false)
        calculateOffsets()
        postInvalidate()
    }

    /**
     * Sets the minimum scale factor value to which can be zoomed out. 1f =
     * fitScreen
     *
     * @param scaleX
     * @param scaleY
     */
    fun setScaleMinima(scaleX: Float, scaleY: Float) {
        mViewPortHandler.setMinimumScaleX(scaleX)
        mViewPortHandler.setMinimumScaleY(scaleY)
    }

    /**
     * Sets the size of the area (range on the x-axis) that should be maximum
     * visible at once (no further zooming out allowed). If this is e.g. set to
     * 10, no more than a range of 10 on the x-axis can be viewed at once without
     * scrolling.
     *
     * @param maxXRange The maximum visible range of x-values.
     */
    open fun setVisibleXRangeMaximum(maxXRange: Float) {
        val xScale = mXAxis.mAxisRange / maxXRange
        mViewPortHandler.setMinimumScaleX(xScale)
    }

    /**
     * Sets the size of the area (range on the x-axis) that should be minimum
     * visible at once (no further zooming in allowed). If this is e.g. set to
     * 10, no less than a range of 10 on the x-axis can be viewed at once without
     * scrolling.
     *
     * @param minXRange The minimum visible range of x-values.
     */
    open fun setVisibleXRangeMinimum(minXRange: Float) {
        val xScale = mXAxis.mAxisRange / minXRange
        mViewPortHandler.setMaximumScaleX(xScale)
    }

    /**
     * Limits the maximum and minimum x range that can be visible by pinching and zooming. e.g. minRange=10, maxRange=100 the
     * smallest range to be displayed at once is 10, and no more than a range of 100 values can be viewed at once without
     * scrolling
     *
     * @param minXRange
     * @param maxXRange
     */
    open fun setVisibleXRange(minXRange: Float, maxXRange: Float) {
        val minScale = mXAxis.mAxisRange / minXRange
        val maxScale = mXAxis.mAxisRange / maxXRange
        mViewPortHandler.setMinMaxScaleX(minScale, maxScale)
    }

    /**
     * Sets the size of the area (range on the y-axis) that should be maximum
     * visible at once.
     *
     * @param maxYRange the maximum visible range on the y-axis
     * @param axis      the axis for which this limit should apply
     */
    open fun setVisibleYRangeMaximum(maxYRange: Float, axis: AxisDependency) {
        val yScale = getAxisRange(axis) / maxYRange
        mViewPortHandler.setMinimumScaleY(yScale)
    }

    /**
     * Sets the size of the area (range on the y-axis) that should be minimum visible at once, no further zooming in possible.
     *
     * @param minYRange
     * @param axis      the axis for which this limit should apply
     */
    open fun setVisibleYRangeMinimum(minYRange: Float, axis: AxisDependency) {
        val yScale = getAxisRange(axis) / minYRange
        mViewPortHandler.setMaximumScaleY(yScale)
    }

    /**
     * Limits the maximum and minimum y range that can be visible by pinching and zooming.
     *
     * @param minYRange
     * @param maxYRange
     * @param axis
     */
    open fun setVisibleYRange(minYRange: Float, maxYRange: Float, axis: AxisDependency) {
        val minScale = getAxisRange(axis) / minYRange
        val maxScale = getAxisRange(axis) / maxYRange
        mViewPortHandler.setMinMaxScaleY(minScale, maxScale)
    }

    /**
     * Moves the left side of the current viewport to the specified x-position.
     * This also refreshes the chart by calling invalidate().
     *
     * @param xValue
     */
    fun moveViewToX(xValue: Float) {
        val job: Runnable? = getInstance(
            mViewPortHandler, xValue, 0f,
            getTransformer(AxisDependency.LEFT), this
        )
        addViewportJob(job!!)
    }

    /**
     * This will move the left side of the current viewport to the specified
     * x-value on the x-axis, and center the viewport to the specified y value on the y-axis.
     * This also refreshes the chart by calling invalidate().
     *
     * @param xValue
     * @param yValue
     * @param axis   - which axis should be used as a reference for the y-axis
     */
    fun moveViewTo(xValue: Float, yValue: Float, axis: AxisDependency) {
        val yInView = getAxisRange(axis) / mViewPortHandler.scaleY
        val job: Runnable? = getInstance(
            mViewPortHandler, xValue, yValue + yInView / 2f,
            getTransformer(axis), this
        )
        addViewportJob(job!!)
    }

    /**
     * This will move the left side of the current viewport to the specified x-value
     * and center the viewport to the y value animated.
     * This also refreshes the chart by calling invalidate().
     *
     * @param xValue
     * @param yValue
     * @param axis
     * @param duration the duration of the animation in milliseconds
     */
    @TargetApi(11)
    fun moveViewToAnimated(xValue: Float, yValue: Float, axis: AxisDependency, duration: Long) {
        val bounds = getValuesByTouchPoint(
            mViewPortHandler.contentLeft(),
            mViewPortHandler.contentTop(),
            axis
        )
        val yInView = getAxisRange(axis) / mViewPortHandler.scaleY
        val job: Runnable? = getInstance(
            mViewPortHandler, xValue, yValue + yInView / 2f,
            getTransformer(axis), this, bounds.x.toFloat(), bounds.y.toFloat(), duration
        )
        addViewportJob(job!!)
        recycleInstance(bounds)
    }

    /**
     * Centers the viewport to the specified y value on the y-axis.
     * This also refreshes the chart by calling invalidate().
     *
     * @param yValue
     * @param axis   - which axis should be used as a reference for the y-axis
     */
    fun centerViewToY(yValue: Float, axis: AxisDependency) {
        val valsInView = getAxisRange(axis) / mViewPortHandler.scaleY
        val job: Runnable? = getInstance(
            mViewPortHandler, 0f, yValue + valsInView / 2f,
            getTransformer(axis), this
        )
        addViewportJob(job!!)
    }

    /**
     * This will move the center of the current viewport to the specified
     * x and y value.
     * This also refreshes the chart by calling invalidate().
     *
     * @param xValue
     * @param yValue
     * @param axis   - which axis should be used as a reference for the y axis
     */
    fun centerViewTo(xValue: Float, yValue: Float, axis: AxisDependency) {
        val yInView = getAxisRange(axis) / mViewPortHandler.scaleY
        val xInView = xAxis.mAxisRange / mViewPortHandler.scaleX
        val job: Runnable? = getInstance(
            mViewPortHandler,
            xValue - xInView / 2f, yValue + yInView / 2f,
            getTransformer(axis), this
        )
        addViewportJob(job!!)
    }

    /**
     * This will move the center of the current viewport to the specified
     * x and y value animated.
     *
     * @param xValue
     * @param yValue
     * @param axis
     * @param duration the duration of the animation in milliseconds
     */
    @TargetApi(11)
    fun centerViewToAnimated(xValue: Float, yValue: Float, axis: AxisDependency, duration: Long) {
        val bounds = getValuesByTouchPoint(
            mViewPortHandler.contentLeft(),
            mViewPortHandler.contentTop(),
            axis
        )
        val yInView = getAxisRange(axis) / mViewPortHandler.scaleY
        val xInView = xAxis.mAxisRange / mViewPortHandler.scaleX
        val job: Runnable? = getInstance(
            mViewPortHandler,
            xValue - xInView / 2f, yValue + yInView / 2f,
            getTransformer(axis), this, bounds.x.toFloat(), bounds.y.toFloat(), duration
        )
        addViewportJob(job!!)
        recycleInstance(bounds)
    }

    /**
     * flag that indicates if a custom viewport offset has been set
     */
    private var mCustomViewPortEnabled = false

    /**
     * Sets custom offsets for the current ViewPort (the offsets on the sides of
     * the actual chart window). Setting this will prevent the chart from
     * automatically calculating it's offsets. Use resetViewPortOffsets() to
     * undo this. ONLY USE THIS WHEN YOU KNOW WHAT YOU ARE DOING, else use
     * setExtraOffsets(...).
     *
     * @param left
     * @param top
     * @param right
     * @param bottom
     */
    fun setViewPortOffsets(
        left: Float, top: Float,
        right: Float, bottom: Float
    ) {
        mCustomViewPortEnabled = true
        post {
            mViewPortHandler.restrainViewPort(left, top, right, bottom)
            prepareOffsetMatrix()
            prepareValuePxMatrix()
        }
    }

    /**
     * Resets all custom offsets set via setViewPortOffsets(...) method. Allows
     * the chart to again calculate all offsets automatically.
     */
    fun resetViewPortOffsets() {
        mCustomViewPortEnabled = false
        calculateOffsets()
    }
    /**
     * ################ ################ ################ ################
     */
    /** CODE BELOW IS GETTERS AND SETTERS  */
    /**
     * Returns the range of the specified axis.
     *
     * @param axis
     * @return
     */
    protected fun getAxisRange(axis: AxisDependency): Float {
        return if (axis === AxisDependency.LEFT) axisLeft!!.mAxisRange else axisRight!!.mAxisRange
    }

    /**
     * Sets the OnDrawListener
     *
     * @param drawListener
     */
    fun setOnDrawListener(drawListener: OnDrawListener?) {
        this.drawListener = drawListener
    }

    protected var mGetPositionBuffer = FloatArray(2)

    /**
     * Returns a recyclable MPPointF instance.
     * Returns the position (in pixels) the provided Entry has inside the chart
     * view or null, if the provided Entry is null.
     *
     * @param e
     * @return
     */
    open fun getPosition(e: Entry?, axis: AxisDependency?): MPPointF? {
        if (e == null) return null
        mGetPositionBuffer[0] = e.x
        mGetPositionBuffer[1] = e.y
        getTransformer(axis)!!.pointValuesToPixel(mGetPositionBuffer)
        return MPPointF.getInstance(mGetPositionBuffer[0], mGetPositionBuffer[1])
    }

    /**
     * sets the number of maximum visible drawn values on the chart only active
     * when setDrawValues() is enabled
     *
     * @param count
     */
    fun setMaxVisibleValueCount(count: Int) {
        maxVisibleCount = count
    }

    /**
     * Sets the color for the background of the chart-drawing area (everything
     * behind the grid lines).
     *
     * @param color
     */
    fun setGridBackgroundColor(color: Int) {
        mGridBackgroundPaint!!.color = color
    }
    /**
     * Returns true if dragging is enabled for the chart, false if not.
     *
     * @return
     */
    /**
     * Set this to true to enable dragging (moving the chart with the finger)
     * for the chart (this does not effect scaling).
     *
     * @param enabled
     */
    var isDragEnabled: Boolean
        get() = isDragXEnabled || isDragYEnabled
        set(enabled) {
            isDragXEnabled = enabled
            isDragYEnabled = enabled
        }

    /**
     * Set this to true to enable scaling (zooming in and out by gesture) for
     * the chart (this does not effect dragging) on both X- and Y-Axis.
     *
     * @param enabled
     */
    fun setScaleEnabled(enabled: Boolean) {
        isScaleXEnabled = enabled
        isScaleYEnabled = enabled
    }

    /**
     * set this to true to draw the grid background, false if not
     *
     * @param enabled
     */
    fun setDrawGridBackground(enabled: Boolean) {
        mDrawGridBackground = enabled
    }

    /**
     * When enabled, the borders rectangle will be rendered.
     * If this is enabled, there is no point drawing the axis-lines of x- and y-axis.
     *
     * @param enabled
     */
    fun setDrawBorders(enabled: Boolean) {
        isDrawBordersEnabled = enabled
    }

    /**
     * When enabled, the values will be clipped to contentRect,
     * otherwise they can bleed outside the content rect.
     *
     * @param enabled
     */
    fun setClipValuesToContent(enabled: Boolean) {
        isClipValuesToContentEnabled = enabled
    }

    /**
     * When disabled, the data and/or highlights will not be clipped to contentRect. Disabling this option can
     * be useful, when the data lies fully within the content rect, but is drawn in such a way (such as thick lines)
     * that there is unwanted clipping.
     *
     * @param enabled
     */
    fun setClipDataToContent(enabled: Boolean) {
        isClipDataToContentEnabled = enabled
    }

    /**
     * Sets the width of the border lines in dp.
     *
     * @param width
     */
    fun setBorderWidth(width: Float) {
        mBorderPaint!!.strokeWidth = convertDpToPixel(width)
    }

    /**
     * Sets the color of the chart border lines.
     *
     * @param color
     */
    fun setBorderColor(color: Int) {
        mBorderPaint!!.color = color
    }

    /**
     * Returns a recyclable MPPointD instance
     * Returns the x and y values in the chart at the given touch point
     * (encapsulated in a MPPointD). This method transforms pixel coordinates to
     * coordinates / values in the chart. This is the opposite method to
     * getPixelForValues(...).
     *
     * @param x
     * @param y
     * @return
     */
    fun getValuesByTouchPoint(x: Float, y: Float, axis: AxisDependency?): MPPointD {
        val result = getInstance(0.0, 0.0)
        getValuesByTouchPoint(x, y, axis, result)
        return result
    }

    fun getValuesByTouchPoint(x: Float, y: Float, axis: AxisDependency?, outputPoint: MPPointD?) {
        getTransformer(axis)!!.getValuesByTouchPoint(x, y, outputPoint!!)
    }

    /**
     * Returns a recyclable MPPointD instance
     * Transforms the given chart values into pixels. This is the opposite
     * method to getValuesByTouchPoint(...).
     *
     * @param x
     * @param y
     * @return
     */
    fun getPixelForValues(x: Float, y: Float, axis: AxisDependency?): MPPointD {
        return getTransformer(axis)!!.getPixelForValues(x, y)
    }

    /**
     * returns the Entry object displayed at the touched position of the chart
     *
     * @param x
     * @param y
     * @return
     */
    fun getEntryByTouchPoint(x: Float, y: Float): Entry? {
        val h = getHighlightByTouchPoint(x, y)
        return if (h != null) {
            mData!!.getEntryForHighlight(h)
        } else null
    }

    /**
     * returns the DataSet object displayed at the touched position of the chart
     *
     * @param x
     * @param y
     * @return
     */
    fun getDataSetByTouchPoint(x: Float, y: Float): IBarLineScatterCandleBubbleDataSet<*>? {
        val h = getHighlightByTouchPoint(x, y)
        return if (h != null) {
            mData!!.getDataSetByIndex(h.dataSetIndex)
        } else null
    }

    /**
     * buffer for storing lowest visible x point
     */
    protected var posForGetLowestVisibleX = getInstance(0.0, 0.0)

    /**
     * Returns the lowest x-index (value on the x-axis) that is still visible on
     * the chart.
     *
     * @return
     */
    override val lowestVisibleX: Float
        get() {
            getTransformer(AxisDependency.LEFT)!!.getValuesByTouchPoint(
                mViewPortHandler.contentLeft(),
                mViewPortHandler.contentBottom(), posForGetLowestVisibleX
            )
            return Math.max(mXAxis.mAxisMinimum, posForGetLowestVisibleX.x).toFloat()
        }

    /**
     * buffer for storing highest visible x point
     */
    protected var posForGetHighestVisibleX = getInstance(0.0, 0.0)

    /**
     * Returns the highest x-index (value on the x-axis) that is still visible
     * on the chart.
     *
     * @return
     */
    override val highestVisibleX: Float
        get() {
            getTransformer(AxisDependency.LEFT)!!.getValuesByTouchPoint(
                mViewPortHandler.contentRight(),
                mViewPortHandler.contentBottom(), posForGetHighestVisibleX
            )
            return Math.min(mXAxis.mAxisMaximum, posForGetHighestVisibleX.x).toFloat()
        }

    /**
     * Returns the range visible on the x-axis.
     *
     * @return
     */
    val visibleXRange: Float
        get() = Math.abs(highestVisibleX - lowestVisibleX)

    /**
     * returns the current x-scale factor
     */
    val scaleX: Float
        get() = if (mViewPortHandler == null) 1f else mViewPortHandler.scaleX

    /**
     * returns the current y-scale factor
     */
    val scaleY: Float
        get() = if (mViewPortHandler == null) 1f else mViewPortHandler.scaleY

    /**
     * if the chart is fully zoomed out, return true
     *
     * @return
     */
    val isFullyZoomedOut: Boolean
        get() = mViewPortHandler.isFullyZoomedOut

    /**
     * Returns the y-axis object to the corresponding AxisDependency. In the
     * horizontal bar-chart, LEFT == top, RIGHT == BOTTOM
     *
     * @param axis
     * @return
     */
    fun getAxis(axis: AxisDependency?): YAxis? {
        return if (axis === AxisDependency.LEFT) axisLeft else axisRight
    }

    override fun isInverted(axis: AxisDependency?): Boolean {
        return getAxis(axis)!!.isInverted
    }

    /**
     * If set to true, both x and y axis can be scaled simultaneously with 2 fingers, if false,
     * x and y axis can be scaled separately. default: false
     *
     * @param enabled
     */
    fun setPinchZoom(enabled: Boolean) {
        isPinchZoomEnabled = enabled
    }

    /**
     * Set an offset in dp that allows the user to drag the chart over it's
     * bounds on the x-axis.
     *
     * @param offset
     */
    fun setDragOffsetX(offset: Float) {
        mViewPortHandler.setDragOffsetX(offset)
    }

    /**
     * Set an offset in dp that allows the user to drag the chart over it's
     * bounds on the y-axis.
     *
     * @param offset
     */
    fun setDragOffsetY(offset: Float) {
        mViewPortHandler.setDragOffsetY(offset)
    }

    /**
     * Returns true if both drag offsets (x and y) are zero or smaller.
     *
     * @return
     */
    fun hasNoDragOffset(): Boolean {
        return mViewPortHandler.hasNoDragOffset()
    }

    /**
     * Sets a custom XAxisRenderer and overrides the existing (default) one.
     *
     * @param xAxisRenderer
     */
    fun setXAxisRenderer(xAxisRenderer: XAxisRenderer?) {
        rendererXAxis = xAxisRenderer
    }

    override val yChartMax: Float
        get() = Math.max(axisLeft!!.mAxisMaximum, axisRight!!.mAxisMaximum)
    override val yChartMin: Float
        get() = Math.min(axisLeft!!.mAxisMinimum, axisRight!!.mAxisMinimum)

    /**
     * Returns true if either the left or the right or both axes are inverted.
     *
     * @return
     */
    val isAnyAxisInverted: Boolean
        get() {
            if (axisLeft!!.isInverted) return true
            return if (axisRight!!.isInverted) true else false
        }

    override fun setPaint(p: Paint?, which: Int) {
        super.setPaint(p, which)
        when (which) {
            Chart.Companion.PAINT_GRID_BACKGROUND -> mGridBackgroundPaint = p
        }
    }

    override fun getPaint(which: Int): Paint? {
        val p = super.getPaint(which)
        if (p != null) return p
        when (which) {
            Chart.Companion.PAINT_GRID_BACKGROUND -> return mGridBackgroundPaint
        }
        return null
    }

    protected var mOnSizeChangedBuffer = FloatArray(2)
    override fun onSizeChanged(w: Int, h: Int, oldw: Int, oldh: Int) {

        // Saving current position of chart.
        mOnSizeChangedBuffer[1] = 0
        mOnSizeChangedBuffer[0] = mOnSizeChangedBuffer[1]
        if (isKeepPositionOnRotation) {
            mOnSizeChangedBuffer[0] = mViewPortHandler.contentLeft()
            mOnSizeChangedBuffer[1] = mViewPortHandler.contentTop()
            getTransformer(AxisDependency.LEFT)!!.pixelsToValue(mOnSizeChangedBuffer)
        }

        //Superclass transforms chart.
        super.onSizeChanged(w, h, oldw, oldh)
        if (isKeepPositionOnRotation) {

            //Restoring old position of chart.
            getTransformer(AxisDependency.LEFT)!!.pointValuesToPixel(mOnSizeChangedBuffer)
            mViewPortHandler.centerViewPort(mOnSizeChangedBuffer, this)
        } else {
            mViewPortHandler.refresh(mViewPortHandler.matrixTouch, this, true)
        }
    }
}