package com.example.annaclinic.presentation.screens.reservation.list

import android.util.Log
import android.widget.Toast
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.safeDrawingPadding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.ModalBottomSheet
import androidx.compose.material3.OutlinedCard
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.material3.rememberModalBottomSheetState
import androidx.compose.material3.rememberTopAppBarState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.input.nestedscroll.nestedScroll
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import cafe.adriel.voyager.core.screen.Screen
import cafe.adriel.voyager.navigator.LocalNavigator
import cafe.adriel.voyager.navigator.currentOrThrow
import com.example.annaclinic.core.component.CenterTopBar
import com.example.annaclinic.core.component.CircularLoading
import com.example.annaclinic.core.theme.fontFamily
import com.example.annaclinic.core.utils.Response
import com.example.annaclinic.presentation.screens.reservation.form.ReservationFormScreen
import com.example.annaclinic.presentation.screens.reservation.product.ProductBottomSheetContent
import org.koin.androidx.compose.koinViewModel
import java.text.NumberFormat

class ReservationListScreen : Screen {
    @OptIn(ExperimentalMaterial3Api::class)
    @Composable
    override fun Content() {
        val navigator = LocalNavigator.currentOrThrow
        val context = LocalContext.current
        val scrollBehavior = TopAppBarDefaults.pinnedScrollBehavior(rememberTopAppBarState())
        val viewModel: ReservationListViewModel = koinViewModel()
        val reservationList = viewModel.reservationList.collectAsState(initial = Response.Loading)
        val sheetStateProducts = rememberModalBottomSheetState(skipPartiallyExpanded = true)
        var showBottomSheetProducts by remember { mutableStateOf(false) }
        var name by remember { mutableStateOf("") }
        var price by remember { mutableStateOf(0L) }

        Scaffold(
            modifier = Modifier.nestedScroll(scrollBehavior.nestedScrollConnection),
            topBar = { CenterTopBar(navigator = navigator, title = "Reservasi") },
        ) { it ->
            Column(
                modifier = Modifier
                    .padding(it)
                    .background(androidx.compose.ui.graphics.Color.White)
            ) {
                when (reservationList.value) {
                    is Response.Loading -> {
                        CircularLoading(isLoading = true)
                    }

                    is Response.Failure -> {
                        // Display the error
                        CircularLoading(isLoading = false)
                    }

                    is Response.Success -> {
                        CircularLoading(isLoading = false)
                        val data = (reservationList.value as Response.Success).data
                        val numberFormat = NumberFormat.getInstance()
                        val rupiah = data.map { numberFormat.format(it.price) }

                        LazyColumn(
                            contentPadding = PaddingValues(vertical = 16.dp),
                        ) {
                            items(data) { reservation ->
                                // convert price to rupiah
                                val index = data.indexOf(reservation)
                                val rupiahPrice = rupiah[index]
                                Column(
                                    modifier = Modifier.padding(horizontal = 16.dp, vertical = 8.dp)
                                ) {
                                    OutlinedCard(
                                        onClick = {
                                            name = reservation.name
                                            price = reservation.price
                                            showBottomSheetProducts = true
                                        },
                                    ) {
                                        Row(
                                            modifier = Modifier
                                                .fillMaxWidth()
                                                .padding(16.dp),
                                            horizontalArrangement = Arrangement.SpaceBetween,
                                            verticalAlignment = Alignment.CenterVertically
                                        ) {
                                            Box(
                                                modifier = Modifier
                                                    .weight(1f)
                                                    .padding(end = 16.dp)
                                            ) {
                                                Text(
                                                    text = reservation.name,
                                                    style = TextStyle(
                                                        fontFamily = fontFamily,
                                                        fontSize = 16.sp,
                                                    )
                                                )
                                            }
                                            Text(
                                                text = "Rp. $rupiahPrice",
                                                style = TextStyle(
                                                    fontFamily = fontFamily,
                                                    fontSize = 16.sp,
                                                )
                                            )
                                        }
                                    }
                                }
                            }
                        }
                    }

                    is Response.Empty -> {}
                }
            }
        }

        when {
            showBottomSheetProducts -> {
                // Display the bottom sheet
                ModalBottomSheet(
                    modifier = Modifier.safeDrawingPadding(),
                    onDismissRequest = {
                        showBottomSheetProducts = false
                    },
                    sheetState = sheetStateProducts,

                    ) {
                    ProductBottomSheetContent(
                        onSkip = {
                            navigator.push(
                                ReservationFormScreen(
                                    name = name,
                                    price = price,
                                )
                            )
                            Log.d(
                                "ReservationListScreen",
                                "name : $name \n price : $price \nProducts onContinue: null"
                            )
                            showBottomSheetProducts = false
                        },
                        onContinue = {
                            if (it.isNotEmpty()) {
                                navigator.push(
                                    ReservationFormScreen(
                                        name = name,
                                        price = price,
                                        products = it
                                    )
                                )
                                Log.d(
                                    "ReservationListScreen",
                                    "name : $name \n price : $price \nProducts onContinue: $it"
                                )
                                showBottomSheetProducts = false
                            } else {
                                Toast.makeText(
                                    context,
                                    "Pilih produk terlebih dahulu!!",
                                    Toast.LENGTH_SHORT
                                ).show()
                            }
                        }
                    )
                }
            }
        }
    }
}