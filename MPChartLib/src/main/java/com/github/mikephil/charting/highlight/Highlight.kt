package com.github.mikephil.charting.highlight

import com.github.mikephil.charting.components.YAxis.AxisDependency

/**
 * Contains information needed to determine the highlighted value.
 *
 * @author Philipp Jahoda
 */
class Highlight {
    /**
     * the x-value of the highlighted value
     */
    private var mX = Float.NaN

    /**
     * the y-value of the highlighted value
     */
    private var mY = Float.NaN

    /**
     * the x-pixel of the highlight
     */
    private var mXPx = 0f

    /**
     * the y-pixel of the highlight
     */
    private var mYPx = 0f

    /**
     * the index of the data object - in case it refers to more than one
     */
    private var mDataIndex = -1

    /**
     * the index of the dataset the highlighted value is in
     */
    private var mDataSetIndex = 0

    /**
     * index which value of a stacked bar entry is highlighted, default -1
     */
    private var mStackIndex = -1

    /**
     * the axis the highlighted value belongs to
     */
    private var axis: AxisDependency? = null

    /**
     * the x-position (pixels) on which this highlight object was last drawn
     */
    private var mDrawX = 0f

    /**
     * the y-position (pixels) on which this highlight object was last drawn
     */
    private var mDrawY = 0f

    constructor(x: Float, y: Float, dataSetIndex: Int, dataIndex: Int) {
        mX = x
        mY = y
        mDataSetIndex = dataSetIndex
        mDataIndex = dataIndex
    }

    constructor(x: Float, y: Float, dataSetIndex: Int) {
        mX = x
        mY = y
        mDataSetIndex = dataSetIndex
        mDataIndex = -1
    }

    constructor(x: Float, dataSetIndex: Int, stackIndex: Int) {
        Highlight(x, Float.NaN, dataSetIndex)
        mStackIndex = stackIndex
    }

    /**
     * constructor
     *
     * @param x            the x-value of the highlighted value
     * @param y            the y-value of the highlighted value
     * @param dataSetIndex the index of the DataSet the highlighted value belongs to
     */
    constructor(
        x: Float,
        y: Float,
        xPx: Float,
        yPx: Float,
        dataSetIndex: Int,
        axis: AxisDependency?
    ) {
        mX = x
        mY = y
        mXPx = xPx
        mYPx = yPx
        mDataSetIndex = dataSetIndex
        this.axis = axis
    }

    /**
     * Constructor, only used for stacked-barchart.
     *
     * @param x            the index of the highlighted value on the x-axis
     * @param y            the y-value of the highlighted value
     * @param dataSetIndex the index of the DataSet the highlighted value belongs to
     * @param stackIndex   references which value of a stacked-bar entry has been
     * selected
     */
    constructor(
        x: Float,
        y: Float,
        xPx: Float,
        yPx: Float,
        dataSetIndex: Int,
        stackIndex: Int,
        axis: AxisDependency?
    ) {
        Highlight(x, y, xPx, yPx, dataSetIndex, axis)
        mStackIndex = stackIndex
    }

    /**
     * returns the x-value of the highlighted value
     *
     * @return
     */
    fun getX(): Float {
        return mX
    }

    /**
     * returns the y-value of the highlighted value
     *
     * @return
     */
    fun getY(): Float {
        return mY
    }

    /**
     * returns the x-position of the highlight in pixels
     */
    fun getXPx(): Float {
        return mXPx
    }

    /**
     * returns the y-position of the highlight in pixels
     */
    fun getYPx(): Float {
        return mYPx
    }

    /**
     * the index of the data object - in case it refers to more than one
     *
     * @return
     */
    fun getDataIndex(): Int {
        return mDataIndex
    }

    fun setDataIndex(mDataIndex: Int) {
        this.mDataIndex = mDataIndex
    }

    /**
     * returns the index of the DataSet the highlighted value is in
     *
     * @return
     */
    fun getDataSetIndex(): Int {
        return mDataSetIndex
    }

    /**
     * Only needed if a stacked-barchart entry was highlighted. References the
     * selected value within the stacked-entry.
     *
     * @return
     */
    fun getStackIndex(): Int {
        return mStackIndex
    }

    fun isStacked(): Boolean {
        return mStackIndex >= 0
    }

    /**
     * Returns the axis the highlighted value belongs to.
     *
     * @return
     */
    fun getAxis(): AxisDependency? {
        return axis
    }

    /**
     * Sets the x- and y-position (pixels) where this highlight was last drawn.
     *
     * @param x
     * @param y
     */
    fun setDraw(x: Float, y: Float) {
        mDrawX = x
        mDrawY = y
    }

    /**
     * Returns the x-position in pixels where this highlight object was last drawn.
     *
     * @return
     */
    fun getDrawX(): Float {
        return mDrawX
    }

    /**
     * Returns the y-position in pixels where this highlight object was last drawn.
     *
     * @return
     */
    fun getDrawY(): Float {
        return mDrawY
    }

    /**
     * Returns true if this highlight object is equal to the other (compares
     * xIndex and dataSetIndex)
     *
     * @param h
     * @return
     */
    fun equalTo(h: Highlight?): Boolean {
        return if (h == null) false else {
            if (mDataSetIndex == h.mDataSetIndex && mX == h.mX && mStackIndex == h.mStackIndex && mDataIndex == h.mDataIndex) true else false
        }
    }

    override fun toString(): String {
        return ("Highlight, x: " + mX + ", y: " + mY + ", dataSetIndex: " + mDataSetIndex
                + ", stackIndex (only stacked barentry): " + mStackIndex)
    }
}