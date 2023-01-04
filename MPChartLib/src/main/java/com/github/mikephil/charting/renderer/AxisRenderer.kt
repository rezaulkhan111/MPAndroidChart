package com.github.mikephil.charting.renderer

import android.graphics.Canvas
import android.graphics.Color
import android.graphics.Paint
import com.github.mikephil.charting.components.AxisBase
import com.github.mikephil.charting.utils.MPPointD
import com.github.mikephil.charting.utils.Transformer
import com.github.mikephil.charting.utils.Utils
import com.github.mikephil.charting.utils.ViewPortHandler

/**
 * Baseclass of all axis renderers.
 *
 * @author Philipp Jahoda
 */
abstract class AxisRenderer : Renderer {

    /** base axis this axis renderer works with  */
    protected var mAxis: AxisBase? = null

    /** transformer to transform values to screen pixels and return  */
    protected var mTrans: Transformer? = null

    /**
     * paint object for the grid lines
     */
    protected var mGridPaint: Paint? = null

    /**
     * paint for the x-label values
     */
    protected var mAxisLabelPaint: Paint? = null

    /**
     * paint for the line surrounding the chart
     */
    protected var mAxisLinePaint: Paint? = null

    /**
     * paint used for the limit lines
     */
    protected var mLimitLinePaint: Paint? = null

    constructor(viewPortHandler: ViewPortHandler?, trans: Transformer?, axis: AxisBase?) : super(
        viewPortHandler
    ) {
        mTrans = trans
        mAxis = axis
        if (mViewPortHandler != null) {
            mAxisLabelPaint = Paint(Paint.ANTI_ALIAS_FLAG)
            mGridPaint = Paint()
            mGridPaint!!.color = Color.GRAY
            mGridPaint!!.strokeWidth = 1f
            mGridPaint!!.style = Paint.Style.STROKE
            mGridPaint!!.alpha = 90
            mAxisLinePaint = Paint()
            mAxisLinePaint!!.color = Color.BLACK
            mAxisLinePaint!!.strokeWidth = 1f
            mAxisLinePaint!!.style = Paint.Style.STROKE
            mLimitLinePaint = Paint(Paint.ANTI_ALIAS_FLAG)
            mLimitLinePaint!!.style = Paint.Style.STROKE
        }
    }

    /**
     * Returns the Paint object used for drawing the axis (labels).
     *
     * @return
     */
    open fun getPaintAxisLabels(): Paint? {
        return mAxisLabelPaint
    }

    /**
     * Returns the Paint object that is used for drawing the grid-lines of the
     * axis.
     *
     * @return
     */
    open fun getPaintGrid(): Paint? {
        return mGridPaint
    }

    /**
     * Returns the Paint object that is used for drawing the axis-line that goes
     * alongside the axis.
     *
     * @return
     */
    open fun getPaintAxisLine(): Paint? {
        return mAxisLinePaint
    }

    /**
     * Returns the Transformer object used for transforming the axis values.
     *
     * @return
     */
    open fun getTransformer(): Transformer? {
        return mTrans
    }

    /**
     * Computes the axis values.
     *
     * @param min - the minimum value in the data object for this axis
     * @param max - the maximum value in the data object for this axis
     */
    open fun computeAxis(min: Float, max: Float, inverted: Boolean) {

        // calculate the starting and entry point of the y-labels (depending on
        // zoom / contentrect bounds)
        var min = min
        var max = max
        if (mViewPortHandler != null && mViewPortHandler?.contentWidth()!! > 10 && !mViewPortHandler?.isFullyZoomedOutY()!!) {
            val p1 = mTrans!!.getValuesByTouchPoint(
                mViewPortHandler!!.contentLeft(),
                mViewPortHandler!!.contentTop()
            )
            val p2 = mTrans!!.getValuesByTouchPoint(
                mViewPortHandler!!.contentLeft(),
                mViewPortHandler!!.contentBottom()
            )
            if (!inverted) {
                min = p2!!.y.toFloat()
                max = p1!!.y.toFloat()
            } else {
                min = p1!!.y.toFloat()
                max = p2!!.y.toFloat()
            }
            MPPointD.recycleInstance(p1)
            MPPointD.recycleInstance(p2)
        }
        computeAxisValues(min, max)
    }

    /**
     * Sets up the axis values. Computes the desired number of labels between the two given extremes.
     *
     * @return
     */
    protected open fun computeAxisValues(min: Float, max: Float) {
        val labelCount = mAxis!!.getLabelCount()
        val range = Math.abs(max - min).toDouble()
        if (labelCount == 0 || range <= 0 || java.lang.Double.isInfinite(range)) {
            mAxis!!.mEntries = floatArrayOf()
            mAxis!!.mCenteredEntries = floatArrayOf()
            mAxis!!.mEntryCount = 0
            return
        }

        // Find out how much spacing (in y value space) between axis values
        val rawInterval = range / labelCount
        var interval = Utils.roundToNextSignificant(rawInterval).toDouble()

        // If granularity is enabled, then do not allow the interval to go below specified granularity.
        // This is used to avoid repeated values when rounding values for display.
        if (mAxis!!.isGranularityEnabled()) interval =
            if (interval < mAxis!!.getGranularity()) mAxis!!.getGranularity()
                .toDouble() else interval

        // Normalize interval
        val intervalMagnitude =
            Utils.roundToNextSignificant(Math.pow(10.0, Math.log10(interval).toInt().toDouble()))
                .toDouble()
        val intervalSigDigit = (interval / intervalMagnitude).toInt()
        if (intervalSigDigit > 5) {
            // Use one order of magnitude higher, to avoid intervals like 0.9 or 90
            // if it's 0.0 after floor(), we use the old value
            interval =
                if (Math.floor(10.0 * intervalMagnitude) == 0.0) interval else Math.floor(10.0 * intervalMagnitude)
        }
        var n = if (mAxis!!.isCenterAxisLabelsEnabled()) 1 else 0

        // force label count
        if (mAxis!!.isForceLabelsEnabled()) {
            interval = (range.toFloat() / (labelCount - 1).toFloat()).toDouble()
            mAxis!!.mEntryCount = labelCount
            if (mAxis!!.mEntries.size < labelCount) {
                // Ensure stops contains at least numStops elements.
                mAxis!!.mEntries = FloatArray(labelCount)
            }
            var v = min
            for (i in 0 until labelCount) {
                mAxis!!.mEntries[i] = v
                v += interval.toFloat()
            }
            n = labelCount

            // no forced count
        } else {
            var first = if (interval == 0.0) 0.0 else Math.ceil(min / interval) * interval
            if (mAxis!!.isCenterAxisLabelsEnabled()) {
                first -= interval
            }
            val last = if (interval == 0.0) 0.0 else Utils.nextUp(
                Math.floor(
                    max / interval
                ) * interval
            )
            var f: Double
            var i: Int
            if (interval != 0.0 && last != first) {
                f = first
                while (f <= last) {
                    ++n
                    f += interval
                }
            } else if (last == first && n == 0) {
                n = 1
            }
            mAxis!!.mEntryCount = n
            if (mAxis!!.mEntries.size < n) {
                // Ensure stops contains at least numStops elements.
                mAxis!!.mEntries = FloatArray(n)
            }
            f = first
            i = 0
            while (i < n) {
                if (f == 0.0) // Fix for negative zero case (Where value == -0.0, and 0.0 == -0.0)
                    f = 0.0
                mAxis!!.mEntries[i] = f.toFloat()
                f += interval
                ++i
            }
        }

        // set decimals
        if (interval < 1) {
            mAxis!!.mDecimals = Math.ceil(-Math.log10(interval)).toInt()
        } else {
            mAxis!!.mDecimals = 0
        }
        if (mAxis!!.isCenterAxisLabelsEnabled()) {
            if (mAxis!!.mCenteredEntries.size < n) {
                mAxis!!.mCenteredEntries = FloatArray(n)
            }
            val offset = interval.toFloat() / 2f
            for (i in 0 until n) {
                mAxis!!.mCenteredEntries[i] = mAxis!!.mEntries[i] + offset
            }
        }
    }

    /**
     * Draws the axis labels to the screen.
     *
     * @param c
     */
    abstract fun renderAxisLabels(c: Canvas?)

    /**
     * Draws the grid lines belonging to the axis.
     *
     * @param c
     */
    abstract fun renderGridLines(c: Canvas?)

    /**
     * Draws the line that goes alongside the axis.
     *
     * @param c
     */
    abstract fun renderAxisLine(c: Canvas?)

    /**
     * Draws the LimitLines associated with this axis to the screen.
     *
     * @param c
     */
    abstract fun renderLimitLines(c: Canvas?)
}