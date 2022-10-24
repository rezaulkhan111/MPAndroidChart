package com.xxmassdeveloper.mpchartexample.notimportant

import android.Manifest
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
import android.view.View
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
 * Base class of all Activities of the Demo Application.
 *
 * @author Philipp Jahoda
 */
abstract class DemoBase : AppCompatActivity(), OnRequestPermissionsResultCallback {
    @JvmField
    protected val months = arrayOf(
        "Jan", "Feb", "Mar", "Apr", "May", "Jun", "Jul", "Aug", "Sep", "Okt", "Nov", "Dec"
    )
    @JvmField
    protected val parties = arrayOf(
        "Party A", "Party B", "Party C", "Party D", "Party E", "Party F", "Party G", "Party H",
        "Party I", "Party J", "Party K", "Party L", "Party M", "Party N", "Party O", "Party P",
        "Party Q", "Party R", "Party S", "Party T", "Party U", "Party V", "Party W", "Party X",
        "Party Y", "Party Z"
    )
    @JvmField
    protected var tfRegular: Typeface? = null
    @JvmField
    protected var tfLight: Typeface? = null
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        tfRegular = Typeface.createFromAsset(assets, "OpenSans-Regular.ttf")
        tfLight = Typeface.createFromAsset(assets, "OpenSans-Light.ttf")
    }

    protected fun getRandom(range: Float, start: Float): Float {
        return (Math.random() * range).toFloat() + start
    }

    override fun onBackPressed() {
        super.onBackPressed()
        overridePendingTransition(R.anim.move_left_in_activity, R.anim.move_right_out_activity)
    }

    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<String>,
        grantResults: IntArray
    ) {
        if (requestCode == PERMISSION_STORAGE) {
            if (grantResults.size == 1 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                saveToGallery()
            } else {
                Toast.makeText(applicationContext, "Saving FAILED!", Toast.LENGTH_SHORT)
                    .show()
            }
        }
    }

    protected fun requestStoragePermission(view: View?) {
        if (ActivityCompat.shouldShowRequestPermissionRationale(
                this,
                Manifest.permission.WRITE_EXTERNAL_STORAGE
            )
        ) {
            Snackbar.make(
                view!!,
                "Write permission is required to save image to gallery",
                Snackbar.LENGTH_INDEFINITE
            )
                .setAction(android.R.string.ok) {
                    ActivityCompat.requestPermissions(
                        this@DemoBase, arrayOf(
                            Manifest.permission.WRITE_EXTERNAL_STORAGE
                        ), PERMISSION_STORAGE
                    )
                }
                .show()
        } else {
            Toast.makeText(applicationContext, "Permission Required!", Toast.LENGTH_SHORT)
                .show()
            ActivityCompat.requestPermissions(
                this@DemoBase,
                arrayOf(Manifest.permission.WRITE_EXTERNAL_STORAGE),
                PERMISSION_STORAGE
            )
        }
    }

    protected fun saveToGallery(chart: Chart<*>, name: String) {
        if (chart.saveToGallery(name + "_" + System.currentTimeMillis(), 70)) Toast.makeText(
            applicationContext, "Saving SUCCESSFUL!",
            Toast.LENGTH_SHORT
        ).show() else Toast.makeText(applicationContext, "Saving FAILED!", Toast.LENGTH_SHORT)
            .show()
    }

    protected abstract fun saveToGallery()

    companion object {
        private const val PERMISSION_STORAGE = 0
    }
}