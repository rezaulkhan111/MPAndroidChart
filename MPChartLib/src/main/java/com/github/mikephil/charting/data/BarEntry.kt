package com.github.mikephil.charting.data

import com.github.mikephil.charting.interfaces.datasets.IDataSet.entryCount
import com.github.mikephil.charting.interfaces.datasets.IDataSet.getEntryForIndex
import com.github.mikephil.charting.interfaces.datasets.IDataSet.label
import com.github.mikephil.charting.highlight.Highlight.x
import com.github.mikephil.charting.interfaces.datasets.IDataSet.calcMinMaxY
import com.github.mikephil.charting.interfaces.datasets.IDataSet.yMax
import com.github.mikephil.charting.interfaces.datasets.IDataSet.yMin
import com.github.mikephil.charting.interfaces.datasets.IDataSet.axisDependency
import com.github.mikephil.charting.highlight.Highlight.dataSetIndex
import com.github.mikephil.charting.interfaces.datasets.IDataSet.getEntryForXValue
import com.github.mikephil.charting.highlight.Highlight.y
import com.github.mikephil.charting.interfaces.datasets.IDataSet.addEntry
import com.github.mikephil.charting.interfaces.datasets.IDataSet.xMax
import com.github.mikephil.charting.interfaces.datasets.IDataSet.xMin
import com.github.mikephil.charting.interfaces.datasets.IDataSet.removeEntry
import com.github.mikephil.charting.interfaces.datasets.IDataSet.colors
import com.github.mikephil.charting.interfaces.datasets.IDataSet.valueFormatter
import com.github.mikephil.charting.interfaces.datasets.IDataSet.valueTextColor
import com.github.mikephil.charting.interfaces.datasets.IDataSet.setValueTextColors
import com.github.mikephil.charting.interfaces.datasets.IDataSet.valueTypeface
import com.github.mikephil.charting.interfaces.datasets.IDataSet.valueTextSize
import com.github.mikephil.charting.interfaces.datasets.IDataSet.setDrawValues
import com.github.mikephil.charting.interfaces.datasets.IDataSet.isHighlightEnabled
import com.github.mikephil.charting.interfaces.datasets.IBubbleDataSet.highlightCircleWidth
import com.github.mikephil.charting.utils.Utils.convertDpToPixel
import com.github.mikephil.charting.interfaces.datasets.IDataSet.calcMinMax
import com.github.mikephil.charting.utils.ColorTemplate.createColors
import com.github.mikephil.charting.utils.Utils.defaultValueFormatter
import com.github.mikephil.charting.interfaces.datasets.IScatterDataSet.scatterShapeSize
import com.github.mikephil.charting.highlight.Highlight.dataIndex
import com.github.mikephil.charting.interfaces.datasets.IDataSet.getEntriesForXValue
import android.annotation.TargetApi
import android.os.Build
import com.github.mikephil.charting.data.filter.ApproximatorN
import android.os.Parcelable
import android.graphics.drawable.Drawable
import android.os.Parcel
import android.os.ParcelFormatException
import android.os.Parcelable.Creator
import com.github.mikephil.charting.data.BarLineScatterCandleBubbleData
import com.github.mikephil.charting.interfaces.datasets.IBarDataSet
import com.github.mikephil.charting.data.BarEntry
import com.github.mikephil.charting.data.BaseDataSet
import com.github.mikephil.charting.data.DataSet.Rounding
import com.github.mikephil.charting.data.DataSet
import com.github.mikephil.charting.data.ChartData
import com.github.mikephil.charting.interfaces.datasets.IPieDataSet
import android.annotation.SuppressLint
import com.github.mikephil.charting.interfaces.datasets.ILineDataSet
import com.github.mikephil.charting.data.PieEntry
import com.github.mikephil.charting.interfaces.datasets.IDataSet
import com.github.mikephil.charting.components.YAxis.AxisDependency
import com.github.mikephil.charting.formatter.IValueFormatter
import android.graphics.Typeface
import com.github.mikephil.charting.interfaces.datasets.IRadarDataSet
import com.github.mikephil.charting.data.BarLineScatterCandleBubbleDataSet
import com.github.mikephil.charting.utils.Fill
import com.github.mikephil.charting.data.BarDataSet
import com.github.mikephil.charting.interfaces.datasets.IBubbleDataSet
import com.github.mikephil.charting.interfaces.datasets.ICandleDataSet
import com.github.mikephil.charting.data.PieDataSet.ValuePosition
import com.github.mikephil.charting.data.PieDataSet
import com.github.mikephil.charting.data.RadarEntry
import com.github.mikephil.charting.components.YAxis
import com.github.mikephil.charting.components.Legend.LegendForm
import com.github.mikephil.charting.components.Legend
import android.graphics.DashPathEffect
import com.github.mikephil.charting.utils.MPPointF
import com.github.mikephil.charting.utils.ColorTemplate
import com.github.mikephil.charting.data.BubbleEntry
import com.github.mikephil.charting.data.CandleEntry
import com.github.mikephil.charting.data.LineRadarDataSet
import com.github.mikephil.charting.formatter.IFillFormatter
import com.github.mikephil.charting.formatter.DefaultFillFormatter
import com.github.mikephil.charting.data.LineDataSet
import com.github.mikephil.charting.interfaces.datasets.IScatterDataSet
import com.github.mikephil.charting.interfaces.datasets.IBarLineScatterCandleBubbleDataSet
import com.github.mikephil.charting.data.LineData
import com.github.mikephil.charting.data.BarData
import com.github.mikephil.charting.data.ScatterData
import com.github.mikephil.charting.data.CandleData
import com.github.mikephil.charting.data.BubbleData
import com.github.mikephil.charting.data.RadarDataSet
import com.github.mikephil.charting.data.BubbleDataSet
import com.github.mikephil.charting.data.LineScatterCandleRadarDataSet
import com.github.mikephil.charting.data.CandleDataSet
import com.github.mikephil.charting.data.ScatterDataSet
import com.github.mikephil.charting.charts.ScatterChart.ScatterShape
import com.github.mikephil.charting.highlight.Range
import com.github.mikephil.charting.renderer.scatter.TriangleShapeRenderer
import com.github.mikephil.charting.interfaces.datasets.ILineRadarDataSet
import com.github.mikephil.charting.interfaces.datasets.ILineScatterCandleRadarDataSet

/**
 * Entry class for the BarChart. (especially stacked bars)
 *
 * @author Philipp Jahoda
 */
@SuppressLint("ParcelCreator")
class BarEntry : Entry {
    /**
     * Returns the stacked values this BarEntry represents, or null, if only a single value is represented (then, use
     * getY()).
     *
     * @return
     */
    /**
     * the values the stacked barchart holds
     */
    var yVals: FloatArray?
        private set
    /**
     * Returns the ranges of the individual stack-entries. Will return null if this entry is not stacked.
     *
     * @return
     */
    /**
     * the ranges for the individual stack values - automatically calculated
     */
    var ranges: Array<Range?>
        private set
    /**
     * Returns the sum of all negative values this entry (if stacked) contains. (this is a positive number)
     *
     * @return
     */
    /**
     * the sum of all negative values this entry (if stacked) contains
     */
    var negativeSum = 0f
        private set
    /**
     * Reuturns the sum of all positive values this entry (if stacked) contains.
     *
     * @return
     */
    /**
     * the sum of all positive values this entry (if stacked) contains
     */
    var positiveSum = 0f
        private set

    /**
     * Constructor for normal bars (not stacked).
     *
     * @param x
     * @param y
     */
    constructor(x: Float, y: Float) : super(x, y) {}

    /**
     * Constructor for normal bars (not stacked).
     *
     * @param x
     * @param y
     * @param data - Spot for additional data this Entry represents.
     */
    constructor(x: Float, y: Float, data: Any?) : super(x, y, data) {}

    /**
     * Constructor for normal bars (not stacked).
     *
     * @param x
     * @param y
     * @param icon - icon image
     */
    constructor(x: Float, y: Float, icon: Drawable?) : super(x, y, icon) {}

    /**
     * Constructor for normal bars (not stacked).
     *
     * @param x
     * @param y
     * @param icon - icon image
     * @param data - Spot for additional data this Entry represents.
     */
    constructor(x: Float, y: Float, icon: Drawable?, data: Any?) : super(x, y, icon, data) {}

    /**
     * Constructor for stacked bar entries. One data object for whole stack
     *
     * @param x
     * @param vals - the stack values, use at least 2
     */
    constructor(x: Float, vals: FloatArray?) : super(x, calcSum(vals)) {
        yVals = vals
        calcPosNegSum()
        calcRanges()
    }

    /**
     * Constructor for stacked bar entries. One data object for whole stack
     *
     * @param x
     * @param vals - the stack values, use at least 2
     * @param data - Spot for additional data this Entry represents.
     */
    constructor(x: Float, vals: FloatArray?, data: Any?) : super(x, calcSum(vals), data) {
        yVals = vals
        calcPosNegSum()
        calcRanges()
    }

    /**
     * Constructor for stacked bar entries. One data object for whole stack
     *
     * @param x
     * @param vals - the stack values, use at least 2
     * @param icon - icon image
     */
    constructor(x: Float, vals: FloatArray?, icon: Drawable?) : super(x, calcSum(vals), icon) {
        yVals = vals
        calcPosNegSum()
        calcRanges()
    }

    /**
     * Constructor for stacked bar entries. One data object for whole stack
     *
     * @param x
     * @param vals - the stack values, use at least 2
     * @param icon - icon image
     * @param data - Spot for additional data this Entry represents.
     */
    constructor(x: Float, vals: FloatArray?, icon: Drawable?, data: Any?) : super(
        x,
        calcSum(vals),
        icon,
        data
    ) {
        yVals = vals
        calcPosNegSum()
        calcRanges()
    }

    /**
     * Returns an exact copy of the BarEntry.
     */
    override fun copy(): BarEntry {
        val copied = BarEntry(x, y, data)
        copied.setVals(yVals)
        return copied
    }

    /**
     * Set the array of values this BarEntry should represent.
     *
     * @param vals
     */
    fun setVals(vals: FloatArray?) {
        setY(calcSum(vals))
        yVals = vals
        calcPosNegSum()
        calcRanges()
    }

    /**
     * Returns the value of this BarEntry. If the entry is stacked, it returns the positive sum of all values.
     *
     * @return
     */
    override var y: Float
        get() = super.getY()
        set(y) {
            super.y = y
        }

    /**
     * Returns true if this BarEntry is stacked (has a values array), false if not.
     *
     * @return
     */
    val isStacked: Boolean
        get() = yVals != null

    /**
     * Use `getSumBelow(stackIndex)` instead.
     */
    @Deprecated("")
    fun getBelowSum(stackIndex: Int): Float {
        return getSumBelow(stackIndex)
    }

    fun getSumBelow(stackIndex: Int): Float {
        if (yVals == null) return 0
        var remainder = 0f
        var index = yVals!!.size - 1
        while (index > stackIndex && index >= 0) {
            remainder += yVals!![index]
            index--
        }
        return remainder
    }

    private fun calcPosNegSum() {
        if (yVals == null) {
            negativeSum = 0f
            positiveSum = 0f
            return
        }
        var sumNeg = 0f
        var sumPos = 0f
        for (f in yVals!!) {
            if (f <= 0f) sumNeg += Math.abs(f) else sumPos += f
        }
        negativeSum = sumNeg
        positiveSum = sumPos
    }

    protected fun calcRanges() {
        val values = yVals
        if (values == null || values.size == 0) return
        ranges = arrayOfNulls(values.size)
        var negRemain = -negativeSum
        var posRemain = 0f
        for (i in ranges.indices) {
            val value = values[i]
            if (value < 0) {
                ranges[i] = Range(negRemain, negRemain - value)
                negRemain -= value
            } else {
                ranges[i] = Range(posRemain, posRemain + value)
                posRemain += value
            }
        }
    }

    companion object {
        /**
         * Calculates the sum across all values of the given stack.
         *
         * @param vals
         * @return
         */
        private fun calcSum(vals: FloatArray?): Float {
            if (vals == null) return 0f
            var sum = 0f
            for (f in vals) sum += f
            return sum
        }
    }
}