package com.github.mikephil.charting.interfaces.datasets

import com.github.mikephil.charting.data.RadarEntry

/**
 * Created by Philipp Jahoda on 03/11/15.
 */
interface IRadarDataSet : ILineRadarDataSet<RadarEntry?> {
    /// Sets whether highlight circle should be drawn or not
    /// flag indicating whether highlight circle should be drawn or not
    var isDrawHighlightCircleEnabled: Boolean
    val highlightCircleFillColor: Int

    /// The stroke color for highlight circle.
    /// If Utils.COLOR_NONE, the color of the dataset is taken.
    val highlightCircleStrokeColor: Int
    val highlightCircleStrokeAlpha: Int
    val highlightCircleInnerRadius: Float
    val highlightCircleOuterRadius: Float
    val highlightCircleStrokeWidth: Float
}