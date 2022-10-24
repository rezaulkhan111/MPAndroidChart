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
interface IPieDataSet : IDataSet<PieEntry?> {
    /**
     * Returns the space that is set to be between the piechart-slices of this
     * DataSet, in pixels.
     *
     * @return
     */
    val sliceSpace: Float

    /**
     * When enabled, slice spacing will be 0.0 when the smallest value is going to be
     * smaller than the slice spacing itself.
     *
     * @return
     */
    val isAutomaticallyDisableSliceSpacingEnabled: Boolean

    /**
     * Returns the distance a highlighted piechart slice is "shifted" away from
     * the chart-center in dp.
     *
     * @return
     */
    val selectionShift: Float
    val xValuePosition: ValuePosition?
    val yValuePosition: ValuePosition?

    /**
     * When valuePosition is OutsideSlice, indicates line color
     */
    val valueLineColor: Int

    /**
     * When valuePosition is OutsideSlice and enabled, line will have the same color as the slice
     */
    val isUseValueColorForLineEnabled: Boolean

    /**
     * When valuePosition is OutsideSlice, indicates line width
     */
    val valueLineWidth: Float

    /**
     * When valuePosition is OutsideSlice, indicates offset as percentage out of the slice size
     */
    val valueLinePart1OffsetPercentage: Float

    /**
     * When valuePosition is OutsideSlice, indicates length of first half of the line
     */
    val valueLinePart1Length: Float

    /**
     * When valuePosition is OutsideSlice, indicates length of second half of the line
     */
    val valueLinePart2Length: Float

    /**
     * When valuePosition is OutsideSlice, this allows variable line length
     */
    val isValueLineVariableLength: Boolean

    /**
     * Gets the color for the highlighted sector
     */
    val highlightColor: Int?
}