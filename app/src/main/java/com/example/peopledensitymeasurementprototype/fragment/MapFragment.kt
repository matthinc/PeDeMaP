package com.example.peopledensitymeasurementprototype.fragment

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import com.example.peopledensitymeasurementprototype.R
import com.example.peopledensitymeasurementprototype.util.toGeoPoint
import com.example.peopledensitymeasurementprototype.view.DensityMapView
import com.example.peopledensitymeasurementprototype.viewmodel.MapViewModel
import kotlinx.android.synthetic.main.fragment_map.view.*

class MapFragment : Fragment() {

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.fragment_map, container, false)

        val viewModel = ViewModelProvider(this).get(MapViewModel::class.java)

        view.osm_map_view.setCurrentPosition(DensityMapView.DEFAULT_CENTER, 0f)

        viewModel.lastLocation.observe(
            viewLifecycleOwner,
            Observer { location ->
                if (location != null) {
                    val locationGeoPoint = location.toGeoPoint()
                    view.osm_map_view.setCurrentPosition(locationGeoPoint, location.accuracy)
                    view.osm_map_view.controller.setCenter(locationGeoPoint)
                }
            }
        )

        return view
    }
}
