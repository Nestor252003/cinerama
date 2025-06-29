package com.nestor.cinerama.login.Entities

import java.util.Date


data class User(
    var id: String? = null,
    var fullName: String? = null,
    var paternalSurname: String? = null,
    var maternalSurname: String? = null,
    var phone: String? = null,
    var dni: String? = null,
    var department: String? = null,
    var province: String? = null,
    var district: String? = null,
    var gender: String? = null,
    var birthdate: Date? = null,
    var name: String? = null,
    var email: String? = null,
    var password: String? = null,
    var favoriteCinema: Int? = null
)