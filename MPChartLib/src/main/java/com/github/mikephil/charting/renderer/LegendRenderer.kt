package com.github.mikephil.charting.renderer

import android.graphics.*
import android.graphics.Paint.Align
import com.github.mikephil.charting.components.Legend
import com.github.mikephil.charting.components.Legend.*
import com.github.mikephil.charting.components.LegendEntry
import com.github.mikephil.charting.data.ChartData
import com.github.mikephil.charting.interfaces.datasets.IBarDataSet
import com.github.mikephil.charting.interfaces.datasets.ICandleDataSet
import com.github.mikephil.charting.interfaces.datasets.IPieDataSet
import com.github.mikephil.charting.utils.ColorTemplate
import com.github.mikephil.charting.utils.Utils.calcTextHeight
import com.github.mikephil.charting.utils.Utils.calcTextWidth
import com.github.mikephil.charting.utils.Utils.convertDpToPixel
import com.github.mikephil.charting.utils.Utils.getLineHeight
import com.github.mikephil.charting.utils.Utils.getLineSpacing
import com.github.mikephil.charting.utils.ViewPortHandler
import java.util.*

class LegendRenderer : Renderer {

    /**
     * paint for the legend labels
     */
    protected var mLegendLabelPaint: Paint? = null

    /**
     * paint used for the legend forms
     */
    protected var mLegendFormPaint: Paint? = null

    /**
     * the legend object this renderer renders
     */
    protected var mLegend: Legend? = null

    constructor(viewPortHandler: ViewPortHandler, legend: Legend) : super(viewPortHandler) {
        mLegend = legend
        mLegendLabelPaint = Paint(Paint.ANTI_ALIAS_FLAG)
        mLegendLabelPaint!!.textSize = convertDpToPixel(9f)
        mLegendLabelPaint!!.textAlign = Align.LEFT
        mLegendFormPaint = Paint(Paint.ANTI_ALIAS_FLAG)
        mLegendFormPaint!!.style = Paint.Style.FILL
    }

    /**
     * Returns the Paint object used for drawing the Legend labels.
     *
     * @return
     */
    fun getLabelPaint(): Paint? {
        return mLegendLabelPaint
    }

    /**
     * Returns the Paint object used for drawing the Legend forms.
     *
     * @return
     */
    fun getFormPaint(): Paint? {
        return mLegendFormPaint
    }


    protected var computedEntries: MutableList<LegendEntry> = ArrayList(16)

    /**
     * Prepares the legend and calculates all needed forms, labels and colors.
     *
     * @param data
     */
    fun computeLegend(data: ChartData<*>) {
        if (!mLegend!!.isLegendCustom()) {
            computedEntries.clear()

            // loop for building up the colors and labels used in the legend
            for (i in 0 until data.getDataSetCount()) {
                val dataSet = data.getDataSetByIndex(i) ?: continue
                val clrs = dataSet.getColors()
                val entryCount = dataSet.getEntryCount()

                // if we have a barchart with stacked bars
                if (dataSet is IBarDataSet && dataSet.isStacked()) {
                    val bds = dataSet
                    val sLabels = bds.getStackLabels()
                    val minEntries = Math.min(clrs!!.size, bds.getStackSize())
                    for (j in 0 until minEntries) {
                        var label: String?
                        label = if (sLabels!!.size > 0) {
                            val labelIndex = j % minEntries
                            if (labelIndex < sLabels.size) sLabels[labelIndex] else null
                        } else {
                            null
                        }
                        computedEntries.add(
                            LegendEntry(
                                label!!,
                                dataSet.getForm()!!,
                                dataSet.getFormSize(),
                                dataSet.getFormLineWidth(),
                                dataSet.getFormLineDashEffect(),
                                clrs[j]!!
                            )
                        )
                    }
                    if (bds.getLabel() != null) {
                        // add the legend description label
                        computedEntries.add(
                            LegendEntry(
                                dataSet.getLabel(),
                                LegendForm.NONE, Float.NaN, Float.NaN,
                                null,
                                ColorTemplate.COLOR_NONE
                            )
                        )
                    }
                } else if (dataSet is IPieDataSet) {
                    val pds = dataSet
                    var j = 0
                    while (j < clrs!!.size && j < entryCount) {
                        computedEntries.add(
                            LegendEntry(
                                pds.getEntryForIndex(j)?.getLabel()!!,
                                dataSet.getForm()!!,
                                dataSet.getFormSize(),
                                dataSet.getFormLineWidth(),
                                dataSet.getFormLineDashEffect(),
                                clrs[j]
                            )
                        )
                        j++
                    }
                    if (pds.getLabel() != null) {
                        // add the legend description label
                        computedEntries.add(
                            LegendEntry(
                                dataSet.getLabel(),
                                LegendForm.NONE, Float.NaN, Float.NaN,
                                null,
                                ColorTemplate.COLOR_NONE
                            )
                        )
                    }
                } else if (dataSet is ICandleDataSet && (dataSet as ICandleDataSet).getDecreasingColor() !=
                    ColorTemplate.COLOR_NONE
                ) {
                    val decreasingColor = (dataSet as ICandleDataSet).getDecreasingColor()
                    val increasingColor = (dataSet as ICandleDataSet).getIncreasingColor()
                    computedEntries.add(
                        LegendEntry(
                            null,
                            dataSet.getForm()!!,
                            dataSet.getFormSize(),
                            dataSet.getFormLineWidth(),
                            dataSet.getFormLineDashEffect(),
                            decreasingColor
                        )
                    )
                    computedEntries.add(
                        LegendEntry(
                            dataSet.getLabel(),
                            dataSet.getForm()!!,
                            dataSet.getFormSize(),
                            dataSet.getFormLineWidth(),
                            dataSet.getFormLineDashEffect(),
                            increasingColor
                        )
                    )
                } else { // all others
                    var j = 0
                    while (j < clrs!!.size && j < entryCount) {
                        var label: String?

                        // if multiple colors are set for a DataSet, group them
                        label = if (j < clrs.size - 1 && j < entryCount - 1) {
                            null
                        } else { // add label to the last entry
                            data.getDataSetByIndex(i)!!.getLabel()
                        }
                        computedEntries.add(
                            LegendEntry(
                                label,
                                dataSet.getForm()!!,
                                dataSet.getFormSize(),
                                dataSet.getFormLineWidth(),
                                dataSet.getFormLineDashEffect(),
                                clrs[j]
                            )
                        )
                        j++
                    }
                }
            }
            if (mLegend!!.getExtraEntries() != null) {
                Collections.addAll(computedEntries.toMutableList(), mLegend!!.getExtraEntries()!!)
            }
            mLegend!!.setEntries(computedEntries)
        }
        val tf = mLegend!!.getTypeface()
        if (tf != null) mLegendLabelPaint!!.typeface = tf
        mLegendLabelPaint!!.textSize = mLegend!!.getTextSize()
        mLegendLabelPaint!!.color = mLegend!!.getTextColor()

        // calculate all dimensions of the mLegend
        mLegend!!.calculateDimensions(mLegendLabelPaint, mViewPortHandler!!)
    }

    protected var legendFontMetrics = Paint.FontMetrics()

    fun renderLegend(c: Canvas) {
        if (!mLegend!!.isEnabled()) return
        val tf = mLegend!!.getTypeface()
        if (tf != null) mLegendLabelPaint!!.typeface = tf
        mLegendLabelPaint!!.textSize = mLegend!!.getTextSize()
        mLegendLabelPaint!!.color = mLegend!!.getTextColor()
        val labelLineHeight = getLineHeight(mLegendLabelPaint!!, legendFontMetrics)
        val labelLineSpacing = (getLineSpacing(
            mLegendLabelPaint!!, legendFontMetrics
        )
                + convertDpToPixel(mLegend!!.getYEntrySpace()))
        val formYOffset = labelLineHeight - calcTextHeight(
            mLegendLabelPaint!!, "ABC"
        ) / 2f
        val entries = mLegend!!.getEntries()
        val formToTextSpace = convertDpToPixel(
            mLegend!!.getFormToTextSpace()
        )
        val xEntrySpace = convertDpToPixel(
            mLegend!!.getXEntrySpace()
        )
        val orientation = mLegend!!.getOrientation()
        val horizontalAlignment = mLegend!!.getHorizontalAlignment()
        val verticalAlignment = mLegend!!.getVerticalAlignment()
        val direction = mLegend!!.getDirection()
        val defaultFormSize = convertDpToPixel(
            mLegend!!.getFormSize()
        )

        // space between the entries
        val stackSpace = convertDpToPixel(
            mLegend!!.getStackSpace()
        )
        val yoffset = mLegend!!.getYOffset()
        val xoffset = mLegend!!.getXOffset()
        var originPosX = 0f
        when (horizontalAlignment) {
            LegendHorizontalAlignment.LEFT -> {
                originPosX =
                    if (orientation === LegendOrientation.VERTICAL) xoffset else mViewPortHandler!!.contentLeft() + xoffset
                if (direction === LegendDirection.RIGHT_TO_LEFT) originPosX += mLegend!!.mNeededWidth
            }
            LegendHorizontalAlignment.RIGHT -> {
                originPosX =
                    if (orientation === LegendOrientation.VERTICAL) mViewPortHandler!!.getChartWidth() - xoffset else mViewPortHandler!!.contentRight() - xoffset
                if (direction === LegendDirection.LEFT_TO_RIGHT) originPosX -= mLegend!!.mNeededWidth
            }
            LegendHorizontalAlignment.CENTER -> {
                originPosX =
                    if (orientation === LegendOrientation.VERTICAL) mViewPortHandler!!.getChartWidth() / 2f else mViewPortHandler!!.contentLeft()
                +mViewPortHandler!!.contentWidth() / 2f
                originPosX += if (direction === LegendDirection.LEFT_TO_RIGHT) +xoffset else -xoffset

                // Horizontally layed out legends do the center offset on a line basis,
                // So here we offset the vertical ones only.
                if (orientation === LegendOrientation.VERTICAL) {
                    originPosX += (if (direction === LegendDirection.LEFT_TO_RIGHT) -mLegend!!.mNeededWidth / 2.0 + xoffset else mLegend!!.mNeededWidth / 2.0 - xoffset).toFloat()
                }
            }
            else -> {}
        }
        when (orientation) {
            LegendOrientation.HORIZONTAL -> {
                val calculatedLineSizes = mLegend!!.getCalculatedLineSizes()
                val calculatedLabelSizes = mLegend!!.getCalculatedLabelSizes()
                val calculatedLabelBreakPoints = mLegend!!.getCalculatedLabelBreakPoints()
                var posX = originPosX
                var posY = 0f
                when (verticalAlignment) {
                    LegendVerticalAlignment.TOP -> posY = yoffset
                    LegendVerticalAlignment.BOTTOM -> posY =
                        mViewPortHandler!!.getChartHeight() - yoffset - mLegend!!.mNeededHeight
                    LegendVerticalAlignment.CENTER -> posY =
                        (mViewPortHandler!!.getChartHeight() - mLegend!!.mNeededHeight) / 2f + yoffset
                    else -> {}
                }
                var lineIndex = 0
                var i = 0
                val count = entries!!.size
                while (i < count) {
                    val e = entries[i]
                    val drawingForm = e.form !== LegendForm.NONE
                    val formSize =
                        if (java.lang.Float.isNaN(e.formSize)) defaultFormSize else convertDpToPixel(
                            e.formSize
                        )
                    if (i < calculatedLabelBreakPoints!!.size && calculatedLabelBreakPoints[i]) {
                        posX = originPosX
                        posY += labelLineHeight + labelLineSpacing
                    }
                    if (posX == originPosX && horizontalAlignment === LegendHorizontalAlignment.CENTER && lineIndex < calculatedLineSizes!!.size) {
                        posX += (if (direction === LegendDirection.RIGHT_TO_LEFT) calculatedLineSizes[lineIndex].width else -calculatedLineSizes[lineIndex].width) / 2f
                        lineIndex++
                    }
                    val isStacked = e.label == null // grouped forms have null labels
                    if (drawingForm) {
                        if (direction === LegendDirection.RIGHT_TO_LEFT) posX -= formSize
                        drawForm(c, posX, posY + formYOffset, e, mLegend)
                        if (direction === LegendDirection.LEFT_TO_RIGHT) posX += formSize
                    }
                    if (!isStacked) {
                        if (drawingForm) posX += if (direction === LegendDirection.RIGHT_TO_LEFT) -formToTextSpace else formToTextSpace
                        if (direction === LegendDirection.RIGHT_TO_LEFT) posX -= calculatedLabelSizes!![i].width
                        drawLabel(c, posX, posY + labelLineHeight, e.label)
                        if (direction === LegendDirection.LEFT_TO_RIGHT) posX += calculatedLabelSizes!![i].width
                        posX += if (direction === LegendDirection.RIGHT_TO_LEFT) -xEntrySpace else xEntrySpace
                    } else posX += if (direction === LegendDirection.RIGHT_TO_LEFT) -stackSpace else stackSpace
                    i++
                }
            }
            LegendOrientation.VERTICAL -> {

                // contains the stacked legend size in pixels
                var stack = 0f
                var wasStacked = false
                var posY = 0f
                when (verticalAlignment) {
                    LegendVerticalAlignment.TOP -> {
                        posY =
                            if (horizontalAlignment === LegendHorizontalAlignment.CENTER) 0f else mViewPortHandler!!.contentTop()
                        posY += yoffset
                    }
                    LegendVerticalAlignment.BOTTOM -> {
                        posY =
                            if (horizontalAlignment === LegendHorizontalAlignment.CENTER) mViewPortHandler!!.getChartHeight() else mViewPortHandler!!.contentBottom()
                        posY -= mLegend!!.mNeededHeight + yoffset
                    }
                    LegendVerticalAlignment.CENTER -> posY =
                        (mViewPortHandler!!.getChartHeight() / 2f
                                - mLegend!!.mNeededHeight / 2f
                                + mLegend!!.getYOffset())
                    else -> {}
                }
                var i = 0
                while (i < entries!!.size) {
                    val e = entries[i]
                    val drawingForm = e.form !== LegendForm.NONE
                    val formSize =
                        if (java.lang.Float.isNaN(e.formSize)) defaultFormSize else convertDpToPixel(
                            e.formSize
                        )
                    var posX = originPosX
                    if (drawingForm) {
                        if (direction === LegendDirection.LEFT_TO_RIGHT) posX += stack else posX -= formSize - stack
                        drawForm(c, posX, posY + formYOffset, e, mLegend)
                        if (direction === LegendDirection.LEFT_TO_RIGHT) posX += formSize
                    }
                    if (e.label != null) {
                        if (drawingForm && !wasStacked) posX += if (direction === LegendDirection.LEFT_TO_RIGHT) formToTextSpace else -formToTextSpace else if (wasStacked) posX =
                            originPosX
                        if (direction === LegendDirection.RIGHT_TO_LEFT) posX -= calcTextWidth(
                            mLegendLabelPaint!!, e.label
                        ).toFloat()
                        if (!wasStacked) {
                            drawLabel(c, posX, posY + labelLineHeight, e.label)
                        } else {
                            posY += labelLineHeight + labelLineSpacing
                            drawLabel(c, posX, posY + labelLineHeight, e.label)
                        }

                        // make a step down
                        posY += labelLineHeight + labelLineSpacing
                        stack = 0f
                    } else {
                        stack += formSize + stackSpace
                        wasStacked = true
                    }
                    i++
                }
            }
            else -> {}
        }
    }

    private val mLineFormPath = Path()

    /**
     * Draws the Legend-form at the given position with the color at the given
     * index.
     *
     * @param c      canvas to draw with
     * @param x      position
     * @param y      position
     * @param entry  the entry to render
     * @param legend the legend context
     */
    protected fun drawForm(
        c: Canvas,
        x: Float, y: Float,
        entry: LegendEntry,
        legend: Legend?
    ) {
        if (entry.formColor == ColorTemplate.COLOR_SKIP || entry.formColor == ColorTemplate.COLOR_NONE || entry.formColor == 0) return
        val restoreCount = c.save()
        var form = entry.form
        if (form === LegendForm.DEFAULT) form = legend!!.getForm()!!
        mLegendFormPaint!!.color = entry.formColor
        val formSize = convertDpToPixel(
            if (java.lang.Float.isNaN(entry.formSize)) legend!!.getFormSize() else entry.formSize
        )
        val half = formSize / 2f
        when (form) {
            LegendForm.NONE -> {}
            LegendForm.EMPTY -> {}
            LegendForm.DEFAULT, LegendForm.CIRCLE -> {
                mLegendFormPaint!!.style = Paint.Style.FILL
                c.drawCircle(x + half, y, half, mLegendFormPaint!!)
            }
            LegendForm.SQUARE -> {
                mLegendFormPaint!!.style = Paint.Style.FILL
                c.drawRect(x, y - half, x + formSize, y + half, mLegendFormPaint!!)
            }
            LegendForm.LINE -> {
                val formLineWidth = convertDpToPixel(
                    if (java.lang.Float.isNaN(entry.formLineWidth)) legend!!.getFormLineWidth() else entry.formLineWidth
                )
                val formLineDashEffect =
                    if (entry.formLineDashEffect == null) legend!!.getFormLineDashEffect() else entry.formLineDashEffect
                mLegendFormPaint!!.style = Paint.Style.STROKE
                mLegendFormPaint!!.strokeWidth = formLineWidth
                mLegendFormPaint!!.pathEffect = formLineDashEffect
                mLineFormPath.reset()
                mLineFormPath.moveTo(x, y)
                mLineFormPath.lineTo(x + formSize, y)
                c.drawPath(mLineFormPath, mLegendFormPaint!!)
            }
        }
        c.restoreToCount(restoreCount)
    }

    /**
     * Draws the provided label at the given position.
     *
     * @param c     canvas to draw with
     * @param x
     * @param y
     * @param label the label to draw
     */
    protected fun drawLabel(c: Canvas, x: Float, y: Float, label: String?) {
        c.drawText(label!!, x, y, mLegendLabelPaint!!)
    }
}