package com.github.mikephil.charting.highlight

import com.github.mikephil.charting.charts.PieChart
import com.github.mikephil.charting.data.Entry

/**
 * Created by philipp on 12/06/16.
 */
class PieHighlighter : PieRadarHighlighter<PieChart> {

    constructor(chart: PieChart) : super(chart) {
    }

    override fun getClosestHighlight(index: Int, x: Float, y: Float): Highlight {
        val set = mChart.getData()!!.getDataSet()
        val entry: Entry = set.getEntryForIndex(index)
        return Highlight(index.toFloat(), entry.getY(), x, y, 0, set.getAxisDependency())
    }
}