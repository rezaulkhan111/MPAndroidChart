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
import android.content.Context
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
import com.github.mikephil.charting.data.Entry
import com.github.mikephil.charting.highlight.Highlight
import com.github.mikephil.charting.interfaces.datasets.IScatterDataSet

/**
 * Custom implementation of the MarkerView.
 *
 * @author Philipp Jahoda
 */
@SuppressLint("ViewConstructor")
class StackedBarsMarkerView(context: Context?, layoutResource: Int) :
    MarkerView(context, layoutResource) {
    private val tvContent: TextView

    // runs every time the MarkerView is redrawn, can be used to update the
    // content (user-interface)
    override fun refreshContent(e: Entry?, highlight: Highlight?) {
        if (e is BarEntry) {
            val be = e
            if (be.yVals != null) {

                // draw the stack value
                tvContent.text = formatNumber(
                    be.yVals!![highlight!!.stackIndex], 0, true
                )
            } else {
                tvContent.text = formatNumber(be.y, 0, true)
            }
        } else {
            tvContent.text = formatNumber(e!!.y, 0, true)
        }
        super.refreshContent(e, highlight)
    }

    override var offset: MPPointF
        get() = MPPointF((-(width / 2)).toFloat(), (-height).toFloat())
        set(offset) {
            super.offset = offset
        }

    init {
        tvContent = findViewById(R.id.tvContent)
    }
}