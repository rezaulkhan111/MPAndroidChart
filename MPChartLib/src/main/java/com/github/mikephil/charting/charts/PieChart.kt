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
import com.github.mikephil.charting.data.ChartData
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
import android.graphics.Bitmap
import android.graphics.drawable.Drawable
import android.os.Environment
import android.graphics.Bitmap.CompressFormat
import android.content.ContentValues
import android.provider.MediaStore.Images
import com.github.mikephil.charting.charts.BarLineChartBase
import com.github.mikephil.charting.data.BarData
import com.github.mikephil.charting.interfaces.dataprovider.BarDataProvider
import com.github.mikephil.charting.renderer.BarChartRenderer
import com.github.mikephil.charting.components.YAxis
import com.github.mikephil.charting.data.BarEntry
import com.github.mikephil.charting.interfaces.datasets.IBarDataSet
import com.github.mikephil.charting.charts.PieRadarChartBase
import com.github.mikephil.charting.data.PieData
import com.github.mikephil.charting.renderer.PieChartRenderer
import com.github.mikephil.charting.interfaces.datasets.IPieDataSet
import com.github.mikephil.charting.data.LineData
import com.github.mikephil.charting.interfaces.dataprovider.LineDataProvider
import com.github.mikephil.charting.renderer.LineChartRenderer
import com.github.mikephil.charting.data.RadarData
import com.github.mikephil.charting.components.YAxis.AxisDependency
import com.github.mikephil.charting.renderer.RadarChartRenderer
import com.github.mikephil.charting.data.BubbleData
import com.github.mikephil.charting.interfaces.dataprovider.BubbleDataProvider
import com.github.mikephil.charting.data.ScatterData
import com.github.mikephil.charting.interfaces.dataprovider.ScatterDataProvider
import com.github.mikephil.charting.charts.ScatterChart.ScatterShape
import com.github.mikephil.charting.data.CombinedData
import com.github.mikephil.charting.interfaces.dataprovider.CombinedDataProvider
import com.github.mikephil.charting.charts.CombinedChart.DrawOrder
import com.github.mikephil.charting.data.CandleData
import android.annotation.SuppressLint
import com.github.mikephil.charting.data.BarLineScatterCandleBubbleData
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
import android.graphics.Canvas
import android.util.AttributeSet
import com.github.mikephil.charting.charts.BarChart
import com.github.mikephil.charting.highlight.*
import com.github.mikephil.charting.utils.HorizontalViewPortHandler
import com.github.mikephil.charting.utils.TransformerHorizontalBarChart
import java.lang.RuntimeException

/**
 * View that represents a pie chart. Draws cake like slices.
 *
 * @author Philipp Jahoda
 */
class PieChart : PieRadarChartBase<PieData?> {
    /**
     * returns the circlebox, the boundingbox of the pie-chart slices
     *
     * @return
     */
    /**
     * rect object that represents the bounds of the piechart, needed for
     * drawing the circle
     */
    val circleBox: RectF? = RectF()
    /**
     * Returns true if drawing the entry labels is enabled, false if not.
     *
     * @return
     */
    /**
     * flag indicating if entry labels should be drawn or not
     */
    var isDrawEntryLabelsEnabled = true
        private set
    /**
     * returns an integer array of all the different angles the chart slices
     * have the angles in the returned array determine how much space (of 360°)
     * each slice takes
     *
     * @return
     */
    /**
     * array that holds the width of each pie-slice in degrees
     */
    var drawAngles = FloatArray(1)
        private set
    /**
     * returns the absolute angles of the different chart slices (where the
     * slices end)
     *
     * @return
     */
    /**
     * array that holds the absolute angle in degrees of each slice
     */
    var absoluteAngles = FloatArray(1)
        private set
    /**
     * returns true if the hole in the center of the pie-chart is set to be
     * visible, false if not
     *
     * @return
     */
    /**
     * set this to true to draw the pie center empty
     *
     * @param enabled
     */
    /**
     * if true, the white hole inside the chart will be drawn
     */
    var isDrawHoleEnabled = true
    /**
     * Returns true if the inner tips of the slices are visible behind the hole,
     * false if not.
     *
     * @return true if slices are visible behind the hole.
     */
    /**
     * if true, the hole will see-through to the inner tips of the slices
     */
    var isDrawSlicesUnderHoleEnabled = false
        private set
    /**
     * Returns true if using percentage values is enabled for the chart.
     *
     * @return
     */
    /**
     * if true, the values inside the piechart are drawn as percent values
     */
    var isUsePercentValuesEnabled = false
        private set
    /**
     * Returns true if the chart is set to draw each end of a pie-slice
     * "rounded".
     *
     * @return
     */
    /**
     * if true, the slices of the piechart are rounded
     */
    var isDrawRoundedSlicesEnabled = false
        private set

    /**
     * variable for the text that is drawn in the center of the pie-chart
     */
    private var mCenterText: CharSequence = ""
    private val mCenterTextOffset = MPPointF.getInstance(0, 0)
    /**
     * Returns the size of the hole radius in percent of the total radius.
     *
     * @return
     */
    /**
     * sets the radius of the hole in the center of the piechart in percent of
     * the maximum radius (max = the radius of the whole chart), default 50%
     *
     * @param percent
     */
    /**
     * indicates the size of the hole in the center of the piechart, default:
     * radius / 2
     */
    var holeRadius = 50f
    /**
     * sets the radius of the transparent circle that is drawn next to the hole
     * in the piechart in percent of the maximum radius (max = the radius of the
     * whole chart), default 55% -> means 5% larger than the center-hole by
     * default
     *
     * @param percent
     */
    /**
     * the radius of the transparent circle next to the chart-hole in the center
     */
    var transparentCircleRadius = 55f
    /**
     * returns true if drawing the center text is enabled
     *
     * @return
     */
    /**
     * if enabled, centertext is drawn
     */
    var isDrawCenterTextEnabled = true
        private set
    /**
     * the rectangular radius of the bounding box for the center text, as a percentage of the pie
     * hole
     * default 1.f (100%)
     */
    /**
     * the rectangular radius of the bounding box for the center text, as a percentage of the pie
     * hole
     * default 1.f (100%)
     */
    var centerTextRadiusPercent = 100f
    protected var mMaxAngle = 360f

    /**
     * Minimum angle to draw slices, this only works if there is enough room for all slices to have
     * the minimum angle, default 0f.
     */
    private var mMinAngleForSlices = 0f

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
        mRenderer = PieChartRenderer(this, mAnimator, mViewPortHandler)
        mXAxis = null
        mHighlighter = PieHighlighter(this)
    }

    override fun onDraw(canvas: Canvas) {
        super.onDraw(canvas)
        if (mData == null) return
        mRenderer.drawData(canvas)
        if (valuesToHighlight()) mRenderer.drawHighlighted(canvas, mIndicesToHighlight)
        mRenderer.drawExtras(canvas)
        mRenderer.drawValues(canvas)
        mLegendRenderer.renderLegend(canvas)
        drawDescription(canvas)
        drawMarkers(canvas)
    }

    override fun calculateOffsets() {
        super.calculateOffsets()

        // prevent nullpointer when no data set
        if (mData == null) return
        val diameter = diameter
        val radius = diameter / 2f
        val c: MPPointF = getCenterOffsets()
        val shift = mData!!.dataSet!!.selectionShift

        // create the circle box that will contain the pie-chart (the bounds of
        // the pie-chart)
        circleBox!![c.x - radius + shift, c.y - radius + shift, c.x + radius - shift] =
            c.y + radius - shift
        recycleInstance(c)
    }

    override fun calcMinMax() {
        calcAngles()
    }

    override fun getMarkerPosition(highlight: Highlight?): FloatArray {
        val center = centerCircleBox
        var r = radius
        var off = r / 10f * 3.6f
        if (isDrawHoleEnabled) {
            off = (r - r / 100f * holeRadius) / 2f
        }
        r -= off // offset to keep things inside the chart
        val rotationAngle = rotationAngle
        val entryIndex = highlight!!.x.toInt()

        // offset needed to center the drawn text in the slice
        val offset = drawAngles[entryIndex] / 2

        // calculate the text position
        val x = (r
                * Math.cos(
            Math.toRadians(
                ((rotationAngle + absoluteAngles[entryIndex] - offset)
                        * mAnimator.phaseY).toDouble()
            )
        ) + center.x).toFloat()
        val y = (r
                * Math.sin(
            Math.toRadians(
                ((rotationAngle + absoluteAngles[entryIndex] - offset)
                        * mAnimator.phaseY).toDouble()
            )
        ) + center.y).toFloat()
        recycleInstance(center)
        return floatArrayOf(x, y)
    }

    /**
     * calculates the needed angles for the chart slices
     */
    private fun calcAngles() {
        val entryCount: Int = mData.entryCount
        if (drawAngles.size != entryCount) {
            drawAngles = FloatArray(entryCount)
        } else {
            for (i in 0 until entryCount) {
                drawAngles[i] = 0
            }
        }
        if (absoluteAngles.size != entryCount) {
            absoluteAngles = FloatArray(entryCount)
        } else {
            for (i in 0 until entryCount) {
                absoluteAngles[i] = 0
            }
        }
        val yValueSum = mData!!.yValueSum
        val dataSets: List<IPieDataSet>? = mData!!.dataSets
        val hasMinAngle = mMinAngleForSlices != 0f && entryCount * mMinAngleForSlices <= mMaxAngle
        val minAngles = FloatArray(entryCount)
        var cnt = 0
        var offset = 0f
        var diff = 0f
        for (i in 0 until mData!!.dataSetCount) {
            val set = dataSets!![i]
            for (j in 0 until set.entryCount) {
                val drawAngle = calcAngle(Math.abs(set.getEntryForIndex(j)!!.y), yValueSum)
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
                drawAngles[cnt] = drawAngle
                if (cnt == 0) {
                    absoluteAngles[cnt] = drawAngles[cnt]
                } else {
                    absoluteAngles[cnt] = absoluteAngles[cnt - 1] + drawAngles[cnt]
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
                    absoluteAngles[0] = minAngles[0]
                } else {
                    absoluteAngles[i] = absoluteAngles[i - 1] + minAngles[i]
                }
            }
            drawAngles = minAngles
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
        for (i in mIndicesToHighlight.indices)  // check if the xvalue for the given dataset needs highlight
            if (mIndicesToHighlight[i].x as Int == index) return true
        return false
    }
    /**
     * calculates the needed angle for a given value
     *
     * @param value
     * @param yValueSum
     * @return
     */
    /**
     * calculates the needed angle for a given value
     *
     * @param value
     * @return
     */
    private fun calcAngle(value: Float, yValueSum: Float = mData!!.yValueSum): Float {
        return value / yValueSum * mMaxAngle
    }

    /**
     * This will throw an exception, PieChart has no XAxis object.
     *
     * @return
     */
    @get:Deprecated("")
    override val xAxis: XAxis?
        get() {
            throw RuntimeException("PieChart has no XAxis")
        }

    override fun getIndexForAngle(angle: Float): Int {

        // take the current angle of the chart into consideration
        val a = getNormalizedAngle(angle - rotationAngle)
        for (i in absoluteAngles.indices) {
            if (absoluteAngles[i] > a) return i
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
        val dataSets: List<IPieDataSet>? = mData!!.dataSets
        for (i in dataSets!!.indices) {
            if (dataSets[i].getEntryForXValue(xIndex.toFloat(), Float.NaN) != null) return i
        }
        return -1
    }

    /**
     * Sets the color for the hole that is drawn in the center of the PieChart
     * (if enabled).
     *
     * @param color
     */
    fun setHoleColor(color: Int) {
        (mRenderer as PieChartRenderer).paintHole.color = color
    }

    /**
     * Enable or disable the visibility of the inner tips of the slices behind the hole
     */
    fun setDrawSlicesUnderHole(enable: Boolean) {
        isDrawSlicesUnderHoleEnabled = enable
    }
    /**
     * returns the text that is drawn in the center of the pie-chart
     *
     * @return
     */
    /**
     * Sets the text String that is displayed in the center of the PieChart.
     *
     * @param text
     */
    var centerText: CharSequence?
        get() = mCenterText
        set(text) {
            mCenterText = text ?: ""
        }

    /**
     * set this to true to draw the text that is displayed in the center of the
     * pie chart
     *
     * @param enabled
     */
    fun setDrawCenterText(enabled: Boolean) {
        isDrawCenterTextEnabled = enabled
    }

    protected override val requiredLegendOffset: Float
        protected get() = mLegendRenderer.labelPaint.textSize * 2f
    protected override val requiredBaseOffset: Float
        protected get() = 0
    override val radius: Float
        get() = if (circleBox == null) 0 else Math.min(
            circleBox.width() / 2f,
            circleBox.height() / 2f
        )

    /**
     * returns the center of the circlebox
     *
     * @return
     */
    val centerCircleBox: MPPointF
        get() = MPPointF.getInstance(circleBox!!.centerX(), circleBox.centerY())

    /**
     * sets the typeface for the center-text paint
     *
     * @param t
     */
    fun setCenterTextTypeface(t: Typeface?) {
        (mRenderer as PieChartRenderer).paintCenterText.typeface = t
    }

    /**
     * Sets the size of the center text of the PieChart in dp.
     *
     * @param sizeDp
     */
    fun setCenterTextSize(sizeDp: Float) {
        (mRenderer as PieChartRenderer).paintCenterText.textSize = convertDpToPixel(sizeDp)
    }

    /**
     * Sets the size of the center text of the PieChart in pixels.
     *
     * @param sizePixels
     */
    fun setCenterTextSizePixels(sizePixels: Float) {
        (mRenderer as PieChartRenderer).paintCenterText.textSize = sizePixels
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
    val centerTextOffset: MPPointF
        get() = MPPointF.getInstance(mCenterTextOffset.x, mCenterTextOffset.y)

    /**
     * Sets the color of the center text of the PieChart.
     *
     * @param color
     */
    fun setCenterTextColor(color: Int) {
        (mRenderer as PieChartRenderer).paintCenterText.color = color
    }

    /**
     * Sets the color the transparent-circle should have.
     *
     * @param color
     */
    fun setTransparentCircleColor(color: Int) {
        val p = (mRenderer as PieChartRenderer).paintTransparentCircle
        val alpha = p.alpha
        p.color = color
        p.alpha = alpha
    }

    /**
     * Sets the amount of transparency the transparent circle should have 0 = fully transparent,
     * 255 = fully opaque.
     * Default value is 100.
     *
     * @param alpha 0-255
     */
    fun setTransparentCircleAlpha(alpha: Int) {
        (mRenderer as PieChartRenderer).paintTransparentCircle.alpha = alpha
    }

    /**
     * Set this to true to draw the entry labels into the pie slices (Provided by the getLabel() method of the PieEntry class).
     * Deprecated -> use setDrawEntryLabels(...) instead.
     *
     * @param enabled
     */
    @Deprecated("")
    fun setDrawSliceText(enabled: Boolean) {
        isDrawEntryLabelsEnabled = enabled
    }

    /**
     * Set this to true to draw the entry labels into the pie slices (Provided by the getLabel() method of the PieEntry class).
     *
     * @param enabled
     */
    fun setDrawEntryLabels(enabled: Boolean) {
        isDrawEntryLabelsEnabled = enabled
    }

    /**
     * Sets the color the entry labels are drawn with.
     *
     * @param color
     */
    fun setEntryLabelColor(color: Int) {
        (mRenderer as PieChartRenderer).paintEntryLabels.color = color
    }

    /**
     * Sets a custom Typeface for the drawing of the entry labels.
     *
     * @param tf
     */
    fun setEntryLabelTypeface(tf: Typeface?) {
        (mRenderer as PieChartRenderer).paintEntryLabels.typeface = tf
    }

    /**
     * Sets the size of the entry labels in dp. Default: 13dp
     *
     * @param size
     */
    fun setEntryLabelTextSize(size: Float) {
        (mRenderer as PieChartRenderer).paintEntryLabels.textSize =
            convertDpToPixel(size)
    }

    /**
     * Sets whether to draw slices in a curved fashion, only works if drawing the hole is enabled
     * and if the slices are not drawn under the hole.
     *
     * @param enabled draw curved ends of slices
     */
    fun setDrawRoundedSlices(enabled: Boolean) {
        isDrawRoundedSlicesEnabled = enabled
    }

    /**
     * If this is enabled, values inside the PieChart are drawn in percent and
     * not with their original value. Values provided for the IValueFormatter to
     * format are then provided in percent.
     *
     * @param enabled
     */
    fun setUsePercentValues(enabled: Boolean) {
        isUsePercentValuesEnabled = enabled
    }

    /**
     * Sets the max angle that is used for calculating the pie-circle. 360f means
     * it's a full PieChart, 180f results in a half-pie-chart. Default: 360f
     *
     * @param maxangle min 90, max 360
     */
    var maxAngle: Float
        get() = mMaxAngle
        set(maxangle) {
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
    /**
     * Set the angle to set minimum size for slices, you must call [.notifyDataSetChanged]
     * and [.invalidate] when changing this, only works if there is enough room for all
     * slices to have the minimum angle.
     *
     * @param minAngle minimum 0, maximum is half of [.setMaxAngle]
     */
    var minAngleForSlices: Float
        get() = mMinAngleForSlices
        set(minAngle) {
            var minAngle = minAngle
            if (minAngle > mMaxAngle / 2f) minAngle =
                mMaxAngle / 2f else if (minAngle < 0) minAngle = 0f
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