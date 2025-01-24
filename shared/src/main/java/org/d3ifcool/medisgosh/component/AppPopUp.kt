package org.d3ifcool.medisgosh.component

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.ButtonDefaults.buttonColors
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Snackbar
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.Dialog
import org.d3ifcool.medisgosh.ui.theme.AppDanger
import org.d3ifcool.medisgosh.ui.theme.AppDarkToscaColor
import org.d3ifcool.medisgosh.ui.theme.AppToscaColor
import org.d3ifcool.medisgosh.ui.theme.AppWarning
import org.d3ifcool.medisgosh.util.AppObjectState

class AppPopUp {
    companion object {
        @Composable
        fun CustomDialog(
            modifier: Modifier = Modifier,
            label: String,
            negativeLabel: String = "Batal",
            positiveLabel: String = "Ok",
            submissionStatus: AppObjectState,
            onDismissRequest: () -> Unit,
            onPositiveClicked: () -> Unit
        ) {
            Dialog(onDismissRequest = onDismissRequest) {
                Column(
                    modifier = modifier
                        .fillMaxWidth()
                        .padding(horizontal = 20.dp),
                    horizontalAlignment = Alignment.CenterHorizontally,
                    verticalArrangement = Arrangement.Center
                ) {
                    Box(
                        modifier = Modifier
                            .clip(RoundedCornerShape(10.dp))
                            .background(AppToscaColor),
                        contentAlignment = Alignment.Center
                    ) {
                        AppText.Small15(
                            modifier = Modifier.padding(15.dp),
                            text = label,
                            color = Color.White,
                            fontWeight = FontWeight.Bold,
                            textAlign = TextAlign.Center,
                        )
                    }
                    Spacer(Modifier.height(32.dp))
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.Center,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Button(
                            modifier = Modifier.width(115.dp),
                            onClick = onDismissRequest,
                            colors = buttonColors(containerColor = AppDanger),
                            elevation = ButtonDefaults.buttonElevation(
                                defaultElevation = 4.dp
                            )
                        ) {
                            if (submissionStatus == AppObjectState.LOADING) {
                                AppCircularLoading(useSpacer = false)
                            } else {
                                AppText.Small15(
                                    text = negativeLabel,
                                    color = Color.White,
                                    fontWeight = FontWeight.SemiBold
                                )
                            }
                        }
                        Spacer(Modifier.width(24.dp))
                        Button(
                            modifier = Modifier.width(115.dp),
                            onClick = onPositiveClicked,
                            colors = buttonColors(containerColor = AppToscaColor),
                            elevation = ButtonDefaults.buttonElevation(
                                defaultElevation = 4.dp
                            )
                        ) {
                            if (submissionStatus == AppObjectState.LOADING) {
                                AppCircularLoading(useSpacer = false)
                            } else {
                                AppText.Small15(
                                    text = positiveLabel,
                                    color = Color.White,
                                    fontWeight = FontWeight.SemiBold
                                )
                            }
                        }
                    }
                }
            }
        }
    }
}