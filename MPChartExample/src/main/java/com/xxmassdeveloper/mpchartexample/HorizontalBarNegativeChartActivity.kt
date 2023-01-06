package com.xxmassdeveloper.mpchartexample

import android.Manifest
import android.content.Intent
import android.content.pm.PackageManager
import android.graphics.RectF
import android.net.Uri
import android.os.Bundle
import android.util.Log
import android.view.Menu
import android.view.MenuItem
import android.view.WindowManager
import android.widget.SeekBar
import android.widget.SeekBar.OnSeekBarChangeListener
import android.widget.TextView
import androidx.core.content.ContextCompat
import com.github.mikephil.charting.charts.HorizontalBarChart
import com.github.mikephil.charting.components.Legend
import com.github.mikephil.charting.components.XAxis.XAxisPosition
import com.github.mikephil.charting.data.BarData
import com.github.mikephil.charting.data.BarDataSet
import com.github.mikephil.charting.data.BarEntry
import com.github.mikephil.charting.data.Entry
import com.github.mikephil.charting.highlight.Highlight
import com.github.mikephil.charting.interfaces.datasets.IBarDataSet
import com.github.mikephil.charting.listener.OnChartValueSelectedListener
import com.github.mikephil.charting.utils.MPPointF.Companion.recycleInstance
import com.xxmassdeveloper.mpchartexample.notimportant.DemoBase


class HorizontalBarNegativeChartActivity : DemoBase(), OnSeekBarChangeListener,
    OnChartValueSelectedListener {

    private lateinit var chart: HorizontalBarChart
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
        setContentView(R.layout.activity_horizontalbarchart)
        title = "HorizontalBarChartActivity"
        tvX = findViewById(R.id.tvXMax)
        tvY = findViewById(R.id.tvYMax)
        seekBarX = findViewById(R.id.seekBar1)
        seekBarY = findViewById(R.id.seekBar2)
        seekBarY.setOnSeekBarChangeListener(this)
        seekBarX.setOnSeekBarChangeListener(this)
        chart = findViewById(R.id.chart1)
        chart.setOnChartValueSelectedListener(this)
        // chart.setHighlightEnabled(false);
        chart.setDrawBarShadow(false)
        chart.setDrawValueAboveBar(true)
        chart.getDescription()!!.setEnabled(false)
        // if more than 60 entries are displayed in the chart, no values will be
        // drawn
        chart.setMaxVisibleValueCount(60)
        // scaling can now only be done on x- and y-axis separately
        chart.setPinchZoom(false)

        // draw shadows for each bar that show the maximum value
        // chart.setDrawBarShadow(true);
        chart.setDrawGridBackground(false)

        val xl = chart.getXAxis()
        xl!!.setPosition(XAxisPosition.BOTTOM)
        xl!!.setTypeface(tfLight!!)
        xl!!.setDrawAxisLine(true)
        xl!!.setDrawGridLines(false)
        xl!!.setGranularity(10f)

        val yl = chart.getAxisLeft()
        yl!!.setTypeface(tfLight!!)
        yl!!.setDrawAxisLine(true)
        yl!!.setDrawGridLines(true)
//        yl.setInverted(true);

        //        yl.setInverted(true);
        val yr = chart.getAxisRight()
        yr!!.setTypeface(tfLight!!)
        yr.setDrawAxisLine(true)
        yr.setDrawGridLines(false)
//        yr.setInverted(true);
        chart.setFitBars(true)
        chart.animateY(2500)
        // setting data
        seekBarY.progress = 50
        seekBarX.progress = 12

        val l = chart.getLegend()
        l!!.setVerticalAlignment(Legend.LegendVerticalAlignment.BOTTOM)
        l.setHorizontalAlignment(Legend.LegendHorizontalAlignment.LEFT)
        l.setOrientation(Legend.LegendOrientation.HORIZONTAL)
        l.setDrawInside(false)
        l.setFormSize(8f)
        l.setXEntrySpace(4f)
    }

    private fun setData(count: Int, range: Float) {
        val barWidth = 9f
        val spaceForBar = 10f
        val values = mutableListOf<BarEntry?>()
        for (i in 0 until count) {
            val yFloat = (Math.random() * range - range / 2).toFloat()
            values.add(
                BarEntry(
                    i * spaceForBar, yFloat,
                    resources.getDrawable(R.drawable.star)
                )
            )
        }

        val set1: BarDataSet?

        if (chart.getData() != null &&
            chart.getData()!!.getDataSetCount() > 0
        ) {
            set1 = chart.getData()!!.getDataSetByIndex(0) as BarDataSet?
            set1!!.setValues(values)
            chart.getData()!!.notifyDataChanged()
            chart.notifyDataSetChanged()
        } else {
            set1 = BarDataSet(values, "DataSet 1")
            set1.setDrawIcons(false)
            val dataSets = ArrayList<IBarDataSet?>()
            dataSets.add(set1)
            val data = BarData(dataSets)
            data.setValueTextSize(10f)
            data.setValueTypeface(tfLight)
            data.setBarWidth(barWidth)
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
                    Uri.parse("https://github.com/PhilJay/MPAndroidChart/blob/master/MPChartExample/src/com/xxmassdeveloper/mpchartexample/HorizontalBarChartActivity.java")
                startActivity(i)
            }
            R.id.actionToggleValues -> {
                val sets = chart.getData()!!.getDataSets()
                for (iSet in sets!!) {
                    iSet!!.setDrawValues(!iSet.isDrawValuesEnabled())
                }
                chart.invalidate()
            }
            R.id.actionToggleIcons -> {
                val sets = chart.getData()!!.getDataSets()
                for (iSet in sets!!) {
                    iSet!!.setDrawIcons(!iSet.isDrawIconsEnabled())
                }
                chart.invalidate()
            }
            R.id.actionToggleHighlight -> {
                if (chart.getData() != null) {
                    chart.getData()!!.setHighlightEnabled(!chart.getData()!!.isHighlightEnabled())
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
                for (set in chart.getData()!!
                    .getDataSets()!!) (set as BarDataSet).setBarBorderWidth(
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
        chart.setFitBars(true)
        chart.invalidate()
    }

    override fun saveToGallery() {
        saveToGallery(chart, "HorizontalBarChartActivity")
    }

    override fun onStartTrackingTouch(seekBar: SeekBar) {}
    override fun onStopTrackingTouch(seekBar: SeekBar) {}
    private val mOnValueSelectedRectF = RectF()
    override fun onValueSelected(e: Entry?, h: Highlight?) {
        if (e == null) return
        val bounds = mOnValueSelectedRectF
        chart.getBarBounds((e as BarEntry), bounds)
        val position = chart.getPosition(
            e, chart.getData()!!.getDataSetByIndex(h!!.getDataSetIndex())!!.getAxisDependency()!!
        )
        recycleInstance(position!!)
    }

    override fun onNothingSelected() {}
}