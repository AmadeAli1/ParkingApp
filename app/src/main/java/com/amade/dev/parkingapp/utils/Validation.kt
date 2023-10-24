package com.amade.dev.parkingapp.utils

import android.util.Patterns

sealed class Validation {
    object Success : Validation()
    data class Failure(val message: String) : Validation()

    class ValidationFields {

        fun isValidEmail(email: String): Validation {
            if (!Patterns.EMAIL_ADDRESS.toRegex().matches(email)) return Failure("Email malformed")
            return Success
        }

        fun isValidPassword(password: String): Validation {
            if (password.isBlank()) return Failure("Required")
            if (password.length < 6) return Failure("Minimum 6 characters")
            return Success
        }

        fun isValidName(name: String): Validation {
            if (name.isBlank()) return Failure("Required")
            if (name.length < 2) return Failure("Minimum 3 characters for name")
            if (name.contains(regex = Regex("[0-9]"))) return Failure("Name with numbers is invalid")
            return Success
        }

    }

}