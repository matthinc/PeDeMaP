package com.example.peopledensitymeasurementprototype.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query
import com.example.peopledensitymeasurementprototype.model.entity.DensityMapEntity

@Dao
interface DensityDao {
    @Insert
    fun insertDensity(entity: DensityMapEntity)

    @Query("DELETE FROM density_table")
    fun deleteAll()
}
