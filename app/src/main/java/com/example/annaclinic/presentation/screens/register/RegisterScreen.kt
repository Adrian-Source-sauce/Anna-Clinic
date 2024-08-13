package com.example.annaclinic.presentation.screens.register

import android.widget.Toast
import androidx.compose.foundation.Image
import androidx.compose.foundation.clickable
import androidx.compose.foundation.gestures.detectTapGestures
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.safeDrawing
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.windowInsetsPadding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.FilledTonalButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalSoftwareKeyboardController
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.compose.LocalLifecycleOwner
import cafe.adriel.voyager.core.screen.Screen
import cafe.adriel.voyager.navigator.LocalNavigator
import cafe.adriel.voyager.navigator.currentOrThrow
import com.example.annaclinic.R
import com.example.annaclinic.core.component.DefaultTextField
import com.example.annaclinic.core.component.DropDownTextField
import com.example.annaclinic.core.component.PasswordTextField
import com.example.annaclinic.core.theme.fontFamily
import com.example.annaclinic.core.utils.Response
import com.example.annaclinic.presentation.screens.login.LoginScreen
import org.koin.androidx.compose.koinViewModel


class RegisterScreen : Screen {
    @OptIn(ExperimentalMaterial3Api::class)
    @Composable
    override fun Content() {
        val viewModel: RegisterViewModel = koinViewModel()
        val navigator = LocalNavigator.currentOrThrow
        val context = LocalContext.current
        val lifecycleOwner = LocalLifecycleOwner.current
        val keyboardController = LocalSoftwareKeyboardController.current
        var name by remember { mutableStateOf("") }
        var email by remember { mutableStateOf("") }
        var password by remember { mutableStateOf("") }
        var confirmPassword by remember { mutableStateOf("") }
        var accountType by remember { mutableStateOf("") }

        fun emailValidator(email: String): Boolean {
            return android.util.Patterns.EMAIL_ADDRESS.matcher(email).matches()
        }

        Column(
            modifier = Modifier
                .fillMaxSize()
                .windowInsetsPadding(WindowInsets.safeDrawing)
                .verticalScroll(rememberScrollState())
                .pointerInput(Unit) {
                    detectTapGestures(onTap = {
                        keyboardController?.hide()
                    })
                },
            verticalArrangement = Arrangement.SpaceAround,
            horizontalAlignment = Alignment.CenterHorizontally,
        ) {
            Column(
                modifier = Modifier.padding(horizontal = 16.dp),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                Image(
                    painterResource(id = R.drawable.illustration_signup),
                    // change size of image
                    modifier = Modifier.size(280.dp),
                    contentDescription = "Illustration Signup"
                )
                Text(
                    text = "Daftar Akun",
                    style = TextStyle(
                        fontSize = 24.sp,
                        fontWeight = FontWeight.Bold,
                        fontFamily = fontFamily,
                    ),
                )
                Spacer(modifier = Modifier.size(16.dp))

                DefaultTextField(label = "Nama", value = name) { name = it }
                Spacer(modifier = Modifier.size(8.dp))

                DefaultTextField(label = "Email", value = email) { email = it }
                Spacer(modifier = Modifier.size(8.dp))

                PasswordTextField(label = "Password", value = password) { password = it }
                Spacer(modifier = Modifier.size(8.dp))

                PasswordTextField(
                    label = "Konfirmasi Password",
                    value = confirmPassword
                ) {
                    confirmPassword = it
                }
                Spacer(modifier = Modifier.size(8.dp))

                FilledTonalButton(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(vertical = 8.dp),
                    onClick = {
                        // configure form check
                        if (name.isEmpty() || email.isEmpty() || password.isEmpty() || confirmPassword.isEmpty()) {
                            // show error message
                            Toast.makeText(context, "Data tidak boleh kosong", Toast.LENGTH_SHORT)
                                .show()
                        } else {
                            if (!emailValidator(email)) {
                                Toast.makeText(
                                    context,
                                    context.getString(R.string.pastikan_penulisan_email_benar),
                                    Toast.LENGTH_SHORT
                                )
                                    .show()
                                return@FilledTonalButton
                            }

                            viewModel.registerUser(
                                name,
                                email,
                                password,
                                confirmPassword,
                            ).observe(lifecycleOwner) {
                                    when (it) {
                                        is Response.Loading -> {
                                            Toast.makeText(
                                                context,
                                                "Loading...",
                                                Toast.LENGTH_SHORT
                                            ).show()
                                        }

                                        is Response.Success -> {
                                            Toast.makeText(
                                                context,
                                                "Berhasil mendaftar",
                                                Toast.LENGTH_SHORT
                                            ).show()
                                            navigator.push(LoginScreen())
                                        }

                                        is Response.Failure -> {
                                            Toast.makeText(
                                                context,
                                                "${it.msg}", Toast.LENGTH_SHORT
                                            ).show()
                                        }

                                        is Response.Empty -> {
                                            Toast.makeText(
                                                context,
                                                "${it.msg}", Toast.LENGTH_SHORT
                                            ).show()
                                        }
                                    }
                                }
                        }
                    },
                    colors = ButtonDefaults.buttonColors(
                        containerColor = Color(0xFFFF8700),
                    )
                ) {
                    Text(
                        "Daftar",
                        style = TextStyle(
                            fontFamily = fontFamily,
                            color = Color.White,
                            fontSize = 18.sp,
                            fontWeight = FontWeight.Bold,
                        )
                    )
                }

                Spacer(modifier = Modifier.size(4.dp))

                Row(
                    modifier = Modifier.padding(bottom = 24.dp),
                    horizontalArrangement = Arrangement.Center,
                    verticalAlignment = Alignment.CenterVertically,
                ) {
                    Text(
                        "Sudah punya akun?",
                        style = TextStyle(
                            fontFamily = fontFamily,
                            color = Color.Gray,
                            fontSize = 16.sp,
                        )
                    )
                    Spacer(modifier = Modifier.size(4.dp))
                    Text(
                        "Masuk",
                        modifier = Modifier.clickable(
                            onClick = {
                                navigator.push(LoginScreen())
                            },
                        ),
                        style = TextStyle(
                            fontFamily = fontFamily,
                            color = Color(0xFFFF8700),
                            fontSize = 16.sp,
                        )
                    )
                }
            }
        }
    }

}