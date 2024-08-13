package com.example.annaclinic.presentation.screens.main.user

import android.util.Log
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import cafe.adriel.voyager.navigator.Navigator
import com.example.annaclinic.core.component.CircularLoading
import com.example.annaclinic.core.component.LottieAnim
import com.example.annaclinic.core.utils.Const
import com.example.annaclinic.core.utils.Response
import com.example.annaclinic.core.utils.SharedPrefUtils
import com.example.annaclinic.presentation.screens.main.MainViewModel
import com.example.annaclinic.presentation.screens.reservation.detail.ReservationDetailScreen
import com.valentinilk.shimmer.shimmer
import org.koin.compose.koinInject

@Composable
fun UserReservationList(
    modifier: Modifier = Modifier,
    navigator: Navigator,
    viewModel: MainViewModel = koinInject(),
    prefs: SharedPrefUtils = koinInject()
) {
    val userReservationList = viewModel.getListByEmail(prefs.getString(Const.EMAIL)!!)
        .collectAsState(initial = Response.Loading)

    Column(
        modifier = modifier.fillMaxSize(),
    ) {
        when (userReservationList.value) {
            is Response.Loading -> {
                // Handle loading
                Log.d("UserReservationList", "Loading...")
                LazyColumn(
                    verticalArrangement = Arrangement.spacedBy(8.dp),
                ) {
                    items(5) {
                        Box(
                            modifier = Modifier
                                .fillMaxWidth()
                                .height(85.dp)
                                .clip(RoundedCornerShape(16.dp))
                                .shimmer()
                                .background(color = Color.LightGray)
                        )
                    }
                }
            }

            is Response.Success -> {
                // Display the reservation list
                CircularLoading(isLoading = false)
                val data = (userReservationList.value as Response.Success).data.sortedByDescending {
                    it.date
                }
                Log.d("UserReservationList", "Data: $data")

                LazyColumn(
                    verticalArrangement = Arrangement.spacedBy(8.dp),
                ) {
                    items(data) { reservation ->
                        UserReservationItem(reservation = reservation, viewModel = viewModel) {
                            navigator.push(
                                ReservationDetailScreen(reservation = reservation)
                            )
                        }
                    }

                }
            }

            is Response.Empty -> {
                // Handle empty data
                CircularLoading(isLoading = false)
                LottieAnim(
                    urlLottie = "https://lottie.host/04e37623-dfc9-4150-ae56-3ae60f6cc6c0/L4jIW578WX.lottie",
                    text = "Waduh, kamu belum melakukan reservasi nih, yuk reservasi sekarang!",
                    size = 200.dp
                )
            }

            is Response.Failure -> {
                // Display the error
                CircularLoading(isLoading = false)
                LottieAnim(
                    urlLottie = "https://lottie.host/27dab8d8-9e4b-41a2-b99f-739f425f6706/6Y6JifJvwX.lottie",
                    text = "Maaf ya, lagi ada gangguan coba lagi nanti!",
                    size = 250.dp
                )
            }
        }
    }

}