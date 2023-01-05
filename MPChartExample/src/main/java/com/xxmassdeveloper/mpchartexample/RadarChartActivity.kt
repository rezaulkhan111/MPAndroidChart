package com.xxmassdeveloper.mpchartexample

import android.Manifest
import android.content.Intent
import android.content.pm.PackageManager
import android.graphics.Color
import android.net.Uri
import android.os.Bundle
import android.view.*
import androidx.core.content.ContextCompat
import com.github.mikephil.charting.animation.Easing
import com.github.mikephil.charting.charts.RadarChart
import com.github.mikephil.charting.components.*
import com.github.mikephil.charting.data.RadarData
import com.github.mikephil.charting.data.RadarDataSet
import com.github.mikephil.charting.data.RadarEntry
import com.github.mikephil.charting.formatter.IAxisValueFormatter
import com.github.mikephil.charting.interfaces.datasets.IRadarDataSet
import com.xxmassdeveloper.mpchartexample.custom.RadarMarkerView
import com.xxmassdeveloper.mpchartexample.notimportant.DemoBase

class RadarChartActivity : DemoBase() {

    private lateinit var chart: RadarChart

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        window.setFlags(
            WindowManager.LayoutParams.FLAG_FULLSCREEN,
            WindowManager.LayoutParams.FLAG_FULLSCREEN
        )
        setContentView(R.layout.activity_radarchart)
        title = "RadarChartActivity"
        chart = findViewById(R.id.chart1)
        chart.setBackgroundColor(Color.rgb(60, 65, 82))

        chart.getDescription()!!.setEnabled(false)
        chart.setWebLineWidth(1f)
        chart.setWebColor(Color.LTGRAY)
        chart.setWebLineWidthInner(1f)
        chart.setWebColorInner(Color.LTGRAY)
        chart.setWebAlpha(100)

        // create a custom MarkerView (extend MarkerView) and specify the layout
        // to use for it
        val mv: MarkerView = RadarMarkerView(this, R.layout.radar_markerview)
        mv.setChartView(chart) // For bounds control
        chart.setMarker(mv) // Set the marker to the chart
        setData()
        chart.animateXY(1400, 1400, Easing.EaseInOutQuad)
        val xAxis = chart.getXAxis()
        xAxis!!.setTypeface(tfLight!!)
        xAxis.setTextSize(9f)
        xAxis.setYOffset(0f)
        xAxis.setXOffset(0f)
        xAxis.setValueFormatter(object : IAxisValueFormatter {
            private val mActivities = arrayOf("Burger", "Steak", "Salad", "Pasta", "Pizza")
            override fun getFormattedValue(value: Float, axis: AxisBase?): String {
                return mActivities[value.toInt() % mActivities.size]
            }
        })
        xAxis.setTextColor(Color.WHITE)

        val yAxis = chart.getYAxis()
        yAxis!!.setTypeface(tfLight!!)
        yAxis.setLabelCount(5, false)
        yAxis.setTextSize(9f)
        yAxis.setAxisMinimum(0f)
        yAxis.setAxisMaximum(80f)
        yAxis.setDrawLabels(false)

        val l = chart.getLegend()
        l!!.setVerticalAlignment(Legend.LegendVerticalAlignment.TOP)
        l.setHorizontalAlignment(Legend.LegendHorizontalAlignment.CENTER)
        l.setOrientation(Legend.LegendOrientation.HORIZONTAL)
        l.setDrawInside(false)
        l.setTypeface(tfLight!!)
        l.setXEntrySpace(7f)
        l.setYEntrySpace(5f)
        l.setTextColor(Color.WHITE)
    }

    private fun setData() {
        val mul = 80f
        val min = 20f
        val cnt = 5
        val entries1 = mutableListOf<RadarEntry>()
        val entries2 = mutableListOf<RadarEntry>()

        // NOTE: The order of the entries when being added to the entries array determines their position around the center of
        // the chart.
        for (i in 0 until cnt) {
            val val1 = (Math.random() * mul).toFloat() + min
            entries1.add(RadarEntry(val1))
            val val2 = (Math.random() * mul).toFloat() + min
            entries2.add(RadarEntry(val2))
        }
        val set1 = RadarDataSet(entries1, "Last Week")
        set1.setColor(Color.rgb(103, 110, 129))
        set1.setFillColor(Color.rgb(103, 110, 129))
        set1.setDrawFilled(true)
        set1.setFillAlpha(180)
        set1.setLineWidth(2f)
        set1.setDrawHighlightCircleEnabled(true)
        set1.setDrawHighlightIndicators(false)

        val set2 = RadarDataSet(entries2, "This Week")
        set2.setColor(Color.rgb(121, 162, 175))
        set2.setFillColor(Color.rgb(121, 162, 175))
        set2.setDrawFilled(true)
        set2.setFillAlpha(180)
        set2.setLineWidth(2f)
        set2.setDrawHighlightCircleEnabled(true)
        set2.setDrawHighlightIndicators(false)

        val sets = mutableListOf<IRadarDataSet>()
        sets.add(set1)
        sets.add(set2)

        val data = RadarData(sets)
        data.setValueTypeface(tfLight)
        data.setValueTextSize(8f)
        data.setDrawValues(false)
        data.setValueTextColor(Color.WHITE)

        chart.setData(data)
        chart.invalidate()
    }

    override fun onCreateOptionsMenu(menu: Menu): Boolean {
        menuInflater.inflate(R.menu.radar, menu)
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when (item.itemId) {
            R.id.viewGithub -> {
                val i = Intent(Intent.ACTION_VIEW)
                i.data =
                    Uri.parse("https://github.com/PhilJay/MPAndroidChart/blob/master/MPChartExample/src/com/xxmassdeveloper/mpchartexample/RadarChartActivity.java")
                startActivity(i)
            }
            R.id.actionToggleValues -> {
                for (set in chart.getData()
                    .getDataSets()) set.setDrawValues(!set.isDrawValuesEnabled())
                chart.invalidate()
            }
            R.id.actionToggleHighlight -> {
                if (chart.getData() != null) {
                    chart.getData().setHighlightEnabled(!chart.getData().isHighlightEnabled())
                    chart.invalidate()
                }
            }
            R.id.actionToggleRotate -> {
                if (chart.isRotationEnabled()) chart.setRotationEnabled(false) else chart.setRotationEnabled(
                    true
                )
                chart.invalidate()
            }
            R.id.actionToggleFilled -> {
                for (set in chart.getData()
                    .getDataSets()) {
                    if (set.isDrawFilledEnabled()) set.setDrawFilled(false) else set.setDrawFilled(
                        true
                    )
                }
                chart.invalidate()
            }
            R.id.actionToggleHighlightCircle -> {
                val sets = chart.getData()
                    .getDataSets() as ArrayList<IRadarDataSet>
                for (set in sets) {
                    set.setDrawHighlightCircleEnabled(!set.isDrawHighlightCircleEnabled())
                }
                chart.invalidate()
            }
            R.id.actionToggleXLabels -> {
                chart.getXAxis()!!.setEnabled(!chart.getXAxis()!!.isEnabled())
                chart.notifyDataSetChanged()
                chart.invalidate()
            }
            R.id.actionToggleYLabels -> {
                chart.getYAxis()!!.setEnabled(!chart.getYAxis()!!.isEnabled())
                chart.invalidate()
            }
            R.id.animateX -> {
                chart.animateX(1400)
            }
            R.id.animateY -> {
                chart.animateY(1400)
            }
            R.id.animateXY -> {
                chart.animateXY(1400, 1400)
            }
            R.id.actionToggleSpin -> {
                chart.spin(
                    2000,
                    chart.getRotationAngle(),
                    chart.getRotationAngle() + 360,
                    Easing.EaseInOutCubic
                )
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
        saveToGallery(chart!!, "RadarChartActivity")
    }
}