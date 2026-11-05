package com.mlab.calculator.compose

import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.darkColorScheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.Color

// Colors extracted directly from demo69.png
val BackgroundDark = Color(0xFF1D232C)
val DisplayBackground = Color(0xFF1D232C)
val TextExpression = Color(0xFF9EABB8)
val TextResult = Color(0xFFFFFFFF)

// Keypad Button Colors
val ButtonNumberBg = Color(0xFFDEE2EA)
val ButtonNumberText = Color(0xFF464D5C)

val ButtonOperatorBg = Color(0xFF757B9C)
val ButtonOperatorText = Color(0xFFFFFFFF)

val ButtonEqualsBg = Color(0xFFFF6014)
val ButtonEqualsText = Color(0xFFFFFFFF)

// Utility Toolbar Colors
val ToolbarButtonBg = Color(0xFF28303C)
val ToolbarIconTint = Color(0xFFBAC5D4)

private val DarkColorScheme = darkColorScheme(
    primary = ButtonEqualsBg,
    background = BackgroundDark,
    surface = BackgroundDark,
    onBackground = TextResult,
    onSurface = TextResult
)

@Composable
fun CalculatorComposeTheme(content: @Composable () -> Unit) {
    MaterialTheme(
        colorScheme = DarkColorScheme,
        content = content
    )
}
