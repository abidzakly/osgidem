package org.d3ifcool.medisgotm.ui.profile

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.width
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.ButtonDefaults.buttonColors
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
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.unit.dp
import com.google.firebase.auth.FirebaseUser
import org.d3ifcool.medisgosh.component.AppCircularLoading
import org.d3ifcool.medisgosh.component.AppInput
import org.d3ifcool.medisgosh.component.AppText
import org.d3ifcool.medisgosh.model.Employee
import org.d3ifcool.medisgosh.model.User
import org.d3ifcool.medisgosh.ui.profile.ProfilePrefixComponent
import org.d3ifcool.medisgosh.ui.theme.AppToscaColor
import org.d3ifcool.medisgosh.util.AppHelper
import org.d3ifcool.medisgosh.util.AppObjectState

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
    var selectedType by remember { mutableStateOf("") }
    var accountName by remember { mutableStateOf("") }
    var bankName by remember { mutableStateOf("") }
    var accountNumber by remember { mutableStateOf("") }
    var strNumber by remember { mutableStateOf("") }

    LaunchedEffect(submissionStatus) {
        when (submissionStatus) {
            AppObjectState.LOADING -> AppHelper.toastGenerator(context, strMessage = "Menunggu")

            AppObjectState.IDLE -> null

            else -> {
                AppHelper.toastGenerator(context, strMessage = submissionStatus.message)
                viewModel.resetStatus()
            }
        }
    }

    LaunchedEffect(profileData) {
        if (profileData != null) {
            name = profileData?.name ?: user?.displayName ?: ""
            selectedDate = profileData?.dateOfBirth ?: ""
            address = profileData?.address ?: ""
            selectedGender = profileData?.gender ?: ""
            contact = profileData?.phoneNumber ?: ""
            selectedType = profileData?.employeeData?.type ?: ""
            accountName = profileData?.employeeData?.accountName ?: ""
            bankName = profileData?.employeeData?.bankName ?: ""
            accountNumber = profileData?.employeeData?.accountNumber ?: ""
            strNumber = profileData?.employeeData?.strNumber ?: ""
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
        }
        AppInput.Dropdown(
            label = "Gender",
            testTag = "genderField",
            initialValue = selectedGender,
            dropdownItems = listOf("Male", "Female"),
            placeHolder = "Your Gender",
        ) {
            selectedGender = it
        }
        AppInput.IsDigitOnly(
            label = "Contact",
            value = contact,
            testTag = "contactField",
            placeHolder = "Your Phone Number",
            placeHolderColor = Color.Gray.copy(alpha = .8f),
        ) {
            contact = it
        }
        AppInput.Dropdown(
            label = "Tenaga Medis",
            dropdownItems = listOf(
                "Dokter Spesialis Penyakit Dalam",
                "Dokter Spesialis Anak",
                "Dokter Spesialis Kulit",
                "Dokter Spesialis Kejiwaan",
                "Dokter Spesialis Lansia",
                "Bidan",
                "Fisioterapi",
                "Perawat"
            ),
            onItemSelected = { selectedType = it },
            testTag = "jobTypeField",
            initialValue = selectedType,
            placeHolder = "Tenaga Medis Sebagai",
        )
        AppInput.WithLabel(
            imeAction = ImeAction.Next,
            label = "Nama Rekening",
            value = accountName,
            testTag = "accountNameField",
            placeHolder = "Nama Rekening Anda",
            placeHolderColor = Color.Gray.copy(alpha = .8f),
        ) {
            accountName = it
        }
        AppInput.WithLabel(
            imeAction = ImeAction.Next,
            label = "Jenis Bank",
            value = bankName,
            testTag = "bankNameField",
            placeHolder = "Jenis Bank Anda",
            placeHolderColor = Color.Gray.copy(alpha = .8f),
        ) {
            bankName = it
        }
        AppInput.IsDigitOnly(
            imeAction = ImeAction.Done,
            label = "Nomor Rekening",
            value = accountNumber,
            testTag = "accountNumberField",
            placeHolder = "Nomor Rekening Anda",
            placeHolderColor = Color.Gray.copy(alpha = .8f),
        ) {
            accountNumber = it
        }
        AppInput.IsDigitOnly(
            imeAction = ImeAction.Done,
            label = "Nomor STR",
            value = strNumber,
            testTag = "",
            placeHolder = "Nomor STR Anda",
            placeHolderColor = Color.Gray.copy(alpha = .8f),
        ) {
            strNumber = it
        }
        Spacer(Modifier.height(15.dp))
        if (
            name.isNotEmpty() && selectedDate.isNotEmpty() && address.isNotEmpty() &&
            selectedGender.isNotEmpty() && contact.isNotEmpty() && selectedType.isNotEmpty() &&
            accountName.isNotEmpty() && bankName.isNotEmpty() && accountNumber.isNotEmpty() && strNumber.isNotEmpty()
        ) {
            val employeeData = Employee(
                type = selectedType,
                accountName = accountName,
                bankName = bankName,
                accountNumber = accountNumber,
                strNumber = strNumber
            )

            val userData = User(
                name = name,
                dateOfBirth = selectedDate,
                address = address,
                gender = selectedGender,
                phoneNumber = contact,
                role = Employee.ROLE,
                employeeData = employeeData
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
                        selectedType = profileData?.employeeData?.type ?: ""
                        accountName = profileData?.employeeData?.accountName ?: ""
                        bankName = profileData?.employeeData?.bankName ?: ""
                        accountNumber = profileData?.employeeData?.accountNumber ?: ""
                        strNumber = profileData?.employeeData?.strNumber ?: ""
                    }, colors = buttonColors(containerColor = AppToscaColor),
                    elevation = ButtonDefaults.buttonElevation(
                        defaultElevation = 4.dp
                    )
                ) {
                    if (submissionStatus == AppObjectState.LOADING) {
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
                    if (submissionStatus == AppObjectState.LOADING) {
                        AppCircularLoading(useSpacer = false)
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