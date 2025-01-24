package org.d3ifcool.medisgosh.ui.auth

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.text.ClickableText
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.AnnotatedString
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import org.d3ifcool.medisgosh.R
import org.d3ifcool.medisgosh.component.AppContainer
import org.d3ifcool.medisgosh.component.AppText
import org.d3ifcool.medisgosh.ui.theme.AppBlueColor
import org.d3ifcool.medisgosh.ui.theme.AppTypography

@Composable
fun SubmitTicketSuccess(modifier: Modifier = Modifier, onBackToSignIn: () -> Unit) {
    AppContainer.Default {
        Column(
            modifier = modifier
                .fillMaxSize()
                .padding(it)
                .padding(horizontal = 34.dp),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center
        ) {
            Image(
                painter = painterResource(R.drawable.app_logo_big),
                contentDescription = null,
            )
            Spacer(Modifier.height(56.dp))
            AppText.Regular16(
                modifier = Modifier.fillMaxWidth(),
                text = "Kami telah mengirimkan email dengan petunjuk selanjutnya ke alamat" +
                        " email yang sudah anda masukan sebelumnya." +
                        " Mohon periksa kotak masuk Anda.",
                textAlign = TextAlign.Center,
                color = Color.Black.copy(alpha = .6f)
            )
            Spacer(Modifier.height(26.dp))
            ClickableText(
                text = AnnotatedString(
                    text = "Back to Sign In",
                ),
                style = AppTypography.copy(fontSize = 15.sp, color = AppBlueColor)
            ) {
                onBackToSignIn()
            }
            Spacer(Modifier.height(26.dp))
            Image(
                modifier = Modifier.size(280.dp),
                painter = painterResource(R.drawable.doctor_holding_stuff),
                contentDescription = null,
            )
        }
    }
}