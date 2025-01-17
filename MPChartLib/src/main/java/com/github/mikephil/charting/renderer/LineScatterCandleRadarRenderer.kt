package com.github.mikephil.charting.renderer

import android.graphics.*
import com.github.mikephil.charting.animation.ChartAnimator
import com.github.mikephil.charting.interfaces.datasets.ILineScatterCandleRadarDataSet
import com.github.mikephil.charting.utils.ViewPortHandler

/**
 * Created by Philipp Jahoda on 11/07/15.
 */
abstract class LineScatterCandleRadarRenderer : BarLineScatterCandleBubbleRenderer {
    /**
     * path that is used for drawing highlight-lines (drawLines(...) cannot be used because of dashes)
     */
    private val mHighlightLinePath = Path()

    constructor(animator: ChartAnimator, viewPortHandler: ViewPortHandler) : super(
        animator,
        viewPortHandler
    ) {
    }

    /**
     * Draws vertical & horizontal highlight-lines if enabled.
     *
     * @param c
     * @param x x-position of the highlight line intersection
     * @param y y-position of the highlight line intersection
     * @param set the currently drawn dataset
     */
    protected open fun drawHighlightLines(
        c: Canvas,
        x: Float,
        y: Float,
        set: ILineScatterCandleRadarDataSet<*>
    ) {

        // set color and stroke-width
        mHighlightPaint!!.color = set.getHighLightColor()
        mHighlightPaint!!.strokeWidth = set.getHighlightLineWidth()

        // draw highlighted lines (if enabled)
        mHighlightPaint!!.pathEffect = set.getDashPathEffectHighlight()

        // draw vertical highlight lines
        if (set.isVerticalHighlightIndicatorEnabled()) {

            // create vertical path
            mHighlightLinePath.reset()
            mHighlightLinePath.moveTo(x, mViewPortHandler!!.contentTop())
            mHighlightLinePath.lineTo(x, mViewPortHandler!!.contentBottom())
            c.drawPath(mHighlightLinePath, mHighlightPaint!!)
        }

        // draw horizontal highlight lines
        if (set.isHorizontalHighlightIndicatorEnabled()) {

            // create horizontal path
            mHighlightLinePath.reset()
            mHighlightLinePath.moveTo(mViewPortHandler!!.contentLeft(), y)
            mHighlightLinePath.lineTo(mViewPortHandler!!.contentRight(), y)
            c.drawPath(mHighlightLinePath, mHighlightPaint!!)
        }
    }
}