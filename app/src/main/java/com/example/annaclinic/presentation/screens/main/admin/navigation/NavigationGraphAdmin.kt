package com.example.annaclinic.presentation.screens.main.admin.navigation

import android.app.Activity
import androidx.activity.compose.BackHandler
import androidx.compose.runtime.Composable
import androidx.compose.ui.platform.LocalContext
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import com.example.annaclinic.presentation.screens.main.admin.screen.HomeScreen
import com.example.annaclinic.presentation.screens.main.admin.screen.ProfileScreen
import com.example.annaclinic.presentation.screens.main.admin.screen.ReservationListScreen
import com.example.annaclinic.presentation.screens.main.admin.screen.UploadImageScreen

@Composable
fun NavigationGraph(navController: NavHostController) {
    val context = LocalContext.current

    NavHost(navController = navController, startDestination = NavigationModelAdmin.Home.route) {
        composable(NavigationModelAdmin.Home.route) {
            HomeScreen()
            BackHandler {
                (context as? Activity)?.finish()
            }
        }
        composable(NavigationModelAdmin.UploadImage.route) {
            UploadImageScreen()
            BackHandler {
                navController.popBackStack()
            }
        }
        composable(NavigationModelAdmin.ReservationList.route) {
            ReservationListScreen()
            BackHandler {
                navController.popBackStack()
            }
        }
        composable(NavigationModelAdmin.Profile.route) {
            ProfileScreen()

            BackHandler {
                navController.popBackStack()
            }
        }
    }
}