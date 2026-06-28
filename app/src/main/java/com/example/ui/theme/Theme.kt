package com.example.ui.theme

import androidx.compose.runtime.Composable
import androidx.tv.material3.MaterialTheme as TvMaterialTheme
import androidx.tv.material3.darkColorScheme as tvDarkColorScheme
import androidx.tv.material3.Typography as TvTypography
import androidx.compose.material3.MaterialTheme as ComposeMaterialTheme
import androidx.compose.material3.darkColorScheme as composeDarkColorScheme
import androidx.compose.material3.Typography as ComposeTypography
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.sp

private val TvDarkColorScheme = tvDarkColorScheme(
    primary = TvPrimary,
    onPrimary = TvOnPrimary,
    primaryContainer = TvPrimaryContainer,
    onPrimaryContainer = TvOnPrimaryContainer,
    secondary = TvTextSecondary,
    onSecondary = TvBackground,
    background = TvBackground,
    surface = TvSurface,
    onBackground = TvTextPrimary,
    onSurface = TvTextPrimary,
    surfaceVariant = TvSurface,
    onSurfaceVariant = TvTextMuted,
    border = TvBorder
)

private val ComposeDarkColorScheme = composeDarkColorScheme(
    primary = TvPrimary,
    onPrimary = TvOnPrimary,
    primaryContainer = TvPrimaryContainer,
    onPrimaryContainer = TvOnPrimaryContainer,
    secondary = TvTextSecondary,
    onSecondary = TvBackground,
    background = TvBackground,
    surface = TvSurface,
    onBackground = TvTextPrimary,
    onSurface = TvTextPrimary,
    surfaceVariant = TvSurface,
    onSurfaceVariant = TvTextMuted,
    outline = TvBorder
)

val TvTypographyData = TvTypography(
    displayMedium = TextStyle(
        fontFamily = FontFamily.Serif,
        fontWeight = FontWeight.Bold,
        fontSize = 45.sp,
        lineHeight = 52.sp,
        letterSpacing = 0.sp
    ),
    bodyMedium = TextStyle(
        fontFamily = FontFamily.SansSerif,
        fontWeight = FontWeight.Normal,
        fontSize = 14.sp,
        lineHeight = 20.sp,
        letterSpacing = 0.25.sp
    ),
    labelMedium = TextStyle(
        fontFamily = FontFamily.SansSerif,
        fontWeight = FontWeight.Medium,
        fontSize = 12.sp,
        lineHeight = 16.sp,
        letterSpacing = 0.5.sp
    )
)

val ComposeTypographyData = ComposeTypography(
    displayMedium = TextStyle(
        fontFamily = FontFamily.Serif,
        fontWeight = FontWeight.Bold,
        fontSize = 45.sp,
        lineHeight = 52.sp,
        letterSpacing = 0.sp
    ),
    bodyMedium = TextStyle(
        fontFamily = FontFamily.SansSerif,
        fontWeight = FontWeight.Normal,
        fontSize = 14.sp,
        lineHeight = 20.sp,
        letterSpacing = 0.25.sp
    ),
    labelMedium = TextStyle(
        fontFamily = FontFamily.SansSerif,
        fontWeight = FontWeight.Medium,
        fontSize = 12.sp,
        lineHeight = 16.sp,
        letterSpacing = 0.5.sp
    )
)

@Composable
fun StreamSixTheme(
  content: @Composable () -> Unit,
) {
  ComposeMaterialTheme(colorScheme = ComposeDarkColorScheme, typography = ComposeTypographyData) {
      TvMaterialTheme(colorScheme = TvDarkColorScheme, typography = TvTypographyData, content = content)
  }
}
