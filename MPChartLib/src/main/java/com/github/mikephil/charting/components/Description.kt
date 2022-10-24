package com.github.mikephil.charting.components

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
 * Created by Philipp Jahoda on 17/09/16.
 */
class Description : ComponentBase() {
    /**
     * Returns the description text.
     *
     * @return
     */
    /**
     * Sets the text to be shown as the description.
     * Never set this to null as this will cause nullpointer exception when drawing with Android Canvas.
     *
     * @param text
     */
    /**
     * the text used in the description
     */
    var text = "Description Label"
    /**
     * Returns the customized position of the description, or null if none set.
     *
     * @return
     */
    /**
     * the custom position of the description text
     */
    var position: MPPointF? = null
        private set
    /**
     * Returns the text alignment of the description.
     *
     * @return
     */
    /**
     * Sets the text alignment of the description text. Default RIGHT.
     *
     * @param align
     */
    /**
     * the alignment of the description text
     */
    var textAlign = Align.RIGHT

    /**
     * Sets a custom position for the description text in pixels on the screen.
     *
     * @param x - xcoordinate
     * @param y - ycoordinate
     */
    fun setPosition(x: Float, y: Float) {
        if (position == null) {
            position = MPPointF.getInstance(x, y)
        } else {
            position!!.x = x
            position!!.y = y
        }
    }

    init {

        // default size
        mTextSize = convertDpToPixel(8f)
    }
}