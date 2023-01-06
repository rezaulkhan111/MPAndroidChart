package com.github.mikephil.charting.interfaces.datasets

import android.graphics.DashPathEffect
import android.graphics.Typeface
import com.github.mikephil.charting.components.Legend.LegendForm
import com.github.mikephil.charting.components.YAxis.AxisDependency
import com.github.mikephil.charting.data.DataSet.Rounding
import com.github.mikephil.charting.data.Entry
import com.github.mikephil.charting.formatter.IValueFormatter
import com.github.mikephil.charting.utils.MPPointF

/**
 * Created by Philipp Jahoda on 21/10/15.
 */
interface IDataSet<T : Entry?> {

    /** ###### ###### DATA RELATED METHODS ###### ######  */

    /**
     * returns the minimum y-value this DataSet holds
     *
     * @return
     */
    fun getYMin(): Float

    /**
     * returns the maximum y-value this DataSet holds
     *
     * @return
     */
    fun getYMax(): Float

    /**
     * returns the minimum x-value this DataSet holds
     *
     * @return
     */
    fun getXMin(): Float

    /**
     * returns the maximum x-value this DataSet holds
     *
     * @return
     */
    fun getXMax(): Float

    /**
     * Returns the number of y-values this DataSet represents -> the size of the y-values array
     * -> yvals.size()
     *
     * @return
     */
    fun getEntryCount(): Int

    /**
     * Calculates the minimum and maximum x and y values (mXMin, mXMax, mYMin, mYMax).
     */
    fun calcMinMax()

    /**
     * Calculates the min and max y-values from the Entry closest to the given fromX to the Entry closest to the given toX value.
     * This is only needed for the autoScaleMinMax feature.
     *
     * @param fromX
     * @param toX
     */
    fun calcMinMaxY(fromX: Float, toX: Float)

    /**
     * Returns the first Entry object found at the given x-value with binary
     * search.
     * If the no Entry at the specified x-value is found, this method
     * returns the Entry at the closest x-value according to the rounding.
     * INFORMATION: This method does calculations at runtime. Do
     * not over-use in performance critical situations.
     *
     * @param xValue the x-value
     * @param closestToY If there are multiple y-values for the specified x-value,
     * @param rounding determine whether to round up/down/closest
     * if there is no Entry matching the provided x-value
     * @return
     */
    fun getEntryForXValue(xValue: Float, closestToY: Float, rounding: Rounding?): T?

    /**
     * Returns the first Entry object found at the given x-value with binary
     * search.
     * If the no Entry at the specified x-value is found, this method
     * returns the Entry at the closest x-value.
     * INFORMATION: This method does calculations at runtime. Do
     * not over-use in performance critical situations.
     *
     *
     * @param xValue the x-value
     * @param closestToY If there are multiple y-values for the specified x-value,
     * @return
     */
    fun getEntryForXValue(xValue: Float, closestToY: Float): T?

    /**
     * Returns all Entry objects found at the given x-value with binary
     * search. An empty array if no Entry object at that x-value.
     * INFORMATION: This method does calculations at runtime. Do
     * not over-use in performance critical situations.
     *
     * @param xValue
     * @return
     */
    fun getEntriesForXValue(xValue: Float): MutableList<T>?

    /**
     * Returns the Entry object found at the given index (NOT xIndex) in the values array.
     *
     * @param index
     * @return
     */
    fun getEntryForIndex(index: Int): T

    /**
     * Returns the first Entry index found at the given x-value with binary
     * search.
     * If the no Entry at the specified x-value is found, this method
     * returns the Entry at the closest x-value according to the rounding.
     * INFORMATION: This method does calculations at runtime. Do
     * not over-use in performance critical situations.
     *
     * @param xValue the x-value
     * @param closestToY If there are multiple y-values for the specified x-value,
     * @param rounding determine whether to round up/down/closest
     * if there is no Entry matching the provided x-value
     * @return
     */
    fun getEntryIndex(xValue: Float, closestToY: Float, rounding: Rounding?): Int

    /**
     * Returns the position of the provided entry in the DataSets Entry array.
     * Returns -1 if doesn't exist.
     *
     * @param e
     * @return
     */
    fun getEntryIndex(e: Entry?): Int


    /**
     * This method returns the actual
     * index in the Entry array of the DataSet for a given xIndex. IMPORTANT: This method does
     * calculations at runtime, do not over-use in performance critical
     * situations.
     *
     * @param xIndex
     * @return
     */
    fun getIndexInEntries(xIndex: Int): Int

    /**
     * Adds an Entry to the DataSet dynamically.
     * Entries are added to the end of the list.
     * This will also recalculate the current minimum and maximum
     * values of the DataSet and the value-sum.
     *
     * @param e
     */
    fun addEntry(e: T): Boolean


    /**
     * Adds an Entry to the DataSet dynamically.
     * Entries are added to their appropriate index in the values array respective to their x-position.
     * This will also recalculate the current minimum and maximum
     * values of the DataSet and the value-sum.
     *
     * @param e
     */
    fun addEntryOrdered(e: T)

    /**
     * Removes the first Entry (at index 0) of this DataSet from the entries array.
     * Returns true if successful, false if not.
     *
     * @return
     */
    fun removeFirst(): Boolean

    /**
     * Removes the last Entry (at index size-1) of this DataSet from the entries array.
     * Returns true if successful, false if not.
     *
     * @return
     */
    fun removeLast(): Boolean

    /**
     * Removes an Entry from the DataSets entries array. This will also
     * recalculate the current minimum and maximum values of the DataSet and the
     * value-sum. Returns true if an Entry was removed, false if no Entry could
     * be removed.
     *
     * @param e
     */
    fun removeEntry(e: T): Boolean

    /**
     * Removes the Entry object closest to the given x-value from the DataSet.
     * Returns true if an Entry was removed, false if no Entry could be removed.
     *
     * @param xValue
     */
    fun removeEntryByXValue(xValue: Float): Boolean

    /**
     * Removes the Entry object at the given index in the values array from the DataSet.
     * Returns true if an Entry was removed, false if no Entry could be removed.
     *
     * @param index
     * @return
     */
    fun removeEntry(index: Int): Boolean

    /**
     * Checks if this DataSet contains the specified Entry. Returns true if so,
     * false if not. NOTE: Performance is pretty bad on this one, do not
     * over-use in performance critical situations.
     *
     * @param entry
     * @return
     */
    operator fun contains(entry: T): Boolean

    /**
     * Removes all values from this DataSet and does all necessary recalculations.
     */
    fun clear()


    /** ###### ###### STYLING RELATED (& OTHER) METHODS ###### ###### */

    /** ###### ###### STYLING RELATED (& OTHER) METHODS ###### ######  */
    /**
     * Returns the label string that describes the DataSet.
     *
     * @return
     */
    fun getLabel(): String?

    /**
     * Sets the label string that describes the DataSet.
     *
     * @param label
     */
    fun setLabel(label: String)

    /**
     * Returns the axis this DataSet should be plotted against.
     *
     * @return
     */
    fun getAxisDependency(): AxisDependency?

    /**
     * Set the y-axis this DataSet should be plotted against (either LEFT or
     * RIGHT). Default: LEFT
     *
     * @param dependency
     */
    fun setAxisDependency(dependency: AxisDependency)

    /**
     * returns all the colors that are set for this DataSet
     *
     * @return
     */
    fun getColors(): MutableList<Int>?

    /**
     * Returns the first color (index 0) of the colors-array this DataSet
     * contains. This is only used for performance reasons when only one color is in the colors array (size == 1)
     *
     * @return
     */
    fun getColor(): Int

    /**
     * Returns the color at the given index of the DataSet's color array.
     * Performs a IndexOutOfBounds check by modulus.
     *
     * @param index
     * @return
     */
    fun getColor(index: Int): Int

    /**
     * returns true if highlighting of values is enabled, false if not
     *
     * @return
     */
    fun isHighlightEnabled(): Boolean

    /**
     * If set to true, value highlighting is enabled which means that values can
     * be highlighted programmatically or by touch gesture.
     *
     * @param enabled
     */
    fun setHighlightEnabled(enabled: Boolean)

    /**
     * Sets the formatter to be used for drawing the values inside the chart. If
     * no formatter is set, the chart will automatically determine a reasonable
     * formatting (concerning decimals) for all the values that are drawn inside
     * the chart. Use chart.getDefaultValueFormatter() to use the formatter
     * calculated by the chart.
     *
     * @param f
     */
    fun setValueFormatter(f: IValueFormatter?)

    /**
     * Returns the formatter used for drawing the values inside the chart.
     *
     * @return
     */
    fun getValueFormatter(): IValueFormatter?

    /**
     * Returns true if the valueFormatter object of this DataSet is null.
     *
     * @return
     */
    fun needsFormatter(): Boolean

    /**
     * Sets the color the value-labels of this DataSet should have.
     *
     * @param color
     */
    fun setValueTextColor(color: Int)

    /**
     * Sets a list of colors to be used as the colors for the drawn values.
     *
     * @param colors
     */
    fun setValueTextColors(colors: MutableList<Int>?)

    /**
     * Sets a Typeface for the value-labels of this DataSet.
     *
     * @param tf
     */
    fun setValueTypeface(tf: Typeface)

    /**
     * Sets the text-size of the value-labels of this DataSet in dp.
     *
     * @param size
     */
    fun setValueTextSize(size: Float)

    /**
     * Returns only the first color of all colors that are set to be used for the values.
     *
     * @return
     */
    fun getValueTextColor(): Int

    /**
     * Returns the color at the specified index that is used for drawing the values inside the chart.
     * Uses modulus internally.
     *
     * @param index
     * @return
     */
    fun getValueTextColor(index: Int): Int?

    /**
     * Returns the typeface that is used for drawing the values inside the chart
     *
     * @return
     */
    fun getValueTypeface(): Typeface?

    /**
     * Returns the text size that is used for drawing the values inside the chart
     *
     * @return
     */
    fun getValueTextSize(): Float

    /**
     * The form to draw for this dataset in the legend.
     *
     *
     * Return `DEFAULT` to use the default legend form.
     */
    fun getForm(): LegendForm?

    /**
     * The form size to draw for this dataset in the legend.
     *
     *
     * Return `Float.NaN` to use the default legend form size.
     */
    fun getFormSize(): Float

    /**
     * The line width for drawing the form of this dataset in the legend
     *
     *
     * Return `Float.NaN` to use the default legend form line width.
     */
    fun getFormLineWidth(): Float

    /**
     * The line dash path effect used for shapes that consist of lines.
     *
     *
     * Return `null` to use the default legend form line dash effect.
     */
    fun getFormLineDashEffect(): DashPathEffect?

    /**
     * set this to true to draw y-values on the chart.
     *
     * NOTE (for bar and line charts): if `maxVisibleCount` is reached, no values will be drawn even
     * if this is enabled
     * @param enabled
     */
    fun setDrawValues(enabled: Boolean)

    /**
     * Returns true if y-value drawing is enabled, false if not
     *
     * @return
     */
    fun isDrawValuesEnabled(): Boolean

    /**
     * Set this to true to draw y-icons on the chart.
     *
     * NOTE (for bar and line charts): if `maxVisibleCount` is reached, no icons will be drawn even
     * if this is enabled
     *
     * @param enabled
     */
    fun setDrawIcons(enabled: Boolean)

    /**
     * Returns true if y-icon drawing is enabled, false if not
     *
     * @return
     */
    fun isDrawIconsEnabled(): Boolean

    /**
     * Offset of icons drawn on the chart.
     *
     * For all charts except Pie and Radar it will be ordinary (x offset,y offset).
     *
     * For Pie and Radar chart it will be (y offset, distance from center offset); so if you want icon to be rendered under value, you should increase X component of CGPoint, and if you want icon to be rendered closet to center, you should decrease height component of CGPoint.
     * @param offset
     */
    fun setIconsOffset(offset: MPPointF?)

    /**
     * Get the offset for drawing icons.
     */
    fun getIconsOffset(): MPPointF?

    /**
     * Set the visibility of this DataSet. If not visible, the DataSet will not
     * be drawn to the chart upon refreshing it.
     *
     * @param visible
     */
    fun setVisible(visible: Boolean)

    /**
     * Returns true if this DataSet is visible inside the chart, or false if it
     * is currently hidden.
     *
     * @return
     */
    fun isVisible(): Boolean
}