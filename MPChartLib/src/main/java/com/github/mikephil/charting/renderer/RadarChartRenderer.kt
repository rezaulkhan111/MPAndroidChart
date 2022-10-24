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
import com.github.mikephil.charting.utils.ViewPortHandler
import com.github.mikephil.charting.renderer.scatter.IShapeRenderer
import com.github.mikephil.charting.utils.ColorTemplate
import com.github.mikephil.charting.components.AxisBase
import com.github.mikephil.charting.utils.MPPointD
import com.github.mikephil.charting.animation.ChartAnimator
import com.github.mikephil.charting.interfaces.dataprovider.ChartInterface
import com.github.mikephil.charting.interfaces.datasets.IDataSet
import com.github.mikephil.charting.formatter.IValueFormatter
import android.graphics.Paint.Align
import com.github.mikephil.charting.components.XAxis
import com.github.mikephil.charting.renderer.AxisRenderer
import com.github.mikephil.charting.utils.FSize
import com.github.mikephil.charting.utils.MPPointF
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
import com.github.mikephil.charting.utils.Fill
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
import com.github.mikephil.charting.highlight.Highlight

class RadarChartRenderer(
    protected var mChart: RadarChart, animator: ChartAnimator?,
    viewPortHandler: ViewPortHandler?
) : LineRadarRenderer(animator, viewPortHandler) {
    /**
     * paint for drawing the web
     */
    var webPaint: Paint
        protected set
    protected var mHighlightCirclePaint: Paint
    override fun initBuffers() {
        // TODO Auto-generated method stub
    }

    override fun drawData(c: Canvas) {
        val radarData = mChart.data
        val mostEntries = radarData.maxEntryCountSet.entryCount
        for (set in radarData.dataSets) {
            if (set.isVisible) {
                drawDataSet(c, set, mostEntries)
            }
        }
    }

    protected var mDrawDataSetSurfacePathBuffer = Path()

    /**
     * Draws the RadarDataSet
     *
     * @param c
     * @param dataSet
     * @param mostEntries the entry count of the dataset with the most entries
     */
    protected fun drawDataSet(c: Canvas, dataSet: IRadarDataSet, mostEntries: Int) {
        val phaseX = mAnimator.phaseX
        val phaseY = mAnimator.phaseY
        val sliceangle = mChart.sliceAngle

        // calculate the factor that is needed for transforming the value to
        // pixels
        val factor = mChart.factor
        val center = mChart.centerOffsets
        val pOut = MPPointF.getInstance(0, 0)
        val surface = mDrawDataSetSurfacePathBuffer
        surface.reset()
        var hasMovedToPoint = false
        for (j in 0 until dataSet.entryCount) {
            mRenderPaint.color = dataSet.getColor(j)
            val e = dataSet.getEntryForIndex(j)
            getPosition(
                center,
                (e.y - mChart.yChartMin) * factor * phaseY,
                sliceangle * j * phaseX + mChart.rotationAngle, pOut
            )
            if (java.lang.Float.isNaN(pOut.x)) continue
            if (!hasMovedToPoint) {
                surface.moveTo(pOut.x, pOut.y)
                hasMovedToPoint = true
            } else surface.lineTo(pOut.x, pOut.y)
        }
        if (dataSet.entryCount > mostEntries) {
            // if this is not the largest set, draw a line to the center before closing
            surface.lineTo(center.x, center.y)
        }
        surface.close()
        if (dataSet.isDrawFilledEnabled) {
            val drawable = dataSet.fillDrawable
            if (drawable != null) {
                drawFilledPath(c, surface, drawable)
            } else {
                drawFilledPath(c, surface, dataSet.fillColor, dataSet.fillAlpha)
            }
        }
        mRenderPaint.strokeWidth = dataSet.lineWidth
        mRenderPaint.style = Paint.Style.STROKE

        // draw the line (only if filled is disabled or alpha is below 255)
        if (!dataSet.isDrawFilledEnabled || dataSet.fillAlpha < 255) c.drawPath(
            surface,
            mRenderPaint
        )
        recycleInstance(center)
        recycleInstance(pOut)
    }

    override fun drawValues(c: Canvas) {
        val phaseX = mAnimator.phaseX
        val phaseY = mAnimator.phaseY
        val sliceangle = mChart.sliceAngle

        // calculate the factor that is needed for transforming the value to
        // pixels
        val factor = mChart.factor
        val center = mChart.centerOffsets
        val pOut = MPPointF.getInstance(0, 0)
        val pIcon = MPPointF.getInstance(0, 0)
        val yoffset = convertDpToPixel(5f)
        for (i in 0 until mChart.data.dataSetCount) {
            val dataSet = mChart.data.getDataSetByIndex(i)
            if (!shouldDrawValues(dataSet)) continue

            // apply the text-styling defined by the DataSet
            applyValueTextStyle(dataSet)
            val iconsOffset = MPPointF.getInstance(dataSet.iconsOffset)
            iconsOffset.x = convertDpToPixel(iconsOffset.x)
            iconsOffset.y = convertDpToPixel(iconsOffset.y)
            for (j in 0 until dataSet.entryCount) {
                val entry = dataSet.getEntryForIndex(j)
                getPosition(
                    center,
                    (entry.y - mChart.yChartMin) * factor * phaseY,
                    sliceangle * j * phaseX + mChart.rotationAngle,
                    pOut
                )
                if (dataSet.isDrawValuesEnabled) {
                    drawValue(
                        c,
                        dataSet.valueFormatter,
                        entry.y,
                        entry,
                        i,
                        pOut.x,
                        pOut.y - yoffset,
                        dataSet.getValueTextColor(j)
                    )
                }
                if (entry.icon != null && dataSet.isDrawIconsEnabled) {
                    val icon = entry.icon
                    getPosition(
                        center,
                        entry.y * factor * phaseY + iconsOffset.y,
                        sliceangle * j * phaseX + mChart.rotationAngle,
                        pIcon
                    )
                    pIcon.y += iconsOffset.x
                    drawImage(
                        c,
                        icon,
                        pIcon.x.toInt(),
                        pIcon.y.toInt(),
                        icon.intrinsicWidth,
                        icon.intrinsicHeight
                    )
                }
            }
            recycleInstance(iconsOffset)
        }
        recycleInstance(center)
        recycleInstance(pOut)
        recycleInstance(pIcon)
    }

    override fun drawExtras(c: Canvas) {
        drawWeb(c)
    }

    protected fun drawWeb(c: Canvas) {
        val sliceangle = mChart.sliceAngle

        // calculate the factor that is needed for transforming the value to
        // pixels
        val factor = mChart.factor
        val rotationangle = mChart.rotationAngle
        val center = mChart.centerOffsets

        // draw the web lines that come from the center
        webPaint.strokeWidth = mChart.webLineWidth
        webPaint.color = mChart.webColor
        webPaint.alpha = mChart.webAlpha
        val xIncrements = 1 + mChart.skipWebLineCount
        val maxEntryCount = mChart.data.maxEntryCountSet.entryCount
        val p = MPPointF.getInstance(0, 0)
        var i = 0
        while (i < maxEntryCount) {
            getPosition(
                center,
                mChart.yRange * factor,
                sliceangle * i + rotationangle,
                p
            )
            c.drawLine(center.x, center.y, p.x, p.y, webPaint)
            i += xIncrements
        }
        recycleInstance(p)

        // draw the inner-web
        webPaint.strokeWidth = mChart.webLineWidthInner
        webPaint.color = mChart.webColorInner
        webPaint.alpha = mChart.webAlpha
        val labelCount = mChart.yAxis.mEntryCount
        val p1out = MPPointF.getInstance(0, 0)
        val p2out = MPPointF.getInstance(0, 0)
        for (j in 0 until labelCount) {
            for (i in 0 until mChart.data.entryCount) {
                val r = (mChart.yAxis.mEntries[j] - mChart.yChartMin) * factor
                getPosition(center, r, sliceangle * i + rotationangle, p1out)
                getPosition(center, r, sliceangle * (i + 1) + rotationangle, p2out)
                c.drawLine(p1out.x, p1out.y, p2out.x, p2out.y, webPaint)
            }
        }
        recycleInstance(p1out)
        recycleInstance(p2out)
    }

    override fun drawHighlighted(c: Canvas, indices: Array<Highlight>) {
        val sliceangle = mChart.sliceAngle

        // calculate the factor that is needed for transforming the value to
        // pixels
        val factor = mChart.factor
        val center = mChart.centerOffsets
        val pOut = MPPointF.getInstance(0, 0)
        val radarData = mChart.data
        for (high in indices) {
            val set = radarData.getDataSetByIndex(high.dataSetIndex)
            if (set == null || !set.isHighlightEnabled) continue
            val e = set.getEntryForIndex(high.x.toInt())
            if (!isInBoundsX(e, set)) continue
            val y = e.y - mChart.yChartMin
            getPosition(
                center,
                y * factor * mAnimator.phaseY,
                sliceangle * high.x * mAnimator.phaseX + mChart.rotationAngle,
                pOut
            )
            high.setDraw(pOut.x, pOut.y)

            // draw the lines
            drawHighlightLines(c, pOut.x, pOut.y, set)
            if (set.isDrawHighlightCircleEnabled) {
                if (!java.lang.Float.isNaN(pOut.x) && !java.lang.Float.isNaN(pOut.y)) {
                    var strokeColor = set.highlightCircleStrokeColor
                    if (strokeColor == ColorTemplate.COLOR_NONE) {
                        strokeColor = set.getColor(0)
                    }
                    if (set.highlightCircleStrokeAlpha < 255) {
                        strokeColor = colorWithAlpha(strokeColor, set.highlightCircleStrokeAlpha)
                    }
                    drawHighlightCircle(
                        c,
                        pOut,
                        set.highlightCircleInnerRadius,
                        set.highlightCircleOuterRadius,
                        set.highlightCircleFillColor,
                        strokeColor,
                        set.highlightCircleStrokeWidth
                    )
                }
            }
        }
        recycleInstance(center)
        recycleInstance(pOut)
    }

    protected var mDrawHighlightCirclePathBuffer = Path()
    fun drawHighlightCircle(
        c: Canvas,
        point: MPPointF,
        innerRadius: Float,
        outerRadius: Float,
        fillColor: Int,
        strokeColor: Int,
        strokeWidth: Float
    ) {
        var innerRadius = innerRadius
        var outerRadius = outerRadius
        c.save()
        outerRadius = convertDpToPixel(outerRadius)
        innerRadius = convertDpToPixel(innerRadius)
        if (fillColor != ColorTemplate.COLOR_NONE) {
            val p = mDrawHighlightCirclePathBuffer
            p.reset()
            p.addCircle(point.x, point.y, outerRadius, Path.Direction.CW)
            if (innerRadius > 0f) {
                p.addCircle(point.x, point.y, innerRadius, Path.Direction.CCW)
            }
            mHighlightCirclePaint.color = fillColor
            mHighlightCirclePaint.style = Paint.Style.FILL
            c.drawPath(p, mHighlightCirclePaint)
        }
        if (strokeColor != ColorTemplate.COLOR_NONE) {
            mHighlightCirclePaint.color = strokeColor
            mHighlightCirclePaint.style = Paint.Style.STROKE
            mHighlightCirclePaint.strokeWidth =
                convertDpToPixel(strokeWidth)
            c.drawCircle(point.x, point.y, outerRadius, mHighlightCirclePaint)
        }
        c.restore()
    }

    init {
        mHighlightPaint = Paint(Paint.ANTI_ALIAS_FLAG)
        mHighlightPaint.style = Paint.Style.STROKE
        mHighlightPaint.strokeWidth = 2f
        mHighlightPaint.color = Color.rgb(255, 187, 115)
        webPaint = Paint(Paint.ANTI_ALIAS_FLAG)
        webPaint.style = Paint.Style.STROKE
        mHighlightCirclePaint = Paint(Paint.ANTI_ALIAS_FLAG)
    }
}