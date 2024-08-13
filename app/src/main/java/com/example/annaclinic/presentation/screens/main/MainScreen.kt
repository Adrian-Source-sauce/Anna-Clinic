package com.example.annaclinic.presentation.screens.main

import androidx.compose.runtime.Composable
import cafe.adriel.voyager.core.screen.Screen
import com.example.annaclinic.core.utils.Const
import com.example.annaclinic.core.utils.SharedPrefUtils
import com.example.annaclinic.presentation.screens.main.admin.AdminScreen
import com.example.annaclinic.presentation.screens.main.user.UserScreen
import org.koin.compose.koinInject


class MainScreen : Screen {
    @Composable
    override fun Content() {
        val prefs = koinInject<SharedPrefUtils>()

        if (prefs.getString(Const.ACCOUNT_TYPE) == "Admin") {
            AdminScreen()
        } else {
            UserScreen()
        }
    }
}
