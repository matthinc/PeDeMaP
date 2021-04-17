package edu.hm.pedemap.fragment

import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import edu.hm.pedemap.R
import edu.hm.pedemap.adapter.LogViewAdapter
import edu.hm.pedemap.getDatabase
import edu.hm.pedemap.model.entity.LogEntity
import edu.hm.pedemap.viewmodel.LogFragmentViewModel
import com.google.android.material.floatingactionbutton.FloatingActionButton
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch

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

            // Also delete all other values from the database
            getDatabase(requireContext()).let {
                GlobalScope.launch {
                    it.densityMapDao().deleteAll()
                    it.locationDao().deleteAll()
                }
            }
        }

        val shareLogFab = view.findViewById<FloatingActionButton>(R.id.share_log_fab)
        shareLogFab.setOnClickListener {
            val sendIntent: Intent = Intent().apply {
                action = Intent.ACTION_SEND
                putExtra(Intent.EXTRA_TEXT, viewModel.logRepository.toCSV())
                type = "text/plain"
            }

            val shareIntent = Intent.createChooser(sendIntent, null)
            startActivity(shareIntent)
        }

        return view
    }

    override fun onChanged(logs: List<LogEntity>) {
        adapter.entries.clear()
        adapter.entries.addAll(logs)
        adapter.notifyDataSetChanged()
    }
}
