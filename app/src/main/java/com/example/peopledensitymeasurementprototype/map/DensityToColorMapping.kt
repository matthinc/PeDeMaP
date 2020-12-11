package com.example.peopledensitymeasurementprototype.map

import android.graphics.Color
import com.example.peopledensitymeasurementprototype.density.Density
import kotlin.math.ln
import kotlin.math.min
import kotlin.math.pow


fun densityToColorMapping(density: Density): Int {
    val p = density.people
    val hue = -319.444 * p.pow(3) + 1033.33  * p.pow(2) - 893.889 * p + 240
    return Color.HSVToColor(floatArrayOf(hue.toFloat(), 1f, 1f))
}
