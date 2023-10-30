package com.amade.dev.parkingapp.service

import com.amade.dev.parkingapp.model.Mpesa
import com.amade.dev.parkingapp.model.Parking
import com.amade.dev.parkingapp.model.ParkingDetail
import com.amade.dev.parkingapp.model.Payment
import com.amade.dev.parkingapp.utils.ApiResponse
import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.POST
import retrofit2.http.Query

interface ParkingService {

    @GET("parking/payment/detail")
    suspend fun parkingPaymentDetails(@Query("utenteId") id: Int): Response<ParkingDetail>

    @GET("parking/payment/confirm")
    suspend fun pay(@Query("utenteId") id: Int): Response<ApiResponse<Unit>>

    @POST("parking/payment/subscription")
    suspend fun paySubscription(@Body mpesa: Mpesa): Response<ApiResponse<Unit>>

    @POST("parking/payment/mpesa")
    suspend fun payWithMpesa(
        @Body mpesa: Mpesa,
    ): Response<ApiResponse<Unit>>


    @GET("parking/parquear")
    suspend fun parquear(@Query("utenteId") id: Int): Response<Parking>

    @GET("parking/history")
    suspend fun findAllPayments(@Query("utenteId") id: Int): Response<List<Payment>>


    @GET("parking/parquear/detail")
    suspend fun detail(@Query("utenteId") id: Int): Response<Parking>

}