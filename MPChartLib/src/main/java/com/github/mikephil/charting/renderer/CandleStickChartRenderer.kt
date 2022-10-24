package com.github.mikephil.charting.rendererimport

import android.graphics.*
import com.github.mikephil.charting.highlight.Highlight
import com.github.mikephil.charting.utils.*

com.github.mikephil.charting.utils.Utils.convertDpToPixel
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

class CandleStickChartRenderer(
    var mChart: CandleDataProvider, animator: ChartAnimator?,
    viewPortHandler: ViewPortHandler?
) : LineScatterCandleRadarRenderer(animator, viewPortHandler) {
    private val mShadowBuffers = FloatArray(8)
    private val mBodyBuffers = FloatArray(4)
    private val mRangeBuffers = FloatArray(4)
    private val mOpenBuffers = FloatArray(4)
    private val mCloseBuffers = FloatArray(4)
    override fun initBuffers() {}
    override fun drawData(c: Canvas) {
        val candleData = mChart.candleData
        for (set in candleData.dataSets) {
            if (set.isVisible) drawDataSet(c, set)
        }
    }

    protected fun drawDataSet(c: Canvas, dataSet: ICandleDataSet) {
        val trans = mChart.getTransformer(dataSet.axisDependency)
        val phaseY = mAnimator.phaseY
        val barSpace = dataSet.barSpace
        val showCandleBar = dataSet.showCandleBar
        mXBounds[mChart] = dataSet
        mRenderPaint.strokeWidth = dataSet.shadowWidth

        // draw the body
        for (j in mXBounds.min..mXBounds.range + mXBounds.min) {

            // get the entry
            val e = dataSet.getEntryForIndex(j) ?: continue
            val xPos = e.x
            val open = e.open
            val close = e.close
            val high = e.high
            val low = e.low
            if (showCandleBar) {
                // calculate the shadow
                mShadowBuffers[0] = xPos
                mShadowBuffers[2] = xPos
                mShadowBuffers[4] = xPos
                mShadowBuffers[6] = xPos
                if (open > close) {
                    mShadowBuffers[1] = high * phaseY
                    mShadowBuffers[3] = open * phaseY
                    mShadowBuffers[5] = low * phaseY
                    mShadowBuffers[7] = close * phaseY
                } else if (open < close) {
                    mShadowBuffers[1] = high * phaseY
                    mShadowBuffers[3] = close * phaseY
                    mShadowBuffers[5] = low * phaseY
                    mShadowBuffers[7] = open * phaseY
                } else {
                    mShadowBuffers[1] = high * phaseY
                    mShadowBuffers[3] = open * phaseY
                    mShadowBuffers[5] = low * phaseY
                    mShadowBuffers[7] = mShadowBuffers[3]
                }
                trans.pointValuesToPixel(mShadowBuffers)

                // draw the shadows
                if (dataSet.shadowColorSameAsCandle) {
                    if (open > close) mRenderPaint.color =
                        if (dataSet.decreasingColor == ColorTemplate.COLOR_NONE) dataSet.getColor(j) else dataSet.decreasingColor else if (open < close) mRenderPaint.color =
                        if (dataSet.increasingColor == ColorTemplate.COLOR_NONE) dataSet.getColor(j) else dataSet.increasingColor else mRenderPaint.color =
                        if (dataSet.neutralColor == ColorTemplate.COLOR_NONE) dataSet.getColor(j) else dataSet.neutralColor
                } else {
                    mRenderPaint.color =
                        if (dataSet.shadowColor == ColorTemplate.COLOR_NONE) dataSet.getColor(j) else dataSet.shadowColor
                }
                mRenderPaint.style = Paint.Style.STROKE
                c.drawLines(mShadowBuffers, mRenderPaint)

                // calculate the body
                mBodyBuffers[0] = xPos - 0.5f + barSpace
                mBodyBuffers[1] = close * phaseY
                mBodyBuffers[2] = xPos + 0.5f - barSpace
                mBodyBuffers[3] = open * phaseY
                trans.pointValuesToPixel(mBodyBuffers)

                // draw body differently for increasing and decreasing entry
                if (open > close) { // decreasing
                    if (dataSet.decreasingColor == ColorTemplate.COLOR_NONE) {
                        mRenderPaint.color = dataSet.getColor(j)
                    } else {
                        mRenderPaint.color = dataSet.decreasingColor
                    }
                    mRenderPaint.style = dataSet.decreasingPaintStyle
                    c.drawRect(
                        mBodyBuffers[0], mBodyBuffers[3],
                        mBodyBuffers[2], mBodyBuffers[1],
                        mRenderPaint
                    )
                } else if (open < close) {
                    if (dataSet.increasingColor == ColorTemplate.COLOR_NONE) {
                        mRenderPaint.color = dataSet.getColor(j)
                    } else {
                        mRenderPaint.color = dataSet.increasingColor
                    }
                    mRenderPaint.style = dataSet.increasingPaintStyle
                    c.drawRect(
                        mBodyBuffers[0], mBodyBuffers[1],
                        mBodyBuffers[2], mBodyBuffers[3],
                        mRenderPaint
                    )
                } else { // equal values
                    if (dataSet.neutralColor == ColorTemplate.COLOR_NONE) {
                        mRenderPaint.color = dataSet.getColor(j)
                    } else {
                        mRenderPaint.color = dataSet.neutralColor
                    }
                    c.drawLine(
                        mBodyBuffers[0], mBodyBuffers[1],
                        mBodyBuffers[2], mBodyBuffers[3],
                        mRenderPaint
                    )
                }
            } else {
                mRangeBuffers[0] = xPos
                mRangeBuffers[1] = high * phaseY
                mRangeBuffers[2] = xPos
                mRangeBuffers[3] = low * phaseY
                mOpenBuffers[0] = xPos - 0.5f + barSpace
                mOpenBuffers[1] = open * phaseY
                mOpenBuffers[2] = xPos
                mOpenBuffers[3] = open * phaseY
                mCloseBuffers[0] = xPos + 0.5f - barSpace
                mCloseBuffers[1] = close * phaseY
                mCloseBuffers[2] = xPos
                mCloseBuffers[3] = close * phaseY
                trans.pointValuesToPixel(mRangeBuffers)
                trans.pointValuesToPixel(mOpenBuffers)
                trans.pointValuesToPixel(mCloseBuffers)

                // draw the ranges
                var barColor: Int
                barColor =
                    if (open > close) if (dataSet.decreasingColor == ColorTemplate.COLOR_NONE) dataSet.getColor(
                        j
                    ) else dataSet.decreasingColor else if (open < close) if (dataSet.increasingColor == ColorTemplate.COLOR_NONE) dataSet.getColor(
                        j
                    ) else dataSet.increasingColor else if (dataSet.neutralColor == ColorTemplate.COLOR_NONE) dataSet.getColor(
                        j
                    ) else dataSet.neutralColor
                mRenderPaint.color = barColor
                c.drawLine(
                    mRangeBuffers[0], mRangeBuffers[1],
                    mRangeBuffers[2], mRangeBuffers[3],
                    mRenderPaint
                )
                c.drawLine(
                    mOpenBuffers[0], mOpenBuffers[1],
                    mOpenBuffers[2], mOpenBuffers[3],
                    mRenderPaint
                )
                c.drawLine(
                    mCloseBuffers[0], mCloseBuffers[1],
                    mCloseBuffers[2], mCloseBuffers[3],
                    mRenderPaint
                )
            }
        }
    }

    override fun drawValues(c: Canvas) {

        // if values are drawn
        if (isDrawingValuesAllowed(mChart)) {
            val dataSets = mChart.candleData.dataSets
            for (i in dataSets.indices) {
                val dataSet = dataSets[i]
                if (!shouldDrawValues(dataSet) || dataSet.entryCount < 1) continue

                // apply the text-styling defined by the DataSet
                applyValueTextStyle(dataSet)
                val trans = mChart.getTransformer(dataSet.axisDependency)
                mXBounds[mChart] = dataSet
                val positions = trans.generateTransformedValuesCandle(
                    dataSet, mAnimator.phaseX, mAnimator.phaseY, mXBounds.min, mXBounds.max
                )
                val yOffset = Utils.convertDpToPixel(5f)
                val iconsOffset = MPPointF.getInstance(dataSet.iconsOffset)
                iconsOffset.x = Utils.convertDpToPixel(iconsOffset.x)
                iconsOffset.y = Utils.convertDpToPixel(iconsOffset.y)
                var j = 0
                while (j < positions.size) {
                    val x = positions[j]
                    val y = positions[j + 1]
                    if (!mViewPortHandler.isInBoundsRight(x)) break
                    if (!mViewPortHandler.isInBoundsLeft(x) || !mViewPortHandler.isInBoundsY(y)) {
                        j += 2
                        continue
                    }
                    val entry = dataSet.getEntryForIndex(j / 2 + mXBounds.min)
                    if (dataSet.isDrawValuesEnabled) {
                        drawValue(
                            c,
                            dataSet.valueFormatter,
                            entry.high,
                            entry,
                            i,
                            x,
                            y - yOffset,
                            dataSet
                                .getValueTextColor(j / 2)
                        )
                    }
                    if (entry.icon != null && dataSet.isDrawIconsEnabled) {
                        val icon = entry.icon
                        drawImage(
                            c,
                            icon, (x + iconsOffset.x).toInt(), (y + iconsOffset.y).toInt(),
                            icon.intrinsicWidth,
                            icon.intrinsicHeight
                        )
                    }
                    j += 2
                }
                recycleInstance(iconsOffset)
            }
        }
    }

    override fun drawExtras(c: Canvas) {}
    override fun drawHighlighted(c: Canvas, indices: Array<Highlight>) {
        val candleData = mChart.candleData
        for (high in indices) {
            val set = candleData.getDataSetByIndex(high.dataSetIndex)
            if (set == null || !set.isHighlightEnabled) continue
            val e = set.getEntryForXValue(high.x, high.y)
            if (!isInBoundsX(e, set)) continue
            val lowValue = e.low * mAnimator.phaseY
            val highValue = e.high * mAnimator.phaseY
            val y = (lowValue + highValue) / 2f
            val pix = mChart.getTransformer(set.axisDependency).getPixelForValues(e.x, y)
            high.setDraw(pix.x.toFloat(), pix.y.toFloat())

            // draw the lines
            drawHighlightLines(c, pix.x.toFloat(), pix.y.toFloat(), set)
        }
    }
}