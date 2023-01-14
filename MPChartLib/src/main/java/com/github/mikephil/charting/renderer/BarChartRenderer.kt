package com.github.mikephil.charting.renderer

import android.graphics.*
import com.github.mikephil.charting.animation.ChartAnimator
import com.github.mikephil.charting.buffer.BarBuffer
import com.github.mikephil.charting.highlight.Highlight
import com.github.mikephil.charting.interfaces.dataprovider.BarDataProvider
import com.github.mikephil.charting.interfaces.datasets.IBarDataSet
import com.github.mikephil.charting.utils.*
import com.github.mikephil.charting.utils.MPPointF.Companion.getInstance
import com.github.mikephil.charting.utils.MPPointF.Companion.recycleInstance
import com.github.mikephil.charting.utils.Utils.calcTextHeight
import com.github.mikephil.charting.utils.Utils.convertDpToPixel
import com.github.mikephil.charting.utils.Utils.drawImage

open class BarChartRenderer : BarLineScatterCandleBubbleRenderer {

    var mChart: BarDataProvider? = null

    /**
     * the rect object that is used for drawing the bars
     */
    protected var mBarRect = RectF()

    protected var mBarBuffers: Array<BarBuffer?>? = null

    protected var mShadowPaint: Paint? = null
    protected var mBarBorderPaint: Paint? = null

    constructor(
        chart: BarDataProvider, animator: ChartAnimator,
        viewPortHandler: ViewPortHandler
    ) : super(animator, viewPortHandler) {
        mChart = chart
        mHighlightPaint = Paint(Paint.ANTI_ALIAS_FLAG)
        mHighlightPaint!!.style = Paint.Style.FILL
        mHighlightPaint!!.color = Color.rgb(0, 0, 0)
        // set alpha after color
        mHighlightPaint!!.alpha = 120
        mShadowPaint = Paint(Paint.ANTI_ALIAS_FLAG)
        mShadowPaint!!.style = Paint.Style.FILL
        mBarBorderPaint = Paint(Paint.ANTI_ALIAS_FLAG)
        mBarBorderPaint!!.style = Paint.Style.STROKE
    }

    override fun initBuffers() {
        val barData = mChart!!.getBarData()
        mBarBuffers = arrayOfNulls(barData!!.getDataSetCount())
        mBarBuffers
        for (i in mBarBuffers!!.indices) {
            val set = barData.getDataSetByIndex(i)
            mBarBuffers!![i] = BarBuffer(
                set!!.getEntryCount() * 4 * if (set.isStacked()) set.getStackSize() else 1,
                barData.getDataSetCount(), set.isStacked()
            )
        }
    }

    override fun drawData(c: Canvas?) {
        val barData = mChart!!.getBarData()
        for (i in 0 until barData!!.getDataSetCount()) {
            val set = barData.getDataSetByIndex(i)
            if (set!!.isVisible()) {
                drawDataSet(c!!, set, i)
            }
        }
    }

    private val mBarShadowRectBuffer = RectF()

    protected open fun drawDataSet(c: Canvas, dataSet: IBarDataSet?, index: Int) {
        val trans = mChart?.getTransformer(dataSet?.getAxisDependency())
        mBarBorderPaint!!.color = dataSet?.getBarBorderColor()!!
        mBarBorderPaint!!.strokeWidth = convertDpToPixel(
            dataSet.getBarBorderWidth()
        )
        val drawBorder = dataSet.getBarBorderWidth() > 0f
        val phaseX = mAnimator?.getPhaseX()
        val phaseY = mAnimator?.getPhaseY()

        // draw the bar shadow before the values
        if (mChart!!.isDrawBarShadowEnabled()) {
            mShadowPaint!!.color = dataSet.getBarShadowColor()
            val barData = mChart!!.getBarData()
            val barWidth = barData!!.getBarWidth()
            val barWidthHalf = barWidth / 2.0f
            var x: Float
            var i = 0
            val count = Math.min(
                Math.ceil((dataSet.getEntryCount().toFloat() * phaseX!!).toDouble()).toInt(),
                dataSet.getEntryCount()
            )
            while (i < count) {
                val e = dataSet.getEntryForIndex(i)!!
                x = e.getX()
                mBarShadowRectBuffer.left = x - barWidthHalf
                mBarShadowRectBuffer.right = x + barWidthHalf
                trans?.rectValueToPixel(mBarShadowRectBuffer)
                if (!mViewPortHandler!!.isInBoundsLeft(mBarShadowRectBuffer.right)) {
                    i++
                    continue
                }
                if (!mViewPortHandler!!.isInBoundsRight(mBarShadowRectBuffer.left)) break
                mBarShadowRectBuffer.top = mViewPortHandler!!.contentTop()
                mBarShadowRectBuffer.bottom = mViewPortHandler!!.contentBottom()
                c.drawRect(mBarShadowRectBuffer, mShadowPaint!!)
                i++
            }
        }

        // initialize the buffer
        val buffer = mBarBuffers!![index]
        buffer?.setPhases(phaseX!!.toFloat(), phaseY!!.toFloat())
        buffer?.setDataSet(index)
        buffer?.setInverted(mChart!!.isInverted(dataSet.getAxisDependency()))
        buffer?.setBarWidth(mChart!!.getBarData()!!.getBarWidth())
        buffer?.feed(dataSet)
        trans?.pointValuesToPixel(buffer?.buffer)
        val isCustomFill = dataSet.getFills() != null && !dataSet.getFills()!!.isEmpty()
        val isSingleColor = dataSet.getColors()!!.size == 1
        val isInverted = mChart!!.isInverted(dataSet.getAxisDependency())
        if (isSingleColor) {
            mRenderPaint?.color = dataSet.getColor()
        }
        var j = 0
        var pos = 0
        while (j < buffer!!.size()) {
            if (!mViewPortHandler!!.isInBoundsLeft(buffer.buffer[j + 2])) {
                j += 4
                pos++
                continue
            }
            if (!mViewPortHandler!!.isInBoundsRight(buffer.buffer[j])) break
            if (!isSingleColor) {
                // Set the color for the currently drawn value. If the index
                // is out of bounds, reuse colors.
                mRenderPaint?.color = dataSet.getColor(pos)
            }
            if (isCustomFill) {
                dataSet.getFill(pos)!!.fillRect(
                    c, mRenderPaint!!,
                    buffer.buffer[j],
                    buffer.buffer[j + 1],
                    buffer.buffer[j + 2],
                    buffer.buffer[j + 3],
                    if (isInverted) Fill.Direction.DOWN else Fill.Direction.UP
                )
            } else {
                c.drawRect(
                    buffer.buffer[j], buffer.buffer[j + 1], buffer.buffer[j + 2],
                    buffer.buffer[j + 3], mRenderPaint!!
                )
            }
            if (drawBorder) {
                c.drawRect(
                    buffer.buffer[j], buffer.buffer[j + 1], buffer.buffer[j + 2],
                    buffer.buffer[j + 3], mBarBorderPaint!!
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
        trans.rectToPixelPhase(mBarRect, mAnimator!!.getPhaseY())
    }

    override fun drawValues(c: Canvas?) {
        // if values are drawn
        if (isDrawingValuesAllowed(mChart!!)) {
            val dataSets: MutableList<IBarDataSet?>? = mChart!!.getBarData()!!.getDataSets()
            val valueOffsetPlus = convertDpToPixel(4.5f)
            var posOffset = 0f
            var negOffset = 0f
            val drawValueAboveBar = mChart!!.isDrawValueAboveBarEnabled()
            for (i in 0 until mChart!!.getBarData()!!.getDataSetCount()) {
                val dataSet = dataSets!![i]!!
                if (!shouldDrawValues(dataSet)) continue

                // apply the text-styling defined by the DataSet
                applyValueTextStyle(dataSet)
                val isInverted = mChart!!.isInverted(dataSet.getAxisDependency())

                // calculate the correct offset depending on the draw position of
                // the value
                val valueTextHeight = calcTextHeight(mValuePaint!!, "8").toFloat()
                posOffset =
                    if (drawValueAboveBar) -valueOffsetPlus else valueTextHeight + valueOffsetPlus
                negOffset =
                    if (drawValueAboveBar) valueTextHeight + valueOffsetPlus else -valueOffsetPlus
                if (isInverted) {
                    posOffset = -posOffset - valueTextHeight
                    negOffset = -negOffset - valueTextHeight
                }

                // get the buffer
                val buffer = mBarBuffers!![i]
                val phaseY = mAnimator!!.getPhaseY()
                val iconsOffset = getInstance(dataSet.getIconsOffset()!!)
                iconsOffset.x = convertDpToPixel(iconsOffset.x)
                iconsOffset.y = convertDpToPixel(iconsOffset.y)

                // if only single values are drawn (sum)
                if (!dataSet.isStacked()) {
                    var j = 0
                    while (j < buffer!!.buffer.size * mAnimator!!.getPhaseX()) {
                        val x = (buffer.buffer[j] + buffer.buffer[j + 2]) / 2f
                        if (!mViewPortHandler!!.isInBoundsRight(x)) break
                        if (!mViewPortHandler!!.isInBoundsY(buffer.buffer[j + 1])
                            || !mViewPortHandler!!.isInBoundsLeft(x)
                        ) {
                            j += 4
                            continue
                        }
                        val entry = dataSet.getEntryForIndex(j / 4)!!
                        val yValue = entry.getY()
                        if (dataSet.isDrawValuesEnabled()) {
                            drawValue(
                                c!!, dataSet.getValueFormatter()!!, yValue, entry, i, x,
                                if (yValue >= 0) buffer.buffer[j + 1] + posOffset else buffer.buffer[j + 3] + negOffset,
                                dataSet.getValueTextColor(j / 4)!!
                            )
                        }
                        if (entry.getIcon() != null && dataSet.isDrawIconsEnabled()) {
                            val icon = entry.getIcon()
                            var px = x
                            var py =
                                if (yValue >= 0) buffer.buffer[j + 1] + posOffset else buffer.buffer[j + 3] + negOffset
                            px += iconsOffset.x
                            py += iconsOffset.y
                            drawImage(
                                c!!,
                                icon!!, px.toInt(), py.toInt(),
                                icon.intrinsicWidth,
                                icon.intrinsicHeight
                            )
                        }
                        j += 4
                    }

                    // if we have stacks
                } else {
                    val trans = mChart?.getTransformer(dataSet.getAxisDependency())
                    var bufferIndex = 0
                    var index = 0
                    while (index < dataSet.getEntryCount() * mAnimator!!.getPhaseX()) {
                        val entry = dataSet.getEntryForIndex(index)!!
                        val vals = entry.getYVals()
                        val x = (buffer!!.buffer[bufferIndex] + buffer.buffer[bufferIndex + 2]) / 2f
                        val color = dataSet.getValueTextColor(index)!!

                        // we still draw stacked bars, but there is one
                        // non-stacked
                        // in between
                        if (vals == null) {
                            if (!mViewPortHandler!!.isInBoundsRight(x)) break
                            if (!mViewPortHandler!!.isInBoundsY(buffer.buffer[bufferIndex + 1])
                                || !mViewPortHandler!!.isInBoundsLeft(x)
                            ) continue
                            if (dataSet.isDrawValuesEnabled()) {
                                drawValue(
                                    c!!, dataSet.getValueFormatter()!!, entry.getY(), entry, i, x,
                                    buffer.buffer[bufferIndex + 1] +
                                            if (entry.getY() >= 0) posOffset else negOffset, color
                                )
                            }
                            if (entry.getIcon() != null && dataSet.isDrawIconsEnabled()) {
                                val icon = entry.getIcon()
                                var px = x
                                var py = buffer.buffer[bufferIndex + 1] +
                                        if (entry.getY() >= 0) posOffset else negOffset
                                px += iconsOffset.x
                                py += iconsOffset.y
                                drawImage(
                                    c!!,
                                    icon!!, px.toInt(), py.toInt(),
                                    icon.intrinsicWidth,
                                    icon.intrinsicHeight
                                )
                            }

                            // draw stack values
                        } else {
                            val transformed = FloatArray(vals.size * 2)
                            var posY = 0f
                            var negY = -entry.getNegativeSum()
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
                            trans?.pointValuesToPixel(transformed)
                            var k = 0
                            while (k < transformed.size) {
                                val `val` = vals[k / 2]
                                val drawBelow = `val` == 0.0f && negY == 0.0f && posY > 0.0f ||
                                        `val` < 0.0f
                                val y = (transformed[k + 1]
                                        + if (drawBelow) negOffset else posOffset)
                                if (!mViewPortHandler!!.isInBoundsRight(x)) break
                                if (!mViewPortHandler!!.isInBoundsY(y)
                                    || !mViewPortHandler!!.isInBoundsLeft(x)
                                ) {
                                    k += 2
                                    continue
                                }
                                if (dataSet.isDrawValuesEnabled()) {
                                    drawValue(
                                        c!!,
                                        dataSet.getValueFormatter()!!,
                                        vals[k / 2],
                                        entry,
                                        i,
                                        x,
                                        y,
                                        color
                                    )
                                }
                                if (entry.getIcon() != null && dataSet.isDrawIconsEnabled()) {
                                    val icon = entry.getIcon()
                                    drawImage(
                                        c!!,
                                        icon!!,
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

    override fun drawHighlighted(c: Canvas?, indices: Array<Highlight>?) {
        val barData = mChart!!.getBarData()
        for (high in indices!!) {
            val set = barData!!.getDataSetByIndex(high.getDataSetIndex())
            if (set == null || !set.isHighlightEnabled()) continue
            val e = set.getEntryForXValue(high.getX(), high.getY())
            if (!isInBoundsX(e, set)) continue
            val trans = mChart!!.getTransformer(set.getAxisDependency())!!
            mHighlightPaint!!.color = set.getHighLightColor()
            mHighlightPaint!!.alpha = set.getHighLightAlpha()
            val isStack = if (high.getStackIndex() >= 0 && e!!.isStacked()) true else false
            val y1: Float
            val y2: Float
            if (isStack) {
                if (mChart!!.isHighlightFullBarEnabled()) {
                    y1 = e!!.getPositiveSum()
                    y2 = -e.getNegativeSum()
                } else {
                    val range = e!!.getRanges()!![high.getStackIndex()]
                    y1 = range.from
                    y2 = range.to
                }
            } else {
                y1 = e!!.getY()
                y2 = 0f
            }
            prepareBarHighlight(e.getX(), y1, y2, barData.getBarWidth() / 2f, trans)
            setHighlightDrawPos(high, mBarRect)
            c!!.drawRect(mBarRect, mHighlightPaint!!)
        }
    }

    /**
     * Sets the drawing position of the highlight object based on the riven bar-rect.
     * @param high
     */
    protected open fun setHighlightDrawPos(high: Highlight, bar: RectF) {
        high.setDraw(bar.centerX(), bar.top)
    }

    override fun drawExtras(c: Canvas?) {}
}