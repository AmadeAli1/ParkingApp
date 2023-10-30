package com.amade.dev.parkingapp.ui.screen

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.DirectionsCar
import androidx.compose.material.icons.filled.ExitToApp
import androidx.compose.material.icons.outlined.LocalParking
import androidx.compose.material3.Card
import androidx.compose.material3.CenterAlignedTopAppBar
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.LargeFloatingActionButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.navigation.NavController
import com.amade.dev.parkingapp.R
import com.amade.dev.parkingapp.model.Utente
import com.amade.dev.parkingapp.ui.navigation.Screen
import com.amade.dev.parkingapp.ui.viewmodel.HomeViewModel
import com.amade.dev.parkingapp.utils.toCurrency

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
                    IconButton(
                        onClick = { /*TODO*/ },
                        enabled = false
                    ) {
                        Icon(
                            imageVector = Icons.Outlined.LocalParking,
                            contentDescription = null
                        )
                    }
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
        }, floatingActionButton = {
            LargeFloatingActionButton(
                onClick = {
                    navController.navigate(route = Screen.ParkingLayout.route) {
                        launchSingleTop = true
                        restoreState = true
                    }
                },
                modifier = Modifier.size(80.dp),
                containerColor = MaterialTheme.colorScheme.tertiaryContainer
            ) {
                Icon(imageVector = Icons.Outlined.LocalParking, contentDescription = null)
            }
        }
    ) { padding ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(padding)
                .padding(14.dp), verticalArrangement = Arrangement.spacedBy(8.dp)
        ) {

            utente?.let {
                if (it.paymentType == Utente.PaymentPlan.Monthly) {
                    MoneyGraph(
                        utente = it,
                        modifier = Modifier.align(Alignment.CenterHorizontally)
                    )
                }
            }

            Card(
                onClick = {
                    navController.navigate(route = Screen.History.route) {
                        launchSingleTop = true
                        restoreState = true
                    }
                }
            ) {
                Box {
                    Image(
                        painter = painterResource(id = R.drawable.parking001),
                        contentDescription = null,
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(230.dp),
                        contentScale = ContentScale.Crop
                    )
                    Text(
                        text = "Parking History",
                        modifier = Modifier
                            .align(Alignment.BottomEnd)
                            .padding(8.dp),
                        style = MaterialTheme.typography.headlineLarge.copy(
                            fontWeight = FontWeight.ExtraBold,
                            color = MaterialTheme.colorScheme.onSecondary
                        )
                    )
                }
            }

        }
    }


}

@Composable
fun MoneyGraph(
    modifier: Modifier = Modifier,
    utente: Utente,
) {
    Surface(
        tonalElevation = 2.dp,
        shadowElevation = 2.dp,
        shape = MaterialTheme.shapes.medium,
        color = MaterialTheme.colorScheme.error,
        modifier = modifier
    ) {
        Box(
            contentAlignment = Alignment.Center
        ) {
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(16.dp),
                verticalArrangement = Arrangement.Center,
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                Icon(
                    imageVector = Icons.Default.DirectionsCar,
                    contentDescription = null,
                    modifier = Modifier.size(38.dp)
                )
                Spacer(modifier = Modifier.height(2.dp))

                Text(
                    text = "Consumo",
                    style = MaterialTheme.typography.titleLarge,
                    textAlign = TextAlign.Center,
                    fontWeight = FontWeight.Bold
                )
                Text(
                    text = utente.divida!!.toCurrency(),
                    style = MaterialTheme.typography.bodyMedium,
                    textAlign = TextAlign.Center, fontWeight = FontWeight.Normal
                )
                if (utente.lastSubscription != null) {
                    Text(
                        text = "Last subscription \n${utente.lastSubscription}",
                        textAlign = TextAlign.Center,
                        style = MaterialTheme.typography.bodyMedium
                    )
                }
            }
        }
    }
}
//
//@Composable
//fun WaterGraphic(
//    modifier: Modifier = Modifier,
//    canvasSize: Dp = 200.dp,
//    consume: Float,
//) {
//    val canvasContent = canvasSize - 30.dp
//    //TODO RememberSaveable para nao fazer reset da animacao
//    var progress by remember {
//        mutableStateOf(0f)
//    }
//    val animateProgress by animateFloatAsState(
//        targetValue = progress,
//        animationSpec = tween(2000, easing = EaseOutSine)
//    )
//
//    LaunchedEffect(key1 = consume) {
//        animate(
//            initialValue = 0f,
//            targetValue = consume * 3.6f,
//            block = { value, _ ->
//                progress = value
//            }
//        )
//    }
//
//    Box(
//        modifier = modifier
//            .size(canvasSize)
//    ) {
//        Box {
//            Canvas(
//                modifier = Modifier.fillMaxSize(),
//                onDraw = {
//                    drawArc(
//                        color = Color.Cyan.copy(alpha = 0.28f),
//                        startAngle = 0f,
//                        useCenter = true,
//                        sweepAngle = 360f,
//                        topLeft = Offset(x = 0f, y = 0f),
//                    )
//                }
//            )
//            Canvas(
//                modifier = Modifier.fillMaxSize(),
//                onDraw = {
//                    drawArc(
//                        color = Color.Cyan,
//                        startAngle = 180f,
//                        useCenter = true,
//                        sweepAngle = animateProgress,
//                        topLeft = Offset(x = 0f, y = 0f),
//                    )
//                }
//            )
//        }
//        Box(
//            modifier = Modifier.align(Alignment.Center),
//            contentAlignment = Alignment.Center
//        ) {
//            val contentColor = MaterialTheme.colorScheme.surfaceColorAtElevation(2.dp)
//            Canvas(
//                modifier = Modifier
//                    .size(canvasContent)
//            ) {
//                drawArc(
//                    color = contentColor,
//                    startAngle = 0f,
//                    useCenter = true,
//                    sweepAngle = 360f,
//                )
//            }
//            Column(
//                modifier = Modifier.width(canvasContent - 20.dp),
//                verticalArrangement = Arrangement.Center,
//                horizontalAlignment = Alignment.CenterHorizontally
//            ) {
//                Icon(
//                    imageVector = Icons.Default.WaterDrop,
//                    contentDescription = null, tint = Color.Cyan.copy(alpha = 0.5f),
//                    modifier = Modifier.size(38.dp)
//                )
//                Spacer(modifier = Modifier.height(2.dp))
//                Text(
//                    text = "${consume * 1000}",
//                    style = MaterialTheme.typography.labelLarge,
//                    textAlign = TextAlign.Center, fontWeight = FontWeight.Bold
//                )
//                Text(
//                    text = "Litros consumidos",
//                    style = MaterialTheme.typography.labelLarge,
//                    textAlign = TextAlign.Center,
//                    fontWeight = FontWeight.Bold
//                )
//            }
//        }
//
//
//    }
//}