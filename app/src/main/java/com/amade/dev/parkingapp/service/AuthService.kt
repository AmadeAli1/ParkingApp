package com.amade.dev.parkingapp.service

import com.amade.dev.parkingapp.model.Utente
import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.POST
import retrofit2.http.Query

interface AuthService {

    @GET("utente/login")
    suspend fun login(
        @Query("email") email: String,
        @Query("password") password: String,
    ): Response<Utente>

    @POST("utente/signUp")
    suspend fun signUp(
        @Body utente: Utente,
    ): Response<Utente>


}