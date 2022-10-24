package com.github.mikephil.charting.jobs

import com.github.mikephil.charting.utils.ObjectPool.Companion.create
import com.github.mikephil.charting.utils.ObjectPool.setReplenishPercentage
import com.github.mikephil.charting.utils.ObjectPool.get
import com.github.mikephil.charting.utils.ObjectPool.recycle
import com.github.mikephil.charting.utils.ViewPortHandler.zoom
import com.github.mikephil.charting.utils.ViewPortHandler.refresh
import com.github.mikephil.charting.utils.ViewPortHandler.scaleY
import com.github.mikephil.charting.utils.ViewPortHandler.scaleX
import com.github.mikephil.charting.utils.Transformer.pointValuesToPixel
import com.github.mikephil.charting.utils.ViewPortHandler.translate
import com.github.mikephil.charting.utils.ViewPortHandler.centerViewPort
import com.github.mikephil.charting.utils.ViewPortHandler.setZoom
import com.github.mikephil.charting.utils.ViewPortHandler
import com.github.mikephil.charting.components.YAxis.AxisDependency
import com.github.mikephil.charting.jobs.ViewPortJob
import com.github.mikephil.charting.jobs.ZoomJob
import com.github.mikephil.charting.charts.BarLineChartBase
import com.github.mikephil.charting.utils.ObjectPool.Poolable
import com.github.mikephil.charting.jobs.MoveViewJob
import android.annotation.SuppressLint
import com.github.mikephil.charting.components.YAxis
import com.github.mikephil.charting.jobs.AnimatedViewPortJob
import com.github.mikephil.charting.jobs.AnimatedZoomJob
import android.animation.ValueAnimator
import com.github.mikephil.charting.jobs.AnimatedMoveViewJob
import android.animation.ValueAnimator.AnimatorUpdateListener
import android.animation.ObjectAnimator
import android.graphics.Matrix
import android.view.View
import com.github.mikephil.charting.utils.ObjectPool
import com.github.mikephil.charting.utils.Transformer

/**
 * Created by Philipp Jahoda on 19/02/16.
 */
class ZoomJob(
    viewPortHandler: ViewPortHandler?,
    protected var scaleX: Float,
    protected var scaleY: Float,
    xValue: Float,
    yValue: Float,
    trans: Transformer?,
    protected var axisDependency: AxisDependency?,
    v: View?
) : ViewPortJob(viewPortHandler, xValue, yValue, trans, v) {
    companion object {
        private var pool: ObjectPool<ZoomJob?>? = null
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
        ): ZoomJob? {
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
            pool!!.recycle(instance)
        }

        init {
            pool = create(1, ZoomJob(null, 0, 0, 0, 0, null, null, null))
            pool!!.setReplenishPercentage(0.5f)
        }
    }

    protected var mRunMatrixBuffer = Matrix()
    override fun run() {
        val save = mRunMatrixBuffer
        mViewPortHandler!!.zoom(scaleX, scaleY, save)
        mViewPortHandler!!.refresh(save, view!!, false)
        val yValsInView =
            (view as BarLineChartBase<*>).getAxis(axisDependency).mAxisRange / mViewPortHandler!!.scaleY
        val xValsInView = (view as BarLineChartBase<*>).xAxis.mAxisRange / mViewPortHandler!!.scaleX
        pts[0] = xValue - xValsInView / 2f
        pts[1] = yValue + yValsInView / 2f
        mTrans!!.pointValuesToPixel(pts)
        mViewPortHandler!!.translate(pts, save)
        mViewPortHandler!!.refresh(save, view, false)
        (view as BarLineChartBase<*>).calculateOffsets()
        view.postInvalidate()
        recycleInstance(this)
    }

    protected override fun instantiate(): Poolable? {
        return ZoomJob(null, 0, 0, 0, 0, null, null, null)
    }
}