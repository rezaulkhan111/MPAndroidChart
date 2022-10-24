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

/**
 * Renderer for the HorizontalBarChart.
 *
 * @author Philipp Jahoda
 */
class HorizontalBarChartRenderer(
    chart: BarDataProvider, animator: ChartAnimator?,
    viewPortHandler: ViewPortHandler?
) : BarChartRenderer(chart, animator, viewPortHandler) {
    override fun initBuffers() {
        val barData = mChart.barData
        mBarBuffers = arrayOfNulls<HorizontalBarBuffer>(barData.dataSetCount)
        for (i in mBarBuffers.indices) {
            val set = barData.getDataSetByIndex(i)
            mBarBuffers[i] = HorizontalBarBuffer(
                set.entryCount * 4 * if (set.isStacked) set.stackSize else 1,
                barData.dataSetCount, set.isStacked
            )
        }
    }

    private val mBarShadowRectBuffer = RectF()
    override fun drawDataSet(c: Canvas, dataSet: IBarDataSet, index: Int) {
        val trans = mChart.getTransformer(dataSet.axisDependency)
        mBarBorderPaint.color = dataSet.barBorderColor
        mBarBorderPaint.strokeWidth = Utils.convertDpToPixel(dataSet.barBorderWidth)
        val drawBorder = dataSet.barBorderWidth > 0f
        val phaseX = mAnimator.phaseX
        val phaseY = mAnimator.phaseY

        // draw the bar shadow before the values
        if (mChart.isDrawBarShadowEnabled) {
            mShadowPaint.color = dataSet.barShadowColor
            val barData = mChart.barData
            val barWidth = barData.barWidth
            val barWidthHalf = barWidth / 2.0f
            var x: Float
            var i = 0
            val count = Math.min(
                Math.ceil((dataSet.entryCount.toFloat() * phaseX).toDouble()).toInt(),
                dataSet.entryCount
            )
            while (i < count) {
                val e = dataSet.getEntryForIndex(i)
                x = e.x
                mBarShadowRectBuffer.top = x - barWidthHalf
                mBarShadowRectBuffer.bottom = x + barWidthHalf
                trans.rectValueToPixel(mBarShadowRectBuffer)
                if (!mViewPortHandler.isInBoundsTop(mBarShadowRectBuffer.bottom)) {
                    i++
                    continue
                }
                if (!mViewPortHandler.isInBoundsBottom(mBarShadowRectBuffer.top)) break
                mBarShadowRectBuffer.left = mViewPortHandler.contentLeft()
                mBarShadowRectBuffer.right = mViewPortHandler.contentRight()
                c.drawRect(mBarShadowRectBuffer, mShadowPaint)
                i++
            }
        }

        // initialize the buffer
        val buffer = mBarBuffers[index]!!
        buffer.setPhases(phaseX, phaseY)
        buffer.setDataSet(index)
        buffer.setInverted(mChart.isInverted(dataSet.axisDependency))
        buffer.setBarWidth(mChart.barData.barWidth)
        buffer.feed(dataSet)
        trans.pointValuesToPixel(buffer.buffer)
        val isCustomFill = dataSet.fills != null && !dataSet.fills.isEmpty()
        val isSingleColor = dataSet.colors.size == 1
        val isInverted = mChart.isInverted(dataSet.axisDependency)
        if (isSingleColor) {
            mRenderPaint.color = dataSet.color
        }
        var j = 0
        var pos = 0
        while (j < buffer.size()) {
            if (!mViewPortHandler.isInBoundsTop(buffer.buffer[j + 3])) break
            if (!mViewPortHandler.isInBoundsBottom(buffer.buffer[j + 1])) {
                j += 4
                pos++
                continue
            }
            if (!isSingleColor) {
                // Set the color for the currently drawn value. If the index
                // is out of bounds, reuse colors.
                mRenderPaint.color = dataSet.getColor(j / 4)
            }
            if (isCustomFill) {
                dataSet.getFill(pos)
                    .fillRect(
                        c, mRenderPaint,
                        buffer.buffer[j],
                        buffer.buffer[j + 1],
                        buffer.buffer[j + 2],
                        buffer.buffer[j + 3],
                        if (isInverted) Fill.Direction.LEFT else Fill.Direction.RIGHT
                    )
            } else {
                c.drawRect(
                    buffer.buffer[j], buffer.buffer[j + 1], buffer.buffer[j + 2],
                    buffer.buffer[j + 3], mRenderPaint
                )
            }
            if (drawBorder) {
                c.drawRect(
                    buffer.buffer[j], buffer.buffer[j + 1], buffer.buffer[j + 2],
                    buffer.buffer[j + 3], mBarBorderPaint
                )
            }
            j += 4
            pos++
        }
    }

    override fun drawValues(c: Canvas) {
        // if values are drawn
        if (isDrawingValuesAllowed(mChart)) {
            val dataSets = mChart.barData.dataSets
            val valueOffsetPlus = Utils.convertDpToPixel(5f)
            var posOffset = 0f
            var negOffset = 0f
            val drawValueAboveBar = mChart.isDrawValueAboveBarEnabled
            for (i in 0 until mChart.barData.dataSetCount) {
                val dataSet = dataSets[i]
                if (!shouldDrawValues(dataSet)) continue
                val isInverted = mChart.isInverted(dataSet.axisDependency)

                // apply the text-styling defined by the DataSet
                applyValueTextStyle(dataSet)
                val halfTextHeight = calcTextHeight(mValuePaint, "10") / 2f
                val formatter = dataSet.valueFormatter

                // get the buffer
                val buffer = mBarBuffers[i]!!
                val phaseY = mAnimator.phaseY
                val iconsOffset = MPPointF.getInstance(dataSet.iconsOffset)
                iconsOffset.x = Utils.convertDpToPixel(iconsOffset.x)
                iconsOffset.y = Utils.convertDpToPixel(iconsOffset.y)

                // if only single values are drawn (sum)
                if (!dataSet.isStacked) {
                    var j = 0
                    while (j < buffer.buffer.size * mAnimator.phaseX) {
                        val y = (buffer.buffer[j + 1] + buffer.buffer[j + 3]) / 2f
                        if (!mViewPortHandler.isInBoundsTop(buffer.buffer[j + 1])) break
                        if (!mViewPortHandler.isInBoundsX(buffer.buffer[j])) {
                            j += 4
                            continue
                        }
                        if (!mViewPortHandler.isInBoundsBottom(buffer.buffer[j + 1])) {
                            j += 4
                            continue
                        }
                        val entry = dataSet.getEntryForIndex(j / 4)
                        val `val` = entry.y
                        val formattedValue =
                            formatter.getFormattedValue(`val`, entry, i, mViewPortHandler)

                        // calculate the correct offset depending on the draw position of the value
                        val valueTextWidth = calcTextWidth(mValuePaint, formattedValue).toFloat()
                        posOffset =
                            if (drawValueAboveBar) valueOffsetPlus else -(valueTextWidth + valueOffsetPlus)
                        negOffset =
                            ((if (drawValueAboveBar) -(valueTextWidth + valueOffsetPlus) else valueOffsetPlus)
                                    - (buffer.buffer[j + 2] - buffer.buffer[j]))
                        if (isInverted) {
                            posOffset = -posOffset - valueTextWidth
                            negOffset = -negOffset - valueTextWidth
                        }
                        if (dataSet.isDrawValuesEnabled) {
                            drawValue(
                                c,
                                formattedValue,
                                buffer.buffer[j + 2] + if (`val` >= 0) posOffset else negOffset,
                                y + halfTextHeight,
                                dataSet.getValueTextColor(j / 2)
                            )
                        }
                        if (entry.icon != null && dataSet.isDrawIconsEnabled) {
                            val icon = entry.icon
                            var px = buffer.buffer[j + 2] + if (`val` >= 0) posOffset else negOffset
                            var py = y
                            px += iconsOffset.x
                            py += iconsOffset.y
                            drawImage(
                                c,
                                icon, px.toInt(), py.toInt(),
                                icon.intrinsicWidth,
                                icon.intrinsicHeight
                            )
                        }
                        j += 4
                    }

                    // if each value of a potential stack should be drawn
                } else {
                    val trans = mChart.getTransformer(dataSet.axisDependency)
                    var bufferIndex = 0
                    var index = 0
                    while (index < dataSet.entryCount * mAnimator.phaseX) {
                        val entry = dataSet.getEntryForIndex(index)
                        val color = dataSet.getValueTextColor(index)
                        val vals = entry.yVals

                        // we still draw stacked bars, but there is one
                        // non-stacked
                        // in between
                        if (vals == null) {
                            if (!mViewPortHandler.isInBoundsTop(buffer.buffer[bufferIndex + 1])) break
                            if (!mViewPortHandler.isInBoundsX(buffer.buffer[bufferIndex])) continue
                            if (!mViewPortHandler.isInBoundsBottom(buffer.buffer[bufferIndex + 1])) continue
                            val `val` = entry.y
                            val formattedValue = formatter.getFormattedValue(
                                `val`,
                                entry, i, mViewPortHandler
                            )

                            // calculate the correct offset depending on the draw position of the value
                            val valueTextWidth =
                                calcTextWidth(mValuePaint, formattedValue).toFloat()
                            posOffset =
                                if (drawValueAboveBar) valueOffsetPlus else -(valueTextWidth + valueOffsetPlus)
                            negOffset =
                                if (drawValueAboveBar) -(valueTextWidth + valueOffsetPlus) else valueOffsetPlus
                            if (isInverted) {
                                posOffset = -posOffset - valueTextWidth
                                negOffset = -negOffset - valueTextWidth
                            }
                            if (dataSet.isDrawValuesEnabled) {
                                drawValue(
                                    c, formattedValue, buffer.buffer[bufferIndex + 2]
                                            + if (entry.y >= 0) posOffset else negOffset,
                                    buffer.buffer[bufferIndex + 1] + halfTextHeight, color
                                )
                            }
                            if (entry.icon != null && dataSet.isDrawIconsEnabled) {
                                val icon = entry.icon
                                var px = (buffer.buffer[bufferIndex + 2]
                                        + if (entry.y >= 0) posOffset else negOffset)
                                var py = buffer.buffer[bufferIndex + 1]
                                px += iconsOffset.x
                                py += iconsOffset.y
                                drawImage(
                                    c,
                                    icon, px.toInt(), py.toInt(),
                                    icon.intrinsicWidth,
                                    icon.intrinsicHeight
                                )
                            }
                        } else {
                            val transformed = FloatArray(vals.size * 2)
                            var posY = 0f
                            var negY = -entry.negativeSum
                            run {
                                var k = 0
                                var idx = 0
                                while (k < transformed.size) {
                                    val value = vals[idx]
                                    var y: Float
                                    if (value == 0.0f && (posY == 0.0f || negY == 0.0f)) {
                                        // Take care of the situation of a 0.0 value, which overlaps a non-zero bar
                                        y = value
                                    } else if (value >= 0.0f) {
                                        posY += value
                                        y = posY
                                    } else {
                                        y = negY
                                        negY -= value
                                    }
                                    transformed[k] = y * phaseY
                                    k += 2
                                    idx++
                                }
                            }
                            trans.pointValuesToPixel(transformed)
                            var k = 0
                            while (k < transformed.size) {
                                val `val` = vals[k / 2]
                                val formattedValue = formatter.getFormattedValue(
                                    `val`,
                                    entry, i, mViewPortHandler
                                )

                                // calculate the correct offset depending on the draw position of the value
                                val valueTextWidth =
                                    calcTextWidth(mValuePaint, formattedValue).toFloat()
                                posOffset =
                                    if (drawValueAboveBar) valueOffsetPlus else -(valueTextWidth + valueOffsetPlus)
                                negOffset =
                                    if (drawValueAboveBar) -(valueTextWidth + valueOffsetPlus) else valueOffsetPlus
                                if (isInverted) {
                                    posOffset = -posOffset - valueTextWidth
                                    negOffset = -negOffset - valueTextWidth
                                }
                                val drawBelow = `val` == 0.0f && negY == 0.0f && posY > 0.0f ||
                                        `val` < 0.0f
                                val x = (transformed[k]
                                        + if (drawBelow) negOffset else posOffset)
                                val y =
                                    (buffer.buffer[bufferIndex + 1] + buffer.buffer[bufferIndex + 3]) / 2f
                                if (!mViewPortHandler.isInBoundsTop(y)) break
                                if (!mViewPortHandler.isInBoundsX(x)) {
                                    k += 2
                                    continue
                                }
                                if (!mViewPortHandler.isInBoundsBottom(y)) {
                                    k += 2
                                    continue
                                }
                                if (dataSet.isDrawValuesEnabled) {
                                    drawValue(c, formattedValue, x, y + halfTextHeight, color)
                                }
                                if (entry.icon != null && dataSet.isDrawIconsEnabled) {
                                    val icon = entry.icon
                                    drawImage(
                                        c,
                                        icon,
                                        (x + iconsOffset.x).toInt(),
                                        (y + iconsOffset.y).toInt(),
                                        icon.intrinsicWidth,
                                        icon.intrinsicHeight
                                    )
                                }
                                k += 2
                            }
                        }
                        bufferIndex =
                            if (vals == null) bufferIndex + 4 else bufferIndex + 4 * vals.size
                        index++
                    }
                }
                recycleInstance(iconsOffset)
            }
        }
    }

    protected fun drawValue(c: Canvas, valueText: String?, x: Float, y: Float, color: Int) {
        mValuePaint.color = color
        c.drawText(valueText!!, x, y, mValuePaint)
    }

    override fun prepareBarHighlight(
        x: Float,
        y1: Float,
        y2: Float,
        barWidthHalf: Float,
        trans: Transformer
    ) {
        val top = x - barWidthHalf
        val bottom = x + barWidthHalf
        mBarRect[y1, top, y2] = bottom
        trans.rectToPixelPhaseHorizontal(mBarRect, mAnimator.phaseY)
    }

    override fun setHighlightDrawPos(high: Highlight, bar: RectF) {
        high.setDraw(bar.centerY(), bar.right)
    }

    override fun isDrawingValuesAllowed(chart: ChartInterface): Boolean {
        return chart.data.entryCount < chart.maxVisibleCount
        * mViewPortHandler.scaleY
    }

    init {
        mValuePaint.textAlign = Align.LEFT
    }
}