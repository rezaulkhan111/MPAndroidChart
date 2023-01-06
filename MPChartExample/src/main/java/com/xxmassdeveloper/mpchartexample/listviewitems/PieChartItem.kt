package com.xxmassdeveloper.mpchartexample.listviewitems

import android.annotation.SuppressLint
import android.content.Context
import android.graphics.Color
import android.graphics.Typeface
import android.text.SpannableString
import android.text.style.ForegroundColorSpan
import android.text.style.RelativeSizeSpan
import android.view.LayoutInflater
import android.view.View
import com.github.mikephil.charting.charts.PieChart
import com.github.mikephil.charting.components.Legend
import com.github.mikephil.charting.data.ChartData
import com.github.mikephil.charting.data.PieData
import com.github.mikephil.charting.formatter.PercentFormatter
import com.github.mikephil.charting.utils.ColorTemplate
import com.github.mikephil.charting.utils.ColorTemplate.getHoloBlue
import com.xxmassdeveloper.mpchartexample.R

class PieChartItem(cd: ChartData<*>, c: Context) : ChartItem(cd) {
    private val mTf: Typeface
    private val mCenterText: SpannableString
    override val itemType: Int
        get() = ChartItem.Companion.TYPE_PIECHART

    @SuppressLint("InflateParams")
    override fun getView(position: Int, convertView: View?, c: Context?): View? {
        var mConveView = convertView
        val holder: ViewHolder
        if (mConveView == null) {
            holder = ViewHolder()
            mConveView = LayoutInflater.from(c).inflate(
                R.layout.list_item_piechart, null
            )
            holder.chart = mConveView.findViewById(R.id.chart)
            mConveView.tag = holder
        } else {
            holder = mConveView.tag as ViewHolder
        }

        // apply styling

        // apply styling
        holder.chart!!.getDescription()!!.setEnabled(false)
        holder.chart!!.setHoleRadius(52f)
        holder.chart!!.setTransparentCircleRadius(57f)
        holder.chart!!.setCenterText(mCenterText)
        holder.chart!!.setCenterTextTypeface(mTf)
        holder.chart!!.setCenterTextSize(9f)
        holder.chart!!.setUsePercentValues(true)
        holder.chart!!.setExtraOffsets(5f, 10f, 50f, 10f)

        mChartData.setValueFormatter(PercentFormatter())
        mChartData.setValueTypeface(mTf)
        mChartData.setValueTextSize(11f)
        mChartData.setValueTextColor(Color.WHITE)
        // set data
        // set data
        holder.chart!!.setData(mChartData as PieData)

        val l = holder.chart!!.getLegend()
        l!!.setVerticalAlignment(Legend.LegendVerticalAlignment.TOP)
        l.setHorizontalAlignment(Legend.LegendHorizontalAlignment.RIGHT)
        l.setOrientation(Legend.LegendOrientation.VERTICAL)
        l.setDrawInside(false)
        l.setYEntrySpace(0f)
        l.setYOffset(0f)

        // do not forget to refresh the chart
        // holder.chart.invalidate();
        holder.chart!!.animateY(900)
        return mConveView
    }

    private fun generateCenterText(): SpannableString {
        val s = SpannableString("MPAndroidChart\ncreated by\nPhilipp Jahoda")
        s.setSpan(RelativeSizeSpan(1.6f), 0, 14, 0)
        s.setSpan(ForegroundColorSpan(ColorTemplate.VORDIPLOM_COLORS[0]), 0, 14, 0)
        s.setSpan(RelativeSizeSpan(.9f), 14, 25, 0)
        s.setSpan(ForegroundColorSpan(Color.GRAY), 14, 25, 0)
        s.setSpan(RelativeSizeSpan(1.4f), 25, s.length, 0)
        s.setSpan(ForegroundColorSpan(getHoloBlue()), 25, s.length, 0)
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