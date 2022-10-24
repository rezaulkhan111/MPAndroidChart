package com.github.mikephil.charting.buffer

import com.github.mikephil.charting.interfaces.datasets.IDataSet.entryCount
import com.github.mikephil.charting.interfaces.datasets.IDataSet.getEntryForIndex
import com.github.mikephil.charting.data.Entry.x
import com.github.mikephil.charting.data.BarEntry.y
import com.github.mikephil.charting.data.BarEntry.yVals
import com.github.mikephil.charting.data.BarEntry.negativeSum
import com.github.mikephil.charting.buffer.AbstractBuffer
import com.github.mikephil.charting.interfaces.datasets.IBarDataSet
import com.github.mikephil.charting.data.BarEntry
import com.github.mikephil.charting.buffer.BarBuffer

/**
 * Buffer class to boost performance while drawing. Concept: Replace instead of
 * recreate.
 *
 * @author Philipp Jahoda
 * @param <T> The data the buffer accepts to be fed with.
</T> */
abstract class AbstractBuffer<T>(size: Int) {
    /** index in the buffer  */
    protected var index = 0

    /** float-buffer that holds the data points to draw, order: x,y,x,y,...  */
    val buffer: FloatArray

    /** animation phase x-axis  */
    protected var phaseX = 1f

    /** animation phase y-axis  */
    protected var phaseY = 1f

    /** indicates from which x-index the visible data begins  */
    protected var mFrom = 0

    /** indicates to which x-index the visible data ranges  */
    protected var mTo = 0

    /** limits the drawing on the x-axis  */
    fun limitFrom(from: Int) {
        var from = from
        if (from < 0) from = 0
        mFrom = from
    }

    /** limits the drawing on the x-axis  */
    fun limitTo(to: Int) {
        var to = to
        if (to < 0) to = 0
        mTo = to
    }

    /**
     * Resets the buffer index to 0 and makes the buffer reusable.
     */
    fun reset() {
        index = 0
    }

    /**
     * Returns the size (length) of the buffer array.
     *
     * @return
     */
    fun size(): Int {
        return buffer.size
    }

    /**
     * Set the phases used for animations.
     *
     * @param phaseX
     * @param phaseY
     */
    fun setPhases(phaseX: Float, phaseY: Float) {
        this.phaseX = phaseX
        this.phaseY = phaseY
    }

    /**
     * Builds up the buffer with the provided data and resets the buffer-index
     * after feed-completion. This needs to run FAST.
     *
     * @param data
     */
    abstract fun feed(data: T)

    /**
     * Initialization with buffer-size.
     *
     * @param size
     */
    init {
        index = 0
        buffer = FloatArray(size)
    }
}