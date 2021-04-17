package edu.hm.pedemap.viewmodel

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import edu.hm.pedemap.repository.LogRepository

class LogFragmentViewModel(application: Application) : AndroidViewModel(application) {

    val logRepository = LogRepository(application)

    val logEntries get() = logRepository.entries
}
