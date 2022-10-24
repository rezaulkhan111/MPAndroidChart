package com.github.mikephil.charting.renderer

import android.graphics.*
import com.github.mikephil.charting.utils.Utils.convertDpToPixel
import com.github.mikephil.charting.utils.ViewPortHandler.contentWidth
import com.github.mikephil.charting.utils.ViewPortHandler.isFullyZoomedOutY
import com.github.mikephil.charting.utils.Transformer.getValuesByTouchPoint
import com.github.mikephil.charting.utils.ViewPortHandler.contentLeft
import com.github.mikephil.charting.utils.ViewPortHandler.contentTop
import com.github.mikephil.charting.utils.ViewPortHandler.contentBottom
import com.github.mikephil.charting.utils.MPPointD.Companion.recycleInstance
import com.github.mikephil.charting.utils.Utils.roundToNextSignificant
import com.github.mikephil.charting.utils.Utils.nextUp
import com.github.mikephil.charting.utils.ViewPortHandler.scaleX
import com.github.mikephil.charting.utils.ViewPortHandler.isFullyZoomedOutX
import com.github.mikephil.charting.utils.ViewPortHandler.contentRight
import com.github.mikephil.charting.utils.Utils.calcTextSize
import com.github.mikephil.charting.utils.Utils.calcTextHeight
import com.github.mikephil.charting.utils.Utils.getSizeOfRotatedRectangleByDegrees
import com.github.mikephil.charting.utils.FSize.Companion.recycleInstance
import com.github.mikephil.charting.utils.MPPointF.Companion.recycleInstance
import com.github.mikephil.charting.utils.Transformer.pointValuesToPixel
import com.github.mikephil.charting.utils.ViewPortHandler.isInBoundsX
import com.github.mikephil.charting.utils.Utils.calcTextWidth
import com.github.mikephil.charting.utils.ViewPortHandler.offsetRight
import com.github.mikephil.charting.utils.ViewPortHandler.chartWidth
import com.github.mikephil.charting.utils.Utils.drawXAxisValue
import com.github.mikephil.charting.utils.ViewPortHandler.contentRect
import com.github.mikephil.charting.utils.ViewPortHandler.offsetLeft
import com.github.mikephil.charting.utils.Transformer.getPixelForValues
import com.github.mikephil.charting.utils.Utils.getLineHeight
import com.github.mikephil.charting.utils.Utils.getLineSpacing
import com.github.mikephil.charting.utils.ViewPortHandler.chartHeight
import com.github.mikephil.charting.utils.Transformer.rectValueToPixel
import com.github.mikephil.charting.utils.ViewPortHandler.isInBoundsLeft
import com.github.mikephil.charting.utils.ViewPortHandler.isInBoundsRight
import com.github.mikephil.charting.utils.Fill.fillRect
import com.github.mikephil.charting.utils.Transformer.rectToPixelPhase
import com.github.mikephil.charting.utils.ViewPortHandler.isInBoundsY
import com.github.mikephil.charting.utils.Utils.drawImage
import com.github.mikephil.charting.utils.ViewPortHandler.smallestContentExtension
import com.github.mikephil.charting.utils.Transformer.pathValueToPixel
import com.github.mikephil.charting.utils.ViewPortHandler.isInBoundsTop
import com.github.mikephil.charting.utils.ViewPortHandler.isInBoundsBottom
import com.github.mikephil.charting.utils.Transformer.generateTransformedValuesLine
import com.github.mikephil.charting.utils.Utils.sDKInt
import com.github.mikephil.charting.utils.Utils.getPosition
import com.github.mikephil.charting.utils.ColorTemplate.colorWithAlpha
import com.github.mikephil.charting.utils.Transformer.generateTransformedValuesBubble
import com.github.mikephil.charting.utils.Transformer.generateTransformedValuesScatter
import com.github.mikephil.charting.utils.Transformer.generateTransformedValuesCandle
import com.github.mikephil.charting.utils.Transformer.rectToPixelPhaseHorizontal
import com.github.mikephil.charting.utils.ViewPortHandler.scaleY
import com.github.mikephil.charting.utils.ViewPortHandler.contentHeight
import com.github.mikephil.charting.interfaces.datasets.IScatterDataSet
import com.github.mikephil.charting.utils.ViewPortHandler
import com.github.mikephil.charting.renderer.scatter.IShapeRenderer
import com.github.mikephil.charting.utils.ColorTemplate
import com.github.mikephil.charting.components.AxisBase
import com.github.mikephil.charting.utils.MPPointD
import com.github.mikephil.charting.animation.ChartAnimator
import com.github.mikephil.charting.interfaces.dataprovider.ChartInterface
import com.github.mikephil.charting.interfaces.datasets.IDataSet
import com.github.mikephil.charting.formatter.IValueFormatter
import android.graphics.Paint.Align
import com.github.mikephil.charting.components.XAxis
import com.github.mikephil.charting.renderer.AxisRenderer
import com.github.mikephil.charting.utils.FSize
import com.github.mikephil.charting.utils.MPPointF
import com.github.mikephil.charting.components.XAxis.XAxisPosition
import com.github.mikephil.charting.components.LimitLine
import com.github.mikephil.charting.components.LimitLine.LimitLabelPosition
import com.github.mikephil.charting.components.YAxis
import com.github.mikephil.charting.components.YAxis.AxisDependency
import com.github.mikephil.charting.components.YAxis.YAxisLabelPosition
import com.github.mikephil.charting.components.Legend
import com.github.mikephil.charting.components.LegendEntry
import com.github.mikephil.charting.data.ChartData
import com.github.mikephil.charting.interfaces.datasets.IBarDataSet
import com.github.mikephil.charting.interfaces.datasets.IPieDataSet
import com.github.mikephil.charting.interfaces.datasets.ICandleDataSet
import com.github.mikephil.charting.components.Legend.LegendOrientation
import com.github.mikephil.charting.components.Legend.LegendHorizontalAlignment
import com.github.mikephil.charting.components.Legend.LegendVerticalAlignment
import com.github.mikephil.charting.components.Legend.LegendDirection
import com.github.mikephil.charting.components.Legend.LegendForm
import com.github.mikephil.charting.interfaces.dataprovider.BarDataProvider
import com.github.mikephil.charting.renderer.BarLineScatterCandleBubbleRenderer
import com.github.mikephil.charting.buffer.BarBuffer
import com.github.mikephil.charting.data.BarData
import com.github.mikephil.charting.data.BarEntry
import com.github.mikephil.charting.utils.Fill
import android.graphics.drawable.Drawable
import com.github.mikephil.charting.charts.PieChart
import com.github.mikephil.charting.renderer.DataRenderer
import android.text.TextPaint
import android.text.StaticLayout
import com.github.mikephil.charting.data.PieData
import com.github.mikephil.charting.data.PieDataSet.ValuePosition
import com.github.mikephil.charting.data.PieEntry
import com.github.mikephil.charting.data.PieDataSet
import android.os.Build
import com.github.mikephil.charting.interfaces.dataprovider.LineDataProvider
import com.github.mikephil.charting.renderer.LineRadarRenderer
import com.github.mikephil.charting.data.LineData
import com.github.mikephil.charting.interfaces.datasets.ILineDataSet
import com.github.mikephil.charting.data.LineDataSet
import com.github.mikephil.charting.renderer.BarLineScatterCandleBubbleRenderer.XBounds
import com.github.mikephil.charting.renderer.LineChartRenderer.DataSetImageCache
import com.github.mikephil.charting.renderer.LineScatterCandleRadarRenderer
import com.github.mikephil.charting.charts.RadarChart
import com.github.mikephil.charting.data.RadarData
import com.github.mikephil.charting.interfaces.datasets.IRadarDataSet
import com.github.mikephil.charting.data.RadarEntry
import com.github.mikephil.charting.interfaces.dataprovider.BubbleDataProvider
import com.github.mikephil.charting.data.BubbleData
import com.github.mikephil.charting.interfaces.datasets.IBubbleDataSet
import com.github.mikephil.charting.data.BubbleEntry
import com.github.mikephil.charting.interfaces.dataprovider.ScatterDataProvider
import com.github.mikephil.charting.data.ScatterData
import com.github.mikephil.charting.charts.CombinedChart
import com.github.mikephil.charting.charts.Chart
import com.github.mikephil.charting.charts.CombinedChart.DrawOrder
import com.github.mikephil.charting.renderer.BarChartRenderer
import com.github.mikephil.charting.renderer.BubbleChartRenderer
import com.github.mikephil.charting.renderer.LineChartRenderer
import com.github.mikephil.charting.renderer.CandleStickChartRenderer
import com.github.mikephil.charting.renderer.ScatterChartRenderer
import com.github.mikephil.charting.data.CombinedData
import com.github.mikephil.charting.renderer.XAxisRenderer
import com.github.mikephil.charting.renderer.YAxisRenderer
import com.github.mikephil.charting.interfaces.dataprovider.CandleDataProvider
import com.github.mikephil.charting.data.CandleData
import com.github.mikephil.charting.data.CandleEntry
import com.github.mikephil.charting.buffer.HorizontalBarBuffer
import com.github.mikephil.charting.interfaces.datasets.ILineScatterCandleRadarDataSet
import com.github.mikephil.charting.charts.BarChart
import com.github.mikephil.charting.interfaces.datasets.IBarLineScatterCandleBubbleDataSet
import com.github.mikephil.charting.interfaces.dataprovider.BarLineScatterCandleBubbleDataProvider
import com.github.mikephil.charting.data.DataSet
import java.util.*

class LegendRenderer(
    viewPortHandler: ViewPortHandler?,
    /**
     * the legend object this renderer renders
     */
    protected var mLegend: Legend
) : Renderer(viewPortHandler) {
    /**
     * Returns the Paint object used for drawing the Legend labels.
     *
     * @return
     */
    /**
     * paint for the legend labels
     */
    var labelPaint: Paint
        protected set
    /**
     * Returns the Paint object used for drawing the Legend forms.
     *
     * @return
     */
    /**
     * paint used for the legend forms
     */
    var formPaint: Paint
        protected set
    protected var computedEntries: MutableList<LegendEntry> = ArrayList(16)

    /**
     * Prepares the legend and calculates all needed forms, labels and colors.
     *
     * @param data
     */
    fun computeLegend(data: ChartData<*>) {
        if (!mLegend.isLegendCustom) {
            computedEntries.clear()

            // loop for building up the colors and labels used in the legend
            for (i in 0 until data.dataSetCount) {
                val dataSet = data.getDataSetByIndex(i) ?: continue
                val clrs = dataSet.colors
                val entryCount = dataSet.entryCount

                // if we have a barchart with stacked bars
                if (dataSet is IBarDataSet && dataSet.isStacked) {
                    val bds = dataSet
                    val sLabels = bds.stackLabels
                    val minEntries = Math.min(clrs.size, bds.stackSize)
                    for (j in 0 until minEntries) {
                        var label: String?
                        label = if (sLabels.size > 0) {
                            val labelIndex = j % minEntries
                            if (labelIndex < sLabels.size) sLabels[labelIndex] else null
                        } else {
                            null
                        }
                        computedEntries.add(
                            LegendEntry(
                                label,
                                dataSet.form,
                                dataSet.formSize,
                                dataSet.formLineWidth,
                                dataSet.formLineDashEffect,
                                clrs[j]
                            )
                        )
                    }
                    if (bds.label != null) {
                        // add the legend description label
                        computedEntries.add(
                            LegendEntry(
                                dataSet.label,
                                LegendForm.NONE, Float.NaN, Float.NaN,
                                null,
                                ColorTemplate.COLOR_NONE
                            )
                        )
                    }
                } else if (dataSet is IPieDataSet) {
                    val pds = dataSet
                    var j = 0
                    while (j < clrs.size && j < entryCount) {
                        computedEntries.add(
                            LegendEntry(
                                pds.getEntryForIndex(j).label,
                                dataSet.form,
                                dataSet.formSize,
                                dataSet.formLineWidth,
                                dataSet.formLineDashEffect,
                                clrs[j]
                            )
                        )
                        j++
                    }
                    if (pds.label != null) {
                        // add the legend description label
                        computedEntries.add(
                            LegendEntry(
                                dataSet.label,
                                LegendForm.NONE, Float.NaN, Float.NaN,
                                null,
                                ColorTemplate.COLOR_NONE
                            )
                        )
                    }
                } else if (dataSet is ICandleDataSet && dataSet.decreasingColor !=
                    ColorTemplate.COLOR_NONE
                ) {
                    val decreasingColor = dataSet.decreasingColor
                    val increasingColor = dataSet.increasingColor
                    computedEntries.add(
                        LegendEntry(
                            null,
                            dataSet.form,
                            dataSet.formSize,
                            dataSet.formLineWidth,
                            dataSet.formLineDashEffect,
                            decreasingColor
                        )
                    )
                    computedEntries.add(
                        LegendEntry(
                            dataSet.label,
                            dataSet.form,
                            dataSet.formSize,
                            dataSet.formLineWidth,
                            dataSet.formLineDashEffect,
                            increasingColor
                        )
                    )
                } else { // all others
                    var j = 0
                    while (j < clrs.size && j < entryCount) {
                        var label: String?

                        // if multiple colors are set for a DataSet, group them
                        label = if (j < clrs.size - 1 && j < entryCount - 1) {
                            null
                        } else { // add label to the last entry
                            data.getDataSetByIndex(i).label
                        }
                        computedEntries.add(
                            LegendEntry(
                                label,
                                dataSet.form,
                                dataSet.formSize,
                                dataSet.formLineWidth,
                                dataSet.formLineDashEffect,
                                clrs[j]
                            )
                        )
                        j++
                    }
                }
            }
            if (mLegend.extraEntries != null) {
                Collections.addAll(computedEntries, *mLegend.extraEntries)
            }
            mLegend.setEntries(computedEntries)
        }
        val tf = mLegend.typeface
        if (tf != null) labelPaint.typeface = tf
        labelPaint.textSize = mLegend.textSize
        labelPaint.color = mLegend.textColor

        // calculate all dimensions of the mLegend
        mLegend.calculateDimensions(labelPaint, mViewPortHandler)
    }

    protected var legendFontMetrics = Paint.FontMetrics()
    fun renderLegend(c: Canvas) {
        if (!mLegend.isEnabled) return
        val tf = mLegend.typeface
        if (tf != null) labelPaint.typeface = tf
        labelPaint.textSize = mLegend.textSize
        labelPaint.color = mLegend.textColor
        val labelLineHeight = getLineHeight(
            labelPaint, legendFontMetrics
        )
        val labelLineSpacing = (getLineSpacing(
            labelPaint, legendFontMetrics
        )
                + convertDpToPixel(mLegend.yEntrySpace))
        val formYOffset = labelLineHeight - calcTextHeight(
            labelPaint, "ABC"
        ) / 2f
        val entries = mLegend.entries
        val formToTextSpace = convertDpToPixel(mLegend.formToTextSpace)
        val xEntrySpace = convertDpToPixel(mLegend.xEntrySpace)
        val orientation = mLegend.orientation
        val horizontalAlignment = mLegend.horizontalAlignment
        val verticalAlignment = mLegend.verticalAlignment
        val direction = mLegend.direction
        val defaultFormSize = convertDpToPixel(mLegend.formSize)

        // space between the entries
        val stackSpace = convertDpToPixel(mLegend.stackSpace)
        val yoffset = mLegend.yOffset
        val xoffset = mLegend.xOffset
        var originPosX = 0f
        when (horizontalAlignment) {
            LegendHorizontalAlignment.LEFT -> {
                originPosX =
                    if (orientation == LegendOrientation.VERTICAL) xoffset else mViewPortHandler.contentLeft() + xoffset
                if (direction == LegendDirection.RIGHT_TO_LEFT) originPosX += mLegend.mNeededWidth
            }
            LegendHorizontalAlignment.RIGHT -> {
                originPosX =
                    if (orientation == LegendOrientation.VERTICAL) mViewPortHandler.chartWidth - xoffset else mViewPortHandler.contentRight() - xoffset
                if (direction == LegendDirection.LEFT_TO_RIGHT) originPosX -= mLegend.mNeededWidth
            }
            LegendHorizontalAlignment.CENTER -> {
                originPosX =
                    if (orientation == LegendOrientation.VERTICAL) mViewPortHandler.chartWidth / 2f else mViewPortHandler.contentLeft()
                +mViewPortHandler.contentWidth() / 2f
                originPosX += if (direction == LegendDirection.LEFT_TO_RIGHT) +xoffset else -xoffset

                // Horizontally layed out legends do the center offset on a line basis,
                // So here we offset the vertical ones only.
                if (orientation == LegendOrientation.VERTICAL) {
                    originPosX += (if (direction == LegendDirection.LEFT_TO_RIGHT) -mLegend.mNeededWidth / 2.0 + xoffset else mLegend.mNeededWidth / 2.0 - xoffset).toFloat()
                }
            }
        }
        when (orientation) {
            LegendOrientation.HORIZONTAL -> {
                val calculatedLineSizes = mLegend.calculatedLineSizes
                val calculatedLabelSizes = mLegend.calculatedLabelSizes
                val calculatedLabelBreakPoints = mLegend.calculatedLabelBreakPoints
                var posX = originPosX
                var posY = 0f
                posY = when (verticalAlignment) {
                    LegendVerticalAlignment.TOP -> yoffset
                    LegendVerticalAlignment.BOTTOM -> mViewPortHandler.chartHeight - yoffset - mLegend.mNeededHeight
                    LegendVerticalAlignment.CENTER -> (mViewPortHandler.chartHeight - mLegend.mNeededHeight) / 2f + yoffset
                }
                var lineIndex = 0
                var i = 0
                val count = entries.size
                while (i < count) {
                    val e = entries[i]
                    val drawingForm = e.form != LegendForm.NONE
                    val formSize =
                        if (java.lang.Float.isNaN(e.formSize)) defaultFormSize else convertDpToPixel(
                            e.formSize
                        )
                    if (i < calculatedLabelBreakPoints.size && calculatedLabelBreakPoints[i]) {
                        posX = originPosX
                        posY += labelLineHeight + labelLineSpacing
                    }
                    if (posX == originPosX && horizontalAlignment == LegendHorizontalAlignment.CENTER && lineIndex < calculatedLineSizes.size) {
                        posX += (if (direction == LegendDirection.RIGHT_TO_LEFT) calculatedLineSizes[lineIndex].width else -calculatedLineSizes[lineIndex].width) / 2f
                        lineIndex++
                    }
                    val isStacked = e.label == null // grouped forms have null labels
                    if (drawingForm) {
                        if (direction == LegendDirection.RIGHT_TO_LEFT) posX -= formSize
                        drawForm(c, posX, posY + formYOffset, e, mLegend)
                        if (direction == LegendDirection.LEFT_TO_RIGHT) posX += formSize
                    }
                    if (!isStacked) {
                        if (drawingForm) posX += if (direction == LegendDirection.RIGHT_TO_LEFT) -formToTextSpace else formToTextSpace
                        if (direction == LegendDirection.RIGHT_TO_LEFT) posX -= calculatedLabelSizes[i].width
                        drawLabel(c, posX, posY + labelLineHeight, e.label)
                        if (direction == LegendDirection.LEFT_TO_RIGHT) posX += calculatedLabelSizes[i].width
                        posX += if (direction == LegendDirection.RIGHT_TO_LEFT) -xEntrySpace else xEntrySpace
                    } else posX += if (direction == LegendDirection.RIGHT_TO_LEFT) -stackSpace else stackSpace
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
                            if (horizontalAlignment == LegendHorizontalAlignment.CENTER) 0f else mViewPortHandler.contentTop()
                        posY += yoffset
                    }
                    LegendVerticalAlignment.BOTTOM -> {
                        posY =
                            if (horizontalAlignment == LegendHorizontalAlignment.CENTER) mViewPortHandler.chartHeight else mViewPortHandler.contentBottom()
                        posY -= mLegend.mNeededHeight + yoffset
                    }
                    LegendVerticalAlignment.CENTER -> posY = (mViewPortHandler.chartHeight / 2f
                            - mLegend.mNeededHeight / 2f
                            + mLegend.yOffset)
                }
                var i = 0
                while (i < entries.size) {
                    val e = entries[i]
                    val drawingForm = e.form != LegendForm.NONE
                    val formSize =
                        if (java.lang.Float.isNaN(e.formSize)) defaultFormSize else convertDpToPixel(
                            e.formSize
                        )
                    var posX = originPosX
                    if (drawingForm) {
                        if (direction == LegendDirection.LEFT_TO_RIGHT) posX += stack else posX -= formSize - stack
                        drawForm(c, posX, posY + formYOffset, e, mLegend)
                        if (direction == LegendDirection.LEFT_TO_RIGHT) posX += formSize
                    }
                    if (e.label != null) {
                        if (drawingForm && !wasStacked) posX += if (direction == LegendDirection.LEFT_TO_RIGHT) formToTextSpace else -formToTextSpace else if (wasStacked) posX =
                            originPosX
                        if (direction == LegendDirection.RIGHT_TO_LEFT) posX -= calcTextWidth(
                            labelPaint, e.label
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
        legend: Legend
    ) {
        if (entry.formColor == ColorTemplate.COLOR_SKIP || entry.formColor == ColorTemplate.COLOR_NONE || entry.formColor == 0) return
        val restoreCount = c.save()
        var form = entry.form
        if (form == LegendForm.DEFAULT) form = legend.form
        formPaint.color = entry.formColor
        val formSize = convertDpToPixel(
            if (java.lang.Float.isNaN(entry.formSize)) legend.formSize else entry.formSize
        )
        val half = formSize / 2f
        when (form) {
            LegendForm.NONE -> {}
            LegendForm.EMPTY -> {}
            LegendForm.DEFAULT, LegendForm.CIRCLE -> {
                formPaint.style = Paint.Style.FILL
                c.drawCircle(x + half, y, half, formPaint)
            }
            LegendForm.SQUARE -> {
                formPaint.style = Paint.Style.FILL
                c.drawRect(x, y - half, x + formSize, y + half, formPaint)
            }
            LegendForm.LINE -> {
                val formLineWidth = convertDpToPixel(
                    if (java.lang.Float.isNaN(entry.formLineWidth)) legend.formLineWidth else entry.formLineWidth
                )
                val formLineDashEffect =
                    if (entry.formLineDashEffect == null) legend.formLineDashEffect else entry.formLineDashEffect
                formPaint.style = Paint.Style.STROKE
                formPaint.strokeWidth = formLineWidth
                formPaint.pathEffect = formLineDashEffect
                mLineFormPath.reset()
                mLineFormPath.moveTo(x, y)
                mLineFormPath.lineTo(x + formSize, y)
                c.drawPath(mLineFormPath, formPaint)
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
        c.drawText(label!!, x, y, labelPaint)
    }

    init {
        labelPaint = Paint(Paint.ANTI_ALIAS_FLAG)
        labelPaint.textSize = convertDpToPixel(9f)
        labelPaint.textAlign = Align.LEFT
        formPaint = Paint(Paint.ANTI_ALIAS_FLAG)
        formPaint.style = Paint.Style.FILL
    }
}