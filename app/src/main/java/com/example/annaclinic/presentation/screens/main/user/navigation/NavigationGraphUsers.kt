package com.example.annaclinic.presentation.screens.main.user.navigation

import android.app.Activity
import androidx.activity.compose.BackHandler
import androidx.compose.animation.AnimatedContentTransitionScope
import androidx.compose.animation.core.tween
import androidx.compose.runtime.Composable
import androidx.compose.ui.platform.LocalContext
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import com.example.annaclinic.presentation.screens.main.user.screen.HomeScreen
import com.example.annaclinic.presentation.screens.main.user.screen.ProfileScreen

@Composable
fun NavigationGraph(navController: NavHostController) {
    val context = LocalContext.current
    NavHost(navController = navController, startDestination = NavigationModelUsers.Home.route) {
        composable(
            NavigationModelUsers.Home.route,
            enterTransition = {
                slideIntoContainer(
                    AnimatedContentTransitionScope.SlideDirection.Right,
                    animationSpec = tween(500)
                )
            },
            exitTransition = {
                slideOutOfContainer(
                    AnimatedContentTransitionScope.SlideDirection.Left,
                    animationSpec = tween(500)
                )
            },
        ) {
            HomeScreen()

            BackHandler {
                 (context as? Activity)?.finish()
            }
        }
        composable(
            NavigationModelUsers.Profile.route,
            enterTransition = {
                slideIntoContainer(
                    AnimatedContentTransitionScope.SlideDirection.Left,
                    animationSpec = tween(500)
                )
            },
            exitTransition = {
                slideOutOfContainer(
                    AnimatedContentTransitionScope.SlideDirection.Right,
                    animationSpec = tween(500)
                )
            },
        ) {
            ProfileScreen()
        }
    }
}