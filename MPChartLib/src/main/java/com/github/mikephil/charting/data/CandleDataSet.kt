package com.github.mikephil.charting.data

import android.graphics.Paint
import com.github.mikephil.charting.interfaces.datasets.ICandleDataSet
import com.github.mikephil.charting.utils.ColorTemplate
import com.github.mikephil.charting.utils.Utils.convertDpToPixel

/**
 * DataSet for the CandleStickChart.
 *
 * @author Philipp Jahoda
 */
class CandleDataSet : LineScatterCandleRadarDataSet<CandleEntry>, ICandleDataSet {

    /**
     * the width of the shadow of the candle
     */
    private var mShadowWidth = 3f

    /**
     * should the candle bars show?
     * when false, only "ticks" will show
     *
     *
     * - default: true
     */
    private var mShowCandleBar = true

    /**
     * the space between the candle entries, default 0.1f (10%)
     */
    private var mBarSpace = 0.1f

    /**
     * use candle color for the shadow
     */
    private var mShadowColorSameAsCandle = false

    /**
     * paint style when open < close
     * increasing candlesticks are traditionally hollow
     */
    private var mIncreasingPaintStyle = Paint.Style.STROKE

    /**
     * paint style when open > close
     * descreasing candlesticks are traditionally filled
     */
    private var mDecreasingPaintStyle = Paint.Style.FILL

    /**
     * color for open == close
     */
    private var mNeutralColor = ColorTemplate.COLOR_SKIP

    /**
     * color for open < close
     */
    private var mIncreasingColor = ColorTemplate.COLOR_SKIP

    /**
     * color for open > close
     */
    private var mDecreasingColor = ColorTemplate.COLOR_SKIP

    /**
     * shadow line color, set -1 for backward compatibility and uses default
     * color
     */
    private var mShadowColor = ColorTemplate.COLOR_SKIP

    constructor(yVals: MutableList<CandleEntry>, label: String) : super(yVals, label) {

    }

    override fun copy(): DataSet<CandleEntry> {
        val entries: MutableList<CandleEntry> = ArrayList()
        for (i in mEntries!!.indices) {
            entries.add(mEntries!![i].copy())
        }
        val copied = CandleDataSet(entries, getLabel())
        copy(copied)
        return copied
    }

    private fun copy(candleDataSet: CandleDataSet) {
        super.copy(candleDataSet)
        candleDataSet.mShadowWidth = mShadowWidth
        candleDataSet.mShowCandleBar = mShowCandleBar
        candleDataSet.mBarSpace = mBarSpace
        candleDataSet.mShadowColorSameAsCandle = mShadowColorSameAsCandle
        candleDataSet.mHighLightColor = mHighLightColor
        candleDataSet.mIncreasingPaintStyle = mIncreasingPaintStyle
        candleDataSet.mDecreasingPaintStyle = mDecreasingPaintStyle
        candleDataSet.mNeutralColor = mNeutralColor
        candleDataSet.mIncreasingColor = mIncreasingColor
        candleDataSet.mDecreasingColor = mDecreasingColor
        candleDataSet.mShadowColor = mShadowColor
    }

    protected fun calcMinMax(e: CandleEntry) {
        if (e.getLow() < mYMin) mYMin = e.getLow()

        if (e.getHigh() > mYMax) mYMax = e.getHigh()
        calcMinMaxX(e)
    }

    override fun calcMinMaxY(e: CandleEntry) {
        if (e.getHigh() < mYMin) mYMin = e.getHigh()

        if (e.getHigh() > mYMax) mYMax = e.getHigh()

        if (e.getLow() < mYMin) mYMin = e.getLow()

        if (e.getLow() > mYMax) mYMax = e.getLow()
    }

    /**
     * Sets the space that is left out on the left and right side of each
     * candle, default 0.1f (10%), max 0.45f, min 0f
     *
     * @param space
     */
    fun setBarSpace(space: Float) {
        var space = space
        if (space < 0f) space = 0f
        if (space > 0.45f) space = 0.45f
        mBarSpace = space
    }

    override fun getBarSpace(): Float {
        return mBarSpace
    }

    /**
     * Sets the width of the candle-shadow-line in pixels. Default 3f.
     *
     * @param width
     */
    fun setShadowWidth(width: Float) {
        mShadowWidth = convertDpToPixel(width)
    }

    override fun getShadowWidth(): Float {
        return mShadowWidth
    }

    /**
     * Sets whether the candle bars should show?
     *
     * @param showCandleBar
     */
    fun setShowCandleBar(showCandleBar: Boolean) {
        mShowCandleBar = showCandleBar
    }

    override fun getShowCandleBar(): Boolean {
        return mShowCandleBar
    }

    // TODO
    /**
     * It is necessary to implement ColorsList class that will encapsulate
     * colors list functionality, because It's wrong to copy paste setColor,
     * addColor, ... resetColors for each time when we want to add a coloring
     * options for one of objects
     *
     * @author Mesrop
     */

    /** BELOW THIS COLOR HANDLING */

    // TODO
    /**
     * It is necessary to implement ColorsList class that will encapsulate
     * colors list functionality, because It's wrong to copy paste setColor,
     * addColor, ... resetColors for each time when we want to add a coloring
     * options for one of objects
     *
     * @author Mesrop
     */
    /** BELOW THIS COLOR HANDLING  */
    /**
     * Sets the one and ONLY color that should be used for this DataSet when
     * open == close.
     *
     * @param color
     */
    fun setNeutralColor(color: Int) {
        mNeutralColor = color
    }

    override fun getNeutralColor(): Int {
        return mNeutralColor
    }

    /**
     * Sets the one and ONLY color that should be used for this DataSet when
     * open <= close.
     *
     * @param color
     */
    fun setIncreasingColor(color: Int) {
        mIncreasingColor = color
    }

    override fun getIncreasingColor(): Int {
        return mIncreasingColor
    }

    /**
     * Sets the one and ONLY color that should be used for this DataSet when
     * open > close.
     *
     * @param color
     */
    fun setDecreasingColor(color: Int) {
        mDecreasingColor = color
    }

    override fun getDecreasingColor(): Int {
        return mDecreasingColor
    }

    override fun getIncreasingPaintStyle(): Paint.Style {
        return mIncreasingPaintStyle
    }

    /**
     * Sets paint style when open < close
     *
     * @param paintStyle
     */
    fun setIncreasingPaintStyle(paintStyle: Paint.Style) {
        mIncreasingPaintStyle = paintStyle
    }

    override fun getDecreasingPaintStyle(): Paint.Style {
        return mDecreasingPaintStyle
    }

    /**
     * Sets paint style when open > close
     *
     * @param decreasingPaintStyle
     */
    fun setDecreasingPaintStyle(decreasingPaintStyle: Paint.Style) {
        mDecreasingPaintStyle = decreasingPaintStyle
    }

    override fun getShadowColor(): Int {
        return mShadowColor
    }

    /**
     * Sets shadow color for all entries
     *
     * @param shadowColor
     */
    fun setShadowColor(shadowColor: Int) {
        mShadowColor = shadowColor
    }

    override fun getShadowColorSameAsCandle(): Boolean {
        return mShadowColorSameAsCandle
    }

    /**
     * Sets shadow color to be the same color as the candle color
     *
     * @param shadowColorSameAsCandle
     */
    fun setShadowColorSameAsCandle(shadowColorSameAsCandle: Boolean) {
        mShadowColorSameAsCandle = shadowColorSameAsCandle
    }
}