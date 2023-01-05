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
import com.github.mikephil.charting.charts.BubbleChart
import com.github.mikephil.charting.components.Legend
import com.github.mikephil.charting.components.XAxis.XAxisPosition
import com.github.mikephil.charting.data.*
import com.github.mikephil.charting.highlight.Highlight
import com.github.mikephil.charting.interfaces.datasets.IBubbleDataSet
import com.github.mikephil.charting.listener.OnChartValueSelectedListener
import com.github.mikephil.charting.utils.ColorTemplate
import com.github.mikephil.charting.utils.MPPointF
import com.xxmassdeveloper.mpchartexample.notimportant.DemoBase

class BubbleChartActivity : DemoBase(), OnSeekBarChangeListener, OnChartValueSelectedListener {

    private lateinit var chart: BubbleChart
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
        setContentView(R.layout.activity_bubblechart)
        title = "BubbleChartActivity"
        tvX = findViewById(R.id.tvXMax)
        tvY = findViewById(R.id.tvYMax)
        seekBarX = findViewById(R.id.seekBar1)
        seekBarX.setOnSeekBarChangeListener(this)
        seekBarY = findViewById(R.id.seekBar2)
        seekBarY.setOnSeekBarChangeListener(this)
        chart = findViewById(R.id.chart1)

        chart.getDescription()!!.setEnabled(false)

        chart.setOnChartValueSelectedListener(this)

        chart.setDrawGridBackground(false)

        chart.setTouchEnabled(true)

        // enable scaling and dragging

        // enable scaling and dragging
        chart.setDragEnabled(true)
        chart.setScaleEnabled(true)

        chart.setMaxVisibleValueCount(200)
        chart.setPinchZoom(true)

        seekBarX.progress = 10
        seekBarY.progress = 50

        val l = chart.getLegend()
        l!!.setVerticalAlignment(Legend.LegendVerticalAlignment.TOP)
        l!!.setHorizontalAlignment(Legend.LegendHorizontalAlignment.RIGHT)
        l!!.setOrientation(Legend.LegendOrientation.VERTICAL)
        l!!.setDrawInside(false)
        l!!.setTypeface(tfLight!!)

        val yl = chart.getAxisLeft()
        yl!!.setTypeface(tfLight!!)
        yl!!.setSpaceTop(30f)
        yl!!.setSpaceBottom(30f)
        yl!!.setDrawZeroLine(false)

        chart.getAxisRight()!!.setEnabled(false)

        val xl = chart.getXAxis()
        xl!!.setPosition(XAxisPosition.BOTTOM)
        xl!!.setTypeface(tfLight!!)
    }

    override fun onProgressChanged(seekBar: SeekBar, progress: Int, fromUser: Boolean) {
        val count = seekBarX!!.progress
        val range = seekBarY!!.progress
        tvX!!.text = count.toString()
        tvY!!.text = range.toString()
        val values1 = mutableListOf<BubbleEntry>()
        val values2 = mutableListOf<BubbleEntry>()
        val values3 = mutableListOf<BubbleEntry>()
        for (i in 0 until count) {
            values1.add(
                BubbleEntry(
                    i.toFloat(),
                    (Math.random() * range).toFloat(),
                    (Math.random() * range).toFloat(),
                    resources.getDrawable(R.drawable.star)
                )
            )
            values2.add(
                BubbleEntry(
                    i.toFloat(),
                    (Math.random() * range).toFloat(),
                    (Math.random() * range).toFloat(),
                    resources.getDrawable(R.drawable.star)
                )
            )
            values3.add(
                BubbleEntry(
                    i.toFloat(),
                    (Math.random() * range).toFloat(),
                    (Math.random() * range).toFloat()
                )
            )
        }

        // create a dataset and give it a type

        // create a dataset and give it a type
        val set1 = BubbleDataSet(values1, "DS 1")
        set1.setDrawIcons(false)
        set1.setColor(ColorTemplate.COLORFUL_COLORS[0], 130)
        set1.setDrawValues(true)

        val set2 = BubbleDataSet(values2, "DS 2")
        set2.setDrawIcons(false)
        set2.setIconsOffset(MPPointF(0f, 15f))
        set2.setColor(ColorTemplate.COLORFUL_COLORS[1], 130)
        set2.setDrawValues(true)

        val set3 = BubbleDataSet(values3, "DS 3")
        set3.setColor(ColorTemplate.COLORFUL_COLORS[2], 130)
        set3.setDrawValues(true)

        val dataSets = ArrayList<IBubbleDataSet>()
        dataSets.add(set1) // add the data sets

        dataSets.add(set2)
        dataSets.add(set3)

        // create a data object with the data sets

        // create a data object with the data sets
        val data = BubbleData(dataSets)
        data.setDrawValues(false)
        data.setValueTypeface(tfLight)
        data.setValueTextSize(8f)
        data.setValueTextColor(Color.WHITE)
        data.setHighlightCircleWidth(1.5f)

        chart.setData(data)
        chart.invalidate()
    }

    override fun onCreateOptionsMenu(menu: Menu): Boolean {
        menuInflater.inflate(R.menu.bubble, menu)
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when (item.itemId) {
            R.id.viewGithub -> {
                val i = Intent(Intent.ACTION_VIEW)
                i.data =
                    Uri.parse("https://github.com/PhilJay/MPAndroidChart/blob/master/MPChartExample/src/com/xxmassdeveloper/mpchartexample/BubbleChartActivity.java")
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
        saveToGallery(chart!!, "BubbleChartActivity")
    }

    override fun onValueSelected(e: Entry, h: Highlight) {
    }

    override fun onNothingSelected() {}
    override fun onStartTrackingTouch(seekBar: SeekBar) {}
    override fun onStopTrackingTouch(seekBar: SeekBar) {}
}