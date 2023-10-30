package com.amade.dev.parkingapp.repository

import com.amade.dev.parkingapp.model.Mpesa
import com.amade.dev.parkingapp.model.Parking
import com.amade.dev.parkingapp.model.ParkingDetail
import com.amade.dev.parkingapp.model.Payment
import com.amade.dev.parkingapp.service.ParkingService
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
class ParkingRepository @Inject constructor(
    private val parkingService: ParkingService,
    private val wss: HttpClient,
) {
    private var session: WebSocketSession? = null

    fun parkingStateFlow(): Flow<Parking> {
        return flow {
            session = wss.webSocketSession {
                url("ws://26.23.254.172:8080/api/ws/parking")
            }
            val state = session!!.incoming
                .consumeAsFlow()
                .filterIsInstance<Frame.Text>()
                .mapNotNull {
                    val readText = it.readText()
                    println(readText)
                    Json.decodeFromString<Parking>(readText)
                }
            emitAll(state)
        }
            .flowOn(Dispatchers.IO)
            .retryWhen { cause, attempt ->
                if (cause is ConnectException && attempt < 15) {
                    delay(5000)
                    return@retryWhen true
                }
                false
            }
    }


    suspend fun parkingDetails(
        utenteId: Int, onLoading: () -> Unit,
        onSuccess: (ParkingDetail) -> Unit,
        onFailure: (String) -> Unit,
    ) = withContext(Dispatchers.IO) {
        try {
            onLoading()
            val response = parkingService.parkingPaymentDetails(utenteId)
            delay(1000)
            if (response.isSuccessful) {
                response.body()?.let(onSuccess)
            } else {
                response.errorBody()?.toMessage()?.let { onFailure(it) }
            }
        } catch (e: Exception) {
            e.cause?.let {
                it.message?.let { it1 -> onFailure(it1) }
            }
        }
    }

    suspend fun pay(
        utenteId: Int,
        onSuccess: (String) -> Unit,
        onFailure: (String) -> Unit,
    ) =
        withContext(Dispatchers.IO) {
            try {
                val response = parkingService.pay(utenteId)
                delay(800)
                if (response.isSuccessful) {
                    response.body()?.let { onSuccess(it.message) }
                } else {
                    response.errorBody()?.toMessage()?.let { onFailure(it) }
                }
            } catch (io: IOException) {
                onFailure("Check your internet connection")
            } catch (e: Exception) {
                e.message?.let(onFailure)
            }
        }

    suspend fun payWithMpesa(
        utenteId: Int,
        phoneNumber: String,
        onSuccess: (String) -> Unit,
        onFailure: (String) -> Unit,
    ) =
        withContext(Dispatchers.IO) {
            try {
                val response = parkingService.payWithMpesa(Mpesa(phoneNumber, utenteId))
                delay(1000)
                if (response.isSuccessful) {
                    response.body()?.let { onSuccess(it.message) }
                } else {
                    response.errorBody()?.toMessage()?.let { onFailure(it) }
                }
            } catch (io: IOException) {
                onFailure("Check your internet connection")
            } catch (e: Exception) {
                e.message?.let(onFailure)
            }
        }

    suspend fun parquear(
        utenteId: Int,
        onSuccess: (Parking) -> Unit,
        onFailure: (String) -> Unit,
    ) =
        withContext(Dispatchers.IO) {
            try {
                val response = parkingService.parquear(utenteId)
                delay(500)
                if (response.isSuccessful) {
                    response.body()?.let { onSuccess(it) }
                } else {
                    response.errorBody()?.toMessage()?.let { onFailure(it) }
                }
            } catch (io: IOException) {
                onFailure("Check your internet connection")
            } catch (e: Exception) {
                e.message?.let(onFailure)
            }
        }


    suspend fun findAllPayments(utenteId: Int): List<Payment> {
        return withContext(Dispatchers.IO) {
            try {
                parkingService.findAllPayments(utenteId).body() ?: emptyList()
            } catch (_: Exception) {
                emptyList()
            }
        }
    }

    suspend fun getSpotDetail(
        utenteId: Int,
        onSuccess: (Parking) -> Unit,
        onFailure: (String) -> Unit,
    ): Unit = withContext(Dispatchers.IO) {
        try {
            val response = parkingService.detail(utenteId)
            delay(500)
            if (response.isSuccessful) {
                response.body()?.let { onSuccess(it) }
            } else {
                response.errorBody()?.toMessage()?.let { onFailure(it) }
            }
        } catch (io: IOException) {
            onFailure("Check your internet connection")
        } catch (e: Exception) {
            e.message?.let(onFailure)
        }
    }

    suspend fun pagarDivida(
        utenteId: Int,
        phoneNumber: String,
        onSuccess: (String) -> Unit,
        onFailure: (String) -> Unit,
    ): Unit = withContext(Dispatchers.IO) {
        try {
            val response = parkingService.paySubscription(Mpesa(phoneNumber, utenteId))
            delay(500)
            if (response.isSuccessful) {
                response.body()?.let { onSuccess(it.message) }
            } else {
                response.errorBody()?.toMessage()?.let { onFailure(it) }
            }
        } catch (io: IOException) {
            onFailure("Check your internet connection")
        } catch (e: Exception) {
            e.message?.let(onFailure)
        }
    }


    suspend fun closeSession() {
        session?.close()
    }

}