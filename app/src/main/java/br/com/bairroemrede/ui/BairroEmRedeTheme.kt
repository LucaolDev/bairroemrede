package br.com.bairroemrede.ui

import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.lightColorScheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.Color

private val BairroCores = lightColorScheme(
    primary = Color(0xFF176148),
    onPrimary = Color.White,
    primaryContainer = Color(0xFFC9EFDA),
    onPrimaryContainer = Color(0xFF10392A),
    secondary = Color(0xFF44655B),
    secondaryContainer = Color(0xFFDCECE4),
    onSecondaryContainer = Color(0xFF19372E),
    tertiary = Color(0xFF7E4C11),
    tertiaryContainer = Color(0xFFFFE6C3),
    onTertiaryContainer = Color(0xFF57310A),
    background = Color(0xFFF5F8F5),
    onBackground = Color(0xFF142821),
    surface = Color(0xFFF5F8F5),
    onSurface = Color(0xFF142821),
    surfaceVariant = Color(0xFFE3EBE5),
    onSurfaceVariant = Color(0xFF40554B),
    surfaceContainer = Color(0xFFEAF0EB),
    surfaceContainerLow = Color.White,
    surfaceContainerHigh = Color(0xFFE5EDE7),
    outline = Color(0xFF607468),
    outlineVariant = Color(0xFFC4D4C8),
    error = Color(0xFF9B2925)
)

@Composable
fun BairroEmRedeTheme(content: @Composable () -> Unit) {
    MaterialTheme(colorScheme = BairroCores, content = content)
}
