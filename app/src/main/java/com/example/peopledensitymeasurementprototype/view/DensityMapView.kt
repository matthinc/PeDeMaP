package com.example.peopledensitymeasurementprototype.view

import android.content.Context
import android.util.AttributeSet
import com.example.peopledensitymeasurementprototype.BuildConfig
import com.example.peopledensitymeasurementprototype.R
import com.example.peopledensitymeasurementprototype.map.DensityGridOverlay
import org.osmdroid.config.Configuration
import org.osmdroid.tileprovider.tilesource.TileSourceFactory
import org.osmdroid.util.GeoPoint
import org.osmdroid.views.MapView
import org.osmdroid.views.overlay.Marker

class DensityMapView(context: Context?, attrs: AttributeSet?) : MapView(context, attrs) {

    private val currentPositionMarker by lazy {
        val marker = Marker(this)

        marker.subDescription = context?.getString(R.string.current_position_text)
        marker.setAnchor(Marker.ANCHOR_CENTER, Marker.ANCHOR_BOTTOM)

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
    fun setCurrentPosition(lat: Double, lng: Double) {
        setCurrentPosition(GeoPoint(lat, lng))
    }

    /**
     * Set current position to move "my current position" marker on the map.
     * @param point position as [GeoPoint]
     */
    fun setCurrentPosition(point: GeoPoint) {
        currentPositionMarker.position = point
    }

    companion object {
        val DEFAULT_CENTER = GeoPoint(48.1617478, 11.586018) // Münchner Freiheit
        private const val DEFAULT_ZOOM = 20.0
    }
}
