package com.github.mikephil.charting.highlight

import com.github.mikephil.charting.interfaces.dataprovider.BarDataProvider.barData
import com.github.mikephil.charting.interfaces.datasets.IBarDataSet.isStacked
import com.github.mikephil.charting.utils.MPPointD.Companion.recycleInstance
import com.github.mikephil.charting.interfaces.datasets.IDataSet.getEntryForXValue
import com.github.mikephil.charting.interfaces.dataprovider.BarLineScatterCandleBubbleDataProvider.getTransformer
import com.github.mikephil.charting.interfaces.datasets.IDataSet.axisDependency
import com.github.mikephil.charting.utils.Transformer.getPixelForValues
import com.github.mikephil.charting.interfaces.datasets.IDataSet.getEntryForIndex
import com.github.mikephil.charting.utils.Transformer.getValuesByTouchPoint
import com.github.mikephil.charting.interfaces.dataprovider.ChartInterface.maxHighlightDistance
import com.github.mikephil.charting.interfaces.datasets.IDataSet.isHighlightEnabled
import com.github.mikephil.charting.interfaces.datasets.IDataSet.getEntriesForXValue
import com.github.mikephil.charting.interfaces.dataprovider.BarLineScatterCandleBubbleDataProvider.data
import com.github.mikephil.charting.utils.Utils.getPosition
import com.github.mikephil.charting.interfaces.dataprovider.CombinedDataProvider.combinedData
import com.github.mikephil.charting.interfaces.datasets.IDataSet.entryCount
import com.github.mikephil.charting.components.YAxis.AxisDependency
import com.github.mikephil.charting.interfaces.dataprovider.BarDataProvider
import com.github.mikephil.charting.highlight.ChartHighlighter
import com.github.mikephil.charting.utils.MPPointD
import com.github.mikephil.charting.data.BarData
import com.github.mikephil.charting.interfaces.datasets.IBarDataSet
import com.github.mikephil.charting.data.BarEntry
import com.github.mikephil.charting.data.BarLineScatterCandleBubbleData
import com.github.mikephil.charting.charts.PieChart
import com.github.mikephil.charting.highlight.PieRadarHighlighter
import com.github.mikephil.charting.interfaces.datasets.IPieDataSet
import com.github.mikephil.charting.interfaces.dataprovider.BarLineScatterCandleBubbleDataProvider
import com.github.mikephil.charting.highlight.IHighlighter
import com.github.mikephil.charting.components.YAxis
import com.github.mikephil.charting.interfaces.datasets.IDataSet
import com.github.mikephil.charting.data.DataSet
import com.github.mikephil.charting.data.DataSet.Rounding
import com.github.mikephil.charting.charts.RadarChart
import com.github.mikephil.charting.utils.MPPointF
import com.github.mikephil.charting.interfaces.dataprovider.CombinedDataProvider
import com.github.mikephil.charting.highlight.BarHighlighter
import com.github.mikephil.charting.data.ChartData
import com.github.mikephil.charting.charts.PieRadarChartBase

/**
 * Created by Philipp Jahoda on 22/07/15.
 */
open class BarHighlighter(chart: BarDataProvider) : ChartHighlighter<BarDataProvider?>(chart) {
    override fun getHighlight(x: Float, y: Float): Highlight? {
        val high = super.getHighlight(x, y) ?: return null
        val pos = getValsForTouch(x, y)
        val barData = mChart!!.barData
        val set = barData!!.getDataSetByIndex(high.dataSetIndex)
        if (set.isStacked) {
            return getStackedHighlight(
                high,
                set, pos!!.x.toFloat(), pos.y.toFloat()
            )
        }
        recycleInstance(pos!!)
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
    fun getStackedHighlight(
        high: Highlight,
        set: IBarDataSet,
        xVal: Float,
        yVal: Float
    ): Highlight? {
        val entry = set.getEntryForXValue(xVal, yVal) ?: return null

        // not stacked
        if (entry.yVals == null) {
            return high
        } else {
            val ranges = entry.ranges
            if (ranges.size > 0) {
                val stackIndex = getClosestStackIndex(ranges, yVal)
                val pixels = mChart!!.getTransformer(set.axisDependency)!!
                    .getPixelForValues(high.x, ranges[stackIndex].to)
                val stackedHigh = Highlight(
                    entry.x,
                    entry.y, pixels.x.toFloat(), pixels.y.toFloat(),
                    high.dataSetIndex,
                    stackIndex,
                    high.axis
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
    protected fun getClosestStackIndex(ranges: Array<Range>?, value: Float): Int {
        if (ranges == null || ranges.size == 0) return 0
        var stackIndex = 0
        for (range in ranges) {
            if (range.contains(value)) return stackIndex else stackIndex++
        }
        val length = Math.max(ranges.size - 1, 0)
        return if (value > ranges[length].to) length else 0
    }

    //    /**
    //     * Splits up the stack-values of the given bar-entry into Range objects.
    //     *
    //     * @param entry
    //     * @return
    //     */
    //    protected Range[] getRanges(BarEntry entry) {
    //
    //        float[] values = entry.getYVals();
    //
    //        if (values == null || values.length == 0)
    //            return new Range[0];
    //
    //        Range[] ranges = new Range[values.length];
    //
    //        float negRemain = -entry.getNegativeSum();
    //        float posRemain = 0f;
    //
    //        for (int i = 0; i < ranges.length; i++) {
    //
    //            float value = values[i];
    //
    //            if (value < 0) {
    //                ranges[i] = new Range(negRemain, negRemain + Math.abs(value));
    //                negRemain += Math.abs(value);
    //            } else {
    //                ranges[i] = new Range(posRemain, posRemain + value);
    //                posRemain += value;
    //            }
    //        }
    //
    //        return ranges;
    //    }
    override fun getDistance(x1: Float, y1: Float, x2: Float, y2: Float): Float {
        return Math.abs(x1 - x2)
    }

    protected override val data: BarLineScatterCandleBubbleData<*>?
        protected get() = mChart!!.barData
}