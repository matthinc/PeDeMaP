package com.example.peopledensitymeasurementprototype.fragment

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import com.example.peopledensitymeasurementprototype.BApplication
import com.example.peopledensitymeasurementprototype.R
import com.example.peopledensitymeasurementprototype.density.UTMLocation
import com.example.peopledensitymeasurementprototype.density.toSingleProto
import com.example.peopledensitymeasurementprototype.util.*
import com.example.peopledensitymeasurementprototype.view.DensityMapView
import com.example.peopledensitymeasurementprototype.viewmodel.MapViewModel
import kotlinx.android.synthetic.main.fragment_map.view.*
import org.osmdroid.util.GeoPoint
import kotlin.random.Random

class MapFragment : Fragment() {

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.fragment_map, container, false)

        val viewModel = ViewModelProvider(this).get(MapViewModel::class.java)

        view.osm_map_view.setCurrentPosition(GeoPoint(DensityMapView.DEFAULT_CENTER.latitude(), DensityMapView.DEFAULT_CENTER.longitude()), 0f)

        val application = requireContext().applicationContext as BApplication
        view.osm_map_view.setDensityGrid(application.grid)

        view.osm_map_view.onLongClick = {
            // Send fake location
            val fakeLocation = UTMLocation.builderFromLocation(it)
                .withDeviceId(Random.nextInt())
                .withTimestamp(epochSecondTimestamp().toInt())
                .withAccuracy(20f)
                .build()

            application.sendLocationStrategy.sendSingleLocationData(fakeLocation.toSingleProto())
        }

        viewModel.lastLocation.observe(
            viewLifecycleOwner,
            Observer { location ->
                if (location != null) {
                    val locationGeoPoint = location.toGeoPoint()
                    view.osm_map_view.setCurrentPosition(locationGeoPoint, location.accuracy)
                    view.osm_map_view.controller.setCenter(locationGeoPoint)
                    view.osm_map_view.setCurrentGridLocation(
                        UTMLocation.newBuilder(
                        location.zoneId,
                        location.northing.toInt(),
                        location.easting.toInt(),
                        location.northernHemisphere
                    ).build())
                }
            }
        )

        return view
    }
}
