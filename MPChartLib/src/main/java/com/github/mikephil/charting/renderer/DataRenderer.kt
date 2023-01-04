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
import com.github.mikephil.charting.utils.Utils.convertDpToPixel

/**
 * Superclass of all render classes for the different data types (line, bar, ...).
 *
 * @author Philipp Jahoda
 */
abstract class DataRenderer : Renderer {
    /**
     * the animator object used to perform animations on the chart data
     */
    protected var mAnimator: ChartAnimator? = null

    /**
     * main paint object used for rendering
     */
    protected var mRenderPaint: Paint? = null

    /**
     * paint used for highlighting values
     */
    protected var mHighlightPaint: Paint? = null

    protected var mDrawPaint: Paint? = null

    /**
     * paint object for drawing values (text representing values of chart
     * entries)
     */
    protected var mValuePaint: Paint? = null

    constructor(
        animator: ChartAnimator?,
        viewPortHandler: ViewPortHandler?
    ) : super(viewPortHandler) {
        mAnimator = animator
        mRenderPaint = Paint(Paint.ANTI_ALIAS_FLAG)
        mRenderPaint!!.style = Paint.Style.FILL
        mDrawPaint = Paint(Paint.DITHER_FLAG)
        mValuePaint = Paint(Paint.ANTI_ALIAS_FLAG)
        mValuePaint!!.color = Color.rgb(63, 63, 63)
        mValuePaint!!.textAlign = Align.CENTER
        mValuePaint!!.textSize = convertDpToPixel(9f)
        mHighlightPaint = Paint(Paint.ANTI_ALIAS_FLAG)
        mHighlightPaint!!.style = Paint.Style.STROKE
        mHighlightPaint!!.strokeWidth = 2f
        mHighlightPaint!!.color = Color.rgb(255, 187, 115)
    }

    protected open fun isDrawingValuesAllowed(chart: ChartInterface): Boolean {
        return chart.getData()
            .getEntryCount() < chart.getMaxVisibleCount() * mViewPortHandler!!.getScaleX()
    }

    /**
     * Returns the Paint object this renderer uses for drawing the values
     * (value-text).
     *
     * @return
     */
    open fun getPaintValues(): Paint? {
        return mValuePaint
    }

    /**
     * Returns the Paint object this renderer uses for drawing highlight
     * indicators.
     *
     * @return
     */
    open fun getPaintHighlight(): Paint? {
        return mHighlightPaint
    }

    /**
     * Returns the Paint object used for rendering.
     *
     * @return
     */
    open fun getPaintRender(): Paint? {
        return mRenderPaint
    }

    /**
     * Applies the required styling (provided by the DataSet) to the value-paint
     * object.
     *
     * @param set
     */
    protected open fun applyValueTextStyle(set: IDataSet<*>) {
        mValuePaint!!.typeface = set.getValueTypeface()
        mValuePaint!!.textSize = set.getValueTextSize()
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
    abstract fun drawData(c: Canvas?)

    /**
     * Loops over all Entrys and draws their values.
     *
     * @param c
     */
    abstract fun drawValues(c: Canvas?)

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
    open fun drawValue(
        c: Canvas,
        formatter: IValueFormatter,
        value: Float,
        entry: Entry?,
        dataSetIndex: Int,
        x: Float,
        y: Float,
        color: Int
    ) {
        mValuePaint!!.color = color
        c.drawText(
            formatter.getFormattedValue(value, entry, dataSetIndex, mViewPortHandler), x, y,
            mValuePaint!!
        )
    }

    /**
     * Draws any kind of additional information (e.g. line-circles).
     *
     * @param c
     */
    abstract fun drawExtras(c: Canvas?)

    /**
     * Draws all highlight indicators for the values that are currently highlighted.
     *
     * @param c
     * @param indices the highlighted values
     */
    abstract fun drawHighlighted(c: Canvas?, indices: Array<Highlight>?)
}