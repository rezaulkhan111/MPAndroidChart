package com.github.mikephil.charting.charts

import android.animation.ObjectAnimator
import android.annotation.SuppressLint
import android.content.Context
import android.util.AttributeSet
import android.util.Log
import android.view.MotionEvent
import com.github.mikephil.charting.animation.Easing.EasingFunction
import com.github.mikephil.charting.components.AxisBase.isDrawLabelsEnabled
import com.github.mikephil.charting.components.ComponentBase.isEnabled
import com.github.mikephil.charting.components.Legend.*
import com.github.mikephil.charting.components.Legend.horizontalAlignment
import com.github.mikephil.charting.components.Legend.isDrawInsideEnabled
import com.github.mikephil.charting.components.Legend.maxSizePercent
import com.github.mikephil.charting.components.Legend.orientation
import com.github.mikephil.charting.components.Legend.verticalAlignment
import com.github.mikephil.charting.data.*
import com.github.mikephil.charting.data.BarEntry.y
import com.github.mikephil.charting.data.BaseEntry.y
import com.github.mikephil.charting.highlight.Highlight.y
import com.github.mikephil.charting.interfaces.datasets.IDataSet
import com.github.mikephil.charting.listener.PieRadarChartTouchListener
import com.github.mikephil.charting.renderer.LegendRenderer.computeLegend
import com.github.mikephil.charting.utils.MPPointD.Companion.recycleInstance
import com.github.mikephil.charting.utils.MPPointF
import com.github.mikephil.charting.utils.MPPointF.Companion.recycleInstance
import com.github.mikephil.charting.utils.Utils.convertDpToPixel
import com.github.mikephil.charting.utils.Utils.getNormalizedAngle
import com.github.mikephil.charting.utils.ViewPortHandler.chartHeight
import com.github.mikephil.charting.utils.ViewPortHandler.chartWidth
import com.github.mikephil.charting.utils.ViewPortHandler.contentRect
import com.github.mikephil.charting.utils.ViewPortHandler.restrainViewPort

/**
 * Baseclass of PieChart and RadarChart.
 *
 * @author Philipp Jahoda
 */
abstract class PieRadarChartBase<T : ChartData<out IDataSet<out Entry?>?>?> : Chart<T> {
    /**
     * holds the normalized version of the current rotation angle of the chart
     */
    private var mRotationAngle = 270f
    /**
     * gets the raw version of the current rotation angle of the pie chart the
     * returned value could be any value, negative or positive, outside of the
     * 360 degrees. this is used when working with rotation direction, mainly by
     * gestures and animations.
     *
     * @return
     */
    /**
     * holds the raw version of the current rotation angle of the chart
     */
    var rawRotationAngle = 270f
        private set
    /**
     * Returns true if rotation of the chart by touch is enabled, false if not.
     *
     * @return
     */
    /**
     * Set this to true to enable the rotation / spinning of the chart by touch.
     * Set it to false to disable it. Default: true
     *
     * @param enabled
     */
    /**
     * flag that indicates if rotation is enabled or not
     */
    var isRotationEnabled = true
    /**
     * Gets the minimum offset (padding) around the chart, defaults to 0.f
     */
    /**
     * Sets the minimum offset (padding) around the chart, defaults to 0.f
     */
    /**
     * Sets the minimum offset (padding) around the chart, defaults to 0.f
     */
    var minOffset = 0f

    constructor(context: Context?) : super(context) {}
    constructor(context: Context?, attrs: AttributeSet?) : super(context, attrs) {}
    constructor(context: Context?, attrs: AttributeSet?, defStyle: Int) : super(
        context,
        attrs,
        defStyle
    ) {
    }

    override fun init() {
        super.init()
        mChartTouchListener = PieRadarChartTouchListener(this)
    }

    override fun calcMinMax() {
        //mXAxis.mAxisRange = mData.getXVals().size() - 1;
    }

    override fun onTouchEvent(event: MotionEvent): Boolean {
        // use the pie- and radarchart listener own listener
        return if (mTouchEnabled && mChartTouchListener != null) mChartTouchListener.onTouch(
            this,
            event
        ) else super.onTouchEvent(event)
    }

    override fun computeScroll() {
        if (mChartTouchListener is PieRadarChartTouchListener) (mChartTouchListener as PieRadarChartTouchListener).computeScroll()
    }

    override fun notifyDataSetChanged() {
        if (mData == null) return
        calcMinMax()
        if (mLegend != null) mLegendRenderer.computeLegend(mData)
        calculateOffsets()
    }

    public override fun calculateOffsets() {
        var legendLeft = 0f
        var legendRight = 0f
        var legendBottom = 0f
        var legendTop = 0f
        if (mLegend != null && mLegend.isEnabled && !mLegend.isDrawInsideEnabled) {
            val fullLegendWidth = Math.min(
                mLegend.mNeededWidth,
                mViewPortHandler.chartWidth * mLegend.maxSizePercent
            )
            when (mLegend.orientation) {
                LegendOrientation.VERTICAL -> {
                    var xLegendOffset = 0f
                    if (mLegend.horizontalAlignment === LegendHorizontalAlignment.LEFT
                        || mLegend.horizontalAlignment === LegendHorizontalAlignment.RIGHT
                    ) {
                        if (mLegend.verticalAlignment === LegendVerticalAlignment.CENTER) {
                            // this is the space between the legend and the chart
                            val spacing = convertDpToPixel(13f)
                            xLegendOffset = fullLegendWidth + spacing
                        } else {
                            // this is the space between the legend and the chart
                            val spacing = convertDpToPixel(8f)
                            val legendWidth = fullLegendWidth + spacing
                            val legendHeight = mLegend.mNeededHeight + mLegend.mTextHeightMax
                            val center = center
                            val bottomX = if (mLegend.horizontalAlignment ===
                                LegendHorizontalAlignment.RIGHT
                            ) getWidth() - legendWidth + 15f else legendWidth - 15f
                            val bottomY = legendHeight + 15f
                            val distLegend = distanceToCenter(bottomX, bottomY)
                            val reference = getPosition(
                                center, radius,
                                getAngleForPoint(bottomX, bottomY)
                            )
                            val distReference = distanceToCenter(reference.x, reference.y)
                            val minOffset = convertDpToPixel(5f)
                            if (bottomY >= center.y && getHeight() - legendWidth > getWidth()) {
                                xLegendOffset = legendWidth
                            } else if (distLegend < distReference) {
                                val diff = distReference - distLegend
                                xLegendOffset = minOffset + diff
                            }
                            recycleInstance(center)
                            recycleInstance(reference)
                        }
                    }
                    when (mLegend.horizontalAlignment) {
                        LegendHorizontalAlignment.LEFT -> legendLeft = xLegendOffset
                        LegendHorizontalAlignment.RIGHT -> legendRight = xLegendOffset
                        LegendHorizontalAlignment.CENTER -> when (mLegend.verticalAlignment) {
                            LegendVerticalAlignment.TOP -> legendTop = Math.min(
                                mLegend.mNeededHeight,
                                mViewPortHandler.chartHeight * mLegend.maxSizePercent
                            )
                            LegendVerticalAlignment.BOTTOM -> legendBottom = Math.min(
                                mLegend.mNeededHeight,
                                mViewPortHandler.chartHeight * mLegend.maxSizePercent
                            )
                        }
                    }
                }
                LegendOrientation.HORIZONTAL -> {
                    var yLegendOffset = 0f
                    if (mLegend.verticalAlignment === LegendVerticalAlignment.TOP ||
                        mLegend.verticalAlignment === LegendVerticalAlignment.BOTTOM
                    ) {

                        // It's possible that we do not need this offset anymore as it
                        //   is available through the extraOffsets, but changing it can mean
                        //   changing default visibility for existing apps.
                        val yOffset = requiredLegendOffset
                        yLegendOffset = Math.min(
                            mLegend.mNeededHeight + yOffset,
                            mViewPortHandler.chartHeight * mLegend.maxSizePercent
                        )
                        when (mLegend.verticalAlignment) {
                            LegendVerticalAlignment.TOP -> legendTop = yLegendOffset
                            LegendVerticalAlignment.BOTTOM -> legendBottom = yLegendOffset
                        }
                    }
                }
            }
            legendLeft += requiredBaseOffset
            legendRight += requiredBaseOffset
            legendTop += requiredBaseOffset
            legendBottom += requiredBaseOffset
        }
        var minOffset = convertDpToPixel(
            minOffset
        )
        if (this is RadarChart) {
            val x = getXAxis()
            if (x!!.isEnabled && x!!.isDrawLabelsEnabled) {
                minOffset = Math.max(minOffset, x!!.mLabelRotatedWidth)
            }
        }
        legendTop += extraTopOffset
        legendRight += extraRightOffset
        legendBottom += extraBottomOffset
        legendLeft += extraLeftOffset
        val offsetLeft = Math.max(minOffset, legendLeft)
        val offsetTop = Math.max(minOffset, legendTop)
        val offsetRight = Math.max(minOffset, legendRight)
        val offsetBottom = Math.max(
            minOffset, Math.max(
                requiredBaseOffset, legendBottom
            )
        )
        mViewPortHandler.restrainViewPort(offsetLeft, offsetTop, offsetRight, offsetBottom)
        if (mLogEnabled) Log.i(
            Chart.Companion.LOG_TAG, "offsetLeft: " + offsetLeft + ", offsetTop: " + offsetTop
                    + ", offsetRight: " + offsetRight + ", offsetBottom: " + offsetBottom
        )
    }

    /**
     * returns the angle relative to the chart center for the given point on the
     * chart in degrees. The angle is always between 0 and 360°, 0° is NORTH,
     * 90° is EAST, ...
     *
     * @param x
     * @param y
     * @return
     */
    fun getAngleForPoint(x: Float, y: Float): Float {
        val c: MPPointF = getCenterOffsets()
        val tx = (x - c.x).toDouble()
        val ty = (y - c.y).toDouble()
        val length = Math.sqrt(tx * tx + ty * ty)
        val r = Math.acos(ty / length)
        var angle = Math.toDegrees(r).toFloat()
        if (x > c.x) angle = 360f - angle

        // add 90° because chart starts EAST
        angle = angle + 90f

        // neutralize overflow
        if (angle > 360f) angle = angle - 360f
        recycleInstance(c)
        return angle
    }

    /**
     * Returns a recyclable MPPointF instance.
     * Calculates the position around a center point, depending on the distance
     * from the center, and the angle of the position around the center.
     *
     * @param center
     * @param dist
     * @param angle  in degrees, converted to radians internally
     * @return
     */
    fun getPosition(center: MPPointF, dist: Float, angle: Float): MPPointF {
        val p = MPPointF.getInstance(0, 0)
        getPosition(center, dist, angle, p)
        return p
    }

    fun getPosition(center: MPPointF, dist: Float, angle: Float, outputPoint: MPPointF) {
        outputPoint.x = (center.x + dist * Math.cos(Math.toRadians(angle.toDouble()))).toFloat()
        outputPoint.y = (center.y + dist * Math.sin(Math.toRadians(angle.toDouble()))).toFloat()
    }

    /**
     * Returns the distance of a certain point on the chart to the center of the
     * chart.
     *
     * @param x
     * @param y
     * @return
     */
    fun distanceToCenter(x: Float, y: Float): Float {
        val c: MPPointF = getCenterOffsets()
        var dist = 0f
        var xDist = 0f
        var yDist = 0f
        xDist = if (x > c.x) {
            x - c.x
        } else {
            c.x - x
        }
        yDist = if (y > c.y) {
            y - c.y
        } else {
            c.y - y
        }

        // pythagoras
        dist =
            Math.sqrt(Math.pow(xDist.toDouble(), 2.0) + Math.pow(yDist.toDouble(), 2.0)).toFloat()
        recycleInstance(c)
        return dist
    }

    /**
     * Returns the xIndex for the given angle around the center of the chart.
     * Returns -1 if not found / outofbounds.
     *
     * @param angle
     * @return
     */
    abstract fun getIndexForAngle(angle: Float): Int
    /**
     * gets a normalized version of the current rotation angle of the pie chart,
     * which will always be between 0.0 < 360.0
     *
     * @return
     */
    /**
     * Set an offset for the rotation of the RadarChart in degrees. Default 270f
     * --> top (NORTH)
     *
     * @param angle
     */
    var rotationAngle: Float
        get() = mRotationAngle
        set(angle) {
            rawRotationAngle = angle
            mRotationAngle = getNormalizedAngle(
                rawRotationAngle
            )
        }

    /**
     * returns the diameter of the pie- or radar-chart
     *
     * @return
     */
    val diameter: Float
        get() {
            val content = mViewPortHandler.contentRect
            content.left += extraLeftOffset
            content.top += extraTopOffset
            content.right -= extraRightOffset
            content.bottom -= extraBottomOffset
            return Math.min(content.width(), content.height())
        }

    /**
     * Returns the radius of the chart in pixels.
     *
     * @return
     */
    abstract val radius: Float

    /**
     * Returns the required offset for the chart legend.
     *
     * @return
     */
    protected abstract val requiredLegendOffset: Float

    /**
     * Returns the base offset needed for the chart without calculating the
     * legend size.
     *
     * @return
     */
    abstract val requiredBaseOffset: Float
        protected get

    // TODO Auto-generated method stub
    val yChartMax: Float
        get() =// TODO Auto-generated method stub
            0

    // TODO Auto-generated method stub
    val yChartMin: Float
        get() =// TODO Auto-generated method stub
            0
    /**
     * ################ ################ ################ ################
     */
    /** CODE BELOW THIS RELATED TO ANIMATION  */
    /**
     * Applys a spin animation to the Chart.
     *
     * @param durationmillis
     * @param fromangle
     * @param toangle
     */
    @SuppressLint("NewApi")
    fun spin(durationmillis: Int, fromangle: Float, toangle: Float, easing: EasingFunction?) {
        rotationAngle = fromangle
        val spinAnimator = ObjectAnimator.ofFloat(
            this, "rotationAngle", fromangle,
            toangle
        )
        spinAnimator.duration = durationmillis.toLong()
        spinAnimator.interpolator = easing
        spinAnimator.addUpdateListener { postInvalidate() }
        spinAnimator.start()
    }
}