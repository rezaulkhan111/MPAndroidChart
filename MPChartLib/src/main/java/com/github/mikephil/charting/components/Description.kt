package com.github.mikephil.charting.components

import android.graphics.Paint.Align
import com.github.mikephil.charting.utils.MPPointF
import com.github.mikephil.charting.utils.Utils.convertDpToPixel

/**
 * Created by Philipp Jahoda on 17/09/16.
 */
class Description : ComponentBase() {
    /**
     * Returns the description text.
     *
     * @return
     */
    /**
     * Sets the text to be shown as the description.
     * Never set this to null as this will cause nullpointer exception when drawing with Android Canvas.
     *
     * @param text
     */
    /**
     * the text used in the description
     */
    var text = "Description Label"
    /**
     * Returns the customized position of the description, or null if none set.
     *
     * @return
     */
    /**
     * the custom position of the description text
     */
    var position: MPPointF? = null
        private set
    /**
     * Returns the text alignment of the description.
     *
     * @return
     */
    /**
     * Sets the text alignment of the description text. Default RIGHT.
     *
     * @param align
     */
    /**
     * the alignment of the description text
     */
    var textAlign = Align.RIGHT

    /**
     * Sets a custom position for the description text in pixels on the screen.
     *
     * @param x - xcoordinate
     * @param y - ycoordinate
     */
    fun setPosition(x: Float, y: Float) {
        if (position == null) {
            position = MPPointF.getInstance(x, y)
        } else {
            position!!.x = x
            position!!.y = y
        }
    }

    init {

        // default size
        mTextSize = convertDpToPixel(8f)
    }
}