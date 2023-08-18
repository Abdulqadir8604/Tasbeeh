package com.lamaq.tasbeeh.ui.theme

import android.app.Activity
import android.os.Build
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.material.ripple.RippleAlpha
import androidx.compose.material.ripple.RippleTheme
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.darkColorScheme
import androidx.compose.material3.dynamicDarkColorScheme
import androidx.compose.material3.dynamicLightColorScheme
import androidx.compose.material3.lightColorScheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.SideEffect
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.toArgb
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalView
import androidx.core.view.WindowCompat

val DarkColorScheme = darkColorScheme(
    surface = Color(0xFFFDEAC9),
    background = Color(0xFFFDEAC9),
    primary = Color(0xFFFCD697),
    secondary = Color(0xFF945D3D),
    tertiary = Color(0xFFDB1A34),
    onTertiary = Color.White,
    primaryContainer = Color(0xA6FCD697), //for +1 button box
    onPrimaryContainer = Color(0x81945D3D), //for +1 button text
    inversePrimary = Color(0xFFBE7348), //for textField label
)

val LightColorScheme = lightColorScheme(
    surface = Color(0xFFFDEAC9),
    background = Color(0xFFFDEAC9),
    primary = Color(0xFFFCD697),
    secondary = Color(0xFF945D3D),
    tertiary = Color(0xFFDB1A34),
    onTertiary = Color.White,
    primaryContainer = Color(0xA6FCD697), //for +1 button box
    onPrimaryContainer = Color(0x81945D3D), //for +1 button text
    inversePrimary = Color(0xFFBE7348), //for textField label

    /* Other default colors to override
    onPrimary = Color.White,
    onSecondary = Color.White,

    onBackground = Color(0xFF1C1B1F),
    onSurface = Color(0xFF1C1B1F),
    */
)

@Composable
fun TasbeehTheme(
    darkTheme: Boolean = isSystemInDarkTheme(),
    // Dynamic color is available on Android 12+
    dynamicColor: Boolean = true,
    content: @Composable () -> Unit
) {
    val colorScheme = when {
        dynamicColor && Build.VERSION.SDK_INT >= Build.VERSION_CODES.S -> {
            val context = LocalContext.current
            if (darkTheme) dynamicDarkColorScheme(context) else dynamicLightColorScheme(context)
        }

        darkTheme -> DarkColorScheme
        else -> LightColorScheme
    }
    val view = LocalView.current
    if (!view.isInEditMode) {
        SideEffect {
            val window = (view.context as Activity).window
            window.statusBarColor =
                Color.Transparent.toArgb()
            WindowCompat.getInsetsController(window, view).isAppearanceLightStatusBars = if (darkTheme) darkTheme else !darkTheme
        }
    }

    MaterialTheme(
        colorScheme = colorScheme,
        typography = Typography,
        content = content,
    )
}

object TasbeehRippleTheme: RippleTheme {
    @Composable
    override fun defaultColor(): Color {
        return Color(0xFF945D3D)
    }

    @Composable
    override fun rippleAlpha(): RippleAlpha {
        return RippleAlpha(0.2f, 0.14f, 0.12f, 0.3f)
    }

}