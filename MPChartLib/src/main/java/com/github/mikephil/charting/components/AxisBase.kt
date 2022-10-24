package com.github.mikephil.charting.components

import android.graphics.Color
import com.github.mikephil.charting.utils.Utils.convertDpToPixel
import com.github.mikephil.charting.utils.Utils.calcTextWidth
import com.github.mikephil.charting.utils.Utils.calcTextHeight
import com.github.mikephil.charting.utils.Utils.getLineHeight
import com.github.mikephil.charting.utils.Utils.getLineSpacing
import com.github.mikephil.charting.utils.ViewPortHandler.contentWidth
import com.github.mikephil.charting.utils.Utils.calcTextSize
import com.github.mikephil.charting.utils.FSize.Companion.getInstance
import com.github.mikephil.charting.formatter.IAxisValueFormatter.getFormattedValue
import com.github.mikephil.charting.formatter.DefaultAxisValueFormatter.decimalDigits
import com.github.mikephil.charting.components.AxisBase
import com.github.mikephil.charting.components.XAxis.XAxisPosition
import com.github.mikephil.charting.components.YAxis.YAxisLabelPosition
import com.github.mikephil.charting.components.YAxis.AxisDependency
import com.github.mikephil.charting.components.ComponentBase
import com.github.mikephil.charting.components.LegendEntry
import com.github.mikephil.charting.components.Legend.LegendHorizontalAlignment
import com.github.mikephil.charting.components.Legend.LegendVerticalAlignment
import com.github.mikephil.charting.components.Legend.LegendOrientation
import com.github.mikephil.charting.components.Legend.LegendDirection
import com.github.mikephil.charting.components.Legend.LegendForm
import android.graphics.DashPathEffect
import com.github.mikephil.charting.utils.ColorTemplate
import com.github.mikephil.charting.utils.FSize
import com.github.mikephil.charting.utils.ViewPortHandler
import com.github.mikephil.charting.utils.MPPointF
import com.github.mikephil.charting.formatter.IAxisValueFormatter
import com.github.mikephil.charting.components.LimitLine
import com.github.mikephil.charting.formatter.DefaultAxisValueFormatter
import com.github.mikephil.charting.components.LimitLine.LimitLabelPosition
import android.widget.RelativeLayout
import com.github.mikephil.charting.components.IMarker
import com.github.mikephil.charting.charts.Chart
import android.view.LayoutInflater
import android.view.View.MeasureSpec
import android.graphics.Paint.Align
import android.graphics.drawable.Drawable
import android.os.Build
import android.graphics.Typeface
import android.util.Log
import java.util.ArrayList

/**
 * Base-class of all axes (previously called labels).
 *
 * @author Philipp Jahoda
 */
abstract class AxisBase : ComponentBase() {
    /**
     * custom formatter that is used instead of the auto-formatter if set
     */
    protected var mAxisValueFormatter: IAxisValueFormatter? = null
    /**
     * Returns the color of the grid lines for this axis (the horizontal lines
     * coming from each label).
     *
     * @return
     */
    /**
     * Sets the color of the grid lines for this axis (the horizontal lines
     * coming from each label).
     *
     * @param color
     */
    var gridColor = Color.GRAY
    private var mGridLineWidth = 1f
    /**
     * Returns the color of the axis line (line alongside the axis).
     *
     * @return
     */
    /**
     * Sets the color of the border surrounding the chart.
     *
     * @param color
     */
    var axisLineColor = Color.GRAY
    private var mAxisLineWidth = 1f

    /**
     * the actual array of entries
     */
    @JvmField
    var mEntries = floatArrayOf()

    /**
     * axis label entries only used for centered labels
     */
    var mCenteredEntries = floatArrayOf()

    /**
     * the number of entries the legend contains
     */
    var mEntryCount = 0

    /**
     * the number of decimal digits to use
     */
    var mDecimals = 0

    /**
     * the number of label entries the axis should have, default 6
     */
    private var mLabelCount = 6

    /**
     * the minimum interval between axis values
     */
    protected var mGranularity = 1.0f
    /**
     * @return true if granularity is enabled
     */
    /**
     * Enabled/disable granularity control on axis value intervals. If enabled, the axis
     * interval is not allowed to go below a certain granularity. Default: false
     *
     * @param enabled
     */
    /**
     * When true, axis labels are controlled by the `granularity` property.
     * When false, axis values could possibly be repeated.
     * This could happen if two adjacent axis values are rounded to same value.
     * If using granularity this could be avoided by having fewer axis values visible.
     */
    var isGranularityEnabled = false
    /**
     * Returns true if focing the y-label count is enabled. Default: false
     *
     * @return
     */
    /**
     * if true, the set number of y-labels will be forced
     */
    var isForceLabelsEnabled = false
        protected set
    /**
     * Returns true if drawing grid lines is enabled for this axis.
     *
     * @return
     */
    /**
     * flag indicating if the grid lines for this axis should be drawn
     */
    var isDrawGridLinesEnabled = true
        protected set
    /**
     * Returns true if the line alongside the axis should be drawn.
     *
     * @return
     */
    /**
     * flag that indicates if the line alongside the axis is drawn or not
     */
    var isDrawAxisLineEnabled = true
        protected set
    /**
     * Returns true if drawing the labels is enabled for this axis.
     *
     * @return
     */
    /**
     * flag that indicates of the labels of this axis should be drawn or not
     */
    var isDrawLabelsEnabled = true
        protected set
    protected var mCenterAxisLabels = false
    /**
     * returns the DashPathEffect that is set for axis line
     *
     * @return
     */
    /**
     * the path effect of the axis line that makes dashed lines possible
     */
    var axisLineDashPathEffect: DashPathEffect? = null
        private set
    /**
     * returns the DashPathEffect that is set for grid line
     *
     * @return
     */
    /**
     * the path effect of the grid lines that makes dashed lines possible
     */
    var gridDashPathEffect: DashPathEffect? = null
        private set

    /**
     * array of limit lines that can be set for the axis
     */
    protected var mLimitLines: MutableList<LimitLine>

    /**
     * flag indicating the limit lines layer depth
     */
    var isDrawLimitLinesBehindDataEnabled = false
        protected set

    /**
     * flag indicating the grid lines layer depth
     */
    var isDrawGridLinesBehindDataEnabled = true
        protected set
    /**
     * Gets extra spacing for `axisMinimum` to be added to automatically calculated `axisMinimum`
     */
    /**
     * Sets extra spacing for `axisMinimum` to be added to automatically calculated `axisMinimum`
     */
    /**
     * Extra spacing for `axisMinimum` to be added to automatically calculated `axisMinimum`
     */
    var spaceMin = 0f
    /**
     * Gets extra spacing for `axisMaximum` to be added to automatically calculated `axisMaximum`
     */
    /**
     * Sets extra spacing for `axisMaximum` to be added to automatically calculated `axisMaximum`
     */
    /**
     * Extra spacing for `axisMaximum` to be added to automatically calculated `axisMaximum`
     */
    var spaceMax = 0f
    /**
     * Returns true if the axis min value has been customized (and is not calculated automatically)
     *
     * @return
     */
    /**
     * flag indicating that the axis-min value has been customized
     */
    var isAxisMinCustom = false
        protected set
    /**
     * Returns true if the axis max value has been customized (and is not calculated automatically)
     *
     * @return
     */
    /**
     * flag indicating that the axis-max value has been customized
     */
    var isAxisMaxCustom = false
        protected set

    /**
     * don't touch this direclty, use setter
     */
    @JvmField
    var mAxisMaximum = 0f

    /**
     * don't touch this directly, use setter
     */
    @JvmField
    var mAxisMinimum = 0f

    /**
     * the total range of values this axis covers
     */
    @JvmField
    var mAxisRange = 0f
    private var mAxisMinLabels = 2
    private var mAxisMaxLabels = 25
    /**
     * The minumum number of labels on the axis
     */
    /**
     * The minumum number of labels on the axis
     */
    var axisMinLabels: Int
        get() = mAxisMinLabels
        set(labels) {
            if (labels > 0) mAxisMinLabels = labels
        }
    /**
     * The maximum number of labels on the axis
     */
    /**
     * The maximum number of labels on the axis
     */
    var axisMaxLabels: Int
        get() = mAxisMaxLabels
        set(labels) {
            if (labels > 0) mAxisMaxLabels = labels
        }

    /**
     * Set this to true to enable drawing the grid lines for this axis.
     *
     * @param enabled
     */
    fun setDrawGridLines(enabled: Boolean) {
        isDrawGridLinesEnabled = enabled
    }

    /**
     * Set this to true if the line alongside the axis should be drawn or not.
     *
     * @param enabled
     */
    fun setDrawAxisLine(enabled: Boolean) {
        isDrawAxisLineEnabled = enabled
    }

    /**
     * Centers the axis labels instead of drawing them at their original position.
     * This is useful especially for grouped BarChart.
     *
     * @param enabled
     */
    fun setCenterAxisLabels(enabled: Boolean) {
        mCenterAxisLabels = enabled
    }

    val isCenterAxisLabelsEnabled: Boolean
        get() = mCenterAxisLabels && mEntryCount > 0
    /**
     * Returns the width of the axis line (line alongside the axis).
     *
     * @return
     */
    /**
     * Sets the width of the border surrounding the chart in dp.
     *
     * @param width
     */
    var axisLineWidth: Float
        get() = mAxisLineWidth
        set(width) {
            mAxisLineWidth = convertDpToPixel(width)
        }
    /**
     * Returns the width of the grid lines that are drawn away from each axis
     * label.
     *
     * @return
     */
    /**
     * Sets the width of the grid lines that are drawn away from each axis
     * label.
     *
     * @param width
     */
    var gridLineWidth: Float
        get() = mGridLineWidth
        set(width) {
            mGridLineWidth = convertDpToPixel(width)
        }

    /**
     * Set this to true to enable drawing the labels of this axis (this will not
     * affect drawing the grid lines or axis lines).
     *
     * @param enabled
     */
    fun setDrawLabels(enabled: Boolean) {
        isDrawLabelsEnabled = enabled
    }

    /**
     * sets the number of label entries for the y-axis max = 25, min = 2, default: 6, be aware
     * that this number is not
     * fixed (if force == false) and can only be approximated.
     *
     * @param count the number of y-axis labels that should be displayed
     * @param force if enabled, the set label count will be forced, meaning that the exact
     * specified count of labels will
     * be drawn and evenly distributed alongside the axis - this might cause labels
     * to have uneven values
     */
    fun setLabelCount(count: Int, force: Boolean) {
        labelCount = count
        isForceLabelsEnabled = force
    }
    /**
     * Returns the number of label entries the y-axis should have
     *
     * @return
     */
    /**
     * Sets the number of label entries for the y-axis max = 25, min = 2, default: 6, be aware
     * that this number is not fixed.
     *
     * @param count the number of y-axis labels that should be displayed
     */
    var labelCount: Int
        get() = mLabelCount
        set(count) {
            var count = count
            if (count > axisMaxLabels) count = axisMaxLabels
            if (count < axisMinLabels) count = axisMinLabels
            mLabelCount = count
            isForceLabelsEnabled = false
        }
    /**
     * @return the minimum interval between axis values
     */// set this to true if it was disabled, as it makes no sense to call this method with granularity disabled
    /**
     * Set a minimum interval for the axis when zooming in. The axis is not allowed to go below
     * that limit. This can be used to avoid label duplicating when zooming in.
     *
     * @param granularity
     */
    var granularity: Float
        get() = mGranularity
        set(granularity) {
            mGranularity = granularity
            // set this to true if it was disabled, as it makes no sense to call this method with granularity disabled
            isGranularityEnabled = true
        }

    /**
     * Adds a new LimitLine to this axis.
     *
     * @param l
     */
    fun addLimitLine(l: LimitLine) {
        mLimitLines.add(l)
        if (mLimitLines.size > 6) {
            Log.e(
                "MPAndroiChart",
                "Warning! You have more than 6 LimitLines on your axis, do you really want " +
                        "that?"
            )
        }
    }

    /**
     * Removes the specified LimitLine from the axis.
     *
     * @param l
     */
    fun removeLimitLine(l: LimitLine) {
        mLimitLines.remove(l)
    }

    /**
     * Removes all LimitLines from the axis.
     */
    fun removeAllLimitLines() {
        mLimitLines.clear()
    }

    /**
     * Returns the LimitLines of this axis.
     *
     * @return
     */
    val limitLines: List<LimitLine>
        get() = mLimitLines

    /**
     * If this is set to true, the LimitLines are drawn behind the actual data,
     * otherwise on top. Default: false
     *
     * @param enabled
     */
    fun setDrawLimitLinesBehindData(enabled: Boolean) {
        isDrawLimitLinesBehindDataEnabled = enabled
    }

    /**
     * If this is set to false, the grid lines are draw on top of the actual data,
     * otherwise behind. Default: true
     *
     * @param enabled
     */
    fun setDrawGridLinesBehindData(enabled: Boolean) {
        isDrawGridLinesBehindDataEnabled = enabled
    }

    /**
     * Returns the longest formatted label (in terms of characters), this axis
     * contains.
     *
     * @return
     */
    val longestLabel: String
        get() {
            var longest = ""
            for (i in mEntries.indices) {
                val text = getFormattedLabel(i)
                if (text != null && longest.length < text.length) longest = text
            }
            return longest
        }

    fun getFormattedLabel(index: Int): String {
        return if (index < 0 || index >= mEntries.size) "" else valueFormatter!!.getFormattedValue(
            mEntries[index],
            this
        )
    }
    /**
     * Returns the formatter used for formatting the axis labels.
     *
     * @return
     */
    /**
     * Sets the formatter to be used for formatting the axis labels. If no formatter is set, the
     * chart will
     * automatically determine a reasonable formatting (concerning decimals) for all the values
     * that are drawn inside
     * the chart. Use chart.getDefaultValueFormatter() to use the formatter calculated by the chart.
     *
     * @param f
     */
    var valueFormatter: IAxisValueFormatter?
        get() {
            if (mAxisValueFormatter == null ||
                mAxisValueFormatter is DefaultAxisValueFormatter &&
                (mAxisValueFormatter as DefaultAxisValueFormatter).decimalDigits != mDecimals
            ) mAxisValueFormatter = DefaultAxisValueFormatter(mDecimals)
            return mAxisValueFormatter
        }
        set(f) {
            mAxisValueFormatter = f ?: DefaultAxisValueFormatter(mDecimals)
        }

    /**
     * Enables the grid line to be drawn in dashed mode, e.g. like this
     * "- - - - - -". THIS ONLY WORKS IF HARDWARE-ACCELERATION IS TURNED OFF.
     * Keep in mind that hardware acceleration boosts performance.
     *
     * @param lineLength  the length of the line pieces
     * @param spaceLength the length of space in between the pieces
     * @param phase       offset, in degrees (normally, use 0)
     */
    fun enableGridDashedLine(lineLength: Float, spaceLength: Float, phase: Float) {
        gridDashPathEffect = DashPathEffect(
            floatArrayOf(
                lineLength, spaceLength
            ), phase
        )
    }

    /**
     * Enables the grid line to be drawn in dashed mode, e.g. like this
     * "- - - - - -". THIS ONLY WORKS IF HARDWARE-ACCELERATION IS TURNED OFF.
     * Keep in mind that hardware acceleration boosts performance.
     *
     * @param effect the DashPathEffect
     */
    fun setGridDashedLine(effect: DashPathEffect?) {
        gridDashPathEffect = effect
    }

    /**
     * Disables the grid line to be drawn in dashed mode.
     */
    fun disableGridDashedLine() {
        gridDashPathEffect = null
    }

    /**
     * Returns true if the grid dashed-line effect is enabled, false if not.
     *
     * @return
     */
    val isGridDashedLineEnabled: Boolean
        get() = if (gridDashPathEffect == null) false else true

    /**
     * Enables the axis line to be drawn in dashed mode, e.g. like this
     * "- - - - - -". THIS ONLY WORKS IF HARDWARE-ACCELERATION IS TURNED OFF.
     * Keep in mind that hardware acceleration boosts performance.
     *
     * @param lineLength  the length of the line pieces
     * @param spaceLength the length of space in between the pieces
     * @param phase       offset, in degrees (normally, use 0)
     */
    fun enableAxisLineDashedLine(lineLength: Float, spaceLength: Float, phase: Float) {
        axisLineDashPathEffect = DashPathEffect(
            floatArrayOf(
                lineLength, spaceLength
            ), phase
        )
    }

    /**
     * Enables the axis line to be drawn in dashed mode, e.g. like this
     * "- - - - - -". THIS ONLY WORKS IF HARDWARE-ACCELERATION IS TURNED OFF.
     * Keep in mind that hardware acceleration boosts performance.
     *
     * @param effect the DashPathEffect
     */
    fun setAxisLineDashedLine(effect: DashPathEffect?) {
        axisLineDashPathEffect = effect
    }

    /**
     * Disables the axis line to be drawn in dashed mode.
     */
    fun disableAxisLineDashedLine() {
        axisLineDashPathEffect = null
    }

    /**
     * Returns true if the axis dashed-line effect is enabled, false if not.
     *
     * @return
     */
    val isAxisLineDashedLineEnabled: Boolean
        get() = if (axisLineDashPathEffect == null) false else true
    /**
     * ###### BELOW CODE RELATED TO CUSTOM AXIS VALUES ######
     */
    /**
     * Set a custom maximum value for this axis. If set, this value will not be calculated
     * automatically depending on
     * the provided data. Use resetAxisMaxValue() to undo this.
     *
     * @param max
     */
    var axisMaximum: Float
        get() = mAxisMaximum
        set(max) {
            isAxisMaxCustom = true
            mAxisMaximum = max
            mAxisRange = Math.abs(max - mAxisMinimum)
        }

    /**
     * Set a custom minimum value for this axis. If set, this value will not be calculated
     * automatically depending on
     * the provided data. Use resetAxisMinValue() to undo this. Do not forget to call
     * setStartAtZero(false) if you use
     * this method. Otherwise, the axis-minimum value will still be forced to 0.
     *
     * @param min
     */
    var axisMinimum: Float
        get() = mAxisMinimum
        set(min) {
            isAxisMinCustom = true
            mAxisMinimum = min
            mAxisRange = Math.abs(mAxisMaximum - min)
        }

    /**
     * By calling this method, any custom maximum value that has been previously set is reseted,
     * and the calculation is
     * done automatically.
     */
    fun resetAxisMaximum() {
        isAxisMaxCustom = false
    }

    /**
     * By calling this method, any custom minimum value that has been previously set is reseted,
     * and the calculation is
     * done automatically.
     */
    fun resetAxisMinimum() {
        isAxisMinCustom = false
    }

    /**
     * Use setAxisMinimum(...) instead.
     *
     * @param min
     */
    @Deprecated("")
    fun setAxisMinValue(min: Float) {
        axisMinimum = min
    }

    /**
     * Use setAxisMaximum(...) instead.
     *
     * @param max
     */
    @Deprecated("")
    fun setAxisMaxValue(max: Float) {
        axisMaximum = max
    }

    /**
     * Calculates the minimum / maximum  and range values of the axis with the given
     * minimum and maximum values from the chart data.
     *
     * @param dataMin the min value according to chart data
     * @param dataMax the max value according to chart data
     */
    open fun calculate(dataMin: Float, dataMax: Float) {

        // if custom, use value as is, else use data value
        var min = if (isAxisMinCustom) mAxisMinimum else dataMin - spaceMin
        var max = if (isAxisMaxCustom) mAxisMaximum else dataMax + spaceMax

        // temporary range (before calculations)
        val range = Math.abs(max - min)

        // in case all values are equal
        if (range == 0f) {
            max = max + 1f
            min = min - 1f
        }
        mAxisMinimum = min
        mAxisMaximum = max

        // actual range
        mAxisRange = Math.abs(max - min)
    }

    /**
     * default constructor
     */
    init {
        mTextSize = convertDpToPixel(10f)
        mXOffset = convertDpToPixel(5f)
        mYOffset = convertDpToPixel(5f)
        mLimitLines = ArrayList()
    }
}