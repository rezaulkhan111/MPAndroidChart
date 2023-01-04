package com.github.mikephil.charting.formatter

import com.github.mikephil.charting.components.AxisBase
import java.text.DecimalFormat

/**
 * Created by philipp on 02/06/16.
 */
class DefaultAxisValueFormatter : IAxisValueFormatter {
    /**
     * decimalformat for formatting
     */
    private var mFormat: DecimalFormat? = null

    /**
     * the number of decimal digits this formatter uses
     */
    private var digits = 0

    /**
     * Constructor that specifies to how many digits the value should be
     * formatted.
     *
     * @param digits
     */
    constructor(digits: Int) {
        this.digits = digits
        val b = StringBuffer()
        for (i in 0 until digits) {
            if (i == 0) b.append(".")
            b.append("0")
        }
        mFormat = DecimalFormat("###,###,###,##0$b")
    }

    override fun getFormattedValue(value: Float, axis: AxisBase?): String {
        // avoid memory allocations here (for performance)
        return mFormat!!.format(value.toDouble())
    }

    /**
     * Returns the number of decimal digits this formatter uses or -1, if unspecified.
     *
     * @return
     */
    fun getDecimalDigits(): Int {
        return digits
    }
}