package com.github.mikephil.charting.renderer

import android.graphics.*
import android.graphics.Paint.Align
import com.github.mikephil.charting.charts.BarChart
import com.github.mikephil.charting.components.LimitLine.LimitLabelPosition
import com.github.mikephil.charting.components.XAxis
import com.github.mikephil.charting.components.XAxis.XAxisPosition
import com.github.mikephil.charting.utils.*
import com.github.mikephil.charting.utils.FSize.Companion.recycleInstance
import com.github.mikephil.charting.utils.MPPointD.Companion.recycleInstance
import com.github.mikephil.charting.utils.MPPointF.Companion.recycleInstance
import com.github.mikephil.charting.utils.Transformer.getValuesByTouchPoint
import com.github.mikephil.charting.utils.Transformer.pointValuesToPixel
import com.github.mikephil.charting.utils.Utils.calcTextHeight
import com.github.mikephil.charting.utils.Utils.calcTextSize
import com.github.mikephil.charting.utils.Utils.getSizeOfRotatedRectangleByDegrees
import com.github.mikephil.charting.utils.ViewPortHandler.contentBottom
import com.github.mikephil.charting.utils.ViewPortHandler.contentLeft
import com.github.mikephil.charting.utils.ViewPortHandler.contentRect
import com.github.mikephil.charting.utils.ViewPortHandler.contentRight
import com.github.mikephil.charting.utils.ViewPortHandler.contentTop
import com.github.mikephil.charting.utils.ViewPortHandler.contentWidth
import com.github.mikephil.charting.utils.ViewPortHandler.isFullyZoomedOutY
import com.github.mikephil.charting.utils.ViewPortHandler.isInBoundsY
import com.github.mikephil.charting.utils.ViewPortHandler.offsetLeft

class XAxisRendererHorizontalBarChart(
    viewPortHandler: ViewPortHandler?, xAxis: XAxis,
    trans: Transformer?, protected var mChart: BarChart
) : XAxisRenderer(viewPortHandler, xAxis, trans) {
    override fun computeAxis(min: Float, max: Float, inverted: Boolean) {

        // calculate the starting and entry point of the y-labels (depending on
        // zoom / contentrect bounds)
        var min = min
        var max = max
        if (mViewPortHandler.contentWidth() > 10 && !mViewPortHandler.isFullyZoomedOutY) {
            val p1 = mTrans.getValuesByTouchPoint(
                mViewPortHandler.contentLeft(),
                mViewPortHandler.contentBottom()
            )
            val p2 = mTrans.getValuesByTouchPoint(
                mViewPortHandler.contentLeft(),
                mViewPortHandler.contentTop()
            )
            if (inverted) {
                min = p2.y.toFloat()
                max = p1.y.toFloat()
            } else {
                min = p1.y.toFloat()
                max = p2.y.toFloat()
            }
            recycleInstance(p1)
            recycleInstance(p2)
        }
        computeAxisValues(min, max)
    }

    override fun computeSize() {
        mAxisLabelPaint.typeface = mXAxis.typeface
        mAxisLabelPaint.textSize = mXAxis.textSize
        val longest = mXAxis.longestLabel
        val labelSize = calcTextSize(mAxisLabelPaint, longest)
        val labelWidth = (labelSize.width + mXAxis.xOffset * 3.5f).toInt().toFloat()
        val labelHeight = labelSize.height
        val labelRotatedSize = getSizeOfRotatedRectangleByDegrees(
            labelSize.width,
            labelHeight,
            mXAxis.labelRotationAngle
        )
        mXAxis.mLabelWidth = Math.round(labelWidth)
        mXAxis.mLabelHeight = Math.round(labelHeight)
        mXAxis.mLabelRotatedWidth = (labelRotatedSize.width + mXAxis.xOffset * 3.5f).toInt()
        mXAxis.mLabelRotatedHeight = Math.round(labelRotatedSize.height)
        recycleInstance(labelRotatedSize)
    }

    override fun renderAxisLabels(c: Canvas) {
        if (!mXAxis.isEnabled || !mXAxis.isDrawLabelsEnabled) return
        val xoffset = mXAxis.xOffset
        mAxisLabelPaint.typeface = mXAxis.typeface
        mAxisLabelPaint.textSize = mXAxis.textSize
        mAxisLabelPaint.color = mXAxis.textColor
        val pointF = MPPointF.getInstance(0, 0)
        if (mXAxis.position == XAxisPosition.TOP) {
            pointF.x = 0.0f
            pointF.y = 0.5f
            drawLabels(c, mViewPortHandler.contentRight() + xoffset, pointF)
        } else if (mXAxis.position == XAxisPosition.TOP_INSIDE) {
            pointF.x = 1.0f
            pointF.y = 0.5f
            drawLabels(c, mViewPortHandler.contentRight() - xoffset, pointF)
        } else if (mXAxis.position == XAxisPosition.BOTTOM) {
            pointF.x = 1.0f
            pointF.y = 0.5f
            drawLabels(c, mViewPortHandler.contentLeft() - xoffset, pointF)
        } else if (mXAxis.position == XAxisPosition.BOTTOM_INSIDE) {
            pointF.x = 1.0f
            pointF.y = 0.5f
            drawLabels(c, mViewPortHandler.contentLeft() + xoffset, pointF)
        } else { // BOTH SIDED
            pointF.x = 0.0f
            pointF.y = 0.5f
            drawLabels(c, mViewPortHandler.contentRight() + xoffset, pointF)
            pointF.x = 1.0f
            pointF.y = 0.5f
            drawLabels(c, mViewPortHandler.contentLeft() - xoffset, pointF)
        }
        recycleInstance(pointF)
    }

    override fun drawLabels(c: Canvas?, pos: Float, anchor: MPPointF?) {
        val labelRotationAngleDegrees = mXAxis.labelRotationAngle
        val centeringEnabled = mXAxis.isCenterAxisLabelsEnabled
        val positions = FloatArray(mXAxis.mEntryCount * 2)
        run {
            var i = 0
            while (i < positions.size) {


                // only fill x values
                if (centeringEnabled) {
                    positions[i + 1] = mXAxis.mCenteredEntries[i / 2]
                } else {
                    positions[i + 1] = mXAxis.mEntries[i / 2]
                }
                i += 2
            }
        }
        mTrans.pointValuesToPixel(positions)
        var i = 0
        while (i < positions.size) {
            val y = positions[i + 1]
            if (mViewPortHandler.isInBoundsY(y)) {
                val label = mXAxis.valueFormatter.getFormattedValue(mXAxis.mEntries[i / 2], mXAxis)
                drawLabel(c, label, pos, y, anchor, labelRotationAngleDegrees)
            }
            i += 2
        }
    }

    override val gridClippingRect: RectF
        get() {
            mGridClippingRect.set(mViewPortHandler.contentRect)
            mGridClippingRect.inset(0f, -mAxis.gridLineWidth)
            return mGridClippingRect
        }

    override fun drawGridLine(c: Canvas, x: Float, y: Float, gridLinePath: Path) {
        gridLinePath.moveTo(mViewPortHandler.contentRight(), y)
        gridLinePath.lineTo(mViewPortHandler.contentLeft(), y)

        // draw a path because lines don't support dashing on lower android versions
        c.drawPath(gridLinePath, mGridPaint)
        gridLinePath.reset()
    }

    override fun renderAxisLine(c: Canvas) {
        if (!mXAxis.isDrawAxisLineEnabled || !mXAxis.isEnabled) return
        mAxisLinePaint.color = mXAxis.axisLineColor
        mAxisLinePaint.strokeWidth = mXAxis.axisLineWidth
        if (mXAxis.position == XAxisPosition.TOP || mXAxis.position == XAxisPosition.TOP_INSIDE || mXAxis.position == XAxisPosition.BOTH_SIDED) {
            c.drawLine(
                mViewPortHandler.contentRight(),
                mViewPortHandler.contentTop(), mViewPortHandler.contentRight(),
                mViewPortHandler.contentBottom(), mAxisLinePaint
            )
        }
        if (mXAxis.position == XAxisPosition.BOTTOM || mXAxis.position == XAxisPosition.BOTTOM_INSIDE || mXAxis.position == XAxisPosition.BOTH_SIDED) {
            c.drawLine(
                mViewPortHandler.contentLeft(),
                mViewPortHandler.contentTop(), mViewPortHandler.contentLeft(),
                mViewPortHandler.contentBottom(), mAxisLinePaint
            )
        }
    }

    protected var mRenderLimitLinesPathBuffer = Path()

    /**
     * Draws the LimitLines associated with this axis to the screen.
     * This is the standard YAxis renderer using the XAxis limit lines.
     *
     * @param c
     */
    override fun renderLimitLines(c: Canvas) {
        val limitLines = mXAxis.limitLines
        if (limitLines == null || limitLines.size <= 0) return
        val pts = mRenderLimitLinesBuffer
        pts[0] = 0
        pts[1] = 0
        val limitLinePath = mRenderLimitLinesPathBuffer
        limitLinePath.reset()
        for (i in limitLines.indices) {
            val l = limitLines[i]
            if (!l.isEnabled) continue
            val clipRestoreCount = c.save()
            mLimitLineClippingRect.set(mViewPortHandler.contentRect)
            mLimitLineClippingRect.inset(0f, -l.lineWidth)
            c.clipRect(mLimitLineClippingRect)
            mLimitLinePaint!!.style = Paint.Style.STROKE
            mLimitLinePaint!!.color = l.lineColor
            mLimitLinePaint!!.strokeWidth = l.lineWidth
            mLimitLinePaint!!.pathEffect = l.dashPathEffect
            pts[1] = l.limit
            mTrans.pointValuesToPixel(pts)
            limitLinePath.moveTo(mViewPortHandler.contentLeft(), pts[1])
            limitLinePath.lineTo(mViewPortHandler.contentRight(), pts[1])
            c.drawPath(limitLinePath, mLimitLinePaint!!)
            limitLinePath.reset()
            // c.drawLines(pts, mLimitLinePaint);
            val label = l.label

            // if drawing the limit-value label is enabled
            if (label != null && label != "") {
                mLimitLinePaint!!.style = l.textStyle
                mLimitLinePaint!!.pathEffect = null
                mLimitLinePaint!!.color = l.textColor
                mLimitLinePaint!!.strokeWidth = 0.5f
                mLimitLinePaint!!.textSize = l.textSize
                val labelLineHeight = calcTextHeight(
                    mLimitLinePaint!!, label
                ).toFloat()
                val xOffset = Utils.convertDpToPixel(4f) + l.xOffset
                val yOffset = l.lineWidth + labelLineHeight + l.yOffset
                val position = l.labelPosition
                if (position == LimitLabelPosition.RIGHT_TOP) {
                    mLimitLinePaint!!.textAlign = Align.RIGHT
                    c.drawText(
                        label,
                        mViewPortHandler.contentRight() - xOffset,
                        pts[1] - yOffset + labelLineHeight, mLimitLinePaint!!
                    )
                } else if (position == LimitLabelPosition.RIGHT_BOTTOM) {
                    mLimitLinePaint!!.textAlign = Align.RIGHT
                    c.drawText(
                        label,
                        mViewPortHandler.contentRight() - xOffset,
                        pts[1] + yOffset, mLimitLinePaint!!
                    )
                } else if (position == LimitLabelPosition.LEFT_TOP) {
                    mLimitLinePaint!!.textAlign = Align.LEFT
                    c.drawText(
                        label,
                        mViewPortHandler.contentLeft() + xOffset,
                        pts[1] - yOffset + labelLineHeight, mLimitLinePaint!!
                    )
                } else {
                    mLimitLinePaint!!.textAlign = Align.LEFT
                    c.drawText(
                        label,
                        mViewPortHandler.offsetLeft() + xOffset,
                        pts[1] + yOffset, mLimitLinePaint!!
                    )
                }
            }
            c.restoreToCount(clipRestoreCount)
        }
    }
}