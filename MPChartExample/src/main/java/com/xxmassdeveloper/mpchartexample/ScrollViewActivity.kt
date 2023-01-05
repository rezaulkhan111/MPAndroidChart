package com.xxmassdeveloper.mpchartexample

import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import android.view.WindowManager
import com.github.mikephil.charting.charts.BarChart
import com.github.mikephil.charting.components.XAxis.XAxisPosition
import com.github.mikephil.charting.data.BarData
import com.github.mikephil.charting.data.BarDataSet
import com.github.mikephil.charting.data.BarEntry
import com.github.mikephil.charting.utils.ColorTemplate
import com.xxmassdeveloper.mpchartexample.notimportant.DemoBase

class ScrollViewActivity : DemoBase() {

    private lateinit var chart: BarChart

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        window.setFlags(
            WindowManager.LayoutParams.FLAG_FULLSCREEN,
            WindowManager.LayoutParams.FLAG_FULLSCREEN
        )
        setContentView(R.layout.activity_scrollview)
        title = "ScrollViewActivity"
        chart = findViewById(R.id.chart1)

        chart.getDescription()!!.setEnabled(false)

        // scaling can now only be done on x- and y-axis separately
        chart.setPinchZoom(false)

        chart.setDrawBarShadow(false)
        chart.setDrawGridBackground(false)

        val xAxis = chart.getXAxis()
        xAxis!!.setPosition(XAxisPosition.BOTTOM)
        xAxis.setDrawGridLines(false)

        chart.getAxisLeft()!!.setDrawGridLines(false)

        chart.getLegend()!!.setEnabled(false)

        setData(10)
        chart.setFitBars(true)
    }

    private fun setData(count: Int) {
        val values = mutableListOf<BarEntry>()
        for (i in 0 until count) {
            val valFloat1 = (Math.random() * count).toFloat() + 15
            values.add(BarEntry(i.toFloat(), valFloat1.toInt().toFloat()))
        }
        val set = BarDataSet(values, "Data Set")
        set.setColors(ColorTemplate.VORDIPLOM_COLORS)
        set.setDrawValues(false)
        val data = BarData(set)
        chart.setData(data)
        chart.invalidate()
        chart.animateY(800)
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
                    Uri.parse("https://github.com/PhilJay/MPAndroidChart/blob/master/MPChartExample/src/com/xxmassdeveloper/mpchartexample/ScrollViewActivity.java")
                startActivity(i)
            }
        }
        return true
    }

    public override fun saveToGallery() { /* Intentionally left empty */
    }
}