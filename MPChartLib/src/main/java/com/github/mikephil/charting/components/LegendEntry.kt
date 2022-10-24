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

class LegendEntry {
    constructor() {}

    /**
     *
     * @param label The legend entry text. A `null` label will start a group.
     * @param form The form to draw for this entry.
     * @param formSize Set to NaN to use the legend's default.
     * @param formLineWidth Set to NaN to use the legend's default.
     * @param formLineDashEffect Set to nil to use the legend's default.
     * @param formColor The color for drawing the form.
     */
    constructor(
        label: String?,
        form: LegendForm,
        formSize: Float,
        formLineWidth: Float,
        formLineDashEffect: DashPathEffect?,
        formColor: Int
    ) {
        this.label = label
        this.form = form
        this.formSize = formSize
        this.formLineWidth = formLineWidth
        this.formLineDashEffect = formLineDashEffect
        this.formColor = formColor
    }

    /**
     * The legend entry text.
     * A `null` label will start a group.
     */
    var label: String? = null

    /**
     * The form to draw for this entry.
     *
     * `NONE` will avoid drawing a form, and any related space.
     * `EMPTY` will avoid drawing a form, but keep its space.
     * `DEFAULT` will use the Legend's default.
     */
    var form = LegendForm.DEFAULT

    /**
     * Form size will be considered except for when .None is used
     *
     * Set as NaN to use the legend's default
     */
    var formSize = Float.NaN

    /**
     * Line width used for shapes that consist of lines.
     *
     * Set as NaN to use the legend's default
     */
    var formLineWidth = Float.NaN

    /**
     * Line dash path effect used for shapes that consist of lines.
     *
     * Set to null to use the legend's default
     */
    var formLineDashEffect: DashPathEffect? = null

    /**
     * The color for drawing the form
     */
    var formColor = ColorTemplate.COLOR_NONE
}