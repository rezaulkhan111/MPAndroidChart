package com.xxmassdeveloper.mpchartexample

import android.Manifest
import com.xxmassdeveloper.mpchartexample.notimportant.DemoBase.onCreate
import com.github.mikephil.charting.charts.Chart.description
import com.github.mikephil.charting.components.ComponentBase.isEnabled
import com.github.mikephil.charting.charts.Chart.setTouchEnabled
import com.github.mikephil.charting.charts.Chart.dragDecelerationFrictionCoef
import com.github.mikephil.charting.charts.BarLineChartBase.isDragEnabled
import com.github.mikephil.charting.charts.BarLineChartBase.setScaleEnabled
import com.github.mikephil.charting.charts.BarLineChartBase.setDrawGridBackground
import com.github.mikephil.charting.charts.BarLineChartBase.isHighlightPerDragEnabled
import com.github.mikephil.charting.charts.BarLineChartBase.setViewPortOffsets
import com.github.mikephil.charting.charts.Chart.legend
import com.github.mikephil.charting.charts.Chart.xAxis
import com.github.mikephil.charting.components.XAxis.position
import com.github.mikephil.charting.components.ComponentBase.typeface
import com.github.mikephil.charting.components.ComponentBase.textSize
import com.github.mikephil.charting.components.ComponentBase.textColor
import com.github.mikephil.charting.components.AxisBase.setDrawAxisLine
import com.github.mikephil.charting.components.AxisBase.setDrawGridLines
import com.github.mikephil.charting.components.AxisBase.setCenterAxisLabels
import com.github.mikephil.charting.components.AxisBase.granularity
import com.github.mikephil.charting.components.AxisBase.valueFormatter
import com.github.mikephil.charting.charts.BarLineChartBase.axisLeft
import com.github.mikephil.charting.components.YAxis.setPosition
import com.github.mikephil.charting.utils.ColorTemplate.holoBlue
import com.github.mikephil.charting.components.AxisBase.isGranularityEnabled
import com.github.mikephil.charting.components.AxisBase.axisMinimum
import com.github.mikephil.charting.components.AxisBase.axisMaximum
import com.github.mikephil.charting.components.ComponentBase.yOffset
import com.github.mikephil.charting.charts.BarLineChartBase.axisRight
import com.xxmassdeveloper.mpchartexample.notimportant.DemoBase.getRandom
import com.github.mikephil.charting.data.BaseDataSet.axisDependency
import com.github.mikephil.charting.data.BaseDataSet.color
import com.github.mikephil.charting.data.BaseDataSet.valueTextColor
import com.github.mikephil.charting.data.LineRadarDataSet.lineWidth
import com.github.mikephil.charting.data.LineDataSet.setDrawCircles
import com.github.mikephil.charting.data.BaseDataSet.setDrawValues
import com.github.mikephil.charting.data.LineRadarDataSet.fillAlpha
import com.github.mikephil.charting.data.LineRadarDataSet.fillColor
import com.github.mikephil.charting.data.BarLineScatterCandleBubbleDataSet.highLightColor
import com.github.mikephil.charting.data.LineDataSet.setDrawCircleHole
import com.github.mikephil.charting.data.ChartData.setValueTextColor
import com.github.mikephil.charting.data.ChartData.setValueTextSize
import com.github.mikephil.charting.charts.Chart.data
import com.github.mikephil.charting.data.ChartData.dataSets
import com.github.mikephil.charting.data.BaseDataSet.isDrawValuesEnabled
import com.github.mikephil.charting.data.ChartData.isHighlightEnabled
import com.github.mikephil.charting.data.LineRadarDataSet.isDrawFilledEnabled
import com.github.mikephil.charting.data.LineRadarDataSet.setDrawFilled
import com.github.mikephil.charting.data.LineDataSet.isDrawCirclesEnabled
import com.github.mikephil.charting.data.LineDataSet.mode
import com.github.mikephil.charting.charts.BarLineChartBase.isPinchZoomEnabled
import com.github.mikephil.charting.charts.BarLineChartBase.setPinchZoom
import com.github.mikephil.charting.charts.BarLineChartBase.isAutoScaleMinMaxEnabled
import com.github.mikephil.charting.charts.BarLineChartBase.notifyDataSetChanged
import com.github.mikephil.charting.charts.Chart.animateX
import com.github.mikephil.charting.charts.Chart.animateY
import com.github.mikephil.charting.charts.Chart.animateXY
import com.xxmassdeveloper.mpchartexample.notimportant.DemoBase.requestStoragePermission
import com.xxmassdeveloper.mpchartexample.notimportant.DemoBase.saveToGallery
import com.github.mikephil.charting.charts.Chart.setOnChartValueSelectedListener
import com.github.mikephil.charting.charts.BarChart.setDrawBarShadow
import com.github.mikephil.charting.charts.BarChart.setDrawValueAboveBar
import com.github.mikephil.charting.charts.BarLineChartBase.setMaxVisibleValueCount
import com.github.mikephil.charting.components.AxisBase.labelCount
import com.github.mikephil.charting.components.AxisBase.setLabelCount
import com.github.mikephil.charting.components.YAxis.spaceTop
import com.github.mikephil.charting.components.Legend.verticalAlignment
import com.github.mikephil.charting.components.Legend.horizontalAlignment
import com.github.mikephil.charting.components.Legend.orientation
import com.github.mikephil.charting.components.Legend.setDrawInside
import com.github.mikephil.charting.components.Legend.form
import com.github.mikephil.charting.components.Legend.formSize
import com.github.mikephil.charting.components.Legend.xEntrySpace
import com.github.mikephil.charting.components.MarkerView.setChartView
import com.github.mikephil.charting.charts.Chart.marker
import com.github.mikephil.charting.data.ChartData.dataSetCount
import com.github.mikephil.charting.data.ChartData.getDataSetByIndex
import com.github.mikephil.charting.data.DataSet.setValues
import com.github.mikephil.charting.data.ChartData.notifyDataChanged
import com.github.mikephil.charting.data.BaseDataSet.setDrawIcons
import com.github.mikephil.charting.data.BarDataSet.setFills
import com.github.mikephil.charting.data.ChartData.setValueTypeface
import com.github.mikephil.charting.data.BarData.barWidth
import com.github.mikephil.charting.interfaces.datasets.IDataSet.setDrawValues
import com.github.mikephil.charting.interfaces.datasets.IDataSet.isDrawValuesEnabled
import com.github.mikephil.charting.interfaces.datasets.IDataSet.setDrawIcons
import com.github.mikephil.charting.interfaces.datasets.IDataSet.isDrawIconsEnabled
import com.github.mikephil.charting.data.BarDataSet.barBorderWidth
import com.github.mikephil.charting.interfaces.datasets.IBarDataSet.barBorderWidth
import com.github.mikephil.charting.charts.BarChart.getBarBounds
import com.github.mikephil.charting.charts.BarLineChartBase.getPosition
import com.github.mikephil.charting.charts.BarLineChartBase.lowestVisibleX
import com.github.mikephil.charting.charts.BarLineChartBase.highestVisibleX
import com.github.mikephil.charting.utils.MPPointF.Companion.recycleInstance
import com.github.mikephil.charting.charts.PieChart.setUsePercentValues
import com.github.mikephil.charting.charts.Chart.setExtraOffsets
import com.github.mikephil.charting.charts.PieChart.setCenterTextTypeface
import com.github.mikephil.charting.charts.PieChart.centerText
import com.github.mikephil.charting.charts.PieChart.isDrawHoleEnabled
import com.github.mikephil.charting.charts.PieChart.setHoleColor
import com.github.mikephil.charting.charts.PieChart.setTransparentCircleColor
import com.github.mikephil.charting.charts.PieChart.setTransparentCircleAlpha
import com.github.mikephil.charting.charts.PieChart.holeRadius
import com.github.mikephil.charting.charts.PieChart.transparentCircleRadius
import com.github.mikephil.charting.charts.PieChart.setDrawCenterText
import com.github.mikephil.charting.charts.PieRadarChartBase.rotationAngle
import com.github.mikephil.charting.charts.PieRadarChartBase.isRotationEnabled
import com.github.mikephil.charting.charts.Chart.isHighlightPerTapEnabled
import com.github.mikephil.charting.components.Legend.yEntrySpace
import com.github.mikephil.charting.charts.PieChart.setEntryLabelColor
import com.github.mikephil.charting.charts.PieChart.setEntryLabelTypeface
import com.github.mikephil.charting.charts.PieChart.setEntryLabelTextSize
import com.github.mikephil.charting.data.PieDataSet.sliceSpace
import com.github.mikephil.charting.data.BaseDataSet.iconsOffset
import com.github.mikephil.charting.data.PieDataSet.selectionShift
import com.github.mikephil.charting.data.BaseDataSet.colors
import com.github.mikephil.charting.data.ChartData.setValueFormatter
import com.github.mikephil.charting.charts.Chart.highlightValues
import com.github.mikephil.charting.data.PieData.dataSets
import com.github.mikephil.charting.charts.PieChart.minAngleForSlices
import com.github.mikephil.charting.charts.PieRadarChartBase.notifyDataSetChanged
import com.github.mikephil.charting.charts.PieChart.isDrawRoundedSlicesEnabled
import com.github.mikephil.charting.charts.PieChart.setDrawRoundedSlices
import com.github.mikephil.charting.charts.PieChart.isDrawSlicesUnderHoleEnabled
import com.github.mikephil.charting.charts.PieChart.setDrawSlicesUnderHole
import com.github.mikephil.charting.charts.PieChart.isDrawCenterTextEnabled
import com.github.mikephil.charting.charts.PieChart.setDrawEntryLabels
import com.github.mikephil.charting.charts.PieChart.isDrawEntryLabelsEnabled
import com.github.mikephil.charting.charts.PieChart.isUsePercentValuesEnabled
import com.github.mikephil.charting.charts.PieRadarChartBase.spin
import com.github.mikephil.charting.data.BaseEntry.y
import com.github.mikephil.charting.highlight.Highlight.x
import com.github.mikephil.charting.highlight.Highlight.dataSetIndex
import com.github.mikephil.charting.charts.BarLineChartBase.setOnDrawListener
import com.github.mikephil.charting.components.XAxis.setAvoidFirstLastClipping
import com.github.mikephil.charting.data.LineDataSet.circleRadius
import com.github.mikephil.charting.data.Entry.x
import com.github.mikephil.charting.data.Entry.toString
import com.github.mikephil.charting.data.DataSet.toSimpleString
import com.github.mikephil.charting.charts.Chart.legendRenderer
import com.github.mikephil.charting.charts.BarLineChartBase.setGridBackgroundColor
import com.github.mikephil.charting.charts.BarLineChartBase.setDrawBorders
import com.github.mikephil.charting.components.YAxis.setDrawZeroLine
import com.github.mikephil.charting.data.LineDataSet.setFillFormatter
import com.github.mikephil.charting.data.ChartData.setDrawValues
import com.github.mikephil.charting.components.AxisBase.enableGridDashedLine
import com.github.mikephil.charting.components.LimitLine.lineWidth
import com.github.mikephil.charting.components.LimitLine.enableDashedLine
import com.github.mikephil.charting.components.LimitLine.labelPosition
import com.github.mikephil.charting.components.AxisBase.setDrawLimitLinesBehindData
import com.github.mikephil.charting.components.AxisBase.addLimitLine
import com.github.mikephil.charting.data.BaseDataSet.notifyDataSetChanged
import com.github.mikephil.charting.data.LineDataSet.enableDashedLine
import com.github.mikephil.charting.data.LineDataSet.setCircleColor
import com.github.mikephil.charting.data.BaseDataSet.formLineWidth
import com.github.mikephil.charting.data.BaseDataSet.formLineDashEffect
import com.github.mikephil.charting.data.BaseDataSet.formSize
import com.github.mikephil.charting.data.BaseDataSet.valueTextSize
import com.github.mikephil.charting.data.LineScatterCandleRadarDataSet.enableDashedHighlightLine
import com.github.mikephil.charting.utils.Utils.sDKInt
import com.github.mikephil.charting.data.LineRadarDataSet.fillDrawable
import com.github.mikephil.charting.data.BaseDataSet.isDrawIconsEnabled
import com.github.mikephil.charting.charts.Chart.xChartMin
import com.github.mikephil.charting.charts.Chart.xChartMax
import com.github.mikephil.charting.charts.BarLineChartBase.yChartMin
import com.github.mikephil.charting.charts.BarLineChartBase.yChartMax
import com.github.mikephil.charting.utils.ColorTemplate.colorWithAlpha
import com.github.mikephil.charting.charts.BarLineChartBase.centerViewToAnimated
import com.github.mikephil.charting.interfaces.datasets.IDataSet.axisDependency
import com.github.mikephil.charting.charts.RadarChart.webLineWidth
import com.github.mikephil.charting.charts.RadarChart.webColor
import com.github.mikephil.charting.charts.RadarChart.webLineWidthInner
import com.github.mikephil.charting.charts.RadarChart.webColorInner
import com.github.mikephil.charting.charts.RadarChart.webAlpha
import com.github.mikephil.charting.components.ComponentBase.xOffset
import com.github.mikephil.charting.charts.RadarChart.yAxis
import com.github.mikephil.charting.components.AxisBase.setDrawLabels
import com.github.mikephil.charting.data.RadarDataSet.setDrawHighlightCircleEnabled
import com.github.mikephil.charting.data.LineScatterCandleRadarDataSet.setDrawHighlightIndicators
import com.github.mikephil.charting.interfaces.datasets.ILineRadarDataSet.isDrawFilledEnabled
import com.github.mikephil.charting.interfaces.datasets.ILineRadarDataSet.setDrawFilled
import com.github.mikephil.charting.interfaces.datasets.IRadarDataSet.isDrawHighlightCircleEnabled
import com.github.mikephil.charting.charts.RadarChart.notifyDataSetChanged
import com.github.mikephil.charting.charts.BarChart.setFitBars
import com.github.mikephil.charting.data.BaseDataSet.setColors
import com.github.mikephil.charting.components.YAxis.spaceBottom
import com.github.mikephil.charting.data.BaseDataSet.setColor
import com.github.mikephil.charting.data.BubbleData.setHighlightCircleWidth
import com.github.mikephil.charting.charts.PieChart.maxAngle
import com.github.mikephil.charting.charts.PieChart.setCenterTextOffset
import com.github.mikephil.charting.charts.BarLineChartBase.resetTracking
import com.github.mikephil.charting.charts.Chart.maxHighlightDistance
import com.github.mikephil.charting.data.ScatterDataSet.setScatterShape
import com.github.mikephil.charting.data.ScatterDataSet.setScatterShapeHoleColor
import com.github.mikephil.charting.data.ScatterDataSet.setScatterShapeHoleRadius
import com.github.mikephil.charting.data.ScatterDataSet.setShapeRenderer
import com.github.mikephil.charting.data.ScatterDataSet.setScatterShapeSize
import com.github.mikephil.charting.utils.FileUtils.loadBarEntriesFromAssets
import com.github.mikephil.charting.charts.CombinedChart.setDrawBarShadow
import com.github.mikephil.charting.charts.CombinedChart.isHighlightFullBarEnabled
import com.github.mikephil.charting.charts.CombinedChart.drawOrder
import com.github.mikephil.charting.components.Legend.isWordWrapEnabled
import com.github.mikephil.charting.data.CombinedData.setData
import com.github.mikephil.charting.data.ChartData.xMax
import com.github.mikephil.charting.charts.CombinedChart.data
import com.github.mikephil.charting.data.ChartData.addDataSet
import com.github.mikephil.charting.data.BarDataSet.stackLabels
import com.github.mikephil.charting.data.BarData.groupBars
import com.github.mikephil.charting.data.CandleDataSet.setDecreasingColor
import com.github.mikephil.charting.data.CandleDataSet.setShadowColor
import com.github.mikephil.charting.data.CandleDataSet.setBarSpace
import com.github.mikephil.charting.data.BubbleDataSet.setHighlightCircleWidth
import com.github.mikephil.charting.components.AxisBase.axisLineColor
import com.github.mikephil.charting.data.LineDataSet.cubicIntensity
import com.github.mikephil.charting.data.LineScatterCandleRadarDataSet.setDrawHorizontalHighlightIndicator
import com.github.mikephil.charting.data.LineDataSet.setCircleColors
import com.github.mikephil.charting.charts.Chart.setNoDataText
import com.github.mikephil.charting.data.ChartData.addEntry
import com.github.mikephil.charting.interfaces.datasets.IDataSet.entryCount
import com.github.mikephil.charting.charts.BarLineChartBase.setVisibleXRangeMaximum
import com.github.mikephil.charting.charts.BarLineChartBase.moveViewTo
import com.github.mikephil.charting.interfaces.datasets.IDataSet.getEntryForXValue
import com.github.mikephil.charting.data.ChartData.removeEntry
import com.github.mikephil.charting.data.ChartData.removeDataSet
import com.github.mikephil.charting.charts.Chart.clear
import com.github.mikephil.charting.charts.Chart.extraTopOffset
import com.github.mikephil.charting.charts.Chart.extraBottomOffset
import com.github.mikephil.charting.charts.Chart.extraLeftOffset
import com.github.mikephil.charting.charts.Chart.extraRightOffset
import com.github.mikephil.charting.components.YAxis.zeroLineColor
import com.github.mikephil.charting.components.YAxis.zeroLineWidth
import com.github.mikephil.charting.data.BaseDataSet.setValueTextColors
import com.github.mikephil.charting.data.CandleDataSet.setShadowWidth
import com.github.mikephil.charting.data.CandleDataSet.setDecreasingPaintStyle
import com.github.mikephil.charting.data.CandleDataSet.setIncreasingColor
import com.github.mikephil.charting.data.CandleDataSet.setIncreasingPaintStyle
import com.github.mikephil.charting.data.CandleDataSet.setNeutralColor
import com.github.mikephil.charting.data.CandleDataSet.setShadowColorSameAsCandle
import com.github.mikephil.charting.interfaces.datasets.ICandleDataSet.shadowColorSameAsCandle
import com.github.mikephil.charting.data.LineDataSet.setCircleHoleColor
import com.github.mikephil.charting.data.LineDataSet.circleHoleRadius
import com.github.mikephil.charting.data.BarDataSet.barShadowColor
import com.github.mikephil.charting.data.PieDataSet.valueLinePart1OffsetPercentage
import com.github.mikephil.charting.data.PieDataSet.valueLinePart1Length
import com.github.mikephil.charting.data.PieDataSet.valueLinePart2Length
import com.github.mikephil.charting.data.PieDataSet.yValuePosition
import com.github.mikephil.charting.components.YAxis.isInverted
import com.github.mikephil.charting.charts.BarLineChartBase.moveViewToX
import com.github.mikephil.charting.charts.Chart.clearValues
import com.xxmassdeveloper.mpchartexample.listviewitems.ChartItem.getView
import com.xxmassdeveloper.mpchartexample.listviewitems.ChartItem.itemType
import com.github.mikephil.charting.data.BarDataSet.highLightAlpha
import com.github.mikephil.charting.charts.BarChart.barData
import com.github.mikephil.charting.data.BarData.getGroupWidth
import com.github.mikephil.charting.charts.BarChart.groupBars
import com.xxmassdeveloper.mpchartexample.notimportant.DemoBase
import android.widget.SeekBar.OnSeekBarChangeListener
import com.github.mikephil.charting.charts.LineChart
import android.widget.SeekBar
import android.widget.TextView
import android.os.Bundle
import com.xxmassdeveloper.mpchartexample.R
import com.github.mikephil.charting.components.Legend
import com.github.mikephil.charting.components.XAxis
import com.github.mikephil.charting.formatter.IAxisValueFormatter
import com.github.mikephil.charting.components.AxisBase
import com.github.mikephil.charting.components.YAxis
import com.github.mikephil.charting.utils.ColorTemplate
import com.github.mikephil.charting.components.YAxis.AxisDependency
import android.content.Intent
import com.github.mikephil.charting.interfaces.datasets.ILineDataSet
import androidx.core.content.ContextCompat
import android.content.pm.PackageManager
import com.github.mikephil.charting.listener.OnChartValueSelectedListener
import com.github.mikephil.charting.charts.BarChart
import com.xxmassdeveloper.mpchartexample.custom.DayAxisValueFormatter
import com.github.mikephil.charting.components.XAxis.XAxisPosition
import com.xxmassdeveloper.mpchartexample.custom.MyAxisValueFormatter
import com.github.mikephil.charting.components.YAxis.YAxisLabelPosition
import com.github.mikephil.charting.components.Legend.LegendForm
import com.xxmassdeveloper.mpchartexample.custom.XYMarkerView
import com.github.mikephil.charting.utils.Fill
import com.github.mikephil.charting.interfaces.datasets.IBarDataSet
import com.github.mikephil.charting.interfaces.datasets.IDataSet
import android.graphics.RectF
import com.github.mikephil.charting.utils.MPPointF
import com.github.mikephil.charting.charts.PieChart
import com.github.mikephil.charting.formatter.PercentFormatter
import android.text.SpannableString
import android.text.style.RelativeSizeSpan
import android.text.style.StyleSpan
import android.graphics.Typeface
import android.text.style.ForegroundColorSpan
import com.github.mikephil.charting.charts.Chart
import com.github.mikephil.charting.formatter.IFillFormatter
import com.github.mikephil.charting.interfaces.dataprovider.LineDataProvider
import com.xxmassdeveloper.mpchartexample.custom.MyMarkerView
import com.github.mikephil.charting.components.LimitLine
import com.github.mikephil.charting.components.LimitLine.LimitLabelPosition
import android.graphics.DashPathEffect
import android.graphics.drawable.Drawable
import com.github.mikephil.charting.charts.RadarChart
import com.github.mikephil.charting.components.MarkerView
import com.xxmassdeveloper.mpchartexample.custom.RadarMarkerView
import com.github.mikephil.charting.interfaces.datasets.IRadarDataSet
import com.github.mikephil.charting.charts.BubbleChart
import com.github.mikephil.charting.interfaces.datasets.IBubbleDataSet
import android.util.DisplayMetrics
import android.widget.RelativeLayout
import com.github.mikephil.charting.charts.ScatterChart
import com.xxmassdeveloper.mpchartexample.custom.CustomScatterShapeRenderer
import com.github.mikephil.charting.interfaces.datasets.IScatterDataSet
import com.github.mikephil.charting.charts.CombinedChart
import com.github.mikephil.charting.charts.CombinedChart.DrawOrder
import com.github.mikephil.charting.listener.OnChartGestureListener
import com.github.mikephil.charting.listener.ChartTouchListener.ChartGesture
import com.github.mikephil.charting.listener.ChartTouchListener
import android.widget.Toast
import com.github.mikephil.charting.formatter.IValueFormatter
import com.github.mikephil.charting.utils.ViewPortHandler
import com.github.mikephil.charting.charts.CandleStickChart
import com.github.mikephil.charting.interfaces.datasets.ICandleDataSet
import android.widget.ArrayAdapter
import android.annotation.SuppressLint
import android.graphics.Color
import android.net.Uri
import android.util.Log
import android.view.*
import com.github.mikephil.charting.utils.EntryXComparator
import com.github.mikephil.charting.charts.HorizontalBarChart
import com.github.mikephil.charting.data.*
import com.xxmassdeveloper.mpchartexample.listviewitems.ChartItem
import com.xxmassdeveloper.mpchartexample.listviewitems.LineChartItem
import com.xxmassdeveloper.mpchartexample.listviewitems.BarChartItem
import com.xxmassdeveloper.mpchartexample.listviewitems.PieChartItem
import com.github.mikephil.charting.formatter.LargeValueFormatter
import com.github.mikephil.charting.highlight.Highlight
import java.util.*

class BarChartActivityMultiDataset : DemoBase(), OnSeekBarChangeListener,
    OnChartValueSelectedListener {
    private var chart: BarChart? = null
    private var seekBarX: SeekBar? = null
    private var seekBarY: SeekBar? = null
    private var tvX: TextView? = null
    private var tvY: TextView? = null
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        window.setFlags(
            WindowManager.LayoutParams.FLAG_FULLSCREEN,
            WindowManager.LayoutParams.FLAG_FULLSCREEN
        )
        setContentView(R.layout.activity_barchart)
        title = "BarChartActivityMultiDataset"
        tvX = findViewById(R.id.tvXMax)
        tvX.setTextSize(10f)
        tvY = findViewById(R.id.tvYMax)
        seekBarX = findViewById(R.id.seekBar1)
        seekBarX.setMax(50)
        seekBarX.setOnSeekBarChangeListener(this)
        seekBarY = findViewById(R.id.seekBar2)
        seekBarY.setOnSeekBarChangeListener(this)
        chart = findViewById(R.id.chart1)
        chart.setOnChartValueSelectedListener(this)
        chart.description!!.isEnabled = false

//        chart.setDrawBorders(true);

        // scaling can now only be done on x- and y-axis separately
        chart.setPinchZoom(false)
        chart.setDrawBarShadow(false)
        chart.setDrawGridBackground(false)

        // create a custom MarkerView (extend MarkerView) and specify the layout
        // to use for it
        val mv = MyMarkerView(this, R.layout.custom_marker_view)
        mv.setChartView(chart) // For bounds control
        chart.marker = mv // Set the marker to the chart
        seekBarX.setProgress(10)
        seekBarY.setProgress(100)
        val l: Legend? = chart.legend
        l!!.verticalAlignment = Legend.LegendVerticalAlignment.TOP
        l.horizontalAlignment = Legend.LegendHorizontalAlignment.RIGHT
        l.orientation = Legend.LegendOrientation.VERTICAL
        l.setDrawInside(true)
        l.typeface = tfLight
        l.yOffset = 0f
        l.xOffset = 10f
        l.yEntrySpace = 0f
        l.textSize = 8f
        val xAxis: XAxis? = chart.xAxis
        xAxis!!.typeface = tfLight
        xAxis.granularity = 1f
        xAxis.setCenterAxisLabels(true)
        xAxis.valueFormatter = object : IAxisValueFormatter {
            override fun getFormattedValue(value: Float, axis: AxisBase?): String {
                return value.toInt().toString()
            }
        }
        val leftAxis = chart.axisLeft
        leftAxis!!.typeface = tfLight
        leftAxis.valueFormatter = LargeValueFormatter()
        leftAxis.setDrawGridLines(false)
        leftAxis.spaceTop = 35f
        leftAxis.axisMinimum = 0f // this replaces setStartAtZero(true)
        chart.axisRight!!.isEnabled = false
    }

    override fun onProgressChanged(seekBar: SeekBar, progress: Int, fromUser: Boolean) {
        val groupSpace = 0.08f
        val barSpace = 0.03f // x4 DataSet
        val barWidth = 0.2f // x4 DataSet
        // (0.2 + 0.03) * 4 + 0.08 = 1.00 -> interval per "group"
        val groupCount = seekBarX!!.progress + 1
        val startYear = 1980
        val endYear = startYear + groupCount
        tvX!!.text = String.format(Locale.ENGLISH, "%d-%d", startYear, endYear)
        tvY!!.text = seekBarY!!.progress.toString()
        val values1 = ArrayList<BarEntry?>()
        val values2 = ArrayList<BarEntry?>()
        val values3 = ArrayList<BarEntry?>()
        val values4 = ArrayList<BarEntry?>()
        val randomMultiplier = seekBarY!!.progress * 100000f
        for (i in startYear until endYear) {
            values1.add(BarEntry(i, (Math.random() * randomMultiplier).toFloat()))
            values2.add(BarEntry(i, (Math.random() * randomMultiplier).toFloat()))
            values3.add(BarEntry(i, (Math.random() * randomMultiplier).toFloat()))
            values4.add(BarEntry(i, (Math.random() * randomMultiplier).toFloat()))
        }
        val set1: BarDataSet?
        val set2: BarDataSet?
        val set3: BarDataSet?
        val set4: BarDataSet?
        if (chart!!.data != null && chart!!.data!!.dataSetCount > 0) {
            set1 = chart!!.data!!.getDataSetByIndex(0) as BarDataSet?
            set2 = chart!!.data!!.getDataSetByIndex(1) as BarDataSet?
            set3 = chart!!.data!!.getDataSetByIndex(2) as BarDataSet?
            set4 = chart!!.data!!.getDataSetByIndex(3) as BarDataSet?
            set1!!.setValues(values1)
            set2!!.setValues(values2)
            set3!!.setValues(values3)
            set4!!.setValues(values4)
            chart!!.data!!.notifyDataChanged()
            chart!!.notifyDataSetChanged()
        } else {
            // create 4 DataSets
            set1 = BarDataSet(values1, "Company A")
            set1.color = Color.rgb(104, 241, 175)
            set2 = BarDataSet(values2, "Company B")
            set2.color = Color.rgb(164, 228, 251)
            set3 = BarDataSet(values3, "Company C")
            set3.color = Color.rgb(242, 247, 158)
            set4 = BarDataSet(values4, "Company D")
            set4.color = Color.rgb(255, 102, 0)
            val data = BarData(set1, set2, set3, set4)
            data.setValueFormatter(LargeValueFormatter())
            data.setValueTypeface(tfLight)
            chart!!.data = data
        }

        // specify the width each bar should have
        chart!!.barData!!.barWidth = barWidth

        // restrict the x-axis range
        chart!!.xAxis.setAxisMinimum(startYear)

        // barData.getGroupWith(...) is a helper that calculates the width each group needs based on the provided parameters
        chart!!.xAxis.setAxisMaximum(
            startYear + chart!!.barData!!.getGroupWidth(
                groupSpace,
                barSpace
            ) * groupCount
        )
        chart!!.groupBars(startYear.toFloat(), groupSpace, barSpace)
        chart.invalidate()
    }

    override fun onCreateOptionsMenu(menu: Menu): Boolean {
        menuInflater.inflate(R.menu.bar, menu)
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when (item.itemId) {
            R.id.viewGithub -> {
                val i = Intent(Intent.ACTION_VIEW)
                i.data =
                    Uri.parse("https://github.com/PhilJay/MPAndroidChart/blob/master/MPChartExample/src/com/xxmassdeveloper/mpchartexample/BarChartActivityMultiDataset.java")
                startActivity(i)
            }
            R.id.actionToggleValues -> {
                for (set in chart!!.data!!.dataSets!!) set!!.setDrawValues(!set.isDrawValuesEnabled)
                chart.invalidate()
            }
            R.id.actionTogglePinch -> {
                if (chart!!.isPinchZoomEnabled) chart!!.setPinchZoom(false) else chart!!.setPinchZoom(
                    true
                )
                chart.invalidate()
            }
            R.id.actionToggleAutoScaleMinMax -> {
                chart!!.isAutoScaleMinMaxEnabled = !chart!!.isAutoScaleMinMaxEnabled
                chart!!.notifyDataSetChanged()
            }
            R.id.actionToggleBarBorders -> {
                for (set in chart!!.data!!.dataSets!!) (set as BarDataSet?)!!.barBorderWidth =
                    if (set!!.barBorderWidth == 1f) 0f else 1f
                chart.invalidate()
            }
            R.id.actionToggleHighlight -> {
                if (chart!!.data != null) {
                    chart!!.data!!.isHighlightEnabled = !chart!!.data!!.isHighlightEnabled
                    chart.invalidate()
                }
            }
            R.id.actionSave -> {
                if (ContextCompat.checkSelfPermission(
                        this,
                        Manifest.permission.WRITE_EXTERNAL_STORAGE
                    ) == PackageManager.PERMISSION_GRANTED
                ) {
                    saveToGallery()
                } else {
                    requestStoragePermission(chart)
                }
            }
            R.id.animateX -> {
                chart!!.animateX(2000)
            }
            R.id.animateY -> {
                chart!!.animateY(2000)
            }
            R.id.animateXY -> {
                chart!!.animateXY(2000, 2000)
            }
        }
        return true
    }

    override fun saveToGallery() {
        saveToGallery(chart!!, "BarChartActivityMultiDataset")
    }

    override fun onStartTrackingTouch(seekBar: SeekBar) {}
    override fun onStopTrackingTouch(seekBar: SeekBar) {}
    override fun onValueSelected(e: Entry?, h: Highlight?) {
        Log.i("Activity", "Selected: " + e.toString() + ", dataSet: " + h!!.dataSetIndex)
    }

    override fun onNothingSelected() {
        Log.i("Activity", "Nothing selected.")
    }
}