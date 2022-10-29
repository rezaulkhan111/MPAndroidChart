package com.github.mikephil.charting.data

import android.annotation.SuppressLint

/**
 * Created by philipp on 13/06/16.
 */
@SuppressLint("ParcelCreator")
class RadarEntry : Entry {
    constructor(value: Float) : super(0f, value) {}
    constructor(value: Float, data: Any?) : super(0f, value, data) {}

    /**
     * This is the same as getY(). Returns the value of the RadarEntry.
     *
     * @return
     */
    val value: Float
        get() = y

    override fun copy(): RadarEntry {
        return RadarEntry(y, data)
    }

    @get:Deprecated("")
    @set:Deprecated("")
    override var x: Float
        get() = super.getX()
        set(x) {
            super.setX(x)
        }
}