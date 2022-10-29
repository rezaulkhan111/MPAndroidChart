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
    /**
     * Returns the upper shadows highest value.
     *
     * @return
     */
    /** shadow-high value  */
    var high = 0f
    /**
     * Returns the lower shadows lowest value.
     *
     * @return
     */
    /** shadow-low value  */
    var low = 0f
    /**
     * Returns the bodys close value.
     *
     * @return
     */
    /** close value  */
    var close = 0f
    /**
     * Returns the bodys open value.
     *
     * @return
     */
    /** open value  */
    var open = 0f

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
        high = shadowH
        low = shadowL
        this.open = open
        this.close = close
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
        high = shadowH
        low = shadowL
        this.open = open
        this.close = close
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
        high = shadowH
        low = shadowL
        this.open = open
        this.close = close
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
        high = shadowH
        low = shadowL
        this.open = open
        this.close = close
    }

    /**
     * Returns the overall range (difference) between shadow-high and
     * shadow-low.
     *
     * @return
     */
    val shadowRange: Float
        get() = Math.abs(high - low)

    /**
     * Returns the body size (difference between open and close).
     *
     * @return
     */
    val bodyRange: Float
        get() = Math.abs(open - close)

    /**
     * Returns the center value of the candle. (Middle value between high and
     * low)
     */
    override var y: Float
        get() = super.getY()
        set(y) {
            super.y = y
        }

    override fun copy(): CandleEntry {
        return CandleEntry(
            x, high, low, open,
            close, data
        )
    }
}