package org.d3ifcool.medisgosh.component

import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp

@Composable
fun AppCircularLoading(color: Color = Color.White, size: Dp = 20.dp, useSpacer: Boolean = true) {
    CircularProgressIndicator(
        modifier = Modifier.size(size),
        color = color,
    )
    if (useSpacer) {
        Spacer(Modifier.width(12.dp))
    }
}