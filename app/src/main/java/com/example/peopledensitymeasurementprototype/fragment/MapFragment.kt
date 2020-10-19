package com.example.peopledensitymeasurementprototype.fragment

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.example.peopledensitymeasurementprototype.R
import com.example.peopledensitymeasurementprototype.view.DensityMapView
import kotlinx.android.synthetic.main.fragment_map.*
import kotlinx.android.synthetic.main.fragment_map.view.*

class MapFragment : Fragment() {

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.fragment_map, container, false)

        view.osm_map_view.setCurrentPosition(48.100, 11.100)
        view.osm_map_view.setCurrentPosition(DensityMapView.DEFAULT_CENTER)

        return view
    }
}
