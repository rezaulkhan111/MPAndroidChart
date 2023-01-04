package com.github.mikephil.charting.highlight

import com.github.mikephil.charting.charts.RadarChart
import com.github.mikephil.charting.interfaces.datasets.IDataSet
import com.github.mikephil.charting.utils.MPPointF.Companion.getInstance
import com.github.mikephil.charting.utils.Utils.getPosition

/**
 * Created by philipp on 12/06/16.
 */
class RadarHighlighter : PieRadarHighlighter<RadarChart> {

    constructor(chart: RadarChart) : super(chart) {}

    override fun getClosestHighlight(index: Int, x: Float, y: Float): Highlight? {
        val highlights = getHighlightsAtIndex(index)
        val distanceToCenter = mChart!!.distanceToCenter(x, y) / mChart!!.getFactor()
        var closest: Highlight? = null
        var distance = Float.MAX_VALUE
        for (i in highlights.indices) {
            val high = highlights[i]
            val cdistance = Math.abs(high.getY() - distanceToCenter)
            if (cdistance < distance) {
                closest = high
                distance = cdistance
            }
        }
        return closest
    }

    /**
     * Returns an array of Highlight objects for the given index. The Highlight
     * objects give information about the value at the selected index and the
     * DataSet it belongs to. INFORMATION: This method does calculations at
     * runtime. Do not over-use in performance critical situations.
     *
     * @param index
     * @return
     */
    private fun getHighlightsAtIndex(index: Int): List<Highlight> {
        mHighlightBuffer.clear()
        val phaseX = mChart!!.getAnimator()!!.getPhaseX()
        val phaseY = mChart!!.getAnimator()!!.getPhaseY()
        val sliceangle = mChart!!.getSliceAngle()
        val factor = mChart!!.getFactor()
        val pOut = getInstance(0f, 0f)
        for (i in 0 until mChart!!.getData().getDataSetCount()) {
            val dataSet: IDataSet<*> = mChart!!.getData().getDataSetByIndex(i)
            val entry = dataSet.getEntryForIndex(index)
            val y = entry.getY() - mChart!!.getYChartMin()
            getPosition(
                mChart!!.getCenterOffsets()!!, y * factor * phaseY,
                sliceangle * index * phaseX + mChart!!.getRotationAngle(), pOut
            )
            mHighlightBuffer.add(
                Highlight(
                    index.toFloat(),
                    entry.getY(),
                    pOut.x,
                    pOut.y,
                    i,
                    dataSet.getAxisDependency()
                )
            )
        }
        return mHighlightBuffer
    }
}