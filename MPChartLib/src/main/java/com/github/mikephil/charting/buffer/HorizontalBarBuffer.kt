package com.github.mikephil.charting.buffer

import com.github.mikephil.charting.interfaces.datasets.IBarDataSet

class HorizontalBarBuffer(size: Int, dataSetCount: Int, containsStacks: Boolean) :
    BarBuffer(size, dataSetCount, containsStacks) {
    override fun feed(data: IBarDataSet) {
        val size = data.entryCount * phaseX
        val barWidthHalf = mBarWidth / 2f
        var i = 0
        while (i < size) {
            val e = data.getEntryForIndex(i)
            if (e == null) {
                i++
                continue
            }
            val x = e.x
            var y = e.y
            val vals = e.yVals
            if (!mContainsStacks || vals == null) {
                val bottom = x - barWidthHalf
                val top = x + barWidthHalf
                var left: Float
                var right: Float
                if (mInverted) {
                    left = if (y >= 0) y else 0
                    right = if (y <= 0) y else 0
                } else {
                    right = if (y >= 0) y else 0
                    left = if (y <= 0) y else 0
                }

                // multiply the height of the rect with the phase
                if (right > 0) right *= phaseY else left *= phaseY
                addBar(left, top, right, bottom)
            } else {
                var posY = 0f
                var negY = -e.negativeSum
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