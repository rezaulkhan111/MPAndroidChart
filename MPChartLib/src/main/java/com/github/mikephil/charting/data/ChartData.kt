package com.github.mikephil.charting.data

import android.graphics.Typeface
import android.util.Log
import com.github.mikephil.charting.components.YAxis.AxisDependency
import com.github.mikephil.charting.formatter.IValueFormatter
import com.github.mikephil.charting.highlight.Highlight
import com.github.mikephil.charting.interfaces.datasets.IDataSet

/**
 * Class that holds all relevant data that represents the chart. That involves
 * at least one (or more) DataSets, and an array of x-values.
 *
 * @author Philipp Jahoda
 */
abstract class ChartData<T : IDataSet<out Entry?>?> {
    /**
     * maximum y-value in the value array across all axes
     */
    protected var mYMax = -Float.MAX_VALUE

    /**
     * the minimum y-value in the value array across all axes
     */
    protected var mYMin = Float.MAX_VALUE

    /**
     * maximum x-value in the value array
     */
    protected var mXMax = -Float.MAX_VALUE

    /**
     * minimum x-value in the value array
     */
    protected var mXMin = Float.MAX_VALUE


    protected var mLeftAxisMax = -Float.MAX_VALUE

    protected var mLeftAxisMin = Float.MAX_VALUE

    protected var mRightAxisMax = -Float.MAX_VALUE

    protected var mRightAxisMin = Float.MAX_VALUE

    /**
     * array that holds all DataSets the ChartData object represents
     */
    protected var mDataSets: MutableList<T>? = null

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
    private fun arrayToList(array: Array<out T>): MutableList<T> {
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
    open fun calcMinMaxY(fromX: Float, toX: Float) {
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
        mYMax = -Float.MAX_VALUE
        mYMin = Float.MAX_VALUE
        mXMax = -Float.MAX_VALUE
        mXMin = Float.MAX_VALUE
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
            mLeftAxisMax = firstLeft.getYMax()
            mLeftAxisMin = firstLeft.getYMin()
            for (dataSet in mDataSets!!) {
                if (dataSet!!.getAxisDependency() == AxisDependency.LEFT) {
                    if (dataSet.getYMin() < mLeftAxisMin) mLeftAxisMin = dataSet.getYMin()
                    if (dataSet.getYMax() > mLeftAxisMax) mLeftAxisMax = dataSet.getYMax()
                }
            }
        }

        // right axis
        val firstRight = getFirstRight(mDataSets!!)
        if (firstRight != null) {
            mRightAxisMax = firstRight.getYMax()
            mRightAxisMin = firstRight.getYMin()
            for (dataSet in mDataSets!!) {
                if (dataSet!!.getAxisDependency() == AxisDependency.RIGHT) {
                    if (dataSet.getYMin() < mRightAxisMin) mRightAxisMin = dataSet.getYMin()
                    if (dataSet.getYMax() > mRightAxisMax) mRightAxisMax = dataSet.getYMax()
                }
            }
        }
    }

    /** ONLY GETTERS AND SETTERS BELOW THIS */

    /** ONLY GETTERS AND SETTERS BELOW THIS  */
    /**
     * returns the number of LineDataSets this object contains
     *
     * @return
     */
    open fun getDataSetCount(): Int {
        return if (mDataSets == null) 0 else mDataSets!!.size
    }

    /**
     * Returns the smallest y-value the data object contains.
     *
     * @return
     */
    open fun getYMin(): Float {
        return mYMin
    }

    /**
     * Returns the minimum y-value for the specified axis.
     *
     * @param axis
     * @return
     */
    open fun getYMin(axis: AxisDependency): Float {
        return if (axis === AxisDependency.LEFT) {
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
     * Returns the greatest y-value the data object contains.
     *
     * @return
     */
    open fun getYMax(): Float {
        return mYMax
    }

    /**
     * Returns the maximum y-value for the specified axis.
     *
     * @param axis
     * @return
     */
    open fun getYMax(axis: AxisDependency): Float {
        return if (axis === AxisDependency.LEFT) {
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
     * Returns the minimum x-value this data object contains.
     *
     * @return
     */
    open fun getXMin(): Float {
        return mXMin
    }

    /**
     * Returns the maximum x-value this data object contains.
     *
     * @return
     */
    open fun getXMax(): Float {
        return mXMax
    }

    /**
     * Returns all DataSet objects this ChartData object holds.
     *
     * @return
     */
    open fun getDataSets(): MutableList<T>? {
        return mDataSets
    }

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
    protected open fun getDataSetIndexByLabel(
        dataSets: MutableList<T>?, label: String,
        ignorecase: Boolean
    ): Int {
        if (ignorecase) {
            for (i in dataSets!!.indices) if (label.equals(
                    dataSets[i]!!.getLabel(),
                    ignoreCase = true
                )
            ) return i
        } else {
            for (i in dataSets!!.indices) if (label == dataSets[i]!!.getLabel()) return i
        }
        return -1
    }

    /**
     * Returns the labels of all DataSets as a string array.
     *
     * @return
     */
    open fun getDataSetLabels(): Array<String?>? {
        val types = arrayOfNulls<String>(mDataSets!!.size)
        for (i in mDataSets!!.indices) {
            types[i] = mDataSets!![i]!!.getLabel()
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
        return if (highlight.getDataSetIndex() >= mDataSets!!.size) null else {
            mDataSets!![highlight.getDataSetIndex()]!!.getEntryForXValue(
                highlight.getX(),
                highlight.getY()
            )
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
    open fun addDataSet(d: T?) {
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
    open fun addEntry(e: Entry, dataSetIndex: Int) {
        if (mDataSets!!.size > dataSetIndex && dataSetIndex >= 0) {
            val set: IDataSet<*> = mDataSets!![dataSetIndex]!!
            // add the entry to the dataset
            if (!set.addEntry(e as Nothing)) return
            calcMinMax(e, set.getAxisDependency()!!)
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
    protected open fun calcMinMax(e: Entry, axis: AxisDependency) {
        if (mYMax < e.getY()) mYMax = e.getY()
        if (mYMin > e.getY()) mYMin = e.getY()
        if (mXMax < e.getX()) mXMax = e.getX()
        if (mXMin > e.getX()) mXMin = e.getX()
        if (axis == AxisDependency.LEFT) {
            if (mLeftAxisMax < e.getY()) mLeftAxisMax = e.getY()
            if (mLeftAxisMin > e.getY()) mLeftAxisMin = e.getY()
        } else {
            if (mRightAxisMax < e.getY()) mRightAxisMax = e.getY()
            if (mRightAxisMin > e.getY()) mRightAxisMin = e.getY()
        }
    }

    /**
     * Adjusts the minimum and maximum values based on the given DataSet.
     *
     * @param d
     */
    protected open fun calcMinMax(d: T) {
        if (mYMax < d!!.getYMax()) mYMax = d.getYMax()
        if (mYMin > d.getYMin()) mYMin = d.getYMin()
        if (mXMax < d.getXMax()) mXMax = d.getXMax()
        if (mXMin > d.getXMin()) mXMin = d.getXMin()
        if (d.getAxisDependency() == AxisDependency.LEFT) {
            if (mLeftAxisMax < d.getYMax()) mLeftAxisMax = d.getYMax()
            if (mLeftAxisMin > d.getYMin()) mLeftAxisMin = d.getYMin()
        } else {
            if (mRightAxisMax < d.getYMax()) mRightAxisMax = d.getYMax()
            if (mRightAxisMin > d.getYMin()) mRightAxisMin = d.getYMin()
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
        val set: IDataSet<*> = mDataSets!![dataSetIndex]!!
        return if (set != null) {
            // remove the entry from the dataset
            val removed: Boolean = set.removeEntry(e as Nothing)
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
        val dataSet: IDataSet<*> = mDataSets!![dataSetIndex]!!
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
    open fun getDataSetForEntry(e: Entry?): T? {
        if (e == null) return null
        for (i in mDataSets!!.indices) {
            val set = mDataSets!![i]
            for (j in 0 until set!!.getEntryCount()) {
                if (e.equalTo(set.getEntryForXValue(e.getX(), e.getY()))) return set
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
    open fun getColors(): IntArray? {
        if (mDataSets == null) return null
        var clrcnt = 0
        for (i in mDataSets!!.indices) {
            clrcnt += mDataSets!![i]!!.getColors()!!.size
        }
        val colors = IntArray(clrcnt)
        var cnt = 0
        for (i in mDataSets!!.indices) {
            val clrs: List<Int> = mDataSets!![i]!!.getColors()!!
            for (clr in clrs) {
                colors[cnt] = clr
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
    open fun getIndexOfDataSet(dataSet: T): Int {
        return mDataSets!!.indexOf(dataSet)
    }

    /**
     * Returns the first DataSet from the datasets-array that has it's dependency on the left axis.
     * Returns null if no DataSet with left dependency could be found.
     *
     * @return
     */
    protected open fun getFirstLeft(sets: List<T>): T? {
        for (dataSet in sets) {
            if (dataSet!!.getAxisDependency() == AxisDependency.LEFT) return dataSet
        }
        return null
    }

    /**
     * Returns the first DataSet from the datasets-array that has it's dependency on the right axis.
     * Returns null if no DataSet with right dependency could be found.
     *
     * @return
     */
    open fun getFirstRight(sets: MutableList<T>): T? {
        for (dataSet in sets) {
            if (dataSet!!.getAxisDependency() == AxisDependency.RIGHT) return dataSet
        }
        return null
    }

    /**
     * Sets a custom IValueFormatter for all DataSets this data object contains.
     *
     * @param f
     */
    open fun setValueFormatter(f: IValueFormatter?) {
        if (f == null) return else {
            for (set in mDataSets!!) {
                set!!.setValueFormatter(f)
            }
        }
    }

    /**
     * Sets the color of the value-text (color in which the value-labels are
     * drawn) for all DataSets this data object contains.
     *
     * @param color
     */
    open fun setValueTextColor(color: Int) {
        for (set in mDataSets!!) {
            set!!.setValueTextColor(color)
        }
    }

    /**
     * Sets the same list of value-colors for all DataSets this
     * data object contains.
     *
     * @param colors
     */
    open fun setValueTextColors(colors: MutableList<Int>) {
        for (set in mDataSets!!) {
            set!!.setValueTextColors(colors)
        }
    }

    /**
     * Sets the Typeface for all value-labels for all DataSets this data object
     * contains.
     *
     * @param tf
     */
    open fun setValueTypeface(tf: Typeface?) {
        for (set in mDataSets!!) {
            set!!.setValueTypeface(tf!!)
        }
    }

    /**
     * Sets the size (in dp) of the value-text for all DataSets this data object
     * contains.
     *
     * @param size
     */
    open fun setValueTextSize(size: Float) {
        for (set in mDataSets!!) {
            set!!.setValueTextSize(size)
        }
    }

    /**
     * Enables / disables drawing values (value-text) for all DataSets this data
     * object contains.
     *
     * @param enabled
     */
    open fun setDrawValues(enabled: Boolean) {
        for (set in mDataSets!!) {
            set!!.setDrawValues(enabled)
        }
    }

    /**
     * Enables / disables highlighting values for all DataSets this data object
     * contains. If set to true, this means that values can
     * be highlighted programmatically or by touch gesture.
     */
    open fun setHighlightEnabled(enabled: Boolean) {
        for (set in mDataSets!!) {
            set!!.setHighlightEnabled(enabled)
        }
    }

    /**
     * Returns true if highlighting of all underlying values is enabled, false
     * if not.
     *
     * @return
     */
    open fun isHighlightEnabled(): Boolean {
        for (set in mDataSets!!) {
            if (!set!!.isHighlightEnabled()) return false
        }
        return true
    }

    /**
     * Clears this data object from all DataSets and removes all Entries. Don't
     * forget to invalidate the chart after this.
     */
    open fun clearValues() {
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
    open operator fun contains(dataSet: T): Boolean {
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
    open fun getEntryCount(): Int {
        var count = 0
        for (set in mDataSets!!) {
            count += set!!.getEntryCount()
        }
        return count
    }

    /**
     * Returns the DataSet object with the maximum number of entries or null if there are no DataSets.
     *
     * @return
     */
    open fun getMaxEntryCountSet(): T? {
        if (mDataSets == null || mDataSets!!.isEmpty()) return null
        var max = mDataSets!![0]
        for (set in mDataSets!!) {
            if (set!!.getEntryCount() > max!!.getEntryCount()) max = set
        }
        return max
    }
}