package edu.hm.pedemap.map

import android.content.Context
import android.graphics.Canvas
import android.graphics.Color
import android.graphics.Paint
import android.graphics.Rect
import androidx.core.graphics.drawable.toBitmap
import edu.hm.pedemap.R
import org.osmdroid.views.Projection
import org.osmdroid.views.overlay.Overlay

class LogoOverlay(val context: Context) : Overlay() {

    val hmLogoBitmap = context.getDrawable(R.drawable.hm_logo)?.toBitmap()
    val bmbfLogoBitmap = context.getDrawable(R.drawable.bmbf_logo)?.toBitmap()

    override fun draw(canvas: Canvas, pProjection: Projection?) {
        val logoHeight = canvas.height / 9

        canvas.drawRect(Rect(0, canvas.height - logoHeight - 20, canvas.width, canvas.height), BACKGROUND_PAINT)

        val hmLogoWidth = ((hmLogoBitmap!!.width.toFloat() / hmLogoBitmap.height) * logoHeight).toInt()
        canvas.drawBitmap(
            hmLogoBitmap,
            null,
            Rect(10, canvas.height - logoHeight - 10, hmLogoWidth + 10, canvas.height - 10),
            null
        )

        val bmbfLogoWidth = ((bmbfLogoBitmap!!.width.toFloat() / bmbfLogoBitmap.height) * logoHeight).toInt()
        canvas.drawBitmap(
            bmbfLogoBitmap,
            null,
            Rect(
                hmLogoWidth + 20,
                canvas.height - logoHeight - 10,
                bmbfLogoWidth + hmLogoWidth + 20,
                canvas.height - 10
            ),
            null
        )
    }

    private val BACKGROUND_PAINT = Paint().also {
        it.color = Color.WHITE
        it.alpha = 150
        it.style = Paint.Style.FILL
    }
}
