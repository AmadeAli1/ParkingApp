package com.amade.dev.parkingapp.ui.viewmodel

import androidx.compose.runtime.State
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.amade.dev.parkingapp.repository.AuthRepository
import com.amade.dev.parkingapp.utils.LoginUIState
import com.amade.dev.parkingapp.utils.Validation
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch
import java.util.function.Predicate
import javax.inject.Inject

@HiltViewModel
class LoginViewModel @Inject constructor(
    private val authRepository: AuthRepository,
) : ViewModel() {
    private val validation: Validation.ValidationFields by lazy { Validation.ValidationFields() }

    private val _email = mutableStateOf("")
    val email: State<String> = _email

    private val _password = mutableStateOf("")
    val password: State<String> = _password

    private val _emailValidation = MutableStateFlow<Validation>(Validation.Success)
    val emailValidation = _emailValidation.asStateFlow()

    private val _passwordValidation = MutableStateFlow<Validation>(Validation.Success)
    val passwordValidation = _passwordValidation.asStateFlow()

    private val _uiState = MutableStateFlow(LoginUIState())
    val uiState = _uiState.stateIn(
        scope = viewModelScope,
        started = SharingStarted.WhileSubscribed(5_000),
        initialValue = LoginUIState()
    )

    fun onEmailChange(email: String) {
        _email.value = email
        _emailValidation.tryEmit(validation.isValidEmail(email))
    }

    fun onPasswordChange(password: String) {
        _password.value = password
        _passwordValidation.tryEmit(validation.isValidPassword(password))
    }

    private fun allFieldsIsValid(): Boolean {
        val predicate =
            Predicate<Validation> { emailValidation.value == it }
                .and { passwordValidation.value == it }
        return predicate.test(Validation.Success)
    }


    fun login() {
        viewModelScope.launch {
            if (allFieldsIsValid()) {
                authRepository.login(email.value, password.value,
                    onLoading = {
                        _uiState.tryEmit(LoginUIState(isLoading = true))
                    }, onSuccess = {
                        _uiState.tryEmit(LoginUIState(isSuccess = true))
                    }, onFailure = {
                        _uiState.tryEmit(LoginUIState(errorMessage = it))
                    }
                )
            }
        }
    }

}