package com.xxmassdeveloper.mpchartexample

import android.content.Intent
import android.graphics.Color
import android.graphics.Typeface
import android.net.Uri
import android.os.Bundle
import android.text.SpannableString
import android.text.style.ForegroundColorSpan
import android.text.style.RelativeSizeSpan
import android.text.style.StyleSpan
import android.util.DisplayMetrics
import android.view.Menu
import android.view.MenuItem
import android.view.WindowManager
import android.widget.RelativeLayout
import com.github.mikephil.charting.animation.Easing
import com.github.mikephil.charting.charts.PieChart
import com.github.mikephil.charting.components.Legend
import com.github.mikephil.charting.data.PieData
import com.github.mikephil.charting.data.PieDataSet
import com.github.mikephil.charting.data.PieEntry
import com.github.mikephil.charting.formatter.PercentFormatter
import com.github.mikephil.charting.utils.ColorTemplate
import com.github.mikephil.charting.utils.ColorTemplate.getHoloBlue
import com.xxmassdeveloper.mpchartexample.notimportant.DemoBase

class HalfPieChartActivity : DemoBase() {

    private lateinit var chart: PieChart

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        window.setFlags(
            WindowManager.LayoutParams.FLAG_FULLSCREEN,
            WindowManager.LayoutParams.FLAG_FULLSCREEN
        )
        setContentView(R.layout.activity_piechart_half)
        title = "HalfPieChartActivity"
        chart = findViewById(R.id.chart1)
        chart.setBackgroundColor(Color.WHITE)

        moveOffScreen()

        chart.setUsePercentValues(true)
        chart.getDescription()!!.setEnabled(false)

        chart.setCenterTextTypeface(tfLight)
        chart.setCenterText(generateCenterSpannableText())

        chart.setDrawHoleEnabled(true)
        chart.setHoleColor(Color.WHITE)

        chart.setTransparentCircleColor(Color.WHITE)
        chart.setTransparentCircleAlpha(110)

        chart.setHoleRadius(58f)
        chart.setTransparentCircleRadius(61f)

        chart.setDrawCenterText(true)

        chart.setRotationEnabled(false)
        chart.setHighlightPerTapEnabled(true)

        chart.setMaxAngle(180f) // HALF CHART

        chart.setRotationAngle(180f)
        chart.setCenterTextOffset(0f, -20f)

        setData(4, 100f)

        chart.animateY(1400, Easing.EaseInOutQuad)

        val l = chart.getLegend()
        l!!.setVerticalAlignment(Legend.LegendVerticalAlignment.TOP)
        l.setHorizontalAlignment(Legend.LegendHorizontalAlignment.CENTER)
        l.setOrientation(Legend.LegendOrientation.HORIZONTAL)
        l.setDrawInside(false)
        l.setXEntrySpace(7f)
        l.setYEntrySpace(0f)
        l.setYOffset(0f)
        // entry label styling
        chart.setEntryLabelColor(Color.WHITE)
        chart.setEntryLabelTypeface(tfRegular)
        chart.setEntryLabelTextSize(12f)
    }

    private fun setData(count: Int, range: Float) {
        val values = mutableListOf<PieEntry>()
        for (i in 0 until count) {
            values.add(
                PieEntry(
                    (Math.random() * range + range / 5).toFloat(),
                    parties[i % parties.size]
                )
            )
        }
        val dataSet = PieDataSet(values, "Election Results")
        dataSet.setSliceSpace(3f)
        dataSet.setSelectionShift(5f)

        dataSet.setColors(*ColorTemplate.MATERIAL_COLORS)
        //dataSet.setSelectionShift(0f);
        val data = PieData(dataSet)
        data.setValueFormatter(PercentFormatter())
        data.setValueTextSize(11f)
        data.setValueTextColor(Color.WHITE)
        data.setValueTypeface(tfLight)
        chart.setData(data)

        chart.invalidate()
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

    private fun moveOffScreen() {
        val displayMetrics = DisplayMetrics()
        windowManager.defaultDisplay.getMetrics(displayMetrics)
        val height = displayMetrics.heightPixels
        val offset = (height * 0.65).toInt() /* percent to move */
        val rlParams = chart.layoutParams as RelativeLayout.LayoutParams
        rlParams.setMargins(0, 0, 0, -offset)
        chart.layoutParams = rlParams
    }

    override fun onCreateOptionsMenu(menu: Menu): Boolean {
        menuInflater.inflate(R.menu.only_github, menu)
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when (item.itemId) {
            R.id.viewGithub -> {
                val i = Intent(Intent.ACTION_VIEW)
                i.data =
                    Uri.parse("https://github.com/PhilJay/MPAndroidChart/blob/master/MPChartExample/src/com/xxmassdeveloper/mpchartexample/HalfPieChartActivity.java")
                startActivity(i)
            }
        }
        return true
    }

    public override fun saveToGallery() { /* Intentionally left empty */
    }
}