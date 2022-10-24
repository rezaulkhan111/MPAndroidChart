package com.github.mikephil.charting.interfaces.datasets

import com.github.mikephil.charting.data.DataSet.Rounding
import com.github.mikephil.charting.components.YAxis.AxisDependency
import com.github.mikephil.charting.formatter.IValueFormatter
import android.graphics.Typeface
import com.github.mikephil.charting.components.Legend.LegendForm
import android.graphics.DashPathEffect
import com.github.mikephil.charting.utils.MPPointF
import com.github.mikephil.charting.interfaces.datasets.IBarLineScatterCandleBubbleDataSet
import com.github.mikephil.charting.utils.Fill
import com.github.mikephil.charting.interfaces.datasets.IDataSet
import com.github.mikephil.charting.data.PieDataSet.ValuePosition
import com.github.mikephil.charting.interfaces.datasets.ILineRadarDataSet
import com.github.mikephil.charting.formatter.IFillFormatter
import com.github.mikephil.charting.interfaces.datasets.ILineScatterCandleRadarDataSet
import android.graphics.drawable.Drawable
import android.graphics.RectF
import com.github.mikephil.charting.interfaces.dataprovider.BarLineScatterCandleBubbleDataProvider
import com.github.mikephil.charting.components.YAxis
import com.github.mikephil.charting.data.*
import com.github.mikephil.charting.interfaces.dataprovider.LineDataProvider
import com.github.mikephil.charting.interfaces.dataprovider.BarDataProvider
import com.github.mikephil.charting.interfaces.dataprovider.BubbleDataProvider
import com.github.mikephil.charting.interfaces.dataprovider.CandleDataProvider
import com.github.mikephil.charting.interfaces.dataprovider.ScatterDataProvider
import com.github.mikephil.charting.interfaces.dataprovider.ChartInterface

/**
 * Created by Philipp Jahoda on 21/10/15.
 */
interface ILineScatterCandleRadarDataSet<T : Entry?> : IBarLineScatterCandleBubbleDataSet<T> {
    /**
     * Returns true if vertical highlight indicator lines are enabled (drawn)
     * @return
     */
    val isVerticalHighlightIndicatorEnabled: Boolean

    /**
     * Returns true if vertical highlight indicator lines are enabled (drawn)
     * @return
     */
    val isHorizontalHighlightIndicatorEnabled: Boolean

    /**
     * Returns the line-width in which highlight lines are to be drawn.
     * @return
     */
    val highlightLineWidth: Float

    /**
     * Returns the DashPathEffect that is used for highlighting.
     * @return
     */
    val dashPathEffectHighlight: DashPathEffect?
}