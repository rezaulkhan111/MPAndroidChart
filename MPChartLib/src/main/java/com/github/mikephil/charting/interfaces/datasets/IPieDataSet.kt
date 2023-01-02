package com.github.mikephil.charting.interfaces.datasets

import com.github.mikephil.charting.data.PieDataSet.ValuePosition
import com.github.mikephil.charting.data.PieEntry

/**
 * Created by Philipp Jahoda on 03/11/15.
 */
interface IPieDataSet : IDataSet<PieEntry> {
    /**
     * Returns the space that is set to be between the piechart-slices of this
     * DataSet, in pixels.
     *
     * @return
     */
    fun getSliceSpace(): Float

    /**
     * When enabled, slice spacing will be 0.0 when the smallest value is going to be
     * smaller than the slice spacing itself.
     *
     * @return
     */
    fun isAutomaticallyDisableSliceSpacingEnabled(): Boolean

    /**
     * Returns the distance a highlighted piechart slice is "shifted" away from
     * the chart-center in dp.
     *
     * @return
     */
    fun getSelectionShift(): Float

    fun getXValuePosition(): ValuePosition
    fun getYValuePosition(): ValuePosition

    /**
     * When valuePosition is OutsideSlice, indicates line color
     */
    fun getValueLineColor(): Int

    /**
     * When valuePosition is OutsideSlice and enabled, line will have the same color as the slice
     */
    fun isUseValueColorForLineEnabled(): Boolean

    /**
     * When valuePosition is OutsideSlice, indicates line width
     */
    fun getValueLineWidth(): Float

    /**
     * When valuePosition is OutsideSlice, indicates offset as percentage out of the slice size
     */
    fun getValueLinePart1OffsetPercentage(): Float

    /**
     * When valuePosition is OutsideSlice, indicates length of first half of the line
     */
    fun getValueLinePart1Length(): Float

    /**
     * When valuePosition is OutsideSlice, indicates length of second half of the line
     */
    fun getValueLinePart2Length(): Float

    /**
     * When valuePosition is OutsideSlice, this allows variable line length
     */
    fun isValueLineVariableLength(): Boolean

    /**
     * Gets the color for the highlighted sector
     */
    fun getHighlightColor(): Int?
}