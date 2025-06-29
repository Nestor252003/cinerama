package com.nestor.cinerama.desing.peliculas.entities

import java.util.UUID

data class Reserva(
    var id: String = UUID.randomUUID().toString(),
    var city: String = "",
    var movie: String = "",
    var cinema: String = "",
    var hour: String = "",
    var date: String = "",
    var seat: String = "",
    var tickets: List<Ticket> = emptyList(),
    var payments: List<PaymentInfo> = emptyList()
)