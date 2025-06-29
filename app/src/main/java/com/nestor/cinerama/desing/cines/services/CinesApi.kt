package com.nestor.cinerama.desing.cines.services

import com.nestor.cinerama.desing.cines.entities.Cines
import retrofit2.Call
import retrofit2.http.GET

interface CinesApi {
    @GET("/cines")
    fun getCines(): Call<List<Cines>>
}