package com.xxmassdeveloper.mpchartexample.fragments

import com.github.mikephil.charting.charts.Chart.description
import com.github.mikephil.charting.components.ComponentBase.isEnabled
import com.github.mikephil.charting.charts.Chart.onChartGestureListener
import com.github.mikephil.charting.components.MarkerView.setChartView
import com.github.mikephil.charting.charts.Chart.marker
import com.github.mikephil.charting.charts.BarLineChartBase.setDrawGridBackground
import com.github.mikephil.charting.charts.BarChart.setDrawBarShadow
import com.github.mikephil.charting.charts.Chart.data
import com.github.mikephil.charting.charts.Chart.legend
import com.github.mikephil.charting.components.ComponentBase.typeface
import com.github.mikephil.charting.charts.BarLineChartBase.axisLeft
import com.github.mikephil.charting.components.AxisBase.axisMinimum
import com.github.mikephil.charting.charts.BarLineChartBase.axisRight
import com.github.mikephil.charting.charts.Chart.xAxis
import com.github.mikephil.charting.charts.Chart.highlightValues
import com.github.mikephil.charting.charts.PieChart.setCenterTextTypeface
import com.github.mikephil.charting.charts.PieChart.centerText
import com.github.mikephil.charting.charts.PieChart.setCenterTextSize
import com.github.mikephil.charting.charts.PieChart.holeRadius
import com.github.mikephil.charting.charts.PieChart.transparentCircleRadius
import com.github.mikephil.charting.components.Legend.verticalAlignment
import com.github.mikephil.charting.components.Legend.horizontalAlignment
import com.github.mikephil.charting.components.Legend.orientation
import com.github.mikephil.charting.components.Legend.setDrawInside
import com.github.mikephil.charting.data.BaseDataSet.setColors
import com.github.mikephil.charting.data.ChartData.setValueTypeface
import com.github.mikephil.charting.charts.ScatterChart.ScatterShape.Companion.allDefaultShapes
import com.github.mikephil.charting.data.ScatterDataSet.setScatterShapeSize
import com.github.mikephil.charting.data.ScatterDataSet.setScatterShape
import com.github.mikephil.charting.data.PieDataSet.sliceSpace
import com.github.mikephil.charting.data.BaseDataSet.valueTextColor
import com.github.mikephil.charting.data.BaseDataSet.valueTextSize
import com.github.mikephil.charting.utils.FileUtils.loadEntriesFromAssets
import com.github.mikephil.charting.data.LineRadarDataSet.lineWidth
import com.github.mikephil.charting.data.LineDataSet.setDrawCircles
import com.github.mikephil.charting.data.BaseDataSet.color
import com.github.mikephil.charting.data.LineDataSet.setCircleColor
import com.github.mikephil.charting.data.LineDataSet.circleRadius
import com.github.mikephil.charting.components.XAxis.position
import com.github.mikephil.charting.components.AxisBase.setDrawGridLines
import com.github.mikephil.charting.components.Legend.isWordWrapEnabled
import com.github.mikephil.charting.components.Legend.formSize
import com.github.mikephil.charting.components.ComponentBase.textSize
import com.github.mikephil.charting.components.ComponentBase.yOffset
import com.github.mikephil.charting.charts.Chart.extraBottomOffset
import com.github.mikephil.charting.charts.Chart.animateX
import com.github.mikephil.charting.components.AxisBase.axisMaximum
import com.xxmassdeveloper.mpchartexample.fragments.SimpleFragment
import com.github.mikephil.charting.listener.OnChartGestureListener
import com.github.mikephil.charting.charts.BarChart
import android.os.Bundle
import com.xxmassdeveloper.mpchartexample.R
import com.xxmassdeveloper.mpchartexample.custom.MyMarkerView
import android.graphics.Typeface
import com.github.mikephil.charting.components.Legend
import com.github.mikephil.charting.components.YAxis
import com.github.mikephil.charting.components.XAxis
import android.widget.FrameLayout
import com.github.mikephil.charting.listener.ChartTouchListener.ChartGesture
import com.xxmassdeveloper.mpchartexample.fragments.BarChartFrag
import com.github.mikephil.charting.charts.PieChart
import android.text.SpannableString
import android.text.style.RelativeSizeSpan
import android.text.style.ForegroundColorSpan
import com.xxmassdeveloper.mpchartexample.fragments.PieChartFrag
import com.github.mikephil.charting.data.BarData
import com.github.mikephil.charting.interfaces.datasets.IBarDataSet
import com.github.mikephil.charting.data.BarEntry
import com.github.mikephil.charting.data.BarDataSet
import com.github.mikephil.charting.utils.ColorTemplate
import com.github.mikephil.charting.data.ScatterData
import com.github.mikephil.charting.interfaces.datasets.IScatterDataSet
import com.github.mikephil.charting.charts.ScatterChart.ScatterShape
import com.github.mikephil.charting.charts.ScatterChart
import com.github.mikephil.charting.data.ScatterDataSet
import com.github.mikephil.charting.data.PieData
import com.github.mikephil.charting.data.PieEntry
import com.github.mikephil.charting.data.PieDataSet
import com.github.mikephil.charting.data.LineData
import com.github.mikephil.charting.interfaces.datasets.ILineDataSet
import com.github.mikephil.charting.data.LineDataSet
import com.xxmassdeveloper.mpchartexample.notimportant.DemoBase
import androidx.viewpager.widget.ViewPager
import com.xxmassdeveloper.mpchartexample.fragments.SimpleChartDemo.PageAdapter
import android.content.DialogInterface
import androidx.fragment.app.FragmentPagerAdapter
import com.xxmassdeveloper.mpchartexample.fragments.SineCosineFragment
import com.xxmassdeveloper.mpchartexample.fragments.ComplexityFragment
import com.xxmassdeveloper.mpchartexample.fragments.ScatterChartFrag
import android.content.Intent
import android.view.*
import androidx.fragment.app.Fragment
import com.github.mikephil.charting.components.XAxis.XAxisPosition
import com.github.mikephil.charting.charts.LineChart

class ScatterChartFrag : SimpleFragment() {
    private var chart: ScatterChart? = null
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val v = inflater.inflate(R.layout.frag_simple_scatter, container, false)
        chart = v.findViewById(R.id.scatterChart1)
        chart.description!!.isEnabled = false
        val tf = Typeface.createFromAsset(context!!.assets, "OpenSans-Light.ttf")
        val mv = MyMarkerView(activity, R.layout.custom_marker_view)
        mv.setChartView(chart) // For bounds control
        chart.marker = mv
        chart.setDrawGridBackground(false)
        chart.data = generateScatterData(6, 10000f, 200)
        val xAxis: XAxis? = chart.xAxis
        xAxis!!.isEnabled = true
        xAxis.position = XAxisPosition.BOTTOM
        val leftAxis = chart.axisLeft
        leftAxis!!.typeface = tf
        val rightAxis = chart.axisRight
        rightAxis!!.typeface = tf
        rightAxis.setDrawGridLines(false)
        val l: Legend? = chart.legend
        l!!.isWordWrapEnabled = true
        l.typeface = tf
        l.formSize = 14f
        l.textSize = 9f

        // increase the space between legend & bottom and legend & content
        l.yOffset = 13f
        chart.extraBottomOffset = 16f
        return v
    }

    companion object {
        fun newInstance(): Fragment {
            return ScatterChartFrag()
        }
    }
}