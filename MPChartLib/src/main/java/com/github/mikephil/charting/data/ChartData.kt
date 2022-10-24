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
import android.util.Log
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
import com.github.mikephil.charting.highlight.Highlight
import com.github.mikephil.charting.renderer.scatter.TriangleShapeRenderer
import com.github.mikephil.charting.interfaces.datasets.ILineRadarDataSet
import com.github.mikephil.charting.interfaces.datasets.ILineScatterCandleRadarDataSet
import java.util.ArrayList

/**
 * Class that holds all relevant data that represents the chart. That involves
 * at least one (or more) DataSets, and an array of x-values.
 *
 * @author Philipp Jahoda
 */
abstract class ChartData<T : IDataSet<out Entry?>?> {
    /**
     * Returns the greatest y-value the data object contains.
     *
     * @return
     */
    /**
     * maximum y-value in the value array across all axes
     */
    var yMax = -Float.MAX_VALUE
        protected set
    /**
     * Returns the smallest y-value the data object contains.
     *
     * @return
     */
    /**
     * the minimum y-value in the value array across all axes
     */
    var yMin = Float.MAX_VALUE
        protected set
    /**
     * Returns the maximum x-value this data object contains.
     *
     * @return
     */
    /**
     * maximum x-value in the value array
     */
    var xMax = -Float.MAX_VALUE
        protected set
    /**
     * Returns the minimum x-value this data object contains.
     *
     * @return
     */
    /**
     * minimum x-value in the value array
     */
    var xMin = Float.MAX_VALUE
        protected set
    protected var mLeftAxisMax = -Float.MAX_VALUE
    protected var mLeftAxisMin = Float.MAX_VALUE
    protected var mRightAxisMax = -Float.MAX_VALUE
    protected var mRightAxisMin = Float.MAX_VALUE

    /**
     * array that holds all DataSets the ChartData object represents
     */
    protected var mDataSets: MutableList<T>?

    /**
     * Default constructor.
     */
    constructor() {
        mDataSets = ArrayList()
    }

    /**
     * Constructor taking single or multiple DataSet objects.
     *
     * @param dataSets
     */
    constructor(vararg dataSets: T) {
        mDataSets = arrayToList(dataSets)
        notifyDataChanged()
    }

    /**
     * Created because Arrays.asList(...) does not support modification.
     *
     * @param array
     * @return
     */
    private fun arrayToList(array: Array<T>): MutableList<T> {
        val list: MutableList<T> = ArrayList()
        for (set in array) {
            list.add(set)
        }
        return list
    }

    /**
     * constructor for chart data
     *
     * @param sets the dataset array
     */
    constructor(sets: MutableList<T>?) {
        mDataSets = sets
        notifyDataChanged()
    }

    /**
     * Call this method to let the ChartData know that the underlying data has
     * changed. Calling this performs all necessary recalculations needed when
     * the contained data has changed.
     */
    open fun notifyDataChanged() {
        calcMinMax()
    }

    /**
     * Calc minimum and maximum y-values over all DataSets.
     * Tell DataSets to recalculate their min and max y-values, this is only needed for autoScaleMinMax.
     *
     * @param fromX the x-value to start the calculation from
     * @param toX   the x-value to which the calculation should be performed
     */
    fun calcMinMaxY(fromX: Float, toX: Float) {
        for (set in mDataSets!!) {
            set!!.calcMinMaxY(fromX, toX)
        }

        // apply the new data
        calcMinMax()
    }

    /**
     * Calc minimum and maximum values (both x and y) over all DataSets.
     */
    open fun calcMinMax() {
        if (mDataSets == null) return
        yMax = -Float.MAX_VALUE
        yMin = Float.MAX_VALUE
        xMax = -Float.MAX_VALUE
        xMin = Float.MAX_VALUE
        for (set in mDataSets!!) {
            calcMinMax(set)
        }
        mLeftAxisMax = -Float.MAX_VALUE
        mLeftAxisMin = Float.MAX_VALUE
        mRightAxisMax = -Float.MAX_VALUE
        mRightAxisMin = Float.MAX_VALUE

        // left axis
        val firstLeft = getFirstLeft(mDataSets!!)
        if (firstLeft != null) {
            mLeftAxisMax = firstLeft.yMax
            mLeftAxisMin = firstLeft.yMin
            for (dataSet in mDataSets!!) {
                if (dataSet!!.axisDependency == AxisDependency.LEFT) {
                    if (dataSet.yMin < mLeftAxisMin) mLeftAxisMin = dataSet.yMin
                    if (dataSet.yMax > mLeftAxisMax) mLeftAxisMax = dataSet.yMax
                }
            }
        }

        // right axis
        val firstRight = getFirstRight(mDataSets!!)
        if (firstRight != null) {
            mRightAxisMax = firstRight.yMax
            mRightAxisMin = firstRight.yMin
            for (dataSet in mDataSets!!) {
                if (dataSet!!.axisDependency == AxisDependency.RIGHT) {
                    if (dataSet.yMin < mRightAxisMin) mRightAxisMin = dataSet.yMin
                    if (dataSet.yMax > mRightAxisMax) mRightAxisMax = dataSet.yMax
                }
            }
        }
    }
    /** ONLY GETTERS AND SETTERS BELOW THIS  */
    /**
     * returns the number of LineDataSets this object contains
     *
     * @return
     */
    val dataSetCount: Int
        get() = if (mDataSets == null) 0 else mDataSets!!.size

    /**
     * Returns the minimum y-value for the specified axis.
     *
     * @param axis
     * @return
     */
    fun getYMin(axis: AxisDependency): Float {
        return if (axis == AxisDependency.LEFT) {
            if (mLeftAxisMin == Float.MAX_VALUE) {
                mRightAxisMin
            } else mLeftAxisMin
        } else {
            if (mRightAxisMin == Float.MAX_VALUE) {
                mLeftAxisMin
            } else mRightAxisMin
        }
    }

    /**
     * Returns the maximum y-value for the specified axis.
     *
     * @param axis
     * @return
     */
    fun getYMax(axis: AxisDependency): Float {
        return if (axis == AxisDependency.LEFT) {
            if (mLeftAxisMax == -Float.MAX_VALUE) {
                mRightAxisMax
            } else mLeftAxisMax
        } else {
            if (mRightAxisMax == -Float.MAX_VALUE) {
                mLeftAxisMax
            } else mRightAxisMax
        }
    }

    /**
     * Returns all DataSet objects this ChartData object holds.
     *
     * @return
     */
    open val dataSets: List<T>?
        get() = mDataSets

    /**
     * Retrieve the index of a DataSet with a specific label from the ChartData.
     * Search can be case sensitive or not. IMPORTANT: This method does
     * calculations at runtime, do not over-use in performance critical
     * situations.
     *
     * @param dataSets   the DataSet array to search
     * @param label
     * @param ignorecase if true, the search is not case-sensitive
     * @return
     */
    protected fun getDataSetIndexByLabel(
        dataSets: List<T>?, label: String,
        ignorecase: Boolean
    ): Int {
        if (ignorecase) {
            for (i in dataSets!!.indices) if (label.equals(
                    dataSets[i]!!.label,
                    ignoreCase = true
                )
            ) return i
        } else {
            for (i in dataSets!!.indices) if (label == dataSets[i]!!.label) return i
        }
        return -1
    }

    /**
     * Returns the labels of all DataSets as a string array.
     *
     * @return
     */
    val dataSetLabels: Array<String?>
        get() {
            val types = arrayOfNulls<String>(mDataSets!!.size)
            for (i in mDataSets!!.indices) {
                types[i] = mDataSets!![i]!!.label
            }
            return types
        }

    /**
     * Get the Entry for a corresponding highlight object
     *
     * @param highlight
     * @return the entry that is highlighted
     */
    open fun getEntryForHighlight(highlight: Highlight): Entry? {
        return if (highlight.dataSetIndex >= mDataSets!!.size) null else {
            mDataSets!![highlight.dataSetIndex]!!
                .getEntryForXValue(highlight.x, highlight.y)
        }
    }

    /**
     * Returns the DataSet object with the given label. Search can be case
     * sensitive or not. IMPORTANT: This method does calculations at runtime.
     * Use with care in performance critical situations.
     *
     * @param label
     * @param ignorecase
     * @return
     */
    open fun getDataSetByLabel(label: String, ignorecase: Boolean): T? {
        val index = getDataSetIndexByLabel(mDataSets, label, ignorecase)
        return if (index < 0 || index >= mDataSets!!.size) null else mDataSets!![index]
    }

    open fun getDataSetByIndex(index: Int): T? {
        return if (mDataSets == null || index < 0 || index >= mDataSets!!.size) null else mDataSets!![index]
    }

    /**
     * Adds a DataSet dynamically.
     *
     * @param d
     */
    fun addDataSet(d: T?) {
        if (d == null) return
        calcMinMax(d)
        mDataSets!!.add(d)
    }

    /**
     * Removes the given DataSet from this data object. Also recalculates all
     * minimum and maximum values. Returns true if a DataSet was removed, false
     * if no DataSet could be removed.
     *
     * @param d
     */
    open fun removeDataSet(d: T?): Boolean {
        if (d == null) return false
        val removed = mDataSets!!.remove(d)

        // if a DataSet was removed
        if (removed) {
            notifyDataChanged()
        }
        return removed
    }

    /**
     * Removes the DataSet at the given index in the DataSet array from the data
     * object. Also recalculates all minimum and maximum values. Returns true if
     * a DataSet was removed, false if no DataSet could be removed.
     *
     * @param index
     */
    open fun removeDataSet(index: Int): Boolean {
        if (index >= mDataSets!!.size || index < 0) return false
        val set = mDataSets!![index]
        return removeDataSet(set)
    }

    /**
     * Adds an Entry to the DataSet at the specified index.
     * Entries are added to the end of the list.
     *
     * @param e
     * @param dataSetIndex
     */
    fun addEntry(e: Entry, dataSetIndex: Int) {
        if (mDataSets!!.size > dataSetIndex && dataSetIndex >= 0) {
            val set: IDataSet<*> = mDataSets!![dataSetIndex]
            // add the entry to the dataset
            if (!set.addEntry(e)) return
            calcMinMax(e, set.axisDependency)
        } else {
            Log.e("addEntry", "Cannot add Entry because dataSetIndex too high or too low.")
        }
    }

    /**
     * Adjusts the current minimum and maximum values based on the provided Entry object.
     *
     * @param e
     * @param axis
     */
    protected fun calcMinMax(e: Entry, axis: AxisDependency?) {
        if (yMax < e.y) yMax = e.y
        if (yMin > e.y) yMin = e.y
        if (xMax < e.x) xMax = e.x
        if (xMin > e.x) xMin = e.x
        if (axis == AxisDependency.LEFT) {
            if (mLeftAxisMax < e.y) mLeftAxisMax = e.y
            if (mLeftAxisMin > e.y) mLeftAxisMin = e.y
        } else {
            if (mRightAxisMax < e.y) mRightAxisMax = e.y
            if (mRightAxisMin > e.y) mRightAxisMin = e.y
        }
    }

    /**
     * Adjusts the minimum and maximum values based on the given DataSet.
     *
     * @param d
     */
    protected fun calcMinMax(d: T) {
        if (yMax < d!!.yMax) yMax = d.yMax
        if (yMin > d.yMin) yMin = d.yMin
        if (xMax < d.xMax) xMax = d.xMax
        if (xMin > d.xMin) xMin = d.xMin
        if (d.axisDependency == AxisDependency.LEFT) {
            if (mLeftAxisMax < d.yMax) mLeftAxisMax = d.yMax
            if (mLeftAxisMin > d.yMin) mLeftAxisMin = d.yMin
        } else {
            if (mRightAxisMax < d.yMax) mRightAxisMax = d.yMax
            if (mRightAxisMin > d.yMin) mRightAxisMin = d.yMin
        }
    }

    /**
     * Removes the given Entry object from the DataSet at the specified index.
     *
     * @param e
     * @param dataSetIndex
     */
    open fun removeEntry(e: Entry?, dataSetIndex: Int): Boolean {

        // entry null, outofbounds
        if (e == null || dataSetIndex >= mDataSets!!.size) return false
        val set: IDataSet<*>? = mDataSets!![dataSetIndex]
        return if (set != null) {
            // remove the entry from the dataset
            val removed = set.removeEntry(e)
            if (removed) {
                notifyDataChanged()
            }
            removed
        } else false
    }

    /**
     * Removes the Entry object closest to the given DataSet at the
     * specified index. Returns true if an Entry was removed, false if no Entry
     * was found that meets the specified requirements.
     *
     * @param xValue
     * @param dataSetIndex
     * @return
     */
    open fun removeEntry(xValue: Float, dataSetIndex: Int): Boolean {
        if (dataSetIndex >= mDataSets!!.size) return false
        val dataSet: IDataSet<*> = mDataSets!![dataSetIndex]
        val e = dataSet.getEntryForXValue(xValue, Float.NaN) ?: return false
        return removeEntry(e, dataSetIndex)
    }

    /**
     * Returns the DataSet that contains the provided Entry, or null, if no
     * DataSet contains this Entry.
     *
     * @param e
     * @return
     */
    fun getDataSetForEntry(e: Entry?): T? {
        if (e == null) return null
        for (i in mDataSets!!.indices) {
            val set = mDataSets!![i]
            for (j in 0 until set!!.entryCount) {
                if (e.equalTo(set.getEntryForXValue(e.x, e.y))) return set
            }
        }
        return null
    }

    /**
     * Returns all colors used across all DataSet objects this object
     * represents.
     *
     * @return
     */
    val colors: IntArray?
        get() {
            if (mDataSets == null) return null
            var clrcnt = 0
            for (i in mDataSets!!.indices) {
                clrcnt += mDataSets!![i]!!.colors!!.size
            }
            val colors = IntArray(clrcnt)
            var cnt = 0
            for (i in mDataSets!!.indices) {
                val clrs = mDataSets!![i]!!.colors
                for (clr in clrs!!) {
                    colors[cnt] = clr!!
                    cnt++
                }
            }
            return colors
        }

    /**
     * Returns the index of the provided DataSet in the DataSet array of this data object, or -1 if it does not exist.
     *
     * @param dataSet
     * @return
     */
    fun getIndexOfDataSet(dataSet: T): Int {
        return mDataSets!!.indexOf(dataSet)
    }

    /**
     * Returns the first DataSet from the datasets-array that has it's dependency on the left axis.
     * Returns null if no DataSet with left dependency could be found.
     *
     * @return
     */
    protected fun getFirstLeft(sets: List<T>): T? {
        for (dataSet in sets) {
            if (dataSet!!.axisDependency == AxisDependency.LEFT) return dataSet
        }
        return null
    }

    /**
     * Returns the first DataSet from the datasets-array that has it's dependency on the right axis.
     * Returns null if no DataSet with right dependency could be found.
     *
     * @return
     */
    fun getFirstRight(sets: List<T>): T? {
        for (dataSet in sets) {
            if (dataSet!!.axisDependency == AxisDependency.RIGHT) return dataSet
        }
        return null
    }

    /**
     * Sets a custom IValueFormatter for all DataSets this data object contains.
     *
     * @param f
     */
    fun setValueFormatter(f: IValueFormatter?) {
        if (f == null) return else {
            for (set in mDataSets!!) {
                set.valueFormatter = f
            }
        }
    }

    /**
     * Sets the color of the value-text (color in which the value-labels are
     * drawn) for all DataSets this data object contains.
     *
     * @param color
     */
    fun setValueTextColor(color: Int) {
        for (set in mDataSets!!) {
            set.valueTextColor = color
        }
    }

    /**
     * Sets the same list of value-colors for all DataSets this
     * data object contains.
     *
     * @param colors
     */
    fun setValueTextColors(colors: List<Int?>?) {
        for (set in mDataSets!!) {
            set.setValueTextColors(colors)
        }
    }

    /**
     * Sets the Typeface for all value-labels for all DataSets this data object
     * contains.
     *
     * @param tf
     */
    fun setValueTypeface(tf: Typeface?) {
        for (set in mDataSets!!) {
            set.valueTypeface = tf
        }
    }

    /**
     * Sets the size (in dp) of the value-text for all DataSets this data object
     * contains.
     *
     * @param size
     */
    fun setValueTextSize(size: Float) {
        for (set in mDataSets!!) {
            set.valueTextSize = size
        }
    }

    /**
     * Enables / disables drawing values (value-text) for all DataSets this data
     * object contains.
     *
     * @param enabled
     */
    fun setDrawValues(enabled: Boolean) {
        for (set in mDataSets!!) {
            set.setDrawValues(enabled)
        }
    }
    /**
     * Returns true if highlighting of all underlying values is enabled, false
     * if not.
     *
     * @return
     */
    /**
     * Enables / disables highlighting values for all DataSets this data object
     * contains. If set to true, this means that values can
     * be highlighted programmatically or by touch gesture.
     */
    var isHighlightEnabled: Boolean
        get() {
            for (set in mDataSets!!) {
                if (!set.isHighlightEnabled) return false
            }
            return true
        }
        set(enabled) {
            for (set in mDataSets!!) {
                set.isHighlightEnabled = enabled
            }
        }

    /**
     * Clears this data object from all DataSets and removes all Entries. Don't
     * forget to invalidate the chart after this.
     */
    fun clearValues() {
        if (mDataSets != null) {
            mDataSets!!.clear()
        }
        notifyDataChanged()
    }

    /**
     * Checks if this data object contains the specified DataSet. Returns true
     * if so, false if not.
     *
     * @param dataSet
     * @return
     */
    operator fun contains(dataSet: T): Boolean {
        for (set in mDataSets!!) {
            if (set == dataSet) return true
        }
        return false
    }

    /**
     * Returns the total entry count across all DataSet objects this data object contains.
     *
     * @return
     */
    val entryCount: Int
        get() {
            var count = 0
            for (set in mDataSets!!) {
                count += set!!.entryCount
            }
            return count
        }

    /**
     * Returns the DataSet object with the maximum number of entries or null if there are no DataSets.
     *
     * @return
     */
    val maxEntryCountSet: T?
        get() {
            if (mDataSets == null || mDataSets!!.isEmpty()) return null
            var max = mDataSets!![0]
            for (set in mDataSets!!) {
                if (set!!.entryCount > max!!.entryCount) max = set
            }
            return max
        }
}