package com.github.mikephil.charting.highlight

import com.github.mikephil.charting.data.DataSet.Rounding
import com.github.mikephil.charting.interfaces.dataprovider.BarDataProvider
import com.github.mikephil.charting.interfaces.datasets.IDataSet
import com.github.mikephil.charting.utils.MPPointD.Companion.recycleInstance

/**
 * Created by Philipp Jahoda on 22/07/15.
 */
class HorizontalBarHighlighter : BarHighlighter {

    constructor(chart: BarDataProvider) : super(chart) {
    }

    override fun getHighlight(x: Float, y: Float): Highlight? {
        val barData = mChart!!.getBarData()
        val pos = getValsForTouch(y, x)
        val high = getHighlightForX(pos.y.toFloat(), y, x) ?: return null
        val set = barData!!.getDataSetByIndex(high.getDataSetIndex())
        if (set!!.isStacked()) {
            return getStackedHighlight(
                high,
                set, pos.y.toFloat(), pos.x.toFloat()
            )
        }
        recycleInstance(pos)
        return high
    }

    override fun buildHighlights(
        set: IDataSet<*>?,
        dataSetIndex: Int,
        xVal: Float,
        rounding: Rounding?
    ): MutableList<Highlight> {
        val highlights = mutableListOf<Highlight>()
        var entries = set!!.getEntriesForXValue(xVal)!!
        if (entries.size == 0) {
            // Try to find closest x-value and take all entries for that x-value
            val closest = set.getEntryForXValue(
                xVal, Float.NaN,
                rounding!!
            )
            if (closest != null) {
                entries = set.getEntriesForXValue(closest.getX())!!
            }
        }
        if (entries.size == 0) return highlights
        for (e in entries) {
            val pixels = mChart!!.getTransformer(
                set.getAxisDependency()
            )!!.getPixelForValues(e!!.getY(), e.getX())
            highlights.add(
                Highlight(
                    e.getX(), e.getY(), pixels!!.x.toFloat(), pixels.y.toFloat(),
                    dataSetIndex, set.getAxisDependency()
                )
            )
        }
        return highlights
    }

    override fun getDistance(x1: Float, y1: Float, x2: Float, y2: Float): Float {
        return Math.abs(y1 - y2)
    }
}