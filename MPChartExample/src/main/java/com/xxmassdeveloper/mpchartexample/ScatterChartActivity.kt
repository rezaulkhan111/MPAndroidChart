package com.xxmassdeveloper.mpchartexample

import android.Manifest
import android.content.Intent
import android.content.pm.PackageManager
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
import com.github.mikephil.charting.charts.ScatterChart
import com.github.mikephil.charting.components.Legend
import com.github.mikephil.charting.data.Entry
import com.github.mikephil.charting.data.ScatterData
import com.github.mikephil.charting.data.ScatterDataSet
import com.github.mikephil.charting.highlight.Highlight
import com.github.mikephil.charting.interfaces.datasets.IScatterDataSet
import com.github.mikephil.charting.listener.OnChartValueSelectedListener
import com.github.mikephil.charting.utils.ColorTemplate
import com.xxmassdeveloper.mpchartexample.custom.CustomScatterShapeRenderer
import com.xxmassdeveloper.mpchartexample.notimportant.DemoBase

class ScatterChartActivity : DemoBase(), OnSeekBarChangeListener, OnChartValueSelectedListener {

    private lateinit var chart: ScatterChart
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
        setContentView(R.layout.activity_scatterchart)
        title = "ScatterChartActivity"
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
        chart.setMaxHighlightDistance(50f)

        // enable scaling and dragging
        chart.setDragEnabled(true)
        chart.setScaleEnabled(true)

        chart.setMaxVisibleValueCount(200)
        chart.setPinchZoom(true)

        seekBarX.progress = 45
        seekBarY.progress = 100

        val l = chart.getLegend()
        l!!.setVerticalAlignment(Legend.LegendVerticalAlignment.TOP)
        l.setHorizontalAlignment(Legend.LegendHorizontalAlignment.RIGHT)
        l.setOrientation(Legend.LegendOrientation.VERTICAL)
        l.setDrawInside(false)
        l.setTypeface(tfLight!!)
        l.setXOffset(5f)

        val yl = chart.getAxisLeft()
        yl!!.setTypeface(tfLight!!)
        yl.setAxisMinimum(0f) // this replaces setStartAtZero(true)


        chart.getAxisRight()!!.setEnabled(false)

        val xl = chart.getXAxis()
        xl!!.setTypeface(tfLight!!)
        xl.setDrawGridLines(false)
    }

    override fun onProgressChanged(seekBar: SeekBar, progress: Int, fromUser: Boolean) {
        tvX.text = seekBarX.progress.toString()
        tvY.text = seekBarY.progress.toString()
        val values1 = mutableListOf<Entry?>()
        val values2 = mutableListOf<Entry?>()
        val values3 = mutableListOf<Entry?>()
        for (i in 0 until seekBarX.progress) {
            val valFloat1 = (Math.random() * seekBarY.progress).toFloat() + 3
            values1.add(Entry(i.toFloat(), valFloat1))
        }
        for (i in 0 until seekBarX.progress) {
            val valFloat2 = (Math.random() * seekBarY.progress).toFloat() + 3
            values2.add(Entry(i + 0.33f, valFloat2))
        }
        for (i in 0 until seekBarX.progress) {
            val valFloat3 = (Math.random() * seekBarY.progress).toFloat() + 3
            values3.add(Entry(i + 0.66f, valFloat3))
        }

        // create a dataset and give it a type
        val set1 = ScatterDataSet(values1, "DS 1")
        set1.setScatterShape(ScatterChart.ScatterShape.SQUARE)
        set1.setColor(ColorTemplate.COLORFUL_COLORS[0])
        val set2 = ScatterDataSet(values2, "DS 2")
        set2.setScatterShape(ScatterChart.ScatterShape.CIRCLE)
        set2.setScatterShapeHoleColor(ColorTemplate.COLORFUL_COLORS[3])
        set2.setScatterShapeHoleRadius(3f)
        set2.setColor(ColorTemplate.COLORFUL_COLORS[1])
        val set3 = ScatterDataSet(values3, "DS 3")
        set3.setShapeRenderer(CustomScatterShapeRenderer())
        set3.setColor(ColorTemplate.COLORFUL_COLORS[2])

        set1.setScatterShapeSize(8f)
        set2.setScatterShapeSize(8f)
        set3.setScatterShapeSize(8f)

        val dataSets = ArrayList<IScatterDataSet?>()
        dataSets.add(set1) // add the data sets

        dataSets.add(set2)
        dataSets.add(set3)

        // create a data object with the data sets
        val data = ScatterData(dataSets)
        data.setValueTypeface(tfLight)

        chart.setData(data)
        chart.invalidate()
    }

    override fun onCreateOptionsMenu(menu: Menu): Boolean {
        menuInflater.inflate(R.menu.scatter, menu)
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when (item.itemId) {
            R.id.viewGithub -> {
                val i = Intent(Intent.ACTION_VIEW)
                i.data =
                    Uri.parse("https://github.com/PhilJay/MPAndroidChart/blob/master/MPChartExample/src/com/xxmassdeveloper/mpchartexample/ScatterChartActivity.java")
                startActivity(i)
            }
            R.id.actionToggleValues -> {
                val sets = chart.getData()!!
                    .getDataSets()
                for (iSet in sets!!) {
                    val set = iSet as ScatterDataSet
                    set.setDrawValues(!set.isDrawValuesEnabled())
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
            R.id.animateX -> {
                chart.animateX(3000)
            }
            R.id.animateY -> {
                chart.animateY(3000)
            }
            R.id.animateXY -> {
                chart.animateXY(3000, 3000)
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
        saveToGallery(chart, "ScatterChartActivity")
    }

    override fun onValueSelected(e: Entry?, h: Highlight?) {
        Log.i(
            "VAL SELECTED",
            "Value: " + e!!.getY() + ", xIndex: " + e.getX()
                    + ", DataSet index: " + h!!.getDataSetIndex()
        )
    }

    override fun onNothingSelected() {}
    override fun onStartTrackingTouch(seekBar: SeekBar) {}
    override fun onStopTrackingTouch(seekBar: SeekBar) {}
}