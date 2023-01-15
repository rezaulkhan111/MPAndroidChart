package com.github.mikephil.charting.components

import android.graphics.Color
import android.graphics.DashPathEffect
import android.util.Log
import com.github.mikephil.charting.formatter.DefaultAxisValueFormatter
import com.github.mikephil.charting.formatter.IAxisValueFormatter
import com.github.mikephil.charting.utils.Utils.convertDpToPixel

/**
 * Base-class of all axes (previously called labels).
 *
 * @author Philipp Jahoda
 */
abstract class AxisBase : ComponentBase {
    /**
     * custom formatter that is used instead of the auto-formatter if set
     */
    protected var mAxisValueFormatter: IAxisValueFormatter? = null

    private var mGridColor = Color.GRAY

    private var mGridLineWidth = 1f

    private var mAxisLineColor = Color.GRAY

    private var mAxisLineWidth = 1f

    /**
     * the actual array of entries
     */
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
     * When true, axis labels are controlled by the `granularity` property.
     * When false, axis values could possibly be repeated.
     * This could happen if two adjacent axis values are rounded to same value.
     * If using granularity this could be avoided by having fewer axis values visible.
     */
    protected var mGranularityEnabled = false

    /**
     * if true, the set number of y-labels will be forced
     */
    protected var mForceLabels = false

    /**
     * flag indicating if the grid lines for this axis should be drawn
     */
    protected var mDrawGridLines = true

    /**
     * flag that indicates if the line alongside the axis is drawn or not
     */
    protected var mDrawAxisLine = true

    /**
     * flag that indicates of the labels of this axis should be drawn or not
     */
    protected var mDrawLabels = true

    protected var mCenterAxisLabels = false

    /**
     * the path effect of the axis line that makes dashed lines possible
     */
    private var mAxisLineDashPathEffect: DashPathEffect? = null

    /**
     * the path effect of the grid lines that makes dashed lines possible
     */
    private var mGridDashPathEffect: DashPathEffect? = null

    /**
     * array of limit lines that can be set for the axis
     */
    protected var mLimitLines: MutableList<LimitLine>? = null

    /**
     * flag indicating the limit lines layer depth
     */
    protected var mDrawLimitLineBehindData = false

    /**
     * flag indicating the grid lines layer depth
     */
    protected var mDrawGridLinesBehindData = true

    /**
     * Extra spacing for `axisMinimum` to be added to automatically calculated `axisMinimum`
     */
    protected var mSpaceMin = 0f

    /**
     * Extra spacing for `axisMaximum` to be added to automatically calculated `axisMaximum`
     */
    protected var mSpaceMax = 0f

    /**
     * flag indicating that the axis-min value has been customized
     */
    protected var mCustomAxisMin = false

    /**
     * flag indicating that the axis-max value has been customized
     */
    protected var mCustomAxisMax = false

    /**
     * don't touch this direclty, use setter
     */
    var mAxisMaximum = 0f

    /**
     * don't touch this directly, use setter
     */
    var mAxisMinimum = 0f

    /**
     * the total range of values this axis covers
     */
    var mAxisRange = 0f

    private var mAxisMinLabels = 2
    private var mAxisMaxLabels = 25

    /**
     * The minumum number of labels on the axis
     */
    open fun getAxisMinLabels(): Int {
        return mAxisMinLabels
    }

    /**
     * The minumum number of labels on the axis
     */
    open fun setAxisMinLabels(labels: Int) {
        if (labels > 0) mAxisMinLabels = labels
    }

    /**
     * The maximum number of labels on the axis
     */
    open fun getAxisMaxLabels(): Int {
        return mAxisMaxLabels
    }

    /**
     * The maximum number of labels on the axis
     */
    open fun setAxisMaxLabels(labels: Int) {
        if (labels > 0) mAxisMaxLabels = labels
    }

    /**
     * default constructor
     */
    constructor() {
        mTextSize = convertDpToPixel(10f)
        mXOffset = convertDpToPixel(5f)
        mYOffset = convertDpToPixel(5f)
        mLimitLines = ArrayList()
    }

    /**
     * Set this to true to enable drawing the grid lines for this axis.
     *
     * @param enabled
     */
    open fun setDrawGridLines(enabled: Boolean) {
        mDrawGridLines = enabled
    }

    /**
     * Returns true if drawing grid lines is enabled for this axis.
     *
     * @return
     */
    open fun isDrawGridLinesEnabled(): Boolean {
        return mDrawGridLines
    }

    /**
     * Set this to true if the line alongside the axis should be drawn or not.
     *
     * @param enabled
     */
    open fun setDrawAxisLine(enabled: Boolean) {
        mDrawAxisLine = enabled
    }

    /**
     * Returns true if the line alongside the axis should be drawn.
     *
     * @return
     */
    open fun isDrawAxisLineEnabled(): Boolean {
        return mDrawAxisLine
    }

    /**
     * Centers the axis labels instead of drawing them at their original position.
     * This is useful especially for grouped BarChart.
     *
     * @param enabled
     */
    open fun setCenterAxisLabels(enabled: Boolean) {
        mCenterAxisLabels = enabled
    }

    open fun isCenterAxisLabelsEnabled(): Boolean {
        return mCenterAxisLabels && mEntryCount > 0
    }

    /**
     * Sets the color of the grid lines for this axis (the horizontal lines
     * coming from each label).
     *
     * @param color
     */
    open fun setGridColor(color: Int) {
        mGridColor = color
    }

    /**
     * Returns the color of the grid lines for this axis (the horizontal lines
     * coming from each label).
     *
     * @return
     */
    open fun getGridColor(): Int {
        return mGridColor
    }

    /**
     * Sets the width of the border surrounding the chart in dp.
     *
     * @param width
     */
    open fun setAxisLineWidth(width: Float) {
        mAxisLineWidth = convertDpToPixel(width)
    }

    /**
     * Returns the width of the axis line (line alongside the axis).
     *
     * @return
     */
    open fun getAxisLineWidth(): Float {
        return mAxisLineWidth
    }

    /**
     * Sets the width of the grid lines that are drawn away from each axis
     * label.
     *
     * @param width
     */
    open fun setGridLineWidth(width: Float) {
        mGridLineWidth = convertDpToPixel(width)
    }

    /**
     * Returns the width of the grid lines that are drawn away from each axis
     * label.
     *
     * @return
     */
    open fun getGridLineWidth(): Float {
        return mGridLineWidth
    }

    /**
     * Sets the color of the border surrounding the chart.
     *
     * @param color
     */
    open fun setAxisLineColor(color: Int) {
        mAxisLineColor = color
    }

    /**
     * Returns the color of the axis line (line alongside the axis).
     *
     * @return
     */
    open fun getAxisLineColor(): Int {
        return mAxisLineColor
    }

    /**
     * Set this to true to enable drawing the labels of this axis (this will not
     * affect drawing the grid lines or axis lines).
     *
     * @param enabled
     */
    open fun setDrawLabels(enabled: Boolean) {
        mDrawLabels = enabled
    }

    /**
     * Returns true if drawing the labels is enabled for this axis.
     *
     * @return
     */
    open fun isDrawLabelsEnabled(): Boolean {
        return mDrawLabels
    }

    /**
     * Sets the number of label entries for the y-axis max = 25, min = 2, default: 6, be aware
     * that this number is not fixed.
     *
     * @param count the number of y-axis labels that should be displayed
     */
    open fun setLabelCount(count: Int) {
        var lCount = count
        if (lCount > getAxisMaxLabels()) lCount = getAxisMaxLabels()
        if (lCount < getAxisMinLabels()) lCount = getAxisMinLabels()
        mLabelCount = lCount
        mForceLabels = false
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
    open fun setLabelCount(count: Int, force: Boolean) {
        setLabelCount(count)
        mForceLabels = force
    }

    /**
     * Returns true if focing the y-label count is enabled. Default: false
     *
     * @return
     */
    open fun isForceLabelsEnabled(): Boolean {
        return mForceLabels
    }

    /**
     * Returns the number of label entries the y-axis should have
     *
     * @return
     */
    open fun getLabelCount(): Int {
        return mLabelCount
    }

    /**
     * @return true if granularity is enabled
     */
    open fun isGranularityEnabled(): Boolean {
        return mGranularityEnabled
    }

    /**
     * Enabled/disable granularity control on axis value intervals. If enabled, the axis
     * interval is not allowed to go below a certain granularity. Default: false
     *
     * @param enabled
     */
    open fun setGranularityEnabled(enabled: Boolean) {
        mGranularityEnabled = enabled
    }

    /**
     * @return the minimum interval between axis values
     */
    open fun getGranularity(): Float {
        return mGranularity
    }

    /**
     * Set a minimum interval for the axis when zooming in. The axis is not allowed to go below
     * that limit. This can be used to avoid label duplicating when zooming in.
     *
     * @param granularity
     */
    open fun setGranularity(granularity: Float) {
        mGranularity = granularity
        // set this to true if it was disabled, as it makes no sense to call this method with granularity disabled
        mGranularityEnabled = true
    }

    /**
     * Adds a new LimitLine to this axis.
     *
     * @param l
     */
    open fun addLimitLine(l: LimitLine?) {
        mLimitLines?.add(l!!)
        if (mLimitLines!!.size > 6) {
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
    open fun removeLimitLine(l: LimitLine?) {
        mLimitLines?.remove(l)
    }

    /**
     * Removes all LimitLines from the axis.
     */
    open fun removeAllLimitLines() {
        mLimitLines?.clear()
    }

    /**
     * Returns the LimitLines of this axis.
     *
     * @return
     */
    open fun getLimitLines(): List<LimitLine?>? {
        return mLimitLines
    }

    /**
     * If this is set to true, the LimitLines are drawn behind the actual data,
     * otherwise on top. Default: false
     *
     * @param enabled
     */
    open fun setDrawLimitLinesBehindData(enabled: Boolean) {
        mDrawLimitLineBehindData = enabled
    }

    open fun isDrawLimitLinesBehindDataEnabled(): Boolean {
        return mDrawLimitLineBehindData
    }

    /**
     * If this is set to false, the grid lines are draw on top of the actual data,
     * otherwise behind. Default: true
     *
     * @param enabled
     */
    open fun setDrawGridLinesBehindData(enabled: Boolean) {
        mDrawGridLinesBehindData = enabled
    }

    open fun isDrawGridLinesBehindDataEnabled(): Boolean {
        return mDrawGridLinesBehindData
    }

    /**
     * Returns the longest formatted label (in terms of characters), this axis
     * contains.
     *
     * @return
     */
    open fun getLongestLabel(): String? {
        var longest = ""
        for (i in mEntries.indices) {
            val text = getFormattedLabel(i)
            if (text != null && longest.length < text.length) longest = text
        }
        return longest
    }

    open fun getFormattedLabel(index: Int): String? {
        return if (index < 0 || index >= mEntries.size) "" else getValueFormatter().getFormattedValue(
            mEntries.get(index),
            this
        )
    }

    /**
     * Sets the formatter to be used for formatting the axis labels. If no formatter is set, the
     * chart will
     * automatically determine a reasonable formatting (concerning decimals) for all the values
     * that are drawn inside
     * the chart. Use chart.getDefaultValueFormatter() to use the formatter calculated by the chart.
     *
     * @param f
     */
    open fun setValueFormatter(f: IAxisValueFormatter?) {
        if (f == null) mAxisValueFormatter =
            DefaultAxisValueFormatter(mDecimals) else mAxisValueFormatter = f
    }

    /**
     * Returns the formatter used for formatting the axis labels.
     *
     * @return
     */
    open fun getValueFormatter(): IAxisValueFormatter {
        if (mAxisValueFormatter == null ||
            mAxisValueFormatter is DefaultAxisValueFormatter &&
            (mAxisValueFormatter as DefaultAxisValueFormatter).getDecimalDigits() != mDecimals
        ) mAxisValueFormatter = DefaultAxisValueFormatter(mDecimals)
        return mAxisValueFormatter!!
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
    open fun enableGridDashedLine(lineLength: Float, spaceLength: Float, phase: Float) {
        mGridDashPathEffect = DashPathEffect(
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
    open fun setGridDashedLine(effect: DashPathEffect) {
        mGridDashPathEffect = effect
    }

    /**
     * Disables the grid line to be drawn in dashed mode.
     */
    open fun disableGridDashedLine() {
        mGridDashPathEffect = null
    }

    /**
     * Returns true if the grid dashed-line effect is enabled, false if not.
     *
     * @return
     */
    open fun isGridDashedLineEnabled(): Boolean {
        return if (mGridDashPathEffect == null) false else true
    }

    /**
     * returns the DashPathEffect that is set for grid line
     *
     * @return
     */
    open fun getGridDashPathEffect(): DashPathEffect? {
        return mGridDashPathEffect
    }


    /**
     * Enables the axis line to be drawn in dashed mode, e.g. like this
     * "- - - - - -". THIS ONLY WORKS IF HARDWARE-ACCELERATION IS TURNED OFF.
     * Keep in mind that hardware acceleration boosts performance.
     *
     * @param lineLength  the length of the line pieces
     * @param spaceLength the length of space in between the pieces
     * @param phase       offset, in degrees (normally, use 0)
     */
    open fun enableAxisLineDashedLine(lineLength: Float, spaceLength: Float, phase: Float) {
        mAxisLineDashPathEffect = DashPathEffect(
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
    open fun setAxisLineDashedLine(effect: DashPathEffect) {
        mAxisLineDashPathEffect = effect
    }

    /**
     * Disables the axis line to be drawn in dashed mode.
     */
    open fun disableAxisLineDashedLine() {
        mAxisLineDashPathEffect = null
    }

    /**
     * Returns true if the axis dashed-line effect is enabled, false if not.
     *
     * @return
     */
    open fun isAxisLineDashedLineEnabled(): Boolean {
        return if (mAxisLineDashPathEffect == null) false else true
    }

    /**
     * returns the DashPathEffect that is set for axis line
     *
     * @return
     */
    open fun getAxisLineDashPathEffect(): DashPathEffect? {
        return mAxisLineDashPathEffect
    }

    /**
     * ###### BELOW CODE RELATED TO CUSTOM AXIS VALUES ######
     */
    open fun getAxisMaximum(): Float {
        return mAxisMaximum
    }

    open fun getAxisMinimum(): Float {
        return mAxisMinimum
    }

    /**
     * By calling this method, any custom maximum value that has been previously set is reseted,
     * and the calculation is
     * done automatically.
     */
    open fun resetAxisMaximum() {
        mCustomAxisMax = false
    }

    /**
     * Returns true if the axis max value has been customized (and is not calculated automatically)
     *
     * @return
     */
    open fun isAxisMaxCustom(): Boolean {
        return mCustomAxisMax
    }

    /**
     * By calling this method, any custom minimum value that has been previously set is reseted,
     * and the calculation is
     * done automatically.
     */
    open fun resetAxisMinimum() {
        mCustomAxisMin = false
    }

    /**
     * Returns true if the axis min value has been customized (and is not calculated automatically)
     *
     * @return
     */
    open fun isAxisMinCustom(): Boolean {
        return mCustomAxisMin
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
    open fun setAxisMinimum(min: Float) {
        mCustomAxisMin = true
        mAxisMinimum = min
        mAxisRange = Math.abs(mAxisMaximum - min)
    }

    /**
     * Use setAxisMinimum(...) instead.
     *
     * @param min
     */
    @Deprecated("")
    open fun setAxisMinValue(min: Float) {
        setAxisMinimum(min)
    }

    /**
     * Set a custom maximum value for this axis. If set, this value will not be calculated
     * automatically depending on
     * the provided data. Use resetAxisMaxValue() to undo this.
     *
     * @param max
     */
    open fun setAxisMaximum(max: Float) {
        mCustomAxisMax = true
        mAxisMaximum = max
        mAxisRange = Math.abs(max - mAxisMinimum)
    }

    /**
     * Use setAxisMaximum(...) instead.
     *
     * @param max
     */
    @Deprecated("")
    open fun setAxisMaxValue(max: Float) {
        setAxisMaximum(max)
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
        var min = if (mCustomAxisMin) mAxisMinimum else (dataMin - mSpaceMin)
        var max = if (mCustomAxisMax) mAxisMaximum else (dataMax + mSpaceMax)

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
     * Gets extra spacing for `axisMinimum` to be added to automatically calculated `axisMinimum`
     */
    open fun getSpaceMin(): Float {
        return mSpaceMin
    }

    /**
     * Sets extra spacing for `axisMinimum` to be added to automatically calculated `axisMinimum`
     */
    open fun setSpaceMin(mSpaceMin: Float) {
        this.mSpaceMin = mSpaceMin
    }

    /**
     * Gets extra spacing for `axisMaximum` to be added to automatically calculated `axisMaximum`
     */
    open fun getSpaceMax(): Float {
        return mSpaceMax
    }

    /**
     * Sets extra spacing for `axisMaximum` to be added to automatically calculated `axisMaximum`
     */
    open fun setSpaceMax(mSpaceMax: Float) {
        this.mSpaceMax = mSpaceMax
    }
}