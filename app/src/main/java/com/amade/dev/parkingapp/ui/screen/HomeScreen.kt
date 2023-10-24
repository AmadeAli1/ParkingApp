package com.amade.dev.parkingapp.ui.screen

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ExitToApp
import androidx.compose.material.icons.outlined.LocalParking
import androidx.compose.material3.Card
import androidx.compose.material3.CenterAlignedTopAppBar
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.OutlinedCard
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.navigation.NavController
import com.amade.dev.parkingapp.R
import com.amade.dev.parkingapp.ui.navigation.Screen
import com.amade.dev.parkingapp.ui.viewmodel.HomeViewModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun HomeScreen(navController: NavController) {
    val homeViewModel = hiltViewModel<HomeViewModel>()
    val utente by homeViewModel.utente.collectAsStateWithLifecycle()
    Scaffold(
        topBar = {
            CenterAlignedTopAppBar(
                title = { Text(text = "Parking") },
                navigationIcon = {
                    Icon(
                        imageVector = Icons.Outlined.LocalParking,
                        contentDescription = null
                    )
                },
                actions = {
                    IconButton(onClick = {
                        homeViewModel.logOut {
                            navController.navigate(route = Screen.Login.route) {
                                popUpTo(route = Screen.Home.route) {
                                    inclusive = true
                                }
                            }
                        }
                    }) {
                        Icon(imageVector = Icons.Default.ExitToApp, contentDescription = null)
                    }
                })
        }
    ) { padding ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(padding)
        ) {

            Card(onClick = { /*TODO*/ }) {
                Box{
                    Image(
                        painter = painterResource(id = R.drawable.parking001),
                        contentDescription = null,
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(250.dp),
                        contentScale = ContentScale.Crop
                    )
                    
                }
            }

        }
    }

}