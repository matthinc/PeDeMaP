package edu.hm.pedemap.util

import android.content.Context
import edu.hm.pedemap.getDatabase
import edu.hm.pedemap.model.entity.makeLogEntity
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch

fun log(ctx: Context, level: Int, category: String, message: String) {
    val db = getDatabase(ctx)

    GlobalScope.launch {
        db.logDao().insert(makeLogEntity(category, level, message))
    }
}

fun log(ctx: Context, level: Int, category: String, message: Array<Any>) {
    log(ctx, level, category, message.joinToString(";") { it.toString() })
}
