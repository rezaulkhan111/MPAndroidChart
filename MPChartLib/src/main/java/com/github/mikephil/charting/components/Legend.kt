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
class Legend() : ComponentBase() {
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
    var entries = arrayOf<LegendEntry>()
        private set

    /**
     * Entries that will be appended to the end of the auto calculated entries after calculating the legend.
     * (if the legend has already been calculated, you will need to call notifyDataSetChanged() to let the changes take effect)
     */
    var extraEntries: Array<LegendEntry>
        private set
    /**
     * @return true if a custom legend entries has been set default
     * false (automatic legend)
     */
    /**
     * Are the legend labels/colors a custom value or auto calculated? If false,
     * then it's auto, if true, then custom. default false (automatic legend)
     */
    var isLegendCustom = false
        private set
    /**
     * returns the horizontal alignment of the legend
     *
     * @return
     */
    /**
     * sets the horizontal alignment of the legend
     *
     * @param value
     */
    var horizontalAlignment = LegendHorizontalAlignment.LEFT
    /**
     * returns the vertical alignment of the legend
     *
     * @return
     */
    /**
     * sets the vertical alignment of the legend
     *
     * @param value
     */
    var verticalAlignment = LegendVerticalAlignment.BOTTOM
    /**
     * returns the orientation of the legend
     *
     * @return
     */
    /**
     * sets the orientation of the legend
     *
     * @param value
     */
    var orientation = LegendOrientation.HORIZONTAL

    /**
     * returns whether the legend will draw inside the chart or outside
     *
     * @return
     */
    var isDrawInsideEnabled = false
        private set
    /**
     * returns the text direction of the legend
     *
     * @return
     */
    /**
     * sets the text direction of the legend
     *
     * @param pos
     */
    /**
     * the text direction for the legend
     */
    var direction = LegendDirection.LEFT_TO_RIGHT
    /**
     * returns the current form/shape that is set for the legend
     *
     * @return
     */
    /**
     * sets the form/shape of the legend forms
     *
     * @param shape
     */
    /**
     * the shape/form the legend colors are drawn in
     */
    var form = LegendForm.SQUARE
    /**
     * returns the size in dp of the legend forms
     *
     * @return
     */
    /**
     * sets the size in dp of the legend forms, default 8f
     *
     * @param size
     */
    /**
     * the size of the legend forms/shapes
     */
    var formSize = 8f
    /**
     * returns the line width in dp for drawing forms that consist of lines
     *
     * @return
     */
    /**
     * sets the line width in dp for forms that consist of lines, default 3f
     *
     * @param size
     */
    /**
     * the size of the legend forms/shapes
     */
    var formLineWidth = 3f
    /**
     * @return The line dash path effect used for shapes that consist of lines.
     */
    /**
     * Sets the line dash path effect used for shapes that consist of lines.
     *
     * @param dashPathEffect
     */
    /**
     * Line dash path effect used for shapes that consist of lines.
     */
    var formLineDashEffect: DashPathEffect? = null
    /**
     * returns the space between the legend entries on a horizontal axis in
     * pixels
     *
     * @return
     */
    /**
     * sets the space between the legend entries on a horizontal axis in pixels,
     * converts to dp internally
     *
     * @param space
     */
    /**
     * the space between the legend entries on a horizontal axis, default 6f
     */
    var xEntrySpace = 6f
    /**
     * returns the space between the legend entries on a vertical axis in pixels
     *
     * @return
     */
    /**
     * sets the space between the legend entries on a vertical axis in pixels,
     * converts to dp internally
     *
     * @param space
     */
    /**
     * the space between the legend entries on a vertical axis, default 5f
     */
    var yEntrySpace = 0f
    /**
     * returns the space between the form and the actual label/text
     *
     * @return
     */
    /**
     * sets the space between the form and the actual label/text, converts to dp
     * internally
     *
     * @param space
     */
    /**
     * the space between the legend entries on a vertical axis, default 2f
     * private float mYEntrySpace = 2f; / ** the space between the form and the
     * actual label/text
     */
    var formToTextSpace = 5f
    /**
     * returns the space that is left out between stacked forms (with no label)
     *
     * @return
     */
    /**
     * sets the space that is left out between stacked forms (with no label)
     *
     * @param space
     */
    /**
     * the space that should be left between stacked forms
     */
    var stackSpace = 3f
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
    /**
     * The maximum relative size out of the whole chart view. / If
     * the legend is to the right/left of the chart, then this affects the width
     * of the legend. / If the legend is to the top/bottom of the chart, then
     * this affects the height of the legend. / default: 0.95f (95%)
     *
     * @param maxSize
     */
    /**
     * the maximum relative size out of the whole chart view in percent
     */
    var maxSizePercent = 0.95f

    /**
     * Constructor. Provide entries for the legend.
     *
     * @param entries
     */
    constructor(entries: Array<LegendEntry>?) : this() {
        requireNotNull(entries) { "entries array is NULL" }
        this.entries = entries
    }

    /**
     * This method sets the automatically computed colors for the legend. Use setCustom(...) to set custom colors.
     *
     * @param entries
     */
    fun setEntries(entries: List<LegendEntry>) {
        this.entries = entries.toTypedArray()
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
        val formToTextSpace = convertDpToPixel(
            formToTextSpace
        )
        for (entry in entries) {
            val formSize = convertDpToPixel(
                if (java.lang.Float.isNaN(entry.formSize)) formSize else entry.formSize
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
        for (entry in entries) {
            val label = entry.label ?: continue
            val length = calcTextHeight(p!!, label).toFloat()
            if (length > max) max = length
        }
        return max
    }

    fun setExtra(entries: List<LegendEntry>) {
        extraEntries = entries.toTypedArray()
    }

    fun setExtra(entries: Array<LegendEntry>?) {
        var entries = entries
        if (entries == null) entries = arrayOf()
        extraEntries = entries
    }

    /**
     * Entries that will be appended to the end of the auto calculated
     * entries after calculating the legend.
     * (if the legend has already been calculated, you will need to call notifyDataSetChanged()
     * to let the changes take effect)
     */
    fun setExtra(colors: IntArray, labels: Array<String?>) {
        val entries: MutableList<LegendEntry> = ArrayList()
        for (i in 0 until Math.min(colors.size, labels.size)) {
            val entry = LegendEntry()
            entry.formColor = colors[i]
            entry.label = labels[i]
            if (entry.formColor == ColorTemplate.COLOR_SKIP ||
                entry.formColor == 0
            ) entry.form =
                LegendForm.NONE else if (entry.formColor == ColorTemplate.COLOR_NONE) entry.form =
                LegendForm.EMPTY
            entries.add(entry)
        }
        extraEntries = entries.toTypedArray()
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
        this.entries = entries
        isLegendCustom = true
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
        this.entries = entries.toTypedArray()
        isLegendCustom = true
    }

    /**
     * Calling this will disable the custom legend entries (set by
     * setCustom(...)). Instead, the entries will again be calculated
     * automatically (after notifyDataSetChanged() is called).
     */
    fun resetCustom() {
        isLegendCustom = false
    }

    /**
     * sets whether the legend will draw inside the chart or outside
     *
     * @param value
     */
    fun setDrawInside(value: Boolean) {
        isDrawInsideEnabled = value
    }

    /**
     * the total width of the legend (needed width space)
     */
    @JvmField
    var mNeededWidth = 0f

    /**
     * the total height of the legend (needed height space)
     */
    @JvmField
    var mNeededHeight = 0f
    @JvmField
    var mTextHeightMax = 0f
    var mTextWidthMax = 0f
    /**
     * If this is set, then word wrapping the legend is enabled. This means the
     * legend will not be cut off if too long.
     *
     * @return
     */
    /**
     * Should the legend word wrap? / this is currently supported only for:
     * BelowChartLeft, BelowChartRight, BelowChartCenter. / note that word
     * wrapping a legend takes a toll on performance. / you may want to set
     * maxSizePercent when word wrapping, to set the point where the text wraps.
     * / default: false
     *
     * @param enabled
     */
    /**
     * flag that indicates if word wrapping is enabled
     */
    var isWordWrapEnabled = false
    private val mCalculatedLabelSizes: MutableList<FSize> = ArrayList(16)
    private val mCalculatedLabelBreakPoints: MutableList<Boolean> = ArrayList(16)
    private val mCalculatedLineSizes: MutableList<FSize> = ArrayList(16)
    val calculatedLabelSizes: List<FSize>
        get() = mCalculatedLabelSizes
    val calculatedLabelBreakPoints: List<Boolean>
        get() = mCalculatedLabelBreakPoints
    val calculatedLineSizes: List<FSize>
        get() = mCalculatedLineSizes

    /**
     * Calculates the dimensions of the Legend. This includes the maximum width
     * and height of a single entry, as well as the total width and height of
     * the Legend.
     *
     * @param labelpaint
     */
    fun calculateDimensions(labelpaint: Paint?, viewPortHandler: ViewPortHandler) {
        val defaultFormSize = convertDpToPixel(
            formSize
        )
        val stackSpace = convertDpToPixel(
            stackSpace
        )
        val formToTextSpace = convertDpToPixel(
            formToTextSpace
        )
        val xEntrySpace = convertDpToPixel(
            xEntrySpace
        )
        val yEntrySpace = convertDpToPixel(
            yEntrySpace
        )
        val wordWrapEnabled = isWordWrapEnabled
        val entries = entries
        val entryCount = entries.size
        mTextWidthMax = getMaximumEntryWidth(labelpaint)
        mTextHeightMax = getMaximumEntryHeight(labelpaint)
        when (orientation) {
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
                    val drawingForm = e.form != LegendForm.NONE
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
                val contentWidth = viewPortHandler.contentWidth() * maxSizePercent

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
                    val drawingForm = e.form != LegendForm.NONE
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

    /**
     * default constructor
     */
    init {
        mTextSize = convertDpToPixel(10f)
        mXOffset = convertDpToPixel(5f)
        mYOffset = convertDpToPixel(3f) // 2
    }
}