package com.example.annaclinic.presentation.screens.banner

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import cafe.adriel.voyager.core.screen.Screen
import cafe.adriel.voyager.navigator.LocalNavigator
import cafe.adriel.voyager.navigator.currentOrThrow
import com.example.annaclinic.core.component.CenterTopBar
import com.example.annaclinic.presentation.screens.main.admin.component.ImagePicker

class UploadBannerScreen : Screen {
    @Composable
    override fun Content() {
        val navigator = LocalNavigator.currentOrThrow
        Scaffold(
            topBar = { CenterTopBar(navigator = navigator, title = "Tambah Banner") }
        ) {
            Column(
                modifier = Modifier.padding(it)
            ) {
                ImagePicker()
            }
        }
    }
}