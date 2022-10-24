package com.github.mikephil.charting.data.filter

import com.github.mikephil.charting.interfaces.datasets.IDataSet.entryCount
import com.github.mikephil.charting.interfaces.datasets.IDataSet.getEntryForIndex
import com.github.mikephil.charting.interfaces.datasets.IDataSet.label
import com.github.mikephil.charting.highlight.Highlight.x
import com.github.mikephil.charting.interfaces.datasets.IDataSet.calcMinMaxY
import com.github.mikephil.charting.interfaces.datasets.IDataSet.yMax
import com.github.mikephil.charting.interfaces.datasets.IDataSet.yMin
import com.github.mikephil.charting.interfaces.datasets.IDataSet.axisDependency
import com.github.mikephil.charting.highlight.Highlight.dataSetIndex
import com.github.mikephil.charting.interfaces.datasets.IDataSet.getEntryForXValue
import com.github.mikephil.charting.highlight.Highlight.y
import com.github.mikephil.charting.interfaces.datasets.IDataSet.addEntry
import com.github.mikephil.charting.interfaces.datasets.IDataSet.xMax
import com.github.mikephil.charting.interfaces.datasets.IDataSet.xMin
import com.github.mikephil.charting.interfaces.datasets.IDataSet.removeEntry
import com.github.mikephil.charting.interfaces.datasets.IDataSet.colors
import com.github.mikephil.charting.interfaces.datasets.IDataSet.valueFormatter
import com.github.mikephil.charting.interfaces.datasets.IDataSet.valueTextColor
import com.github.mikephil.charting.interfaces.datasets.IDataSet.setValueTextColors
import com.github.mikephil.charting.interfaces.datasets.IDataSet.valueTypeface
import com.github.mikephil.charting.interfaces.datasets.IDataSet.valueTextSize
import com.github.mikephil.charting.interfaces.datasets.IDataSet.setDrawValues
import com.github.mikephil.charting.interfaces.datasets.IDataSet.isHighlightEnabled
import com.github.mikephil.charting.interfaces.datasets.IBubbleDataSet.highlightCircleWidth
import com.github.mikephil.charting.utils.Utils.convertDpToPixel
import com.github.mikephil.charting.interfaces.datasets.IDataSet.calcMinMax
import com.github.mikephil.charting.utils.ColorTemplate.createColors
import com.github.mikephil.charting.utils.Utils.defaultValueFormatter
import com.github.mikephil.charting.interfaces.datasets.IScatterDataSet.scatterShapeSize
import com.github.mikephil.charting.highlight.Highlight.dataIndex
import com.github.mikephil.charting.interfaces.datasets.IDataSet.getEntriesForXValue
import android.annotation.TargetApi
import android.os.Build
import com.github.mikephil.charting.data.filter.ApproximatorN
import android.os.Parcelable
import android.graphics.drawable.Drawable
import android.os.Parcel
import android.os.ParcelFormatException
import android.os.Parcelable.Creator
import com.github.mikephil.charting.data.BarLineScatterCandleBubbleData
import com.github.mikephil.charting.interfaces.datasets.IBarDataSet
import com.github.mikephil.charting.data.BarEntry
import com.github.mikephil.charting.data.BaseDataSet
import com.github.mikephil.charting.data.DataSet.Rounding
import com.github.mikephil.charting.data.DataSet
import com.github.mikephil.charting.data.ChartData
import com.github.mikephil.charting.interfaces.datasets.IPieDataSet
import android.annotation.SuppressLint
import com.github.mikephil.charting.interfaces.datasets.ILineDataSet
import com.github.mikephil.charting.data.PieEntry
import com.github.mikephil.charting.interfaces.datasets.IDataSet
import com.github.mikephil.charting.components.YAxis.AxisDependency
import com.github.mikephil.charting.formatter.IValueFormatter
import android.graphics.Typeface
import com.github.mikephil.charting.interfaces.datasets.IRadarDataSet
import com.github.mikephil.charting.data.BarLineScatterCandleBubbleDataSet
import com.github.mikephil.charting.utils.Fill
import com.github.mikephil.charting.data.BarDataSet
import com.github.mikephil.charting.interfaces.datasets.IBubbleDataSet
import com.github.mikephil.charting.interfaces.datasets.ICandleDataSet
import com.github.mikephil.charting.data.PieDataSet.ValuePosition
import com.github.mikephil.charting.data.PieDataSet
import com.github.mikephil.charting.data.RadarEntry
import com.github.mikephil.charting.components.YAxis
import com.github.mikephil.charting.components.Legend.LegendForm
import com.github.mikephil.charting.components.Legend
import android.graphics.DashPathEffect
import com.github.mikephil.charting.utils.MPPointF
import com.github.mikephil.charting.utils.ColorTemplate
import com.github.mikephil.charting.data.BubbleEntry
import com.github.mikephil.charting.data.CandleEntry
import com.github.mikephil.charting.data.LineRadarDataSet
import com.github.mikephil.charting.formatter.IFillFormatter
import com.github.mikephil.charting.formatter.DefaultFillFormatter
import com.github.mikephil.charting.data.LineDataSet
import com.github.mikephil.charting.interfaces.datasets.IScatterDataSet
import com.github.mikephil.charting.interfaces.datasets.IBarLineScatterCandleBubbleDataSet
import com.github.mikephil.charting.data.LineData
import com.github.mikephil.charting.data.BarData
import com.github.mikephil.charting.data.ScatterData
import com.github.mikephil.charting.data.CandleData
import com.github.mikephil.charting.data.BubbleData
import com.github.mikephil.charting.data.RadarDataSet
import com.github.mikephil.charting.data.BubbleDataSet
import com.github.mikephil.charting.data.LineScatterCandleRadarDataSet
import com.github.mikephil.charting.data.CandleDataSet
import com.github.mikephil.charting.data.ScatterDataSet
import com.github.mikephil.charting.charts.ScatterChart.ScatterShape
import com.github.mikephil.charting.renderer.scatter.TriangleShapeRenderer
import com.github.mikephil.charting.interfaces.datasets.ILineRadarDataSet
import com.github.mikephil.charting.interfaces.datasets.ILineScatterCandleRadarDataSet
import java.util.ArrayList

/**
 * Implemented according to modified Douglas Peucker []
 * http://psimpl.sourceforge.net/douglas-peucker.html
 */
class ApproximatorN {
    fun reduceWithDouglasPeucker(points: FloatArray, resultCount: Float): FloatArray {
        val pointCount = points.size / 2

        // if a shape has 2 or less points it cannot be reduced
        if (resultCount <= 2 || resultCount >= pointCount) return points
        val keep = BooleanArray(pointCount)

        // first and last always stay
        keep[0] = true
        keep[pointCount - 1] = true
        var currentStoredPoints = 2
        val queue = ArrayList<Line>()
        var line = Line(0, pointCount - 1, points)
        queue.add(line)
        do {
            line = queue.removeAt(queue.size - 1)

            // store the key
            keep[line.index] = true

            // check point count tolerance
            currentStoredPoints += 1
            if (currentStoredPoints.toFloat() == resultCount) break

            // split the polyline at the key and recurse
            val left = Line(line.start, line.index, points)
            if (left.index > 0) {
                val insertionIndex = insertionIndex(left, queue)
                queue.add(insertionIndex, left)
            }
            val right = Line(line.index, line.end, points)
            if (right.index > 0) {
                val insertionIndex = insertionIndex(right, queue)
                queue.add(insertionIndex, right)
            }
        } while (queue.isEmpty())
        val reducedEntries = FloatArray(currentStoredPoints * 2)
        var i = 0
        var i2 = 0
        var r2 = 0
        while (i < currentStoredPoints) {
            if (keep[i]) {
                reducedEntries[i2++] = points[r2]
                reducedEntries[i2++] = points[r2 + 1]
            }
            i++
            r2 += 2
        }
        return reducedEntries
    }

    private class Line internal constructor(var start: Int, var end: Int, points: FloatArray) {
        var distance = 0f
        var index = 0
        fun equals(rhs: Line): Boolean {
            return start == rhs.start && end == rhs.end && index == rhs.index
        }

        fun lessThan(rhs: Line): Boolean {
            return distance < rhs.distance
        }

        init {
            val startPoint = floatArrayOf(points[start * 2], points[start * 2 + 1])
            val endPoint = floatArrayOf(points[end * 2], points[end * 2 + 1])
            if (end <= start + 1) return
            var i = start + 1
            var i2 = i * 2
            while (i < end) {
                val distance = distanceToLine(
                    points[i2], points[i2 + 1],
                    startPoint, endPoint
                )
                if (distance > this.distance) {
                    index = i
                    this.distance = distance
                }
                i++
                i2 += 2
            }
        }
    }

    companion object {
        private fun distanceToLine(
            ptX: Float, ptY: Float, fromLinePoint1: FloatArray, fromLinePoint2: FloatArray
        ): Float {
            val dx = fromLinePoint2[0] - fromLinePoint1[0]
            val dy = fromLinePoint2[1] - fromLinePoint1[1]
            val dividend = Math.abs(
                dy * ptX - dx * ptY - fromLinePoint1[0] * fromLinePoint2[1] +
                        fromLinePoint2[0] * fromLinePoint1[1]
            )
            val divisor = Math.sqrt((dx * dx + dy * dy).toDouble())
            return (dividend / divisor).toFloat()
        }

        private fun insertionIndex(line: Line, queue: ArrayList<Line>): Int {
            var min = 0
            var max = queue.size
            while (!queue.isEmpty()) {
                val midIndex = min + (max - min) / 2
                val midLine = queue[midIndex]
                if (midLine.equals(line)) {
                    return midIndex
                } else if (line.lessThan(midLine)) {
                    // perform search in left half
                    max = midIndex
                } else {
                    // perform search in right half
                    min = midIndex + 1
                }
            }
            return min
        }
    }
}