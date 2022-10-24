package com.github.mikephil.charting.data

import com.github.mikephil.charting.interfaces.datasets.IDataSet.entryCount
import com.github.mikephil.charting.interfaces.datasets.IDataSet.getEntryForIndex
import com.github.mikephil.charting.interfaces.datasets.IDataSet.label
import com.github.mikephil.charting.highlight.Highlight.x
import com.github.mikephil.charting.interfaces.datasets.IDataSet.calcMinMaxY
import com.github.mikephil.charting.interfaces.datasets.IDataSet.yMax
import com.github.mikephil.charting.interfaces.datasets.IDataSet.yMin
import com.github.mikephil.charting.interfaces.datasets.IDataSet.axisDependency
import com.github.mikephil.charting.highlight.Highlight.dataSetIndex
import com.github.mikephil.charting.interfaces.datasets.IDataSet.getEntryForXValue
import com.github.mikephil.charting.highlight.Highlight.y
import com.github.mikephil.charting.interfaces.datasets.IDataSet.addEntry
import com.github.mikephil.charting.interfaces.datasets.IDataSet.xMax
import com.github.mikephil.charting.interfaces.datasets.IDataSet.xMin
import com.github.mikephil.charting.interfaces.datasets.IDataSet.removeEntry
import com.github.mikephil.charting.interfaces.datasets.IDataSet.colors
import com.github.mikephil.charting.interfaces.datasets.IDataSet.valueFormatter
import com.github.mikephil.charting.interfaces.datasets.IDataSet.valueTextColor
import com.github.mikephil.charting.interfaces.datasets.IDataSet.setValueTextColors
import com.github.mikephil.charting.interfaces.datasets.IDataSet.valueTypeface
import com.github.mikephil.charting.interfaces.datasets.IDataSet.valueTextSize
import com.github.mikephil.charting.interfaces.datasets.IDataSet.setDrawValues
import com.github.mikephil.charting.interfaces.datasets.IDataSet.isHighlightEnabled
import com.github.mikephil.charting.interfaces.datasets.IBubbleDataSet.highlightCircleWidth
import com.github.mikephil.charting.utils.Utils.convertDpToPixel
import com.github.mikephil.charting.interfaces.datasets.IDataSet.calcMinMax
import com.github.mikephil.charting.utils.ColorTemplate.createColors
import com.github.mikephil.charting.utils.Utils.defaultValueFormatter
import com.github.mikephil.charting.interfaces.datasets.IScatterDataSet.scatterShapeSize
import com.github.mikephil.charting.highlight.Highlight.dataIndex
import com.github.mikephil.charting.interfaces.datasets.IDataSet.getEntriesForXValue
import android.annotation.TargetApi
import android.os.Build
import com.github.mikephil.charting.data.filter.ApproximatorN
import android.os.Parcelable
import android.graphics.drawable.Drawable
import android.os.Parcel
import android.os.ParcelFormatException
import android.os.Parcelable.Creator
import com.github.mikephil.charting.data.BarLineScatterCandleBubbleData
import com.github.mikephil.charting.interfaces.datasets.IBarDataSet
import com.github.mikephil.charting.data.BarEntry
import com.github.mikephil.charting.data.BaseDataSet
import com.github.mikephil.charting.data.DataSet.Rounding
import com.github.mikephil.charting.data.DataSet
import com.github.mikephil.charting.data.ChartData
import com.github.mikephil.charting.interfaces.datasets.IPieDataSet
import android.annotation.SuppressLint
import com.github.mikephil.charting.interfaces.datasets.ILineDataSet
import com.github.mikephil.charting.data.PieEntry
import com.github.mikephil.charting.interfaces.datasets.IDataSet
import com.github.mikephil.charting.components.YAxis.AxisDependency
import com.github.mikephil.charting.formatter.IValueFormatter
import android.graphics.Typeface
import com.github.mikephil.charting.interfaces.datasets.IRadarDataSet
import com.github.mikephil.charting.data.BarLineScatterCandleBubbleDataSet
import com.github.mikephil.charting.utils.Fill
import com.github.mikephil.charting.data.BarDataSet
import com.github.mikephil.charting.interfaces.datasets.IBubbleDataSet
import com.github.mikephil.charting.interfaces.datasets.ICandleDataSet
import com.github.mikephil.charting.data.PieDataSet.ValuePosition
import com.github.mikephil.charting.data.PieDataSet
import com.github.mikephil.charting.data.RadarEntry
import com.github.mikephil.charting.components.YAxis
import com.github.mikephil.charting.components.Legend.LegendForm
import com.github.mikephil.charting.components.Legend
import android.graphics.DashPathEffect
import com.github.mikephil.charting.utils.MPPointF
import com.github.mikephil.charting.utils.ColorTemplate
import com.github.mikephil.charting.data.BubbleEntry
import com.github.mikephil.charting.data.CandleEntry
import com.github.mikephil.charting.data.LineRadarDataSet
import com.github.mikephil.charting.formatter.IFillFormatter
import com.github.mikephil.charting.formatter.DefaultFillFormatter
import com.github.mikephil.charting.data.LineDataSet
import com.github.mikephil.charting.interfaces.datasets.IScatterDataSet
import com.github.mikephil.charting.interfaces.datasets.IBarLineScatterCandleBubbleDataSet
import com.github.mikephil.charting.data.LineData
import com.github.mikephil.charting.data.BarData
import com.github.mikephil.charting.data.ScatterData
import com.github.mikephil.charting.data.CandleData
import com.github.mikephil.charting.data.BubbleData
import com.github.mikephil.charting.data.RadarDataSet
import com.github.mikephil.charting.data.BubbleDataSet
import com.github.mikephil.charting.data.LineScatterCandleRadarDataSet
import com.github.mikephil.charting.data.CandleDataSet
import com.github.mikephil.charting.data.ScatterDataSet
import com.github.mikephil.charting.charts.ScatterChart.ScatterShape
import com.github.mikephil.charting.renderer.scatter.TriangleShapeRenderer
import com.github.mikephil.charting.interfaces.datasets.ILineRadarDataSet
import com.github.mikephil.charting.interfaces.datasets.ILineScatterCandleRadarDataSet

/**
 * Subclass of Entry that holds all values for one entry in a CandleStickChart.
 *
 * @author Philipp Jahoda
 */
@SuppressLint("ParcelCreator")
class CandleEntry : Entry {
    /**
     * Returns the upper shadows highest value.
     *
     * @return
     */
    /** shadow-high value  */
    var high = 0f
    /**
     * Returns the lower shadows lowest value.
     *
     * @return
     */
    /** shadow-low value  */
    var low = 0f
    /**
     * Returns the bodys close value.
     *
     * @return
     */
    /** close value  */
    var close = 0f
    /**
     * Returns the bodys open value.
     *
     * @return
     */
    /** open value  */
    var open = 0f

    /**
     * Constructor.
     *
     * @param x The value on the x-axis
     * @param shadowH The (shadow) high value
     * @param shadowL The (shadow) low value
     * @param open The open value
     * @param close The close value
     */
    constructor(x: Float, shadowH: Float, shadowL: Float, open: Float, close: Float) : super(
        x,
        (shadowH + shadowL) / 2f
    ) {
        high = shadowH
        low = shadowL
        this.open = open
        this.close = close
    }

    /**
     * Constructor.
     *
     * @param x The value on the x-axis
     * @param shadowH The (shadow) high value
     * @param shadowL The (shadow) low value
     * @param open
     * @param close
     * @param data Spot for additional data this Entry represents
     */
    constructor(
        x: Float, shadowH: Float, shadowL: Float, open: Float, close: Float,
        data: Any?
    ) : super(x, (shadowH + shadowL) / 2f, data) {
        high = shadowH
        low = shadowL
        this.open = open
        this.close = close
    }

    /**
     * Constructor.
     *
     * @param x The value on the x-axis
     * @param shadowH The (shadow) high value
     * @param shadowL The (shadow) low value
     * @param open
     * @param close
     * @param icon Icon image
     */
    constructor(
        x: Float, shadowH: Float, shadowL: Float, open: Float, close: Float,
        icon: Drawable?
    ) : super(x, (shadowH + shadowL) / 2f, icon) {
        high = shadowH
        low = shadowL
        this.open = open
        this.close = close
    }

    /**
     * Constructor.
     *
     * @param x The value on the x-axis
     * @param shadowH The (shadow) high value
     * @param shadowL The (shadow) low value
     * @param open
     * @param close
     * @param icon Icon image
     * @param data Spot for additional data this Entry represents
     */
    constructor(
        x: Float, shadowH: Float, shadowL: Float, open: Float, close: Float,
        icon: Drawable?, data: Any?
    ) : super(x, (shadowH + shadowL) / 2f, icon, data) {
        high = shadowH
        low = shadowL
        this.open = open
        this.close = close
    }

    /**
     * Returns the overall range (difference) between shadow-high and
     * shadow-low.
     *
     * @return
     */
    val shadowRange: Float
        get() = Math.abs(high - low)

    /**
     * Returns the body size (difference between open and close).
     *
     * @return
     */
    val bodyRange: Float
        get() = Math.abs(open - close)

    /**
     * Returns the center value of the candle. (Middle value between high and
     * low)
     */
    override var y: Float
        get() = super.getY()
        set(y) {
            super.y = y
        }

    override fun copy(): CandleEntry {
        return CandleEntry(
            x, high, low, open,
            close, data
        )
    }
}