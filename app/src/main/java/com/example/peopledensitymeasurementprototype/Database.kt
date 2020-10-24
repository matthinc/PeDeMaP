package com.example.peopledensitymeasurementprototype

import android.content.Context
import androidx.room.Room
import androidx.room.RoomDatabase
import com.example.peopledensitymeasurementprototype.dao.LocationDao
import com.example.peopledensitymeasurementprototype.dao.LogDao
import com.example.peopledensitymeasurementprototype.model.entity.LocationEntity
import com.example.peopledensitymeasurementprototype.model.entity.LogEntity

@androidx.room.Database(entities = [LogEntity::class, LocationEntity::class], version = 1)
abstract class Database : RoomDatabase() {
    abstract fun logDao(): LogDao
    abstract fun locationDao(): LocationDao
}

private var database: Database? = null

fun getDatabase(ctx: Context): Database {
    if (database == null) {
        database = Room.databaseBuilder(ctx, Database::class.java, "default-database")
            .build()
    }
    return database!!
}
