package com.github.mikephil.charting.highlight

import com.github.mikephil.charting.data.BarData
import com.github.mikephil.charting.data.ChartData
import com.github.mikephil.charting.data.DataSet.Rounding
import com.github.mikephil.charting.interfaces.dataprovider.BarDataProvider
import com.github.mikephil.charting.interfaces.dataprovider.CombinedDataProvider
import com.github.mikephil.charting.interfaces.datasets.IDataSet

/**
 * Created by Philipp Jahoda on 12/09/15.
 */
class CombinedHighlighter : ChartHighlighter<CombinedDataProvider>, IHighlighter {

    /**
     * bar highlighter for supporting stacked highlighting
     */
    private var barHighlighter: BarHighlighter? = null

    constructor(chart: CombinedDataProvider, barChart: BarDataProvider) : super(chart) {
        // if there is BarData, create a BarHighlighter
        barHighlighter = if (barChart.getBarData() == null) null else BarHighlighter(barChart)
    }

    override fun getHighlightsAtXValue(xVal: Float, x: Float, y: Float): List<Highlight> {
        mHighlightBuffer.clear()
        val dataObjects = mChart!!.getCombinedData()!!
            .getAllData()
        for (i in dataObjects.indices) {
            val dataObject: ChartData<*> = dataObjects[i]

            // in case of BarData, let the BarHighlighter take over
            if (barHighlighter != null && dataObject is BarData) {
                val high = barHighlighter!!.getHighlight(x, y)
                if (high != null) {
                    high.setDataIndex(i)
                    mHighlightBuffer.add(high)
                }
            } else {
                var j = 0
                val dataSetCount = dataObject.getDataSetCount()
                while (j < dataSetCount) {
                    val dataSet: IDataSet<*>? = dataObjects[i].getDataSetByIndex(j)
                    // don't include datasets that cannot be highlighted
                    if (!dataSet!!.isHighlightEnabled()) {
                        j++
                        continue
                    }
                    val highs = buildHighlights(dataSet, j, xVal, Rounding.CLOSEST)
                    for (high in highs) {
                        high.setDataIndex(i)
                        mHighlightBuffer.add(high)
                    }
                    j++
                }
            }
        }
        return mHighlightBuffer
    }
}