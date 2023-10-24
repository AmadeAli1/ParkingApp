package com.amade.dev.parkingapp.ui.screen

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavHostController
import com.airbnb.lottie.compose.LottieAnimation
import com.airbnb.lottie.compose.LottieCompositionSpec
import com.airbnb.lottie.compose.animateLottieCompositionAsState
import com.airbnb.lottie.compose.rememberLottieComposition
import com.amade.dev.parkingapp.R
import com.amade.dev.parkingapp.ui.navigation.Screen
import com.amade.dev.parkingapp.ui.viewmodel.SplashViewModel
import com.amade.dev.parkingapp.utils.SplashState
import kotlinx.coroutines.delay

@Composable
fun SplashScreen(navController: NavHostController) {
    val composition by rememberLottieComposition(LottieCompositionSpec.RawRes(R.raw.car))
    val lottieComposition = animateLottieCompositionAsState(composition = composition)
    val splashViewModel = hiltViewModel<SplashViewModel>()
    val uiState by splashViewModel.uiState

    LaunchedEffect(key1 = uiState) {

        when (uiState) {
            SplashState.Init -> Unit

            SplashState.Success -> {
                navController.navigate(route = Screen.Home.route) {
                    popUpTo(route = Screen.Splash.route) {
                        inclusive = true
                    }
                }
            }

            SplashState.Failure -> {
                navController.navigate(route = Screen.Login.route) {
                    popUpTo(route = Screen.Splash.route) {
                        inclusive = true
                    }
                }
            }

        }


    }


    Box(
        contentAlignment = Alignment.Center,
        modifier = Modifier.fillMaxSize()
    ) {
        LottieAnimation(
            composition = composition,
            progress = lottieComposition.progress,
            modifier = Modifier.size(350.dp)
        )

        Text(
            text = "Created by Edson & Humeid",
            style = MaterialTheme.typography.labelLarge,
            color = MaterialTheme.colorScheme.primary,
            modifier = Modifier
                .align(Alignment.BottomCenter)
                .padding(8.dp)
        )
    }
}