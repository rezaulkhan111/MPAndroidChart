package com.github.mikephil.charting.utils

import android.annotation.SuppressLint
import android.content.Context
import android.content.res.Resources
import android.graphics.Canvas
import android.graphics.Paint
import android.graphics.Rect
import android.graphics.drawable.Drawable
import android.os.Build
import android.text.Layout
import android.text.StaticLayout
import android.text.TextPaint
import android.util.DisplayMetrics
import android.util.Log
import android.view.MotionEvent
import android.view.VelocityTracker
import android.view.View
import android.view.ViewConfiguration
import com.github.mikephil.charting.formatter.DefaultValueFormatter
import com.github.mikephil.charting.formatter.IValueFormatter

/**
 * Utilities class that has some helper methods. Needs to be initialized by
 * calling Utils.init(...) before usage. Inside the Chart.init() method, this is
 * done, if the Utils are used before that, Utils.init(...) needs to be called
 * manually.
 *
 * @author Philipp Jahoda
 */
object Utils {

    private var mMetrics: DisplayMetrics? = null
    private var mMinimumFlingVelocity = 50
    private var mMaximumFlingVelocity = 8000
    val DEG2RAD = Math.PI / 180.0
    val FDEG2RAD = Math.PI.toFloat() / 180f

    val DOUBLE_EPSILON = java.lang.Double.longBitsToDouble(1)

    val FLOAT_EPSILON = java.lang.Float.intBitsToFloat(1)

    /**
     * initialize method, called inside the Chart.init() method.
     *
     * @param context
     */
    fun init(context: Context?) {
        if (context == null) {
            // noinspection deprecation
            mMinimumFlingVelocity = ViewConfiguration.getMinimumFlingVelocity()
            // noinspection deprecation
            mMaximumFlingVelocity = ViewConfiguration.getMaximumFlingVelocity()
            Log.e(
                "MPChartLib-Utils", "Utils.init(...) PROVIDED CONTEXT OBJECT IS NULL"
            )
        } else {
            val viewConfiguration = ViewConfiguration.get(context)
            mMinimumFlingVelocity = viewConfiguration.scaledMinimumFlingVelocity
            mMaximumFlingVelocity = viewConfiguration.scaledMaximumFlingVelocity
            val res = context.resources
            mMetrics = res.displayMetrics
        }
    }

    /**
     * initialize method, called inside the Chart.init() method. backwards
     * compatibility - to not break existing code
     *
     * @param res
     */
    @Deprecated("")
    fun init(res: Resources) {
        mMetrics = res.displayMetrics

        // noinspection deprecation
        mMinimumFlingVelocity = ViewConfiguration.getMinimumFlingVelocity()
        // noinspection deprecation
        mMaximumFlingVelocity = ViewConfiguration.getMaximumFlingVelocity()
    }

    /**
     * This method converts dp unit to equivalent pixels, depending on device
     * density. NEEDS UTILS TO BE INITIALIZED BEFORE USAGE.
     *
     * @param dp A value in dp (density independent pixels) unit. Which we need
     * to convert into pixels
     * @return A float value to represent px equivalent to dp depending on
     * device density
     */
    fun convertDpToPixel(dp: Float): Float {
        if (mMetrics == null) {
            Log.e(
                "MPChartLib-Utils",
                "Utils NOT INITIALIZED. You need to call Utils.init(...) at least once before" +
                        " calling Utils.convertDpToPixel(...). Otherwise conversion does not " +
                        "take place."
            )
            return dp
        }
        return dp * mMetrics!!.density
    }

    /**
     * This method converts device specific pixels to density independent
     * pixels. NEEDS UTILS TO BE INITIALIZED BEFORE USAGE.
     *
     * @param px A value in px (pixels) unit. Which we need to convert into db
     * @return A float value to represent dp equivalent to px value
     */
    fun convertPixelsToDp(px: Float): Float {
        if (mMetrics == null) {
            Log.e(
                "MPChartLib-Utils",
                ("Utils NOT INITIALIZED. You need to call Utils.init(...) at least once before" +
                        " calling Utils.convertPixelsToDp(...). Otherwise conversion does not" +
                        " take place.")
            )
            return px
        }
        return px / mMetrics!!.density
    }

    /**
     * calculates the approximate width of a text, depending on a demo text
     * avoid repeated calls (e.g. inside drawing methods)
     *
     * @param paint
     * @param demoText
     * @return
     */
    fun calcTextWidth(paint: Paint, demoText: String?): Int {
        return paint.measureText(demoText).toInt()
    }

    private val mCalcTextHeightRect = Rect()

    /**
     * calculates the approximate height of a text, depending on a demo text
     * avoid repeated calls (e.g. inside drawing methods)
     *
     * @param paint
     * @param demoText
     * @return
     */
    fun calcTextHeight(paint: Paint, demoText: String): Int {
        val r = mCalcTextHeightRect
        r[0, 0, 0] = 0
        paint.getTextBounds(demoText, 0, demoText.length, r)
        return r.height()
    }

    private val mFontMetrics = Paint.FontMetrics()

    fun getLineHeight(paint: Paint): Float {
        return getLineHeight(paint, mFontMetrics)
    }

    fun getLineHeight(paint: Paint, fontMetrics: Paint.FontMetrics): Float {
        paint.getFontMetrics(fontMetrics)
        return fontMetrics.descent - fontMetrics.ascent
    }

    fun getLineSpacing(paint: Paint): Float {
        return getLineSpacing(paint, mFontMetrics)
    }

    fun getLineSpacing(paint: Paint, fontMetrics: Paint.FontMetrics): Float {
        paint.getFontMetrics(fontMetrics)
        return fontMetrics.ascent - fontMetrics.top + fontMetrics.bottom
    }

    /**
     * Returns a recyclable FSize instance.
     * calculates the approximate size of a text, depending on a demo text
     * avoid repeated calls (e.g. inside drawing methods)
     *
     * @param paint
     * @param demoText
     * @return A Recyclable FSize instance
     */
    fun calcTextSize(paint: Paint, demoText: String): FSize? {
        val result = FSize.getInstance(0f, 0f)
        calcTextSize(paint, demoText, result)
        return result
    }

    private val mCalcTextSizeRect = Rect()

    /**
     * calculates the approximate size of a text, depending on a demo text
     * avoid repeated calls (e.g. inside drawing methods)
     *
     * @param paint
     * @param demoText
     * @param outputFSize An output variable, modified by the function.
     */
    fun calcTextSize(paint: Paint, demoText: String, outputFSize: FSize) {
        val r = mCalcTextSizeRect
        r[0, 0, 0] = 0
        paint.getTextBounds(demoText, 0, demoText.length, r)
        outputFSize.width = r.width().toFloat()
        outputFSize.height = r.height().toFloat()
    }


    /**
     * Math.pow(...) is very expensive, so avoid calling it and create it
     * yourself.
     */
    private val POW_10 = intArrayOf(
        1, 10, 100, 1000, 10000, 100000, 1000000, 10000000, 100000000, 1000000000
    )

    private val mDefaultValueFormatter = generateDefaultValueFormatter()

    private fun generateDefaultValueFormatter(): IValueFormatter {
        return DefaultValueFormatter(1)
    }

    /// - returns: The default value formatter used for all chart components that needs a default
    fun getDefaultValueFormatter(): IValueFormatter? {
        return mDefaultValueFormatter
    }

    /**
     * Formats the given number to the given number of decimals, and returns the
     * number as a string, maximum 35 characters. If thousands are separated, the separating
     * character is a dot (".").
     *
     * @param number
     * @param digitCount
     * @param separateThousands set this to true to separate thousands values
     * @return
     */
    fun formatNumber(number: Float, digitCount: Int, separateThousands: Boolean): String? {
        return formatNumber(number, digitCount, separateThousands, '.')
    }

    /**
     * Formats the given number to the given number of decimals, and returns the
     * number as a string, maximum 35 characters.
     *
     * @param number
     * @param digitCount
     * @param separateThousands set this to true to separate thousands values
     * @param separateChar      a caracter to be paced between the "thousands"
     * @return
     */
    fun formatNumber(
        number: Float, digitCount: Int, separateThousands: Boolean,
        separateChar: Char
    ): String? {
        var lNumber = number
        var lDigitCount = digitCount
        val out = CharArray(35)
        var neg = false
        if (lNumber == 0f) {
            return "0"
        }
        var zero = false
        if (lNumber < 1 && lNumber > -1) {
            zero = true
        }
        if (lNumber < 0) {
            neg = true
            lNumber = -lNumber
        }
        if (lDigitCount > POW_10.size) {
            lDigitCount = POW_10.size - 1
        }
        lNumber *= POW_10[lDigitCount]
        var lval = Math.round(lNumber).toLong()
        var ind = out.size - 1
        var charCount = 0
        var decimalPointAdded = false
        while (lval != 0L || charCount < (lDigitCount + 1)) {
            val digit = (lval % 10).toInt()
            lval = lval / 10
            out[ind--] = (digit + '0'.code).toChar()
            charCount++

            // add decimal point
            if (charCount == lDigitCount) {
                out[ind--] = ','
                charCount++
                decimalPointAdded = true

                // add thousand separators
            } else if (separateThousands && (lval != 0L) && (charCount > lDigitCount)) {
                if (decimalPointAdded) {
                    if ((charCount - lDigitCount) % 4 == 0) {
                        out[ind--] = separateChar
                        charCount++
                    }
                } else {
                    if ((charCount - lDigitCount) % 4 == 3) {
                        out[ind--] = separateChar
                        charCount++
                    }
                }
            }
        }

        // if number around zero (between 1 and -1)
        if (zero) {
            out[ind--] = '0'
            charCount += 1
        }

        // if the number is negative
        if (neg) {
            out[ind--] = '-'
            charCount += 1
        }
        val start = out.size - charCount

        // use this instead of "new String(...)" because of issue < Android 4.0
        return String(out, start, out.size - start)
    }

    /**
     * rounds the given number to the next significant number
     *
     * @param number
     * @return
     */
    fun roundToNextSignificant(number: Double): Float {
        if ((java.lang.Double.isInfinite(number) ||
                    java.lang.Double.isNaN(number) || (
                    number == 0.0))
        ) return 0f
        val d = Math.ceil(Math.log10(if (number < 0) -number else number).toFloat().toDouble())
            .toFloat()
        val pw = 1 - d.toInt()
        val magnitude = Math.pow(10.0, pw.toDouble()).toFloat()
        val shifted = Math.round(number * magnitude)
        return shifted / magnitude
    }

    /**
     * Returns the appropriate number of decimals to be used for the provided
     * number.
     *
     * @param number
     * @return
     */
    fun getDecimals(number: Float): Int {
        val i = roundToNextSignificant(number.toDouble())
        return if (java.lang.Float.isInfinite(i)) 0 else Math.ceil(-Math.log10(i.toDouble()))
            .toInt() + 2
    }

    /**
     * Converts the provided Integer List to an int array.
     *
     * @param integers
     * @return
     */
    fun convertIntegers(integers: List<Int>): IntArray? {
        val ret = IntArray(integers.size)
        copyIntegers(integers, ret)
        return ret
    }

    fun copyIntegers(from: List<Int>, to: IntArray) {
        val count = if (to.size < from.size) to.size else from.size
        for (i in 0 until count) {
            to[i] = from[i]
        }
    }

    /**
     * Converts the provided String List to a String array.
     *
     * @param strings
     * @return
     */
    fun convertStrings(strings: List<String?>): Array<String?>? {
        val ret = arrayOfNulls<String>(strings.size)
        for (i in ret.indices) {
            ret[i] = strings[i]
        }
        return ret
    }

    fun copyStrings(from: List<String?>, to: Array<String?>) {
        val count = if (to.size < from.size) to.size else from.size
        for (i in 0 until count) {
            to[i] = from[i]
        }
    }

    /**
     * Replacement for the Math.nextUp(...) method that is only available in
     * HONEYCOMB and higher. Dat's some seeeeek sheeet.
     *
     * @param d
     * @return
     */
    fun nextUp(d: Double): Double {
        var ldou = d
        return if (ldou == Double.POSITIVE_INFINITY) ldou else {
            ldou += 0.0
            java.lang.Double.longBitsToDouble(
                java.lang.Double.doubleToRawLongBits(ldou) +
                        (if ((ldou >= 0.0)) +1L else -1L)
            )
        }
    }

    /**
     * Returns a recyclable MPPointF instance.
     * Calculates the position around a center point, depending on the distance
     * from the center, and the angle of the position around the center.
     *
     * @param center
     * @param dist
     * @param angle  in degrees, converted to radians internally
     * @return
     */
    fun getPosition(center: MPPointF, dist: Float, angle: Float): MPPointF {
        val p = MPPointF.getInstance(0f, 0f)
        getPosition(center, dist, angle, p)
        return p
    }

    fun getPosition(center: MPPointF, dist: Float, angle: Float, outputPoint: MPPointF) {
        outputPoint.x = (center.x + dist * Math.cos(Math.toRadians(angle.toDouble()))).toFloat()
        outputPoint.y = (center.y + dist * Math.sin(Math.toRadians(angle.toDouble()))).toFloat()
    }

    fun velocityTrackerPointerUpCleanUpIfNecessary(
        ev: MotionEvent,
        tracker: VelocityTracker
    ) {

        // Check the dot product of current velocities.
        // If the pointer that left was opposing another velocity vector, clear.
        tracker.computeCurrentVelocity(1000, mMaximumFlingVelocity.toFloat())
        val upIndex = ev.actionIndex
        val id1 = ev.getPointerId(upIndex)
        val x1 = tracker.getXVelocity(id1)
        val y1 = tracker.getYVelocity(id1)
        var i = 0
        val count = ev.pointerCount
        while (i < count) {
            if (i == upIndex) {
                i++
                continue
            }
            val id2 = ev.getPointerId(i)
            val x = x1 * tracker.getXVelocity(id2)
            val y = y1 * tracker.getYVelocity(id2)
            val dot = x + y
            if (dot < 0) {
                tracker.clear()
                break
            }
            i++
        }
    }

    /**
     * Original method view.postInvalidateOnAnimation() only supportd in API >=
     * 16, This is a replica of the code from ViewCompat.
     *
     * @param view
     */
    @SuppressLint("NewApi")
    fun postInvalidateOnAnimation(view: View) {
        if (Build.VERSION.SDK_INT >= 16) view.postInvalidateOnAnimation() else view.postInvalidateDelayed(
            10
        )
    }

    fun getMinimumFlingVelocity(): Int {
        return mMinimumFlingVelocity
    }

    fun getMaximumFlingVelocity(): Int {
        return mMaximumFlingVelocity
    }

    /**
     * returns an angle between 0.f < 360.f (not less than zero, less than 360)
     */
    fun getNormalizedAngle(angle: Float): Float {
        var lAngle = angle
        while (lAngle < 0f) lAngle += 360f
        return lAngle % 360f
    }

    private val mDrawableBoundsCache = Rect()

    fun drawImage(
        canvas: Canvas,
        drawable: Drawable,
        x: Int, y: Int,
        width: Int, height: Int
    ) {
        val drawOffset = MPPointF.getInstance()
        drawOffset.x = (x - (width / 2)).toFloat()
        drawOffset.y = (y - (height / 2)).toFloat()
        drawable.copyBounds(mDrawableBoundsCache)
        drawable.setBounds(
            mDrawableBoundsCache.left,
            mDrawableBoundsCache.top,
            mDrawableBoundsCache.left + width,
            mDrawableBoundsCache.top + width
        )
        val saveId = canvas.save()
        // translate to the correct position and draw
        canvas.translate(drawOffset.x, drawOffset.y)
        drawable.draw(canvas)
        canvas.restoreToCount(saveId)
    }

    private val mDrawTextRectBuffer = Rect()
    private val mFontMetricsBuffer = Paint.FontMetrics()

    fun drawXAxisValue(
        c: Canvas, text: String, x: Float, y: Float,
        paint: Paint,
        anchor: MPPointF, angleDegrees: Float
    ) {
        var drawOffsetX = 0f
        var drawOffsetY = 0f
        val lineHeight = paint.getFontMetrics(mFontMetricsBuffer)
        paint.getTextBounds(text, 0, text.length, mDrawTextRectBuffer)

        // Android sometimes has pre-padding
        drawOffsetX -= mDrawTextRectBuffer.left.toFloat()

        // Android does not snap the bounds to line boundaries,
        //  and draws from bottom to top.
        // And we want to normalize it.
        drawOffsetY += -mFontMetricsBuffer.ascent

        // To have a consistent point of reference, we always draw left-aligned
        val originalTextAlign = paint.textAlign
        paint.textAlign = Paint.Align.LEFT
        if (angleDegrees != 0f) {

            // Move the text drawing rect in a way that it always rotates around its center
            drawOffsetX -= mDrawTextRectBuffer.width() * 0.5f
            drawOffsetY -= lineHeight * 0.5f
            var translateX = x
            var translateY = y

            // Move the "outer" rect relative to the anchor, assuming its centered
            if (anchor.x != 0.5f || anchor.y != 0.5f) {
                val rotatedSize = getSizeOfRotatedRectangleByDegrees(
                    mDrawTextRectBuffer.width().toFloat(),
                    lineHeight,
                    angleDegrees
                )
                translateX -= rotatedSize.width * (anchor.x - 0.5f)
                translateY -= rotatedSize.height * (anchor.y - 0.5f)
                FSize.recycleInstance(rotatedSize)
            }
            c.save()
            c.translate(translateX, translateY)
            c.rotate(angleDegrees)
            c.drawText(text, drawOffsetX, drawOffsetY, paint)
            c.restore()
        } else {
            if (anchor.x != 0f || anchor.y != 0f) {
                drawOffsetX -= mDrawTextRectBuffer.width() * anchor.x
                drawOffsetY -= lineHeight * anchor.y
            }
            drawOffsetX += x
            drawOffsetY += y
            c.drawText(text, drawOffsetX, drawOffsetY, paint)
        }
        paint.textAlign = originalTextAlign
    }

    fun drawMultilineText(
        c: Canvas, textLayout: StaticLayout,
        x: Float, y: Float,
        paint: TextPaint,
        anchor: MPPointF, angleDegrees: Float
    ) {
        var drawOffsetX = 0f
        var drawOffsetY = 0f
        val drawWidth: Float
        val drawHeight: Float
        val lineHeight = paint.getFontMetrics(mFontMetricsBuffer)
        drawWidth = textLayout.width.toFloat()
        drawHeight = textLayout.lineCount * lineHeight

        // Android sometimes has pre-padding
        drawOffsetX -= mDrawTextRectBuffer.left.toFloat()

        // Android does not snap the bounds to line boundaries,
        //  and draws from bottom to top.
        // And we want to normalize it.
        drawOffsetY += drawHeight

        // To have a consistent point of reference, we always draw left-aligned
        val originalTextAlign = paint.textAlign
        paint.textAlign = Paint.Align.LEFT
        if (angleDegrees != 0f) {

            // Move the text drawing rect in a way that it always rotates around its center
            drawOffsetX -= drawWidth * 0.5f
            drawOffsetY -= drawHeight * 0.5f
            var translateX = x
            var translateY = y

            // Move the "outer" rect relative to the anchor, assuming its centered
            if (anchor.x != 0.5f || anchor.y != 0.5f) {
                val rotatedSize = getSizeOfRotatedRectangleByDegrees(
                    drawWidth,
                    drawHeight,
                    angleDegrees
                )
                translateX -= rotatedSize.width * (anchor.x - 0.5f)
                translateY -= rotatedSize.height * (anchor.y - 0.5f)
                FSize.recycleInstance(rotatedSize)
            }
            c.save()
            c.translate(translateX, translateY)
            c.rotate(angleDegrees)
            c.translate(drawOffsetX, drawOffsetY)
            textLayout.draw(c)
            c.restore()
        } else {
            if (anchor.x != 0f || anchor.y != 0f) {
                drawOffsetX -= drawWidth * anchor.x
                drawOffsetY -= drawHeight * anchor.y
            }
            drawOffsetX += x
            drawOffsetY += y
            c.save()
            c.translate(drawOffsetX, drawOffsetY)
            textLayout.draw(c)
            c.restore()
        }
        paint.textAlign = originalTextAlign
    }

    fun drawMultilineText(
        c: Canvas, text: String,
        x: Float, y: Float,
        paint: TextPaint,
        constrainedToSize: FSize,
        anchor: MPPointF, angleDegrees: Float
    ) {
        val textLayout = StaticLayout(
            text, 0, text.length,
            paint, Math.max(Math.ceil(constrainedToSize.width.toDouble()), 1.0).toInt(),
            Layout.Alignment.ALIGN_NORMAL, 1f, 0f, false
        )
        drawMultilineText(c, textLayout, x, y, paint, anchor, angleDegrees)
    }

    /**
     * Returns a recyclable FSize instance.
     * Represents size of a rotated rectangle by degrees.
     *
     * @param rectangleSize
     * @param degrees
     * @return A Recyclable FSize instance
     */
    fun getSizeOfRotatedRectangleByDegrees(rectangleSize: FSize, degrees: Float): FSize? {
        val radians = degrees * FDEG2RAD
        return getSizeOfRotatedRectangleByRadians(
            rectangleSize.width, rectangleSize.height,
            radians
        )
    }

    /**
     * Returns a recyclable FSize instance.
     * Represents size of a rotated rectangle by radians.
     *
     * @param rectangleSize
     * @param radians
     * @return A Recyclable FSize instance
     */
    fun getSizeOfRotatedRectangleByRadians(rectangleSize: FSize, radians: Float): FSize? {
        return getSizeOfRotatedRectangleByRadians(
            rectangleSize.width, rectangleSize.height,
            radians
        )
    }

    /**
     * Returns a recyclable FSize instance.
     * Represents size of a rotated rectangle by degrees.
     *
     * @param rectangleWidth
     * @param rectangleHeight
     * @param degrees
     * @return A Recyclable FSize instance
     */
    fun getSizeOfRotatedRectangleByDegrees(
        rectangleWidth: Float,
        rectangleHeight: Float,
        degrees: Float
    ): FSize {
        val radians = degrees * FDEG2RAD
        return getSizeOfRotatedRectangleByRadians(rectangleWidth, rectangleHeight, radians)
    }

    /**
     * Returns a recyclable FSize instance.
     * Represents size of a rotated rectangle by radians.
     *
     * @param rectangleWidth
     * @param rectangleHeight
     * @param radians
     * @return A Recyclable FSize instance
     */
    fun getSizeOfRotatedRectangleByRadians(
        rectangleWidth: Float,
        rectangleHeight: Float,
        radians: Float
    ): FSize {
        return FSize.getInstance(
            Math.abs(rectangleWidth * Math.cos(radians.toDouble()).toFloat()) + Math.abs(
                rectangleHeight * Math.sin(radians.toDouble()).toFloat()
            ),
            Math.abs(rectangleWidth * Math.sin(radians.toDouble()).toFloat()) + Math.abs(
                rectangleHeight * Math.cos(radians.toDouble()).toFloat()
            )
        )
    }

    fun getSDKInt(): Int {
        return Build.VERSION.SDK_INT
    }
}