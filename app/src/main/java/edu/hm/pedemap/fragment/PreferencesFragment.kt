package edu.hm.pedemap.fragment

import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import edu.hm.pedemap.R
import edu.hm.pedemap.databinding.FragmentPreferencesBinding
import edu.hm.pedemap.service.LocationService
import edu.hm.pedemap.util.restartService
import edu.hm.pedemap.viewmodel.PreferencesViewModel

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
            requireContext().restartService(serviceIntent)
        }

        return binding.root
    }
}
