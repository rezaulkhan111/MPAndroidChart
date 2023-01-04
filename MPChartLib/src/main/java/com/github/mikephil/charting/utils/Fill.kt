package com.github.mikephil.charting.utils

import android.graphics.*
import android.graphics.drawable.Drawable

open class Fill {

    enum class Type {
        EMPTY, COLOR, LINEAR_GRADIENT, DRAWABLE
    }

    enum class Direction {
        DOWN, UP, RIGHT, LEFT
    }

    /**
     * the type of fill
     */
    private var mType = Type.EMPTY

    /**
     * the color that is used for filling
     */
    private var mColor: Int? = null

    private var mFinalColor: Int? = null

    /**
     * the drawable to be used for filling
     */
    protected var mDrawable: Drawable? = null

    private var mGradientColors: IntArray? = null

    private var mGradientPositions: FloatArray? = null

    /**
     * transparency used for filling
     */
    private var mAlpha = 255

    constructor() {}

    constructor(color: Int) {
        mType = Type.COLOR
        mColor = color
        calculateFinalColor()
    }

    constructor(startColor: Int, endColor: Int) {
        mType = Type.LINEAR_GRADIENT
        mGradientColors = intArrayOf(startColor, endColor)
    }

    constructor(gradientColors: IntArray) {
        mType = Type.LINEAR_GRADIENT
        mGradientColors = gradientColors
    }

    constructor(gradientColors: IntArray, gradientPositions: FloatArray) {
        mType = Type.LINEAR_GRADIENT
        mGradientColors = gradientColors
        mGradientPositions = gradientPositions
    }

    constructor(drawable: Drawable) {
        mType = Type.DRAWABLE
        mDrawable = drawable
    }

    open fun getType(): Type? {
        return mType
    }

    open fun setType(type: Type) {
        mType = type
    }

    open fun getColor(): Int? {
        return mColor
    }

    open fun setColor(color: Int) {
        mColor = color
        calculateFinalColor()
    }

    open fun getGradientColors(): IntArray? {
        return mGradientColors
    }

    open fun setGradientColors(colors: IntArray?) {
        mGradientColors = colors
    }

    open fun getGradientPositions(): FloatArray? {
        return mGradientPositions
    }

    open fun setGradientPositions(positions: FloatArray?) {
        mGradientPositions = positions
    }

    open fun setGradientColors(startColor: Int, endColor: Int) {
        mGradientColors = intArrayOf(startColor, endColor)
    }

    open fun getAlpha(): Int {
        return mAlpha
    }

    open fun setAlpha(alpha: Int) {
        mAlpha = alpha
        calculateFinalColor()
    }

    private fun calculateFinalColor() {
        mFinalColor = if (mColor == null) {
            null
        } else {
            val alpha = Math.floor((mColor!! shr 24) / 255.0 * (mAlpha / 255.0) * 255.0).toInt()
            alpha shl 24 or (mColor!! and 0xffffff)
        }
    }

    open fun fillRect(
        c: Canvas, paint: Paint,
        left: Float, top: Float, right: Float, bottom: Float,
        gradientDirection: Direction
    ) {
        when (mType) {
            Type.EMPTY -> return
            Type.COLOR -> {
                if (mFinalColor == null) return
                if (isClipPathSupported()) {
                    val save = c.save()
                    c.clipRect(left, top, right, bottom)
                    c.drawColor(mFinalColor!!)
                    c.restoreToCount(save)
                } else {
                    // save
                    val previous = paint.style
                    val previousColor = paint.color

                    // set
                    paint.style = Paint.Style.FILL
                    paint.color = mFinalColor!!
                    c.drawRect(left, top, right, bottom, paint)

                    // restore
                    paint.color = previousColor
                    paint.style = previous
                }
            }
            Type.LINEAR_GRADIENT -> {
                if (mGradientColors == null) return
                val gradient: LinearGradient = LinearGradient(
                    (if (gradientDirection == Direction.RIGHT) right else if (gradientDirection == Direction.LEFT) left else left).toInt()
                        .toFloat(),
                    (if (gradientDirection == Direction.UP) bottom else if (gradientDirection == Direction.DOWN) top else top).toInt()
                        .toFloat(),
                    (if (gradientDirection == Direction.RIGHT) left else if (gradientDirection == Direction.LEFT) right else left).toInt()
                        .toFloat(),
                    (if (gradientDirection == Direction.UP) top else if (gradientDirection == Direction.DOWN) bottom else top).toInt()
                        .toFloat(),
                    mGradientColors!!,
                    mGradientPositions,
                    Shader.TileMode.MIRROR
                )
                paint.shader = gradient
                c.drawRect(left, top, right, bottom, paint)
            }
            Type.DRAWABLE -> {
                if (mDrawable == null) return
                mDrawable!!.setBounds(left.toInt(), top.toInt(), right.toInt(), bottom.toInt())
                mDrawable!!.draw(c)
            }
        }
    }

    open fun fillPath(
        c: Canvas, path: Path?, paint: Paint,
        clipRect: RectF?
    ) {
        when (mType) {
            Type.EMPTY -> return
            Type.COLOR -> {
                if (mFinalColor == null) return
                if (clipRect != null && isClipPathSupported()) {
                    val save = c.save()
                    c.clipPath(path!!)
                    c.drawColor(mFinalColor!!)
                    c.restoreToCount(save)
                } else {
                    // save
                    val previous = paint.style
                    val previousColor = paint.color

                    // set
                    paint.style = Paint.Style.FILL
                    paint.color = mFinalColor!!
                    c.drawPath(path!!, paint)

                    // restore
                    paint.color = previousColor
                    paint.style = previous
                }
            }
            Type.LINEAR_GRADIENT -> {
                if (mGradientColors == null) return
                val gradient = LinearGradient(
                    0f,
                    0f,
                    c.width.toFloat(),
                    c.height.toFloat(),
                    mGradientColors!!,
                    mGradientPositions,
                    Shader.TileMode.MIRROR
                )
                paint.shader = gradient
                c.drawPath(path!!, paint)
            }
            Type.DRAWABLE -> {
                if (mDrawable == null) return
                ensureClipPathSupported()
                val save = c.save()
                c.clipPath(path!!)
                mDrawable!!.setBounds(
                    clipRect?.left?.toInt() ?: 0,
                    clipRect?.top?.toInt() ?: 0,
                    clipRect?.right?.toInt() ?: c.width,
                    clipRect?.bottom?.toInt() ?: c.height
                )
                mDrawable!!.draw(c)
                c.restoreToCount(save)
            }
        }
    }

    private fun isClipPathSupported(): Boolean {
        return Utils.getSDKInt() >= 18
    }

    private fun ensureClipPathSupported() {
        if (Utils.getSDKInt() < 18) {
            throw RuntimeException(
                "Fill-drawables not (yet) supported below API level 18, " +
                        "this code was run on API level " + Utils.getSDKInt() + "."
            )
        }
    }
}