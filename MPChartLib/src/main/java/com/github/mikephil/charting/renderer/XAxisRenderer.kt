package com.github.mikephil.charting.renderer

import android.graphics.*
import android.graphics.Paint.Align
import com.github.mikephil.charting.components.LimitLine
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
import com.github.mikephil.charting.utils.Utils.calcTextWidth
import com.github.mikephil.charting.utils.Utils.convertDpToPixel
import com.github.mikephil.charting.utils.Utils.drawXAxisValue
import com.github.mikephil.charting.utils.Utils.getSizeOfRotatedRectangleByDegrees

open class XAxisRenderer : AxisRenderer {

    protected var mXAxis: XAxis? = null

    constructor(
        viewPortHandler: ViewPortHandler,
        xAxis: XAxis,
        trans: Transformer?
    ) : super(
        viewPortHandler,
        trans,
        xAxis
    ) {
        mXAxis = xAxis
        mAxisLabelPaint!!.color = Color.BLACK
        mAxisLabelPaint!!.textAlign = Align.CENTER
        mAxisLabelPaint!!.textSize = convertDpToPixel(10f)
    }

    protected open fun setupGridPaint() {
        mGridPaint!!.color = mXAxis!!.getGridColor()
        mGridPaint!!.strokeWidth = mXAxis!!.getGridLineWidth()
        mGridPaint!!.pathEffect = mXAxis!!.getGridDashPathEffect()
    }

    override fun computeAxis(min: Float, max: Float, inverted: Boolean) {
        // calculate the starting and entry point of the y-labels (depending on
        // zoom / contentrect bounds)
        var minVari = min
        var maxVari = max
        if (mViewPortHandler!!.contentWidth() > 10 && !mViewPortHandler!!.isFullyZoomedOutX()) {
            val p1 = mTrans!!.getValuesByTouchPoint(
                mViewPortHandler!!.contentLeft(),
                mViewPortHandler!!.contentTop()
            )
            val p2 = mTrans!!.getValuesByTouchPoint(
                mViewPortHandler!!.contentRight(),
                mViewPortHandler!!.contentTop()
            )
            if (inverted) {
                minVari = p2!!.x.toFloat()
                maxVari = p1!!.x.toFloat()
            } else {
                minVari = p1!!.x.toFloat()
                maxVari = p2!!.x.toFloat()
            }
            recycleInstance(p1)
            recycleInstance(p2)
        }
        computeAxisValues(minVari, maxVari)
    }

    override fun computeAxisValues(min: Float, max: Float) {
        super.computeAxisValues(min, max)
        computeSize()
    }

    protected open fun computeSize() {
        val longest = mXAxis!!.getLongestLabel()
        mAxisLabelPaint!!.typeface = mXAxis!!.getTypeface()
        mAxisLabelPaint!!.textSize = mXAxis!!.getTextSize()
        val labelSize = calcTextSize(
            mAxisLabelPaint!!,
            longest!!
        )
        val labelWidth = labelSize!!.width
        val labelHeight = calcTextHeight(mAxisLabelPaint!!, "Q").toFloat()
        val labelRotatedSize = getSizeOfRotatedRectangleByDegrees(
            labelWidth,
            labelHeight,
            mXAxis!!.getLabelRotationAngle()
        )
        mXAxis!!.mLabelWidth = Math.round(labelWidth)
        mXAxis!!.mLabelHeight = Math.round(labelHeight)
        mXAxis!!.mLabelRotatedWidth = Math.round(labelRotatedSize.width)
        mXAxis!!.mLabelRotatedHeight = Math.round(labelRotatedSize.height)
        recycleInstance(labelRotatedSize)
        recycleInstance(labelSize)
    }

    override fun renderAxisLabels(c: Canvas?) {
        if (!mXAxis!!.isEnabled() || !mXAxis!!.isDrawLabelsEnabled()) return
        val yoffset = mXAxis!!.getYOffset()
        mAxisLabelPaint!!.typeface = mXAxis!!.getTypeface()
        mAxisLabelPaint!!.textSize = mXAxis!!.getTextSize()
        mAxisLabelPaint!!.color = mXAxis!!.getTextColor()
        val pointF = getInstance(0f, 0f)
        if (mXAxis!!.getPosition() === XAxisPosition.TOP) {
            pointF.x = 0.5f
            pointF.y = 1.0f
            drawLabels(c, mViewPortHandler!!.contentTop() - yoffset, pointF)
        } else if (mXAxis!!.getPosition() === XAxisPosition.TOP_INSIDE) {
            pointF.x = 0.5f
            pointF.y = 1.0f
            drawLabels(
                c,
                mViewPortHandler!!.contentTop() + yoffset + mXAxis!!.mLabelRotatedHeight,
                pointF
            )
        } else if (mXAxis!!.getPosition() === XAxisPosition.BOTTOM) {
            pointF.x = 0.5f
            pointF.y = 0.0f
            drawLabels(c, mViewPortHandler!!.contentBottom() + yoffset, pointF)
        } else if (mXAxis!!.getPosition() === XAxisPosition.BOTTOM_INSIDE) {
            pointF.x = 0.5f
            pointF.y = 0.0f
            drawLabels(
                c,
                mViewPortHandler!!.contentBottom() - yoffset - mXAxis!!.mLabelRotatedHeight,
                pointF
            )
        } else { // BOTH SIDED
            pointF.x = 0.5f
            pointF.y = 1.0f
            drawLabels(c, mViewPortHandler!!.contentTop() - yoffset, pointF)
            pointF.x = 0.5f
            pointF.y = 0.0f
            drawLabels(c, mViewPortHandler!!.contentBottom() + yoffset, pointF)
        }
        recycleInstance(pointF)
    }

    override fun renderAxisLine(c: Canvas?) {
        if (!mXAxis!!.isDrawAxisLineEnabled() || !mXAxis!!.isEnabled()) return
        mAxisLinePaint!!.color = mXAxis!!.getAxisLineColor()
        mAxisLinePaint!!.strokeWidth = mXAxis!!.getAxisLineWidth()
        mAxisLinePaint!!.pathEffect = mXAxis!!.getAxisLineDashPathEffect()
        if (mXAxis!!.getPosition() === XAxisPosition.TOP || mXAxis!!.getPosition() === XAxisPosition.TOP_INSIDE || mXAxis!!.getPosition() === XAxisPosition.BOTH_SIDED) {
            c!!.drawLine(
                mViewPortHandler!!.contentLeft(),
                mViewPortHandler!!.contentTop(), mViewPortHandler!!.contentRight(),
                mViewPortHandler!!.contentTop(), mAxisLinePaint!!
            )
        }
        if (mXAxis!!.getPosition() === XAxisPosition.BOTTOM || mXAxis!!.getPosition() === XAxisPosition.BOTTOM_INSIDE || mXAxis!!.getPosition() === XAxisPosition.BOTH_SIDED) {
            c!!.drawLine(
                mViewPortHandler!!.contentLeft(),
                mViewPortHandler!!.contentBottom(), mViewPortHandler!!.contentRight(),
                mViewPortHandler!!.contentBottom(), mAxisLinePaint!!
            )
        }
    }

    /**
     * draws the x-labels on the specified y-position
     *
     * @param pos
     */
    protected open fun drawLabels(c: Canvas?, pos: Float, anchor: MPPointF?) {
        val labelRotationAngleDegrees = mXAxis!!.getLabelRotationAngle()
        val centeringEnabled = mXAxis!!.isCenterAxisLabelsEnabled()
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
        mTrans!!.pointValuesToPixel(positions)
        var i = 0
        while (i < positions.size) {
            var x = positions[i]
            if (mViewPortHandler!!.isInBoundsX(x)) {
                val label =
                    mXAxis!!.getValueFormatter().getFormattedValue(mXAxis!!.mEntries[i / 2], mXAxis)
                if (mXAxis!!.isAvoidFirstLastClippingEnabled()) {

                    // avoid clipping of the last
                    if (i / 2 == mXAxis!!.mEntryCount - 1 && mXAxis!!.mEntryCount > 1) {
                        val width = calcTextWidth(
                            mAxisLabelPaint!!, label
                        ).toFloat()
                        if (width > mViewPortHandler!!.offsetRight() * 2
                            && x + width > mViewPortHandler!!.getChartWidth()
                        ) x -= width / 2

                        // avoid clipping of the first
                    } else if (i == 0) {
                        val width = calcTextWidth(
                            mAxisLabelPaint!!, label
                        ).toFloat()
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
            formattedLabel!!, x, y, mAxisLabelPaint!!, anchor!!, angleDegrees
        )
    }

    protected var mRenderGridLinesPath = Path()
    protected var mRenderGridLinesBuffer = FloatArray(2)
    override fun renderGridLines(c: Canvas?) {
        if (!mXAxis!!.isDrawGridLinesEnabled() || !mXAxis!!.isEnabled()) return
        val clipRestoreCount = c!!.save()
        c.clipRect(getGridClippingRect()!!)
        if (mRenderGridLinesBuffer.size != mAxis!!.mEntryCount * 2) {
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
        mTrans!!.pointValuesToPixel(positions)
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
        mGridClippingRect.set(mViewPortHandler!!.getContentRect()!!)
        mGridClippingRect.inset(-mAxis!!.getGridLineWidth(), 0f)
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
        gridLinePath.moveTo(x, mViewPortHandler!!.contentBottom())
        gridLinePath.lineTo(x, mViewPortHandler!!.contentTop())

        // draw a path because lines don't support dashing on lower android versions
        c.drawPath(gridLinePath, mGridPaint!!)
        gridLinePath.reset()
    }

    protected var mRenderLimitLinesBuffer = FloatArray(2)
    protected var mLimitLineClippingRect = RectF()

    /**
     * Draws the LimitLines associated with this axis to the screen.
     *
     * @param c
     */
    override fun renderLimitLines(c: Canvas?) {
        val limitLines = mXAxis!!.getLimitLines()
        if (limitLines == null || limitLines.size <= 0) return
        val position = mRenderLimitLinesBuffer
        position[0] = 0f
        position[1] = 0f
        for (i in limitLines.indices) {
            val l = limitLines[i]
            if (!l!!.isEnabled()) continue
            val clipRestoreCount = c!!.save()
            mLimitLineClippingRect.set(mViewPortHandler!!.getContentRect()!!)
            mLimitLineClippingRect.inset(-l.getLineWidth(), 0f)
            c.clipRect(mLimitLineClippingRect)
            position[0] = l.getLimit()
            position[1] = 0f
            mTrans!!.pointValuesToPixel(position)
            renderLimitLineLine(c, l, position)
            renderLimitLineLabel(c, l, position, 2f + l.getYOffset())
            c.restoreToCount(clipRestoreCount)
        }
    }

    var mLimitLineSegmentsBuffer = FloatArray(4)
    private val mLimitLinePath = Path()

    open fun renderLimitLineLine(c: Canvas, limitLine: LimitLine?, position: FloatArray) {
        mLimitLineSegmentsBuffer[0] = position[0]
        mLimitLineSegmentsBuffer[1] = mViewPortHandler!!.contentTop()
        mLimitLineSegmentsBuffer[2] = position[0]
        mLimitLineSegmentsBuffer[3] = mViewPortHandler!!.contentBottom()
        mLimitLinePath.reset()
        mLimitLinePath.moveTo(mLimitLineSegmentsBuffer[0], mLimitLineSegmentsBuffer[1])
        mLimitLinePath.lineTo(mLimitLineSegmentsBuffer[2], mLimitLineSegmentsBuffer[3])
        mLimitLinePaint!!.style = Paint.Style.STROKE
        mLimitLinePaint!!.color = limitLine!!.getLineColor()
        mLimitLinePaint!!.strokeWidth = limitLine.getLineWidth()
        mLimitLinePaint!!.pathEffect = limitLine.getDashPathEffect()
        c.drawPath(mLimitLinePath, mLimitLinePaint!!)
    }

    open fun renderLimitLineLabel(
        c: Canvas,
        limitLine: LimitLine?,
        position: FloatArray,
        yOffset: Float
    ) {
        val label = limitLine!!.getLabel()

        // if drawing the limit-value label is enabled
        if (label != null && label != "") {
            mLimitLinePaint!!.style = limitLine.getTextStyle()
            mLimitLinePaint!!.pathEffect = null
            mLimitLinePaint!!.color = limitLine.getTextColor()
            mLimitLinePaint!!.strokeWidth = 0.5f
            mLimitLinePaint!!.textSize = limitLine.getTextSize()
            val xOffset = limitLine.getLineWidth() + limitLine.getXOffset()
            val labelPosition = limitLine.getLabelPosition()
            if (labelPosition === LimitLabelPosition.RIGHT_TOP) {
                val labelLineHeight = calcTextHeight(
                    mLimitLinePaint!!, label
                ).toFloat()
                mLimitLinePaint!!.textAlign = Align.LEFT
                c.drawText(
                    label,
                    position[0] + xOffset,
                    mViewPortHandler!!.contentTop() + yOffset + labelLineHeight,
                    mLimitLinePaint!!
                )
            } else if (labelPosition === LimitLabelPosition.RIGHT_BOTTOM) {
                mLimitLinePaint!!.textAlign = Align.LEFT
                c.drawText(
                    label, position[0] + xOffset, mViewPortHandler!!.contentBottom() - yOffset,
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
                    mViewPortHandler!!.contentTop() + yOffset + labelLineHeight,
                    mLimitLinePaint!!
                )
            } else {
                mLimitLinePaint!!.textAlign = Align.RIGHT
                c.drawText(
                    label, position[0] - xOffset, mViewPortHandler!!.contentBottom() - yOffset,
                    mLimitLinePaint!!
                )
            }
        }
    }
}