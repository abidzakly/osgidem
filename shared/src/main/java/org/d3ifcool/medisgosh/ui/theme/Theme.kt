package org.d3ifcool.medisgosh.ui.theme

import android.os.Build
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.dynamicDarkColorScheme
import androidx.compose.material3.dynamicLightColorScheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.platform.LocalContext

@Composable
fun MedisGoShTheme(
    content: @Composable () -> Unit
) {
    MaterialTheme(
        typography = AppTypography2,
        content = content
    )
}