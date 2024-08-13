package com.example.annaclinic.presentation.screens.main.user

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.OutlinedCard
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.annaclinic.core.theme.fontFamily
import com.example.annaclinic.core.utils.Response
import com.example.annaclinic.domain.model.Reservation
import com.example.annaclinic.presentation.screens.main.MainViewModel
import com.valentinilk.shimmer.shimmer

@Composable
fun UserReservationItem(
    modifier: Modifier = Modifier,
    reservation: Reservation,
    viewModel: MainViewModel,
    onClick: () -> Unit
) {
    val realtimeQueue = viewModel.getRealTimeQueue.collectAsState(initial = Response.Loading)

    Column {
        OutlinedCard(onClick = { onClick() }) {
            Row(
                modifier = modifier
                    .fillMaxWidth()
                    .padding(16.dp),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Box(
                    modifier = modifier
                        .weight(1f)
                        .padding(end = 16.dp)
                ) {
                    Column(
                        verticalArrangement = Arrangement.spacedBy(6.dp),
                    ) {
                        Text(
                            text = reservation.service,
                            style = TextStyle(
                                fontSize = 18.sp,
                                fontFamily = fontFamily,
                                fontWeight = FontWeight.Medium
                            )
                        )
                        if (reservation.product != "" && reservation.totalProductPrice != "") {
                            Text(
                                text = reservation.product!!,
                                style = TextStyle(fontSize = 14.sp, fontFamily = fontFamily)
                            )
                        }
                        Text(
                            text = reservation.date,
                            style = TextStyle(fontFamily = fontFamily)
                        )
                        Text(
                            text = reservation.totalPrice,
                            style = TextStyle(fontFamily = fontFamily)
                        )
                    }
                }

                when (realtimeQueue.value) {
                    is Response.Loading -> {
                        Box(
                            modifier = modifier
                                .size(40.dp)
                                .clip(CircleShape)
                                .shimmer()
                                .background(color = Color.LightGray)
                        )
                    }

                    is Response.Success -> {
                        Box(
                            contentAlignment = Alignment.Center,
                            modifier = modifier
                                .size(40.dp)
                                .clip(CircleShape)
                                .background(Color(0xFFFFCCBC))
                        ) {
                            Text(
                                text = reservation.queue.toString(),
                                textAlign = TextAlign.Center,
                                style = TextStyle(
                                    fontFamily = fontFamily,
                                    fontSize = 24.sp,
                                    fontWeight = FontWeight.Bold
                                ),
                            )
                        }
                    }

                    is Response.Empty -> {}

                    is Response.Failure -> {}
                }
            }
        }
    }
}