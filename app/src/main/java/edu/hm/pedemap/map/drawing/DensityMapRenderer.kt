package edu.hm.pedemap.map.drawing

import edu.hm.pedemap.density.Density
import edu.hm.pedemap.density.UTMLocation
import edu.hm.pedemap.density.grid.DensityGrid
import edu.hm.pedemap.map.TransactionalMultiBitmap
import edu.hm.pedemap.map.getPaintForDensity
import org.osmdroid.views.Projection

fun renderDensityMap(
    bitmap: TransactionalMultiBitmap,
    grid: DensityGrid,
    projection: Projection,
    center: UTMLocation,
    cellSize: Int,
    gridSize: Int,
    densityCallback: (Int, Int, Density) -> Unit
) {
    val alignedGridSize = gridSize - (gridSize % cellSize)
    val gridCenteringRange = (-alignedGridSize until alignedGridSize step cellSize)

    for (x in gridCenteringRange) {
        for (y in gridCenteringRange) {
            val gridPosition = center.withOffset(y, x)
            val density = grid.getDensityAt(gridPosition)

            fun UTMLocation.xP() = projection.getLongPixelXFromLongitude(longitude()).toFloat()
            fun UTMLocation.yP() = projection.getLongPixelYFromLatitude(latitude()).toFloat()

            // Only draw cells with density > 0
            if (density.people > 0) {
                val path = listOf(
                    TransactionalMultiBitmap.PathPoint(gridPosition.xP(), gridPosition.yP()),
                    TransactionalMultiBitmap.PathPoint(
                        gridPosition.withOffset(0, cellSize).xP(),
                        gridPosition.withOffset(0, cellSize).yP()
                    ),
                    TransactionalMultiBitmap.PathPoint(
                        gridPosition.withOffset(cellSize, cellSize).xP(),
                        gridPosition.withOffset(cellSize, cellSize).yP()
                    ),
                    TransactionalMultiBitmap.PathPoint(
                        gridPosition.withOffset(cellSize, 0).xP(),
                        gridPosition.withOffset(cellSize, 0).yP()
                    )
                )

                bitmap.drawPath(path, getPaintForDensity(density))
                densityCallback(gridPosition.northing, gridPosition.easting, density)
            }
        }
    }
}
