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
import com.github.mikephil.charting.data.Entry
import java.text.DecimalFormat

/**
 * Default formatter used for formatting values inside the chart. Uses a DecimalFormat with
 * pre-calculated number of digits (depending on max and min value).
 *
 * @author Philipp Jahoda
 */
class DefaultValueFormatter(digits: Int) : IValueFormatter {
    /**
     * DecimalFormat for formatting
     */
    protected var mFormat: DecimalFormat? = null

    /**
     * Returns the number of decimal digits this formatter uses.
     *
     * @return
     */
    var decimalDigits = 0
        protected set

    /**
     * Sets up the formatter with a given number of decimal digits.
     *
     * @param digits
     */
    fun setup(digits: Int) {
        decimalDigits = digits
        val b = StringBuffer()
        for (i in 0 until digits) {
            if (i == 0) b.append(".")
            b.append("0")
        }
        mFormat = DecimalFormat("###,###,###,##0$b")
    }

    override fun getFormattedValue(
        value: Float,
        entry: Entry?,
        dataSetIndex: Int,
        viewPortHandler: ViewPortHandler?
    ): String {

        // put more logic here ...
        // avoid memory allocations here (for performance reasons)
        return mFormat!!.format(value.toDouble())
    }

    /**
     * Constructor that specifies to how many digits the value should be
     * formatted.
     *
     * @param digits
     */
    init {
        setup(digits)
    }
}