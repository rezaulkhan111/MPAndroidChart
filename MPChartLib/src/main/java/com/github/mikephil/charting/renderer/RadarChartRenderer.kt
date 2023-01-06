package com.github.mikephil.charting.renderer

import android.graphics.*
import com.github.mikephil.charting.animation.ChartAnimator
import com.github.mikephil.charting.charts.RadarChart
import com.github.mikephil.charting.data.RadarData
import com.github.mikephil.charting.highlight.Highlight
import com.github.mikephil.charting.interfaces.datasets.IRadarDataSet
import com.github.mikephil.charting.utils.ColorTemplate
import com.github.mikephil.charting.utils.ColorTemplate.colorWithAlpha
import com.github.mikephil.charting.utils.MPPointF
import com.github.mikephil.charting.utils.MPPointF.Companion.getInstance
import com.github.mikephil.charting.utils.MPPointF.Companion.recycleInstance
import com.github.mikephil.charting.utils.Utils.convertDpToPixel
import com.github.mikephil.charting.utils.Utils.drawImage
import com.github.mikephil.charting.utils.Utils.getPosition
import com.github.mikephil.charting.utils.ViewPortHandler

class RadarChartRenderer : LineRadarRenderer {
    protected var mChart: RadarChart? = null

    /**
     * paint for drawing the web
     */
    protected var mWebPaint: Paint? = null
    protected var mHighlightCirclePaint: Paint? = null

    constructor(
        chart: RadarChart,
        animator: ChartAnimator,
        viewPortHandler: ViewPortHandler
    ) : super(animator, viewPortHandler) {
        mChart = chart
        mHighlightPaint = Paint(Paint.ANTI_ALIAS_FLAG)
        mHighlightPaint!!.style = Paint.Style.STROKE
        mHighlightPaint!!.strokeWidth = 2f
        mHighlightPaint!!.color = Color.rgb(255, 187, 115)
        mWebPaint = Paint(Paint.ANTI_ALIAS_FLAG)
        mWebPaint!!.style = Paint.Style.STROKE
        mHighlightCirclePaint = Paint(Paint.ANTI_ALIAS_FLAG)
    }

    fun getWebPaint(): Paint? {
        return mWebPaint
    }

    override fun initBuffers() {
        // TODO Auto-generated method stub
    }

    override fun drawData(c: Canvas?) {
        val radarData: RadarData? = mChart!!.getData()
        val mostEntries = radarData!!.getMaxEntryCountSet()!!.getEntryCount()
        for (set in radarData.getDataSets()!!) {
            if (set!!.isVisible()) {
                drawDataSet(c!!, set, mostEntries)
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
        val phaseX = mAnimator!!.getPhaseX()
        val phaseY = mAnimator!!.getPhaseY()
        val sliceangle = mChart!!.getSliceAngle()

        // calculate the factor that is needed for transforming the value to
        // pixels
        val factor = mChart!!.getFactor()
        val center = mChart!!.getCenterOffsets()
        val pOut = getInstance(0f, 0f)
        val surface = mDrawDataSetSurfacePathBuffer
        surface.reset()
        var hasMovedToPoint = false
        for (j in 0 until dataSet.getEntryCount()) {
            mRenderPaint!!.color = dataSet.getColor(j)
            val e = dataSet.getEntryForIndex(j)!!
            getPosition(
                center,
                (e.getY() - mChart!!.getYChartMin()) * factor * phaseY,
                sliceangle * j * phaseX + mChart!!.getRotationAngle(), pOut
            )
            if (java.lang.Float.isNaN(pOut.x)) continue
            if (!hasMovedToPoint) {
                surface.moveTo(pOut.x, pOut.y)
                hasMovedToPoint = true
            } else surface.lineTo(pOut.x, pOut.y)
        }
        if (dataSet.getEntryCount() > mostEntries) {
            // if this is not the largest set, draw a line to the center before closing
            surface.lineTo(center!!.x, center.y)
        }
        surface.close()
        if (dataSet.isDrawFilledEnabled()) {
            val drawable = dataSet.getFillDrawable()
            if (drawable != null) {
                drawFilledPath(c, surface, drawable)
            } else {
                drawFilledPath(c, surface, dataSet.getFillColor(), dataSet.getFillAlpha())
            }
        }
        mRenderPaint!!.strokeWidth = dataSet.getLineWidth()
        mRenderPaint!!.style = Paint.Style.STROKE

        // draw the line (only if filled is disabled or alpha is below 255)
        if (!dataSet.isDrawFilledEnabled() || dataSet.getFillAlpha() < 255) c.drawPath(
            surface,
            mRenderPaint!!
        )
        recycleInstance(center!!)
        recycleInstance(pOut)
    }

    override fun drawValues(c: Canvas?) {
        val phaseX = mAnimator!!.getPhaseX()
        val phaseY = mAnimator!!.getPhaseY()
        val sliceangle = mChart!!.getSliceAngle()

        // calculate the factor that is needed for transforming the value to
        // pixels
        val factor = mChart!!.getFactor()
        val center = mChart!!.getCenterOffsets()
        val pOut = getInstance(0f, 0f)
        val pIcon = getInstance(0f, 0f)
        val yoffset = convertDpToPixel(5f)
        for (i in 0 until mChart!!.getData()!!.getDataSetCount()) {
            val dataSet: IRadarDataSet = mChart!!.getData()!!.getDataSetByIndex(i)!!
            if (!shouldDrawValues(dataSet)) continue

            // apply the text-styling defined by the DataSet
            applyValueTextStyle(dataSet)
            val iconsOffset = getInstance(dataSet.getIconsOffset()!!)
            iconsOffset.x = convertDpToPixel(iconsOffset.x)
            iconsOffset.y = convertDpToPixel(iconsOffset.y)
            for (j in 0 until dataSet.getEntryCount()) {
                val entry = dataSet.getEntryForIndex(j)!!
                getPosition(
                    center,
                    (entry.getY() - mChart!!.getYChartMin()) * factor * phaseY,
                    sliceangle * j * phaseX + mChart!!.getRotationAngle(),
                    pOut
                )
                if (dataSet.isDrawValuesEnabled()) {
                    drawValue(
                        c!!,
                        dataSet.getValueFormatter()!!,
                        entry.getY(),
                        entry,
                        i,
                        pOut.x,
                        pOut.y - yoffset,
                        dataSet.getValueTextColor(j)!!
                    )
                }
                if (entry.getIcon() != null && dataSet.isDrawIconsEnabled()) {
                    val icon = entry.getIcon()
                    getPosition(
                        center,
                        entry.getY() * factor * phaseY + iconsOffset.y,
                        sliceangle * j * phaseX + mChart!!.getRotationAngle(),
                        pIcon
                    )
                    pIcon.y += iconsOffset.x
                    drawImage(
                        c!!,
                        icon!!,
                        pIcon.x.toInt(),
                        pIcon.y.toInt(),
                        icon.intrinsicWidth,
                        icon.intrinsicHeight
                    )
                }
            }
            recycleInstance(iconsOffset)
        }
        recycleInstance(center!!)
        recycleInstance(pOut)
        recycleInstance(pIcon)
    }

    override fun drawExtras(c: Canvas?) {
        drawWeb(c!!)
    }

    protected fun drawWeb(c: Canvas) {
        val sliceangle = mChart!!.getSliceAngle()

        // calculate the factor that is needed for transforming the value to
        // pixels
        val factor = mChart!!.getFactor()
        val rotationangle = mChart!!.getRotationAngle()
        val center = mChart!!.getCenterOffsets()

        // draw the web lines that come from the center
        mWebPaint!!.strokeWidth = mChart!!.getWebLineWidth()
        mWebPaint!!.color = mChart!!.getWebColor()
        mWebPaint!!.alpha = mChart!!.getWebAlpha()
        val xIncrements = 1 + mChart!!.getSkipWebLineCount()
        val maxEntryCount: Int = mChart!!.getData()!!.getMaxEntryCountSet()!!.getEntryCount()
        val p = getInstance(0f, 0f)
        var i = 0
        while (i < maxEntryCount) {
            getPosition(
                center,
                mChart!!.getYRange() * factor,
                sliceangle * i + rotationangle,
                p
            )
            c.drawLine(center.x, center.y, p.x, p.y, mWebPaint!!)
            i += xIncrements
        }
        recycleInstance(p)

        // draw the inner-web
        mWebPaint!!.strokeWidth = mChart!!.getWebLineWidthInner()
        mWebPaint!!.color = mChart!!.getWebColorInner()
        mWebPaint!!.alpha = mChart!!.getWebAlpha()
        val labelCount = mChart!!.getYAxis()!!.mEntryCount
        val p1out = getInstance(0f, 0f)
        val p2out = getInstance(0f, 0f)
        for (j in 0 until labelCount) {
            for (i in 0 until mChart!!.getData()!!.getEntryCount()) {
                val r = (mChart!!.getYAxis()!!.mEntries[j] - mChart!!.getYChartMin()) * factor
                getPosition(center, r, sliceangle * i + rotationangle, p1out)
                getPosition(center, r, sliceangle * (i + 1) + rotationangle, p2out)
                c.drawLine(p1out.x, p1out.y, p2out.x, p2out.y, mWebPaint!!)
            }
        }
        recycleInstance(p1out)
        recycleInstance(p2out)
    }

    override fun drawHighlighted(c: Canvas?, indices: Array<Highlight>?) {
        val sliceangle = mChart!!.getSliceAngle()

        // calculate the factor that is needed for transforming the value to
        // pixels
        val factor = mChart!!.getFactor()
        val center = mChart!!.getCenterOffsets()
        val pOut = getInstance(0f, 0f)
        val radarData: RadarData? = mChart!!.getData()
        for (high in indices!!) {
            val set = radarData!!.getDataSetByIndex(high.getDataSetIndex())
            if (set == null || !set.isHighlightEnabled()) continue
            val e = set.getEntryForIndex(high.getX().toInt())!!
            if (!isInBoundsX(e, set)) continue
            val y = e.getY() - mChart!!.getYChartMin()
            getPosition(
                center,
                y * factor * mAnimator!!.getPhaseY(),
                sliceangle * high.getX() * mAnimator!!.getPhaseX() + mChart!!.getRotationAngle(),
                pOut
            )
            high.setDraw(pOut.x, pOut.y)

            // draw the lines
            drawHighlightLines(c!!, pOut.x, pOut.y, set)
            if (set.isDrawHighlightCircleEnabled()) {
                if (!java.lang.Float.isNaN(pOut.x) && !java.lang.Float.isNaN(pOut.y)) {
                    var strokeColor = set.getHighlightCircleStrokeColor()
                    if (strokeColor == ColorTemplate.COLOR_NONE) {
                        strokeColor = set.getColor(0)
                    }
                    if (set.getHighlightCircleStrokeAlpha() < 255) {
                        strokeColor =
                            colorWithAlpha(strokeColor, set.getHighlightCircleStrokeAlpha())
                    }
                    drawHighlightCircle(
                        c,
                        pOut,
                        set.getHighlightCircleInnerRadius(),
                        set.getHighlightCircleOuterRadius(),
                        set.getHighlightCircleFillColor(),
                        strokeColor,
                        set.getHighlightCircleStrokeWidth()
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
        var mInnRadius = innerRadius
        var mOuterRadius = outerRadius
        c.save()
        mOuterRadius = convertDpToPixel(mOuterRadius)
        mInnRadius = convertDpToPixel(mInnRadius)
        if (fillColor != ColorTemplate.COLOR_NONE) {
            val p = mDrawHighlightCirclePathBuffer
            p.reset()
            p.addCircle(point.x, point.y, mOuterRadius, Path.Direction.CW)
            if (mInnRadius > 0f) {
                p.addCircle(point.x, point.y, mInnRadius, Path.Direction.CCW)
            }
            mHighlightCirclePaint!!.color = fillColor
            mHighlightCirclePaint!!.style = Paint.Style.FILL
            c.drawPath(p, mHighlightCirclePaint!!)
        }
        if (strokeColor != ColorTemplate.COLOR_NONE) {
            mHighlightCirclePaint!!.color = strokeColor
            mHighlightCirclePaint!!.style = Paint.Style.STROKE
            mHighlightCirclePaint!!.strokeWidth = convertDpToPixel(strokeWidth)
            c.drawCircle(point.x, point.y, mOuterRadius, mHighlightCirclePaint!!)
        }
        c.restore()
    }
}