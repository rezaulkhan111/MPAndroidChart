package com.xxmassdeveloper.mpchartexample.fragments

import android.graphics.Typeface
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.github.mikephil.charting.charts.LineChart
import com.xxmassdeveloper.mpchartexample.R

class SineCosineFragment : SimpleFragment() {

    private lateinit var chart: LineChart

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val v = inflater.inflate(R.layout.frag_simple_line, container, false)
        chart = v.findViewById(R.id.lineChart1)

        chart.getDescription()!!.setEnabled(false)

        chart.setDrawGridBackground(false)

        chart.setData(generateLineData())
        chart.animateX(3000)

        val tf = Typeface.createFromAsset(context!!.assets, "OpenSans-Light.ttf")

        val l = chart.getLegend()
        l!!.setTypeface(tf)

        val leftAxis = chart.getAxisLeft()
        leftAxis!!.setTypeface(tf)
        leftAxis.setAxisMaximum(1.2f)
        leftAxis.setAxisMinimum(-1.2f)

        chart.getAxisRight()!!.setEnabled(false)

        val xAxis = chart.getXAxis()
        xAxis!!.setEnabled(false)
        return v
    }

    companion object {
        fun newInstance(): Fragment {
            return SineCosineFragment()
        }
    }
}