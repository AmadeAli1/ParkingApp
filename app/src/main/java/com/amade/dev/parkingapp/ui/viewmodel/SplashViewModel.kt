package com.amade.dev.parkingapp.ui.viewmodel

import androidx.compose.runtime.State
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.amade.dev.parkingapp.repository.AuthRepository
import com.amade.dev.parkingapp.utils.SplashState
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class SplashViewModel @Inject constructor(
    private val authRepository: AuthRepository,
) : ViewModel() {

    private val _uiState = mutableStateOf<SplashState>(SplashState.Init)
    val uiState: State<SplashState> = _uiState

    init {
        getLastUtente()
    }

    private fun getLastUtente(): Unit {
        viewModelScope.launch {
            val state = when (authRepository.isLastLoginUtenteAvailable()) {
                true -> SplashState.Success
                false -> SplashState.Failure
            }
            delay(1500)
            _uiState.value = state
        }
    }

}