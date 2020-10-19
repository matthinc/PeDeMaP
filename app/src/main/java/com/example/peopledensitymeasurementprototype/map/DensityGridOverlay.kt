package com.example.peopledensitymeasurementprototype.map

import android.graphics.Canvas
import android.graphics.Color
import android.graphics.Paint
import com.example.peopledensitymeasurementprototype.view.DensityMapView
import org.osmdroid.views.Projection
import org.osmdroid.views.overlay.Overlay

class DensityGridOverlay : Overlay() {

    /**
     * n x n cells
     * For example [gridSize] = 5 -> 25 cells total
     */
    var gridSize = 5

    /**
     * Size of a cell in m
     */
    var cellSize = 5f

    /**
     * Grid position (top-left corner)
     */
    var cellPosition = DensityMapView.DEFAULT_CENTER

    override fun draw(canvas: Canvas?, projection: Projection?) {
        if (canvas != null && projection != null) drawToCanvas(canvas, projection)
    }

    private fun drawToCanvas(canvas: Canvas, projection: Projection) {
        for (x in 0 until gridSize) {
            for (y in 0 until gridSize) {

                val drawX = projection.getLongPixelXFromLongitude(cellPosition.longitude) +
                    projection.metersToPixels(x.toFloat())

                val drawY = projection.getLongPixelYFromLatitude(cellPosition.latitude) +
                    projection.metersToPixels(y.toFloat())

                val drawSize = projection.metersToPixels(cellSize)

                canvas.drawCell(drawX, drawY, drawSize)
            }
        }
    }

    private fun Canvas.drawCell(x: Float, y: Float, size: Float) {
        this.drawRect(x, y, x + size, y + size, BORDER_PAINT)
    }

    companion object {
        private val BORDER_PAINT = Paint().also {
            it.style = Paint.Style.STROKE
            it.strokeWidth = 5f
            it.color = Color.BLACK
        }
    }
}
