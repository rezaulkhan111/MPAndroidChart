package com.github.mikephil.charting.model

import com.github.mikephil.charting.utils.Fill

/**
 * Deprecated. Use `Fill`
 */
@Deprecated("")
class GradientColor : Fill() {
    /**
     * Deprecated. Use `Fill.getGradientColors()`
     */
    @Deprecated("")
    fun getStartColor(): Int {
        return getGradientColors()!![0]
    }

    /**
     * Deprecated. Use `Fill.setGradientColors(...)`
     */
    @Deprecated("")
    fun setStartColor(startColor: Int) {
        if (getGradientColors() == null || getGradientColors()?.size != 2) {
            setGradientColors(
                intArrayOf(
                    startColor,
                    if (getGradientColors() != null && getGradientColors()?.size!! > 1) getGradientColors()!![1] else 0
                )
            )
        } else {
            getGradientColors()!![0] = startColor
        }
    }

    /**
     * Deprecated. Use `Fill.getGradientColors()`
     */
    @Deprecated("")
    fun getEndColor(): Int {
        return getGradientColors()!![1]
    }

    /**
     * Deprecated. Use `Fill.setGradientColors(...)`
     */
    @Deprecated("")
    fun setEndColor(endColor: Int) {
        if (getGradientColors() == null || getGradientColors()?.size != 2) {
            setGradientColors(
                intArrayOf(
                    if (getGradientColors() != null && getGradientColors()?.size!! > 0) getGradientColors()!![0] else 0,
                    endColor
                )
            )
        } else {
            getGradientColors()!![1] = endColor
        }
    }
}