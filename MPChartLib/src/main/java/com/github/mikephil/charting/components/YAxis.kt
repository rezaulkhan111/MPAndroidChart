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
     * returns true if drawing the bottom y-axis label entry is enabled
     *
     * @return
     */
    /**
     * indicates if the bottom y-label entry is drawn or not
     */
    val isDrawBottomYLabelEntryEnabled = true
    /**
     * returns true if drawing the top y-axis label entry is enabled
     *
     * @return
     */
    /**
     * indicates if the top y-label entry is drawn or not
     */
    var isDrawTopYLabelEntryEnabled = true
        private set
    /**
     * If this returns true, the y-axis is inverted.
     *
     * @return
     */
    /**
     * If this is set to true, the y-axis is inverted which means that low values are on top of
     * the chart, high values
     * on bottom.
     *
     * @param enabled
     */
    /**
     * flag that indicates if the axis is inverted or not
     */
    var isInverted = false

    /**
     * flag that indicates if the zero-line should be drawn regardless of other grid lines
     */
    var isDrawZeroLineEnabled = false
        protected set
    /**
     * Returns true if autoscale restriction for axis min value is enabled
     */
    /**
     * Sets autoscale restriction for axis min value as enabled/disabled
     */
    /**
     * flag indicating that auto scale min restriction should be used
     */
    @get:Deprecated("")
    @set:Deprecated("")
    var isUseAutoScaleMinRestriction = false
    /**
     * Returns true if autoscale restriction for axis max value is enabled
     */
    /**
     * Sets autoscale restriction for axis max value as enabled/disabled
     */
    /**
     * flag indicating that auto scale max restriction should be used
     */
    @get:Deprecated("")
    @set:Deprecated("")
    var isUseAutoScaleMaxRestriction = false
    /**
     * Sets the color of the zero line
     *
     * @param color
     */
    /**
     * Color of the zero line
     */
    var zeroLineColor = Color.GRAY

    /**
     * Width of the zero line in pixels
     */
    protected var mZeroLineWidth = 1f
    /**
     * Returns the top axis space in percent of the full range. Default 10f
     *
     * @return
     */
    /**
     * Sets the top axis space in percent of the full range. Default 10f
     *
     * @param percent
     */
    /**
     * axis space from the largest value to the top in percent of the total axis range
     */
    var spaceTop = 10f
    /**
     * Returns the bottom axis space in percent of the full range. Default 10f
     *
     * @return
     */
    /**
     * Sets the bottom axis space in percent of the full range. Default 10f
     *
     * @param percent
     */
    /**
     * axis space from the smallest value to the bottom in percent of the total axis range
     */
    var spaceBottom = 10f
    /**
     * returns the position of the y-labels
     */
    /**
     * the position of the y-labels relative to the chart
     */
    var labelPosition = YAxisLabelPosition.OUTSIDE_CHART
        private set
    /**
     * returns the horizontal offset of the y-label
     */
    /**
     * sets the horizontal offset of the y-label
     *
     * @param xOffset
     */
    /**
     * the horizontal offset of the y-label
     */
    var labelXOffset = 0.0f

    /**
     * enum for the position of the y-labels relative to the chart
     */
    enum class YAxisLabelPosition {
        OUTSIDE_CHART, INSIDE_CHART
    }

    /**
     * the side this axis object represents
     */
    var axisDependency: AxisDependency
        private set
    /**
     * @return the minimum width that the axis should take (in dp).
     */
    /**
     * Sets the minimum width that the axis should take (in dp).
     *
     * @param minWidth
     */
    /**
     * the minimum width that the axis should take (in dp).
     *
     *
     * default: 0.0
     */
    var minWidth = 0f
    /**
     * @return the maximum width that the axis can take (in dp).
     */
    /**
     * Sets the maximum width that the axis can take (in dp).
     *
     * @param maxWidth
     */
    /**
     * the maximum width that the axis can take (in dp).
     * use Inifinity for disabling the maximum
     * default: Float.POSITIVE_INFINITY (no maximum specified)
     */
    var maxWidth = Float.POSITIVE_INFINITY

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
        axisDependency = AxisDependency.LEFT
        mYOffset = 0f
    }

    constructor(position: AxisDependency) : super() {
        axisDependency = position
        mYOffset = 0f
    }

    /**
     * sets the position of the y-labels
     *
     * @param pos
     */
    fun setPosition(pos: YAxisLabelPosition) {
        labelPosition = pos
    }

    /**
     * set this to true to enable drawing the top y-label entry. Disabling this can be helpful
     * when the top y-label and
     * left x-label interfere with each other. default: true
     *
     * @param enabled
     */
    fun setDrawTopYLabelEntry(enabled: Boolean) {
        isDrawTopYLabelEntryEnabled = enabled
    }

    /**
     * This method is deprecated.
     * Use setAxisMinimum(...) / setAxisMaximum(...) instead.
     *
     * @param startAtZero
     */
    @Deprecated("")
    fun setStartAtZero(startAtZero: Boolean) {
        if (startAtZero) axisMinimum = 0f else resetAxisMinimum()
    }

    /**
     * Set this to true to draw the zero-line regardless of weather other
     * grid-lines are enabled or not. Default: false
     *
     * @param mDrawZeroLine
     */
    fun setDrawZeroLine(mDrawZeroLine: Boolean) {
        isDrawZeroLineEnabled = mDrawZeroLine
    }

    /**
     * Sets the width of the zero line in dp
     *
     * @param width
     */
    var zeroLineWidth: Float
        get() = mZeroLineWidth
        set(width) {
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
        val label = longestLabel
        var width = calcTextWidth(p, label).toFloat() + xOffset * 2f
        var minWidth = minWidth
        var maxWidth = maxWidth
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
        val label = longestLabel
        return calcTextHeight(p, label!!).toFloat() + yOffset * 2f
    }

    /**
     * Returns true if this axis needs horizontal offset, false if no offset is needed.
     *
     * @return
     */
    fun needsOffset(): Boolean {
        return if (isEnabled && isDrawLabelsEnabled && labelPosition == YAxisLabelPosition.OUTSIDE_CHART) true else false
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
        mAxisMinimum = if (mCustomAxisMin) mAxisMinimum else min - range / 100f * spaceBottom
        mAxisMaximum = if (mCustomAxisMax) mAxisMaximum else max + range / 100f * spaceTop
        mAxisRange = Math.abs(mAxisMinimum - mAxisMaximum)
    }
}