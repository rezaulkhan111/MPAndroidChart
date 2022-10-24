package com.github.mikephil.charting.interfaces.datasets

import com.github.mikephil.charting.data.DataSet.Rounding
import com.github.mikephil.charting.components.YAxis.AxisDependency
import com.github.mikephil.charting.formatter.IValueFormatter
import android.graphics.Typeface
import com.github.mikephil.charting.components.Legend.LegendForm
import android.graphics.DashPathEffect
import android.graphics.Paint
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
 * Created by philipp on 21/10/15.
 */
interface ICandleDataSet : ILineScatterCandleRadarDataSet<CandleEntry?> {
    /**
     * Returns the space that is left out on the left and right side of each
     * candle.
     *
     * @return
     */
    val barSpace: Float

    /**
     * Returns whether the candle bars should show?
     * When false, only "ticks" will show
     *
     * - default: true
     *
     * @return
     */
    val showCandleBar: Boolean

    /**
     * Returns the width of the candle-shadow-line in pixels.
     *
     * @return
     */
    val shadowWidth: Float

    /**
     * Returns shadow color for all entries
     *
     * @return
     */
    val shadowColor: Int

    /**
     * Returns the neutral color (for open == close)
     *
     * @return
     */
    val neutralColor: Int

    /**
     * Returns the increasing color (for open < close).
     *
     * @return
     */
    val increasingColor: Int

    /**
     * Returns the decreasing color (for open > close).
     *
     * @return
     */
    val decreasingColor: Int

    /**
     * Returns paint style when open < close
     *
     * @return
     */
    val increasingPaintStyle: Paint.Style?

    /**
     * Returns paint style when open > close
     *
     * @return
     */
    val decreasingPaintStyle: Paint.Style?

    /**
     * Is the shadow color same as the candle color?
     *
     * @return
     */
    val shadowColorSameAsCandle: Boolean
}