package com.github.mikephil.charting.renderer

import android.graphics.*
import android.graphics.Paint.Align
import com.github.mikephil.charting.components.LimitLine.LimitLabelPosition
import com.github.mikephil.charting.components.YAxis
import com.github.mikephil.charting.components.YAxis.AxisDependency
import com.github.mikephil.charting.components.YAxis.YAxisLabelPosition
import com.github.mikephil.charting.utils.*
import com.github.mikephil.charting.utils.FSize.Companion.recycleInstance
import com.github.mikephil.charting.utils.MPPointD.Companion.recycleInstance
import com.github.mikephil.charting.utils.MPPointF.Companion.recycleInstance
import com.github.mikephil.charting.utils.Transformer.getPixelForValues
import com.github.mikephil.charting.utils.Transformer.getValuesByTouchPoint
import com.github.mikephil.charting.utils.Transformer.pointValuesToPixel
import com.github.mikephil.charting.utils.Utils.calcTextHeight
import com.github.mikephil.charting.utils.ViewPortHandler.contentBottom
import com.github.mikephil.charting.utils.ViewPortHandler.contentHeight
import com.github.mikephil.charting.utils.ViewPortHandler.contentLeft
import com.github.mikephil.charting.utils.ViewPortHandler.contentRect
import com.github.mikephil.charting.utils.ViewPortHandler.contentRight
import com.github.mikephil.charting.utils.ViewPortHandler.contentTop
import com.github.mikephil.charting.utils.ViewPortHandler.isFullyZoomedOutX

class YAxisRendererHorizontalBarChart(
    viewPortHandler: ViewPortHandler?, yAxis: YAxis,
    trans: Transformer?
) : YAxisRenderer(viewPortHandler, yAxis, trans) {
    /**
     * Computes the axis values.
     *
     * @param yMin - the minimum y-value in the data object for this axis
     * @param yMax - the maximum y-value in the data object for this axis
     */
    override fun computeAxis(yMin: Float, yMax: Float, inverted: Boolean) {

        // calculate the starting and entry point of the y-labels (depending on
        // zoom / contentrect bounds)
        var yMin = yMin
        var yMax = yMax
        if (mViewPortHandler.contentHeight() > 10 && !mViewPortHandler.isFullyZoomedOutX) {
            val p1 = mTrans.getValuesByTouchPoint(
                mViewPortHandler.contentLeft(),
                mViewPortHandler.contentTop()
            )
            val p2 = mTrans.getValuesByTouchPoint(
                mViewPortHandler.contentRight(),
                mViewPortHandler.contentTop()
            )
            if (!inverted) {
                yMin = p1.x.toFloat()
                yMax = p2.x.toFloat()
            } else {
                yMin = p2.x.toFloat()
                yMax = p1.x.toFloat()
            }
            recycleInstance(p1)
            recycleInstance(p2)
        }
        computeAxisValues(yMin, yMax)
    }

    /**
     * draws the y-axis labels to the screen
     */
    override fun renderAxisLabels(c: Canvas) {
        if (!mYAxis.isEnabled || !mYAxis.isDrawLabelsEnabled) return
        val positions = transformedPositions
        mAxisLabelPaint.typeface = mYAxis.typeface
        mAxisLabelPaint.textSize = mYAxis.textSize
        mAxisLabelPaint.color = mYAxis.textColor
        mAxisLabelPaint.textAlign = Align.CENTER
        val baseYOffset = Utils.convertDpToPixel(2.5f)
        val textHeight = calcTextHeight(mAxisLabelPaint, "Q").toFloat()
        val dependency = mYAxis.axisDependency
        val labelPosition = mYAxis.labelPosition
        var yPos = 0f
        yPos = if (dependency == AxisDependency.LEFT) {
            if (labelPosition == YAxisLabelPosition.OUTSIDE_CHART) {
                mViewPortHandler.contentTop() - baseYOffset
            } else {
                mViewPortHandler.contentTop() - baseYOffset
            }
        } else {
            if (labelPosition == YAxisLabelPosition.OUTSIDE_CHART) {
                mViewPortHandler.contentBottom() + textHeight + baseYOffset
            } else {
                mViewPortHandler.contentBottom() + textHeight + baseYOffset
            }
        }
        drawYLabels(c, yPos, positions, mYAxis.yOffset)
    }

    override fun renderAxisLine(c: Canvas) {
        if (!mYAxis.isEnabled || !mYAxis.isDrawAxisLineEnabled) return
        mAxisLinePaint.color = mYAxis.axisLineColor
        mAxisLinePaint.strokeWidth = mYAxis.axisLineWidth
        if (mYAxis.axisDependency == AxisDependency.LEFT) {
            c.drawLine(
                mViewPortHandler.contentLeft(),
                mViewPortHandler.contentTop(), mViewPortHandler.contentRight(),
                mViewPortHandler.contentTop(), mAxisLinePaint
            )
        } else {
            c.drawLine(
                mViewPortHandler.contentLeft(),
                mViewPortHandler.contentBottom(), mViewPortHandler.contentRight(),
                mViewPortHandler.contentBottom(), mAxisLinePaint
            )
        }
    }

    /**
     * draws the y-labels on the specified x-position
     *
     * @param fixedPosition
     * @param positions
     */
    override fun drawYLabels(
        c: Canvas,
        fixedPosition: Float,
        positions: FloatArray,
        offset: Float
    ) {
        mAxisLabelPaint.typeface = mYAxis.typeface
        mAxisLabelPaint.textSize = mYAxis.textSize
        mAxisLabelPaint.color = mYAxis.textColor
        val from = if (mYAxis.isDrawBottomYLabelEntryEnabled) 0 else 1
        val to =
            if (mYAxis.isDrawTopYLabelEntryEnabled) mYAxis.mEntryCount else mYAxis.mEntryCount - 1
        val xOffset = mYAxis.labelXOffset
        for (i in from until to) {
            val text = mYAxis.getFormattedLabel(i)
            c.drawText(
                text,
                positions[i * 2],
                fixedPosition - offset + xOffset,
                mAxisLabelPaint
            )
        }
    }

    // only fill x values, y values are not needed for x-labels
    protected override val transformedPositions: FloatArray
        protected get() {
            if (mGetTransformedPositionsBuffer.size != mYAxis.mEntryCount * 2) {
                mGetTransformedPositionsBuffer = FloatArray(mYAxis.mEntryCount * 2)
            }
            val positions = mGetTransformedPositionsBuffer
            var i = 0
            while (i < positions.size) {

                // only fill x values, y values are not needed for x-labels
                positions[i] = mYAxis.mEntries[i / 2]
                i += 2
            }
            mTrans.pointValuesToPixel(positions)
            return positions
        }
    override val gridClippingRect: RectF
        get() {
            mGridClippingRect.set(mViewPortHandler.contentRect)
            mGridClippingRect.inset(-mAxis.gridLineWidth, 0f)
            return mGridClippingRect
        }

    override fun linePath(p: Path, i: Int, positions: FloatArray): Path {
        p.moveTo(positions[i], mViewPortHandler.contentTop())
        p.lineTo(positions[i], mViewPortHandler.contentBottom())
        return p
    }

    protected var mDrawZeroLinePathBuffer = Path()
    override fun drawZeroLine(c: Canvas) {
        val clipRestoreCount = c.save()
        mZeroLineClippingRect.set(mViewPortHandler.contentRect)
        mZeroLineClippingRect.inset(-mYAxis.zeroLineWidth, 0f)
        c.clipRect(mLimitLineClippingRect)

        // draw zero line
        val pos = mTrans.getPixelForValues(0f, 0f)
        mZeroLinePaint!!.color = mYAxis.zeroLineColor
        mZeroLinePaint!!.strokeWidth = mYAxis.zeroLineWidth
        val zeroLinePath = mDrawZeroLinePathBuffer
        zeroLinePath.reset()
        zeroLinePath.moveTo(pos.x.toFloat() - 1, mViewPortHandler.contentTop())
        zeroLinePath.lineTo(pos.x.toFloat() - 1, mViewPortHandler.contentBottom())

        // draw a path because lines don't support dashing on lower android versions
        c.drawPath(zeroLinePath, mZeroLinePaint!!)
        c.restoreToCount(clipRestoreCount)
    }

    protected var mRenderLimitLinesPathBuffer = Path()
    protected override var mRenderLimitLinesBuffer = FloatArray(4)

    /**
     * Draws the LimitLines associated with this axis to the screen.
     * This is the standard XAxis renderer using the YAxis limit lines.
     *
     * @param c
     */
    override fun renderLimitLines(c: Canvas) {
        val limitLines = mYAxis.limitLines
        if (limitLines == null || limitLines.size <= 0) return
        val pts = mRenderLimitLinesBuffer
        pts[0] = 0
        pts[1] = 0
        pts[2] = 0
        pts[3] = 0
        val limitLinePath = mRenderLimitLinesPathBuffer
        limitLinePath.reset()
        for (i in limitLines.indices) {
            val l = limitLines[i]
            if (!l.isEnabled) continue
            val clipRestoreCount = c.save()
            mLimitLineClippingRect.set(mViewPortHandler.contentRect)
            mLimitLineClippingRect.inset(-l.lineWidth, 0f)
            c.clipRect(mLimitLineClippingRect)
            pts[0] = l.limit
            pts[2] = l.limit
            mTrans.pointValuesToPixel(pts)
            pts[1] = mViewPortHandler.contentTop()
            pts[3] = mViewPortHandler.contentBottom()
            limitLinePath.moveTo(pts[0], pts[1])
            limitLinePath.lineTo(pts[2], pts[3])
            mLimitLinePaint!!.style = Paint.Style.STROKE
            mLimitLinePaint!!.color = l.lineColor
            mLimitLinePaint!!.pathEffect = l.dashPathEffect
            mLimitLinePaint!!.strokeWidth = l.lineWidth
            c.drawPath(limitLinePath, mLimitLinePaint!!)
            limitLinePath.reset()
            val label = l.label

            // if drawing the limit-value label is enabled
            if (label != null && label != "") {
                mLimitLinePaint!!.style = l.textStyle
                mLimitLinePaint!!.pathEffect = null
                mLimitLinePaint!!.color = l.textColor
                mLimitLinePaint!!.typeface = l.typeface
                mLimitLinePaint!!.strokeWidth = 0.5f
                mLimitLinePaint!!.textSize = l.textSize
                val xOffset = l.lineWidth + l.xOffset
                val yOffset = Utils.convertDpToPixel(2f) + l.yOffset
                val position = l.labelPosition
                if (position == LimitLabelPosition.RIGHT_TOP) {
                    val labelLineHeight = calcTextHeight(
                        mLimitLinePaint!!, label
                    ).toFloat()
                    mLimitLinePaint!!.textAlign = Align.LEFT
                    c.drawText(
                        label,
                        pts[0] + xOffset,
                        mViewPortHandler.contentTop() + yOffset + labelLineHeight,
                        mLimitLinePaint!!
                    )
                } else if (position == LimitLabelPosition.RIGHT_BOTTOM) {
                    mLimitLinePaint!!.textAlign = Align.LEFT
                    c.drawText(
                        label,
                        pts[0] + xOffset,
                        mViewPortHandler.contentBottom() - yOffset,
                        mLimitLinePaint!!
                    )
                } else if (position == LimitLabelPosition.LEFT_TOP) {
                    mLimitLinePaint!!.textAlign = Align.RIGHT
                    val labelLineHeight = calcTextHeight(
                        mLimitLinePaint!!, label
                    ).toFloat()
                    c.drawText(
                        label,
                        pts[0] - xOffset,
                        mViewPortHandler.contentTop() + yOffset + labelLineHeight,
                        mLimitLinePaint!!
                    )
                } else {
                    mLimitLinePaint!!.textAlign = Align.RIGHT
                    c.drawText(
                        label,
                        pts[0] - xOffset,
                        mViewPortHandler.contentBottom() - yOffset,
                        mLimitLinePaint!!
                    )
                }
            }
            c.restoreToCount(clipRestoreCount)
        }
    }

    init {
        mLimitLinePaint!!.textAlign = Align.LEFT
    }
}