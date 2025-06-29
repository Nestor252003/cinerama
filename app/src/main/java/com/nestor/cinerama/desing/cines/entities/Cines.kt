package com.nestor.cinerama.desing.cines.entities

data class Cines(
    var horarios: List<String> = emptyList(),
    var id: String? = null,
    var name: String? = null,
    var sala: List<String> = emptyList()
)