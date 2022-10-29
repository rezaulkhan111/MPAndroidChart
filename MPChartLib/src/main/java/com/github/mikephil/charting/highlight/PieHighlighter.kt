package com.github.mikephil.charting.highlight

import com.github.mikephil.charting.charts.PieChart
import com.github.mikephil.charting.data.*
import com.github.mikephil.charting.interfaces.datasets.IPieDataSet

/**
 * Created by philipp on 12/06/16.
 */
class PieHighlighter(chart: PieChart) : PieRadarHighlighter<PieChart?>(chart) {
    override fun getClosestHighlight(index: Int, x: Float, y: Float): Highlight? {
        val set: IPieDataSet = mChart.getData().getDataSet()
        val entry: Entry? = set.getEntryForIndex(index)
        return Highlight(index.toFloat(), entry!!.y, x, y, 0, set.axisDependency)
    }
}