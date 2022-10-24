package com.github.mikephil.charting.renderer

import android.graphics.*
import com.github.mikephil.charting.charts.RadarChart
import com.github.mikephil.charting.components.XAxis
import com.github.mikephil.charting.utils.FSize.Companion.recycleInstance
import com.github.mikephil.charting.utils.MPPointD.Companion.recycleInstance
import com.github.mikephil.charting.utils.MPPointF
import com.github.mikephil.charting.utils.MPPointF.Companion.recycleInstance
import com.github.mikephil.charting.utils.Utils.getPosition
import com.github.mikephil.charting.utils.ViewPortHandler

class XAxisRendererRadarChart(
    viewPortHandler: ViewPortHandler?,
    xAxis: XAxis,
    private val mChart: RadarChart
) : XAxisRenderer(viewPortHandler, xAxis, null) {
    override fun renderAxisLabels(c: Canvas) {
        if (!mXAxis.isEnabled || !mXAxis.isDrawLabelsEnabled) return
        val labelRotationAngleDegrees = mXAxis.labelRotationAngle
        val drawLabelAnchor = MPPointF.getInstance(0.5f, 0.25f)
        mAxisLabelPaint.typeface = mXAxis.typeface
        mAxisLabelPaint.textSize = mXAxis.textSize
        mAxisLabelPaint.color = mXAxis.textColor
        val sliceangle = mChart.sliceAngle

        // calculate the factor that is needed for transforming the value to
        // pixels
        val factor = mChart.factor
        val center = mChart.centerOffsets
        val pOut = MPPointF.getInstance(0, 0)
        for (i in 0 until mChart.data.maxEntryCountSet.entryCount) {
            val label = mXAxis.valueFormatter.getFormattedValue(i.toFloat(), mXAxis)
            val angle = (sliceangle * i + mChart.rotationAngle) % 360f
            getPosition(
                center, mChart.yRange * factor
                        + mXAxis.mLabelRotatedWidth / 2f, angle, pOut
            )
            drawLabel(
                c, label, pOut.x, pOut.y - mXAxis.mLabelRotatedHeight / 2f,
                drawLabelAnchor, labelRotationAngleDegrees
            )
        }
        recycleInstance(center)
        recycleInstance(pOut)
        recycleInstance(drawLabelAnchor)
    }

    /**
     * XAxis LimitLines on RadarChart not yet supported.
     *
     * @param c
     */
    override fun renderLimitLines(c: Canvas) {
        // this space intentionally left blank
    }
}