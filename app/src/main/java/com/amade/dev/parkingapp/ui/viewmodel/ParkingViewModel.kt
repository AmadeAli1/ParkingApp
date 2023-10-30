package com.amade.dev.parkingapp.ui.viewmodel

import androidx.compose.runtime.mutableStateMapOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.amade.dev.parkingapp.model.ParkingSpot
import com.amade.dev.parkingapp.model.Spot
import com.amade.dev.parkingapp.model.Utente
import com.amade.dev.parkingapp.repository.AuthRepository
import com.amade.dev.parkingapp.repository.ParkingRepository
import com.amade.dev.parkingapp.utils.ParkingInfo
import com.amade.dev.parkingapp.utils.ParkingState
import com.amade.dev.parkingapp.utils.PaymentState
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class ParkingViewModel @Inject constructor(
    private val parkingRepository: ParkingRepository,
    private val authRepository: AuthRepository,
) : ViewModel() {

    val utente = authRepository.user

    private val _slots = mutableStateMapOf(* initSlots())
    val slots: Map<Int, ParkingSpot> = _slots

    private val _uiState = MutableStateFlow(ParkingState())
    val uiState = _uiState.stateIn(
        scope = viewModelScope,
        started = SharingStarted.WhileSubscribed(20_000),
        initialValue = ParkingState()
    )

    private val _paymentState = MutableStateFlow<PaymentState>(PaymentState.Hide)
    val paymentState = _paymentState.stateIn(
        scope = viewModelScope,
        started = SharingStarted.WhileSubscribed(10_000),
        initialValue = PaymentState.Hide
    )

    private val _parkingInfo = MutableStateFlow<ParkingInfo>(ParkingInfo.Empty)
    val parkingInfo = _parkingInfo.stateIn(
        scope = viewModelScope,
        started = SharingStarted.WhileSubscribed(5000),
        initialValue = ParkingInfo.Empty
    )

    init {
        initializeWSS()
    }


    fun paymentDetails() {
        viewModelScope.launch {
            utente.value!!.id?.let {
                _parkingInfo.tryEmit(ParkingInfo.Empty)
                parkingRepository.parkingDetails(
                    utenteId = it,
                    onLoading = {
                        _paymentState.tryEmit(PaymentState.Loading)
                    },
                    onSuccess = { detail ->
                        _paymentState.tryEmit(PaymentState.Show(detail))
                    },
                    onFailure = { message ->
                        _paymentState.tryEmit(PaymentState.Failure(message))
                    }
                )
            }

        }
    }


    fun cancelPopUp() {
        viewModelScope.launch {
            _paymentState.emit(PaymentState.Hide)
            _parkingInfo.emit(ParkingInfo.Empty)
        }
    }

    private fun initializeWSS() {
        viewModelScope.launch {
            try {
                parkingRepository.parkingStateFlow().collect {
                    when (it.spot?.status) {
                        Spot.Status.EXIT -> {
                            val parkingSpot = slots[it.spot.number]
                            if (parkingSpot != null) {
                                _slots[it.spot.number] = parkingSpot.copy(
                                    spot = Spot(
                                        number = it.spot.number,
                                        available = true,
                                        status = Spot.Status.EXIT
                                    )
                                )
                            }
                        }

                        Spot.Status.ENTRY -> {
                            _slots[it.spot.number] = ParkingSpot(it.spot, it)
                        }

                        else -> Unit
                    }
                    parkingInfo()
                }
            } catch (e: Exception) {
                println(e.message)
            }
        }

    }

    private suspend fun parkingInfo() {
        val available = slots.count { it.value.spot.available }
        val occupied = slots.count { !it.value.spot.available }
        _uiState.emit(_uiState.value.copy(available = available, occupied = occupied))
    }

    private fun initSlots(): Array<Pair<Int, ParkingSpot>> {
        val pairs = mutableListOf<Pair<Int, ParkingSpot>>()
        repeat(20) {
            val parkingSpot =
                ParkingSpot(
                    spot = Spot(available = true, number = it + 1, status = Spot.Status.EXIT)
                )
            pairs.add(Pair(it + 1, parkingSpot))
        }
        return pairs.toTypedArray()
    }

    override fun onCleared() {
        viewModelScope.launch {
            parkingRepository.closeSession()
        }
    }

    fun pay() {
        viewModelScope.launch {
            utente.value?.id?.let {
                parkingRepository.pay(
                    utenteId = it,
                    onSuccess = { message ->
                        _uiState.tryEmit(_uiState.value.copy(message = message))
                        _paymentState.tryEmit(PaymentState.Success)
                    },
                    onFailure = { message ->
                        _uiState.tryEmit(_uiState.value.copy(message = message))
                    }
                )
            }
        }
    }

    fun payWithMpesa(phoneNumber: String) {
        viewModelScope.launch {
            if (utente.value?.paymentType == Utente.PaymentPlan.Monthly) {
                _uiState.tryEmit(_uiState.value.copy(message = "Mpesa esta ativo somente para clientes que pagam Diariamente!"))
                _paymentState.tryEmit(PaymentState.Hide)
                return@launch
            }

            utente.value?.id?.let {
                parkingRepository.payWithMpesa(
                    utenteId = it,
                    phoneNumber = phoneNumber,
                    onSuccess = { message ->
                        _uiState.tryEmit(_uiState.value.copy(message = message))
                        _paymentState.tryEmit(PaymentState.Success)
                    },
                    onFailure = { message ->
                        _uiState.tryEmit(_uiState.value.copy(message = message))
                    }
                )
            }
        }
    }

    fun parquear() {
        viewModelScope.launch {
            utente.value?.id?.let {
                parkingRepository.parquear(
                    utenteId = it,
                    onSuccess = { body ->
                        viewModelScope.launch {
                            _parkingInfo.tryEmit(ParkingInfo.Success(body))
//                            delay(5000)
//                            _parkingInfo.tryEmit()
                        }
                    },
                    onFailure = { message ->
                        _parkingInfo.tryEmit(ParkingInfo.Failure(message))
                    }
                )
            }
        }
    }

    fun detail() {
        viewModelScope.launch {
            utente.value?.id?.let {
                parkingRepository.getSpotDetail(
                    utenteId = it,
                    onSuccess = { body ->
                        viewModelScope.launch {
                            _parkingInfo.tryEmit(ParkingInfo.Success(body))
                        }
                    },
                    onFailure = { message ->
                        _parkingInfo.tryEmit(ParkingInfo.Failure(message))
                    }
                )
            }
        }
    }

    fun pagarDivida(phoneNumber: String) {
        viewModelScope.launch {
            utente.value?.id?.let {
                parkingRepository.pagarDivida(
                    utenteId = it,
                    phoneNumber = phoneNumber,
                    onSuccess = { message ->
                        _uiState.tryEmit(_uiState.value.copy(message = message))
                        _paymentState.tryEmit(PaymentState.Success)
                    },
                    onFailure = { message ->
                        _uiState.tryEmit(_uiState.value.copy(message = message))
                    }
                )
            }
        }

    }

}