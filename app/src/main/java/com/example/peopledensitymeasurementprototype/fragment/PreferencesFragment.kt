package com.example.peopledensitymeasurementprototype.fragment

import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import com.example.peopledensitymeasurementprototype.R
import com.example.peopledensitymeasurementprototype.databinding.FragmentPreferencesBinding
import com.example.peopledensitymeasurementprototype.service.LocationService
import com.example.peopledensitymeasurementprototype.viewmodel.PreferencesViewModel

class PreferencesFragment : Fragment() {

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val binding: FragmentPreferencesBinding = DataBindingUtil.inflate(
            inflater,
            R.layout.fragment_preferences,
            container,
            false
        )

        val viewModel = ViewModelProvider(this).get(PreferencesViewModel::class.java)
        binding.viewModel = viewModel

        // Restart location service
        binding.restartLocationService.setOnClickListener {
            val serviceIntent = Intent(context, LocationService::class.java)

            requireContext().apply {
                stopService(serviceIntent)
                startService(serviceIntent)
            }
        }

        return binding.root
    }
}
