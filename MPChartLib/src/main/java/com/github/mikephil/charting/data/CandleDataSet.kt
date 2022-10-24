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
import android.graphics.Paint
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
import java.util.ArrayList

/**
 * DataSet for the CandleStickChart.
 *
 * @author Philipp Jahoda
 */
class CandleDataSet(yVals: MutableList<CandleEntry?>?, label: String?) :
    LineScatterCandleRadarDataSet<CandleEntry?>(yVals, label), ICandleDataSet {
    /**
     * the width of the shadow of the candle
     */
    private var mShadowWidth = 3f

    /**
     * should the candle bars show?
     * when false, only "ticks" will show
     *
     *
     * - default: true
     */
    private var mShowCandleBar = true

    /**
     * the space between the candle entries, default 0.1f (10%)
     */
    private var mBarSpace = 0.1f

    /**
     * use candle color for the shadow
     */
    private var mShadowColorSameAsCandle = false

    /**
     * paint style when open < close
     * increasing candlesticks are traditionally hollow
     */
    protected var mIncreasingPaintStyle = Paint.Style.STROKE

    /**
     * paint style when open > close
     * descreasing candlesticks are traditionally filled
     */
    protected var mDecreasingPaintStyle = Paint.Style.FILL

    /**
     * color for open == close
     */
    protected var mNeutralColor = ColorTemplate.COLOR_SKIP

    /**
     * color for open < close
     */
    protected var mIncreasingColor = ColorTemplate.COLOR_SKIP

    /**
     * color for open > close
     */
    protected var mDecreasingColor = ColorTemplate.COLOR_SKIP

    /**
     * shadow line color, set -1 for backward compatibility and uses default
     * color
     */
    protected var mShadowColor = ColorTemplate.COLOR_SKIP
    override fun copy(): DataSet<CandleEntry?>? {
        val entries: MutableList<CandleEntry?> = ArrayList()
        for (i in mEntries!!.indices) {
            entries.add(mEntries!![i]!!.copy())
        }
        val copied = CandleDataSet(entries, getLabel())
        copy(copied)
        return copied
    }

    protected fun copy(candleDataSet: CandleDataSet) {
        super.copy(candleDataSet)
        candleDataSet.mShadowWidth = mShadowWidth
        candleDataSet.mShowCandleBar = mShowCandleBar
        candleDataSet.mBarSpace = mBarSpace
        candleDataSet.mShadowColorSameAsCandle = mShadowColorSameAsCandle
        candleDataSet.mHighLightColor = mHighLightColor
        candleDataSet.mIncreasingPaintStyle = mIncreasingPaintStyle
        candleDataSet.mDecreasingPaintStyle = mDecreasingPaintStyle
        candleDataSet.mNeutralColor = mNeutralColor
        candleDataSet.mIncreasingColor = mIncreasingColor
        candleDataSet.mDecreasingColor = mDecreasingColor
        candleDataSet.mShadowColor = mShadowColor
    }

    override fun calcMinMax(e: CandleEntry?) {
        if (e.getLow() < mYMin) mYMin = e.getLow()
        if (e.getHigh() > mYMax) mYMax = e.getHigh()
        calcMinMaxX(e)
    }

    protected override fun calcMinMaxY(e: CandleEntry) {
        if (e.high < mYMin) mYMin = e.high
        if (e.high > mYMax) mYMax = e.high
        if (e.low < mYMin) mYMin = e.low
        if (e.low > mYMax) mYMax = e.low
    }

    /**
     * Sets the space that is left out on the left and right side of each
     * candle, default 0.1f (10%), max 0.45f, min 0f
     *
     * @param space
     */
    fun setBarSpace(space: Float) {
        var space = space
        if (space < 0f) space = 0f
        if (space > 0.45f) space = 0.45f
        mBarSpace = space
    }

    override fun getBarSpace(): Float {
        return mBarSpace
    }

    /**
     * Sets the width of the candle-shadow-line in pixels. Default 3f.
     *
     * @param width
     */
    fun setShadowWidth(width: Float) {
        mShadowWidth = convertDpToPixel(width)
    }

    override fun getShadowWidth(): Float {
        return mShadowWidth
    }

    /**
     * Sets whether the candle bars should show?
     *
     * @param showCandleBar
     */
    fun setShowCandleBar(showCandleBar: Boolean) {
        mShowCandleBar = showCandleBar
    }

    override fun getShowCandleBar(): Boolean {
        return mShowCandleBar
    }
    // TODO
    /**
     * It is necessary to implement ColorsList class that will encapsulate
     * colors list functionality, because It's wrong to copy paste setColor,
     * addColor, ... resetColors for each time when we want to add a coloring
     * options for one of objects
     *
     * @author Mesrop
     */
    /** BELOW THIS COLOR HANDLING  */
    /**
     * Sets the one and ONLY color that should be used for this DataSet when
     * open == close.
     *
     * @param color
     */
    fun setNeutralColor(color: Int) {
        mNeutralColor = color
    }

    override fun getNeutralColor(): Int {
        return mNeutralColor
    }

    /**
     * Sets the one and ONLY color that should be used for this DataSet when
     * open <= close.
     *
     * @param color
     */
    fun setIncreasingColor(color: Int) {
        mIncreasingColor = color
    }

    override fun getIncreasingColor(): Int {
        return mIncreasingColor
    }

    /**
     * Sets the one and ONLY color that should be used for this DataSet when
     * open > close.
     *
     * @param color
     */
    fun setDecreasingColor(color: Int) {
        mDecreasingColor = color
    }

    override fun getDecreasingColor(): Int {
        return mDecreasingColor
    }

    override fun getIncreasingPaintStyle(): Paint.Style {
        return mIncreasingPaintStyle
    }

    /**
     * Sets paint style when open < close
     *
     * @param paintStyle
     */
    fun setIncreasingPaintStyle(paintStyle: Paint.Style) {
        mIncreasingPaintStyle = paintStyle
    }

    override fun getDecreasingPaintStyle(): Paint.Style {
        return mDecreasingPaintStyle
    }

    /**
     * Sets paint style when open > close
     *
     * @param decreasingPaintStyle
     */
    fun setDecreasingPaintStyle(decreasingPaintStyle: Paint.Style) {
        mDecreasingPaintStyle = decreasingPaintStyle
    }

    override fun getShadowColor(): Int {
        return mShadowColor
    }

    /**
     * Sets shadow color for all entries
     *
     * @param shadowColor
     */
    fun setShadowColor(shadowColor: Int) {
        mShadowColor = shadowColor
    }

    override fun getShadowColorSameAsCandle(): Boolean {
        return mShadowColorSameAsCandle
    }

    /**
     * Sets shadow color to be the same color as the candle color
     *
     * @param shadowColorSameAsCandle
     */
    fun setShadowColorSameAsCandle(shadowColorSameAsCandle: Boolean) {
        mShadowColorSameAsCandle = shadowColorSameAsCandle
    }
}