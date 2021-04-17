package edu.hm.pedemap

import android.content.Context
import androidx.room.Room
import androidx.room.RoomDatabase
import edu.hm.pedemap.dao.DensityDao
import edu.hm.pedemap.dao.LocationDao
import edu.hm.pedemap.dao.LogDao
import edu.hm.pedemap.model.entity.DensityMapEntity
import edu.hm.pedemap.model.entity.LocationEntity
import edu.hm.pedemap.model.entity.LogEntity

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
