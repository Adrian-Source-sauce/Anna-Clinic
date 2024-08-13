package com.example.annaclinic.presentation.screens.profile

import android.util.Log
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Scaffold
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.material3.rememberTopAppBarState
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.input.nestedscroll.nestedScroll
import cafe.adriel.voyager.core.screen.Screen
import cafe.adriel.voyager.navigator.LocalNavigator
import cafe.adriel.voyager.navigator.currentOrThrow
import com.example.annaclinic.core.component.CenterTopBar
import com.example.annaclinic.core.utils.Const
import com.example.annaclinic.core.utils.SharedPrefUtils
import com.example.annaclinic.domain.model.User
import com.example.annaclinic.presentation.screens.login.LoginScreen
import com.example.annaclinic.presentation.screens.profile.component.ProfileContent
import org.koin.compose.koinInject

@OptIn(ExperimentalMaterial3Api::class)
data class ProfileScreen(val user: User) : Screen {
    @Composable
    override fun Content() {
        Log.d("ProfileScreen", "Content -> $user")
        val navigator = LocalNavigator.currentOrThrow
        val scrollBehavior = TopAppBarDefaults.pinnedScrollBehavior(rememberTopAppBarState())
        val prefs = koinInject<SharedPrefUtils>()

        Scaffold(
            modifier = Modifier.nestedScroll(scrollBehavior.nestedScrollConnection),
            topBar = { CenterTopBar(navigator = navigator, title = "Profile") }
        ) { paddingValues ->
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(paddingValues)
                    .verticalScroll(rememberScrollState())
            ) {
                ProfileContent(user = user) {
                    prefs.remove(Const.USER_ID)
                    prefs.remove(Const.EMAIL)

                    // navigator to login screen and clear back stack
                    navigator.replaceAll(LoginScreen())
                }
            }
        }
    }
}