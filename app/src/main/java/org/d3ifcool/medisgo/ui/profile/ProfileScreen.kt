package org.d3ifcool.medisgo.ui.profile

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.ButtonDefaults.buttonColors
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
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.unit.dp
import com.google.firebase.auth.FirebaseUser
import org.d3ifcool.medisgosh.component.AppCircularLoading
import org.d3ifcool.medisgosh.component.AppInput
import org.d3ifcool.medisgosh.component.AppText
import org.d3ifcool.medisgosh.model.User
import org.d3ifcool.medisgosh.ui.profile.ProfilePrefixComponent
import org.d3ifcool.medisgosh.ui.theme.AppToscaColor
import org.d3ifcool.medisgosh.util.AppHelper
import org.d3ifcool.medisgosh.util.FlashMessageHelper
import org.d3ifcool.medisgosh.util.FlashMessageHelper.Companion.rememberSnackbarHostState
import org.d3ifcool.medisgosh.util.ResponseStatus

@Composable
fun ProfileScreen(modifier: Modifier = Modifier, user: FirebaseUser?, viewModel: ProfileViewModel) {
    val context = LocalContext.current

    val submissionStatus by viewModel.submissionStatus
    val profileData by viewModel.profileData
    var name by remember { mutableStateOf("") }
    var selectedDate by remember { mutableStateOf("") }
    var address by remember { mutableStateOf("") }
    var selectedGender by remember { mutableStateOf("") }
    var contact by remember { mutableStateOf("") }
    var isModified by remember { mutableStateOf(false) }
    fun checkIfModified() {
        isModified = name != profileData?.name ||
                selectedDate != profileData?.dateOfBirth ||
                address != profileData?.address ||
                selectedGender != profileData?.gender ||
                contact != profileData?.phoneNumber
    }

    val snackbarHostState = rememberSnackbarHostState()
    val coroutineScope = rememberCoroutineScope()

    LaunchedEffect(submissionStatus) {
        when (submissionStatus) {

            ResponseStatus.SUCCESS -> {
                FlashMessageHelper.showSuccess(
                    snackbarHostState, coroutineScope,
                    submissionStatus.message ?: "Berhasil",
                )
                viewModel.resetStatus()
            }

            ResponseStatus.FAILED -> {
                FlashMessageHelper.showError(
                    snackbarHostState, coroutineScope,
                    submissionStatus.message ?: "Gagal",
                )
                viewModel.resetStatus()
            }
            else -> null
        }
    }

    LaunchedEffect(profileData) {
        if (profileData != null) {
            name = profileData?.name ?: user?.displayName ?: ""
            selectedDate = profileData?.dateOfBirth ?: ""
            address = profileData?.address ?: ""
            selectedGender = profileData?.gender ?: ""
            contact = profileData?.phoneNumber ?: ""
        }
    }

    ProfilePrefixComponent(
        modifier = modifier,
        user = user,
        onSelectedImage = {
            viewModel.changeProfilePicture(it!!)
        },
        onRefreshParams = {
            viewModel.observeProfileData()
        },
        onLogout = {
            viewModel.logout()
        }
    ) {
        AppInput.WithLabel(
            imeAction = ImeAction.Next,
            label = "Name",
            value = name,
            testTag = "nameField",
            placeHolder = "Your Name",
            placeHolderColor = Color.Gray.copy(alpha = .8f),
        ) {
            name = it
            checkIfModified()
        }
        AppInput.DatePicker(
            useLabel = true,
            label = "Date of birth",
            value = selectedDate,
            testTag = "dateField",
            placeHolder = "Your Date of Birth",
            placeHolderColor = Color.Gray.copy(alpha = .8f),
        ) {
            selectedDate = it
            checkIfModified()
        }
        AppInput.WithLabel(
            imeAction = ImeAction.Next,
            label = "Address",
            value = address,
            testTag = "addressField",
            placeHolder = "Your Address",
            placeHolderColor = Color.Gray.copy(alpha = .8f),
        ) {
            address = it
            checkIfModified()
        }
        AppInput.Dropdown(
            label = "Gender",
            testTag = "genderField",
            initialValue = selectedGender,
            dropdownItems = listOf("Male", "Female"),
            placeHolder = "Your Gender",
        ) {
            selectedGender = it
            checkIfModified()
        }
        AppInput.IsDigitOnly(
            label = "Contact",
            value = contact,
            testTag = "contactField",
            placeHolder = "Your Phone Number",
            placeHolderColor = Color.Gray.copy(alpha = .8f),
            imeAction = ImeAction.Done
        ) {
            contact = it
            checkIfModified()
        }
        if (isModified) {
            val userData = User(
                name = name,
                dateOfBirth = selectedDate,
                address = address,
                gender = selectedGender,
                phoneNumber = contact,
                role = User.CLIENT_ROLE,
                employeeData = null
            )
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(12.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
                Button(
                    onClick = {
                        name = profileData?.name ?: user?.displayName ?: ""
                        selectedDate = profileData?.dateOfBirth ?: ""
                        selectedGender = profileData?.gender ?: ""
                        contact = profileData?.phoneNumber ?: ""
                        address = profileData?.address ?: ""
                    }, colors = buttonColors(containerColor = AppToscaColor),
                    elevation = ButtonDefaults.buttonElevation(
                        defaultElevation = 4.dp
                    )
                ) {
                    if (submissionStatus == ResponseStatus.LOADING) {
                        AppCircularLoading(useSpacer = false)
                    } else {
                        AppText.Small15(
                            text = "Batal",
                            color = Color.White,
                            fontWeight = FontWeight.SemiBold
                        )
                    }
                }
                Button(
                    enabled = (profileData ?: User()) != userData,
                    onClick = {
                        viewModel.editProfile(
                            userData
                        )
                    }, colors = buttonColors(containerColor = AppToscaColor),
                    elevation = ButtonDefaults.buttonElevation(
                        defaultElevation = 4.dp
                    )
                ) {
                    if (submissionStatus == ResponseStatus.LOADING) {
                        AppCircularLoading()
                    } else {
                        AppText.Small15(
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