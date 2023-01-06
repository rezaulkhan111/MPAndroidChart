package com.github.mikephil.charting.listener

import android.view.GestureDetector
import android.view.GestureDetector.SimpleOnGestureListener
import android.view.MotionEvent
import android.view.View.OnTouchListener
import com.github.mikephil.charting.charts.Chart
import com.github.mikephil.charting.highlight.Highlight

/**
 * Created by philipp on 12/06/15.
 */
abstract class ChartTouchListener<T : Chart<*>?> : SimpleOnGestureListener, OnTouchListener {

    enum class ChartGesture {
        NONE, DRAG, X_ZOOM, Y_ZOOM, PINCH_ZOOM, ROTATE, SINGLE_TAP, DOUBLE_TAP, LONG_PRESS, FLING
    }

    /**
     * the last touch gesture that has been performed
     */
    protected var mLastGesture = ChartGesture.NONE

    // states
    protected val NONE = 0
    protected val DRAG = 1
    protected val X_ZOOM = 2
    protected val Y_ZOOM = 3
    protected val PINCH_ZOOM = 4
    protected val POST_ZOOM = 5
    protected val ROTATE = 6

    /**
     * integer field that holds the current touch-state
     */
    protected var mTouchMode = NONE

    /**
     * the last highlighted object (via touch)
     */
    protected var mLastHighlighted: Highlight? = null

    /**
     * the gesturedetector used for detecting taps and longpresses, ...
     */
    protected var mGestureDetector: GestureDetector

    /**
     * the chart the listener represents
     */
    protected var mChart: T

    constructor(chart: T) {
        mChart = chart
        mGestureDetector = GestureDetector(chart!!.context, this)
    }

    /**
     * Calls the OnChartGestureListener to do the start callback
     *
     * @param me
     */
    open fun startAction(me: MotionEvent?) {
        val l = mChart!!.getOnChartGestureListener()
        l?.onChartGestureStart(me!!, mLastGesture)
    }

    /**
     * Calls the OnChartGestureListener to do the end callback
     *
     * @param me
     */
    open fun endAction(me: MotionEvent?) {
        val l = mChart!!.getOnChartGestureListener()
        l?.onChartGestureEnd(me!!, mLastGesture)
    }

    /**
     * Sets the last value that was highlighted via touch.
     *
     * @param high
     */
    open fun setLastHighlighted(high: Highlight?) {
        mLastHighlighted = high
    }

    /**
     * returns the touch mode the listener is currently in
     *
     * @return
     */
    open fun getTouchMode(): Int {
        return mTouchMode
    }

    /**
     * Returns the last gesture that has been performed on the chart.
     *
     * @return
     */
    open fun getLastGesture(): ChartGesture? {
        return mLastGesture
    }


    /**
     * Perform a highlight operation.
     *
     * @param e
     */
    protected open fun performHighlight(h: Highlight?, e: MotionEvent?) {
        mLastHighlighted = if (h == null || h.equalTo(mLastHighlighted)) {
            mChart!!.highlightValue(null, true)
            null
        } else {
            mChart!!.highlightValue(h, true)
            h
        }
    }

    /**
     * returns the distance between two points
     *
     * @param eventX
     * @param startX
     * @param eventY
     * @param startY
     * @return
     */
    protected open fun distance(eventX: Float, startX: Float, eventY: Float, startY: Float): Float {
        val dx = eventX - startX
        val dy = eventY - startY
        return Math.sqrt((dx * dx + dy * dy).toDouble()).toFloat()
    }
}