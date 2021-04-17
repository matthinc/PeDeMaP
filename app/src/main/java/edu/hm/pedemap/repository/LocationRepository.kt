package edu.hm.pedemap.repository

import android.content.Context
import edu.hm.pedemap.Database
import edu.hm.pedemap.getDatabase
import edu.hm.pedemap.model.entity.LocationEntity
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch

class LocationRepository(ctx: Context) {

    private val db: Database = getDatabase(ctx)

    val lastLocation = db.locationDao().getLastLocation()

    fun insertLocation(location: LocationEntity) {
        GlobalScope.launch { db.locationDao().insertLocation(location) }
    }
}
