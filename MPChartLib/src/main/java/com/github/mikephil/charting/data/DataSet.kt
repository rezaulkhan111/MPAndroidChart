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
import com.github.mikephil.charting.renderer.scatter.TriangleShapeRenderer
import com.github.mikephil.charting.interfaces.datasets.ILineRadarDataSet
import com.github.mikephil.charting.interfaces.datasets.ILineScatterCandleRadarDataSet
import java.util.ArrayList

/**
 * The DataSet class represents one group or type of entries (Entry) in the
 * Chart that belong together. It is designed to logically separate different
 * groups of values inside the Chart (e.g. the values for a specific line in the
 * LineChart, or the values of a specific group of bars in the BarChart).
 *
 * @author Philipp Jahoda
 */
abstract class DataSet<T : Entry?>(entries: MutableList<T?>?, label: String?) :
    BaseDataSet<T>(label) {
    /**
     * the entries that this DataSet represents / holds together
     */
    protected var mEntries: MutableList<T>?

    /**
     * maximum y-value in the value array
     */
    override var yMax = -Float.MAX_VALUE
        protected set

    /**
     * minimum y-value in the value array
     */
    override var yMin = Float.MAX_VALUE
        protected set

    /**
     * maximum x-value in the value array
     */
    override var xMax = -Float.MAX_VALUE
        protected set

    /**
     * minimum x-value in the value array
     */
    override var xMin = Float.MAX_VALUE
        protected set

    override fun calcMinMax() {
        yMax = -Float.MAX_VALUE
        yMin = Float.MAX_VALUE
        xMax = -Float.MAX_VALUE
        xMin = Float.MAX_VALUE
        if (mEntries == null || mEntries!!.isEmpty()) return
        for (e in mEntries!!) {
            calcMinMax(e)
        }
    }

    override fun calcMinMaxY(fromX: Float, toX: Float) {
        yMax = -Float.MAX_VALUE
        yMin = Float.MAX_VALUE
        if (mEntries == null || mEntries!!.isEmpty()) return
        val indexFrom = getEntryIndex(fromX, Float.NaN, Rounding.DOWN)
        val indexTo = getEntryIndex(toX, Float.NaN, Rounding.UP)
        if (indexTo < indexFrom) return
        for (i in indexFrom..indexTo) {

            // only recalculate y
            calcMinMaxY(mEntries!![i])
        }
    }

    /**
     * Updates the min and max x and y value of this DataSet based on the given Entry.
     *
     * @param e
     */
    protected open fun calcMinMax(e: T?) {
        if (e == null) return
        calcMinMaxX(e)
        calcMinMaxY(e)
    }

    protected fun calcMinMaxX(e: T) {
        if (e.getX() < xMin) xMin = e.getX()
        if (e.getX() > xMax) xMax = e.getX()
    }

    protected open fun calcMinMaxY(e: T) {
        if (e.getY() < yMin) yMin = e.getY()
        if (e.getY() > yMax) yMax = e.getY()
    }

    override val entryCount: Int
        get() = mEntries!!.size

    /**
     * This method is deprecated.
     * Use getEntries() instead.
     *
     * @return
     */
    @get:Deprecated("")
    val values: List<T>?
        get() = mEntries
    /**
     * Returns the array of entries that this DataSet represents.
     *
     * @return
     */
    /**
     * Sets the array of entries that this DataSet represents, and calls notifyDataSetChanged()
     *
     * @return
     */
    var entries: MutableList<T>?
        get() = mEntries
        set(entries) {
            mEntries = entries
            notifyDataSetChanged()
        }

    /**
     * This method is deprecated.
     * Use setEntries(...) instead.
     *
     * @param values
     */
    @Deprecated("")
    fun setValues(values: MutableList<T>?) {
        entries = values
    }

    /**
     * Provides an exact copy of the DataSet this method is used on.
     *
     * @return
     */
    abstract fun copy(): DataSet<T>

    /**
     *
     * @param dataSet
     */
    protected fun copy(dataSet: DataSet<*>?) {
        super.copy(dataSet!!)
    }

    override fun toString(): String {
        val buffer = StringBuffer()
        buffer.append(toSimpleString())
        for (i in mEntries!!.indices) {
            buffer.append(mEntries!![i].toString() + " ")
        }
        return buffer.toString()
    }

    /**
     * Returns a simple string representation of the DataSet with the type and
     * the number of Entries.
     *
     * @return
     */
    fun toSimpleString(): String {
        val buffer = StringBuffer()
        buffer.append(
            """
    DataSet, label: ${if (getLabel() == null) "" else getLabel()}, entries: ${mEntries!!.size}
    
    """.trimIndent()
        )
        return buffer.toString()
    }

    override fun addEntryOrdered(e: T) {
        if (e == null) return
        if (mEntries == null) {
            mEntries = ArrayList()
        }
        calcMinMax(e)
        if (mEntries!!.size > 0 && mEntries!![mEntries!!.size - 1].getX() > e.x) {
            val closestIndex = getEntryIndex(e.x, e.y, Rounding.UP)
            mEntries!!.add(closestIndex, e)
        } else {
            mEntries!!.add(e)
        }
    }

    override fun clear() {
        mEntries!!.clear()
        notifyDataSetChanged()
    }

    override fun addEntry(e: T): Boolean {
        if (e == null) return false
        var values = entries
        if (values == null) {
            values = ArrayList()
        }
        calcMinMax(e)

        // add the entry
        return values.add(e)
    }

    override fun removeEntry(e: T): Boolean {
        if (e == null) return false
        if (mEntries == null) return false

        // remove the entry
        val removed = mEntries!!.remove(e)
        if (removed) {
            calcMinMax()
        }
        return removed
    }

    override fun getEntryIndex(e: Entry): Int {
        return mEntries!!.indexOf(e)
    }

    override fun getEntryForXValue(xValue: Float, closestToY: Float, rounding: Rounding?): T {
        val index = getEntryIndex(xValue, closestToY, rounding)
        return if (index > -1) mEntries!![index] else null
    }

    override fun getEntryForXValue(xValue: Float, closestToY: Float): T {
        return getEntryForXValue(xValue, closestToY, Rounding.CLOSEST)
    }

    override fun getEntryForIndex(index: Int): T {
        return mEntries!![index]
    }

    override fun getEntryIndex(xValue: Float, closestToY: Float, rounding: Rounding?): Int {
        if (mEntries == null || mEntries!!.isEmpty()) return -1
        var low = 0
        var high = mEntries!!.size - 1
        var closest = high
        while (low < high) {
            val m = (low + high) / 2
            val d1 = mEntries!![m].getX() - xValue
            val d2 = mEntries!![m + 1].getX() - xValue
            val ad1 = Math.abs(d1)
            val ad2 = Math.abs(d2)
            if (ad2 < ad1) {
                // [m + 1] is closer to xValue
                // Search in an higher place
                low = m + 1
            } else if (ad1 < ad2) {
                // [m] is closer to xValue
                // Search in a lower place
                high = m
            } else {
                // We have multiple sequential x-value with same distance
                if (d1 >= 0.0) {
                    // Search in a lower place
                    high = m
                } else if (d1 < 0.0) {
                    // Search in an higher place
                    low = m + 1
                }
            }
            closest = high
        }
        if (closest != -1) {
            val closestXValue = mEntries!![closest].getX()
            if (rounding == Rounding.UP) {
                // If rounding up, and found x-value is lower than specified x, and we can go upper...
                if (closestXValue < xValue && closest < mEntries!!.size - 1) {
                    ++closest
                }
            } else if (rounding == Rounding.DOWN) {
                // If rounding down, and found x-value is upper than specified x, and we can go lower...
                if (closestXValue > xValue && closest > 0) {
                    --closest
                }
            }

            // Search by closest to y-value
            if (!java.lang.Float.isNaN(closestToY)) {
                while (closest > 0 && mEntries!![closest - 1].getX() == closestXValue) closest -= 1
                var closestYValue = mEntries!![closest].getY()
                var closestYIndex = closest
                while (true) {
                    closest += 1
                    if (closest >= mEntries!!.size) break
                    val value: Entry = mEntries!![closest]
                    if (value.x != closestXValue) break
                    if (Math.abs(value.y - closestToY) <= Math.abs(closestYValue - closestToY)) {
                        closestYValue = closestToY
                        closestYIndex = closest
                    }
                }
                closest = closestYIndex
            }
        }
        return closest
    }

    override fun getEntriesForXValue(xValue: Float): List<T>? {
        val entries: MutableList<T> = ArrayList()
        var low = 0
        var high = mEntries!!.size - 1
        while (low <= high) {
            var m = (high + low) / 2
            var entry = mEntries!![m]

            // if we have a match
            if (xValue == entry.getX()) {
                while (m > 0 && mEntries!![m - 1].getX() == xValue) m--
                high = mEntries!!.size

                // loop over all "equal" entries
                while (m < high) {
                    entry = mEntries!![m]
                    if (entry.getX() == xValue) {
                        entries.add(entry)
                    } else {
                        break
                    }
                    m++
                }
                break
            } else {
                if (xValue > entry.getX()) low = m + 1 else high = m - 1
            }
        }
        return entries
    }

    /**
     * Determines how to round DataSet index values for
     * [DataSet.getEntryIndex] DataSet.getEntryIndex()}
     * when an exact x-index is not found.
     */
    enum class Rounding {
        UP, DOWN, CLOSEST
    }

    /**
     * Creates a new DataSet object with the given values (entries) it represents. Also, a
     * label that describes the DataSet can be specified. The label can also be
     * used to retrieve the DataSet from a ChartData object.
     *
     * @param entries
     * @param label
     */
    init {
        mEntries = entries
        if (mEntries == null) mEntries = ArrayList()
        calcMinMax()
    }
}