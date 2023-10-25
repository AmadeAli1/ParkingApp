package com.amade.dev.parkingapp.utils

import com.amade.dev.parkingapp.model.Parking
import com.amade.dev.parkingapp.model.ParkingDetail


data class LoginUIState(
    val isLoading: Boolean = false,
    val isSuccess: Boolean = false,
    val errorMessage: String? = null,
)

data class RegisterUserUIState(
    val isLoading: Boolean = false,
    val isSuccess: Boolean = false,
    val errorMessage: String? = null,
)

data class ParkingState(
    val isClosed: Boolean = false,
    val available: Int = 20,
    val occupied: Int = 0,
    val message: String = "",
)

sealed class ParkingInfo {

    object Empty : ParkingInfo()
    data class Failure(val message: String) : ParkingInfo()
    data class Success(val parking: Parking) : ParkingInfo()
}

sealed class PaymentState {
    object Hide : PaymentState()
    object Loading : PaymentState()

    object Success : PaymentState()
    data class Show(val parkingDetail: ParkingDetail) : PaymentState()
    data class Failure(val message: String) : PaymentState()
}

sealed class SplashState {
    object Init : SplashState()
    object Success : SplashState()
    object Failure : SplashState()
}