package com.example.annaclinic.presentation.screens.main.component

import android.util.Log
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.AccountCircle
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import cafe.adriel.voyager.navigator.LocalNavigator
import cafe.adriel.voyager.navigator.currentOrThrow
import com.example.annaclinic.core.theme.fontFamily
import com.example.annaclinic.core.utils.Const
import com.example.annaclinic.core.utils.Response
import com.example.annaclinic.core.utils.SharedPrefUtils
import com.example.annaclinic.presentation.screens.main.MainViewModel
import com.example.annaclinic.presentation.screens.profile.ProfileScreen
import com.valentinilk.shimmer.shimmer
import org.koin.compose.koinInject

@Composable
fun Greeting(
    modifier: Modifier = Modifier,
    viewModel: MainViewModel = koinInject(),
    prefs: SharedPrefUtils = koinInject()
) {
    val navigator = LocalNavigator.currentOrThrow
    val user = viewModel.getUserById(prefs.getString(Const.USER_ID)!!)
        .collectAsState(initial = Response.Loading)

    when (user.value) {
        is Response.Loading -> {
            Box(
                modifier = Modifier
                    .size(24.dp)
                    .clip(RoundedCornerShape(16.dp))
                    .shimmer()
                    .background(Color.White)
            )

            Row(
                modifier = modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Row(
                    modifier = modifier
                        .padding(16.dp),
                    horizontalArrangement = Arrangement.Center,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Text(
                        "Halo, ",
                        style = TextStyle(
                            fontSize = 24.sp,
                            fontWeight = FontWeight.Medium,
                            fontFamily = fontFamily
                        )
                    )
                    Box(
                        modifier = Modifier
                            .size(height = 24.dp, width = 100.dp)
                            .clip(RoundedCornerShape(8.dp))
                            .shimmer()
                            .background(Color.LightGray)
                    )
                }

                // add Icon here
                IconButton(
                    modifier = modifier.padding(16.dp),
                    onClick = {

                    }
                ) {
                    Icon(
                        Icons.Rounded.AccountCircle,
                        "Localized Description",
                        modifier = modifier.size(32.dp)
                    )
                }
            }
        }

        is Response.Success -> {
            val users = (user.value as Response.Success).data
            val name = users.name
            Log.d("Greeting", "Name: $name")
            Row(
                modifier = modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Row(
                    modifier = modifier
                        .padding(start = 16.dp, end = 16.dp),
                    horizontalArrangement = Arrangement.Center,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Text(
                        "Halo, ",
                        style = TextStyle(
                            fontSize = 24.sp,
                            fontWeight = FontWeight.Medium,
                            fontFamily = fontFamily
                        )
                    )
                    if (name != null) {
                        Text(
                            name, style = TextStyle(
                                fontSize = 24.sp,
                                fontWeight = FontWeight.Medium,
                                fontFamily = fontFamily
                            )
                        )
                    }
                }

                // add Icon here
                IconButton(
                    modifier = modifier.padding(16.dp),
                    onClick = {
                        navigator.push(
                            ProfileScreen(users)
                        )
                    }
                ) {
                    Icon(
                        Icons.Rounded.AccountCircle,
                        "Localized Description",
                        modifier = modifier.size(32.dp)
                    )
                }
            }
        }

        is Response.Failure -> {
            Text("Error")
        }

        is Response.Empty -> {

        }
    }
}