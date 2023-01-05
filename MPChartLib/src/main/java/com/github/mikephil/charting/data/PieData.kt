package com.github.mikephil.charting.data

import android.util.Log
import com.github.mikephil.charting.highlight.Highlight
import com.github.mikephil.charting.interfaces.datasets.IPieDataSet

/**
 * A PieData object can only represent one DataSet. Unlike all other charts, the
 * legend labels of the PieChart are created from the x-values array, and not
 * from the DataSet labels. Each PieData object can only represent one
 * PieDataSet (multiple PieDataSets inside a single PieChart are not possible).
 *
 * @author Philipp Jahoda
 */
class PieData : ChartData<IPieDataSet> {

    constructor() : super() {}
    constructor(dataSet: IPieDataSet) : super(dataSet) {}

    /**
     * Sets the PieDataSet this data object should represent.
     *
     * @param dataSet
     */
    fun setDataSet(dataSet: IPieDataSet?) {
        mDataSets!!.clear()
        mDataSets!!.add(dataSet!!)
        notifyDataChanged()
    }

    /**
     * Returns the DataSet this PieData object represents. A PieData object can
     * only contain one DataSet.
     *
     * @return
     */
    fun getDataSet(): IPieDataSet {
        return mDataSets!![0]
    }

    override fun getDataSets(): MutableList<IPieDataSet>? {
        val dataSets: MutableList<IPieDataSet>? = super.getDataSets()
        if (dataSets!!.size < 1) {
            Log.e(
                "MPAndroidChart",
                "Found multiple data sets while pie chart only allows one"
            )
        }
        return dataSets
    }

    /**
     * The PieData object can only have one DataSet. Use getDataSet() method instead.
     *
     * @param index
     * @return
     */
    override fun getDataSetByIndex(index: Int): IPieDataSet? {
        return if (index == 0) getDataSet() else null
    }

    override fun getDataSetByLabel(label: String, ignorecase: Boolean): IPieDataSet? {
        return if (ignorecase) if (label.equals(
                mDataSets!![0].getLabel(),
                ignoreCase = true
            )
        ) mDataSets!![0] else null else if (label == mDataSets!![0].getLabel()) mDataSets!![0] else null
    }

    override fun getEntryForHighlight(highlight: Highlight): Entry {
        return getDataSet().getEntryForIndex(highlight.getX().toInt())
    }

    /**
     * Returns the sum of all values in this PieData object.
     *
     * @return
     */
    fun getYValueSum(): Float {
        var sum = 0f
        for (i in 0 until getDataSet().getEntryCount()) sum += getDataSet().getEntryForIndex(i)
            .getY()
        return sum
    }
}