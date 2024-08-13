package com.example.annaclinic.presentation.screens.main.admin.component

import android.util.Log
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material3.Card
import androidx.compose.material3.ColorScheme
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedCard
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.LocalLifecycleOwner
import coil.compose.AsyncImage
import com.example.annaclinic.core.component.AnnaDialog
import com.example.annaclinic.core.component.CircularLoading
import com.example.annaclinic.core.component.LottieAnim
import com.example.annaclinic.core.utils.Response
import com.example.annaclinic.core.utils.decodeBase64
import com.example.annaclinic.presentation.screens.main.MainViewModel
import org.koin.compose.koinInject

@Composable
fun BannerList(
    modifier: Modifier = Modifier,
    viewModel: MainViewModel = koinInject()
) {
    val listImage = viewModel.getListImage.collectAsState(initial = Response.Loading)

    Column(
        modifier = modifier.padding(top = 16.dp)
    ) {
        when (listImage.value) {
            is Response.Loading -> {
                // Display the loading
                Log.d("BannerListAdmin", "Loading...")
                CircularLoading(isLoading = true)
            }

            is Response.Success -> {
                CircularLoading(isLoading = false)
                val data = (listImage.value as Response.Success).data
                Log.d("BannerListAdmin", "Success")
                Log.d("BannerListAdmin", data.toString())

                LazyColumn(
                    verticalArrangement = Arrangement.spacedBy(12.dp),
                ) {
                    items(data) { banner ->
                        BannerListItem(banner = banner)
                    }
                }
            }

            is Response.Empty -> {
                // Display the empty state
                CircularLoading(isLoading = false)
                Log.d("BannerListAdmin", "Empty")
                Column(
                    modifier= modifier
                        .fillMaxHeight()
                        .padding(top = 64.dp)
                ) {
                    LottieAnim(
                        urlLottie = "https://lottie.host/416f4bd1-7e56-40e3-a35e-524facc3dee2/70l3L2gcUh.json",
                        text = "Waduh, belum ada banner nih, yuk tambah banner sekarang!",
                        size = 280.dp
                    )
                }
            }

            is Response.Failure -> {
                // Display the error
                CircularLoading(isLoading = false)
                Log.d("BannerListAdmin", "Error")
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

//@Preview
