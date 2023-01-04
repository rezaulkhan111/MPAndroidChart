package com.github.mikephil.charting.jobs

import android.animation.Animator
import android.animation.ValueAnimator
import android.annotation.SuppressLint
import android.graphics.Matrix
import android.view.View
import com.github.mikephil.charting.charts.BarLineChartBase
import com.github.mikephil.charting.components.YAxis
import com.github.mikephil.charting.utils.ObjectPool
import com.github.mikephil.charting.utils.ObjectPool.Poolable
import com.github.mikephil.charting.utils.Transformer
import com.github.mikephil.charting.utils.ViewPortHandler

/**
 * Created by Philipp Jahoda on 19/02/16.
 */
@SuppressLint("NewApi")
class AnimatedZoomJob : AnimatedViewPortJob, Animator.AnimatorListener {


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
        ): AnimatedZoomJob {
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
            result.animator?.duration = duration
            return result
        }
    }

    private var zoomOriginX = 0f
    private var zoomOriginY = 0f
    private var zoomCenterX = 0f
    private var zoomCenterY = 0f
    private var yAxis: YAxis? = null
    private var xAxisRange = 0f

    constructor(
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
    ) : super(viewPortHandler, scaleX, scaleY, trans, v, xOrigin, yOrigin, duration) {
        this.zoomCenterX = zoomCenterX
        this.zoomCenterY = zoomCenterY
        this.zoomOriginX = zoomOriginX
        this.zoomOriginY = zoomOriginY
        animator!!.addListener(this)
        yAxis = axis
        this.xAxisRange = xAxisRange
    }


    private var mOnAnimationUpdateMatrixBuffer = Matrix()
    override fun onAnimationUpdate(animation: ValueAnimator?) {
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

    override fun onAnimationEnd(animation: Animator?) {
        (view as BarLineChartBase<*>).calculateOffsets()
        view?.postInvalidate()
    }

    override fun onAnimationCancel(animation: Animator?) {}

    override fun onAnimationRepeat(animation: Animator?) {}

    override fun recycleSelf() {}

    override fun onAnimationStart(animation: Animator?) {}

    override fun instantiate(): Poolable {
        return AnimatedZoomJob(
            null,
            null,
            null,
            null,
            0f,
            0f,
            0f,
            0f,
            0f,
            0f,
            0f,
            0f,
            0f,
            0
        )
    }
}