package com.nestor.cinerama.desing.peliculas.entities

import java.util.UUID

data class PaymentInfo(
    var nombre: String,
    var email: String,
    var metodoPago: String,
    var opcionSecundaria: String,
    var total: Double,
    var id: String = UUID.randomUUID().toString()
)