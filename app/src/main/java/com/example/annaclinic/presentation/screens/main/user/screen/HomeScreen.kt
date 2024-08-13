package com.example.annaclinic.presentation.screens.main.user.screen

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material3.BottomSheetScaffold
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.ExtendedFloatingActionButton
import androidx.compose.material3.FabPosition
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SheetValue
import androidx.compose.material3.Text
import androidx.compose.material3.rememberBottomSheetScaffoldState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.derivedStateOf
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import cafe.adriel.voyager.navigator.LocalNavigator
import cafe.adriel.voyager.navigator.currentOrThrow
import com.example.annaclinic.R
import com.example.annaclinic.core.component.CenterAppBar
import com.example.annaclinic.core.theme.fontFamily
import com.example.annaclinic.presentation.screens.main.component.BannerSlider
import com.example.annaclinic.presentation.screens.main.user.UserReservationList
import com.example.annaclinic.presentation.screens.reservation.list.ReservationListScreen

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun HomeScreen(modifier: Modifier = Modifier) {
    val navigator = LocalNavigator.currentOrThrow
    val scaffoldState = rememberBottomSheetScaffoldState()
    val configuration = LocalConfiguration.current
    val screenHeight = configuration.screenHeightDp.dp
    val sheetPeekHeight = screenHeight * 0.6f

    val expandedFab by remember {
        derivedStateOf {
            scaffoldState.bottomSheetState.targetValue == SheetValue.PartiallyExpanded
        }
    }

    Scaffold(
        floatingActionButton = {
            ExtendedFloatingActionButton(
                onClick = {
                    // Handle click
                    navigator.push(ReservationListScreen())
                },
                expanded = expandedFab,
                icon = { Icon(Icons.Filled.Add, "Localized Description") },
                text = { Text(text = "Reservasi") },
                containerColor = MaterialTheme.colorScheme.primary,
                contentColor = MaterialTheme.colorScheme.onPrimary,
            )
        },
        floatingActionButtonPosition = FabPosition.End,
    ) {
        BottomSheetScaffold(
            modifier = modifier.padding(it),
            scaffoldState = scaffoldState,
            sheetPeekHeight = sheetPeekHeight,
            sheetDragHandle = { Box { } },
            sheetShadowElevation = 8.dp,
            topBar = { CenterAppBar(R.string.anna_clinic) },

            sheetContent = {
                Column(
                    modifier = modifier
                        .fillMaxWidth()
                        .padding(16.dp),
                    horizontalAlignment = Alignment.Start
                ) {
                    Text(
                        text = "Reservasi Kamu",
                        style = TextStyle(
                            fontSize = 24.sp,
                            fontWeight = FontWeight.Medium,
                            fontFamily = fontFamily
                        )
                    )
                    Spacer(modifier = modifier.padding(top = 16.dp))
                    UserReservationList(navigator = navigator)
                }
            }) { innerPadding ->
            Column(
                modifier = Modifier
                    .background(Color.White)
                    .fillMaxSize()
                    .padding(innerPadding),
            ) {
                Spacer(modifier = Modifier.padding(16.dp))
                BannerSlider()
            }
        }
    }
}
