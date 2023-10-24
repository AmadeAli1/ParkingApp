package com.amade.dev.parkingapp

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.ui.Modifier
import com.amade.dev.parkingapp.ui.navigation.EntryPoint
import com.amade.dev.parkingapp.ui.theme.ParkingAppTheme
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            ParkingAppTheme {
                // A surface container using the 'background' color from the theme
                Surface(
                    modifier = Modifier.fillMaxSize(),
                    color = MaterialTheme.colorScheme.background
                ) {
                    EntryPoint()
                }
            }
        }
    }
}


//QRCODE(
//url = ticket.id, modifier = Modifier
//.fillMaxWidth()
//.height(200.dp)
//.align(
//Alignment.CenterHorizontally
//)
//)
