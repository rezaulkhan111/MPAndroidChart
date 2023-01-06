package com.github.mikephil.charting.renderer

import android.graphics.*
import android.graphics.Paint.Align
import com.github.mikephil.charting.components.LimitLine.LimitLabelPosition
import com.github.mikephil.charting.components.YAxis
import com.github.mikephil.charting.components.YAxis.AxisDependency
import com.github.mikephil.charting.components.YAxis.YAxisLabelPosition
import com.github.mikephil.charting.utils.*
import com.github.mikephil.charting.utils.MPPointD.Companion.recycleInstance
import com.github.mikephil.charting.utils.Utils.calcTextHeight
import com.github.mikephil.charting.utils.Utils.convertDpToPixel

class YAxisRendererHorizontalBarChart : YAxisRenderer {

    constructor(
        viewPortHandler: ViewPortHandler,
        yAxis: YAxis,
        trans: Transformer?
    ) : super(viewPortHandler, yAxis, trans) {
        mLimitLinePaint!!.textAlign = Align.LEFT
    }

    /**
     * Computes the axis values.
     *
     * @param yMin - the minimum y-value in the data object for this axis
     * @param yMax - the maximum y-value in the data object for this axis
     */
    override fun computeAxis(mMin: Float, mMax: Float, inverted: Boolean) {
        // calculate the starting and entry point of the y-labels (depending on
        // zoom / contentrect bounds)
        var yMin = mMin
        var yMax = mMax
        if (mViewPortHandler!!.contentHeight() > 10 && !mViewPortHandler!!.isFullyZoomedOutX()) {
            val p1 = mTrans!!.getValuesByTouchPoint(
                mViewPortHandler!!.contentLeft(),
                mViewPortHandler!!.contentTop()
            )!!
            val p2 = mTrans!!.getValuesByTouchPoint(
                mViewPortHandler!!.contentRight(),
                mViewPortHandler!!.contentTop()
            )!!
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
    override fun renderAxisLabels(c: Canvas?) {
        if (!mYAxis!!.isEnabled() || !mYAxis!!.isDrawLabelsEnabled()) return
        val positions = getTransformedPositions()
        mAxisLabelPaint!!.typeface = mYAxis!!.getTypeface()
        mAxisLabelPaint!!.textSize = mYAxis!!.getTextSize()
        mAxisLabelPaint!!.color = mYAxis!!.getTextColor()
        mAxisLabelPaint!!.textAlign = Align.CENTER
        val baseYOffset = convertDpToPixel(2.5f)
        val textHeight = calcTextHeight(mAxisLabelPaint!!, "Q").toFloat()
        val dependency = mYAxis!!.getAxisDependency()
        val labelPosition = mYAxis!!.getLabelPosition()
        var yPos = 0f
        yPos = if (dependency === AxisDependency.LEFT) {
            if (labelPosition === YAxisLabelPosition.OUTSIDE_CHART) {
                mViewPortHandler!!.contentTop() - baseYOffset
            } else {
                mViewPortHandler!!.contentTop() - baseYOffset
            }
        } else {
            if (labelPosition === YAxisLabelPosition.OUTSIDE_CHART) {
                mViewPortHandler!!.contentBottom() + textHeight + baseYOffset
            } else {
                mViewPortHandler!!.contentBottom() + textHeight + baseYOffset
            }
        }
        drawYLabels(c!!, yPos, positions, mYAxis!!.getYOffset())
    }

    override fun renderAxisLine(c: Canvas?) {
        if (!mYAxis!!.isEnabled() || !mYAxis!!.isDrawAxisLineEnabled()) return
        mAxisLinePaint!!.color = mYAxis!!.getAxisLineColor()
        mAxisLinePaint!!.strokeWidth = mYAxis!!.getAxisLineWidth()
        if (mYAxis!!.getAxisDependency() === AxisDependency.LEFT) {
            c!!.drawLine(
                mViewPortHandler!!.contentLeft(),
                mViewPortHandler!!.contentTop(), mViewPortHandler!!.contentRight(),
                mViewPortHandler!!.contentTop(), mAxisLinePaint!!
            )
        } else {
            c!!.drawLine(
                mViewPortHandler!!.contentLeft(),
                mViewPortHandler!!.contentBottom(), mViewPortHandler!!.contentRight(),
                mViewPortHandler!!.contentBottom(), mAxisLinePaint!!
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
        mAxisLabelPaint!!.typeface = mYAxis!!.getTypeface()
        mAxisLabelPaint!!.textSize = mYAxis!!.getTextSize()
        mAxisLabelPaint!!.color = mYAxis!!.getTextColor()
        val from = if (mYAxis!!.isDrawBottomYLabelEntryEnabled()) 0 else 1
        val to =
            if (mYAxis!!.isDrawTopYLabelEntryEnabled()) mYAxis!!.mEntryCount else mYAxis!!.mEntryCount - 1
        val xOffset = mYAxis!!.getLabelXOffset()
        for (i in from until to) {
            val text = mYAxis!!.getFormattedLabel(i)
            c.drawText(
                text!!,
                positions[i * 2],
                fixedPosition - offset + xOffset,
                mAxisLabelPaint!!
            )
        }
    }

    override fun getTransformedPositions(): FloatArray {
        if (mGetTransformedPositionsBuffer.size != mYAxis!!.mEntryCount * 2) {
            mGetTransformedPositionsBuffer = FloatArray(mYAxis!!.mEntryCount * 2)
        }
        val positions = mGetTransformedPositionsBuffer
        var i = 0
        while (i < positions.size) {

            // only fill x values, y values are not needed for x-labels
            positions[i] = mYAxis!!.mEntries[i / 2]
            i += 2
        }
        mTrans!!.pointValuesToPixel(positions)
        return positions
    }

    override fun getGridClippingRect(): RectF {
        mGridClippingRect.set(mViewPortHandler!!.getContentRect()!!)
        mGridClippingRect.inset(-mAxis!!.getGridLineWidth(), 0f)
        return mGridClippingRect
    }

    override fun linePath(p: Path, i: Int, positions: FloatArray): Path {
        p.moveTo(positions[i], mViewPortHandler!!.contentTop())
        p.lineTo(positions[i], mViewPortHandler!!.contentBottom())
        return p
    }

    protected var mDrawZeroLinePathBuffer = Path()

    override fun drawZeroLine(c: Canvas) {
        val clipRestoreCount = c.save()
        mZeroLineClippingRect.set(mViewPortHandler!!.getContentRect()!!)
        mZeroLineClippingRect.inset(-mYAxis!!.getZeroLineWidth(), 0f)
        c.clipRect(mLimitLineClippingRect)

        // draw zero line
        val pos = mTrans!!.getPixelForValues(0f, 0f)!!
        mZeroLinePaint!!.color = mYAxis!!.getZeroLineColor()
        mZeroLinePaint!!.strokeWidth = mYAxis!!.getZeroLineWidth()
        val zeroLinePath = mDrawZeroLinePathBuffer
        zeroLinePath.reset()
        zeroLinePath.moveTo(pos.x.toFloat() - 1, mViewPortHandler!!.contentTop())
        zeroLinePath.lineTo(pos.x.toFloat() - 1, mViewPortHandler!!.contentBottom())

        // draw a path because lines don't support dashing on lower android versions
        c.drawPath(zeroLinePath, mZeroLinePaint!!)
        c.restoreToCount(clipRestoreCount)
    }

    protected var mRenderLimitLinesPathBuffer = Path()
    private var mRenderLimitLinesBuffer = FloatArray(4)

    /**
     * Draws the LimitLines associated with this axis to the screen.
     * This is the standard XAxis renderer using the YAxis limit lines.
     *
     * @param c
     */
    override fun renderLimitLines(c: Canvas?) {
        val limitLines = mYAxis!!.getLimitLines()
        if (limitLines == null || limitLines.size <= 0) return
        val pts = mRenderLimitLinesBuffer
        pts[0] = 0f
        pts[1] = 0f
        pts[2] = 0f
        pts[3] = 0f
        val limitLinePath = mRenderLimitLinesPathBuffer
        limitLinePath.reset()
        for (i in limitLines.indices) {
            val l = limitLines[i]
            if (!l!!.isEnabled()) continue
            val clipRestoreCount = c!!.save()
            mLimitLineClippingRect.set(mViewPortHandler!!.getContentRect()!!)
            mLimitLineClippingRect.inset(-l.getLineWidth(), 0f)
            c.clipRect(mLimitLineClippingRect)
            pts[0] = l.getLimit()
            pts[2] = l.getLimit()
            mTrans!!.pointValuesToPixel(pts)
            pts[1] = mViewPortHandler!!.contentTop()
            pts[3] = mViewPortHandler!!.contentBottom()
            limitLinePath.moveTo(pts[0], pts[1])
            limitLinePath.lineTo(pts[2], pts[3])
            mLimitLinePaint!!.style = Paint.Style.STROKE
            mLimitLinePaint!!.color = l.getLineColor()
            mLimitLinePaint!!.pathEffect = l.getDashPathEffect()
            mLimitLinePaint!!.strokeWidth = l.getLineWidth()
            c.drawPath(limitLinePath, mLimitLinePaint!!)
            limitLinePath.reset()
            val label = l.getLabel()

            // if drawing the limit-value label is enabled
            if (label != null && label != "") {
                mLimitLinePaint!!.style = l.getTextStyle()
                mLimitLinePaint!!.pathEffect = null
                mLimitLinePaint!!.color = l.getTextColor()
                mLimitLinePaint!!.typeface = l.getTypeface()
                mLimitLinePaint!!.strokeWidth = 0.5f
                mLimitLinePaint!!.textSize = l.getTextSize()
                val xOffset = l.getLineWidth() + l.getXOffset()
                val yOffset = convertDpToPixel(2f) + l.getYOffset()
                val position = l.getLabelPosition()
                if (position === LimitLabelPosition.RIGHT_TOP) {
                    val labelLineHeight = calcTextHeight(
                        mLimitLinePaint!!, label
                    ).toFloat()
                    mLimitLinePaint!!.textAlign = Align.LEFT
                    c.drawText(
                        label,
                        pts[0] + xOffset,
                        mViewPortHandler!!.contentTop() + yOffset + labelLineHeight,
                        mLimitLinePaint!!
                    )
                } else if (position === LimitLabelPosition.RIGHT_BOTTOM) {
                    mLimitLinePaint!!.textAlign = Align.LEFT
                    c.drawText(
                        label, pts[0] + xOffset, mViewPortHandler!!.contentBottom() - yOffset,
                        mLimitLinePaint!!
                    )
                } else if (position === LimitLabelPosition.LEFT_TOP) {
                    mLimitLinePaint!!.textAlign = Align.RIGHT
                    val labelLineHeight = calcTextHeight(
                        mLimitLinePaint!!, label
                    ).toFloat()
                    c.drawText(
                        label,
                        pts[0] - xOffset,
                        mViewPortHandler!!.contentTop() + yOffset + labelLineHeight,
                        mLimitLinePaint!!
                    )
                } else {
                    mLimitLinePaint!!.textAlign = Align.RIGHT
                    c.drawText(
                        label, pts[0] - xOffset, mViewPortHandler!!.contentBottom() - yOffset,
                        mLimitLinePaint!!
                    )
                }
            }
            c.restoreToCount(clipRestoreCount)
        }
    }
}