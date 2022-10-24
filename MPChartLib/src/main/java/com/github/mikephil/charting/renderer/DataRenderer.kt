package com.github.mikephil.charting.renderer

import android.graphics.*
import android.graphics.Paint.Align
import com.github.mikephil.charting.animation.ChartAnimator
import com.github.mikephil.charting.data.*
import com.github.mikephil.charting.formatter.IValueFormatter
import com.github.mikephil.charting.highlight.Highlight
import com.github.mikephil.charting.interfaces.dataprovider.ChartInterface
import com.github.mikephil.charting.interfaces.datasets.IDataSet
import com.github.mikephil.charting.renderer.*
import com.github.mikephil.charting.utils.*

/**
 * Superclass of all render classes for the different data types (line, bar, ...).
 *
 * @author Philipp Jahoda
 */
abstract class DataRenderer(
    /**
     * the animator object used to perform animations on the chart data
     */
    protected var mAnimator: ChartAnimator, viewPortHandler: ViewPortHandler?
) : Renderer(viewPortHandler) {
    /**
     * Returns the Paint object used for rendering.
     *
     * @return
     */
    /**
     * main paint object used for rendering
     */
    var paintRender: Paint
        protected set
    /**
     * Returns the Paint object this renderer uses for drawing highlight
     * indicators.
     *
     * @return
     */
    /**
     * paint used for highlighting values
     */
    var paintHighlight: Paint
        protected set
    protected var mDrawPaint: Paint
    /**
     * Returns the Paint object this renderer uses for drawing the values
     * (value-text).
     *
     * @return
     */
    /**
     * paint object for drawing values (text representing values of chart
     * entries)
     */
    var paintValues: Paint
        protected set

    protected open fun isDrawingValuesAllowed(chart: ChartInterface): Boolean {
        return chart.data.entryCount < chart.maxVisibleCount
        * mViewPortHandler.scaleX
    }

    /**
     * Applies the required styling (provided by the DataSet) to the value-paint
     * object.
     *
     * @param set
     */
    protected fun applyValueTextStyle(set: IDataSet<*>) {
        paintValues.typeface = set.valueTypeface
        paintValues.textSize = set.valueTextSize
    }

    /**
     * Initializes the buffers used for rendering with a new size. Since this
     * method performs memory allocations, it should only be called if
     * necessary.
     */
    abstract fun initBuffers()

    /**
     * Draws the actual data in form of lines, bars, ... depending on Renderer subclass.
     *
     * @param c
     */
    abstract fun drawData(c: Canvas)

    /**
     * Loops over all Entrys and draws their values.
     *
     * @param c
     */
    abstract fun drawValues(c: Canvas)

    /**
     * Draws the value of the given entry by using the provided IValueFormatter.
     *
     * @param c            canvas
     * @param formatter    formatter for custom value-formatting
     * @param value        the value to be drawn
     * @param entry        the entry the value belongs to
     * @param dataSetIndex the index of the DataSet the drawn Entry belongs to
     * @param x            position
     * @param y            position
     * @param color
     */
    fun drawValue(
        c: Canvas,
        formatter: IValueFormatter,
        value: Float,
        entry: Entry?,
        dataSetIndex: Int,
        x: Float,
        y: Float,
        color: Int
    ) {
        paintValues.color = color
        c.drawText(
            formatter.getFormattedValue(value, entry, dataSetIndex, mViewPortHandler),
            x,
            y,
            paintValues
        )
    }

    /**
     * Draws any kind of additional information (e.g. line-circles).
     *
     * @param c
     */
    abstract fun drawExtras(c: Canvas)

    /**
     * Draws all highlight indicators for the values that are currently highlighted.
     *
     * @param c
     * @param indices the highlighted values
     */
    abstract fun drawHighlighted(c: Canvas, indices: Array<Highlight>)

    init {
        paintRender = Paint(Paint.ANTI_ALIAS_FLAG)
        paintRender.style = Paint.Style.FILL
        mDrawPaint = Paint(Paint.DITHER_FLAG)
        paintValues = Paint(Paint.ANTI_ALIAS_FLAG)
        paintValues.color = Color.rgb(63, 63, 63)
        paintValues.textAlign = Align.CENTER
        paintValues.textSize = Utils.convertDpToPixel(9f)
        paintHighlight = Paint(Paint.ANTI_ALIAS_FLAG)
        paintHighlight.style = Paint.Style.STROKE
        paintHighlight.strokeWidth = 2f
        paintHighlight.color = Color.rgb(255, 187, 115)
    }
}