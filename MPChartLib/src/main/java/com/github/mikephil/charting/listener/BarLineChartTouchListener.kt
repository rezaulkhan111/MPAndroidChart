package com.github.mikephil.charting.listener

import com.github.mikephil.charting.utils.Utils.convertDpToPixel
import com.github.mikephil.charting.utils.Utils.maximumFlingVelocity
import com.github.mikephil.charting.utils.Utils.minimumFlingVelocity
import com.github.mikephil.charting.utils.Utils.postInvalidateOnAnimation
import com.github.mikephil.charting.utils.Utils.velocityTrackerPointerUpCleanUpIfNecessary
import com.github.mikephil.charting.utils.ViewPortHandler.refresh
import com.github.mikephil.charting.utils.ViewPortHandler.canZoomOutMoreX
import com.github.mikephil.charting.utils.ViewPortHandler.canZoomInMoreX
import com.github.mikephil.charting.utils.ViewPortHandler.canZoomOutMoreY
import com.github.mikephil.charting.utils.ViewPortHandler.canZoomInMoreY
import com.github.mikephil.charting.utils.MPPointF.Companion.recycleInstance
import com.github.mikephil.charting.utils.ViewPortHandler.offsetLeft
import com.github.mikephil.charting.utils.ViewPortHandler.offsetTop
import com.github.mikephil.charting.utils.ViewPortHandler.offsetBottom
import com.github.mikephil.charting.data.DataSet
import com.github.mikephil.charting.charts.Chart
import android.view.GestureDetector.SimpleOnGestureListener
import android.view.View.OnTouchListener
import com.github.mikephil.charting.listener.ChartTouchListener.ChartGesture
import com.github.mikephil.charting.listener.ChartTouchListener
import android.view.GestureDetector
import android.view.MotionEvent
import com.github.mikephil.charting.listener.OnChartGestureListener
import com.github.mikephil.charting.charts.BarLineChartBase
import com.github.mikephil.charting.data.BarLineScatterCandleBubbleData
import com.github.mikephil.charting.interfaces.datasets.IBarLineScatterCandleBubbleDataSet
import com.github.mikephil.charting.utils.MPPointF
import com.github.mikephil.charting.interfaces.datasets.IDataSet
import android.view.VelocityTracker
import android.annotation.SuppressLint
import android.graphics.Matrix
import android.util.Log
import android.view.View
import android.view.animation.AnimationUtils
import com.github.mikephil.charting.listener.BarLineChartTouchListener
import com.github.mikephil.charting.charts.HorizontalBarChart
import com.github.mikephil.charting.utils.ViewPortHandler
import com.github.mikephil.charting.charts.PieRadarChartBase
import com.github.mikephil.charting.data.Entry
import com.github.mikephil.charting.listener.PieRadarChartTouchListener.AngularVelocitySample

/**
 * TouchListener for Bar-, Line-, Scatter- and CandleStickChart with handles all
 * touch interaction. Longpress == Zoom out. Double-Tap == Zoom in.
 *
 * @author Philipp Jahoda
 */
class BarLineChartTouchListener(
    chart: BarLineChartBase<out BarLineScatterCandleBubbleData<out IBarLineScatterCandleBubbleDataSet<out Entry?>?>?>,
    touchMatrix: Matrix,
    dragTriggerDistance: Float
) : ChartTouchListener<BarLineChartBase<out BarLineScatterCandleBubbleData<out IBarLineScatterCandleBubbleDataSet<out Entry?>?>?>?>(
    chart
) {
    /**
     * returns the matrix object the listener holds
     *
     * @return
     */
    /**
     * the original touch-matrix from the chart
     */
    var matrix = Matrix()
        private set

    /**
     * matrix for saving the original matrix state
     */
    private val mSavedMatrix = Matrix()

    /**
     * point where the touch action started
     */
    private val mTouchStartPoint = MPPointF.getInstance(0, 0)

    /**
     * center between two pointers (fingers on the display)
     */
    private val mTouchPointCenter = MPPointF.getInstance(0, 0)
    private var mSavedXDist = 1f
    private var mSavedYDist = 1f
    private var mSavedDist = 1f
    private var mClosestDataSetToTouch: IDataSet<*>? = null

    /**
     * used for tracking velocity of dragging
     */
    private var mVelocityTracker: VelocityTracker? = null
    private var mDecelerationLastTime: Long = 0
    private val mDecelerationCurrentPoint = MPPointF.getInstance(0, 0)
    private val mDecelerationVelocity = MPPointF.getInstance(0, 0)

    /**
     * the distance of movement that will be counted as a drag
     */
    private var mDragTriggerDist: Float

    /**
     * the minimum distance between the pointers that will trigger a zoom gesture
     */
    private val mMinScalePointerDistance: Float
    @SuppressLint("ClickableViewAccessibility")
    override fun onTouch(v: View, event: MotionEvent): Boolean {
        if (mVelocityTracker == null) {
            mVelocityTracker = VelocityTracker.obtain()
        }
        mVelocityTracker!!.addMovement(event)
        if (event.actionMasked == MotionEvent.ACTION_CANCEL) {
            if (mVelocityTracker != null) {
                mVelocityTracker!!.recycle()
                mVelocityTracker = null
            }
        }
        if (mTouchMode == ChartTouchListener.Companion.NONE) {
            mGestureDetector.onTouchEvent(event)
        }
        if (!mChart!!.isDragEnabled && !mChart!!.isScaleXEnabled && !mChart!!.isScaleYEnabled) return true
        when (event.action and MotionEvent.ACTION_MASK) {
            MotionEvent.ACTION_DOWN -> {
                startAction(event)
                stopDeceleration()
                saveTouchStart(event)
            }
            MotionEvent.ACTION_POINTER_DOWN -> if (event.pointerCount >= 2) {
                mChart!!.disableScroll()
                saveTouchStart(event)

                // get the distance between the pointers on the x-axis
                mSavedXDist = getXDist(event)

                // get the distance between the pointers on the y-axis
                mSavedYDist = getYDist(event)

                // get the total distance between the pointers
                mSavedDist = spacing(event)
                if (mSavedDist > 10f) {
                    mTouchMode = if (mChart!!.isPinchZoomEnabled) {
                        ChartTouchListener.Companion.PINCH_ZOOM
                    } else {
                        if (mChart!!.isScaleXEnabled != mChart!!.isScaleYEnabled) {
                            if (mChart!!.isScaleXEnabled) ChartTouchListener.Companion.X_ZOOM else ChartTouchListener.Companion.Y_ZOOM
                        } else {
                            if (mSavedXDist > mSavedYDist) ChartTouchListener.Companion.X_ZOOM else ChartTouchListener.Companion.Y_ZOOM
                        }
                    }
                }

                // determine the touch-pointer center
                midPoint(mTouchPointCenter, event)
            }
            MotionEvent.ACTION_MOVE -> if (mTouchMode == ChartTouchListener.Companion.DRAG) {
                mChart!!.disableScroll()
                val x = if (mChart!!.isDragXEnabled) event.x - mTouchStartPoint.x else 0f
                val y = if (mChart!!.isDragYEnabled) event.y - mTouchStartPoint.y else 0f
                performDrag(event, x, y)
            } else if (mTouchMode == ChartTouchListener.Companion.X_ZOOM || mTouchMode == ChartTouchListener.Companion.Y_ZOOM || mTouchMode == ChartTouchListener.Companion.PINCH_ZOOM) {
                mChart!!.disableScroll()
                if (mChart!!.isScaleXEnabled || mChart!!.isScaleYEnabled) performZoom(event)
            } else if (mTouchMode == ChartTouchListener.Companion.NONE
                && Math.abs(
                    ChartTouchListener.Companion.distance(
                        event.x, mTouchStartPoint.x, event.y,
                        mTouchStartPoint.y
                    )
                ) > mDragTriggerDist
            ) {
                if (mChart!!.isDragEnabled) {
                    val shouldPan = !mChart!!.isFullyZoomedOut ||
                            !mChart!!.hasNoDragOffset()
                    if (shouldPan) {
                        val distanceX = Math.abs(event.x - mTouchStartPoint.x)
                        val distanceY = Math.abs(event.y - mTouchStartPoint.y)

                        // Disable dragging in a direction that's disallowed
                        if ((mChart!!.isDragXEnabled || distanceY >= distanceX) &&
                            (mChart!!.isDragYEnabled || distanceY <= distanceX)
                        ) {
                            mLastGesture = ChartGesture.DRAG
                            mTouchMode = ChartTouchListener.Companion.DRAG
                        }
                    } else {
                        if (mChart!!.isHighlightPerDragEnabled) {
                            mLastGesture = ChartGesture.DRAG
                            if (mChart!!.isHighlightPerDragEnabled) performHighlightDrag(event)
                        }
                    }
                }
            }
            MotionEvent.ACTION_UP -> {
                val velocityTracker = mVelocityTracker
                val pointerId = event.getPointerId(0)
                velocityTracker!!.computeCurrentVelocity(1000, maximumFlingVelocity.toFloat())
                val velocityY = velocityTracker.getYVelocity(pointerId)
                val velocityX = velocityTracker.getXVelocity(pointerId)
                if (Math.abs(velocityX) > minimumFlingVelocity ||
                    Math.abs(velocityY) > minimumFlingVelocity
                ) {
                    if (mTouchMode == ChartTouchListener.Companion.DRAG && mChart!!.isDragDecelerationEnabled) {
                        stopDeceleration()
                        mDecelerationLastTime = AnimationUtils.currentAnimationTimeMillis()
                        mDecelerationCurrentPoint.x = event.x
                        mDecelerationCurrentPoint.y = event.y
                        mDecelerationVelocity.x = velocityX
                        mDecelerationVelocity.y = velocityY
                        postInvalidateOnAnimation(mChart!!) // This causes computeScroll to fire, recommended for this by
                        // Google
                    }
                }
                if (mTouchMode == ChartTouchListener.Companion.X_ZOOM || mTouchMode == ChartTouchListener.Companion.Y_ZOOM || mTouchMode == ChartTouchListener.Companion.PINCH_ZOOM || mTouchMode == ChartTouchListener.Companion.POST_ZOOM) {

                    // Range might have changed, which means that Y-axis labels
                    // could have changed in size, affecting Y-axis size.
                    // So we need to recalculate offsets.
                    mChart!!.calculateOffsets()
                    mChart!!.postInvalidate()
                }
                mTouchMode = ChartTouchListener.Companion.NONE
                mChart!!.enableScroll()
                if (mVelocityTracker != null) {
                    mVelocityTracker!!.recycle()
                    mVelocityTracker = null
                }
                endAction(event)
            }
            MotionEvent.ACTION_POINTER_UP -> {
                velocityTrackerPointerUpCleanUpIfNecessary(event, mVelocityTracker!!)
                mTouchMode = ChartTouchListener.Companion.POST_ZOOM
            }
            MotionEvent.ACTION_CANCEL -> {
                mTouchMode = ChartTouchListener.Companion.NONE
                endAction(event)
            }
        }

        // perform the transformation, update the chart
        matrix = mChart!!.viewPortHandler.refresh(matrix, mChart!!, true)
        return true // indicate event was handled
    }
    /**
     * ################ ################ ################ ################
     */
    /** BELOW CODE PERFORMS THE ACTUAL TOUCH ACTIONS  */
    /**
     * Saves the current Matrix state and the touch-start point.
     *
     * @param event
     */
    private fun saveTouchStart(event: MotionEvent) {
        mSavedMatrix.set(matrix)
        mTouchStartPoint.x = event.x
        mTouchStartPoint.y = event.y
        mClosestDataSetToTouch = mChart!!.getDataSetByTouchPoint(event.x, event.y)
    }

    /**
     * Performs all necessary operations needed for dragging.
     *
     * @param event
     */
    private fun performDrag(event: MotionEvent, distanceX: Float, distanceY: Float) {
        var distanceX = distanceX
        var distanceY = distanceY
        mLastGesture = ChartGesture.DRAG
        matrix.set(mSavedMatrix)
        val l = mChart!!.onChartGestureListener

        // check if axis is inverted
        if (inverted()) {

            // if there is an inverted horizontalbarchart
            if (mChart is HorizontalBarChart) {
                distanceX = -distanceX
            } else {
                distanceY = -distanceY
            }
        }
        matrix.postTranslate(distanceX, distanceY)
        l?.onChartTranslate(event, distanceX, distanceY)
    }

    /**
     * Performs the all operations necessary for pinch and axis zoom.
     *
     * @param event
     */
    private fun performZoom(event: MotionEvent) {
        if (event.pointerCount >= 2) { // two finger zoom
            val l = mChart!!.onChartGestureListener

            // get the distance between the pointers of the touch event
            val totalDist = spacing(event)
            if (totalDist > mMinScalePointerDistance) {

                // get the translation
                val t = getTrans(mTouchPointCenter.x, mTouchPointCenter.y)
                val h = mChart!!.viewPortHandler

                // take actions depending on the activated touch mode
                if (mTouchMode == ChartTouchListener.Companion.PINCH_ZOOM) {
                    mLastGesture = ChartGesture.PINCH_ZOOM
                    val scale = totalDist / mSavedDist // total scale
                    val isZoomingOut = scale < 1
                    val canZoomMoreX = if (isZoomingOut) h.canZoomOutMoreX() else h.canZoomInMoreX()
                    val canZoomMoreY = if (isZoomingOut) h.canZoomOutMoreY() else h.canZoomInMoreY()
                    val scaleX = if (mChart!!.isScaleXEnabled) scale else 1f
                    val scaleY = if (mChart!!.isScaleYEnabled) scale else 1f
                    if (canZoomMoreY || canZoomMoreX) {
                        matrix.set(mSavedMatrix)
                        matrix.postScale(scaleX, scaleY, t.x, t.y)
                        l?.onChartScale(event, scaleX, scaleY)
                    }
                } else if (mTouchMode == ChartTouchListener.Companion.X_ZOOM && mChart!!.isScaleXEnabled) {
                    mLastGesture = ChartGesture.X_ZOOM
                    val xDist = getXDist(event)
                    val scaleX = xDist / mSavedXDist // x-axis scale
                    val isZoomingOut = scaleX < 1
                    val canZoomMoreX = if (isZoomingOut) h.canZoomOutMoreX() else h.canZoomInMoreX()
                    if (canZoomMoreX) {
                        matrix.set(mSavedMatrix)
                        matrix.postScale(scaleX, 1f, t.x, t.y)
                        l?.onChartScale(event, scaleX, 1f)
                    }
                } else if (mTouchMode == ChartTouchListener.Companion.Y_ZOOM && mChart!!.isScaleYEnabled) {
                    mLastGesture = ChartGesture.Y_ZOOM
                    val yDist = getYDist(event)
                    val scaleY = yDist / mSavedYDist // y-axis scale
                    val isZoomingOut = scaleY < 1
                    val canZoomMoreY = if (isZoomingOut) h.canZoomOutMoreY() else h.canZoomInMoreY()
                    if (canZoomMoreY) {
                        matrix.set(mSavedMatrix)
                        matrix.postScale(1f, scaleY, t.x, t.y)
                        l?.onChartScale(event, 1f, scaleY)
                    }
                }
                recycleInstance(t)
            }
        }
    }

    /**
     * Highlights upon dragging, generates callbacks for the selection-listener.
     *
     * @param e
     */
    private fun performHighlightDrag(e: MotionEvent) {
        val h = mChart!!.getHighlightByTouchPoint(e.x, e.y)
        if (h != null && !h.equalTo(mLastHighlighted)) {
            mLastHighlighted = h
            mChart!!.highlightValue(h, true)
        }
    }

    /**
     * Returns a recyclable MPPointF instance.
     * returns the correct translation depending on the provided x and y touch
     * points
     *
     * @param x
     * @param y
     * @return
     */
    fun getTrans(x: Float, y: Float): MPPointF {
        val vph = mChart!!.viewPortHandler
        val xTrans = x - vph.offsetLeft()
        var yTrans = 0f

        // check if axis is inverted
        yTrans = if (inverted()) {
            -(y - vph.offsetTop())
        } else {
            -(mChart!!.measuredHeight - y - vph.offsetBottom())
        }
        return MPPointF.getInstance(xTrans, yTrans)
    }

    /**
     * Returns true if the current touch situation should be interpreted as inverted, false if not.
     *
     * @return
     */
    private fun inverted(): Boolean {
        return mClosestDataSetToTouch == null && mChart!!.isAnyAxisInverted || (mClosestDataSetToTouch != null
                && mChart!!.isInverted(mClosestDataSetToTouch!!.axisDependency))
    }
    /**
     * ################ ################ ################ ################
     */
    /** GETTERS AND GESTURE RECOGNITION BELOW  */
    /**
     * Sets the minimum distance that will be interpreted as a "drag" by the chart in dp.
     * Default: 3dp
     *
     * @param dragTriggerDistance
     */
    fun setDragTriggerDist(dragTriggerDistance: Float) {
        mDragTriggerDist = convertDpToPixel(dragTriggerDistance)
    }

    override fun onDoubleTap(e: MotionEvent): Boolean {
        mLastGesture = ChartGesture.DOUBLE_TAP
        val l = mChart!!.onChartGestureListener
        l?.onChartDoubleTapped(e)

        // check if double-tap zooming is enabled
        if (mChart!!.isDoubleTapToZoomEnabled && mChart!!.data!!.entryCount > 0) {
            val trans = getTrans(e.x, e.y)
            val scaleX = if (mChart!!.isScaleXEnabled) 1.4f else 1f
            val scaleY = if (mChart!!.isScaleYEnabled) 1.4f else 1f
            mChart!!.zoom(scaleX, scaleY, trans.x, trans.y)
            if (mChart!!.isLogEnabled) Log.i(
                "BarlineChartTouch", "Double-Tap, Zooming In, x: " + trans.x + ", y: "
                        + trans.y
            )
            l?.onChartScale(e, scaleX, scaleY)
            recycleInstance(trans)
        }
        return super.onDoubleTap(e)
    }

    override fun onLongPress(e: MotionEvent) {
        mLastGesture = ChartGesture.LONG_PRESS
        val l = mChart!!.onChartGestureListener
        l?.onChartLongPressed(e)
    }

    override fun onSingleTapUp(e: MotionEvent): Boolean {
        mLastGesture = ChartGesture.SINGLE_TAP
        val l = mChart!!.onChartGestureListener
        l?.onChartSingleTapped(e)
        if (!mChart!!.isHighlightPerTapEnabled) {
            return false
        }
        val h = mChart!!.getHighlightByTouchPoint(e.x, e.y)
        performHighlight(h, e)
        return super.onSingleTapUp(e)
    }

    override fun onFling(
        e1: MotionEvent,
        e2: MotionEvent,
        velocityX: Float,
        velocityY: Float
    ): Boolean {
        mLastGesture = ChartGesture.FLING
        val l = mChart!!.onChartGestureListener
        l?.onChartFling(e1, e2, velocityX, velocityY)
        return super.onFling(e1, e2, velocityX, velocityY)
    }

    fun stopDeceleration() {
        mDecelerationVelocity.x = 0
        mDecelerationVelocity.y = 0
    }

    fun computeScroll() {
        if (mDecelerationVelocity.x == 0f && mDecelerationVelocity.y == 0f) return  // There's no deceleration in progress
        val currentTime = AnimationUtils.currentAnimationTimeMillis()
        mDecelerationVelocity.x *= mChart!!.dragDecelerationFrictionCoef
        mDecelerationVelocity.y *= mChart!!.dragDecelerationFrictionCoef
        val timeInterval = (currentTime - mDecelerationLastTime).toFloat() / 1000f
        val distanceX = mDecelerationVelocity.x * timeInterval
        val distanceY = mDecelerationVelocity.y * timeInterval
        mDecelerationCurrentPoint.x += distanceX
        mDecelerationCurrentPoint.y += distanceY
        val event = MotionEvent.obtain(
            currentTime, currentTime, MotionEvent.ACTION_MOVE, mDecelerationCurrentPoint.x,
            mDecelerationCurrentPoint.y, 0
        )
        val dragDistanceX =
            if (mChart!!.isDragXEnabled) mDecelerationCurrentPoint.x - mTouchStartPoint.x else 0f
        val dragDistanceY =
            if (mChart!!.isDragYEnabled) mDecelerationCurrentPoint.y - mTouchStartPoint.y else 0f
        performDrag(event, dragDistanceX, dragDistanceY)
        event.recycle()
        matrix = mChart!!.viewPortHandler.refresh(matrix, mChart!!, false)
        mDecelerationLastTime = currentTime
        if (Math.abs(mDecelerationVelocity.x) >= 0.01 || Math.abs(mDecelerationVelocity.y) >= 0.01) postInvalidateOnAnimation(
            mChart!!
        ) // This causes computeScroll to fire, recommended for this by Google
        else {
            // Range might have changed, which means that Y-axis labels
            // could have changed in size, affecting Y-axis size.
            // So we need to recalculate offsets.
            mChart!!.calculateOffsets()
            mChart!!.postInvalidate()
            stopDeceleration()
        }
    }

    companion object {
        /**
         * ################ ################ ################ ################
         */
        /** DOING THE MATH BELOW ;-)  */
        /**
         * Determines the center point between two pointer touch points.
         *
         * @param point
         * @param event
         */
        private fun midPoint(point: MPPointF, event: MotionEvent) {
            val x = event.getX(0) + event.getX(1)
            val y = event.getY(0) + event.getY(1)
            point.x = x / 2f
            point.y = y / 2f
        }

        /**
         * returns the distance between two pointer touch points
         *
         * @param event
         * @return
         */
        private fun spacing(event: MotionEvent): Float {
            val x = event.getX(0) - event.getX(1)
            val y = event.getY(0) - event.getY(1)
            return Math.sqrt((x * x + y * y).toDouble()).toFloat()
        }

        /**
         * calculates the distance on the x-axis between two pointers (fingers on
         * the display)
         *
         * @param e
         * @return
         */
        private fun getXDist(e: MotionEvent): Float {
            return Math.abs(e.getX(0) - e.getX(1))
        }

        /**
         * calculates the distance on the y-axis between two pointers (fingers on
         * the display)
         *
         * @param e
         * @return
         */
        private fun getYDist(e: MotionEvent): Float {
            return Math.abs(e.getY(0) - e.getY(1))
        }
    }

    /**
     * Constructor with initialization parameters.
     *
     * @param chart               instance of the chart
     * @param touchMatrix         the touch-matrix of the chart
     * @param dragTriggerDistance the minimum movement distance that will be interpreted as a "drag" gesture in dp (3dp equals
     * to about 9 pixels on a 5.5" FHD screen)
     */
    init {
        matrix = touchMatrix
        mDragTriggerDist = convertDpToPixel(dragTriggerDistance)
        mMinScalePointerDistance = convertDpToPixel(3.5f)
    }
}