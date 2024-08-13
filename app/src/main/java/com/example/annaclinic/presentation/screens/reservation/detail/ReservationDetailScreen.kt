package com.example.annaclinic.presentation.screens.reservation.detail

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import cafe.adriel.voyager.core.screen.Screen
import cafe.adriel.voyager.navigator.LocalNavigator
import cafe.adriel.voyager.navigator.currentOrThrow
import com.example.annaclinic.R
import com.example.annaclinic.core.component.CenterAppBar
import com.example.annaclinic.core.component.ReservationTicket
import com.example.annaclinic.domain.model.Reservation
import com.example.annaclinic.presentation.screens.reservation.detail.component.ReservationDetailTopBar

data class ReservationDetailScreen(
    val reservation: Reservation
) : Screen {
    @Composable
    override fun Content() {
        val navigator = LocalNavigator.currentOrThrow

        Column {
            ReservationDetailTopBar(navigator = navigator)
            Column(
                modifier = Modifier.fillMaxSize(),
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.Center
            ) {
                ReservationTicket(reservation = reservation)
            }
        }
    }
}