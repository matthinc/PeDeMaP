package com.example.peopledensitymeasurementprototype

import android.app.Application
import com.example.peopledensitymeasurementprototype.density.DensityGrid

class BApplication: Application() {
    val grid = DensityGrid()
}
