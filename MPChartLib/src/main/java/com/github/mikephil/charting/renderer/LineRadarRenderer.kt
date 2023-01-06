package com.github.mikephil.charting.renderer

import android.graphics.*
import android.graphics.drawable.Drawable
import com.github.mikephil.charting.animation.ChartAnimator
import com.github.mikephil.charting.utils.Utils
import com.github.mikephil.charting.utils.ViewPortHandler

/**
 * Created by Philipp Jahoda on 25/01/16.
 */
abstract class LineRadarRenderer : LineScatterCandleRadarRenderer {

    constructor(animator: ChartAnimator, viewPortHandler: ViewPortHandler) : super(
        animator,
        viewPortHandler
    ) {
    }

    /**
     * Draws the provided path in filled mode with the provided drawable.
     *
     * @param c
     * @param filledPath
     * @param drawable
     */
    protected open fun drawFilledPath(c: Canvas, filledPath: Path?, drawable: Drawable) {
        if (clipPathSupported()) {
            val save = c.save()
            c.clipPath(filledPath!!)
            drawable.setBounds(
                mViewPortHandler!!.contentLeft().toInt(),
                mViewPortHandler!!.contentTop().toInt(),
                mViewPortHandler!!.contentRight().toInt(),
                mViewPortHandler!!.contentBottom().toInt()
            )
            drawable.draw(c)
            c.restoreToCount(save)
        } else {
            throw RuntimeException(
                "Fill-drawables not (yet) supported below API level 18, " +
                        "this code was run on API level " + Utils.getSDKInt() + "."
            )
        }
    }

    /**
     * Draws the provided path in filled mode with the provided color and alpha.
     * Special thanks to Angelo Suzuki (https://github.com/tinsukE) for this.
     *
     * @param c
     * @param filledPath
     * @param fillColor
     * @param fillAlpha
     */
    protected open fun drawFilledPath(
        c: Canvas,
        filledPath: Path?,
        fillColor: Int,
        fillAlpha: Int
    ) {
        val color = (fillAlpha shl 24) or (fillColor and 0xffffff)
        if (clipPathSupported()) {
            val save = c.save()
            c.clipPath((filledPath)!!)
            c.drawColor(color)
            c.restoreToCount(save)
        } else {

            // save
            val previous = mRenderPaint!!.style
            val previousColor = mRenderPaint!!.color

            // set
            mRenderPaint!!.style = Paint.Style.FILL
            mRenderPaint!!.color = color
            c.drawPath((filledPath)!!, (mRenderPaint)!!)

            // restore
            mRenderPaint!!.color = previousColor
            mRenderPaint!!.style = previous
        }
    }

    /**
     * Clip path with hardware acceleration only working properly on API level 18 and above.
     *
     * @return
     */
    private fun clipPathSupported(): Boolean {
        return Utils.getSDKInt() >= 18
    }
}