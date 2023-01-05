package com.xxmassdeveloper.mpchartexample

import android.Manifest
import android.content.Intent
import android.content.pm.PackageManager
import android.graphics.Color
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
import com.github.mikephil.charting.charts.BarChart
import com.github.mikephil.charting.components.Legend
import com.github.mikephil.charting.components.XAxis.XAxisPosition
import com.github.mikephil.charting.data.BarData
import com.github.mikephil.charting.data.BarDataSet
import com.github.mikephil.charting.data.BarEntry
import com.github.mikephil.charting.data.Entry
import com.github.mikephil.charting.highlight.Highlight
import com.github.mikephil.charting.interfaces.datasets.IBarDataSet
import com.github.mikephil.charting.listener.OnChartValueSelectedListener
import com.github.mikephil.charting.utils.ColorTemplate
import com.xxmassdeveloper.mpchartexample.custom.MyAxisValueFormatter
import com.xxmassdeveloper.mpchartexample.custom.MyValueFormatter
import com.xxmassdeveloper.mpchartexample.notimportant.DemoBase

class StackedBarActivity : DemoBase(), OnSeekBarChangeListener, OnChartValueSelectedListener {

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
        title = "StackedBarActivity"
        tvX = findViewById(R.id.tvXMax)
        tvY = findViewById(R.id.tvYMax)
        seekBarX = findViewById(R.id.seekBar1)
        seekBarX.setOnSeekBarChangeListener(this)
        seekBarY = findViewById(R.id.seekBar2)
        seekBarY.setOnSeekBarChangeListener(this)
        chart = findViewById(R.id.chart1)
        chart.setOnChartValueSelectedListener(this)

        chart.getDescription()!!.setEnabled(false)
        // if more than 60 entries are displayed in the chart, no values will be
        // drawn
        chart.setMaxVisibleValueCount(40)

        // scaling can now only be done on x- and y-axis separately
        chart.setPinchZoom(false)

        chart.setDrawGridBackground(false)
        chart.setDrawBarShadow(false)

        chart.setDrawValueAboveBar(false)
        chart.setHighlightFullBarEnabled(false)

        // change the position of the y-labels
        val leftAxis = chart.getAxisLeft()
        leftAxis!!.setValueFormatter(MyAxisValueFormatter())
        leftAxis.setAxisMinimum(0f) // this replaces setStartAtZero(true)
        chart.getAxisRight()!!.setEnabled(false)
        val xLabels = chart.getXAxis()
        xLabels!!.setPosition(XAxisPosition.TOP)

        // chart.setDrawXLabels(false);
        // chart.setDrawYLabels(false);
        // setting data
        seekBarX.progress = 12
        seekBarY.progress = 100

        val l = chart.getLegend()
        l!!.setVerticalAlignment(Legend.LegendVerticalAlignment.BOTTOM)
        l.setHorizontalAlignment(Legend.LegendHorizontalAlignment.RIGHT)
        l.setOrientation(Legend.LegendOrientation.HORIZONTAL)
        l.setDrawInside(false)
        l.setFormSize(8f)
        l.setFormToTextSpace(4f)
        l.setXEntrySpace(6f)

        // chart.setDrawLegend(false);
    }

    override fun onProgressChanged(seekBar: SeekBar, progress: Int, fromUser: Boolean) {
        tvX.text = seekBarX.progress.toString()
        tvY.text = seekBarY.progress.toString()
        val values = mutableListOf<BarEntry>()
        for (i in 0 until seekBarX.progress) {
            val mul = (seekBarY.progress + 1).toFloat()
            val val1 = (Math.random() * mul).toFloat() + mul / 3
            val val2 = (Math.random() * mul).toFloat() + mul / 3
            val val3 = (Math.random() * mul).toFloat() + mul / 3
            values.add(
                BarEntry(
                    i.toFloat(), floatArrayOf(val1, val2, val3),
                    resources.getDrawable(R.drawable.star)
                )
            )
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
            set1 = BarDataSet(values, "Statistics Vienna 2014")
            set1.setDrawIcons(false)
            set1.setColors(getColors())
            set1.setStackLabels(arrayOf("Births", "Divorces", "Marriages"))
            val dataSets = ArrayList<IBarDataSet>()
            dataSets.add(set1)
            val data = BarData(dataSets)
            data.setValueFormatter(MyValueFormatter())
            data.setValueTextColor(Color.WHITE)
            chart.setData(data)
        }
        chart.setFitBars(true)
        chart.invalidate()
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
                    Uri.parse("https://github.com/PhilJay/MPAndroidChart/blob/master/MPChartExample/src/com/xxmassdeveloper/mpchartexample/StackedBarActivity.java")
                startActivity(i)
            }
            R.id.actionToggleValues -> {
                val sets: List<IBarDataSet>? = chart.getData()
                    .getDataSets()
                for (iSet in sets!!) {
                    val set = iSet as BarDataSet
                    set.setDrawValues(!set.isDrawValuesEnabled())
                }
                chart.invalidate()
            }
            R.id.actionToggleIcons -> {
                val sets: List<IBarDataSet>? = chart.getData()
                    .getDataSets()
                for (iSet in sets!!) {
                    val set = iSet as BarDataSet
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

    override fun saveToGallery() {
        saveToGallery(chart, "StackedBarActivity")
    }

    override fun onStartTrackingTouch(seekBar: SeekBar) {}
    override fun onStopTrackingTouch(seekBar: SeekBar) {}
    override fun onValueSelected(e: Entry, h: Highlight) {
        val entry = e as BarEntry?
        if (entry!!.getYVals() != null) Log.i(
            "VAL SELECTED",
            "Value: " + entry.getYVals()!![h.getStackIndex()]
        ) else Log.i("VAL SELECTED", "Value: " + entry.getY())
    }

    override fun onNothingSelected() {}

    // have as many colors as stack-values per entry
    private fun getColors(): IntArray {
        // have as many colors as stack-values per entry
        val colors = IntArray(3)
        System.arraycopy(ColorTemplate.MATERIAL_COLORS, 0, colors, 0, 3)
        return colors
    }
}