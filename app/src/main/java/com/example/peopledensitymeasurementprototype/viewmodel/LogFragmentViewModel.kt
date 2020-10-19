package com.example.peopledensitymeasurementprototype.viewmodel

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import com.example.peopledensitymeasurementprototype.repository.LogRepository

class LogFragmentViewModel(application: Application) : AndroidViewModel(application) {

    val logRepository = LogRepository(application)

    val logEntries get() = logRepository.entries
}
