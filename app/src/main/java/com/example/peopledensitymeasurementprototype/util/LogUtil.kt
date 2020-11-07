package com.example.peopledensitymeasurementprototype.util

import android.content.Context
import com.example.peopledensitymeasurementprototype.getDatabase
import com.example.peopledensitymeasurementprototype.model.entity.makeLogEntity
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import java.util.*

fun log(ctx: Context, level: Int, category: String, message: String) {
    val db = getDatabase(ctx)

    GlobalScope.launch {
        db.logDao().insert(makeLogEntity(category, level, message))
    }
}

fun log(ctx: Context, level: Int, category: String, message: Array<Any>) {
    log(ctx, level, category, message.joinToString(";") { it.toString() })
}
