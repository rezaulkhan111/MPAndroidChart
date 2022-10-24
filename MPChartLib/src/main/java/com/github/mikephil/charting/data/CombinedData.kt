package com.github.mikephil.charting.data

import com.github.mikephil.charting.interfaces.datasets.IDataSet.entryCount
import com.github.mikephil.charting.interfaces.datasets.IDataSet.getEntryForIndex
import com.github.mikephil.charting.interfaces.datasets.IDataSet.label
import com.github.mikephil.charting.highlight.Highlight.x
import com.github.mikephil.charting.interfaces.datasets.IDataSet.calcMinMaxY
import com.github.mikephil.charting.interfaces.datasets.IDataSet.yMax
import com.github.mikephil.charting.interfaces.datasets.IDataSet.yMin
import com.github.mikephil.charting.interfaces.datasets.IDataSet.axisDependency
import com.github.mikephil.charting.highlight.Highlight.dataSetIndex
import com.github.mikephil.charting.interfaces.datasets.IDataSet.getEntryForXValue
import com.github.mikephil.charting.highlight.Highlight.y
import com.github.mikephil.charting.interfaces.datasets.IDataSet.addEntry
import com.github.mikephil.charting.interfaces.datasets.IDataSet.xMax
import com.github.mikephil.charting.interfaces.datasets.IDataSet.xMin
import com.github.mikephil.charting.interfaces.datasets.IDataSet.removeEntry
import com.github.mikephil.charting.interfaces.datasets.IDataSet.colors
import com.github.mikephil.charting.interfaces.datasets.IDataSet.valueFormatter
import com.github.mikephil.charting.interfaces.datasets.IDataSet.valueTextColor
import com.github.mikephil.charting.interfaces.datasets.IDataSet.setValueTextColors
import com.github.mikephil.charting.interfaces.datasets.IDataSet.valueTypeface
import com.github.mikephil.charting.interfaces.datasets.IDataSet.valueTextSize
import com.github.mikephil.charting.interfaces.datasets.IDataSet.setDrawValues
import com.github.mikephil.charting.interfaces.datasets.IDataSet.isHighlightEnabled
import com.github.mikephil.charting.interfaces.datasets.IBubbleDataSet.highlightCircleWidth
import com.github.mikephil.charting.utils.Utils.convertDpToPixel
import com.github.mikephil.charting.interfaces.datasets.IDataSet.calcMinMax
import com.github.mikephil.charting.utils.ColorTemplate.createColors
import com.github.mikephil.charting.utils.Utils.defaultValueFormatter
import com.github.mikephil.charting.interfaces.datasets.IScatterDataSet.scatterShapeSize
import com.github.mikephil.charting.highlight.Highlight.dataIndex
import com.github.mikephil.charting.interfaces.datasets.IDataSet.getEntriesForXValue
import android.annotation.TargetApi
import android.os.Build
import com.github.mikephil.charting.data.filter.ApproximatorN
import android.os.Parcelable
import android.graphics.drawable.Drawable
import android.os.Parcel
import android.os.ParcelFormatException
import android.os.Parcelable.Creator
import com.github.mikephil.charting.data.BarLineScatterCandleBubbleData
import com.github.mikephil.charting.interfaces.datasets.IBarDataSet
import com.github.mikephil.charting.data.BarEntry
import com.github.mikephil.charting.data.BaseDataSet
import com.github.mikephil.charting.data.DataSet.Rounding
import com.github.mikephil.charting.data.DataSet
import com.github.mikephil.charting.data.ChartData
import com.github.mikephil.charting.interfaces.datasets.IPieDataSet
import android.annotation.SuppressLint
import com.github.mikephil.charting.interfaces.datasets.ILineDataSet
import com.github.mikephil.charting.data.PieEntry
import com.github.mikephil.charting.interfaces.datasets.IDataSet
import com.github.mikephil.charting.components.YAxis.AxisDependency
import com.github.mikephil.charting.formatter.IValueFormatter
import android.graphics.Typeface
import com.github.mikephil.charting.interfaces.datasets.IRadarDataSet
import com.github.mikephil.charting.data.BarLineScatterCandleBubbleDataSet
import com.github.mikephil.charting.utils.Fill
import com.github.mikephil.charting.data.BarDataSet
import com.github.mikephil.charting.interfaces.datasets.IBubbleDataSet
import com.github.mikephil.charting.interfaces.datasets.ICandleDataSet
import com.github.mikephil.charting.data.PieDataSet.ValuePosition
import com.github.mikephil.charting.data.PieDataSet
import com.github.mikephil.charting.data.RadarEntry
import com.github.mikephil.charting.components.YAxis
import com.github.mikephil.charting.components.Legend.LegendForm
import com.github.mikephil.charting.components.Legend
import android.graphics.DashPathEffect
import android.util.Log
import com.github.mikephil.charting.utils.MPPointF
import com.github.mikephil.charting.utils.ColorTemplate
import com.github.mikephil.charting.data.BubbleEntry
import com.github.mikephil.charting.data.CandleEntry
import com.github.mikephil.charting.data.LineRadarDataSet
import com.github.mikephil.charting.formatter.IFillFormatter
import com.github.mikephil.charting.formatter.DefaultFillFormatter
import com.github.mikephil.charting.data.LineDataSet
import com.github.mikephil.charting.interfaces.datasets.IScatterDataSet
import com.github.mikephil.charting.interfaces.datasets.IBarLineScatterCandleBubbleDataSet
import com.github.mikephil.charting.data.LineData
import com.github.mikephil.charting.data.BarData
import com.github.mikephil.charting.data.ScatterData
import com.github.mikephil.charting.data.CandleData
import com.github.mikephil.charting.data.BubbleData
import com.github.mikephil.charting.data.RadarDataSet
import com.github.mikephil.charting.data.BubbleDataSet
import com.github.mikephil.charting.data.LineScatterCandleRadarDataSet
import com.github.mikephil.charting.data.CandleDataSet
import com.github.mikephil.charting.data.ScatterDataSet
import com.github.mikephil.charting.charts.ScatterChart.ScatterShape
import com.github.mikephil.charting.highlight.Highlight
import com.github.mikephil.charting.renderer.scatter.TriangleShapeRenderer
import com.github.mikephil.charting.interfaces.datasets.ILineRadarDataSet
import com.github.mikephil.charting.interfaces.datasets.ILineScatterCandleRadarDataSet
import java.util.ArrayList

/**
 * Data object that allows the combination of Line-, Bar-, Scatter-, Bubble- and
 * CandleData. Used in the CombinedChart class.
 *
 * @author Philipp Jahoda
 */
class CombinedData :
    BarLineScatterCandleBubbleData<IBarLineScatterCandleBubbleDataSet<out Entry?>?>() {
    private var mLineData: LineData? = null
    private var mBarData: BarData? = null
    private var mScatterData: ScatterData? = null
    private var mCandleData: CandleData? = null
    private var mBubbleData: BubbleData? = null
    fun setData(data: LineData?) {
        mLineData = data
        notifyDataChanged()
    }

    fun setData(data: BarData?) {
        mBarData = data
        notifyDataChanged()
    }

    fun setData(data: ScatterData?) {
        mScatterData = data
        notifyDataChanged()
    }

    fun setData(data: CandleData?) {
        mCandleData = data
        notifyDataChanged()
    }

    fun setData(data: BubbleData?) {
        mBubbleData = data
        notifyDataChanged()
    }

    public override fun calcMinMax() {
        if (mDataSets == null) {
            mDataSets = ArrayList<IBarLineScatterCandleBubbleDataSet<out Entry>>()
        }
        mDataSets!!.clear()
        mYMax = -Float.MAX_VALUE
        mYMin = Float.MAX_VALUE
        mXMax = -Float.MAX_VALUE
        mXMin = Float.MAX_VALUE
        mLeftAxisMax = -Float.MAX_VALUE
        mLeftAxisMin = Float.MAX_VALUE
        mRightAxisMax = -Float.MAX_VALUE
        mRightAxisMin = Float.MAX_VALUE
        val allData = getAllData()
        for (data in allData) {
            data.calcMinMax()
            val sets = data.dataSets
            mDataSets!!.addAll(sets!!)
            if (data.yMax > mYMax) mYMax = data.yMax
            if (data.yMin < mYMin) mYMin = data.yMin
            if (data.xMax > mXMax) mXMax = data.xMax
            if (data.xMin < mXMin) mXMin = data.xMin
            for (dataset in sets) {
                if (dataset!!.axisDependency == AxisDependency.LEFT) {
                    if (dataset.yMax > mLeftAxisMax) {
                        mLeftAxisMax = dataset.yMax
                    }
                    if (dataset.yMin < mLeftAxisMin) {
                        mLeftAxisMin = dataset.yMin
                    }
                } else {
                    if (dataset.yMax > mRightAxisMax) {
                        mRightAxisMax = dataset.yMax
                    }
                    if (dataset.yMin < mRightAxisMin) {
                        mRightAxisMin = dataset.yMin
                    }
                }
            }
        }
    }

    fun getBubbleData(): BubbleData? {
        return mBubbleData
    }

    fun getLineData(): LineData? {
        return mLineData
    }

    fun getBarData(): BarData? {
        return mBarData
    }

    fun getScatterData(): ScatterData? {
        return mScatterData
    }

    fun getCandleData(): CandleData? {
        return mCandleData
    }

    /**
     * Returns all data objects in row: line-bar-scatter-candle-bubble if not null.
     *
     * @return
     */
    fun getAllData(): List<BarLineScatterCandleBubbleData<*>> {
        val data: MutableList<BarLineScatterCandleBubbleData<*>> = ArrayList()
        if (mLineData != null) data.add(mLineData!!)
        if (mBarData != null) data.add(mBarData!!)
        if (mScatterData != null) data.add(mScatterData!!)
        if (mCandleData != null) data.add(mCandleData!!)
        if (mBubbleData != null) data.add(mBubbleData!!)
        return data
    }

    fun getDataByIndex(index: Int): BarLineScatterCandleBubbleData<*> {
        return getAllData()[index]
    }

    override fun notifyDataChanged() {
        if (mLineData != null) mLineData!!.notifyDataChanged()
        if (mBarData != null) mBarData!!.notifyDataChanged()
        if (mCandleData != null) mCandleData!!.notifyDataChanged()
        if (mScatterData != null) mScatterData!!.notifyDataChanged()
        if (mBubbleData != null) mBubbleData!!.notifyDataChanged()
        calcMinMax() // recalculate everything
    }

    /**
     * Get the Entry for a corresponding highlight object
     *
     * @param highlight
     * @return the entry that is highlighted
     */
    override fun getEntryForHighlight(highlight: Highlight): Entry? {
        if (highlight.dataIndex >= getAllData().size) return null
        val data: ChartData<*> = getDataByIndex(highlight.dataIndex)
        if (highlight.dataSetIndex >= data.dataSetCount) return null

        // The value of the highlighted entry could be NaN -
        //   if we are not interested in highlighting a specific value.
        val entries = data.getDataSetByIndex(highlight.dataSetIndex)
            .getEntriesForXValue(highlight.x)
        for (entry in entries!!) if (entry.y == highlight.y ||
            java.lang.Float.isNaN(highlight.y)
        ) return entry
        return null
    }

    /**
     * Get dataset for highlight
     *
     * @param highlight current highlight
     * @return dataset related to highlight
     */
    fun getDataSetByHighlight(highlight: Highlight): IBarLineScatterCandleBubbleDataSet<out Entry>? {
        if (highlight.dataIndex >= getAllData().size) return null
        val data = getDataByIndex(highlight.dataIndex)
        return if (highlight.dataSetIndex >= data.dataSetCount) null else data.dataSets[highlight.dataSetIndex] as IBarLineScatterCandleBubbleDataSet<out Entry>
    }

    fun getDataIndex(data: ChartData<*>?): Int {
        return getAllData().indexOf(data)
    }

    override fun removeDataSet(d: IBarLineScatterCandleBubbleDataSet<out Entry?>?): Boolean {
        val datas = getAllData()
        var success = false
        for (data in datas) {
            success = data.removeDataSet(d)
            if (success) {
                break
            }
        }
        return success
    }

    @Deprecated("")
    override fun removeDataSet(index: Int): Boolean {
        Log.e("MPAndroidChart", "removeDataSet(int index) not supported for CombinedData")
        return false
    }

    @Deprecated("")
    override fun removeEntry(e: Entry?, dataSetIndex: Int): Boolean {
        Log.e("MPAndroidChart", "removeEntry(...) not supported for CombinedData")
        return false
    }

    @Deprecated("")
    override fun removeEntry(xValue: Float, dataSetIndex: Int): Boolean {
        Log.e("MPAndroidChart", "removeEntry(...) not supported for CombinedData")
        return false
    }
}