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
import android.widget.Toast
import androidx.core.content.ContextCompat
import com.github.mikephil.charting.charts.LineChart
import com.github.mikephil.charting.components.Legend.LegendForm
import com.github.mikephil.charting.components.YAxis.AxisDependency
import com.github.mikephil.charting.data.Entry
import com.github.mikephil.charting.data.LineData
import com.github.mikephil.charting.data.LineDataSet
import com.github.mikephil.charting.highlight.Highlight
import com.github.mikephil.charting.listener.OnChartValueSelectedListener
import com.github.mikephil.charting.utils.ColorTemplate.getHoloBlue
import com.xxmassdeveloper.mpchartexample.notimportant.DemoBase

class RealtimeLineChartActivity : DemoBase(), OnChartValueSelectedListener {

    private lateinit var chart: LineChart

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        window.setFlags(
            WindowManager.LayoutParams.FLAG_FULLSCREEN,
            WindowManager.LayoutParams.FLAG_FULLSCREEN
        )
        setContentView(R.layout.activity_realtime_linechart)
        title = "RealtimeLineChartActivity"
        chart = findViewById(R.id.chart1)
        chart.setOnChartValueSelectedListener(this)

        // enable description text
        chart.getDescription()!!.setEnabled(true)

        // enable touch gestures
        chart.setTouchEnabled(true)

        // enable scaling and dragging
        chart.setDragEnabled(true)
        chart.setScaleEnabled(true)
        chart.setDrawGridBackground(false)

        // if disabled, scaling can be done on x- and y-axis separately
        chart.setPinchZoom(true)

        // set an alternative background color
        chart.setBackgroundColor(Color.LTGRAY)

        val data = LineData()
        data.setValueTextColor(Color.WHITE)

        // add empty data
        chart.setData(data)

        // get the legend (only possible after setting data)
        val l = chart.getLegend()

        // modify the legend ...
        l!!.setForm(LegendForm.LINE)
        l.setTypeface(tfLight!!)
        l.setTextColor(Color.WHITE)

        val xl = chart.getXAxis()
        xl!!.setTypeface(tfLight!!)
        xl.setTextColor(Color.WHITE)
        xl.setDrawGridLines(false)
        xl.setAvoidFirstLastClipping(true)
        xl.setEnabled(true)

        val leftAxis = chart.getAxisLeft()
        leftAxis!!.setTypeface(tfLight!!)
        leftAxis.setTextColor(Color.WHITE)
        leftAxis.setAxisMaximum(100f)
        leftAxis.setAxisMinimum(0f)
        leftAxis.setDrawGridLines(true)

        val rightAxis = chart.getAxisRight()
        rightAxis!!.setEnabled(false)
    }

    private fun addEntry() {
        val data = chart.getData()
        if (data != null) {
            var set = data.getDataSetByIndex(0)
            // set.addEntry(...); // can be called as well
            if (set == null) {
                set = createSet()
                data.addDataSet(set)
            }
            data.addEntry(
                Entry(
                    set.getEntryCount().toFloat(),
                    (Math.random() * 40).toFloat() + 30f
                ), 0
            )
            data.notifyDataChanged()

            // let the chart know it's data has changed
            chart.notifyDataSetChanged()

            // limit the number of visible entries
            chart.setVisibleXRangeMaximum(120f)
            // chart.setVisibleYRange(30, AxisDependency.LEFT);

            // move to the latest entry
            chart.moveViewToX(data.getEntryCount().toFloat())

            // this automatically refreshes the chart (calls invalidate())
            // chart.moveViewTo(data.getXValCount()-7, 55f,
            // AxisDependency.LEFT);
        }
    }

    private fun createSet(): LineDataSet {
        val set = LineDataSet(mutableListOf(), "Dynamic Data")
        set.setAxisDependency(AxisDependency.LEFT)
        set.setColor(getHoloBlue())
        set.setCircleColor(Color.WHITE)
        set.setLineWidth(2f)
        set.setCircleRadius(4f)
        set.setFillAlpha(65)
        set.setFillColor(getHoloBlue())
        set.setHighLightColor(Color.rgb(244, 117, 117))
        set.setValueTextColor(Color.WHITE)
        set.setValueTextSize(9f)
        set.setDrawValues(false)
        return set
    }

    private var thread: Thread? = null
    private fun feedMultiple() {
        if (thread != null) thread!!.interrupt()
        val runnable = Runnable { addEntry() }
        thread = Thread {
            for (i in 0..999) {

                // Don't generate garbage runnables inside the loop.
                runOnUiThread(runnable)
                try {
                    Thread.sleep(25)
                } catch (e: InterruptedException) {
                    e.printStackTrace()
                }
            }
        }
        thread!!.start()
    }

    override fun onCreateOptionsMenu(menu: Menu): Boolean {
        menuInflater.inflate(R.menu.realtime, menu)
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when (item.itemId) {
            R.id.viewGithub -> {
                val i = Intent(Intent.ACTION_VIEW)
                i.data =
                    Uri.parse("https://github.com/PhilJay/MPAndroidChart/blob/master/MPChartExample/src/com/xxmassdeveloper/mpchartexample/RealtimeLineChartActivity.java")
                startActivity(i)
            }
            R.id.actionAdd -> {
                addEntry()
            }
            R.id.actionClear -> {
                chart!!.clearValues()
                Toast.makeText(this, "Chart cleared!", Toast.LENGTH_SHORT).show()
            }
            R.id.actionFeedMultiple -> {
                feedMultiple()
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
        saveToGallery(chart, "RealtimeLineChartActivity")
    }

    override fun onValueSelected(e: Entry, h: Highlight) {
        Log.i("Entry selected", e.toString())
    }

    override fun onNothingSelected() {
        Log.i("Nothing selected", "Nothing selected.")
    }

    override fun onPause() {
        super.onPause()
        if (thread != null) {
            thread!!.interrupt()
        }
    }
}