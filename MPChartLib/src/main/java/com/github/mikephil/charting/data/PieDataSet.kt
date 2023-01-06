package com.github.mikephil.charting.data

import com.github.mikephil.charting.interfaces.datasets.IPieDataSet
import com.github.mikephil.charting.utils.Utils.convertDpToPixel

class PieDataSet : DataSet<PieEntry?>, IPieDataSet {

    /**
     * the space in pixels between the chart-slices, default 0f
     */
    private var mSliceSpace = 0f
    private var mAutomaticallyDisableSliceSpacing = false

    /**
     * indicates the selection distance of a pie slice
     */
    private var mShift = 18f

    private var mXValuePosition = ValuePosition.INSIDE_SLICE
    private var mYValuePosition = ValuePosition.INSIDE_SLICE
    private var mValueLineColor = -0x1000000
    private var mUseValueColorForLine = false
    private var mValueLineWidth = 1.0f
    private var mValueLinePart1OffsetPercentage = 75f
    private var mValueLinePart1Length = 0.3f
    private var mValueLinePart2Length = 0.4f
    private var mValueLineVariableLength = true
    private var mHighlightColor: Int? = null

    constructor(yVals: MutableList<PieEntry?>?, label: String) : super(yVals, label) {
//        mShift = Utils.convertDpToPixel(12f);
    }

    override fun copy(): DataSet<PieEntry?> {
        val entries: MutableList<PieEntry?> = ArrayList()
        for (i in mEntries!!.indices) {
            entries.add(mEntries!![i]!!.copy())
        }
        val copied = PieDataSet(entries, getLabel())
        copy(copied)
        return copied
    }

    protected fun copy(pieDataSet: PieDataSet) {
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
    fun setSliceSpace(spaceDp: Float) {
        var spaceDp = spaceDp
        if (spaceDp > 20) spaceDp = 20f
        if (spaceDp < 0) spaceDp = 0f
        mSliceSpace = convertDpToPixel(spaceDp)
    }

    override fun getSliceSpace(): Float {
        return mSliceSpace
    }

    /**
     * When enabled, slice spacing will be 0.0 when the smallest value is going to be
     * smaller than the slice spacing itself.
     *
     * @param autoDisable
     */
    fun setAutomaticallyDisableSliceSpacing(autoDisable: Boolean) {
        mAutomaticallyDisableSliceSpacing = autoDisable
    }

    /**
     * When enabled, slice spacing will be 0.0 when the smallest value is going to be
     * smaller than the slice spacing itself.
     *
     * @return
     */
    override fun isAutomaticallyDisableSliceSpacingEnabled(): Boolean {
        return mAutomaticallyDisableSliceSpacing
    }

    /**
     * sets the distance the highlighted piechart-slice of this DataSet is
     * "shifted" away from the center of the chart, default 12f
     *
     * @param shift
     */
    fun setSelectionShift(shift: Float) {
        mShift = convertDpToPixel(shift)
    }

    override fun getSelectionShift(): Float {
        return mShift
    }

    override fun getXValuePosition(): ValuePosition {
        return mXValuePosition
    }

    fun setXValuePosition(xValuePosition: ValuePosition) {
        mXValuePosition = xValuePosition
    }

    override fun getYValuePosition(): ValuePosition {
        return mYValuePosition
    }

    fun setYValuePosition(yValuePosition: ValuePosition) {
        mYValuePosition = yValuePosition
    }

    /**
     * This method is deprecated.
     * Use isUseValueColorForLineEnabled() instead.
     */
    @Deprecated("")
    fun isUsingSliceColorAsValueLineColor(): Boolean {
        return isUseValueColorForLineEnabled()
    }

    /**
     * This method is deprecated.
     * Use setUseValueColorForLine(...) instead.
     *
     * @param enabled
     */
    @Deprecated("")
    fun setUsingSliceColorAsValueLineColor(enabled: Boolean) {
        setUseValueColorForLine(enabled)
    }

    /**
     * When valuePosition is OutsideSlice, indicates line color
     */
    override fun getValueLineColor(): Int {
        return mValueLineColor
    }

    fun setValueLineColor(valueLineColor: Int) {
        mValueLineColor = valueLineColor
    }

    override fun isUseValueColorForLineEnabled(): Boolean {
        return mUseValueColorForLine
    }

    fun setUseValueColorForLine(enabled: Boolean) {
        mUseValueColorForLine = enabled
    }

    /**
     * When valuePosition is OutsideSlice, indicates line width
     */
    override fun getValueLineWidth(): Float {
        return mValueLineWidth
    }

    fun setValueLineWidth(valueLineWidth: Float) {
        mValueLineWidth = valueLineWidth
    }

    /**
     * When valuePosition is OutsideSlice, indicates offset as percentage out of the slice size
     */
    override fun getValueLinePart1OffsetPercentage(): Float {
        return mValueLinePart1OffsetPercentage
    }

    fun setValueLinePart1OffsetPercentage(valueLinePart1OffsetPercentage: Float) {
        mValueLinePart1OffsetPercentage = valueLinePart1OffsetPercentage
    }

    /**
     * When valuePosition is OutsideSlice, indicates length of first half of the line
     */
    override fun getValueLinePart1Length(): Float {
        return mValueLinePart1Length
    }

    fun setValueLinePart1Length(valueLinePart1Length: Float) {
        mValueLinePart1Length = valueLinePart1Length
    }

    /**
     * When valuePosition is OutsideSlice, indicates length of second half of the line
     */
    override fun getValueLinePart2Length(): Float {
        return mValueLinePart2Length
    }

    fun setValueLinePart2Length(valueLinePart2Length: Float) {
        mValueLinePart2Length = valueLinePart2Length
    }

    /**
     * When valuePosition is OutsideSlice, this allows variable line length
     */
    override fun isValueLineVariableLength(): Boolean {
        return mValueLineVariableLength
    }

    fun setValueLineVariableLength(valueLineVariableLength: Boolean) {
        mValueLineVariableLength = valueLineVariableLength
    }

    /** Gets the color for the highlighted sector  */
    override fun getHighlightColor(): Int? {
        return mHighlightColor
    }

    /** Sets the color for the highlighted sector (null for using entry color)  */
    fun setHighlightColor(color: Int?) {
        mHighlightColor = color
    }

    enum class ValuePosition {
        INSIDE_SLICE, OUTSIDE_SLICE
    }
}