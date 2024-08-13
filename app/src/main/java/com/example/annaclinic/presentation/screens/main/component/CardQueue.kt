package com.example.annaclinic.presentation.screens.main.component

import android.util.Log
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Card
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.TextUnit
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.annaclinic.core.utils.Response
import com.example.annaclinic.presentation.screens.main.MainViewModel
import com.valentinilk.shimmer.shimmer
import org.koin.compose.koinInject

@Composable
fun CardQueue(
    modifier: Modifier = Modifier,
    width: Dp = 180.dp,
    height: Dp = 120.dp,
    shimmerSize: Dp = 40.dp,
    fontSize: TextUnit = 44.sp,
    viewModel: MainViewModel = koinInject()
) {
    val realTimeQueue = viewModel.getRealTimeQueue.collectAsState(initial = Response.Loading)

    Card(
        modifier
            .size(width, height)
            .clip(RoundedCornerShape(16.dp))
    ) {
        Column(
            modifier = modifier
                .padding(horizontal = 24.dp, vertical = 24.dp)
                .fillMaxSize(),
            horizontalAlignment = Alignment.CenterHorizontally,
        ) {
            Text(
                text = "Antrian",
                maxLines = 1,
                overflow = TextOverflow.Ellipsis,
            )
            when (realTimeQueue.value) {
                is Response.Loading -> {
                    Column(
                        modifier = modifier.fillMaxHeight(),
                        horizontalAlignment = Alignment.CenterHorizontally,
                        verticalArrangement = Arrangement.Center
                    ) {
                        Box(
                            modifier = modifier
                                .padding(top = 12.dp)
                                .size(shimmerSize)
                                .clip(RoundedCornerShape(12.dp))
                                .shimmer()
                                .background(Color.White)
                        )
                    }
                }

                is Response.Success -> {
                    val queue = (realTimeQueue.value as Response.Success).data
                    // Display the queue
                    Column(
                        modifier = modifier.fillMaxHeight(),
                        horizontalAlignment = Alignment.CenterHorizontally,
                        verticalArrangement = Arrangement.Center
                    ) {
                        Spacer(modifier = modifier.padding(2.dp))
                        Text(
                            text = queue.toString(), style = TextStyle(
                                fontSize = fontSize,
                                fontWeight = FontWeight.Bold,
                            )
                        )
                    }
                }

                is Response.Failure -> {
                    val error = (realTimeQueue.value as Response.Failure).msg
                    Log.e("CardQueue", "error: $error")
                    Text(text = "0")
                }

                is Response.Empty -> {}
            }
        }
    }
}