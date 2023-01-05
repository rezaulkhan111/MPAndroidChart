package com.github.mikephil.charting.interfaces.datasets

import com.github.mikephil.charting.data.Entry

/**
 * Created by philipp on 21/10/15.
 */
interface IBarLineScatterCandleBubbleDataSet<T : Entry> : IDataSet<T> {
    /**
     * Returns the color that is used for drawing the highlight indicators.
     *
     * @return
     */
    fun getHighLightColor(): Int
}