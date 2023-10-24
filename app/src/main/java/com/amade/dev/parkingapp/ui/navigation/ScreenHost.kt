package com.amade.dev.parkingapp.ui.navigation

import androidx.compose.runtime.Composable
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.amade.dev.parkingapp.ui.screen.HomeScreen
import com.amade.dev.parkingapp.ui.screen.LoginScreen
import com.amade.dev.parkingapp.ui.screen.SignUpScreen
import com.amade.dev.parkingapp.ui.screen.SplashScreen

@Composable
fun EntryPoint(
) {
    val navController = rememberNavController()

    NavHost(
        navController = navController,
        startDestination = Screen.Splash.route
    ) {

        composable(route = Screen.Splash.route){
            SplashScreen(navController)
        }

        composable(route = Screen.Login.route){
            LoginScreen(navController)
        }


        composable(route = Screen.Signup.route){
            SignUpScreen(navController)
        }


        composable(route = Screen.Home.route){
            HomeScreen(navController)
        }

    }

}

sealed class Screen(val route: String) {
    object Splash : Screen("splash")
    object Login : Screen("login")
    object Signup : Screen("signup")
    object Home : Screen("home")
}