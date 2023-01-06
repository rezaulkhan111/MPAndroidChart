package com.xxmassdeveloper.mpchartexample.fragments

import android.graphics.Color
import android.graphics.Typeface
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.github.mikephil.charting.charts.ScatterChart.ScatterShape.Companion.allDefaultShapes
import com.github.mikephil.charting.data.*
import com.github.mikephil.charting.interfaces.datasets.IBarDataSet
import com.github.mikephil.charting.interfaces.datasets.ILineDataSet
import com.github.mikephil.charting.interfaces.datasets.IScatterDataSet
import com.github.mikephil.charting.utils.ColorTemplate
import com.github.mikephil.charting.utils.FileUtils.loadEntriesFromAssets

abstract class SimpleFragment : Fragment() {

    private var tf: Typeface? = null
//    var context: Context? = null
//    override fun onAttach(context: Context) {
//        super.onAttach(context)
//        this.context = context
//    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        tf = Typeface.createFromAsset(requireContext().assets, "OpenSans-Regular.ttf")
        return super.onCreateView(inflater, container, savedInstanceState)
    }

    fun generateBarData(dataSets: Int, range: Float, count: Int): BarData {
        val sets = mutableListOf<IBarDataSet?>()
        for (i in 0 until dataSets) {
            val entries = mutableListOf<BarEntry?>()
            for (j in 0 until count) {
                entries.add(BarEntry(j.toFloat(), (Math.random() * range).toFloat() + range / 4))
            }
            val ds = BarDataSet(entries, getLabel(i))
            ds.setColors(ColorTemplate.VORDIPLOM_COLORS)
            sets.add(ds)
        }
        val d = BarData(sets)
        d.setValueTypeface(tf)
        return d
    }

    fun generateScatterData(dataSets: Int, range: Float, count: Int): ScatterData {
        val sets = mutableListOf<IScatterDataSet?>()
        val shapes = allDefaultShapes
        for (i in 0 until dataSets) {
            val entries = mutableListOf<Entry?>()
            for (j in 0 until count) {
                entries.add(Entry(j.toFloat(), (Math.random() * range).toFloat() + range / 4))
            }
            val ds = ScatterDataSet(entries, getLabel(i))
            ds.setScatterShapeSize(12f)
            ds.setScatterShape(shapes[i % shapes.size])
            ds.setColors(ColorTemplate.COLORFUL_COLORS)
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
    fun generatePieData(): PieData {
        val count = 4
        val entries1 = ArrayList<PieEntry?>()
        for (i in 0 until count) {
            entries1.add(PieEntry((Math.random() * 60 + 40).toFloat(), "Quarter " + (i + 1)))
        }
        val ds1 = PieDataSet(entries1, "Quarterly Revenues 2015")
        ds1.setColors(ColorTemplate.VORDIPLOM_COLORS)
        ds1.setSliceSpace(2f)
        ds1.setValueTextColor(Color.WHITE)
        ds1.setValueTextSize(12f)
        val d = PieData(ds1)
        d.setValueTypeface(tf)
        return d
    }

    fun generateLineData(): LineData {
        val sets = ArrayList<ILineDataSet?>()
        val ds1 = LineDataSet(
            loadEntriesFromAssets(
                requireContext().assets, "sine.txt"
            ).toMutableList(), "Sine function"
        )
        val ds2 = LineDataSet(
            loadEntriesFromAssets(
                requireContext().assets, "cosine.txt"
            ).toMutableList(), "Cosine function"
        )

        ds1.setLineWidth(2f)
        ds2.setLineWidth(2f)

        ds1.setDrawCircles(false)
        ds2.setDrawCircles(false)

        ds1.setColor(ColorTemplate.VORDIPLOM_COLORS[0])
        ds2.setColor(ColorTemplate.VORDIPLOM_COLORS[1])


        // load DataSets from files in assets folder
        sets.add(ds1)
        sets.add(ds2)
        val d = LineData(sets)
        d.setValueTypeface(tf)
        return d
    }

    // load DataSets from files in assets folder
    val complexity: LineData
        get() {
            val sets = ArrayList<ILineDataSet?>()
            val ds1 = LineDataSet(
                loadEntriesFromAssets(
                    requireContext().assets, "n.txt"
                ).toMutableList(), "O(n)"
            )
            val ds2 = LineDataSet(
                loadEntriesFromAssets(
                    requireContext().assets, "nlogn.txt"
                ).toMutableList(), "O(nlogn)"
            )
            val ds3 = LineDataSet(
                loadEntriesFromAssets(
                    requireContext().assets, "square.txt"
                ).toMutableList(), "O(n\u00B2)"
            )
            val ds4 = LineDataSet(
                loadEntriesFromAssets(
                    requireContext().assets, "three.txt"
                ).toMutableList(), "O(n\u00B3)"
            )
            ds1.setColor(ColorTemplate.VORDIPLOM_COLORS[0])
            ds2.setColor(ColorTemplate.VORDIPLOM_COLORS[1])
            ds3.setColor(ColorTemplate.VORDIPLOM_COLORS[2])
            ds4.setColor(ColorTemplate.VORDIPLOM_COLORS[3])

            ds1.setCircleColor(ColorTemplate.VORDIPLOM_COLORS[0])
            ds2.setCircleColor(ColorTemplate.VORDIPLOM_COLORS[1])
            ds3.setCircleColor(ColorTemplate.VORDIPLOM_COLORS[2])
            ds4.setCircleColor(ColorTemplate.VORDIPLOM_COLORS[3])

            ds1.setLineWidth(2.5f)
            ds1.setCircleRadius(3f)
            ds2.setLineWidth(2.5f)
            ds2.setCircleRadius(3f)
            ds3.setLineWidth(2.5f)
            ds3.setCircleRadius(3f)
            ds4.setLineWidth(2.5f)
            ds4.setCircleRadius(3f)


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