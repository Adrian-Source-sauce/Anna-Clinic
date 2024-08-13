package com.example.annaclinic.presentation.screens.main.user

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
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.annaclinic.core.utils.Const
import com.example.annaclinic.core.utils.Response
import com.example.annaclinic.core.utils.SharedPrefUtils
import com.example.annaclinic.presentation.screens.main.MainViewModel
import com.valentinilk.shimmer.shimmer
import org.koin.compose.koinInject

@Composable
fun UsersQueue(
    modifier: Modifier = Modifier,
    viewModel: MainViewModel = koinInject(),
    sharedPref: SharedPrefUtils = koinInject()
) {
    val userQueue = viewModel.getQueueByEmail(sharedPref.getString(Const.EMAIL)!!).collectAsState(
        initial = Response.Loading
    )
    Card(
        modifier = modifier
            .size(180.dp, 120.dp)
            .clip(RoundedCornerShape(16.dp))
    ) {
        Column(
            modifier = Modifier
                .padding(horizontal = 24.dp, vertical = 24.dp)
                .fillMaxSize(),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Text(text = "Antrian Kamu")
            when (userQueue.value) {
                is Response.Loading -> {
                    // Display the loading
                    Log.d("UsersQueue", "Loading...")
                    Column(
                        modifier = modifier.fillMaxHeight(),
                        horizontalAlignment = Alignment.CenterHorizontally,
                        verticalArrangement = Arrangement.Center
                    ) {
                        Box(
                            modifier = modifier
                                .padding(top = 12.dp)
                                .size(40.dp)
                                .clip(RoundedCornerShape(12.dp))
                                .shimmer()
                                .background(Color.White)
                        )
                    }
                }

                is Response.Success -> {
                    // Handle success
                    val queueNumber = (userQueue.value as Response.Success<Int>).data
                    Log.d("UsersQueue", "Data: $queueNumber")
                    Column(
                        modifier = modifier.fillMaxHeight(),
                        horizontalAlignment = Alignment.CenterHorizontally,
                        verticalArrangement = Arrangement.Center
                    ) {
                        Spacer(modifier = modifier.padding(2.dp))
                        Text(
                            text = queueNumber.toString(),
                            style = TextStyle(
                                fontSize = 44.sp,
                                fontWeight = FontWeight.Bold,
                            )
                        )
                    }
                }

                is Response.Empty -> {
                    // Handle empty
                    Log.d("UsersQueue", "Empty")
                    Column(
                        modifier = modifier.fillMaxHeight(),
                        horizontalAlignment = Alignment.CenterHorizontally,
                        verticalArrangement = Arrangement.Center
                    ) {
                        Spacer(modifier = modifier.padding(2.dp))
                        Text(
                            text = "0",
                            style = TextStyle(
                                fontSize = 44.sp,
                                fontWeight = FontWeight.Bold,
                            )
                        )
                    }
                }

                is Response.Failure -> {
                    // Handle failure
                    Log.d("UsersQueue", "Error")
                    Column(
                        modifier = modifier.fillMaxHeight(),
                        horizontalAlignment = Alignment.CenterHorizontally,
                        verticalArrangement = Arrangement.Center
                    ) {
                        Spacer(modifier = modifier.padding(2.dp))
                        Text(
                            text = "0",
                            style = TextStyle(
                                fontSize = 44.sp,
                                fontWeight = FontWeight.Bold,
                            )
                        )
                    }
                }
            }
        }
    }
}