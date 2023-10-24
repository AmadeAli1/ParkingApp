package com.amade.dev.parkingapp.ui.screen

import androidx.compose.animation.animateContentSize
import androidx.compose.animation.core.EaseIn
import androidx.compose.animation.core.tween
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.ClickableText
import androidx.compose.material3.Button
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Snackbar
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.buildAnnotatedString
import androidx.compose.ui.text.withStyle
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.navigation.NavController
import com.amade.dev.parkingapp.R
import com.amade.dev.parkingapp.ui.components.EmailField
import com.amade.dev.parkingapp.ui.components.PasswordField
import com.amade.dev.parkingapp.ui.navigation.Screen
import com.amade.dev.parkingapp.ui.viewmodel.LoginViewModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun LoginScreen(navController: NavController) {
    val loginViewModel = hiltViewModel<LoginViewModel>()
    val passwordValidation by loginViewModel.passwordValidation.collectAsStateWithLifecycle()
    val emailValidation by loginViewModel.emailValidation.collectAsStateWithLifecycle()
    val uiState by loginViewModel.uiState.collectAsStateWithLifecycle()
    val snackbarHostState = remember { SnackbarHostState() }

    LaunchedEffect(key1 = uiState) {
        if (uiState.isSuccess) {
            navController.navigate(route = Screen.Home.route) {
                popUpTo(route = Screen.Login.route) {
                    inclusive = true
                }
            }
            return@LaunchedEffect
        }
        uiState.errorMessage?.let {
            snackbarHostState.showSnackbar(message = it)
        }
    }

    Scaffold(
        snackbarHost = {
            SnackbarHost(snackbarHostState) {
                Snackbar(
                    snackbarData = it,
                    shape = MaterialTheme.shapes.small,
                    modifier = Modifier.fillMaxWidth()
                )
            }
        }
    ) {
        Box(
            modifier = Modifier
                .fillMaxSize()
                .padding(it)
        ) {
            Image(
                painter = painterResource(id = R.drawable.login),
                contentDescription = "login image",
                modifier = Modifier
                    .fillMaxWidth()
                    .fillMaxHeight(0.5f)
                    .clip(RoundedCornerShape(bottomEnd = 8.dp, bottomStart = 8.dp)),
                contentScale = ContentScale.Crop
            )

            Surface(
                modifier = Modifier
                    .fillMaxWidth()
                    .fillMaxHeight(0.6f)
                    .align(Alignment.BottomCenter)
                    .animateContentSize(tween(500, easing = EaseIn)),
                shape = RoundedCornerShape(30.dp, 30.dp),
            ) {

                Column(
                    modifier = Modifier.padding(20.dp),
                    verticalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    Text(text = "Welcome back", style = MaterialTheme.typography.headlineMedium)
                    Text(
                        text = "Log in",
                        style = MaterialTheme.typography.titleMedium.copy(MaterialTheme.colorScheme.primary)
                    )

                    Spacer(modifier = Modifier.padding(vertical = 10.dp))
                    EmailField(
                        email = loginViewModel.email.value,
                        onValueChange = loginViewModel::onEmailChange,
                        validation = emailValidation
                    )
                    Spacer(modifier = Modifier.padding(vertical = 2.dp))
                    PasswordField(
                        password = loginViewModel.password.value,
                        onValueChange = loginViewModel::onPasswordChange,
                        validation = passwordValidation
                    )

                    Spacer(modifier = Modifier.padding(vertical = 8.dp))
                    Button(
                        onClick = loginViewModel::login,
                        modifier = Modifier
                            .align(Alignment.CenterHorizontally)
                            .fillMaxWidth(),
                        enabled = !uiState.isLoading,
                        contentPadding = PaddingValues(16.dp), shape = MaterialTheme.shapes.small
                    ) {
                        Text(text = "Login")
                    }

                    if (uiState.isLoading) {
                        CircularProgressIndicator(modifier = Modifier.align(Alignment.CenterHorizontally))
                    }

                    Box(
                        modifier = Modifier.fillMaxSize(),
                        contentAlignment = Alignment.BottomCenter
                    ) {
                        ClickableText(
                            text = buildAnnotatedString {
                                withStyle(
                                    MaterialTheme.typography.labelLarge.copy(color = MaterialTheme.colorScheme.onBackground)
                                        .toSpanStyle()
                                ) {
                                    append("New user?  ")
                                }
                                withStyle(
                                    MaterialTheme.typography.labelLarge.copy(color = MaterialTheme.colorScheme.primary)
                                        .toSpanStyle()
                                ) {
                                    append("Signup")
                                }
                            },
                            onClick = {
                                navController.navigate(Screen.Signup.route) {

                                }
                            }
                        )
                    }
                }

            }
        }
    }


}