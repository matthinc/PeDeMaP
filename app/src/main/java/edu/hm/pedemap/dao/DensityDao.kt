package edu.hm.pedemap.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query
import edu.hm.pedemap.model.entity.DensityMapEntity

@Dao
interface DensityDao {
    @Insert
    fun insertDensity(entity: DensityMapEntity)

    @Query("DELETE FROM density_table")
    fun deleteAll()
}
