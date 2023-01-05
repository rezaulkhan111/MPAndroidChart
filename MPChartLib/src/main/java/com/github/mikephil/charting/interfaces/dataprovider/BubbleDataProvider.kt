package com.github.mikephil.charting.interfaces.dataprovider

import com.github.mikephil.charting.data.BubbleData

interface BubbleDataProvider : BarLineScatterCandleBubbleDataProvider {
    fun getBubbleData(): BubbleData?
}