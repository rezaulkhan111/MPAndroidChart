package com.github.mikephil.charting.data

import com.github.mikephil.charting.interfaces.datasets.IPieDataSet
import com.github.mikephil.charting.utils.Utils.convertDpToPixel

class PieDataSet(yVals: MutableList<PieEntry?>?, label: String?) : DataSet<PieEntry?>(yVals, label),
    IPieDataSet {
    /**
     * the space in pixels between the chart-slices, default 0f
     */
    private var mSliceSpace = 0f

    /**
     * When enabled, slice spacing will be 0.0 when the smallest value is going to be
     * smaller than the slice spacing itself.
     *
     * @return
     */
    override var isAutomaticallyDisableSliceSpacingEnabled = false
        private set

    /**
     * indicates the selection distance of a pie slice
     */
    private var mShift = 18f
    override var xValuePosition = ValuePosition.INSIDE_SLICE
    override var yValuePosition = ValuePosition.INSIDE_SLICE

    /**
     * When valuePosition is OutsideSlice, indicates line color
     */
    override var valueLineColor = -0x1000000
    override var isUseValueColorForLineEnabled = false
        private set

    /**
     * When valuePosition is OutsideSlice, indicates line width
     */
    override var valueLineWidth = 1.0f

    /**
     * When valuePosition is OutsideSlice, indicates offset as percentage out of the slice size
     */
    override var valueLinePart1OffsetPercentage = 75f

    /**
     * When valuePosition is OutsideSlice, indicates length of first half of the line
     */
    override var valueLinePart1Length = 0.3f

    /**
     * When valuePosition is OutsideSlice, indicates length of second half of the line
     */
    override var valueLinePart2Length = 0.4f

    /**
     * When valuePosition is OutsideSlice, this allows variable line length
     */
    override var isValueLineVariableLength = true
    /** Gets the color for the highlighted sector  */
    /** Sets the color for the highlighted sector (null for using entry color)  */
    override var highlightColor: Int? = null
    override fun copy(): DataSet<PieEntry?> {
        val entries: MutableList<PieEntry?> = ArrayList()
        for (i in mEntries!!.indices) {
            entries.add(mEntries!![i]!!.copy())
        }
        val copied = PieDataSet(entries, getLabel())
        copy(copied)
        return copied
    }

    protected fun copy(pieDataSet: PieDataSet?) {
        super.copy(pieDataSet)
    }

    override fun calcMinMax(e: PieEntry?) {
        if (e == null) return
        calcMinMaxY(e)
    }

    /**
     * Sets the space that is left out between the piechart-slices in dp.
     * Default: 0 --> no space, maximum 20f
     *
     * @param spaceDp
     */
    override var sliceSpace: Float
        get() = mSliceSpace
        set(spaceDp) {
            var spaceDp = spaceDp
            if (spaceDp > 20) spaceDp = 20f
            if (spaceDp < 0) spaceDp = 0f
            mSliceSpace = convertDpToPixel(spaceDp)
        }

    /**
     * When enabled, slice spacing will be 0.0 when the smallest value is going to be
     * smaller than the slice spacing itself.
     *
     * @param autoDisable
     */
    fun setAutomaticallyDisableSliceSpacing(autoDisable: Boolean) {
        isAutomaticallyDisableSliceSpacingEnabled = autoDisable
    }

    /**
     * sets the distance the highlighted piechart-slice of this DataSet is
     * "shifted" away from the center of the chart, default 12f
     *
     * @param shift
     */
    override var selectionShift: Float
        get() = mShift
        set(shift) {
            mShift = convertDpToPixel(shift)
        }
    /**
     * This method is deprecated.
     * Use isUseValueColorForLineEnabled() instead.
     */
    /**
     * This method is deprecated.
     * Use setUseValueColorForLine(...) instead.
     *
     * @param enabled
     */
    @get:Deprecated("")
    @set:Deprecated("")
    var isUsingSliceColorAsValueLineColor: Boolean
        get() = isUseValueColorForLineEnabled
        set(enabled) {
            setUseValueColorForLine(enabled)
        }

    fun setUseValueColorForLine(enabled: Boolean) {
        isUseValueColorForLineEnabled = enabled
    }

    enum class ValuePosition {
        INSIDE_SLICE, OUTSIDE_SLICE
    }
}