package com.example.peopledensitymeasurementprototype.map

import android.graphics.Color
import android.graphics.Paint
import com.example.peopledensitymeasurementprototype.density.Density
import kotlin.math.pow

fun densityToColorMapping(density: Density): Int {
    val d = density.people

    // Coefficients for the interpolated spline function
    // For exaple: 1, 2, 3, 4 -> 1x^3 + 2x^2 + 3x + 4
    val splineCoefficients = when (d) {
        in 0f..0.2f -> listOf(11427.41f, -1635.48f, -830.0f, 240.0f)
        in 0.2f..1f -> listOf(-79.3f, 142.07f, -112.9f, 100f)
        in 1f..2f -> listOf(5.78f, 2.06f, -37.85f, 60f)
        // For density > 2
        else -> listOf(0.4f, 0.9f, -16.36f, 30f)
    }

    val hue = splineCoefficients[0] * d.pow(3) +
        splineCoefficients[1] * d.pow(2) +
        splineCoefficients[2] * d +
        splineCoefficients[3]

    return Color.HSVToColor(floatArrayOf(hue.toFloat(), 1f, 1f))
}

fun getPaintForDensity(density: Density): Paint {
    return Paint().also {
        it.style = Paint.Style.FILL
        it.color = densityToColorMapping(density)
        it.alpha = 128
    }
}
