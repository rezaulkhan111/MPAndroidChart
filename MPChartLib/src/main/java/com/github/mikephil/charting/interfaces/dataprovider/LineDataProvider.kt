package com.github.mikephil.charting.interfaces.dataprovider

import com.github.mikephil.charting.components.YAxis
import com.github.mikephil.charting.components.YAxis.AxisDependency
import com.github.mikephil.charting.data.LineData

interface LineDataProvider : BarLineScatterCandleBubbleDataProvider {
    fun getLineData(): LineData

    fun getAxis(dependency: AxisDependency): YAxis
}