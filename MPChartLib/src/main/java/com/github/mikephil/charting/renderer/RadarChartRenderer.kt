package com.github.mikephil.charting.renderer

import android.graphics.*
import com.github.mikephil.charting.animation.ChartAnimator
import com.github.mikephil.charting.charts.RadarChart
import com.github.mikephil.charting.highlight.Highlight
import com.github.mikephil.charting.interfaces.datasets.IRadarDataSet
import com.github.mikephil.charting.utils.ColorTemplate
import com.github.mikephil.charting.utils.ColorTemplate.colorWithAlpha
import com.github.mikephil.charting.utils.FSize.Companion.recycleInstance
import com.github.mikephil.charting.utils.MPPointD.Companion.recycleInstance
import com.github.mikephil.charting.utils.MPPointF
import com.github.mikephil.charting.utils.MPPointF.Companion.recycleInstance
import com.github.mikephil.charting.utils.Utils.convertDpToPixel
import com.github.mikephil.charting.utils.Utils.drawImage
import com.github.mikephil.charting.utils.Utils.getPosition
import com.github.mikephil.charting.utils.ViewPortHandler
import com.github.mikephil.charting.utils.ViewPortHandler.isInBoundsX

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