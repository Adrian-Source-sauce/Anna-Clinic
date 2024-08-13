package com.example.annaclinic.presentation.screens.reservation.form.component

import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.annaclinic.core.theme.fontFamily

@Composable
fun <T> ReservationTextField(
    placeHolder: String,
    value: T,
    isSingleLine: Boolean = true,
    keyboardType: KeyboardType,
    lines: Int = 1,
    isDisabled: Boolean = false,
    onValueChange: (T) -> Unit,
) {
    OutlinedTextField(
        modifier = Modifier
            .fillMaxWidth()
            .padding(start = 16.dp, end = 16.dp),
        singleLine = isSingleLine,
        keyboardOptions = KeyboardOptions(keyboardType = keyboardType),
        placeholder = {
            Text(text = placeHolder)
        },
        textStyle = TextStyle(
            fontFamily = fontFamily,
            color = Color.Black,
            fontSize = 16.sp,
            letterSpacing = 0.5.sp,
        ),
        minLines = lines,
        value = value.toString(),
        onValueChange = { onValueChange(it as T) },
        enabled = !isDisabled,
        shape = RoundedCornerShape(16.dp),

    )
}