package com.example.annaclinic.presentation

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.core.splashscreen.SplashScreen.Companion.installSplashScreen
import cafe.adriel.voyager.navigator.Navigator
import com.example.annaclinic.core.theme.AnnaClinicTheme
import com.example.annaclinic.core.utils.SharedPrefUtils
import com.example.annaclinic.presentation.screens.login.LoginScreen
import com.example.annaclinic.presentation.screens.main.MainScreen
import com.example.annaclinic.presentation.screens.profile.ProfileScreen
import org.koin.android.ext.android.inject

class MainActivity : ComponentActivity() {
    private val sharedPrefUtils: SharedPrefUtils by inject()

    override fun onCreate(savedInstanceState: Bundle?) {
        installSplashScreen()
        enableEdgeToEdge()
        super.onCreate(savedInstanceState)
        setContent {
            AnnaClinicTheme {
                if (sharedPrefUtils.getString("email").isNullOrEmpty()) {
                    Navigator(LoginScreen(), onBackPressed = null)
                } else {
                    Navigator(MainScreen())
                }
            }
        }

//        splashScreen.setKeepOnScreenCondition { true }

    }
}