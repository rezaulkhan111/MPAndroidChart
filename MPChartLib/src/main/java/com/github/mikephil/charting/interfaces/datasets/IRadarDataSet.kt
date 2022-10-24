package com.github.mikephil.charting.interfaces.datasets

import com.github.mikephil.charting.data.DataSet.Rounding
import com.github.mikephil.charting.components.YAxis.AxisDependency
import com.github.mikephil.charting.formatter.IValueFormatter
import android.graphics.Typeface
import com.github.mikephil.charting.components.Legend.LegendForm
import android.graphics.DashPathEffect
import com.github.mikephil.charting.utils.MPPointF
import com.github.mikephil.charting.interfaces.datasets.IBarLineScatterCandleBubbleDataSet
import com.github.mikephil.charting.data.BarEntry
import com.github.mikephil.charting.utils.Fill
import com.github.mikephil.charting.interfaces.datasets.IDataSet
import com.github.mikephil.charting.data.PieEntry
import com.github.mikephil.charting.data.PieDataSet.ValuePosition
import com.github.mikephil.charting.interfaces.datasets.ILineRadarDataSet
import com.github.mikephil.charting.data.LineDataSet
import com.github.mikephil.charting.formatter.IFillFormatter
import com.github.mikephil.charting.data.RadarEntry
import com.github.mikephil.charting.data.BubbleEntry
import com.github.mikephil.charting.interfaces.datasets.ILineScatterCandleRadarDataSet
import com.github.mikephil.charting.data.CandleEntry
import android.graphics.drawable.Drawable
import android.graphics.RectF
import com.github.mikephil.charting.data.ChartData
import com.github.mikephil.charting.interfaces.dataprovider.BarLineScatterCandleBubbleDataProvider
import com.github.mikephil.charting.data.BarData
import com.github.mikephil.charting.data.LineData
import com.github.mikephil.charting.components.YAxis
import com.github.mikephil.charting.data.BubbleData
import com.github.mikephil.charting.data.CandleData
import com.github.mikephil.charting.data.ScatterData
import com.github.mikephil.charting.interfaces.dataprovider.LineDataProvider
import com.github.mikephil.charting.interfaces.dataprovider.BarDataProvider
import com.github.mikephil.charting.interfaces.dataprovider.BubbleDataProvider
import com.github.mikephil.charting.interfaces.dataprovider.CandleDataProvider
import com.github.mikephil.charting.interfaces.dataprovider.ScatterDataProvider
import com.github.mikephil.charting.data.CombinedData
import com.github.mikephil.charting.interfaces.dataprovider.ChartInterface
import com.github.mikephil.charting.data.BarLineScatterCandleBubbleData

/**
 * Created by Philipp Jahoda on 03/11/15.
 */
interface IRadarDataSet : ILineRadarDataSet<RadarEntry?> {
    /// Sets whether highlight circle should be drawn or not
    /// flag indicating whether highlight circle should be drawn or not
    var isDrawHighlightCircleEnabled: Boolean
    val highlightCircleFillColor: Int

    /// The stroke color for highlight circle.
    /// If Utils.COLOR_NONE, the color of the dataset is taken.
    val highlightCircleStrokeColor: Int
    val highlightCircleStrokeAlpha: Int
    val highlightCircleInnerRadius: Float
    val highlightCircleOuterRadius: Float
    val highlightCircleStrokeWidth: Float
}