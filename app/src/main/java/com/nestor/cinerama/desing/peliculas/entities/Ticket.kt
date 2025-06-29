package com.nestor.cinerama.desing.peliculas.entities

import java.util.UUID

data class Ticket(
    var id: String = UUID.randomUUID().toString(),
    var type: String = "",
    var price: Double = 0.0
)