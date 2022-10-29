package com.github.mikephil.charting.renderer

import android.graphics.*
import android.graphics.Paint.Align
import com.github.mikephil.charting.components.LimitLine
import com.github.mikephil.charting.components.LimitLine.LimitLabelPosition
import com.github.mikephil.charting.components.XAxis
import com.github.mikephil.charting.components.XAxis.XAxisPosition
import com.github.mikephil.charting.components.YAxis
import com.github.mikephil.charting.utils.*
import com.github.mikephil.charting.utils.FSize.Companion.recycleInstance
import com.github.mikephil.charting.utils.MPPointD.Companion.recycleInstance
import com.github.mikephil.charting.utils.MPPointF.Companion.getInstance
import com.github.mikephil.charting.utils.MPPointF.Companion.recycleInstance
import com.github.mikephil.charting.utils.Transformer.pointValuesToPixel
import com.github.mikephil.charting.utils.Utils.calcTextHeight
import com.github.mikephil.charting.utils.Utils.calcTextSize
import com.github.mikephil.charting.utils.Utils.calcTextWidth
import com.github.mikephil.charting.utils.Utils.convertDpToPixel
import com.github.mikephil.charting.utils.Utils.drawXAxisValue
import com.github.mikephil.charting.utils.Utils.getSizeOfRotatedRectangleByDegrees

open class YAxisRenderer : AxisRenderer {

    var mXAxis: XAxis? = null

    constructor(viewPortHandler: ViewPortHandler?, xAxis: XAxis?, trans: Transformer?) :
            super(
                viewPortHandler,
                trans,
                xAxis
            ) {
        mXAxis = xAxis
        mAxisLabelPaint.setColor(Color.BLACK)
        mAxisLabelPaint.setTextAlign(Align.CENTER)
        mAxisLabelPaint.setTextSize(convertDpToPixel(10f))
    }

    protected open fun setupGridPaint() {
        mGridPaint.setColor(mXAxis!!.gridColor)
        mGridPaint.setStrokeWidth(mXAxis!!.gridLineWidth)
        mGridPaint.setPathEffect(mXAxis!!.gridDashPathEffect)
    }

    override fun computeAxis(min: Float, max: Float, inverted: Boolean) {

        // calculate the starting and entry point of the y-labels (depending on
        // zoom / contentrect bounds)
        var min = min
        var max = max
        if (mViewPortHandler.contentWidth() > 10 && !mViewPortHandler.isFullyZoomedOutX) {
            val p1: MPPointD = mTrans.getValuesByTouchPoint(
                mViewPortHandler.contentLeft(),
                mViewPortHandler.contentTop()
            )
            val p2: MPPointD = mTrans.getValuesByTouchPoint(
                mViewPortHandler.contentRight(),
                mViewPortHandler.contentTop()
            )
            if (inverted) {
                min = p2.x.toFloat()
                max = p1.x.toFloat()
            } else {
                min = p1.x.toFloat()
                max = p2.x.toFloat()
            }
            recycleInstance(p1)
            recycleInstance(p2)
        }
        computeAxisValues(min, max)
    }

    protected override fun computeAxisValues(min: Float, max: Float) {
        super.computeAxisValues(min, max)
        computeSize()
    }

    protected open fun computeSize() {
        val longest = mXAxis!!.longestLabel
        mAxisLabelPaint.setTypeface(mXAxis!!.typeface)
        mAxisLabelPaint.setTextSize(mXAxis!!.textSize)
        val labelSize = calcTextSize(mAxisLabelPaint, longest)
        val labelWidth = labelSize.width
        val labelHeight = calcTextHeight(mAxisLabelPaint, "Q").toFloat()
        val labelRotatedSize = getSizeOfRotatedRectangleByDegrees(
            labelWidth,
            labelHeight,
            mXAxis!!.labelRotationAngle
        )
        mXAxis!!.mLabelWidth = Math.round(labelWidth)
        mXAxis!!.mLabelHeight = Math.round(labelHeight)
        mXAxis!!.mLabelRotatedWidth = Math.round(labelRotatedSize.width)
        mXAxis!!.mLabelRotatedHeight = Math.round(labelRotatedSize.height)
        recycleInstance(labelRotatedSize)
        recycleInstance(labelSize)
    }

    open fun renderAxisLabels(c: Canvas?) {
        if (!mXAxis!!.isEnabled || !mXAxis!!.isDrawLabelsEnabled) return
        val yoffset = mXAxis!!.yOffset
        mAxisLabelPaint.setTypeface(mXAxis!!.typeface)
        mAxisLabelPaint.setTextSize(mXAxis!!.textSize)
        mAxisLabelPaint.setColor(mXAxis!!.textColor)
        val pointF = getInstance(0, 0)
        if (mXAxis!!.position === XAxisPosition.TOP) {
            pointF.x = 0.5f
            pointF.y = 1.0f
            drawLabels(c, mViewPortHandler.contentTop() - yoffset, pointF)
        } else if (mXAxis!!.position === XAxisPosition.TOP_INSIDE) {
            pointF.x = 0.5f
            pointF.y = 1.0f
            drawLabels(
                c,
                mViewPortHandler.contentTop() + yoffset + mXAxis!!.mLabelRotatedHeight,
                pointF
            )
        } else if (mXAxis!!.position === XAxisPosition.BOTTOM) {
            pointF.x = 0.5f
            pointF.y = 0.0f
            drawLabels(c, mViewPortHandler.contentBottom() + yoffset, pointF)
        } else if (mXAxis!!.position === XAxisPosition.BOTTOM_INSIDE) {
            pointF.x = 0.5f
            pointF.y = 0.0f
            drawLabels(
                c,
                mViewPortHandler.contentBottom() - yoffset - mXAxis!!.mLabelRotatedHeight,
                pointF
            )
        } else { // BOTH SIDED
            pointF.x = 0.5f
            pointF.y = 1.0f
            drawLabels(c, mViewPortHandler.contentTop() - yoffset, pointF)
            pointF.x = 0.5f
            pointF.y = 0.0f
            drawLabels(c, mViewPortHandler.contentBottom() + yoffset, pointF)
        }
        recycleInstance(pointF)
    }

    override fun renderAxisLine(c: Canvas) {
        if (!mXAxis!!.isDrawAxisLineEnabled || !mXAxis!!.isEnabled) return
        mAxisLinePaint.setColor(mXAxis!!.axisLineColor)
        mAxisLinePaint.setStrokeWidth(mXAxis!!.axisLineWidth)
        mAxisLinePaint.setPathEffect(mXAxis!!.axisLineDashPathEffect)
        if (mXAxis!!.position === XAxisPosition.TOP || mXAxis!!.position === XAxisPosition.TOP_INSIDE || mXAxis!!.position === XAxisPosition.BOTH_SIDED) {
            c.drawLine(
                mViewPortHandler.contentLeft(),
                mViewPortHandler.contentTop(), mViewPortHandler.contentRight(),
                mViewPortHandler.contentTop(), mAxisLinePaint
            )
        }
        if (mXAxis!!.position === XAxisPosition.BOTTOM || mXAxis!!.position === XAxisPosition.BOTTOM_INSIDE || mXAxis!!.position === XAxisPosition.BOTH_SIDED) {
            c.drawLine(
                mViewPortHandler.contentLeft(),
                mViewPortHandler.contentBottom(), mViewPortHandler.contentRight(),
                mViewPortHandler.contentBottom(), mAxisLinePaint
            )
        }
    }

    /**
     * draws the x-labels on the specified y-position
     *
     * @param pos
     */
    protected open fun drawLabels(c: Canvas?, pos: Float, anchor: MPPointF?) {
        val labelRotationAngleDegrees = mXAxis!!.labelRotationAngle
        val centeringEnabled = mXAxis!!.isCenterAxisLabelsEnabled
        val positions = FloatArray(mXAxis!!.mEntryCount * 2)
        run {
            var i = 0
            while (i < positions.size) {


                // only fill x values
                if (centeringEnabled) {
                    positions[i] = mXAxis!!.mCenteredEntries[i / 2]
                } else {
                    positions[i] = mXAxis!!.mEntries[i / 2]
                }
                i += 2
            }
        }
        mTrans.pointValuesToPixel(positions)
        var i = 0
        while (i < positions.size) {
            var x = positions[i]
            if (mViewPortHandler.isInBoundsX(x)) {
                val label =
                    mXAxis!!.valueFormatter!!.getFormattedValue(mXAxis!!.mEntries[i / 2], mXAxis)
                if (mXAxis!!.isAvoidFirstLastClippingEnabled) {

                    // avoid clipping of the last
                    if (i / 2 == mXAxis!!.mEntryCount - 1 && mXAxis!!.mEntryCount > 1) {
                        val width = calcTextWidth(mAxisLabelPaint, label).toFloat()
                        if (width > mViewPortHandler.offsetRight() * 2
                            && x + width > mViewPortHandler.chartWidth
                        ) x -= width / 2

                        // avoid clipping of the first
                    } else if (i == 0) {
                        val width = calcTextWidth(mAxisLabelPaint, label).toFloat()
                        x += width / 2
                    }
                }
                drawLabel(c, label, x, pos, anchor, labelRotationAngleDegrees)
            }
            i += 2
        }
    }

    protected open fun drawLabel(
        c: Canvas?,
        formattedLabel: String?,
        x: Float,
        y: Float,
        anchor: MPPointF?,
        angleDegrees: Float
    ) {
        drawXAxisValue(
            c!!,
            formattedLabel!!, x, y, mAxisLabelPaint, anchor!!, angleDegrees
        )
    }

    protected var mRenderGridLinesPath = Path()
    protected var mRenderGridLinesBuffer = FloatArray(2)
    override fun renderGridLines(c: Canvas) {
        if (!mXAxis!!.isDrawGridLinesEnabled || !mXAxis!!.isEnabled) return
        val clipRestoreCount = c.save()
        c.clipRect(getGridClippingRect()!!)
        if (mRenderGridLinesBuffer.size != mAxis.mEntryCount * 2) {
            mRenderGridLinesBuffer = FloatArray(mXAxis!!.mEntryCount * 2)
        }
        val positions = mRenderGridLinesBuffer
        run {
            var i = 0
            while (i < positions.size) {
                positions[i] = mXAxis!!.mEntries[i / 2]
                positions[i + 1] = mXAxis!!.mEntries[i / 2]
                i += 2
            }
        }
        mTrans.pointValuesToPixel(positions)
        setupGridPaint()
        val gridLinePath = mRenderGridLinesPath
        gridLinePath.reset()
        var i = 0
        while (i < positions.size) {
            drawGridLine(c, positions[i], positions[i + 1], gridLinePath)
            i += 2
        }
        c.restoreToCount(clipRestoreCount)
    }

    protected var mGridClippingRect = RectF()

    open fun getGridClippingRect(): RectF? {
        mGridClippingRect.set(mViewPortHandler.contentRect)
        mGridClippingRect.inset(-mAxis.gridLineWidth, 0f)
        return mGridClippingRect
    }

    /**
     * Draws the grid line at the specified position using the provided path.
     *
     * @param c
     * @param x
     * @param y
     * @param gridLinePath
     */
    protected open fun drawGridLine(c: Canvas, x: Float, y: Float, gridLinePath: Path) {
        gridLinePath.moveTo(x, mViewPortHandler.contentBottom())
        gridLinePath.lineTo(x, mViewPortHandler.contentTop())

        // draw a path because lines don't support dashing on lower android versions
        c.drawPath(gridLinePath, mGridPaint)
        gridLinePath.reset()
    }

    protected var mRenderLimitLinesBuffer = FloatArray(2)
    protected var mLimitLineClippingRect = RectF()

    /**
     * Draws the LimitLines associated with this axis to the screen.
     *
     * @param c
     */
    override fun renderLimitLines(c: Canvas) {
        val limitLines = mXAxis!!.limitLines
        if (limitLines == null || limitLines.size <= 0) return
        val position = mRenderLimitLinesBuffer
        position[0] = 0
        position[1] = 0
        for (i in limitLines.indices) {
            val l = limitLines[i]
            if (!l.isEnabled) continue
            val clipRestoreCount = c.save()
            mLimitLineClippingRect.set(mViewPortHandler.contentRect)
            mLimitLineClippingRect.inset(-l.lineWidth, 0f)
            c.clipRect(mLimitLineClippingRect)
            position[0] = l.limit
            position[1] = 0f
            mTrans.pointValuesToPixel(position)
            renderLimitLineLine(c, l, position)
            renderLimitLineLabel(c, l, position, 2f + l.yOffset)
            c.restoreToCount(clipRestoreCount)
        }
    }

    var mLimitLineSegmentsBuffer = FloatArray(4)
    private val mLimitLinePath = Path()

    open fun renderLimitLineLine(c: Canvas, limitLine: LimitLine, position: FloatArray) {
        mLimitLineSegmentsBuffer[0] = position[0]
        mLimitLineSegmentsBuffer[1] = mViewPortHandler.contentTop()
        mLimitLineSegmentsBuffer[2] = position[0]
        mLimitLineSegmentsBuffer[3] = mViewPortHandler.contentBottom()
        mLimitLinePath.reset()
        mLimitLinePath.moveTo(mLimitLineSegmentsBuffer[0], mLimitLineSegmentsBuffer[1])
        mLimitLinePath.lineTo(mLimitLineSegmentsBuffer[2], mLimitLineSegmentsBuffer[3])
        mLimitLinePaint!!.style = Paint.Style.STROKE
        mLimitLinePaint!!.color = limitLine.lineColor
        mLimitLinePaint!!.strokeWidth = limitLine.lineWidth
        mLimitLinePaint!!.pathEffect = limitLine.dashPathEffect
        c.drawPath(mLimitLinePath, mLimitLinePaint!!)
    }

    open fun renderLimitLineLabel(
        c: Canvas,
        limitLine: LimitLine,
        position: FloatArray,
        yOffset: Float
    ) {
        val label = limitLine.label

        // if drawing the limit-value label is enabled
        if (label != null && label != "") {
            mLimitLinePaint!!.style = limitLine.textStyle
            mLimitLinePaint!!.pathEffect = null
            mLimitLinePaint!!.color = limitLine.textColor
            mLimitLinePaint!!.strokeWidth = 0.5f
            mLimitLinePaint!!.textSize = limitLine.textSize
            val xOffset = limitLine.lineWidth + limitLine.xOffset
            val labelPosition = limitLine.labelPosition
            if (labelPosition === LimitLabelPosition.RIGHT_TOP) {
                val labelLineHeight = calcTextHeight(
                    mLimitLinePaint!!, label
                ).toFloat()
                mLimitLinePaint!!.textAlign = Align.LEFT
                c.drawText(
                    label,
                    position[0] + xOffset,
                    mViewPortHandler.contentTop() + yOffset + labelLineHeight,
                    mLimitLinePaint!!
                )
            } else if (labelPosition === LimitLabelPosition.RIGHT_BOTTOM) {
                mLimitLinePaint!!.textAlign = Align.LEFT
                c.drawText(
                    label, position[0] + xOffset, mViewPortHandler.contentBottom() - yOffset,
                    mLimitLinePaint!!
                )
            } else if (labelPosition === LimitLabelPosition.LEFT_TOP) {
                mLimitLinePaint!!.textAlign = Align.RIGHT
                val labelLineHeight = calcTextHeight(
                    mLimitLinePaint!!, label
                ).toFloat()
                c.drawText(
                    label,
                    position[0] - xOffset,
                    mViewPortHandler.contentTop() + yOffset + labelLineHeight,
                    mLimitLinePaint!!
                )
            } else {
                mLimitLinePaint!!.textAlign = Align.RIGHT
                c.drawText(
                    label, position[0] - xOffset, mViewPortHandler.contentBottom() - yOffset,
                    mLimitLinePaint!!
                )
            }
        }
    }
}