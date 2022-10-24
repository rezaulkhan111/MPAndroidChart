package com.xxmassdeveloper.mpchartexample.listviewitems

import com.github.mikephil.charting.charts.Chart.description
import com.github.mikephil.charting.components.ComponentBase.isEnabled
import com.github.mikephil.charting.charts.BarLineChartBase.setDrawGridBackground
import com.github.mikephil.charting.charts.BarChart.setDrawBarShadow
import com.github.mikephil.charting.charts.Chart.xAxis
import com.github.mikephil.charting.components.XAxis.position
import com.github.mikephil.charting.components.ComponentBase.typeface
import com.github.mikephil.charting.components.AxisBase.setDrawGridLines
import com.github.mikephil.charting.components.AxisBase.setDrawAxisLine
import com.github.mikephil.charting.charts.BarLineChartBase.axisLeft
import com.github.mikephil.charting.components.AxisBase.setLabelCount
import com.github.mikephil.charting.components.YAxis.spaceTop
import com.github.mikephil.charting.components.AxisBase.axisMinimum
import com.github.mikephil.charting.charts.BarLineChartBase.axisRight
import com.github.mikephil.charting.data.ChartData.setValueTypeface
import com.github.mikephil.charting.charts.Chart.data
import com.github.mikephil.charting.charts.BarChart.setFitBars
import com.github.mikephil.charting.charts.Chart.animateY
import com.github.mikephil.charting.charts.PieChart.holeRadius
import com.github.mikephil.charting.charts.PieChart.transparentCircleRadius
import com.github.mikephil.charting.charts.PieChart.centerText
import com.github.mikephil.charting.charts.PieChart.setCenterTextTypeface
import com.github.mikephil.charting.charts.PieChart.setCenterTextSize
import com.github.mikephil.charting.charts.PieChart.setUsePercentValues
import com.github.mikephil.charting.charts.Chart.setExtraOffsets
import com.github.mikephil.charting.data.ChartData.setValueFormatter
import com.github.mikephil.charting.data.ChartData.setValueTextSize
import com.github.mikephil.charting.data.ChartData.setValueTextColor
import com.github.mikephil.charting.charts.Chart.legend
import com.github.mikephil.charting.components.Legend.verticalAlignment
import com.github.mikephil.charting.components.Legend.horizontalAlignment
import com.github.mikephil.charting.components.Legend.orientation
import com.github.mikephil.charting.components.Legend.setDrawInside
import com.github.mikephil.charting.components.Legend.yEntrySpace
import com.github.mikephil.charting.components.ComponentBase.yOffset
import com.github.mikephil.charting.utils.ColorTemplate.holoBlue
import com.github.mikephil.charting.charts.Chart.animateX
import com.github.mikephil.charting.data.ChartData
import com.xxmassdeveloper.mpchartexample.listviewitems.ChartItem
import android.graphics.Typeface
import android.annotation.SuppressLint
import android.content.Context
import android.graphics.Color
import android.view.LayoutInflater
import com.xxmassdeveloper.mpchartexample.R
import com.github.mikephil.charting.components.XAxis
import com.github.mikephil.charting.components.XAxis.XAxisPosition
import com.github.mikephil.charting.components.YAxis
import com.github.mikephil.charting.data.BarData
import com.github.mikephil.charting.charts.BarChart
import android.text.SpannableString
import com.github.mikephil.charting.formatter.PercentFormatter
import com.github.mikephil.charting.data.PieData
import com.github.mikephil.charting.components.Legend
import android.text.style.RelativeSizeSpan
import android.text.style.ForegroundColorSpan
import android.view.View
import com.github.mikephil.charting.utils.ColorTemplate
import com.github.mikephil.charting.charts.PieChart
import com.github.mikephil.charting.data.LineData
import com.github.mikephil.charting.charts.LineChart

class PieChartItem(cd: ChartData<*>, c: Context) : ChartItem(cd) {
    private val mTf: Typeface
    private val mCenterText: SpannableString
    override val itemType: Int
        get() = ChartItem.Companion.TYPE_PIECHART

    @SuppressLint("InflateParams")
    override fun getView(position: Int, convertView: View?, c: Context?): View? {
        var convertView = convertView
        val holder: ViewHolder
        if (convertView == null) {
            holder = ViewHolder()
            convertView = LayoutInflater.from(c).inflate(
                R.layout.list_item_piechart, null
            )
            holder.chart = convertView.findViewById(R.id.chart)
            convertView.tag = holder
        } else {
            holder = convertView.tag as ViewHolder
        }

        // apply styling
        holder.chart!!.description!!.isEnabled = false
        holder.chart!!.holeRadius = 52f
        holder.chart!!.transparentCircleRadius = 57f
        holder.chart!!.centerText = mCenterText
        holder.chart!!.setCenterTextTypeface(mTf)
        holder.chart!!.setCenterTextSize(9f)
        holder.chart!!.setUsePercentValues(true)
        holder.chart!!.setExtraOffsets(5f, 10f, 50f, 10f)
        mChartData.setValueFormatter(PercentFormatter())
        mChartData.setValueTypeface(mTf)
        mChartData.setValueTextSize(11f)
        mChartData.setValueTextColor(Color.WHITE)
        // set data
        holder.chart!!.data = mChartData as PieData
        val l: Legend? = holder.chart!!.legend
        l!!.verticalAlignment = Legend.LegendVerticalAlignment.TOP
        l.horizontalAlignment = Legend.LegendHorizontalAlignment.RIGHT
        l.orientation = Legend.LegendOrientation.VERTICAL
        l.setDrawInside(false)
        l.yEntrySpace = 0f
        l.yOffset = 0f

        // do not forget to refresh the chart
        // holder.chart.invalidate();
        holder.chart!!.animateY(900)
        return convertView
    }

    private fun generateCenterText(): SpannableString {
        val s = SpannableString("MPAndroidChart\ncreated by\nPhilipp Jahoda")
        s.setSpan(RelativeSizeSpan(1.6f), 0, 14, 0)
        s.setSpan(ForegroundColorSpan(ColorTemplate.VORDIPLOM_COLORS[0]), 0, 14, 0)
        s.setSpan(RelativeSizeSpan(.9f), 14, 25, 0)
        s.setSpan(ForegroundColorSpan(Color.GRAY), 14, 25, 0)
        s.setSpan(RelativeSizeSpan(1.4f), 25, s.length, 0)
        s.setSpan(ForegroundColorSpan(holoBlue), 25, s.length, 0)
        return s
    }

    private class ViewHolder {
        var chart: PieChart? = null
    }

    init {
        mTf = Typeface.createFromAsset(c.assets, "OpenSans-Regular.ttf")
        mCenterText = generateCenterText()
    }
}