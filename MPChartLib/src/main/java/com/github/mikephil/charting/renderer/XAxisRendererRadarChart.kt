package com.github.mikephil.charting.renderer

import android.graphics.*
import com.github.mikephil.charting.charts.RadarChart
import com.github.mikephil.charting.components.XAxis
import com.github.mikephil.charting.utils.MPPointF.Companion.getInstance
import com.github.mikephil.charting.utils.MPPointF.Companion.recycleInstance
import com.github.mikephil.charting.utils.Utils.getPosition
import com.github.mikephil.charting.utils.ViewPortHandler

class XAxisRendererRadarChart : XAxisRenderer {

    private var mChart: RadarChart? = null

    constructor(
        viewPortHandler: ViewPortHandler,
        xAxis: XAxis,
        chart: RadarChart
    ) : super(viewPortHandler, xAxis, null) {
        mChart = chart
    }

    override fun renderAxisLabels(c: Canvas?) {
        if (!mXAxis!!.isEnabled() || !mXAxis!!.isDrawLabelsEnabled()) return
        val labelRotationAngleDegrees = mXAxis!!.getLabelRotationAngle()
        val drawLabelAnchor = getInstance(0.5f, 0.25f)
        mAxisLabelPaint!!.typeface = mXAxis!!.getTypeface()
        mAxisLabelPaint!!.textSize = mXAxis!!.getTextSize()
        mAxisLabelPaint!!.color = mXAxis!!.getTextColor()
        val sliceangle = mChart!!.getSliceAngle()

        // calculate the factor that is needed for transforming the value to
        // pixels
        val factor = mChart!!.getFactor()
        val center = mChart!!.getCenterOffsets()
        val pOut = getInstance(0f, 0f)
        for (i in 0 until mChart!!.getData()!!.getMaxEntryCountSet()!!.getEntryCount()) {
            val label = mXAxis!!.getValueFormatter().getFormattedValue(i.toFloat(), mXAxis)
            val angle = (sliceangle * i + mChart!!.getRotationAngle()) % 360f
            getPosition(
                center, mChart!!.getYRange() * factor
                        + mXAxis!!.mLabelRotatedWidth / 2f, angle, pOut
            )
            drawLabel(
                c, label, pOut.x, pOut.y - mXAxis!!.mLabelRotatedHeight / 2f,
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
    override fun renderLimitLines(c: Canvas?) {
        // this space intentionally left blank
    }
}