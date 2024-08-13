package com.example.annaclinic.presentation.screens.main.admin.navigation

import androidx.compose.material3.Icon
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.navigation.NavHostController
import androidx.navigation.compose.currentBackStackEntryAsState

@Composable
fun BottomNavigationBar(
    modifier: Modifier = Modifier,
    navController: NavHostController
) {
    var selectedItem by remember { mutableIntStateOf(0) }
    val items = listOf(
        NavigationModelAdmin.Home,
        NavigationModelAdmin.UploadImage,
        NavigationModelAdmin.ReservationList,
        NavigationModelAdmin.Profile
    )

    val navBackStackEntry by navController.currentBackStackEntryAsState()
    val currentRoute = navBackStackEntry?.destination?.route

    NavigationBar {
        items.forEachIndexed { index, item ->
            NavigationBarItem(
                icon = {
                    item.selectedIcon?.let { icon ->
                        Icon(
//                            imageVector = if (selectedItem == index) icon
//                            else item.unselectedIcon ?: icon,
                            imageVector = if (currentRoute?.contains(item.route) == true) icon
                            else item.unselectedIcon ?: icon,
                            contentDescription = null
                        )
                    }
                },
                label = {
                    Text(stringResource(item.title))
                },
                selected = currentRoute?.contains(item.route) == true,
                onClick = {
                    selectedItem = index
                    // make sure we do not navigate twice to the same screen
                    if (currentRoute?.contains(item.route) == true) return@NavigationBarItem
                    navController.navigate(item.route)
                }
            )
        }
    }
}