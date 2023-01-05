package com.github.mikephil.charting.highlight

import com.github.mikephil.charting.data.BarLineScatterCandleBubbleData
import com.github.mikephil.charting.interfaces.dataprovider.BarDataProvider
import com.github.mikephil.charting.interfaces.datasets.IBarDataSet
import com.github.mikephil.charting.utils.MPPointD.Companion.recycleInstance

/**
 * Created by Philipp Jahoda on 22/07/15.
 */
open class BarHighlighter : ChartHighlighter<BarDataProvider> {

    constructor(chart: BarDataProvider) : super(chart) {
    }

    override fun getHighlight(x: Float, y: Float): Highlight? {
        val high = super.getHighlight(x, y) ?: return null
        val pos = getValsForTouch(x, y)
        val barData = mChart!!.getBarData()
        val set = barData!!.getDataSetByIndex(high.getDataSetIndex())
        if (set!!.isStacked()) {
            return getStackedHighlight(
                high,
                set,
                pos.x.toFloat(),
                pos.y.toFloat()
            )
        }
        recycleInstance(pos)
        return high
    }

    /**
     * This method creates the Highlight object that also indicates which value of a stacked BarEntry has been
     * selected.
     *
     * @param high the Highlight to work with looking for stacked values
     * @param set
     * @param xVal
     * @param yVal
     * @return
     */
    open fun getStackedHighlight(
        high: Highlight,
        set: IBarDataSet?,
        xVal: Float,
        yVal: Float
    ): Highlight? {
        val entry = set!!.getEntryForXValue(xVal, yVal) ?: return null

        // not stacked

        // not stacked
        if (entry.getYVals() == null) {
            return high
        } else {
            val ranges = entry.getRanges()
            if (ranges.size > 0) {
                val stackIndex = getClosestStackIndex(ranges, yVal)
                val pixels = mChart!!.getTransformer(set.getAxisDependency())
                    .getPixelForValues(high.getX(), ranges[stackIndex].to)
                val stackedHigh = Highlight(
                    entry.getX(),
                    entry.getY(),
                    pixels!!.x.toFloat(),
                    pixels.y.toFloat(),
                    high.getDataSetIndex(),
                    stackIndex,
                    high.getAxis()
                )
                recycleInstance(pixels)
                return stackedHigh
            }
        }
        return null
    }

    /**
     * Returns the index of the closest value inside the values array / ranges (stacked barchart) to the value
     * given as
     * a parameter.
     *
     * @param ranges
     * @param value
     * @return
     */
    protected open fun getClosestStackIndex(ranges: Array<Range>?, value: Float): Int {
        if (ranges == null || ranges.size == 0) return 0
        var stackIndex = 0
        for (range in ranges) {
            if (range.contains(value)) return stackIndex else stackIndex++
        }
        val length = Math.max(ranges.size - 1, 0)
        return if (value > ranges[length].to) length else 0
    }

    override fun getDistance(x1: Float, y1: Float, x2: Float, y2: Float): Float {
        return Math.abs(x1 - x2)
    }

    override fun getData(): BarLineScatterCandleBubbleData<*>? {
        return mChart!!.getBarData()
    }
}