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
import java.text.DecimalFormat

/**
 * Created by Philipp Jahoda on 14/09/15.
 *
 */
@Deprecated("The {@link MyAxisValueFormatter} does exactly the same thing and is more functional.")
class MyCustomXAxisValueFormatter(private val mViewPortHandler: ViewPortHandler) :
    IAxisValueFormatter {
    private val mFormat: DecimalFormat
    override fun getFormattedValue(value: Float, axis: AxisBase?): String {

        //Log.i("TRANS", "x: " + viewPortHandler.getTransX() + ", y: " + viewPortHandler.getTransY());

        // e.g. adjust the x-axis values depending on scale / zoom level
        val xScale = mViewPortHandler.scaleX
        return if (xScale > 5) "4" else if (xScale > 3) "3" else if (xScale > 1) "2" else mFormat.format(
            value.toDouble()
        )
    }

    init {
        // maybe do something here or provide parameters in constructor
        mFormat = DecimalFormat("###,###,###,##0.0")
    }
}