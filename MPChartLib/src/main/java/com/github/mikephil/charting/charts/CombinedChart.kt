package com.github.mikephil.charting.charts

import android.content.Context
import android.graphics.Canvas
import android.util.AttributeSet
import android.util.Log
import com.github.mikephil.charting.data.Entry
import com.github.mikephil.charting.highlight.Highlight
import com.github.mikephil.charting.renderer.CombinedChartRenderer

/**
 * This chart class allows the combination of lines, bars, scatter and candle
 * data all displayed in one chart area.
 *
 * @author Philipp Jahoda
 */
class CombinedChart : BarLineChartBase<CombinedData?>, CombinedDataProvider {
    /**
     * if set to true, all values are drawn above their bars, instead of below
     * their top
     */
    var isDrawValueAboveBarEnabled = true
        private set
    /**
     * @return true the highlight operation is be full-bar oriented, false if single-value
     */
    /**
     * Set this to true to make the highlight operation full-bar oriented,
     * false to make it highlight single values (relevant only for stacked).
     *
     * @param enabled
     */
    /**
     * flag that indicates whether the highlight should be full-bar oriented, or single-value?
     */
    var isHighlightFullBarEnabled = false

    /**
     * if set to true, a grey area is drawn behind each bar that indicates the
     * maximum value
     */
    var isDrawBarShadowEnabled = false
        private set
    protected var mDrawOrder: Array<DrawOrder>

    /**
     * enum that allows to specify the order in which the different data objects
     * for the combined-chart are drawn
     */
    enum class DrawOrder {
        BAR, BUBBLE, LINE, CANDLE, SCATTER
    }

    constructor(context: Context?) : super(context) {}
    constructor(context: Context?, attrs: AttributeSet?) : super(context, attrs) {}
    constructor(context: Context?, attrs: AttributeSet?, defStyle: Int) : super(
        context,
        attrs,
        defStyle
    ) {
    }

    override fun init() {
        super.init()

        // Default values are not ready here yet
        mDrawOrder = arrayOf(
            DrawOrder.BAR, DrawOrder.BUBBLE, DrawOrder.LINE, DrawOrder.CANDLE, DrawOrder.SCATTER
        )
        setHighlighter(CombinedHighlighter(this, this))

        // Old default behaviour
        isHighlightFullBarEnabled = true
        mRenderer = CombinedChartRenderer(this, mAnimator, mViewPortHandler)
    }

    val combinedData: CombinedData
        get() = mData
    override var data: T?
        get() = super.data
        set(data) {
            super.setData(data)
            setHighlighter(CombinedHighlighter(this, this))
            (mRenderer as CombinedChartRenderer).createRenderers()
            mRenderer.initBuffers()
        }

    /**
     * Returns the Highlight object (contains x-index and DataSet index) of the selected value at the given touch
     * point
     * inside the CombinedChart.
     *
     * @param x
     * @param y
     * @return
     */
    override fun getHighlightByTouchPoint(x: Float, y: Float): Highlight? {
        return if (mData == null) {
            Log.e(Chart.Companion.LOG_TAG, "Can't select by touch. No data set.")
            null
        } else {
            val h = highlighter.getHighlight(x, y)
            if (h == null || !isHighlightFullBarEnabled) h else Highlight(
                h.x, h.y,
                h.xPx, h.yPx,
                h.dataSetIndex, -1, h.axis
            )

            // For isHighlightFullBarEnabled, remove stackIndex
        }
    }

    val lineData: LineData?
        get() = if (mData == null) null else mData.getLineData()
    val barData: BarData?
        get() = if (mData == null) null else mData.getBarData()
    val scatterData: ScatterData?
        get() = if (mData == null) null else mData.getScatterData()
    val candleData: CandleData?
        get() = if (mData == null) null else mData.getCandleData()
    val bubbleData: BubbleData?
        get() = if (mData == null) null else mData.getBubbleData()

    /**
     * If set to true, all values are drawn above their bars, instead of below
     * their top.
     *
     * @param enabled
     */
    fun setDrawValueAboveBar(enabled: Boolean) {
        isDrawValueAboveBarEnabled = enabled
    }

    /**
     * If set to true, a grey area is drawn behind each bar that indicates the
     * maximum value. Enabling his will reduce performance by about 50%.
     *
     * @param enabled
     */
    fun setDrawBarShadow(enabled: Boolean) {
        isDrawBarShadowEnabled = enabled
    }
    /**
     * Returns the currently set draw order.
     *
     * @return
     */
    /**
     * Sets the order in which the provided data objects should be drawn. The
     * earlier you place them in the provided array, the further they will be in
     * the background. e.g. if you provide new DrawOrer[] { DrawOrder.BAR,
     * DrawOrder.LINE }, the bars will be drawn behind the lines.
     *
     * @param order
     */
    var drawOrder: Array<DrawOrder>?
        get() = mDrawOrder
        set(order) {
            if (order == null || order.size <= 0) return
            mDrawOrder = order
        }

    /**
     * draws all MarkerViews on the highlighted positions
     */
    override fun drawMarkers(canvas: Canvas?) {

        // if there is no marker view or drawing marker is disabled
        if (mMarker == null || !isDrawMarkersEnabled || !valuesToHighlight()) return
        for (i in mIndicesToHighlight.indices) {
            val highlight = mIndicesToHighlight[i]
            val set: IDataSet<*> = mData.getDataSetByHighlight(highlight)
            val e: Entry = mData.getEntryForHighlight(highlight)
                ?: continue
            val entryIndex: Int = set.getEntryIndex(e)

            // make sure entry not null
            if (entryIndex > set.entryCount * mAnimator.phaseX) continue
            val pos = getMarkerPosition(highlight)

            // check bounds
            if (!mViewPortHandler.isInBounds(pos!![0], pos[1])) continue

            // callbacks to update the content
            mMarker.refreshContent(e, highlight)

            // draw the marker
            mMarker.draw(canvas, pos[0], pos[1])
        }
    }
}