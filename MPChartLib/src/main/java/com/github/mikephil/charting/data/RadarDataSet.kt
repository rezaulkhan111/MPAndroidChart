package com.github.mikephil.charting.data

import android.graphics.Color
import com.github.mikephil.charting.interfaces.datasets.IRadarDataSet
import com.github.mikephil.charting.utils.ColorTemplate

class RadarDataSet(yVals: MutableList<RadarEntry?>?, label: String?) :
    LineRadarDataSet<RadarEntry?>(yVals, label), IRadarDataSet {
    /// flag indicating whether highlight circle should be drawn or not
    protected var mDrawHighlightCircleEnabled = false
    protected var mHighlightCircleFillColor = Color.WHITE

    /// The stroke color for highlight circle.
    /// If Utils.COLOR_NONE, the color of the dataset is taken.
    protected var mHighlightCircleStrokeColor = ColorTemplate.COLOR_NONE
    protected var mHighlightCircleStrokeAlpha = (0.3 * 255).toInt()
    protected var mHighlightCircleInnerRadius = 3.0f
    protected var mHighlightCircleOuterRadius = 4.0f
    protected var mHighlightCircleStrokeWidth = 2.0f

    /// Returns true if highlight circle should be drawn, false if not
    override fun isDrawHighlightCircleEnabled(): Boolean {
        return mDrawHighlightCircleEnabled
    }

    /// Sets whether highlight circle should be drawn or not
    override fun setDrawHighlightCircleEnabled(enabled: Boolean) {
        mDrawHighlightCircleEnabled = enabled
    }

    override fun getHighlightCircleFillColor(): Int {
        return mHighlightCircleFillColor
    }

    fun setHighlightCircleFillColor(color: Int) {
        mHighlightCircleFillColor = color
    }

    /// Returns the stroke color for highlight circle.
    /// If Utils.COLOR_NONE, the color of the dataset is taken.
    override fun getHighlightCircleStrokeColor(): Int {
        return mHighlightCircleStrokeColor
    }

    /// Sets the stroke color for highlight circle.
    /// Set to Utils.COLOR_NONE in order to use the color of the dataset;
    fun setHighlightCircleStrokeColor(color: Int) {
        mHighlightCircleStrokeColor = color
    }

    override fun getHighlightCircleStrokeAlpha(): Int {
        return mHighlightCircleStrokeAlpha
    }

    fun setHighlightCircleStrokeAlpha(alpha: Int) {
        mHighlightCircleStrokeAlpha = alpha
    }

    override fun getHighlightCircleInnerRadius(): Float {
        return mHighlightCircleInnerRadius
    }

    fun setHighlightCircleInnerRadius(radius: Float) {
        mHighlightCircleInnerRadius = radius
    }

    override fun getHighlightCircleOuterRadius(): Float {
        return mHighlightCircleOuterRadius
    }

    fun setHighlightCircleOuterRadius(radius: Float) {
        mHighlightCircleOuterRadius = radius
    }

    override fun getHighlightCircleStrokeWidth(): Float {
        return mHighlightCircleStrokeWidth
    }

    fun setHighlightCircleStrokeWidth(strokeWidth: Float) {
        mHighlightCircleStrokeWidth = strokeWidth
    }

    override fun copy(): DataSet<RadarEntry?>? {
        val entries: MutableList<RadarEntry?> = ArrayList()
        for (i in mEntries!!.indices) {
            entries.add(mEntries!![i]!!.copy())
        }
        val copied = RadarDataSet(entries, getLabel())
        copy(copied)
        return copied
    }

    protected fun copy(radarDataSet: RadarDataSet) {
        super.copy(radarDataSet)
        radarDataSet.mDrawHighlightCircleEnabled = mDrawHighlightCircleEnabled
        radarDataSet.mHighlightCircleFillColor = mHighlightCircleFillColor
        radarDataSet.mHighlightCircleInnerRadius = mHighlightCircleInnerRadius
        radarDataSet.mHighlightCircleStrokeAlpha = mHighlightCircleStrokeAlpha
        radarDataSet.mHighlightCircleStrokeColor = mHighlightCircleStrokeColor
        radarDataSet.mHighlightCircleStrokeWidth = mHighlightCircleStrokeWidth
    }
}