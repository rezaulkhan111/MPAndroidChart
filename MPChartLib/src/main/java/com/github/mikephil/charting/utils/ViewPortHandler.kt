package com.github.mikephil.charting.utils

import android.graphics.Matrix
import android.graphics.RectF
import android.view.View
import com.github.mikephil.charting.utils.Utils.convertDpToPixel

/**
 * Class that contains information about the charts current viewport settings, including offsets, scale & translation
 * levels, ...
 *
 * @author Philipp Jahoda
 */
open class ViewPortHandler
/**
 * Constructor - don't forget calling setChartDimens(...)
 */
{
    /**
     * Returns the charts-touch matrix used for translation and scale on touch.
     *
     * @return
     */
    /**
     * matrix used for touch events
     */
    val matrixTouch = Matrix()

    /**
     * this rectangle defines the area in which graph values can be drawn
     */
    var contentRect = RectF()
        protected set
    var chartWidth = 0f
        protected set
    var chartHeight = 0f
        protected set

    /**
     * minimum scale value on the y-axis
     */
    var minScaleY = 1f
        private set

    /**
     * maximum scale value on the y-axis
     */
    var maxScaleY = Float.MAX_VALUE
        private set

    /**
     * minimum scale value on the x-axis
     */
    var minScaleX = 1f
        private set

    /**
     * maximum scale value on the x-axis
     */
    var maxScaleX = Float.MAX_VALUE
        private set
    /**
     * returns the current x-scale factor
     */
    /**
     * contains the current scale factor of the x-axis
     */
    var scaleX = 1f
        private set
    /**
     * returns the current y-scale factor
     */
    /**
     * contains the current scale factor of the y-axis
     */
    var scaleY = 1f
        private set
    /**
     * Returns the translation (drag / pan) distance on the x-axis
     *
     * @return
     */
    /**
     * current translation (drag distance) on the x-axis
     */
    var transX = 0f
        private set
    /**
     * Returns the translation (drag / pan) distance on the y-axis
     *
     * @return
     */
    /**
     * current translation (drag distance) on the y-axis
     */
    var transY = 0f
        private set

    /**
     * offset that allows the chart to be dragged over its bounds on the x-axis
     */
    private var mTransOffsetX = 0f

    /**
     * offset that allows the chart to be dragged over its bounds on the x-axis
     */
    private var mTransOffsetY = 0f

    /**
     * Sets the width and height of the chart.
     *
     * @param width
     * @param height
     */
    fun setChartDimens(width: Float, height: Float) {
        val offsetLeft = offsetLeft()
        val offsetTop = offsetTop()
        val offsetRight = offsetRight()
        val offsetBottom = offsetBottom()
        chartHeight = height
        chartWidth = width
        restrainViewPort(offsetLeft, offsetTop, offsetRight, offsetBottom)
    }

    fun hasChartDimens(): Boolean {
        return if (chartHeight > 0 && chartWidth > 0) true else false
    }

    fun restrainViewPort(
        offsetLeft: Float, offsetTop: Float, offsetRight: Float,
        offsetBottom: Float
    ) {
        contentRect[offsetLeft, offsetTop, chartWidth - offsetRight] = (chartHeight
                - offsetBottom)
    }

    fun offsetLeft(): Float {
        return contentRect.left
    }

    fun offsetRight(): Float {
        return chartWidth - contentRect.right
    }

    fun offsetTop(): Float {
        return contentRect.top
    }

    fun offsetBottom(): Float {
        return chartHeight - contentRect.bottom
    }

    fun contentTop(): Float {
        return contentRect.top
    }

    fun contentLeft(): Float {
        return contentRect.left
    }

    fun contentRight(): Float {
        return contentRect.right
    }

    fun contentBottom(): Float {
        return contentRect.bottom
    }

    fun contentWidth(): Float {
        return contentRect.width()
    }

    fun contentHeight(): Float {
        return contentRect.height()
    }

    /**
     * Returns the smallest extension of the content rect (width or height).
     *
     * @return
     */
    val smallestContentExtension: Float
        get() = Math.min(contentRect.width(), contentRect.height())
    /**
     * ################ ################ ################ ################
     */
    /** CODE BELOW THIS RELATED TO SCALING AND GESTURES  */
    /**
     * Zooms in by 1.4f, x and y are the coordinates (in pixels) of the zoom
     * center.
     *
     * @param x
     * @param y
     */
    fun zoomIn(x: Float, y: Float): Matrix {
        val save = Matrix()
        zoomIn(x, y, save)
        return save
    }

    fun zoomIn(x: Float, y: Float, outputMatrix: Matrix) {
        outputMatrix.reset()
        outputMatrix.set(matrixTouch)
        outputMatrix.postScale(1.4f, 1.4f, x, y)
    }

    /**
     * Zooms out by 0.7f, x and y are the coordinates (in pixels) of the zoom
     * center.
     */
    fun zoomOut(x: Float, y: Float): Matrix {
        val save = Matrix()
        zoomOut(x, y, save)
        return save
    }

    fun zoomOut(x: Float, y: Float, outputMatrix: Matrix) {
        outputMatrix.reset()
        outputMatrix.set(matrixTouch)
        outputMatrix.postScale(0.7f, 0.7f, x, y)
    }

    /**
     * Zooms out to original size.
     * @param outputMatrix
     */
    fun resetZoom(outputMatrix: Matrix) {
        outputMatrix.reset()
        outputMatrix.set(matrixTouch)
        outputMatrix.postScale(1.0f, 1.0f, 0.0f, 0.0f)
    }

    /**
     * Post-scales by the specified scale factors.
     *
     * @param scaleX
     * @param scaleY
     * @return
     */
    fun zoom(scaleX: Float, scaleY: Float): Matrix {
        val save = Matrix()
        zoom(scaleX, scaleY, save)
        return save
    }

    fun zoom(scaleX: Float, scaleY: Float, outputMatrix: Matrix) {
        outputMatrix.reset()
        outputMatrix.set(matrixTouch)
        outputMatrix.postScale(scaleX, scaleY)
    }

    /**
     * Post-scales by the specified scale factors. x and y is pivot.
     *
     * @param scaleX
     * @param scaleY
     * @param x
     * @param y
     * @return
     */
    fun zoom(scaleX: Float, scaleY: Float, x: Float, y: Float): Matrix {
        val save = Matrix()
        zoom(scaleX, scaleY, x, y, save)
        return save
    }

    fun zoom(scaleX: Float, scaleY: Float, x: Float, y: Float, outputMatrix: Matrix) {
        outputMatrix.reset()
        outputMatrix.set(matrixTouch)
        outputMatrix.postScale(scaleX, scaleY, x, y)
    }

    /**
     * Sets the scale factor to the specified values.
     *
     * @param scaleX
     * @param scaleY
     * @return
     */
    fun setZoom(scaleX: Float, scaleY: Float): Matrix {
        val save = Matrix()
        setZoom(scaleX, scaleY, save)
        return save
    }

    fun setZoom(scaleX: Float, scaleY: Float, outputMatrix: Matrix) {
        outputMatrix.reset()
        outputMatrix.set(matrixTouch)
        outputMatrix.setScale(scaleX, scaleY)
    }

    /**
     * Sets the scale factor to the specified values. x and y is pivot.
     *
     * @param scaleX
     * @param scaleY
     * @param x
     * @param y
     * @return
     */
    fun setZoom(scaleX: Float, scaleY: Float, x: Float, y: Float): Matrix {
        val save = Matrix()
        save.set(matrixTouch)
        save.setScale(scaleX, scaleY, x, y)
        return save
    }

    protected var valsBufferForFitScreen = FloatArray(9)

    /**
     * Resets all zooming and dragging and makes the chart fit exactly it's
     * bounds.
     */
    fun fitScreen(): Matrix {
        val save = Matrix()
        fitScreen(save)
        return save
    }

    /**
     * Resets all zooming and dragging and makes the chart fit exactly it's
     * bounds.  Output Matrix is available for those who wish to cache the object.
     */
    fun fitScreen(outputMatrix: Matrix) {
        minScaleX = 1f
        minScaleY = 1f
        outputMatrix.set(matrixTouch)
        val vals = valsBufferForFitScreen
        for (i in 0..8) {
            vals[i] = 0F
        }
        outputMatrix.getValues(vals)

        // reset all translations and scaling
        vals[Matrix.MTRANS_X] = 0f
        vals[Matrix.MTRANS_Y] = 0f
        vals[Matrix.MSCALE_X] = 1f
        vals[Matrix.MSCALE_Y] = 1f
        outputMatrix.setValues(vals)
    }

    /**
     * Post-translates to the specified points.  Less Performant.
     *
     * @param transformedPts
     * @return
     */
    fun translate(transformedPts: FloatArray): Matrix {
        val save = Matrix()
        translate(transformedPts, save)
        return save
    }

    /**
     * Post-translates to the specified points.  Output matrix allows for caching objects.
     *
     * @param transformedPts
     * @return
     */
    fun translate(transformedPts: FloatArray, outputMatrix: Matrix) {
        outputMatrix.reset()
        outputMatrix.set(matrixTouch)
        val x = transformedPts[0] - offsetLeft()
        val y = transformedPts[1] - offsetTop()
        outputMatrix.postTranslate(-x, -y)
    }

    protected var mCenterViewPortMatrixBuffer = Matrix()

    /**
     * Centers the viewport around the specified position (x-index and y-value)
     * in the chart. Centering the viewport outside the bounds of the chart is
     * not possible. Makes most sense in combination with the
     * setScaleMinima(...) method.
     *
     * @param transformedPts the position to center view viewport to
     * @param view
     * @return save
     */
    fun centerViewPort(transformedPts: FloatArray, view: View) {
        val save = mCenterViewPortMatrixBuffer
        save.reset()
        save.set(matrixTouch)
        val x = transformedPts[0] - offsetLeft()
        val y = transformedPts[1] - offsetTop()
        save.postTranslate(-x, -y)
        refresh(save, view, true)
    }

    /**
     * buffer for storing the 9 matrix values of a 3x3 matrix
     */
    protected val matrixBuffer = FloatArray(9)

    /**
     * call this method to refresh the graph with a given matrix
     *
     * @param newMatrix
     * @return
     */
    fun refresh(newMatrix: Matrix, chart: View, invalidate: Boolean): Matrix {
        matrixTouch.set(newMatrix)

        // make sure scale and translation are within their bounds
        limitTransAndScale(matrixTouch, contentRect)
        if (invalidate) chart.invalidate()
        newMatrix.set(matrixTouch)
        return newMatrix
    }

    /**
     * limits the maximum scale and X translation of the given matrix
     *
     * @param matrix
     */
    fun limitTransAndScale(matrix: Matrix, content: RectF?) {
        matrix.getValues(matrixBuffer)
        val curTransX = matrixBuffer[Matrix.MTRANS_X]
        val curScaleX = matrixBuffer[Matrix.MSCALE_X]
        val curTransY = matrixBuffer[Matrix.MTRANS_Y]
        val curScaleY = matrixBuffer[Matrix.MSCALE_Y]

        // min scale-x is 1f
        scaleX = Math.min(Math.max(minScaleX, curScaleX), maxScaleX)

        // min scale-y is 1f
        scaleY = Math.min(Math.max(minScaleY, curScaleY), maxScaleY)
        var width = 0f
        var height = 0f
        if (content != null) {
            width = content.width()
            height = content.height()
        }
        val maxTransX = -width * (scaleX - 1f)
        transX = Math.min(Math.max(curTransX, maxTransX - mTransOffsetX), mTransOffsetX)
        val maxTransY = height * (scaleY - 1f)
        transY = Math.max(Math.min(curTransY, maxTransY + mTransOffsetY), -mTransOffsetY)
        matrixBuffer[Matrix.MTRANS_X] = transX
        matrixBuffer[Matrix.MSCALE_X] = scaleX
        matrixBuffer[Matrix.MTRANS_Y] = transY
        matrixBuffer[Matrix.MSCALE_Y] = scaleY
        matrix.setValues(matrixBuffer)
    }

    /**
     * Sets the minimum scale factor for the x-axis
     *
     * @param xScale
     */
    fun setMinimumScaleX(xScale: Float) {
        var xScale = xScale
        if (xScale < 1f) xScale = 1f
        minScaleX = xScale
        limitTransAndScale(matrixTouch, contentRect)
    }

    /**
     * Sets the maximum scale factor for the x-axis
     *
     * @param xScale
     */
    fun setMaximumScaleX(xScale: Float) {
        var xScale = xScale
        if (xScale == 0f) xScale = Float.MAX_VALUE
        maxScaleX = xScale
        limitTransAndScale(matrixTouch, contentRect)
    }

    /**
     * Sets the minimum and maximum scale factors for the x-axis
     *
     * @param minScaleX
     * @param maxScaleX
     */
    fun setMinMaxScaleX(minScaleX: Float, maxScaleX: Float) {
        var minScaleX = minScaleX
        var maxScaleX = maxScaleX
        if (minScaleX < 1f) minScaleX = 1f
        if (maxScaleX == 0f) maxScaleX = Float.MAX_VALUE
        this.minScaleX = minScaleX
        this.maxScaleX = maxScaleX
        limitTransAndScale(matrixTouch, contentRect)
    }

    /**
     * Sets the minimum scale factor for the y-axis
     *
     * @param yScale
     */
    fun setMinimumScaleY(yScale: Float) {
        var yScale = yScale
        if (yScale < 1f) yScale = 1f
        minScaleY = yScale
        limitTransAndScale(matrixTouch, contentRect)
    }

    /**
     * Sets the maximum scale factor for the y-axis
     *
     * @param yScale
     */
    fun setMaximumScaleY(yScale: Float) {
        var yScale = yScale
        if (yScale == 0f) yScale = Float.MAX_VALUE
        maxScaleY = yScale
        limitTransAndScale(matrixTouch, contentRect)
    }

    fun setMinMaxScaleY(minScaleY: Float, maxScaleY: Float) {
        var minScaleY = minScaleY
        var maxScaleY = maxScaleY
        if (minScaleY < 1f) minScaleY = 1f
        if (maxScaleY == 0f) maxScaleY = Float.MAX_VALUE
        this.minScaleY = minScaleY
        this.maxScaleY = maxScaleY
        limitTransAndScale(matrixTouch, contentRect)
    }
    /**
     * ################ ################ ################ ################
     */
    /**
     * BELOW METHODS FOR BOUNDS CHECK
     */
    fun isInBoundsX(x: Float): Boolean {
        return isInBoundsLeft(x) && isInBoundsRight(x)
    }

    fun isInBoundsY(y: Float): Boolean {
        return isInBoundsTop(y) && isInBoundsBottom(y)
    }

    fun isInBounds(x: Float, y: Float): Boolean {
        return isInBoundsX(x) && isInBoundsY(y)
    }

    fun isInBoundsLeft(x: Float): Boolean {
        return contentRect.left <= x + 1
    }

    fun isInBoundsRight(x: Float): Boolean {
        var x = x
        x = (x * 100f).toInt().toFloat() / 100f
        return contentRect.right >= x - 1
    }

    fun isInBoundsTop(y: Float): Boolean {
        return contentRect.top <= y
    }

    fun isInBoundsBottom(y: Float): Boolean {
        var y = y
        y = (y * 100f).toInt().toFloat() / 100f
        return contentRect.bottom >= y
    }

    /**
     * if the chart is fully zoomed out, return true
     *
     * @return
     */
    val isFullyZoomedOut: Boolean
        get() = isFullyZoomedOutX && isFullyZoomedOutY

    /**
     * Returns true if the chart is fully zoomed out on it's y-axis (vertical).
     *
     * @return
     */
    val isFullyZoomedOutY: Boolean
        get() = !(scaleY > minScaleY || minScaleY > 1f)

    /**
     * Returns true if the chart is fully zoomed out on it's x-axis
     * (horizontal).
     *
     * @return
     */
    val isFullyZoomedOutX: Boolean
        get() = !(scaleX > minScaleX || minScaleX > 1f)

    /**
     * Set an offset in dp that allows the user to drag the chart over it's
     * bounds on the x-axis.
     *
     * @param offset
     */
    fun setDragOffsetX(offset: Float) {
        mTransOffsetX = convertDpToPixel(offset)
    }

    /**
     * Set an offset in dp that allows the user to drag the chart over it's
     * bounds on the y-axis.
     *
     * @param offset
     */
    fun setDragOffsetY(offset: Float) {
        mTransOffsetY = convertDpToPixel(offset)
    }

    /**
     * Returns true if both drag offsets (x and y) are zero or smaller.
     *
     * @return
     */
    fun hasNoDragOffset(): Boolean {
        return mTransOffsetX <= 0 && mTransOffsetY <= 0
    }

    /**
     * Returns true if the chart is not yet fully zoomed out on the x-axis
     *
     * @return
     */
    fun canZoomOutMoreX(): Boolean {
        return scaleX > minScaleX
    }

    /**
     * Returns true if the chart is not yet fully zoomed in on the x-axis
     *
     * @return
     */
    fun canZoomInMoreX(): Boolean {
        return scaleX < maxScaleX
    }

    /**
     * Returns true if the chart is not yet fully zoomed out on the y-axis
     *
     * @return
     */
    fun canZoomOutMoreY(): Boolean {
        return scaleY > minScaleY
    }

    /**
     * Returns true if the chart is not yet fully zoomed in on the y-axis
     *
     * @return
     */
    fun canZoomInMoreY(): Boolean {
        return scaleY < maxScaleY
    }
}