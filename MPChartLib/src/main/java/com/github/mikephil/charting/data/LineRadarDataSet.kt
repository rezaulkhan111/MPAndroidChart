package com.github.mikephil.charting.data

import android.annotation.TargetApi
import android.graphics.Color
import android.graphics.drawable.Drawable
import com.github.mikephil.charting.interfaces.datasets.ILineRadarDataSet
import com.github.mikephil.charting.utils.Utils.convertDpToPixel

/**
 * Base dataset for line and radar DataSets.
 *
 * @author Philipp Jahoda
 */
abstract class LineRadarDataSet<T : Entry> : LineScatterCandleRadarDataSet<T>,
    ILineRadarDataSet<T> {

    // TODO: Move to using `Fill` class
    /**
     * the color that is used for filling the line surface
     */
    private var mFillColor = Color.rgb(140, 234, 255)

    /**
     * the drawable to be used for filling the line surface
     */
    private var mFillDrawable: Drawable? = null

    /**
     * transparency used for filling line surface
     */
    private var mFillAlpha = 85

    /**
     * the width of the drawn data lines
     */
    private var mLineWidth = 2.5f

    /**
     * if true, the data will also be drawn filled
     */
    private var mDrawFilled = false


    constructor(yVals: MutableList<T>?, label: String?) : super(yVals, label) {
    }

    override fun getFillColor(): Int {
        return mFillColor
    }

    /**
     * Sets the color that is used for filling the area below the line.
     * Resets an eventually set "fillDrawable".
     *
     * @param color
     */
    open fun setFillColor(color: Int) {
        mFillColor = color
        mFillDrawable = null
    }

    override fun getFillDrawable(): Drawable {
        return mFillDrawable!!
    }

    /**
     * Sets the drawable to be used to fill the area below the line.
     *
     * @param drawable
     */
    @TargetApi(18)
    open fun setFillDrawable(drawable: Drawable?) {
        mFillDrawable = drawable
    }

    override fun getFillAlpha(): Int {
        return mFillAlpha
    }

    /**
     * sets the alpha value (transparency) that is used for filling the line
     * surface (0-255), default: 85
     *
     * @param alpha
     */
    open fun setFillAlpha(alpha: Int) {
        mFillAlpha = alpha
    }

    /**
     * set the line width of the chart (min = 0.2f, max = 10f); default 1f NOTE:
     * thinner line == better performance, thicker line == worse performance
     *
     * @param width
     */
    open fun setLineWidth(width: Float) {
        var width = width
        if (width < 0.0f) width = 0.0f
        if (width > 10.0f) width = 10.0f
        mLineWidth = convertDpToPixel(width)
    }

    override fun getLineWidth(): Float {
        return mLineWidth
    }

    override fun setDrawFilled(filled: Boolean) {
        mDrawFilled = filled
    }

    override fun isDrawFilledEnabled(): Boolean {
        return mDrawFilled
    }

    protected open fun copy(lineRadarDataSet: LineRadarDataSet<*>) {
        super.copy(lineRadarDataSet)
        lineRadarDataSet.mDrawFilled = mDrawFilled
        lineRadarDataSet.mFillAlpha = mFillAlpha
        lineRadarDataSet.mFillColor = mFillColor
        lineRadarDataSet.mFillDrawable = mFillDrawable
        lineRadarDataSet.mLineWidth = mLineWidth
    }
}