package com.nestor.cinerama.desing.cines.entities

import java.io.Serializable

data class Cinema(
    var name: String? = null,
    var address: String? = null,
    var city: String? = null,
    var format: List<String> = emptyList(),
    var horas: List<String> = emptyList(),
    var urlImage: String? = null,
    var location: CoordenadasCine? = null
) : Serializable