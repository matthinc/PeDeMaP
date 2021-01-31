package com.example.peopledensitymeasurementprototype

import android.content.Context
import androidx.room.Room
import androidx.room.RoomDatabase
import com.example.peopledensitymeasurementprototype.dao.DensityDao
import com.example.peopledensitymeasurementprototype.dao.LocationDao
import com.example.peopledensitymeasurementprototype.dao.LogDao
import com.example.peopledensitymeasurementprototype.model.entity.DensityMapEntity
import com.example.peopledensitymeasurementprototype.model.entity.LocationEntity
import com.example.peopledensitymeasurementprototype.model.entity.LogEntity

@androidx.room.Database(entities = [LogEntity::class, LocationEntity::class, DensityMapEntity::class], version = 2)
abstract class Database : RoomDatabase() {
    abstract fun logDao(): LogDao
    abstract fun locationDao(): LocationDao
    abstract fun densityMapDao(): DensityDao
}

private var database: Database? = null

fun getDatabase(ctx: Context): Database {
    if (database == null) {
        database = Room.databaseBuilder(ctx, Database::class.java, "default-database")
            .build()
    }
    return database!!
}
