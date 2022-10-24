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
 * Created by Philipp Jahoda on 24/07/15. Class that represents the range of one value in a stacked bar entry. e.g.
 * stack values are -10, 5, 20 -> then ranges are (-10 - 0, 0 - 5, 5 - 25).
 */
class Range(var from: Float, var to: Float) {
    /**
     * Returns true if this range contains (if the value is in between) the given value, false if not.
     *
     * @param value
     * @return
     */
    operator fun contains(value: Float): Boolean {
        return if (value > from && value <= to) true else false
    }

    fun isLarger(value: Float): Boolean {
        return value > to
    }

    fun isSmaller(value: Float): Boolean {
        return value < from
    }
}