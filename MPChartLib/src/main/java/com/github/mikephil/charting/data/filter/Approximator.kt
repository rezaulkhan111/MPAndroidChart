package com.github.mikephil.charting.data.filter

import android.annotation.TargetApi
import android.os.Build
import java.util.*

/**
 * Implemented according to Wiki-Pseudocode []
 * http://en.wikipedia.org/wiki/Ramer�Douglas�Peucker_algorithm
 *
 * @author Philipp Baldauf & Phliipp Jahoda
 */
class Approximator {
    @TargetApi(Build.VERSION_CODES.GINGERBREAD)
    fun reduceWithDouglasPeucker(points: FloatArray, tolerance: Float): FloatArray {
        var greatestIndex = 0
        var greatestDistance = 0f
        val line: Line = Line(
            points[0], points[1], points[points.size - 2], points[points.size - 1]
        )
        var i = 2
        while (i < points.size - 2) {
            val distance = line.distance(points[i], points[i + 1])
            if (distance > greatestDistance) {
                greatestDistance = distance
                greatestIndex = i
            }
            i += 2
        }
        return if (greatestDistance > tolerance) {
            val reduced1 = reduceWithDouglasPeucker(
                Arrays.copyOfRange(points, 0, greatestIndex + 2),
                tolerance
            )
            val reduced2 = reduceWithDouglasPeucker(
                Arrays.copyOfRange(points, greatestIndex, points.size),
                tolerance
            )
            val result2 = Arrays.copyOfRange(reduced2, 2, reduced2.size)
            concat(reduced1, result2)
        } else {
            line.points
        }
    }

    /**
     * Combine arrays.
     *
     * @param arrays
     * @return
     */
    fun concat(vararg arrays: FloatArray): FloatArray {
        var length = 0
        for (array in arrays) {
            length += array.size
        }
        val result = FloatArray(length)
        var pos = 0
        for (array in arrays) {
            for (element in array) {
                result[pos] = element
                pos++
            }
        }
        return result
    }

    private inner class Line(x1: Float, y1: Float, x2: Float, y2: Float) {
        val points: FloatArray
        private val sxey: Float
        private val exsy: Float
        private val dx: Float
        private val dy: Float
        private val length: Float
        fun distance(x: Float, y: Float): Float {
            return Math.abs(dy * x - dx * y + sxey - exsy) / length
        }

        init {
            dx = x1 - x2
            dy = y1 - y2
            sxey = x1 * y2
            exsy = x2 * y1
            length = Math.sqrt((dx * dx + dy * dy).toDouble()).toFloat()
            points = floatArrayOf(x1, y1, x2, y2)
        }
    }
}