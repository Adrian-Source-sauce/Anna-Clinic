package com.example.annaclinic.core.component

import androidx.annotation.StringRes
import androidx.compose.foundation.layout.RowScope
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material3.CenterAlignedTopAppBar
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.unit.sp
import cafe.adriel.voyager.navigator.Navigator
import com.example.annaclinic.core.theme.fontFamily

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun CenterAppBar(
    @StringRes titleRes: Int,
    modifier: Modifier = Modifier,
    navigator: Navigator? = null,
    actions: @Composable (RowScope.() -> Unit)? = null,
) {
    CenterAlignedTopAppBar(
        title = { Text(text = stringResource(id = titleRes), style = TextStyle(fontFamily = fontFamily, fontSize = 24.sp)) },
        colors = TopAppBarDefaults.centerAlignedTopAppBarColors(
            containerColor = MaterialTheme.colorScheme.background,
        ),
        modifier = modifier.testTag("annaClinicTopAppBar"),
        actions = actions ?: {},
        navigationIcon = {
            if (navigator != null) {
                IconButton(onClick = {
                    navigator.pop()
                }) {
                    Icon(
                        imageVector = Icons.Filled.ArrowBack,
                        contentDescription = "Localized description"
                    )
                }
            }
        },
    )
}