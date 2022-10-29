package com.github.mikephil.charting.data

import android.graphics.DashPathEffect
import com.github.mikephil.charting.interfaces.datasets.ILineScatterCandleRadarDataSet
import com.github.mikephil.charting.utils.Utils.convertDpToPixel

/**
 * Created by Philipp Jahoda on 11/07/15.
 */
abstract class LineScatterCandleRadarDataSet<T : Entry?>(yVals: MutableList<T?>?, label: String?) :
    BarLineScatterCandleBubbleDataSet<T>(yVals, label), ILineScatterCandleRadarDataSet<T> {
    override var isVerticalHighlightIndicatorEnabled = true
         set
    override var isHorizontalHighlightIndicatorEnabled = true
         set

    /** the width of the highlight indicator lines  */
     var mHighlightLineWidth = 0.5f

    /** the path effect for dashed highlight-lines  */
    override var dashPathEffectHighlight: DashPathEffect? = null
         set

    /**
     * Enables / disables the horizontal highlight-indicator. If disabled, the indicator is not drawn.
     * @param enabled
     */
    fun setDrawHorizontalHighlightIndicator(enabled: Boolean) {
        isHorizontalHighlightIndicatorEnabled = enabled
    }

    /**
     * Enables / disables the vertical highlight-indicator. If disabled, the indicator is not drawn.
     * @param enabled
     */
    fun setDrawVerticalHighlightIndicator(enabled: Boolean) {
        isVerticalHighlightIndicatorEnabled = enabled
    }

    /**
     * Enables / disables both vertical and horizontal highlight-indicators.
     * @param enabled
     */
    fun setDrawHighlightIndicators(enabled: Boolean) {
        setDrawVerticalHighlightIndicator(enabled)
        setDrawHorizontalHighlightIndicator(enabled)
    }

    /**
     * Sets the width of the highlight line in dp.
     * @param width
     */
    override var highlightLineWidth: Float
        get() = mHighlightLineWidth
        set(width) {
            mHighlightLineWidth = convertDpToPixel(width)
        }

    /**
     * Enables the highlight-line to be drawn in dashed mode, e.g. like this "- - - - - -"
     *
     * @param lineLength the length of the line pieces
     * @param spaceLength the length of space inbetween the line-pieces
     * @param phase offset, in degrees (normally, use 0)
     */
    fun enableDashedHighlightLine(lineLength: Float, spaceLength: Float, phase: Float) {
        dashPathEffectHighlight = DashPathEffect(
            floatArrayOf(
                lineLength, spaceLength
            ), phase
        )
    }

    /**
     * Disables the highlight-line to be drawn in dashed mode.
     */
    fun disableDashedHighlightLine() {
        dashPathEffectHighlight = null
    }

    /**
     * Returns true if the dashed-line effect is enabled for highlight lines, false if not.
     * Default: disabled
     *
     * @return
     */
    val isDashedHighlightLineEnabled: Boolean
        get() = if (dashPathEffectHighlight == null) false else true

     fun copy(lineScatterCandleRadarDataSet: LineScatterCandleRadarDataSet<*>) {
        super.copy(lineScatterCandleRadarDataSet)
        lineScatterCandleRadarDataSet.isHorizontalHighlightIndicatorEnabled =
            isHorizontalHighlightIndicatorEnabled
        lineScatterCandleRadarDataSet.isVerticalHighlightIndicatorEnabled =
            isVerticalHighlightIndicatorEnabled
        lineScatterCandleRadarDataSet.mHighlightLineWidth = mHighlightLineWidth
        lineScatterCandleRadarDataSet.dashPathEffectHighlight = dashPathEffectHighlight
    }

    init {
        mHighlightLineWidth = convertDpToPixel(0.5f)
    }
}