package com.example.peopledensitymeasurementprototype.fragment

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.observe
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.peopledensitymeasurementprototype.R
import com.example.peopledensitymeasurementprototype.adapter.LogViewAdapter
import com.example.peopledensitymeasurementprototype.model.entity.LogEntity
import com.example.peopledensitymeasurementprototype.viewmodel.LogFragmentViewModel
import com.google.android.material.floatingactionbutton.FloatingActionButton
import kotlinx.android.synthetic.main.fragment_log.*

class LogFragment : Fragment(), Observer<List<LogEntity>> {

    lateinit var adapter: LogViewAdapter

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.fragment_log, container, false)

        val viewModel = ViewModelProvider(this).get(LogFragmentViewModel::class.java)
        viewModel.logEntries.observe(viewLifecycleOwner, this)

        adapter = LogViewAdapter(mutableListOf(), requireContext())

        val logView = view.findViewById<RecyclerView>(R.id.log_view)
        logView.layoutManager = LinearLayoutManager(requireContext(), LinearLayoutManager.VERTICAL, false)
        logView.adapter = adapter

        val logFab = view.findViewById<FloatingActionButton>(R.id.log_fab)
        logFab.setOnClickListener {
            viewModel.logRepository.deleteAll()
        }

        return view
    }

    override fun onChanged(logs: List<LogEntity>) {
        adapter.entries.clear()
        adapter.entries.addAll(logs)
        adapter.notifyDataSetChanged()
    }
}
