package com.example.peopledensitymeasurementprototype.density

data class Density(val people: Double) {
    fun normalized(factor: Double): Density {
        return Density(people / factor)
    }

    companion object {
        fun empty() = Density(0.0)
    }
}

fun Iterable<Density>.sum() = Density(sumByDouble { it.people })
