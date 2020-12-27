package com.example.peopledensitymeasurementprototype.map

import android.graphics.Color
import com.example.peopledensitymeasurementprototype.density.Density
import kotlin.math.ln
import kotlin.math.min
import kotlin.math.pow


fun densityToColorMapping(density: Density): Int {
    val d = density.people

    val splineCoefs = when (d) {
        in 0f..0.2f -> listOf(11427.41f, -1635.48f, -830.0f, 240.0f)
        in 0.2f..1f -> listOf(-79.3f, 142.07f, -112.9f, 100f)
        in 1f..2f   -> listOf(5.78f, 2.06f, -37.85f, 60f)
        else        -> listOf(0.4f, 0.9f, -16.36f, 30f)
    }

    val hue = splineCoefs[0] * d.pow(3) + splineCoefs[1] * d.pow(2) + splineCoefs[2] * d + splineCoefs[3]

    return Color.HSVToColor(floatArrayOf(hue.toFloat(), 1f, 1f))
}


