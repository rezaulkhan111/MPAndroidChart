package com.github.mikephil.charting.interfaces.datasets

import com.github.mikephil.charting.data.Entry
import com.github.mikephil.charting.renderer.scatter.IShapeRenderer

/**
 * Created by philipp on 21/10/15.
 */
interface IScatterDataSet : ILineScatterCandleRadarDataSet<Entry> {
    /**
     * Returns the currently set scatter shape size
     *
     * @return
     */
    fun getScatterShapeSize(): Float

    /**
     * Returns radius of the hole in the shape
     *
     * @return
     */
    fun getScatterShapeHoleRadius(): Float

    /**
     * Returns the color for the hole in the shape
     *
     * @return
     */
    fun getScatterShapeHoleColor(): Int

    /**
     * Returns the IShapeRenderer responsible for rendering this DataSet.
     *
     * @return
     */
    fun getShapeRenderer(): IShapeRenderer?
}