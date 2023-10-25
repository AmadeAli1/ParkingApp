package com.amade.dev.parkingapp.model

import kotlinx.serialization.Serializable

@Serializable
data class Parking(
    val id: Int,
    val entranceTime: String,
    val utente: Utente?,
    val utenteId: Int?,
    val spot: Spot?,
)