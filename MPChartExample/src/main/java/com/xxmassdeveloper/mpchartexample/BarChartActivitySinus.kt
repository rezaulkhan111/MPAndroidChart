package com.xxmassdeveloper.mpchartexample

import android.Manifest
import android.content.Intent
import android.content.pm.PackageManager
import android.graphics.Color
import android.net.Uri
import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import android.view.WindowManager
import android.widget.SeekBar
import android.widget.SeekBar.OnSeekBarChangeListener
import android.widget.TextView
import androidx.core.content.ContextCompat
import com.github.mikephil.charting.charts.BarChart
import com.github.mikephil.charting.components.Legend
import com.github.mikephil.charting.components.Legend.LegendForm
import com.github.mikephil.charting.data.BarData
import com.github.mikephil.charting.data.BarDataSet
import com.github.mikephil.charting.data.BarEntry
import com.github.mikephil.charting.utils.FileUtils.loadBarEntriesFromAssets
import com.xxmassdeveloper.mpchartexample.notimportant.DemoBase

class BarChartActivitySinus : DemoBase(), OnSeekBarChangeListener {

    private lateinit var chart: BarChart
    private lateinit var seekBarX: SeekBar
    private lateinit var tvX: TextView
    private var data: MutableList<BarEntry>? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        window.setFlags(
            WindowManager.LayoutParams.FLAG_FULLSCREEN,
            WindowManager.LayoutParams.FLAG_FULLSCREEN
        )
        setContentView(R.layout.activity_barchart_sinus)
        title = "BarChartActivitySinus"
        data = loadBarEntriesFromAssets(assets, "othersine.txt")
        tvX = findViewById(R.id.tvValueCount)
        seekBarX = findViewById(R.id.seekbarValues)
        chart = findViewById(R.id.chart1)
        chart.setDrawBarShadow(false)
        chart.setDrawValueAboveBar(true)
        chart.getDescription()!!.setEnabled(false)

        // if more than 60 entries are displayed in the chart, no values will be
        // drawn
        chart.setMaxVisibleValueCount(60)

        // scaling can now only be done on x- and y-axis separately
        chart.setPinchZoom(false)

        chart.setDrawGridBackground(false)
        val xAxis = chart.getXAxis()
        xAxis!!.setEnabled(false)

        val leftAxis = chart.getAxisLeft()
        leftAxis!!.setTypeface(tfLight!!)
        leftAxis.setLabelCount(6, false)
        leftAxis.setAxisMinimum(-2.5f)
        leftAxis.setAxisMaximum(2.5f)
        leftAxis.setGranularityEnabled(true)
        leftAxis.setGranularity(0.1f)

        val rightAxis = chart.getAxisRight()
        rightAxis!!.setDrawGridLines(false)
        rightAxis.setTypeface(tfLight!!)
        rightAxis.setLabelCount(6, false)
        rightAxis.setAxisMinimum(-2.5f)
        rightAxis.setAxisMaximum(2.5f)
        rightAxis.setGranularity(0.1f)

        seekBarX.setOnSeekBarChangeListener(this)
        seekBarX.progress = 150 // set data

        val l = chart.getLegend()
        l!!.setVerticalAlignment(Legend.LegendVerticalAlignment.BOTTOM)
        l.setHorizontalAlignment(Legend.LegendHorizontalAlignment.LEFT)
        l.setOrientation(Legend.LegendOrientation.HORIZONTAL)
        l.setDrawInside(false)
        l.setForm(LegendForm.SQUARE)
        l.setFormSize(9f)
        l.setTextSize(11f)
        l.setXEntrySpace(4f)

        chart.animateXY(1500, 1500)
    }

    private fun setData(count: Int) {
        val entries = mutableListOf<BarEntry>()
        for (i in 0 until count) {
            entries.add(data!![i])
        }

        val set: BarDataSet?

        if (chart.getData() != null &&
            chart.getData().getDataSetCount() > 0
        ) {
            set = chart.getData().getDataSetByIndex(0) as BarDataSet?
            set!!.setValues(entries)
            chart.getData().notifyDataChanged()
            chart.notifyDataSetChanged()
        } else {
            set = BarDataSet(entries, "Sinus Function")
            set.setColor(Color.rgb(240, 120, 124))
        }

        val data = BarData(set)
        data.setValueTextSize(10f)
        data.setValueTypeface(tfLight)
        data.setDrawValues(false)
        data.setBarWidth(0.8f)

        chart.setData(data)
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
                    Uri.parse("https://github.com/PhilJay/MPAndroidChart/blob/master/MPChartExample/src/com/xxmassdeveloper/mpchartexample/BarChartActivitySinus.java")
                startActivity(i)
            }
            R.id.actionToggleValues -> {
                for (set in chart.getData()
                    .getDataSets()!!) set.setDrawValues(!set.isDrawValuesEnabled())
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
        tvX!!.text = seekBarX!!.progress.toString()
        setData(seekBarX!!.progress)
        chart.invalidate()
    }

    override fun saveToGallery() {
        saveToGallery(chart!!, "BarChartActivitySinus")
    }

    override fun onStartTrackingTouch(seekBar: SeekBar) {}
    override fun onStopTrackingTouch(seekBar: SeekBar) {}
}