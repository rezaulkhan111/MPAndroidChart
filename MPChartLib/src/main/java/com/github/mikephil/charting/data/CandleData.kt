package com.github.mikephil.charting.data

import com.github.mikephil.charting.interfaces.datasets.ICandleDataSet

class CandleData : BarLineScatterCandleBubbleData<ICandleDataSet> {
    constructor() : super() {}
    constructor(dataSets: List<ICandleDataSet>?) : super(dataSets) {}
    constructor(vararg dataSets: ICandleDataSet?) : super(*dataSets) {}
}