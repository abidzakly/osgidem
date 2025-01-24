package org.d3ifcool.medisgosh.component

import android.graphics.Bitmap
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults.buttonColors
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults.cardColors
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.RadioButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.asImageBitmap
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.Dialog
import org.d3ifcool.medisgosh.ui.theme.AppLightBlueColor
import org.d3ifcool.medisgosh.ui.theme.AppToscaColor

@Composable
fun ImageDialog(
    bitmap: Bitmap?,
    onDismissRequest: () -> Unit,
    onConfirmation: () -> Unit
) {
    Dialog(onDismissRequest = { onDismissRequest() }) {
        Box(
            modifier = Modifier
                .background(
                    Brush.verticalGradient(
                        colors = listOf(
                            AppLightBlueColor,
                            Color.White,
                        )
                    ),
                )
                .clip(shape = RoundedCornerShape(16.dp))
        ) {
            Card(
                modifier = Modifier.padding(16.dp),
                colors = cardColors(containerColor = Color.Transparent)
            ) {
                Column(
                    modifier = Modifier.padding(16.dp),
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    AppText.Large24(
                        modifier = Modifier.fillMaxWidth(),
                        text = "Hasil Foto",
                        fontWeight = FontWeight.SemiBold,
                        color = Color.Black,
                    )
                    Spacer(Modifier.height(10.dp))
                    Image(
                        bitmap = bitmap!!.asImageBitmap(),
                        contentDescription = null,
                        modifier = Modifier
                            .fillMaxWidth()
                            .clip(shape = RoundedCornerShape(20.dp))
                            .aspectRatio(1f)
                    )
                    Spacer(Modifier.height(10.dp))

                    Row(
                        modifier = Modifier
                            .fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically

                    ) {
                        Button(
                            modifier = Modifier.width(115.dp),
                            onClick = { onDismissRequest() },
                            colors = buttonColors(
                                containerColor = Color.Red
                            )
                        ) {
                            AppText.Regular16(
                                text = "Batal",
                                color = Color.White,
                                fontWeight = FontWeight.SemiBold
                            )
                        }
                        Button(
                            modifier = Modifier.width(115.dp),
                            onClick = { onConfirmation() },
                            colors = buttonColors(
                                containerColor = AppToscaColor
                            )
                        ) {
                            AppText.Regular16(
                                text = "Simpan",
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

@Composable
fun RadiOptions(label: String, isSelected: Boolean, modifier: Modifier) {
    Row(
        modifier = modifier,
        verticalAlignment = Alignment.CenterVertically
    ) {
        RadioButton(selected = isSelected, onClick = null)
        Text(
            text = label,
            style = MaterialTheme.typography.bodyLarge,
            modifier = Modifier.padding(start = 8.dp)
        )
    }
}