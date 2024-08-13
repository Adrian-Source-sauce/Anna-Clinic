package com.example.annaclinic.presentation.screens.main.admin.navigation

import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.CalendarMonth
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.Image
import androidx.compose.material.icons.filled.Person
import androidx.compose.material.icons.outlined.CalendarMonth
import androidx.compose.material.icons.outlined.Home
import androidx.compose.material.icons.outlined.Image
import androidx.compose.material.icons.outlined.Person
import androidx.compose.ui.graphics.vector.ImageVector
import com.example.annaclinic.R

sealed class NavigationModelAdmin(
    val route: String,
    val selectedIcon: ImageVector? = null,
    val unselectedIcon: ImageVector? = null,
    val title: Int,
    val appBarTitle: Int? = null
) {
    data object Home : NavigationModelAdmin(
        route = "home",
        selectedIcon = Icons.Filled.Home,
        unselectedIcon = Icons.Outlined.Home,
        title = R.string.home,
    )

    data object UploadImage : NavigationModelAdmin(
        route = "upload_image",
        selectedIcon = Icons.Filled.Image,
        unselectedIcon = Icons.Outlined.Image,
        title = R.string.upload_image,
    )

    data object ReservationList : NavigationModelAdmin(
        route = "reservationList",
        selectedIcon = Icons.Filled.CalendarMonth,
        unselectedIcon = Icons.Outlined.CalendarMonth,
        title = R.string.reservation_list,
    )

    data object Profile : NavigationModelAdmin(
        route = "profile",
        selectedIcon = Icons.Filled.Person,
        unselectedIcon = Icons.Outlined.Person,
        title = R.string.profile
    )
}