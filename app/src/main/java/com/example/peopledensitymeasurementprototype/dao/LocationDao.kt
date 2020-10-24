package com.example.peopledensitymeasurementprototype.dao

import androidx.lifecycle.LiveData
import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query
import com.example.peopledensitymeasurementprototype.model.entity.LocationEntity

@Dao
interface LocationDao {
    @Insert
    fun insertLocation(entity: LocationEntity)

    @Query("SELECT * FROM location_table ORDER BY timestamp DESC LIMIT 1")
    fun getLastLocation(): LiveData<LocationEntity>
}
