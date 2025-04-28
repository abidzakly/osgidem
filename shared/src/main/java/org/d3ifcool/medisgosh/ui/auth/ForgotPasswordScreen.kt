package org.d3ifcool.medisgosh.ui.auth

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.ClickableText
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Mail
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults.buttonColors
import androidx.compose.material3.ButtonDefaults.buttonElevation
import androidx.compose.material3.Icon
import androidx.compose.material3.OutlinedTextFieldDefaults
import androidx.compose.material3.VerticalDivider
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.AnnotatedString
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import org.d3ifcool.medisgosh.R
import org.d3ifcool.medisgosh.component.AppCircularLoading
import org.d3ifcool.medisgosh.component.AppContainer
import org.d3ifcool.medisgosh.component.AppInput
import org.d3ifcool.medisgosh.component.AppText
import org.d3ifcool.medisgosh.ui.theme.AppBlueColor
import org.d3ifcool.medisgosh.ui.theme.AppDarkToscaColor
import org.d3ifcool.medisgosh.ui.theme.AppTypography
import org.d3ifcool.medisgosh.util.AppHelper
import org.d3ifcool.medisgosh.util.FlashMessageHelper
import org.d3ifcool.medisgosh.util.FlashMessageHelper.Companion.rememberSnackbarHostState
import org.d3ifcool.medisgosh.util.ResponseStatus

@Composable
fun ForgotPasswordScreen(
    modifier: Modifier = Modifier,
    isEmployee: Boolean,
    viewModel: ForgotPasswordViewModel,
    onBackToSignUp: () -> Unit = {},
    onBackToSignIn: () -> Unit,
    onSubmitSuccess: () -> Unit
) {
    val context = LocalContext.current
    val status by viewModel.status

    var email by remember { mutableStateOf("") }
    var emailError by remember { mutableStateOf(false) }

    val snackbarHostState = rememberSnackbarHostState()
    val coroutineScope = rememberCoroutineScope()

    LaunchedEffect(status) {
        when (status) {
            ResponseStatus.SUCCESS -> {
                FlashMessageHelper.showSuccess(
                    snackbarHostState, coroutineScope,
                    status.message ?: "-"
                )
                delay(2000)
                viewModel.resetStatus()
                onSubmitSuccess()
            }

            ResponseStatus.FAILED -> {
                FlashMessageHelper.showError(
                    snackbarHostState, coroutineScope,
                    status.message ?: "-"
                )
                viewModel.resetStatus()
            }

            else -> null
        }
    }

    LaunchedEffect(Unit) {
        FlashMessageHelper.showSuccess(
            snackbarHostState, coroutineScope,
            "Menggunakan format @gmail.com"
        )
    }

    AppContainer.Default(
        snackbarHost = {
            FlashMessageHelper.FlashMessageHost(
                snackbarHostState,
            )
        },
    ) { innerPadding ->
        Column(
            modifier = modifier
                .fillMaxSize()
                .padding(innerPadding)
                .padding(horizontal = 54.dp),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center
        ) {
            Image(
                painter = painterResource(R.drawable.app_logo_big),
                contentDescription = null,
            )
            Spacer(Modifier.height(56.dp))
            AppInput.Default(
                testTag = "emailField",
                isError = emailError,
                value = email,
                leadingIcon = {
                    Icon(
                        imageVector = Icons.Default.Mail,
                        contentDescription = null,
                        tint = Color.Gray
                    )
                },
                placeHolder = "Email",
                placeHolderColor = Color.Gray.copy(alpha = .8f),
                colors = OutlinedTextFieldDefaults.colors(
                    focusedBorderColor = AppBlueColor,
                    unfocusedBorderColor = AppBlueColor
                ),
                keyboardType = KeyboardType.Email
            ) {
                email = it
                emailError = false
            }
            Spacer(Modifier.height(16.dp))
            Button(
                modifier = Modifier.fillMaxWidth(),
                onClick = {
                    if (email.isEmpty()) {
                        coroutineScope.launch {
                            FlashMessageHelper.showWarning(
                                snackbarHostState, coroutineScope,
                                "Pastikan anda telah mengisi data yang diperlukan."
                            )
                        }
                        emailError = true
                    } else if (!email.contains("@") || !email.contains(".")) {
                        coroutineScope.launch {
                            FlashMessageHelper.showWarning(
                                snackbarHostState, coroutineScope,
                                "Pastikan format email sudah benar."
                            )
                        }
                        emailError = true
                    } else {
                        if (!AppHelper.isInternetAvailable(context)) {
                            coroutineScope.launch {
                                FlashMessageHelper.showWarning(
                                    snackbarHostState, coroutineScope,
                                    "Internet tidak tersedia."
                                )
                            }
                        } else {
                            emailError = false
                            viewModel.submitEmail(email)
                        }
                    }
                },
                colors = buttonColors(containerColor = AppDarkToscaColor),
                shape = RoundedCornerShape(8.dp),
                elevation = buttonElevation(defaultElevation = 5.dp),
                contentPadding = PaddingValues(vertical = 10.dp)
            ) {
                if (status == ResponseStatus.LOADING) {
                    AppCircularLoading()
                }
                AppText.Small15(
                    text = "Reset Password",
                    color = Color.White,
                    fontWeight = FontWeight.SemiBold
                )
            }
            Spacer(Modifier.height(10.dp))
            if (!isEmployee) {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.Center,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    ClickableText(
                        text = AnnotatedString(
                            text = "Back to Sign In",
                        ),
                        style = AppTypography.copy(fontSize = 15.sp, color = AppBlueColor)
                    ) {
                        onBackToSignIn()
                    }
                    Spacer(Modifier.width(8.dp))
                    VerticalDivider(
                        modifier = Modifier.height(16.dp),
                        thickness = 2.dp,
                        color = Color.Black
                    )
                    Spacer(Modifier.width(8.dp))
                    ClickableText(
                        text = AnnotatedString(
                            text = "Back to Sign Up",
                        ),
                        style = AppTypography.copy(fontSize = 15.sp, color = AppBlueColor)
                    ) {
                        onBackToSignUp()
                    }
                }
            } else {
                ClickableText(
                    modifier = Modifier.fillMaxWidth(),
                    text = AnnotatedString(
                        text = "Back to Sign In",
                    ),
                    style = AppTypography.copy(fontSize = 15.sp, color = AppBlueColor)
                ) {
                    onBackToSignIn()
                }
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