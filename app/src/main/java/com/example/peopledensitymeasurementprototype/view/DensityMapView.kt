package com.example.peopledensitymeasurementprototype.view

import android.content.Context
import android.util.AttributeSet
import com.example.peopledensitymeasurementprototype.BuildConfig
import com.example.peopledensitymeasurementprototype.map.CurrentPositionMarker
import com.example.peopledensitymeasurementprototype.map.DensityGridOverlay
import org.osmdroid.config.Configuration
import org.osmdroid.tileprovider.tilesource.TileSourceFactory
import org.osmdroid.util.GeoPoint
import org.osmdroid.views.MapView

class DensityMapView(context: Context?, attrs: AttributeSet?) : MapView(context, attrs) {

    private val currentPositionMarker by lazy {
        val marker = CurrentPositionMarker(DEFAULT_CENTER, 0f)

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

        // Default position and zoom
        controller.setCenter(DEFAULT_CENTER)
        controller.setZoom(DEFAULT_ZOOM)

        gridOverlay = DensityGridOverlay()
        gridOverlay.gridSize = 25
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

    companion object {
        val DEFAULT_CENTER = GeoPoint(48.1617478, 11.586018) // Münchner Freiheit
        private const val DEFAULT_ZOOM = 18.0
    }
}
