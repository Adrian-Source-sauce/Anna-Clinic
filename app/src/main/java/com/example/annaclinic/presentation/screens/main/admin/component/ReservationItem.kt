package com.example.annaclinic.presentation.screens.main.admin.component

import android.Manifest
import android.Manifest.permission.READ_EXTERNAL_STORAGE
import android.Manifest.permission.READ_MEDIA_IMAGES
import android.Manifest.permission.READ_MEDIA_VIDEO
import android.Manifest.permission.READ_MEDIA_VISUAL_USER_SELECTED
import android.os.Build
import android.util.Log
import android.widget.Toast
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.safeDrawingPadding
import androidx.compose.foundation.layout.size
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material.icons.filled.Info
import androidx.compose.material.icons.filled.Notifications
import androidx.compose.material.icons.rounded.Delete
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.ModalBottomSheet
import androidx.compose.material3.OutlinedCard
import androidx.compose.material3.Text
import androidx.compose.material3.rememberModalBottomSheetState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.compose.LocalLifecycleOwner
import com.example.annaclinic.core.component.AnnaDialog
import com.example.annaclinic.core.component.ReservationDetail
import com.example.annaclinic.core.theme.fontFamily
import com.example.annaclinic.core.utils.Response
import com.example.annaclinic.domain.model.Reservation
import com.example.annaclinic.presentation.screens.main.MainViewModel
import com.google.accompanist.permissions.ExperimentalPermissionsApi
import com.google.accompanist.permissions.isGranted
import com.google.accompanist.permissions.rememberMultiplePermissionsState
import com.google.accompanist.permissions.shouldShowRationale
import kotlinx.coroutines.launch
import org.koin.compose.koinInject

@OptIn(ExperimentalMaterial3Api::class, ExperimentalPermissionsApi::class)
@Composable
fun ReservationItem(
    modifier: Modifier = Modifier,
    reservation: Reservation,
    viewModel: MainViewModel = koinInject(),
) {
    val lifecycleOwner = LocalLifecycleOwner.current
    val context = LocalContext.current
    val scope = rememberCoroutineScope()
    val sheetStateNotification = rememberModalBottomSheetState(skipPartiallyExpanded = true)
    var showBottomSheetNotification by remember { mutableStateOf(false) }
    val sheetStateDetail = rememberModalBottomSheetState(skipPartiallyExpanded = true)
    var showBottomSheetDetail by remember { mutableStateOf(false) }
    val openAlertDialog = remember { mutableStateOf(false) }
    val storagePermissionState = rememberMultiplePermissionsState(
        permissions = listOf(
            READ_EXTERNAL_STORAGE,
            READ_MEDIA_IMAGES,
            READ_MEDIA_VIDEO,
            READ_MEDIA_VISUAL_USER_SELECTED,

        )
    )

    LaunchedEffect(storagePermissionState) {
        if (!storagePermissionState.allPermissionsGranted && storagePermissionState.shouldShowRationale) {
            // Show rationale if needed
        } else {
            storagePermissionState.launchMultiplePermissionRequest()
        }
    }

    Column {
        OutlinedCard {
            Row(
                modifier = modifier
                    .fillMaxWidth()
                    .padding(16.dp),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Column {
                    Text(
                        text = reservation.name,
                        style = TextStyle(
                            fontSize = 16.sp,
                            fontFamily = fontFamily
                        )
                    )

                    Spacer(modifier = modifier.size(4.dp))

                    Text(
                        text = reservation.email,
                        style = TextStyle(fontFamily = fontFamily)
                    )
                }

                Row(
                    modifier = modifier.fillMaxHeight(),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    IconButton(onClick = {
                        showBottomSheetNotification = true
                        scope.launch { sheetStateNotification.expand() }
                    }) {
                        Icon(
                            imageVector = Icons.Default.Notifications,
                            contentDescription = null,
                            tint = MaterialTheme.colorScheme.primary
                        )
                    }

                    IconButton(onClick = {
                        showBottomSheetDetail = true
                        scope.launch { sheetStateDetail.expand() }
                    }) {
                        Icon(
                            imageVector = Icons.Default.Info,
                            contentDescription = null,
                            tint = MaterialTheme.colorScheme.primary
                        )
                    }

                    IconButton(
                        onClick = { openAlertDialog.value = true }
                    ) {
                        Icon(
                            imageVector = Icons.Default.Delete,
                            contentDescription = null,
                            tint = Color.Red
                        )
                    }
                }
            }
        }

        when {
            showBottomSheetNotification -> {
                ModalBottomSheet(
                    onDismissRequest = {
                        showBottomSheetNotification = false
                    },
                    sheetState = sheetStateNotification,
                ) {
                    // Sheet content
                    SendNotification(reservation = reservation)
                }
            }

            showBottomSheetDetail -> {
                ModalBottomSheet(
                    modifier = Modifier.safeDrawingPadding(),
                    onDismissRequest = {
                        showBottomSheetDetail = false
                    },
                    sheetState = sheetStateDetail,
                ) {
                    // Sheet content
                    ReservationDetail(reservation = reservation)
                }
            }
        }

        if (openAlertDialog.value) {
            AnnaDialog(
                onDismissRequest = { openAlertDialog.value = false },
                onConfirmation = {
                    viewModel.deleteReservation(reservation.id)
                        .observe(lifecycleOwner) { reservation ->
                            when (reservation) {
                                Response.Loading -> {
                                    Log.d("ReservationDelLoading", "Loading")
                                }

                                is Response.Empty -> {
                                    Log.d("ReservationDelEmpty", "Empty")
                                }

                                is Response.Success -> {
                                    Log.d("ReservationDelSuccess", "Success")
                                }

                                is Response.Failure -> {
                                    Log.d("ReservationDelFailure", "Failure")
                                }
                            }
                        }
                    openAlertDialog.value = false
                },
                dialogTitle = "Hapus Reservasi",
                dialogText = "Apakah anda yakin ingin menghapus reservasi ini ? Tindakan ini tidak dapat diurungkan.",
                icon = Icons.Rounded.Delete
            )
        }
    }
}

