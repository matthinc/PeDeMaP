package edu.hm.pedemap.model.entity

import androidx.annotation.NonNull
import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import org.jetbrains.annotations.NotNull

@Entity(tableName = "density_table")
data class DensityMapEntity(
    @PrimaryKey(autoGenerate = true)
    @NonNull
    val id: Int,

    @ColumnInfo(name = "northing")
    @NonNull
    val northing: Int,

    @ColumnInfo(name = "easting")
    @NonNull
    val easting: Int,

    @ColumnInfo(name = "timestamp")
    @NonNull
    val timestamp: Long,

    @ColumnInfo(name = "density")
    @NotNull
    val desity: Double
)
