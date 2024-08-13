//package com.example.annaclinic.core.component
//
//
//import android.app.TimePickerDialog
//import android.icu.text.SimpleDateFormat
//import android.os.Build
//import androidx.annotation.RequiresApi
//import androidx.compose.foundation.layout.Box
//import androidx.compose.material.icons.Icons
//import androidx.compose.material.icons.outlined.Keyboard
//import androidx.compose.material.icons.outlined.Schedule
//import androidx.compose.material3.Button
//import androidx.compose.material3.ExperimentalMaterial3Api
//import androidx.compose.material3.Icon
//import androidx.compose.material3.IconButton
//import androidx.compose.material3.SnackbarHost
//import androidx.compose.material3.SnackbarHostState
//import androidx.compose.material3.Text
//import androidx.compose.material3.TimeInput
//import androidx.compose.material3.TimePicker
//import androidx.compose.material3.rememberTimePickerState
//import androidx.compose.runtime.Composable
//import androidx.compose.runtime.getValue
//import androidx.compose.runtime.mutableStateOf
//import androidx.compose.runtime.remember
//import androidx.compose.runtime.rememberCoroutineScope
//import androidx.compose.runtime.setValue
//import androidx.compose.ui.Alignment
//import androidx.compose.ui.Modifier
//import androidx.compose.ui.platform.LocalConfiguration
//import kotlinx.coroutines.launch
//import java.util.Calendar
//import java.util.Locale
//
//@RequiresApi(Build.VERSION_CODES.N)
//@OptIn(ExperimentalMaterial3Api::class)
//@Composable
//fun PickerTime(modifier: Modifier = Modifier) {
//    var showTimePicker by remember { mutableStateOf(false) }
//    val state = rememberTimePickerState()
//    val formatter = remember { SimpleDateFormat("hh:mm a", Locale.getDefault()) }
//    val snackState = remember { SnackbarHostState() }
//    val showingPicker = remember { mutableStateOf(true) }
//    val snackScope = rememberCoroutineScope()
//    val configuration = LocalConfiguration.current
//
//    Box(propagateMinConstraints = false) {
//        Button(
//            modifier = Modifier.align(Alignment.Center),
//            onClick = { showTimePicker = true }
//        ) {
//            Text("Set Time")
//        }
//        SnackbarHost(hostState = snackState)
//    }
//
//    if (showTimePicker) {
//        TimePickerDialog(
//            title = if (showingPicker.value) {
//                "Select Time "
//            } else {
//                "Enter Time"
//            },
//            onCancel = { showTimePicker = false },
//            onConfirm = {
//                val cal = Calendar.getInstance()
//                cal.set(Calendar.HOUR_OF_DAY, state.hour)
//                cal.set(Calendar.MINUTE, state.minute)
//                cal.isLenient = false
//                snackScope.launch {
//                    snackState.showSnackbar("Entered time: ${formatter.format(cal.time)}")
//                }
//                showTimePicker = false
//            },
//            toggle = {
//                if (configuration.screenHeightDp > 400) {
//                    IconButton(onClick = { showingPicker.value = !showingPicker.value }) {
//                        val icon = if (showingPicker.value) {
//                            Icons.Outlined.Keyboard
//                        } else {
//                            Icons.Outlined.Schedule
//                        }
//                        Icon(
//                            icon,
//                            contentDescription = if (showingPicker.value) {
//                                "Switch to Text Input"
//                            } else {
//                                "Switch to Touch Input"
//                            }
//                        )
//                    }
//                }
//            }
//        ) {
//            if (showingPicker.value && configuration.screenHeightDp > 400) {
//                TimePicker(state = state)
//            } else {
//                TimeInput(state = state)
//            }
//        }
//    }
//}