package com.amade.dev.parkingapp.dataStore

import android.content.Context
import androidx.datastore.dataStore

val Context.dataStore by dataStore("app-user.json", UtenteSerializer)
