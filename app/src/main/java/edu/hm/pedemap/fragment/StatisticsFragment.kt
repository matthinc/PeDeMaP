package edu.hm.pedemap.fragment

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.Observable
import androidx.databinding.ObservableFloat
import androidx.databinding.ObservableInt
import androidx.fragment.app.Fragment
import edu.hm.pedemap.R
import edu.hm.pedemap.util.bApplication
import kotlinx.android.synthetic.main.fragment_statistics.view.*
import java.util.*
import kotlin.concurrent.fixedRateTimer

class StatisticsFragment : Fragment() {

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment

        val view = inflater.inflate(R.layout.fragment_statistics, container, false)

        fixedRateTimer("Update Rate Timer", false, 0L, 10_000) {
            activity?.runOnUiThread {
                requireContext().bApplication().statisticsManager.messageRateCounter.updateRate()
            }
        }

        requireContext().bApplication().statisticsManager.numberOfUsers.addOnPropertyChangedCallback(
            object : Observable.OnPropertyChangedCallback() {
                override fun onPropertyChanged(sender: Observable?, propertyId: Int) {
                    view.statistics_users.text = (sender as ObservableInt).get().toString()
                }
            }
        )

        requireContext().bApplication().statisticsManager.messageRateCounter.rate.addOnPropertyChangedCallback(
            object : Observable.OnPropertyChangedCallback() {
                override fun onPropertyChanged(sender: Observable?, propertyId: Int) {
                    view.statistics_rate.text = "${((sender as ObservableFloat).get() * 60).toInt()} msg/min"
                }
            }
        )

        return view
    }
}
