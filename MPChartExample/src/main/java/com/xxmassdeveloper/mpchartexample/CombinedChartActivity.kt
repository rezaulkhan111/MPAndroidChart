package com.xxmassdeveloper.mpchartexample

import android.content.Intent
import android.graphics.Color
import android.net.Uri
import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import android.view.WindowManager
import com.github.mikephil.charting.charts.CombinedChart
import com.github.mikephil.charting.charts.CombinedChart.DrawOrder
import com.github.mikephil.charting.components.AxisBase
import com.github.mikephil.charting.components.Legend
import com.github.mikephil.charting.components.XAxis.XAxisPosition
import com.github.mikephil.charting.components.YAxis.AxisDependency
import com.github.mikephil.charting.data.*
import com.github.mikephil.charting.formatter.IAxisValueFormatter
import com.github.mikephil.charting.utils.ColorTemplate
import com.xxmassdeveloper.mpchartexample.notimportant.DemoBase

class CombinedChartActivity : DemoBase() {

    private lateinit var chart: CombinedChart
    private val count = 12

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        window.setFlags(
            WindowManager.LayoutParams.FLAG_FULLSCREEN,
            WindowManager.LayoutParams.FLAG_FULLSCREEN
        )
        setContentView(R.layout.activity_combined)
        title = "CombinedChartActivity"
        chart = findViewById(R.id.chart1)

        chart.getDescription()!!.setEnabled(false)
        chart.setBackgroundColor(Color.WHITE)
        chart.setDrawGridBackground(false)
        chart.setDrawBarShadow(false)
        chart.setHighlightFullBarEnabled(false)

        // draw bars behind lines

        // draw bars behind lines
        chart.setDrawOrder(
            arrayOf(
                DrawOrder.BAR, DrawOrder.BUBBLE, DrawOrder.CANDLE, DrawOrder.LINE, DrawOrder.SCATTER
            )
        )

        val l = chart.getLegend()
        l!!.setWordWrapEnabled(true)
        l.setVerticalAlignment(Legend.LegendVerticalAlignment.BOTTOM)
        l.setHorizontalAlignment(Legend.LegendHorizontalAlignment.CENTER)
        l.setOrientation(Legend.LegendOrientation.HORIZONTAL)
        l.setDrawInside(false)

        val rightAxis = chart.getAxisRight()
        rightAxis!!.setDrawGridLines(false)
        rightAxis.setAxisMinimum(0f) // this replaces setStartAtZero(true)


        val leftAxis = chart.getAxisLeft()
        leftAxis!!.setDrawGridLines(false)
        leftAxis!!.setAxisMinimum(0f) // this replaces setStartAtZero(true)


        val xAxis = chart.getXAxis()
        xAxis!!.setPosition(XAxisPosition.BOTH_SIDED)
        xAxis!!.setAxisMinimum(0f)
        xAxis!!.setGranularity(1f)
        xAxis!!.setValueFormatter(object : IAxisValueFormatter {
            override fun getFormattedValue(value: Float, axis: AxisBase?): String {
                return months[value.toInt() % months.size]
            }
        })

        val data = CombinedData()

        data.setData(generateLineData())
        data.setData(generateBarData())
        data.setData(generateBubbleData())
        data.setData(generateScatterData())
        data.setData(generateCandleData())
        data.setValueTypeface(tfLight)
        xAxis.setAxisMaximum(data.getXMax() + 0.25f)

        chart.setData(data)
        chart.invalidate()
    }

    private fun generateLineData(): LineData {
        val d = LineData()
        val entries = mutableListOf<Entry>()
        for (index in 0 until count) entries.add(Entry(index + 0.5f, getRandom(15f, 5f)))

        val set = LineDataSet(entries, "Line DataSet")
        set.setColor(Color.rgb(240, 238, 70))
        set.setLineWidth(2.5f)
        set.setCircleColor(Color.rgb(240, 238, 70))
        set.setCircleRadius(5f)
        set.setFillColor(Color.rgb(240, 238, 70))
        set.setMode(LineDataSet.Mode.CUBIC_BEZIER)
        set.setDrawValues(true)
        set.setValueTextSize(10f)
        set.setValueTextColor(Color.rgb(240, 238, 70))

        set.setAxisDependency(AxisDependency.LEFT)
        d.addDataSet(set)
        return d
    }

    private fun generateBarData(): BarData {
        val entries1 = mutableListOf<BarEntry>()
        val entries2 = mutableListOf<BarEntry>()
        for (index in 0 until count) {
            entries1.add(BarEntry(0f, getRandom(25f, 25f)))
            // stacked
            entries2.add(BarEntry(0f, floatArrayOf(getRandom(13f, 12f), getRandom(13f, 12f))))
        }

        val set1 = BarDataSet(entries1, "Bar 1")
        set1.setColor(Color.rgb(60, 220, 78))
        set1.setValueTextColor(Color.rgb(60, 220, 78))
        set1.setValueTextSize(10f)
        set1.setAxisDependency(AxisDependency.LEFT)

        val set2 = BarDataSet(entries2, "")
        set2.setStackLabels(arrayOf("Stack 1", "Stack 2"))
//        set2.setColors(Color.rgb(61, 165, 255), Color.rgb(23, 197, 255))
        set2.setValueTextColor(Color.rgb(61, 165, 255))
        set2.setValueTextSize(10f)
        set2.setAxisDependency(AxisDependency.LEFT)
        val groupSpace = 0.06f
        val barSpace = 0.02f // x2 dataset
        val barWidth = 0.45f // x2 dataset

        // (0.45 + 0.02) * 2 + 0.06 = 1.00 -> interval per "group"
        val d = BarData(set1, set2)
        d.setBarWidth(barWidth)

        // make this BarData object grouped
        d.groupBars(0f, groupSpace, barSpace) // start at x = 0
        return d
    }

    private fun generateScatterData(): ScatterData {
        val d = ScatterData()
        val entries = mutableListOf<Entry>()
        var index = 0f
        while (index < count) {
            entries.add(Entry(index + 0.25f, getRandom(10f, 55f)))
            index += 0.5f
        }

        val set = ScatterDataSet(entries, "Scatter DataSet")
        set.setColors(ColorTemplate.MATERIAL_COLORS)
        set.setScatterShapeSize(7.5f)
        set.setDrawValues(false)
        set.setValueTextSize(10f)
        d.addDataSet(set)
        return d
    }

    private fun generateCandleData(): CandleData {
        val d = CandleData()
        val entries = mutableListOf<CandleEntry>()
        var index = 0
        while (index < count) {
            entries.add(CandleEntry(index + 1f, 90f, 70f, 85f, 75f))
            index += 2
        }
        val set = CandleDataSet(entries, "Candle DataSet")
        set.setDecreasingColor(Color.rgb(142, 150, 175))
        set.setShadowColor(Color.DKGRAY)
        set.setBarSpace(0.3f)
        set.setValueTextSize(10f)
        set.setDrawValues(false)
        d.addDataSet(set)
        return d
    }

    private fun generateBubbleData(): BubbleData {
        val bd = BubbleData()
        val entries = mutableListOf<BubbleEntry>()
        for (index in 0 until count) {
            val y = getRandom(10f, 105f)
            val size = getRandom(100f, 105f)
            entries.add(BubbleEntry(index + 0.5f, y, size))
        }

        val set = BubbleDataSet(entries, "Bubble DataSet")
        set.setColors(ColorTemplate.VORDIPLOM_COLORS)
        set.setValueTextSize(10f)
        set.setValueTextColor(Color.WHITE)
        set.setHighlightCircleWidth(1.5f)
        set.setDrawValues(true)
        bd.addDataSet(set)
        return bd
    }

    override fun onCreateOptionsMenu(menu: Menu): Boolean {
        menuInflater.inflate(R.menu.combined, menu)
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when (item.itemId) {
            R.id.viewGithub -> {
                val i = Intent(Intent.ACTION_VIEW)
                i.data =
                    Uri.parse("https://github.com/PhilJay/MPAndroidChart/blob/master/MPChartExample/src/com/xxmassdeveloper/mpchartexample/CombinedChartActivity.java")
                startActivity(i)
            }
            R.id.actionToggleLineValues -> {
                for (set in chart.getData().getDataSets()!!) {
                    (set as? LineDataSet)?.setDrawValues(!set.isDrawValuesEnabled())
                }
                chart.invalidate()
            }
            R.id.actionToggleBarValues -> {
                for (set in chart.getData().getDataSets()!!) {
                    (set as? BarDataSet)?.setDrawValues(!set.isDrawValuesEnabled())
                }
                chart.invalidate()
            }
            R.id.actionRemoveDataSet -> {
                val rnd = getRandom(chart.getData().getDataSetCount().toFloat(), 0f).toInt()
                chart.getData().removeDataSet(chart.getData().getDataSetByIndex(rnd)!!)
                chart.getData().notifyDataChanged()
                chart.notifyDataSetChanged()
                chart.invalidate()
            }
        }
        return true
    }

    public override fun saveToGallery() { /* Intentionally left empty */
    }
}