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
import com.github.mikephil.charting.components.AxisBase
import com.github.mikephil.charting.components.XAxis.XAxisPosition
import com.github.mikephil.charting.components.YAxis.AxisDependency
import com.github.mikephil.charting.components.YAxis.YAxisLabelPosition
import com.github.mikephil.charting.data.Entry
import com.github.mikephil.charting.data.LineData
import com.github.mikephil.charting.data.LineDataSet
import com.github.mikephil.charting.formatter.IAxisValueFormatter
import com.github.mikephil.charting.interfaces.datasets.ILineDataSet
import com.github.mikephil.charting.utils.ColorTemplate.getHoloBlue
import com.xxmassdeveloper.mpchartexample.notimportant.DemoBase
import java.text.SimpleDateFormat
import java.util.*
import java.util.concurrent.TimeUnit

class LineChartTime : DemoBase(), OnSeekBarChangeListener {

    private lateinit var chart: LineChart
    private lateinit var seekBarX: SeekBar
    private lateinit var tvX: TextView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        window.setFlags(
            WindowManager.LayoutParams.FLAG_FULLSCREEN,
            WindowManager.LayoutParams.FLAG_FULLSCREEN
        )
        setContentView(R.layout.activity_linechart_time)
        title = "LineChartTime"
        tvX = findViewById(R.id.tvXMax)
        seekBarX = findViewById(R.id.seekBar1)
        seekBarX.setOnSeekBarChangeListener(this)
        chart = findViewById(R.id.chart1)

        // no description text
        chart.getDescription()!!.setEnabled(false)

        // enable touch gestures
        chart.setTouchEnabled(true)

        chart.setDragDecelerationFrictionCoef(0.9f)

        // enable scaling and dragging
        chart.setDragEnabled(true)
        chart.setScaleEnabled(true)
        chart.setDrawGridBackground(false)
        chart.setHighlightPerDragEnabled(true)

        // set an alternative background color
        chart.setBackgroundColor(Color.WHITE)
        chart.setViewPortOffsets(0f, 0f, 0f, 0f)

        // add data
        seekBarX.progress = 100

        // get the legend (only possible after setting data)
        val l = chart.getLegend()
        l!!.setEnabled(false)

        val xAxis = chart.getXAxis()
        xAxis!!.setPosition(XAxisPosition.TOP_INSIDE)
        xAxis.setTypeface(tfLight!!)
        xAxis.setTextSize(10f)
        xAxis.setTextColor(Color.WHITE)
        xAxis.setDrawAxisLine(false)
        xAxis.setDrawGridLines(true)
        xAxis.setTextColor(Color.rgb(255, 192, 56))
        xAxis.setCenterAxisLabels(true)
        xAxis.setGranularity(1f) // one hour

        xAxis.setValueFormatter(object : IAxisValueFormatter {
            private val mFormat = SimpleDateFormat("dd MMM HH:mm", Locale.ENGLISH)
            override fun getFormattedValue(value: Float, axis: AxisBase?): String {
                val millis = TimeUnit.HOURS.toMillis(value.toLong())
                return mFormat.format(Date(millis))
            }
        })

        val leftAxis = chart.getAxisLeft()
        leftAxis!!.setPosition(YAxisLabelPosition.INSIDE_CHART)
        leftAxis.setTypeface(tfLight!!)
        leftAxis.setTextColor(getHoloBlue())
        leftAxis.setDrawGridLines(true)
        leftAxis.setGranularityEnabled(true)
        leftAxis.setAxisMinimum(0f)
        leftAxis.setAxisMaximum(170f)
        leftAxis.setYOffset(-9f)
        leftAxis.setTextColor(Color.rgb(255, 192, 56))

        val rightAxis = chart.getAxisRight()
        rightAxis!!.setEnabled(false)
    }

    private fun setData(count: Int, range: Float) {
        // now in hours
        val now = TimeUnit.MILLISECONDS.toHours(System.currentTimeMillis())
        val values = mutableListOf<Entry>()

        // count = hours
        val to = (now + count).toFloat()

        // increment by 1 hour
        var x = now.toFloat()
        while (x < to) {
            val y = getRandom(range, 50f)
            values.add(Entry(x, y)) // add one entry per hour
            x++
        }

        // create a dataset and give it a type
        val set1 = LineDataSet(values, "DataSet 1")
        set1.setAxisDependency(AxisDependency.LEFT)
        set1.setColor(getHoloBlue())
        set1.setValueTextColor(getHoloBlue())
        set1.setLineWidth(1.5f)
        set1.setDrawCircles(false)
        set1.setDrawValues(false)
        set1.setFillAlpha(65)
        set1.setFillColor(getHoloBlue())
        set1.setHighLightColor(Color.rgb(244, 117, 117))
        set1.setDrawCircleHole(false)

        // create a data object with the data sets
        val data = LineData(set1)
        data.setValueTextColor(Color.WHITE)
        data.setValueTextSize(9f)

        // set data
        chart.setData(data)
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
                    Uri.parse("https://github.com/PhilJay/MPAndroidChart/blob/master/MPChartExample/src/com/xxmassdeveloper/mpchartexample/LineChartTime.java")
                startActivity(i)
            }
            R.id.actionToggleValues -> {
                val sets: List<ILineDataSet>? = chart.getData()
                    .getDataSets()
                for (iSet in sets!!) {
                    val set = iSet as LineDataSet
                    set.setDrawValues(!set.isDrawValuesEnabled())
                }
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
                    if (set.getMode() === LineDataSet.Mode.CUBIC_BEZIER) set.setMode(LineDataSet.Mode.LINEAR) else set.setMode(
                        LineDataSet.Mode.CUBIC_BEZIER
                    )
                }
                chart.invalidate()
            }
            R.id.actionToggleStepped -> {
                val sets: List<ILineDataSet>? = chart.getData()
                    .getDataSets()
                for (iSet in sets!!) {
                    val set = iSet as LineDataSet
                    if (set.getMode() === LineDataSet.Mode.STEPPED) set.setMode(LineDataSet.Mode.LINEAR) else set.setMode(
                        LineDataSet.Mode.STEPPED
                    )
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
        tvX.text = seekBarX.progress.toString()
        setData(seekBarX.progress, 50f)

        // redraw
        chart.invalidate()
    }

    override fun saveToGallery() {
        saveToGallery(chart, "LineChartTime")
    }

    override fun onStartTrackingTouch(seekBar: SeekBar) {}
    override fun onStopTrackingTouch(seekBar: SeekBar) {}
}