package com.xxmassdeveloper.mpchartexample.fragments

import android.graphics.Typeface
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.github.mikephil.charting.charts.LineChart
import com.xxmassdeveloper.mpchartexample.R

 class ComplexityFragment : SimpleFragment() {
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
        chart.setData(complexity)
        chart.animateX(3000)
        val tf = Typeface.createFromAsset(requireContext().assets, "OpenSans-Light.ttf")
        val l = chart.getLegend()
        l!!.setTypeface(tf)

        val leftAxis = chart.getAxisLeft()
        leftAxis!!.setTypeface(tf)

        chart.getAxisRight()!!.setEnabled(false)

        val xAxis = chart.getXAxis()
        xAxis!!.setEnabled(false)
        return v
    }

    companion object {
        fun newInstance(): Fragment {
            return ComplexityFragment()
        }
    }
}