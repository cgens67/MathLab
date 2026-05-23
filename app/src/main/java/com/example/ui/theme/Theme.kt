package com.example.ui.theme

import android.os.Build
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.darkColorScheme
import androidx.compose.material3.dynamicDarkColorScheme
import androidx.compose.material3.dynamicLightColorScheme
import androidx.compose.material3.lightColorScheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import com.example.ui.settings.SettingsManager

private val DarkColorScheme =
  darkColorScheme(
    primary = PrimaryDark,
    onPrimary = OnPrimaryDark,
    primaryContainer = PrimaryContainerDark,
    onPrimaryContainer = OnPrimaryContainerDark,
    secondary = SecondaryDark,
    onSecondary = OnSecondaryDark,
    secondaryContainer = SecondaryContainerDark,
    onSecondaryContainer = OnSecondaryContainerDark,
    tertiary = TertiaryDark,
    onTertiary = OnTertiaryDark,
    tertiaryContainer = TertiaryContainerDark,
    onTertiaryContainer = OnTertiaryContainerDark,
    background = BackgroundDark,
    onBackground = OnBackgroundDark,
    surface = SurfaceDark,
    onSurface = OnSurfaceDark,
    surfaceVariant = SurfaceVariantDark,
    onSurfaceVariant = OnSurfaceVariantDark,
    outline = OutlineDark
  )

private val LightColorScheme =
  lightColorScheme(
    primary = PrimaryLight,
    onPrimary = OnPrimaryLight,
    primaryContainer = PrimaryContainerLight,
    onPrimaryContainer = OnPrimaryContainerLight,
    secondary = SecondaryLight,
    onSecondary = OnSecondaryLight,
    secondaryContainer = SecondaryContainerLight,
    onSecondaryContainer = OnSecondaryContainerLight,
    tertiary = TertiaryLight,
    onTertiary = OnTertiaryLight,
    tertiaryContainer = TertiaryContainerLight,
    onTertiaryContainer = OnTertiaryContainerLight,
    background = BackgroundLight,
    onBackground = OnBackgroundLight,
    surface = SurfaceLight,
    onSurface = OnSurfaceLight,
    surfaceVariant = SurfaceVariantLight,
    onSurfaceVariant = OnSurfaceVariantLight,
    outline = OutlineLight
  )

private val ForestEmeraldLightScheme = lightColorScheme(
    primary = ForestEmeraldPrimaryLight,
    onPrimary = ForestEmeraldOnPrimaryLight,
    primaryContainer = ForestEmeraldPrimaryContainerLight,
    onPrimaryContainer = ForestEmeraldOnPrimaryContainerLight,
    secondary = SecondaryLight,
    onSecondary = OnSecondaryLight,
    secondaryContainer = SecondaryContainerLight,
    onSecondaryContainer = OnSecondaryContainerLight,
    background = ForestEmeraldBackgroundLight,
    onBackground = OnBackgroundLight,
    surface = ForestEmeraldSurfaceLight,
    onSurface = OnBackgroundLight,
    surfaceVariant = SurfaceVariantLight,
    onSurfaceVariant = OnSurfaceVariantLight,
    outline = OutlineLight
)

private val ForestEmeraldDarkScheme = darkColorScheme(
    primary = ForestEmeraldPrimaryDark,
    onPrimary = ForestEmeraldOnPrimaryDark,
    primaryContainer = ForestEmeraldPrimaryContainerDark,
    onPrimaryContainer = ForestEmeraldOnPrimaryContainerDark,
    secondary = SecondaryDark,
    onSecondary = OnSecondaryDark,
    secondaryContainer = SecondaryContainerDark,
    onSecondaryContainer = OnSecondaryContainerDark,
    background = ForestEmeraldBackgroundDark,
    onBackground = OnBackgroundDark,
    surface = ForestEmeraldSurfaceDark,
    onSurface = OnBackgroundDark,
    surfaceVariant = SurfaceVariantDark,
    onSurfaceVariant = OnSurfaceVariantDark,
    outline = OutlineDark
)

private val RoyalOceanLightScheme = lightColorScheme(
    primary = RoyalOceanPrimaryLight,
    onPrimary = RoyalOceanOnPrimaryLight,
    primaryContainer = RoyalOceanPrimaryContainerLight,
    onPrimaryContainer = RoyalOceanOnPrimaryContainerLight,
    secondary = SecondaryLight,
    onSecondary = OnSecondaryLight,
    secondaryContainer = SecondaryContainerLight,
    onSecondaryContainer = OnSecondaryContainerLight,
    background = RoyalOceanBackgroundLight,
    onBackground = OnBackgroundLight,
    surface = RoyalOceanSurfaceLight,
    onSurface = OnBackgroundLight,
    surfaceVariant = SurfaceVariantLight,
    onSurfaceVariant = OnSurfaceVariantLight,
    outline = OutlineLight
)

private val RoyalOceanDarkScheme = darkColorScheme(
    primary = RoyalOceanPrimaryDark,
    onPrimary = RoyalOceanOnPrimaryDark,
    primaryContainer = RoyalOceanPrimaryContainerDark,
    onPrimaryContainer = RoyalOceanOnPrimaryContainerDark,
    secondary = SecondaryDark,
    onSecondary = OnSecondaryDark,
    secondaryContainer = SecondaryContainerDark,
    onSecondaryContainer = OnSecondaryContainerDark,
    background = RoyalOceanBackgroundDark,
    onBackground = OnBackgroundDark,
    surface = RoyalOceanSurfaceDark,
    onSurface = OnBackgroundDark,
    surfaceVariant = SurfaceVariantDark,
    onSurfaceVariant = OnSurfaceVariantDark,
    outline = OutlineDark
)

private val SunsetCopperLightScheme = lightColorScheme(
    primary = SunsetCopperPrimaryLight,
    onPrimary = SunsetCopperOnPrimaryLight,
    primaryContainer = SunsetCopperPrimaryContainerLight,
    onPrimaryContainer = SunsetCopperOnPrimaryContainerLight,
    secondary = SecondaryLight,
    onSecondary = OnSecondaryLight,
    secondaryContainer = SecondaryContainerLight,
    onSecondaryContainer = OnSecondaryContainerLight,
    background = SunsetCopperBackgroundLight,
    onBackground = OnBackgroundLight,
    surface = SunsetCopperSurfaceLight,
    onSurface = OnBackgroundLight,
    surfaceVariant = SurfaceVariantLight,
    onSurfaceVariant = OnSurfaceVariantLight,
    outline = OutlineLight
)

private val SunsetCopperDarkScheme = darkColorScheme(
    primary = SunsetCopperPrimaryDark,
    onPrimary = SunsetCopperOnPrimaryDark,
    primaryContainer = SunsetCopperPrimaryContainerDark,
    onPrimaryContainer = SunsetCopperOnPrimaryContainerDark,
    secondary = SecondaryDark,
    onSecondary = OnSecondaryDark,
    secondaryContainer = SecondaryContainerDark,
    onSecondaryContainer = OnSecondaryContainerDark,
    background = SunsetCopperBackgroundDark,
    onBackground = OnBackgroundDark,
    surface = SunsetCopperSurfaceDark,
    onSurface = OnBackgroundDark,
    surfaceVariant = SurfaceVariantDark,
    onSurfaceVariant = OnSurfaceVariantDark,
    outline = OutlineDark
)

fun getScaledTypography(scale: Float): androidx.compose.material3.Typography {
    val base = Typography
    if (scale == 1.0f) return base
    fun scaleStyle(style: androidx.compose.ui.text.TextStyle) = if (style.fontSize == androidx.compose.ui.unit.TextUnit.Unspecified) style else style.copy(
        fontSize = style.fontSize * scale,
        lineHeight = if (style.lineHeight == androidx.compose.ui.unit.TextUnit.Unspecified) style.lineHeight else style.lineHeight * scale
    )
    return androidx.compose.material3.Typography(
        displayLarge = scaleStyle(base.displayLarge),
        displayMedium = scaleStyle(base.displayMedium),
        displaySmall = scaleStyle(base.displaySmall),
        headlineLarge = scaleStyle(base.headlineLarge),
        headlineMedium = scaleStyle(base.headlineMedium),
        headlineSmall = scaleStyle(base.headlineSmall),
        titleLarge = scaleStyle(base.titleLarge),
        titleMedium = scaleStyle(base.titleMedium),
        titleSmall = scaleStyle(base.titleSmall),
        bodyLarge = scaleStyle(base.bodyLarge),
        bodyMedium = scaleStyle(base.bodyMedium),
        bodySmall = scaleStyle(base.bodySmall),
        labelLarge = scaleStyle(base.labelLarge),
        labelMedium = scaleStyle(base.labelMedium),
        labelSmall = scaleStyle(base.labelSmall)
    )
}

fun getCustomShapes(roundedness: String): androidx.compose.material3.Shapes {
    return when (roundedness) {
        "Classic Brutalist" -> androidx.compose.material3.Shapes(
            small = RoundedCornerShape(0.dp),
            medium = RoundedCornerShape(0.dp),
            large = RoundedCornerShape(0.dp)
        )
        "Organic Fluid" -> androidx.compose.material3.Shapes(
            small = RoundedCornerShape(16.dp),
            medium = RoundedCornerShape(24.dp),
            large = RoundedCornerShape(36.dp)
        )
        else -> androidx.compose.material3.Shapes( // "Modern Balanced"
            small = RoundedCornerShape(8.dp),
            medium = RoundedCornerShape(16.dp),
            large = RoundedCornerShape(24.dp)
        )
    }
}

@Composable
fun MyApplicationTheme(
  darkTheme: Boolean = isSystemInDarkTheme(),
  dynamicColor: Boolean = true,
  themePreset: String = "Lavender",
  fontSizeScale: Float = 1.0f,
  shapeRoundedness: String = "Modern Balanced",
  content: @Composable () -> Unit,
) {
  val colorScheme =
    when {
      dynamicColor && Build.VERSION.SDK_INT >= Build.VERSION_CODES.S -> {
        val context = LocalContext.current
        if (darkTheme) dynamicDarkColorScheme(context) else dynamicLightColorScheme(context)
      }

      else -> {
          if (darkTheme) {
              when (themePreset) {
                  "Forest Emerald" -> ForestEmeraldDarkScheme
                  "Royal Ocean" -> RoyalOceanDarkScheme
                  "Sunset Copper" -> SunsetCopperDarkScheme
                  else -> DarkColorScheme
              }
          } else {
              when (themePreset) {
                  "Forest Emerald" -> ForestEmeraldLightScheme
                  "Royal Ocean" -> RoyalOceanLightScheme
                  "Sunset Copper" -> SunsetCopperLightScheme
                  else -> LightColorScheme
              }
          }
      }
    }

  val scaledTypography = getScaledTypography(fontSizeScale)
  val customShapes = getCustomShapes(shapeRoundedness)

  MaterialTheme(
      colorScheme = colorScheme,
      typography = scaledTypography,
      shapes = customShapes,
      content = content
  )
}

@Composable
fun appRoundedCornerShape(
    size: androidx.compose.ui.unit.Dp = 0.dp,
    topStart: androidx.compose.ui.unit.Dp = size,
    topEnd: androidx.compose.ui.unit.Dp = size,
    bottomEnd: androidx.compose.ui.unit.Dp = size,
    bottomStart: androidx.compose.ui.unit.Dp = size
): RoundedCornerShape {
    val roundedness by SettingsManager.shapeRoundedness.collectAsState()
    val multiplier = when (roundedness) {
        "Classic Brutalist" -> 0f
        "Organic Fluid" -> 1.5f
        else -> 1f
    }
    return if (size != 0.dp && topStart == size && topEnd == size && bottomEnd == size && bottomStart == size) {
        RoundedCornerShape(size * multiplier)
    } else {
        RoundedCornerShape(
            topStart = topStart * multiplier,
            topEnd = topEnd * multiplier,
            bottomEnd = bottomEnd * multiplier,
            bottomStart = bottomStart * multiplier
        )
    }
}

