package com.example.annaclinic.presentation.screens.reservation.product

import android.util.Log
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import com.example.annaclinic.core.utils.Response
import com.example.annaclinic.domain.model.Products
import com.valentinilk.shimmer.shimmer
import org.koin.compose.koinInject
import java.text.NumberFormat

@Composable
fun ListProducts(
    modifier: Modifier = Modifier,
    viewModel: ProductViewModel = koinInject(),
    sendSelectedProducts: (List<Products>) -> Unit
) {
    var selectedProducts by remember { mutableStateOf(listOf<Products>()) }

    val reservationList = viewModel.productList.collectAsState(initial = Response.Loading)

    // Display the product list
    when (reservationList.value) {
        is Response.Loading -> {
            // Handle loading
            LazyColumn(
                verticalArrangement = Arrangement.spacedBy(8.dp),
            ) {
                items(10) {
                    Box(
                        modifier = modifier
                            .fillMaxWidth()
                            .height(85.dp)
                            .clip(RoundedCornerShape(16.dp))
                            .shimmer()
                            .background(color = Color.LightGray)
                    )
                }
            }
        }

        is Response.Failure -> {
            // Handle failure
        }

        is Response.Success -> {
            // Handle success
            val data = (reservationList.value as Response.Success).data

            LazyColumn(
                verticalArrangement = Arrangement.spacedBy(8.dp),
                contentPadding = PaddingValues(bottom = 16.dp)
            ) {
                items(data) {
                    ProductItem(product = it) { isChecked, product ->
                        selectedProducts = if (isChecked) {
                            selectedProducts + product
                        } else {
                            selectedProducts - product
                        }
                        Log.d("ProductList", "Selected Products: $selectedProducts")

                        sendSelectedProducts(selectedProducts)
                    }
                }
            }
        }

        is Response.Empty -> {
            // Handle empty
        }
    }
}
