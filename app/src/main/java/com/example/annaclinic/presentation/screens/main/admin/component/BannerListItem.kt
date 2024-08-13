package com.example.annaclinic.presentation.screens.main.admin.component

import android.util.Log
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material.icons.rounded.Delete
import androidx.compose.material3.Card
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedCard
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.LocalLifecycleOwner
import coil.compose.AsyncImage
import com.example.annaclinic.core.component.AnnaDialog
import com.example.annaclinic.core.utils.Response
import com.example.annaclinic.core.utils.decodeBase64
import com.example.annaclinic.domain.model.Banner
import com.example.annaclinic.presentation.screens.main.MainViewModel
import org.koin.compose.koinInject

@Composable
fun BannerListItem(
    modifier: Modifier = Modifier,
    banner: Banner,
    viewModel: MainViewModel = koinInject(),
) {
    val lifecycleOwner = LocalLifecycleOwner.current
    val openAlertDialog = remember { mutableStateOf(false) }

    OutlinedCard(
        modifier = modifier.fillMaxWidth(),
    ) {
        Row(
            modifier = modifier.fillMaxWidth(),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            Box(
                modifier = modifier
                    .size(100.dp)
                    .padding(12.dp)
            ) {
                Card(
                    shape = RoundedCornerShape(16.dp)
                ) {
                    val base64 = decodeBase64(banner.imageBase64)
                    AsyncImage(
                        model = base64,
                        contentDescription = null,
                        contentScale = ContentScale.Crop
                    )
                }
            }

            IconButton(
                modifier = modifier.padding(12.dp),
                onClick = { openAlertDialog.value = true }) {
                Icon(
                    Icons.Default.Delete,
                    contentDescription = null,
                    tint = Color.Red
                )
            }
        }
    }

    if (openAlertDialog.value) {
        AnnaDialog(
            onDismissRequest = { openAlertDialog.value = false },
            onConfirmation = {
                viewModel.deleteImage(banner.id).observe(lifecycleOwner) { response ->
                    when (response) {
                        Response.Loading -> {
                            Log.d("BannerListAdminDelete", "Loading")
                        }

                        is Response.Empty -> {
                            Log.d("BannerListAdminDelete", "Empty")
                        }

                        is Response.Success -> {
                            Log.d("BannerListAdminDelete", "Success")
                        }

                        is Response.Failure -> {
                            Log.d("BannerListAdminDelete", "Failure")
                        }
                    }
                }

                openAlertDialog.value = false
            },
            dialogTitle = "Hapus Banner",
            dialogText = "Apakah anda yakin ingin menghapus banner ini ? Tindakan ini tidak dapat diurungkan.",
            icon = Icons.Rounded.Delete
        )
    }
}