package com.example.unitconverter.ui.components

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Clear
import androidx.compose.material.icons.filled.Search
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.unitconverter.data.CurrencyRate
import com.example.unitconverter.ui.theme.*

@Composable
fun SearchSheetContent(
    query: String,
    onQueryChange: (String) -> Unit,
    filteredRates: List<CurrencyRate>,
    onSelect: (CurrencyRate) -> Unit,
    isDarkTheme: Boolean
) {
    // Dynamic Colors based on Dark/Light theme mode
    val textPrimary = if (isDarkTheme) TextDarkPrimary else TextLightPrimary
    val textSecondary = if (isDarkTheme) TextDarkSecondary else TextLightSecondary
    val textTertiary = if (isDarkTheme) TextDarkTertiary else TextLightTertiary
    val sheetBorder = if (isDarkTheme) BorderDark else BorderLight
    val itemBg = if (isDarkTheme) ItemBgDark else ItemBgLight
    val itemBorder = if (isDarkTheme) ItemBorderDark else ItemBorderLight

    Column(
        modifier = Modifier
            .fillMaxHeight(0.85f)
            .padding(16.dp)
    ) {
        Text(
            text = "Select Currency",
            fontSize = 20.sp,
            fontWeight = FontWeight.Bold,
            color = textPrimary,
            modifier = Modifier.padding(bottom = 12.dp)
        )

        // Search Bar
        OutlinedTextField(
            value = query,
            onValueChange = onQueryChange,
            placeholder = { Text("Search by country or code...", color = textTertiary) },
            leadingIcon = { Icon(Icons.Default.Search, contentDescription = null, tint = textSecondary) },
            trailingIcon = {
                if (query.isNotEmpty()) {
                    IconButton(onClick = { onQueryChange("") }) {
                        Icon(Icons.Default.Clear, contentDescription = "Clear search", tint = textSecondary)
                    }
                }
            },
            textStyle = LocalTextStyle.current.copy(color = textPrimary),
            colors = OutlinedTextFieldDefaults.colors(
                focusedBorderColor = IndigoPrimary,
                unfocusedBorderColor = sheetBorder
            ),
            modifier = Modifier.fillMaxWidth(),
            singleLine = true
        )

        Spacer(modifier = Modifier.height(16.dp))

        // Currency rates list
        if (filteredRates.isEmpty()) {
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .weight(1f),
                contentAlignment = Alignment.Center
            ) {
                Text("No currencies found", color = textSecondary, fontSize = 16.sp)
            }
        } else {
            LazyColumn(
                modifier = Modifier.weight(1f),
                verticalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                items(filteredRates) { rate ->
                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .clip(RoundedCornerShape(12.dp))
                            .background(itemBg)
                            .border(1.dp, itemBorder, RoundedCornerShape(12.dp))
                            .clickable { onSelect(rate) }
                            .padding(16.dp),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Column {
                            Text(rate.code, fontSize = 18.sp, fontWeight = FontWeight.Bold, color = textPrimary)
                            Text(rate.fullName, fontSize = 13.sp, color = textSecondary)
                        }
                        Text(
                            text = String.format("%.4f", rate.rate),
                            fontSize = 16.sp,
                            fontWeight = FontWeight.Medium,
                            color = AccentTeal
                        )
                    }
                }
            }
        }
    }
}
