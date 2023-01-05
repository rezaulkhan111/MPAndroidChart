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
import android.view.Menu
import android.view.MenuItem
import android.view.WindowManager
import android.widget.SeekBar
import android.widget.SeekBar.OnSeekBarChangeListener
import android.widget.TextView
import androidx.core.content.ContextCompat
import com.github.mikephil.charting.animation.Easing
import com.github.mikephil.charting.charts.PieChart
import com.github.mikephil.charting.components.Legend
import com.github.mikephil.charting.data.Entry
import com.github.mikephil.charting.data.PieData
import com.github.mikephil.charting.data.PieDataSet
import com.github.mikephil.charting.data.PieEntry
import com.github.mikephil.charting.formatter.PercentFormatter
import com.github.mikephil.charting.highlight.Highlight
import com.github.mikephil.charting.listener.OnChartValueSelectedListener
import com.github.mikephil.charting.utils.ColorTemplate
import com.github.mikephil.charting.utils.ColorTemplate.getHoloBlue
import com.github.mikephil.charting.utils.MPPointF
import com.xxmassdeveloper.mpchartexample.notimportant.DemoBase

class PieChartActivity : DemoBase(), OnSeekBarChangeListener, OnChartValueSelectedListener {

    private lateinit var chart: PieChart
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
        setContentView(R.layout.activity_piechart)
        title = "PieChartActivity"
        tvX = findViewById(R.id.tvXMax)
        tvY = findViewById(R.id.tvYMax)
        seekBarX = findViewById(R.id.seekBar1)
        seekBarY = findViewById(R.id.seekBar2)
        seekBarX.setOnSeekBarChangeListener(this)
        seekBarY.setOnSeekBarChangeListener(this)
        chart = findViewById(R.id.chart1)
        chart.setUsePercentValues(true)

        chart.getDescription()!!.setEnabled(false)
        chart.setExtraOffsets(5f, 10f, 5f, 5f)

        chart.setDragDecelerationFrictionCoef(0.95f)

        chart.setCenterTextTypeface(tfLight)
        chart.setCenterText(generateCenterSpannableText())

        chart.setDrawHoleEnabled(true)
        chart.setHoleColor(Color.WHITE)

        chart.setTransparentCircleColor(Color.WHITE)
        chart.setTransparentCircleAlpha(110)

        chart.setHoleRadius(58f)
        chart.setTransparentCircleRadius(61f)

        chart.setDrawCenterText(true)

        chart.setRotationAngle(0f)
        // enable rotation of the chart by touch
        chart.setRotationEnabled(true)
        chart.setHighlightPerTapEnabled(true)

        // chart.setUnit(" €");
        // chart.setDrawUnitsInChart(true);

        // add a selection listener
        chart.setOnChartValueSelectedListener(this)

        seekBarX.progress = 4
        seekBarY.progress = 10

        chart.animateY(1400, Easing.EaseInOutQuad)
        // chart.spin(2000, 0, 360);
        val l = chart.getLegend()
        l!!.setVerticalAlignment(Legend.LegendVerticalAlignment.TOP)
        l!!.setHorizontalAlignment(Legend.LegendHorizontalAlignment.RIGHT)
        l!!.setOrientation(Legend.LegendOrientation.VERTICAL)
        l!!.setDrawInside(false)
        l!!.setXEntrySpace(7f)
        l!!.setYEntrySpace(0f)
        l!!.setYOffset(0f)

        // entry label styling
        chart.setEntryLabelColor(Color.WHITE)
        chart.setEntryLabelTypeface(tfRegular)
        chart.setEntryLabelTextSize(12f)
    }

    private fun setData(count: Int, range: Float) {
        val entries = mutableListOf<PieEntry>()

        // NOTE: The order of the entries when being added to the entries array determines their position around the center of
        // the chart.
        for (i in 0 until count) {
            entries.add(
                PieEntry(
                    (Math.random() * range + range / 5).toFloat(),
                    parties[i % parties.size],
                    resources.getDrawable(R.drawable.star)
                )
            )
        }

        val dataSet = PieDataSet(entries, "Election Results")
        dataSet.setDrawIcons(false)
        dataSet.setSliceSpace(3f)
        dataSet.setIconsOffset(MPPointF(0f, 40f))
        dataSet.setSelectionShift(5f)

        // add a lot of colors
        val colors = ArrayList<Int>()
        for (c in ColorTemplate.VORDIPLOM_COLORS) colors.add(c)
        for (c in ColorTemplate.JOYFUL_COLORS) colors.add(c)
        for (c in ColorTemplate.COLORFUL_COLORS) colors.add(c)
        for (c in ColorTemplate.LIBERTY_COLORS) colors.add(c)
        for (c in ColorTemplate.PASTEL_COLORS) colors.add(c)
        colors.add(getHoloBlue())

        dataSet.setColors(colors)
        //dataSet.setSelectionShift(0f);
        val data = PieData(dataSet)
        data.setValueFormatter(PercentFormatter())
        data.setValueTextSize(11f)
        data.setValueTextColor(Color.WHITE)
        data.setValueTypeface(tfLight)
        chart.setData(data)

        // undo all highlights
        chart.highlightValues(null)

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
                    Uri.parse("https://github.com/PhilJay/MPAndroidChart/blob/master/MPChartExample/src/com/xxmassdeveloper/mpchartexample/PieChartActivity.java")
                startActivity(i)
            }
            R.id.actionToggleValues -> {
                for (set in chart.getData()!!
                    .getDataSets()!!) set.setDrawValues(!set.isDrawValuesEnabled())
                chart.invalidate()
            }
            R.id.actionToggleIcons -> {
                for (set in chart.getData()!!
                    .getDataSets()!!) set.setDrawIcons(!set.isDrawIconsEnabled())
                chart.invalidate()
            }
            R.id.actionToggleHole -> {
                if (chart.isDrawHoleEnabled()) chart.setDrawHoleEnabled(false) else chart.setDrawHoleEnabled(
                    true
                )
                chart.invalidate()
            }
            R.id.actionToggleMinAngles -> {
                if (chart.getMinAngleForSlices() == 0f) chart.setMinAngleForSlices(36f) else chart.setMinAngleForSlices(
                    0f
                )
                chart.notifyDataSetChanged()
                chart.invalidate()
            }
            R.id.actionToggleCurvedSlices -> {
                val toSet = !chart.isDrawRoundedSlicesEnabled() || !chart.isDrawHoleEnabled()
                chart.setDrawRoundedSlices(toSet)
                if (toSet && !chart.isDrawHoleEnabled()) {
                    chart.setDrawHoleEnabled(true)
                }
                if (toSet && chart.isDrawSlicesUnderHoleEnabled()) {
                    chart.setDrawSlicesUnderHole(false)
                }
                chart.invalidate()
            }
            R.id.actionDrawCenter -> {
                if (chart.isDrawCenterTextEnabled()) chart.setDrawCenterText(false) else chart.setDrawCenterText(
                    true
                )
                chart.invalidate()
            }
            R.id.actionToggleXValues -> {
                chart.setDrawEntryLabels(!chart.isDrawEntryLabelsEnabled())
                chart.invalidate()
            }
            R.id.actionTogglePercent -> {
                chart.setUsePercentValues(!chart.isUsePercentValuesEnabled())
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
                    1000,
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

    override fun onProgressChanged(seekBar: SeekBar, progress: Int, fromUser: Boolean) {
        tvX.text = seekBarX.progress.toString()
        tvY.text = seekBarY.progress.toString()
        setData(seekBarX.progress, seekBarY.progress.toFloat())
    }

    override fun saveToGallery() {
        saveToGallery(chart, "PieChartActivity")
    }

    private fun generateCenterSpannableText(): SpannableString {
        val s = SpannableString("MPAndroidChart\ndeveloped by Philipp Jahoda")
        s.setSpan(RelativeSizeSpan(1.7f), 0, 14, 0)
        s.setSpan(StyleSpan(Typeface.NORMAL), 14, s.length - 15, 0)
        s.setSpan(ForegroundColorSpan(Color.GRAY), 14, s.length - 15, 0)
        s.setSpan(RelativeSizeSpan(.8f), 14, s.length - 15, 0)
        s.setSpan(StyleSpan(Typeface.ITALIC), s.length - 14, s.length, 0)
        s.setSpan(ForegroundColorSpan(getHoloBlue()), s.length - 14, s.length, 0)
        return s
    }

    override fun onValueSelected(e: Entry, h: Highlight) {
        if (e == null) return
        Log.i(
            "VAL SELECTED",
            "Value: " + e.getY() + ", index: " + h.getX()
                    + ", DataSet index: " + h.getDataSetIndex()
        )
    }

    override fun onNothingSelected() {
        Log.i("PieChart", "nothing selected")
    }

    override fun onStartTrackingTouch(seekBar: SeekBar) {}
    override fun onStopTrackingTouch(seekBar: SeekBar) {}
}