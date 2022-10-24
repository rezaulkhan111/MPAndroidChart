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
import android.view.View
import com.github.mikephil.charting.utils.Transformer
import java.lang.IllegalArgumentException

/**
 * Created by Philipp Jahoda on 19/02/16.
 */
@SuppressLint("NewApi")
abstract class AnimatedViewPortJob(
    viewPortHandler: ViewPortHandler?,
    xValue: Float,
    yValue: Float,
    trans: Transformer?,
    v: View?,
    var xOrigin: Float,
    var yOrigin: Float,
    duration: Long
) : ViewPortJob(viewPortHandler, xValue, yValue, trans, v), AnimatorUpdateListener,
    Animator.AnimatorListener {
    protected var animator: ObjectAnimator
    var phase = 0f
    @SuppressLint("NewApi")
    override fun run() {
        animator.start()
    }

    abstract fun recycleSelf()
    protected fun resetAnimator() {
        animator.removeAllListeners()
        animator.removeAllUpdateListeners()
        animator.reverse()
        animator.addUpdateListener(this)
        animator.addListener(this)
    }

    override fun onAnimationStart(animation: Animator) {}
    override fun onAnimationEnd(animation: Animator) {
        try {
            recycleSelf()
        } catch (e: IllegalArgumentException) {
            // don't worry about it.
        }
    }

    override fun onAnimationCancel(animation: Animator) {
        try {
            recycleSelf()
        } catch (e: IllegalArgumentException) {
            // don't worry about it.
        }
    }

    override fun onAnimationRepeat(animation: Animator) {}
    override fun onAnimationUpdate(animation: ValueAnimator) {}

    init {
        animator = ObjectAnimator.ofFloat(this, "phase", 0f, 1f)
        animator.duration = duration
        animator.addUpdateListener(this)
        animator.addListener(this)
    }
}