package com.example.annaclinic.presentation.screens.main.admin.screen

import android.util.Log
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Restore
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.compose.LocalLifecycleOwner
import com.example.annaclinic.R
import com.example.annaclinic.core.component.CenterAppBar
import com.example.annaclinic.core.utils.Const
import com.example.annaclinic.core.utils.Response
import com.example.annaclinic.core.utils.SharedPrefUtils
import com.example.annaclinic.presentation.screens.main.MainViewModel
import com.example.annaclinic.presentation.screens.main.admin.FabAdmin
import com.example.annaclinic.presentation.screens.main.component.CardQueue
import org.koin.compose.koinInject

@Composable
fun HomeScreen(
    modifier: Modifier = Modifier,
    viewModel: MainViewModel = koinInject(),
    prefs: SharedPrefUtils = koinInject()
) {
    val queueCounter = remember { mutableIntStateOf(1) }
    val lifecycleOwner = LocalLifecycleOwner.current

    Scaffold(
        floatingActionButton = {
            Column {
                FabAdmin(icon = Icons.Filled.Add) {
                    viewModel.postQueue(
                        queueCounter.intValue + 1,
                        prefs.getString(Const.ACCOUNT_TYPE)!!
                    ).observe(lifecycleOwner) {
                        when (it) {
                            is Response.Loading -> {
                                Log.d("MainScreen", "Loading")
                            }

                            is Response.Success -> {
                                Log.d("MainScreen", "Queue ${queueCounter.intValue} posted")
                                queueCounter.intValue += 1
                            }

                            is Response.Failure -> {
                                Log.d("MainScreen", "Error: ${it.msg}")
                            }

                            is Response.Empty -> {}
                        }
                    }
                }
                Spacer(modifier = Modifier.size(16.dp))
                FabAdmin(icon = Icons.Filled.Restore) {
                    // Handle click
                    viewModel.postQueue(
                        0,
                        prefs.getString(Const.ACCOUNT_TYPE)!!
                    ).observe(lifecycleOwner) {
                        when (it) {
                            is Response.Loading -> {
                                Log.d("MainScreen", "Loading")
                            }

                            is Response.Success -> {
                                Log.d("MainScreen", "Queue reset")
                                queueCounter.intValue = 0
                            }

                            is Response.Failure -> {
                                Log.d("MainScreen", "Error: ${it.msg}")
                            }

                            is Response.Empty -> {}
                        }
                    }
                }
            }
        },
        topBar = {
            CenterAppBar(R.string.anna_clinic)
        }
    ) {
        Column(
            modifier = modifier
                .padding(it)
                .background(androidx.compose.ui.graphics.Color.White)
                .fillMaxSize(),
            horizontalAlignment = Alignment.CenterHorizontally,
        ) {
            Spacer(modifier = Modifier.size(120.dp))
            CardQueue(
                width = 300.dp,
                height = 300.dp,
                fontSize = 64.sp,
                shimmerSize = 64.dp
            )
        }
    }
}