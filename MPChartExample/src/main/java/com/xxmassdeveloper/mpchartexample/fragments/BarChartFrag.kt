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
import android.util.Log
import android.view.*
import androidx.fragment.app.Fragment
import com.github.mikephil.charting.components.XAxis.XAxisPosition
import com.github.mikephil.charting.charts.LineChart

class BarChartFrag : SimpleFragment(), OnChartGestureListener {
    private var chart: BarChart? = null
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val v = inflater.inflate(R.layout.frag_simple_bar, container, false)

        // create a new chart object
        chart = BarChart(activity)
        chart!!.description!!.isEnabled = false
        chart!!.onChartGestureListener = this
        val mv = MyMarkerView(activity, R.layout.custom_marker_view)
        mv.setChartView(chart!!) // For bounds control
        chart!!.marker = mv
        chart!!.setDrawGridBackground(false)
        chart!!.setDrawBarShadow(false)
        val tf = Typeface.createFromAsset(context!!.assets, "OpenSans-Light.ttf")
        chart!!.data = generateBarData(1, 20000f, 12)
        val l: Legend? = chart!!.legend
        l!!.typeface = tf
        val leftAxis = chart!!.axisLeft
        leftAxis!!.typeface = tf
        leftAxis.axisMinimum = 0f // this replaces setStartAtZero(true)
        chart!!.axisRight!!.isEnabled = false
        val xAxis: XAxis? = chart!!.xAxis
        xAxis!!.isEnabled = false

        // programmatically add the chart
        val parent = v.findViewById<FrameLayout>(R.id.parentLayout)
        parent.addView(chart)
        return v
    }

    override fun onChartGestureStart(me: MotionEvent?, lastPerformedGesture: ChartGesture?) {
        Log.i("Gesture", "START")
    }

    override fun onChartGestureEnd(me: MotionEvent?, lastPerformedGesture: ChartGesture?) {
        Log.i("Gesture", "END")
        chart!!.highlightValues(null)
    }

    override fun onChartLongPressed(me: MotionEvent?) {
        Log.i("LongPress", "Chart long pressed.")
    }

    override fun onChartDoubleTapped(me: MotionEvent?) {
        Log.i("DoubleTap", "Chart double-tapped.")
    }

    override fun onChartSingleTapped(me: MotionEvent?) {
        Log.i("SingleTap", "Chart single-tapped.")
    }

    override fun onChartFling(
        me1: MotionEvent?,
        me2: MotionEvent?,
        velocityX: Float,
        velocityY: Float
    ) {
        Log.i("Fling", "Chart fling. VelocityX: $velocityX, VelocityY: $velocityY")
    }

    override fun onChartScale(me: MotionEvent?, scaleX: Float, scaleY: Float) {
        Log.i("Scale / Zoom", "ScaleX: $scaleX, ScaleY: $scaleY")
    }

    override fun onChartTranslate(me: MotionEvent?, dX: Float, dY: Float) {
        Log.i("Translate / Move", "dX: $dX, dY: $dY")
    }

    companion object {
        fun newInstance(): Fragment {
            return BarChartFrag()
        }
    }
}