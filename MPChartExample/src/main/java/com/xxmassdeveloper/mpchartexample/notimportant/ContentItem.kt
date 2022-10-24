package com.xxmassdeveloper.mpchartexample.notimportant

import com.github.mikephil.charting.charts.Chart.saveToGallery
import com.github.mikephil.charting.utils.Utils.init
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityCompat.OnRequestPermissionsResultCallback
import android.graphics.Typeface
import android.os.Bundle
import com.xxmassdeveloper.mpchartexample.R
import com.xxmassdeveloper.mpchartexample.notimportant.DemoBase
import android.content.pm.PackageManager
import android.widget.Toast
import androidx.core.app.ActivityCompat
import com.google.android.material.snackbar.Snackbar
import com.github.mikephil.charting.charts.Chart
import com.xxmassdeveloper.mpchartexample.notimportant.ContentItem
import android.widget.ArrayAdapter
import android.annotation.SuppressLint
import android.view.ViewGroup
import android.view.LayoutInflater
import android.widget.TextView
import android.widget.AdapterView.OnItemClickListener
import android.view.WindowManager
import com.xxmassdeveloper.mpchartexample.notimportant.MyAdapter
import android.widget.AdapterView
import android.content.Intent
import com.xxmassdeveloper.mpchartexample.LineChartActivity1
import com.xxmassdeveloper.mpchartexample.MultiLineChartActivity
import com.xxmassdeveloper.mpchartexample.LineChartActivity2
import com.xxmassdeveloper.mpchartexample.InvertedLineChartActivity
import com.xxmassdeveloper.mpchartexample.CubicLineChartActivity
import com.xxmassdeveloper.mpchartexample.LineChartActivityColored
import com.xxmassdeveloper.mpchartexample.PerformanceLineChart
import com.xxmassdeveloper.mpchartexample.FilledLineActivity
import com.xxmassdeveloper.mpchartexample.BarChartActivity
import com.xxmassdeveloper.mpchartexample.AnotherBarActivity
import com.xxmassdeveloper.mpchartexample.BarChartActivityMultiDataset
import com.xxmassdeveloper.mpchartexample.HorizontalBarChartActivity
import com.xxmassdeveloper.mpchartexample.StackedBarActivity
import com.xxmassdeveloper.mpchartexample.BarChartPositiveNegative
import com.xxmassdeveloper.mpchartexample.HorizontalBarNegativeChartActivity
import com.xxmassdeveloper.mpchartexample.StackedBarActivityNegative
import com.xxmassdeveloper.mpchartexample.BarChartActivitySinus
import com.xxmassdeveloper.mpchartexample.PieChartActivity
import com.xxmassdeveloper.mpchartexample.PiePolylineChartActivity
import com.xxmassdeveloper.mpchartexample.HalfPieChartActivity
import com.xxmassdeveloper.mpchartexample.CombinedChartActivity
import com.xxmassdeveloper.mpchartexample.ScatterChartActivity
import com.xxmassdeveloper.mpchartexample.BubbleChartActivity
import com.xxmassdeveloper.mpchartexample.CandleStickChartActivity
import com.xxmassdeveloper.mpchartexample.RadarChartActivity
import com.xxmassdeveloper.mpchartexample.ListViewMultiChartActivity
import com.xxmassdeveloper.mpchartexample.fragments.SimpleChartDemo
import com.xxmassdeveloper.mpchartexample.ScrollViewActivity
import com.xxmassdeveloper.mpchartexample.ListViewBarChartActivity
import com.xxmassdeveloper.mpchartexample.DynamicalAddingActivity
import com.xxmassdeveloper.mpchartexample.RealtimeLineChartActivity
import com.xxmassdeveloper.mpchartexample.LineChartTime

/**
 * Created by Philipp Jahoda on 07/12/15.
 */
internal class ContentItem {
    val name: String
    val desc: String
    var isSection = false

    constructor(n: String) {
        name = n
        desc = ""
        isSection = true
    }

    constructor(n: String, d: String) {
        name = n
        desc = d
    }
}