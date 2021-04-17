package edu.hm.pedemap.map

import android.graphics.Canvas
import android.graphics.Path
import edu.hm.pedemap.BApplication
import edu.hm.pedemap.density.*
import edu.hm.pedemap.getDatabase
import edu.hm.pedemap.model.entity.DensityMapEntity
import edu.hm.pedemap.util.epochSecondTimestamp
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import org.osmdroid.views.Projection
import org.osmdroid.views.overlay.Overlay

class DensityGridOverlay(val application: BApplication) : Overlay() {

    /**
     * in meters
     */
    var gridSize = 50

    /**
     * Grid position (top-left corner)
     */
    var cellPosition: UTMLocation = DensityMapView.DEFAULT_CENTER

    var densityGrid: DensityGrid? = null

    // Make sure gridSize is dividable by cellSize
    private val alignedGridSize get() = gridSize - (gridSize % application.cellSize)

    override fun draw(canvas: Canvas?, projection: Projection?) {
        if (canvas != null && projection != null) drawToCanvas(canvas, projection)
    }

    /**
     * Get path for a single Single cell
     */
    private fun getCellPath(position: UTMLocation, projection: Projection, cellSize: Int): Path {
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
        val cellSize = application.cellSize

        val startTime = System.currentTimeMillis()
        var numberOfCells = 0

        val gridCenteringRange = (-alignedGridSize until alignedGridSize step cellSize)

        for (x in gridCenteringRange) {
            for (y in gridCenteringRange) {
                // Current drawing position
                val gridPosition = cellPosition.withOffset(y, x)

                if (densityGrid != null) {
                    val density = densityGrid!!.getDensityAt(gridPosition)

                    // Only draw cells with density > 0
                    if (density.people > 0) {
                        numberOfCells++
                        canvas.drawPath(getCellPath(gridPosition, projection, cellSize), getPaintForDensity(density))

                        // Also save density to database
                        GlobalScope.launch {
                            getDatabase(application).densityMapDao().insertDensity(
                                DensityMapEntity(
                                    0,
                                    gridPosition.northing,
                                    gridPosition.easting,
                                    epochSecondTimestamp(),
                                    density.people
                                )
                            )
                        }
                    }
                }
            }
        }

        // Log number of cells and duration
        val duration = System.currentTimeMillis() - startTime
        // log(application, LOG_LEVEL_DEBUG, "DensityGridOverlay", "$numberOfCells $duration")
    }
}
