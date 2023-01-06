package com.github.mikephil.charting.data

import android.graphics.DashPathEffect
import com.github.mikephil.charting.interfaces.datasets.ILineScatterCandleRadarDataSet
import com.github.mikephil.charting.utils.Utils.convertDpToPixel

/**
 * Created by Philipp Jahoda on 11/07/15.
 */
abstract class LineScatterCandleRadarDataSet<T : Entry?> :
    BarLineScatterCandleBubbleDataSet<T>,
    ILineScatterCandleRadarDataSet<T> {

    private var mDrawVerticalHighlightIndicator = true
    private var mDrawHorizontalHighlightIndicator = true

    /** the width of the highlight indicator lines  */
    private var mHighlightLineWidth = 0.5f

    /** the path effect for dashed highlight-lines  */
    private var mHighlightDashPathEffect: DashPathEffect? = null


    constructor(yVals: MutableList<T>, label: String) : super(yVals, label) {
        mHighlightLineWidth = convertDpToPixel(0.5f)
    }

    /**
     * Enables / disables the horizontal highlight-indicator. If disabled, the indicator is not drawn.
     * @param enabled
     */
    open fun setDrawHorizontalHighlightIndicator(enabled: Boolean) {
        mDrawHorizontalHighlightIndicator = enabled
    }

    /**
     * Enables / disables the vertical highlight-indicator. If disabled, the indicator is not drawn.
     * @param enabled
     */
    open fun setDrawVerticalHighlightIndicator(enabled: Boolean) {
        mDrawVerticalHighlightIndicator = enabled
    }

    /**
     * Enables / disables both vertical and horizontal highlight-indicators.
     * @param enabled
     */
    open fun setDrawHighlightIndicators(enabled: Boolean) {
        setDrawVerticalHighlightIndicator(enabled)
        setDrawHorizontalHighlightIndicator(enabled)
    }

    override fun isVerticalHighlightIndicatorEnabled(): Boolean {
        return mDrawVerticalHighlightIndicator
    }

    override fun isHorizontalHighlightIndicatorEnabled(): Boolean {
        return mDrawHorizontalHighlightIndicator
    }

    /**
     * Sets the width of the highlight line in dp.
     * @param width
     */
    open fun setHighlightLineWidth(width: Float) {
        mHighlightLineWidth = convertDpToPixel(width)
    }

    override fun getHighlightLineWidth(): Float {
        return mHighlightLineWidth
    }

    /**
     * Enables the highlight-line to be drawn in dashed mode, e.g. like this "- - - - - -"
     *
     * @param lineLength the length of the line pieces
     * @param spaceLength the length of space inbetween the line-pieces
     * @param phase offset, in degrees (normally, use 0)
     */
    open fun enableDashedHighlightLine(lineLength: Float, spaceLength: Float, phase: Float) {
        mHighlightDashPathEffect = DashPathEffect(
            floatArrayOf(
                lineLength, spaceLength
            ), phase
        )
    }

    /**
     * Disables the highlight-line to be drawn in dashed mode.
     */
    open fun disableDashedHighlightLine() {
        mHighlightDashPathEffect = null
    }

    /**
     * Returns true if the dashed-line effect is enabled for highlight lines, false if not.
     * Default: disabled
     *
     * @return
     */
    open fun isDashedHighlightLineEnabled(): Boolean {
        return mHighlightDashPathEffect != null
    }

    override fun getDashPathEffectHighlight(): DashPathEffect? {
        return mHighlightDashPathEffect
    }

    protected open fun copy(lineScatterCandleRadarDataSet: LineScatterCandleRadarDataSet<*>) {
        super.copy(lineScatterCandleRadarDataSet)
        lineScatterCandleRadarDataSet.mDrawHorizontalHighlightIndicator =
            mDrawHorizontalHighlightIndicator
        lineScatterCandleRadarDataSet.mDrawVerticalHighlightIndicator =
            mDrawVerticalHighlightIndicator
        lineScatterCandleRadarDataSet.mHighlightLineWidth = mHighlightLineWidth
        lineScatterCandleRadarDataSet.mHighlightDashPathEffect = mHighlightDashPathEffect
    }

}