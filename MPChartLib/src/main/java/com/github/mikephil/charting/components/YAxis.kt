package com.github.mikephil.charting.components

import android.graphics.Color
import android.graphics.Paint
import com.github.mikephil.charting.utils.Utils.calcTextHeight
import com.github.mikephil.charting.utils.Utils.calcTextWidth
import com.github.mikephil.charting.utils.Utils.convertDpToPixel

/**
 * Class representing the y-axis labels settings and its entries. Only use the setter methods to
 * modify it. Do not
 * access public variables directly. Be aware that not all features the YLabels class provides
 * are suitable for the
 * RadarChart. Customizations that affect the value range of the axis need to be applied before
 * setting data for the
 * chart.
 *
 * @author Philipp Jahoda
 */
class YAxis : AxisBase {
    /**
     * indicates if the bottom y-label entry is drawn or not
     */
    private val mDrawBottomYLabelEntry = true

    /**
     * indicates if the top y-label entry is drawn or not
     */
    private var mDrawTopYLabelEntry = true

    /**
     * flag that indicates if the axis is inverted or not
     */
    protected var mInverted = false

    /**
     * flag that indicates if the zero-line should be drawn regardless of other grid lines
     */
    protected var mDrawZeroLine = false

    /**
     * flag indicating that auto scale min restriction should be used
     */
    private var mUseAutoScaleRestrictionMin = false

    /**
     * flag indicating that auto scale max restriction should be used
     */
    private var mUseAutoScaleRestrictionMax = false

    /**
     * Color of the zero line
     */
    protected var mZeroLineColor = Color.GRAY

    /**
     * Width of the zero line in pixels
     */
    protected var mZeroLineWidth = 1f

    /**
     * axis space from the largest value to the top in percent of the total axis range
     */
    protected var mSpacePercentTop = 10f

    /**
     * axis space from the smallest value to the bottom in percent of the total axis range
     */
    protected var mSpacePercentBottom = 10f

    /**
     * the position of the y-labels relative to the chart
     */
    private var mPosition = YAxisLabelPosition.OUTSIDE_CHART

    /**
     * the horizontal offset of the y-label
     */
    private var mXLabelOffset = 0.0f

    /**
     * enum for the position of the y-labels relative to the chart
     */
    enum class YAxisLabelPosition {
        OUTSIDE_CHART, INSIDE_CHART
    }

    /**
     * the side this axis object represents
     */
    private var mAxisDependency: AxisDependency? = null

    /**
     * the minimum width that the axis should take (in dp).
     *
     *
     * default: 0.0
     */
    protected var mMinWidth = 0f

    /**
     * the maximum width that the axis can take (in dp).
     * use Inifinity for disabling the maximum
     * default: Float.POSITIVE_INFINITY (no maximum specified)
     */
    protected var mMaxWidth = Float.POSITIVE_INFINITY

    /**
     * Enum that specifies the axis a DataSet should be plotted against, either LEFT or RIGHT.
     *
     * @author Philipp Jahoda
     */
    enum class AxisDependency {
        LEFT, RIGHT
    }

    constructor() : super() {
        // default left
        mAxisDependency = AxisDependency.LEFT
        mYOffset = 0f
    }

    constructor(position: AxisDependency) : super() {
        mAxisDependency = position
        mYOffset = 0f
    }

    fun getAxisDependency(): AxisDependency? {
        return mAxisDependency
    }

    /**
     * @return the minimum width that the axis should take (in dp).
     */
    fun getMinWidth(): Float {
        return mMinWidth
    }

    /**
     * Sets the minimum width that the axis should take (in dp).
     *
     * @param minWidth
     */
    fun setMinWidth(minWidth: Float) {
        mMinWidth = minWidth
    }

    /**
     * @return the maximum width that the axis can take (in dp).
     */
    fun getMaxWidth(): Float {
        return mMaxWidth
    }

    /**
     * Sets the maximum width that the axis can take (in dp).
     *
     * @param maxWidth
     */
    fun setMaxWidth(maxWidth: Float) {
        mMaxWidth = maxWidth
    }

    /**
     * returns the position of the y-labels
     */
    fun getLabelPosition(): YAxisLabelPosition {
        return mPosition
    }

    /**
     * sets the position of the y-labels
     *
     * @param pos
     */
    fun setPosition(pos: YAxisLabelPosition) {
        mPosition = pos
    }

    /**
     * returns the horizontal offset of the y-label
     */
    fun getLabelXOffset(): Float {
        return mXLabelOffset
    }

    /**
     * sets the horizontal offset of the y-label
     *
     * @param xOffset
     */
    fun setLabelXOffset(xOffset: Float) {
        mXLabelOffset = xOffset
    }

    /**
     * returns true if drawing the top y-axis label entry is enabled
     *
     * @return
     */
    fun isDrawTopYLabelEntryEnabled(): Boolean {
        return mDrawTopYLabelEntry
    }

    /**
     * returns true if drawing the bottom y-axis label entry is enabled
     *
     * @return
     */
    fun isDrawBottomYLabelEntryEnabled(): Boolean {
        return mDrawBottomYLabelEntry
    }

    /**
     * set this to true to enable drawing the top y-label entry. Disabling this can be helpful
     * when the top y-label and
     * left x-label interfere with each other. default: true
     *
     * @param enabled
     */
    fun setDrawTopYLabelEntry(enabled: Boolean) {
        mDrawTopYLabelEntry = enabled
    }

    /**
     * If this is set to true, the y-axis is inverted which means that low values are on top of
     * the chart, high values
     * on bottom.
     *
     * @param enabled
     */
    fun setInverted(enabled: Boolean) {
        mInverted = enabled
    }

    /**
     * If this returns true, the y-axis is inverted.
     *
     * @return
     */
    fun isInverted(): Boolean {
        return mInverted
    }

    /**
     * This method is deprecated.
     * Use setAxisMinimum(...) / setAxisMaximum(...) instead.
     *
     * @param startAtZero
     */
    @Deprecated("")
    fun setStartAtZero(startAtZero: Boolean) {
        if (startAtZero) setAxisMinimum(0f) else resetAxisMinimum()
    }

    /**
     * Sets the top axis space in percent of the full range. Default 10f
     *
     * @param percent
     */
    fun setSpaceTop(percent: Float) {
        mSpacePercentTop = percent
    }

    /**
     * Returns the top axis space in percent of the full range. Default 10f
     *
     * @return
     */
    fun getSpaceTop(): Float {
        return mSpacePercentTop
    }

    /**
     * Sets the bottom axis space in percent of the full range. Default 10f
     *
     * @param percent
     */
    fun setSpaceBottom(percent: Float) {
        mSpacePercentBottom = percent
    }

    /**
     * Returns the bottom axis space in percent of the full range. Default 10f
     *
     * @return
     */
    fun getSpaceBottom(): Float {
        return mSpacePercentBottom
    }

    fun isDrawZeroLineEnabled(): Boolean {
        return mDrawZeroLine
    }

    /**
     * Set this to true to draw the zero-line regardless of weather other
     * grid-lines are enabled or not. Default: false
     *
     * @param mDrawZeroLine
     */
    fun setDrawZeroLine(mDrawZeroLine: Boolean) {
        this.mDrawZeroLine = mDrawZeroLine
    }

    fun getZeroLineColor(): Int {
        return mZeroLineColor
    }

    /**
     * Sets the color of the zero line
     *
     * @param color
     */
    fun setZeroLineColor(color: Int) {
        mZeroLineColor = color
    }

    fun getZeroLineWidth(): Float {
        return mZeroLineWidth
    }

    /**
     * Sets the width of the zero line in dp
     *
     * @param width
     */
    fun setZeroLineWidth(width: Float) {
        mZeroLineWidth = convertDpToPixel(width)
    }

    /**
     * This is for normal (not horizontal) charts horizontal spacing.
     *
     * @param p
     * @return
     */
    fun getRequiredWidthSpace(p: Paint): Float {
        p.textSize = mTextSize
        val label = getLongestLabel()
        var width = calcTextWidth(p, label).toFloat() + getXOffset() * 2f
        var minWidth = getMinWidth()
        var maxWidth = getMaxWidth()
        if (minWidth > 0f) minWidth = convertDpToPixel(minWidth)
        if (maxWidth > 0f && maxWidth != Float.POSITIVE_INFINITY) maxWidth =
            convertDpToPixel(maxWidth)
        width = Math.max(minWidth, Math.min(width, if (maxWidth > 0.0) maxWidth else width))
        return width
    }

    /**
     * This is for HorizontalBarChart vertical spacing.
     *
     * @param p
     * @return
     */
    fun getRequiredHeightSpace(p: Paint): Float {
        p.textSize = mTextSize
        val label = getLongestLabel()
        return calcTextHeight(p, label!!).toFloat() + getYOffset() * 2f
    }

    /**
     * Returns true if this axis needs horizontal offset, false if no offset is needed.
     *
     * @return
     */
    fun needsOffset(): Boolean {
        return if (isEnabled() && isDrawLabelsEnabled() && getLabelPosition() == YAxisLabelPosition.OUTSIDE_CHART) true else false
    }

    /**
     * Returns true if autoscale restriction for axis min value is enabled
     */
    @Deprecated("")
    fun isUseAutoScaleMinRestriction(): Boolean {
        return mUseAutoScaleRestrictionMin
    }

    /**
     * Sets autoscale restriction for axis min value as enabled/disabled
     */
    @Deprecated("")
    fun setUseAutoScaleMinRestriction(isEnabled: Boolean) {
        mUseAutoScaleRestrictionMin = isEnabled
    }

    /**
     * Returns true if autoscale restriction for axis max value is enabled
     */
    @Deprecated("")
    fun isUseAutoScaleMaxRestriction(): Boolean {
        return mUseAutoScaleRestrictionMax
    }

    /**
     * Sets autoscale restriction for axis max value as enabled/disabled
     */
    @Deprecated("")
    fun setUseAutoScaleMaxRestriction(isEnabled: Boolean) {
        mUseAutoScaleRestrictionMax = isEnabled
    }


    override fun calculate(dataMin: Float, dataMax: Float) {
        var min = dataMin
        var max = dataMax

        // Make sure max is greater than min
        // Discussion: https://github.com/danielgindi/Charts/pull/3650#discussion_r221409991
        if (min > max) {
            if (mCustomAxisMax && mCustomAxisMin) {
                val t = min
                min = max
                max = t
            } else if (mCustomAxisMax) {
                min = if (max < 0f) max * 1.5f else max * 0.5f
            } else if (mCustomAxisMin) {
                max = if (min < 0f) min * 0.5f else min * 1.5f
            }
        }
        var range = Math.abs(max - min)

        // in case all values are equal
        if (range == 0f) {
            max = max + 1f
            min = min - 1f
        }

        // recalculate
        range = Math.abs(max - min)

        // calc extra spacing
        mAxisMinimum = if (mCustomAxisMin) mAxisMinimum else min - range / 100f * getSpaceBottom()
        mAxisMaximum = if (mCustomAxisMax) mAxisMaximum else max + range / 100f * getSpaceTop()
        mAxisRange = Math.abs(mAxisMinimum - mAxisMaximum)
    }
}