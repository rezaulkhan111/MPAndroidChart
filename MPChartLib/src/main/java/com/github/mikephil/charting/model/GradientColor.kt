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
    /**
     * Deprecated. Use `Fill.setGradientColors(...)`
     */
    @get:Deprecated("")
    @set:Deprecated("")
    var startColor: Int
        get() = gradientColors!![0]
        set(startColor) {
            if (gradientColors == null || gradientColors!!.size != 2) {
                gradientColors = intArrayOf(
                    startColor,
                    if (gradientColors != null && gradientColors!!.size > 1) gradientColors!![1] else 0
                )
            } else {
                gradientColors!![0] = startColor
            }
        }
    /**
     * Deprecated. Use `Fill.getGradientColors()`
     */
    /**
     * Deprecated. Use `Fill.setGradientColors(...)`
     */
    @get:Deprecated("")
    @set:Deprecated("")
    var endColor: Int
        get() = gradientColors!![1]
        set(endColor) {
            if (gradientColors == null || gradientColors!!.size != 2) {
                gradientColors = intArrayOf(
                    if (gradientColors != null && gradientColors!!.size > 0) gradientColors!![0] else 0,
                    endColor
                )
            } else {
                gradientColors!![1] = endColor
            }
        }
}