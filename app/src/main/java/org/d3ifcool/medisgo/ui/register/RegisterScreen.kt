package org.d3ifcool.medisgo.ui.register

import android.app.Activity
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.Image
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
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.ClickableText
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Email
import androidx.compose.material.icons.filled.Lock
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults.buttonColors
import androidx.compose.material3.ButtonDefaults.buttonElevation
import androidx.compose.material3.Checkbox
import androidx.compose.material3.CheckboxDefaults
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
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.AnnotatedString
import androidx.compose.ui.text.SpanStyle
import androidx.compose.ui.text.buildAnnotatedString
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.withStyle
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.firebase.ui.auth.FirebaseAuthUIActivityResultContract
import org.d3ifcool.medisgo.R
import org.d3ifcool.medisgosh.component.AppCircularLoading
import org.d3ifcool.medisgosh.component.AppContainer
import org.d3ifcool.medisgosh.component.AppInput
import org.d3ifcool.medisgosh.component.AppText
import org.d3ifcool.medisgosh.ui.theme.AdlamDisplay
import org.d3ifcool.medisgosh.ui.theme.AppBlue2Color
import org.d3ifcool.medisgosh.ui.theme.AppBlueColor
import org.d3ifcool.medisgosh.ui.theme.AppToscaColor
import org.d3ifcool.medisgosh.ui.theme.AppTypography
import org.d3ifcool.medisgosh.util.AppHelper
import org.d3ifcool.medisgosh.util.AppObjectState

@Composable
fun RegisterScreen(
    modifier: Modifier = Modifier,
    viewModel: RegisterViewModel,
    onSignIn: () -> Unit
) {
    val context = LocalContext.current
    val status by viewModel.status

    var email by remember { mutableStateOf("") }
    var username by remember { mutableStateOf("") }
    var password by remember { mutableStateOf("") }

    var emailError by remember { mutableStateOf(false) }
    var usernameError by remember { mutableStateOf(false) }
    var passwordError by remember { mutableStateOf(false) }

    var isChecked by remember { mutableStateOf(false) }

    LaunchedEffect(status) {
        when (status) {
            AppObjectState.SUCCESS -> {
                AppHelper.toastGenerator(context, strMessage = status.message)
            }

            else -> AppHelper.toastGenerator(context, strMessage = status.message)
        }
    }

    AppContainer.Default(
        modifier = Modifier
    ) { innerPadding ->
        LazyColumn(
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding)
                .padding(horizontal = 54.dp),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center
        ) {
            item {
                Image(
                    painter = painterResource(R.drawable.app_logo_big),
                    contentDescription = null,
                )
                Spacer(Modifier.height(56.dp))
                AppText.Regular16(
                    modifier = Modifier.fillMaxWidth(),
                    text = "Daftar sebagai pasien",
                    fontFamily = AdlamDisplay,
                    textAlign = TextAlign.Center
                )
                Spacer(Modifier.height(18.dp))
                AppInput.Default(
                    testTag = "usernameField",
                    isError = usernameError,
                    value = username,
                    leadingIcon = {
                        Icon(
                            painter = painterResource(R.drawable.person_hand_up),
                            contentDescription = null,
                            tint = Color.Gray
                        )
                    },
                    placeHolder = "Nickname",
                    placeHolderColor = Color.Gray.copy(alpha = .8f),
                    colors = OutlinedTextFieldDefaults.colors(
                        focusedBorderColor = AppBlueColor,
                        unfocusedBorderColor = AppBlueColor
                    ),
                    keyboardType = KeyboardType.Text
                ) {
                    username = it
                    usernameError = false
                }
                Spacer(Modifier.height(8.dp))
                AppInput.Default(
                    testTag = "emailField",
                    isError = emailError,
                    value = email,
                    leadingIcon = {
                        Icon(
                            imageVector = Icons.Default.Email,
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
                                viewModel.signUp(username, email, password)
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
                        text = "Sign Up",
                        color = Color.White,
                        fontWeight = FontWeight.SemiBold
                    )
                }
                Spacer(Modifier.height(10.dp))
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.Center
                ) {
                    val annotatedText = buildAnnotatedString {
                        append("I agree to the ")

                        pushStringAnnotation(tag = "TERMS", annotation = "TERMS")
                        withStyle(style = SpanStyle(color = Color.Blue)) {
                            append("Terms")
                        }
                        pop()

                        append(" of Service and ")

                        pushStringAnnotation(tag = "PRIVACY", annotation = "PRIVACY")
                        withStyle(style = SpanStyle(color = Color.Blue)) {
                            append("Privacy")
                        }
                        pop()

                        append(" & ")

                        pushStringAnnotation(tag = "SECURITY", annotation = "SECURITY")
                        withStyle(style = SpanStyle(color = Color.Blue)) {
                            append("Security")
                        }
                        pop()

                        append(" Policy.")
                    }
                    Box(
                        modifier = Modifier.clip(RoundedCornerShape(4.dp)).background(Color.White).size(20.dp)
                    ) {
                        Checkbox(
                            checked = isChecked,
                            onCheckedChange = { isChecked = it },
                            colors = CheckboxDefaults.colors(
                                checkedColor = Color.White,
                                uncheckedColor = Color.White,
                                checkmarkColor = Color.Black
                            ),
                        )
                    }
                    Spacer(Modifier.width(10.dp))
                    AppText.Small15Annotated(
                        text= annotatedText
                    )
                }
                Spacer(Modifier.height(29.dp))
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
                        text = "Have an Account already? ",
                        color = Color.Black.copy(alpha = .6f)
                    )
                    Spacer(Modifier.width(4.dp))
                    ClickableText(
                        text = AnnotatedString(
                            text = "Sign In",
                        ),
                        style = AppTypography.copy(fontSize = 15.sp, color = AppBlueColor)
                    ) {
                        onSignIn()
                    }
                }
            }
        }
    }
}