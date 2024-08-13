package com.example.annaclinic.presentation.screens.main.user.screen

import android.util.Log
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.ui.Modifier
import cafe.adriel.voyager.navigator.LocalNavigator
import cafe.adriel.voyager.navigator.currentOrThrow
import com.example.annaclinic.R
import com.example.annaclinic.core.component.CenterAppBar
import com.example.annaclinic.core.component.CircularLoading
import com.example.annaclinic.core.utils.Const
import com.example.annaclinic.core.utils.Response
import com.example.annaclinic.core.utils.SharedPrefUtils
import com.example.annaclinic.presentation.screens.login.LoginScreen
import com.example.annaclinic.presentation.screens.main.MainViewModel
import com.example.annaclinic.presentation.screens.profile.component.ProfileContent
import org.koin.compose.koinInject

@Composable
fun ProfileScreen(
    modifier: Modifier = Modifier,
    viewModel: MainViewModel = koinInject(),
    prefs: SharedPrefUtils = koinInject()
) {
    val navigator = LocalNavigator.currentOrThrow
    val user = viewModel.getUserById(prefs.getString(Const.USER_ID)!!)
        .collectAsState(initial = Response.Loading)
    Scaffold(
        topBar = { CenterAppBar(R.string.profile) }
    ) {
        Column(
            modifier = modifier
                .padding(it)
                .fillMaxSize()
                .background(androidx.compose.ui.graphics.Color.White),
        ) {
            when (user.value) {
                is Response.Loading -> {
                    CircularLoading(isLoading = true)
                }

                is Response.Success -> {
                    val users = (user.value as Response.Success).data
                    val name = users.name
                    CircularLoading(isLoading = false)
                    Log.d("Greeting", "Name: $name")
                    ProfileContent(user = users) {
                        prefs.remove(Const.USER_ID)
                        prefs.remove(Const.EMAIL)

                        // navigator to login screen and clear back stack
                        navigator.replaceAll(LoginScreen())
                    }
                }

                is Response.Failure -> {
                    CircularLoading(isLoading = false)
                    Text("Error")
                }

                is Response.Empty -> {
                    CircularLoading(isLoading = false)
                }
            }
        }
    }


}