package com.github.mikephil.charting.renderer

import android.graphics.*
import android.graphics.Paint.Align
import com.github.mikephil.charting.components.LimitLine.LimitLabelPosition
import com.github.mikephil.charting.components.YAxis
import com.github.mikephil.charting.components.YAxis.AxisDependency
import com.github.mikephil.charting.components.YAxis.YAxisLabelPosition
import com.github.mikephil.charting.utils.*
import com.github.mikephil.charting.utils.Utils.calcTextHeight
import com.github.mikephil.charting.utils.Utils.convertDpToPixel

open class YAxisRenderer : AxisRenderer {

    protected var mYAxis: YAxis? = null

    protected var mZeroLinePaint: Paint? = null

    constructor(
        viewPortHandler: ViewPortHandler,
        yAxis: YAxis,
        trans: Transformer?
    ) : super(
        viewPortHandler,
        trans,
        yAxis
    ) {
        mYAxis = yAxis
        if (mViewPortHandler != null) {
            mAxisLabelPaint!!.color = Color.BLACK
            mAxisLabelPaint!!.textSize = convertDpToPixel(10f)
            mZeroLinePaint = Paint(Paint.ANTI_ALIAS_FLAG)
            mZeroLinePaint!!.color = Color.GRAY
            mZeroLinePaint!!.strokeWidth = 1f
            mZeroLinePaint!!.style = Paint.Style.STROKE
        }
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
        val xoffset = mYAxis!!.getXOffset()
        val yoffset = calcTextHeight(mAxisLabelPaint!!, "A") / 2.5f + mYAxis!!.getYOffset()
        val dependency = mYAxis!!.getAxisDependency()
        val labelPosition = mYAxis!!.getLabelPosition()
        var xPos = 0f
        if (dependency === AxisDependency.LEFT) {
            if (labelPosition === YAxisLabelPosition.OUTSIDE_CHART) {
                mAxisLabelPaint!!.textAlign = Align.RIGHT
                xPos = mViewPortHandler!!.offsetLeft() - xoffset
            } else {
                mAxisLabelPaint!!.textAlign = Align.LEFT
                xPos = mViewPortHandler!!.offsetLeft() + xoffset
            }
        } else {
            if (labelPosition === YAxisLabelPosition.OUTSIDE_CHART) {
                mAxisLabelPaint!!.textAlign = Align.LEFT
                xPos = mViewPortHandler!!.contentRight() + xoffset
            } else {
                mAxisLabelPaint!!.textAlign = Align.RIGHT
                xPos = mViewPortHandler!!.contentRight() - xoffset
            }
        }
        drawYLabels(c!!, xPos, positions, yoffset)
    }

    override fun renderAxisLine(c: Canvas?) {
        if (!mYAxis!!.isEnabled() || !mYAxis!!.isDrawAxisLineEnabled()) return
        mAxisLinePaint!!.color = mYAxis!!.getAxisLineColor()
        mAxisLinePaint!!.strokeWidth = mYAxis!!.getAxisLineWidth()
        if (mYAxis!!.getAxisDependency() === AxisDependency.LEFT) {
            c!!.drawLine(
                mViewPortHandler!!.contentLeft(),
                mViewPortHandler!!.contentTop(),
                mViewPortHandler!!.contentLeft(),
                mViewPortHandler!!.contentBottom(),
                mAxisLinePaint!!
            )
        } else {
            c!!.drawLine(
                mViewPortHandler!!.contentRight(),
                mViewPortHandler!!.contentTop(),
                mViewPortHandler!!.contentRight(),
                mViewPortHandler!!.contentBottom(),
                mAxisLinePaint!!
            )
        }
    }

    /**
     * draws the y-labels on the specified x-position
     *
     * @param fixedPosition
     * @param positions
     */
    protected open fun drawYLabels(
        c: Canvas,
        fixedPosition: Float,
        positions: FloatArray,
        offset: Float
    ) {
        val from = if (mYAxis!!.isDrawBottomYLabelEntryEnabled()) 0 else 1
        val to =
            if (mYAxis!!.isDrawTopYLabelEntryEnabled()) mYAxis!!.mEntryCount else mYAxis!!.mEntryCount - 1
        val xOffset = mYAxis!!.getLabelXOffset()

        // draw
        for (i in from until to) {
            val text = mYAxis!!.getFormattedLabel(i)
            c.drawText(
                text!!,
                fixedPosition + xOffset,
                positions[i * 2 + 1] + offset,
                mAxisLabelPaint!!
            )
        }
    }

    protected var mRenderGridLinesPath = Path()
    override fun renderGridLines(c: Canvas?) {
        if (!mYAxis!!.isEnabled()) return
        if (mYAxis!!.isDrawGridLinesEnabled()) {
            val clipRestoreCount = c!!.save()
            c.clipRect(getGridClippingRect()!!)
            val positions = getTransformedPositions()
            mGridPaint!!.color = mYAxis!!.getGridColor()
            mGridPaint!!.strokeWidth = mYAxis!!.getGridLineWidth()
            mGridPaint!!.pathEffect = mYAxis!!.getGridDashPathEffect()
            val gridLinePath = mRenderGridLinesPath
            gridLinePath.reset()

            // draw the grid
            var i = 0
            while (i < positions.size) {


                // draw a path because lines don't support dashing on lower android versions
                c.drawPath(linePath(gridLinePath, i, positions)!!, mGridPaint!!)
                gridLinePath.reset()
                i += 2
            }
            c.restoreToCount(clipRestoreCount)
        }
        if (mYAxis!!.isDrawZeroLineEnabled()) {
            drawZeroLine(c!!)
        }
    }

    protected var mGridClippingRect = RectF()

    open fun getGridClippingRect(): RectF? {
        mGridClippingRect.set(mViewPortHandler!!.getContentRect()!!)
        mGridClippingRect.inset(0f, -mAxis!!.getGridLineWidth())
        return mGridClippingRect
    }

    /**
     * Calculates the path for a grid line.
     *
     * @param p
     * @param i
     * @param positions
     * @return
     */
    protected open fun linePath(p: Path, i: Int, positions: FloatArray): Path? {
        p.moveTo(mViewPortHandler!!.offsetLeft(), positions[i + 1])
        p.lineTo(mViewPortHandler!!.contentRight(), positions[i + 1])
        return p
    }

    protected var mGetTransformedPositionsBuffer = FloatArray(2)

    /**
     * Transforms the values contained in the axis entries to screen pixels and returns them in form of a float array
     * of x- and y-coordinates.
     *
     * @return
     */
    protected open fun getTransformedPositions(): FloatArray {
        if (mGetTransformedPositionsBuffer.size != mYAxis!!.mEntryCount * 2) {
            mGetTransformedPositionsBuffer = FloatArray(mYAxis!!.mEntryCount * 2)
        }
        val positions = mGetTransformedPositionsBuffer
        var i = 0
        while (i < positions.size) {

            // only fill y values, x values are not needed for y-labels
            positions[i + 1] = mYAxis!!.mEntries[i / 2]
            i += 2
        }
        mTrans!!.pointValuesToPixel(positions)
        return positions
    }

    protected var mDrawZeroLinePath = Path()
    protected var mZeroLineClippingRect = RectF()

    /**
     * Draws the zero line.
     */
    protected open fun drawZeroLine(c: Canvas) {
        val clipRestoreCount = c.save()
        mZeroLineClippingRect.set(mViewPortHandler!!.getContentRect()!!)
        mZeroLineClippingRect.inset(0f, -mYAxis!!.getZeroLineWidth())
        c.clipRect(mZeroLineClippingRect)

        // draw zero line
        val pos = mTrans!!.getPixelForValues(0f, 0f)!!
        mZeroLinePaint!!.color = mYAxis!!.getZeroLineColor()
        mZeroLinePaint!!.strokeWidth = mYAxis!!.getZeroLineWidth()
        val zeroLinePath = mDrawZeroLinePath
        zeroLinePath.reset()
        zeroLinePath.moveTo(mViewPortHandler!!.contentLeft(), pos.y.toFloat())
        zeroLinePath.lineTo(mViewPortHandler!!.contentRight(), pos.y.toFloat())

        // draw a path because lines don't support dashing on lower android versions
        c.drawPath(zeroLinePath, mZeroLinePaint!!)
        c.restoreToCount(clipRestoreCount)
    }

    protected var mRenderLimitLines = Path()
    private var mRenderLimitLinesBuffer = FloatArray(2)
    protected var mLimitLineClippingRect = RectF()

    /**
     * Draws the LimitLines associated with this axis to the screen.
     *
     * @param c
     */
    override fun renderLimitLines(c: Canvas?) {
        val limitLines = mYAxis!!.getLimitLines()
        if (limitLines == null || limitLines.size <= 0) return
        val pts = mRenderLimitLinesBuffer
        pts[0] = 0f
        pts[1] = 0f
        val limitLinePath = mRenderLimitLines
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
                mLimitLinePaint!!.typeface = l.getTypeface()
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