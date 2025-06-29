package com.nestor.cinerama.desing.cines.entities

import java.io.Serializable

data class CineHorario(
    var cineId: String? = null,
    var horarios: List<String>? = null,
    var name: String? = null
) : Serializable