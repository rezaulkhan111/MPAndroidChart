package com.xxmassdeveloper.mpchartexample

import android.Manifest
import android.content.Intent
import android.content.pm.PackageManager
import android.graphics.Color
import android.net.Uri
import android.os.Bundle
import android.util.Log
import android.view.*
import android.widget.SeekBar
import android.widget.SeekBar.OnSeekBarChangeListener
import android.widget.TextView
import androidx.core.content.ContextCompat
import com.github.mikephil.charting.charts.BarChart
import com.github.mikephil.charting.components.AxisBase
import com.github.mikephil.charting.components.Legend
import com.github.mikephil.charting.data.*
import com.github.mikephil.charting.formatter.IAxisValueFormatter
import com.github.mikephil.charting.formatter.LargeValueFormatter
import com.github.mikephil.charting.highlight.Highlight
import com.github.mikephil.charting.listener.OnChartValueSelectedListener
import com.xxmassdeveloper.mpchartexample.custom.MyMarkerView
import com.xxmassdeveloper.mpchartexample.notimportant.DemoBase
import java.util.*

class BarChartActivityMultiDataset : DemoBase(), OnSeekBarChangeListener,
    OnChartValueSelectedListener {

    private lateinit var chart: BarChart
    private lateinit var seekBarX: SeekBar
    private lateinit var seekBarY: SeekBar
    private lateinit var tvX: TextView
    private lateinit var tvY: TextView
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
        chart.getDescription()!!.setEnabled(false)

//        chart.setDrawBorders(true);

        // scaling can now only be done on x- and y-axis separately

//        chart.setDrawBorders(true);

        // scaling can now only be done on x- and y-axis separately
        chart.setPinchZoom(false)

        chart.setDrawBarShadow(false)

        chart.setDrawGridBackground(false)

        // create a custom MarkerView (extend MarkerView) and specify the layout
        // to use for it

        // create a custom MarkerView (extend MarkerView) and specify the layout
        // to use for it
        val mv = MyMarkerView(this, R.layout.custom_marker_view)
        mv.setChartView(chart) // For bounds control

        chart.setMarker(mv) // Set the marker to the chart


        seekBarX.setProgress(10)
        seekBarY.setProgress(100)

        val l = chart.getLegend()
        l!!.setVerticalAlignment(Legend.LegendVerticalAlignment.TOP)
        l!!.setHorizontalAlignment(Legend.LegendHorizontalAlignment.RIGHT)
        l!!.setOrientation(Legend.LegendOrientation.VERTICAL)
        l!!.setDrawInside(true)
        l!!.setTypeface(tfLight!!)
        l!!.setYOffset(0f)
        l!!.setXOffset(10f)
        l!!.setYEntrySpace(0f)
        l!!.setTextSize(8f)

        val xAxis = chart.getXAxis()
        xAxis!!.setTypeface(tfLight!!)
        xAxis!!.setGranularity(1f)
        xAxis!!.setCenterAxisLabels(true)
        xAxis!!.setValueFormatter(object : IAxisValueFormatter {
            override fun getFormattedValue(value: Float, axis: AxisBase?): String {
                return value.toInt().toString()
            }
        })

        val leftAxis = chart.getAxisLeft()
        leftAxis!!.setTypeface(tfLight!!)
        leftAxis!!.setValueFormatter(LargeValueFormatter())
        leftAxis!!.setDrawGridLines(false)
        leftAxis!!.setSpaceTop(35f)
        leftAxis!!.setAxisMinimum(0f) // this replaces setStartAtZero(true)


        chart.getAxisRight()!!.setEnabled(false)
    }

    override fun onProgressChanged(seekBar: SeekBar, progress: Int, fromUser: Boolean) {
        val groupSpace = 0.08f
        val barSpace = 0.03f // x4 DataSet
        val barWidth = 0.2f // x4 DataSet
        // (0.2 + 0.03) * 4 + 0.08 = 1.00 -> interval per "group"
        val groupCount = seekBarX.progress + 1
        val startYear = 1980
        val endYear = startYear + groupCount
        tvX.text = String.format(Locale.ENGLISH, "%d-%d", startYear, endYear)
        tvY.text = seekBarY.progress.toString()
        val values1 = mutableListOf<BarEntry?>()
        val values2 = mutableListOf<BarEntry?>()
        val values3 = mutableListOf<BarEntry?>()
        val values4 = mutableListOf<BarEntry?>()
        val randomMultiplier = seekBarY.progress * 100000f
        for (i in startYear until endYear) {
            values1.add(BarEntry(i.toFloat(), (Math.random() * randomMultiplier).toFloat()))
            values2.add(BarEntry(i.toFloat(), (Math.random() * randomMultiplier).toFloat()))
            values3.add(BarEntry(i.toFloat(), (Math.random() * randomMultiplier).toFloat()))
            values4.add(BarEntry(i.toFloat(), (Math.random() * randomMultiplier).toFloat()))
        }
        val set1: BarDataSet?
        val set2: BarDataSet?
        val set3: BarDataSet?
        val set4: BarDataSet?
        if (chart.getData() != null && chart.getData()!!.getDataSetCount() > 0) {
            set1 = chart.getData()!!.getDataSetByIndex(0) as BarDataSet?
            set2 = chart.getData()!!.getDataSetByIndex(1) as BarDataSet?
            set3 = chart.getData()!!.getDataSetByIndex(2) as BarDataSet?
            set4 = chart.getData()!!.getDataSetByIndex(3) as BarDataSet?
            set1!!.setValues(values1)
            set2!!.setValues(values2)
            set3!!.setValues(values3)
            set4!!.setValues(values4)
            chart.getData()!!.notifyDataChanged()
            chart.notifyDataSetChanged()
        } else {
            // create 4 DataSets
            set1 = BarDataSet(values1, "Company A")
            set1.setColor(Color.rgb(104, 241, 175))
            set2 = BarDataSet(values2, "Company B")
            set2.setColor(Color.rgb(164, 228, 251))
            set3 = BarDataSet(values3, "Company C")
            set3.setColor(Color.rgb(242, 247, 158))
            set4 = BarDataSet(values4, "Company D")
            set4.setColor(Color.rgb(255, 102, 0))
            val data = BarData(set1, set2, set3, set4)
            data.setValueFormatter(LargeValueFormatter())
            data.setValueTypeface(tfLight)
            chart.setData(data)
        }

        // specify the width each bar should have
        chart.getBarData()!!.setBarWidth(barWidth)

        // restrict the x-axis range
        chart.getXAxis()!!.setAxisMinimum(startYear.toFloat())
        // barData.getGroupWith(...) is a helper that calculates the width each group needs based on the provided parameters
        chart.getXAxis()!!.setAxisMaximum(
            startYear + chart.getBarData()!!.getGroupWidth(groupSpace, barSpace) * groupCount
        )
        chart.groupBars(startYear.toFloat(), groupSpace, barSpace)
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
                for (set in chart.getData()!!
                    .getDataSets()!!) set!!.setDrawValues(!set.isDrawValuesEnabled())
                chart.invalidate()
            }
            R.id.actionTogglePinch -> {
                if (chart.isPinchZoomEnabled()) chart.setPinchZoom(false) else chart.setPinchZoom(
                    true
                )
                chart.invalidate()
            }
            R.id.actionToggleAutoScaleMinMax -> {
                chart.setAutoScaleMinMaxEnabled(!chart.isAutoScaleMinMaxEnabled())
                chart.notifyDataSetChanged()
            }
            R.id.actionToggleBarBorders -> {
                for (set in chart.getData()!!
                    .getDataSets()!!) (set as BarDataSet).setBarBorderWidth(
                    if (set.getBarBorderWidth() == 1f) 0f else 1f
                )
                chart.invalidate()
            }
            R.id.actionToggleHighlight -> {
                if (chart.getData() != null) {
                    chart.getData()!!.setHighlightEnabled(!chart.getData()!!.isHighlightEnabled())
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
                chart.animateX(2000)
            }
            R.id.animateY -> {
                chart.animateY(2000)
            }
            R.id.animateXY -> {
                chart.animateXY(2000, 2000)
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
    }

    override fun onNothingSelected() {
        Log.i("Activity", "Nothing selected.")
    }
}