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
import android.graphics.Color
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
import java.util.ArrayList

class BarDataSet(yVals: MutableList<BarEntry?>, label: String?) :
    BarLineScatterCandleBubbleDataSet<BarEntry?>(yVals, label), IBarDataSet {
    /**
     * the maximum number of bars that are stacked upon each other, this value
     * is calculated from the Entries that are added to the DataSet
     */
    override var stackSize = 1
        private set
    /**
     * Sets the color used for drawing the bar-shadows. The bar shadows is a
     * surface behind the bar that indicates the maximum value. Don't for get to
     * use getResources().getColor(...) to set this. Or Color.rgb(...).
     *
     * @param color
     */
    /**
     * the color used for drawing the bar shadows
     */
    override var barShadowColor = Color.rgb(215, 215, 215)
    /**
     * Returns the width used for drawing borders around the bars.
     * If borderWidth == 0, no border will be drawn.
     *
     * @return
     */
    /**
     * Sets the width used for drawing borders around the bars.
     * If borderWidth == 0, no border will be drawn.
     *
     * @return
     */
    override var barBorderWidth = 0.0f
    /**
     * Returns the color drawing borders around the bars.
     *
     * @return
     */
    /**
     * Sets the color drawing borders around the bars.
     *
     * @return
     */
    override var barBorderColor = Color.BLACK
    /**
     * Set the alpha value (transparency) that is used for drawing the highlight
     * indicator bar. min = 0 (fully transparent), max = 255 (fully opaque)
     *
     * @param alpha
     */
    /**
     * the alpha value used to draw the highlight indicator bar
     */
    override var highLightAlpha = 120
    /**
     * returns the overall entry count, including counting each stack-value
     * individually
     *
     * @return
     */
    /**
     * the overall entry count, including counting each stack-value individually
     */
    var entryCountStacks = 0
        private set
    /**
     * Sets labels for different values of bar-stacks, in case there are one.
     *
     * @param labels
     */
    /**
     * array of labels used to describe the different values of the stacked bars
     */
    override var stackLabels = arrayOf<String>()
    protected var mFills: MutableList<Fill>? = null
    override fun copy(): DataSet<BarEntry?>? {
        val entries: MutableList<BarEntry?> = ArrayList()
        for (i in mEntries!!.indices) {
            entries.add(mEntries!![i]!!.copy())
        }
        val copied = BarDataSet(entries, getLabel())
        copy(copied)
        return copied
    }

    protected fun copy(barDataSet: BarDataSet) {
        super.copy(barDataSet)
        barDataSet.stackSize = stackSize
        barDataSet.barShadowColor = barShadowColor
        barDataSet.barBorderWidth = barBorderWidth
        barDataSet.stackLabels = stackLabels
        barDataSet.highLightAlpha = highLightAlpha
    }

    override val fills: List<Fill>?
        get() = mFills

    override fun getFill(index: Int): Fill? {
        return mFills!![index % mFills!!.size]
    }

    /**
     * This method is deprecated.
     * Use getFills() instead.
     */
    @get:Deprecated("")
    val gradients: List<Fill>?
        get() = mFills

    /**
     * This method is deprecated.
     * Use getFill(...) instead.
     *
     * @param index
     */
    @Deprecated("")
    fun getGradient(index: Int): Fill? {
        return getFill(index)
    }

    /**
     * Sets the start and end color for gradient color, ONLY color that should be used for this DataSet.
     *
     * @param startColor
     * @param endColor
     */
    fun setGradientColor(startColor: Int, endColor: Int) {
        mFills!!.clear()
        mFills!!.add(Fill(startColor, endColor))
    }

    /**
     * This method is deprecated.
     * Use setFills(...) instead.
     *
     * @param gradientColors
     */
    @Deprecated("")
    fun setGradientColors(gradientColors: MutableList<Fill>?) {
        mFills = gradientColors
    }

    /**
     * Sets the fills for the bars in this dataset.
     *
     * @param fills
     */
    fun setFills(fills: MutableList<Fill>?) {
        mFills = fills
    }

    /**
     * Calculates the total number of entries this DataSet represents, including
     * stacks. All values belonging to a stack are calculated separately.
     */
    private fun calcEntryCountIncludingStacks(yVals: List<BarEntry?>) {
        entryCountStacks = 0
        for (i in yVals.indices) {
            val vals = yVals[i].getYVals()
            if (vals == null) entryCountStacks++ else entryCountStacks += vals.size
        }
    }

    /**
     * calculates the maximum stacksize that occurs in the Entries array of this
     * DataSet
     */
    private fun calcStackSize(yVals: List<BarEntry?>) {
        for (i in yVals.indices) {
            val vals = yVals[i].getYVals()
            if (vals != null && vals.size > stackSize) stackSize = vals.size
        }
    }

    override fun calcMinMax(e: BarEntry?) {
        if (e != null && !java.lang.Float.isNaN(e.y)) {
            if (e.yVals == null) {
                if (e.y < mYMin) mYMin = e.y
                if (e.y > mYMax) mYMax = e.y
            } else {
                if (-e.negativeSum < mYMin) mYMin = -e.negativeSum
                if (e.positiveSum > mYMax) mYMax = e.positiveSum
            }
            calcMinMaxX(e)
        }
    }

    override val isStacked: Boolean
        get() = if (stackSize > 1) true else false

    init {
        mHighLightColor = Color.rgb(0, 0, 0)
        calcStackSize(yVals)
        calcEntryCountIncludingStacks(yVals)
    }
}