package com.github.mikephil.charting.renderer

import android.graphics.*
import com.github.mikephil.charting.animation.ChartAnimator
import com.github.mikephil.charting.buffer.BarBuffer
import com.github.mikephil.charting.highlight.Highlight
import com.github.mikephil.charting.interfaces.dataprovider.BarDataProvider
import com.github.mikephil.charting.interfaces.datasets.IBarDataSet
import com.github.mikephil.charting.utils.*
import com.github.mikephil.charting.utils.MPPointF.Companion.recycleInstance
import com.github.mikephil.charting.utils.Utils.calcTextHeight
import com.github.mikephil.charting.utils.Utils.convertDpToPixel
import com.github.mikephil.charting.utils.Utils.drawImage

open class BarChartRenderer(
    var mChart: BarDataProvider, animator: ChartAnimator?,
    viewPortHandler: ViewPortHandler?
) : BarLineScatterCandleBubbleRenderer(animator, viewPortHandler) {
    /**
     * the rect object that is used for drawing the bars
     */
    protected var mBarRect = RectF()
    protected var mBarBuffers: Array<BarBuffer?>
    protected var mShadowPaint: Paint
    protected var mBarBorderPaint: Paint
    override fun initBuffers() {
        val barData = mChart.barData
        mBarBuffers = arrayOfNulls(barData.dataSetCount)
        for (i in mBarBuffers.indices) {
            val set = barData.getDataSetByIndex(i)
            mBarBuffers[i] = BarBuffer(
                set.entryCount * 4 * if (set.isStacked) set.stackSize else 1,
                barData.dataSetCount, set.isStacked
            )
        }
    }

    override fun drawData(c: Canvas) {
        val barData = mChart.barData
        for (i in 0 until barData.dataSetCount) {
            val set = barData.getDataSetByIndex(i)
            if (set.isVisible) {
                drawDataSet(c, set, i)
            }
        }
    }

    private val mBarShadowRectBuffer = RectF()
    protected open fun drawDataSet(c: Canvas, dataSet: IBarDataSet, index: Int) {
        val trans = mChart.getTransformer(dataSet.axisDependency)
        mBarBorderPaint.color = dataSet.barBorderColor
        mBarBorderPaint.strokeWidth =
            convertDpToPixel(dataSet.barBorderWidth)
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
                mBarShadowRectBuffer.left = x - barWidthHalf
                mBarShadowRectBuffer.right = x + barWidthHalf
                trans.rectValueToPixel(mBarShadowRectBuffer)
                if (!mViewPortHandler.isInBoundsLeft(mBarShadowRectBuffer.right)) {
                    i++
                    continue
                }
                if (!mViewPortHandler.isInBoundsRight(mBarShadowRectBuffer.left)) break
                mBarShadowRectBuffer.top = mViewPortHandler.contentTop()
                mBarShadowRectBuffer.bottom = mViewPortHandler.contentBottom()
                c.drawRect(mBarShadowRectBuffer, mShadowPaint)
                i++
            }
        }

        // initialize the buffer
        val buffer = mBarBuffers[index]
        buffer!!.setPhases(phaseX, phaseY)
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
            if (!mViewPortHandler.isInBoundsLeft(buffer.buffer[j + 2])) {
                j += 4
                pos++
                continue
            }
            if (!mViewPortHandler.isInBoundsRight(buffer.buffer[j])) break
            if (!isSingleColor) {
                // Set the color for the currently drawn value. If the index
                // is out of bounds, reuse colors.
                mRenderPaint.color = dataSet.getColor(pos)
            }
            if (isCustomFill) {
                dataSet.getFill(pos)
                    .fillRect(
                        c, mRenderPaint,
                        buffer.buffer[j],
                        buffer.buffer[j + 1],
                        buffer.buffer[j + 2],
                        buffer.buffer[j + 3],
                        if (isInverted) Fill.Direction.DOWN else Fill.Direction.UP
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

    protected open fun prepareBarHighlight(
        x: Float,
        y1: Float,
        y2: Float,
        barWidthHalf: Float,
        trans: Transformer
    ) {
        val left = x - barWidthHalf
        val right = x + barWidthHalf
        mBarRect[left, y1, right] = y2
        trans.rectToPixelPhase(mBarRect, mAnimator.phaseY)
    }

    override fun drawValues(c: Canvas) {

        // if values are drawn
        if (isDrawingValuesAllowed(mChart)) {
            val dataSets = mChart.barData.dataSets
            val valueOffsetPlus = convertDpToPixel(4.5f)
            var posOffset = 0f
            var negOffset = 0f
            val drawValueAboveBar = mChart.isDrawValueAboveBarEnabled
            for (i in 0 until mChart.barData.dataSetCount) {
                val dataSet = dataSets[i]
                if (!shouldDrawValues(dataSet)) continue

                // apply the text-styling defined by the DataSet
                applyValueTextStyle(dataSet)
                val isInverted = mChart.isInverted(dataSet.axisDependency)

                // calculate the correct offset depending on the draw position of
                // the value
                val valueTextHeight = calcTextHeight(mValuePaint, "8").toFloat()
                posOffset =
                    if (drawValueAboveBar) -valueOffsetPlus else valueTextHeight + valueOffsetPlus
                negOffset =
                    if (drawValueAboveBar) valueTextHeight + valueOffsetPlus else -valueOffsetPlus
                if (isInverted) {
                    posOffset = -posOffset - valueTextHeight
                    negOffset = -negOffset - valueTextHeight
                }

                // get the buffer
                val buffer = mBarBuffers[i]
                val phaseY = mAnimator.phaseY
                val iconsOffset = MPPointF.getInstance(dataSet.iconsOffset)
                iconsOffset.x = convertDpToPixel(iconsOffset.x)
                iconsOffset.y = convertDpToPixel(iconsOffset.y)

                // if only single values are drawn (sum)
                if (!dataSet.isStacked) {
                    var j = 0
                    while (j < buffer!!.buffer.size * mAnimator.phaseX) {
                        val x = (buffer.buffer[j] + buffer.buffer[j + 2]) / 2f
                        if (!mViewPortHandler.isInBoundsRight(x)) break
                        if (!mViewPortHandler.isInBoundsY(buffer.buffer[j + 1])
                            || !mViewPortHandler.isInBoundsLeft(x)
                        ) {
                            j += 4
                            continue
                        }
                        val entry = dataSet.getEntryForIndex(j / 4)
                        val `val` = entry.y
                        if (dataSet.isDrawValuesEnabled) {
                            drawValue(
                                c, dataSet.valueFormatter, `val`, entry, i, x,
                                if (`val` >= 0) buffer.buffer[j + 1] + posOffset else buffer.buffer[j + 3] + negOffset,
                                dataSet.getValueTextColor(j / 4)
                            )
                        }
                        if (entry.icon != null && dataSet.isDrawIconsEnabled) {
                            val icon = entry.icon
                            var px = x
                            var py =
                                if (`val` >= 0) buffer.buffer[j + 1] + posOffset else buffer.buffer[j + 3] + negOffset
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

                    // if we have stacks
                } else {
                    val trans = mChart.getTransformer(dataSet.axisDependency)
                    var bufferIndex = 0
                    var index = 0
                    while (index < dataSet.entryCount * mAnimator.phaseX) {
                        val entry = dataSet.getEntryForIndex(index)
                        val vals = entry.yVals
                        val x = (buffer!!.buffer[bufferIndex] + buffer.buffer[bufferIndex + 2]) / 2f
                        val color = dataSet.getValueTextColor(index)

                        // we still draw stacked bars, but there is one
                        // non-stacked
                        // in between
                        if (vals == null) {
                            if (!mViewPortHandler.isInBoundsRight(x)) break
                            if (!mViewPortHandler.isInBoundsY(buffer.buffer[bufferIndex + 1])
                                || !mViewPortHandler.isInBoundsLeft(x)
                            ) continue
                            if (dataSet.isDrawValuesEnabled) {
                                drawValue(
                                    c, dataSet.valueFormatter, entry.y, entry, i, x,
                                    buffer.buffer[bufferIndex + 1] +
                                            if (entry.y >= 0) posOffset else negOffset,
                                    color
                                )
                            }
                            if (entry.icon != null && dataSet.isDrawIconsEnabled) {
                                val icon = entry.icon
                                var px = x
                                var py = buffer.buffer[bufferIndex + 1] +
                                        if (entry.y >= 0) posOffset else negOffset
                                px += iconsOffset.x
                                py += iconsOffset.y
                                drawImage(
                                    c,
                                    icon, px.toInt(), py.toInt(),
                                    icon.intrinsicWidth,
                                    icon.intrinsicHeight
                                )
                            }

                            // draw stack values
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
                                    transformed[k + 1] = y * phaseY
                                    k += 2
                                    idx++
                                }
                            }
                            trans.pointValuesToPixel(transformed)
                            var k = 0
                            while (k < transformed.size) {
                                val `val` = vals[k / 2]
                                val drawBelow = `val` == 0.0f && negY == 0.0f && posY > 0.0f ||
                                        `val` < 0.0f
                                val y = (transformed[k + 1]
                                        + if (drawBelow) negOffset else posOffset)
                                if (!mViewPortHandler.isInBoundsRight(x)) break
                                if (!mViewPortHandler.isInBoundsY(y)
                                    || !mViewPortHandler.isInBoundsLeft(x)
                                ) {
                                    k += 2
                                    continue
                                }
                                if (dataSet.isDrawValuesEnabled) {
                                    drawValue(
                                        c,
                                        dataSet.valueFormatter,
                                        vals[k / 2],
                                        entry,
                                        i,
                                        x,
                                        y,
                                        color
                                    )
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

    override fun drawHighlighted(c: Canvas, indices: Array<Highlight>) {
        val barData = mChart.barData
        for (high in indices) {
            val set = barData.getDataSetByIndex(high.dataSetIndex)
            if (set == null || !set.isHighlightEnabled) continue
            val e = set.getEntryForXValue(high.x, high.y)
            if (!isInBoundsX(e, set)) continue
            val trans = mChart.getTransformer(set.axisDependency)
            mHighlightPaint.color = set.highLightColor
            mHighlightPaint.alpha = set.highLightAlpha
            val isStack = if (high.stackIndex >= 0 && e.isStacked) true else false
            val y1: Float
            val y2: Float
            if (isStack) {
                if (mChart.isHighlightFullBarEnabled) {
                    y1 = e.positiveSum
                    y2 = -e.negativeSum
                } else {
                    val range = e.ranges[high.stackIndex]
                    y1 = range.from
                    y2 = range.to
                }
            } else {
                y1 = e.y
                y2 = 0f
            }
            prepareBarHighlight(e.x, y1, y2, barData.barWidth / 2f, trans)
            setHighlightDrawPos(high, mBarRect)
            c.drawRect(mBarRect, mHighlightPaint)
        }
    }

    /**
     * Sets the drawing position of the highlight object based on the riven bar-rect.
     * @param high
     */
    protected open fun setHighlightDrawPos(high: Highlight, bar: RectF) {
        high.setDraw(bar.centerX(), bar.top)
    }

    override fun drawExtras(c: Canvas) {}

    init {
        mHighlightPaint = Paint(Paint.ANTI_ALIAS_FLAG)
        mHighlightPaint.style = Paint.Style.FILL
        mHighlightPaint.color = Color.rgb(0, 0, 0)
        // set alpha after color
        mHighlightPaint.alpha = 120
        mShadowPaint = Paint(Paint.ANTI_ALIAS_FLAG)
        mShadowPaint.style = Paint.Style.FILL
        mBarBorderPaint = Paint(Paint.ANTI_ALIAS_FLAG)
        mBarBorderPaint.style = Paint.Style.STROKE
    }
}