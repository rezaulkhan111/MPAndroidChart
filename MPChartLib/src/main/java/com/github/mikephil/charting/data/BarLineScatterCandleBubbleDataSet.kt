package com.github.mikephil.charting.data

import android.graphics.Color
import com.github.mikephil.charting.interfaces.datasets.IBarLineScatterCandleBubbleDataSet

/**
 * Baseclass of all DataSets for Bar-, Line-, Scatter- and CandleStickChart.
 *
 * @author Philipp Jahoda
 */
abstract class BarLineScatterCandleBubbleDataSet<T : Entry?>(
    yVals: MutableList<T?>?,
    label: String?
) : DataSet<T>(yVals, label), IBarLineScatterCandleBubbleDataSet<T> {
    /**
     * Sets the color that is used for drawing the highlight indicators. Dont
     * forget to resolve the color using getResources().getColor(...) or
     * Color.rgb(...).
     *
     * @param color
     */
    /**
     * default highlight color
     */
    override var highLightColor = Color.rgb(255, 187, 115)
    protected fun copy(barLineScatterCandleBubbleDataSet: BarLineScatterCandleBubbleDataSet<*>) {
        super.copy(barLineScatterCandleBubbleDataSet)
        barLineScatterCandleBubbleDataSet.highLightColor = highLightColor
    }
}