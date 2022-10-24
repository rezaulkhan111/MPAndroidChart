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
import com.github.mikephil.charting.listener.BarLineChartTouchListener
import com.github.mikephil.charting.charts.HorizontalBarChart
import com.github.mikephil.charting.utils.ViewPortHandler
import com.github.mikephil.charting.charts.PieRadarChartBase
import com.github.mikephil.charting.listener.PieRadarChartTouchListener.AngularVelocitySample

/**
 * Listener for callbacks when doing gestures on the chart.
 *
 * @author Philipp Jahoda
 */
interface OnChartGestureListener {
    /**
     * Callbacks when a touch-gesture has started on the chart (ACTION_DOWN)
     *
     * @param me
     * @param lastPerformedGesture
     */
    fun onChartGestureStart(me: MotionEvent?, lastPerformedGesture: ChartGesture?)

    /**
     * Callbacks when a touch-gesture has ended on the chart (ACTION_UP, ACTION_CANCEL)
     *
     * @param me
     * @param lastPerformedGesture
     */
    fun onChartGestureEnd(me: MotionEvent?, lastPerformedGesture: ChartGesture?)

    /**
     * Callbacks when the chart is longpressed.
     *
     * @param me
     */
    fun onChartLongPressed(me: MotionEvent?)

    /**
     * Callbacks when the chart is double-tapped.
     *
     * @param me
     */
    fun onChartDoubleTapped(me: MotionEvent?)

    /**
     * Callbacks when the chart is single-tapped.
     *
     * @param me
     */
    fun onChartSingleTapped(me: MotionEvent?)

    /**
     * Callbacks then a fling gesture is made on the chart.
     *
     * @param me1
     * @param me2
     * @param velocityX
     * @param velocityY
     */
    fun onChartFling(me1: MotionEvent?, me2: MotionEvent?, velocityX: Float, velocityY: Float)

    /**
     * Callbacks when the chart is scaled / zoomed via pinch zoom / double-tap gesture.
     *
     * @param me
     * @param scaleX scalefactor on the x-axis
     * @param scaleY scalefactor on the y-axis
     */
    fun onChartScale(me: MotionEvent?, scaleX: Float, scaleY: Float)

    /**
     * Callbacks when the chart is moved / translated via drag gesture.
     *
     * @param me
     * @param dX translation distance on the x-axis
     * @param dY translation distance on the y-axis
     */
    fun onChartTranslate(me: MotionEvent?, dX: Float, dY: Float)
}