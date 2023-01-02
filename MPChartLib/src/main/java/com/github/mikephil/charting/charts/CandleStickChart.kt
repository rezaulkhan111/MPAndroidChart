package com.github.mikephil.charting.charts

import android.content.Context
import android.util.AttributeSet
import com.github.mikephil.charting.data.CandleData
import com.github.mikephil.charting.interfaces.dataprovider.CandleDataProvider
import com.github.mikephil.charting.renderer.CandleStickChartRenderer

/**
 * Financial chart type that draws candle-sticks (OHCL chart).
 *
 * @author Philipp Jahoda
 */
class CandleStickChart : BarLineChartBase<CandleData?>, CandleDataProvider {

    constructor(context: Context?) : super(context) {}
    constructor(context: Context?, attrs: AttributeSet?) : super(context, attrs) {}
    constructor(context: Context?, attrs: AttributeSet?, defStyle: Int) : super(
        context,
        attrs,
        defStyle
    ) {
    }

    override fun init() {
        super.init()
        mRenderer = CandleStickChartRenderer(this, mAnimator, mViewPortHandler)
        xAxis!!.setSpaceMin(0.5f)
        xAxis!!.setSpaceMax(0.5f)
    }

    override fun getCandleData(): CandleData? {
        return mData
    }
}