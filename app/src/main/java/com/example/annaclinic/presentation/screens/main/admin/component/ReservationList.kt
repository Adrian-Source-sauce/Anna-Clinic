package com.example.annaclinic.presentation.screens.main.admin.component

import android.util.Log
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.example.annaclinic.core.component.CircularLoading
import com.example.annaclinic.core.component.LottieAnim
import com.example.annaclinic.core.utils.Const
import com.example.annaclinic.core.utils.Response
import com.example.annaclinic.core.utils.SharedPrefUtils
import com.example.annaclinic.presentation.screens.main.MainViewModel
import org.koin.compose.koinInject

@Composable
fun ReservationList(
    modifier: Modifier = Modifier,
    viewModel: MainViewModel = koinInject(),
    prefs: SharedPrefUtils = koinInject()
) {
    val adminReservationList = viewModel.getAllReservation(prefs.getString(Const.ACCOUNT_TYPE)!!)
        .collectAsState(initial = Response.Loading)

    Column(modifier = modifier.padding(top = 16.dp)) {
        when (adminReservationList.value) {
            is Response.Loading -> {
                Log.d("AdminReservationList", "Loading...")
                CircularLoading(isLoading = true)
            }

            is Response.Success -> {
                // Display the reservation list
                CircularLoading(isLoading = false)
                val data = (adminReservationList.value as Response.Success).data
                Log.d("AdminReservationList", "Data: $data")

                LazyColumn(
                    verticalArrangement = Arrangement.spacedBy(12.dp),
                    contentPadding = PaddingValues(bottom = 120.dp),
                ) {
                    items(data) { reservation ->
                        ReservationItem(reservation = reservation)
                    }
                }
            }

            is Response.Empty -> {
                CircularLoading(isLoading = false)
                LottieAnim(
                    modifier = modifier.padding(top = 64.dp),
                    urlLottie = "https://lottie.host/416f4bd1-7e56-40e3-a35e-524facc3dee2/70l3L2gcUh.json",
                    text = "Belum ada reservasi yang masuk!",
                    size = 280.dp
                )
            }

            is Response.Failure -> {
                CircularLoading(isLoading = false)
                LottieAnim(
                    modifier = modifier.padding(top = 85.dp),
                    urlLottie = "https://lottie.host/27dab8d8-9e4b-41a2-b99f-739f425f6706/6Y6JifJvwX.lottie",
                    text = "Maaf ya, lagi ada gangguan coba lagi nanti!",
                    size = 250.dp
                )
            }
        }
    }
}