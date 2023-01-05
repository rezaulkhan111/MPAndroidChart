package com.github.mikephil.charting.interfaces.dataprovider

import com.github.mikephil.charting.data.ScatterData

interface ScatterDataProvider : BarLineScatterCandleBubbleDataProvider {
    fun getScatterData(): ScatterData?
}