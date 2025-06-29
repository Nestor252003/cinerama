package com.nestor.cinerama.desing.peliculas.services


import com.nestor.cinerama.desing.peliculas.entities.Movie
import retrofit2.Call
import retrofit2.http.GET
import retrofit2.http.Path

interface MovieApi {
    @GET("/Movie")
    fun getMovies(): Call<List<Movie>>

    @GET("/Movie/{id}")
    fun getMovieDetails(@Path("id") id: String): Call<Movie>
}