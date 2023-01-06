package com.github.mikephil.charting.data

import com.github.mikephil.charting.highlight.Highlight
import com.github.mikephil.charting.interfaces.datasets.IRadarDataSet
import java.util.*

/**
 * Data container for the RadarChart.
 *
 * @author Philipp Jahoda
 */
class RadarData : ChartData<IRadarDataSet?> {

    private var mLabels: List<String>? = null

    constructor() : super() {}
    constructor(dataSets: MutableList<IRadarDataSet?>?) : super(dataSets) {}
    constructor(vararg dataSets: IRadarDataSet?) : super(*dataSets) {}

    /**
     * Sets the labels that should be drawn around the RadarChart at the end of each web line.
     *
     * @param labels
     */
    fun setLabels(labels: MutableList<String>) {
        this.mLabels = labels
    }

    /**
     * Sets the labels that should be drawn around the RadarChart at the end of each web line.
     *
     * @param labels
     */
    fun setLabels(vararg labels: String) {
        this.mLabels = Arrays.asList(*labels)
    }

    fun getLabels(): List<String?>? {
        return mLabels
    }

    override fun getEntryForHighlight(highlight: Highlight): Entry? {
        return getDataSetByIndex(highlight.getDataSetIndex())!!.getEntryForIndex(
            highlight.getX().toInt()
        )
    }
}