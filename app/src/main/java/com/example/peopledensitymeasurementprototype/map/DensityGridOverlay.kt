package com.example.peopledensitymeasurementprototype.map

import android.graphics.Canvas
import android.graphics.Color
import android.graphics.Paint
import com.example.peopledensitymeasurementprototype.density.Density
import com.example.peopledensitymeasurementprototype.density.DensityGrid
import com.example.peopledensitymeasurementprototype.density.UTMLocation
import com.example.peopledensitymeasurementprototype.view.DensityMapView
import org.osmdroid.views.Projection
import org.osmdroid.views.overlay.Overlay

class DensityGridOverlay : Overlay() {

    /**
     * in meters
     */
    var gridSize = 20

    /**
     * Size of a cell in m
     */
    var cellSize = UTMLocation.CELL_SIZE

    /**
     * Grid position (top-left corner)
     */
    var cellPosition: UTMLocation = DensityMapView.DEFAULT_CENTER

    var userPosition: UTMLocation = DensityMapView.DEFAULT_CENTER

    var densityGrid: DensityGrid? = null

    override fun draw(canvas: Canvas?, projection: Projection?) {
        if (canvas != null && projection != null) drawToCanvas(canvas, projection)
    }


    private fun drawToCanvas(canvas: Canvas, projection: Projection) {

        val gridCenteringRange = (-(gridSize * 0.5).toInt() until (gridSize * 0.5).toInt() step cellSize)

        val drawSize = projection.metersToPixels(cellSize.toFloat())

        for (x in gridCenteringRange) {
            for (y in gridCenteringRange) {
                val gridPosition = cellPosition.withOffset(y, x)

                val drawX = projection.getLongPixelXFromLongitude(gridPosition.longitude()).toFloat()
                val drawY = projection.getLongPixelYFromLatitude(gridPosition.latitude()).toFloat()

                if (densityGrid != null) {
                    val density = densityGrid!!.getDensityAt(gridPosition)

                    if (density.people > 0) {
                        canvas.drawRect(drawX, drawY, drawX + drawSize, drawY - drawSize, getPaintForDensity(density))
                    }
                }

                canvas.drawCell(drawX, drawY, drawSize)
            }
        }

        // Draw user rect
        val userOffset = userPosition.withOffset(cellSize, cellSize)

        canvas.drawLine(
            projection.getLongPixelXFromLongitude(userPosition.longitude()).toFloat(),
            projection.getLongPixelYFromLatitude(userPosition.latitude()).toFloat(),
            projection.getLongPixelXFromLongitude(userPosition.longitude()).toFloat(),
            projection.getLongPixelYFromLatitude(userOffset.latitude()).toFloat(),
            BORDER_PAINT
        )

        canvas.drawLine(
            projection.getLongPixelXFromLongitude(userOffset.longitude()).toFloat(),
            projection.getLongPixelYFromLatitude(userOffset.latitude()).toFloat(),
            projection.getLongPixelXFromLongitude(userPosition.longitude()).toFloat(),
            projection.getLongPixelYFromLatitude(userOffset.latitude()).toFloat(),
            BORDER_PAINT
        )

        canvas.drawLine(
            projection.getLongPixelXFromLongitude(userOffset.longitude()).toFloat(),
            projection.getLongPixelYFromLatitude(userOffset.latitude()).toFloat(),
            projection.getLongPixelXFromLongitude(userOffset.longitude()).toFloat(),
            projection.getLongPixelYFromLatitude(userPosition.latitude()).toFloat(),
            BORDER_PAINT
        )

        canvas.drawLine(
            projection.getLongPixelXFromLongitude(userPosition.longitude()).toFloat(),
            projection.getLongPixelYFromLatitude(userPosition.latitude()).toFloat(),
            projection.getLongPixelXFromLongitude(userOffset.longitude()).toFloat(),
            projection.getLongPixelYFromLatitude(userPosition.latitude()).toFloat(),
            BORDER_PAINT
        )

        canvas.drawText(
            userPosition.toString(),
            projection.getLongPixelXFromLongitude(userPosition.longitude()).toFloat(),
            projection.getLongPixelYFromLatitude(userPosition.latitude()).toFloat() + 50,
            TEXT_PAINT
        )

    }

    private fun Canvas.drawCell(x: Float, y: Float, size: Float) {
        this.drawPoint(x, y, BORDER_PAINT)
    }

    private fun getPaintForDensity(density: Density): Paint {
        return Paint().also {
            it.style = Paint.Style.FILL
            it.color = Color.RED
        }
    }

    companion object {
        private val BORDER_PAINT = Paint().also {
            it.style = Paint.Style.STROKE
            it.strokeWidth = 2f
            it.color = Color.BLACK
        }
        private val TEXT_PAINT = Paint().also {
            it.style = Paint.Style.FILL
            it.color = Color.BLACK
            it.textSize = 30f
        }
    }
}
