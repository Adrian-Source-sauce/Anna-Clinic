package com.example.annaclinic.core.component

import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.annaclinic.core.theme.fontFamily

@Composable
fun DefaultTextField(
    modifier: Modifier = Modifier,
    label: String,
    value: String,
    isDisabled: Boolean = false,
    isSingleLine: Boolean = true,
    lines: Int = 1,
    dynamicLines: Boolean = false,
    onValueChange: (String) -> Unit = {},
) {
    var textFieldValue by remember { mutableStateOf(value) }
    var calculatedLines by remember { mutableStateOf(lines) }

    if (dynamicLines) {
        LaunchedEffect(textFieldValue) {
            // Measure text and calculate number of lines needed
            val charWidth = 8.5 // Assuming an average character width in pixels, this might need adjustment
            val maxCharsInLine = 300 / charWidth // Assuming the width of the field is 300 pixels
            val newLines = (textFieldValue.length / maxCharsInLine).toInt() + 1
            calculatedLines = if (newLines > 1) newLines else 1
        }
    } else {
        calculatedLines = lines
    }

    OutlinedTextField(
        modifier = Modifier.fillMaxWidth(),
        singleLine = isSingleLine,
        textStyle = TextStyle(
            fontFamily = fontFamily,
            color = Color.Black,
            fontSize = 16.sp,
            letterSpacing = 0.5.sp,
        ),
        value = textFieldValue,
        onValueChange = {
            textFieldValue = it
            onValueChange(it)
        },
        minLines = calculatedLines,
        maxLines = calculatedLines,
        enabled = !isDisabled,
        label = { Text(label, style = TextStyle(fontFamily = fontFamily)) },
        shape = RoundedCornerShape(16.dp),
    )
}