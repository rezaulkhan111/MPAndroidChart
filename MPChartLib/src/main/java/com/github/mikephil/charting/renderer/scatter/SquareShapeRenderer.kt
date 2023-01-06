package com.github.mikephil.charting.renderer.scatter

import android.graphics.Canvas
import android.graphics.Paint
import com.github.mikephil.charting.interfaces.datasets.IScatterDataSet
import com.github.mikephil.charting.utils.ColorTemplate
import com.github.mikephil.charting.utils.Utils.convertDpToPixel
import com.github.mikephil.charting.utils.ViewPortHandler

/**
 * Created by wajdic on 15/06/2016.
 * Created at Time 09:08
 */
class SquareShapeRenderer : IShapeRenderer {

    override fun renderShape(
        c: Canvas,
        dataSet: IScatterDataSet?,
        viewPortHandler: ViewPortHandler?,
        posX: Float,
        posY: Float,
        renderPaint: Paint
    ) {
        val shapeSize = dataSet!!.getScatterShapeSize()
        val shapeHalf = shapeSize / 2f
        val shapeHoleSizeHalf = convertDpToPixel(dataSet.getScatterShapeHoleRadius())
        val shapeHoleSize = shapeHoleSizeHalf * 2f
        val shapeStrokeSize = (shapeSize - shapeHoleSize) / 2f
        val shapeStrokeSizeHalf = shapeStrokeSize / 2f
        val shapeHoleColor = dataSet.getScatterShapeHoleColor()

        if (shapeSize > 0.0) {
            renderPaint.style = Paint.Style.STROKE
            renderPaint.strokeWidth = shapeStrokeSize
            c.drawRect(
                posX - shapeHoleSizeHalf - shapeStrokeSizeHalf,
                posY - shapeHoleSizeHalf - shapeStrokeSizeHalf,
                posX + shapeHoleSizeHalf + shapeStrokeSizeHalf,
                posY + shapeHoleSizeHalf + shapeStrokeSizeHalf,
                renderPaint
            )
            if (shapeHoleColor != ColorTemplate.COLOR_NONE) {
                renderPaint.style = Paint.Style.FILL
                renderPaint.color = shapeHoleColor
                c.drawRect(
                    posX - shapeHoleSizeHalf,
                    posY - shapeHoleSizeHalf,
                    posX + shapeHoleSizeHalf,
                    posY + shapeHoleSizeHalf,
                    renderPaint
                )
            }
        } else {
            renderPaint.style = Paint.Style.FILL
            c.drawRect(
                posX - shapeHalf,
                posY - shapeHalf,
                posX + shapeHalf,
                posY + shapeHalf,
                renderPaint
            )
        }
    }
}