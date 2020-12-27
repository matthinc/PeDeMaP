package com.example.peopledensitymeasurementprototype.model.entity

import androidx.annotation.NonNull
import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "location_table")
data class LocationEntity(
    @PrimaryKey(autoGenerate = true)
    @NonNull
    val id: Int,

    @ColumnInfo(name = "latitude")
    @NonNull
    val latitude: Double,

    @ColumnInfo(name = "longitude")
    @NonNull
    val longitude: Double,

    @ColumnInfo(name = "accuracy")
    @NonNull
    val accuracy: Float,

    @ColumnInfo(name = "timestamp")
    @NonNull
    val timestamp: Long,

    @ColumnInfo(name = "zoneId")
    @NonNull
    val zoneId: Int,

    @ColumnInfo(name = "northing")
    @NonNull
    val northing: Long,

    @ColumnInfo(name = "easting")
    @NonNull
    val easting: Long,

    @ColumnInfo(name = "northernHemisphere")
    @NonNull
    val northernHemisphere: Boolean
)

