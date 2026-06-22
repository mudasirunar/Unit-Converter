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
fun GradientBackground(content: @Composable () -> Unit) {
    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(
                Brush.verticalGradient(
                    colors = listOf(DarkBgStart, DarkBgEnd)
                )
            )
    ) {
        // Decorative glowing background circles for premium visual depth
        Box(
            modifier = Modifier
                .size(300.dp)
                .align(Alignment.TopEnd)
                .offset(x = 100.dp, y = (-50).dp)
                .background(Brush.radialGradient(listOf(IndigoPrimary.copy(alpha = 0.2f), Color.Transparent)))
                .blur(50.dp)
        )
        Box(
            modifier = Modifier
                .size(300.dp)
                .align(Alignment.BottomStart)
                .offset(x = (-100).dp, y = 100.dp)
                .background(Brush.radialGradient(listOf(VioletSecondary.copy(alpha = 0.15f), Color.Transparent)))
                .blur(50.dp)
        )

        content()
    }
}
