package com.example.unitconverter.ui.components

import androidx.compose.foundation.background
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
    onSelect: (CurrencyRate) -> Unit
) {
    Column(
        modifier = Modifier
            .fillMaxHeight(0.85f)
            .padding(16.dp)
    ) {
        Text(
            text = "Select Currency",
            fontSize = 20.sp,
            fontWeight = FontWeight.Bold,
            color = TextPrimary,
            modifier = Modifier.padding(bottom = 12.dp)
        )

        // Search Bar
        OutlinedTextField(
            value = query,
            onValueChange = onQueryChange,
            placeholder = { Text("Search by country or code...", color = TextTertiary) },
            leadingIcon = { Icon(Icons.Default.Search, contentDescription = null, tint = TextSecondary) },
            trailingIcon = {
                if (query.isNotEmpty()) {
                    IconButton(onClick = { onQueryChange("") }) {
                        Icon(Icons.Default.Clear, contentDescription = "Clear search", tint = TextSecondary)
                    }
                }
            },
            textStyle = LocalTextStyle.current.copy(color = TextPrimary),
            colors = OutlinedTextFieldDefaults.colors(
                focusedBorderColor = IndigoPrimary,
                unfocusedBorderColor = CardBorder
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
                Text("No currencies found", color = TextSecondary, fontSize = 16.sp)
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
                            .clickable { onSelect(rate) }
                            .background(CardBorder.copy(alpha = 0.5f), RoundedCornerShape(12.dp))
                            .padding(16.dp),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Column {
                            Text(rate.code, fontSize = 18.sp, fontWeight = FontWeight.Bold, color = TextPrimary)
                            Text(rate.fullName, fontSize = 13.sp, color = TextSecondary)
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
