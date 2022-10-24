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
 * Created by Philpp Jahoda on 21/10/15.
 */
interface ILineDataSet : ILineRadarDataSet<Entry?> {
    /**
     * Returns the drawing mode for this line dataset
     *
     * @return
     */
    val mode: LineDataSet.Mode?

    /**
     * Returns the intensity of the cubic lines (the effect intensity).
     * Max = 1f = very cubic, Min = 0.05f = low cubic effect, Default: 0.2f
     *
     * @return
     */
    val cubicIntensity: Float

    @get:Deprecated("")
    val isDrawCubicEnabled: Boolean

    @get:Deprecated("")
    val isDrawSteppedEnabled: Boolean

    /**
     * Returns the size of the drawn circles.
     */
    val circleRadius: Float

    /**
     * Returns the hole radius of the drawn circles.
     */
    val circleHoleRadius: Float

    /**
     * Returns the color at the given index of the DataSet's circle-color array.
     * Performs a IndexOutOfBounds check by modulus.
     *
     * @param index
     * @return
     */
    fun getCircleColor(index: Int): Int

    /**
     * Returns the number of colors in this DataSet's circle-color array.
     *
     * @return
     */
    val circleColorCount: Int

    /**
     * Returns true if drawing circles for this DataSet is enabled, false if not
     *
     * @return
     */
    val isDrawCirclesEnabled: Boolean

    /**
     * Returns the color of the inner circle (the circle-hole).
     *
     * @return
     */
    val circleHoleColor: Int

    /**
     * Returns true if drawing the circle-holes is enabled, false if not.
     *
     * @return
     */
    val isDrawCircleHoleEnabled: Boolean

    /**
     * Returns the DashPathEffect that is used for drawing the lines.
     *
     * @return
     */
    val dashPathEffect: DashPathEffect?

    /**
     * Returns true if the dashed-line effect is enabled, false if not.
     * If the DashPathEffect object is null, also return false here.
     *
     * @return
     */
    val isDashedLineEnabled: Boolean

    /**
     * Returns the IFillFormatter that is set for this DataSet.
     *
     * @return
     */
    val fillFormatter: IFillFormatter?
}