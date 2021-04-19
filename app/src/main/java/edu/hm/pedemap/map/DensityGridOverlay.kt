package edu.hm.pedemap.map

import android.graphics.*
import edu.hm.pedemap.BApplication
import edu.hm.pedemap.density.*
import edu.hm.pedemap.getDatabase
import edu.hm.pedemap.map.drawing.renderDensityMap
import edu.hm.pedemap.model.entity.DensityMapEntity
import edu.hm.pedemap.util.epochSecondTimestamp
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import org.osmdroid.util.GeoPoint
import org.osmdroid.views.Projection
import org.osmdroid.views.overlay.Overlay

class DensityGridOverlay(val application: BApplication) : Overlay() {
    var gridSize = 50

    var gridCenterPosition: UTMLocation = DensityMapView.DEFAULT_CENTER

    var densityGrid: DensityGrid? = null

    var cacheBitmap: Bitmap? = null

    var lastDensityGridHash: Int = densityGrid.hashCode()

    var lastCenterPosition = gridCenterPosition

    override fun draw(canvas: Canvas?, projection: Projection?) {
        if (projection == null || canvas == null) return

        /*
        Drawing the density map is relatively slow due to the use of CPU rendering in osmdroid.
        To improve the general performance of the app, the density map is rendered to a bitmap
        which is then drawn to the map. The density map will only be rendered if the density grid or the
        center position changes.
         */
        if (lastDensityGridHash != densityGrid.hashCode() || lastCenterPosition != gridCenterPosition) {
            lastDensityGridHash = densityGrid.hashCode()
            lastCenterPosition = gridCenterPosition

            val bitmapSize = projection.metersToPixels(gridSize.toFloat()).toInt() + 1
            cacheBitmap = Bitmap.createBitmap(bitmapSize, bitmapSize, Bitmap.Config.ARGB_8888)

            // The bitmap cache needs its own projection
            val newProjection = Projection(
                projection.zoomLevel,
                bitmapSize,
                bitmapSize,
                GeoPoint(gridCenterPosition.latitude(), gridCenterPosition.longitude()),
                projection.orientation,
                false,
                false,
                0,
                0
            )

            // Render the density map to the bitmap
            renderDensityMap(
                cacheBitmap!!,
                densityGrid!!,
                newProjection,
                gridCenterPosition,
                application.cellSize,
                gridSize
            ) { n, e, d ->
                GlobalScope.launch {
                    getDatabase(application).densityMapDao().insertDensity(
                        DensityMapEntity(0, n, e, epochSecondTimestamp(), d.people)
                    )
                }
            }
        }

        /*
        Draw the bitmap to the map. Only the center position of the bitmap is known at this point.
        The upper left corner is defined as (center - bitmapWidth / 2, center - bitmapHeight / 2)
         */
        val bitmapX = (
            projection.getLongPixelXFromLongitude(gridCenterPosition.longitude()) -
                projection.metersToPixels(gridSize.toFloat() / 2)
            ).toInt()

        val bitmapY = (
            projection.getLongPixelYFromLatitude(gridCenterPosition.latitude()) -
                projection.metersToPixels(gridSize.toFloat() / 2)
            ).toInt()

        if (cacheBitmap != null) {
            /*
            The bitmap does not need to be re-rendered when the map's zoom level changes.
            Therefore the size of the bitmap has to be calculated.
             */
            val projectionSize = projection.metersToPixels(gridSize.toFloat()).toInt()

            val srcRect = Rect(0, 0, cacheBitmap!!.width, cacheBitmap!!.height)
            val destRect = Rect(bitmapX, bitmapY, bitmapX + projectionSize, bitmapY + projectionSize)

            canvas.drawBitmap(cacheBitmap!!, srcRect, destRect, null)
        }
    }
}
