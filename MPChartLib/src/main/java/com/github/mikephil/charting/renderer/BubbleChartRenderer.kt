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
 * Bubble chart implementation: Copyright 2015 Pierre-Marc Airoldi Licensed
 * under Apache License 2.0 Ported by Daniel Cohen Gindi
 */
class BubbleChartRenderer(
    var mChart: BubbleDataProvider, animator: ChartAnimator?,
    viewPortHandler: ViewPortHandler?
) : BarLineScatterCandleBubbleRenderer(animator, viewPortHandler) {
    override fun initBuffers() {}
    override fun drawData(c: Canvas) {
        val bubbleData = mChart.bubbleData
        for (set in bubbleData.dataSets) {
            if (set.isVisible) drawDataSet(c, set)
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
        if (dataSet.entryCount < 1) return
        val trans = mChart.getTransformer(dataSet.axisDependency)
        val phaseY = mAnimator.phaseY
        mXBounds[mChart] = dataSet
        sizeBuffer[0] = 0f
        sizeBuffer[2] = 1f
        trans.pointValuesToPixel(sizeBuffer)
        val normalizeSize = dataSet.isNormalizeSizeEnabled

        // calcualte the full width of 1 step on the x-axis
        val maxBubbleWidth = Math.abs(sizeBuffer[2] - sizeBuffer[0])
        val maxBubbleHeight =
            Math.abs(mViewPortHandler.contentBottom() - mViewPortHandler.contentTop())
        val referenceSize = Math.min(maxBubbleHeight, maxBubbleWidth)
        for (j in mXBounds.min..mXBounds.range + mXBounds.min) {
            val entry = dataSet.getEntryForIndex(j)
            pointBuffer[0] = entry.x
            pointBuffer[1] = entry.y * phaseY
            trans.pointValuesToPixel(pointBuffer)
            val shapeHalf =
                getShapeSize(entry.size, dataSet.maxSize, referenceSize, normalizeSize) / 2f
            if (!mViewPortHandler.isInBoundsTop(pointBuffer[1] + shapeHalf)
                || !mViewPortHandler.isInBoundsBottom(pointBuffer[1] - shapeHalf)
            ) continue
            if (!mViewPortHandler.isInBoundsLeft(pointBuffer[0] + shapeHalf)) continue
            if (!mViewPortHandler.isInBoundsRight(pointBuffer[0] - shapeHalf)) break
            val color = dataSet.getColor(j)
            mRenderPaint.color = color
            c.drawCircle(pointBuffer[0], pointBuffer[1], shapeHalf, mRenderPaint)
        }
    }

    override fun drawValues(c: Canvas) {
        val bubbleData = mChart.bubbleData ?: return

        // if values are drawn
        if (isDrawingValuesAllowed(mChart)) {
            val dataSets = bubbleData.dataSets
            val lineHeight = calcTextHeight(mValuePaint, "1").toFloat()
            for (i in dataSets.indices) {
                val dataSet = dataSets[i]
                if (!shouldDrawValues(dataSet) || dataSet.entryCount < 1) continue

                // apply the text-styling defined by the DataSet
                applyValueTextStyle(dataSet)
                val phaseX = Math.max(0f, Math.min(1f, mAnimator.phaseX))
                val phaseY = mAnimator.phaseY
                mXBounds[mChart] = dataSet
                val positions = mChart.getTransformer(dataSet.axisDependency)
                    .generateTransformedValuesBubble(dataSet, phaseY, mXBounds.min, mXBounds.max)
                val alpha = if (phaseX == 1f) phaseY else phaseX
                val iconsOffset = MPPointF.getInstance(dataSet.iconsOffset)
                iconsOffset.x = Utils.convertDpToPixel(iconsOffset.x)
                iconsOffset.y = Utils.convertDpToPixel(iconsOffset.y)
                var j = 0
                while (j < positions.size) {
                    var valueTextColor = dataSet.getValueTextColor(j / 2 + mXBounds.min)
                    valueTextColor = Color.argb(
                        Math.round(255f * alpha), Color.red(valueTextColor),
                        Color.green(valueTextColor), Color.blue(valueTextColor)
                    )
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
                            c, dataSet.valueFormatter, entry.size, entry, i, x,
                            y + 0.5f * lineHeight, valueTextColor
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
    private val _hsvBuffer = FloatArray(3)
    override fun drawHighlighted(c: Canvas, indices: Array<Highlight>) {
        val bubbleData = mChart.bubbleData
        val phaseY = mAnimator.phaseY
        for (high in indices) {
            val set = bubbleData.getDataSetByIndex(high.dataSetIndex)
            if (set == null || !set.isHighlightEnabled) continue
            val entry = set.getEntryForXValue(high.x, high.y)
            if (entry.y != high.y) continue
            if (!isInBoundsX(entry, set)) continue
            val trans = mChart.getTransformer(set.axisDependency)
            sizeBuffer[0] = 0f
            sizeBuffer[2] = 1f
            trans.pointValuesToPixel(sizeBuffer)
            val normalizeSize = set.isNormalizeSizeEnabled

            // calcualte the full width of 1 step on the x-axis
            val maxBubbleWidth = Math.abs(sizeBuffer[2] - sizeBuffer[0])
            val maxBubbleHeight = Math.abs(
                mViewPortHandler.contentBottom() - mViewPortHandler.contentTop()
            )
            val referenceSize = Math.min(maxBubbleHeight, maxBubbleWidth)
            pointBuffer[0] = entry.x
            pointBuffer[1] = entry.y * phaseY
            trans.pointValuesToPixel(pointBuffer)
            high.setDraw(pointBuffer[0], pointBuffer[1])
            val shapeHalf = getShapeSize(
                entry.size,
                set.maxSize,
                referenceSize,
                normalizeSize
            ) / 2f
            if (!mViewPortHandler.isInBoundsTop(pointBuffer[1] + shapeHalf)
                || !mViewPortHandler.isInBoundsBottom(pointBuffer[1] - shapeHalf)
            ) continue
            if (!mViewPortHandler.isInBoundsLeft(pointBuffer[0] + shapeHalf)) continue
            if (!mViewPortHandler.isInBoundsRight(pointBuffer[0] - shapeHalf)) break
            val originalColor = set.getColor(entry.x.toInt())
            Color.RGBToHSV(
                Color.red(originalColor), Color.green(originalColor),
                Color.blue(originalColor), _hsvBuffer
            )
            _hsvBuffer[2] *= 0.5f
            val color = Color.HSVToColor(Color.alpha(originalColor), _hsvBuffer)
            mHighlightPaint.color = color
            mHighlightPaint.strokeWidth = set.highlightCircleWidth
            c.drawCircle(pointBuffer[0], pointBuffer[1], shapeHalf, mHighlightPaint)
        }
    }

    init {
        mRenderPaint.style = Paint.Style.FILL
        mHighlightPaint.style = Paint.Style.STROKE
        mHighlightPaint.strokeWidth = Utils.convertDpToPixel(1.5f)
    }
}