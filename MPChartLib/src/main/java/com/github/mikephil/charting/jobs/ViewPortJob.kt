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
abstract class ViewPortJob(
    protected var mViewPortHandler: ViewPortHandler?, xValue: Float, yValue: Float,
    trans: Transformer?, v: View?
) : Poolable(), Runnable {
    protected var pts = FloatArray(2)
    var xValue = 0f
        protected set
    var yValue = 0f
        protected set
    protected var mTrans: Transformer?
    protected var view: View?

    init {
        this.xValue = xValue
        this.yValue = yValue
        mTrans = trans
        view = v
    }
}