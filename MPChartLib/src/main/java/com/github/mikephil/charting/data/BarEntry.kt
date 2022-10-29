package com.github.mikephil.charting.data

import android.annotation.SuppressLint
import android.graphics.drawable.Drawable
import com.github.mikephil.charting.highlight.Range

/**
 * Entry class for the BarChart. (especially stacked bars)
 *
 * @author Philipp Jahoda
 */
@SuppressLint("ParcelCreator")
class BarEntry : Entry {
    /**
     * Returns the stacked values this BarEntry represents, or null, if only a single value is represented (then, use
     * getY()).
     *
     * @return
     */
    /**
     * the values the stacked barchart holds
     */
    var yVals: FloatArray?
        private set
    /**
     * Returns the ranges of the individual stack-entries. Will return null if this entry is not stacked.
     *
     * @return
     */
    /**
     * the ranges for the individual stack values - automatically calculated
     */
    var ranges: Array<Range?>
        private set
    /**
     * Returns the sum of all negative values this entry (if stacked) contains. (this is a positive number)
     *
     * @return
     */
    /**
     * the sum of all negative values this entry (if stacked) contains
     */
    var negativeSum = 0f
        private set
    /**
     * Reuturns the sum of all positive values this entry (if stacked) contains.
     *
     * @return
     */
    /**
     * the sum of all positive values this entry (if stacked) contains
     */
    var positiveSum = 0f
        private set

    /**
     * Constructor for normal bars (not stacked).
     *
     * @param x
     * @param y
     */
    constructor(x: Float, y: Float) : super(x, y) {}

    /**
     * Constructor for normal bars (not stacked).
     *
     * @param x
     * @param y
     * @param data - Spot for additional data this Entry represents.
     */
    constructor(x: Float, y: Float, data: Any?) : super(x, y, data) {}

    /**
     * Constructor for normal bars (not stacked).
     *
     * @param x
     * @param y
     * @param icon - icon image
     */
    constructor(x: Float, y: Float, icon: Drawable?) : super(x, y, icon) {}

    /**
     * Constructor for normal bars (not stacked).
     *
     * @param x
     * @param y
     * @param icon - icon image
     * @param data - Spot for additional data this Entry represents.
     */
    constructor(x: Float, y: Float, icon: Drawable?, data: Any?) : super(x, y, icon, data) {}

    /**
     * Constructor for stacked bar entries. One data object for whole stack
     *
     * @param x
     * @param vals - the stack values, use at least 2
     */
    constructor(x: Float, vals: FloatArray?) : super(x, calcSum(vals)) {
        yVals = vals
        calcPosNegSum()
        calcRanges()
    }

    /**
     * Constructor for stacked bar entries. One data object for whole stack
     *
     * @param x
     * @param vals - the stack values, use at least 2
     * @param data - Spot for additional data this Entry represents.
     */
    constructor(x: Float, vals: FloatArray?, data: Any?) : super(x, calcSum(vals), data) {
        yVals = vals
        calcPosNegSum()
        calcRanges()
    }

    /**
     * Constructor for stacked bar entries. One data object for whole stack
     *
     * @param x
     * @param vals - the stack values, use at least 2
     * @param icon - icon image
     */
    constructor(x: Float, vals: FloatArray?, icon: Drawable?) : super(x, calcSum(vals), icon) {
        yVals = vals
        calcPosNegSum()
        calcRanges()
    }

    /**
     * Constructor for stacked bar entries. One data object for whole stack
     *
     * @param x
     * @param vals - the stack values, use at least 2
     * @param icon - icon image
     * @param data - Spot for additional data this Entry represents.
     */
    constructor(x: Float, vals: FloatArray?, icon: Drawable?, data: Any?) : super(
        x,
        calcSum(vals),
        icon,
        data
    ) {
        yVals = vals
        calcPosNegSum()
        calcRanges()
    }

    /**
     * Returns an exact copy of the BarEntry.
     */
    override fun copy(): BarEntry {
        val copied = BarEntry(x, y, data)
        copied.setVals(yVals)
        return copied
    }

    /**
     * Set the array of values this BarEntry should represent.
     *
     * @param vals
     */
    fun setVals(vals: FloatArray?) {
        setY(calcSum(vals))
        yVals = vals
        calcPosNegSum()
        calcRanges()
    }

    /**
     * Returns the value of this BarEntry. If the entry is stacked, it returns the positive sum of all values.
     *
     * @return
     */
    override var y: Float
        get() = super.getY()
        set(y) {
            super.y = y
        }

    /**
     * Returns true if this BarEntry is stacked (has a values array), false if not.
     *
     * @return
     */
    val isStacked: Boolean
        get() = yVals != null

    /**
     * Use `getSumBelow(stackIndex)` instead.
     */
    @Deprecated("")
    fun getBelowSum(stackIndex: Int): Float {
        return getSumBelow(stackIndex)
    }

    fun getSumBelow(stackIndex: Int): Float {
        if (yVals == null) return 0
        var remainder = 0f
        var index = yVals!!.size - 1
        while (index > stackIndex && index >= 0) {
            remainder += yVals!![index]
            index--
        }
        return remainder
    }

    private fun calcPosNegSum() {
        if (yVals == null) {
            negativeSum = 0f
            positiveSum = 0f
            return
        }
        var sumNeg = 0f
        var sumPos = 0f
        for (f in yVals!!) {
            if (f <= 0f) sumNeg += Math.abs(f) else sumPos += f
        }
        negativeSum = sumNeg
        positiveSum = sumPos
    }

    protected fun calcRanges() {
        val values = yVals
        if (values == null || values.size == 0) return
        ranges = arrayOfNulls(values.size)
        var negRemain = -negativeSum
        var posRemain = 0f
        for (i in ranges.indices) {
            val value = values[i]
            if (value < 0) {
                ranges[i] = Range(negRemain, negRemain - value)
                negRemain -= value
            } else {
                ranges[i] = Range(posRemain, posRemain + value)
                posRemain += value
            }
        }
    }

    companion object {
        /**
         * Calculates the sum across all values of the given stack.
         *
         * @param vals
         * @return
         */
        private fun calcSum(vals: FloatArray?): Float {
            if (vals == null) return 0f
            var sum = 0f
            for (f in vals) sum += f
            return sum
        }
    }
}