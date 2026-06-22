package com.example.unitconverter.ui.components

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.ui.draw.clip
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.KeyboardArrowDown
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.graphics.Color
import com.example.unitconverter.data.ConversionUnit
import com.example.unitconverter.ui.theme.*

@Composable
fun UnitDropDown(
    label: String,
    selectedUnit: ConversionUnit,
    units: List<ConversionUnit>,
    onUnitSelected: (ConversionUnit) -> Unit,
    isDark: Boolean
) {
    var expanded by remember { mutableStateOf(false) }

    val containerBg = if (isDark) SlateDarkCard else SlateLightCard
    val borderCol = if (isDark) BorderDark else BorderLight
    val menuBg = if (isDark) SlateDarkBgStart else SlateLightCard
    val labelColor = if (isDark) TextDarkSecondary else TextLightSecondary
    val textColor = if (isDark) TextDarkPrimary else TextLightPrimary

    Box(
        modifier = Modifier
            .fillMaxWidth()
            .clip(RoundedCornerShape(12.dp))
            .background(containerBg)
            .border(1.dp, borderCol, RoundedCornerShape(12.dp))
            .clickable { expanded = true }
            .padding(horizontal = 14.dp, vertical = 10.dp)
    ) {
        Row(
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically,
            modifier = Modifier.fillMaxWidth()
        ) {
            Column(
                modifier = Modifier.weight(1f)
            ) {
                Text(label, fontSize = 11.sp, color = labelColor, fontWeight = FontWeight.SemiBold)
                Spacer(modifier = Modifier.height(2.dp))
                Text(
                    text = selectedUnit.name,
                    fontSize = 14.sp,
                    fontWeight = FontWeight.Bold,
                    color = textColor,
                    maxLines = 2
                )
            }
            Spacer(modifier = Modifier.width(4.dp))
            Icon(
                imageVector = Icons.Default.KeyboardArrowDown,
                contentDescription = null,
                tint = labelColor,
                modifier = Modifier.size(20.dp)
            )
        }

        DropdownMenu(
            expanded = expanded,
            onDismissRequest = { expanded = false },
            modifier = Modifier
                .clip(RoundedCornerShape(12.dp))
                .background(menuBg)
                .border(1.dp, borderCol, RoundedCornerShape(12.dp)),
            shape = RoundedCornerShape(12.dp)
        ) {
            units.forEach { unit ->
                val isSelected = unit == selectedUnit
                val itemBgColor = if (isSelected) {
                    if (isDark) IndigoPrimary.copy(alpha = 0.15f) else IndigoLight
                } else {
                    Color.Transparent
                }
                val itemTextColor = if (isSelected) {
                    if (isDark) AccentTeal else IndigoPrimary
                } else {
                    textColor
                }
                val itemFontWeight = if (isSelected) FontWeight.Bold else FontWeight.Medium

                DropdownMenuItem(
                    text = { 
                        Text(
                            text = unit.name, 
                            color = itemTextColor,
                            fontSize = 14.sp,
                            fontWeight = itemFontWeight
                        ) 
                    },
                    onClick = {
                        onUnitSelected(unit)
                        expanded = false
                    },
                    modifier = Modifier
                        .fillMaxWidth()
                        .background(itemBgColor)
                )
            }
        }
    }
}
