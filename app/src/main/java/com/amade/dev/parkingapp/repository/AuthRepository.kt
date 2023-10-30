package com.amade.dev.parkingapp.repository


import com.amade.dev.parkingapp.dataStore.UtenteDataStore
import com.amade.dev.parkingapp.model.Utente
import com.amade.dev.parkingapp.service.AuthService
import com.amade.dev.parkingapp.utils.toMessage
import io.ktor.client.HttpClient
import io.ktor.client.plugins.websocket.webSocketSession
import io.ktor.client.request.url
import io.ktor.websocket.Frame
import io.ktor.websocket.WebSocketSession
import io.ktor.websocket.close
import io.ktor.websocket.readText
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.consumeAsFlow
import kotlinx.coroutines.flow.emitAll
import kotlinx.coroutines.flow.filterIsInstance
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.flowOn
import kotlinx.coroutines.flow.mapNotNull
import kotlinx.coroutines.flow.retryWhen
import kotlinx.coroutines.withContext
import kotlinx.serialization.decodeFromString
import kotlinx.serialization.json.Json
import java.io.IOException
import java.net.ConnectException
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class AuthRepository @Inject constructor(
    private val authService: AuthService,
    private val dataStore: UtenteDataStore,
    private val wss: HttpClient,
) {
    private val _user = MutableStateFlow<Utente?>(null)
    val user: StateFlow<Utente?> = _user

    private var session: WebSocketSession? = null

    fun getUtenteRealtimeConnection(): Flow<Utente> {
        return flow {
            session = wss.webSocketSession {
                url("ws://26.23.254.172:8080/api/ws/utente/${user.value?.id}")
            }
            val state = session!!.incoming
                .consumeAsFlow()
                .filterIsInstance<Frame.Text>()
                .mapNotNull {
                    val readText = it.readText()
                    println(readText)
                    Json.decodeFromString<Utente>(readText)
                }
            emitAll(state)
        }.flowOn(Dispatchers.IO)
            .retryWhen { cause, attempt ->
            if (cause is ConnectException && attempt < 30) {
                delay(5000)
                return@retryWhen true
            }
            false
        }.flowOn(Dispatchers.IO)
    }

    suspend fun upgradeUtente(utente: Utente) {
        _user.emit(utente)
        dataStore.save(utente)
    }


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

    suspend fun closeSession(){
        try{ session?.close() }catch (_:Exception){}
    }
}