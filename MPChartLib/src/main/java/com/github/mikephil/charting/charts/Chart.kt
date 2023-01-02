package com.github.mikephil.charting.charts

import android.content.ContentValues
import android.content.Context
import android.graphics.*
import android.graphics.Bitmap.CompressFormat
import android.graphics.Paint.Align
import android.os.Environment
import android.provider.MediaStore.Images
import android.text.TextUtils
import android.util.AttributeSet
import android.util.Log
import android.view.View
import android.view.ViewGroup
import androidx.annotation.RequiresApi
import com.github.mikephil.charting.animation.ChartAnimator
import com.github.mikephil.charting.animation.Easing.EasingFunction
import com.github.mikephil.charting.components.Description
import com.github.mikephil.charting.components.IMarker
import com.github.mikephil.charting.components.Legend
import com.github.mikephil.charting.components.XAxis
import com.github.mikephil.charting.data.ChartData
import com.github.mikephil.charting.data.Entry
import com.github.mikephil.charting.formatter.DefaultValueFormatter
import com.github.mikephil.charting.formatter.IValueFormatter
import com.github.mikephil.charting.highlight.ChartHighlighter
import com.github.mikephil.charting.highlight.Highlight
import com.github.mikephil.charting.highlight.IHighlighter
import com.github.mikephil.charting.interfaces.dataprovider.ChartInterface
import com.github.mikephil.charting.interfaces.datasets.IDataSet
import com.github.mikephil.charting.listener.ChartTouchListener
import com.github.mikephil.charting.listener.OnChartGestureListener
import com.github.mikephil.charting.listener.OnChartValueSelectedListener
import com.github.mikephil.charting.renderer.DataRenderer
import com.github.mikephil.charting.renderer.LegendRenderer
import com.github.mikephil.charting.utils.MPPointF
import com.github.mikephil.charting.utils.MPPointF.Companion.getInstance
import com.github.mikephil.charting.utils.Utils.convertDpToPixel
import com.github.mikephil.charting.utils.Utils.getDecimals
import com.github.mikephil.charting.utils.Utils.init
import com.github.mikephil.charting.utils.ViewPortHandler
import java.io.File
import java.io.FileOutputStream
import java.io.IOException
import java.io.OutputStream

/**
 * Baseclass of all Chart-Views.
 *
 * @author Philipp Jahoda
 */
abstract class Chart<T : ChartData<out IDataSet<out Entry?>?>?> : ViewGroup, ChartInterface {
    val LOG_TAG = "MPAndroidChart"

    /**
     * flag that indicates if logging is enabled or not
     */
    protected var mLogEnabled = false

    /**
     * object that holds all data that was originally set for the chart, before
     * it was modified or any filtering algorithms had been applied
     */
    protected var mData: T? = null

    /**
     * Flag that indicates if highlighting per tap (touch) is enabled
     */
    protected var mHighLightPerTapEnabled = true

    /**
     * If set to true, chart continues to scroll after touch up
     */
    private var mDragDecelerationEnabled = true

    /**
     * Deceleration friction coefficient in [0 ; 1] interval, higher values
     * indicate that speed will decrease slowly, for example if it set to 0, it
     * will stop immediately. 1 is an invalid value, and will be converted to
     * 0.999f automatically.
     */
    private var mDragDecelerationFrictionCoef = 0.9f

    /**
     * default value-formatter, number of digits depends on provided chart-data
     */
    protected var mDefaultValueFormatter = DefaultValueFormatter(0)

    /**
     * paint object used for drawing the description text in the bottom right
     * corner of the chart
     */
    protected var mDescPaint: Paint? = null

    /**
     * paint object for drawing the information text when there are no values in
     * the chart
     */
    protected var mInfoPaint: Paint? = null

    /**
     * the object representing the labels on the x-axis
     */
    protected var mXAxis: XAxis? = null

    /**
     * if true, touch gestures are enabled on the chart
     */
    protected var mTouchEnabled = true

    /**
     * the object responsible for representing the description text
     */
    protected var mDescription: Description? = null

    /**
     * the legend object containing all data associated with the legend
     */
    protected var mLegend: Legend? = null

    /**
     * listener that is called when a value on the chart is selected
     */
    protected var mSelectionListener: OnChartValueSelectedListener? = null

    protected var mChartTouchListener: ChartTouchListener<*>? = null

    /**
     * text that is displayed when the chart is empty
     */
    private var mNoDataText = "No chart data available."

    /**
     * Gesture listener for custom callbacks when making gestures on the chart.
     */
    private var mGestureListener: OnChartGestureListener? = null

    protected var mLegendRenderer: LegendRenderer? = null

    /**
     * object responsible for rendering the data
     */
    protected var mRenderer: DataRenderer? = null

    protected var mHighlighter: IHighlighter? = null

    /**
     * object that manages the bounds and drawing constraints of the chart
     */
    protected var mViewPortHandler = ViewPortHandler()

    /**
     * object responsible for animations
     */
    protected var mAnimator: ChartAnimator? = null

    /**
     * Extra offsets to be appended to the viewport
     */
    private var mExtraTopOffset = 0f;

    /**
     * Extra offsets to be appended to the viewport
     */
    private var mExtraRightOffset = 0f;

    /**
     * Extra offsets to be appended to the viewport
     */
    private var mExtraBottomOffset = 0f;

    /**
     * Extra offsets to be appended to the viewport
     */
    private var mExtraLeftOffset = 0f

    /**
     * default constructor for initialization in code
     */
    constructor(context: Context?) : super(context) {
        init()
    }

    /**
     * constructor for initialization in xml
     */
    constructor(context: Context?, attrs: AttributeSet?) : super(context, attrs) {
        init()
    }

    /**
     * even more awesome constructor
     */
    constructor(context: Context?, attrs: AttributeSet?, defStyle: Int) : super(
        context,
        attrs,
        defStyle
    ) {
        init()
    }

    /**
     * initialize all paints and stuff
     */
    protected open fun init() {
        setWillNotDraw(false)
        // setLayerType(View.LAYER_TYPE_HARDWARE, null);
        mAnimator = ChartAnimator { // ViewCompat.postInvalidateOnAnimation(Chart.this);
            postInvalidate()
        }

        // initialize the utils
        init(context)
        mMaxHighlightDistance = convertDpToPixel(500f)
        mDescription = Description()
        mLegend = Legend()
        mLegendRenderer = LegendRenderer(mViewPortHandler, mLegend!!)
        mXAxis = XAxis()
        mDescPaint = Paint(Paint.ANTI_ALIAS_FLAG)
        mInfoPaint = Paint(Paint.ANTI_ALIAS_FLAG)
        mInfoPaint!!.color = Color.rgb(247, 189, 51) // orange
        mInfoPaint!!.textAlign = Align.CENTER
        mInfoPaint!!.textSize = convertDpToPixel(12f)
        if (mLogEnabled) Log.i("", "Chart.init()")
    }

    // public void initWithDummyData() {
    // ColorTemplate template = new ColorTemplate();
    // template.addColorsForDataSets(ColorTemplate.COLORFUL_COLORS,
    // getContext());
    //
    // setColorTemplate(template);
    // setDrawYValues(false);
    //
    // ArrayList<String> xVals = new ArrayList<String>();
    // Calendar calendar = Calendar.getInstance();
    // for (int i = 0; i < 12; i++) {
    // xVals.add(calendar.getDisplayName(Calendar.MONTH, Calendar.SHORT,
    // Locale.getDefault()));
    // }
    //
    // ArrayList<DataSet> dataSets = new ArrayList<DataSet>();
    // for (int i = 0; i < 3; i++) {
    //
    // ArrayList<Entry> yVals = new ArrayList<Entry>();
    //
    // for (int j = 0; j < 12; j++) {
    // float val = (float) (Math.random() * 100);
    // yVals.add(new Entry(val, j));
    // }
    //
    // DataSet set = new DataSet(yVals, "DataSet " + i);
    // dataSets.add(set); // add the datasets
    // }
    // // create a data object with the datasets
    // ChartData data = new ChartData(xVals, dataSets);
    // setData(data);
    // invalidate();
    // }

    // public void initWithDummyData() {
    // ColorTemplate template = new ColorTemplate();
    // template.addColorsForDataSets(ColorTemplate.COLORFUL_COLORS,
    // getContext());
    //
    // setColorTemplate(template);
    // setDrawYValues(false);
    //
    // ArrayList<String> xVals = new ArrayList<String>();
    // Calendar calendar = Calendar.getInstance();
    // for (int i = 0; i < 12; i++) {
    // xVals.add(calendar.getDisplayName(Calendar.MONTH, Calendar.SHORT,
    // Locale.getDefault()));
    // }
    //
    // ArrayList<DataSet> dataSets = new ArrayList<DataSet>();
    // for (int i = 0; i < 3; i++) {
    //
    // ArrayList<Entry> yVals = new ArrayList<Entry>();
    //
    // for (int j = 0; j < 12; j++) {
    // float val = (float) (Math.random() * 100);
    // yVals.add(new Entry(val, j));
    // }
    //
    // DataSet set = new DataSet(yVals, "DataSet " + i);
    // dataSets.add(set); // add the datasets
    // }
    // // create a data object with the datasets
    // ChartData data = new ChartData(xVals, dataSets);
    // setData(data);
    // invalidate();
    // }
    /**
     * Sets a new data object for the chart. The data object contains all values
     * and information needed for displaying.
     *
     * @param data
     */
    open fun setData(data: T?) {
        mData = data
        mOffsetsCalculated = false
        if (data == null) {
            return
        }

        // calculate how many digits are needed
        setupDefaultFormatter(data.yMin, data.yMax)
        for (set: IDataSet<*> in mData!!.dataSets!!) {
            if (set.needsFormatter() || set.getValueFormatter() === mDefaultValueFormatter) set.setValueFormatter(
                mDefaultValueFormatter
            )
        }

        // let the chart know there is new data
        notifyDataSetChanged()
        if (mLogEnabled) Log.i(LOG_TAG, "Data is set.")
    }

    /**
     * Clears the chart from all data (sets it to null) and refreshes it (by
     * calling invalidate()).
     */
    open fun clear() {
        mData = null
        mOffsetsCalculated = false
        mIndicesToHighlight = null
        mChartTouchListener!!.setLastHighlighted(null)
        invalidate()
    }

    /**
     * Removes all DataSets (and thereby Entries) from the chart. Does not set the data object to null. Also refreshes the
     * chart by calling invalidate().
     */
    open fun clearValues() {
        mData!!.clearValues()
        invalidate()
    }

    /**
     * Returns true if the chart is empty (meaning it's data object is either
     * null or contains no entries).
     *
     * @return
     */
    open fun isEmpty(): Boolean {
        if (mData == null) return true else {
            return if (mData!!.getEntryCount() <= 0) true else false
        }
    }

    /**
     * Lets the chart know its underlying data has changed and performs all
     * necessary recalculations. It is crucial that this method is called
     * everytime data is changed dynamically. Not calling this method can lead
     * to crashes or unexpected behaviour.
     */
    abstract fun notifyDataSetChanged()

    /**
     * Calculates the offsets of the chart to the border depending on the
     * position of an eventual legend or depending on the length of the y-axis
     * and x-axis labels and their position
     */
    protected abstract fun calculateOffsets()

    /**
     * Calculates the y-min and y-max value and the y-delta and x-delta value
     */
    protected abstract fun calcMinMax()

    /**
     * Calculates the required number of digits for the values that might be
     * drawn in the chart (if enabled), and creates the default-value-formatter
     */
    protected open fun setupDefaultFormatter(min: Float, max: Float) {
        var reference = 0f
        if (mData == null || mData!!.getEntryCount() < 2) {
            reference = Math.max(Math.abs(min), Math.abs(max))
        } else {
            reference = Math.abs(max - min)
        }
        val digits = getDecimals(reference)

        // setup the formatter with a new number of digits
        mDefaultValueFormatter.setup(digits)
    }

    /**
     * flag that indicates if offsets calculation has already been done or not
     */
    private var mOffsetsCalculated = false

    override fun onDraw(canvas: Canvas) {
        // super.onDraw(canvas);
        if (mData == null) {
            val hasText = !TextUtils.isEmpty(mNoDataText)
            if (hasText) {
                val pt = getCenter()
                when (mInfoPaint!!.textAlign) {
                    Align.LEFT -> {
                        pt.x = 0f
                        canvas.drawText(mNoDataText, pt.x, pt.y, mInfoPaint!!)
                    }
                    Align.RIGHT -> {
                        (pt.x *= 2.0).toFloat()
                        canvas.drawText(mNoDataText, pt.x, pt.y, mInfoPaint!!)
                    }
                    else -> canvas.drawText(mNoDataText, pt.x, pt.y, mInfoPaint!!)
                }
            }
            return
        }
        if (!mOffsetsCalculated) {
            calculateOffsets()
            mOffsetsCalculated = true
        }
    }

    /**
     * Draws the description text in the bottom right corner of the chart (per default)
     */
    protected open fun drawDescription(c: Canvas) {

        // check if description should be drawn
        if (mDescription != null && mDescription!!.isEnabled()) {
            val position: MPPointF = mDescription!!.getPosition()
            mDescPaint!!.typeface = mDescription!!.getTypeface()
            mDescPaint!!.textSize = mDescription!!.getTextSize()
            mDescPaint!!.color = mDescription!!.getTextColor()
            mDescPaint!!.textAlign = mDescription!!.getTextAlign()
            val x: Float
            val y: Float

            // if no position specified, draw on default position
            if (position == null) {
                x = width - mViewPortHandler.offsetRight() - mDescription!!.getXOffset()
                y = height - mViewPortHandler.offsetBottom() - mDescription!!.getYOffset()
            } else {
                x = position.x
                y = position.y
            }
            c.drawText(mDescription!!.getText(), x, y, mDescPaint!!)
        }
    }

    /**
     * ################ ################ ################ ################
     */
    /** BELOW THIS CODE FOR HIGHLIGHTING */

    /**
     * ################ ################ ################ ################
     */
    /** BELOW THIS CODE FOR HIGHLIGHTING  */
    /**
     * array of Highlight objects that reference the highlighted slices in the
     * chart
     */
    protected var mIndicesToHighlight: Array<Highlight?>?

    /**
     * The maximum distance in dp away from an entry causing it to highlight.
     */
    protected var mMaxHighlightDistance = 0f

    override fun getMaxHighlightDistance(): Float {
        return mMaxHighlightDistance
    }

    /**
     * Sets the maximum distance in screen dp a touch can be away from an entry to cause it to get highlighted.
     * Default: 500dp
     *
     * @param distDp
     */
    open fun setMaxHighlightDistance(distDp: Float) {
        mMaxHighlightDistance = convertDpToPixel(distDp)
    }

    /**
     * Returns the array of currently highlighted values. This might a null or
     * empty array if nothing is highlighted.
     *
     * @return
     */
    open fun getHighlighted(): Array<Highlight?>? {
        return mIndicesToHighlight
    }

    /**
     * Returns true if values can be highlighted via tap gesture, false if not.
     *
     * @return
     */
    open fun isHighlightPerTapEnabled(): Boolean {
        return mHighLightPerTapEnabled
    }

    /**
     * Set this to false to prevent values from being highlighted by tap gesture.
     * Values can still be highlighted via drag or programmatically. Default: true
     *
     * @param enabled
     */
    open fun setHighlightPerTapEnabled(enabled: Boolean) {
        mHighLightPerTapEnabled = enabled
    }

    /**
     * Returns true if there are values to highlight, false if there are no
     * values to highlight. Checks if the highlight array is null, has a length
     * of zero or if the first object is null.
     *
     * @return
     */
    open fun valuesToHighlight(): Boolean {
        return if (mIndicesToHighlight == null || mIndicesToHighlight!!.size <= 0 || mIndicesToHighlight!![0] == null) false else true
    }

    /**
     * Sets the last highlighted value for the touchlistener.
     *
     * @param highs
     */
    protected open fun setLastHighlighted(highs: Array<Highlight?>?) {
        if (highs == null || highs.size <= 0 || highs[0] == null) {
            mChartTouchListener!!.setLastHighlighted(null)
        } else {
            mChartTouchListener!!.setLastHighlighted(highs[0])
        }
    }

    /**
     * Highlights the values at the given indices in the given DataSets. Provide
     * null or an empty array to undo all highlighting. This should be used to
     * programmatically highlight values.
     * This method *will not* call the listener.
     *
     * @param highs
     */
    open fun highlightValues(highs: Array<Highlight?>?) {

        // set the indices to highlight
        mIndicesToHighlight = highs
        setLastHighlighted(highs)

        // redraw the chart
        invalidate()
    }

    /**
     * Highlights any y-value at the given x-value in the given DataSet.
     * Provide -1 as the dataSetIndex to undo all highlighting.
     * This method will call the listener.
     * @param x The x-value to highlight
     * @param dataSetIndex The dataset index to search in
     * @param dataIndex The data index to search in (only used in CombinedChartView currently)
     */
    open fun highlightValue(x: Float, dataSetIndex: Int, dataIndex: Int) {
        highlightValue(x, dataSetIndex, dataIndex, true)
    }

    /**
     * Highlights any y-value at the given x-value in the given DataSet.
     * Provide -1 as the dataSetIndex to undo all highlighting.
     * This method will call the listener.
     * @param x The x-value to highlight
     * @param dataSetIndex The dataset index to search in
     */
    open fun highlightValue(x: Float, dataSetIndex: Int) {
        highlightValue(x, dataSetIndex, -1, true)
    }

    /**
     * Highlights the value at the given x-value and y-value in the given DataSet.
     * Provide -1 as the dataSetIndex to undo all highlighting.
     * This method will call the listener.
     * @param x The x-value to highlight
     * @param y The y-value to highlight. Supply `NaN` for "any"
     * @param dataSetIndex The dataset index to search in
     * @param dataIndex The data index to search in (only used in CombinedChartView currently)
     */
    open fun highlightValue(x: Float, y: Float, dataSetIndex: Int, dataIndex: Int) {
        highlightValue(x, y, dataSetIndex, dataIndex, true)
    }

    /**
     * Highlights the value at the given x-value and y-value in the given DataSet.
     * Provide -1 as the dataSetIndex to undo all highlighting.
     * This method will call the listener.
     * @param x The x-value to highlight
     * @param y The y-value to highlight. Supply `NaN` for "any"
     * @param dataSetIndex The dataset index to search in
     */
    open fun highlightValue(x: Float, y: Float, dataSetIndex: Int) {
        highlightValue(x, y, dataSetIndex, -1, true)
    }

    /**
     * Highlights any y-value at the given x-value in the given DataSet.
     * Provide -1 as the dataSetIndex to undo all highlighting.
     * @param x The x-value to highlight
     * @param dataSetIndex The dataset index to search in
     * @param dataIndex The data index to search in (only used in CombinedChartView currently)
     * @param callListener Should the listener be called for this change
     */
    open fun highlightValue(x: Float, dataSetIndex: Int, dataIndex: Int, callListener: Boolean) {
        highlightValue(x, Float.NaN, dataSetIndex, dataIndex, callListener)
    }

    /**
     * Highlights any y-value at the given x-value in the given DataSet.
     * Provide -1 as the dataSetIndex to undo all highlighting.
     * @param x The x-value to highlight
     * @param dataSetIndex The dataset index to search in
     * @param callListener Should the listener be called for this change
     */
    open fun highlightValue(x: Float, dataSetIndex: Int, callListener: Boolean) {
        highlightValue(x, Float.NaN, dataSetIndex, -1, callListener)
    }

    /**
     * Highlights any y-value at the given x-value in the given DataSet.
     * Provide -1 as the dataSetIndex to undo all highlighting.
     * @param x The x-value to highlight
     * @param y The y-value to highlight. Supply `NaN` for "any"
     * @param dataSetIndex The dataset index to search in
     * @param dataIndex The data index to search in (only used in CombinedChartView currently)
     * @param callListener Should the listener be called for this change
     */
    open fun highlightValue(
        x: Float,
        y: Float,
        dataSetIndex: Int,
        dataIndex: Int,
        callListener: Boolean
    ) {
        if (dataSetIndex < 0 || dataSetIndex >= mData!!.dataSetCount) {
            highlightValue(null, callListener)
        } else {
            highlightValue(Highlight(x, y, dataSetIndex, dataIndex), callListener)
        }
    }

    /**
     * Highlights any y-value at the given x-value in the given DataSet.
     * Provide -1 as the dataSetIndex to undo all highlighting.
     * @param x The x-value to highlight
     * @param y The y-value to highlight. Supply `NaN` for "any"
     * @param dataSetIndex The dataset index to search in
     * @param callListener Should the listener be called for this change
     */
    open fun highlightValue(x: Float, y: Float, dataSetIndex: Int, callListener: Boolean) {
        highlightValue(x, y, dataSetIndex, -1, callListener)
    }

    /**
     * Highlights the values represented by the provided Highlight object
     * This method *will not* call the listener.
     *
     * @param highlight contains information about which entry should be highlighted
     */
    open fun highlightValue(highlight: Highlight?) {
        highlightValue(highlight, false)
    }

    /**
     * Highlights the value selected by touch gesture. Unlike
     * highlightValues(...), this generates a callback to the
     * OnChartValueSelectedListener.
     *
     * @param high         - the highlight object
     * @param callListener - call the listener
     */
    open fun highlightValue(high: Highlight?, callListener: Boolean) {
        var high = high
        var e: Entry? = null
        if (high == null) mIndicesToHighlight = null else {
            if (mLogEnabled) Log.i(LOG_TAG, "Highlighted: $high")
            e = mData!!.getEntryForHighlight(high)
            if (e == null) {
                mIndicesToHighlight = null
                high = null
            } else {

                // set the indices to highlight
                mIndicesToHighlight = arrayOf(
                    high
                )
            }
        }
        setLastHighlighted(mIndicesToHighlight)
        if (callListener && mSelectionListener != null) {
            if (!valuesToHighlight()) mSelectionListener!!.onNothingSelected() else {
                // notify the listener
                mSelectionListener!!.onValueSelected(e, high)
            }
        }

        // redraw the chart
        invalidate()
    }

    /**
     * Returns the Highlight object (contains x-index and DataSet index) of the
     * selected value at the given touch point inside the Line-, Scatter-, or
     * CandleStick-Chart.
     *
     * @param x
     * @param y
     * @return
     */
    open fun getHighlightByTouchPoint(x: Float, y: Float): Highlight? {
        if (mData == null) {
            Log.e(LOG_TAG, "Can't select by touch. No data set.")
            return null
        } else return getHighlighter()!!.getHighlight(x, y)
    }

    /**
     * Set a new (e.g. custom) ChartTouchListener NOTE: make sure to
     * setTouchEnabled(true); if you need touch gestures on the chart
     *
     * @param l
     */
    open fun setOnTouchListener(l: ChartTouchListener<*>?) {
        mChartTouchListener = l
    }

    /**
     * Returns an instance of the currently active touch listener.
     *
     * @return
     */
    open fun getOnTouchListener(): ChartTouchListener<*>? {
        return mChartTouchListener
    }

    /**
     * ################ ################ ################ ################
     */
    /** BELOW CODE IS FOR THE MARKER VIEW */

    /**
     * ################ ################ ################ ################
     */
    /** BELOW CODE IS FOR THE MARKER VIEW  */
    /**
     * if set to true, the marker view is drawn when a value is clicked
     */
    protected var mDrawMarkers = true

    /**
     * the view that represents the marker
     */
    protected var mMarker: IMarker? = null

    /**
     * draws all MarkerViews on the highlighted positions
     */
    protected open fun drawMarkers(canvas: Canvas?) {
        // if there is no marker view or drawing marker is disabled
        if (mMarker == null || !isDrawMarkersEnabled() || !valuesToHighlight()) return
        for (i in mIndicesToHighlight!!.indices) {
            val highlight = mIndicesToHighlight!![i]
            val set = mData!!.getDataSetByIndex(highlight!!.dataSetIndex)!!
            val e = mData!!.getEntryForHighlight(
                mIndicesToHighlight!![i]!!
            )
            val entryIndex = set.getEntryIndex(e)

            // make sure entry not null
            if (e == null || entryIndex > set.getEntryCount() * mAnimator!!.getPhaseX()) continue
            val pos = getMarkerPosition(highlight)

            // check bounds
            if (!mViewPortHandler.isInBounds(pos[0], pos[1])) continue

            // callbacks to update the content
            mMarker!!.refreshContent(e, highlight)

            // draw the marker
            mMarker!!.draw(canvas, pos[0], pos[1])
        }
    }

    /**
     * Returns the actual position in pixels of the MarkerView for the given
     * Highlight object.
     *
     * @param high
     * @return
     */
    protected open fun getMarkerPosition(high: Highlight?): FloatArray {
        return floatArrayOf(high!!.drawX, high.drawY)
    }

    /**
     * ################ ################ ################ ################
     * ANIMATIONS ONLY WORK FOR API LEVEL 11 (Android 3.0.x) AND HIGHER.
     */
    /** CODE BELOW THIS RELATED TO ANIMATION */

    /**
     * ################ ################ ################ ################
     * ANIMATIONS ONLY WORK FOR API LEVEL 11 (Android 3.0.x) AND HIGHER.
     */
    /** CODE BELOW THIS RELATED TO ANIMATION  */
    /**
     * Returns the animator responsible for animating chart values.
     *
     * @return
     */
    open fun getAnimator(): ChartAnimator? {
        return mAnimator
    }

    /**
     * If set to true, chart continues to scroll after touch up default: true
     */
    open fun isDragDecelerationEnabled(): Boolean {
        return mDragDecelerationEnabled
    }

    /**
     * If set to true, chart continues to scroll after touch up. Default: true.
     *
     * @param enabled
     */
    open fun setDragDecelerationEnabled(enabled: Boolean) {
        mDragDecelerationEnabled = enabled
    }

    /**
     * Returns drag deceleration friction coefficient
     *
     * @return
     */
    open fun getDragDecelerationFrictionCoef(): Float {
        return mDragDecelerationFrictionCoef
    }

    /**
     * Deceleration friction coefficient in [0 ; 1] interval, higher values
     * indicate that speed will decrease slowly, for example if it set to 0, it
     * will stop immediately. 1 is an invalid value, and will be converted to
     * 0.999f automatically.
     *
     * @param newValue
     */
    open fun setDragDecelerationFrictionCoef(newValue: Float) {
        var newValue = newValue
        if (newValue < 0f) newValue = 0f
        if (newValue >= 1f) newValue = 0.999f
        mDragDecelerationFrictionCoef = newValue
    }

    /**
     * ################ ################ ################ ################
     * ANIMATIONS ONLY WORK FOR API LEVEL 11 (Android 3.0.x) AND HIGHER.
     */
    /** CODE BELOW FOR PROVIDING EASING FUNCTIONS */

    /**
     * ################ ################ ################ ################
     * ANIMATIONS ONLY WORK FOR API LEVEL 11 (Android 3.0.x) AND HIGHER.
     */
    /** CODE BELOW FOR PROVIDING EASING FUNCTIONS  */
    /**
     * Animates the drawing / rendering of the chart on both x- and y-axis with
     * the specified animation time. If animate(...) is called, no further
     * calling of invalidate() is necessary to refresh the chart. ANIMATIONS
     * ONLY WORK FOR API LEVEL 11 (Android 3.0.x) AND HIGHER.
     *
     * @param durationMillisX
     * @param durationMillisY
     * @param easingX         a custom easing function to be used on the animation phase
     * @param easingY         a custom easing function to be used on the animation phase
     */
    @RequiresApi(11)
    open fun animateXY(
        durationMillisX: Int, durationMillisY: Int, easingX: EasingFunction?,
        easingY: EasingFunction?
    ) {
        mAnimator!!.animateXY(durationMillisX, durationMillisY, easingX!!, easingY!!)
    }

    /**
     * Animates the drawing / rendering of the chart on both x- and y-axis with
     * the specified animation time. If animate(...) is called, no further
     * calling of invalidate() is necessary to refresh the chart. ANIMATIONS
     * ONLY WORK FOR API LEVEL 11 (Android 3.0.x) AND HIGHER.
     *
     * @param durationMillisX
     * @param durationMillisY
     * @param easing         a custom easing function to be used on the animation phase
     */
    @RequiresApi(11)
    open fun animateXY(durationMillisX: Int, durationMillisY: Int, easing: EasingFunction?) {
        mAnimator!!.animateXY(durationMillisX, durationMillisY, easing!!)
    }

    /**
     * Animates the rendering of the chart on the x-axis with the specified
     * animation time. If animate(...) is called, no further calling of
     * invalidate() is necessary to refresh the chart. ANIMATIONS ONLY WORK FOR
     * API LEVEL 11 (Android 3.0.x) AND HIGHER.
     *
     * @param durationMillis
     * @param easing         a custom easing function to be used on the animation phase
     */
    @RequiresApi(11)
    open fun animateX(durationMillis: Int, easing: EasingFunction?) {
        mAnimator!!.animateX(durationMillis, easing!!)
    }

    /**
     * Animates the rendering of the chart on the y-axis with the specified
     * animation time. If animate(...) is called, no further calling of
     * invalidate() is necessary to refresh the chart. ANIMATIONS ONLY WORK FOR
     * API LEVEL 11 (Android 3.0.x) AND HIGHER.
     *
     * @param durationMillis
     * @param easing         a custom easing function to be used on the animation phase
     */
    @RequiresApi(11)
    open fun animateY(durationMillis: Int, easing: EasingFunction?) {
        mAnimator!!.animateY(durationMillis, easing!!)
    }

    /**
     * ################ ################ ################ ################
     * ANIMATIONS ONLY WORK FOR API LEVEL 11 (Android 3.0.x) AND HIGHER.
     */
    /** CODE BELOW FOR PREDEFINED EASING OPTIONS */

    /**
     * ################ ################ ################ ################
     * ANIMATIONS ONLY WORK FOR API LEVEL 11 (Android 3.0.x) AND HIGHER.
     */
    /** CODE BELOW FOR ANIMATIONS WITHOUT EASING */

    /**
     * ################ ################ ################ ################
     * ANIMATIONS ONLY WORK FOR API LEVEL 11 (Android 3.0.x) AND HIGHER.
     */
    /** CODE BELOW FOR PREDEFINED EASING OPTIONS  */
    /**
     * ################ ################ ################ ################
     * ANIMATIONS ONLY WORK FOR API LEVEL 11 (Android 3.0.x) AND HIGHER.
     */
    /** CODE BELOW FOR ANIMATIONS WITHOUT EASING  */
    /**
     * Animates the rendering of the chart on the x-axis with the specified
     * animation time. If animate(...) is called, no further calling of
     * invalidate() is necessary to refresh the chart. ANIMATIONS ONLY WORK FOR
     * API LEVEL 11 (Android 3.0.x) AND HIGHER.
     *
     * @param durationMillis
     */
    @RequiresApi(11)
    open fun animateX(durationMillis: Int) {
        mAnimator!!.animateX(durationMillis)
    }

    /**
     * Animates the rendering of the chart on the y-axis with the specified
     * animation time. If animate(...) is called, no further calling of
     * invalidate() is necessary to refresh the chart. ANIMATIONS ONLY WORK FOR
     * API LEVEL 11 (Android 3.0.x) AND HIGHER.
     *
     * @param durationMillis
     */
    @RequiresApi(11)
    open fun animateY(durationMillis: Int) {
        mAnimator!!.animateY(durationMillis)
    }

    /**
     * Animates the drawing / rendering of the chart on both x- and y-axis with
     * the specified animation time. If animate(...) is called, no further
     * calling of invalidate() is necessary to refresh the chart. ANIMATIONS
     * ONLY WORK FOR API LEVEL 11 (Android 3.0.x) AND HIGHER.
     *
     * @param durationMillisX
     * @param durationMillisY
     */
    @RequiresApi(11)
    open fun animateXY(durationMillisX: Int, durationMillisY: Int) {
        mAnimator!!.animateXY(durationMillisX, durationMillisY)
    }

    /**
     * ################ ################ ################ ################
     */
    /** BELOW THIS ONLY GETTERS AND SETTERS */


    /**
     * ################ ################ ################ ################
     */
    /** BELOW THIS ONLY GETTERS AND SETTERS  */
    /**
     * Returns the object representing all x-labels, this method can be used to
     * acquire the XAxis object and modify it (e.g. change the position of the
     * labels, styling, etc.)
     *
     * @return
     */
    open fun getXAxis(): XAxis? {
        return mXAxis
    }

    /**
     * Returns the default IValueFormatter that has been determined by the chart
     * considering the provided minimum and maximum values.
     *
     * @return
     */
    override fun getDefaultValueFormatter(): IValueFormatter? {
        return mDefaultValueFormatter
    }

    /**
     * set a selection listener for the chart
     *
     * @param l
     */
    open fun setOnChartValueSelectedListener(l: OnChartValueSelectedListener?) {
        mSelectionListener = l
    }

    /**
     * Sets a gesture-listener for the chart for custom callbacks when executing
     * gestures on the chart surface.
     *
     * @param l
     */
    open fun setOnChartGestureListener(l: OnChartGestureListener?) {
        mGestureListener = l
    }

    /**
     * Returns the custom gesture listener.
     *
     * @return
     */
    open fun getOnChartGestureListener(): OnChartGestureListener? {
        return mGestureListener
    }

    /**
     * returns the current y-max value across all DataSets
     *
     * @return
     */
    open fun getYMax(): Float {
        return mData!!.yMax
    }

    /**
     * returns the current y-min value across all DataSets
     *
     * @return
     */
    open fun getYMin(): Float {
        return mData!!.yMin
    }

    override fun getXChartMax(): Float {
        return mXAxis!!.mAxisMaximum
    }

    override fun getXChartMin(): Float {
        return mXAxis!!.mAxisMinimum
    }

    override fun getXRange(): Float {
        return mXAxis!!.mAxisRange
    }

    /**
     * Returns a recyclable MPPointF instance.
     * Returns the center point of the chart (the whole View) in pixels.
     *
     * @return
     */
    open fun getCenter(): MPPointF {
        return getInstance(width / 2f, height / 2f)
    }

    /**
     * Returns a recyclable MPPointF instance.
     * Returns the center of the chart taking offsets under consideration.
     * (returns the center of the content rectangle)
     *
     * @return
     */
    override fun getCenterOffsets(): MPPointF? {
        return mViewPortHandler.getContentCenter()
    }

    /**
     * Sets extra offsets (around the chart view) to be appended to the
     * auto-calculated offsets.
     *
     * @param left
     * @param top
     * @param right
     * @param bottom
     */
    open fun setExtraOffsets(left: Float, top: Float, right: Float, bottom: Float) {
        setExtraLeftOffset(left)
        setExtraTopOffset(top)
        setExtraRightOffset(right)
        setExtraBottomOffset(bottom)
    }

    /**
     * Set an extra offset to be appended to the viewport's top
     */
    open fun setExtraTopOffset(offset: Float) {
        mExtraTopOffset = convertDpToPixel(offset)
    }

    /**
     * @return the extra offset to be appended to the viewport's top
     */
    open fun getExtraTopOffset(): Float {
        return mExtraTopOffset
    }

    /**
     * Set an extra offset to be appended to the viewport's right
     */
    open fun setExtraRightOffset(offset: Float) {
        mExtraRightOffset = convertDpToPixel(offset)
    }

    /**
     * @return the extra offset to be appended to the viewport's right
     */
    open fun getExtraRightOffset(): Float {
        return mExtraRightOffset
    }

    /**
     * Set an extra offset to be appended to the viewport's bottom
     */
    open fun setExtraBottomOffset(offset: Float) {
        mExtraBottomOffset = convertDpToPixel(offset)
    }

    /**
     * @return the extra offset to be appended to the viewport's bottom
     */
    open fun getExtraBottomOffset(): Float {
        return mExtraBottomOffset
    }

    /**
     * Set an extra offset to be appended to the viewport's left
     */
    open fun setExtraLeftOffset(offset: Float) {
        mExtraLeftOffset = convertDpToPixel(offset)
    }

    /**
     * @return the extra offset to be appended to the viewport's left
     */
    open fun getExtraLeftOffset(): Float {
        return mExtraLeftOffset
    }

    /**
     * Set this to true to enable logcat outputs for the chart. Beware that
     * logcat output decreases rendering performance. Default: disabled.
     *
     * @param enabled
     */
    open fun setLogEnabled(enabled: Boolean) {
        mLogEnabled = enabled
    }

    /**
     * Returns true if log-output is enabled for the chart, fals if not.
     *
     * @return
     */
    open fun isLogEnabled(): Boolean {
        return mLogEnabled
    }

    /**
     * Sets the text that informs the user that there is no data available with
     * which to draw the chart.
     *
     * @param text
     */
    open fun setNoDataText(text: String) {
        mNoDataText = text
    }

    /**
     * Sets the color of the no data text.
     *
     * @param color
     */
    open fun setNoDataTextColor(color: Int) {
        mInfoPaint!!.color = color
    }

    /**
     * Sets the typeface to be used for the no data text.
     *
     * @param tf
     */
    open fun setNoDataTextTypeface(tf: Typeface?) {
        mInfoPaint!!.typeface = tf
    }

    /**
     * alignment of the no data text
     *
     * @param align
     */
    open fun setNoDataTextAlignment(align: Align?) {
        mInfoPaint!!.textAlign = align
    }

    /**
     * Set this to false to disable all gestures and touches on the chart,
     * default: true
     *
     * @param enabled
     */
    open fun setTouchEnabled(enabled: Boolean) {
        mTouchEnabled = enabled
    }

    /**
     * sets the marker that is displayed when a value is clicked on the chart
     *
     * @param marker
     */
    open fun setMarker(marker: IMarker?) {
        mMarker = marker
    }

    /**
     * returns the marker that is set as a marker view for the chart
     *
     * @return
     */
    open fun getMarker(): IMarker? {
        return mMarker
    }

    @Deprecated("")
    open fun setMarkerView(v: IMarker?) {
        setMarker(v)
    }

    @Deprecated("")
    open fun getMarkerView(): IMarker? {
        return getMarker()
    }

    /**
     * Sets a new Description object for the chart.
     *
     * @param desc
     */
    open fun setDescription(desc: Description?) {
        mDescription = desc
    }

    /**
     * Returns the Description object of the chart that is responsible for holding all information related
     * to the description text that is displayed in the bottom right corner of the chart (by default).
     *
     * @return
     */
    open fun getDescription(): Description? {
        return mDescription
    }

    /**
     * Returns the Legend object of the chart. This method can be used to get an
     * instance of the legend in order to customize the automatically generated
     * Legend.
     *
     * @return
     */
    open fun getLegend(): Legend? {
        return mLegend
    }

    /**
     * Returns the renderer object responsible for rendering / drawing the
     * Legend.
     *
     * @return
     */
    open fun getLegendRenderer(): LegendRenderer? {
        return mLegendRenderer
    }

    /**
     * Returns the rectangle that defines the borders of the chart-value surface
     * (into which the actual values are drawn).
     *
     * @return
     */
    override fun getContentRect(): RectF? {
        return mViewPortHandler.contentRect
    }

    /**
     * disables intercept touchevents
     */
    open fun disableScroll() {
        val parent = parent
        parent?.requestDisallowInterceptTouchEvent(true)
    }

    /**
     * enables intercept touchevents
     */
    open fun enableScroll() {
        val parent = parent
        parent?.requestDisallowInterceptTouchEvent(false)
    }

    /**
     * paint for the grid background (only line and barchart)
     */
    val PAINT_GRID_BACKGROUND = 4

    /**
     * paint for the info text that is displayed when there are no values in the
     * chart
     */
    val PAINT_INFO = 7

    /**
     * paint for the description text in the bottom right corner
     */
    val PAINT_DESCRIPTION = 11

    /**
     * paint for the hole in the middle of the pie chart
     */
    val PAINT_HOLE = 13

    /**
     * paint for the text in the middle of the pie chart
     */
    val PAINT_CENTER_TEXT = 14

    /**
     * paint used for the legend
     */
    val PAINT_LEGEND_LABEL = 18

    /**
     * set a new paint object for the specified parameter in the chart e.g.
     * Chart.PAINT_VALUES
     *
     * @param p     the new paint object
     * @param which Chart.PAINT_VALUES, Chart.PAINT_GRID, Chart.PAINT_VALUES,
     * ...
     */
    open fun setPaint(p: Paint?, which: Int) {
        when (which) {
            PAINT_INFO -> mInfoPaint = p
            PAINT_DESCRIPTION -> mDescPaint = p
        }
    }

    /**
     * Returns the paint object associated with the provided constant.
     *
     * @param which e.g. Chart.PAINT_LEGEND_LABEL
     * @return
     */
    open fun getPaint(which: Int): Paint? {
        when (which) {
            PAINT_INFO -> return mInfoPaint
            PAINT_DESCRIPTION -> return mDescPaint
        }
        return null
    }

    @Deprecated("")
    open fun isDrawMarkerViewsEnabled(): Boolean {
        return isDrawMarkersEnabled()
    }

    @Deprecated("")
    open fun setDrawMarkerViews(enabled: Boolean) {
        setDrawMarkers(enabled)
    }

    /**
     * returns true if drawing the marker is enabled when tapping on values
     * (use the setMarker(IMarker marker) method to specify a marker)
     *
     * @return
     */
    open fun isDrawMarkersEnabled(): Boolean {
        return mDrawMarkers
    }

    /**
     * Set this to true to draw a user specified marker when tapping on
     * chart values (use the setMarker(IMarker marker) method to specify a
     * marker). Default: true
     *
     * @param enabled
     */
    open fun setDrawMarkers(enabled: Boolean) {
        mDrawMarkers = enabled
    }

    /**
     * Returns the ChartData object that has been set for the chart.
     *
     * @return
     */
    override fun getData(): T? {
        return mData
    }

    /**
     * Returns the ViewPortHandler of the chart that is responsible for the
     * content area of the chart and its offsets and dimensions.
     *
     * @return
     */
    open fun getViewPortHandler(): ViewPortHandler? {
        return mViewPortHandler
    }

    /**
     * Returns the Renderer object the chart uses for drawing data.
     *
     * @return
     */
    open fun getRenderer(): DataRenderer? {
        return mRenderer
    }

    /**
     * Sets a new DataRenderer object for the chart.
     *
     * @param renderer
     */
    open fun setRenderer(renderer: DataRenderer?) {
        if (renderer != null) mRenderer = renderer
    }

    open fun getHighlighter(): IHighlighter? {
        return mHighlighter
    }

    /**
     * Sets a custom highligher object for the chart that handles / processes
     * all highlight touch events performed on the chart-view.
     *
     * @param highlighter
     */
    open fun setHighlighter(highlighter: ChartHighlighter<*>?) {
        mHighlighter = highlighter
    }

    /**
     * Returns a recyclable MPPointF instance.
     *
     * @return
     */
    override fun getCenterOfView(): MPPointF? {
        return getCenter()
    }

    /**
     * Returns the bitmap that represents the chart.
     *
     * @return
     */
    open fun getChartBitmap(): Bitmap {
        // Define a bitmap with the same size as the view
        val returnedBitmap = Bitmap.createBitmap(width, height, Bitmap.Config.RGB_565)
        // Bind a canvas to it
        val canvas = Canvas(returnedBitmap)
        // Get the view's background
        val bgDrawable = background
        if (bgDrawable != null) // has background drawable, then draw it on the canvas
            bgDrawable.draw(canvas) else  // does not have background drawable, then draw white background on
        // the canvas
            canvas.drawColor(Color.WHITE)
        // draw the view on the canvas
        draw(canvas)
        // return the bitmap
        return returnedBitmap
    }

    /**
     * Saves the current chart state with the given name to the given path on
     * the sdcard leaving the path empty "" will put the saved file directly on
     * the SD card chart is saved as a PNG image, example:
     * saveToPath("myfilename", "foldername1/foldername2");
     *
     * @param title
     * @param pathOnSD e.g. "folder1/folder2/folder3"
     * @return returns true on success, false on error
     */
    open fun saveToPath(title: String, pathOnSD: String): Boolean {
        val b = getChartBitmap()
        var stream: OutputStream? = null
        try {
            stream = FileOutputStream(
                Environment.getExternalStorageDirectory().path
                        + pathOnSD + "/" + title
                        + ".png"
            )

            /*
             * Write bitmap to file using JPEG or PNG and 40% quality hint for
             * JPEG.
             */b.compress(CompressFormat.PNG, 40, stream)
            stream.close()
        } catch (e: Exception) {
            e.printStackTrace()
            return false
        }
        return true
    }

    /**
     * Saves the current state of the chart to the gallery as an image type. The
     * compression must be set for JPEG only. 0 == maximum compression, 100 = low
     * compression (high quality). NOTE: Needs permission WRITE_EXTERNAL_STORAGE
     *
     * @param fileName        e.g. "my_image"
     * @param subFolderPath   e.g. "ChartPics"
     * @param fileDescription e.g. "Chart details"
     * @param format          e.g. Bitmap.CompressFormat.PNG
     * @param quality         e.g. 50, min = 0, max = 100
     * @return returns true if saving was successful, false if not
     */
    open fun saveToGallery(
        fileName: String,
        subFolderPath: String,
        fileDescription: String?,
        format: CompressFormat?,
        quality: Int
    ): Boolean {
        // restrain quality
        var fileName = fileName
        var quality = quality
        if (quality < 0 || quality > 100) quality = 50
        val currentTime = System.currentTimeMillis()
        val extBaseDir = Environment.getExternalStorageDirectory()
        val file = File(extBaseDir.absolutePath + "/DCIM/" + subFolderPath)
        if (!file.exists()) {
            if (!file.mkdirs()) {
                return false
            }
        }
        var mimeType: String = ""
        when (format) {
            CompressFormat.PNG -> {
                mimeType = "image/png"
                if (!fileName.endsWith(".png")) fileName += ".png"
            }
            CompressFormat.WEBP -> {
                mimeType = "image/webp"
                if (!fileName.endsWith(".webp")) fileName += ".webp"
            }
            CompressFormat.JPEG -> {
                mimeType = "image/jpeg"
                if (!(fileName.endsWith(".jpg") || fileName.endsWith(".jpeg"))) fileName += ".jpg"
            }
            else -> {
                mimeType = "image/jpeg"
                if (!(fileName.endsWith(".jpg") || fileName.endsWith(".jpeg"))) fileName += ".jpg"
            }
        }
        val filePath = file.absolutePath + "/" + fileName
        var out: FileOutputStream? = null
        try {
            out = FileOutputStream(filePath)
            val b = getChartBitmap()
            b.compress(format, quality, out)
            out.flush()
            out.close()
        } catch (e: IOException) {
            e.printStackTrace()
            return false
        }
        val size = File(filePath).length()
        val values = ContentValues(8)

        // store the details
        values.put(Images.Media.TITLE, fileName)
        values.put(Images.Media.DISPLAY_NAME, fileName)
        values.put(Images.Media.DATE_ADDED, currentTime)
        values.put(Images.Media.MIME_TYPE, mimeType)
        values.put(Images.Media.DESCRIPTION, fileDescription)
        values.put(Images.Media.ORIENTATION, 0)
        values.put(Images.Media.DATA, filePath)
        values.put(Images.Media.SIZE, size)
        return context.contentResolver.insert(Images.Media.EXTERNAL_CONTENT_URI, values) != null
    }

    /**
     * Saves the current state of the chart to the gallery as a JPEG image. The
     * filename and compression can be set. 0 == maximum compression, 100 = low
     * compression (high quality). NOTE: Needs permission WRITE_EXTERNAL_STORAGE
     *
     * @param fileName e.g. "my_image"
     * @param quality  e.g. 50, min = 0, max = 100
     * @return returns true if saving was successful, false if not
     */
    open fun saveToGallery(fileName: String, quality: Int): Boolean {
        return saveToGallery(
            fileName,
            "",
            "MPAndroidChart-Library Save",
            CompressFormat.PNG,
            quality
        )
    }

    /**
     * Saves the current state of the chart to the gallery as a PNG image.
     * NOTE: Needs permission WRITE_EXTERNAL_STORAGE
     *
     * @param fileName e.g. "my_image"
     * @return returns true if saving was successful, false if not
     */
    open fun saveToGallery(fileName: String): Boolean {
        return saveToGallery(fileName, "", "MPAndroidChart-Library Save", CompressFormat.PNG, 40)
    }

    /**
     * tasks to be done after the view is setup
     */
    protected var mJobs = ArrayList<Runnable>()

    open fun removeViewportJob(job: Runnable) {
        mJobs.remove(job)
    }

    open fun clearAllViewportJobs() {
        mJobs.clear()
    }

    /**
     * Either posts a job immediately if the chart has already setup it's
     * dimensions or adds the job to the execution queue.
     *
     * @param job
     */
    open fun addViewportJob(job: Runnable) {
        if (mViewPortHandler.hasChartDimens()) {
            post(job)
        } else {
            mJobs.add(job)
        }
    }

    /**
     * Returns all jobs that are scheduled to be executed after
     * onSizeChanged(...).
     *
     * @return
     */
    open fun getJobs(): ArrayList<Runnable>? {
        return mJobs
    }

    override fun onLayout(changed: Boolean, left: Int, top: Int, right: Int, bottom: Int) {
        for (i in 0 until childCount) {
            getChildAt(i).layout(left, top, right, bottom)
        }
    }

    override fun onMeasure(widthMeasureSpec: Int, heightMeasureSpec: Int) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec)
        val size = convertDpToPixel(50f).toInt()
        setMeasuredDimension(
            Math.max(
                suggestedMinimumWidth,
                resolveSize(
                    size,
                    widthMeasureSpec
                )
            ),
            Math.max(
                suggestedMinimumHeight,
                resolveSize(
                    size,
                    heightMeasureSpec
                )
            )
        )
    }

    override fun onSizeChanged(w: Int, h: Int, oldw: Int, oldh: Int) {
        if (mLogEnabled) Log.i(LOG_TAG, "OnSizeChanged()")
        if ((w > 0) && (h > 0) && (w < 10000) && (h < 10000)) {
            if (mLogEnabled) Log.i(LOG_TAG, "Setting chart dimens, width: $w, height: $h")
            mViewPortHandler.setChartDimens(w.toFloat(), h.toFloat())
        } else {
            if (mLogEnabled) Log.w(
                LOG_TAG,
                "*Avoiding* setting chart dimens! width: $w, height: $h"
            )
        }

        // This may cause the chart view to mutate properties affecting the view port --
        //   lets do this before we try to run any pending jobs on the view port itself
        notifyDataSetChanged()
        for (r: Runnable? in mJobs) {
            post(r)
        }
        mJobs.clear()
        super.onSizeChanged(w, h, oldw, oldh)
    }

    /**
     * Setting this to true will set the layer-type HARDWARE for the view, false
     * will set layer-type SOFTWARE.
     *
     * @param enabled
     */
    open fun setHardwareAccelerationEnabled(enabled: Boolean) {
        if (enabled) setLayerType(LAYER_TYPE_HARDWARE, null) else setLayerType(
            LAYER_TYPE_SOFTWARE,
            null
        )
    }

    override fun onDetachedFromWindow() {
        super.onDetachedFromWindow()

        //Log.i(LOG_TAG, "Detaching...");
        if (mUnbind) unbindDrawables(this)
    }

    /**
     * unbind flag
     */
    private var mUnbind = false

    /**
     * Unbind all drawables to avoid memory leaks.
     * Link: http://stackoverflow.com/a/6779164/1590502
     *
     * @param view
     */
    private fun unbindDrawables(view: View) {
        if (view.background != null) {
            view.background.callback = null
        }
        if (view is ViewGroup) {
            for (i in 0 until view.childCount) {
                unbindDrawables(view.getChildAt(i))
            }
            view.removeAllViews()
        }
    }

    /**
     * Set this to true to enable "unbinding" of drawables. When a View is detached
     * from a window. This helps avoid memory leaks.
     * Default: false
     * Link: http://stackoverflow.com/a/6779164/1590502
     *
     * @param enabled
     */
    open fun setUnbindEnabled(enabled: Boolean) {
        mUnbind = enabled
    }
}