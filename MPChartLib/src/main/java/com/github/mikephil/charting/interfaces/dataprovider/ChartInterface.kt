package com.github.mikephil.charting.interfaces.dataprovider

import android.graphics.RectF
import com.github.mikephil.charting.data.ChartData
import com.github.mikephil.charting.formatter.IValueFormatter
import com.github.mikephil.charting.utils.MPPointF

/**
 * Interface that provides everything there is to know about the dimensions,
 * bounds, and range of the chart.
 *
 * @author Philipp Jahoda
 */
interface ChartInterface {
    /**
     * Returns the minimum x value of the chart, regardless of zoom or translation.
     *
     * @return
     */
    fun getXChartMin(): Float

    /**
     * Returns the maximum x value of the chart, regardless of zoom or translation.
     *
     * @return
     */
    fun getXChartMax(): Float

    fun getXRange(): Float

    /**
     * Returns the minimum y value of the chart, regardless of zoom or translation.
     *
     * @return
     */
    fun getYChartMin(): Float

    /**
     * Returns the maximum y value of the chart, regardless of zoom or translation.
     *
     * @return
     */
    fun getYChartMax(): Float

    /**
     * Returns the maximum distance in scren dp a touch can be away from an entry to cause it to get highlighted.
     *
     * @return
     */
    fun getMaxHighlightDistance(): Float

    fun getWidth(): Int

    fun getHeight(): Int

    fun getCenterOfView(): MPPointF?

    fun getCenterOffsets(): MPPointF?

    fun getContentRect(): RectF?

    fun getDefaultValueFormatter(): IValueFormatter?

    fun getData(): ChartData<*>?

    fun getMaxVisibleCount(): Int
}