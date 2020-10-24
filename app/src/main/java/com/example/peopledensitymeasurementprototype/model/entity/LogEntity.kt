package com.example.peopledensitymeasurementprototype.model.entity

import androidx.annotation.NonNull
import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import com.example.peopledensitymeasurementprototype.util.epochSecondTimestamp
import org.jetbrains.annotations.NotNull

@Entity(tableName = "log_table")
data class LogEntity(
    @PrimaryKey(autoGenerate = true)
    @NonNull
    val id: Int,

    @ColumnInfo(name = "category")
    @NonNull
    val category: String,

    @ColumnInfo(name = "level")
    @NonNull
    val level: Int,

    @ColumnInfo(name = "timestamp")
    @NonNull
    val timestamp: Long,

    @ColumnInfo(name = "message")
    @NotNull
    val message: String
)

const val LOG_LEVEL_DEBUG = 1
const val LOG_LEVEL_INFO = 10
const val LOG_LEVEL_WARN = 100
const val LOG_LEVEL_ERROR = 300

fun makeLogEntity(category: String, level: Int, message: String): LogEntity {
    return LogEntity(0, category, level, epochSecondTimestamp(), message)
}
