package com.example.unitconverter.ui.components

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.pager.HorizontalPager
import androidx.compose.foundation.pager.rememberPagerState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.*
import androidx.compose.material3.TabRowDefaults.tabIndicatorOffset
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.rotate
import androidx.compose.animation.core.Spring
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.core.spring
import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.relocation.BringIntoViewRequester
import androidx.compose.foundation.relocation.bringIntoViewRequester
import androidx.compose.ui.focus.onFocusChanged
import kotlinx.coroutines.delay
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.unitconverter.data.ConversionCategory
import com.example.unitconverter.data.convertUnits
import com.example.unitconverter.data.getConversionData
import com.example.unitconverter.data.formatValue
import com.example.unitconverter.ui.theme.*
import kotlinx.coroutines.launch
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.platform.LocalSoftwareKeyboardController

@OptIn(ExperimentalFoundationApi::class)
@Composable
fun UnitConverterCard() {
    val categories = getConversionData()
    val pagerState = rememberPagerState { categories.size }
    val coroutineScope = rememberCoroutineScope()
    val focusManager = LocalFocusManager.current
    val keyboardController = LocalSoftwareKeyboardController.current

    // Clear focus and hide keyboard whenever the page/tab changes
    LaunchedEffect(pagerState.currentPage) {
        focusManager.clearFocus()
        keyboardController?.hide()
    }

    // Dynamic colors derived from theme state
    val isDark = MaterialTheme.colorScheme.background == SlateDarkBgStart
    val cardBorder = if (isDark) BorderDark else BorderLight
    val textPrimary = MaterialTheme.colorScheme.onSurface
    val textSecondary = if (isDark) TextDarkSecondary else TextLightSecondary

    Card(
        modifier = Modifier
            .fillMaxWidth()
            .border(1.dp, cardBorder, RoundedCornerShape(24.dp))
            .clip(RoundedCornerShape(24.dp)),
        colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface)
    ) {
        Column(
            modifier = Modifier.padding(20.dp)
        ) {
            Text(
                text = "Unit Converter",
                fontSize = 18.sp,
                fontWeight = FontWeight.Bold,
                color = textPrimary
            )

            Spacer(modifier = Modifier.height(16.dp))

            // Categories horizontal tabs synced with swipe gestures
            ScrollableTabRow(
                selectedTabIndex = pagerState.currentPage,
                edgePadding = 0.dp,
                containerColor = Color.Transparent,
                divider = {},
                indicator = { tabPositions ->
                    TabRowDefaults.SecondaryIndicator(
                        Modifier.tabIndicatorOffset(tabPositions[pagerState.currentPage]),
                        color = AccentTeal
                    )
                }
            ) {
                categories.forEachIndexed { index, category ->
                    Tab(
                        selected = pagerState.currentPage == index,
                        onClick = {
                            coroutineScope.launch {
                                pagerState.animateScrollToPage(index)
                            }
                        },
                        text = {
                            Text(
                                text = category.title,
                                color = if (pagerState.currentPage == index) textPrimary else textSecondary,
                                fontWeight = if (pagerState.currentPage == index) FontWeight.Bold else FontWeight.Normal
                            )
                        }
                    )
                }
            }

            Spacer(modifier = Modifier.height(20.dp))

            // Swipeable pager containing the conversion calculator content for each category
            HorizontalPager(
                state = pagerState,
                modifier = Modifier.fillMaxWidth(),
                pageSpacing = 16.dp
            ) { page ->
                val category = categories[page]
                UnitConverterPageContent(
                    category = category,
                    textPrimary = textPrimary,
                    textSecondary = textSecondary,
                    cardBorder = cardBorder,
                    isDark = isDark
                )
            }
        }
    }
}

@OptIn(ExperimentalFoundationApi::class)
@Composable
fun UnitConverterPageContent(
    category: ConversionCategory,
    textPrimary: Color,
    textSecondary: Color,
    cardBorder: Color,
    isDark: Boolean
) {
    var valueInput by remember { mutableStateOf("1") }
    var selectedFromUnit by remember(category) { mutableStateOf(getDefaultFromUnit(category)) }
    var selectedToUnit by remember(category) { mutableStateOf(getDefaultToUnit(category)) }
    val coroutineScope = rememberCoroutineScope()
    val unitViewRequester = remember { BringIntoViewRequester() }

    Column(
        modifier = Modifier
            .fillMaxWidth()
            .defaultMinSize(minHeight = 260.dp)
            .bringIntoViewRequester(unitViewRequester)
    ) {
        // Value Input field
        OutlinedTextField(
            value = valueInput,
            onValueChange = { valueInput = it },
            label = { Text("Value to Convert", color = textSecondary) },
            keyboardOptions = KeyboardOptions(
                keyboardType = KeyboardType.Number,
                imeAction = ImeAction.Done
            ),
            textStyle = LocalTextStyle.current.copy(color = textPrimary, fontSize = 18.sp),
            colors = OutlinedTextFieldDefaults.colors(
                focusedBorderColor = AccentTeal,
                unfocusedBorderColor = cardBorder,
                focusedLabelColor = AccentTeal
            ),
            modifier = Modifier
                .fillMaxWidth()
                .onFocusChanged { focusState ->
                    if (focusState.isFocused) {
                        coroutineScope.launch {
                            delay(300)
                            unitViewRequester.bringIntoView()
                        }
                    }
                },
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
                    label = "From",
                    selectedUnit = selectedFromUnit,
                    units = category.units,
                    onUnitSelected = { selectedFromUnit = it },
                    isDark = isDark
                )
            }

            var rotationAngle by remember { mutableStateOf(0f) }
            val animatedRotation by animateFloatAsState(
                targetValue = rotationAngle,
                animationSpec = spring(
                    dampingRatio = Spring.DampingRatioMediumBouncy,
                    stiffness = Spring.StiffnessLow
                )
            )

            Box(
                modifier = Modifier
                    .padding(horizontal = 8.dp)
                    .size(40.dp)
                    .rotate(animatedRotation)
                    .clip(RoundedCornerShape(50))
                    .background(AccentTeal)
                    .clickable {
                        rotationAngle += 180f
                        val temp = selectedFromUnit
                        selectedFromUnit = selectedToUnit
                        selectedToUnit = temp
                    },
                contentAlignment = Alignment.Center
            ) {
                Text(
                    text = "⇄",
                    color = TextDarkPrimary,
                    fontSize = 20.sp,
                    fontWeight = FontWeight.Bold,
                    textAlign = TextAlign.Center
                )
            }

            // To Unit Dropdown Selector
            Box(modifier = Modifier.weight(1f)) {
                UnitDropDown(
                    label = "To",
                    selectedUnit = selectedToUnit,
                    units = category.units,
                    onUnitSelected = { selectedToUnit = it },
                    isDark = isDark
                )
            }
        }

        Spacer(modifier = Modifier.height(24.dp))

        // Conversion result box
        val inputVal = valueInput.toDoubleOrNull() ?: 0.0
        val convertedVal = convertUnits(inputVal, selectedFromUnit, selectedToUnit, category.isTemperature)
        val formattedResult = formatValue(convertedVal)

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
                    text = "${formatValue(inputVal)} ${selectedFromUnit.name} =",
                    fontSize = 14.sp,
                    color = textSecondary,
                    textAlign = TextAlign.Center
                )
                Spacer(modifier = Modifier.height(4.dp))
                Text(
                    text = "$formattedResult ${selectedToUnit.name}",
                    fontSize = 24.sp,
                    fontWeight = FontWeight.Bold,
                    color = textPrimary,
                    textAlign = TextAlign.Center
                )
            }
        }

        // Push result area higher above the keyboard when focused
        Spacer(modifier = Modifier.height(24.dp))
    }
}

private fun getDefaultFromUnit(category: ConversionCategory): com.example.unitconverter.data.ConversionUnit {
    val name = when (category.title) {
        "Length" -> "Kilometers"
        "Weight" -> "Kilograms"
        "Temperature" -> "Celsius"
        "Area" -> "Sq Meters"
        "Volume" -> "Liters"
        "Storage" -> "Gigabytes (GB)"
        "Time" -> "Hours"
        "Speed" -> "km/h"
        "Energy" -> "Kilocalories (kcal)"
        "Power" -> "Kilowatts (kW)"
        "Pressure" -> "Psi"
        else -> category.units[0].name
    }
    return category.units.firstOrNull { it.name == name } ?: category.units[0]
}

private fun getDefaultToUnit(category: ConversionCategory): com.example.unitconverter.data.ConversionUnit {
    val name = when (category.title) {
        "Length" -> "Meters"
        "Weight" -> "Grams"
        "Temperature" -> "Fahrenheit"
        "Area" -> "Sq Feet"
        "Volume" -> "Milliliters"
        "Storage" -> "Megabytes (MB)"
        "Time" -> "Minutes"
        "Speed" -> "mph"
        "Energy" -> "Kilojoules (kJ)"
        "Power" -> "Horsepower (hp)"
        "Pressure" -> "Bar"
        else -> category.units.getOrElse(1) { category.units[0] }.name
    }
    return category.units.firstOrNull { it.name == name } ?: category.units.getOrElse(1) { category.units[0] }
}
