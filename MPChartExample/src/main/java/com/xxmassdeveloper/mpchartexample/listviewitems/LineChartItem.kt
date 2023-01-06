package com.xxmassdeveloper.mpchartexample.listviewitems

import android.annotation.SuppressLint
import android.content.Context
import android.graphics.Typeface
import android.view.LayoutInflater
import android.view.View
import com.github.mikephil.charting.charts.LineChart
import com.github.mikephil.charting.components.XAxis.XAxisPosition
import com.github.mikephil.charting.data.ChartData
import com.github.mikephil.charting.data.LineData
import com.xxmassdeveloper.mpchartexample.R

class LineChartItem(cd: ChartData<*>, c: Context) : ChartItem(cd) {
    private val mTf: Typeface
    override val itemType: Int
        get() = ChartItem.Companion.TYPE_LINECHART

    @SuppressLint("InflateParams")
    override fun getView(position: Int, convertView: View?, c: Context?): View? {
        var convertView = convertView
        val holder: ViewHolder
        if (convertView == null) {
            holder = ViewHolder()
            convertView = LayoutInflater.from(c).inflate(
                R.layout.list_item_linechart, null
            )
            holder.chart = convertView.findViewById(R.id.chart)
            convertView.tag = holder
        } else {
            holder = convertView.tag as ViewHolder
        }

        // apply styling
        // holder.chart.setValueTypeface(mTf);
        holder.chart!!.getDescription()!!.setEnabled(false)
        holder.chart!!.setDrawGridBackground(false)

        val xAxis = holder.chart!!.getXAxis()
        xAxis!!.setPosition(XAxisPosition.BOTTOM)
        xAxis.setTypeface(mTf)
        xAxis.setDrawGridLines(false)
        xAxis.setDrawAxisLine(true)

        val leftAxis = holder.chart!!.getAxisLeft()
        leftAxis!!.setTypeface(mTf)
        leftAxis.setLabelCount(5, false)
        leftAxis.setAxisMinimum(0f) // this replaces setStartAtZero(true)

        val rightAxis = holder.chart!!.getAxisRight()
        rightAxis!!.setTypeface(mTf)
        rightAxis.setLabelCount(5, false)
        rightAxis.setDrawGridLines(false)
        rightAxis.setAxisMinimum(0f) // this replaces setStartAtZero(true)

        // set data
        holder.chart!!.setData(mChartData as LineData)

        // do not forget to refresh the chart
        // holder.chart.invalidate();
        holder.chart!!.animateX(750)
        return convertView
    }

    private class ViewHolder {
        var chart: LineChart? = null
    }

    init {
        mTf = Typeface.createFromAsset(c.assets, "OpenSans-Regular.ttf")
    }
}