package com.nestor.cinerama.desing.cines.entities

data class CoordenadasCine(
    var latitud: Double = 0.0,
    var longitud: Double = 0.0
) {
    override fun toString(): String {
        return "Latitud: $latitud, Longitud: $longitud"
    }
}