package com.github.mikephil.charting.data

import android.graphics.Color
import com.github.mikephil.charting.interfaces.datasets.IBarDataSet
import com.github.mikephil.charting.utils.Fill

class BarDataSet(yVals: MutableList<BarEntry?>, label: String?) :
    BarLineScatterCandleBubbleDataSet<BarEntry?>(yVals, label), IBarDataSet {
    /**
     * the maximum number of bars that are stacked upon each other, this value
     * is calculated from the Entries that are added to the DataSet
     */
    override var stackSize = 1
        private set
    /**
     * Sets the color used for drawing the bar-shadows. The bar shadows is a
     * surface behind the bar that indicates the maximum value. Don't for get to
     * use getResources().getColor(...) to set this. Or Color.rgb(...).
     *
     * @param color
     */
    /**
     * the color used for drawing the bar shadows
     */
    override var barShadowColor = Color.rgb(215, 215, 215)
    /**
     * Returns the width used for drawing borders around the bars.
     * If borderWidth == 0, no border will be drawn.
     *
     * @return
     */
    /**
     * Sets the width used for drawing borders around the bars.
     * If borderWidth == 0, no border will be drawn.
     *
     * @return
     */
    override var barBorderWidth = 0.0f
    /**
     * Returns the color drawing borders around the bars.
     *
     * @return
     */
    /**
     * Sets the color drawing borders around the bars.
     *
     * @return
     */
    override var barBorderColor = Color.BLACK
    /**
     * Set the alpha value (transparency) that is used for drawing the highlight
     * indicator bar. min = 0 (fully transparent), max = 255 (fully opaque)
     *
     * @param alpha
     */
    /**
     * the alpha value used to draw the highlight indicator bar
     */
    override var highLightAlpha = 120
    /**
     * returns the overall entry count, including counting each stack-value
     * individually
     *
     * @return
     */
    /**
     * the overall entry count, including counting each stack-value individually
     */
    var entryCountStacks = 0
        private set
    /**
     * Sets labels for different values of bar-stacks, in case there are one.
     *
     * @param labels
     */
    /**
     * array of labels used to describe the different values of the stacked bars
     */
    override var stackLabels = arrayOf<String>()
     var mFills: MutableList<Fill>? = null
    override fun copy(): DataSet<BarEntry?>? {
        val entries: MutableList<BarEntry?> = ArrayList()
        for (i in mEntries!!.indices) {
            entries.add(mEntries!![i]!!.copy())
        }
        val copied = BarDataSet(entries, getLabel())
        copy(copied)
        return copied
    }

     fun copy(barDataSet: BarDataSet) {
        super.copy(barDataSet)
        barDataSet.stackSize = stackSize
        barDataSet.barShadowColor = barShadowColor
        barDataSet.barBorderWidth = barBorderWidth
        barDataSet.stackLabels = stackLabels
        barDataSet.highLightAlpha = highLightAlpha
    }

    override val fills: List<Fill>?
        get() = mFills

    override fun getFill(index: Int): Fill? {
        return mFills!![index % mFills!!.size]
    }

    /**
     * This method is deprecated.
     * Use getFills() instead.
     */
    @get:Deprecated("")
    val gradients: List<Fill>?
        get() = mFills

    /**
     * This method is deprecated.
     * Use getFill(...) instead.
     *
     * @param index
     */
    @Deprecated("")
    fun getGradient(index: Int): Fill? {
        return getFill(index)
    }

    /**
     * Sets the start and end color for gradient color, ONLY color that should be used for this DataSet.
     *
     * @param startColor
     * @param endColor
     */
    fun setGradientColor(startColor: Int, endColor: Int) {
        mFills!!.clear()
        mFills!!.add(Fill(startColor, endColor))
    }

    /**
     * This method is deprecated.
     * Use setFills(...) instead.
     *
     * @param gradientColors
     */
    @Deprecated("")
    fun setGradientColors(gradientColors: MutableList<Fill>?) {
        mFills = gradientColors
    }

    /**
     * Sets the fills for the bars in this dataset.
     *
     * @param fills
     */
    fun setFills(fills: MutableList<Fill>?) {
        mFills = fills
    }

    /**
     * Calculates the total number of entries this DataSet represents, including
     * stacks. All values belonging to a stack are calculated separately.
     */
    private fun calcEntryCountIncludingStacks(yVals: List<BarEntry?>) {
        entryCountStacks = 0
        for (i in yVals.indices) {
            val vals = yVals[i].getYVals()
            if (vals == null) entryCountStacks++ else entryCountStacks += vals.size
        }
    }

    /**
     * calculates the maximum stacksize that occurs in the Entries array of this
     * DataSet
     */
    private fun calcStackSize(yVals: List<BarEntry?>) {
        for (i in yVals.indices) {
            val vals = yVals[i].getYVals()
            if (vals != null && vals.size > stackSize) stackSize = vals.size
        }
    }

    override fun calcMinMax(e: BarEntry?) {
        if (e != null && !java.lang.Float.isNaN(e.y)) {
            if (e.yVals == null) {
                if (e.y < mYMin) mYMin = e.y
                if (e.y > mYMax) mYMax = e.y
            } else {
                if (-e.negativeSum < mYMin) mYMin = -e.negativeSum
                if (e.positiveSum > mYMax) mYMax = e.positiveSum
            }
            calcMinMaxX(e)
        }
    }

    override val isStacked: Boolean
        get() = if (stackSize > 1) true else false

    init {
        mHighLightColor = Color.rgb(0, 0, 0)
        calcStackSize(yVals)
        calcEntryCountIncludingStacks(yVals)
    }
}