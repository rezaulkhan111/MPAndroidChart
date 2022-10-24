package com.xxmassdeveloper.mpchartexample.custom

import com.github.mikephil.charting.utils.Utils.formatNumber
import com.github.mikephil.charting.data.CandleEntry.high
import com.github.mikephil.charting.data.BaseEntry.y
import com.github.mikephil.charting.components.MarkerView.refreshContent
import com.github.mikephil.charting.formatter.IAxisValueFormatter.getFormattedValue
import com.github.mikephil.charting.data.Entry.x
import com.github.mikephil.charting.charts.BarLineChartBase.visibleXRange
import com.github.mikephil.charting.data.BarEntry.yVals
import com.github.mikephil.charting.highlight.Highlight.stackIndex
import com.github.mikephil.charting.data.BarEntry.y
import com.github.mikephil.charting.interfaces.datasets.IScatterDataSet.scatterShapeSize
import com.github.mikephil.charting.utils.ViewPortHandler.scaleX
import android.annotation.SuppressLint
import com.github.mikephil.charting.components.MarkerView
import android.widget.TextView
import com.github.mikephil.charting.data.CandleEntry
import com.github.mikephil.charting.utils.MPPointF
import com.xxmassdeveloper.mpchartexample.R
import com.github.mikephil.charting.formatter.IAxisValueFormatter
import com.github.mikephil.charting.formatter.IFillFormatter
import com.github.mikephil.charting.interfaces.datasets.ILineDataSet
import com.github.mikephil.charting.interfaces.dataprovider.LineDataProvider
import android.graphics.Typeface
import com.github.mikephil.charting.formatter.IValueFormatter
import com.github.mikephil.charting.utils.ViewPortHandler
import com.github.mikephil.charting.components.AxisBase
import com.github.mikephil.charting.charts.BarLineChartBase
import com.github.mikephil.charting.data.BarEntry
import com.github.mikephil.charting.interfaces.datasets.IScatterDataSet

/**
 * Created by Philipp Jahoda on 14/09/15.
 */
class YearXAxisFormatter : IAxisValueFormatter {
    private val mMonths = arrayOf(
        "Jan", "Feb", "Mar", "Apr", "May", "Jun", "Jul", "Aug", "Sep", "Okt", "Nov", "Dec"
    )

    override fun getFormattedValue(value: Float, axis: AxisBase?): String {
        val percent = value / axis!!.mAxisRange
        return mMonths[(mMonths.size * percent).toInt()]
    }
}