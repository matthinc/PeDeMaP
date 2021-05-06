package edu.hm.pedemap.statistics

import androidx.databinding.ObservableFloat

class RateCounter(val window: Long) {
    private var timer = System.currentTimeMillis()
    private var counter: Long = 0

    val rate = ObservableFloat(0f)

    fun reset(counter: Long = 0) {
        this.counter = counter
        resetTimer()
    }

    fun countEvent() {
        updateRate()
        counter++
    }

    fun updateRate() {
        if (elapsed() >= window) {
            rate.set(calculateRate())
            reset(0)
        }
    }

    private fun calculateRate(): Float {
        return ((counter * 1_000) / elapsed().toFloat())
    }

    private fun elapsed(): Long {
        return System.currentTimeMillis() - timer
    }

    private fun resetTimer() {
        timer = System.currentTimeMillis()
    }
}
