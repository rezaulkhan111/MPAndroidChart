package com.github.mikephil.charting.highlight

/**
 * Created by Philipp Jahoda on 24/07/15. Class that represents the range of one value in a stacked bar entry. e.g.
 * stack values are -10, 5, 20 -> then ranges are (-10 - 0, 0 - 5, 5 - 25).
 */
class Range {

    var from = 0f
    var to = 0f

   constructor(from: Float, to: Float) {
        this.from = from
        this.to = to
    }

    /**
     * Returns true if this range contains (if the value is in between) the given value, false if not.
     *
     * @param value
     * @return
     */
    operator fun contains(value: Float): Boolean {
        return if (value > from && value <= to) true else false
    }

    fun isLarger(value: Float): Boolean {
        return value > to
    }

    fun isSmaller(value: Float): Boolean {
        return value < from
    }
}