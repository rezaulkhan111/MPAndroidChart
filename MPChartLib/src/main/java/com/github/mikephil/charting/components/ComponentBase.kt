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
 * This class encapsulates everything both Axis, Legend and LimitLines have in common.
 *
 * @author Philipp Jahoda
 */
abstract class ComponentBase {
    /**
     * Returns true if this comonent is enabled (should be drawn), false if not.
     *
     * @return
     */
    /**
     * Set this to true if this component should be enabled (should be drawn),
     * false if not. If disabled, nothing of this component will be drawn.
     * Default: true
     *
     * @param enabled
     */
    /**
     * flag that indicates if this axis / legend is enabled or not
     */
    var isEnabled = true

    /**
     * the offset in pixels this component has on the x-axis
     */
    protected var mXOffset = 5f

    /**
     * the offset in pixels this component has on the Y-axis
     */
    protected var mYOffset = 5f
    /**
     * returns the Typeface used for the labels, returns null if none is set
     *
     * @return
     */
    /**
     * sets a specific Typeface for the labels
     *
     * @param tf
     */
    /**
     * the typeface used for the labels
     */
    var typeface: Typeface? = null

    /**
     * the text size of the labels
     */
    protected var mTextSize = convertDpToPixel(10f)
    /**
     * Returns the text color that is set for the labels.
     *
     * @return
     */
    /**
     * Sets the text color to use for the labels. Make sure to use
     * getResources().getColor(...) when using a color from the resources.
     *
     * @param color
     */
    /**
     * the text color to use for the labels
     */
    var textColor = Color.BLACK
    /**
     * Returns the used offset on the x-axis for drawing the axis or legend
     * labels. This offset is applied before and after the label.
     *
     * @return
     */
    /**
     * Sets the used x-axis offset for the labels on this axis.
     *
     * @param xOffset
     */
    var xOffset: Float
        get() = mXOffset
        set(xOffset) {
            mXOffset = convertDpToPixel(xOffset)
        }
    /**
     * Returns the used offset on the x-axis for drawing the axis labels. This
     * offset is applied before and after the label.
     *
     * @return
     */
    /**
     * Sets the used y-axis offset for the labels on this axis. For the legend,
     * higher offset means the legend as a whole will be placed further away
     * from the top.
     *
     * @param yOffset
     */
    var yOffset: Float
        get() = mYOffset
        set(yOffset) {
            mYOffset = convertDpToPixel(yOffset)
        }
    /**
     * returns the text size that is currently set for the labels, in pixels
     *
     * @return
     */
    /**
     * sets the size of the label text in density pixels min = 6f, max = 24f, default
     * 10f
     *
     * @param size the text size, in DP
     */
    var textSize: Float
        get() = mTextSize
        set(size) {
            var size = size
            if (size > 24f) size = 24f
            if (size < 6f) size = 6f
            mTextSize = convertDpToPixel(size)
        }
}