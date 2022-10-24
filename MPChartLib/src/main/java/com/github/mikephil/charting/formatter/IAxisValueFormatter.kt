package com.github.mikephil.charting.formatter

import com.github.mikephil.charting.interfaces.dataprovider.ChartInterface.yChartMax
import com.github.mikephil.charting.interfaces.dataprovider.ChartInterface.yChartMin
import com.github.mikephil.charting.interfaces.dataprovider.LineDataProvider.lineData
import com.github.mikephil.charting.interfaces.datasets.IDataSet.yMax
import com.github.mikephil.charting.interfaces.datasets.IDataSet.yMin
import com.github.mikephil.charting.interfaces.datasets.IDataSet
import com.github.mikephil.charting.interfaces.datasets.ILineDataSet
import com.github.mikephil.charting.interfaces.dataprovider.LineDataProvider
import com.github.mikephil.charting.utils.ViewPortHandler
import com.github.mikephil.charting.formatter.IValueFormatter
import com.github.mikephil.charting.formatter.IAxisValueFormatter
import com.github.mikephil.charting.components.AxisBase
import com.github.mikephil.charting.formatter.IFillFormatter
import com.github.mikephil.charting.data.LineData
import com.github.mikephil.charting.data.BarEntry

/**
 * Created by Philipp Jahoda on 20/09/15.
 * Custom formatter interface that allows formatting of
 * axis labels before they are being drawn.
 */
interface IAxisValueFormatter {
    /**
     * Called when a value from an axis is to be formatted
     * before being drawn. For performance reasons, avoid excessive calculations
     * and memory allocations inside this method.
     *
     * @param value the value to be formatted
     * @param axis  the axis the value belongs to
     * @return
     */
    fun getFormattedValue(value: Float, axis: AxisBase?): String
}