package com.github.mikephil.charting.charts

import android.content.Context
import android.util.AttributeSet
import com.github.mikephil.charting.data.ScatterData
import com.github.mikephil.charting.interfaces.dataprovider.ScatterDataProvider
import com.github.mikephil.charting.renderer.ScatterChartRenderer

/**
 * The ScatterChart. Draws dots, triangles, squares and custom shapes into the
 * Chart-View. CIRCLE and SCQUARE offer the best performance, TRIANGLE has the
 * worst performance.
 *
 * @author Philipp Jahoda
 */
class ScatterChart : BarLineChartBase<ScatterData>, ScatterDataProvider {
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
        mRenderer = ScatterChartRenderer(this, mAnimator!!, mViewPortHandler)
        getXAxis()!!.setSpaceMin(0.5f)
        getXAxis()!!.setSpaceMax(0.5f)
    }

    override fun getScatterData(): ScatterData {
        return mData!!
    }

    /**
     * Predefined ScatterShapes that allow the specification of a shape a ScatterDataSet should be drawn with.
     * If a ScatterShape is specified for a ScatterDataSet, the required renderer is set.
     */
    enum class ScatterShape(private val shapeIdentifier: String) {
        SQUARE("SQUARE"), CIRCLE("CIRCLE"), TRIANGLE("TRIANGLE"), CROSS("CROSS"), X("X"), CHEVRON_UP(
            "CHEVRON_UP"
        ),
        CHEVRON_DOWN("CHEVRON_DOWN");

        override fun toString(): String {
            return shapeIdentifier
        }

        companion object {
            val allDefaultShapes: Array<ScatterShape>
                get() = arrayOf(SQUARE, CIRCLE, TRIANGLE, CROSS, X, CHEVRON_UP, CHEVRON_DOWN)
        }
    }
}