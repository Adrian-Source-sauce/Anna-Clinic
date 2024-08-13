package com.example.annaclinic.presentation.screens.main.admin.screen

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.example.annaclinic.R
import com.example.annaclinic.core.component.CenterAppBar
import com.example.annaclinic.presentation.screens.main.admin.component.ReservationList

@Composable
fun ReservationListScreen(
    modifier: Modifier = Modifier,
) {
    Scaffold(
        topBar = { CenterAppBar(R.string.reservation_list) }
    ) {
        Column(
            modifier = modifier.padding(it).background(androidx.compose.ui.graphics.Color.White)
        ) {
            Column(
                modifier = modifier
                    .fillMaxWidth()
                    .fillMaxHeight()
                    .padding(horizontal = 16.dp)
            ) {
                ReservationList()
            }
        }
    }
}

