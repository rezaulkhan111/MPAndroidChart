package com.github.mikephil.charting.formatter

import com.github.mikephil.charting.components.AxisBase

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
        values?.let { setValues(it) }
    }

    /**
     * Constructor that specifies axis labels.
     *
     * @param values The values string array
     */
    constructor(values: Collection<String>?) {
        if (values != null) setValues(values.toTypedArray())
    }

    override fun getFormattedValue(value: Float, axis: AxisBase?): String {
        val index = Math.round(value)
        return if (index < 0 || index >= mValueCount || index != value.toInt()) "" else mValues[index]
    }

    fun getValues(): Array<String> {
        return mValues
    }

    fun setValues(values: Array<String>?) {
        var lValues = values
        if (lValues == null) lValues = arrayOf()
        mValues = lValues
        mValueCount = lValues.size
    }
}