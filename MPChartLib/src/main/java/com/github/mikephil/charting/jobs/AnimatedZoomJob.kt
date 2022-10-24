package com.github.mikephil.charting.jobs

import android.animation.Animator
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
@SuppressLint("NewApi")
class AnimatedZoomJob @SuppressLint("NewApi") constructor(
    viewPortHandler: ViewPortHandler?,
    v: View?,
    trans: Transformer?,
    axis: YAxis?,
    xAxisRange: Float,
    scaleX: Float,
    scaleY: Float,
    xOrigin: Float,
    yOrigin: Float,
    protected var zoomCenterX: Float,
    protected var zoomCenterY: Float,
    protected var zoomOriginX: Float,
    protected var zoomOriginY: Float,
    duration: Long
) : AnimatedViewPortJob(viewPortHandler, scaleX, scaleY, trans, v, xOrigin, yOrigin, duration),
    Animator.AnimatorListener {
    companion object {
        private var pool: ObjectPool<AnimatedZoomJob?>? = null
        @JvmStatic
        fun getInstance(
            viewPortHandler: ViewPortHandler?,
            v: View?,
            trans: Transformer?,
            axis: YAxis?,
            xAxisRange: Float,
            scaleX: Float,
            scaleY: Float,
            xOrigin: Float,
            yOrigin: Float,
            zoomCenterX: Float,
            zoomCenterY: Float,
            zoomOriginX: Float,
            zoomOriginY: Float,
            duration: Long
        ): AnimatedZoomJob? {
            val result = pool!!.get()
            result!!.mViewPortHandler = viewPortHandler
            result.xValue = scaleX
            result.yValue = scaleY
            result.mTrans = trans
            result.view = v
            result.xOrigin = xOrigin
            result.yOrigin = yOrigin
            result.yAxis = axis
            result.xAxisRange = xAxisRange
            result.resetAnimator()
            result.animator.duration = duration
            return result
        }

        init {
            pool = create(8, AnimatedZoomJob(null, null, null, null, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0))
        }
    }

    protected var yAxis: YAxis?
    protected var xAxisRange: Float
    protected var mOnAnimationUpdateMatrixBuffer = Matrix()
    override fun onAnimationUpdate(animation: ValueAnimator) {
        val scaleX = xOrigin + (xValue - xOrigin) * phase
        val scaleY = yOrigin + (yValue - yOrigin) * phase
        val save = mOnAnimationUpdateMatrixBuffer
        mViewPortHandler!!.setZoom(scaleX, scaleY, save)
        mViewPortHandler!!.refresh(save, view!!, false)
        val valsInView = yAxis!!.mAxisRange / mViewPortHandler!!.scaleY
        val xsInView = xAxisRange / mViewPortHandler!!.scaleX
        pts[0] = zoomOriginX + (zoomCenterX - xsInView / 2f - zoomOriginX) * phase
        pts[1] = zoomOriginY + (zoomCenterY + valsInView / 2f - zoomOriginY) * phase
        mTrans!!.pointValuesToPixel(pts)
        mViewPortHandler!!.translate(pts, save)
        mViewPortHandler!!.refresh(save, view!!, true)
    }

    override fun onAnimationEnd(animation: Animator) {
        (view as BarLineChartBase<*>).calculateOffsets()
        view.postInvalidate()
    }

    override fun onAnimationCancel(animation: Animator) {}
    override fun onAnimationRepeat(animation: Animator) {}
    override fun recycleSelf() {}
    override fun onAnimationStart(animation: Animator) {}
    protected override fun instantiate(): Poolable? {
        return AnimatedZoomJob(null, null, null, null, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0)
    }

    init {
        animator.addListener(this)
        yAxis = axis
        this.xAxisRange = xAxisRange
    }
}