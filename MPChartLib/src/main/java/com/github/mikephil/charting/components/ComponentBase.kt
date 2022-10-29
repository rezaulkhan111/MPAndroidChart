package com.github.mikephil.charting.components

import android.graphics.Color
import android.graphics.Typeface
import com.github.mikephil.charting.utils.Utils.convertDpToPixel

/**
 * This class encapsulates everything both Axis, Legend and LimitLines have in common.
 *
 * @author Philipp Jahoda
 */
abstract class ComponentBase {
    /**
     * flag that indicates if this axis / legend is enabled or not
     */
    var mEnabled = true

    /**
     * the offset in pixels this component has on the x-axis
     */
    var mXOffset = 5f

    /**
     * the offset in pixels this component has on the Y-axis
     */
    var mYOffset = 5f

    /**
     * the typeface used for the labels
     */
    private var mTypeface: Typeface? = null

    /**
     * the text size of the labels
     */
    var mTextSize = convertDpToPixel(10f)

    /**
     * the text color to use for the labels
     */
    private var mTextColor = Color.BLACK


    open fun ComponentBase() {}

    /**
     * Returns the used offset on the x-axis for drawing the axis or legend
     * labels. This offset is applied before and after the label.
     *
     * @return
     */
    open fun getXOffset(): Float {
        return mXOffset
    }

    /**
     * Sets the used x-axis offset for the labels on this axis.
     *
     * @param xOffset
     */
    open fun setXOffset(xOffset: Float) {
        mXOffset = convertDpToPixel(xOffset)
    }

    /**
     * Returns the used offset on the x-axis for drawing the axis labels. This
     * offset is applied before and after the label.
     *
     * @return
     */
    open fun getYOffset(): Float {
        return mYOffset
    }

    /**
     * Sets the used y-axis offset for the labels on this axis. For the legend,
     * higher offset means the legend as a whole will be placed further away
     * from the top.
     *
     * @param yOffset
     */
    open fun setYOffset(yOffset: Float) {
        mYOffset = convertDpToPixel(yOffset)
    }

    /**
     * returns the Typeface used for the labels, returns null if none is set
     *
     * @return
     */
    open fun getTypeface(): Typeface? {
        return mTypeface
    }

    /**
     * sets a specific Typeface for the labels
     *
     * @param tf
     */
    open fun setTypeface(tf: Typeface?) {
        mTypeface = tf
    }

    /**
     * sets the size of the label text in density pixels min = 6f, max = 24f, default
     * 10f
     *
     * @param size the text size, in DP
     */
    open fun setTextSize(size: Float) {
        var size = size
        if (size > 24f) size = 24f
        if (size < 6f) size = 6f
        mTextSize = convertDpToPixel(size)
    }

    /**
     * returns the text size that is currently set for the labels, in pixels
     *
     * @return
     */
    open fun getTextSize(): Float {
        return mTextSize
    }


    /**
     * Sets the text color to use for the labels. Make sure to use
     * getResources().getColor(...) when using a color from the resources.
     *
     * @param color
     */
    open fun setTextColor(color: Int) {
        mTextColor = color
    }

    /**
     * Returns the text color that is set for the labels.
     *
     * @return
     */
    open fun getTextColor(): Int {
        return mTextColor
    }

    /**
     * Set this to true if this component should be enabled (should be drawn),
     * false if not. If disabled, nothing of this component will be drawn.
     * Default: true
     *
     * @param enabled
     */
    open fun setEnabled(enabled: Boolean) {
        mEnabled = enabled
    }

    /**
     * Returns true if this comonent is enabled (should be drawn), false if not.
     *
     * @return
     */
    open fun isEnabled(): Boolean {
        return mEnabled
    }
}