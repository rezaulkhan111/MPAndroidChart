package com.github.mikephil.charting.buffer

/**
 * Buffer class to boost performance while drawing. Concept: Replace instead of
 * recreate.
 *
 * @author Philipp Jahoda
 * @param <T> The data the buffer accepts to be fed with.
</T> */
abstract class AbstractBuffer<T> {
    /** index in the buffer  */
    protected var index = 0

    /** float-buffer that holds the data points to draw, order: x,y,x,y,...  */
    var buffer: FloatArray

    /** animation phase x-axis  */
    protected var phaseX = 1f

    /** animation phase y-axis  */
    protected var phaseY = 1f

    /** indicates from which x-index the visible data begins  */
    private var mFrom = 0

    /** indicates to which x-index the visible data ranges  */
    private var mTo = 0

    /**
     * Initialization with buffer-size.
     *
     * @param size
     */
    constructor(size: Int) {
        index = 0
        buffer = FloatArray(size)
    }

    /** limits the drawing on the x-axis  */
    open fun limitFrom(from: Int) {
        var mFrom = from
        if (mFrom < 0) mFrom = 0
        this.mFrom = mFrom
    }

    /** limits the drawing on the x-axis  */
    open fun limitTo(to: Int) {
        var mTo = to
        if (mTo < 0) mTo = 0
        this.mTo = mTo
    }

    /**
     * Resets the buffer index to 0 and makes the buffer reusable.
     */
    open fun reset() {
        index = 0
    }

    /**
     * Returns the size (length) of the buffer array.
     *
     * @return
     */
    open fun size(): Int {
        return buffer.size
    }

    /**
     * Set the phases used for animations.
     *
     * @param phaseX
     * @param phaseY
     */
    open fun setPhases(phaseX: Float, phaseY: Float) {
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
}