package edu.hm.pedemap.map

import android.graphics.Canvas
import android.graphics.Color
import android.graphics.Paint
import org.osmdroid.util.GeoPoint
import org.osmdroid.views.Projection
import org.osmdroid.views.overlay.Overlay

class CurrentPositionMarker(var position: GeoPoint, var radius: Float, val showRadius: Boolean) : Overlay() {

    override fun draw(canvas: Canvas?, projection: Projection?) {
        if (canvas != null && projection != null) drawToCanvas(canvas, projection)
    }

    private fun drawToCanvas(canvas: Canvas, projection: Projection) {
        val x = projection.getLongPixelXFromLongitude(position.longitude)
        val y = projection.getLongPixelYFromLatitude(position.latitude)
        val radius = projection.metersToPixels(radius)

        if (showRadius) {
            canvas.drawCircle(x.toFloat(), y.toFloat(), radius, RADIUS_CIRCLE_PAINT)
        }

        canvas.drawCircle(x.toFloat(), y.toFloat(), POSITION_CENTER_RADIUS + 2, POSITION_CENTER_SHADOW_PAINT)
        canvas.drawCircle(x.toFloat(), y.toFloat(), POSITION_CENTER_RADIUS, POSITION_CENTER_PAINT)
    }

    companion object {
        private val RADIUS_CIRCLE_PAINT = Paint().also {
            it.color = Color.BLUE
            it.strokeWidth = 2f
            it.style = Paint.Style.STROKE
        }
        private val POSITION_CENTER_PAINT = Paint().also {
            it.color = 0x038cfc
            it.alpha = 255
            it.style = Paint.Style.FILL
        }
        private val POSITION_CENTER_SHADOW_PAINT = Paint().also {
            it.color = Color.WHITE
            it.alpha = 255
            it.style = Paint.Style.FILL
        }
        private const val POSITION_CENTER_RADIUS = 10f
    }
}
