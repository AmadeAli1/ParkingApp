package com.amade.dev.parkingapp.ui.screen

import android.widget.Toast
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.animateColorAsState
import androidx.compose.animation.core.EaseInBounce
import androidx.compose.animation.core.EaseInElastic
import androidx.compose.animation.core.EaseOutSine
import androidx.compose.animation.core.tween
import androidx.compose.animation.fadeIn
import androidx.compose.animation.shrinkOut
import androidx.compose.animation.slideInVertically
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.MonetizationOn
import androidx.compose.material.icons.outlined.LocalParking
import androidx.compose.material.icons.outlined.Payment
import androidx.compose.material3.Button
import androidx.compose.material3.CenterAlignedTopAppBar
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Divider
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.LargeFloatingActionButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.ProvideTextStyle
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.surfaceColorAtElevation
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.Font
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.font.toFontFamily
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.window.Dialog
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.navigation.NavController
import com.amade.dev.parkingapp.R
import com.amade.dev.parkingapp.model.Parking
import com.amade.dev.parkingapp.model.ParkingDetail
import com.amade.dev.parkingapp.model.ParkingSpot
import com.amade.dev.parkingapp.model.Utente
import com.amade.dev.parkingapp.ui.components.QrCodeView
import com.amade.dev.parkingapp.ui.viewmodel.ParkingViewModel
import com.amade.dev.parkingapp.utils.ParkingInfo
import com.amade.dev.parkingapp.utils.PaymentState
import java.util.Locale

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ParkingScreen(navController: NavController) {
    val viewModel = hiltViewModel<ParkingViewModel>()
    val uiState by viewModel.uiState.collectAsStateWithLifecycle()
    val parkingInfo by viewModel.parkingInfo.collectAsStateWithLifecycle()
    val detailState by viewModel.paymentState.collectAsStateWithLifecycle()
    val utente by viewModel.utente.collectAsStateWithLifecycle()
    val showParkingButton = remember { mutableStateOf(true) }
    val context = LocalContext.current
    //val snackbarHostState = remember { SnackbarHostState() }

    LaunchedEffect(key1 = uiState) {
        if (uiState.message.isNotBlank()) {
            Toast.makeText(context, uiState.message, Toast.LENGTH_LONG).show()
        }
    }

    LaunchedEffect(key1 = parkingInfo) {

        when (parkingInfo) {
            is ParkingInfo.Failure -> {
                //snackbarHostState.showSnackbar(message = (parkingInfo as ParkingInfo.Failure).message)
                Toast.makeText(
                    context,
                    (parkingInfo as ParkingInfo.Failure).message,
                    Toast.LENGTH_LONG
                ).show()
            }

            else -> Unit
        }

    }

    Scaffold(
        topBar = {
            CenterAlignedTopAppBar(
                title = { Text(text = "Isctem Parking Area") },
                navigationIcon = {
                    IconButton(
                        onClick = navController::popBackStack
                    ) {
                        Icon(
                            imageVector = Icons.Default.ArrowBack, contentDescription = null
                        )
                    }
                }, actions = {
                    IconButton(onClick = viewModel::parquear) {
                        Icon(
                            imageVector = Icons.Outlined.LocalParking,
                            contentDescription = null
                        )
                    }
                }
            )
        },
        floatingActionButton = {
            LargeFloatingActionButton(
                onClick = viewModel::paymentDetails,
                modifier = Modifier.size(80.dp)
            ) {
                Icon(imageVector = Icons.Outlined.Payment, contentDescription = null)
            }
        },
    ) {
        Column(
            modifier = Modifier
                .padding(it)
                .fillMaxSize(),
            verticalArrangement = Arrangement.spacedBy(12.dp)
        ) {

            Surface(
                tonalElevation = 8.dp,
                shape = MaterialTheme.shapes.small,
                modifier = Modifier.padding(16.dp)
            ) {

                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(60.dp),
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.Center
                ) {

                    Row(
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.spacedBy(8.dp)
                    ) {
                        Box(
                            modifier = Modifier
                                .clip(CircleShape)
                                .size(32.dp)
                                .background(Color.Green), contentAlignment = Alignment.Center
                        ) {
                            Text(
                                text = "${uiState.available}", color = Color.Black,
                                fontWeight = FontWeight.ExtraBold
                            )
                        }
                        Text(text = "Espaço Livre")
                    }

                    Spacer(modifier = Modifier.padding(8.dp))

                    Row(
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.spacedBy(8.dp)
                    ) {
                        Box(
                            modifier = Modifier
                                .clip(CircleShape)
                                .size(32.dp)
                                .background(MaterialTheme.colorScheme.error),
                            contentAlignment = Alignment.Center
                        ) {
                            Text(
                                text = "${uiState.occupied}",
                                color = MaterialTheme.colorScheme.onError,
                                fontWeight = FontWeight.ExtraBold
                            )
                        }
                        Text(text = "Espaço Ocupado")
                    }
                }

            }

            utente?.let { user ->
                LazyVerticalGrid(
                    columns = GridCells.Fixed(3),
                    modifier = Modifier.fillMaxSize(),
                    verticalArrangement = Arrangement.spacedBy(8.dp),
                    horizontalArrangement = Arrangement.spacedBy(8.dp),
                    contentPadding = PaddingValues(8.dp),
                    content = {
                        items(
                            items = viewModel.slots.toSortedMap().toList(),
                            key = { key -> key.first }
                        ) { item ->
                            Spot(parkingSpot = item.second,
                                utente = user,
                                spotInfo = { info ->
                                    showParkingButton.value = info
                                }
                            )
                        }

                    }
                )
            }

            when (detailState) {
                is PaymentState.Failure -> Toast.makeText(
                    context,
                    (detailState as PaymentState.Failure).message, Toast.LENGTH_LONG
                ).show()

                PaymentState.Hide -> Unit
                PaymentState.Loading -> Dialog(
                    onDismissRequest = viewModel::cancelPopUp
                ) {
                    CircularProgressIndicator()
                }

                is PaymentState.Show -> {
                    ParkingDetailBeforePay(
                        detail = (detailState as PaymentState.Show).parkingDetail,
                        onClick = viewModel::pay
                    ) {
                        viewModel.cancelPopUp()
                    }
                }

                is PaymentState.Success -> {}
            }

            if (parkingInfo is ParkingInfo.Success) {
                ParkingInfo(
                    parking = (parkingInfo as ParkingInfo.Success).parking,
                    onCancel = viewModel::cancelPopUp
                )
            }

        }

    }
}

@Composable
fun Spot(
    utente: Utente,
    parkingSpot: ParkingSpot,
    spotInfo: (Boolean) -> Unit = {},
) {

    LaunchedEffect(key1 = parkingSpot) {
        if (utente.id == parkingSpot.parking?.utenteId) {
            println("HERE::::::::::")
            spotInfo(true)
        }
    }

    val sizeModifier = Modifier
        .height(180.dp)
        .width(120.dp)
    val color by animateColorAsState(
        targetValue = if (parkingSpot.spot.available) Color.Green else MaterialTheme.colorScheme.error,
        animationSpec = tween(
            durationMillis = 300, easing = EaseOutSine
        ),
        label = "Spot color"
    )
    Box(
        modifier = sizeModifier
            .clip(MaterialTheme.shapes.small)
            .background(color),
        contentAlignment = Alignment.Center
    ) {

        AnimatedVisibility(
            visible = !parkingSpot.spot.available && utente.id == parkingSpot.parking?.utenteId,
            enter = fadeIn(tween(500)) + slideInVertically(tween(400, easing = EaseInBounce)),
            exit = shrinkOut(tween(1000, easing = EaseInElastic))
        ) {
            Image(
                painter = painterResource(R.drawable.car1),
                contentDescription = null,
                modifier = sizeModifier
                    .clip(MaterialTheme.shapes.large)
            )
        }

        Text(
            "P${parkingSpot.spot.number}",
            modifier = Modifier
                .align(Alignment.BottomCenter)
                .padding(4.dp),
            style = MaterialTheme.typography.labelLarge.copy(
                fontWeight = FontWeight.Bold,
                color = MaterialTheme.colorScheme.tertiaryContainer
            )
        )
    }
}

@Composable
fun ParkingDetailBeforePay(
    detail: ParkingDetail,
    onClick: () -> Unit,
    onCancel: () -> Unit,
) {
    Dialog(onDismissRequest = onCancel) {
        Surface(
            modifier = Modifier
                .fillMaxWidth()
                .height(380.dp),
            shape = MaterialTheme.shapes.large
        ) {

            Column(
                modifier = Modifier
                    .padding(16.dp)
                    .fillMaxWidth(),
                verticalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                Box(
                    modifier = Modifier
                        .size(100.dp)
                        .clip(CircleShape)
                        .background(MaterialTheme.colorScheme.surfaceColorAtElevation(8.dp))
                        .align(Alignment.CenterHorizontally),
                    contentAlignment = Alignment.Center
                ) {
                    Text(
                        text = detail.timeInParking,
                        style = MaterialTheme.typography.labelLarge
                    )
                }

                Text(
                    text = "Entrance time ${detail.entranceTime}",
                    textAlign = TextAlign.Center,
                    style = MaterialTheme.typography.titleSmall,
                    color = MaterialTheme.colorScheme.primary
                )

                Text(
                    text = "Exit time ${detail.exitTime}",
                    textAlign = TextAlign.Center,
                    style = MaterialTheme.typography.titleSmall,
                    color = MaterialTheme.colorScheme.tertiary
                )

                Text(
                    text = "UtenteId ${detail.utenteId}",
                    style = MaterialTheme.typography.titleSmall
                )

                Text(
                    text = "Amount per hour ${detail.amountPerHour} Mt",
                    style = MaterialTheme.typography.labelLarge,
                )

                Text(
                    text = "Discount ${detail.discount} Mt",
                    style = MaterialTheme.typography.labelLarge,
                )

                Text(
                    text = "Amount ${detail.amount} Mt",
                    style = MaterialTheme.typography.titleSmall,
                )
                Spacer(modifier = Modifier.height(1.dp))

                Button(
                    onClick = onClick,
                    modifier = Modifier.fillMaxWidth(),
                    contentPadding = PaddingValues(16.dp),
                    shape = MaterialTheme.shapes.small
                ) {
                    Text(text = "Payment")
                    Icon(
                        imageVector = Icons.Default.MonetizationOn,
                        contentDescription = null,
                        modifier = Modifier.padding(start = 5.dp)
                    )
                }

            }
        }
    }
}


@Composable
fun ParkingInfo(
    parking: Parking,
    onCancel: () -> Unit,
) {
    ProvideTextStyle(
        value = MaterialTheme.typography.labelLarge.copy(
            fontWeight = FontWeight.Bold,
            fontFamily = Font(R.font.poppins_bold).toFontFamily()
        ),
    ) {
        Dialog(onDismissRequest = onCancel) {
            Surface(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(390.dp),
                shape = MaterialTheme.shapes.small
            ) {

                Column(
                    modifier = Modifier
                        .padding(16.dp)
                        .fillMaxWidth(),
                    verticalArrangement = Arrangement.spacedBy(12.dp),
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {

                    Text(
                        text = "Veiculo parqueado".toUpperCase(Locale.ROOT),
                        fontSize = 22.sp,
                        textAlign = TextAlign.Center

                    )

                    Divider()

                    Text(
                        text = parking.entranceTime,
                        fontSize = MaterialTheme.typography.titleMedium.fontSize,
                        textAlign = TextAlign.Center
                    )

                    Text(
                        text = "UTENTE NUMBER ${parking.utenteId}",
                        textAlign = TextAlign.Center,
                        fontSize = MaterialTheme.typography.titleMedium.fontSize,
                        color = MaterialTheme.colorScheme.primary
                    )


                    QrCodeView(
                        url = "http://26.23.254.172:8080/api/parking/payment/detail?utenteId=${parking.utenteId}",
                        modifier = Modifier
                            .size(200.dp)
                            .clip(MaterialTheme.shapes.medium)
                    )

                }
            }
        }
    }
}

