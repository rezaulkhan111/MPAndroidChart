package com.github.mikephil.charting.interfaces.datasets

import android.graphics.DashPathEffect
import com.github.mikephil.charting.data.Entry

/**
 * Created by Philipp Jahoda on 21/10/15.
 */
interface ILineScatterCandleRadarDataSet<T : Entry> : IBarLineScatterCandleBubbleDataSet<T> {
    /**
     * Returns true if vertical highlight indicator lines are enabled (drawn)
     * @return
     */
    fun isVerticalHighlightIndicatorEnabled(): Boolean

    /**
     * Returns true if vertical highlight indicator lines are enabled (drawn)
     * @return
     */
    fun isHorizontalHighlightIndicatorEnabled(): Boolean

    /**
     * Returns the line-width in which highlight lines are to be drawn.
     * @return
     */
    fun getHighlightLineWidth(): Float

    /**
     * Returns the DashPathEffect that is used for highlighting.
     * @return
     */
    fun getDashPathEffectHighlight(): DashPathEffect
}