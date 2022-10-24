package com.xxmassdeveloper.mpchartexample.fragments

import android.content.Context
import com.github.mikephil.charting.charts.Chart.description
import com.github.mikephil.charting.components.ComponentBase.isEnabled
import com.github.mikephil.charting.charts.Chart.onChartGestureListener
import com.github.mikephil.charting.components.MarkerView.setChartView
import com.github.mikephil.charting.charts.Chart.marker
import com.github.mikephil.charting.charts.BarLineChartBase.setDrawGridBackground
import com.github.mikephil.charting.charts.BarChart.setDrawBarShadow
import com.github.mikephil.charting.charts.Chart.data
import com.github.mikephil.charting.charts.Chart.legend
import com.github.mikephil.charting.components.ComponentBase.typeface
import com.github.mikephil.charting.charts.BarLineChartBase.axisLeft
import com.github.mikephil.charting.components.AxisBase.axisMinimum
import com.github.mikephil.charting.charts.BarLineChartBase.axisRight
import com.github.mikephil.charting.charts.Chart.xAxis
import com.github.mikephil.charting.charts.Chart.highlightValues
import com.github.mikephil.charting.charts.PieChart.setCenterTextTypeface
import com.github.mikephil.charting.charts.PieChart.centerText
import com.github.mikephil.charting.charts.PieChart.setCenterTextSize
import com.github.mikephil.charting.charts.PieChart.holeRadius
import com.github.mikephil.charting.charts.PieChart.transparentCircleRadius
import com.github.mikephil.charting.components.Legend.verticalAlignment
import com.github.mikephil.charting.components.Legend.horizontalAlignment
import com.github.mikephil.charting.components.Legend.orientation
import com.github.mikephil.charting.components.Legend.setDrawInside
import com.github.mikephil.charting.data.BaseDataSet.setColors
import com.github.mikephil.charting.data.ChartData.setValueTypeface
import com.github.mikephil.charting.charts.ScatterChart.ScatterShape.Companion.allDefaultShapes
import com.github.mikephil.charting.data.ScatterDataSet.setScatterShapeSize
import com.github.mikephil.charting.data.ScatterDataSet.setScatterShape
import com.github.mikephil.charting.data.PieDataSet.sliceSpace
import com.github.mikephil.charting.data.BaseDataSet.valueTextColor
import com.github.mikephil.charting.data.BaseDataSet.valueTextSize
import com.github.mikephil.charting.utils.FileUtils.loadEntriesFromAssets
import com.github.mikephil.charting.data.LineRadarDataSet.lineWidth
import com.github.mikephil.charting.data.LineDataSet.setDrawCircles
import com.github.mikephil.charting.data.BaseDataSet.color
import com.github.mikephil.charting.data.LineDataSet.setCircleColor
import com.github.mikephil.charting.data.LineDataSet.circleRadius
import com.github.mikephil.charting.components.XAxis.position
import com.github.mikephil.charting.components.AxisBase.setDrawGridLines
import com.github.mikephil.charting.components.Legend.isWordWrapEnabled
import com.github.mikephil.charting.components.Legend.formSize
import com.github.mikephil.charting.components.ComponentBase.textSize
import com.github.mikephil.charting.components.ComponentBase.yOffset
import com.github.mikephil.charting.charts.Chart.extraBottomOffset
import com.github.mikephil.charting.charts.Chart.animateX
import com.github.mikephil.charting.components.AxisBase.axisMaximum
import com.xxmassdeveloper.mpchartexample.fragments.SimpleFragment
import com.github.mikephil.charting.listener.OnChartGestureListener
import com.github.mikephil.charting.charts.BarChart
import android.os.Bundle
import com.xxmassdeveloper.mpchartexample.R
import com.xxmassdeveloper.mpchartexample.custom.MyMarkerView
import android.graphics.Typeface
import com.github.mikephil.charting.components.Legend
import com.github.mikephil.charting.components.YAxis
import com.github.mikephil.charting.components.XAxis
import android.widget.FrameLayout
import com.github.mikephil.charting.listener.ChartTouchListener.ChartGesture
import com.xxmassdeveloper.mpchartexample.fragments.BarChartFrag
import com.github.mikephil.charting.charts.PieChart
import android.text.SpannableString
import android.text.style.RelativeSizeSpan
import android.text.style.ForegroundColorSpan
import com.xxmassdeveloper.mpchartexample.fragments.PieChartFrag
import com.github.mikephil.charting.interfaces.datasets.IBarDataSet
import com.github.mikephil.charting.utils.ColorTemplate
import com.github.mikephil.charting.interfaces.datasets.IScatterDataSet
import com.github.mikephil.charting.charts.ScatterChart.ScatterShape
import com.github.mikephil.charting.charts.ScatterChart
import com.github.mikephil.charting.interfaces.datasets.ILineDataSet
import com.xxmassdeveloper.mpchartexample.notimportant.DemoBase
import androidx.viewpager.widget.ViewPager
import com.xxmassdeveloper.mpchartexample.fragments.SimpleChartDemo.PageAdapter
import android.content.DialogInterface
import androidx.fragment.app.FragmentPagerAdapter
import com.xxmassdeveloper.mpchartexample.fragments.SineCosineFragment
import com.xxmassdeveloper.mpchartexample.fragments.ComplexityFragment
import com.xxmassdeveloper.mpchartexample.fragments.ScatterChartFrag
import android.content.Intent
import android.graphics.Color
import android.view.*
import androidx.fragment.app.Fragment
import com.github.mikephil.charting.components.XAxis.XAxisPosition
import com.github.mikephil.charting.charts.LineChart
import com.github.mikephil.charting.data.*
import java.util.ArrayList

abstract class SimpleFragment : Fragment() {
    private var tf: Typeface? = null
    protected var context: Context? = null
    override fun onAttach(context: Context) {
        super.onAttach(context)
        this.context = context
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        tf = Typeface.createFromAsset(context!!.assets, "OpenSans-Regular.ttf")
        return super.onCreateView(inflater, container, savedInstanceState)
    }

    protected fun generateBarData(dataSets: Int, range: Float, count: Int): BarData {
        val sets = ArrayList<IBarDataSet>()
        for (i in 0 until dataSets) {
            val entries = ArrayList<BarEntry?>()
            for (j in 0 until count) {
                entries.add(BarEntry(j, (Math.random() * range).toFloat() + range / 4))
            }
            val ds = BarDataSet(entries, getLabel(i))
            ds.setColors(*ColorTemplate.VORDIPLOM_COLORS)
            sets.add(ds)
        }
        val d = BarData(sets)
        d.setValueTypeface(tf)
        return d
    }

    protected fun generateScatterData(dataSets: Int, range: Float, count: Int): ScatterData {
        val sets = ArrayList<IScatterDataSet>()
        val shapes = allDefaultShapes
        for (i in 0 until dataSets) {
            val entries = ArrayList<Entry?>()
            for (j in 0 until count) {
                entries.add(Entry(j.toFloat(), (Math.random() * range).toFloat() + range / 4))
            }
            val ds = ScatterDataSet(entries, getLabel(i))
            ds.setScatterShapeSize(12f)
            ds.setScatterShape(shapes[i % shapes.size])
            ds.setColors(*ColorTemplate.COLORFUL_COLORS)
            ds.setScatterShapeSize(9f)
            sets.add(ds)
        }
        val d = ScatterData(sets)
        d.setValueTypeface(tf)
        return d
    }

    /**
     * generates less data (1 DataSet, 4 values)
     * @return PieData
     */
    protected fun generatePieData(): PieData {
        val count = 4
        val entries1 = ArrayList<PieEntry?>()
        for (i in 0 until count) {
            entries1.add(PieEntry((Math.random() * 60 + 40).toFloat(), "Quarter " + (i + 1)))
        }
        val ds1 = PieDataSet(entries1, "Quarterly Revenues 2015")
        ds1.setColors(*ColorTemplate.VORDIPLOM_COLORS)
        ds1.sliceSpace = 2f
        ds1.valueTextColor = Color.WHITE
        ds1.valueTextSize = 12f
        val d = PieData(ds1)
        d.setValueTypeface(tf)
        return d
    }

    protected fun generateLineData(): LineData {
        val sets = ArrayList<ILineDataSet>()
        val ds1 = LineDataSet(
            loadEntriesFromAssets(
                context!!.assets, "sine.txt"
            ), "Sine function"
        )
        val ds2 = LineDataSet(
            loadEntriesFromAssets(
                context!!.assets, "cosine.txt"
            ), "Cosine function"
        )
        ds1.lineWidth = 2f
        ds2.lineWidth = 2f
        ds1.setDrawCircles(false)
        ds2.setDrawCircles(false)
        ds1.color = ColorTemplate.VORDIPLOM_COLORS[0]
        ds2.color = ColorTemplate.VORDIPLOM_COLORS[1]

        // load DataSets from files in assets folder
        sets.add(ds1)
        sets.add(ds2)
        val d = LineData(sets)
        d.setValueTypeface(tf)
        return d
    }

    // load DataSets from files in assets folder
    protected val complexity: LineData
        protected get() {
            val sets = ArrayList<ILineDataSet>()
            val ds1 = LineDataSet(
                loadEntriesFromAssets(
                    context!!.assets, "n.txt"
                ), "O(n)"
            )
            val ds2 = LineDataSet(
                loadEntriesFromAssets(
                    context!!.assets, "nlogn.txt"
                ), "O(nlogn)"
            )
            val ds3 = LineDataSet(
                loadEntriesFromAssets(
                    context!!.assets, "square.txt"
                ), "O(n\u00B2)"
            )
            val ds4 = LineDataSet(
                loadEntriesFromAssets(
                    context!!.assets, "three.txt"
                ), "O(n\u00B3)"
            )
            ds1.color = ColorTemplate.VORDIPLOM_COLORS[0]
            ds2.color = ColorTemplate.VORDIPLOM_COLORS[1]
            ds3.color = ColorTemplate.VORDIPLOM_COLORS[2]
            ds4.color = ColorTemplate.VORDIPLOM_COLORS[3]
            ds1.setCircleColor(ColorTemplate.VORDIPLOM_COLORS[0])
            ds2.setCircleColor(ColorTemplate.VORDIPLOM_COLORS[1])
            ds3.setCircleColor(ColorTemplate.VORDIPLOM_COLORS[2])
            ds4.setCircleColor(ColorTemplate.VORDIPLOM_COLORS[3])
            ds1.lineWidth = 2.5f
            ds1.circleRadius = 3f
            ds2.lineWidth = 2.5f
            ds2.circleRadius = 3f
            ds3.lineWidth = 2.5f
            ds3.circleRadius = 3f
            ds4.lineWidth = 2.5f
            ds4.circleRadius = 3f


            // load DataSets from files in assets folder
            sets.add(ds1)
            sets.add(ds2)
            sets.add(ds3)
            sets.add(ds4)
            val d = LineData(sets)
            d.setValueTypeface(tf)
            return d
        }
    private val mLabels =
        arrayOf("Company A", "Company B", "Company C", "Company D", "Company E", "Company F")

    private fun getLabel(i: Int): String {
        return mLabels[i]
    }
}