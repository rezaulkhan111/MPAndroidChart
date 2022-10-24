package com.github.mikephil.charting.components

import android.graphics.Color
import com.github.mikephil.charting.utils.Utils.convertDpToPixel
import com.github.mikephil.charting.utils.Utils.calcTextWidth
import com.github.mikephil.charting.utils.Utils.calcTextHeight
import com.github.mikephil.charting.utils.Utils.getLineHeight
import com.github.mikephil.charting.utils.Utils.getLineSpacing
import com.github.mikephil.charting.utils.ViewPortHandler.contentWidth
import com.github.mikephil.charting.utils.Utils.calcTextSize
import com.github.mikephil.charting.utils.FSize.Companion.getInstance
import com.github.mikephil.charting.formatter.IAxisValueFormatter.getFormattedValue
import com.github.mikephil.charting.formatter.DefaultAxisValueFormatter.decimalDigits
import com.github.mikephil.charting.components.AxisBase
import com.github.mikephil.charting.components.XAxis.XAxisPosition
import com.github.mikephil.charting.components.YAxis.YAxisLabelPosition
import com.github.mikephil.charting.components.YAxis.AxisDependency
import com.github.mikephil.charting.components.ComponentBase
import com.github.mikephil.charting.components.LegendEntry
import com.github.mikephil.charting.components.Legend.LegendHorizontalAlignment
import com.github.mikephil.charting.components.Legend.LegendVerticalAlignment
import com.github.mikephil.charting.components.Legend.LegendOrientation
import com.github.mikephil.charting.components.Legend.LegendDirection
import com.github.mikephil.charting.components.Legend.LegendForm
import android.graphics.DashPathEffect
import android.graphics.Paint
import com.github.mikephil.charting.utils.ColorTemplate
import com.github.mikephil.charting.utils.FSize
import com.github.mikephil.charting.utils.ViewPortHandler
import com.github.mikephil.charting.utils.MPPointF
import com.github.mikephil.charting.formatter.IAxisValueFormatter
import com.github.mikephil.charting.components.LimitLine
import com.github.mikephil.charting.formatter.DefaultAxisValueFormatter
import com.github.mikephil.charting.components.LimitLine.LimitLabelPosition
import android.widget.RelativeLayout
import com.github.mikephil.charting.components.IMarker
import com.github.mikephil.charting.charts.Chart
import android.view.LayoutInflater
import android.view.View.MeasureSpec
import android.graphics.Paint.Align
import android.graphics.drawable.Drawable
import android.os.Build
import android.graphics.Typeface

/**
 * The limit line is an additional feature for all Line-, Bar- and
 * ScatterCharts. It allows the displaying of an additional line in the chart
 * that marks a certain maximum / limit on the specified axis (x- or y-axis).
 *
 * @author Philipp Jahoda
 */
class LimitLine : ComponentBase {
    /**
     * Returns the limit that is set for this line.
     *
     * @return
     */
    /** limit / maximum (the y-value or xIndex)  */
    var limit = 0f
        private set

    /** the width of the limit line  */
    private var mLineWidth = 2f
    /**
     * Returns the color that is used for this LimitLine
     *
     * @return
     */
    /**
     * Sets the linecolor for this LimitLine. Make sure to use
     * getResources().getColor(...)
     *
     * @param color
     */
    /** the color of the limit line  */
    var lineColor = Color.rgb(237, 91, 91)
    /**
     * Returns the color of the value-text that is drawn next to the LimitLine.
     *
     * @return
     */
    /**
     * Sets the color of the value-text that is drawn next to the LimitLine.
     * Default: Paint.Style.FILL_AND_STROKE
     *
     * @param style
     */
    /** the style of the label text  */
    var textStyle = Paint.Style.FILL_AND_STROKE
    /**
     * Returns the label that is drawn next to the limit line.
     *
     * @return
     */
    /**
     * Sets the label that is drawn next to the limit line. Provide "" if no
     * label is required.
     *
     * @param label
     */
    /** label string that is drawn next to the limit line  */
    var label = ""
    /**
     * returns the DashPathEffect that is set for this LimitLine
     *
     * @return
     */
    /** the path effect of this LimitLine that makes dashed lines possible  */
    var dashPathEffect: DashPathEffect? = null
        private set
    /**
     * Returns the position of the LimitLine label (value).
     *
     * @return
     */
    /**
     * Sets the position of the LimitLine value label (either on the right or on
     * the left edge of the chart). Not supported for RadarChart.
     *
     * @param pos
     */
    /** indicates the position of the LimitLine label  */
    var labelPosition = LimitLabelPosition.RIGHT_TOP

    /** enum that indicates the position of the LimitLine label  */
    enum class LimitLabelPosition {
        LEFT_TOP, LEFT_BOTTOM, RIGHT_TOP, RIGHT_BOTTOM
    }

    /**
     * Constructor with limit.
     *
     * @param limit - the position (the value) on the y-axis (y-value) or x-axis
     * (xIndex) where this line should appear
     */
    constructor(limit: Float) {
        this.limit = limit
    }

    /**
     * Constructor with limit and label.
     *
     * @param limit - the position (the value) on the y-axis (y-value) or x-axis
     * (xIndex) where this line should appear
     * @param label - provide "" if no label is required
     */
    constructor(limit: Float, label: String) {
        this.limit = limit
        this.label = label
    }
    /**
     * returns the width of limit line
     *
     * @return
     */
    /**
     * set the line width of the chart (min = 0.2f, max = 12f); default 2f NOTE:
     * thinner line == better performance, thicker line == worse performance
     *
     * @param width
     */
    var lineWidth: Float
        get() = mLineWidth
        set(width) {
            var width = width
            if (width < 0.2f) width = 0.2f
            if (width > 12.0f) width = 12.0f
            mLineWidth = convertDpToPixel(width)
        }

    /**
     * Enables the line to be drawn in dashed mode, e.g. like this "- - - - - -"
     *
     * @param lineLength the length of the line pieces
     * @param spaceLength the length of space inbetween the pieces
     * @param phase offset, in degrees (normally, use 0)
     */
    fun enableDashedLine(lineLength: Float, spaceLength: Float, phase: Float) {
        dashPathEffect = DashPathEffect(
            floatArrayOf(
                lineLength, spaceLength
            ), phase
        )
    }

    /**
     * Disables the line to be drawn in dashed mode.
     */
    fun disableDashedLine() {
        dashPathEffect = null
    }

    /**
     * Returns true if the dashed-line effect is enabled, false if not. Default:
     * disabled
     *
     * @return
     */
    val isDashedLineEnabled: Boolean
        get() = if (dashPathEffect == null) false else true
}