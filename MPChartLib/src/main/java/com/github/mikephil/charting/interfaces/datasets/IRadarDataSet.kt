package com.github.mikephil.charting.interfaces.datasets

import com.github.mikephil.charting.data.RadarEntry

/**
 * Created by Philipp Jahoda on 03/11/15.
 */
interface IRadarDataSet : ILineRadarDataSet<RadarEntry> {
    /// flag indicating whether highlight circle should be drawn or not
    fun isDrawHighlightCircleEnabled(): Boolean

    /// Sets whether highlight circle should be drawn or not
    fun setDrawHighlightCircleEnabled(enabled: Boolean)

    fun getHighlightCircleFillColor(): Int

    /// The stroke color for highlight circle.
    /// If Utils.COLOR_NONE, the color of the dataset is taken.
    fun getHighlightCircleStrokeColor(): Int

    fun getHighlightCircleStrokeAlpha(): Int

    fun getHighlightCircleInnerRadius(): Float

    fun getHighlightCircleOuterRadius(): Float

    fun getHighlightCircleStrokeWidth(): Float
}