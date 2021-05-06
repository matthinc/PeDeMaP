package edu.hm.pedemap.statistics

import androidx.databinding.ObservableInt

class StatisticsManager {
    val messageRateCounter = RateCounter(10000)
    var numberOfUsers = ObservableInt(0)
}
