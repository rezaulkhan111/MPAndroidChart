package com.xxmassdeveloper.mpchartexample

import android.Manifest
import android.content.Intent
import android.content.pm.PackageManager
import android.graphics.Color
import android.net.Uri
import android.os.Bundle
import android.util.Log
import android.view.Menu
import android.view.MenuItem
import android.view.WindowManager
import android.widget.SeekBar
import android.widget.SeekBar.OnSeekBarChangeListener
import android.widget.TextView
import androidx.core.content.ContextCompat
import com.github.mikephil.charting.charts.LineChart
import com.github.mikephil.charting.components.Legend
import com.github.mikephil.charting.components.Legend.LegendForm
import com.github.mikephil.charting.components.YAxis.AxisDependency
import com.github.mikephil.charting.data.Entry
import com.github.mikephil.charting.data.LineData
import com.github.mikephil.charting.data.LineDataSet
import com.github.mikephil.charting.highlight.Highlight
import com.github.mikephil.charting.interfaces.datasets.ILineDataSet
import com.github.mikephil.charting.listener.OnChartValueSelectedListener
import com.github.mikephil.charting.utils.ColorTemplate.colorWithAlpha
import com.github.mikephil.charting.utils.ColorTemplate.getHoloBlue
import com.xxmassdeveloper.mpchartexample.notimportant.DemoBase

/**
 * Example of a dual axis [LineChart] with multiple data sets.
 *
 * @since 1.7.4
 * @version 3.1.0
 */
class LineChartActivity2 : DemoBase(), OnSeekBarChangeListener, OnChartValueSelectedListener {

    private lateinit var chart: LineChart
    private lateinit var seekBarX: SeekBar
    private lateinit var seekBarY: SeekBar
    private lateinit var tvX: TextView
    private lateinit var tvY: TextView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        window.setFlags(
            WindowManager.LayoutParams.FLAG_FULLSCREEN,
            WindowManager.LayoutParams.FLAG_FULLSCREEN
        )
        setContentView(R.layout.activity_linechart)
        title = "LineChartActivity2"
        tvX = findViewById(R.id.tvXMax)
        tvY = findViewById(R.id.tvYMax)
        seekBarX = findViewById(R.id.seekBar1)
        seekBarX.setOnSeekBarChangeListener(this)
        seekBarY = findViewById(R.id.seekBar2)
        seekBarY.setOnSeekBarChangeListener(this)
        chart = findViewById(R.id.chart1)
        chart.setOnChartValueSelectedListener(this)

        // no description text
        chart.getDescription()!!.setEnabled(false)

        // enable touch gestures
        chart.setTouchEnabled(true)

        chart.setDragDecelerationFrictionCoef(0.9f)

        // enable scaling and dragging
        chart.setDragEnabled(true)
        chart.setScaleEnabled(true)
        chart.setDrawGridBackground(false)
        chart.setHighlightPerDragEnabled(true)

        // if disabled, scaling can be done on x- and y-axis separately
        chart.setPinchZoom(true)

        // set an alternative background color
        chart.setBackgroundColor(Color.LTGRAY)

        // add data
        seekBarX.progress = 20
        seekBarY.progress = 30

        chart.animateX(1500)

        // get the legend (only possible after setting data)
        val l = chart.getLegend()

        // modify the legend ...
        l!!.setForm(LegendForm.LINE)
        l.setTypeface(tfLight!!)
        l.setTextSize(11f)
        l.setTextColor(Color.WHITE)
        l.setVerticalAlignment(Legend.LegendVerticalAlignment.BOTTOM)
        l.setHorizontalAlignment(Legend.LegendHorizontalAlignment.LEFT)
        l.setOrientation(Legend.LegendOrientation.HORIZONTAL)
        l.setDrawInside(false)
//        l.setYOffset(11f);
        val xAxis = chart.getXAxis()
        xAxis!!.setTypeface(tfLight!!)
        xAxis.setTextSize(11f)
        xAxis.setTextColor(Color.WHITE)
        xAxis.setDrawGridLines(false)
        xAxis.setDrawAxisLine(false)

        val leftAxis = chart.getAxisLeft()
        leftAxis!!.setTypeface(tfLight!!)
        leftAxis.setTextColor(getHoloBlue())
        leftAxis.setAxisMaximum(200f)
        leftAxis.setAxisMinimum(0f)
        leftAxis.setDrawGridLines(true)
        leftAxis.setGranularityEnabled(true)

        val rightAxis = chart.getAxisRight()
        rightAxis!!.setTypeface(tfLight!!)
        rightAxis.setTextColor(Color.RED)
        rightAxis.setAxisMaximum(900f)
        rightAxis.setAxisMinimum(-200f)
        rightAxis.setDrawGridLines(false)
        rightAxis.setDrawZeroLine(false)
        rightAxis.setGranularityEnabled(false)
    }

    private fun setData(count: Int, range: Float) {
        val values1 = mutableListOf<Entry>()
        for (i in 0 until count) {
            val valF1 = (Math.random() * (range / 2f)).toFloat() + 50
            values1.add(Entry(i.toFloat(), valF1))
        }
        val values2 = mutableListOf<Entry>()
        for (i in 0 until count) {
            val valF2 = (Math.random() * range).toFloat() + 450
            values2.add(Entry(i.toFloat(), valF2))
        }
        val values3 = mutableListOf<Entry>()
        for (i in 0 until count) {
            val valF3 = (Math.random() * range).toFloat() + 500
            values3.add(Entry(i.toFloat(), valF3))
        }
        val set1: LineDataSet?
        val set2: LineDataSet?
        val set3: LineDataSet?

        if (chart.getData() != null &&
            chart.getData().getDataSetCount() > 0
        ) {
            set1 = chart.getData().getDataSetByIndex(0) as LineDataSet?
            set2 = chart.getData().getDataSetByIndex(1) as LineDataSet?
            set3 = chart.getData().getDataSetByIndex(2) as LineDataSet?
            set1!!.setValues(values1)
            set2!!.setValues(values2)
            set3!!.setValues(values3)
            chart.getData().notifyDataChanged()
            chart.notifyDataSetChanged()
        } else {
            // create a dataset and give it a type
            set1 = LineDataSet(values1, "DataSet 1")
            set1.setAxisDependency(AxisDependency.LEFT)
            set1.setColor(getHoloBlue())
            set1.setCircleColor(Color.WHITE)
            set1.setLineWidth(2f)
            set1.setCircleRadius(3f)
            set1.setFillAlpha(65)
            set1.setFillColor(getHoloBlue())
            set1.setHighLightColor(Color.rgb(244, 117, 117))
            set1.setDrawCircleHole(false)
            //set1.setFillFormatter(new MyFillFormatter(0f));
            //set1.setDrawHorizontalHighlightIndicator(false);
            //set1.setVisible(false);
            //set1.setCircleHoleColor(Color.WHITE);

            // create a dataset and give it a type
            set2 = LineDataSet(values2, "DataSet 2")
            set2.setAxisDependency(AxisDependency.RIGHT)
            set2.setColor(Color.RED)
            set2.setCircleColor(Color.WHITE)
            set2.setLineWidth(2f)
            set2.setCircleRadius(3f)
            set2.setFillAlpha(65)
            set2.setFillColor(Color.RED)
            set2.setDrawCircleHole(false)
            set2.setHighLightColor(Color.rgb(244, 117, 117))
            //set2.setFillFormatter(new MyFillFormatter(900f));
            set3 = LineDataSet(values3, "DataSet 3")
            set3.setAxisDependency(AxisDependency.RIGHT)
            set3.setColor(Color.YELLOW)
            set3.setCircleColor(Color.WHITE)
            set3.setLineWidth(2f)
            set3.setCircleRadius(3f)
            set3.setFillAlpha(65)
            set3.setFillColor(colorWithAlpha(Color.YELLOW, 200))
            set3.setDrawCircleHole(false)
            set3.setHighLightColor(Color.rgb(244, 117, 117))

            // create a data object with the data sets
            val data = LineData(set1, set2, set3)
            data.setValueTextColor(Color.WHITE)
            data.setValueTextSize(9f)

            // set data
            chart.setData(data)
        }
    }

    override fun onCreateOptionsMenu(menu: Menu): Boolean {
        menuInflater.inflate(R.menu.line, menu)
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when (item.itemId) {
            R.id.viewGithub -> {
                val i = Intent(Intent.ACTION_VIEW)
                i.data =
                    Uri.parse("https://github.com/PhilJay/MPAndroidChart/blob/master/MPChartExample/src/com/xxmassdeveloper/mpchartexample/LineChartActivity2.java")
                startActivity(i)
            }
            R.id.actionToggleValues -> {
                val sets: List<ILineDataSet>? = chart.getData()
                    .getDataSets()
                for (iSet in sets!!) {
                    val set = iSet as LineDataSet
                    set.setDrawValues(!set.isDrawValuesEnabled())
                }
                chart.invalidate()
            }
            R.id.actionToggleHighlight -> {
                if (chart.getData() != null) {
                    chart.getData().setHighlightEnabled(!chart.getData().isHighlightEnabled())
                    chart.invalidate()
                }
            }
            R.id.actionToggleFilled -> {
                val sets: List<ILineDataSet>? = chart.getData()
                    .getDataSets()
                for (iSet in sets!!) {
                    val set = iSet as LineDataSet
                    if (set.isDrawFilledEnabled()) set.setDrawFilled(false) else set.setDrawFilled(
                        true
                    )
                }
                chart.invalidate()
            }
            R.id.actionToggleCircles -> {
                val sets: List<ILineDataSet>? = chart.getData()
                    .getDataSets()
                for (iSet in sets!!) {
                    val set = iSet as LineDataSet
                    if (set.isDrawCirclesEnabled()) set.setDrawCircles(false) else set.setDrawCircles(
                        true
                    )
                }
                chart.invalidate()
            }
            R.id.actionToggleCubic -> {
                val sets: List<ILineDataSet>? = chart.getData()
                    .getDataSets()
                for (iSet in sets!!) {
                    val set = iSet as LineDataSet
                    set.setMode(if (set.getMode() == LineDataSet.Mode.CUBIC_BEZIER) LineDataSet.Mode.LINEAR else LineDataSet.Mode.CUBIC_BEZIER)
                }
                chart.invalidate()
            }
            R.id.actionToggleStepped -> {
                val sets: List<ILineDataSet>? = chart.getData()
                    .getDataSets()
                for (iSet in sets!!) {
                    val set = iSet as LineDataSet
                    set.setMode(if (set.getMode() == LineDataSet.Mode.STEPPED) LineDataSet.Mode.LINEAR else LineDataSet.Mode.STEPPED)
                }
                chart.invalidate()
            }
            R.id.actionToggleHorizontalCubic -> {
                val sets: List<ILineDataSet>? = chart.getData()
                    .getDataSets()
                for (iSet in sets!!) {
                    val set = iSet as LineDataSet
                    set.setMode(if (set.getMode() == LineDataSet.Mode.HORIZONTAL_BEZIER) LineDataSet.Mode.LINEAR else LineDataSet.Mode.HORIZONTAL_BEZIER)
                }
                chart.invalidate()
            }
            R.id.actionTogglePinch -> {
                if (chart.isPinchZoomEnabled()) chart.setPinchZoom(false) else chart.setPinchZoom(
                    true
                )
                chart.invalidate()
            }
            R.id.actionToggleAutoScaleMinMax -> {
                chart.setAutoScaleMinMaxEnabled(!chart.isAutoScaleMinMaxEnabled())
                chart.notifyDataSetChanged()
            }
            R.id.animateX -> {
                chart.animateX(2000)
            }
            R.id.animateY -> {
                chart.animateY(2000)
            }
            R.id.animateXY -> {
                chart.animateXY(2000, 2000)
            }
            R.id.actionSave -> {
                if (ContextCompat.checkSelfPermission(
                        this,
                        Manifest.permission.WRITE_EXTERNAL_STORAGE
                    ) == PackageManager.PERMISSION_GRANTED
                ) {
                    saveToGallery()
                } else {
                    requestStoragePermission(chart)
                }
            }
        }
        return true
    }

    override fun onProgressChanged(seekBar: SeekBar, progress: Int, fromUser: Boolean) {
        tvX.text = seekBarX.progress.toString()
        tvY.text = seekBarY.progress.toString()
        setData(seekBarX.progress, seekBarY.progress.toFloat())

        // redraw
        chart.invalidate()
    }

    override fun saveToGallery() {
        saveToGallery(chart!!, "LineChartActivity2")
    }

    override fun onValueSelected(e: Entry, h: Highlight) {
        Log.i("Entry selected", e.toString())

        chart.centerViewToAnimated(
            e.getX(),
            e.getY(),
            chart.getData().getDataSetByIndex(h.getDataSetIndex())!!.getAxisDependency(),
            500
        )
        //chart.zoomAndCenterAnimated(2.5f, 2.5f, e.getX(), e.getY(), chart.getData().getDataSetByIndex(dataSetIndex)
        // .getAxisDependency(), 1000);
        //chart.zoomAndCenterAnimated(1.8f, 1.8f, e.getX(), e.getY(), chart.getData().getDataSetByIndex(dataSetIndex)
        // .getAxisDependency(), 1000);
    }

    override fun onNothingSelected() {
        Log.i("Nothing selected", "Nothing selected.")
    }

    override fun onStartTrackingTouch(seekBar: SeekBar) {}
    override fun onStopTrackingTouch(seekBar: SeekBar) {}
}