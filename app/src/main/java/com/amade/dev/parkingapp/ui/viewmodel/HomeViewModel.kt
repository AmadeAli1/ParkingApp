package com.amade.dev.parkingapp.ui.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.amade.dev.parkingapp.repository.AuthRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class HomeViewModel @Inject constructor(
    private val authRepository: AuthRepository,
) : ViewModel() {
    val utente = authRepository.user

    fun logOut(onLogout:()->Unit){
        viewModelScope.launch {
            if (authRepository.logOut()) {
                onLogout()
            }
        }
    }
}