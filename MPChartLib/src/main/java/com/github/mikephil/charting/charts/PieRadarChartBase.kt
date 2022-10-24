package com.github.mikephil.charting.charts

import com.github.mikephil.charting.utils.Utils.init
import com.github.mikephil.charting.utils.Utils.convertDpToPixel
import com.github.mikephil.charting.data.ChartData.yMin
import com.github.mikephil.charting.data.ChartData.yMax
import com.github.mikephil.charting.data.ChartData.dataSets
import com.github.mikephil.charting.interfaces.datasets.IDataSet.needsFormatter
import com.github.mikephil.charting.interfaces.datasets.IDataSet.valueFormatter
import com.github.mikephil.charting.listener.ChartTouchListener.setLastHighlighted
import com.github.mikephil.charting.data.ChartData.clearValues
import com.github.mikephil.charting.data.ChartData.entryCount
import com.github.mikephil.charting.utils.Utils.getDecimals
import com.github.mikephil.charting.formatter.DefaultValueFormatter.setup
import com.github.mikephil.charting.components.ComponentBase.isEnabled
import com.github.mikephil.charting.components.Description.position
import com.github.mikephil.charting.components.ComponentBase.typeface
import com.github.mikephil.charting.components.ComponentBase.textSize
import com.github.mikephil.charting.components.ComponentBase.textColor
import com.github.mikephil.charting.components.Description.textAlign
import com.github.mikephil.charting.utils.ViewPortHandler.offsetRight
import com.github.mikephil.charting.components.ComponentBase.xOffset
import com.github.mikephil.charting.utils.ViewPortHandler.offsetBottom
import com.github.mikephil.charting.components.ComponentBase.yOffset
import com.github.mikephil.charting.components.Description.text
import com.github.mikephil.charting.data.ChartData.dataSetCount
import com.github.mikephil.charting.highlight.Highlight.toString
import com.github.mikephil.charting.data.ChartData.getEntryForHighlight
import com.github.mikephil.charting.listener.OnChartValueSelectedListener.onNothingSelected
import com.github.mikephil.charting.listener.OnChartValueSelectedListener.onValueSelected
import com.github.mikephil.charting.highlight.IHighlighter.getHighlight
import com.github.mikephil.charting.data.ChartData.getDataSetByIndex
import com.github.mikephil.charting.highlight.Highlight.dataSetIndex
import com.github.mikephil.charting.interfaces.datasets.IDataSet.getEntryIndex
import com.github.mikephil.charting.interfaces.datasets.IDataSet.entryCount
import com.github.mikephil.charting.utils.ViewPortHandler.isInBounds
import com.github.mikephil.charting.components.IMarker.refreshContent
import com.github.mikephil.charting.components.IMarker.draw
import com.github.mikephil.charting.highlight.Highlight.drawX
import com.github.mikephil.charting.highlight.Highlight.drawY
import com.github.mikephil.charting.utils.ViewPortHandler.contentRect
import com.github.mikephil.charting.utils.ViewPortHandler.hasChartDimens
import com.github.mikephil.charting.utils.ViewPortHandler.setChartDimens
import com.github.mikephil.charting.components.AxisBase.spaceMin
import com.github.mikephil.charting.components.AxisBase.spaceMax
import com.github.mikephil.charting.components.AxisBase.calculate
import com.github.mikephil.charting.data.ChartData.xMin
import com.github.mikephil.charting.data.BarData.barWidth
import com.github.mikephil.charting.data.ChartData.xMax
import com.github.mikephil.charting.components.YAxis.calculate
import com.github.mikephil.charting.data.ChartData.getYMin
import com.github.mikephil.charting.data.ChartData.getYMax
import com.github.mikephil.charting.highlight.Highlight.x
import com.github.mikephil.charting.highlight.Highlight.y
import com.github.mikephil.charting.highlight.Highlight.xPx
import com.github.mikephil.charting.highlight.Highlight.yPx
import com.github.mikephil.charting.highlight.Highlight.axis
import com.github.mikephil.charting.data.ChartData.getDataSetForEntry
import com.github.mikephil.charting.data.BarEntry.y
import com.github.mikephil.charting.data.Entry.x
import com.github.mikephil.charting.interfaces.datasets.IDataSet.axisDependency
import com.github.mikephil.charting.utils.Transformer.rectValueToPixel
import com.github.mikephil.charting.data.BarData.groupBars
import com.github.mikephil.charting.renderer.LegendRenderer.renderLegend
import com.github.mikephil.charting.data.PieData.dataSet
import com.github.mikephil.charting.interfaces.datasets.IPieDataSet.selectionShift
import com.github.mikephil.charting.utils.MPPointF.Companion.recycleInstance
import com.github.mikephil.charting.data.PieData.yValueSum
import com.github.mikephil.charting.data.PieData.dataSets
import com.github.mikephil.charting.interfaces.datasets.IDataSet.getEntryForIndex
import com.github.mikephil.charting.data.BaseEntry.y
import com.github.mikephil.charting.utils.Utils.getNormalizedAngle
import com.github.mikephil.charting.interfaces.datasets.IDataSet.getEntryForXValue
import com.github.mikephil.charting.renderer.PieChartRenderer.paintHole
import com.github.mikephil.charting.renderer.LegendRenderer.labelPaint
import com.github.mikephil.charting.renderer.PieChartRenderer.paintCenterText
import com.github.mikephil.charting.renderer.PieChartRenderer.paintTransparentCircle
import com.github.mikephil.charting.renderer.PieChartRenderer.paintEntryLabels
import com.github.mikephil.charting.renderer.PieChartRenderer.releaseBitmap
import com.github.mikephil.charting.renderer.LineChartRenderer.releaseBitmap
import com.github.mikephil.charting.components.YAxis.labelXOffset
import com.github.mikephil.charting.data.ChartData.maxEntryCountSet
import com.github.mikephil.charting.components.YAxis.isInverted
import com.github.mikephil.charting.components.Legend.isLegendCustom
import com.github.mikephil.charting.renderer.LegendRenderer.computeLegend
import com.github.mikephil.charting.components.AxisBase.isDrawLimitLinesBehindDataEnabled
import com.github.mikephil.charting.components.AxisBase.isDrawLabelsEnabled
import com.github.mikephil.charting.data.CombinedData.getLineData
import com.github.mikephil.charting.data.CombinedData.getBarData
import com.github.mikephil.charting.data.CombinedData.getScatterData
import com.github.mikephil.charting.data.CombinedData.getCandleData
import com.github.mikephil.charting.data.CombinedData.getBubbleData
import com.github.mikephil.charting.data.CombinedData.getDataSetByHighlight
import com.github.mikephil.charting.data.CombinedData.getEntryForHighlight
import com.github.mikephil.charting.utils.ViewPortHandler.matrixTouch
import com.github.mikephil.charting.renderer.AxisRenderer.computeAxis
import com.github.mikephil.charting.renderer.XAxisRenderer.computeAxis
import com.github.mikephil.charting.renderer.XAxisRenderer.renderAxisLine
import com.github.mikephil.charting.renderer.YAxisRenderer.renderAxisLine
import com.github.mikephil.charting.components.AxisBase.isDrawGridLinesBehindDataEnabled
import com.github.mikephil.charting.renderer.XAxisRenderer.renderGridLines
import com.github.mikephil.charting.renderer.YAxisRenderer.renderGridLines
import com.github.mikephil.charting.renderer.XAxisRenderer.renderLimitLines
import com.github.mikephil.charting.renderer.YAxisRenderer.renderLimitLines
import com.github.mikephil.charting.renderer.XAxisRenderer.renderAxisLabels
import com.github.mikephil.charting.renderer.YAxisRenderer.renderAxisLabels
import com.github.mikephil.charting.utils.Transformer.prepareMatrixValuePx
import com.github.mikephil.charting.utils.Transformer.prepareMatrixOffset
import com.github.mikephil.charting.data.ChartData.calcMinMaxY
import com.github.mikephil.charting.components.Legend.isDrawInsideEnabled
import com.github.mikephil.charting.utils.ViewPortHandler.chartWidth
import com.github.mikephil.charting.components.Legend.maxSizePercent
import com.github.mikephil.charting.utils.ViewPortHandler.chartHeight
import com.github.mikephil.charting.components.Legend.verticalAlignment
import com.github.mikephil.charting.components.Legend.horizontalAlignment
import com.github.mikephil.charting.components.Legend.orientation
import com.github.mikephil.charting.components.YAxis.needsOffset
import com.github.mikephil.charting.components.YAxis.getRequiredWidthSpace
import com.github.mikephil.charting.renderer.AxisRenderer.paintAxisLabels
import com.github.mikephil.charting.components.XAxis.position
import com.github.mikephil.charting.utils.ViewPortHandler.restrainViewPort
import com.github.mikephil.charting.listener.BarLineChartTouchListener.computeScroll
import com.github.mikephil.charting.utils.ViewPortHandler.zoomIn
import com.github.mikephil.charting.utils.ViewPortHandler.refresh
import com.github.mikephil.charting.utils.ViewPortHandler.zoomOut
import com.github.mikephil.charting.utils.ViewPortHandler.resetZoom
import com.github.mikephil.charting.utils.ViewPortHandler.zoom
import com.github.mikephil.charting.jobs.ZoomJob.Companion.getInstance
import com.github.mikephil.charting.utils.ViewPortHandler.contentLeft
import com.github.mikephil.charting.utils.ViewPortHandler.contentTop
import com.github.mikephil.charting.jobs.AnimatedZoomJob.Companion.getInstance
import com.github.mikephil.charting.utils.ViewPortHandler.scaleX
import com.github.mikephil.charting.utils.ViewPortHandler.scaleY
import com.github.mikephil.charting.utils.MPPointD.Companion.recycleInstance
import com.github.mikephil.charting.utils.ViewPortHandler.fitScreen
import com.github.mikephil.charting.utils.ViewPortHandler.setMinimumScaleX
import com.github.mikephil.charting.utils.ViewPortHandler.setMinimumScaleY
import com.github.mikephil.charting.utils.ViewPortHandler.setMaximumScaleX
import com.github.mikephil.charting.utils.ViewPortHandler.setMinMaxScaleX
import com.github.mikephil.charting.utils.ViewPortHandler.setMaximumScaleY
import com.github.mikephil.charting.utils.ViewPortHandler.setMinMaxScaleY
import com.github.mikephil.charting.jobs.MoveViewJob.Companion.getInstance
import com.github.mikephil.charting.jobs.AnimatedMoveViewJob.Companion.getInstance
import com.github.mikephil.charting.utils.Transformer.pointValuesToPixel
import com.github.mikephil.charting.utils.MPPointD.Companion.getInstance
import com.github.mikephil.charting.utils.Transformer.getValuesByTouchPoint
import com.github.mikephil.charting.utils.Transformer.getPixelForValues
import com.github.mikephil.charting.utils.ViewPortHandler.contentBottom
import com.github.mikephil.charting.utils.ViewPortHandler.contentRight
import com.github.mikephil.charting.utils.ViewPortHandler.isFullyZoomedOut
import com.github.mikephil.charting.utils.ViewPortHandler.setDragOffsetX
import com.github.mikephil.charting.utils.ViewPortHandler.setDragOffsetY
import com.github.mikephil.charting.utils.ViewPortHandler.hasNoDragOffset
import com.github.mikephil.charting.utils.Transformer.pixelsToValue
import com.github.mikephil.charting.utils.ViewPortHandler.centerViewPort
import com.github.mikephil.charting.listener.PieRadarChartTouchListener.computeScroll
import com.github.mikephil.charting.components.YAxis.getRequiredHeightSpace
import com.github.mikephil.charting.interfaces.datasets.IDataSet
import android.view.ViewGroup
import com.github.mikephil.charting.interfaces.dataprovider.ChartInterface
import com.github.mikephil.charting.formatter.DefaultValueFormatter
import com.github.mikephil.charting.components.XAxis
import com.github.mikephil.charting.components.Legend
import com.github.mikephil.charting.listener.OnChartValueSelectedListener
import com.github.mikephil.charting.listener.ChartTouchListener
import com.github.mikephil.charting.listener.OnChartGestureListener
import com.github.mikephil.charting.renderer.LegendRenderer
import com.github.mikephil.charting.highlight.IHighlighter
import com.github.mikephil.charting.utils.ViewPortHandler
import com.github.mikephil.charting.animation.ChartAnimator
import android.animation.ValueAnimator.AnimatorUpdateListener
import android.animation.ValueAnimator
import android.graphics.Paint.Align
import com.github.mikephil.charting.charts.Chart
import android.text.TextUtils
import com.github.mikephil.charting.utils.MPPointF
import kotlin.jvm.JvmOverloads
import com.github.mikephil.charting.components.IMarker
import com.github.mikephil.charting.animation.Easing.EasingFunction
import com.github.mikephil.charting.formatter.IValueFormatter
import android.graphics.Typeface
import android.graphics.RectF
import android.view.ViewParent
import com.github.mikephil.charting.highlight.ChartHighlighter
import android.graphics.Bitmap
import android.graphics.drawable.Drawable
import android.os.Environment
import android.graphics.Bitmap.CompressFormat
import android.content.ContentValues
import android.provider.MediaStore.Images
import com.github.mikephil.charting.charts.BarLineChartBase
import com.github.mikephil.charting.interfaces.dataprovider.BarDataProvider
import com.github.mikephil.charting.renderer.BarChartRenderer
import com.github.mikephil.charting.highlight.BarHighlighter
import com.github.mikephil.charting.components.YAxis
import com.github.mikephil.charting.interfaces.datasets.IBarDataSet
import com.github.mikephil.charting.charts.PieRadarChartBase
import com.github.mikephil.charting.renderer.PieChartRenderer
import com.github.mikephil.charting.highlight.PieHighlighter
import com.github.mikephil.charting.interfaces.datasets.IPieDataSet
import com.github.mikephil.charting.interfaces.dataprovider.LineDataProvider
import com.github.mikephil.charting.renderer.LineChartRenderer
import com.github.mikephil.charting.components.YAxis.AxisDependency
import com.github.mikephil.charting.renderer.RadarChartRenderer
import com.github.mikephil.charting.highlight.RadarHighlighter
import com.github.mikephil.charting.interfaces.dataprovider.BubbleDataProvider
import com.github.mikephil.charting.interfaces.dataprovider.ScatterDataProvider
import com.github.mikephil.charting.charts.ScatterChart.ScatterShape
import com.github.mikephil.charting.interfaces.dataprovider.CombinedDataProvider
import com.github.mikephil.charting.charts.CombinedChart.DrawOrder
import com.github.mikephil.charting.highlight.CombinedHighlighter
import android.annotation.SuppressLint
import com.github.mikephil.charting.interfaces.datasets.IBarLineScatterCandleBubbleDataSet
import com.github.mikephil.charting.interfaces.dataprovider.BarLineScatterCandleBubbleDataProvider
import com.github.mikephil.charting.renderer.YAxisRenderer
import com.github.mikephil.charting.renderer.XAxisRenderer
import com.github.mikephil.charting.listener.BarLineChartTouchListener
import com.github.mikephil.charting.components.Legend.LegendOrientation
import com.github.mikephil.charting.components.Legend.LegendHorizontalAlignment
import com.github.mikephil.charting.components.Legend.LegendVerticalAlignment
import com.github.mikephil.charting.components.XAxis.XAxisPosition
import android.view.MotionEvent
import com.github.mikephil.charting.jobs.ZoomJob
import android.annotation.TargetApi
import com.github.mikephil.charting.utils.MPPointD
import com.github.mikephil.charting.jobs.AnimatedZoomJob
import com.github.mikephil.charting.jobs.MoveViewJob
import com.github.mikephil.charting.jobs.AnimatedMoveViewJob
import com.github.mikephil.charting.interfaces.dataprovider.CandleDataProvider
import com.github.mikephil.charting.listener.PieRadarChartTouchListener
import com.github.mikephil.charting.charts.RadarChart
import android.animation.ObjectAnimator
import android.content.Context
import android.util.AttributeSet
import android.util.Log
import com.github.mikephil.charting.charts.BarChart
import com.github.mikephil.charting.data.*
import com.github.mikephil.charting.utils.HorizontalViewPortHandler
import com.github.mikephil.charting.utils.TransformerHorizontalBarChart
import com.github.mikephil.charting.highlight.HorizontalBarHighlighter

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
     * gets the raw version of the current rotation angle of the pie chart the
     * returned value could be any value, negative or positive, outside of the
     * 360 degrees. this is used when working with rotation direction, mainly by
     * gestures and animations.
     *
     * @return
     */
    /**
     * holds the raw version of the current rotation angle of the chart
     */
    var rawRotationAngle = 270f
        private set
    /**
     * Returns true if rotation of the chart by touch is enabled, false if not.
     *
     * @return
     */
    /**
     * Set this to true to enable the rotation / spinning of the chart by touch.
     * Set it to false to disable it. Default: true
     *
     * @param enabled
     */
    /**
     * flag that indicates if rotation is enabled or not
     */
    var isRotationEnabled = true
    /**
     * Gets the minimum offset (padding) around the chart, defaults to 0.f
     */
    /**
     * Sets the minimum offset (padding) around the chart, defaults to 0.f
     */
    /**
     * Sets the minimum offset (padding) around the chart, defaults to 0.f
     */
    var minOffset = 0f

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

    override fun onTouchEvent(event: MotionEvent): Boolean {
        // use the pie- and radarchart listener own listener
        return if (mTouchEnabled && mChartTouchListener != null) mChartTouchListener.onTouch(
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
        if (mLegend != null) mLegendRenderer.computeLegend(mData)
        calculateOffsets()
    }

    public override fun calculateOffsets() {
        var legendLeft = 0f
        var legendRight = 0f
        var legendBottom = 0f
        var legendTop = 0f
        if (mLegend != null && mLegend.isEnabled && !mLegend.isDrawInsideEnabled) {
            val fullLegendWidth = Math.min(
                mLegend.mNeededWidth,
                mViewPortHandler.chartWidth * mLegend.maxSizePercent
            )
            when (mLegend.orientation) {
                LegendOrientation.VERTICAL -> {
                    var xLegendOffset = 0f
                    if (mLegend.horizontalAlignment === LegendHorizontalAlignment.LEFT
                        || mLegend.horizontalAlignment === LegendHorizontalAlignment.RIGHT
                    ) {
                        if (mLegend.verticalAlignment === LegendVerticalAlignment.CENTER) {
                            // this is the space between the legend and the chart
                            val spacing = convertDpToPixel(13f)
                            xLegendOffset = fullLegendWidth + spacing
                        } else {
                            // this is the space between the legend and the chart
                            val spacing = convertDpToPixel(8f)
                            val legendWidth = fullLegendWidth + spacing
                            val legendHeight = mLegend.mNeededHeight + mLegend.mTextHeightMax
                            val center = center
                            val bottomX = if (mLegend.horizontalAlignment ===
                                LegendHorizontalAlignment.RIGHT
                            ) getWidth() - legendWidth + 15f else legendWidth - 15f
                            val bottomY = legendHeight + 15f
                            val distLegend = distanceToCenter(bottomX, bottomY)
                            val reference = getPosition(
                                center, radius,
                                getAngleForPoint(bottomX, bottomY)
                            )
                            val distReference = distanceToCenter(reference.x, reference.y)
                            val minOffset = convertDpToPixel(5f)
                            if (bottomY >= center.y && getHeight() - legendWidth > getWidth()) {
                                xLegendOffset = legendWidth
                            } else if (distLegend < distReference) {
                                val diff = distReference - distLegend
                                xLegendOffset = minOffset + diff
                            }
                            recycleInstance(center)
                            recycleInstance(reference)
                        }
                    }
                    when (mLegend.horizontalAlignment) {
                        LegendHorizontalAlignment.LEFT -> legendLeft = xLegendOffset
                        LegendHorizontalAlignment.RIGHT -> legendRight = xLegendOffset
                        LegendHorizontalAlignment.CENTER -> when (mLegend.verticalAlignment) {
                            LegendVerticalAlignment.TOP -> legendTop = Math.min(
                                mLegend.mNeededHeight,
                                mViewPortHandler.chartHeight * mLegend.maxSizePercent
                            )
                            LegendVerticalAlignment.BOTTOM -> legendBottom = Math.min(
                                mLegend.mNeededHeight,
                                mViewPortHandler.chartHeight * mLegend.maxSizePercent
                            )
                        }
                    }
                }
                LegendOrientation.HORIZONTAL -> {
                    var yLegendOffset = 0f
                    if (mLegend.verticalAlignment === LegendVerticalAlignment.TOP ||
                        mLegend.verticalAlignment === LegendVerticalAlignment.BOTTOM
                    ) {

                        // It's possible that we do not need this offset anymore as it
                        //   is available through the extraOffsets, but changing it can mean
                        //   changing default visibility for existing apps.
                        val yOffset = requiredLegendOffset
                        yLegendOffset = Math.min(
                            mLegend.mNeededHeight + yOffset,
                            mViewPortHandler.chartHeight * mLegend.maxSizePercent
                        )
                        when (mLegend.verticalAlignment) {
                            LegendVerticalAlignment.TOP -> legendTop = yLegendOffset
                            LegendVerticalAlignment.BOTTOM -> legendBottom = yLegendOffset
                        }
                    }
                }
            }
            legendLeft += requiredBaseOffset
            legendRight += requiredBaseOffset
            legendTop += requiredBaseOffset
            legendBottom += requiredBaseOffset
        }
        var minOffset = convertDpToPixel(
            minOffset
        )
        if (this is RadarChart) {
            val x = getXAxis()
            if (x!!.isEnabled && x!!.isDrawLabelsEnabled) {
                minOffset = Math.max(minOffset, x!!.mLabelRotatedWidth)
            }
        }
        legendTop += extraTopOffset
        legendRight += extraRightOffset
        legendBottom += extraBottomOffset
        legendLeft += extraLeftOffset
        val offsetLeft = Math.max(minOffset, legendLeft)
        val offsetTop = Math.max(minOffset, legendTop)
        val offsetRight = Math.max(minOffset, legendRight)
        val offsetBottom = Math.max(
            minOffset, Math.max(
                requiredBaseOffset, legendBottom
            )
        )
        mViewPortHandler.restrainViewPort(offsetLeft, offsetTop, offsetRight, offsetBottom)
        if (mLogEnabled) Log.i(
            Chart.Companion.LOG_TAG, "offsetLeft: " + offsetLeft + ", offsetTop: " + offsetTop
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
    fun getAngleForPoint(x: Float, y: Float): Float {
        val c: MPPointF = getCenterOffsets()
        val tx = (x - c.x).toDouble()
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
    fun getPosition(center: MPPointF, dist: Float, angle: Float): MPPointF {
        val p = MPPointF.getInstance(0, 0)
        getPosition(center, dist, angle, p)
        return p
    }

    fun getPosition(center: MPPointF, dist: Float, angle: Float, outputPoint: MPPointF) {
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
    fun distanceToCenter(x: Float, y: Float): Float {
        val c: MPPointF = getCenterOffsets()
        var dist = 0f
        var xDist = 0f
        var yDist = 0f
        xDist = if (x > c.x) {
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
     * gets a normalized version of the current rotation angle of the pie chart,
     * which will always be between 0.0 < 360.0
     *
     * @return
     */
    /**
     * Set an offset for the rotation of the RadarChart in degrees. Default 270f
     * --> top (NORTH)
     *
     * @param angle
     */
    var rotationAngle: Float
        get() = mRotationAngle
        set(angle) {
            rawRotationAngle = angle
            mRotationAngle = getNormalizedAngle(
                rawRotationAngle
            )
        }

    /**
     * returns the diameter of the pie- or radar-chart
     *
     * @return
     */
    val diameter: Float
        get() {
            val content = mViewPortHandler.contentRect
            content.left += extraLeftOffset
            content.top += extraTopOffset
            content.right -= extraRightOffset
            content.bottom -= extraBottomOffset
            return Math.min(content.width(), content.height())
        }

    /**
     * Returns the radius of the chart in pixels.
     *
     * @return
     */
    abstract val radius: Float

    /**
     * Returns the required offset for the chart legend.
     *
     * @return
     */
    protected abstract val requiredLegendOffset: Float

    /**
     * Returns the base offset needed for the chart without calculating the
     * legend size.
     *
     * @return
     */
    abstract val requiredBaseOffset: Float
        protected get

    // TODO Auto-generated method stub
    val yChartMax: Float
        get() =// TODO Auto-generated method stub
            0

    // TODO Auto-generated method stub
    val yChartMin: Float
        get() =// TODO Auto-generated method stub
            0
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
    fun spin(durationmillis: Int, fromangle: Float, toangle: Float, easing: EasingFunction?) {
        rotationAngle = fromangle
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