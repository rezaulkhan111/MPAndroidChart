package com.github.mikephil.charting.data

import android.graphics.drawable.Drawable
import com.github.mikephil.charting.highlight.Highlight.y

/**
 * Created by Philipp Jahoda on 02/06/16.
 */
abstract class BaseEntry {
    /**
     * Returns the y value of this Entry.
     *
     * @return
     */
    /**
     * Sets the y-value for the Entry.
     *
     * @param y
     */
    /** the y value  */
    open var y = 0f
    /**
     * Returns the data, additional information that this Entry represents, or
     * null, if no data has been specified.
     *
     * @return
     */
    /**
     * Sets additional data this Entry should represent.
     *
     * @param data
     */
    /** optional spot for additional data this Entry represents  */
    var data: Any? = null
    /**
     * Returns the icon of this Entry.
     *
     * @return
     */
    /**
     * Sets the icon drawable
     *
     * @param icon
     */
    /** optional icon image  */
    var icon: Drawable? = null

    constructor() {}
    constructor(y: Float) {
        this.y = y
    }

    constructor(y: Float, data: Any?) : this(y) {
        this.data = data
    }

    constructor(y: Float, icon: Drawable?) : this(y) {
        this.icon = icon
    }

    constructor(y: Float, icon: Drawable?, data: Any?) : this(y) {
        this.icon = icon
        this.data = data
    }
}