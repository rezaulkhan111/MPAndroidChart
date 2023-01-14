package com.github.mikephil.charting.utils

import android.graphics.Matrix
import android.graphics.Path
import android.graphics.RectF
import com.github.mikephil.charting.data.Entry
import com.github.mikephil.charting.interfaces.datasets.IBubbleDataSet
import com.github.mikephil.charting.interfaces.datasets.ICandleDataSet
import com.github.mikephil.charting.interfaces.datasets.ILineDataSet
import com.github.mikephil.charting.interfaces.datasets.IScatterDataSet
import com.github.mikephil.charting.utils.MPPointD.Companion.getInstance

/**
 * Transformer class that contains all matrices and is responsible for
 * transforming values into pixels on the screen and backwards.
 *
 * @author Philipp Jahoda
 */
open class Transformer {
    /**
     * matrix to map the values to the screen pixels
     */
    protected var mMatrixValueToPx = Matrix()

    /**
     * matrix for handling the different offsets of the chart
     */
    protected var mMatrixOffset = Matrix()

    protected var mViewPortHandler: ViewPortHandler? = null

    constructor(viewPortHandler: ViewPortHandler) {
        mViewPortHandler = viewPortHandler
    }

    /**
     * Prepares the matrix that transforms values to pixels. Calculates the
     * scale factors from the charts size and offsets.
     *
     * @param xChartMin
     * @param deltaX
     * @param deltaY
     * @param yChartMin
     */
    open fun prepareMatrixValuePx(
        xChartMin: Float,
        deltaX: Float,
        deltaY: Float,
        yChartMin: Float
    ) {
        var scaleX = (mViewPortHandler!!.contentWidth() / deltaX)
        var scaleY = (mViewPortHandler!!.contentHeight() / deltaY)
        if (java.lang.Float.isInfinite(scaleX)) {
            scaleX = 0f
        }
        if (java.lang.Float.isInfinite(scaleY)) {
            scaleY = 0f
        }

        // setup all matrices
        mMatrixValueToPx.reset()
        mMatrixValueToPx.postTranslate(-xChartMin, -yChartMin)
        mMatrixValueToPx.postScale(scaleX, -scaleY)
    }

    /**
     * Prepares the matrix that contains all offsets.
     *
     * @param inverted
     */
    open fun prepareMatrixOffset(inverted: Boolean) {
        mMatrixOffset.reset()

        // offset.postTranslate(mOffsetLeft, getHeight() - mOffsetBottom);
        if (!inverted) mMatrixOffset.postTranslate(
            mViewPortHandler!!.offsetLeft(),
            mViewPortHandler!!.getChartHeight() - mViewPortHandler!!.offsetBottom()
        ) else {
            mMatrixOffset
                .setTranslate(mViewPortHandler!!.offsetLeft(), -mViewPortHandler!!.offsetTop())
            mMatrixOffset.postScale(1.0f, -1.0f)
        }
    }

    private var valuePointsForGenerateTransformedValuesScatter = FloatArray(1)

    /**
     * Transforms an List of Entry into a float array containing the x and
     * y values transformed with all matrices for the SCATTERCHART.
     *
     * @param data
     * @return
     */
    open fun generateTransformedValuesScatter(
        data: IScatterDataSet, phaseX: Float,
        phaseY: Float, from: Int, to: Int
    ): FloatArray {
        val count = ((to - from) * phaseX + 1).toInt() * 2
        if (valuePointsForGenerateTransformedValuesScatter.size != count) {
            valuePointsForGenerateTransformedValuesScatter = FloatArray(count)
        }
        val valuePoints = valuePointsForGenerateTransformedValuesScatter
        var j = 0
        while (j < count) {
            val e = data.getEntryForIndex(j / 2 + from)
            if (e != null) {
                valuePoints[j] = e.getX()
                valuePoints[j + 1] = e.getY() * phaseY
            } else {
                valuePoints[j] = 0f
                valuePoints[j + 1] = 0f
            }
            j += 2
        }
        getValueToPixelMatrix().mapPoints(valuePoints)
        return valuePoints
    }

    private var valuePointsForGenerateTransformedValuesBubble = FloatArray(1)

    /**
     * Transforms an List of Entry into a float array containing the x and
     * y values transformed with all matrices for the BUBBLECHART.
     *
     * @param data
     * @return
     */
    open fun generateTransformedValuesBubble(
        data: IBubbleDataSet, phaseY: Float,
        from: Int, to: Int
    ): FloatArray {
        val count = (to - from + 1) * 2 // (int) Math.ceil((to - from) * phaseX) * 2;
        if (valuePointsForGenerateTransformedValuesBubble.size != count) {
            valuePointsForGenerateTransformedValuesBubble = FloatArray(count)
        }
        val valuePoints = valuePointsForGenerateTransformedValuesBubble
        var j = 0
        while (j < count) {
            val e: Entry? = data.getEntryForIndex(j / 2 + from)
            if (e != null) {
                valuePoints[j] = e.getX()
                valuePoints[j + 1] = e.getY() * phaseY
            } else {
                valuePoints[j] = 0f
                valuePoints[j + 1] = 0f
            }
            j += 2
        }
        getValueToPixelMatrix().mapPoints(valuePoints)
        return valuePoints
    }

    private var valuePointsForGenerateTransformedValuesLine = FloatArray(1)

    /**
     * Transforms an List of Entry into a float array containing the x and
     * y values transformed with all matrices for the LINECHART.
     *
     * @param data
     * @return
     */
    open fun generateTransformedValuesLine(
        data: ILineDataSet,
        phaseX: Float, phaseY: Float,
        min: Int, max: Int
    ): FloatArray? {
        val count = (((max - min) * phaseX).toInt() + 1) * 2
        if (valuePointsForGenerateTransformedValuesLine.size != count) {
            valuePointsForGenerateTransformedValuesLine = FloatArray(count)
        }
        val valuePoints = valuePointsForGenerateTransformedValuesLine
        var j = 0
        while (j < count) {
            val e = data.getEntryForIndex(j / 2 + min)
            if (e != null) {
                valuePoints[j] = e.getX()
                valuePoints[j + 1] = e.getY() * phaseY
            } else {
                valuePoints[j] = 0f
                valuePoints[j + 1] = 0f
            }
            j += 2
        }
        getValueToPixelMatrix().mapPoints(valuePoints)
        return valuePoints
    }

    private var valuePointsForGenerateTransformedValuesCandle = FloatArray(1)

    /**
     * Transforms an List of Entry into a float array containing the x and
     * y values transformed with all matrices for the CANDLESTICKCHART.
     *
     * @param data
     * @return
     */
    open fun generateTransformedValuesCandle(
        data: ICandleDataSet,
        phaseX: Float, phaseY: Float, from: Int, to: Int
    ): FloatArray? {
        val count = ((to - from) * phaseX + 1).toInt() * 2
        if (valuePointsForGenerateTransformedValuesCandle.size != count) {
            valuePointsForGenerateTransformedValuesCandle = FloatArray(count)
        }
        val valuePoints = valuePointsForGenerateTransformedValuesCandle
        var j = 0
        while (j < count) {
            val e = data.getEntryForIndex(j / 2 + from)
            if (e != null) {
                valuePoints[j] = e.getX()
                valuePoints[j + 1] = e.getHigh() * phaseY
            } else {
                valuePoints[j] = 0f
                valuePoints[j + 1] = 0f
            }
            j += 2
        }
        getValueToPixelMatrix().mapPoints(valuePoints)
        return valuePoints
    }

    /**
     * transform a path with all the given matrices VERY IMPORTANT: keep order
     * to value-touch-offset
     *
     * @param path
     */
    open fun pathValueToPixel(path: Path) {
        path.transform(mMatrixValueToPx)
        path.transform(mViewPortHandler!!.getMatrixTouch()!!)
        path.transform(mMatrixOffset)
    }

    /**
     * Transforms multiple paths will all matrices.
     *
     * @param paths
     */
    open fun pathValuesToPixel(paths: List<Path>) {
        for (i in paths.indices) {
            pathValueToPixel(paths[i])
        }
    }

    /**
     * Transform an array of points with all matrices. VERY IMPORTANT: Keep
     * matrix order "value-touch-offset" when transforming.
     *
     * @param pts
     */
    open fun pointValuesToPixel(pts: FloatArray?) {
        mMatrixValueToPx.mapPoints(pts)
        mViewPortHandler!!.getMatrixTouch()?.mapPoints(pts)
        mMatrixOffset.mapPoints(pts)
    }

    /**
     * Transform a rectangle with all matrices.
     *
     * @param r
     */
    open fun rectValueToPixel(r: RectF?) {
        mMatrixValueToPx.mapRect(r)
        mViewPortHandler!!.getMatrixTouch()?.mapRect(r)
        mMatrixOffset.mapRect(r)
    }

    /**
     * Transform a rectangle with all matrices with potential animation phases.
     *
     * @param r
     * @param phaseY
     */
    open fun rectToPixelPhase(r: RectF, phaseY: Float) {

        // multiply the height of the rect with the phase
        r.top *= phaseY
        r.bottom *= phaseY
        mMatrixValueToPx.mapRect(r)
        mViewPortHandler!!.getMatrixTouch()?.mapRect(r)
        mMatrixOffset.mapRect(r)
    }

    open fun rectToPixelPhaseHorizontal(r: RectF, phaseY: Float) {

        // multiply the height of the rect with the phase
        r.left *= phaseY
        r.right *= phaseY
        mMatrixValueToPx.mapRect(r)
        mViewPortHandler!!.getMatrixTouch()?.mapRect(r)
        mMatrixOffset.mapRect(r)
    }

    /**
     * Transform a rectangle with all matrices with potential animation phases.
     *
     * @param r
     */
    open fun rectValueToPixelHorizontal(r: RectF?) {
        mMatrixValueToPx.mapRect(r)
        mViewPortHandler!!.getMatrixTouch()?.mapRect(r)
        mMatrixOffset.mapRect(r)
    }

    /**
     * Transform a rectangle with all matrices with potential animation phases.
     *
     * @param r
     * @param phaseY
     */
    open fun rectValueToPixelHorizontal(r: RectF, phaseY: Float) {
        // multiply the height of the rect with the phase
        r.left *= phaseY
        r.right *= phaseY
        mMatrixValueToPx.mapRect(r)
        mViewPortHandler!!.getMatrixTouch()?.mapRect(r)
        mMatrixOffset.mapRect(r)
    }

    /**
     * transforms multiple rects with all matrices
     *
     * @param rects
     */
    open fun rectValuesToPixel(rects: List<RectF?>) {
        val m = getValueToPixelMatrix()
        for (i in rects.indices) m.mapRect(rects[i])
    }

    private var mPixelToValueMatrixBuffer = Matrix()

    /**
     * Transforms the given array of touch positions (pixels) (x, y, x, y, ...)
     * into values on the chart.
     *
     * @param pixels
     */
    open fun pixelsToValue(pixels: FloatArray?) {
        val tmp = mPixelToValueMatrixBuffer
        tmp.reset()

        // invert all matrixes to convert back to the original value
        mMatrixOffset.invert(tmp)
        tmp.mapPoints(pixels)
        mViewPortHandler!!.getMatrixTouch()?.invert(tmp)
        tmp.mapPoints(pixels)
        mMatrixValueToPx.invert(tmp)
        tmp.mapPoints(pixels)
    }

    /**
     * buffer for performance
     */
    var ptsBuffer = FloatArray(2)

    /**
     * Returns a recyclable MPPointD instance.
     * returns the x and y values in the chart at the given touch point
     * (encapsulated in a MPPointD). This method transforms pixel coordinates to
     * coordinates / values in the chart. This is the opposite method to
     * getPixelForValues(...).
     *
     * @param x
     * @param y
     * @return
     */
    open fun getValuesByTouchPoint(x: Float, y: Float): MPPointD? {
        val result = getInstance(0.0, 0.0)
        getValuesByTouchPoint(x, y, result)
        return result
    }

    open fun getValuesByTouchPoint(x: Float, y: Float, outputPoint: MPPointD) {
        ptsBuffer[0] = x
        ptsBuffer[1] = y
        pixelsToValue(ptsBuffer)
        outputPoint.x = ptsBuffer[0].toDouble()
        outputPoint.y = ptsBuffer[1].toDouble()
    }

    /**
     * Returns a recyclable MPPointD instance.
     * Returns the x and y coordinates (pixels) for a given x and y value in the chart.
     *
     * @param x
     * @param y
     * @return
     */
    open fun getPixelForValues(x: Float, y: Float): MPPointD? {
        ptsBuffer[0] = x
        ptsBuffer[1] = y
        pointValuesToPixel(ptsBuffer)
        val xPx = ptsBuffer[0].toDouble()
        val yPx = ptsBuffer[1].toDouble()
        return getInstance(xPx, yPx)
    }

    open fun getValueMatrix(): Matrix? {
        return mMatrixValueToPx
    }

    open fun getOffsetMatrix(): Matrix? {
        return mMatrixOffset
    }

    private val mMBuffer1 = Matrix()

    open fun getValueToPixelMatrix(): Matrix {
        mMBuffer1.set(mMatrixValueToPx)
        mMBuffer1.postConcat(mViewPortHandler?.getMatrixTouch())
        mMBuffer1.postConcat(mMatrixOffset)
        return mMBuffer1
    }

    private val mMBuffer2 = Matrix()

    open fun getPixelToValueMatrix(): Matrix? {
        getValueToPixelMatrix().invert(mMBuffer2)
        return mMBuffer2
    }
}