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
@SuppressLint("NewApi")
class AnimatedMoveViewJob(
    viewPortHandler: ViewPortHandler?,
    xValue: Float,
    yValue: Float,
    trans: Transformer?,
    v: View?,
    xOrigin: Float,
    yOrigin: Float,
    duration: Long
) : AnimatedViewPortJob(viewPortHandler, xValue, yValue, trans, v, xOrigin, yOrigin, duration) {
    companion object {
        private var pool: ObjectPool<AnimatedMoveViewJob?>? = null
        @JvmStatic
        fun getInstance(
            viewPortHandler: ViewPortHandler?,
            xValue: Float,
            yValue: Float,
            trans: Transformer?,
            v: View?,
            xOrigin: Float,
            yOrigin: Float,
            duration: Long
        ): AnimatedMoveViewJob? {
            val result = pool!!.get()
            result!!.mViewPortHandler = viewPortHandler
            result.xValue = xValue
            result.yValue = yValue
            result.mTrans = trans
            result.view = v
            result.xOrigin = xOrigin
            result.yOrigin = yOrigin
            //result.resetAnimator();
            result.animator.duration = duration
            return result
        }

        fun recycleInstance(instance: AnimatedMoveViewJob?) {
            pool!!.recycle(instance)
        }

        init {
            pool = create(4, AnimatedMoveViewJob(null, 0, 0, null, null, 0, 0, 0))
            pool!!.setReplenishPercentage(0.5f)
        }
    }

    override fun onAnimationUpdate(animation: ValueAnimator) {
        pts[0] = xOrigin + (xValue - xOrigin) * phase
        pts[1] = yOrigin + (yValue - yOrigin) * phase
        mTrans!!.pointValuesToPixel(pts)
        mViewPortHandler!!.centerViewPort(pts, view!!)
    }

    override fun recycleSelf() {
        recycleInstance(this)
    }

    protected override fun instantiate(): Poolable? {
        return AnimatedMoveViewJob(null, 0, 0, null, null, 0, 0, 0)
    }
}