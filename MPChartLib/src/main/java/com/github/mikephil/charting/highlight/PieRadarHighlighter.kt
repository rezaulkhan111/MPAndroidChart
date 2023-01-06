package com.github.mikephil.charting.highlight

import com.github.mikephil.charting.charts.PieChart
import com.github.mikephil.charting.charts.PieRadarChartBase

/**
 * Created by philipp on 12/06/16.
 */
abstract class PieRadarHighlighter<T : PieRadarChartBase<*>?> : IHighlighter {

    protected var mChart: T? = null

    /**
     * buffer for storing previously highlighted values
     */
    protected var mHighlightBuffer: MutableList<Highlight> = ArrayList()

    constructor(chart: T) {
        mChart = chart
    }

    override fun getHighlight(x: Float, y: Float): Highlight? {
        val touchDistanceToCenter = mChart!!.distanceToCenter(x, y)

        // check if a slice was touched
        return if (touchDistanceToCenter > mChart!!.getRadius()) {

            // if no slice was touched, highlight nothing
            null
        } else {
            var angle = mChart!!.getAngleForPoint(x, y)
            if (mChart is PieChart) {
                angle /= mChart?.getAnimator()!!.getPhaseY()
            }
            val index = mChart!!.getIndexForAngle(angle)

            // check if the index could be found
            if (index < 0 || index >= mChart!!.getData()!!.getMaxEntryCountSet()!!
                    .getEntryCount()
            ) {
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