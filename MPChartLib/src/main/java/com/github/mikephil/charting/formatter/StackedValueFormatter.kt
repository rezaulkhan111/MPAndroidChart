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
 * Created by Philipp Jahoda on 28/01/16.
 *
 *
 * A formatter specifically for stacked BarChart that allows to specify whether the all stack values
 * or just the top value should be drawn.
 */
class StackedValueFormatter(
    /**
     * if true, all stack values of the stacked bar entry are drawn, else only top
     */
    private val mDrawWholeStack: Boolean,
    /**
     * a string that should be appended behind the value
     */
    private val mAppendix: String, decimals: Int
) : IValueFormatter {
    private val mFormat: DecimalFormat
    override fun getFormattedValue(
        value: Float,
        entry: Entry?,
        dataSetIndex: Int,
        viewPortHandler: ViewPortHandler?
    ): String {
        if (!mDrawWholeStack && entry is BarEntry) {
            val barEntry = entry
            val vals = barEntry.yVals
            if (vals != null) {

                // find out if we are on top of the stack
                return if (vals[vals.size - 1] == value) {

                    // return the "sum" across all stack values
                    mFormat.format(barEntry.y.toDouble()) + mAppendix
                } else {
                    "" // return empty
                }
            }
        }

        // return the "proposed" value
        return mFormat.format(value.toDouble()) + mAppendix
    }

    /**
     * Constructor.
     *
     * @param drawWholeStack if true, all stack values of the stacked bar entry are drawn, else only top
     * @param appendix       a string that should be appended behind the value
     * @param decimals       the number of decimal digits to use
     */
    init {
        val b = StringBuffer()
        for (i in 0 until decimals) {
            if (i == 0) b.append(".")
            b.append("0")
        }
        mFormat = DecimalFormat("###,###,###,##0$b")
    }
}