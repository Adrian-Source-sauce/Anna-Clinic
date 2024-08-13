package com.example.annaclinic.presentation.screens.main.component

import android.util.Log
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Card
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.unit.dp
import androidx.compose.ui.util.lerp
import coil.compose.AsyncImage
import com.example.annaclinic.core.utils.Response
import com.example.annaclinic.core.utils.decodeBase64
import com.example.annaclinic.presentation.screens.main.MainViewModel
import com.google.accompanist.pager.ExperimentalPagerApi
import com.google.accompanist.pager.HorizontalPager
import com.google.accompanist.pager.HorizontalPagerIndicator
import com.google.accompanist.pager.calculateCurrentOffsetForPage
import com.google.accompanist.pager.rememberPagerState
import com.valentinilk.shimmer.shimmer
import kotlinx.coroutines.delay
import kotlinx.coroutines.yield
import org.koin.compose.koinInject
import kotlin.math.absoluteValue

@OptIn(ExperimentalPagerApi::class)
@Composable
fun BannerSlider(
    modifier: Modifier = Modifier,
    viewModel: MainViewModel = koinInject(),
) {
    val pagerState = rememberPagerState(initialPage = 0)
    val listImage = viewModel.getListImage.collectAsState(initial = Response.Loading)

    Column {
        when (listImage.value) {
            is Response.Loading -> {
                // Display the loading
                Log.d("BannerSlider", "Loading...")
                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(114.dp)
                        .padding(horizontal = 16.dp)
                        .clip(RoundedCornerShape(16.dp))
                        .shimmer()
                        .background(color = Color.LightGray),

                )
            }

            is Response.Success -> {
                LaunchedEffect(Unit) {
                    while (true) {
                        yield()
                        delay(2500)
                        pagerState.animateScrollToPage(
                            page = (pagerState.currentPage + 1) % (pagerState.pageCount)
                        )
                    }
                }
                Log.d("BannerSlider", "List Image -> ${(listImage.value as Response.Success).data}")

                val list = (listImage.value as Response.Success).data
                // convert each item list to base64
                val listBase64 = list.map { decodeBase64(it.imageBase64) }

                Column {
                    HorizontalPager(
                        count = list.size,
                        state = pagerState,
                        contentPadding = PaddingValues(horizontal = 16.dp),
                        modifier = modifier
                            .height(114.dp)
                            .fillMaxWidth()
                    ) { page ->
                        Card(
                            shape = RoundedCornerShape(16.dp),
                            modifier = modifier.graphicsLayer {
                                val pageOffset = calculateCurrentOffsetForPage(page).absoluteValue

                                lerp(
                                    start = 0.85f,
                                    stop = 1f,
                                    fraction = 1f - pageOffset.coerceIn(0f, 1f)
                                ).also { scale ->
                                    scaleX = scale
                                    scaleY = scale
                                }

                                alpha = lerp(
                                    start = 0.5f,
                                    stop = 1f,
                                    fraction = 1f - pageOffset.coerceIn(0f, 1f)
                                )
                            }
                        ) {
                            AsyncImage(
                                model = listBase64[page],
                                contentDescription = "banner untuk promosi",
                                contentScale = ContentScale.Crop
                            )
                        }
                    }

                    HorizontalPagerIndicator(
                        pagerState = pagerState,
                        modifier = modifier
                            .align(Alignment.CenterHorizontally)
                            .padding(16.dp)
                    )
                }
            }

            is Response.Failure -> {
                // Display the failure
                Log.e("BannerSlider", "Error: ${(listImage.value as Response.Failure).msg}")
            }

            is Response.Empty -> {
                Log.d("BannerSlider", "Empty")
                Box {
                    // do nothing
                }
            }
        }
    }
}