package com.example.unitconverter.ui.components

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.KeyboardArrowDown
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.unitconverter.data.ConversionUnit
import com.example.unitconverter.ui.theme.*

@Composable
fun UnitDropDown(
    label: String,
    selectedUnit: ConversionUnit,
    units: List<ConversionUnit>,
    onUnitSelected: (ConversionUnit) -> Unit
) {
    var expanded by remember { mutableStateOf(false) }

    Box(
        modifier = Modifier
            .fillMaxWidth()
            .clickable { expanded = true }
            .background(CardBorder, RoundedCornerShape(12.dp))
            .padding(12.dp)
    ) {
        Row(
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically,
            modifier = Modifier.fillMaxWidth()
        ) {
            Column {
                Text(label, fontSize = 11.sp, color = TextSecondary)
                Text(selectedUnit.name, fontSize = 16.sp, fontWeight = FontWeight.Bold, color = TextPrimary)
            }
            Icon(Icons.Default.KeyboardArrowDown, contentDescription = null, tint = TextSecondary)
        }

        DropdownMenu(
            expanded = expanded,
            onDismissRequest = { expanded = false },
            modifier = Modifier.background(BottomSheetBg)
        ) {
            units.forEach { unit ->
                DropdownMenuItem(
                    text = { Text(unit.name, color = TextPrimary) },
                    onClick = {
                        onUnitSelected(unit)
                        expanded = false
                    }
                )
            }
        }
    }
}
