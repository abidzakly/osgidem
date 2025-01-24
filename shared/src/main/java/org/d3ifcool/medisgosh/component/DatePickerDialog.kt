package org.d3ifcool.medisgosh.component

import android.os.Build
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.Dialog
import org.d3ifcool.medisgosh.ui.theme.AppBlue2Color
import java.time.LocalDate


@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun DatePickerDialog(
    onDismissRequest: () -> Unit,
    onDateSelected: (LocalDate) -> Unit
) {
    Dialog(onDismissRequest = onDismissRequest) {
        Surface(shape = MaterialTheme.shapes.medium) {
            Column(modifier = Modifier.padding(16.dp)) {
                val state = rememberDatePickerState()

                DatePicker(
                    state = state
                )

                Spacer(modifier = Modifier.height(16.dp))

                Row(
                    horizontalArrangement = Arrangement.End,
                    modifier = Modifier.fillMaxWidth()
                ) {
                    TextButton(onClick = onDismissRequest) {
                        Text("Cancel")
                    }
                    TextButton(onClick = {
                        val selectedMillis = state.selectedDateMillis
                        if (selectedMillis != null) {
                            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                                onDateSelected(LocalDate.ofEpochDay(selectedMillis / 86_400_000L))
                            }
                        }
                    }) {
                        Text("OK")
                    }
                }
            }
        }
    }
}
