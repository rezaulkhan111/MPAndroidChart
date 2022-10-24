package com.github.mikephil.charting.highlight

import com.github.mikephil.charting.interfaces.dataprovider.BarDataProvider.barData
import com.github.mikephil.charting.interfaces.datasets.IBarDataSet.isStacked
import com.github.mikephil.charting.utils.MPPointD.Companion.recycleInstance
import com.github.mikephil.charting.interfaces.datasets.IDataSet.getEntryForXValue
import com.github.mikephil.charting.interfaces.dataprovider.BarLineScatterCandleBubbleDataProvider.getTransformer
import com.github.mikephil.charting.interfaces.datasets.IDataSet.axisDependency
import com.github.mikephil.charting.utils.Transformer.getPixelForValues
import com.github.mikephil.charting.interfaces.datasets.IDataSet.getEntryForIndex
import com.github.mikephil.charting.utils.Transformer.getValuesByTouchPoint
import com.github.mikephil.charting.interfaces.dataprovider.ChartInterface.maxHighlightDistance
import com.github.mikephil.charting.interfaces.datasets.IDataSet.isHighlightEnabled
import com.github.mikephil.charting.interfaces.datasets.IDataSet.getEntriesForXValue
import com.github.mikephil.charting.interfaces.dataprovider.BarLineScatterCandleBubbleDataProvider.data
import com.github.mikephil.charting.utils.Utils.getPosition
import com.github.mikephil.charting.interfaces.dataprovider.CombinedDataProvider.combinedData
import com.github.mikephil.charting.interfaces.datasets.IDataSet.entryCount
import com.github.mikephil.charting.components.YAxis.AxisDependency
import com.github.mikephil.charting.interfaces.dataprovider.BarDataProvider
import com.github.mikephil.charting.highlight.ChartHighlighter
import com.github.mikephil.charting.utils.MPPointD
import com.github.mikephil.charting.data.BarData
import com.github.mikephil.charting.interfaces.datasets.IBarDataSet
import com.github.mikephil.charting.data.BarEntry
import com.github.mikephil.charting.data.BarLineScatterCandleBubbleData
import com.github.mikephil.charting.charts.PieChart
import com.github.mikephil.charting.highlight.PieRadarHighlighter
import com.github.mikephil.charting.interfaces.datasets.IPieDataSet
import com.github.mikephil.charting.interfaces.dataprovider.BarLineScatterCandleBubbleDataProvider
import com.github.mikephil.charting.highlight.IHighlighter
import com.github.mikephil.charting.components.YAxis
import com.github.mikephil.charting.interfaces.datasets.IDataSet
import com.github.mikephil.charting.data.DataSet
import com.github.mikephil.charting.data.DataSet.Rounding
import com.github.mikephil.charting.charts.RadarChart
import com.github.mikephil.charting.utils.MPPointF
import com.github.mikephil.charting.interfaces.dataprovider.CombinedDataProvider
import com.github.mikephil.charting.highlight.BarHighlighter
import com.github.mikephil.charting.data.ChartData
import com.github.mikephil.charting.charts.PieRadarChartBase

/**
 * Created by philipp on 12/06/16.
 */
class RadarHighlighter(chart: RadarChart) : PieRadarHighlighter<RadarChart?>(chart) {
    override fun getClosestHighlight(index: Int, x: Float, y: Float): Highlight? {
        val highlights = getHighlightsAtIndex(index)
        val distanceToCenter = mChart!!.distanceToCenter(x, y) / mChart!!.factor
        var closest: Highlight? = null
        var distance = Float.MAX_VALUE
        for (i in highlights.indices) {
            val high = highlights[i]
            val cdistance = Math.abs(high.y - distanceToCenter)
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
    protected fun getHighlightsAtIndex(index: Int): List<Highlight> {
        mHighlightBuffer.clear()
        val phaseX = mChart!!.animator.phaseX
        val phaseY = mChart!!.animator.phaseY
        val sliceangle = mChart!!.sliceAngle
        val factor = mChart!!.factor
        val pOut = MPPointF.getInstance(0, 0)
        for (i in 0 until mChart.getData().getDataSetCount()) {
            val dataSet: IDataSet<*> = mChart.getData().getDataSetByIndex(i)
            val entry = dataSet.getEntryForIndex(index)!!
            val y: Float = entry.y - mChart.getYChartMin()
            getPosition(
                mChart.getCenterOffsets(), y * factor * phaseY,
                sliceangle * index * phaseX + mChart!!.rotationAngle, pOut
            )
            mHighlightBuffer.add(
                Highlight(
                    index.toFloat(),
                    entry.y,
                    pOut.x,
                    pOut.y,
                    i,
                    dataSet.axisDependency
                )
            )
        }
        return mHighlightBuffer
    }
}