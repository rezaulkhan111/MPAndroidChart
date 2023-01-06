package com.github.mikephil.charting.interfaces.datasets

import android.graphics.Paint
import com.github.mikephil.charting.data.CandleEntry

/**
 * Created by philipp on 21/10/15.
 */
interface ICandleDataSet : ILineScatterCandleRadarDataSet<CandleEntry?> {
    /**
     * Returns the space that is left out on the left and right side of each
     * candle.
     *
     * @return
     */
    fun getBarSpace(): Float

    /**
     * Returns whether the candle bars should show?
     * When false, only "ticks" will show
     *
     * - default: true
     *
     * @return
     */
    fun getShowCandleBar(): Boolean

    /**
     * Returns the width of the candle-shadow-line in pixels.
     *
     * @return
     */
    fun getShadowWidth(): Float

    /**
     * Returns shadow color for all entries
     *
     * @return
     */
    fun getShadowColor(): Int

    /**
     * Returns the neutral color (for open == close)
     *
     * @return
     */
    fun getNeutralColor(): Int

    /**
     * Returns the increasing color (for open < close).
     *
     * @return
     */
    fun getIncreasingColor(): Int

    /**
     * Returns the decreasing color (for open > close).
     *
     * @return
     */
    fun getDecreasingColor(): Int

    /**
     * Returns paint style when open < close
     *
     * @return
     */
    fun getIncreasingPaintStyle(): Paint.Style?

    /**
     * Returns paint style when open > close
     *
     * @return
     */
    fun getDecreasingPaintStyle(): Paint.Style?

    /**
     * Is the shadow color same as the candle color?
     *
     * @return
     */
    fun getShadowColorSameAsCandle(): Boolean
}