package com.github.mikephil.charting.buffer

import com.github.mikephil.charting.interfaces.datasets.IBarDataSet

class HorizontalBarBuffer : BarBuffer {

    constructor(size: Int, dataSetCount: Int, containsStacks: Boolean) : super(
        size,
        dataSetCount,
        containsStacks
    ) {
    }

    override fun feed(data: IBarDataSet) {
        val size = data.getEntryCount() * phaseX
        val barWidthHalf = mBarWidth / 2f
        var i = 0
        while (i < size) {
            val e = data.getEntryForIndex(i)
            if (e == null) {
                i++
                continue
            }
            val x = e.getX()
            var y = e.getY()
            val vals = e.getYVals()
            if (!mContainsStacks || vals == null) {
                val bottom = x - barWidthHalf
                val top = x + barWidthHalf
                var left: Float
                var right: Float
                if (mInverted) {
                    left = if (y >= 0) y else 0f
                    right = if (y <= 0) y else 0f
                } else {
                    right = if (y >= 0) y else 0f
                    left = if (y <= 0) y else 0f
                }

                // multiply the height of the rect with the phase
                if (right > 0) right *= phaseY else left *= phaseY
                addBar(left, top, right, bottom)
            } else {
                var posY = 0f
                var negY = -e.getNegativeSum()
                var yStart = 0f

                // fill the stack
                for (k in vals.indices) {
                    val value = vals[k]
                    if (value >= 0f) {
                        y = posY
                        yStart = posY + value
                        posY = yStart
                    } else {
                        y = negY
                        yStart = negY + Math.abs(value)
                        negY += Math.abs(value)
                    }
                    val bottom = x - barWidthHalf
                    val top = x + barWidthHalf
                    var left: Float
                    var right: Float
                    if (mInverted) {
                        left = if (y >= yStart) y else yStart
                        right = if (y <= yStart) y else yStart
                    } else {
                        right = if (y >= yStart) y else yStart
                        left = if (y <= yStart) y else yStart
                    }

                    // multiply the height of the rect with the phase
                    right *= phaseY
                    left *= phaseY
                    addBar(left, top, right, bottom)
                }
            }
            i++
        }
        reset()
    }
}