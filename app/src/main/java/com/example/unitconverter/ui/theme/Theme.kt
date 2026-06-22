package com.example.unitconverter.ui.theme

import android.app.Activity
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.darkColorScheme
import androidx.compose.material3.lightColorScheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.SideEffect
import androidx.compose.ui.graphics.toArgb
import androidx.compose.ui.platform.LocalView
import androidx.core.view.WindowCompat

private val DarkColorScheme = darkColorScheme(
    primary = IndigoPrimary,
    background = SlateDarkBgStart,
    surface = SlateDarkCard,
    onPrimary = TextDarkPrimary,
    onBackground = TextDarkPrimary,
    onSurface = TextDarkPrimary
)

private val LightColorScheme = lightColorScheme(
    primary = IndigoPrimary,
    background = SlateLightBgStart,
    surface = SlateLightCard,
    onPrimary = TextLightPrimary,
    onBackground = TextLightPrimary,
    onSurface = TextLightPrimary
)

@Composable
fun UnitConverterTheme(
    darkTheme: Boolean = isSystemInDarkTheme(),
    content: @Composable () -> Unit
) {
    val colorScheme = if (darkTheme) DarkColorScheme else LightColorScheme
    val view = LocalView.current
    if (!view.isInEditMode) {
        SideEffect {
            val window = (view.context as Activity).window
            val statusBarBgColor = if (darkTheme) SlateDarkBgStart else SlateLightBgStart
            window.statusBarColor = statusBarBgColor.toArgb()
            
            window.setBackgroundDrawable(android.graphics.drawable.ColorDrawable(statusBarBgColor.toArgb()))
            
            // Set light or dark status bar icons based on theme
            val insetsController = WindowCompat.getInsetsController(window, view)
            insetsController.isAppearanceLightStatusBars = !darkTheme
        }
    }

    MaterialTheme(
        colorScheme = colorScheme,
        typography = Typography,
        content = content
    )
}