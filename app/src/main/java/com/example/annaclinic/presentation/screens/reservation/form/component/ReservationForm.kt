package com.example.annaclinic.presentation.screens.reservation.form.component

import android.os.Build
import android.widget.Toast
import androidx.annotation.RequiresApi
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.safeDrawing
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.windowInsetsPadding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.DateRange
import androidx.compose.material.icons.rounded.Warning
import androidx.compose.material3.Button
import androidx.compose.material3.DatePicker
import androidx.compose.material3.DatePickerDialog
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.OutlinedTextFieldDefaults
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.material3.rememberDatePickerState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.derivedStateOf
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableLongStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.compose.LocalLifecycleOwner
import cafe.adriel.voyager.navigator.Navigator
import com.example.annaclinic.core.component.AnnaDialog
import com.example.annaclinic.core.component.DefaultTextField
import com.example.annaclinic.core.theme.fontFamily
import com.example.annaclinic.core.utils.Const
import com.example.annaclinic.core.utils.DateUtils
import com.example.annaclinic.core.utils.Response
import com.example.annaclinic.core.utils.SharedPrefUtils
import com.example.annaclinic.domain.model.Products
import com.example.annaclinic.domain.model.Reservation
import com.example.annaclinic.presentation.screens.reservation.detail.ReservationDetailScreen
import com.example.annaclinic.presentation.screens.reservation.form.ReservationFormViewModel
import kotlinx.coroutines.launch
import org.koin.compose.koinInject
import java.text.DecimalFormat
import java.text.NumberFormat

@RequiresApi(Build.VERSION_CODES.O)
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ReservationForm(
    modifier: Modifier = Modifier,
    serviceName: String,
    servicePrice: Long,
    addOnProducts: List<Products>? = null,
    navigator: Navigator,
    viewModel: ReservationFormViewModel = koinInject(),
    prefs: SharedPrefUtils = koinInject()
) {
    val lifecycleOwner = LocalLifecycleOwner.current
    val context = LocalContext.current

    // variable for form
    val price by remember { mutableLongStateOf(servicePrice) }
    var name by remember { mutableStateOf("") }
    var email by remember { mutableStateOf(prefs.getString(Const.EMAIL)) }
    var phone by remember { mutableStateOf("") }
    var nameOfService by remember { mutableStateOf(serviceName) }
    val queue by remember { mutableIntStateOf(0) }
    var description by remember { mutableStateOf("") }
    var date by remember { mutableStateOf("") }
    val selectedProducts = addOnProducts?.joinToString { it.name }
    val totalOfProductsPrice = addOnProducts?.sumOf { it.price }

    val showFormDialog = remember { mutableStateOf(false) }
    val showDatePicker = remember { mutableStateOf(false) }
    val showReservationDialog = remember { mutableStateOf(false) }

    val snackState = remember { SnackbarHostState() }
    val snackScope = rememberCoroutineScope()
    SnackbarHost(hostState = snackState, Modifier)

    fun rupiahFormat(price: Long?): String? {
        val formatter: NumberFormat = DecimalFormat("#,###")
        return if (price != null) {
            "Rp. ${formatter.format(price)}"
        } else {
            null
        }
    }

    fun postTheReservation() {
        val reservation = Reservation(
            name = name,
            email = email!!,
            phone = phone,
            service = nameOfService,
            price = rupiahFormat(price)!!,
            queue = queue,
            description = description,
            date = date,
            product = selectedProducts,
            totalProductPrice = rupiahFormat(totalOfProductsPrice),
            totalPrice = rupiahFormat(
                totalOfProductsPrice?.let { it + price } ?: price
            )!!

        )
        viewModel.postTheReservation(reservation).observe(lifecycleOwner) {
            when (it) {
                is Response.Loading -> {
                    // Handle loading
                    Toast.makeText(
                        context,
                        "Loading",
                        Toast.LENGTH_SHORT
                    ).show()
                }

                is Response.Success -> {
                    // Handle success
                    Toast.makeText(
                        context,
                        "Reservasi Berhasil",
                        Toast.LENGTH_SHORT
                    ).show()
                    navigator.replaceAll(
                        ReservationDetailScreen(reservation = reservation)
                    )
                }

                is Response.Failure -> {
                    showReservationDialog.value = true
                }

                is Response.Empty -> {
                    // Handle empty
                }
            }
        }
    }

    fun validationForm(): Boolean {
        return name.isNotEmpty() && phone.isNotEmpty() && description.isNotEmpty()
    }

    Column(
        modifier = modifier.windowInsetsPadding(WindowInsets.safeDrawing)
    ) {
        /**
         * Name
         */
        Text(text = "Nama", modifier = Modifier.padding(start = 16.dp))
        Spacer(modifier = Modifier.size(8.dp))
        ReservationTextField(
            placeHolder = "Masukan Nama",
            value = name,
            keyboardType = KeyboardType.Text
        ) {
            name = it
        }

        /**
         * Phone
         */
        Text(text = "No. telp", modifier = Modifier.padding(start = 16.dp, top = 16.dp))
        Spacer(modifier = Modifier.size(8.dp))
        ReservationTextField(
            placeHolder = "Masukan No. telp",
            value = phone,
            keyboardType = KeyboardType.Number
        ) {
            phone = it
        }

        /**
         * Date
         */
        Text(text = "Tanggal", modifier = Modifier.padding(start = 16.dp, top = 16.dp))
        Spacer(modifier = Modifier.size(8.dp))
        OutlinedTextField(
            modifier = modifier
                .padding(start = 16.dp, end = 16.dp)
                .fillMaxWidth(),
            value = date,
            onValueChange = {
                date = it
            },
            placeholder = {
                Text(text = "Pilih Tanggal")
            },
            enabled = false,
            shape = RoundedCornerShape(16.dp),
            trailingIcon = {
                IconButton(
                    onClick = {
                        showDatePicker.value = true
                    }
                ) {
                    Icon(Icons.Rounded.DateRange, contentDescription = "Date Picker")
                }
            },
            colors = OutlinedTextFieldDefaults.colors(
                disabledBorderColor = MaterialTheme.colorScheme.outline,
                disabledTrailingIconColor = MaterialTheme.colorScheme.onSurfaceVariant,
                disabledTextColor = MaterialTheme.colorScheme.onSurfaceVariant,
                disabledPlaceholderColor = MaterialTheme.colorScheme.onSurfaceVariant
            )
        )

        /**
         * Email
         */
        Text(text = "Email", modifier = Modifier.padding(start = 16.dp, top = 16.dp))
        Spacer(modifier = Modifier.size(8.dp))
        ReservationTextField(
            placeHolder = "Masukan Email",
            value = email,
            keyboardType = KeyboardType.Email,
            isDisabled = true
        ) {
            email = it
        }

        /**
         * Service
         */
        Text(text = "Layanan", modifier = Modifier.padding(start = 16.dp, top = 16.dp))
        Spacer(modifier = Modifier.size(8.dp))
        ReservationTextField(
            placeHolder = "",
            value = nameOfService,
            keyboardType = KeyboardType.Text,
            isDisabled = true
        ) {
            nameOfService = it
        }

        /**
         * Price
         */
        Text(text = "Harga Layanan", modifier = Modifier.padding(start = 16.dp, top = 16.dp))
        Spacer(modifier = Modifier.size(8.dp))
        ReservationTextField(
            placeHolder = "",
            value = rupiahFormat(price),
            keyboardType = KeyboardType.Text,
            isDisabled = true
        ) {}

        if (addOnProducts != null) {

            /**
             * Products
             */
            Text(text = "Produk", modifier = Modifier.padding(start = 16.dp, top = 16.dp))
            if (selectedProducts != null) {
                Box(
                    modifier = modifier.padding(start = 16.dp, end = 16.dp)
                ) {
                    DefaultTextField(
                        label = "",
                        value = selectedProducts,
                        isDisabled = true,
                        dynamicLines = true,
                        isSingleLine = false
                    ) {}
                }
            }

            /**
             * Price of Products
             */
            Text(text = "Harga Produk", modifier = Modifier.padding(start = 16.dp, top = 16.dp))
            Spacer(modifier = Modifier.size(8.dp))
            ReservationTextField(
                placeHolder = "",
                value = "${totalOfProductsPrice?.let { rupiahFormat(it) }}",
                keyboardType = KeyboardType.Text,
                isDisabled = true
            ) {}
        } else {
            Box { }
        }

        /**
         * Total Price
         */
        Text(text = "Total Harga", modifier = Modifier.padding(start = 16.dp, top = 16.dp))
        Spacer(modifier = Modifier.size(8.dp))
        ReservationTextField(
            placeHolder = "",
            value = rupiahFormat(totalOfProductsPrice?.let { it + price } ?: price),
            keyboardType = KeyboardType.Text,
            isDisabled = true
        ) { }

        /**
         * Description
         **/
        Text(text = "Deskripsi", modifier = Modifier.padding(start = 16.dp, top = 16.dp))
        Spacer(modifier = Modifier.size(8.dp))
        ReservationTextField(
            placeHolder = "Masukan Deskripsi",
            value = description,
            isSingleLine = false,
            keyboardType = KeyboardType.Text,
            lines = 8
        ) {
            description = it
        }

        Button(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp),
            onClick = {
                if (validationForm()) {
                    postTheReservation()
                } else {
                    showFormDialog.value = true
                }
            }
        ) {
            Text(
                text = "Pesan",
                style = TextStyle(
                    fontFamily = fontFamily,
                    fontSize = 16.sp,
                    fontWeight = FontWeight.Medium,
                )
            )
        }
    }

    when {
        showFormDialog.value -> {
            AnnaDialog(
                onDismissRequest = { showFormDialog.value = false },
                onConfirmation = { showFormDialog.value = false },
                dialogTitle = "Peringatan",
                dialogText = "Pastikan semua form terisi dengan benar!",
                icon = Icons.Rounded.Warning
            )
        }

        showDatePicker.value -> {
            val datePickerState = rememberDatePickerState()
            val confirmEnabled = remember {
                derivedStateOf { datePickerState.selectedDateMillis != null }
            }
            val millisToLocalDate = datePickerState.selectedDateMillis?.let {
                DateUtils().convertMillisToLocalDate(it)
            }
            val dateToString = millisToLocalDate?.let {
                DateUtils().dateToString(millisToLocalDate)
            } ?: "Choose Date"

            DatePickerDialog(
                onDismissRequest = {
                    showDatePicker.value = false
                },
                confirmButton = {
                    TextButton(
                        onClick = {
                            showDatePicker.value = false
                            snackScope.launch {
                                date = dateToString
                            }
                        },
                        enabled = confirmEnabled.value
                    ) {
                        Text("OK")
                    }
                },
                dismissButton = {
                    TextButton(
                        onClick = {
                            showDatePicker.value = false
                        }
                    ) {
                        Text("Cancel")
                    }
                }
            ) {
                DatePicker(state = datePickerState)
            }
        }

        showReservationDialog.value -> {
            AnnaDialog(
                onDismissRequest = { showReservationDialog.value = false },
                onConfirmation = { showReservationDialog.value = false },
                dialogTitle = "Peringatan",
                dialogText = "Reservasi hanya bisa dilakukan 1x dalam sehari!",
                icon = Icons.Rounded.Warning,
                confirmText = "OK"
            )
        }
    }
}