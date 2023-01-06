package com.github.mikephil.charting.data

import android.graphics.drawable.Drawable

/**
 * Created by Philipp Jahoda on 02/06/16.
 */
abstract class BaseEntry {
    /** the y value  */
    private var y = 0f

    /** optional spot for additional data this Entry represents  */
    private var mData: Any? = null

    /** optional icon image  */
    private var mIcon: Drawable? = null

    constructor() {}

    constructor(y: Float) {
        this.y = y
    }

    constructor(y: Float, data: Any?) {
//        this(y)
        this.y = y
        mData = data!!
    }

    constructor(y: Float, icon: Drawable?) {
//        this(y)
        this.y = y
        mIcon = icon!!
    }

    constructor(y: Float, icon: Drawable?, data: Any?) {
//        this(y)
        this.y = y
        mIcon = icon!!
        mData = data!!
    }

    /**
     * Returns the y value of this Entry.
     *
     * @return
     */
    open fun getY(): Float {
        return y
    }

    /**
     * Sets the icon drawable
     *
     * @param icon
     */
    open fun setIcon(icon: Drawable?) {
        mIcon = icon!!
    }

    /**
     * Returns the icon of this Entry.
     *
     * @return
     */
    open fun getIcon(): Drawable? {
        return mIcon
    }

    /**
     * Sets the y-value for the Entry.
     *
     * @param y
     */
    open fun setY(y: Float) {
        this.y = y
    }

    /**
     * Returns the data, additional information that this Entry represents, or
     * null, if no data has been specified.
     *
     * @return
     */
    open fun getData(): Any? {
        return mData
    }

    /**
     * Sets additional data this Entry should represent.
     *
     * @param data
     */
    open fun setData(data: Any) {
        mData = data
    }
}