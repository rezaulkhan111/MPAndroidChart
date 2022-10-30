package com.github.mikephil.charting.components

import android.graphics.Color
import android.graphics.DashPathEffect
import android.graphics.Paint
import com.github.mikephil.charting.utils.Utils.convertDpToPixel

/**
 * The limit line is an additional feature for all Line-, Bar- and
 * ScatterCharts. It allows the displaying of an additional line in the chart
 * that marks a certain maximum / limit on the specified axis (x- or y-axis).
 *
 * @author Philipp Jahoda
 */
class LimitLine : ComponentBase {

    /** limit / maximum (the y-value or xIndex)  */
    private var mLimit = 0f

    /** the width of the limit line  */
    private var mLineWidth = 2f

    /** the color of the limit line  */
    private var mLineColor = Color.rgb(237, 91, 91)

    /** the style of the label text  */
    private var mTextStyle = Paint.Style.FILL_AND_STROKE

    /** label string that is drawn next to the limit line  */
    private var mLabel = ""

    /** the path effect of this LimitLine that makes dashed lines possible  */
    private var mDashPathEffect: DashPathEffect? = null

    /** indicates the position of the LimitLine label  */
    private var mLabelPosition = LimitLabelPosition.RIGHT_TOP

    /** enum that indicates the position of the LimitLine label  */
    enum class LimitLabelPosition {
        LEFT_TOP, LEFT_BOTTOM, RIGHT_TOP, RIGHT_BOTTOM
    }

    /**
     * Constructor with limit.
     *
     * @param limit - the position (the value) on the y-axis (y-value) or x-axis
     * (xIndex) where this line should appear
     */
    constructor(limit: Float) {
        mLimit = limit
    }

    /**
     * Constructor with limit and label.
     *
     * @param limit - the position (the value) on the y-axis (y-value) or x-axis
     * (xIndex) where this line should appear
     * @param label - provide "" if no label is required
     */
    constructor(limit: Float, label: String) {
        mLimit = limit
        mLabel = label
    }

    /**
     * Returns the limit that is set for this line.
     *
     * @return
     */
    fun getLimit(): Float {
        return mLimit
    }

    /**
     * set the line width of the chart (min = 0.2f, max = 12f); default 2f NOTE:
     * thinner line == better performance, thicker line == worse performance
     *
     * @param width
     */
    fun setLineWidth(width: Float) {
        var width = width
        if (width < 0.2f) width = 0.2f
        if (width > 12.0f) width = 12.0f
        mLineWidth = convertDpToPixel(width)
    }

    /**
     * returns the width of limit line
     *
     * @return
     */
    fun getLineWidth(): Float {
        return mLineWidth
    }

    /**
     * Sets the linecolor for this LimitLine. Make sure to use
     * getResources().getColor(...)
     *
     * @param color
     */
    fun setLineColor(color: Int) {
        mLineColor = color
    }

    /**
     * Returns the color that is used for this LimitLine
     *
     * @return
     */
    fun getLineColor(): Int {
        return mLineColor
    }

    /**
     * Enables the line to be drawn in dashed mode, e.g. like this "- - - - - -"
     *
     * @param lineLength the length of the line pieces
     * @param spaceLength the length of space inbetween the pieces
     * @param phase offset, in degrees (normally, use 0)
     */
    fun enableDashedLine(lineLength: Float, spaceLength: Float, phase: Float) {
        mDashPathEffect = DashPathEffect(
            floatArrayOf(
                lineLength, spaceLength
            ), phase
        )
    }

    /**
     * Disables the line to be drawn in dashed mode.
     */
    fun disableDashedLine() {
        mDashPathEffect = null
    }

    /**
     * Returns true if the dashed-line effect is enabled, false if not. Default:
     * disabled
     *
     * @return
     */
    fun isDashedLineEnabled(): Boolean {
        return mDashPathEffect != null
    }

    /**
     * returns the DashPathEffect that is set for this LimitLine
     *
     * @return
     */
    fun getDashPathEffect(): DashPathEffect {
        return mDashPathEffect!!
    }

    /**
     * Sets the color of the value-text that is drawn next to the LimitLine.
     * Default: Paint.Style.FILL_AND_STROKE
     *
     * @param style
     */
    fun setTextStyle(style: Paint.Style) {
        mTextStyle = style
    }

    /**
     * Returns the color of the value-text that is drawn next to the LimitLine.
     *
     * @return
     */
    fun getTextStyle(): Paint.Style {
        return mTextStyle
    }

    /**
     * Sets the position of the LimitLine value label (either on the right or on
     * the left edge of the chart). Not supported for RadarChart.
     *
     * @param pos
     */
    fun setLabelPosition(pos: LimitLabelPosition) {
        mLabelPosition = pos
    }

    /**
     * Returns the position of the LimitLine label (value).
     *
     * @return
     */
    fun getLabelPosition(): LimitLabelPosition {
        return mLabelPosition
    }

    /**
     * Sets the label that is drawn next to the limit line. Provide "" if no
     * label is required.
     *
     * @param label
     */
    fun setLabel(label: String) {
        mLabel = label
    }

    /**
     * Returns the label that is drawn next to the limit line.
     *
     * @return
     */
    fun getLabel(): String {
        return mLabel
    }
}