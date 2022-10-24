package com.xxmassdeveloper.mpchartexample.custom

import android.graphics.Canvas
import android.graphics.Paint
import com.github.mikephil.charting.renderer.scatter.IShapeRenderer

/**
 * Custom shape renderer that draws a single line.
 * Created by philipp on 26/06/16.
 */
class CustomScatterShapeRenderer : IShapeRenderer {
    fun renderShape(
        c: Canvas, dataSet: IScatterDataSet, viewPortHandler: ViewPortHandler?,
        posX: Float, posY: Float, renderPaint: Paint?
    ) {
        val shapeHalf: Float = dataSet.scatterShapeSize / 2f
        c.drawLine(
            posX - shapeHalf,
            posY - shapeHalf,
            posX + shapeHalf,
            posY + shapeHalf,
            renderPaint!!
        )
    }
}