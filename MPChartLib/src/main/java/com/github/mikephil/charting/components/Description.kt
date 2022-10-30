package com.github.mikephil.charting.components

import android.graphics.Paint.Align
import com.github.mikephil.charting.utils.MPPointF
import com.github.mikephil.charting.utils.MPPointF.Companion.getInstance
import com.github.mikephil.charting.utils.Utils.convertDpToPixel

/**
 * Created by Philipp Jahoda on 17/09/16.
 */
class Description() : ComponentBase() {
    init {
        mTextSize = convertDpToPixel(8f)
    }

    /**
     * the text used in the description
     */
    private var text = "Description Label"

    /**
     * the custom position of the description text
     */
    private lateinit var mPosition: MPPointF

    /**
     * the alignment of the description text
     */
    private var mTextAlign = Align.RIGHT


    /**
     * Sets the text to be shown as the description.
     * Never set this to null as this will cause nullpointer exception when drawing with Android Canvas.
     *
     * @param text
     */
    fun setText(text: String) {
        this.text = text
    }

    /**
     * Returns the description text.
     *
     * @return
     */
    fun getText(): String {
        return text
    }

    /**
     * Sets a custom position for the description text in pixels on the screen.
     *
     * @param x - xcoordinate
     * @param y - ycoordinate
     */
    fun setPosition(x: Float, y: Float) {
        if (mPosition == null) {
            mPosition = getInstance(x, y)
        } else {
            mPosition.x = x
            mPosition.y = y
        }
    }

    /**
     * Returns the customized position of the description, or null if none set.
     *
     * @return
     */
    fun getPosition(): MPPointF {
        return mPosition
    }

    /**
     * Sets the text alignment of the description text. Default RIGHT.
     *
     * @param align
     */
    fun setTextAlign(align: Align) {
        mTextAlign = align
    }

    /**
     * Returns the text alignment of the description.
     *
     * @return
     */
    fun getTextAlign(): Align {
        return mTextAlign
    }
}