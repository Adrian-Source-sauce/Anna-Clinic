package com.example.annaclinic.presentation.screens.main.admin.component

import android.content.Intent
import android.net.Uri
import androidx.compose.foundation.gestures.detectTapGestures
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Button
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalSoftwareKeyboardController
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.annaclinic.core.component.DefaultTextField
import com.example.annaclinic.core.theme.fontFamily
import com.example.annaclinic.domain.model.Reservation


@Composable
fun SendNotification(
    reservation: Reservation,
    modifier: Modifier = Modifier
) {
    var emailSubject by remember { mutableStateOf("Reservasi Anda") }
    var message by remember { mutableStateOf("") }
    val keyboardController = LocalSoftwareKeyboardController.current
    val context = LocalContext.current

    Column(
        modifier = modifier
            .padding(horizontal = 16.dp, vertical = 16.dp)
            .verticalScroll(rememberScrollState())
            .pointerInput(Unit) {
                detectTapGestures(onTap = {
                    keyboardController?.hide()
                })
            },
    ) {
        Text(
            text = "Kirim Notifikasi Via Email",
            style = TextStyle(
                fontSize = 20.sp,
                fontFamily = fontFamily
            )
        )
        Spacer(modifier = Modifier.padding(8.dp))

        DefaultTextField(label = "Email", value = reservation.email, isDisabled = true)
        Spacer(modifier = Modifier.padding(8.dp))

        DefaultTextField(label = "Subjek", value = "Reservasi Anda", isDisabled = true) {
            emailSubject = it
        }
        Spacer(modifier = Modifier.padding(8.dp))

        DefaultTextField(label = "Pesan", value = message, isSingleLine = false, lines = 8) {
            message = it
        }
        Spacer(modifier = Modifier.padding(16.dp))

        Button(
            modifier = modifier.fillMaxWidth(),
            onClick = {
                val selectorIntent = Intent(Intent.ACTION_SENDTO)
                selectorIntent.setData(Uri.parse("mailto:"))

                val emailIntent = Intent(Intent.ACTION_SEND)
                emailIntent.putExtra(Intent.EXTRA_EMAIL, arrayOf(reservation.email))
                emailIntent.putExtra(Intent.EXTRA_SUBJECT, emailSubject)
                emailIntent.putExtra(Intent.EXTRA_TEXT, message)
                emailIntent.selector = selectorIntent
                context.startActivity(Intent.createChooser(emailIntent, "Send email..."))
            }
        ) {
            Text(text = "Kirim")
        }
    }
}