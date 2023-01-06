package com.github.mikephil.charting.data

import android.graphics.Color
import com.github.mikephil.charting.interfaces.datasets.IRadarDataSet
import com.github.mikephil.charting.utils.ColorTemplate

class RadarDataSet : LineRadarDataSet<RadarEntry?>, IRadarDataSet {

    /// flag indicating whether highlight circle should be drawn or not
    private var mDrawHighlightCircleEnabled = false
    private var mHighlightCircleFillColor = Color.WHITE

    /// The stroke color for highlight circle.
    /// If Utils.COLOR_NONE, the color of the dataset is taken.
    private var mHighlightCircleStrokeColor = ColorTemplate.COLOR_NONE
    private var mHighlightCircleStrokeAlpha = (0.3 * 255).toInt()
    private var mHighlightCircleInnerRadius = 3.0f
    private var mHighlightCircleOuterRadius = 4.0f
    private var mHighlightCircleStrokeWidth = 2.0f

    constructor(yVals: MutableList<RadarEntry?>, label: String) : super(yVals, label) {}

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

    override fun copy(): DataSet<RadarEntry?> {
        val entries: MutableList<RadarEntry?> = ArrayList()
        for (i in mEntries!!.indices) {
            entries.add(mEntries!![i]?.copy())
        }
        val copied = RadarDataSet(entries, getLabel())
        copy(copied)
        return copied
    }

    private fun copy(radarDataSet: RadarDataSet) {
        super.copy(radarDataSet)
        radarDataSet.mDrawHighlightCircleEnabled = mDrawHighlightCircleEnabled
        radarDataSet.mHighlightCircleFillColor = mHighlightCircleFillColor
        radarDataSet.mHighlightCircleInnerRadius = mHighlightCircleInnerRadius
        radarDataSet.mHighlightCircleStrokeAlpha = mHighlightCircleStrokeAlpha
        radarDataSet.mHighlightCircleStrokeColor = mHighlightCircleStrokeColor
        radarDataSet.mHighlightCircleStrokeWidth = mHighlightCircleStrokeWidth
    }
}