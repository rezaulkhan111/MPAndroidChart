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
import android.view.View
import com.github.mikephil.charting.utils.ObjectPool
import com.github.mikephil.charting.utils.Transformer

/**
 * Created by Philipp Jahoda on 19/02/16.
 */
class MoveViewJob(
    viewPortHandler: ViewPortHandler?,
    xValue: Float,
    yValue: Float,
    trans: Transformer?,
    v: View?
) : ViewPortJob(viewPortHandler, xValue, yValue, trans, v) {
    companion object {
        private var pool: ObjectPool<MoveViewJob?>? = null
        @JvmStatic
        fun getInstance(
            viewPortHandler: ViewPortHandler?,
            xValue: Float,
            yValue: Float,
            trans: Transformer?,
            v: View?
        ): MoveViewJob? {
            val result = pool!!.get()
            result!!.mViewPortHandler = viewPortHandler
            result.xValue = xValue
            result.yValue = yValue
            result.mTrans = trans
            result.view = v
            return result
        }

        fun recycleInstance(instance: MoveViewJob?) {
            pool!!.recycle(instance)
        }

        init {
            pool = create(2, MoveViewJob(null, 0, 0, null, null))
            pool!!.setReplenishPercentage(0.5f)
        }
    }

    override fun run() {
        pts[0] = xValue
        pts[1] = yValue
        mTrans!!.pointValuesToPixel(pts)
        mViewPortHandler!!.centerViewPort(pts, view!!)
        recycleInstance(this)
    }

    protected override fun instantiate(): Poolable? {
        return MoveViewJob(mViewPortHandler, xValue, yValue, mTrans, view)
    }
}