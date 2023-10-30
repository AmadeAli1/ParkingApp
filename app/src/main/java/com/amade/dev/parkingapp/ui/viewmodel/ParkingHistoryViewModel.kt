package com.amade.dev.parkingapp.ui.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.amade.dev.parkingapp.repository.AuthRepository
import com.amade.dev.parkingapp.repository.ParkingRepository
import com.amade.dev.parkingapp.utils.ParkingHistoryState
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class ParkingHistoryViewModel @Inject constructor(
    private val parkingRepository: ParkingRepository,
    private val authRepository: AuthRepository,
) : ViewModel() {

    private val utente = authRepository.user

    private val _uiState = MutableStateFlow(ParkingHistoryState())
    val uiState = _uiState.stateIn(
        scope = viewModelScope,
        started = SharingStarted.WhileSubscribed(5_000),
        initialValue = ParkingHistoryState()
    )


    init {
        findAllPayments()
    }

    fun findAllPayments() {
        viewModelScope.launch(Dispatchers.Default) {
            utente.value?.let {
                val data = it.id?.let { it1 -> parkingRepository.findAllPayments(it1) }
                if (data != null) {
                    delay(800)
                    _uiState.emit(
                        ParkingHistoryState(
                            isLoading = false,
                            total = data.sumOf { p -> p.amount },
                            data = data
                        )
                    )
                }
            }
        }
    }


}