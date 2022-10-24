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
 * Created by philipp on 10/06/16.
 */
interface IHighlighter {
    /**
     * Returns a Highlight object corresponding to the given x- and y- touch positions in pixels.
     *
     * @param x
     * @param y
     * @return
     */
    fun getHighlight(x: Float, y: Float): Highlight?
}