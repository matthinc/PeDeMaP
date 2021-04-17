package edu.hm.pedemap.dao

import androidx.lifecycle.LiveData
import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query
import edu.hm.pedemap.model.entity.LocationEntity

@Dao
interface LocationDao {
    @Insert
    fun insertLocation(entity: LocationEntity)

    @Query("SELECT * FROM location_table ORDER BY timestamp DESC LIMIT 1")
    fun getLastLocation(): LiveData<LocationEntity>

    @Query("DELETE FROM location_table")
    fun deleteAll()
}
