package com.github.mikephil.charting.renderer

import android.graphics.*
import com.github.mikephil.charting.utils.Utils.convertDpToPixel
import com.github.mikephil.charting.utils.ViewPortHandler.contentWidth
import com.github.mikephil.charting.utils.ViewPortHandler.isFullyZoomedOutY
import com.github.mikephil.charting.utils.Transformer.getValuesByTouchPoint
import com.github.mikephil.charting.utils.ViewPortHandler.contentLeft
import com.github.mikephil.charting.utils.ViewPortHandler.contentTop
import com.github.mikephil.charting.utils.ViewPortHandler.contentBottom
import com.github.mikephil.charting.utils.MPPointD.Companion.recycleInstance
import com.github.mikephil.charting.utils.Utils.roundToNextSignificant
import com.github.mikephil.charting.utils.Utils.nextUp
import com.github.mikephil.charting.utils.ViewPortHandler.scaleX
import com.github.mikephil.charting.utils.ViewPortHandler.isFullyZoomedOutX
import com.github.mikephil.charting.utils.ViewPortHandler.contentRight
import com.github.mikephil.charting.utils.Utils.calcTextSize
import com.github.mikephil.charting.utils.Utils.calcTextHeight
import com.github.mikephil.charting.utils.Utils.getSizeOfRotatedRectangleByDegrees
import com.github.mikephil.charting.utils.FSize.Companion.recycleInstance
import com.github.mikephil.charting.utils.MPPointF.Companion.recycleInstance
import com.github.mikephil.charting.utils.Transformer.pointValuesToPixel
import com.github.mikephil.charting.utils.ViewPortHandler.isInBoundsX
import com.github.mikephil.charting.utils.Utils.calcTextWidth
import com.github.mikephil.charting.utils.ViewPortHandler.offsetRight
import com.github.mikephil.charting.utils.ViewPortHandler.chartWidth
import com.github.mikephil.charting.utils.Utils.drawXAxisValue
import com.github.mikephil.charting.utils.ViewPortHandler.contentRect
import com.github.mikephil.charting.utils.ViewPortHandler.offsetLeft
import com.github.mikephil.charting.utils.Transformer.getPixelForValues
import com.github.mikephil.charting.utils.Utils.getLineHeight
import com.github.mikephil.charting.utils.Utils.getLineSpacing
import com.github.mikephil.charting.utils.ViewPortHandler.chartHeight
import com.github.mikephil.charting.utils.Transformer.rectValueToPixel
import com.github.mikephil.charting.utils.ViewPortHandler.isInBoundsLeft
import com.github.mikephil.charting.utils.ViewPortHandler.isInBoundsRight
import com.github.mikephil.charting.utils.Fill.fillRect
import com.github.mikephil.charting.utils.Transformer.rectToPixelPhase
import com.github.mikephil.charting.utils.ViewPortHandler.isInBoundsY
import com.github.mikephil.charting.utils.Utils.drawImage
import com.github.mikephil.charting.utils.ViewPortHandler.smallestContentExtension
import com.github.mikephil.charting.utils.Transformer.pathValueToPixel
import com.github.mikephil.charting.utils.ViewPortHandler.isInBoundsTop
import com.github.mikephil.charting.utils.ViewPortHandler.isInBoundsBottom
import com.github.mikephil.charting.utils.Transformer.generateTransformedValuesLine
import com.github.mikephil.charting.utils.Utils.sDKInt
import com.github.mikephil.charting.utils.Utils.getPosition
import com.github.mikephil.charting.utils.ColorTemplate.colorWithAlpha
import com.github.mikephil.charting.utils.Transformer.generateTransformedValuesBubble
import com.github.mikephil.charting.utils.Transformer.generateTransformedValuesScatter
import com.github.mikephil.charting.utils.Transformer.generateTransformedValuesCandle
import com.github.mikephil.charting.utils.Transformer.rectToPixelPhaseHorizontal
import com.github.mikephil.charting.utils.ViewPortHandler.scaleY
import com.github.mikephil.charting.utils.ViewPortHandler.contentHeight
import com.github.mikephil.charting.interfaces.datasets.IScatterDataSet
import com.github.mikephil.charting.renderer.scatter.IShapeRenderer
import com.github.mikephil.charting.components.AxisBase
import com.github.mikephil.charting.animation.ChartAnimator
import com.github.mikephil.charting.interfaces.dataprovider.ChartInterface
import com.github.mikephil.charting.interfaces.datasets.IDataSet
import com.github.mikephil.charting.formatter.IValueFormatter
import android.graphics.Paint.Align
import com.github.mikephil.charting.components.XAxis
import com.github.mikephil.charting.renderer.AxisRenderer
import com.github.mikephil.charting.components.XAxis.XAxisPosition
import com.github.mikephil.charting.components.LimitLine
import com.github.mikephil.charting.components.LimitLine.LimitLabelPosition
import com.github.mikephil.charting.components.YAxis
import com.github.mikephil.charting.components.YAxis.AxisDependency
import com.github.mikephil.charting.components.YAxis.YAxisLabelPosition
import com.github.mikephil.charting.components.Legend
import com.github.mikephil.charting.components.LegendEntry
import com.github.mikephil.charting.data.ChartData
import com.github.mikephil.charting.interfaces.datasets.IBarDataSet
import com.github.mikephil.charting.interfaces.datasets.IPieDataSet
import com.github.mikephil.charting.interfaces.datasets.ICandleDataSet
import com.github.mikephil.charting.components.Legend.LegendOrientation
import com.github.mikephil.charting.components.Legend.LegendHorizontalAlignment
import com.github.mikephil.charting.components.Legend.LegendVerticalAlignment
import com.github.mikephil.charting.components.Legend.LegendDirection
import com.github.mikephil.charting.components.Legend.LegendForm
import com.github.mikephil.charting.interfaces.dataprovider.BarDataProvider
import com.github.mikephil.charting.renderer.BarLineScatterCandleBubbleRenderer
import com.github.mikephil.charting.buffer.BarBuffer
import com.github.mikephil.charting.data.BarData
import com.github.mikephil.charting.data.BarEntry
import android.graphics.drawable.Drawable
import com.github.mikephil.charting.charts.PieChart
import com.github.mikephil.charting.renderer.DataRenderer
import android.text.TextPaint
import android.text.StaticLayout
import com.github.mikephil.charting.data.PieData
import com.github.mikephil.charting.data.PieDataSet.ValuePosition
import com.github.mikephil.charting.data.PieEntry
import com.github.mikephil.charting.data.PieDataSet
import android.os.Build
import com.github.mikephil.charting.interfaces.dataprovider.LineDataProvider
import com.github.mikephil.charting.renderer.LineRadarRenderer
import com.github.mikephil.charting.data.LineData
import com.github.mikephil.charting.interfaces.datasets.ILineDataSet
import com.github.mikephil.charting.data.LineDataSet
import com.github.mikephil.charting.renderer.BarLineScatterCandleBubbleRenderer.XBounds
import com.github.mikephil.charting.renderer.LineChartRenderer.DataSetImageCache
import com.github.mikephil.charting.renderer.LineScatterCandleRadarRenderer
import com.github.mikephil.charting.charts.RadarChart
import com.github.mikephil.charting.data.RadarData
import com.github.mikephil.charting.interfaces.datasets.IRadarDataSet
import com.github.mikephil.charting.data.RadarEntry
import com.github.mikephil.charting.interfaces.dataprovider.BubbleDataProvider
import com.github.mikephil.charting.data.BubbleData
import com.github.mikephil.charting.interfaces.datasets.IBubbleDataSet
import com.github.mikephil.charting.data.BubbleEntry
import com.github.mikephil.charting.interfaces.dataprovider.ScatterDataProvider
import com.github.mikephil.charting.data.ScatterData
import com.github.mikephil.charting.charts.CombinedChart
import com.github.mikephil.charting.charts.Chart
import com.github.mikephil.charting.charts.CombinedChart.DrawOrder
import com.github.mikephil.charting.renderer.BarChartRenderer
import com.github.mikephil.charting.renderer.BubbleChartRenderer
import com.github.mikephil.charting.renderer.LineChartRenderer
import com.github.mikephil.charting.renderer.CandleStickChartRenderer
import com.github.mikephil.charting.renderer.ScatterChartRenderer
import com.github.mikephil.charting.data.CombinedData
import com.github.mikephil.charting.renderer.XAxisRenderer
import com.github.mikephil.charting.renderer.YAxisRenderer
import com.github.mikephil.charting.interfaces.dataprovider.CandleDataProvider
import com.github.mikephil.charting.data.CandleData
import com.github.mikephil.charting.data.CandleEntry
import com.github.mikephil.charting.buffer.HorizontalBarBuffer
import com.github.mikephil.charting.interfaces.datasets.ILineScatterCandleRadarDataSet
import com.github.mikephil.charting.charts.BarChart
import com.github.mikephil.charting.interfaces.datasets.IBarLineScatterCandleBubbleDataSet
import com.github.mikephil.charting.interfaces.dataprovider.BarLineScatterCandleBubbleDataProvider
import com.github.mikephil.charting.data.DataSet
import com.github.mikephil.charting.utils.*

open class XAxisRenderer(
    viewPortHandler: ViewPortHandler?,
    protected var mXAxis: XAxis,
    trans: Transformer?
) : AxisRenderer(viewPortHandler, trans, mXAxis) {
    protected fun setupGridPaint() {
        mGridPaint.color = mXAxis.gridColor
        mGridPaint.strokeWidth = mXAxis.gridLineWidth
        mGridPaint.pathEffect = mXAxis.gridDashPathEffect
    }

    override fun computeAxis(min: Float, max: Float, inverted: Boolean) {

        // calculate the starting and entry point of the y-labels (depending on
        // zoom / contentrect bounds)
        var min = min
        var max = max
        if (mViewPortHandler.contentWidth() > 10 && !mViewPortHandler.isFullyZoomedOutX) {
            val p1 = mTrans.getValuesByTouchPoint(
                mViewPortHandler.contentLeft(),
                mViewPortHandler.contentTop()
            )
            val p2 = mTrans.getValuesByTouchPoint(
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

    override fun computeAxisValues(min: Float, max: Float) {
        super.computeAxisValues(min, max)
        computeSize()
    }

    protected open fun computeSize() {
        val longest = mXAxis.longestLabel
        mAxisLabelPaint.typeface = mXAxis.typeface
        mAxisLabelPaint.textSize = mXAxis.textSize
        val labelSize = calcTextSize(mAxisLabelPaint, longest)
        val labelWidth = labelSize.width
        val labelHeight = calcTextHeight(mAxisLabelPaint, "Q").toFloat()
        val labelRotatedSize = getSizeOfRotatedRectangleByDegrees(
            labelWidth,
            labelHeight,
            mXAxis.labelRotationAngle
        )
        mXAxis.mLabelWidth = Math.round(labelWidth)
        mXAxis.mLabelHeight = Math.round(labelHeight)
        mXAxis.mLabelRotatedWidth = Math.round(labelRotatedSize.width)
        mXAxis.mLabelRotatedHeight = Math.round(labelRotatedSize.height)
        recycleInstance(labelRotatedSize)
        recycleInstance(labelSize)
    }

    override fun renderAxisLabels(c: Canvas) {
        if (!mXAxis.isEnabled || !mXAxis.isDrawLabelsEnabled) return
        val yoffset = mXAxis.yOffset
        mAxisLabelPaint.typeface = mXAxis.typeface
        mAxisLabelPaint.textSize = mXAxis.textSize
        mAxisLabelPaint.color = mXAxis.textColor
        val pointF = MPPointF.getInstance(0, 0)
        if (mXAxis.position == XAxisPosition.TOP) {
            pointF.x = 0.5f
            pointF.y = 1.0f
            drawLabels(c, mViewPortHandler.contentTop() - yoffset, pointF)
        } else if (mXAxis.position == XAxisPosition.TOP_INSIDE) {
            pointF.x = 0.5f
            pointF.y = 1.0f
            drawLabels(
                c,
                mViewPortHandler.contentTop() + yoffset + mXAxis.mLabelRotatedHeight,
                pointF
            )
        } else if (mXAxis.position == XAxisPosition.BOTTOM) {
            pointF.x = 0.5f
            pointF.y = 0.0f
            drawLabels(c, mViewPortHandler.contentBottom() + yoffset, pointF)
        } else if (mXAxis.position == XAxisPosition.BOTTOM_INSIDE) {
            pointF.x = 0.5f
            pointF.y = 0.0f
            drawLabels(
                c,
                mViewPortHandler.contentBottom() - yoffset - mXAxis.mLabelRotatedHeight,
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
        if (!mXAxis.isDrawAxisLineEnabled || !mXAxis.isEnabled) return
        mAxisLinePaint.color = mXAxis.axisLineColor
        mAxisLinePaint.strokeWidth = mXAxis.axisLineWidth
        mAxisLinePaint.pathEffect = mXAxis.axisLineDashPathEffect
        if (mXAxis.position == XAxisPosition.TOP || mXAxis.position == XAxisPosition.TOP_INSIDE || mXAxis.position == XAxisPosition.BOTH_SIDED) {
            c.drawLine(
                mViewPortHandler.contentLeft(),
                mViewPortHandler.contentTop(), mViewPortHandler.contentRight(),
                mViewPortHandler.contentTop(), mAxisLinePaint
            )
        }
        if (mXAxis.position == XAxisPosition.BOTTOM || mXAxis.position == XAxisPosition.BOTTOM_INSIDE || mXAxis.position == XAxisPosition.BOTH_SIDED) {
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
        val labelRotationAngleDegrees = mXAxis.labelRotationAngle
        val centeringEnabled = mXAxis.isCenterAxisLabelsEnabled
        val positions = FloatArray(mXAxis.mEntryCount * 2)
        run {
            var i = 0
            while (i < positions.size) {


                // only fill x values
                if (centeringEnabled) {
                    positions[i] = mXAxis.mCenteredEntries[i / 2]
                } else {
                    positions[i] = mXAxis.mEntries[i / 2]
                }
                i += 2
            }
        }
        mTrans.pointValuesToPixel(positions)
        var i = 0
        while (i < positions.size) {
            var x = positions[i]
            if (mViewPortHandler.isInBoundsX(x)) {
                val label = mXAxis.valueFormatter.getFormattedValue(mXAxis.mEntries[i / 2], mXAxis)
                if (mXAxis.isAvoidFirstLastClippingEnabled) {

                    // avoid clipping of the last
                    if (i / 2 == mXAxis.mEntryCount - 1 && mXAxis.mEntryCount > 1) {
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

    protected fun drawLabel(
        c: Canvas?,
        formattedLabel: String?,
        x: Float,
        y: Float,
        anchor: MPPointF?,
        angleDegrees: Float
    ) {
        drawXAxisValue(c!!, formattedLabel!!, x, y, mAxisLabelPaint, anchor!!, angleDegrees)
    }

    protected var mRenderGridLinesPath = Path()
    protected var mRenderGridLinesBuffer = FloatArray(2)
    override fun renderGridLines(c: Canvas) {
        if (!mXAxis.isDrawGridLinesEnabled || !mXAxis.isEnabled) return
        val clipRestoreCount = c.save()
        c.clipRect(gridClippingRect)
        if (mRenderGridLinesBuffer.size != mAxis.mEntryCount * 2) {
            mRenderGridLinesBuffer = FloatArray(mXAxis.mEntryCount * 2)
        }
        val positions = mRenderGridLinesBuffer
        run {
            var i = 0
            while (i < positions.size) {
                positions[i] = mXAxis.mEntries[i / 2]
                positions[i + 1] = mXAxis.mEntries[i / 2]
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
    open val gridClippingRect: RectF
        get() {
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
        val limitLines = mXAxis.limitLines
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
    fun renderLimitLineLine(c: Canvas, limitLine: LimitLine, position: FloatArray) {
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

    fun renderLimitLineLabel(
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
            if (labelPosition == LimitLabelPosition.RIGHT_TOP) {
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
            } else if (labelPosition == LimitLabelPosition.RIGHT_BOTTOM) {
                mLimitLinePaint!!.textAlign = Align.LEFT
                c.drawText(
                    label,
                    position[0] + xOffset,
                    mViewPortHandler.contentBottom() - yOffset,
                    mLimitLinePaint!!
                )
            } else if (labelPosition == LimitLabelPosition.LEFT_TOP) {
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
                    label,
                    position[0] - xOffset,
                    mViewPortHandler.contentBottom() - yOffset,
                    mLimitLinePaint!!
                )
            }
        }
    }

    init {
        mAxisLabelPaint.color = Color.BLACK
        mAxisLabelPaint.textAlign = Align.CENTER
        mAxisLabelPaint.textSize = convertDpToPixel(10f)
    }
}