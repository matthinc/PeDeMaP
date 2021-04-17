package edu.hm.pedemap.dao

import androidx.lifecycle.LiveData
import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query
import edu.hm.pedemap.model.entity.LogEntity

@Dao
interface LogDao {

    @Insert
    fun insert(log: LogEntity)

    @Query("DELETE FROM log_table")
    fun deleteAll()

    @Query("SELECT * FROM log_table ORDER BY timestamp DESC")
    fun getAll(): LiveData<List<LogEntity>>
}
