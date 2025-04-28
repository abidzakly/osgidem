package org.d3ifcool.medisgosh.util
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.launch
import org.d3ifcool.medisgosh.ui.theme.AppBrightRed
import org.d3ifcool.medisgosh.ui.theme.AppDanger
import org.d3ifcool.medisgosh.ui.theme.AppDarkToscaColor
import org.d3ifcool.medisgosh.ui.theme.AppGreenSuccess
import org.d3ifcool.medisgosh.ui.theme.AppWarning
import org.d3ifcool.medisgosh.ui.theme.Candal

class FlashMessageHelper {

    companion object {
        @Composable
        fun rememberSnackbarHostState(): SnackbarHostState {
            return remember { SnackbarHostState() }
        }

        @Composable
        fun FlashMessageHost(
            snackbarHostState: SnackbarHostState,
        ) {
            SnackbarHost(hostState = snackbarHostState) { data ->
                val backgroundColor = when (data.visuals.actionLabel) {
                    "error" -> AppDanger
                    "warning" -> AppWarning
                    else -> AppDarkToscaColor
                }

                Box(
                    modifier = Modifier
                        .fillMaxSize()
                        .offset(y = (50).dp),
                    contentAlignment = Alignment.TopCenter
                ) {
                    Snackbar(
                        modifier = Modifier
                            .padding(12.dp)
                            .clip(RoundedCornerShape(12.dp)),
                        containerColor = backgroundColor,
                        contentColor = Color.White,
                    ) {
                        Text(text = data.visuals.message, fontSize = 14.sp, color = Color.White, fontFamily = Candal)
                    }
                }
            }
        }

        fun showError(
            snackbarHostState: SnackbarHostState,
            coroutineScope: CoroutineScope,
            message: String,
            onDismiss: () -> Unit = {},
        ) {
            showSnackbar(snackbarHostState, coroutineScope, message, "error", onDismiss)
        }

        fun showWarning(
            snackbarHostState: SnackbarHostState,
            coroutineScope: CoroutineScope,
            message: String,
            onDismiss: () -> Unit = {},
        ) {
            showSnackbar(snackbarHostState, coroutineScope, message, "warning", onDismiss)
        }

        fun showSuccess(
            snackbarHostState: SnackbarHostState,
            coroutineScope: CoroutineScope,
            message: String,
            onDismiss: () -> Unit = {},
        ) {
            showSnackbar(snackbarHostState, coroutineScope, message, "success", onDismiss)
        }

        private fun showSnackbar(
            snackbarHostState: SnackbarHostState,
            coroutineScope: CoroutineScope,
            message: String,
            type: String,
            onDismiss: () -> Unit = {},
        ) {
            coroutineScope.launch {
                val result = snackbarHostState.showSnackbar(
                    message = message,
                    actionLabel = type,
                    duration = SnackbarDuration.Short
                )
                if (result == SnackbarResult.Dismissed || result == SnackbarResult.ActionPerformed) {
                    onDismiss()
                }
            }
        }
    }
}
