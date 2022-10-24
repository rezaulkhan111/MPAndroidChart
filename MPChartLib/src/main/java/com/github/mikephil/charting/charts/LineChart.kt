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
import com.github.mikephil.charting.data.BarData
import com.github.mikephil.charting.interfaces.dataprovider.BarDataProvider
import com.github.mikephil.charting.renderer.BarChartRenderer
import com.github.mikephil.charting.highlight.BarHighlighter
import com.github.mikephil.charting.components.YAxis
import com.github.mikephil.charting.data.BarEntry
import com.github.mikephil.charting.interfaces.datasets.IBarDataSet
import com.github.mikephil.charting.charts.PieRadarChartBase
import com.github.mikephil.charting.data.PieData
import com.github.mikephil.charting.renderer.PieChartRenderer
import com.github.mikephil.charting.highlight.PieHighlighter
import com.github.mikephil.charting.interfaces.datasets.IPieDataSet
import com.github.mikephil.charting.data.LineData
import com.github.mikephil.charting.interfaces.dataprovider.LineDataProvider
import com.github.mikephil.charting.renderer.LineChartRenderer
import com.github.mikephil.charting.data.RadarData
import com.github.mikephil.charting.components.YAxis.AxisDependency
import com.github.mikephil.charting.renderer.RadarChartRenderer
import com.github.mikephil.charting.highlight.RadarHighlighter
import com.github.mikephil.charting.data.BubbleData
import com.github.mikephil.charting.interfaces.dataprovider.BubbleDataProvider
import com.github.mikephil.charting.data.ScatterData
import com.github.mikephil.charting.interfaces.dataprovider.ScatterDataProvider
import com.github.mikephil.charting.charts.ScatterChart.ScatterShape
import com.github.mikephil.charting.data.CombinedData
import com.github.mikephil.charting.interfaces.dataprovider.CombinedDataProvider
import com.github.mikephil.charting.charts.CombinedChart.DrawOrder
import com.github.mikephil.charting.highlight.CombinedHighlighter
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
import android.util.AttributeSet
import com.github.mikephil.charting.charts.BarChart
import com.github.mikephil.charting.utils.HorizontalViewPortHandler
import com.github.mikephil.charting.utils.TransformerHorizontalBarChart
import com.github.mikephil.charting.highlight.HorizontalBarHighlighter

/**
 * Chart that draws lines, surfaces, circles, ...
 *
 * @author Philipp Jahoda
 */
class LineChart : BarLineChartBase<LineData?>, LineDataProvider {
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
        mRenderer = LineChartRenderer(this, mAnimator, mViewPortHandler)
    }

    override val lineData: LineData
        get() = mData!!

    override fun onDetachedFromWindow() {
        // releases the bitmap in the renderer to avoid oom error
        if (mRenderer != null && mRenderer is LineChartRenderer) {
            (mRenderer as LineChartRenderer).releaseBitmap()
        }
        super.onDetachedFromWindow()
    }
}