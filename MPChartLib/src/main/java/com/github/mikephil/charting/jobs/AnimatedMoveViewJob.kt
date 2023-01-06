package com.github.mikephil.charting.jobs

import android.animation.ValueAnimator
import android.annotation.SuppressLint
import android.view.View
import com.github.mikephil.charting.utils.ObjectPool
import com.github.mikephil.charting.utils.ObjectPool.Poolable
import com.github.mikephil.charting.utils.Transformer
import com.github.mikephil.charting.utils.ViewPortHandler

/**
 * Created by Philipp Jahoda on 19/02/16.
 */
@SuppressLint("NewApi")
class AnimatedMoveViewJob : AnimatedViewPortJob {

    companion object {
        private var pool: ObjectPool<AnimatedMoveViewJob>? = null

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
            val result = pool?.get()
            result?.mViewPortHandler = viewPortHandler
            result?.xValue = xValue
            result?.yValue = yValue
            result?.mTrans = trans
            result?.view = v
            result?.xOrigin = xOrigin
            result?.yOrigin = yOrigin
            //result.resetAnimator();
            result?.animator?.duration = duration
            return result
        }

        fun recycleInstance(instance: AnimatedMoveViewJob?) {
            pool!!.recycle(instance!!)
        }
    }

    constructor(
        viewPortHandler: ViewPortHandler?,
        xValue: Float,
        yValue: Float,
        trans: Transformer?,
        v: View?,
        xOrigin: Float,
        yOrigin: Float,
        duration: Long
    ) : super(viewPortHandler, xValue, yValue, trans, v, xOrigin, yOrigin, duration) {

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

    override fun instantiate(): Poolable {
        return AnimatedMoveViewJob(null, 0f, 0f, null, null, 0f, 0f, 0)
    }
}