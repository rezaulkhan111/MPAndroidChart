package com.github.mikephil.charting.highlight

import com.github.mikephil.charting.charts.PieChart
import com.github.mikephil.charting.charts.PieRadarChartBase
import com.github.mikephil.charting.interfaces.datasets.IDataSet.entryCount

/**
 * Created by philipp on 12/06/16.
 */
abstract class PieRadarHighlighter<T : PieRadarChartBase<*>?>(protected var mChart: T) :
    IHighlighter {
    /**
     * buffer for storing previously highlighted values
     */
    protected var mHighlightBuffer: List<Highlight> = ArrayList()
    override fun getHighlight(x: Float, y: Float): Highlight? {
        val touchDistanceToCenter = mChart!!.distanceToCenter(x, y)

        // check if a slice was touched
        return if (touchDistanceToCenter > mChart!!.radius) {

            // if no slice was touched, highlight nothing
            null
        } else {
            var angle = mChart!!.getAngleForPoint(x, y)
            if (mChart is PieChart) {
                angle /= mChart.animator.phaseY
            }
            val index = mChart!!.getIndexForAngle(angle)

            // check if the index could be found
            if (index < 0 || index >= mChart.getData().getMaxEntryCountSet().entryCount) {
                null
            } else {
                getClosestHighlight(index, x, y)
            }
        }
    }

    /**
     * Returns the closest Highlight object of the given objects based on the touch position inside the chart.
     *
     * @param index
     * @param x
     * @param y
     * @return
     */
    protected abstract fun getClosestHighlight(index: Int, x: Float, y: Float): Highlight?
}