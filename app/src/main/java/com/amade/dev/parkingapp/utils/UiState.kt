package com.amade.dev.parkingapp.utils


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

sealed class SplashState {
    object Init : SplashState()
    object Success : SplashState()
    object Failure : SplashState()
}