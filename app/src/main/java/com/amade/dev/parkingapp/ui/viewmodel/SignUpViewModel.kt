package com.amade.dev.parkingapp.ui.viewmodel

import androidx.compose.runtime.State
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.amade.dev.parkingapp.model.Utente
import com.amade.dev.parkingapp.repository.AuthRepository
import com.amade.dev.parkingapp.utils.RegisterUserUIState
import com.amade.dev.parkingapp.utils.Validation
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch
import java.util.function.Predicate
import javax.inject.Inject

@HiltViewModel
class SignUpViewModel @Inject constructor(
    private val authRepository: AuthRepository,
) : ViewModel() {

    private val validation: Validation.ValidationFields by lazy { Validation.ValidationFields() }

    private var _paymentPlan = Utente.PaymentPlan.Daily
    private var _utenteType = Utente.UtenteType.ESTUDANTE

    private val _email = mutableStateOf("")
    val email: State<String> = _email

    private val _name = mutableStateOf("")
    val name: State<String> = _name

    private val _password = mutableStateOf("")
    val password: State<String> = _password

    private val _emailValidation = MutableStateFlow<Validation>(Validation.Success)
    val emailValidation = _emailValidation.stateIn(
        scope = viewModelScope,
        started = SharingStarted.WhileSubscribed(5000),
        initialValue = Validation.Success
    )

    private val _passwordValidation = MutableStateFlow<Validation>(Validation.Success)
    val passwordValidation = _passwordValidation.stateIn(
        scope = viewModelScope,
        started = SharingStarted.WhileSubscribed(5000),
        initialValue = Validation.Success
    )

    private val _nameValidation = MutableStateFlow<Validation>(Validation.Success)
    val nameValidation = _nameValidation.stateIn(
        scope = viewModelScope,
        started = SharingStarted.WhileSubscribed(5000),
        initialValue = Validation.Success
    )


    private val _uiState = MutableStateFlow(RegisterUserUIState())
    val uiState = _uiState.stateIn(
        scope = viewModelScope,
        started = SharingStarted.WhileSubscribed(5_000),
        initialValue = RegisterUserUIState()
    )

    fun onPaymentPlanChange(plan: Utente.PaymentPlan) {
        _paymentPlan = plan
    }

    fun onUtenteTypeChange(utenteType: Utente.UtenteType) {
        _utenteType = utenteType
    }

    fun onEmailChange(email: String) {
        _email.value = email
        _emailValidation.tryEmit(validation.isValidEmail(email))
    }

    fun onNameChange(name: String) {
        _name.value = name
        _nameValidation.tryEmit(validation.isValidName(name))
    }

    fun onPasswordChange(password: String) {
        _password.value = password
        _passwordValidation.tryEmit(validation.isValidPassword(password))
    }


    private fun allFieldsIsValid(): Boolean {
        val predicate = Predicate<Validation> { emailValidation.value == it }
            .and { passwordValidation.value == it }
            .and { nameValidation.value == it }
        return predicate.test(Validation.Success)
    }

    fun register() {
        viewModelScope.launch {
            if (allFieldsIsValid()) {
                authRepository.register(
                    utente = Utente(
                        email = email.value,
                        nome = name.value,
                        password = password.value,
                        paymentType = _paymentPlan,
                        type = _utenteType,
                    ),
                    onLoading = {
                        _uiState.tryEmit(RegisterUserUIState(isLoading = true))
                    }, onSuccess = {
                        _uiState.tryEmit(RegisterUserUIState(isSuccess = true))
                    }, onFailure = {
                        _uiState.tryEmit(RegisterUserUIState(errorMessage = it))
                    }
                )
            }
        }

    }
}