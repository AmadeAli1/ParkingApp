package com.amade.dev.parkingapp.model

import kotlinx.serialization.Serializable


@Serializable
data class Utente(
    val nome: String,
    val email: String,
    val password: String,
    val type: UtenteType? = null,
    val paymentType: PaymentPlan? = null,
    val id: Int? = null,
    val divida: Float? = 0.0f,
    val lastSubscription: String? = null,
) {

    enum class UtenteType {
        ESTUDANTE,
        OUTROS
    }


    enum class PaymentPlan {
        Monthly,
        Daily;
    }


}