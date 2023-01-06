package com.github.mikephil.charting.renderer

import android.graphics.*
import android.util.Log
import com.github.mikephil.charting.animation.ChartAnimator
import com.github.mikephil.charting.highlight.Highlight
import com.github.mikephil.charting.interfaces.dataprovider.ScatterDataProvider
import com.github.mikephil.charting.interfaces.datasets.IScatterDataSet
import com.github.mikephil.charting.utils.*
import com.github.mikephil.charting.utils.MPPointF.Companion.getInstance
import com.github.mikephil.charting.utils.MPPointF.Companion.recycleInstance
import com.github.mikephil.charting.utils.Utils.convertDpToPixel
import com.github.mikephil.charting.utils.Utils.drawImage

class ScatterChartRenderer : LineScatterCandleRadarRenderer {

    public var mChart: ScatterDataProvider? = null

    constructor(
        chart: ScatterDataProvider,
        animator: ChartAnimator,
        viewPortHandler: ViewPortHandler
    ) : super(animator, viewPortHandler) {
        mChart = chart
    }

    override fun initBuffers() {}

    override fun drawData(c: Canvas?) {
        val scatterData = mChart!!.getScatterData()
        for (set in scatterData!!.getDataSets()!!) {
            if (set!!.isVisible()) drawDataSet(c, set)
        }
    }

    var mPixelBuffer = FloatArray(2)

    protected fun drawDataSet(c: Canvas?, dataSet: IScatterDataSet) {
        if (dataSet.getEntryCount() < 1) return
        val viewPortHandler = mViewPortHandler!!
        val trans = mChart!!.getTransformer(dataSet.getAxisDependency())!!
        val phaseY = mAnimator!!.getPhaseY()
        val renderer = dataSet.getShapeRenderer()
        if (renderer == null) {
            Log.i("MISSING", "There's no IShapeRenderer specified for ScatterDataSet")
            return
        }
        val max = Math.min(
            Math.ceil((dataSet.getEntryCount().toFloat() * mAnimator!!.getPhaseX()).toDouble()),
            dataSet.getEntryCount().toFloat().toDouble()
        )
            .toInt()
        for (i in 0 until max) {
            val e = dataSet.getEntryForIndex(i)!!
            mPixelBuffer[0] = e.getX()
            mPixelBuffer[1] = e.getY() * phaseY
            trans.pointValuesToPixel(mPixelBuffer)
            if (!viewPortHandler.isInBoundsRight(mPixelBuffer[0])) break
            if (!viewPortHandler.isInBoundsLeft(mPixelBuffer[0])
                || !viewPortHandler.isInBoundsY(mPixelBuffer[1])
            ) continue
            mRenderPaint!!.color = dataSet.getColor(i / 2)
            renderer.renderShape(
                c!!, dataSet, mViewPortHandler!!,
                mPixelBuffer[0], mPixelBuffer[1],
                mRenderPaint!!
            )
        }
    }

    override fun drawValues(c: Canvas?) {
        // if values are drawn
        if (isDrawingValuesAllowed(mChart!!)) {
            val dataSets: MutableList<IScatterDataSet?>? = mChart!!.getScatterData()!!.getDataSets()
            for (i in 0 until mChart!!.getScatterData()!!.getDataSetCount()) {
                val dataSet = dataSets!![i]
                if (!shouldDrawValues(dataSet!!) || dataSet.getEntryCount() < 1) continue

                // apply the text-styling defined by the DataSet
                applyValueTextStyle(dataSet)
                mXBounds[mChart!!] = dataSet
                val positions = mChart!!.getTransformer(dataSet.getAxisDependency())!!
                    .generateTransformedValuesScatter(
                        dataSet,
                        mAnimator!!.getPhaseX(), mAnimator!!.getPhaseY(), mXBounds.min, mXBounds.max
                    )
                val shapeSize = convertDpToPixel(dataSet.getScatterShapeSize())
                val iconsOffset = getInstance(dataSet.getIconsOffset()!!)
                iconsOffset.x = convertDpToPixel(iconsOffset.x)
                iconsOffset.y = convertDpToPixel(iconsOffset.y)
                var j = 0
                while (j < positions!!.size) {
                    if (!mViewPortHandler!!.isInBoundsRight(positions[j])) break

                    // make sure the lines don't do shitty things outside bounds
                    if (!mViewPortHandler!!.isInBoundsLeft(positions[j])
                        || !mViewPortHandler!!.isInBoundsY(positions[j + 1])
                    ) {
                        j += 2
                        continue
                    }
                    val entry = dataSet.getEntryForIndex(j / 2 + mXBounds.min)!!
                    if (dataSet.isDrawValuesEnabled()) {
                        drawValue(
                            c!!,
                            dataSet.getValueFormatter()!!,
                            entry.getY(),
                            entry,
                            i,
                            positions[j],
                            positions[j + 1] - shapeSize,
                            dataSet.getValueTextColor(j / 2 + mXBounds.min)!!
                        )
                    }
                    if (entry.getIcon() != null && dataSet.isDrawIconsEnabled()) {
                        val icon = entry.getIcon()
                        drawImage(
                            c!!,
                            icon!!,
                            (positions[j] + iconsOffset.x).toInt(),
                            (positions[j + 1] + iconsOffset.y).toInt(),
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
        val scatterData = mChart!!.getScatterData()
        for (high in indices!!) {
            val set = scatterData!!.getDataSetByIndex(high.getDataSetIndex())
            if (set == null || !set.isHighlightEnabled()) continue
            val e = set.getEntryForXValue(high.getX(), high.getY())
            if (!isInBoundsX(e, set)) continue
            val pix = mChart!!.getTransformer(set.getAxisDependency())!!.getPixelForValues(
                e!!.getX(), e.getY() * mAnimator!!.getPhaseY()
            )
            high.setDraw(pix!!.x.toFloat(), pix.y.toFloat())

            // draw the lines
            drawHighlightLines(c!!, pix.x.toFloat(), pix.y.toFloat(), set)
        }
    }
}