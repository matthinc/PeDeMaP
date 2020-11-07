package com.example.peopledensitymeasurementprototype.repository

import android.content.Context
import androidx.lifecycle.LiveData
import com.example.peopledensitymeasurementprototype.Database
import com.example.peopledensitymeasurementprototype.getDatabase
import com.example.peopledensitymeasurementprototype.model.entity.LogEntity
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch

class LogRepository(ctx: Context) {

    private val db: Database = getDatabase(ctx)

    val entries: LiveData<List<LogEntity>> = db.logDao().getAll()

    fun insert(log: LogEntity) {
        GlobalScope.launch { db.logDao().insert(log) }
    }

    fun deleteAll() {
        GlobalScope.launch { db.logDao().deleteAll() }
    }

    fun toCSV(): String {
        return entries.value
            ?.joinToString("\n") { "${it.id};${it.timestamp};${it.level};${it.category};${it.message}" }
            ?:""
    }
}
