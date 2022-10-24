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
 * Created by Philipp Jahoda on 12/09/15.
 */
class CombinedHighlighter(chart: CombinedDataProvider, barChart: BarDataProvider) :
    ChartHighlighter<CombinedDataProvider?>(chart), IHighlighter {
    /**
     * bar highlighter for supporting stacked highlighting
     */
    protected var barHighlighter: BarHighlighter?
    override fun getHighlightsAtXValue(xVal: Float, x: Float, y: Float): List<Highlight> {
        mHighlightBuffer.clear()
        val dataObjects = mChart!!.combinedData!!.allData
        for (i in dataObjects.indices) {
            val dataObject: ChartData<*> = dataObjects[i]

            // in case of BarData, let the BarHighlighter take over
            if (barHighlighter != null && dataObject is BarData) {
                val high = barHighlighter!!.getHighlight(x, y)
                if (high != null) {
                    high.dataIndex = i
                    mHighlightBuffer.add(high)
                }
            } else {
                var j = 0
                val dataSetCount = dataObject.dataSetCount
                while (j < dataSetCount) {
                    val dataSet: IDataSet<*> = dataObjects[i].getDataSetByIndex(j)

                    // don't include datasets that cannot be highlighted
                    if (!dataSet.isHighlightEnabled) {
                        j++
                        continue
                    }
                    val highs = buildHighlights(dataSet, j, xVal, Rounding.CLOSEST)
                    for (high in highs!!) {
                        high.dataIndex = i
                        mHighlightBuffer.add(high!!)
                    }
                    j++
                }
            }
        }
        return mHighlightBuffer
    } //    protected Highlight getClosest(float x, float y, Highlight... highs) {

    //
    //        Highlight closest = null;
    //        float minDistance = Float.MAX_VALUE;
    //
    //        for (Highlight high : highs) {
    //
    //            if (high == null)
    //                continue;
    //
    //            float tempDistance = getDistance(x, y, high.getXPx(), high.getYPx());
    //
    //            if (tempDistance < minDistance) {
    //                minDistance = tempDistance;
    //                closest = high;
    //            }
    //        }
    //
    //        return closest;
    //    }
    init {

        // if there is BarData, create a BarHighlighter
        barHighlighter = if (barChart.barData == null) null else BarHighlighter(barChart)
    }
}