package com.example.annaclinic.presentation.screens.main.user.navigation

import android.util.Log
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
        NavigationModelUsers.Home,
        NavigationModelUsers.Profile
    )

    val navBackStackEntry by navController.currentBackStackEntryAsState()
    val currentRoute = navBackStackEntry?.destination?.route

    NavigationBar {
        items.forEachIndexed { index, item ->
            val isSelected = currentRoute?.contains(item.route) == true
            NavigationBarItem(
                icon = {
                    item.selectedIcon?.let { icon ->
                        Icon(
                            imageVector = if (isSelected) icon
                            else item.unselectedIcon ?: icon,
                            contentDescription = null
                        )
                    }
                },
                label = {
                    Text(stringResource(item.title))
                },
                selected = isSelected,
                onClick = {
                    Log.d("BottomNavigationBar", "onClick : $index")
                    selectedItem = index
                    // make sure we do not navigate twice to the same screen
                    if (currentRoute?.contains(item.route) == true) return@NavigationBarItem
                    navController.navigate(item.route)
                }
            )
        }
    }
}