package com.amade.dev.parkingapp.utils

data class ApiResponse<T>(
    val message: String,
    val response: T? = null,
)