package com.github.mikephil.charting.renderer

import com.github.mikephil.charting.animation.ChartAnimator
import com.github.mikephil.charting.data.DataSet
import com.github.mikephil.charting.data.Entry
import com.github.mikephil.charting.interfaces.dataprovider.BarLineScatterCandleBubbleDataProvider
import com.github.mikephil.charting.interfaces.datasets.IBarLineScatterCandleBubbleDataSet
import com.github.mikephil.charting.interfaces.datasets.IDataSet
import com.github.mikephil.charting.utils.ViewPortHandler

/**
 * Created by Philipp Jahoda on 09/06/16.
 */
abstract class BarLineScatterCandleBubbleRenderer : DataRenderer {
    /**
     * buffer for storing the current minimum and maximum visible x
     */
    protected var mXBounds = XBounds()

    constructor(
        animator: ChartAnimator,
        viewPortHandler: ViewPortHandler?
    ) : super(animator, viewPortHandler) {
    }

    /**
     * Returns true if the DataSet values should be drawn, false if not.
     *
     * @param set
     * @return
     */
    protected open fun shouldDrawValues(set: IDataSet<*>): Boolean {
        return set.isVisible() && (set.isDrawValuesEnabled() || set.isDrawIconsEnabled())
    }

    /**
     * Checks if the provided entry object is in bounds for drawing considering the current animation phase.
     *
     * @param e
     * @param set
     * @return
     */
    protected fun isInBoundsX(e: Entry?, set: IBarLineScatterCandleBubbleDataSet<*>): Boolean {
        if (e == null) return false
        val entryIndex = set.getEntryIndex(e as Nothing).toFloat()
        return !(e == null || entryIndex >= set.getEntryCount() * mAnimator!!.getPhaseX())
    }

    /**
     * Class representing the bounds of the current viewport in terms of indices in the values array of a DataSet.
     */
    inner class XBounds {
        /**
         * minimum visible entry index
         */
        var min = 0

        /**
         * maximum visible entry index
         */
        var max = 0

        /**
         * range of visible entry indices
         */
        var range = 0

        /**
         * Calculates the minimum and maximum x values as well as the range between them.
         *
         * @param chart
         * @param dataSet
         */
        operator fun set(
            chart: BarLineScatterCandleBubbleDataProvider,
            dataSet: IBarLineScatterCandleBubbleDataSet<*>
        ) {
            val phaseX = Math.max(0f, Math.min(1f, mAnimator!!.getPhaseX()))
            val low = chart.getLowestVisibleX()
            val high = chart.getHighestVisibleX()
            val entryFrom = dataSet.getEntryForXValue(low, Float.NaN, DataSet.Rounding.DOWN)
            val entryTo = dataSet.getEntryForXValue(high, Float.NaN, DataSet.Rounding.UP)
            min = if (entryFrom == null) 0 else dataSet.getEntryIndex(entryFrom as Nothing)
            max = if (entryTo == null) 0 else dataSet.getEntryIndex(entryTo as Nothing)
            range = ((max - min) * phaseX).toInt()
        }
    }
}