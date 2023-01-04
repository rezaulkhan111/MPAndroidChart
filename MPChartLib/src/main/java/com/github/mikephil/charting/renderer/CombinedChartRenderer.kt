package com.github.mikephil.charting.renderer

import android.graphics.Canvas
import com.github.mikephil.charting.animation.ChartAnimator
import com.github.mikephil.charting.charts.Chart
import com.github.mikephil.charting.charts.CombinedChart
import com.github.mikephil.charting.charts.CombinedChart.DrawOrder
import com.github.mikephil.charting.data.ChartData
import com.github.mikephil.charting.data.CombinedData
import com.github.mikephil.charting.highlight.Highlight
import com.github.mikephil.charting.utils.ViewPortHandler
import java.lang.ref.WeakReference

/**
 * Renderer class that is responsible for rendering multiple different data-types.
 */
class CombinedChartRenderer : DataRenderer {
    /**
     * all rederers for the different kinds of data this combined-renderer can draw
     */
    protected var mRenderers: MutableList<DataRenderer> = ArrayList(5)

    protected var mChart: WeakReference<Chart<*>>? = null

    constructor(
        chart: CombinedChart,
        animator: ChartAnimator,
        viewPortHandler: ViewPortHandler?
    ) : super(animator, viewPortHandler) {
        mChart = WeakReference(chart)
        createRenderers()
    }

    /**
     * Creates the renderers needed for this combined-renderer in the required order. Also takes the DrawOrder into
     * consideration.
     */
    fun createRenderers() {
        mRenderers.clear()
        val chart = mChart!!.get() as CombinedChart? ?: return
        val orders = chart.getDrawOrder()
        for (order in orders!!) {
            when (order) {
                DrawOrder.BAR -> if (chart.getBarData() != null) mRenderers.add(
                    BarChartRenderer(
                        chart,
                        mAnimator!!,
                        mViewPortHandler
                    )
                )
                DrawOrder.BUBBLE -> if (chart.getBubbleData() != null) mRenderers.add(
                    BubbleChartRenderer(chart, mAnimator!!, mViewPortHandler)
                )
                DrawOrder.LINE -> if (chart.getLineData() != null) mRenderers.add(
                    LineChartRenderer(
                        chart,
                        mAnimator,
                        mViewPortHandler
                    )
                )
                DrawOrder.CANDLE -> if (chart.getCandleData() != null) mRenderers.add(
                    CandleStickChartRenderer(chart, mAnimator!!, mViewPortHandler)
                )
                DrawOrder.SCATTER -> if (chart.getScatterData() != null) mRenderers.add(
                    ScatterChartRenderer(chart, mAnimator!!, mViewPortHandler)
                )
            }
        }
    }

    override fun initBuffers() {
        for (renderer in mRenderers) renderer.initBuffers()
    }

    override fun drawData(c: Canvas?) {
        for (renderer in mRenderers) renderer.drawData(c!!)
    }

    override fun drawValues(c: Canvas?) {
        for (renderer in mRenderers) renderer.drawValues(c!!)
    }

    override fun drawExtras(c: Canvas?) {
        for (renderer in mRenderers) renderer.drawExtras(c!!)
    }

    protected var mHighlightBuffer: MutableList<Highlight> = ArrayList()

    override fun drawHighlighted(c: Canvas?, indices: Array<Highlight>?) {
        val chart = mChart!!.get() ?: return
        for (renderer in mRenderers) {
            var data: ChartData<*>? = null
            if (renderer is BarChartRenderer) {
                data = renderer.mChart?.getBarData()
            } else if (renderer is LineChartRenderer) {
                data = renderer.mChart?.getLineData()
            } else if (renderer is CandleStickChartRenderer) {
                data = renderer.mChart?.getCandleData()
            } else if (renderer is ScatterChartRenderer) {
                data = renderer.mChart?.getScatterData()
            } else if (renderer is BubbleChartRenderer) {
                data = renderer.mChart?.getBubbleData()
            }
            val dataIndex =
                if (data == null) -1 else (chart.getData() as CombinedData?)!!.getAllData()
                    .indexOf(data)
            mHighlightBuffer.clear()
            for (h in indices!!) {
                if (h.getDataIndex() == dataIndex || h.getDataIndex() == -1) mHighlightBuffer.add(h)
            }
            renderer.drawHighlighted(c!!, mHighlightBuffer.toTypedArray())
        }
    }

    /**
     * Returns the sub-renderer object at the specified index.
     *
     * @param index
     * @return
     */
    fun getSubRenderer(index: Int): DataRenderer? {
        return if (index >= mRenderers.size || index < 0) null else mRenderers[index]
    }

    /**
     * Returns all sub-renderers.
     *
     * @return
     */
    fun getSubRenderers(): List<DataRenderer> {
        return mRenderers
    }

    fun setSubRenderers(renderers: MutableList<DataRenderer>) {
        mRenderers = renderers
    }
}