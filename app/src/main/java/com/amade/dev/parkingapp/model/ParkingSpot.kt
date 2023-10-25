package com.amade.dev.parkingapp.model

data class ParkingSpot(
    val spot: Spot,
    val parking: Parking? = null,
)
