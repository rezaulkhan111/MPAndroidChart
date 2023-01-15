package com.github.mikephil.charting.formatter

import com.github.mikephil.charting.interfaces.dataprovider.LineDataProvider
import com.github.mikephil.charting.interfaces.datasets.ILineDataSet

/**
 * Default formatter that calculates the position of the filled line.
 *
 * @author Philipp Jahoda
 */
class DefaultFillFormatter : IFillFormatter {

    override fun getFillLinePosition(dataSet: ILineDataSet, dataProvider: LineDataProvider): Float {
        val fillMin: Float
        val chartMaxY = dataProvider.getYChartMax()
        val chartMinY = dataProvider.getYChartMin()
        val data = dataProvider.getLineData()
        fillMin = if (dataSet.getYMax() > 0 && dataSet.getYMin() < 0) {
            0f
        } else {
            val max: Float = if (data!!.getYMax() > 0) 0f else chartMaxY
            val min: Float = if (data.getYMin() < 0) 0f else chartMinY
            if (dataSet.getYMin() >= 0) min else max
        }
        return fillMin
    }
}