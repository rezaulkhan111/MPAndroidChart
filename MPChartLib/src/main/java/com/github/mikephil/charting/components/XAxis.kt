package com.github.mikephil.charting.components

import com.github.mikephil.charting.utils.Utils.convertDpToPixel

/**
 * Class representing the x-axis labels settings. Only use the setter methods to
 * modify it. Do not access public variables directly. Be aware that not all
 * features the XLabels class provides are suitable for the RadarChart.
 *
 * @author Philipp Jahoda
 */
class XAxis : AxisBase() {
    /**
     * width of the x-axis labels in pixels - this is automatically
     * calculated by the computeSize() methods in the renderers
     */
    var mLabelWidth = 1

    /**
     * height of the x-axis labels in pixels - this is automatically
     * calculated by the computeSize() methods in the renderers
     */
    var mLabelHeight = 1

    /**
     * width of the (rotated) x-axis labels in pixels - this is automatically
     * calculated by the computeSize() methods in the renderers
     */
    @JvmField
    var mLabelRotatedWidth = 1

    /**
     * height of the (rotated) x-axis labels in pixels - this is automatically
     * calculated by the computeSize() methods in the renderers
     */
    @JvmField
    var mLabelRotatedHeight = 1
    /**
     * returns the angle for drawing the X axis labels (in degrees)
     */
    /**
     * sets the angle for drawing the X axis labels (in degrees)
     *
     * @param angle the angle in degrees
     */
    /**
     * This is the angle for drawing the X axis labels (in degrees)
     */
    var labelRotationAngle = 0f
    /**
     * returns true if avoid-first-lastclipping is enabled, false if not
     *
     * @return
     */
    /**
     * if set to true, the chart will avoid that the first and last label entry
     * in the chart "clip" off the edge of the chart
     */
    var isAvoidFirstLastClippingEnabled = false
        private set
    /**
     * returns the position of the x-labels
     */
    /**
     * sets the position of the x-labels
     *
     * @param pos
     */
    /**
     * the position of the x-labels relative to the chart
     */
    var position = XAxisPosition.TOP

    /**
     * enum for the position of the x-labels relative to the chart
     */
    enum class XAxisPosition {
        TOP, BOTTOM, BOTH_SIDED, TOP_INSIDE, BOTTOM_INSIDE
    }

    /**
     * if set to true, the chart will avoid that the first and last label entry
     * in the chart "clip" off the edge of the chart or the screen
     *
     * @param enabled
     */
    fun setAvoidFirstLastClipping(enabled: Boolean) {
        isAvoidFirstLastClippingEnabled = enabled
    }

    init {
        mYOffset = convertDpToPixel(4f) // -3
    }
}