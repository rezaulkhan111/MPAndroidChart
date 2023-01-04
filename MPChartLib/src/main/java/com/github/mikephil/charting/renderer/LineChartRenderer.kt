package com.github.mikephil.charting.renderer

import android.graphics.*
import com.github.mikephil.charting.animation.ChartAnimator
import com.github.mikephil.charting.data.*
import com.github.mikephil.charting.highlight.Highlight
import com.github.mikephil.charting.interfaces.dataprovider.LineDataProvider
import com.github.mikephil.charting.interfaces.datasets.IDataSet
import com.github.mikephil.charting.interfaces.datasets.ILineDataSet
import com.github.mikephil.charting.utils.*
import com.github.mikephil.charting.utils.MPPointF.Companion.getInstance
import com.github.mikephil.charting.utils.MPPointF.Companion.recycleInstance
import com.github.mikephil.charting.utils.Utils.convertDpToPixel
import com.github.mikephil.charting.utils.Utils.drawImage
import java.lang.ref.WeakReference

class LineChartRenderer : LineRadarRenderer {

    public var mChart: LineDataProvider? = null

    /**
     * paint for the inner circle of the value indicators
     */
    protected var mCirclePaintInner: Paint? = null

    /**
     * Bitmap object used for drawing the paths (otherwise they are too long if
     * rendered directly on the canvas)
     */
    protected var mDrawBitmap: WeakReference<Bitmap?>? = null

    /**
     * on this canvas, the paths are rendered, it is initialized with the
     * pathBitmap
     */
    protected var mBitmapCanvas: Canvas? = null

    /**
     * the bitmap configuration to be used
     */
    protected var mBitmapConfig = Bitmap.Config.ARGB_8888

    protected var cubicPath = Path()
    protected var cubicFillPath = Path()

    constructor(
        chart: LineDataProvider?, animator: ChartAnimator?,
        viewPortHandler: ViewPortHandler?
    ) : super(animator, viewPortHandler) {
        mChart = chart
        mCirclePaintInner = Paint(Paint.ANTI_ALIAS_FLAG)
        mCirclePaintInner!!.style = Paint.Style.FILL
        mCirclePaintInner!!.color = Color.WHITE
    }

    override fun initBuffers() {}

    override fun drawData(c: Canvas?) {
        val width = mViewPortHandler?.getChartWidth()?.toInt()!!
        val height = mViewPortHandler?.getChartHeight()?.toInt()!!
        var drawBitmap = if (mDrawBitmap == null) null else mDrawBitmap!!.get()
        if (drawBitmap == null || drawBitmap.width != width
            || drawBitmap.height != height
        ) {
            if (width > 0 && height > 0) {
                drawBitmap = Bitmap.createBitmap(width, height, mBitmapConfig)
                mDrawBitmap = WeakReference(drawBitmap)
                mBitmapCanvas = Canvas(drawBitmap)
            } else return
        }
        drawBitmap!!.eraseColor(Color.TRANSPARENT)
        val lineData = mChart!!.getLineData()
        for (set in lineData.getDataSets()!!) {
            if (set.isVisible()) drawDataSet(c, set)
        }
        c?.drawBitmap(drawBitmap, 0f, 0f, mRenderPaint)
    }

    protected fun drawDataSet(c: Canvas?, dataSet: ILineDataSet) {
        if (dataSet.getEntryCount() < 1) return
        mRenderPaint!!.strokeWidth = dataSet.getLineWidth()
        mRenderPaint!!.pathEffect = dataSet.getDashPathEffect()
        when (dataSet.getMode()) {
            LineDataSet.Mode.LINEAR, LineDataSet.Mode.STEPPED -> drawLinear(c, dataSet)
            LineDataSet.Mode.CUBIC_BEZIER -> drawCubicBezier(dataSet)
            LineDataSet.Mode.HORIZONTAL_BEZIER -> drawHorizontalBezier(dataSet)
            else -> drawLinear(c, dataSet)
        }
        mRenderPaint!!.pathEffect = null
    }

    protected fun drawHorizontalBezier(dataSet: ILineDataSet) {
        val phaseY = mAnimator!!.getPhaseY()
        val trans = mChart!!.getTransformer(dataSet.getAxisDependency())
        mXBounds[mChart!!] = dataSet
        cubicPath.reset()
        if (mXBounds.range >= 1) {
            var prev = dataSet.getEntryForIndex(mXBounds.min)
            var cur = prev

            // let the spline start
            cubicPath.moveTo(cur.getX(), cur.getY() * phaseY)
            for (j in mXBounds.min + 1..mXBounds.range + mXBounds.min) {
                prev = cur
                cur = dataSet.getEntryForIndex(j)
                val cpx = (prev.getX()
                        + (cur.getX() - prev.getX()) / 2.0f)
                cubicPath.cubicTo(
                    cpx, prev.getY() * phaseY,
                    cpx, cur.getY() * phaseY,
                    cur.getX(), cur.getY() * phaseY
                )
            }
        }

        // if filled is enabled, close the path
        if (dataSet.isDrawFilledEnabled()) {
            cubicFillPath.reset()
            cubicFillPath.addPath(cubicPath)
            // create a new path, this is bad for performance
            drawCubicFill(mBitmapCanvas, dataSet, cubicFillPath, trans, mXBounds)
        }
        mRenderPaint!!.color = dataSet.getColor()
        mRenderPaint!!.style = Paint.Style.STROKE
        trans.pathValueToPixel(cubicPath)
        mBitmapCanvas!!.drawPath(cubicPath, mRenderPaint!!)
        mRenderPaint!!.pathEffect = null
    }

    protected fun drawCubicBezier(dataSet: ILineDataSet) {
        val phaseY = mAnimator!!.getPhaseY()
        val trans = mChart!!.getTransformer(dataSet.getAxisDependency())
        mXBounds[mChart!!] = dataSet
        val intensity = dataSet.getCubicIntensity()
        cubicPath.reset()
        if (mXBounds.range >= 1) {
            var prevDx = 0f
            var prevDy = 0f
            var curDx = 0f
            var curDy = 0f

            // Take an extra point from the left, and an extra from the right.
            // That's because we need 4 points for a cubic bezier (cubic=4), otherwise we get lines moving and doing weird stuff on the edges of the chart.
            // So in the starting `prev` and `cur`, go -2, -1
            // And in the `lastIndex`, add +1
            val firstIndex = mXBounds.min + 1
            val lastIndex = mXBounds.min + mXBounds.range
            var prevPrev: Entry?
            var prev: Entry? = dataSet.getEntryForIndex(Math.max(firstIndex - 2, 0))
            var cur = dataSet.getEntryForIndex(Math.max(firstIndex - 1, 0))
            var next = cur
            var nextIndex = -1
            if (cur == null) return

            // let the spline start
            cubicPath.moveTo(cur.getX(), cur.getY() * phaseY)
            for (j in mXBounds.min + 1..mXBounds.range + mXBounds.min) {
                prevPrev = prev
                prev = cur
                cur = if (nextIndex == j) next else dataSet.getEntryForIndex(j)
                nextIndex = if (j + 1 < dataSet.getEntryCount()) j + 1 else j
                next = dataSet.getEntryForIndex(nextIndex)
                prevDx = (cur.getX() - prevPrev!!.getX()) * intensity
                prevDy = (cur.getY() - prevPrev.getY()) * intensity
                curDx = (next.getX() - prev!!.getX()) * intensity
                curDy = (next.getY() - prev.getY()) * intensity
                cubicPath.cubicTo(
                    prev.getX() + prevDx, (prev.getY() + prevDy) * phaseY,
                    cur.getX() - curDx,
                    (cur.getY() - curDy) * phaseY, cur.getX(), cur.getY() * phaseY
                )
            }
        }

        // if filled is enabled, close the path
        if (dataSet.isDrawFilledEnabled()) {
            cubicFillPath.reset()
            cubicFillPath.addPath(cubicPath)
            drawCubicFill(mBitmapCanvas, dataSet, cubicFillPath, trans, mXBounds)
        }
        mRenderPaint!!.color = dataSet.getColor()
        mRenderPaint!!.style = Paint.Style.STROKE
        trans.pathValueToPixel(cubicPath)
        mBitmapCanvas!!.drawPath(cubicPath, mRenderPaint!!)
        mRenderPaint!!.pathEffect = null
    }

    protected fun drawCubicFill(
        c: Canvas?,
        dataSet: ILineDataSet,
        spline: Path,
        trans: Transformer,
        bounds: XBounds
    ) {
        val fillMin = dataSet.getFillFormatter()
            .getFillLinePosition(dataSet, mChart!!)
        spline.lineTo(dataSet.getEntryForIndex(bounds.min + bounds.range).getX(), fillMin)
        spline.lineTo(dataSet.getEntryForIndex(bounds.min).getX(), fillMin)
        spline.close()
        trans.pathValueToPixel(spline)
        val drawable = dataSet.getFillDrawable()
        if (drawable != null) {
            drawFilledPath(c!!, spline, drawable)
        } else {
            drawFilledPath(c!!, spline, dataSet.getFillColor(), dataSet.getFillAlpha())
        }
    }

    private var mLineBuffer = FloatArray(4)

    /**
     * Draws a normal line.
     *
     * @param c
     * @param dataSet
     */
    protected fun drawLinear(c: Canvas?, dataSet: ILineDataSet) {
        val entryCount = dataSet.getEntryCount()
        val isDrawSteppedEnabled = dataSet.isDrawSteppedEnabled()
        val pointsPerEntryPair = if (isDrawSteppedEnabled) 4 else 2
        val trans = mChart!!.getTransformer(dataSet.getAxisDependency())
        val phaseY = mAnimator!!.getPhaseY()
        mRenderPaint!!.style = Paint.Style.STROKE
        var canvas: Canvas? = null

        // if the data-set is dashed, draw on bitmap-canvas
        canvas = if (dataSet.isDashedLineEnabled()) {
            mBitmapCanvas
        } else {
            c
        }
        mXBounds[mChart!!] = dataSet

        // if drawing filled is enabled
        if (dataSet.isDrawFilledEnabled() && entryCount > 0) {
            drawLinearFill(c, dataSet, trans, mXBounds)
        }

        // more than 1 color
        if (dataSet.getColors().size > 1) {
            val numberOfFloats = pointsPerEntryPair * 2
            if (mLineBuffer.size <= numberOfFloats) mLineBuffer = FloatArray(numberOfFloats * 2)
            val max = mXBounds.min + mXBounds.range
            for (j in mXBounds.min until max) {
                var e = dataSet.getEntryForIndex(j)
                    ?: continue
                mLineBuffer[0] = e.getX()
                mLineBuffer[1] = e.getY() * phaseY
                if (j < mXBounds.max) {
                    e = dataSet.getEntryForIndex(j + 1)
                    if (e == null) break
                    if (isDrawSteppedEnabled) {
                        mLineBuffer[2] = e.getX()
                        mLineBuffer[3] = mLineBuffer[1]
                        mLineBuffer[4] = mLineBuffer[2]
                        mLineBuffer[5] = mLineBuffer[3]
                        mLineBuffer[6] = e.getX()
                        mLineBuffer[7] = e.getY() * phaseY
                    } else {
                        mLineBuffer[2] = e.getX()
                        mLineBuffer[3] = e.getY() * phaseY
                    }
                } else {
                    mLineBuffer[2] = mLineBuffer[0]
                    mLineBuffer[3] = mLineBuffer[1]
                }

                // Determine the start and end coordinates of the line, and make sure they differ.
                val firstCoordinateX = mLineBuffer[0]
                val firstCoordinateY = mLineBuffer[1]
                val lastCoordinateX = mLineBuffer[numberOfFloats - 2]
                val lastCoordinateY = mLineBuffer[numberOfFloats - 1]
                if (firstCoordinateX == lastCoordinateX &&
                    firstCoordinateY == lastCoordinateY
                ) continue
                trans.pointValuesToPixel(mLineBuffer)
                if (!mViewPortHandler!!.isInBoundsRight(firstCoordinateX)) break

                // make sure the lines don't do shitty things outside
                // bounds
                if (!mViewPortHandler!!.isInBoundsLeft(lastCoordinateX) ||
                    !mViewPortHandler!!.isInBoundsTop(
                        Math.max(
                            firstCoordinateY,
                            lastCoordinateY
                        )
                    ) ||
                    !mViewPortHandler!!.isInBoundsBottom(
                        Math.min(
                            firstCoordinateY,
                            lastCoordinateY
                        )
                    )
                ) continue

                // get the color that is set for this line-segment
                mRenderPaint!!.color = dataSet.getColor(j)
                canvas!!.drawLines(mLineBuffer, 0, pointsPerEntryPair * 2, mRenderPaint!!)
            }
        } else { // only one color per dataset
            if (mLineBuffer.size < Math.max(
                    entryCount * pointsPerEntryPair,
                    pointsPerEntryPair
                ) * 2
            ) mLineBuffer = FloatArray(
                Math.max(
                    entryCount * pointsPerEntryPair, pointsPerEntryPair
                ) * 4
            )
            var e1: Entry
            var e2: Entry
            e1 = dataSet.getEntryForIndex(mXBounds.min)
            if (e1 != null) {
                var j = 0
                for (x in mXBounds.min..mXBounds.range + mXBounds.min) {
                    e1 = dataSet.getEntryForIndex(if (x == 0) 0 else x - 1)
                    e2 = dataSet.getEntryForIndex(x)
                    if (e1 == null || e2 == null) continue
                    mLineBuffer[j++] = e1.getX()
                    mLineBuffer[j++] = e1.getY() * phaseY
                    if (isDrawSteppedEnabled) {
                        mLineBuffer[j++] = e2.getX()
                        mLineBuffer[j++] = e1.getY() * phaseY
                        mLineBuffer[j++] = e2.getX()
                        mLineBuffer[j++] = e1.getY() * phaseY
                    }
                    mLineBuffer[j++] = e2.getX()
                    mLineBuffer[j++] = e2.getY() * phaseY
                }
                if (j > 0) {
                    trans.pointValuesToPixel(mLineBuffer)
                    val size =
                        Math.max((mXBounds.range + 1) * pointsPerEntryPair, pointsPerEntryPair) * 2
                    mRenderPaint!!.color = dataSet.getColor()
                    canvas!!.drawLines(mLineBuffer, 0, size, mRenderPaint!!)
                }
            }
        }
        mRenderPaint!!.pathEffect = null
    }

    protected var mGenerateFilledPathBuffer = Path()

    /**
     * Draws a filled linear path on the canvas.
     *
     * @param c
     * @param dataSet
     * @param trans
     * @param bounds
     */
    protected fun drawLinearFill(
        c: Canvas?,
        dataSet: ILineDataSet,
        trans: Transformer,
        bounds: XBounds
    ) {
        val filled = mGenerateFilledPathBuffer
        val startingIndex = bounds.min
        val endingIndex = bounds.range + bounds.min
        val indexInterval = 128
        var currentStartIndex = 0
        var currentEndIndex = indexInterval
        var iterations = 0

        // Doing this iteratively in order to avoid OutOfMemory errors that can happen on large bounds sets.
        do {
            currentStartIndex = startingIndex + iterations * indexInterval
            currentEndIndex = currentStartIndex + indexInterval
            currentEndIndex = if (currentEndIndex > endingIndex) endingIndex else currentEndIndex
            if (currentStartIndex <= currentEndIndex) {
                generateFilledPath(dataSet, currentStartIndex, currentEndIndex, filled)
                trans.pathValueToPixel(filled)
                val drawable = dataSet.getFillDrawable()
                if (drawable != null) {
                    drawFilledPath(c!!, filled, drawable)
                } else {
                    drawFilledPath(c!!, filled, dataSet.getFillColor(), dataSet.getFillAlpha())
                }
            }
            iterations++
        } while (currentStartIndex <= currentEndIndex)
    }

    /**
     * Generates a path that is used for filled drawing.
     *
     * @param dataSet    The dataset from which to read the entries.
     * @param startIndex The index from which to start reading the dataset
     * @param endIndex   The index from which to stop reading the dataset
     * @param outputPath The path object that will be assigned the chart data.
     * @return
     */
    private fun generateFilledPath(
        dataSet: ILineDataSet,
        startIndex: Int,
        endIndex: Int,
        outputPath: Path
    ) {
        val fillMin = dataSet.getFillFormatter().getFillLinePosition(
            dataSet,
            mChart!!
        )
        val phaseY = mAnimator!!.getPhaseY()
        val isDrawSteppedEnabled = dataSet.getMode() === LineDataSet.Mode.STEPPED
        outputPath.reset()
        val entry = dataSet.getEntryForIndex(startIndex)
        outputPath.moveTo(entry.getX(), fillMin)
        outputPath.lineTo(entry.getX(), entry.getY() * phaseY)

        // create a new path
        var currentEntry: Entry? = null
        var previousEntry = entry
        for (x in startIndex + 1..endIndex) {
            currentEntry = dataSet.getEntryForIndex(x)
            if (isDrawSteppedEnabled) {
                outputPath.lineTo(currentEntry.getX(), previousEntry.getY() * phaseY)
            }
            outputPath.lineTo(currentEntry.getX(), currentEntry.getY() * phaseY)
            previousEntry = currentEntry
        }

        // close up
        if (currentEntry != null) {
            outputPath.lineTo(currentEntry.getX(), fillMin)
        }
        outputPath.close()
    }

    override fun drawValues(c: Canvas?) {
        if (isDrawingValuesAllowed(mChart!!)) {
            val dataSets: List<ILineDataSet>? = mChart!!.getLineData().getDataSets()
            for (i in dataSets!!.indices) {
                val dataSet = dataSets[i]
                if (!shouldDrawValues(dataSet) || dataSet.getEntryCount() < 1) continue

                // apply the text-styling defined by the DataSet
                applyValueTextStyle(dataSet)
                val trans = mChart!!.getTransformer(dataSet.getAxisDependency())

                // make sure the values do not interfear with the circles
                var valOffset = (dataSet.getCircleRadius() * 1.75f).toInt()
                if (!dataSet.isDrawCirclesEnabled()) valOffset = valOffset / 2
                mXBounds[mChart!!] = dataSet
                val positions = trans.generateTransformedValuesLine(
                    dataSet, mAnimator!!.getPhaseX(), mAnimator!!
                        .getPhaseY(), mXBounds.min, mXBounds.max
                )
                val iconsOffset = getInstance(dataSet.getIconsOffset())
                iconsOffset.x = convertDpToPixel(iconsOffset.x)
                iconsOffset.y = convertDpToPixel(iconsOffset.y)
                var j = 0
                while (j < positions!!.size) {
                    val x = positions[j]
                    val y = positions[j + 1]
                    if (!mViewPortHandler!!.isInBoundsRight(x)) break
                    if (!mViewPortHandler!!.isInBoundsLeft(x) || !mViewPortHandler!!.isInBoundsY(y)) {
                        j += 2
                        continue
                    }
                    val entry = dataSet.getEntryForIndex(j / 2 + mXBounds.min)
                    if (dataSet.isDrawValuesEnabled()) {
                        drawValue(
                            c!!, dataSet.getValueFormatter(), entry.getY(), entry, i, x,
                            y - valOffset, dataSet.getValueTextColor(j / 2)
                        )
                    }
                    if (entry.getIcon() != null && dataSet.isDrawIconsEnabled()) {
                        val icon = entry.getIcon()
                        drawImage(
                            c!!,
                            icon!!, (x + iconsOffset.x).toInt(), (y + iconsOffset.y).toInt(),
                            icon.intrinsicWidth,
                            icon.intrinsicHeight
                        )
                    }
                    j += 2
                }
                recycleInstance(iconsOffset)
            }
        }
    }

    override fun drawExtras(c: Canvas?) {
        drawCircles(c!!)
    }

    /**
     * cache for the circle bitmaps of all datasets
     */
    private val mImageCaches = HashMap<IDataSet<*>, DataSetImageCache>()

    /**
     * buffer for drawing the circles
     */
    private val mCirclesBuffer = FloatArray(2)

    protected fun drawCircles(c: Canvas) {
        mRenderPaint!!.style = Paint.Style.FILL
        val phaseY = mAnimator!!.getPhaseY()
        mCirclesBuffer[0] = 0f
        mCirclesBuffer[1] = 0f
        val dataSets: List<ILineDataSet>? = mChart!!.getLineData().getDataSets()
        for (i in dataSets!!.indices) {
            val dataSet = dataSets[i]
            if (!dataSet.isVisible() || !dataSet.isDrawCirclesEnabled() || dataSet.getEntryCount() == 0) continue
            mCirclePaintInner!!.color = dataSet.getCircleHoleColor()
            val trans = mChart!!.getTransformer(dataSet.getAxisDependency())
            mXBounds[mChart!!] = dataSet
            val circleRadius = dataSet.getCircleRadius()
            val circleHoleRadius = dataSet.getCircleHoleRadius()
            val drawCircleHole =
                dataSet.isDrawCircleHoleEnabled() && circleHoleRadius < circleRadius && circleHoleRadius > 0f
            val drawTransparentCircleHole = drawCircleHole &&
                    dataSet.getCircleHoleColor() == ColorTemplate.COLOR_NONE
            var imageCache: DataSetImageCache?
            if (mImageCaches.containsKey(dataSet)) {
                imageCache = mImageCaches[dataSet]
            } else {
                imageCache = DataSetImageCache()
                mImageCaches[dataSet] = imageCache
            }
            val changeRequired = imageCache!!.init(dataSet)

            // only fill the cache with new bitmaps if a change is required
            if (changeRequired) {
                imageCache.fill(dataSet, drawCircleHole, drawTransparentCircleHole)
            }
            val boundsRangeCount = mXBounds.range + mXBounds.min
            for (j in mXBounds.min..boundsRangeCount) {
                val e = dataSet.getEntryForIndex(j) ?: break
                mCirclesBuffer[0] = e.getX()
                mCirclesBuffer[1] = e.getY() * phaseY
                trans.pointValuesToPixel(mCirclesBuffer)
                if (!mViewPortHandler!!.isInBoundsRight(mCirclesBuffer[0])) break
                if (!mViewPortHandler!!.isInBoundsLeft(mCirclesBuffer[0]) ||
                    !mViewPortHandler!!.isInBoundsY(mCirclesBuffer[1])
                ) continue
                val circleBitmap = imageCache.getBitmap(j)
                if (circleBitmap != null) {
                    c.drawBitmap(
                        circleBitmap,
                        mCirclesBuffer[0] - circleRadius,
                        mCirclesBuffer[1] - circleRadius,
                        null
                    )
                }
            }
        }
    }

    override fun drawHighlighted(c: Canvas?, indices: Array<Highlight>?) {
        val lineData = mChart!!.getLineData()
        for (high in indices!!) {
            val set = lineData.getDataSetByIndex(high.getDataSetIndex())
            if (set == null || !set.isHighlightEnabled()) continue
            val e = set.getEntryForXValue(high.getX(), high.getY())
            if (!isInBoundsX(e, set)) continue
            val pix = mChart!!.getTransformer(set.getAxisDependency()).getPixelForValues(
                e!!.getX(), e.getY() * mAnimator!!.getPhaseY()
            )
            high.setDraw(pix!!.x.toFloat(), pix.y.toFloat())

            // draw the lines
            drawHighlightLines(c!!, pix.x.toFloat(), pix.y.toFloat(), set)
        }
    }

    /**
     * Sets the Bitmap.Config to be used by this renderer.
     * Default: Bitmap.Config.ARGB_8888
     * Use Bitmap.Config.ARGB_4444 to consume less memory.
     *
     * @param config
     */
    fun setBitmapConfig(config: Bitmap.Config) {
        mBitmapConfig = config
        releaseBitmap()
    }

    /**
     * Returns the Bitmap.Config that is used by this renderer.
     *
     * @return
     */
    fun getBitmapConfig(): Bitmap.Config? {
        return mBitmapConfig
    }

    /**
     * Releases the drawing bitmap. This should be called when [LineChart.onDetachedFromWindow].
     */
    fun releaseBitmap() {
        if (mBitmapCanvas != null) {
            mBitmapCanvas!!.setBitmap(null)
            mBitmapCanvas = null
        }
        if (mDrawBitmap != null) {
            val drawBitmap = mDrawBitmap!!.get()
            drawBitmap?.recycle()
            mDrawBitmap!!.clear()
            mDrawBitmap = null
        }
    }

    inner class DataSetImageCache {

        private val mCirclePathBuffer = Path()
        private var circleBitmaps: Array<Bitmap?>? = null

        /**
         * Sets up the cache, returns true if a change of cache was required.
         *
         * @param set
         * @return
         */
        fun init(set: ILineDataSet): Boolean {
            val size = set.getCircleColorCount()
            var changeRequired = false
            if (circleBitmaps == null) {
                circleBitmaps = arrayOfNulls(size)
                changeRequired = true
            } else if (circleBitmaps!!.size != size) {
                circleBitmaps = arrayOfNulls(size)
                changeRequired = true
            }
            return changeRequired
        }

        /**
         * Fills the cache with bitmaps for the given dataset.
         *
         * @param set
         * @param drawCircleHole
         * @param drawTransparentCircleHole
         */
        fun fill(set: ILineDataSet, drawCircleHole: Boolean, drawTransparentCircleHole: Boolean) {
            val colorCount = set.getCircleColorCount()
            val circleRadius = set.getCircleRadius()
            val circleHoleRadius = set.getCircleHoleRadius()
            for (i in 0 until colorCount) {
                val conf = Bitmap.Config.ARGB_4444
                val circleBitmap = Bitmap.createBitmap(
                    (circleRadius * 2.1).toInt(),
                    (circleRadius * 2.1).toInt(),
                    conf
                )
                val canvas = Canvas(circleBitmap)
                circleBitmaps!![i] = circleBitmap
                mRenderPaint?.color = set.getCircleColor(i)
                if (drawTransparentCircleHole) {
                    // Begin path for circle with hole
                    mCirclePathBuffer.reset()
                    mCirclePathBuffer.addCircle(
                        circleRadius,
                        circleRadius,
                        circleRadius,
                        Path.Direction.CW
                    )

                    // Cut hole in path
                    mCirclePathBuffer.addCircle(
                        circleRadius,
                        circleRadius,
                        circleHoleRadius,
                        Path.Direction.CCW
                    )

                    // Fill in-between
                    canvas.drawPath(mCirclePathBuffer, mRenderPaint!!)
                } else {
                    canvas.drawCircle(
                        circleRadius,
                        circleRadius,
                        circleRadius,
                        mRenderPaint!!
                    )
                    if (drawCircleHole) {
                        canvas.drawCircle(
                            circleRadius,
                            circleRadius,
                            circleHoleRadius,
                            mCirclePaintInner!!
                        )
                    }
                }
            }
        }

        /**
         * Returns the cached Bitmap at the given index.
         *
         * @param index
         * @return
         */
        fun getBitmap(index: Int): Bitmap? {
            return circleBitmaps!![index % circleBitmaps!!.size]
        }
    }
}