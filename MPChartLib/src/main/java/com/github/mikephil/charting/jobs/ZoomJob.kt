package com.github.mikephil.charting.jobs

import android.graphics.Matrix
import android.view.View
import com.github.mikephil.charting.charts.BarLineChartBase
import com.github.mikephil.charting.components.YAxis.AxisDependency
import com.github.mikephil.charting.utils.ObjectPool
import com.github.mikephil.charting.utils.ObjectPool.Companion.create
import com.github.mikephil.charting.utils.ObjectPool.Poolable
import com.github.mikephil.charting.utils.Transformer
import com.github.mikephil.charting.utils.ViewPortHandler

/**
 * Created by Philipp Jahoda on 19/02/16.
 */
class ZoomJob : ViewPortJob {

    companion object {
        private var pool: ObjectPool<ZoomJob>? = null

        @JvmStatic
        fun getInstance(
            viewPortHandler: ViewPortHandler?,
            scaleX: Float,
            scaleY: Float,
            xValue: Float,
            yValue: Float,
            trans: Transformer?,
            axis: AxisDependency?,
            v: View?
        ): ZoomJob {
            val result = pool!!.get()
            result!!.xValue = xValue
            result.yValue = yValue
            result.scaleX = scaleX
            result.scaleY = scaleY
            result.mViewPortHandler = viewPortHandler
            result.mTrans = trans
            result.axisDependency = axis
            result.view = v
            return result
        }

        fun recycleInstance(instance: ZoomJob?) {
            pool!!.recycle(instance!!)
        }
    }

    private var scaleX = 0f
    private var scaleY = 0f
    private var axisDependency: AxisDependency? = null

    constructor(
        viewPortHandler: ViewPortHandler?,
        scaleX: Float,
        scaleY: Float,
        xValue: Float,
        yValue: Float,
        trans: Transformer?,
        axis: AxisDependency?,
        v: View?
    ) : super(viewPortHandler, xValue, yValue, trans, v) {
        this.scaleX = scaleX
        this.scaleY = scaleY
        axisDependency = axis
    }

    private var mRunMatrixBuffer = Matrix()
    override fun run() {
        val save = mRunMatrixBuffer
        mViewPortHandler!!.zoom(scaleX, scaleY, save)
        mViewPortHandler!!.refresh(save, view!!, false)
        val yValsInView =
            (view as BarLineChartBase<*>).getAxis(axisDependency!!)!!.mAxisRange / mViewPortHandler!!.scaleY
        val xValsInView =
            (view as BarLineChartBase<*>).getXAxis()!!.mAxisRange / mViewPortHandler!!.scaleX
        pts[0] = xValue - xValsInView / 2f
        pts[1] = yValue + yValsInView / 2f
        mTrans!!.pointValuesToPixel(pts)
        mViewPortHandler!!.translate(pts, save)
        mViewPortHandler!!.refresh(save, view!!, false)
        (view as BarLineChartBase<*>).calculateOffsets()
        view!!.postInvalidate()
        recycleInstance(this)
    }

    override fun instantiate(): Poolable {
        return ZoomJob(null, 0f, 0f, 0f, 0f, null, null, null)
    }
}