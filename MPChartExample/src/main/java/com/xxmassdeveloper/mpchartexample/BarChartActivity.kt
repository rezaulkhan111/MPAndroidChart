package com.xxmassdeveloper.mpchartexample

import android.Manifest
import android.content.Intent
import android.content.pm.PackageManager
import android.graphics.RectF
import android.net.Uri
import android.os.Bundle
import android.util.Log
import android.view.*
import android.widget.SeekBar
import android.widget.SeekBar.OnSeekBarChangeListener
import android.widget.TextView
import androidx.core.content.ContextCompat
import com.github.mikephil.charting.charts.BarChart
import com.github.mikephil.charting.components.Legend
import com.github.mikephil.charting.components.Legend.LegendForm
import com.github.mikephil.charting.components.XAxis.XAxisPosition
import com.github.mikephil.charting.components.YAxis.AxisDependency
import com.github.mikephil.charting.components.YAxis.YAxisLabelPosition
import com.github.mikephil.charting.data.*
import com.github.mikephil.charting.formatter.IAxisValueFormatter
import com.github.mikephil.charting.highlight.Highlight
import com.github.mikephil.charting.interfaces.datasets.IBarDataSet
import com.github.mikephil.charting.listener.OnChartValueSelectedListener
import com.github.mikephil.charting.utils.Fill
import com.github.mikephil.charting.utils.MPPointF.Companion.recycleInstance
import com.xxmassdeveloper.mpchartexample.custom.DayAxisValueFormatter
import com.xxmassdeveloper.mpchartexample.custom.MyAxisValueFormatter
import com.xxmassdeveloper.mpchartexample.custom.XYMarkerView
import com.xxmassdeveloper.mpchartexample.notimportant.DemoBase

class BarChartActivity : DemoBase(), OnSeekBarChangeListener, OnChartValueSelectedListener {

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
        title = "BarChartActivity"
        tvX = findViewById(R.id.tvXMax)
        tvY = findViewById(R.id.tvYMax)
        seekBarX = findViewById(R.id.seekBar1)
        seekBarY = findViewById(R.id.seekBar2)
        seekBarY.setOnSeekBarChangeListener(this)
        seekBarX.setOnSeekBarChangeListener(this)
        chart = findViewById(R.id.chart1)
        chart.setOnChartValueSelectedListener(this)
        chart.setDrawBarShadow(false)
        chart.setDrawValueAboveBar(true)
        chart.getDescription()!!.setEnabled(false)

        // if more than 60 entries are displayed in the chart, no values will be
        // drawn

        // if more than 60 entries are displayed in the chart, no values will be
        // drawn
        chart.setMaxVisibleValueCount(60)

        // scaling can now only be done on x- and y-axis separately

        // scaling can now only be done on x- and y-axis separately
        chart.setPinchZoom(false)

        chart.setDrawGridBackground(false)
        // chart.setDrawYLabels(false);

        // chart.setDrawYLabels(false);
        val xAxisFormatter: IAxisValueFormatter = DayAxisValueFormatter(chart)

        val xAxis = chart.getXAxis()
        xAxis!!.setPosition(XAxisPosition.BOTTOM)
        xAxis.setTypeface(tfLight!!)
        xAxis.setDrawGridLines(false)
        xAxis.setGranularity(1f) // only intervals of 1 day

        xAxis.setLabelCount(7)
        xAxis.setValueFormatter(xAxisFormatter)

        val custom: IAxisValueFormatter = MyAxisValueFormatter()

        val leftAxis = chart.getAxisLeft()
        leftAxis!!.setTypeface(tfLight!!)
        leftAxis.setLabelCount(8, false)
        leftAxis.setValueFormatter(custom)
        leftAxis.setPosition(YAxisLabelPosition.OUTSIDE_CHART)
        leftAxis.setSpaceTop(15f)
        leftAxis.setAxisMinimum(0f) // this replaces setStartAtZero(true)


        val rightAxis = chart.getAxisRight()
        rightAxis!!.setDrawGridLines(false)
        rightAxis.setTypeface(tfLight!!)
        rightAxis.setLabelCount(8, false)
        rightAxis.setValueFormatter(custom)
        rightAxis.setSpaceTop(15f)
        rightAxis.setAxisMinimum(0f) // this replaces setStartAtZero(true)

        val l = chart.getLegend()
        l!!.setVerticalAlignment(Legend.LegendVerticalAlignment.BOTTOM)
        l.setHorizontalAlignment(Legend.LegendHorizontalAlignment.LEFT)
        l.setOrientation(Legend.LegendOrientation.HORIZONTAL)
        l.setDrawInside(false)
        l.setForm(LegendForm.SQUARE)
        l.setFormSize(9f)
        l.setTextSize(11f)
        l.setXEntrySpace(4f)
        val mv = XYMarkerView(this, xAxisFormatter)
        mv.setChartView(chart) // For bounds control
        chart.setMarker(mv) // Set the marker to the chart
        // setting data
        seekBarY.progress = 50
        seekBarX.progress = 12

        // chart.setDrawLegend(false);
    }

    private fun setData(count: Int, range: Float) {
        val start = 1f
        val values: MutableList<BarEntry> = mutableListOf()
        var i = start.toInt()
        while (i < start + count) {
            val valFloat = (Math.random() * (range + 1)).toFloat()
            if (Math.random() * 100 < 25) {
                values.add(BarEntry(i.toFloat(), valFloat, resources.getDrawable(R.drawable.star)))
            } else {
                values.add(BarEntry(i.toFloat(), valFloat))
            }
            i++
        }

        val set1: BarDataSet?

        if (chart.getData() != null &&
            chart.getData().getDataSetCount() > 0
        ) {
            set1 = chart.getData().getDataSetByIndex(0) as BarDataSet?
            set1!!.setValues(values)
            chart.getData().notifyDataChanged()
            chart.notifyDataSetChanged()
        } else {
            set1 = BarDataSet(values, "The year 2017")
            set1.setDrawIcons(false)
            val startColor1 = ContextCompat.getColor(this, android.R.color.holo_orange_light)
            val startColor2 = ContextCompat.getColor(this, android.R.color.holo_blue_light)
            val startColor3 = ContextCompat.getColor(this, android.R.color.holo_orange_light)
            val startColor4 = ContextCompat.getColor(this, android.R.color.holo_green_light)
            val startColor5 = ContextCompat.getColor(this, android.R.color.holo_red_light)
            val endColor1 = ContextCompat.getColor(this, android.R.color.holo_blue_dark)
            val endColor2 = ContextCompat.getColor(this, android.R.color.holo_purple)
            val endColor3 = ContextCompat.getColor(this, android.R.color.holo_green_dark)
            val endColor4 = ContextCompat.getColor(this, android.R.color.holo_red_dark)
            val endColor5 = ContextCompat.getColor(this, android.R.color.holo_orange_dark)
            val gradientFills: MutableList<Fill> = ArrayList()
            gradientFills.add(Fill(startColor1, endColor1))
            gradientFills.add(Fill(startColor2, endColor2))
            gradientFills.add(Fill(startColor3, endColor3))
            gradientFills.add(Fill(startColor4, endColor4))
            gradientFills.add(Fill(startColor5, endColor5))
            set1.setFills(gradientFills)
            val dataSets = ArrayList<IBarDataSet>()
            dataSets.add(set1)
            val data = BarData(dataSets)
            data.setValueTextSize(10f)
            data.setValueTypeface(tfLight)
            data.setBarWidth(0.9f)

            chart.setData(data)
        }
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
                    Uri.parse("https://github.com/PhilJay/MPAndroidChart/blob/master/MPChartExample/src/com/xxmassdeveloper/mpchartexample/BarChartActivity.java")
                startActivity(i)
            }
            R.id.actionToggleValues -> {
                for (set in chart.getData()
                    .getDataSets()!!) set.setDrawValues(!set.isDrawValuesEnabled())
                chart.invalidate()
            }
            R.id.actionToggleIcons -> {
                for (set in chart.getData()
                    .getDataSets()!!) set.setDrawIcons(!set.isDrawIconsEnabled())
                chart.invalidate()
            }
            R.id.actionToggleHighlight -> {
                if (chart.getData() != null) {
                    chart.getData().setHighlightEnabled(!chart.getData().isHighlightEnabled())
                    chart.invalidate()
                }
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
                for (set in chart.getData().getDataSets()!!) (set as BarDataSet).setBarBorderWidth(
                    if (set.getBarBorderWidth() == 1f) 0f else 1f
                )
                chart.invalidate()
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
        }
        return true
    }

    override fun onProgressChanged(seekBar: SeekBar, progress: Int, fromUser: Boolean) {
        tvX.text = seekBarX.progress.toString()
        tvY.text = seekBarY.progress.toString()
        setData(seekBarX.progress, seekBarY.progress.toFloat())
        chart.invalidate()
    }

    override fun saveToGallery() {
        saveToGallery(chart, "BarChartActivity")
    }

    override fun onStartTrackingTouch(seekBar: SeekBar) {}
    override fun onStopTrackingTouch(seekBar: SeekBar) {}
    private val onValueSelectedRectF = RectF()
    override fun onValueSelected(e: Entry, h: Highlight) {
        if (e == null) return
        val bounds = onValueSelectedRectF
        chart.getBarBounds((e as BarEntry?)!!, bounds)
        val position = chart.getPosition(e, AxisDependency.LEFT)
        Log.i("bounds", bounds.toString())
        Log.i("position", position.toString())
        recycleInstance(position!!)
    }

    override fun onNothingSelected() {}
}