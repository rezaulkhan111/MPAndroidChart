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
 * This formatter is used for passing an array of x-axis labels, on whole x steps.
 */
class IndexAxisValueFormatter : IAxisValueFormatter {
    private var mValues = arrayOf<String>()
    private var mValueCount = 0

    /**
     * An empty constructor.
     * Use `setValues` to set the axis labels.
     */
    constructor() {}

    /**
     * Constructor that specifies axis labels.
     *
     * @param values The values string array
     */
    constructor(values: Array<String>?) {
        if (values != null) values = values
    }

    /**
     * Constructor that specifies axis labels.
     *
     * @param values The values string array
     */
    constructor(values: Collection<String>?) {
        if (values != null) values = values.toTypedArray()
    }

    override fun getFormattedValue(value: Float, axis: AxisBase?): String {
        val index = Math.round(value)
        return if (index < 0 || index >= mValueCount || index != value.toInt()) "" else mValues[index]
    }

    var values: Array<String>?
        get() = mValues
        set(values) {
            var values = values
            if (values == null) values = arrayOf()
            mValues = values
            mValueCount = values.size
        }
}