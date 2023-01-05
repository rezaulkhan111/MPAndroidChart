package com.github.mikephil.charting.data

import com.github.mikephil.charting.interfaces.datasets.ICandleDataSet

class CandleData : BarLineScatterCandleBubbleData<ICandleDataSet> {
    constructor() : super() {}
    constructor(vararg dataSets: ICandleDataSet) : super(*dataSets) {}
    constructor(dataSets: MutableList<ICandleDataSet>) : super(dataSets) {}
}