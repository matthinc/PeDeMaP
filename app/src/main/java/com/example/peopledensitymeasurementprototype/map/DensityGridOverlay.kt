package com.example.peopledensitymeasurementprototype.map

import android.graphics.Canvas
import android.graphics.Color
import android.graphics.Paint
import android.graphics.Path
import com.example.peopledensitymeasurementprototype.density.Density
import com.example.peopledensitymeasurementprototype.density.BaseDensityGrid
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

    private fun getCellPath(position: UTMLocation, projection: Projection): Path {
        fun UTMLocation.yP() = projection.getLongPixelYFromLatitude(this.latitude()).toFloat()
        fun UTMLocation.xP() = projection.getLongPixelXFromLongitude(this.longitude()).toFloat()

        return Path().apply {
            reset()
            moveTo(position.xP(), position.yP())
            lineTo(position.withOffset(0, cellSize).xP(), position.withOffset(0, cellSize).yP())
            lineTo(position.withOffset(cellSize, cellSize).xP(), position.withOffset(cellSize, cellSize).yP())
            lineTo(position.withOffset(cellSize, 0).xP(), position.withOffset(cellSize, 0).yP())
            lineTo(position.xP(), position.yP())
        }
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
                    canvas.drawPath(getCellPath(gridPosition, projection), getPaintForDensity(density))
                }

            }
        }

        canvas.drawText(
            userPosition.toString(),
            projection.getLongPixelXFromLongitude(userPosition.longitude()).toFloat(),
            projection.getLongPixelYFromLatitude(userPosition.latitude()).toFloat() + 50,
            TEXT_PAINT
        )

    }

    private fun getPaintForDensity(density: Density): Paint {
        return Paint().also {
            it.style = Paint.Style.FILL
            it.color = densityToColorMapping(density)
            it.alpha = 128
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
