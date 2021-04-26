package edu.hm.pedemap.fragment

import android.app.AlertDialog
import android.location.Location
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import edu.hm.pedemap.BApplication
import edu.hm.pedemap.BuildConfig
import edu.hm.pedemap.R
import edu.hm.pedemap.density.UTMLocation
import edu.hm.pedemap.density.toSingleProto
import edu.hm.pedemap.map.DensityMapView
import edu.hm.pedemap.messages.WarnMessage
import edu.hm.pedemap.util.*
import edu.hm.pedemap.viewmodel.MapViewModel
import kotlinx.android.synthetic.main.fragment_map.view.*
import org.osmdroid.util.GeoPoint
import java.util.*
import kotlin.random.Random

class MapFragment : Fragment() {

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.fragment_map, container, false)

        val viewModel = ViewModelProvider(this).get(MapViewModel::class.java)

        view.osm_map_view.setCurrentPosition(
            GeoPoint(DensityMapView.DEFAULT_CENTER.latitude(), DensityMapView.DEFAULT_CENTER.longitude()),
            0f
        )

        val application = requireContext().applicationContext as BApplication
        view.osm_map_view.setDensityGrid(application.grid)

        view.osm_map_view.onLongClick = {
            debugMenu(application, it)
        }

        application.warnMessageManager.observer = {
            view.osm_map_view.setWarnMessages(it.getAll())
        }

        application.grid.observer = {
            view.osm_map_view.redrawDensity()
        }

        // Perform aging process every second to update view
        Timer().scheduleAtFixedRate(
            object : TimerTask() {
                override fun run() {
                    this@MapFragment.activity?.runOnUiThread {
                        application.grid.purge()
                    }
                }
            },
            1000,
            1000
        )

        viewModel.lastLocation.observe(
            viewLifecycleOwner,
            Observer { location ->
                if (location != null) {
                    val locationGeoPoint = location.toGeoPoint()
                    view.osm_map_view.setCurrentPosition(locationGeoPoint, location.accuracy)
                    view.osm_map_view.setCurrentGridLocation(
                        UTMLocation.newBuilder(
                            location.zoneId,
                            location.northing.toInt(),
                            location.easting.toInt(),
                            location.northernHemisphere
                        ).build()
                    )
                }
            }
        )

        return view
    }

    private fun debugMenu(application: BApplication, loc: Location) {
        if (BuildConfig.FLAVOR == "demo_ui") return

        fun sendFakeLocation() {
            val fakeLocation = UTMLocation.builderFromLocation(loc, requireContext().bApplication().cellSize)
                .withDeviceId(Random.nextInt())
                .withTimestamp(epochSecondTimestamp())
                .withAccuracy(10f)
                .withTTL(60)
                .build()

            application.sendLocationStrategy.sendSingleLocationData(fakeLocation.toSingleProto())
        }

        fun sendWarnMessage() {
            val message = WarnMessage(
                "Warn message from ${requireContext().bApplication().getDeviceId()}",
                epochSecondTimestamp() + 60,
                loc.latitude,
                loc.longitude
            )

            application.sendLocationStrategy.sendWarnMessage(message.toProto())
        }

        val builder = AlertDialog.Builder(context).apply {
            setTitle("Send...")
            setItems(arrayOf("Fake location", "Warn message")) { dialog, which ->
                when (which) {
                    0 -> sendFakeLocation()
                    1 -> sendWarnMessage()
                }
                dialog.cancel()
            }
        }
        builder.show()
    }
}
