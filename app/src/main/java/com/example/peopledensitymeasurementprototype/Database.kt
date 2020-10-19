package com.example.peopledensitymeasurementprototype

import android.content.Context
import androidx.room.Room
import androidx.room.RoomDatabase
import com.example.peopledensitymeasurementprototype.dao.LogDao
import com.example.peopledensitymeasurementprototype.model.entity.LogEntity

@androidx.room.Database(entities = [LogEntity::class], version = 1)
abstract class Database : RoomDatabase() {
    abstract fun logDao(): LogDao
}

fun getDatabase(ctx: Context) = Room.databaseBuilder(ctx, Database::class.java, "default-database")
    .build()
