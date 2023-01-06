package com.github.mikephil.charting.data

import android.util.Log
import com.github.mikephil.charting.components.YAxis.AxisDependency
import com.github.mikephil.charting.highlight.Highlight
import com.github.mikephil.charting.interfaces.datasets.IBarLineScatterCandleBubbleDataSet

/**
 * Data object that allows the combination of Line-, Bar-, Scatter-, Bubble- and
 * CandleData. Used in the CombinedChart class.
 *
 * @author Philipp Jahoda
 */
class CombinedData() :
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

    override fun calcMinMax() {
        if (mDataSets == null) {
            mDataSets = ArrayList()
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
            val sets = data.getDataSets()
            mDataSets!!.addAll(sets!!)
            if (data.getYMax() > mYMax) mYMax = data.getYMax()
            if (data.getYMin() < mYMin) mYMin = data.getYMin()
            if (data.getXMax() > mXMax) mXMax = data.getXMax()
            if (data.getXMin() < mXMin) mXMin = data.getXMin()
            for (dataset in sets) {
                if (dataset!!.getAxisDependency() == AxisDependency.LEFT) {
                    if (dataset.getYMax() > mLeftAxisMax) {
                        mLeftAxisMax = dataset.getYMax()
                    }
                    if (dataset.getYMin() < mLeftAxisMin) {
                        mLeftAxisMin = dataset.getYMin()
                    }
                } else {
                    if (dataset.getYMax() > mRightAxisMax) {
                        mRightAxisMax = dataset.getYMax()
                    }
                    if (dataset.getYMin() < mRightAxisMin) {
                        mRightAxisMin = dataset.getYMin()
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
        if (highlight.getDataIndex() >= getAllData().size) return null
        val data: ChartData<*> = getDataByIndex(highlight.getDataIndex())
        if (highlight.getDataSetIndex() >= data.getDataSetCount()) return null
        // The value of the highlighted entry could be NaN -
        //   if we are not interested in highlighting a specific value.
        val entries = data.getDataSetByIndex(highlight.getDataSetIndex())!!
            .getEntriesForXValue(highlight.getX())!!
        for (entry in entries) if (entry!!.getY() == highlight.getY() ||
            java.lang.Float.isNaN(highlight.getY())
        ) return entry
        return null
    }

    /**
     * Get dataset for highlight
     *
     * @param highlight current highlight
     * @return dataset related to highlight
     */
    fun getDataSetByHighlight(highlight: Highlight): IBarLineScatterCandleBubbleDataSet<out Entry?>? {
        if (highlight.getDataIndex() >= getAllData().size) return null
        val data = getDataByIndex(highlight.getDataIndex())
        return if (highlight.getDataSetIndex() >= data.getDataSetCount()) null else data.getDataSets()!![highlight.getDataSetIndex()]

    }

    fun getDataIndex(data: ChartData<*>?): Int {
        return getAllData().indexOf(data)
    }

    override fun removeDataSet(d: IBarLineScatterCandleBubbleDataSet<out Entry?>?): Boolean {
        val datas = getAllData()
        var success = false
        for (data in datas) {
            success = data.removeDataSet(0)
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