package edu.hm.pedemap.util

import android.content.Context
import android.util.Log
import edu.hm.pedemap.getDatabase
import edu.hm.pedemap.model.entity.makeLogEntity
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch

fun log(ctx: Context, level: Int, category: String, message: String) {
    val db = getDatabase(ctx)

    GlobalScope.launch {
        db.logDao().insert(makeLogEntity(category, level, message))
    }

    Log.d("LogMessage", "$level - $category:\n$message\n")
}

fun log(ctx: Context, level: Int, category: String, message: Array<Any>) {
    log(ctx, level, category, message.joinToString(";") { it.toString() })
}
