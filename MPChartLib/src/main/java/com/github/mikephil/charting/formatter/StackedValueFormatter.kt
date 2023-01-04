package com.github.mikephil.charting.formatter

import com.github.mikephil.charting.data.BarEntry
import com.github.mikephil.charting.data.Entry
import com.github.mikephil.charting.utils.ViewPortHandler
import java.text.DecimalFormat

/**
 * Created by Philipp Jahoda on 28/01/16.
 *
 *
 * A formatter specifically for stacked BarChart that allows to specify whether the all stack values
 * or just the top value should be drawn.
 */
class StackedValueFormatter : IValueFormatter {

    /**
     * if true, all stack values of the stacked bar entry are drawn, else only top
     */
    private var mDrawWholeStack = false

    /**
     * a string that should be appended behind the value
     */
    private var mAppendix: String? = null

    private var mFormat: DecimalFormat? = null

    /**
     * Constructor.
     *
     * @param drawWholeStack if true, all stack values of the stacked bar entry are drawn, else only top
     * @param appendix       a string that should be appended behind the value
     * @param decimals       the number of decimal digits to use
     */
    constructor(drawWholeStack: Boolean, appendix: String?, decimals: Int) {
        mDrawWholeStack = drawWholeStack
        mAppendix = appendix
        val b = StringBuffer()
        for (i in 0 until decimals) {
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
        if (!mDrawWholeStack && entry is BarEntry) {
            val barEntry = entry
            val vals = barEntry.getYVals()
            if (vals != null) {
                // find out if we are on top of the stack
                return if (vals[vals.size - 1] == value) {

                    // return the "sum" across all stack values
                    mFormat!!.format(barEntry.getY().toDouble()) + mAppendix
                } else {
                    "" // return empty
                }
            }
        }
        // return the "proposed" value
        return mFormat!!.format(value.toDouble()) + mAppendix
    }
}