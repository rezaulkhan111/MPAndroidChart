package com.xxmassdeveloper.mpchartexample.fragments

import android.app.AlertDialog
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
import android.net.Uri
import android.view.*
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import com.github.mikephil.charting.components.XAxis.XAxisPosition
import com.github.mikephil.charting.charts.LineChart

/**
 * Demonstrates how to keep your charts straight forward, simple and beautiful with the MPAndroidChart library.
 *
 * @author Philipp Jahoda
 */
class SimpleChartDemo : DemoBase() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        window.setFlags(
            WindowManager.LayoutParams.FLAG_FULLSCREEN,
            WindowManager.LayoutParams.FLAG_FULLSCREEN
        )
        setContentView(R.layout.activity_awesomedesign)
        title = "SimpleChartDemo"
        val pager = findViewById<ViewPager>(R.id.pager)
        pager.offscreenPageLimit = 3
        val a = PageAdapter(supportFragmentManager)
        pager.adapter = a
        val b = AlertDialog.Builder(this)
        b.setTitle("This is a ViewPager.")
        b.setMessage("Swipe left and right for more awesome design examples!")
        b.setPositiveButton("OK") { dialog, which -> dialog.dismiss() }
        b.show()
    }

    private inner class PageAdapter internal constructor(fm: FragmentManager?) :
        FragmentPagerAdapter(
            fm!!
        ) {
        override fun getItem(pos: Int): Fragment {
            var f: Fragment? = null
            when (pos) {
                0 -> f = SineCosineFragment.Companion.newInstance()
                1 -> f = ComplexityFragment.Companion.newInstance()
                2 -> f = BarChartFrag.Companion.newInstance()
                3 -> f = ScatterChartFrag.Companion.newInstance()
                4 -> f = PieChartFrag.Companion.newInstance()
            }
            return f!!
        }

        override fun getCount(): Int {
            return 5
        }
    }

    override fun onCreateOptionsMenu(menu: Menu): Boolean {
        menuInflater.inflate(R.menu.only_github, menu)
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when (item.itemId) {
            R.id.viewGithub -> {
                val i = Intent(Intent.ACTION_VIEW)
                i.data =
                    Uri.parse("https://github.com/PhilJay/MPAndroidChart/blob/master/MPChartExample/src/com/xxmassdeveloper/mpchartexample/fragments/SimpleChartDemo.java")
                startActivity(i)
            }
        }
        return true
    }

    public override fun saveToGallery() { /* Intentionally left empty */
    }
}