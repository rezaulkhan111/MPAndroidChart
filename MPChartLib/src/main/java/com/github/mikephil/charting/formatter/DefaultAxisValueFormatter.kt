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
import java.text.DecimalFormat

/**
 * Created by philipp on 02/06/16.
 */
class DefaultAxisValueFormatter(digits: Int) : IAxisValueFormatter {
    /**
     * decimalformat for formatting
     */
    protected var mFormat: DecimalFormat
    /**
     * Returns the number of decimal digits this formatter uses or -1, if unspecified.
     *
     * @return
     */
    /**
     * the number of decimal digits this formatter uses
     */
    var decimalDigits = 0
        protected set

    override fun getFormattedValue(value: Float, axis: AxisBase?): String {
        // avoid memory allocations here (for performance)
        return mFormat.format(value.toDouble())
    }

    /**
     * Constructor that specifies to how many digits the value should be
     * formatted.
     *
     * @param digits
     */
    init {
        decimalDigits = digits
        val b = StringBuffer()
        for (i in 0 until digits) {
            if (i == 0) b.append(".")
            b.append("0")
        }
        mFormat = DecimalFormat("###,###,###,##0$b")
    }
}