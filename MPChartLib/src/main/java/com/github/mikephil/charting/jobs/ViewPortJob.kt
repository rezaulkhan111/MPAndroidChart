package com.github.mikephil.charting.jobs

import android.view.View
import com.github.mikephil.charting.utils.ObjectPool.Poolable
import com.github.mikephil.charting.utils.Transformer
import com.github.mikephil.charting.utils.ViewPortHandler

/**
 * Runnable that is used for viewport modifications since they cannot be
 * executed at any time. This can be used to delay the execution of viewport
 * modifications until the onSizeChanged(...) method of the chart-view is called.
 * This is especially important if viewport modifying methods are called on the chart
 * directly after initialization.
 *
 * @author Philipp Jahoda
 */
abstract class ViewPortJob : Poolable, Runnable {

    protected var pts = FloatArray(2)
    protected var mViewPortHandler: ViewPortHandler? = null
    protected var xValue = 0f
    protected var yValue = 0f
    protected var mTrans: Transformer? = null
    protected var view: View? = null

    constructor(
        viewPortHandler: ViewPortHandler?, xValue: Float, yValue: Float,
        trans: Transformer?, v: View?
    ) {
        mViewPortHandler = viewPortHandler
        this.xValue = xValue
        this.yValue = yValue
        mTrans = trans
        view = v
    }

    @JvmName("getXValue1")
    fun getXValue(): Float {
        return xValue
    }

    @JvmName("getYValue1")
    fun getYValue(): Float {
        return yValue
    }
}