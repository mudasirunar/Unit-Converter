package com.example.unitconverter.ui.components

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.blur
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import com.example.unitconverter.ui.theme.*

@Composable
fun GradientBackground(isDarkTheme: Boolean, content: @Composable () -> Unit) {
    val bgStart = if (isDarkTheme) SlateDarkBgStart else SlateLightBgStart
    val bgEnd = if (isDarkTheme) SlateDarkBgEnd else SlateLightBgEnd
    val glowColor1 = IndigoPrimary
    val glowColor2 = AccentAmber
    val glowAlpha1 = if (isDarkTheme) 0.15f else 0.05f
    val glowAlpha2 = if (isDarkTheme) 0.1f else 0.03f

    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(
                Brush.verticalGradient(
                    colors = listOf(bgStart, bgEnd)
                )
            )
    ) {
        // Glowing background circles for visual depth
        Box(
            modifier = Modifier
                .size(350.dp)
                .align(Alignment.TopEnd)
                .offset(x = 120.dp, y = (-70).dp)
                .background(Brush.radialGradient(listOf(glowColor1.copy(alpha = glowAlpha1), Color.Transparent)))
                .blur(60.dp)
        )
        Box(
            modifier = Modifier
                .size(300.dp)
                .align(Alignment.BottomStart)
                .offset(x = (-100).dp, y = 120.dp)
                .background(Brush.radialGradient(listOf(glowColor2.copy(alpha = glowAlpha2), Color.Transparent)))
                .blur(60.dp)
        )

        content()
    }
}
