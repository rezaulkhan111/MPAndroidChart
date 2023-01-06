package com.xxmassdeveloper.mpchartexample.fragments

import android.graphics.Typeface
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.github.mikephil.charting.charts.ScatterChart
import com.github.mikephil.charting.components.XAxis.XAxisPosition
import com.xxmassdeveloper.mpchartexample.R
import com.xxmassdeveloper.mpchartexample.custom.MyMarkerView

class ScatterChartFrag : SimpleFragment() {

    private lateinit var chart: ScatterChart

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val v = inflater.inflate(R.layout.frag_simple_scatter, container, false)
        chart = v.findViewById(R.id.scatterChart1)

        chart.getDescription()!!.setEnabled(false)

        val tf = Typeface.createFromAsset(context!!.assets, "OpenSans-Light.ttf")

        val mv = MyMarkerView(activity, R.layout.custom_marker_view)
        mv.setChartView(chart) // For bounds control

        chart.setMarker(mv)

        chart.setDrawGridBackground(false)
        chart.setData(generateScatterData(6, 10000f, 200))

        val xAxis = chart.getXAxis()
        xAxis!!.setEnabled(true)
        xAxis.setPosition(XAxisPosition.BOTTOM)

        val leftAxis = chart.getAxisLeft()
        leftAxis!!.setTypeface(tf)

        val rightAxis = chart.getAxisRight()
        rightAxis!!.setTypeface(tf)
        rightAxis.setDrawGridLines(false)

        val l = chart.getLegend()
        l!!.setWordWrapEnabled(true)
        l.setTypeface(tf)
        l.setFormSize(14f)
        l.setTextSize(9f)

        // increase the space between legend & bottom and legend & content

        // increase the space between legend & bottom and legend & content
        l.setYOffset(13f)
        chart.setExtraBottomOffset(16f)
        return v
    }

    companion object {
        fun newInstance(): Fragment {
            return ScatterChartFrag()
        }
    }
}