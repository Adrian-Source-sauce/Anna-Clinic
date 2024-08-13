package com.example.annaclinic.presentation.screens.reservation.product

import android.util.Log
import androidx.compose.animation.animateContentSize
import androidx.compose.animation.core.tween
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Card
import androidx.compose.material3.Checkbox
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedCard
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.style.TextDecoration
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import coil.compose.AsyncImage
import com.example.annaclinic.core.theme.fontFamily
import com.example.annaclinic.domain.model.Products
import java.text.NumberFormat

@Composable
fun ProductItem(
    modifier: Modifier = Modifier,
    product: Products,
    onProductChecked: (Boolean, Products) -> Unit
) {
    var checked by remember { mutableStateOf(false) }
    var isExpanded by remember { mutableStateOf(false) }
    val numberFormat = NumberFormat.getInstance()

    OutlinedCard(
        modifier = modifier
            .fillMaxWidth()
            .animateContentSize(tween(durationMillis = 500)),
    ) {
        Row(
            modifier = modifier.fillMaxWidth(),
            verticalAlignment = Alignment.CenterVertically,
        ) {
            Box(
                modifier = modifier
                    .size(100.dp)
                    .padding(12.dp)

            ) {
                Column(
                    modifier = modifier.fillMaxSize(),
                    horizontalAlignment = Alignment.CenterHorizontally,
                    verticalArrangement = Arrangement.Center
                ) {
                    Card(
                        shape = RoundedCornerShape(16.dp)
                    ) {
                        AsyncImage(
                            model = product.imageUrl,
                            contentDescription = null,
                            contentScale = ContentScale.Crop
                        )
                    }
                }
            }

            Box(
                modifier = modifier
                    .weight(1f)
                    .padding(
                        end = 16.dp,
                        start = 8.dp,
                        top = 16.dp,
                        bottom = 16.dp,
                    )
            ) {
                Column(
                    verticalArrangement = Arrangement.spacedBy(8.dp),
                ) {
                    Text(
                        text = product.name,
                        style = TextStyle(fontSize = 16.sp, fontFamily = fontFamily)
                    )
                    Text(
                        text = "Rp. ${numberFormat.format(product.price)}",
                        style = TextStyle(fontSize = 16.sp, fontFamily = fontFamily)
                    )
                    Text(
                        modifier = modifier
                            .clickable {
                                isExpanded = !isExpanded // Toggle expansion
                                Log.d("ProductItem", "ProductItem: ${product.detail}")
                            },
                        text = if (isExpanded) "Hide Details" else "Detail",
                        style = TextStyle(
                            fontSize = 16.sp,
                            fontFamily = fontFamily,
                            color = MaterialTheme.colorScheme.primary,
                            textDecoration = TextDecoration.Underline
                        )
                    )
                }
            }

            Row(
                modifier = modifier.padding(12.dp),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.End
            ) {
                Checkbox(
                    checked = checked,
                    onCheckedChange = {
                        checked = it
                        onProductChecked(it, product)
                        Log.d("ProductItem", "ProductItem: $it \n Product: $product")
                    },
                )
            }
        }

        // add detail below the product item
        // Show details if expanded
        if (isExpanded) {
            Column(
                modifier = modifier
                    .fillMaxWidth()
                    .padding(12.dp)
            ) {
                Text(
                    text = product.detail,
                    style = TextStyle(fontSize = 16.sp, fontFamily = fontFamily)
                )
            }
        }
    }
}