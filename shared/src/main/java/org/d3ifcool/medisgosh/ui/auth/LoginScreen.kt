package org.d3ifcool.medisgosh.ui.auth

import android.app.Activity
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.ClickableText
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Lock
import androidx.compose.material.icons.filled.Mail
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults.buttonColors
import androidx.compose.material3.ButtonDefaults.buttonElevation
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.OutlinedTextFieldDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
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
import androidx.lifecycle.viewmodel.compose.viewModel
import com.firebase.ui.auth.FirebaseAuthUIActivityResultContract
import org.d3ifcool.medisgosh.R
import org.d3ifcool.medisgosh.component.AppCircularLoading
import org.d3ifcool.medisgosh.component.AppContainer
import org.d3ifcool.medisgosh.component.AppInput
import org.d3ifcool.medisgosh.component.AppText
import org.d3ifcool.medisgosh.ui.theme.AppBlueColor
import org.d3ifcool.medisgosh.ui.theme.AppToscaColor
import org.d3ifcool.medisgosh.ui.theme.AppTypography
import org.d3ifcool.medisgosh.util.AppHelper
import org.d3ifcool.medisgosh.util.AppObjectState

@Composable
fun LoginScreen(
    modifier: Modifier = Modifier,
    isEmployee: Boolean,
    viewModel: LoginViewModel,
    onSignUp: () -> Unit = {},
    onForgotPassword: () -> Unit,
) {
    val context = LocalContext.current
    val status by viewModel.status

    var email by remember { mutableStateOf("") }
    var password by remember { mutableStateOf("") }

    var emailError by remember { mutableStateOf(false) }
    var passwordError by remember { mutableStateOf(false) }

    LaunchedEffect(status) {
        when (status) {
            AppObjectState.SUCCESS -> {
                AppHelper.toastGenerator(context, strMessage = status.message)
            }
            else -> AppHelper.toastGenerator(context, strMessage = status.message)
        }
    }

    AppContainer.Default(
        modifier = modifier
    ) { innerPadding ->
        Column(
            modifier = Modifier
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
            Spacer(Modifier.height(8.dp))
            AppInput.Default(
                testTag = "passwordField",
                isError = passwordError,
                isPassword = true,
                value = password,
                leadingIcon = {
                    Icon(
                        imageVector = Icons.Default.Lock,
                        contentDescription = null,
                        tint = Color.Gray
                    )
                },
                placeHolder = "Password",
                placeHolderColor = Color.Gray.copy(alpha = .8f),
                colors = OutlinedTextFieldDefaults.colors(
                    focusedBorderColor = AppBlueColor,
                    unfocusedBorderColor = AppBlueColor
                )
            ) {
                password = it
                passwordError = false
            }
            Spacer(Modifier.height(32.dp))
            Button(
                modifier = Modifier.fillMaxWidth(),
                onClick = {
                    if (email.isEmpty() || password.isEmpty()) {
                        AppHelper.toastGenerator(
                            context,
                            strMessage = "Pastikan seluruh data telah diisi."
                        )
                        emailError = true
                        passwordError = true
                    } else if (!email.contains("@") || !email.contains(".")) {
                        AppHelper.toastGenerator(
                            context,
                            strMessage = "Pastikan format email sudah benar."
                        )
                        emailError = true
                    } else {
                        if (!AppHelper.isInternetAvailable(context)) {
                            AppHelper.toastGenerator(
                                context,
                                strMessage = "Internet tidak tersedia."
                            )
                        } else {
                            emailError = false
                            passwordError = false
                            viewModel.signIn(email, password)
                        }
                    }
                },
                colors = buttonColors(containerColor = AppToscaColor),
                shape = RoundedCornerShape(8.dp),
                elevation = buttonElevation(defaultElevation = 5.dp)
            ) {
                if (status == AppObjectState.LOADING) {
                    AppCircularLoading()
                }
                AppText.Small15(
                    text = "Sign In",
                    color = Color.White,
                    fontWeight = FontWeight.SemiBold
                )
            }
            Spacer(Modifier.height(10.dp))
            ClickableText(
                modifier = Modifier.fillMaxWidth(),
                text = AnnotatedString(
                    text = "Forgot Password",
                ),
                style = AppTypography.copy(fontSize = 15.sp, color = AppBlueColor)
            ) {
                onForgotPassword()
            }
            Spacer(Modifier.height(29.dp))
            if (!isEmployee) {
                val contract = FirebaseAuthUIActivityResultContract()
                val launcher = rememberLauncherForActivityResult(contract) { result ->
                    if (result.resultCode == Activity.RESULT_OK) {
                        // Firebase Authentication successful
                        AppHelper.toastGenerator(context, strMessage = "Login Berhasil")
                    } else {
                        // Handle sign-in failure
                        AppHelper.toastGenerator(context, strMessage = "Login Gagal.")
                    }
                }
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.SpaceBetween
                ) {
                    HorizontalDivider(
                        modifier = Modifier.fillMaxWidth(.4f),
                        thickness = 2.dp,
                        color = Color.Gray.copy(alpha = .5f)
                    )
                    AppText.Small12(
                        text = "Sign in with",
                        fontWeight = FontWeight.SemiBold,
                        color = Color.Black.copy(alpha = .4f)
                    )
                    HorizontalDivider(
                        thickness = 2.dp,
                        color = Color.Gray.copy(alpha = .5f)
                    )
                }
                Spacer(Modifier.height(29.dp))
                OutlinedButton(
                    modifier = Modifier.fillMaxWidth(),
                    onClick = {
                        launcher.launch(viewModel.getSignInIntent())
                    },
                    border = BorderStroke(width = 2.dp, color = Color.White)
                ) {
                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(vertical = 8.dp),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Icon(
                            painter = painterResource(R.drawable.google),
                            contentDescription = null,
                            tint = Color.Unspecified
                        )
                        AppText.Small15(
                            text = "Google",
                            color = Color.Black.copy(alpha = .5f),
                            fontWeight = FontWeight.SemiBold
                        )
                        Spacer(Modifier)
                    }
                }
                Spacer(Modifier.height(18.dp))
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.Center,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    AppText.Small15(
                        text = "Don't have an Account?",
                        color = Color.Black.copy(alpha = .6f)
                    )
                    Spacer(Modifier.width(4.dp))
                    ClickableText(
                        text = AnnotatedString(
                            text = "Sign Up",
                        ),
                        style = AppTypography.copy(fontSize = 15.sp, color = AppBlueColor)
                    ) {
                        onSignUp()
                    }
                }
            }
        }
    }
}