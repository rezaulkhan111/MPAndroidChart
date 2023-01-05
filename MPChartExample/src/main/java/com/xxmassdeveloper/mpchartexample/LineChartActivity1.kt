package com.xxmassdeveloper.mpchartexample

import android.Manifest
import android.content.Intent
import android.content.pm.PackageManager
import android.graphics.Color
import android.graphics.DashPathEffect
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
import com.github.mikephil.charting.animation.Easing
import com.github.mikephil.charting.charts.LineChart
import com.github.mikephil.charting.components.Legend.LegendForm
import com.github.mikephil.charting.components.LimitLine
import com.github.mikephil.charting.components.LimitLine.LimitLabelPosition
import com.github.mikephil.charting.components.XAxis
import com.github.mikephil.charting.components.YAxis
import com.github.mikephil.charting.data.Entry
import com.github.mikephil.charting.data.LineData
import com.github.mikephil.charting.data.LineDataSet
import com.github.mikephil.charting.formatter.IFillFormatter
import com.github.mikephil.charting.highlight.Highlight
import com.github.mikephil.charting.interfaces.dataprovider.LineDataProvider
import com.github.mikephil.charting.interfaces.datasets.ILineDataSet
import com.github.mikephil.charting.listener.OnChartValueSelectedListener
import com.github.mikephil.charting.utils.Utils.getSDKInt
import com.xxmassdeveloper.mpchartexample.custom.MyMarkerView
import com.xxmassdeveloper.mpchartexample.notimportant.DemoBase

/**
 * Example of a heavily customized [LineChart] with limit lines, custom line shapes, etc.
 *
 * @since 1.7.4
 * @version 3.1.0
 */
class LineChartActivity1 : DemoBase(), OnSeekBarChangeListener, OnChartValueSelectedListener {
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
        title = "LineChartActivity1"
        tvX = findViewById(R.id.tvXMax)
        tvY = findViewById(R.id.tvYMax)
        seekBarX = findViewById(R.id.seekBar1)
        seekBarX.setOnSeekBarChangeListener(this)
        seekBarY = findViewById(R.id.seekBar2)
        seekBarY.setMax(180)
        seekBarY.setOnSeekBarChangeListener(this)
        run {
            // // Chart Style // //
            chart = findViewById(R.id.chart1)

            // background color
            chart.setBackgroundColor(Color.WHITE)

            // disable description text

            // disable description text
            chart.getDescription()!!.setEnabled(false)

            // enable touch gestures
            chart.setTouchEnabled(true)

            // set listeners
            chart.setOnChartValueSelectedListener(this)
            chart.setDrawGridBackground(false)

            // create marker to display box when values are selected
            val mv = MyMarkerView(this, R.layout.custom_marker_view)

            // Set the marker to the chart
            mv.setChartView(chart)
            chart.setMarker(mv)

            // enable scaling and dragging
            chart.setDragEnabled(true)
            chart.setScaleEnabled(true)
            // chart.setScaleXEnabled(true);
            // chart.setScaleYEnabled(true);

            // force pinch zoom along both axis
            chart.setPinchZoom(true)
        }
        var xAxis: XAxis?
        run {
            // // X-Axis Style // //
            xAxis = chart.getXAxis()

            // vertical grid lines
            xAxis!!.enableGridDashedLine(10f, 10f, 0f)
        }
        var yAxis: YAxis?
        run {
            // // Y-Axis Style // //
            yAxis = chart.getAxisLeft()

            // disable dual axis (only use LEFT axis)
            chart.getAxisRight()!!.setEnabled(false)

            // horizontal grid lines
            yAxis!!.enableGridDashedLine(10f, 10f, 0f)

            // axis range
            yAxis!!.setAxisMaximum(200f)
            yAxis!!.setAxisMinimum(-50f)
        }
        run {
            // // Create Limit Lines // //
            val llXAxis = LimitLine(9f, "Index 10")
            llXAxis.setLineWidth(4f)
            llXAxis.enableDashedLine(10f, 10f, 0f)
            llXAxis.setLabelPosition(LimitLabelPosition.RIGHT_BOTTOM)
            llXAxis.setTextSize(10f)
            llXAxis.setTypeface(tfRegular!!)

            val ll1 = LimitLine(150f, "Upper Limit")
            ll1.setLineWidth(4f)
            ll1.enableDashedLine(10f, 10f, 0f)
            ll1.setLabelPosition(LimitLabelPosition.RIGHT_TOP)
            ll1.setTextSize(10f)
            ll1.setTypeface(tfRegular!!)

            val ll2 = LimitLine(-30f, "Lower Limit")
            ll2.setLineWidth(4f)
            ll2.enableDashedLine(10f, 10f, 0f)
            ll2.setLabelPosition(LimitLabelPosition.RIGHT_BOTTOM)
            ll2.setTextSize(10f)
            ll2.setTypeface(tfRegular!!)

            // draw limit lines behind data instead of on top
            yAxis!!.setDrawLimitLinesBehindData(true)
            xAxis!!.setDrawLimitLinesBehindData(true)

            // add limit lines
            yAxis!!.addLimitLine(ll1)
            yAxis!!.addLimitLine(ll2)
        }

        // add data
        seekBarX.progress = 45
        seekBarY.progress = 180
        setData(45, 180f)

        // draw points over time
        chart!!.animateX(1500)

        // get the legend (only possible after setting data)
        val l = chart.getLegend()

        // draw legend entries as lines
        l!!.setForm(LegendForm.LINE)
    }

    private fun setData(count: Int, range: Float) {
        val values = mutableListOf<Entry>()
        for (i in 0 until count) {
            val `val` = (Math.random() * range).toFloat() - 30
            values.add(Entry(i.toFloat(), `val`, resources.getDrawable(R.drawable.star)))
        }
        val set1: LineDataSet?

        if (chart.getData() != null &&
            chart.getData().getDataSetCount() > 0
        ) {
            set1 = chart.getData().getDataSetByIndex(0) as LineDataSet?
            set1!!.setValues(values)
            set1!!.notifyDataSetChanged()
            chart.getData().notifyDataChanged()
            chart.notifyDataSetChanged()
        } else {
            // create a dataset and give it a type
            set1 = LineDataSet(values, "DataSet 1")
            set1.setDrawIcons(false)

            // draw dashed line
            set1.enableDashedLine(10f, 5f, 0f)

            // black lines and points
            set1.setColor(Color.BLACK)
            set1.setCircleColor(Color.BLACK)

            // line thickness and point size
            set1.setLineWidth(1f)
            set1.setCircleRadius(3f)

            // draw points as solid circles
            set1.setDrawCircleHole(false)

            // customize legend entry
            set1.setFormLineWidth(1f)
            set1.setFormLineDashEffect(DashPathEffect(floatArrayOf(10f, 5f), 0f))
            set1.setFormSize(15f)

            // text size of values
            set1.setValueTextSize(9f)

            // draw selection line as dashed
            set1.enableDashedHighlightLine(10f, 5f, 0f)

            // set the filled area
            set1.setDrawFilled(true)
            set1.setFillFormatter(object : IFillFormatter {
                override fun getFillLinePosition(
                    dataSet: ILineDataSet,
                    dataProvider: LineDataProvider
                ): Float {
                    return chart.getAxisLeft()!!.getAxisMinimum()
                }
            })

            // set color of filled area
            if (getSDKInt() >= 18) {
                // drawables only supported on api level 18 and above
                val drawable = ContextCompat.getDrawable(this, R.drawable.fade_red)
                set1.setFillDrawable(drawable)
            } else {
                set1.setFillColor(Color.BLACK)
            }
            val dataSets = java.util.ArrayList<ILineDataSet>()
            dataSets.add(set1) // add the data sets

            // create a data object with the data sets
            val data = LineData(dataSets)

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
                    Uri.parse("https://github.com/PhilJay/MPAndroidChart/blob/master/MPChartExample/src/com/xxmassdeveloper/mpchartexample/LineChartActivity1.java")
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
            R.id.actionToggleIcons -> {
                val sets: List<ILineDataSet>? = chart.getData()
                    .getDataSets()
                for (iSet in sets!!) {
                    val set = iSet as LineDataSet
                    set.setDrawIcons(!set.isDrawIconsEnabled())
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
                chart.animateY(2000, Easing.EaseInCubic)
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

        // redraw
        chart.invalidate()
    }

    override fun saveToGallery() {
        saveToGallery(chart, "LineChartActivity1")
    }

    override fun onStartTrackingTouch(seekBar: SeekBar) {}
    override fun onStopTrackingTouch(seekBar: SeekBar) {}
    override fun onValueSelected(e: Entry, h: Highlight) {
        Log.i("Entry selected", e.toString())
        Log.i(
            "LOW HIGH",
            "low: " + chart.getLowestVisibleX() + ", high: " + chart.getHighestVisibleX()
        )
        Log.i(
            "MIN MAX",
            "xMin: " + chart.getXChartMin() + ", xMax: " + chart.getXChartMax() + ", yMin: " + chart.getYChartMin() + ", yMax: " + chart.getYChartMax()
        )
    }

    override fun onNothingSelected() {
        Log.i("Nothing selected", "Nothing selected.")
    }
}