package com.xxmassdeveloper.mpchartexample

import android.Manifest
import android.content.Intent
import android.content.pm.PackageManager
import android.graphics.Color
import android.graphics.Paint
import android.net.Uri
import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import android.view.WindowManager
import android.widget.SeekBar
import android.widget.SeekBar.OnSeekBarChangeListener
import android.widget.TextView
import androidx.core.content.ContextCompat
import com.github.mikephil.charting.charts.CandleStickChart
import com.github.mikephil.charting.components.XAxis.XAxisPosition
import com.github.mikephil.charting.components.YAxis.AxisDependency
import com.github.mikephil.charting.data.CandleData
import com.github.mikephil.charting.data.CandleDataSet
import com.github.mikephil.charting.data.CandleEntry
import com.xxmassdeveloper.mpchartexample.notimportant.DemoBase

class CandleStickChartActivity : DemoBase(), OnSeekBarChangeListener {

    private lateinit var chart: CandleStickChart
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
        setContentView(R.layout.activity_candlechart)
        title = "CandleStickChartActivity"
        tvX = findViewById(R.id.tvXMax)
        tvY = findViewById(R.id.tvYMax)
        seekBarX = findViewById(R.id.seekBar1)
        seekBarX.setOnSeekBarChangeListener(this)
        seekBarY = findViewById(R.id.seekBar2)
        seekBarY.setOnSeekBarChangeListener(this)
        chart = findViewById(R.id.chart1)
        chart.setBackgroundColor(Color.WHITE)
        chart.getDescription()!!.setEnabled(false)

        // if more than 60 entries are displayed in the chart, no values will be
        // drawn
        chart.setMaxVisibleValueCount(60)
        // scaling can now only be done on x- and y-axis separately
        chart.setPinchZoom(false)
        chart.setDrawGridBackground(false)
        val xAxis = chart.getXAxis()
        xAxis!!.setPosition(XAxisPosition.BOTTOM)
        xAxis!!.setDrawGridLines(false)

        val leftAxis = chart.getAxisLeft()
//        leftAxis.setEnabled(false);
        //        leftAxis.setEnabled(false);
        leftAxis!!.setLabelCount(7, false)
        leftAxis!!.setDrawGridLines(false)
        leftAxis!!.setDrawAxisLine(false)
        val rightAxis = chart.getAxisRight()
        rightAxis!!.setEnabled(false)
//        rightAxis.setStartAtZero(false);
        // setting data
        seekBarX.progress = 40
        seekBarY.progress = 100

        chart.getLegend()!!.setEnabled(false)
    }

    override fun onProgressChanged(seekBar: SeekBar, progress: Int, fromUser: Boolean) {
        var progress = progress
        progress = seekBarX!!.progress
        tvX!!.text = progress.toString()
        tvY!!.text = seekBarY!!.progress.toString()
        chart!!.resetTracking()
        val values = mutableListOf<CandleEntry>()
        for (i in 0 until progress) {
            val multi = (seekBarY!!.progress + 1).toFloat()
            val `val` = (Math.random() * 40).toFloat() + multi
            val high = (Math.random() * 9).toFloat() + 8f
            val low = (Math.random() * 9).toFloat() + 8f
            val open = (Math.random() * 6).toFloat() + 1f
            val close = (Math.random() * 6).toFloat() + 1f
            val even = i % 2 == 0
            values.add(
                CandleEntry(
                    i.toFloat(), `val` + high,
                    `val` - low,
                    if (even) `val` + open else `val` - open,
                    if (even) `val` - close else `val` + close,
                    resources.getDrawable(R.drawable.star)
                )
            )
        }
        val set1 = CandleDataSet(values, "Data Set")
        set1.setDrawIcons(false)
        set1.setAxisDependency(AxisDependency.LEFT)
//        set1.setColor(Color.rgb(80, 80, 80));
        set1.setShadowColor(Color.DKGRAY)
        set1.setShadowWidth(0.7f)
        set1.setDecreasingColor(Color.RED)
        set1.setDecreasingPaintStyle(Paint.Style.FILL)
        set1.setIncreasingColor(Color.rgb(122, 242, 84))
        set1.setIncreasingPaintStyle(Paint.Style.STROKE)
        set1.setNeutralColor(Color.BLUE)
        //set1.setHighlightLineWidth(1f);
        val data = CandleData(set1)

        chart.setData(data)
        chart.invalidate()
    }

    override fun onCreateOptionsMenu(menu: Menu): Boolean {
        menuInflater.inflate(R.menu.candle, menu)
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when (item.itemId) {
            R.id.viewGithub -> {
                val i = Intent(Intent.ACTION_VIEW)
                i.data =
                    Uri.parse("https://github.com/PhilJay/MPAndroidChart/blob/master/MPChartExample/src/com/xxmassdeveloper/mpchartexample/CandleStickChartActivity.java")
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
            R.id.actionToggleMakeShadowSameColorAsCandle -> {
                for (set in chart.getData().getDataSets()!!) {
                    (set as CandleDataSet).setShadowColorSameAsCandle(!set.getShadowColorSameAsCandle())
                }
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

    override fun saveToGallery() {
        saveToGallery(chart!!, "CandleStickChartActivity")
    }

    override fun onStartTrackingTouch(seekBar: SeekBar) {}
    override fun onStopTrackingTouch(seekBar: SeekBar) {}
}