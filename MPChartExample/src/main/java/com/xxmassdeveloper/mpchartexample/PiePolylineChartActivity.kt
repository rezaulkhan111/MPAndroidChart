package com.xxmassdeveloper.mpchartexample

import android.Manifest
import android.content.Intent
import android.content.pm.PackageManager
import android.graphics.Color
import android.graphics.Typeface
import android.net.Uri
import android.os.Bundle
import android.text.SpannableString
import android.text.style.ForegroundColorSpan
import android.text.style.RelativeSizeSpan
import android.text.style.StyleSpan
import android.util.Log
import android.view.*
import android.widget.SeekBar
import android.widget.SeekBar.OnSeekBarChangeListener
import android.widget.TextView
import androidx.core.content.ContextCompat
import com.github.mikephil.charting.animation.Easing
import com.github.mikephil.charting.charts.PieChart
import com.github.mikephil.charting.components.Legend
import com.github.mikephil.charting.data.*
import com.github.mikephil.charting.data.BaseDataSet.isDrawValuesEnabled
import com.github.mikephil.charting.data.BaseDataSet.setDrawValues
import com.github.mikephil.charting.data.ChartData.setDrawValues
import com.github.mikephil.charting.formatter.PercentFormatter
import com.github.mikephil.charting.highlight.Highlight
import com.github.mikephil.charting.interfaces.datasets.IDataSet.isDrawValuesEnabled
import com.github.mikephil.charting.interfaces.datasets.IDataSet.setDrawValues
import com.github.mikephil.charting.listener.OnChartValueSelectedListener
import com.github.mikephil.charting.utils.ColorTemplate
import com.github.mikephil.charting.utils.ColorTemplate.holoBlue
import com.xxmassdeveloper.mpchartexample.notimportant.DemoBase

class PiePolylineChartActivity : DemoBase(), OnSeekBarChangeListener, OnChartValueSelectedListener {
    private var chart: PieChart? = null
    private var seekBarX: SeekBar? = null
    private var seekBarY: SeekBar? = null
    private var tvX: TextView? = null
    private var tvY: TextView? = null
    private var tf: Typeface? = null
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        window.setFlags(
            WindowManager.LayoutParams.FLAG_FULLSCREEN,
            WindowManager.LayoutParams.FLAG_FULLSCREEN
        )
        setContentView(R.layout.activity_piechart)
        title = "PiePolylineChartActivity"
        tvX = findViewById(R.id.tvXMax)
        tvY = findViewById(R.id.tvYMax)
        seekBarX = findViewById(R.id.seekBar1)
        seekBarY = findViewById(R.id.seekBar2)
        seekBarX.setOnSeekBarChangeListener(this)
        seekBarY.setOnSeekBarChangeListener(this)
        chart = findViewById(R.id.chart1)
        chart.setUsePercentValues(true)
        chart.description!!.isEnabled = false
        chart.setExtraOffsets(5f, 10f, 5f, 5f)
        chart.dragDecelerationFrictionCoef = 0.95f
        tf = Typeface.createFromAsset(assets, "OpenSans-Regular.ttf")
        chart.setCenterTextTypeface(Typeface.createFromAsset(assets, "OpenSans-Light.ttf"))
        chart.centerText = generateCenterSpannableText()
        chart.setExtraOffsets(20f, 0f, 20f, 0f)
        chart.isDrawHoleEnabled = true
        chart.setHoleColor(Color.WHITE)
        chart.setTransparentCircleColor(Color.WHITE)
        chart.setTransparentCircleAlpha(110)
        chart.holeRadius = 58f
        chart.transparentCircleRadius = 61f
        chart.setDrawCenterText(true)
        chart.rotationAngle = 0f
        // enable rotation of the chart by touch
        chart.isRotationEnabled = true
        chart.isHighlightPerTapEnabled = true

        // chart.setUnit(" €");
        // chart.setDrawUnitsInChart(true);

        // add a selection listener
        chart.setOnChartValueSelectedListener(this)
        seekBarX.setProgress(4)
        seekBarY.setProgress(100)
        chart.animateY(1400, Easing.EaseInOutQuad)
        // chart.spin(2000, 0, 360);
        val l: Legend? = chart.legend
        l!!.verticalAlignment = Legend.LegendVerticalAlignment.TOP
        l.horizontalAlignment = Legend.LegendHorizontalAlignment.RIGHT
        l.orientation = Legend.LegendOrientation.VERTICAL
        l.setDrawInside(false)
        l.isEnabled = false
    }

    private fun setData(count: Int, range: Float) {
        val entries = ArrayList<PieEntry?>()

        // NOTE: The order of the entries when being added to the entries array determines their position around the center of
        // the chart.
        for (i in 0 until count) {
            entries.add(
                PieEntry(
                    (Math.random() * range).toFloat() + range / 5,
                    parties[i % parties.length]
                )
            )
        }
        val dataSet = PieDataSet(entries, "Election Results")
        dataSet.sliceSpace = 3f
        dataSet.selectionShift = 5f

        // add a lot of colors
        val colors = ArrayList<Int>()
        for (c in ColorTemplate.VORDIPLOM_COLORS) colors.add(c)
        for (c in ColorTemplate.JOYFUL_COLORS) colors.add(c)
        for (c in ColorTemplate.COLORFUL_COLORS) colors.add(c)
        for (c in ColorTemplate.LIBERTY_COLORS) colors.add(c)
        for (c in ColorTemplate.PASTEL_COLORS) colors.add(c)
        colors.add(holoBlue)
        dataSet.colors = colors
        //dataSet.setSelectionShift(0f);
        dataSet.valueLinePart1OffsetPercentage = 80f
        dataSet.valueLinePart1Length = 0.2f
        dataSet.valueLinePart2Length = 0.4f

        //dataSet.setXValuePosition(PieDataSet.ValuePosition.OUTSIDE_SLICE);
        dataSet.yValuePosition = PieDataSet.ValuePosition.OUTSIDE_SLICE
        val data = PieData(dataSet)
        data.setValueFormatter(PercentFormatter())
        data.setValueTextSize(11f)
        data.setValueTextColor(Color.BLACK)
        data.setValueTypeface(tf)
        chart!!.data = data

        // undo all highlights
        chart!!.highlightValues(null)
        chart.invalidate()
    }

    override fun onCreateOptionsMenu(menu: Menu): Boolean {
        menuInflater.inflate(R.menu.pie, menu)
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when (item.itemId) {
            R.id.viewGithub -> {
                val i = Intent(Intent.ACTION_VIEW)
                i.data =
                    Uri.parse("https://github.com/PhilJay/MPAndroidChart/blob/master/MPChartExample/src/com/xxmassdeveloper/mpchartexample/PiePolylineChartActivity.java")
                startActivity(i)
            }
            R.id.actionToggleValues -> {
                for (set in chart!!.data!!.dataSets) set.setDrawValues(!set.isDrawValuesEnabled)
                chart.invalidate()
            }
            R.id.actionToggleHole -> {
                if (chart!!.isDrawHoleEnabled) chart!!.isDrawHoleEnabled =
                    false else chart!!.isDrawHoleEnabled = true
                chart.invalidate()
            }
            R.id.actionToggleMinAngles -> {
                if (chart!!.minAngleForSlices == 0f) chart!!.minAngleForSlices =
                    36f else chart!!.minAngleForSlices = 0f
                chart!!.notifyDataSetChanged()
                chart.invalidate()
            }
            R.id.actionToggleCurvedSlices -> {
                val toSet = !chart!!.isDrawRoundedSlicesEnabled || !chart!!.isDrawHoleEnabled
                chart!!.setDrawRoundedSlices(toSet)
                if (toSet && !chart!!.isDrawHoleEnabled) {
                    chart!!.isDrawHoleEnabled = true
                }
                if (toSet && chart!!.isDrawSlicesUnderHoleEnabled) {
                    chart!!.setDrawSlicesUnderHole(false)
                }
                chart.invalidate()
            }
            R.id.actionDrawCenter -> {
                if (chart!!.isDrawCenterTextEnabled) chart!!.setDrawCenterText(false) else chart!!.setDrawCenterText(
                    true
                )
                chart.invalidate()
            }
            R.id.actionToggleXValues -> {
                chart!!.setDrawEntryLabels(!chart!!.isDrawEntryLabelsEnabled)
                chart.invalidate()
            }
            R.id.actionTogglePercent -> {
                chart!!.setUsePercentValues(!chart!!.isUsePercentValuesEnabled)
                chart.invalidate()
            }
            R.id.animateX -> {
                chart!!.animateX(1400)
            }
            R.id.animateY -> {
                chart!!.animateY(1400)
            }
            R.id.animateXY -> {
                chart!!.animateXY(1400, 1400)
            }
            R.id.actionToggleSpin -> {
                chart!!.spin(
                    1000,
                    chart!!.rotationAngle,
                    chart!!.rotationAngle + 360,
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

    override fun onProgressChanged(seekBar: SeekBar, progress: Int, fromUser: Boolean) {
        tvX!!.text = seekBarX!!.progress.toString()
        tvY!!.text = seekBarY!!.progress.toString()
        setData(seekBarX!!.progress, seekBarY!!.progress.toFloat())
    }

    override fun saveToGallery() {
        saveToGallery(chart!!, "PiePolylineChartActivity")
    }

    private fun generateCenterSpannableText(): SpannableString {
        val s = SpannableString("MPAndroidChart\ndeveloped by Philipp Jahoda")
        s.setSpan(RelativeSizeSpan(1.5f), 0, 14, 0)
        s.setSpan(StyleSpan(Typeface.NORMAL), 14, s.length - 15, 0)
        s.setSpan(ForegroundColorSpan(Color.GRAY), 14, s.length - 15, 0)
        s.setSpan(RelativeSizeSpan(.65f), 14, s.length - 15, 0)
        s.setSpan(StyleSpan(Typeface.ITALIC), s.length - 14, s.length, 0)
        s.setSpan(ForegroundColorSpan(holoBlue), s.length - 14, s.length, 0)
        return s
    }

    override fun onValueSelected(e: Entry?, h: Highlight?) {
        if (e == null) return
        Log.i(
            "VAL SELECTED",
            "Value: " + e.y + ", xIndex: " + e.x
                    + ", DataSet index: " + h!!.dataSetIndex
        )
    }

    override fun onNothingSelected() {
        Log.i("PieChart", "nothing selected")
    }

    override fun onStartTrackingTouch(seekBar: SeekBar) {}
    override fun onStopTrackingTouch(seekBar: SeekBar) {}
}