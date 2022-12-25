package com.github.mikephil.charting.data

import com.github.mikephil.charting.interfaces.datasets.IBubbleDataSet
import com.github.mikephil.charting.utils.Utils.convertDpToPixel

class BubbleDataSet : BarLineScatterCandleBubbleDataSet<BubbleEntry>, IBubbleDataSet {

    protected var mMaxSize = 0f
    protected var mNormalizeSize = true
    private var mHighlightCircleWidth = 2.5f

    constructor(yVals: List<BubbleEntry?>?, label: String?) {
        super(yVals, label)
    }

    override fun setHighlightCircleWidth(width: Float) {
        mHighlightCircleWidth = convertDpToPixel(width)
    }

    override fun getHighlightCircleWidth(): Float {
        return mHighlightCircleWidth
    }

    protected override fun calcMinMax(e: BubbleEntry?) {
        super.calcMinMax(e)
        val size = e!!.size
        if (size > mMaxSize) {
            mMaxSize = size
        }
    }

    override fun copy(): DataSet<BubbleEntry> {
        val entries: MutableList<BubbleEntry> = ArrayList()
        for (i in mEntries!!.indices) {
            entries.add(mEntries!![i]!!.copy())
        }
        val copied = BubbleDataSet(entries, label)
        copy(copied)
        return copied
    }

    protected fun copy(bubbleDataSet: BubbleDataSet) {
        bubbleDataSet.mHighlightCircleWidth = mHighlightCircleWidth
        bubbleDataSet.mNormalizeSize = mNormalizeSize
    }

    override fun getMaxSize(): Float {
        return mMaxSize
    }

    override fun isNormalizeSizeEnabled(): Boolean {
        return mNormalizeSize
    }

    fun setNormalizeSizeEnabled(normalizeSize: Boolean) {
        mNormalizeSize = normalizeSize
    }
}