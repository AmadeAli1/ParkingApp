package com.amade.dev.parkingapp.dataStore

import android.content.Context
import com.amade.dev.parkingapp.model.Utente
import dagger.hilt.android.qualifiers.ApplicationContext
import kotlinx.coroutines.flow.firstOrNull
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class UtenteDataStore @Inject constructor(
    @ApplicationContext private val context: Context,
) {
    private val data = context.dataStore.data

    suspend fun save(utente: Utente) {
        context.dataStore.updateData {
            it.copy(
                id = utente.id,
                nome = utente.nome,
                email = utente.email,
                password = utente.password,
                type = utente.type,
                paymentType = utente.paymentType,
                divida = utente.divida,
                lastSubscription = utente.lastSubscription
            )
        }
    }

    suspend fun lastStudentLogin() = data.firstOrNull()


}