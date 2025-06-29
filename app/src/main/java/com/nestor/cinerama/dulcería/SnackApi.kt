package com.nestor.cinerama.dulcería

import retrofit2.Call
import retrofit2.http.GET
import retrofit2.http.Query

interface SnackApi {

    // Obtener la lista de dulcería con paginación desde la API
    @GET("Dulceria")
    fun getDulceria(
        @Query("page") page: Int,
        @Query("limit") limit: Int
    ): Call<List<Dulceria>>
}