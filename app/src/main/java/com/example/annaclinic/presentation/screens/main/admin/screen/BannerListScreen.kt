package com.example.annaclinic.presentation.screens.main.admin.screen

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material3.ExtendedFloatingActionButton
import androidx.compose.material3.FabPosition
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import cafe.adriel.voyager.navigator.LocalNavigator
import cafe.adriel.voyager.navigator.currentOrThrow
import com.example.annaclinic.R
import com.example.annaclinic.core.component.CenterAppBar
import com.example.annaclinic.core.theme.fontFamily
import com.example.annaclinic.presentation.screens.banner.UploadBannerScreen
import com.example.annaclinic.presentation.screens.main.MainViewModel
import com.example.annaclinic.presentation.screens.main.admin.component.BannerList
import org.koin.compose.koinInject

@Composable
fun UploadImageScreen(
    modifier: Modifier = Modifier,
    viewModel: MainViewModel = koinInject()
) {
    val navigator = LocalNavigator.currentOrThrow
    Scaffold(
        floatingActionButton = {
            ExtendedFloatingActionButton(
                onClick = {
                    // Handle click
                    navigator.push(UploadBannerScreen())
                },
//                expanded = expandedFab,
                icon = { Icon(Icons.Filled.Add, "Localized Description") },
                text = {
                    Text(
                        text = "Tambah", style = TextStyle(
                            fontSize = 18.sp,
                            fontFamily = fontFamily
                        )
                    )
                },
                containerColor = MaterialTheme.colorScheme.primary,
                contentColor = MaterialTheme.colorScheme.onPrimary,
            )
        },
        floatingActionButtonPosition = FabPosition.End,
    ) {
        Column(
            modifier = modifier.padding(it).background(androidx.compose.ui.graphics.Color.White)
        ) {
            CenterAppBar(R.string.upload_image)
            Column(
                modifier = modifier
                    .fillMaxWidth()
                    .fillMaxHeight()
                    .padding(horizontal = 16.dp)
            ) {
                BannerList()
            }
        }
    }
}