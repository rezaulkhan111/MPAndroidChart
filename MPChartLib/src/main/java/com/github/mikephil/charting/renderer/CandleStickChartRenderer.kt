package com.github.mikephil.charting.renderer

import android.graphics.*
import com.github.mikephil.charting.animation.ChartAnimator
import com.github.mikephil.charting.highlight.Highlight
import com.github.mikephil.charting.interfaces.dataprovider.CandleDataProvider
import com.github.mikephil.charting.interfaces.datasets.ICandleDataSet
import com.github.mikephil.charting.utils.*
import com.github.mikephil.charting.utils.MPPointF.Companion.getInstance
import com.github.mikephil.charting.utils.MPPointF.Companion.recycleInstance
import com.github.mikephil.charting.utils.Utils.convertDpToPixel
import com.github.mikephil.charting.utils.Utils.drawImage

class CandleStickChartRenderer : LineScatterCandleRadarRenderer {

    var mChart: CandleDataProvider? = null

    private val mShadowBuffers = FloatArray(8)
    private val mBodyBuffers = FloatArray(4)
    private val mRangeBuffers = FloatArray(4)
    private val mOpenBuffers = FloatArray(4)
    private val mCloseBuffers = FloatArray(4)

    constructor(
        chart: CandleDataProvider, animator: ChartAnimator,
        viewPortHandler: ViewPortHandler
    ) : super(animator, viewPortHandler) {
        mChart = chart
    }

    override fun initBuffers() {}

    override fun drawData(c: Canvas?) {
        val candleData = mChart!!.getCandleData()
        for (set in candleData!!.getDataSets()!!) {
            if (set!!.isVisible()) drawDataSet(c!!, set)
        }
    }

    protected fun drawDataSet(c: Canvas, dataSet: ICandleDataSet) {
        val trans = mChart!!.getTransformer(dataSet.getAxisDependency())!!
        val phaseY = mAnimator!!.getPhaseY()
        val barSpace = dataSet.getBarSpace()
        val showCandleBar = dataSet.getShowCandleBar()
        mXBounds[mChart!!] = dataSet
        mRenderPaint!!.strokeWidth = dataSet.getShadowWidth()

        // draw the body
        for (j in mXBounds.min..mXBounds.range + mXBounds.min) {

            // get the entry
            val e = dataSet.getEntryForIndex(j) ?: continue
            val xPos = e.getX()
            val open = e.getOpen()
            val close = e.getClose()
            val high = e.getHigh()
            val low = e.getLow()
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
                if (dataSet.getShadowColorSameAsCandle()) {
                    if (open > close) mRenderPaint!!.color =
                        if (dataSet.getDecreasingColor() == ColorTemplate.COLOR_NONE) dataSet.getColor(
                            j
                        ) else dataSet.getDecreasingColor() else if (open < close) mRenderPaint!!.color =
                        if (dataSet.getIncreasingColor() == ColorTemplate.COLOR_NONE) dataSet.getColor(
                            j
                        ) else dataSet.getIncreasingColor() else mRenderPaint!!.color =
                        if (dataSet.getNeutralColor() == ColorTemplate.COLOR_NONE) dataSet.getColor(
                            j
                        ) else dataSet.getNeutralColor()
                } else {
                    mRenderPaint!!.color =
                        if (dataSet.getShadowColor() == ColorTemplate.COLOR_NONE) dataSet.getColor(j) else dataSet.getShadowColor()
                }
                mRenderPaint!!.style = Paint.Style.STROKE
                c.drawLines(mShadowBuffers, mRenderPaint!!)

                // calculate the body
                mBodyBuffers[0] = xPos - 0.5f + barSpace
                mBodyBuffers[1] = close * phaseY
                mBodyBuffers[2] = xPos + 0.5f - barSpace
                mBodyBuffers[3] = open * phaseY
                trans.pointValuesToPixel(mBodyBuffers)

                // draw body differently for increasing and decreasing entry
                if (open > close) { // decreasing
                    if (dataSet.getDecreasingColor() == ColorTemplate.COLOR_NONE) {
                        mRenderPaint?.setColor(dataSet.getColor(j))
                    } else {
                        mRenderPaint?.setColor(dataSet.getDecreasingColor())
                    }
                    mRenderPaint?.setStyle(dataSet.getDecreasingPaintStyle())
                    c.drawRect(
                        mBodyBuffers[0], mBodyBuffers[3],
                        mBodyBuffers[2], mBodyBuffers[1],
                        mRenderPaint!!
                    )
                } else if (open < close) {
                    if (dataSet.getIncreasingColor() == ColorTemplate.COLOR_NONE) {
                        mRenderPaint?.setColor(dataSet.getColor(j))
                    } else {
                        mRenderPaint?.setColor(dataSet.getIncreasingColor())
                    }
                    mRenderPaint?.setStyle(dataSet.getIncreasingPaintStyle())
                    c.drawRect(
                        mBodyBuffers[0], mBodyBuffers[1],
                        mBodyBuffers[2], mBodyBuffers[3],
                        mRenderPaint!!
                    )
                } else { // equal values
                    if (dataSet.getNeutralColor() == ColorTemplate.COLOR_NONE) {
                        mRenderPaint?.setColor(dataSet.getColor(j))
                    } else {
                        mRenderPaint?.setColor(dataSet.getNeutralColor())
                    }
                    c.drawLine(
                        mBodyBuffers[0], mBodyBuffers[1],
                        mBodyBuffers[2], mBodyBuffers[3],
                        mRenderPaint!!
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
                    if (open > close) if (dataSet.getDecreasingColor() == ColorTemplate.COLOR_NONE) dataSet.getColor(
                        j
                    ) else dataSet.getDecreasingColor() else if (open < close) if (dataSet.getIncreasingColor() == ColorTemplate.COLOR_NONE) dataSet.getColor(
                        j
                    ) else dataSet.getIncreasingColor() else if (dataSet.getNeutralColor() == ColorTemplate.COLOR_NONE) dataSet.getColor(
                        j
                    ) else dataSet.getNeutralColor()
                mRenderPaint?.setColor(barColor)
                c.drawLine(
                    mRangeBuffers[0], mRangeBuffers[1],
                    mRangeBuffers[2], mRangeBuffers[3],
                    mRenderPaint!!
                )
                c.drawLine(
                    mOpenBuffers[0], mOpenBuffers[1],
                    mOpenBuffers[2], mOpenBuffers[3],
                    mRenderPaint!!
                )
                c.drawLine(
                    mCloseBuffers[0], mCloseBuffers[1],
                    mCloseBuffers[2], mCloseBuffers[3],
                    mRenderPaint!!
                )
            }
        }
    }

    override fun drawValues(c: Canvas?) {
        // if values are drawn
        if (isDrawingValuesAllowed(mChart!!)) {
            val dataSets: MutableList<ICandleDataSet?>? = mChart!!.getCandleData()!!.getDataSets()
            for (i in dataSets!!.indices) {
                val dataSet = dataSets[i]
                if (!shouldDrawValues(dataSet!!) || dataSet.getEntryCount() < 1) continue

                // apply the text-styling defined by the DataSet
                applyValueTextStyle(dataSet)
                val trans = mChart!!.getTransformer(dataSet.getAxisDependency())!!
                mXBounds[mChart!!] = dataSet
                val positions = trans.generateTransformedValuesCandle(
                    dataSet,
                    mAnimator!!.getPhaseX(),
                    mAnimator!!.getPhaseY(),
                    mXBounds.min,
                    mXBounds.max
                )
                val yOffset = convertDpToPixel(5f)
                val iconsOffset = getInstance(dataSet.getIconsOffset()!!)
                iconsOffset.x = convertDpToPixel(iconsOffset.x)
                iconsOffset.y = convertDpToPixel(iconsOffset.y)
                var j = 0
                while (j < positions!!.size) {
                    val x = positions[j]
                    val y = positions[j + 1]
                    if (!mViewPortHandler!!.isInBoundsRight(x)) break
                    if (!mViewPortHandler!!.isInBoundsLeft(x) || !mViewPortHandler!!.isInBoundsY(y)) {
                        j += 2
                        continue
                    }
                    val entry = dataSet.getEntryForIndex(j / 2 + mXBounds.min)!!
                    if (dataSet.isDrawValuesEnabled()) {
                        drawValue(
                            c!!,
                            dataSet.getValueFormatter()!!,
                            entry.getHigh(),
                            entry,
                            i,
                            x,
                            y - yOffset,
                            dataSet.getValueTextColor(j / 2)!!
                        )
                    }
                    if (entry.getIcon() != null && dataSet.isDrawIconsEnabled()) {
                        val icon = entry.getIcon()
                        drawImage(
                            c!!,
                            icon!!, (x + iconsOffset.x).toInt(), (y + iconsOffset.y).toInt(),
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

    override fun drawExtras(c: Canvas?) {}

    override fun drawHighlighted(c: Canvas?, indices: Array<Highlight>?) {
        val candleData = mChart!!.getCandleData()
        for (high in indices!!) {
            val set = candleData!!.getDataSetByIndex(high.getDataSetIndex())
            if (set == null || !set.isHighlightEnabled()) continue
            val e = set.getEntryForXValue(high.getX(), high.getY())
            if (!isInBoundsX(e, set)) continue
            val lowValue = e!!.getLow() * mAnimator?.getPhaseY()!!
            val highValue = e.getHigh() * mAnimator?.getPhaseY()!!
            val y = (lowValue + highValue) / 2f
            val pix =
                mChart!!.getTransformer(set.getAxisDependency())!!.getPixelForValues(e.getX(), y)
            high.setDraw(pix!!.x.toFloat(), pix.y.toFloat())

            // draw the lines
            drawHighlightLines(c!!, pix.x.toFloat(), pix.y.toFloat(), set)
        }
    }
}