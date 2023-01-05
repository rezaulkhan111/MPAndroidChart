package com.xxmassdeveloper.mpchartexample

import android.Manifest
import android.content.Intent
import android.content.pm.PackageManager
import android.net.Uri
import android.os.Bundle
import android.util.Log
import android.view.*
import android.widget.SeekBar
import android.widget.SeekBar.OnSeekBarChangeListener
import android.widget.TextView
import androidx.core.content.ContextCompat
import com.github.mikephil.charting.charts.LineChart
import com.github.mikephil.charting.components.Legend.LegendForm
import com.github.mikephil.charting.data.*
import com.github.mikephil.charting.highlight.Highlight
import com.github.mikephil.charting.interfaces.datasets.ILineDataSet
import com.github.mikephil.charting.listener.OnChartValueSelectedListener
import com.github.mikephil.charting.utils.EntryXComparator
import com.xxmassdeveloper.mpchartexample.custom.MyMarkerView
import com.xxmassdeveloper.mpchartexample.notimportant.DemoBase
import java.util.*

class InvertedLineChartActivity : DemoBase(), OnSeekBarChangeListener,
    OnChartValueSelectedListener {

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
        title = "InvertedLineChartActivity"
        tvX = findViewById(R.id.tvXMax)
        tvY = findViewById(R.id.tvYMax)
        seekBarX = findViewById(R.id.seekBar1)
        seekBarY = findViewById(R.id.seekBar2)
        seekBarY.setOnSeekBarChangeListener(this)
        seekBarX.setOnSeekBarChangeListener(this)
        chart = findViewById(R.id.chart1)
        chart.setOnChartValueSelectedListener(this)
        chart.setDrawGridBackground(false)

        // no description text
        chart.getDescription()!!.setEnabled(false)

        // enable touch gestures
        chart.setTouchEnabled(true)

        // enable scaling and dragging
        chart.setDragEnabled(true)
        chart.setScaleEnabled(true)

        // if disabled, scaling can be done on x- and y-axis separately
        chart.setPinchZoom(true)

        // set an alternative background color
        // chart.setBackgroundColor(Color.GRAY);

        // create a custom MarkerView (extend MarkerView) and specify the layout
        // to use for it
        val mv = MyMarkerView(this, R.layout.custom_marker_view)
        mv.setChartView(chart) // For bounds control
        chart.setMarker(mv) // Set the marker to the chart
        val xl = chart.getXAxis()
        xl!!.setAvoidFirstLastClipping(true)
        xl!!.setAxisMinimum(0f)
        val leftAxis = chart.getAxisLeft()
        leftAxis!!.setInverted(true)
        leftAxis!!.setAxisMinimum(0f) // this replaces setStartAtZero(true)
        val rightAxis = chart.getAxisRight()
        rightAxis!!.setEnabled(false)

        // add data
        seekBarX.progress = 25
        seekBarY.progress = 50

        // // restrain the maximum scale-out factor
        // chart.setScaleMinima(3f, 3f);
        //
        // // center the view to a specific position inside the chart
        // chart.centerViewPort(10, 50);

        // get the legend (only possible after setting data)

        // // restrain the maximum scale-out factor
        // chart.setScaleMinima(3f, 3f);
        //
        // // center the view to a specific position inside the chart
        // chart.centerViewPort(10, 50);

        // get the legend (only possible after setting data)
        val l = chart.getLegend()

        // modify the legend ...
        l!!.setForm(LegendForm.LINE)

        // don't forget to refresh the drawing
        chart.invalidate()
    }

    private fun setData(count: Int, range: Float) {
        val entries = mutableListOf<Entry>()
        for (i in 0 until count) {
            val xVal = (Math.random() * range).toFloat()
            val yVal = (Math.random() * range).toFloat()
            entries.add(Entry(xVal, yVal))
        }

        // sort by x-value
        Collections.sort(entries, EntryXComparator())
        // create a dataset and give it a type
        val set1 = LineDataSet(entries, "DataSet 1")
        set1.setLineWidth(1.5f)
        set1.setCircleRadius(4f)
        // create a data object with the data sets
        val data = LineData(set1)
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
                    Uri.parse("https://github.com/PhilJay/MPAndroidChart/blob/master/MPChartExample/src/com/xxmassdeveloper/mpchartexample/InvertedLineChartActivity.java")
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
            R.id.animateX -> {
                chart.animateX(2000)
            }
            R.id.animateY -> {
                chart.animateY(2000)
            }
            R.id.animateXY -> {
                chart.animateXY(2000, 2000)
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
        saveToGallery(chart!!, "InvertedLineChartActivity")
    }

    override fun onValueSelected(e: Entry, h: Highlight) {
    }

    override fun onNothingSelected() {}
    override fun onStartTrackingTouch(seekBar: SeekBar) {}
    override fun onStopTrackingTouch(seekBar: SeekBar) {}
}