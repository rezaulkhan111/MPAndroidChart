package com.xxmassdeveloper.mpchartexample

import android.content.Intent
import android.graphics.Color
import android.net.Uri
import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import android.view.WindowManager
import com.github.mikephil.charting.charts.BarChart
import com.github.mikephil.charting.components.AxisBase
import com.github.mikephil.charting.components.XAxis.XAxisPosition
import com.github.mikephil.charting.data.BarData
import com.github.mikephil.charting.data.BarDataSet
import com.github.mikephil.charting.data.BarEntry
import com.github.mikephil.charting.data.Entry
import com.github.mikephil.charting.formatter.IAxisValueFormatter
import com.github.mikephil.charting.formatter.IValueFormatter
import com.github.mikephil.charting.utils.ViewPortHandler
import com.xxmassdeveloper.mpchartexample.notimportant.DemoBase
import java.text.DecimalFormat

class BarChartPositiveNegative : DemoBase() {
    private lateinit var chart: BarChart

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        window.setFlags(
            WindowManager.LayoutParams.FLAG_FULLSCREEN,
            WindowManager.LayoutParams.FLAG_FULLSCREEN
        )
        setContentView(R.layout.activity_barchart_noseekbar)
        title = "BarChartPositiveNegative"
        chart = findViewById(R.id.chart1)
        chart.setBackgroundColor(Color.WHITE)
        chart.setExtraTopOffset(-30f)
        chart.setExtraBottomOffset(10f)
        chart.setExtraLeftOffset(70f)
        chart.setExtraRightOffset(70f)

        chart.setDrawBarShadow(false)
        chart.setDrawValueAboveBar(true)

        chart.getDescription()!!.setEnabled(false)

        // scaling can now only be done on x- and y-axis separately
        chart.setPinchZoom(false)

        chart.setDrawGridBackground(false)

        val xAxis = chart.getXAxis()
        xAxis!!.setPosition(XAxisPosition.BOTTOM)
        xAxis!!.setTypeface(tfRegular!!)
        xAxis!!.setDrawGridLines(false)
        xAxis!!.setDrawAxisLine(false)
        xAxis!!.setTextColor(Color.LTGRAY)
        xAxis!!.setTextSize(13f)
        xAxis!!.setLabelCount(5)
        xAxis!!.setCenterAxisLabels(true)
        xAxis!!.setGranularity(1f)

        val left = chart.getAxisLeft()
        left!!.setDrawLabels(false)
        left!!.setSpaceTop(25f)
        left!!.setSpaceBottom(25f)
        left!!.setDrawAxisLine(false)
        left!!.setDrawGridLines(false)
        left!!.setDrawZeroLine(true) // draw a zero line

        left!!.setZeroLineColor(Color.GRAY)
        left!!.setZeroLineWidth(0.7f)
        chart.getAxisRight()!!.setEnabled(false)
        chart.getLegend()!!.setEnabled(false)

        // THIS IS THE ORIGINAL DATA YOU WANT TO PLOT

        // THIS IS THE ORIGINAL DATA YOU WANT TO PLOT
        val data: MutableList<Data> = java.util.ArrayList()
        data.add(Data(0f, -224.1f, "12-29"))
        data.add(Data(1f, 238.5f, "12-30"))
        data.add(Data(2f, 1280.1f, "12-31"))
        data.add(Data(3f, -442.3f, "01-01"))
        data.add(Data(4f, -2280.1f, "01-02"))

        xAxis!!.setValueFormatter(object : IAxisValueFormatter {
            override fun getFormattedValue(value: Float, axis: AxisBase?): String {
                return data[Math.min(Math.max(value.toInt(), 0), data.size - 1)].xAxisValue
            }
        })

        setData(data)
    }

    private fun setData(dataList: List<Data>) {
        val values = mutableListOf<BarEntry>()
        val colors: MutableList<Int> = ArrayList()
        val green = Color.rgb(110, 190, 102)
        val red = Color.rgb(211, 74, 88)
        for (i in dataList.indices) {
            val d = dataList[i]
            val entry = BarEntry(d.xValue, d.yValue)
            values.add(entry)

            // specific colors
            if (d.yValue >= 0) colors.add(red) else colors.add(green)
        }

        val set: BarDataSet?

        if (chart.getData() != null &&
            chart.getData().getDataSetCount() > 0
        ) {
            set = chart.getData().getDataSetByIndex(0) as BarDataSet?
            set!!.setValues(values)
            chart.getData().notifyDataChanged()
            chart.notifyDataSetChanged()
        } else {
            set = BarDataSet(values, "Values")
            set.setColors(colors)
            set.setValueTextColors(colors)
            val data = BarData(set)
            data.setValueTextSize(13f)
            data.setValueTypeface(tfRegular)
            data.setValueFormatter(ValueFormatter())
            data.setBarWidth(0.8f)
            chart.setData(data)
            chart.invalidate()
        }
    }

    /**
     * Demo class representing data.
     */
    private inner class Data internal constructor(
        val xValue: Float,
        val yValue: Float,
        val xAxisValue: String
    )

    private inner class ValueFormatter internal constructor() : IValueFormatter {
        private val mFormat: DecimalFormat
        override fun getFormattedValue(
            value: Float,
            entry: Entry?,
            dataSetIndex: Int,
            viewPortHandler: ViewPortHandler?
        ): String {
            return mFormat.format(value.toDouble())
        }

        init {
            mFormat = DecimalFormat("######.0")
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
                    Uri.parse("https://github.com/PhilJay/MPAndroidChart/blob/master/MPChartExample/src/com/xxmassdeveloper/mpchartexample/BarChartPositiveNegative.java")
                startActivity(i)
            }
        }
        return true
    }

    public override fun saveToGallery() { /* Intentionally left empty */
    }
}