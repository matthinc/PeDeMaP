package com.example.peopledensitymeasurementprototype.repository

import android.content.Context
import com.example.peopledensitymeasurementprototype.Database
import com.example.peopledensitymeasurementprototype.getDatabase
import com.example.peopledensitymeasurementprototype.model.entity.LocationEntity
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch

class LocationRepository(ctx: Context) {

    private val db: Database = getDatabase(ctx)

    val lastLocation = db.locationDao().getLastLocation()

    fun insertLocation(location: LocationEntity) {
        GlobalScope.launch { db.locationDao().insertLocation(location) }
    }
}
