package com.example.peopledensitymeasurementprototype.view

import android.content.Context
import android.location.Location
import android.util.AttributeSet
import com.example.peopledensitymeasurementprototype.BuildConfig
import com.example.peopledensitymeasurementprototype.density.DensityGrid
import com.example.peopledensitymeasurementprototype.density.UTMLocation
import com.example.peopledensitymeasurementprototype.map.CurrentPositionMarker
import com.example.peopledensitymeasurementprototype.map.DensityGridOverlay
import com.example.peopledensitymeasurementprototype.util.bApplication
import org.osmdroid.config.Configuration
import org.osmdroid.events.MapEventsReceiver
import org.osmdroid.tileprovider.tilesource.TileSourceFactory
import org.osmdroid.util.GeoPoint
import org.osmdroid.views.MapView
import org.osmdroid.views.overlay.MapEventsOverlay

class DensityMapView(context: Context?, attrs: AttributeSet?) : MapView(context, attrs), MapEventsReceiver {

    var onLongClick: (Location) -> Unit = {}

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
        setMultiTouchControls(true)
        zoomController.setZoomInEnabled(true)
        zoomController.setZoomOutEnabled(true)

        // Default position and zoom
        controller.setCenter(GeoPoint(DEFAULT_CENTER.latitude(), DEFAULT_CENTER.longitude()))
        controller.setZoom(DEFAULT_ZOOM)

        gridOverlay = DensityGridOverlay(context!!.bApplication())
        gridOverlay.gridSize = 101
        overlays.add(gridOverlay)

        // Receive events
        overlays.add(MapEventsOverlay(this))
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
    }

    fun setDensityGrid(grid: DensityGrid) {
        gridOverlay.densityGrid = grid
    }

    fun redrawDensity() {
        controller.setZoom(DEFAULT_ZOOM)
    }

    companion object {
        val DEFAULT_CENTER = UTMLocation.newBuilder(32, 100, 100, true).build()
        private const val DEFAULT_ZOOM = 20.0
    }

    override fun longPressHelper(p: GeoPoint?): Boolean {

        p?.let {
            val location = Location("fake")
            location.latitude = it.latitude
            location.longitude = it.longitude
            location.accuracy = 15f
            onLongClick(location)
        }

        return true
    }

    override fun singleTapConfirmedHelper(p: GeoPoint?): Boolean {
        return true
    }
}
