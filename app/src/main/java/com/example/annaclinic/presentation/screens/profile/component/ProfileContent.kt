package com.example.annaclinic.presentation.screens.profile.component

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.rounded.Logout
import androidx.compose.material3.Button
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.annaclinic.core.component.AnnaDialog
import com.example.annaclinic.core.component.DefaultTextField
import com.example.annaclinic.core.component.PasswordTextField
import com.example.annaclinic.core.theme.fontFamily
import com.example.annaclinic.domain.model.User

@Composable
fun ProfileContent(modifier: Modifier = Modifier, user: User, onLogout: () -> Unit) {
    var name by remember { mutableStateOf("") }
    var email by remember { mutableStateOf("") }
    var password by remember { mutableStateOf("") }
    var accountType by remember { mutableStateOf("") }
    var openDialog by remember { mutableStateOf(false) }

    Column(
        modifier = modifier
            .fillMaxSize()
            .padding(horizontal = 16.dp, vertical = 16.dp),
    ) {
        Spacer(modifier = modifier.size(8.dp))
        DefaultTextField(label = "Nama", value = user.name ?: "", isDisabled = true) {
            name = it
        }
        Spacer(modifier = modifier.size(16.dp))
        DefaultTextField(label = "Email", value = user.email ?: "", isDisabled = true) {
            email = it
        }
        Spacer(modifier = modifier.size(16.dp))
        PasswordTextField(label = "password", value = user.password ?: "", isDisabled = true) {
            password = it
        }
        Spacer(modifier = modifier.size(16.dp))
        DefaultTextField(label = "Tipe Akun", value = user.accountType ?: "", isDisabled = true) {
            accountType = it
        }
        Spacer(modifier = modifier.size(16.dp))
        Button(
            modifier = modifier
                .fillMaxWidth(),
            onClick = {
                openDialog = true
            }
        ) {
            Text(
                text = "Keluar",
                style = TextStyle(
                    fontSize = 16.sp,
                    fontFamily = fontFamily,
                    fontWeight = FontWeight.Medium
                )
            )
        }
    }

    when {
        openDialog -> {
            AnnaDialog(
                onDismissRequest = { openDialog = false },
                onConfirmation = {
                    onLogout()
                    openDialog = false
                },
                dialogTitle = "Keluar",
                dialogText = "Apakah Anda yakin ingin keluar? ",
                icon = Icons.AutoMirrored.Rounded.Logout
            )
        }
    }
}