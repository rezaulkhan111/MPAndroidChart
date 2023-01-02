package com.github.mikephil.charting.components

import android.graphics.DashPathEffect
import android.graphics.Paint
import com.github.mikephil.charting.utils.ColorTemplate
import com.github.mikephil.charting.utils.FSize
import com.github.mikephil.charting.utils.FSize.Companion.getInstance
import com.github.mikephil.charting.utils.Utils.calcTextHeight
import com.github.mikephil.charting.utils.Utils.calcTextSize
import com.github.mikephil.charting.utils.Utils.calcTextWidth
import com.github.mikephil.charting.utils.Utils.convertDpToPixel
import com.github.mikephil.charting.utils.Utils.getLineHeight
import com.github.mikephil.charting.utils.Utils.getLineSpacing
import com.github.mikephil.charting.utils.ViewPortHandler

/**
 * Class representing the legend of the chart. The legend will contain one entry
 * per color and DataSet. Multiple colors in one DataSet are grouped together.
 * The legend object is NOT available before setting data to the chart.
 *
 * @author Philipp Jahoda
 */
class Legend : ComponentBase {

    enum class LegendForm {
        /**
         * Avoid drawing a form
         */
        NONE,

        /**
         * Do not draw the a form, but leave space for it
         */
        EMPTY,

        /**
         * Use default (default dataset's form to the legend's form)
         */
        DEFAULT,

        /**
         * Draw a square
         */
        SQUARE,

        /**
         * Draw a circle
         */
        CIRCLE,

        /**
         * Draw a horizontal line
         */
        LINE
    }

    enum class LegendHorizontalAlignment {
        LEFT, CENTER, RIGHT
    }

    enum class LegendVerticalAlignment {
        TOP, CENTER, BOTTOM
    }

    enum class LegendOrientation {
        HORIZONTAL, VERTICAL
    }

    enum class LegendDirection {
        LEFT_TO_RIGHT, RIGHT_TO_LEFT
    }

    /**
     * The legend entries array
     */
    private var mEntries = arrayOf<LegendEntry>()

    /**
     * Entries that will be appended to the end of the auto calculated entries after calculating the legend.
     * (if the legend has already been calculated, you will need to call notifyDataSetChanged() to let the changes take effect)
     */
    private lateinit var mExtraEntries: Array<LegendEntry?>

    /**
     * Are the legend labels/colors a custom value or auto calculated? If false,
     * then it's auto, if true, then custom. default false (automatic legend)
     */
    private var mIsLegendCustom = false

    private var mHorizontalAlignment = LegendHorizontalAlignment.LEFT
    private var mVerticalAlignment = LegendVerticalAlignment.BOTTOM
    private var mOrientation = LegendOrientation.HORIZONTAL
    private var mDrawInside = false

    /**
     * the text direction for the legend
     */
    private var mDirection = LegendDirection.LEFT_TO_RIGHT

    /**
     * the shape/form the legend colors are drawn in
     */
    private var mShape = LegendForm.SQUARE

    /**
     * the size of the legend forms/shapes
     */
    private var mFormSize = 8f

    /**
     * the size of the legend forms/shapes
     */
    private var mFormLineWidth = 3f

    /**
     * Line dash path effect used for shapes that consist of lines.
     */
    private var mFormLineDashEffect: DashPathEffect? = null

    /**
     * the space between the legend entries on a horizontal axis, default 6f
     */
    private var mXEntrySpace = 6f

    /**
     * the space between the legend entries on a vertical axis, default 5f
     */
    private var mYEntrySpace = 0f

    /**
     * the space between the legend entries on a vertical axis, default 2f
     * private float mYEntrySpace = 2f; / ** the space between the form and the
     * actual label/text
     */
    private var mFormToTextSpace = 5f

    /**
     * the space that should be left between stacked forms
     */
    private var mStackSpace = 3f

    /**
     * the maximum relative size out of the whole chart view in percent
     */
    private var mMaxSizePercent = 0.95f

    /**
     * default constructor
     */
    constructor() {
        mTextSize = convertDpToPixel(10f)
        mXOffset = convertDpToPixel(5f)
        mYOffset = convertDpToPixel(3f) // 2
    }

    /**
     * Constructor. Provide entries for the legend.
     *
     * @param entries
     */
    constructor(entries: Array<LegendEntry>?) : super() {
        requireNotNull(entries) { "entries array is NULL" }
        mEntries = entries
    }

    /**
     * This method sets the automatically computed colors for the legend. Use setCustom(...) to set custom colors.
     *
     * @param entries
     */
    fun setEntries(entries: List<LegendEntry>) {
        mEntries = entries.toTypedArray()
    }

    fun getEntries(): Array<LegendEntry>? {
        return mEntries
    }

    /**
     * returns the maximum length in pixels across all legend labels + formsize
     * + formtotextspace
     *
     * @param p the paint object used for rendering the text
     * @return
     */
    fun getMaximumEntryWidth(p: Paint?): Float {
        var max = 0f
        var maxFormSize = 0f
        val formToTextSpace = convertDpToPixel(mFormToTextSpace)
        for (entry in mEntries) {
            val formSize = convertDpToPixel(
                if (java.lang.Float.isNaN(entry.formSize)) mFormSize else entry.formSize
            )
            if (formSize > maxFormSize) maxFormSize = formSize
            val label = entry.label ?: continue
            val length = calcTextWidth(p!!, label).toFloat()
            if (length > max) max = length
        }
        return max + maxFormSize + formToTextSpace
    }

    /**
     * returns the maximum height in pixels across all legend labels
     *
     * @param p the paint object used for rendering the text
     * @return
     */
    fun getMaximumEntryHeight(p: Paint?): Float {
        var max = 0f
        for (entry in mEntries) {
            val label = entry.label ?: continue
            val length = calcTextHeight(p!!, label).toFloat()
            if (length > max) max = length
        }
        return max
    }

    fun getExtraEntries(): Array<LegendEntry?>? {
        return mExtraEntries
    }

    fun setExtra(entries: List<LegendEntry?>) {
        mExtraEntries = entries.toTypedArray()
    }

    fun setExtra(entries: Array<LegendEntry?>?) {
        var entries = entries
        if (entries == null) entries = arrayOf()
        mExtraEntries = entries
    }

    /**
     * Entries that will be appended to the end of the auto calculated
     * entries after calculating the legend.
     * (if the legend has already been calculated, you will need to call notifyDataSetChanged()
     * to let the changes take effect)
     */
    fun setExtra(colors: IntArray, labels: Array<String?>) {
        val entries: MutableList<LegendEntry?> = ArrayList()
        for (i in 0 until Math.min(colors.size, labels.size)) {
            val entry = LegendEntry()
            entry.formColor = colors[i]
            entry.label = labels[i]!!
            if (entry.formColor == ColorTemplate.COLOR_SKIP ||
                entry.formColor == 0
            ) entry.form =
                LegendForm.NONE else if (entry.formColor == ColorTemplate.COLOR_NONE) entry.form =
                LegendForm.EMPTY
            entries.add(entry)
        }
        mExtraEntries = entries.toTypedArray()
    }

    /**
     * Sets a custom legend's entries array.
     * * A null label will start a group.
     * This will disable the feature that automatically calculates the legend
     * entries from the datasets.
     * Call resetCustom() to re-enable automatic calculation (and then
     * notifyDataSetChanged() is needed to auto-calculate the legend again)
     */
    fun setCustom(entries: Array<LegendEntry>) {
        mEntries = entries
        mIsLegendCustom = true
    }

    /**
     * Sets a custom legend's entries array.
     * * A null label will start a group.
     * This will disable the feature that automatically calculates the legend
     * entries from the datasets.
     * Call resetCustom() to re-enable automatic calculation (and then
     * notifyDataSetChanged() is needed to auto-calculate the legend again)
     */
    fun setCustom(entries: List<LegendEntry>) {
        mEntries = entries.toTypedArray()
        mIsLegendCustom = true
    }

    /**
     * Calling this will disable the custom legend entries (set by
     * setCustom(...)). Instead, the entries will again be calculated
     * automatically (after notifyDataSetChanged() is called).
     */
    fun resetCustom() {
        mIsLegendCustom = false
    }

    /**
     * @return true if a custom legend entries has been set default
     * false (automatic legend)
     */
    fun isLegendCustom(): Boolean {
        return mIsLegendCustom
    }

    /**
     * returns the horizontal alignment of the legend
     *
     * @return
     */
    fun getHorizontalAlignment(): LegendHorizontalAlignment? {
        return mHorizontalAlignment
    }

    /**
     * sets the horizontal alignment of the legend
     *
     * @param value
     */
    fun setHorizontalAlignment(value: LegendHorizontalAlignment) {
        mHorizontalAlignment = value
    }

    /**
     * returns the vertical alignment of the legend
     *
     * @return
     */
    fun getVerticalAlignment(): LegendVerticalAlignment? {
        return mVerticalAlignment
    }

    /**
     * sets the vertical alignment of the legend
     *
     * @param value
     */
    fun setVerticalAlignment(value: LegendVerticalAlignment) {
        mVerticalAlignment = value
    }

    /**
     * returns the orientation of the legend
     *
     * @return
     */
    fun getOrientation(): LegendOrientation? {
        return mOrientation
    }

    /**
     * sets the orientation of the legend
     *
     * @param value
     */
    fun setOrientation(value: LegendOrientation) {
        mOrientation = value
    }

    /**
     * returns whether the legend will draw inside the chart or outside
     *
     * @return
     */
    fun isDrawInsideEnabled(): Boolean {
        return mDrawInside
    }

    /**
     * sets whether the legend will draw inside the chart or outside
     *
     * @param value
     */
    fun setDrawInside(value: Boolean) {
        mDrawInside = value
    }

    /**
     * returns the text direction of the legend
     *
     * @return
     */
    fun getDirection(): LegendDirection? {
        return mDirection
    }

    /**
     * sets the text direction of the legend
     *
     * @param pos
     */
    fun setDirection(pos: LegendDirection) {
        mDirection = pos
    }

    /**
     * returns the current form/shape that is set for the legend
     *
     * @return
     */
    fun getForm(): LegendForm? {
        return mShape
    }

    /**
     * sets the form/shape of the legend forms
     *
     * @param shape
     */
    fun setForm(shape: LegendForm) {
        mShape = shape
    }

    /**
     * sets the size in dp of the legend forms, default 8f
     *
     * @param size
     */
    fun setFormSize(size: Float) {
        mFormSize = size
    }

    /**
     * returns the size in dp of the legend forms
     *
     * @return
     */
    fun getFormSize(): Float {
        return mFormSize
    }

    /**
     * sets the line width in dp for forms that consist of lines, default 3f
     *
     * @param size
     */
    fun setFormLineWidth(size: Float) {
        mFormLineWidth = size
    }

    /**
     * returns the line width in dp for drawing forms that consist of lines
     *
     * @return
     */
    fun getFormLineWidth(): Float {
        return mFormLineWidth
    }

    /**
     * Sets the line dash path effect used for shapes that consist of lines.
     *
     * @param dashPathEffect
     */
    fun setFormLineDashEffect(dashPathEffect: DashPathEffect?) {
        mFormLineDashEffect = dashPathEffect
    }

    /**
     * @return The line dash path effect used for shapes that consist of lines.
     */
    fun getFormLineDashEffect(): DashPathEffect? {
        return mFormLineDashEffect
    }

    /**
     * returns the space between the legend entries on a horizontal axis in
     * pixels
     *
     * @return
     */
    fun getXEntrySpace(): Float {
        return mXEntrySpace
    }

    /**
     * sets the space between the legend entries on a horizontal axis in pixels,
     * converts to dp internally
     *
     * @param space
     */
    fun setXEntrySpace(space: Float) {
        mXEntrySpace = space
    }

    /**
     * returns the space between the legend entries on a vertical axis in pixels
     *
     * @return
     */
    fun getYEntrySpace(): Float {
        return mYEntrySpace
    }

    /**
     * sets the space between the legend entries on a vertical axis in pixels,
     * converts to dp internally
     *
     * @param space
     */
    fun setYEntrySpace(space: Float) {
        mYEntrySpace = space
    }

    /**
     * returns the space between the form and the actual label/text
     *
     * @return
     */
    fun getFormToTextSpace(): Float {
        return mFormToTextSpace
    }

    /**
     * sets the space between the form and the actual label/text, converts to dp
     * internally
     *
     * @param space
     */
    fun setFormToTextSpace(space: Float) {
        mFormToTextSpace = space
    }

    /**
     * returns the space that is left out between stacked forms (with no label)
     *
     * @return
     */
    fun getStackSpace(): Float {
        return mStackSpace
    }

    /**
     * sets the space that is left out between stacked forms (with no label)
     *
     * @param space
     */
    fun setStackSpace(space: Float) {
        mStackSpace = space
    }

    /**
     * the total width of the legend (needed width space)
     */
    var mNeededWidth = 0f

    /**
     * the total height of the legend (needed height space)
     */
    var mNeededHeight = 0f

    var mTextHeightMax = 0f

    var mTextWidthMax = 0f

    /**
     * flag that indicates if word wrapping is enabled
     */
    private var mWordWrapEnabled = false

    /**
     * Should the legend word wrap? / this is currently supported only for:
     * BelowChartLeft, BelowChartRight, BelowChartCenter. / note that word
     * wrapping a legend takes a toll on performance. / you may want to set
     * maxSizePercent when word wrapping, to set the point where the text wraps.
     * / default: false
     *
     * @param enabled
     */
    fun setWordWrapEnabled(enabled: Boolean) {
        mWordWrapEnabled = enabled
    }

    /**
     * If this is set, then word wrapping the legend is enabled. This means the
     * legend will not be cut off if too long.
     *
     * @return
     */
    fun isWordWrapEnabled(): Boolean {
        return mWordWrapEnabled
    }

    /**
     * The maximum relative size out of the whole chart view. / If the legend is
     * to the right/left of the chart, then this affects the width of the
     * legend. / If the legend is to the top/bottom of the chart, then this
     * affects the height of the legend. / If the legend is the center of the
     * piechart, then this defines the size of the rectangular bounds out of the
     * size of the "hole". / default: 0.95f (95%)
     *
     * @return
     */
    fun getMaxSizePercent(): Float {
        return mMaxSizePercent
    }

    /**
     * The maximum relative size out of the whole chart view. / If
     * the legend is to the right/left of the chart, then this affects the width
     * of the legend. / If the legend is to the top/bottom of the chart, then
     * this affects the height of the legend. / default: 0.95f (95%)
     *
     * @param maxSize
     */
    fun setMaxSizePercent(maxSize: Float) {
        mMaxSizePercent = maxSize
    }

    private val mCalculatedLabelSizes: MutableList<FSize> = ArrayList(16)
    private val mCalculatedLabelBreakPoints: MutableList<Boolean> = ArrayList(16)
    private val mCalculatedLineSizes: MutableList<FSize> = ArrayList(16)

    fun getCalculatedLabelSizes(): List<FSize>? {
        return mCalculatedLabelSizes
    }

    fun getCalculatedLabelBreakPoints(): List<Boolean>? {
        return mCalculatedLabelBreakPoints
    }

    fun getCalculatedLineSizes(): List<FSize>? {
        return mCalculatedLineSizes
    }

    /**
     * Calculates the dimensions of the Legend. This includes the maximum width
     * and height of a single entry, as well as the total width and height of
     * the Legend.
     *
     * @param labelpaint
     */
    fun calculateDimensions(labelpaint: Paint?, viewPortHandler: ViewPortHandler) {
        val defaultFormSize = convertDpToPixel(mFormSize)
        val stackSpace = convertDpToPixel(mStackSpace)
        val formToTextSpace = convertDpToPixel(mFormToTextSpace)
        val xEntrySpace = convertDpToPixel(mXEntrySpace)
        val yEntrySpace = convertDpToPixel(mYEntrySpace)
        val wordWrapEnabled = mWordWrapEnabled
        val entries = mEntries
        val entryCount = entries.size
        mTextWidthMax = getMaximumEntryWidth(labelpaint)
        mTextHeightMax = getMaximumEntryHeight(labelpaint)
        when (mOrientation) {
            LegendOrientation.VERTICAL -> {
                var maxWidth = 0f
                var maxHeight = 0f
                var width = 0f
                val labelLineHeight = getLineHeight(
                    labelpaint!!
                )
                var wasStacked = false
                var i = 0
                while (i < entryCount) {
                    val e = entries[i]
                    val drawingForm = e.form !== LegendForm.NONE
                    val formSize =
                        if (java.lang.Float.isNaN(e.formSize)) defaultFormSize else convertDpToPixel(
                            e.formSize
                        )
                    val label = e.label
                    if (!wasStacked) width = 0f
                    if (drawingForm) {
                        if (wasStacked) width += stackSpace
                        width += formSize
                    }

                    // grouped forms have null labels
                    if (label != null) {

                        // make a step to the left
                        if (drawingForm && !wasStacked) width += formToTextSpace else if (wasStacked) {
                            maxWidth = Math.max(maxWidth, width)
                            maxHeight += labelLineHeight + yEntrySpace
                            width = 0f
                            wasStacked = false
                        }
                        width += calcTextWidth(labelpaint, label).toFloat()
                        maxHeight += labelLineHeight + yEntrySpace
                    } else {
                        wasStacked = true
                        width += formSize
                        if (i < entryCount - 1) width += stackSpace
                    }
                    maxWidth = Math.max(maxWidth, width)
                    i++
                }
                mNeededWidth = maxWidth
                mNeededHeight = maxHeight
            }
            LegendOrientation.HORIZONTAL -> {
                val labelLineHeight = getLineHeight(
                    labelpaint!!
                )
                val labelLineSpacing = getLineSpacing(
                    labelpaint
                ) + yEntrySpace
                val contentWidth = viewPortHandler.contentWidth() * mMaxSizePercent

                // Start calculating layout
                var maxLineWidth = 0f
                var currentLineWidth = 0f
                var requiredWidth = 0f
                var stackedStartIndex = -1
                mCalculatedLabelBreakPoints.clear()
                mCalculatedLabelSizes.clear()
                mCalculatedLineSizes.clear()
                var i = 0
                while (i < entryCount) {
                    val e = entries[i]
                    val drawingForm = e.form !== LegendForm.NONE
                    val formSize =
                        if (java.lang.Float.isNaN(e.formSize)) defaultFormSize else convertDpToPixel(
                            e.formSize
                        )
                    val label = e.label
                    mCalculatedLabelBreakPoints.add(false)
                    if (stackedStartIndex == -1) {
                        // we are not stacking, so required width is for this label
                        // only
                        requiredWidth = 0f
                    } else {
                        // add the spacing appropriate for stacked labels/forms
                        requiredWidth += stackSpace
                    }

                    // grouped forms have null labels
                    if (label != null) {
                        mCalculatedLabelSizes.add(
                            calcTextSize(
                                labelpaint, label
                            )
                        )
                        requiredWidth += if (drawingForm) formToTextSpace + formSize else 0f
                        requiredWidth += mCalculatedLabelSizes[i].width
                    } else {
                        mCalculatedLabelSizes.add(getInstance(0f, 0f))
                        requiredWidth += if (drawingForm) formSize else 0f
                        if (stackedStartIndex == -1) {
                            // mark this index as we might want to break here later
                            stackedStartIndex = i
                        }
                    }
                    if (label != null || i == entryCount - 1) {
                        val requiredSpacing = if (currentLineWidth == 0f) 0f else xEntrySpace
                        if (!wordWrapEnabled // No word wrapping, it must fit.
                            // The line is empty, it must fit
                            || currentLineWidth == 0f // It simply fits
                            || contentWidth - currentLineWidth >=
                            requiredSpacing + requiredWidth
                        ) {
                            // Expand current line
                            currentLineWidth += requiredSpacing + requiredWidth
                        } else { // It doesn't fit, we need to wrap a line

                            // Add current line size to array
                            mCalculatedLineSizes.add(getInstance(currentLineWidth, labelLineHeight))
                            maxLineWidth = Math.max(maxLineWidth, currentLineWidth)

                            // Start a new line
                            mCalculatedLabelBreakPoints[if (stackedStartIndex > -1) stackedStartIndex else i] =
                                true
                            currentLineWidth = requiredWidth
                        }
                        if (i == entryCount - 1) {
                            // Add last line size to array
                            mCalculatedLineSizes.add(getInstance(currentLineWidth, labelLineHeight))
                            maxLineWidth = Math.max(maxLineWidth, currentLineWidth)
                        }
                    }
                    stackedStartIndex = if (label != null) -1 else stackedStartIndex
                    i++
                }
                mNeededWidth = maxLineWidth
                mNeededHeight = (labelLineHeight
                        * mCalculatedLineSizes.size.toFloat()
                        + labelLineSpacing * (if (mCalculatedLineSizes.size == 0) 0 else mCalculatedLineSizes.size - 1).toFloat())
            }
        }
        mNeededHeight += mYOffset
        mNeededWidth += mXOffset
    }
}