package org.d3ifcool.medisgosh.component

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import org.d3ifcool.medisgosh.ui.theme.AppGrayColor

class AppLabel {
    companion object {
        @Composable
        fun Default(
            modifier: Modifier = Modifier,
            label: String,
            useSpacer: Boolean = true,
            color: Color = AppGrayColor
        ) {
            Column(modifier = modifier.fillMaxWidth()) {
                AppText.Small15(
                    text = label,
                    fontWeight = FontWeight.SemiBold,
                    color = color
                )
                if (useSpacer) {
                    Spacer(Modifier.height(12.dp))
                }
            }
        }

        @Composable
        fun WithTextData(
            modifier: Modifier = Modifier,
            label: String,
            textData: String,
            useSpacer: Boolean = true,
            color: Color = AppGrayColor,
        ) {
            Column(modifier = modifier.fillMaxWidth()) {
                AppText.Small15(
                    text = label,
                    fontWeight = FontWeight.SemiBold,
                    color = color
                )
                Spacer(Modifier.height(4.dp))
                AppText.Small15(
                    text = textData,
                    color = color,
                )
                if (useSpacer) {
                    Spacer(Modifier.height(12.dp))
                }
            }
        }
    }
}