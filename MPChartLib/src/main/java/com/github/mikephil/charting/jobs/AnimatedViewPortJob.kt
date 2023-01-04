package com.github.mikephil.charting.jobs

import android.animation.Animator
import android.animation.ObjectAnimator
import android.animation.ValueAnimator
import android.animation.ValueAnimator.AnimatorUpdateListener
import android.annotation.SuppressLint
import android.view.View
import com.github.mikephil.charting.utils.Transformer
import com.github.mikephil.charting.utils.ViewPortHandler

/**
 * Created by Philipp Jahoda on 19/02/16.
 */
@SuppressLint("NewApi")
abstract class AnimatedViewPortJob : ViewPortJob, AnimatorUpdateListener,
    Animator.AnimatorListener {

    protected var animator: ObjectAnimator? = null
    protected var phase = 0f
    protected var xOrigin = 0f
    protected var yOrigin = 0f

    constructor(
        viewPortHandler: ViewPortHandler?,
        xValue: Float,
        yValue: Float,
        trans: Transformer?,
        v: View?,
        xOrigin: Float,
        yOrigin: Float,
        duration: Long
    ) : super(viewPortHandler, xValue, yValue, trans, v) {
        this.xOrigin = xOrigin
        this.yOrigin = yOrigin
        animator = ObjectAnimator.ofFloat(this, "phase", 0f, 1f)
        animator?.duration = duration
        animator?.addUpdateListener(this)
        animator?.addListener(this)
    }

    @SuppressLint("NewApi")
    override fun run() {
        animator!!.start()
    }

    @JvmName("getPhase1")
    fun getPhase(): Float {
        return phase
    }

    @JvmName("setPhase1")
    fun setPhase(phase: Float) {
        this.phase = phase
    }

    @JvmName("getXOrigin1")
    fun getXOrigin(): Float {
        return xOrigin
    }

    @JvmName("getYOrigin1")
    fun getYOrigin(): Float {
        return yOrigin
    }

    abstract fun recycleSelf()

    protected open fun resetAnimator() {
        animator!!.removeAllListeners()
        animator!!.removeAllUpdateListeners()
        animator!!.reverse()
        animator!!.addUpdateListener(this)
        animator!!.addListener(this)
    }

    override fun onAnimationStart(animation: Animator?) {}

    override fun onAnimationEnd(animation: Animator?) {
        try {
            recycleSelf()
        } catch (e: IllegalArgumentException) {
            // don't worry about it.
        }
    }

    override fun onAnimationCancel(animation: Animator?) {
        try {
            recycleSelf()
        } catch (e: IllegalArgumentException) {
            // don't worry about it.
        }
    }

    override fun onAnimationRepeat(animation: Animator?) {}

    override fun onAnimationUpdate(animation: ValueAnimator?) {}
}