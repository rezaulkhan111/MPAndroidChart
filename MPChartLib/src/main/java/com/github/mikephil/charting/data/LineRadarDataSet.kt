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
abstract class LineRadarDataSet<T : Entry?>(yVals: MutableList<T?>?, label: String?) :
    LineScatterCandleRadarDataSet<T>(yVals, label), ILineRadarDataSet<T> {
    // TODO: Move to using `Fill` class
    /**
     * the color that is used for filling the line surface
     */
    private var mFillColor = Color.rgb(140, 234, 255)
    /**
     * Sets the drawable to be used to fill the area below the line.
     *
     * @param drawable
     */
    /**
     * the drawable to be used for filling the line surface
     */
    @set:TargetApi(18)
    override var fillDrawable: Drawable? = null
    /**
     * sets the alpha value (transparency) that is used for filling the line
     * surface (0-255), default: 85
     *
     * @param alpha
     */
    /**
     * transparency used for filling line surface
     */
    override var fillAlpha = 85

    /**
     * the width of the drawn data lines
     */
    private var mLineWidth = 2.5f

    /**
     * if true, the data will also be drawn filled
     */
    override var isDrawFilledEnabled = false
        private set

    /**
     * Sets the color that is used for filling the area below the line.
     * Resets an eventually set "fillDrawable".
     *
     * @param color
     */
    override var fillColor: Int
        get() = mFillColor
        set(color) {
            mFillColor = color
            fillDrawable = null
        }

    /**
     * set the line width of the chart (min = 0.2f, max = 10f); default 1f NOTE:
     * thinner line == better performance, thicker line == worse performance
     *
     * @param width
     */
    override var lineWidth: Float
        get() = mLineWidth
        set(width) {
            var width = width
            if (width < 0.0f) width = 0.0f
            if (width > 10.0f) width = 10.0f
            mLineWidth = convertDpToPixel(width)
        }

    override fun setDrawFilled(filled: Boolean) {
        isDrawFilledEnabled = filled
    }

     fun copy(lineRadarDataSet: LineRadarDataSet<*>) {
        super.copy(lineRadarDataSet)
        lineRadarDataSet.isDrawFilledEnabled = isDrawFilledEnabled
        lineRadarDataSet.fillAlpha = fillAlpha
        lineRadarDataSet.mFillColor = mFillColor
        lineRadarDataSet.fillDrawable = fillDrawable
        lineRadarDataSet.mLineWidth = mLineWidth
    }
}