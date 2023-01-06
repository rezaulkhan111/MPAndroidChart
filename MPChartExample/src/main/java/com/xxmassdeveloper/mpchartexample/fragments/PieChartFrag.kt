package com.xxmassdeveloper.mpchartexample.fragments

import android.graphics.Color
import android.graphics.Typeface
import android.os.Bundle
import android.text.SpannableString
import android.text.style.ForegroundColorSpan
import android.text.style.RelativeSizeSpan
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.github.mikephil.charting.charts.PieChart
import com.github.mikephil.charting.components.Legend
import com.xxmassdeveloper.mpchartexample.R

class PieChartFrag : SimpleFragment() {

    private lateinit var chart: PieChart

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val v = inflater.inflate(R.layout.frag_simple_pie, container, false)
        chart = v.findViewById(R.id.pieChart1)

        chart.getDescription()!!.setEnabled(false)

        val tf = Typeface.createFromAsset(context!!.assets, "OpenSans-Light.ttf")

        chart.setCenterTextTypeface(tf)
        chart.setCenterText(generateCenterText())
        chart.setCenterTextSize(10f)
        chart.setCenterTextTypeface(tf)

        // radius of the center hole in percent of maximum radius

        // radius of the center hole in percent of maximum radius
        chart.setHoleRadius(45f)
        chart.setTransparentCircleRadius(50f)

        val l = chart.getLegend()
        l!!.setVerticalAlignment(Legend.LegendVerticalAlignment.TOP)
        l.setHorizontalAlignment(Legend.LegendHorizontalAlignment.RIGHT)
        l.setOrientation(Legend.LegendOrientation.VERTICAL)
        l.setDrawInside(false)

        chart.setData(generatePieData())
        return v
    }

    private fun generateCenterText(): SpannableString {
        val s = SpannableString("Revenues\nQuarters 2015")
        s.setSpan(RelativeSizeSpan(2f), 0, 8, 0)
        s.setSpan(ForegroundColorSpan(Color.GRAY), 8, s.length, 0)
        return s
    }

    companion object {
        fun newInstance(): Fragment {
            return PieChartFrag()
        }
    }
}