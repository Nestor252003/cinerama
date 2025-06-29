package com.nestor.cinerama.desing.peliculas.entities

import com.nestor.cinerama.desing.cines.entities.CineHorario

data class Movie(
    var id: String? = null,
    var title: String? = null,
    var url: String? = null,
    var idioma: List<String>? = null,
    var sinopsis: String? = null,
    var genre: String? = null,
    var status: List<String>? = null,
    var director: String? = null,
    var durationMovie: String? = null,
    var age: String? = null,
    var urlTrailer: String? = null,
    var cineHorarios: List<CineHorario>? = null,
    var disponible: List<String>? = null
)