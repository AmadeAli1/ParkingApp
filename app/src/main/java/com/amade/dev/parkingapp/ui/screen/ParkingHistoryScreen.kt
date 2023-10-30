package com.amade.dev.parkingapp.ui.screen

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.Discount
import androidx.compose.material.icons.filled.HourglassBottom
import androidx.compose.material.icons.filled.KeyboardDoubleArrowDown
import androidx.compose.material.icons.filled.MonetizationOn
import androidx.compose.material.icons.filled.TimeToLeave
import androidx.compose.material.icons.filled.Timelapse
import androidx.compose.material3.Button
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.ProvideTextStyle
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.Font
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.font.toFontFamily
import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.Dialog
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.navigation.NavController
import com.amade.dev.parkingapp.R
import com.amade.dev.parkingapp.model.Payment
import com.amade.dev.parkingapp.ui.viewmodel.ParkingHistoryViewModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ParkingHistoryScreen(navController: NavController) {
    val viewModel = hiltViewModel<ParkingHistoryViewModel>()
    val uiState by viewModel.uiState.collectAsStateWithLifecycle()


    Scaffold(
        topBar = {
            TopAppBar(title = { Text(text = "Parking History") }, navigationIcon = {
                IconButton(onClick = navController::popBackStack) {
                    Icon(imageVector = Icons.Default.ArrowBack, contentDescription = null)
                }
            })
        },
        bottomBar = {
            OutlinedButton(
                onClick = { /*TODO*/ },
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(8.dp),
                contentPadding = PaddingValues(12.dp),
                shape = MaterialTheme.shapes.small,
                enabled = false
            ) {
                Text(text = "Total ${uiState.total} MTs")
            }
        }
    ) {
        Box(
            modifier = Modifier
                .fillMaxSize()
                .padding(it)
        ) {
            LazyColumn(
                modifier = Modifier
                    .fillMaxSize(),
                contentPadding = PaddingValues(8.dp),
                verticalArrangement = Arrangement.spacedBy(16.dp),
                content = {
                    items(
                        items = uiState.data, key = { key -> key.id }
                    ) { item ->
                        PaymentItem(payment = item, onClick = {})
                    }
                }
            )

            if (uiState.isLoading) {
                Dialog(onDismissRequest = { /*TODO*/ }) {
                    CircularProgressIndicator()
                }
            } else if (uiState.data.isEmpty()) {
                Column(
                    modifier = Modifier.align(Alignment.Center),
                    verticalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    Text(text = "Payments not found or doesn't exists!")
                    Button(onClick = viewModel::findAllPayments) {
                        Text(text = "Try again")
                    }
                }
            }
        }


    }

}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun PaymentItem(payment: Payment, onClick: () -> Unit) {
    Surface(
        onClick = onClick,
        shape = MaterialTheme.shapes.small,
        tonalElevation = 1.5.dp, shadowElevation = 1.5.dp
    ) {
        ProvideTextStyle(
            value = MaterialTheme.typography.titleMedium.copy(
                fontWeight = FontWeight.Bold,
                fontFamily = Font(R.font.poppins_bold).toFontFamily()
            ),
        ) {
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(12.dp),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.spacedBy(4.dp)
                ) {
                    Icon(
                        imageVector = Icons.Default.KeyboardDoubleArrowDown,
                        contentDescription = null, tint = MaterialTheme.colorScheme.primary
                    )
                    Text(text = payment.entranceTime, color = MaterialTheme.colorScheme.primary)
                }
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.spacedBy(4.dp)
                ) {
                    Icon(
                        imageVector = Icons.Default.TimeToLeave,
                        contentDescription = null, tint = MaterialTheme.colorScheme.error
                    )
                    Text(
                        text = payment.exitTime,
                        color = MaterialTheme.colorScheme.error
                    )
                }
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.spacedBy(4.dp)
                ) {
                    Icon(
                        imageVector = Icons.Default.Timelapse,
                        contentDescription = null, tint = MaterialTheme.colorScheme.error
                    )
                    Text(
                        text = payment.timeInParking,
                        color = MaterialTheme.colorScheme.error
                    )
                }

                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.spacedBy(4.dp)
                ) {
                    Icon(
                        imageVector = Icons.Default.HourglassBottom,
                        contentDescription = null, tint = MaterialTheme.colorScheme.onErrorContainer
                    )
                    Text(
                        text = "Price per hour ${payment.amountPerHour} Mts",
                        color = MaterialTheme.colorScheme.onErrorContainer
                    )
                }

                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.spacedBy(4.dp)
                ) {
                    Icon(
                        imageVector = Icons.Default.Discount,
                        contentDescription = null, tint = MaterialTheme.colorScheme.onErrorContainer
                    )
                    Text(
                        text = "Discount ${payment.discount} Mts",
                        color = MaterialTheme.colorScheme.onErrorContainer
                    )
                }

                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.spacedBy(4.dp)
                ) {
                    Icon(
                        imageVector = Icons.Default.MonetizationOn,
                        contentDescription = null, tint = MaterialTheme.colorScheme.onErrorContainer
                    )
                    Text(
                        text = "Final price ${payment.amount} Mts",
                        color = MaterialTheme.colorScheme.onErrorContainer
                    )
                }


            }
        }

    }
}