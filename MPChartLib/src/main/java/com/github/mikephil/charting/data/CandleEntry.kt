package com.github.mikephil.charting.data

import android.annotation.SuppressLint
import android.graphics.drawable.Drawable

/**
 * Subclass of Entry that holds all values for one entry in a CandleStickChart.
 *
 * @author Philipp Jahoda
 */
@SuppressLint("ParcelCreator")
class CandleEntry : Entry {
    /** shadow-high value  */
    private var mShadowHigh = 0f

    /** shadow-low value  */
    private var mShadowLow = 0f

    /** close value  */
    private var mClose = 0f

    /** open value  */
    private var mOpen = 0f

    /**
     * Constructor.
     *
     * @param x The value on the x-axis
     * @param shadowH The (shadow) high value
     * @param shadowL The (shadow) low value
     * @param open The open value
     * @param close The close value
     */
    constructor(x: Float, shadowH: Float, shadowL: Float, open: Float, close: Float) : super(
        x,
        (shadowH + shadowL) / 2f
    ) {
        mShadowHigh = shadowH
        mShadowLow = shadowL
        mOpen = open
        mClose = close
    }

    /**
     * Constructor.
     *
     * @param x The value on the x-axis
     * @param shadowH The (shadow) high value
     * @param shadowL The (shadow) low value
     * @param open
     * @param close
     * @param data Spot for additional data this Entry represents
     */
    constructor(
        x: Float, shadowH: Float, shadowL: Float, open: Float, close: Float,
        data: Any?
    ) : super(x, (shadowH + shadowL) / 2f, data) {
        mShadowHigh = shadowH
        mShadowLow = shadowL
        mOpen = open
        mClose = close
    }

    /**
     * Constructor.
     *
     * @param x The value on the x-axis
     * @param shadowH The (shadow) high value
     * @param shadowL The (shadow) low value
     * @param open
     * @param close
     * @param icon Icon image
     */
    constructor(
        x: Float, shadowH: Float, shadowL: Float, open: Float, close: Float,
        icon: Drawable?
    ) : super(x, (shadowH + shadowL) / 2f, icon) {
        mShadowHigh = shadowH
        mShadowLow = shadowL
        mOpen = open
        mClose = close
    }

    /**
     * Constructor.
     *
     * @param x The value on the x-axis
     * @param shadowH The (shadow) high value
     * @param shadowL The (shadow) low value
     * @param open
     * @param close
     * @param icon Icon image
     * @param data Spot for additional data this Entry represents
     */
    constructor(
        x: Float, shadowH: Float, shadowL: Float, open: Float, close: Float,
        icon: Drawable?, data: Any?
    ) : super(x, (shadowH + shadowL) / 2f, icon, data) {
        mShadowHigh = shadowH
        mShadowLow = shadowL
        mOpen = open
        mClose = close
    }

    /**
     * Returns the overall range (difference) between shadow-high and
     * shadow-low.
     *
     * @return
     */
    fun getShadowRange(): Float {
        return Math.abs(mShadowHigh - mShadowLow)
    }

    /**
     * Returns the body size (difference between open and close).
     *
     * @return
     */
    fun getBodyRange(): Float {
        return Math.abs(mOpen - mClose)
    }

    /**
     * Returns the center value of the candle. (Middle value between high and
     * low)
     */
    override fun getY(): Float {
        return super.getY()
    }

    override fun copy(): CandleEntry {
        return CandleEntry(
            getX(), mShadowHigh, mShadowLow, mOpen,
            mClose, getData()
        )
    }

    /**
     * Returns the upper shadows highest value.
     *
     * @return
     */
    fun getHigh(): Float {
        return mShadowHigh
    }

    fun setHigh(mShadowHigh: Float) {
        this.mShadowHigh = mShadowHigh
    }

    /**
     * Returns the lower shadows lowest value.
     *
     * @return
     */
    fun getLow(): Float {
        return mShadowLow
    }

    fun setLow(mShadowLow: Float) {
        this.mShadowLow = mShadowLow
    }

    /**
     * Returns the bodys close value.
     *
     * @return
     */
    fun getClose(): Float {
        return mClose
    }

    fun setClose(mClose: Float) {
        this.mClose = mClose
    }

    /**
     * Returns the bodys open value.
     *
     * @return
     */
    fun getOpen(): Float {
        return mOpen
    }

    fun setOpen(mOpen: Float) {
        this.mOpen = mOpen
    }
}