package com.example.unitconverter.ui

import android.view.HapticFeedbackConstants
import androidx.compose.animation.core.Spring
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.core.spring
import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.interaction.PressInteraction
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.imePadding
import androidx.compose.foundation.layout.navigationBarsPadding
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.statusBarsPadding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.relocation.BringIntoViewRequester
import androidx.compose.foundation.relocation.bringIntoViewRequester
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.KeyboardArrowDown
import androidx.compose.material.icons.filled.NightsStay
import androidx.compose.material.icons.filled.Refresh
import androidx.compose.material.icons.filled.WbSunny
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.LocalTextStyle
import androidx.compose.material3.ModalBottomSheet
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.OutlinedTextFieldDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.rotate
import androidx.compose.ui.focus.onFocusChanged
import androidx.compose.ui.geometry.Rect
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.onGloballyPositioned
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.platform.LocalView
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.IntSize
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.unitconverter.data.CurrencyViewModel
import com.example.unitconverter.data.UnitConverterViewModel
import com.example.unitconverter.data.formatValue
import com.example.unitconverter.ui.components.SearchSheetContent
import com.example.unitconverter.ui.components.UnitConverterCard
import com.example.unitconverter.ui.theme.AccentAmber
import com.example.unitconverter.ui.theme.AccentTeal
import com.example.unitconverter.ui.theme.BorderDark
import com.example.unitconverter.ui.theme.BorderLight
import com.example.unitconverter.ui.theme.IndigoPrimary
import com.example.unitconverter.ui.theme.SlateDarkBgStart
import com.example.unitconverter.ui.theme.SlateDarkCard
import com.example.unitconverter.ui.theme.SlateLightBgStart
import com.example.unitconverter.ui.theme.SlateLightCard
import com.example.unitconverter.ui.theme.TextDarkPrimary
import com.example.unitconverter.ui.theme.TextDarkSecondary
import com.example.unitconverter.ui.theme.TextDarkTertiary
import com.example.unitconverter.ui.theme.TextLightPrimary
import com.example.unitconverter.ui.theme.TextLightSecondary
import com.example.unitconverter.ui.theme.TextLightTertiary
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

@OptIn(ExperimentalMaterial3Api::class, ExperimentalFoundationApi::class)
@Composable
fun ConverterApp(
    viewModel: CurrencyViewModel,
    unitViewModel: UnitConverterViewModel,
    isDarkTheme: Boolean,
    onThemeChanged: (Boolean) -> Unit
) {
    val scrollState = rememberScrollState()
    val coroutineScope = rememberCoroutineScope()
    val currencyViewRequester = remember { BringIntoViewRequester() }
    val view = LocalView.current
    
    // States for Currency conversion - Default USD to PKR
    var amountInput by remember { mutableStateOf("1") }
    var fromCurrency by remember { mutableStateOf("USD") }
    var toCurrency by remember { mutableStateOf("PKR") }
    
    // Bottom sheet controls
    var showBottomSheet by remember { mutableStateOf(false) }
    var selectSource by remember { mutableStateOf(true) } // true for "From", false for "To"
    var searchQuery by remember { mutableStateOf("") }
    
    // Rotate state for swap button
    var rotationAngle by remember { mutableStateOf(0f) }

    var currencyResultSize by remember { mutableStateOf(IntSize.Zero) }
    val density = LocalDensity.current

    val currencyInteractionSource = remember { MutableInteractionSource() }

    LaunchedEffect(currencyInteractionSource) {
        currencyInteractionSource.interactions.collect { interaction ->
            if (interaction is PressInteraction.Release) {
                delay(400)
                val width = currencyResultSize.width.toFloat()
                val height = currencyResultSize.height.toFloat()
                val extraPx = with(density) { 48.dp.toPx() }
                currencyViewRequester.bringIntoView(
                    Rect(0f, 0f, width, height + extraPx)
                )
            }
        }
    }

    // Dynamic Theme Colors
    val cardBg = if (isDarkTheme) SlateDarkCard else SlateLightCard
    val cardBorder = if (isDarkTheme) BorderDark else BorderLight
    val textPrimary = if (isDarkTheme) TextDarkPrimary else TextLightPrimary
    val textSecondary = if (isDarkTheme) TextDarkSecondary else TextLightSecondary

    Column(
        modifier = Modifier
            .fillMaxSize()
            .statusBarsPadding()
            .navigationBarsPadding()
            .imePadding()
            .verticalScroll(scrollState)
            .padding(20.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        // App Title Header Row with Theme Toggle
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(top = 10.dp, bottom = 24.dp),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Text(
                text = "Unit Converter",
                fontSize = 28.sp,
                fontWeight = FontWeight.ExtraBold,
                color = textPrimary
            )
            
            ThemeToggle(isDarkTheme = isDarkTheme, onThemeChanged = onThemeChanged)
        }

        // 1. Currency Exchange Card
        Card(
            modifier = Modifier
                .fillMaxWidth()
                .border(1.dp, cardBorder, RoundedCornerShape(24.dp))
                .clip(RoundedCornerShape(24.dp)),
            colors = CardDefaults.cardColors(containerColor = cardBg)
        ) {
            Column(
                modifier = Modifier.padding(20.dp)
            ) {
                // Card Header
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Text(
                        text = "Currency Exchange",
                        fontSize = 18.sp,
                        fontWeight = FontWeight.Bold,
                        color = textPrimary
                    )
                    IconButton(
                        onClick = {
                            view.performHapticFeedback(HapticFeedbackConstants.CLOCK_TICK)
                            viewModel.fetchRates()
                        },
                        modifier = Modifier.size(24.dp)
                    ) {
                        Icon(
                            imageVector = Icons.Default.Refresh,
                            contentDescription = "Refresh rates",
                            tint = AccentTeal
                        )
                    }
                }
                
                Spacer(modifier = Modifier.height(16.dp))

                // Error State + No Cache: Fatal Error block
                if (viewModel.errorMessage != null && viewModel.rates.isEmpty()) {
                    Column(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(vertical = 24.dp),
                        horizontalAlignment = Alignment.CenterHorizontally,
                        verticalArrangement = Arrangement.Center
                    ) {
                        Text(
                            text = "⚠️ Connection Failed",
                            color = AccentTeal,
                            fontSize = 18.sp,
                            fontWeight = FontWeight.Bold,
                            modifier = Modifier.padding(bottom = 8.dp)
                        )
                        Text(
                            text = viewModel.errorMessage ?: "Failed to sync exchange rates. Please check your internet connection.",
                            color = textSecondary,
                            fontSize = 13.sp,
                            textAlign = TextAlign.Center,
                            modifier = Modifier.padding(bottom = 20.dp, start = 16.dp, end = 16.dp)
                        )
                        Button(
                            onClick = { viewModel.fetchRates() },
                            colors = ButtonDefaults.buttonColors(containerColor = IndigoPrimary)
                        ) {
                            Text("Try Again", color = TextDarkPrimary)
                        }
                    }
                } else {
                    // Normal state: Input + Selectors + Result
                    
                    // Amount Input
                    OutlinedTextField(
                        value = amountInput,
                        onValueChange = { amountInput = it },
                        label = { Text("Amount", color = textSecondary) },
                        keyboardOptions = KeyboardOptions(
                            keyboardType = KeyboardType.Number,
                            imeAction = ImeAction.Done
                        ),
                        textStyle = LocalTextStyle.current.copy(color = textPrimary, fontSize = 18.sp),
                        colors = OutlinedTextFieldDefaults.colors(
                            focusedBorderColor = IndigoPrimary,
                            unfocusedBorderColor = cardBorder,
                            focusedLabelColor = IndigoPrimary
                        ),
                        interactionSource = currencyInteractionSource,
                        modifier = Modifier
                            .fillMaxWidth()
                            .onFocusChanged { focusState ->
                                if (focusState.isFocused) {
                                    coroutineScope.launch {
                                        delay(400)
                                        val width = currencyResultSize.width.toFloat()
                                        val height = currencyResultSize.height.toFloat()
                                        val extraPx = with(density) { 48.dp.toPx() }
                                        currencyViewRequester.bringIntoView(
                                            Rect(0f, 0f, width, height + extraPx)
                                        )
                                    }
                                }
                            },
                        singleLine = true
                    )

                    Spacer(modifier = Modifier.height(16.dp))

                    // Currency Selectors
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        // From Currency Select Box
                        Box(
                            modifier = Modifier
                                .weight(1f)
                                .clip(RoundedCornerShape(12.dp))
                                .background(cardBorder)
                                .clickable {
                                    selectSource = true
                                    searchQuery = ""
                                    showBottomSheet = true
                                }
                                .padding(12.dp)
                        ) {
                            Row(
                                horizontalArrangement = Arrangement.SpaceBetween,
                                verticalAlignment = Alignment.CenterVertically,
                                modifier = Modifier.fillMaxWidth()
                            ) {
                                Column {
                                    Text("From", fontSize = 11.sp, color = textSecondary)
                                    Text(fromCurrency, fontSize = 18.sp, fontWeight = FontWeight.Bold, color = textPrimary)
                                }
                                Icon(Icons.Default.KeyboardArrowDown, contentDescription = null, tint = textSecondary)
                            }
                        }

                        // Swap Button with rotation animation
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
                                .background(IndigoPrimary)
                                .clickable {
                                    view.performHapticFeedback(HapticFeedbackConstants.CLOCK_TICK)
                                    rotationAngle += 180f
                                    val temp = fromCurrency
                                    fromCurrency = toCurrency
                                    toCurrency = temp
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

                        // To Currency Select Box
                        Box(
                            modifier = Modifier
                                .weight(1f)
                                .clip(RoundedCornerShape(12.dp))
                                .background(cardBorder)
                                .clickable {
                                    selectSource = false
                                    searchQuery = ""
                                    showBottomSheet = true
                                }
                                .padding(12.dp)
                        ) {
                            Row(
                                horizontalArrangement = Arrangement.SpaceBetween,
                                verticalAlignment = Alignment.CenterVertically,
                                modifier = Modifier.fillMaxWidth()
                            ) {
                                Column {
                                    Text("To", fontSize = 11.sp, color = textSecondary)
                                    Text(toCurrency, fontSize = 18.sp, fontWeight = FontWeight.Bold, color = textPrimary)
                                }
                                Icon(Icons.Default.KeyboardArrowDown, contentDescription = null, tint = textSecondary)
                            }
                        }
                    }

                    Spacer(modifier = Modifier.height(20.dp))

                    // Loading State or Conversion Result
                    if (viewModel.isLoading) {
                        Box(
                            modifier = Modifier
                                .fillMaxWidth()
                                .height(60.dp),
                            contentAlignment = Alignment.Center
                        ) {
                            CircularProgressIndicator(color = IndigoPrimary)
                        }
                    } else {
                        val amount = amountInput.toDoubleOrNull() ?: 0.0
                        val result = viewModel.convert(amount, fromCurrency, toCurrency)
                        
                        Box(
                            modifier = Modifier
                                .fillMaxWidth()
                                .background(
                                    Brush.horizontalGradient(listOf(IndigoPrimary.copy(alpha = 0.1f), AccentTeal.copy(alpha = 0.1f))),
                                    RoundedCornerShape(16.dp)
                                )
                                .padding(16.dp)
                                .bringIntoViewRequester(currencyViewRequester)
                                .onGloballyPositioned { coordinates ->
                                    currencyResultSize = coordinates.size
                                }
                        ) {
                            Column(horizontalAlignment = Alignment.CenterHorizontally, modifier = Modifier.fillMaxWidth()) {
                                Text(
                                    text = "${formatValue(amount)} $fromCurrency =",
                                    fontSize = 14.sp,
                                    color = textSecondary,
                                    textAlign = TextAlign.Center
                                )
                                Spacer(modifier = Modifier.height(4.dp))
                                Text(
                                    text = "${formatValue(result)} $toCurrency",
                                    fontSize = 28.sp,
                                    fontWeight = FontWeight.Bold,
                                    color = textPrimary,
                                    textAlign = TextAlign.Center
                                )
                                // If sync failed but we are displaying cached values, show a non-intrusive warning tag
                                if (viewModel.isUsingOfflineRates) {
                                    Spacer(modifier = Modifier.height(6.dp))
                                    Text(
                                        text = "⚠️ Offline Rates (cached)",
                                        color = AccentTeal,
                                        fontSize = 12.sp,
                                        fontWeight = FontWeight.SemiBold,
                                        textAlign = TextAlign.Center
                                    )
                                }
                            }
                        }
                        
                        // Push result area higher above the keyboard when focused
                        Spacer(modifier = Modifier.height(8.dp))
                    }
                }
            }
        }

        Spacer(modifier = Modifier.height(24.dp))

        // 2. Other Unit Converters section
        UnitConverterCard(unitViewModel = unitViewModel)

        // Extra bottom spacing to allow scrolling past elements easily
        Spacer(modifier = Modifier.height(24.dp))
    }

    // Currency Picker Bottom Sheet
    if (showBottomSheet) {
        ModalBottomSheet(
            onDismissRequest = { showBottomSheet = false },
            containerColor = if (isDarkTheme) SlateDarkBgStart else SlateLightBgStart
        ) {
            SearchSheetContent(
                query = searchQuery,
                onQueryChange = { searchQuery = it },
                filteredRates = viewModel.getFilteredRates(searchQuery),
                onSelect = { rate ->
                    if (selectSource) {
                        fromCurrency = rate.code
                    } else {
                        toCurrency = rate.code
                    }
                    showBottomSheet = false
                },
                isDarkTheme = isDarkTheme
            )
        }
    }
}

@Composable
fun ThemeToggle(
    isDarkTheme: Boolean,
    onThemeChanged: (Boolean) -> Unit
) {
    val view = LocalView.current
    Row(
        modifier = Modifier
            .clip(RoundedCornerShape(20.dp))
            .background(if (isDarkTheme) BorderDark else BorderLight)
            .clickable {
                view.performHapticFeedback(HapticFeedbackConstants.VIRTUAL_KEY)
                onThemeChanged(!isDarkTheme)
            }
            .padding(4.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        // Sun Icon (Light Theme)
        Box(
            modifier = Modifier
                .size(32.dp)
                .clip(RoundedCornerShape(50))
                .background(if (!isDarkTheme) AccentAmber else Color.Transparent),
            contentAlignment = Alignment.Center
        ) {
            Icon(
                imageVector = Icons.Default.WbSunny,
                contentDescription = "Light Theme",
                tint = if (!isDarkTheme) TextLightPrimary else TextDarkTertiary,
                modifier = Modifier.size(18.dp)
            )
        }
        
        Spacer(modifier = Modifier.width(4.dp))
        
        // Moon Icon (Dark Theme)
        Box(
            modifier = Modifier
                .size(32.dp)
                .clip(RoundedCornerShape(50))
                .background(if (isDarkTheme) IndigoPrimary else Color.Transparent),
            contentAlignment = Alignment.Center
        ) {
            Icon(
                imageVector = Icons.Default.NightsStay,
                contentDescription = "Dark Theme",
                tint = if (isDarkTheme) TextDarkPrimary else TextLightTertiary,
                modifier = Modifier.size(18.dp)
            )
        }
    }
}
