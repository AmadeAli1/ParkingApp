package com.amade.dev.parkingapp.repository


import com.amade.dev.parkingapp.dataStore.UtenteDataStore
import com.amade.dev.parkingapp.model.Utente
import com.amade.dev.parkingapp.service.AuthService
import com.amade.dev.parkingapp.utils.toMessage
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.withContext
import java.io.IOException
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class AuthRepository @Inject constructor(
    private val authService: AuthService,
    private val dataStore: UtenteDataStore,
) {
    private val _user = MutableStateFlow<Utente?>(null)
    val user: StateFlow<Utente?> = _user

    suspend fun isLastLoginUtenteAvailable(): Boolean {
        val utente = dataStore.lastStudentLogin() ?: return false
        if (utente.id == null) return false
        try {
            _user.emit(utente)
        } catch (_: Exception) {
            return false
        }
        return true
    }

    suspend fun logOut(): Boolean {
        return try {
            dataStore.save(Utente("", "", "", id = null))
            true
        } catch (_: Exception) {
            false
        }
    }

    suspend fun login(
        email: String,
        password: String,
        onLoading: () -> Unit,
        onSuccess: () -> Unit,
        onFailure: (String) -> Unit,
    ) {
        onLoading()
        delay(1000)
        try {
            val response = withContext(Dispatchers.IO) {
                authService.login(email, password)
            }
            if (response.isSuccessful) {
                val userResponse = response.body()!!
                dataStore.save(userResponse)
                _user.emit(userResponse)
                return onSuccess()
            }
            response.errorBody()?.toMessage()?.let { onFailure(it) }
        } catch (networkException: IOException) {
            onFailure("Check your network connection")
        } catch (e: Exception) {
            e.message?.let(onFailure)
        }
    }

    suspend fun register(
        utente: Utente,
        onLoading: () -> Unit,
        onSuccess: () -> Unit,
        onFailure: (String) -> Unit,
    ) {
        onLoading()
        delay(1000)
        try {
            val response = withContext(Dispatchers.IO) {
                authService.signUp(utente)
            }
            if (response.isSuccessful) {
                val userResponse = response.body()!!
                dataStore.save(userResponse)
                _user.emit(userResponse)
                return onSuccess()
            }
            response.errorBody()?.toMessage()?.let { onFailure(it) }
        } catch (networkException: IOException) {
            onFailure("Check your network connection")
        } catch (e: Exception) {
            e.message?.let(onFailure)
        }
    }

}