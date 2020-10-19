package com.example.peopledensitymeasurementprototype.dao

import androidx.lifecycle.LiveData
import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query
import com.example.peopledensitymeasurementprototype.model.entity.LogEntity

@Dao
interface LogDao {

    @Insert
    fun insert(log: LogEntity)

    @Query("DELETE FROM log_table")
    fun deleteAll()

    @Query("SELECT * FROM log_table ORDER BY timestamp DESC")
    fun getAll(): LiveData<List<LogEntity>>
}
