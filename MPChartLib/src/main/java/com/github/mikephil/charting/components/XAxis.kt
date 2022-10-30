package com.github.mikephil.charting.components

import com.github.mikephil.charting.utils.Utils.convertDpToPixel

/**
 * Class representing the x-axis labels settings. Only use the setter methods to
 * modify it. Do not access public variables directly. Be aware that not all
 * features the XLabels class provides are suitable for the RadarChart.
 *
 * @author Philipp Jahoda
 */
class XAxis : AxisBase {
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
    var mLabelRotatedWidth = 1

    /**
     * height of the (rotated) x-axis labels in pixels - this is automatically
     * calculated by the computeSize() methods in the renderers
     */
    var mLabelRotatedHeight = 1

    /**
     * This is the angle for drawing the X axis labels (in degrees)
     */
    protected var mLabelRotationAngle = 0f

    /**
     * if set to true, the chart will avoid that the first and last label entry
     * in the chart "clip" off the edge of the chart
     */
    private var mAvoidFirstLastClipping = false

    /**
     * the position of the x-labels relative to the chart
     */
    private var mPosition = XAxisPosition.TOP

    /**
     * enum for the position of the x-labels relative to the chart
     */
    enum class XAxisPosition {
        TOP, BOTTOM, BOTH_SIDED, TOP_INSIDE, BOTTOM_INSIDE
    }

    constructor() : super() {
        mYOffset = convertDpToPixel(4f) // -3
    }

    /**
     * returns the position of the x-labels
     */
    fun getPosition(): XAxisPosition {
        return mPosition
    }

    /**
     * sets the position of the x-labels
     *
     * @param pos
     */
    fun setPosition(pos: XAxisPosition) {
        mPosition = pos
    }

    /**
     * returns the angle for drawing the X axis labels (in degrees)
     */
    fun getLabelRotationAngle(): Float {
        return mLabelRotationAngle
    }

    /**
     * sets the angle for drawing the X axis labels (in degrees)
     *
     * @param angle the angle in degrees
     */
    fun setLabelRotationAngle(angle: Float) {
        mLabelRotationAngle = angle
    }

    /**
     * if set to true, the chart will avoid that the first and last label entry
     * in the chart "clip" off the edge of the chart or the screen
     *
     * @param enabled
     */
    fun setAvoidFirstLastClipping(enabled: Boolean) {
        mAvoidFirstLastClipping = enabled
    }

    /**
     * returns true if avoid-first-lastclipping is enabled, false if not
     *
     * @return
     */
    fun isAvoidFirstLastClippingEnabled(): Boolean {
        return mAvoidFirstLastClipping
    }
}