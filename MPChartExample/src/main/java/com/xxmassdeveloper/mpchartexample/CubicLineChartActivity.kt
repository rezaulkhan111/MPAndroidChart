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
import com.github.mikephil.charting.charts.LineChart
import com.github.mikephil.charting.components.YAxis.YAxisLabelPosition
import com.github.mikephil.charting.data.Entry
import com.github.mikephil.charting.data.LineData
import com.github.mikephil.charting.data.LineDataSet
import com.github.mikephil.charting.formatter.IFillFormatter
import com.github.mikephil.charting.interfaces.dataprovider.LineDataProvider
import com.github.mikephil.charting.interfaces.datasets.ILineDataSet
import com.xxmassdeveloper.mpchartexample.notimportant.DemoBase

class CubicLineChartActivity : DemoBase(), OnSeekBarChangeListener {

    private lateinit var chart: LineChart
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
        setContentView(R.layout.activity_linechart)
        title = "CubicLineChartActivity"
        tvX = findViewById(R.id.tvXMax)
        tvY = findViewById(R.id.tvYMax)
        seekBarX = findViewById(R.id.seekBar1)
        seekBarY = findViewById(R.id.seekBar2)
        chart = findViewById(R.id.chart1)
        chart.setViewPortOffsets(0f, 0f, 0f, 0f)
        chart.setBackgroundColor(Color.rgb(104, 241, 175))

        // no description text

        // no description text
        chart.getDescription()!!.setEnabled(false)

        // enable touch gestures

        // enable touch gestures
        chart.setTouchEnabled(true)

        // enable scaling and dragging

        // enable scaling and dragging
        chart.setDragEnabled(true)
        chart.setScaleEnabled(true)

        // if disabled, scaling can be done on x- and y-axis separately

        // if disabled, scaling can be done on x- and y-axis separately
        chart.setPinchZoom(false)

        chart.setDrawGridBackground(false)
        chart.setMaxHighlightDistance(300f)

        val x = chart.getXAxis()
        x!!.setEnabled(false)

        val y = chart.getAxisLeft()
        y!!.setTypeface(tfLight!!)
        y!!.setLabelCount(6, false)
        y!!.setTextColor(Color.WHITE)
        y!!.setPosition(YAxisLabelPosition.INSIDE_CHART)
        y!!.setDrawGridLines(false)
        y!!.setAxisLineColor(Color.WHITE)

        chart.getAxisRight()!!.setEnabled(false)

        // add data
        seekBarY.setOnSeekBarChangeListener(this)
        seekBarX.setOnSeekBarChangeListener(this)

        // lower max, as cubic runs significantly slower than linear
        seekBarX.max = 700

        seekBarX.progress = 45
        seekBarY.progress = 100

        chart.getLegend()!!.setEnabled(false)

        chart.animateXY(2000, 2000)

        // don't forget to refresh the drawing
        chart.invalidate()
    }

    private fun setData(count: Int, range: Float) {
        val values = mutableListOf<Entry>()
        for (i in 0 until count) {
            val `val` = (Math.random() * (range + 1)).toFloat() + 20
            values.add(Entry(i.toFloat(), `val`))
        }
        val set1: LineDataSet?

        if (chart.getData() != null &&
            chart.getData().getDataSetCount() > 0
        ) {
            set1 = chart.getData().getDataSetByIndex(0) as LineDataSet?
            set1!!.setValues(values)
            chart.getData().notifyDataChanged()
            chart.notifyDataSetChanged()
        } else {
            // create a dataset and give it a type
            set1 = LineDataSet(values, "DataSet 1")
            set1.setMode(LineDataSet.Mode.CUBIC_BEZIER)
            set1.setCubicIntensity(0.2f)
            set1.setDrawFilled(true)
            set1.setDrawCircles(false)
            set1.setLineWidth(1.8f)
            set1.setCircleRadius(4f)
            set1.setCircleColor(Color.WHITE)
            set1.setHighLightColor(Color.rgb(244, 117, 117))
            set1.setColor(Color.WHITE)
            set1.setFillColor(Color.WHITE)
            set1.setFillAlpha(100)
            set1.setDrawHorizontalHighlightIndicator(false)
            set1.setFillFormatter(object : IFillFormatter {
                override fun getFillLinePosition(
                    dataSet: ILineDataSet,
                    dataProvider: LineDataProvider
                ): Float {
                    return chart.getAxisLeft()!!.getAxisMinimum()
                }
            })
            // create a data object with the data sets
            val data = LineData(set1)
            data.setValueTypeface(tfLight)
            data.setValueTextSize(9f)
            data.setDrawValues(false)
            // set data
            chart.setData(data)
        }
    }

    override fun onCreateOptionsMenu(menu: Menu): Boolean {
        menuInflater.inflate(R.menu.line, menu)
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when (item.itemId) {
            R.id.viewGithub -> {
                val i = Intent(Intent.ACTION_VIEW)
                i.data =
                    Uri.parse("https://github.com/PhilJay/MPAndroidChart/blob/master/MPChartExample/src/com/xxmassdeveloper/mpchartexample/CubicLineChartActivity.java")
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
            R.id.actionToggleFilled -> {
                val sets: List<ILineDataSet>? = chart.getData()
                    .getDataSets()
                for (iSet in sets!!) {
                    val set = iSet as LineDataSet
                    if (set.isDrawFilledEnabled()) set.setDrawFilled(false) else set.setDrawFilled(
                        true
                    )
                }
                chart.invalidate()
            }
            R.id.actionToggleCircles -> {
                val sets: List<ILineDataSet>? = chart.getData()
                    .getDataSets()
                for (iSet in sets!!) {
                    val set = iSet as LineDataSet
                    if (set.isDrawCirclesEnabled()) set.setDrawCircles(false) else set.setDrawCircles(
                        true
                    )
                }
                chart.invalidate()
            }
            R.id.actionToggleCubic -> {
                val sets: List<ILineDataSet>? = chart.getData()
                    .getDataSets()
                for (iSet in sets!!) {
                    val set = iSet as LineDataSet
                    set.setMode(if (set.getMode() === LineDataSet.Mode.CUBIC_BEZIER) LineDataSet.Mode.LINEAR else LineDataSet.Mode.CUBIC_BEZIER)
                }
                chart.invalidate()
            }
            R.id.actionToggleStepped -> {
                val sets: List<ILineDataSet>? = chart.getData()
                    .getDataSets()
                for (iSet in sets!!) {
                    val set = iSet as LineDataSet
                    set.setMode(if (set.getMode() === LineDataSet.Mode.STEPPED) LineDataSet.Mode.LINEAR else LineDataSet.Mode.STEPPED)
                }
                chart.invalidate()
            }
            R.id.actionToggleHorizontalCubic -> {
                val sets: List<ILineDataSet>? = chart.getData()
                    .getDataSets()
                for (iSet in sets!!) {
                    val set = iSet as LineDataSet
                    set.setMode(if (set.getMode() === LineDataSet.Mode.HORIZONTAL_BEZIER) LineDataSet.Mode.LINEAR else LineDataSet.Mode.HORIZONTAL_BEZIER)
                }
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
        tvY!!.text = seekBarY!!.progress.toString()
        setData(seekBarX!!.progress, seekBarY!!.progress.toFloat())

        // redraw
        chart.invalidate()
    }

    override fun saveToGallery() {
        saveToGallery(chart!!, "CubicLineChartActivity")
    }

    override fun onStartTrackingTouch(seekBar: SeekBar) {}
    override fun onStopTrackingTouch(seekBar: SeekBar) {}
}