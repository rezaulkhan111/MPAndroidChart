// TODO: Finish and add to main activity list
package com.xxmassdeveloper.mpchartexample

import android.Manifest
import android.content.pm.PackageManager
import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import android.view.WindowManager
import androidx.core.content.ContextCompat
import com.github.mikephil.charting.charts.LineChart
import com.github.mikephil.charting.data.DataSet
import com.github.mikephil.charting.data.Entry
import com.github.mikephil.charting.data.LineData
import com.github.mikephil.charting.data.LineDataSet
import com.github.mikephil.charting.highlight.Highlight
import com.github.mikephil.charting.interfaces.datasets.ILineDataSet
import com.github.mikephil.charting.listener.OnChartValueSelectedListener
import com.github.mikephil.charting.listener.OnDrawListener
import com.xxmassdeveloper.mpchartexample.notimportant.DemoBase

/**
 * This Activity demonstrates drawing into the Chart with the finger. Both line,
 * bar and scatter charts can be used for drawing.
 *
 * @author Philipp Jahoda
 */
class DrawChartActivity : DemoBase(), OnChartValueSelectedListener, OnDrawListener {

    private lateinit var chart: LineChart

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        window.setFlags(
            WindowManager.LayoutParams.FLAG_FULLSCREEN,
            WindowManager.LayoutParams.FLAG_FULLSCREEN
        )
        setContentView(R.layout.activity_draw_chart)
        title = "DrawChartActivity"
        chart = findViewById(R.id.chart1)

        // listener for selecting and drawing
        chart.setOnChartValueSelectedListener(this)
        chart.setOnDrawListener(this)

        // if disabled, drawn data sets with the finger will not be automatically
        // finished
        // chart.setAutoFinish(true);
        chart.setDrawGridBackground(false)

        // add dummy-data to the chart
        initWithDummyData()
        val xl = chart.getXAxis()
        xl!!.setTypeface(tfRegular!!)
        xl.setAvoidFirstLastClipping(true)

        val yl = chart.getAxisLeft()
        yl!!.setTypeface(tfRegular!!)

        chart.getLegend()!!.setEnabled(false)

        // chart.setYRange(-40f, 40f, true);
        // call this to reset the changed y-range
        // chart.resetYRange(true);
    }

    private fun initWithDummyData() {
        val values = mutableListOf<Entry>()

        // create a dataset and give it a type (0)
        val set1 = LineDataSet(values, "DataSet")
        set1.setLineWidth(3f)
        set1.setCircleRadius(5f)

        // create a data object with the data sets
        val data = LineData(set1)

        chart.setData(data)
    }

    override fun onCreateOptionsMenu(menu: Menu): Boolean {
        menuInflater.inflate(R.menu.draw, menu)
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when (item.itemId) {
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

    override fun saveToGallery() {
        saveToGallery(chart!!, "DrawChartActivity")
    }

    override fun onValueSelected(e: Entry, h: Highlight) {

    }

    override fun onNothingSelected() {}

    /** callback for each new entry drawn with the finger  */
    override fun onEntryAdded(entry: Entry) {
    }

    /** callback when a DataSet has been drawn (when lifting the finger)  */
    override fun onDrawFinished(dataSet: DataSet<*>) {
        // prepare the legend again
        chart.getLegendRenderer()!!.computeLegend(chart.getData())
    }

    override fun onEntryMoved(entry: Entry) {
    }
}