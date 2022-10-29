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
class MarkerImage(private val mContext: Context, drawableResourceId: Int) : IMarker {
    private var mDrawable: Drawable? = null
    private var mOffset: MPPointF? = MPPointF()
    private val mOffset2 = MPPointF()
    private var mWeakChart: WeakReference<Chart<*>>? = null
    private var mSize: FSize? = FSize()
    private val mDrawableBoundsCache = Rect()
    fun setOffset(offsetX: Float, offsetY: Float) {
        mOffset!!.x = offsetX
        mOffset!!.y = offsetY
    }

    override var offset: MPPointF?
        get() = mOffset
        set(offset) {
            mOffset = offset
            if (mOffset == null) {
                mOffset = MPPointF()
            }
        }
    var size: FSize?
        get() = mSize
        set(size) {
            mSize = size
            if (mSize == null) {
                mSize = FSize()
            }
        }

    fun setChartView(chart: Chart<*>) {
        mWeakChart = WeakReference(chart)
    }

    val chartView: Chart<*>?
        get() = if (mWeakChart == null) null else mWeakChart!!.get()

    override fun getOffsetForDrawingAtPoint(posX: Float, posY: Float): MPPointF {
        val offset = offset
        mOffset2.x = offset!!.x
        mOffset2.y = offset.y
        val chart = chartView
        var width = mSize!!.width
        var height = mSize!!.height
        if (width == 0f && mDrawable != null) {
            width = mDrawable.getIntrinsicWidth().toFloat()
        }
        if (height == 0f && mDrawable != null) {
            height = mDrawable.getIntrinsicHeight().toFloat()
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

    override fun refreshContent(e: Entry?, highlight: Highlight?) {}
    override fun draw(canvas: Canvas, posX: Float, posY: Float) {
        if (mDrawable == null) return
        val offset = getOffsetForDrawingAtPoint(posX, posY)
        var width = mSize!!.width
        var height = mSize!!.height
        if (width == 0f) {
            width = mDrawable.getIntrinsicWidth().toFloat()
        }
        if (height == 0f) {
            height = mDrawable.getIntrinsicHeight().toFloat()
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
        mDrawable.setBounds(mDrawableBoundsCache)
    }

    /**
     * Constructor. Sets up the MarkerView with a custom layout resource.
     *
     * @param context
     * @param drawableResourceId the drawable resource to render
     */
    init {
        mDrawable = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            mContext.resources.getDrawable(drawableResourceId, null)
        } else {
            mContext.resources.getDrawable(drawableResourceId)
        }
    }
}