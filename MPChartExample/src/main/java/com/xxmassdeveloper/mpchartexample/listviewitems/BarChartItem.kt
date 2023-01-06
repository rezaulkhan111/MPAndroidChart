package com.xxmassdeveloper.mpchartexample.listviewitems

import android.annotation.SuppressLint
import android.content.Context
import android.graphics.Typeface
import android.view.LayoutInflater
import android.view.View
import com.github.mikephil.charting.charts.BarChart
import com.github.mikephil.charting.components.XAxis.XAxisPosition
import com.github.mikephil.charting.data.BarData
import com.github.mikephil.charting.data.ChartData
import com.xxmassdeveloper.mpchartexample.R

class BarChartItem(cd: ChartData<*>, c: Context) : ChartItem(cd) {
    private val mTf: Typeface
    override val itemType: Int
        get() = ChartItem.Companion.TYPE_BARCHART

    @SuppressLint("InflateParams")
    override fun getView(position: Int, convertView: View?, c: Context?): View? {

        var mConveView = convertView
        val holder: ViewHolder
        if (mConveView == null) {
            holder = ViewHolder()
            mConveView = LayoutInflater.from(c).inflate(
                R.layout.list_item_barchart, null
            )
            holder.chart = mConveView.findViewById(R.id.chart)
            mConveView.tag = holder
        } else {
            holder = mConveView.tag as ViewHolder
        }

        // apply styling
        holder.chart!!.getDescription()!!.setEnabled(false)
        holder.chart!!.setDrawGridBackground(false)
        holder.chart!!.setDrawBarShadow(false)

        val xAxis = holder.chart!!.getXAxis()
        xAxis!!.setPosition(XAxisPosition.BOTTOM)
        xAxis.setTypeface(mTf)
        xAxis.setDrawGridLines(false)
        xAxis.setDrawAxisLine(true)

        val leftAxis = holder.chart!!.getAxisLeft()
        leftAxis!!.setTypeface(mTf)
        leftAxis.setLabelCount(5, false)
        leftAxis.setSpaceTop(20f)
        leftAxis.setAxisMinimum(0f) // this replaces setStartAtZero(true)

        val rightAxis = holder.chart!!.getAxisRight()
        rightAxis!!.setTypeface(mTf)
        rightAxis.setLabelCount(5, false)
        rightAxis.setSpaceTop(20f)
        rightAxis.setAxisMinimum(0f) // this replaces setStartAtZero(true)
        mChartData.setValueTypeface(mTf)
        // set data
        holder.chart!!.setData(mChartData as BarData)
        holder.chart!!.setFitBars(true)
        // do not forget to refresh the chart
//        holder.chart.invalidate();
        holder.chart!!.animateY(700)
        return mConveView
    }

    private class ViewHolder {
        var chart: BarChart? = null
    }

    init {
        mTf = Typeface.createFromAsset(c.assets, "OpenSans-Regular.ttf")
    }
}