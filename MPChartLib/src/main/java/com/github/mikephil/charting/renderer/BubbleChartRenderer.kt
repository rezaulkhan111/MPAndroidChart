package com.github.mikephil.charting.renderer

import android.graphics.*
import com.github.mikephil.charting.animation.ChartAnimator
import com.github.mikephil.charting.highlight.Highlight
import com.github.mikephil.charting.interfaces.dataprovider.BubbleDataProvider
import com.github.mikephil.charting.interfaces.datasets.IBubbleDataSet
import com.github.mikephil.charting.utils.*
import com.github.mikephil.charting.utils.Utils.calcTextHeight

/**
 * Bubble chart implementation: Copyright 2015 Pierre-Marc Airoldi Licensed
 * under Apache License 2.0 Ported by Daniel Cohen Gindi
 */
class BubbleChartRenderer : BarLineScatterCandleBubbleRenderer {

    var mChart: BubbleDataProvider? = null

    constructor(
        chart: BubbleDataProvider?, animator: ChartAnimator,
        viewPortHandler: ViewPortHandler?
    ) : super(animator, viewPortHandler) {
        mChart = chart
        mRenderPaint?.style = Paint.Style.FILL
        mHighlightPaint?.style = Paint.Style.STROKE
        mHighlightPaint?.strokeWidth = Utils.convertDpToPixel(1.5f)
    }

    override fun initBuffers() {}

    override fun drawData(c: Canvas?) {
        val bubbleData = mChart!!.getBubbleData()
        for (set in bubbleData.getDataSets()!!) {
            if (set.isVisible()) drawDataSet(c!!, set)
        }
    }

    private val sizeBuffer = FloatArray(4)
    private val pointBuffer = FloatArray(2)

    protected fun getShapeSize(
        entrySize: Float,
        maxSize: Float,
        reference: Float,
        normalizeSize: Boolean
    ): Float {
        val factor =
            if (normalizeSize) if (maxSize == 0f) 1f else Math.sqrt((entrySize / maxSize).toDouble())
                .toFloat() else entrySize
        return reference * factor
    }

    protected fun drawDataSet(c: Canvas, dataSet: IBubbleDataSet) {
        if (dataSet.getEntryCount() < 1) return
        val trans = mChart!!.getTransformer(dataSet.getAxisDependency())
        val phaseY = mAnimator?.getPhaseY()
        mXBounds[mChart!!] = dataSet
        sizeBuffer[0] = 0f
        sizeBuffer[2] = 1f
        trans.pointValuesToPixel(sizeBuffer)
        val normalizeSize = dataSet.isNormalizeSizeEnabled()

        // calcualte the full width of 1 step on the x-axis
        val maxBubbleWidth = Math.abs(sizeBuffer[2] - sizeBuffer[0])
        val maxBubbleHeight =
            Math.abs(mViewPortHandler!!.contentBottom() - mViewPortHandler!!.contentTop())
        val referenceSize = Math.min(maxBubbleHeight, maxBubbleWidth)
        for (j in mXBounds.min..mXBounds.range + mXBounds.min) {
            val entry = dataSet.getEntryForIndex(j)
            pointBuffer[0] = entry.getX()
            pointBuffer[1] = entry.getY() * phaseY!!
            trans.pointValuesToPixel(pointBuffer)
            val shapeHalf = getShapeSize(
                entry.getSize(),
                dataSet.getMaxSize(),
                referenceSize,
                normalizeSize
            ) / 2f
            if (!mViewPortHandler!!.isInBoundsTop(pointBuffer[1] + shapeHalf)
                || !mViewPortHandler!!.isInBoundsBottom(pointBuffer[1] - shapeHalf)
            ) continue
            if (!mViewPortHandler!!.isInBoundsLeft(pointBuffer[0] + shapeHalf)) continue
            if (!mViewPortHandler!!.isInBoundsRight(pointBuffer[0] - shapeHalf)) break
            val color = dataSet.getColor(j)
            mRenderPaint!!.color = color
            c.drawCircle(pointBuffer[0], pointBuffer[1], shapeHalf, mRenderPaint!!)
        }
    }

    override fun drawValues(c: Canvas?) {
        val bubbleData = mChart!!.getBubbleData() ?: return
        // if values are drawn
        if (isDrawingValuesAllowed(mChart!!)) {
            val dataSets: List<IBubbleDataSet>? = bubbleData.getDataSets()
            val lineHeight = calcTextHeight(mValuePaint!!, "1").toFloat()
            for (i in dataSets!!.indices) {
                val dataSet = dataSets[i]
                if (!shouldDrawValues(dataSet) || dataSet.getEntryCount() < 1) continue

                // apply the text-styling defined by the DataSet
                applyValueTextStyle(dataSet)
                val phaseX = Math.max(0f, Math.min(1f, mAnimator!!.getPhaseX()))
                val phaseY = mAnimator!!.getPhaseY()
                mXBounds[mChart!!] = dataSet
                val positions = mChart!!.getTransformer(dataSet.getAxisDependency())
                    .generateTransformedValuesBubble(dataSet, phaseY, mXBounds.min, mXBounds.max)
                val alpha = if (phaseX == 1f) phaseY else phaseX
                val iconsOffset = MPPointF.getInstance(dataSet.getIconsOffset())
                iconsOffset.x = Utils.convertDpToPixel(iconsOffset.x)
                iconsOffset.y = Utils.convertDpToPixel(iconsOffset.y)
                var j = 0
                while (j < positions!!.size) {
                    var valueTextColor = dataSet.getValueTextColor(j / 2 + mXBounds.min)
                    valueTextColor = Color.argb(
                        Math.round(255f * alpha), Color.red(valueTextColor),
                        Color.green(valueTextColor), Color.blue(valueTextColor)
                    )
                    val x = positions[j]
                    val y = positions[j + 1]
                    if (!mViewPortHandler!!.isInBoundsRight(x)) break
                    if (!mViewPortHandler!!.isInBoundsLeft(x) || !mViewPortHandler!!.isInBoundsY(y)) {
                        j += 2
                        continue
                    }
                    val entry = dataSet.getEntryForIndex(j / 2 + mXBounds.min)
                    if (dataSet.isDrawValuesEnabled()) {
                        drawValue(
                            c!!, dataSet.getValueFormatter(), entry.getSize(), entry, i, x,
                            y + 0.5f * lineHeight, valueTextColor
                        )
                    }
                    if (entry.getIcon() != null && dataSet.isDrawIconsEnabled()) {
                        val icon = entry.getIcon()
                        Utils.drawImage(
                            c!!,
                            icon!!, (x + iconsOffset.x).toInt(), (y + iconsOffset.y).toInt(),
                            icon.intrinsicWidth,
                            icon.intrinsicHeight
                        )
                    }
                    j += 2
                }
                MPPointF.recycleInstance(iconsOffset)
            }
        }
    }

    override fun drawExtras(c: Canvas?) {}

    private val _hsvBuffer = FloatArray(3)

    override fun drawHighlighted(c: Canvas?, indices: Array<Highlight>?) {
        val bubbleData = mChart!!.getBubbleData()
        val phaseY = mAnimator!!.getPhaseY()
        for (high in indices!!) {
            val set = bubbleData.getDataSetByIndex(high.getDataSetIndex())
            if (set == null || !set.isHighlightEnabled()) continue
            val entry = set.getEntryForXValue(high.getX(), high.getY())
            if (entry!!.getY() != high.getY()) continue
            if (!isInBoundsX(entry, set)) continue
            val trans = mChart!!.getTransformer(set.getAxisDependency())
            sizeBuffer[0] = 0f
            sizeBuffer[2] = 1f
            trans.pointValuesToPixel(sizeBuffer)
            val normalizeSize = set.isNormalizeSizeEnabled()

            // calcualte the full width of 1 step on the x-axis
            val maxBubbleWidth = Math.abs(sizeBuffer[2] - sizeBuffer[0])
            val maxBubbleHeight = Math.abs(
                mViewPortHandler!!.contentBottom() - mViewPortHandler!!.contentTop()
            )
            val referenceSize = Math.min(maxBubbleHeight, maxBubbleWidth)
            pointBuffer[0] = entry.getX()
            pointBuffer[1] = entry.getY() * phaseY
            trans.pointValuesToPixel(pointBuffer)
            high.setDraw(pointBuffer[0], pointBuffer[1])
            val shapeHalf = getShapeSize(
                entry.getSize(),
                set.getMaxSize(),
                referenceSize,
                normalizeSize
            ) / 2f
            if (!mViewPortHandler!!.isInBoundsTop(pointBuffer[1] + shapeHalf)
                || !mViewPortHandler!!.isInBoundsBottom(pointBuffer[1] - shapeHalf)
            ) continue
            if (!mViewPortHandler!!.isInBoundsLeft(pointBuffer[0] + shapeHalf)) continue
            if (!mViewPortHandler!!.isInBoundsRight(pointBuffer[0] - shapeHalf)) break
            val originalColor = set.getColor(entry.getX().toInt())
            Color.RGBToHSV(
                Color.red(originalColor), Color.green(originalColor),
                Color.blue(originalColor), _hsvBuffer
            )
            _hsvBuffer[2] *= 0.5f
            val color = Color.HSVToColor(Color.alpha(originalColor), _hsvBuffer)
            mHighlightPaint!!.color = color
            mHighlightPaint!!.strokeWidth = set.getHighlightCircleWidth()
            c!!.drawCircle(pointBuffer[0], pointBuffer[1], shapeHalf, mHighlightPaint!!)
        }
    }
}