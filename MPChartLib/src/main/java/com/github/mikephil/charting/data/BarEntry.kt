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
     * the values the stacked barchart holds
     */
    private lateinit var mYVals: FloatArray

    /**
     * the ranges for the individual stack values - automatically calculated
     */
    private lateinit var mRanges: Array<Range>

    /**
     * the sum of all negative values this entry (if stacked) contains
     */
    private var mNegativeSum = 0f

    /**
     * the sum of all positive values this entry (if stacked) contains
     */
    private var mPositiveSum = 0f

    /**
     * Constructor for normal bars (not stacked).
     *
     * @param x
     * @param y
     */
    constructor(x: Float, y: Float) : super(x, y) {

    }

    /**
     * Constructor for normal bars (not stacked).
     *
     * @param x
     * @param y
     * @param data - Spot for additional data this Entry represents.
     */
    constructor(x: Float, y: Float, data: Any?) : super(x, y, data) {

    }

    /**
     * Constructor for normal bars (not stacked).
     *
     * @param x
     * @param y
     * @param icon - icon image
     */
    constructor(x: Float, y: Float, icon: Drawable?) : super(x, y, icon) {

    }

    /**
     * Constructor for normal bars (not stacked).
     *
     * @param x
     * @param y
     * @param icon - icon image
     * @param data - Spot for additional data this Entry represents.
     */
    constructor(x: Float, y: Float, icon: Drawable?, data: Any?) : super(x, y, icon, data) {
    }

    /**
     * Constructor for stacked bar entries. One data object for whole stack
     *
     * @param x
     * @param vals - the stack values, use at least 2
     */
    constructor(x: Float, vals: FloatArray?) : super(x, calcSum(vals)) {
        mYVals = vals
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
        mYVals = vals
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
        mYVals = vals
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
        mYVals = vals
        calcPosNegSum()
        calcRanges()
    }

    /**
     * Returns an exact copy of the BarEntry.
     */
    override fun copy(): BarEntry? {
        val copied = BarEntry(getX(), getY(), getData())
        copied.setVals(mYVals)
        return copied
    }

    /**
     * Returns the stacked values this BarEntry represents, or null, if only a single value is represented (then, use
     * getY()).
     *
     * @return
     */
    fun getYVals(): FloatArray? {
        return mYVals
    }

    /**
     * Set the array of values this BarEntry should represent.
     *
     * @param vals
     */
    fun setVals(vals: FloatArray) {
        setY(calcSum(vals))
        mYVals = vals
        calcPosNegSum()
        calcRanges()
    }

    /**
     * Returns the value of this BarEntry. If the entry is stacked, it returns the positive sum of all values.
     *
     * @return
     */
    override fun getY(): Float {
        return super.getY()
    }

    /**
     * Returns the ranges of the individual stack-entries. Will return null if this entry is not stacked.
     *
     * @return
     */
    fun getRanges(): Array<Range> {
        return mRanges
    }

    /**
     * Returns true if this BarEntry is stacked (has a values array), false if not.
     *
     * @return
     */
    fun isStacked(): Boolean {
        return mYVals != null
    }

    /**
     * Use `getSumBelow(stackIndex)` instead.
     */
    @Deprecated("")
    fun getBelowSum(stackIndex: Int): Float {
        return getSumBelow(stackIndex)
    }

    fun getSumBelow(stackIndex: Int): Float {
        if (mYVals == null) return 0f
        var remainder = 0f
        var index = mYVals!!.size - 1
        while (index > stackIndex && index >= 0) {
            remainder += mYVals!![index]
            index--
        }
        return remainder
    }

    /**
     * Reuturns the sum of all positive values this entry (if stacked) contains.
     *
     * @return
     */
    fun getPositiveSum(): Float {
        return mPositiveSum
    }

    /**
     * Returns the sum of all negative values this entry (if stacked) contains. (this is a positive number)
     *
     * @return
     */
    fun getNegativeSum(): Float {
        return mNegativeSum
    }

    private fun calcPosNegSum() {
        if (mYVals == null) {
            mNegativeSum = 0f
            mPositiveSum = 0f
            return
        }
        var sumNeg = 0f
        var sumPos = 0f
        for (f in mYVals!!) {
            if (f <= 0f) sumNeg += Math.abs(f) else sumPos += f
        }
        mNegativeSum = sumNeg
        mPositiveSum = sumPos
    }

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

    protected fun calcRanges() {
        val values = getYVals()
        if (values == null || values.size == 0) return
        mRanges = arrayOfNulls(values.size)
        var negRemain = -getNegativeSum()
        var posRemain = 0f
        for (i in mRanges.indices) {
            val value = values[i]
            if (value < 0) {
                mRanges[i] = Range(negRemain, negRemain - value)
                negRemain -= value
            } else {
                mRanges[i] = Range(posRemain, posRemain + value)
                posRemain += value
            }
        }
    }
}