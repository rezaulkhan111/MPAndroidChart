package com.github.mikephil.charting.data

import com.github.mikephil.charting.interfaces.datasets.IScatterDataSet

class ScatterData : BarLineScatterCandleBubbleData<IScatterDataSet?> {

    constructor() : super() {}
    constructor(dataSets: MutableList<IScatterDataSet?>?) : super(dataSets) {}
    constructor(vararg dataSets: IScatterDataSet?) : super(*dataSets) {}

    /**
     * Returns the maximum shape-size across all DataSets.
     *
     * @return
     */
    fun getGreatestShapeSize(): Float {
        var max = 0f
        for (set in mDataSets!!) {
            val size = set!!.getScatterShapeSize()
            if (size > max) max = size
        }
        return max
    }
}