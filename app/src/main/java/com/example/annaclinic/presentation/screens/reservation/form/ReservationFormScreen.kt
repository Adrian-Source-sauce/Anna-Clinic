package com.example.annaclinic.presentation.screens.reservation.form

import android.os.Build
import androidx.annotation.RequiresApi
import androidx.compose.foundation.background
import androidx.compose.foundation.gestures.detectTapGestures
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.platform.LocalSoftwareKeyboardController
import cafe.adriel.voyager.core.screen.Screen
import cafe.adriel.voyager.navigator.LocalNavigator
import cafe.adriel.voyager.navigator.currentOrThrow
import com.example.annaclinic.R
import com.example.annaclinic.core.component.CenterAppBar
import com.example.annaclinic.domain.model.Products
import com.example.annaclinic.presentation.screens.reservation.form.component.ReservationForm

data class ReservationFormScreen(
    val name: String,
    val price: Long,
    val products: List<Products>? = null
) : Screen {

    @RequiresApi(Build.VERSION_CODES.O)
    @Composable
    override fun Content() {
        val keyboardController = LocalSoftwareKeyboardController.current
        val navigator = LocalNavigator.currentOrThrow

        Scaffold(
            topBar = {
                CenterAppBar(titleRes = R.string.reservasi, navigator = navigator)
            }
        ) {
            Column(
                modifier = Modifier
                    .padding(it)
                    .background(androidx.compose.ui.graphics.Color.White)
                    .fillMaxSize()
                    .verticalScroll(rememberScrollState())
                    .pointerInput(Unit) {
                        detectTapGestures(onTap = {
                            keyboardController?.hide()
                        })
                    },
            ) {
                ReservationForm(
                    serviceName = name,
                    servicePrice = price,
                    addOnProducts = products,
                    navigator = navigator,
                )

            }
        }

    }

}