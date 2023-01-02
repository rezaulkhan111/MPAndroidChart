package com.github.mikephil.charting.interfaces.dataprovider

import com.github.mikephil.charting.data.BarData

interface BarDataProvider : BarLineScatterCandleBubbleDataProvider {
    fun getBarData(): BarData
    fun isDrawBarShadowEnabled(): Boolean
    fun isDrawValueAboveBarEnabled(): Boolean
    fun isHighlightFullBarEnabled(): Boolean
}