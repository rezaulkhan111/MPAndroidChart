package com.github.mikephil.charting.data

import android.graphics.Color
import com.github.mikephil.charting.interfaces.datasets.IBarLineScatterCandleBubbleDataSet

/**
 * Baseclass of all DataSets for Bar-, Line-, Scatter- and CandleStickChart.
 *
 * @author Philipp Jahoda
 */
abstract class BarLineScatterCandleBubbleDataSet<T : Entry> constructor(
    yVals: MutableList<T>?,
    label: String?
) : DataSet<T>(yVals, label), IBarLineScatterCandleBubbleDataSet<T> {
    /**
     * default highlight color
     */
    protected var mHighLightColor = Color.rgb(255, 187, 115)

    /**
     * Sets the color that is used for drawing the highlight indicators. Dont
     * forget to resolve the color using getResources().getColor(...) or
     * Color.rgb(...).
     *
     * @param color
     */
    open fun setHighLightColor(color: Int) {
        mHighLightColor = color
    }

    override fun getHighLightColor(): Int {
        return mHighLightColor
    }

    protected open fun copy(barLineScatterCandleBubbleDataSet: BarLineScatterCandleBubbleDataSet<*>) {
        super.copy(barLineScatterCandleBubbleDataSet)
        barLineScatterCandleBubbleDataSet.mHighLightColor = mHighLightColor
    }
}