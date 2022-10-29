package com.github.mikephil.charting.data

import com.github.mikephil.charting.interfaces.datasets.IScatterDataSet
import com.github.mikephil.charting.renderer.scatter.ChevronDownShapeRenderer
import com.github.mikephil.charting.renderer.scatter.IShapeRenderer
import com.github.mikephil.charting.renderer.scatter.SquareShapeRenderer
import com.github.mikephil.charting.utils.ColorTemplate
import java.util.ArrayList

class ScatterDataSet(yVals: MutableList<Entry?>?, label: String?) :
    LineScatterCandleRadarDataSet<Entry?>(yVals, label), IScatterDataSet {
    /**
     * the size the scattershape will have, in density pixels
     */
    private var mShapeSize = 15f

    /**
     * Renderer responsible for rendering this DataSet, default: square
     */
    protected var mShapeRenderer: IShapeRenderer? = SquareShapeRenderer()

    /**
     * The radius of the hole in the shape (applies to Square, Circle and Triangle)
     * - default: 0.0
     */
    private var mScatterShapeHoleRadius = 0f

    /**
     * Color for the hole in the shape.
     * Setting to `ColorTemplate.COLOR_NONE` will behave as transparent.
     * - default: ColorTemplate.COLOR_NONE
     */
    private var mScatterShapeHoleColor: Int = ColorTemplate.COLOR_NONE
    override fun copy(): DataSet<Entry?>? {
        val entries: MutableList<Entry?> = ArrayList()
        for (i in mEntries!!.indices) {
            entries.add(mEntries!![i]!!.copy())
        }
        val copied = ScatterDataSet(entries, getLabel())
        copy(copied)
        return copied
    }

    protected fun copy(scatterDataSet: ScatterDataSet) {
        super.copy(scatterDataSet)
        scatterDataSet.mShapeSize = mShapeSize
        scatterDataSet.mShapeRenderer = mShapeRenderer
        scatterDataSet.mScatterShapeHoleRadius = mScatterShapeHoleRadius
        scatterDataSet.mScatterShapeHoleColor = mScatterShapeHoleColor
    }

    /**
     * Sets the size in density pixels the drawn scattershape will have. This
     * only applies for non custom shapes.
     *
     * @param size
     */
    fun setScatterShapeSize(size: Float) {
        mShapeSize = size
    }

    override fun getScatterShapeSize(): Float {
        return mShapeSize
    }

    /**
     * Sets the ScatterShape this DataSet should be drawn with. This will search for an available IShapeRenderer and set this
     * renderer for the DataSet.
     *
     * @param shape
     */
    fun setScatterShape(shape: ScatterShape?) {
        mShapeRenderer = getRendererForShape(shape)
    }

    /**
     * Sets a new IShapeRenderer responsible for drawing this DataSet.
     * This can also be used to set a custom IShapeRenderer aside from the default ones.
     *
     * @param shapeRenderer
     */
    fun setShapeRenderer(shapeRenderer: IShapeRenderer?) {
        mShapeRenderer = shapeRenderer
    }

    override fun getShapeRenderer(): IShapeRenderer? {
        return mShapeRenderer
    }

    /**
     * Sets the radius of the hole in the shape (applies to Square, Circle and Triangle)
     * Set this to <= 0 to remove holes.
     *
     * @param holeRadius
     */
    fun setScatterShapeHoleRadius(holeRadius: Float) {
        mScatterShapeHoleRadius = holeRadius
    }

    override fun getScatterShapeHoleRadius(): Float {
        return mScatterShapeHoleRadius
    }

    /**
     * Sets the color for the hole in the shape.
     *
     * @param holeColor
     */
    fun setScatterShapeHoleColor(holeColor: Int) {
        mScatterShapeHoleColor = holeColor
    }

    override fun getScatterShapeHoleColor(): Int {
        return mScatterShapeHoleColor
    }

    companion object {
        fun getRendererForShape(shape: ScatterShape?): IShapeRenderer? {
            when (shape) {
                ScatterShape.SQUARE -> return SquareShapeRenderer()
                ScatterShape.CIRCLE -> return CircleShapeRenderer()
                ScatterShape.TRIANGLE -> return TriangleShapeRenderer()
                ScatterShape.CROSS -> return CrossShapeRenderer()
                ScatterShape.X -> return XShapeRenderer()
                ScatterShape.CHEVRON_UP -> return ChevronUpShapeRenderer()
                ScatterShape.CHEVRON_DOWN -> return ChevronDownShapeRenderer()
            }
            return null
        }
    }
}