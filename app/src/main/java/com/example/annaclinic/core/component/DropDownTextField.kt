package com.example.annaclinic.core.component

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.ExposedDropdownMenuBox
import androidx.compose.material3.ExposedDropdownMenuDefaults
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
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

@ExperimentalMaterial3Api
@Composable
fun DropDownTextField(
    label: String,
    value: String,
    onValueChange: (String) -> Unit
) {
    val options = listOf("Pasien", "Admin")
    var expanded by remember { mutableStateOf(false) }
    var selectedOptionText by remember { mutableStateOf(options[0]) }

    Box( modifier = Modifier
        .fillMaxWidth()
    ){
        ExposedDropdownMenuBox(
            expanded = expanded,
            onExpandedChange = { expanded = !expanded },
        ) {
            OutlinedTextField(
                modifier = Modifier
                    .menuAnchor()
                    .fillMaxWidth(),
                value = value,
                readOnly = true,
                onValueChange = { onValueChange(it) },
                textStyle = TextStyle(
                    fontFamily = fontFamily,
                    color = Color.Black,
                    fontSize = 16.sp,
                    letterSpacing = 0.5.sp,
                ),
                label = { Text(label, style = TextStyle(fontFamily = fontFamily)) },
                shape = RoundedCornerShape(16.dp),
                trailingIcon = {
                    ExposedDropdownMenuDefaults.TrailingIcon(
                        expanded = expanded
                    )
                },
                colors = ExposedDropdownMenuDefaults.outlinedTextFieldColors(),
            )
            ExposedDropdownMenu(
                expanded = expanded,
                onDismissRequest = {
                    expanded = false
                },
            ) {
                options.forEach { selectionOption ->
                    DropdownMenuItem(text = {
                        Text(selectionOption)
                    }, onClick = {
                        selectedOptionText = selectionOption
                        onValueChange(selectionOption)
                        expanded = false
                    })
                }
            }
        }
    }
}