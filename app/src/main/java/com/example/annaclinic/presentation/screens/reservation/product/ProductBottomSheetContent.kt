package com.example.annaclinic.presentation.screens.reservation.product

import android.util.Log
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.annaclinic.core.theme.fontFamily
import com.example.annaclinic.domain.model.Products

@Composable
fun ProductBottomSheetContent(
    modifier: Modifier = Modifier,
    onSkip: () -> Unit,
    onContinue: (products: List<Products>) -> Unit
) {
    var receivedProducts by remember { mutableStateOf(listOf<Products>()) }
    val handleSelectedProducts: (List<Products>) -> Unit = { selectedProducts ->
        receivedProducts = selectedProducts
        Log.d("ProductBottomSheet", "Received Selected Products: $receivedProducts")
    }

    Column(
        modifier = modifier.padding(horizontal = 16.dp),
        verticalArrangement = Arrangement.spacedBy(8.dp)
    ) {
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Text(
                text = "Produk",
                style = TextStyle(
                    fontSize = 18.sp,
                    fontFamily = fontFamily,
                    fontWeight = FontWeight.Bold
                )
            )

            Row {
                TextButton(onClick = { onSkip() }) {
                    Text(
                        text = "Lewati",
                        style = TextStyle(
                            fontFamily = fontFamily,
                            fontSize = 16.sp,
                            fontWeight = FontWeight.Medium
                        )
                    )
                }

                TextButton(onClick = {
                    onContinue(receivedProducts)
                }) {
                    Text(
                        text = "Tambah",
                        style = TextStyle(
                            fontFamily = fontFamily,
                            fontSize = 16.sp,
                            fontWeight = FontWeight.Medium
                        )
                    )
                }
            }
        }

        ListProducts(sendSelectedProducts = handleSelectedProducts)
    }
}