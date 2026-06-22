package com.example.unitconverter.ui

import androidx.compose.animation.*
import androidx.compose.animation.core.*
import androidx.compose.foundation.*
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.KeyboardArrowDown
import androidx.compose.material.icons.filled.Refresh
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.rotate
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.unitconverter.data.CurrencyViewModel
import com.example.unitconverter.ui.components.SearchSheetContent
import com.example.unitconverter.ui.components.UnitConverterCard
import com.example.unitconverter.ui.theme.*

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ConverterApp(viewModel: CurrencyViewModel) {
    val scrollState = rememberScrollState()
    
    // States for Currency conversion
    var amountInput by remember { mutableStateOf("1.0") }
    var fromCurrency by remember { mutableStateOf("USD") }
    var toCurrency by remember { mutableStateOf("EUR") }
    
    // Bottom sheet controls
    var showBottomSheet by remember { mutableStateOf(false) }
    var selectSource by remember { mutableStateOf(true) } // true for "From", false for "To"
    var searchQuery by remember { mutableStateOf("") }
    
    // Rotate state for swap button
    var rotationAngle by remember { mutableStateOf(0f) }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .verticalScroll(scrollState)
            .padding(20.dp)
            .navigationBarsPadding()
            .statusBarsPadding(),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        // App Title Header
        Text(
            text = "OmniConverter",
            fontSize = 32.sp,
            fontWeight = FontWeight.Bold,
            color = TextPrimary,
            textAlign = TextAlign.Center,
            modifier = Modifier.padding(top = 10.dp, bottom = 4.dp)
        )
        Text(
            text = "Beautiful converter with offline support",
            fontSize = 14.sp,
            color = TextSecondary,
            textAlign = TextAlign.Center,
            modifier = Modifier.padding(bottom = 24.dp)
        )

        // 1. Currency Exchange Card
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
                        color = TextPrimary
                    )
                    IconButton(
                        onClick = { viewModel.fetchRates() },
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
                            text = viewModel.errorMessage ?: "Failed to sync exchange rates. Please check your internet connection and try again.",
                            color = TextSecondary,
                            fontSize = 13.sp,
                            textAlign = TextAlign.Center,
                            modifier = Modifier.padding(bottom = 20.dp, start = 16.dp, end = 16.dp)
                        )
                        Button(
                            onClick = { viewModel.fetchRates() },
                            colors = ButtonDefaults.buttonColors(containerColor = IndigoPrimary)
                        ) {
                            Text("Try Again", color = TextPrimary)
                        }
                    }
                } else {
                    // Normal state: Input + Selectors + Result
                    
                    // Amount Input
                    OutlinedTextField(
                        value = amountInput,
                        onValueChange = { amountInput = it },
                        label = { Text("Amount", color = TextSecondary) },
                        keyboardOptions = KeyboardOptions(
                            keyboardType = KeyboardType.Number,
                            imeAction = ImeAction.Done
                        ),
                        textStyle = LocalTextStyle.current.copy(color = TextPrimary, fontSize = 18.sp),
                        colors = OutlinedTextFieldDefaults.colors(
                            focusedBorderColor = IndigoPrimary,
                            unfocusedBorderColor = CardBorder,
                            focusedLabelColor = IndigoPrimary
                        ),
                        modifier = Modifier.fillMaxWidth(),
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
                                .clickable {
                                    selectSource = true
                                    searchQuery = ""
                                    showBottomSheet = true
                                }
                                .background(CardBorder, RoundedCornerShape(12.dp))
                                .padding(12.dp)
                        ) {
                            Row(
                                horizontalArrangement = Arrangement.SpaceBetween,
                                verticalAlignment = Alignment.CenterVertically,
                                modifier = Modifier.fillMaxWidth()
                            ) {
                                Column {
                                    Text("From", fontSize = 11.sp, color = TextSecondary)
                                    Text(fromCurrency, fontSize = 18.sp, fontWeight = FontWeight.Bold, color = TextPrimary)
                                }
                                Icon(Icons.Default.KeyboardArrowDown, contentDescription = null, tint = TextSecondary)
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
                                .background(IndigoPrimary, RoundedCornerShape(50))
                                .clickable {
                                    rotationAngle += 180f
                                    val temp = fromCurrency
                                    fromCurrency = toCurrency
                                    toCurrency = temp
                                },
                            contentAlignment = Alignment.Center
                        ) {
                            Text(
                                text = "⇄",
                                color = TextPrimary,
                                fontSize = 20.sp,
                                fontWeight = FontWeight.Bold,
                                textAlign = TextAlign.Center
                            )
                        }

                        // To Currency Select Box
                        Box(
                            modifier = Modifier
                                .weight(1f)
                                .clickable {
                                    selectSource = false
                                    searchQuery = ""
                                    showBottomSheet = true
                                }
                                .background(CardBorder, RoundedCornerShape(12.dp))
                                .padding(12.dp)
                        ) {
                            Row(
                                horizontalArrangement = Arrangement.SpaceBetween,
                                verticalAlignment = Alignment.CenterVertically,
                                modifier = Modifier.fillMaxWidth()
                            ) {
                                Column {
                                    Text("To", fontSize = 11.sp, color = TextSecondary)
                                    Text(toCurrency, fontSize = 18.sp, fontWeight = FontWeight.Bold, color = TextPrimary)
                                }
                                Icon(Icons.Default.KeyboardArrowDown, contentDescription = null, tint = TextSecondary)
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
                                    Brush.horizontalGradient(listOf(IndigoPrimary.copy(alpha = 0.1f), VioletSecondary.copy(alpha = 0.1f))),
                                    RoundedCornerShape(16.dp)
                                )
                                .padding(16.dp)
                        ) {
                            Column(horizontalAlignment = Alignment.CenterHorizontally, modifier = Modifier.fillMaxWidth()) {
                                Text(
                                    text = String.format("%.2f %s = ", amount, fromCurrency),
                                    fontSize = 14.sp,
                                    color = TextSecondary
                                )
                                Text(
                                    text = String.format("%.4f %s", result, toCurrency),
                                    fontSize = 28.sp,
                                    fontWeight = FontWeight.Bold,
                                    color = TextPrimary
                                )
                                // If sync failed but we are displaying cached values, show a non-intrusive warning tag
                                if (viewModel.isUsingOfflineRates) {
                                    Spacer(modifier = Modifier.height(6.dp))
                                    Text(
                                        text = "⚠️ Offline Rates (cached)",
                                        color = AccentTeal,
                                        fontSize = 12.sp,
                                        fontWeight = FontWeight.SemiBold
                                    )
                                }
                            }
                        }
                    }
                }
            }
        }

        Spacer(modifier = Modifier.height(24.dp))

        // 2. Other Unit Converters section
        UnitConverterCard()
    }

    // Currency Picker Bottom Sheet
    if (showBottomSheet) {
        ModalBottomSheet(
            onDismissRequest = { showBottomSheet = false },
            containerColor = BottomSheetBg
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
                }
            )
        }
    }
}
