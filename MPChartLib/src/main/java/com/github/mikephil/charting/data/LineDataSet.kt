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
import android.content.Context
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
import android.util.Log
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

class LineDataSet(yVals: MutableList<Entry?>?, label: String?) :
    LineRadarDataSet<Entry?>(yVals, label), ILineDataSet {
    /**
     * Returns the drawing mode for this line dataset
     *
     * @return
     */
    /**
     * Returns the drawing mode for this LineDataSet
     *
     * @return
     */
    /**
     * Drawing mode for this line dataset
     */
    override var mode = Mode.LINEAR
    /**
     * returns all colors specified for the circles
     *
     * @return
     */
    /**
     * Sets the colors that should be used for the circles of this DataSet.
     * Colors are reused as soon as the number of Entries the DataSet represents
     * is higher than the size of the colors array. Make sure that the colors
     * are already prepared (by calling getResources().getColor(...)) before
     * adding them to the DataSet.
     *
     * @param colors
     */
    /**
     * List representing all colors that are used for the circles
     */
    var circleColors: List<Int>? = null

    /**
     * the color of the inner circles
     */
    private var mCircleHoleColor = Color.WHITE

    /**
     * the radius of the circle-shaped value indicators
     */
    private var mCircleRadius = 8f

    /**
     * the hole radius of the circle-shaped value indicators
     */
    private var mCircleHoleRadius = 4f

    /**
     * sets the intensity of the cubic lines
     */
    private var mCubicIntensity = 0.2f

    /**
     * the path effect of this DataSet that makes dashed lines possible
     */
    override var dashPathEffect: DashPathEffect? = null
        private set

    /**
     * formatter for customizing the position of the fill-line
     */
    private var mFillFormatter: IFillFormatter = DefaultFillFormatter()

    /**
     * if true, drawing circles is enabled
     */
    override var isDrawCirclesEnabled = true
        private set
    private var mDrawCircleHole = true
    override fun copy(): DataSet<Entry?>? {
        val entries: MutableList<Entry?> = ArrayList()
        for (i in mEntries!!.indices) {
            entries.add(mEntries!![i]!!.copy())
        }
        val copied = LineDataSet(entries, getLabel())
        copy(copied)
        return copied
    }

    protected fun copy(lineDataSet: LineDataSet) {
        super.copy(lineDataSet)
        lineDataSet.circleColors = circleColors
        lineDataSet.mCircleHoleColor = mCircleHoleColor
        lineDataSet.mCircleHoleRadius = mCircleHoleRadius
        lineDataSet.mCircleRadius = mCircleRadius
        lineDataSet.mCubicIntensity = mCubicIntensity
        lineDataSet.dashPathEffect = dashPathEffect
        lineDataSet.mDrawCircleHole = mDrawCircleHole
        lineDataSet.isDrawCirclesEnabled = mDrawCircleHole
        lineDataSet.mFillFormatter = mFillFormatter
        lineDataSet.mode = mode
    }

    /**
     * Sets the intensity for cubic lines (if enabled). Max = 1f = very cubic,
     * Min = 0.05f = low cubic effect, Default: 0.2f
     *
     * @param intensity
     */
    override var cubicIntensity: Float
        get() = mCubicIntensity
        set(intensity) {
            var intensity = intensity
            if (intensity > 1f) intensity = 1f
            if (intensity < 0.05f) intensity = 0.05f
            mCubicIntensity = intensity
        }

    /**
     * Sets the radius of the drawn circles.
     * Default radius = 4f, Min = 1f
     *
     * @param radius
     */
    override var circleRadius: Float
        get() = mCircleRadius
        set(radius) {
            if (radius >= 1f) {
                mCircleRadius = convertDpToPixel(radius)
            } else {
                Log.e("LineDataSet", "Circle radius cannot be < 1")
            }
        }

    /**
     * Sets the hole radius of the drawn circles.
     * Default radius = 2f, Min = 0.5f
     *
     * @param holeRadius
     */
    override var circleHoleRadius: Float
        get() = mCircleHoleRadius
        set(holeRadius) {
            if (holeRadius >= 0.5f) {
                mCircleHoleRadius = convertDpToPixel(holeRadius)
            } else {
                Log.e("LineDataSet", "Circle radius cannot be < 0.5")
            }
        }
    /**
     * This function is deprecated because of unclarity. Use getCircleRadius instead.
     */
    /**
     * sets the size (radius) of the circle shpaed value indicators,
     * default size = 4f
     *
     *
     * This method is deprecated because of unclarity. Use setCircleRadius instead.
     *
     * @param size
     */
    @get:Deprecated("")
    @set:Deprecated("")
    var circleSize: Float
        get() = circleRadius
        set(size) {
            circleRadius = size
        }

    /**
     * Enables the line to be drawn in dashed mode, e.g. like this
     * "- - - - - -". THIS ONLY WORKS IF HARDWARE-ACCELERATION IS TURNED OFF.
     * Keep in mind that hardware acceleration boosts performance.
     *
     * @param lineLength  the length of the line pieces
     * @param spaceLength the length of space in between the pieces
     * @param phase       offset, in degrees (normally, use 0)
     */
    fun enableDashedLine(lineLength: Float, spaceLength: Float, phase: Float) {
        dashPathEffect = DashPathEffect(
            floatArrayOf(
                lineLength, spaceLength
            ), phase
        )
    }

    /**
     * Disables the line to be drawn in dashed mode.
     */
    fun disableDashedLine() {
        dashPathEffect = null
    }

    override val isDashedLineEnabled: Boolean
        get() = if (dashPathEffect == null) false else true

    /**
     * set this to true to enable the drawing of circle indicators for this
     * DataSet, default true
     *
     * @param enabled
     */
    fun setDrawCircles(enabled: Boolean) {
        isDrawCirclesEnabled = enabled
    }

    @get:Deprecated("")
    override val isDrawCubicEnabled: Boolean
        get() = mode == Mode.CUBIC_BEZIER

    @get:Deprecated("")
    override val isDrawSteppedEnabled: Boolean
        get() = mode == Mode.STEPPED

    /** ALL CODE BELOW RELATED TO CIRCLE-COLORS  */
    override fun getCircleColor(index: Int): Int {
        return circleColors!![index]
    }

    override fun getCircleColorCount(): Int {
        return circleColors!!.size
    }

    /**
     * Sets the colors that should be used for the circles of this DataSet.
     * Colors are reused as soon as the number of Entries the DataSet represents
     * is higher than the size of the colors array. Make sure that the colors
     * are already prepared (by calling getResources().getColor(...)) before
     * adding them to the DataSet.
     *
     * @param colors
     */
    fun setCircleColors(vararg colors: Int) {
        circleColors = createColors(colors)
    }

    /**
     * ets the colors that should be used for the circles of this DataSet.
     * Colors are reused as soon as the number of Entries the DataSet represents
     * is higher than the size of the colors array. You can use
     * "new String[] { R.color.red, R.color.green, ... }" to provide colors for
     * this method. Internally, the colors are resolved using
     * getResources().getColor(...)
     *
     * @param colors
     */
    fun setCircleColors(colors: IntArray, c: Context) {
        var clrs = circleColors
        if (clrs == null) {
            clrs = ArrayList()
        }
        clrs.clear()
        for (color in colors) {
            clrs.add(c.resources.getColor(color))
        }
        circleColors = clrs
    }

    /**
     * Sets the one and ONLY color that should be used for this DataSet.
     * Internally, this recreates the colors array and adds the specified color.
     *
     * @param color
     */
    fun setCircleColor(color: Int) {
        resetCircleColors()
        circleColors.add(color)
    }

    /**
     * resets the circle-colors array and creates a new one
     */
    fun resetCircleColors() {
        if (circleColors == null) {
            circleColors = ArrayList()
        }
        circleColors.clear()
    }

    /**
     * Sets the color of the inner circle of the line-circles.
     *
     * @param color
     */
    fun setCircleHoleColor(color: Int) {
        mCircleHoleColor = color
    }

    override fun getCircleHoleColor(): Int {
        return mCircleHoleColor
    }

    /**
     * Set this to true to allow drawing a hole in each data circle.
     *
     * @param enabled
     */
    fun setDrawCircleHole(enabled: Boolean) {
        mDrawCircleHole = enabled
    }

    override fun isDrawCircleHoleEnabled(): Boolean {
        return mDrawCircleHole
    }

    /**
     * Sets a custom IFillFormatter to the chart that handles the position of the
     * filled-line for each DataSet. Set this to null to use the default logic.
     *
     * @param formatter
     */
    fun setFillFormatter(formatter: IFillFormatter?) {
        mFillFormatter = formatter ?: DefaultFillFormatter()
    }

    override fun getFillFormatter(): IFillFormatter {
        return mFillFormatter
    }

    enum class Mode {
        LINEAR, STEPPED, CUBIC_BEZIER, HORIZONTAL_BEZIER
    }

    init {

        // mCircleRadius = Utils.convertDpToPixel(4f);
        // mLineWidth = Utils.convertDpToPixel(1f);
        if (circleColors == null) {
            circleColors = ArrayList()
        }
        circleColors.clear()

        // default colors
        // mColors.add(Color.rgb(192, 255, 140));
        // mColors.add(Color.rgb(255, 247, 140));
        circleColors.add(Color.rgb(140, 234, 255))
    }
}