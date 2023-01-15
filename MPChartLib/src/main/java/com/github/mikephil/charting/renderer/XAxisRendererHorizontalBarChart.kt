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
import com.github.mikephil.charting.utils.MPPointF.Companion.getInstance
import com.github.mikephil.charting.utils.MPPointF.Companion.recycleInstance
import com.github.mikephil.charting.utils.Utils.calcTextHeight
import com.github.mikephil.charting.utils.Utils.calcTextSize
import com.github.mikephil.charting.utils.Utils.convertDpToPixel
import com.github.mikephil.charting.utils.Utils.getSizeOfRotatedRectangleByDegrees

class XAxisRendererHorizontalBarChart : XAxisRenderer {

    protected var mChart: BarChart? = null

    constructor(
        viewPortHandler: ViewPortHandler,
        xAxis: XAxis,
        trans: Transformer?,
        chart: BarChart
    ) : super(viewPortHandler, xAxis, trans) {
        mChart = chart
    }

    override fun computeAxis(min: Float, max: Float, inverted: Boolean) {
        // calculate the starting and entry point of the y-labels (depending on
        // zoom / contentrect bounds)
        var lMin = min
        var lMax = max
        if (mViewPortHandler!!.contentWidth() > 10 && !mViewPortHandler!!.isFullyZoomedOutY()) {
            val p1 = mTrans!!.getValuesByTouchPoint(
                mViewPortHandler!!.contentLeft(),
                mViewPortHandler!!.contentBottom()
            )!!
            val p2 = mTrans!!.getValuesByTouchPoint(
                mViewPortHandler!!.contentLeft(),
                mViewPortHandler!!.contentTop()
            )!!
            if (inverted) {
                lMin = p2.y.toFloat()
                lMax = p1.y.toFloat()
            } else {
                lMin = p1.y.toFloat()
                lMax = p2.y.toFloat()
            }
            recycleInstance(p1)
            recycleInstance(p2)
        }
        computeAxisValues(lMin, lMax)
    }

    override fun computeSize() {
        mAxisLabelPaint!!.typeface = mXAxis!!.getTypeface()
        mAxisLabelPaint!!.textSize = mXAxis!!.getTextSize()
        val longest = mXAxis!!.getLongestLabel()
        val labelSize = calcTextSize(
            mAxisLabelPaint!!,
            longest!!
        )!!
        val labelWidth = (labelSize.width + mXAxis!!.getXOffset() * 3.5f).toInt().toFloat()
        val labelHeight = labelSize.height
        val labelRotatedSize = getSizeOfRotatedRectangleByDegrees(
            labelSize.width,
            labelHeight,
            mXAxis!!.getLabelRotationAngle()
        )
        mXAxis!!.mLabelWidth = Math.round(labelWidth)
        mXAxis!!.mLabelHeight = Math.round(labelHeight)
        mXAxis!!.mLabelRotatedWidth =
            (labelRotatedSize.width + mXAxis!!.getXOffset() * 3.5f).toInt()
        mXAxis!!.mLabelRotatedHeight = Math.round(labelRotatedSize.height)
        recycleInstance(labelRotatedSize)
    }

    override fun renderAxisLabels(c: Canvas?) {
        if (!mXAxis!!.isEnabled() || !mXAxis!!.isDrawLabelsEnabled()) return
        val xoffset = mXAxis!!.getXOffset()
        mAxisLabelPaint!!.typeface = mXAxis!!.getTypeface()
        mAxisLabelPaint!!.textSize = mXAxis!!.getTextSize()
        mAxisLabelPaint!!.color = mXAxis!!.getTextColor()
        val pointF = getInstance(0f, 0f)
        if (mXAxis!!.getPosition() === XAxisPosition.TOP) {
            pointF.x = 0.0f
            pointF.y = 0.5f
            drawLabels(c, mViewPortHandler!!.contentRight() + xoffset, pointF)
        } else if (mXAxis!!.getPosition() === XAxisPosition.TOP_INSIDE) {
            pointF.x = 1.0f
            pointF.y = 0.5f
            drawLabels(c, mViewPortHandler!!.contentRight() - xoffset, pointF)
        } else if (mXAxis!!.getPosition() === XAxisPosition.BOTTOM) {
            pointF.x = 1.0f
            pointF.y = 0.5f
            drawLabels(c, mViewPortHandler!!.contentLeft() - xoffset, pointF)
        } else if (mXAxis!!.getPosition() === XAxisPosition.BOTTOM_INSIDE) {
            pointF.x = 1.0f
            pointF.y = 0.5f
            drawLabels(c, mViewPortHandler!!.contentLeft() + xoffset, pointF)
        } else { // BOTH SIDED
            pointF.x = 0.0f
            pointF.y = 0.5f
            drawLabels(c, mViewPortHandler!!.contentRight() + xoffset, pointF)
            pointF.x = 1.0f
            pointF.y = 0.5f
            drawLabels(c, mViewPortHandler!!.contentLeft() - xoffset, pointF)
        }
        recycleInstance(pointF)
    }

    override fun drawLabels(c: Canvas?, pos: Float, anchor: MPPointF?) {
        val labelRotationAngleDegrees = mXAxis!!.getLabelRotationAngle()
        val centeringEnabled = mXAxis!!.isCenterAxisLabelsEnabled()
        val positions = FloatArray(mXAxis!!.mEntryCount * 2)
        run {
            var i = 0
            while (i < positions.size) {


                // only fill x values
                if (centeringEnabled) {
                    positions[i + 1] = mXAxis!!.mCenteredEntries[i / 2]
                } else {
                    positions[i + 1] = mXAxis!!.mEntries[i / 2]
                }
                i += 2
            }
        }
        mTrans!!.pointValuesToPixel(positions)
        var i = 0
        while (i < positions.size) {
            val y = positions[i + 1]
            if (mViewPortHandler!!.isInBoundsY(y)) {
                val label =
                    mXAxis!!.getValueFormatter().getFormattedValue(mXAxis!!.mEntries[i / 2], mXAxis)
                drawLabel(c, label, pos, y, anchor, labelRotationAngleDegrees)
            }
            i += 2
        }
    }

    override fun getGridClippingRect(): RectF {
        mGridClippingRect.set(mViewPortHandler!!.getContentRect()!!)
        mGridClippingRect.inset(0f, -mAxis!!.getGridLineWidth())
        return mGridClippingRect
    }

    override fun drawGridLine(c: Canvas, x: Float, y: Float, gridLinePath: Path) {
        gridLinePath.moveTo(mViewPortHandler!!.contentRight(), y)
        gridLinePath.lineTo(mViewPortHandler!!.contentLeft(), y)

        // draw a path because lines don't support dashing on lower android versions
        c.drawPath(gridLinePath, mGridPaint!!)
        gridLinePath.reset()
    }

    override fun renderAxisLine(c: Canvas?) {
        if (!mXAxis!!.isDrawAxisLineEnabled() || !mXAxis!!.isEnabled()) return
        mAxisLinePaint!!.color = mXAxis!!.getAxisLineColor()
        mAxisLinePaint!!.strokeWidth = mXAxis!!.getAxisLineWidth()
        if (mXAxis!!.getPosition() === XAxisPosition.TOP || mXAxis!!.getPosition() === XAxisPosition.TOP_INSIDE || mXAxis!!.getPosition() === XAxisPosition.BOTH_SIDED) {
            c!!.drawLine(
                mViewPortHandler!!.contentRight(),
                mViewPortHandler!!.contentTop(), mViewPortHandler!!.contentRight(),
                mViewPortHandler!!.contentBottom(), mAxisLinePaint!!
            )
        }
        if (mXAxis!!.getPosition() === XAxisPosition.BOTTOM || mXAxis!!.getPosition() === XAxisPosition.BOTTOM_INSIDE || mXAxis!!.getPosition() === XAxisPosition.BOTH_SIDED) {
            c!!.drawLine(
                mViewPortHandler!!.contentLeft(),
                mViewPortHandler!!.contentTop(), mViewPortHandler!!.contentLeft(),
                mViewPortHandler!!.contentBottom(), mAxisLinePaint!!
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
    override fun renderLimitLines(c: Canvas?) {
        val limitLines = mXAxis!!.getLimitLines()
        if (limitLines == null || limitLines.size <= 0) return
        val pts = mRenderLimitLinesBuffer
        pts[0] = 0f
        pts[1] = 0f
        val limitLinePath = mRenderLimitLinesPathBuffer
        limitLinePath.reset()
        for (i in limitLines.indices) {
            val l = limitLines[i]
            if (!l!!.isEnabled()) continue
            val clipRestoreCount = c!!.save()
            mLimitLineClippingRect.set(mViewPortHandler!!.getContentRect()!!)
            mLimitLineClippingRect.inset(0f, -l.getLineWidth())
            c.clipRect(mLimitLineClippingRect)
            mLimitLinePaint!!.style = Paint.Style.STROKE
            mLimitLinePaint!!.color = l.getLineColor()
            mLimitLinePaint!!.strokeWidth = l.getLineWidth()
            mLimitLinePaint!!.pathEffect = l.getDashPathEffect()
            pts[1] = l.getLimit()
            mTrans!!.pointValuesToPixel(pts)
            limitLinePath.moveTo(mViewPortHandler!!.contentLeft(), pts[1])
            limitLinePath.lineTo(mViewPortHandler!!.contentRight(), pts[1])
            c.drawPath(limitLinePath, mLimitLinePaint!!)
            limitLinePath.reset()
            // c.drawLines(pts, mLimitLinePaint);
            val label = l.getLabel()

            // if drawing the limit-value label is enabled
            if (label != null && label != "") {
                mLimitLinePaint!!.style = l.getTextStyle()
                mLimitLinePaint!!.pathEffect = null
                mLimitLinePaint!!.color = l.getTextColor()
                mLimitLinePaint!!.strokeWidth = 0.5f
                mLimitLinePaint!!.textSize = l.getTextSize()
                val labelLineHeight = calcTextHeight(
                    mLimitLinePaint!!, label
                ).toFloat()
                val xOffset = convertDpToPixel(4f) + l.getXOffset()
                val yOffset = l.getLineWidth() + labelLineHeight + l.getYOffset()
                val position = l.getLabelPosition()
                if (position === LimitLabelPosition.RIGHT_TOP) {
                    mLimitLinePaint!!.textAlign = Align.RIGHT
                    c.drawText(
                        label,
                        mViewPortHandler!!.contentRight() - xOffset,
                        pts[1] - yOffset + labelLineHeight, mLimitLinePaint!!
                    )
                } else if (position === LimitLabelPosition.RIGHT_BOTTOM) {
                    mLimitLinePaint!!.textAlign = Align.RIGHT
                    c.drawText(
                        label,
                        mViewPortHandler!!.contentRight() - xOffset,
                        pts[1] + yOffset, mLimitLinePaint!!
                    )
                } else if (position === LimitLabelPosition.LEFT_TOP) {
                    mLimitLinePaint!!.textAlign = Align.LEFT
                    c.drawText(
                        label,
                        mViewPortHandler!!.contentLeft() + xOffset,
                        pts[1] - yOffset + labelLineHeight, mLimitLinePaint!!
                    )
                } else {
                    mLimitLinePaint!!.textAlign = Align.LEFT
                    c.drawText(
                        label,
                        mViewPortHandler!!.offsetLeft() + xOffset,
                        pts[1] + yOffset, mLimitLinePaint!!
                    )
                }
            }
            c.restoreToCount(clipRestoreCount)
        }
    }
}