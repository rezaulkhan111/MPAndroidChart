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
import java.util.ArrayList

class PieDataSet(yVals: MutableList<PieEntry?>?, label: String?) : DataSet<PieEntry?>(yVals, label),
    IPieDataSet {
    /**
     * the space in pixels between the chart-slices, default 0f
     */
    private var mSliceSpace = 0f

    /**
     * When enabled, slice spacing will be 0.0 when the smallest value is going to be
     * smaller than the slice spacing itself.
     *
     * @return
     */
    override var isAutomaticallyDisableSliceSpacingEnabled = false
        private set

    /**
     * indicates the selection distance of a pie slice
     */
    private var mShift = 18f
    override var xValuePosition = ValuePosition.INSIDE_SLICE
    override var yValuePosition = ValuePosition.INSIDE_SLICE

    /**
     * When valuePosition is OutsideSlice, indicates line color
     */
    override var valueLineColor = -0x1000000
    override var isUseValueColorForLineEnabled = false
        private set

    /**
     * When valuePosition is OutsideSlice, indicates line width
     */
    override var valueLineWidth = 1.0f

    /**
     * When valuePosition is OutsideSlice, indicates offset as percentage out of the slice size
     */
    override var valueLinePart1OffsetPercentage = 75f

    /**
     * When valuePosition is OutsideSlice, indicates length of first half of the line
     */
    override var valueLinePart1Length = 0.3f

    /**
     * When valuePosition is OutsideSlice, indicates length of second half of the line
     */
    override var valueLinePart2Length = 0.4f

    /**
     * When valuePosition is OutsideSlice, this allows variable line length
     */
    override var isValueLineVariableLength = true
    /** Gets the color for the highlighted sector  */
    /** Sets the color for the highlighted sector (null for using entry color)  */
    override var highlightColor: Int? = null
    override fun copy(): DataSet<PieEntry?> {
        val entries: MutableList<PieEntry?> = ArrayList()
        for (i in mEntries!!.indices) {
            entries.add(mEntries!![i]!!.copy())
        }
        val copied = PieDataSet(entries, getLabel())
        copy(copied)
        return copied
    }

    protected fun copy(pieDataSet: PieDataSet?) {
        super.copy(pieDataSet)
    }

    override fun calcMinMax(e: PieEntry?) {
        if (e == null) return
        calcMinMaxY(e)
    }

    /**
     * Sets the space that is left out between the piechart-slices in dp.
     * Default: 0 --> no space, maximum 20f
     *
     * @param spaceDp
     */
    override var sliceSpace: Float
        get() = mSliceSpace
        set(spaceDp) {
            var spaceDp = spaceDp
            if (spaceDp > 20) spaceDp = 20f
            if (spaceDp < 0) spaceDp = 0f
            mSliceSpace = convertDpToPixel(spaceDp)
        }

    /**
     * When enabled, slice spacing will be 0.0 when the smallest value is going to be
     * smaller than the slice spacing itself.
     *
     * @param autoDisable
     */
    fun setAutomaticallyDisableSliceSpacing(autoDisable: Boolean) {
        isAutomaticallyDisableSliceSpacingEnabled = autoDisable
    }

    /**
     * sets the distance the highlighted piechart-slice of this DataSet is
     * "shifted" away from the center of the chart, default 12f
     *
     * @param shift
     */
    override var selectionShift: Float
        get() = mShift
        set(shift) {
            mShift = convertDpToPixel(shift)
        }
    /**
     * This method is deprecated.
     * Use isUseValueColorForLineEnabled() instead.
     */
    /**
     * This method is deprecated.
     * Use setUseValueColorForLine(...) instead.
     *
     * @param enabled
     */
    @get:Deprecated("")
    @set:Deprecated("")
    var isUsingSliceColorAsValueLineColor: Boolean
        get() = isUseValueColorForLineEnabled
        set(enabled) {
            setUseValueColorForLine(enabled)
        }

    fun setUseValueColorForLine(enabled: Boolean) {
        isUseValueColorForLineEnabled = enabled
    }

    enum class ValuePosition {
        INSIDE_SLICE, OUTSIDE_SLICE
    }
}