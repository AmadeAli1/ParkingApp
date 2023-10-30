package com.amade.dev.parkingapp.utils

import android.icu.text.NumberFormat
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.State
import com.google.gson.JsonParser
import okhttp3.ResponseBody
import java.util.Locale

fun ResponseBody.toMessage(): String {
    return try {
        JsonParser.parseString(string()).asJsonObject.get("message").asString
    } catch (e: Exception) {
        "Internal Server Error"
    }
}

fun <T> MutableState<T>.asState(): State<T> {
    return this
}

fun Number.toCurrency(): String {
    val xs = NumberFormat.getCurrencyInstance(Locale("pt", "MZ"))
    return xs.format(this)
}