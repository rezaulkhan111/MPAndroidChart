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
        var fillMin = 0f
        val chartMaxY = dataProvider.getYChartMax()
        val chartMinY = dataProvider.getYChartMin()
        val data = dataProvider.getLineData()
        if (dataSet.getYMax() > 0 && dataSet.getYMin() < 0) {
            fillMin = 0f
        } else {
            val max: Float
            val min: Float
            max = if (data.getYMax() > 0) 0f else chartMaxY
            min = if (data.getYMin() < 0) 0f else chartMinY
            fillMin = if (dataSet.getYMin() >= 0) min else max
        }
        return fillMin
    }
}