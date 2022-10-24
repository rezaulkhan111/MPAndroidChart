package com.github.mikephil.charting.utils

import android.content.res.Resources
import android.graphics.Color

/**
 * Class that holds predefined color integer arrays (e.g.
 * ColorTemplate.VORDIPLOM_COLORS) and convenience methods for loading colors
 * from resources.
 *
 * @author Philipp Jahoda
 */
object ColorTemplate {
    /**
     * an "invalid" color that indicates that no color is set
     */
    const val COLOR_NONE = 0x00112233

    /**
     * this "color" is used for the Legend creation and indicates that the next
     * form should be skipped
     */
    const val COLOR_SKIP = 0x00112234

    /**
     * THE COLOR THEMES ARE PREDEFINED (predefined color integer arrays), FEEL
     * FREE TO CREATE YOUR OWN WITH AS MANY DIFFERENT COLORS AS YOU WANT
     */
    @JvmField
    val LIBERTY_COLORS = intArrayOf(
        Color.rgb(207, 248, 246), Color.rgb(148, 212, 212), Color.rgb(136, 180, 187),
        Color.rgb(118, 174, 175), Color.rgb(42, 109, 130)
    )
    @JvmField
    val JOYFUL_COLORS = intArrayOf(
        Color.rgb(217, 80, 138), Color.rgb(254, 149, 7), Color.rgb(254, 247, 120),
        Color.rgb(106, 167, 134), Color.rgb(53, 194, 209)
    )
    @JvmField
    val PASTEL_COLORS = intArrayOf(
        Color.rgb(64, 89, 128), Color.rgb(149, 165, 124), Color.rgb(217, 184, 162),
        Color.rgb(191, 134, 134), Color.rgb(179, 48, 80)
    )
    @JvmField
    val COLORFUL_COLORS = intArrayOf(
        Color.rgb(193, 37, 82), Color.rgb(255, 102, 0), Color.rgb(245, 199, 0),
        Color.rgb(106, 150, 31), Color.rgb(179, 100, 53)
    )
    @JvmField
    val VORDIPLOM_COLORS = intArrayOf(
        Color.rgb(192, 255, 140), Color.rgb(255, 247, 140), Color.rgb(255, 208, 140),
        Color.rgb(140, 234, 255), Color.rgb(255, 140, 157)
    )
    @JvmField
    val MATERIAL_COLORS = intArrayOf(
        rgb("#2ecc71"), rgb("#f1c40f"), rgb("#e74c3c"), rgb("#3498db")
    )

    /**
     * Converts the given hex-color-string to rgb.
     *
     * @param hex
     * @return
     */
    fun rgb(hex: String): Int {
        val color = hex.replace("#", "").toLong(16).toInt()
        val r = color shr 16 and 0xFF
        val g = color shr 8 and 0xFF
        val b = color shr 0 and 0xFF
        return Color.rgb(r, g, b)
    }

    /**
     * Returns the Android ICS holo blue light color.
     *
     * @return
     */
    @JvmStatic
    val holoBlue: Int
        get() = Color.rgb(51, 181, 229)

    /**
     * Sets the alpha component of the given color.
     *
     * @param color
     * @param alpha 0 - 255
     * @return
     */
    @JvmStatic
    fun colorWithAlpha(color: Int, alpha: Int): Int {
        return color and 0xffffff or (alpha and 0xff shl 24)
    }

    /**
     * turn an array of resource-colors (contains resource-id integers) into an
     * array list of actual color integers
     *
     * @param r
     * @param colors an integer array of resource id's of colors
     * @return
     */
    fun createColors(r: Resources, colors: IntArray): List<Int> {
        val result: MutableList<Int> = ArrayList()
        for (i in colors) {
            result.add(r.getColor(i))
        }
        return result
    }

    /**
     * Turns an array of colors (integer color values) into an ArrayList of
     * colors.
     *
     * @param colors
     * @return
     */
    @JvmStatic
    fun createColors(colors: IntArray): List<Int> {
        val result: MutableList<Int> = ArrayList()
        for (i in colors) {
            result.add(i)
        }
        return result
    }
}