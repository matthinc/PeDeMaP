package com.example.peopledensitymeasurementprototype.view

import android.content.Context
import android.util.AttributeSet
import com.example.peopledensitymeasurementprototype.BuildConfig
import com.example.peopledensitymeasurementprototype.density.DensityGrid
import com.example.peopledensitymeasurementprototype.map.CurrentPositionMarker
import com.example.peopledensitymeasurementprototype.map.DensityGridOverlay
import com.example.peopledensitymeasurementprototype.density.UTMLocation
import org.osmdroid.config.Configuration
import org.osmdroid.tileprovider.tilesource.TileSourceFactory
import org.osmdroid.util.GeoPoint
import org.osmdroid.views.MapView

class DensityMapView(context: Context?, attrs: AttributeSet?) : MapView(context, attrs) {

    private val currentPositionMarker by lazy {
        val marker = CurrentPositionMarker(GeoPoint(DEFAULT_CENTER.latitude(), DEFAULT_CENTER.longitude()), 0f)

        overlays.add(marker)
        marker
    }

    private val gridOverlay: DensityGridOverlay

    init {
        Configuration.getInstance().userAgentValue = BuildConfig.APPLICATION_ID

        // Set tile source
        setTileSource(TileSourceFactory.MAPNIK)

        // Enable controls
        setMultiTouchControls(false)
        zoomController.setZoomInEnabled(false)
        zoomController.setZoomOutEnabled(false)

        // Default position and zoom
        controller.setCenter(GeoPoint(DEFAULT_CENTER.latitude(), DEFAULT_CENTER.longitude()))
        controller.setZoom(DEFAULT_ZOOM)

        gridOverlay = DensityGridOverlay()
        gridOverlay.gridSize = 101
        overlays.add(gridOverlay)
    }

    /**
     * Set current position to move "my current position" marker on the map.
     * @param lat Latitude
     * @param lng Longitude
     */
    fun setCurrentPosition(lat: Double, lng: Double, acc: Float) {
        setCurrentPosition(GeoPoint(lat, lng), acc)
    }

    /**
     * Set current position to move "my current position" marker on the map.
     * @param point position as [GeoPoint]
     */
    fun setCurrentPosition(point: GeoPoint, acc: Float) {
        currentPositionMarker.position = point
        currentPositionMarker.radius = acc
    }

    fun setCurrentGridLocation(location: UTMLocation) {
        gridOverlay.cellPosition = location
        gridOverlay.userPosition = location
    }

    fun setDensityGrid(grid: DensityGrid) {
        gridOverlay.densityGrid = grid
    }

    companion object {
        val DEFAULT_CENTER = UTMLocation.newBuilder(32, 100, 100, true).build()
        private const val DEFAULT_ZOOM = 21.0
    }
}
