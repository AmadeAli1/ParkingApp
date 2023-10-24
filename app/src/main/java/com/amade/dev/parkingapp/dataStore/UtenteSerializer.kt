package com.amade.dev.parkingapp.dataStore

import androidx.datastore.core.Serializer
import com.amade.dev.parkingapp.model.Utente
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import kotlinx.serialization.SerializationException
import kotlinx.serialization.json.Json
import java.io.InputStream
import java.io.OutputStream

object UtenteSerializer : Serializer<Utente> {
    override val defaultValue: Utente
        get() = Utente("", "", "")

    override suspend fun readFrom(input: InputStream): Utente {
        return try {
            Json.decodeFromString(
                deserializer = Utente.serializer(),
                string = input.readBytes().decodeToString()
            )
        } catch (e: SerializationException) {
            e.printStackTrace()
            defaultValue
        }
    }

    override suspend fun writeTo(utente: Utente, output: OutputStream) {
        withContext(Dispatchers.IO) {
            output.write(
                Json.encodeToString(serializer = Utente.serializer(), value = utente)
                    .encodeToByteArray()
            )
        }
    }

}