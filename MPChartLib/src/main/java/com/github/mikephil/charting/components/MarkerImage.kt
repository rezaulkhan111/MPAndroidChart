package com.github.mikephil.charting.components

import android.content.Context
import android.graphics.Canvas
import android.graphics.Rect
import android.graphics.drawable.Drawable
import android.os.Build
import com.github.mikephil.charting.charts.Chart
import com.github.mikephil.charting.data.Entry
import com.github.mikephil.charting.highlight.Highlight
import com.github.mikephil.charting.utils.FSize
import com.github.mikephil.charting.utils.MPPointF
import java.lang.ref.WeakReference

/**
 * View that can be displayed when selecting values in the chart. Extend this class to provide custom layouts for your
 * markers.
 *
 * @author Philipp Jahoda
 */
open class MarkerImage : IMarker {
    private var mContext: Context
    private var mDrawable: Drawable

    private var mOffset: MPPointF = MPPointF()
    private val mOffset2 = MPPointF()
    private lateinit var mWeakChart: WeakReference<Chart<*>>

    private var mSize: FSize = FSize()
    private val mDrawableBoundsCache = Rect()

    /**
     * Constructor. Sets up the MarkerView with a custom layout resource.
     *
     * @param context
     * @param drawableResourceId the drawable resource to render
     */
    constructor(context: Context, drawableResourceId: Int) {
        mContext = context
        mDrawable = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            mContext.resources.getDrawable(drawableResourceId, null)
        } else {
            mContext.resources.getDrawable(drawableResourceId)
        }
    }

    fun setOffset(offset: MPPointF) {
        mOffset = offset
        if (mOffset == null) {
            mOffset = MPPointF()
        }
    }

    fun setOffset(offsetX: Float, offsetY: Float) {
        mOffset.x = offsetX
        mOffset.y = offsetY
    }

    override val offset: MPPointF = mOffset

    fun setSize(size: FSize) {
        mSize = size
        if (mSize == null) {
            mSize = FSize()
        }
    }

    fun getSize(): FSize {
        return mSize
    }

    fun setChartView(chart: Chart<*>) {
        mWeakChart = WeakReference(chart)
    }

    private fun getChartView(): Chart<*>? {
        return if (mWeakChart == null) null else mWeakChart.get()
    }

    override fun getOffsetForDrawingAtPoint(posX: Float, posY: Float): MPPointF {
        val offset = offset
        mOffset2.x = offset.x
        mOffset2.y = offset.y
        val chart = getChartView()
        var width = mSize!!.width
        var height = mSize!!.height
        if (width == 0f && mDrawable != null) {
            width = mDrawable.intrinsicWidth.toFloat()
        }
        if (height == 0f && mDrawable != null) {
            height = mDrawable.intrinsicHeight.toFloat()
        }
        if (posX + mOffset2.x < 0) {
            mOffset2.x = -posX
        } else if (chart != null && posX + width + mOffset2.x > chart.getWidth()) {
            mOffset2.x = chart.getWidth() - posX - width
        }
        if (posY + mOffset2.y < 0) {
            mOffset2.y = -posY
        } else if (chart != null && posY + height + mOffset2.y > chart.getHeight()) {
            mOffset2.y = chart.getHeight() - posY - height
        }
        return mOffset2
    }

    override fun refreshContent(e: Entry, highlight: Highlight) {

    }

    override fun draw(canvas: Canvas, posX: Float, posY: Float) {
        if (mDrawable == null) return
        val offset = getOffsetForDrawingAtPoint(posX, posY)
        var width = mSize!!.width
        var height = mSize!!.height
        if (width == 0f) {
            width = mDrawable.intrinsicWidth.toFloat()
        }
        if (height == 0f) {
            height = mDrawable.intrinsicHeight.toFloat()
        }
        mDrawable.copyBounds(mDrawableBoundsCache)
        mDrawable.setBounds(
            mDrawableBoundsCache.left,
            mDrawableBoundsCache.top,
            mDrawableBoundsCache.left + width.toInt(),
            mDrawableBoundsCache.top + height.toInt()
        )
        val saveId = canvas.save()
        // translate to the correct position and draw
        canvas.translate(posX + offset.x, posY + offset.y)
        mDrawable.draw(canvas)
        canvas.restoreToCount(saveId)
        mDrawable.bounds = mDrawableBoundsCache
    }
}