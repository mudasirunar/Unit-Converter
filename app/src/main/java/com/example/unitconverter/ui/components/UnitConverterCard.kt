package com.example.unitconverter.ui.components

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.*
import androidx.compose.material3.TabRowDefaults.tabIndicatorOffset
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.unitconverter.data.convertUnits
import com.example.unitconverter.data.getConversionData
import com.example.unitconverter.ui.theme.*

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun UnitConverterCard() {
    val categories = getConversionData()
    var selectedCategoryIndex by remember { mutableStateOf(0) }
    val currentCategory = categories[selectedCategoryIndex]

    var valueInput by remember { mutableStateOf("1.0") }
    var selectedFromUnit by remember { mutableStateOf(currentCategory.units[0]) }
    var selectedToUnit by remember { mutableStateOf(currentCategory.units.getOrElse(1) { currentCategory.units[0] }) }

    // Reset units whenever category changes
    LaunchedEffect(selectedCategoryIndex) {
        selectedFromUnit = currentCategory.units[0]
        selectedToUnit = currentCategory.units.getOrElse(1) { currentCategory.units[0] }
    }

    Card(
        modifier = Modifier
            .fillMaxWidth()
            .border(1.dp, CardBorder, RoundedCornerShape(24.dp))
            .clip(RoundedCornerShape(24.dp)),
        colors = CardDefaults.cardColors(containerColor = CardBg)
    ) {
        Column(
            modifier = Modifier.padding(20.dp)
        ) {
            Text(
                text = "Unit Converter",
                fontSize = 18.sp,
                fontWeight = FontWeight.Bold,
                color = TextPrimary
            )

            Spacer(modifier = Modifier.height(16.dp))

            // Categories horizontal tabs
            ScrollableTabRow(
                selectedTabIndex = selectedCategoryIndex,
                edgePadding = 0.dp,
                containerColor = Color.Transparent,
                divider = {},
                indicator = { tabPositions ->
                    TabRowDefaults.SecondaryIndicator(
                        Modifier.tabIndicatorOffset(tabPositions[selectedCategoryIndex]),
                        color = AccentTeal
                    )
                }
            ) {
                categories.forEachIndexed { index, category ->
                    Tab(
                        selected = selectedCategoryIndex == index,
                        onClick = { selectedCategoryIndex = index },
                        text = {
                            Text(
                                text = category.title,
                                color = if (selectedCategoryIndex == index) TextPrimary else TextSecondary,
                                fontWeight = if (selectedCategoryIndex == index) FontWeight.Bold else FontWeight.Normal
                            )
                        }
                    )
                }
            }

            Spacer(modifier = Modifier.height(20.dp))

            // Value Input field
            OutlinedTextField(
                value = valueInput,
                onValueChange = { valueInput = it },
                label = { Text("Value to Convert", color = TextSecondary) },
                keyboardOptions = KeyboardOptions(
                    keyboardType = KeyboardType.Number,
                    imeAction = ImeAction.Done
                ),
                textStyle = LocalTextStyle.current.copy(color = TextPrimary, fontSize = 18.sp),
                colors = OutlinedTextFieldDefaults.colors(
                    focusedBorderColor = AccentTeal,
                    unfocusedBorderColor = CardBorder,
                    focusedLabelColor = AccentTeal
                ),
                modifier = Modifier.fillMaxWidth(),
                singleLine = true
            )

            Spacer(modifier = Modifier.height(16.dp))

            // Dual selectors: From Unit & To Unit
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                // From Unit Dropdown Selector
                Box(modifier = Modifier.weight(1f)) {
                    UnitDropDown(
                        label = "From Unit",
                        selectedUnit = selectedFromUnit,
                        units = currentCategory.units,
                        onUnitSelected = { selectedFromUnit = it }
                    )
                }

                Spacer(modifier = Modifier.width(16.dp))

                // To Unit Dropdown Selector
                Box(modifier = Modifier.weight(1f)) {
                    UnitDropDown(
                        label = "To Unit",
                        selectedUnit = selectedToUnit,
                        units = currentCategory.units,
                        onUnitSelected = { selectedToUnit = it }
                    )
                }
            }

            Spacer(modifier = Modifier.height(24.dp))

            // Conversion result box
            val inputVal = valueInput.toDoubleOrNull() ?: 0.0
            val convertedVal = convertUnits(inputVal, selectedFromUnit, selectedToUnit, currentCategory.isTemperature)

            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .background(
                        Brush.horizontalGradient(listOf(AccentTeal.copy(alpha = 0.1f), IndigoPrimary.copy(alpha = 0.1f))),
                        RoundedCornerShape(16.dp)
                    )
                    .padding(16.dp)
            ) {
                Column(
                    horizontalAlignment = Alignment.CenterHorizontally,
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Text(
                        text = String.format("%s %s =", valueInput, selectedFromUnit.name),
                        fontSize = 14.sp,
                        color = TextSecondary
                    )
                    Text(
                        text = String.format("%.5f %s", convertedVal, selectedToUnit.name),
                        fontSize = 24.sp,
                        fontWeight = FontWeight.Bold,
                        color = TextPrimary
                    )
                }
            }
        }
    }
}
