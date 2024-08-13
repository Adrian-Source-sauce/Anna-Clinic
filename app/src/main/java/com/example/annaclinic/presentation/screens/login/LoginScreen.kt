package com.example.annaclinic.presentation.screens.login

import android.util.Log
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
import com.example.annaclinic.core.utils.Const
import com.example.annaclinic.core.utils.Response
import com.example.annaclinic.core.utils.SharedPrefUtils
import com.example.annaclinic.presentation.screens.main.MainScreen
import com.example.annaclinic.presentation.screens.register.RegisterScreen
import org.koin.androidx.compose.koinViewModel
import org.koin.compose.koinInject

@OptIn(ExperimentalMaterial3Api::class)
class LoginScreen : Screen {
    @Composable
    override fun Content() {
        val viewModel: LoginViewModel = koinViewModel()
        val navigator = LocalNavigator.currentOrThrow
        val context = LocalContext.current
        val lifecycleOwner = LocalLifecycleOwner.current
        val keyboardController = LocalSoftwareKeyboardController.current
        val pref = koinInject<SharedPrefUtils>()
        var email by remember { mutableStateOf("") }
        var password by remember { mutableStateOf("") }
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
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center,
        ) {
            Column(
                modifier = Modifier.padding(top = 12.dp),
                horizontalAlignment = Alignment.CenterHorizontally,
            ) {
                Image(
                    painterResource(id = R.drawable.illustration_login),
                    modifier = Modifier.size(280.dp),
                    contentDescription = "Illustration Login",
                )
                Text(
                    text = "Login",
                    style = TextStyle(
                        fontSize = 24.sp,
                        fontWeight = FontWeight.Bold,
                        fontFamily = fontFamily,
                    ),
                )
                Spacer(modifier = Modifier.size(8.dp))
                Text(
                    modifier = Modifier.padding(horizontal = 16.dp),
                    text = "Masukan Email dan Password anda yang sudah di daftarkan sebelumnya",
                    textAlign = TextAlign.Center,
                    softWrap = true,
                    style = TextStyle(
                        fontSize = 16.sp,
                        fontFamily = fontFamily,
                        color = Color.Gray,
                    ),
                )
            }
            Column(
                modifier = Modifier.padding(top = 32.dp, start = 16.dp, end = 16.dp),
                verticalArrangement = Arrangement.Center,
                horizontalAlignment = Alignment.CenterHorizontally,
            ) {
                DefaultTextField(label = "Email", value = email) { email = it }
                Spacer(modifier = Modifier.size(4.dp))

                PasswordTextField(label = "Password", value = password) { password = it }
                Spacer(modifier = Modifier.size(4.dp))

                DropDownTextField(label = "Tipe Akun", value = accountType) { accountType = it }
                Spacer(modifier = Modifier.size(8.dp))

                FilledTonalButton(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(vertical = 8.dp),
                    onClick = {
                        // check if email and password is not empty
                        if (email.isNotEmpty() && password.isNotEmpty() && accountType.isNotEmpty()) {
                            if (!emailValidator(email)) {
                                Toast.makeText(
                                    context,
                                    context.getString(R.string.pastikan_penulisan_email_benar),
                                    Toast.LENGTH_SHORT
                                ).show()
                            } else {
                                viewModel.loginUser(email, password, accountType)
                                    .observe(lifecycleOwner) { result ->
                                        when (result) {
                                            is Response.Loading -> {
                                                // show loading
                                                Toast.makeText(
                                                    context,
                                                    "Loading...",
                                                    Toast.LENGTH_SHORT
                                                ).show()
                                            }

                                            is Response.Success -> {
                                                // show success message
                                                Toast.makeText(
                                                    context,
                                                    "Login berhasil!",
                                                    Toast.LENGTH_SHORT
                                                ).show()
                                                // move to home screen
                                                Log.d("Login", "Login berhasil!")
                                                pref.saveString(Const.EMAIL, email)
                                                pref.saveString(Const.PASSWORD, password)
                                                pref.saveString(Const.ACCOUNT_TYPE, accountType)
                                                navigator.push(MainScreen())
                                            }

                                            is Response.Failure -> {
                                                // show error message
                                                Toast.makeText(
                                                    context,
                                                    result.msg,
                                                    Toast.LENGTH_SHORT
                                                ).show()
                                                Log.e("Login", "msg : ${result.msg}")
                                            }

                                            is Response.Empty -> {}
                                        }
                                    }
                            }
                        } else {
                            // show error message
                            Toast.makeText(
                                context,
                                "Pastikan semua kolom terisi!",
                                Toast.LENGTH_SHORT
                            ).show()
                        }
                    },
                    colors = ButtonDefaults.buttonColors(
                        containerColor = Color(0xFFFF8700),
                    )
                ) {
                    Text(
                        "Masuk",
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
                    modifier = Modifier.padding(bottom = 46.dp),
                    horizontalArrangement = Arrangement.Center,
                    verticalAlignment = Alignment.CenterVertically,
                ) {
                    Text(
                        "Belum punya akun?",
                        style = TextStyle(
                            fontFamily = fontFamily,
                            color = Color.Gray,
                            fontSize = 16.sp,
                        )
                    )
                    Spacer(modifier = Modifier.size(4.dp))
                    Text(
                        "Daftar",
                        modifier = Modifier.clickable(
                            onClick = {
                                navigator.push(RegisterScreen())
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