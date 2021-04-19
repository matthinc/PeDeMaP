package edu.hm.pedemap.map.drawing

import android.graphics.Bitmap
import android.graphics.Canvas
import android.graphics.Path
import edu.hm.pedemap.density.Density
import edu.hm.pedemap.density.DensityGrid
import edu.hm.pedemap.density.UTMLocation
import edu.hm.pedemap.map.getPaintForDensity
import org.osmdroid.views.Projection

fun renderDensityMap(
    bitmap: Bitmap,
    grid: DensityGrid,
    projection: Projection,
    center: UTMLocation,
    cellSize: Int,
    gridSize: Int,
    densityCallback: (Int, Int, Density) -> Unit
) {
    val alignedGridSize = gridSize - (gridSize % cellSize)
    val gridCenteringRange = (-alignedGridSize until alignedGridSize step cellSize)

    val canvas = Canvas(bitmap)

    for (x in gridCenteringRange) {
        for (y in gridCenteringRange) {
            val gridPosition = center.withOffset(y, x)
            val density = grid.getDensityAt(gridPosition)

            fun UTMLocation.xP() = projection.getLongPixelXFromLongitude(longitude()).toFloat()
            fun UTMLocation.yP() = projection.getLongPixelYFromLatitude(latitude()).toFloat()

            // Only draw cells with density > 0
            if (density.people > 0) {
                val path = Path().apply {
                    reset()
                    moveTo(gridPosition.xP(), gridPosition.yP())
                    lineTo(gridPosition.withOffset(0, cellSize).xP(), gridPosition.withOffset(0, cellSize).yP())
                    lineTo(
                        gridPosition.withOffset(cellSize, cellSize).xP(),
                        gridPosition.withOffset(cellSize, cellSize).yP()
                    )
                    lineTo(gridPosition.withOffset(cellSize, 0).xP(), gridPosition.withOffset(cellSize, 0).yP())
                    lineTo(gridPosition.xP(), gridPosition.yP())
                }

                canvas.drawPath(path, getPaintForDensity(density))
                densityCallback(gridPosition.northing, gridPosition.easting, density)
            }
        }
    }
}
