package org.d3ifcool.medisgo.ui.profile

import android.content.Context
import android.graphics.Bitmap
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Arrangement
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
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.ButtonDefaults.buttonColors
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.pulltorefresh.PullToRefreshBox
import androidx.compose.material3.pulltorefresh.rememberPullToRefreshState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.asImageBitmap
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.vectorResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import androidx.navigation.NavHostController
import com.canhub.cropper.CropImageContract
import com.canhub.cropper.CropImageContractOptions
import com.canhub.cropper.CropImageOptions
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import org.d3ifcool.medisgosh.R
import org.d3ifcool.medisgosh.component.AppContainer
import org.d3ifcool.medisgosh.component.AppInput
import org.d3ifcool.medisgosh.component.AppText
import org.d3ifcool.medisgosh.component.AppTopBar
import org.d3ifcool.medisgosh.model.User
import org.d3ifcool.medisgosh.navigation.Screen
import org.d3ifcool.medisgosh.ui.theme.AppToscaColor
import org.d3ifcool.medisgosh.util.AppHelper
import org.d3ifcool.medisgosh.util.MediaUtils
import org.d3ifcool.medisgosh.util.ResponseStatus

enum class PagesStep {
    firstPage,
    secondPage,
    thirdPage,
    fourthPage,
    lastPage,
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ProfileOnboardingScreen(
    context: Context,
    profileData: User? = null,
    modifier: Modifier = Modifier,
    topPadding: Dp,
    bottomPadding: Dp,
    navController: NavHostController,
    isLoading: Boolean,
    viewModel: ProfileViewModel,
) {
    var currentPage by remember { mutableStateOf(PagesStep.firstPage) }
    val state = rememberPullToRefreshState()
    val coroutineScope = rememberCoroutineScope()
    var bitmap: Bitmap? by remember { mutableStateOf(null) }
    val launcher = rememberLauncherForActivityResult(CropImageContract()) {
        bitmap = MediaUtils.getCroppedImage(context.contentResolver, it)
    }

    var isRefreshing by remember { mutableStateOf(false) }
    val onRefresh: () -> Unit = {
        isRefreshing = true
        coroutineScope.launch {
            delay(300)
            isRefreshing = false
        }
    }

    var finalName by remember { mutableStateOf("") }
    var finalDate by remember { mutableStateOf("") }
    var finalGender by remember { mutableStateOf("") }
    var finalContact by remember { mutableStateOf("") }
    var finalAddress by remember { mutableStateOf("") }

    var finalKabupaten by remember { mutableStateOf("") }
    var finalKecamatan by remember { mutableStateOf("") }
    var finalRtRw by remember { mutableStateOf("") }
    var finalKodePos by remember { mutableStateOf("") }

    LaunchedEffect(profileData) {
        if (profileData != null) {
            finalName = profileData.name ?: ""
        }
    }

    AppContainer.Default(
        modifier = modifier,
    ) { innerPadding ->
        PullToRefreshBox(
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding),
            state = state,
            isRefreshing = isRefreshing,
            onRefresh = onRefresh,
        ) {
            LazyColumn(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(horizontal = 15.dp),
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.Top,
            ) {
                item {
                    AppTopBar(
                        modifier = Modifier.padding(horizontal = 12.dp),
                        imageUrl = "",
                        state = ResponseStatus.IDLE,
                        hideUserPicture = true,
                    )
                }
                item {
                    Spacer(Modifier.height(100.dp))
                }
                item {
                    when (currentPage) {
                        PagesStep.firstPage -> {
                            FirstPage {
                                currentPage = PagesStep.secondPage
                            }
                        }

                        PagesStep.secondPage -> {
                            SecondPage(
                                finalName = finalName,
                                finalDate = finalDate,
                                context = context,
                                bitmap = bitmap,
                                onBitmapChanged = { bitmap = it },
                            ) { name, selectedDate ->
                                currentPage = PagesStep.thirdPage
                                finalName = name
                                finalDate = selectedDate
                            }
                        }

                        PagesStep.thirdPage -> {
                            ThirdPage(
                                finalKabupaten = finalKabupaten,
                                finalKecamatan = finalKecamatan,
                                finalRtRw = finalRtRw,
                                finalKodePos = finalKodePos,
                                context = context,
                                bitmap = bitmap,
                                onBitmapChanged = { bitmap = it },
                                onBack = { kabupaten, kecamatan, rtRw, kodePos ->
                                    currentPage = PagesStep.secondPage
                                    finalKabupaten = kabupaten
                                    finalKecamatan = kecamatan
                                    finalRtRw = rtRw
                                    finalKodePos = kodePos
                                    finalAddress =
                                        "Kabupaten $kabupaten, Kecamatan $kecamatan, RT/RW $rtRw, Kode Pos $kodePos"

                                }
                            ) { kabupaten, kecamatan, rtRw, kodePos ->
                                currentPage = PagesStep.fourthPage
                                finalKabupaten = kabupaten
                                finalKecamatan = kecamatan
                                finalRtRw = rtRw
                                finalKodePos = kodePos
                                finalAddress =
                                    "Kabupaten $kabupaten, Kecamatan $kecamatan, RT/RW $rtRw, Kode Pos $kodePos"
                            }
                        }

                        PagesStep.fourthPage -> {
                            FourthPage(
                                finalGender = finalGender,
                                finalContact = finalContact,
                                context = context,
                                bitmap = bitmap,
                                onBitmapChanged = { bitmap = it },
                                onBack = { gender, contact ->
                                    currentPage = PagesStep.thirdPage
                                    finalGender = gender
                                    finalContact = contact
                                }
                            ) { gender, contact ->
                                currentPage = PagesStep.lastPage
                                finalGender = gender
                                finalContact = contact
                            }
                        }

                        PagesStep.lastPage -> {
                            LastPage {
                                val userData = User(
                                    name = finalName,
                                    dateOfBirth = finalDate,
                                    address = finalAddress,
                                    gender = finalGender,
                                    phoneNumber = finalContact,
                                    role = User.CLIENT_ROLE,
                                    employeeData = null,
                                    hasDoneOnboarding = true,
                                )
                                viewModel.editProfile(userData, bitmap)
                            }
                        }
                    }
                }
                item {
                    Spacer(Modifier.height(bottomPadding))
                }
            }
        }
    }
}

@Composable
private fun FirstPage(onNextPage: () -> Unit) {
    Column(
        modifier = Modifier.fillMaxSize().padding(horizontal = 15.dp),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally,
    ) {
        Image(
            modifier = Modifier.size(307.dp),
            painter = painterResource(org.d3ifcool.medisgo.R.drawable.doctor_illustration),
            contentDescription = null,
        )
        Spacer(Modifier.height(32.dp))
        AppText.Small15(
            text = "Untuk menggunakan fitur ini, pastikan data profil Anda sudah lengkap. Yuk, lengkapi sekarang agar pengalaman Anda lebih optimal!",
            textAlign = TextAlign.Center,
        )
        Spacer(Modifier.height(24.dp))
        CustomBerikutnyaButton(
            isCentered = true
        ) {
            onNextPage()
        }
    }
}

@Composable
private fun SecondPage(
    finalName: String,
    finalDate: String,
    context: Context,
    bitmap: Bitmap? = null,
    onBitmapChanged: (Bitmap?) -> Unit, onNextPage: (String, String) -> Unit
) {
    var name by remember { mutableStateOf(finalName) }
    var selectedDate by remember { mutableStateOf(finalDate) }
    Column(
        modifier = Modifier.fillMaxSize().padding(horizontal = 15.dp),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally,
    ) {
        CustomFormWithProfile(
            context = context,
            bitmap = bitmap,
            onBitmapChanged = onBitmapChanged, onNextPage = {
                onNextPage(name, selectedDate)
            }
        ) {
            AppInput.WithLabel(
                isRequired = true,
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
                isRequired = true,
                useLabel = true,
                label = "Date of birth",
                value = selectedDate,
                testTag = "dateField",
                placeHolder = "Your Date of Birth",
                placeHolderColor = Color.Gray.copy(alpha = .8f),
            ) {
                selectedDate = it
            }
        }
    }
}

@Composable
private fun ThirdPage(
    finalKabupaten: String,
    finalKecamatan: String,
    finalRtRw: String,
    finalKodePos: String,
    context: Context,
    bitmap: Bitmap? = null,
    onBitmapChanged: (Bitmap?) -> Unit,
    onBack: (String, String, String, String) -> Unit,
    onNextPage: (String, String, String, String) -> Unit
) {
    var kabupaten by remember { mutableStateOf(finalKabupaten) }
    var kecamatan by remember { mutableStateOf(finalKecamatan) }
    var rtRw by remember { mutableStateOf(finalRtRw) }
    var kodePos by remember { mutableStateOf(finalKodePos) }
    Column(
        modifier = Modifier.fillMaxSize().padding(horizontal = 15.dp),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally,
    ) {
        CustomFormWithProfile(
            useBackButton = true,
            context = context,
            bitmap = bitmap,
            onBitmapChanged = onBitmapChanged,
            onBack = {
                onBack(kabupaten, kecamatan, rtRw, kodePos)
            },
            onNextPage = {
                onNextPage(kabupaten, kecamatan, rtRw, kodePos)
            }
        ) {
            AppInput.WithLabel(
                isRequired = true,
                imeAction = ImeAction.Next,
                label = "Address",
                value = kabupaten,
                placeHolder = "Kabupaten",
                placeHolderColor = Color.Gray.copy(alpha = .8f),
            ) {
                kabupaten = it
            }
            AppInput.WithLabel(
                isRequired = true,
                imeAction = ImeAction.Next,
                label = "Address",
                value = kecamatan,
                placeHolder = "Kecamatan",
                placeHolderColor = Color.Gray.copy(alpha = .8f),
            ) {
                kecamatan = it
            }
            AppInput.WithLabel(
                isRequired = true,
                imeAction = ImeAction.Next,
                label = "Address",
                value = rtRw,
                placeHolder = "RT/RW",
                placeHolderColor = Color.Gray.copy(alpha = .8f),
            ) {
                rtRw = it
            }
            AppInput.IsDigitOnly(
                isRequired = true,
                imeAction = ImeAction.Next,
                label = "Address",
                value = kodePos,
                placeHolder = "Kode Pos",
                placeHolderColor = Color.Gray.copy(alpha = .8f),
            ) {
                kodePos = it
            }
        }
    }
}

@Composable
private fun FourthPage(
    finalGender: String,
    finalContact: String,
    context: Context,
    bitmap: Bitmap? = null,
    onBitmapChanged: (Bitmap?) -> Unit,
    onBack: (String, String) -> Unit,
    onNextPage: (String, String) -> Unit
) {
    var selectedGender by remember { mutableStateOf(finalGender) }
    var contact by remember { mutableStateOf(finalContact) }

    Column(
        modifier = Modifier.fillMaxSize().padding(horizontal = 15.dp),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally,
    ) {
        CustomFormWithProfile(
            useBackButton = true,
            context = context,
            bitmap = bitmap,
            onBitmapChanged = onBitmapChanged,
            onBack = {
                onBack(selectedGender, contact)
            },
            onNextPage = {
                onNextPage(selectedGender, contact)
            }
        ) {
            AppInput.Dropdown(
                isRequired = true,
                label = "Gender",
                initialValue = selectedGender,
                dropdownItems = listOf("Male", "Female"),
                placeHolder = "Your Gender",
            ) {
                selectedGender = it
            }
            AppInput.IsDigitOnly(
                isRequired = true,
                imeAction = ImeAction.Next,
                label = "Contact",
                value = contact,
                placeHolder = "Phone Number",
                placeHolderColor = Color.Gray.copy(alpha = .8f),
            ) {
                contact = it
            }
        }
    }
}

@Composable
private fun LastPage(
    onNextPage: () -> Unit,
) {
    Column(
        modifier = Modifier.fillMaxSize().padding(horizontal = 15.dp),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally,
    ) {
        Image(
            modifier = Modifier.size(198.dp),
            painter = painterResource(R.drawable.checklist_blue),
            contentDescription = null,
        )
        Spacer(Modifier.height(32.dp))
        AppText.Small15(
            text = "Yeay! Biodata Anda berhasil disimpan. Terima kasih sudah melengkapi data Anda!",
            textAlign = TextAlign.Center,
        )
        Spacer(Modifier.height(32.dp))
        CustomBerikutnyaButton(
            isCentered = true,
        ) {
            onNextPage()
        }
    }
}

@Composable
private fun CustomFormWithProfile(
    context: Context,
    bitmap: Bitmap? = null,
    useBackButton: Boolean = false,
    onBitmapChanged: (Bitmap?) -> Unit,
    onBack: () -> Unit = {},
    onNextPage: () -> Unit,
    forms: @Composable () -> Unit,
) {
    val launcher = rememberLauncherForActivityResult(CropImageContract()) {
        onBitmapChanged(MediaUtils.getCroppedImage(context.contentResolver, it))
    }
    if (bitmap != null)
        Image(
            bitmap = bitmap!!.asImageBitmap(),
            contentDescription = null,
            modifier = Modifier
                .size(100.dp)
                .clip(CircleShape),
        )
    else Image(
        modifier = Modifier.size(100.dp),
        imageVector = ImageVector.vectorResource(id = R.drawable.account_circle),
        contentDescription = null
    )
    IconButton(
        onClick = {
            val options = CropImageContractOptions(
                null, CropImageOptions(
                    imageSourceIncludeGallery = true,
                    imageSourceIncludeCamera = false,
                    fixAspectRatio = true,
                    outputCompressQuality = 50,
                )
            )
            launcher.launch(options)
        }
    ) {
        Icon(
            painter = painterResource(org.d3ifcool.medisgosh.R.drawable.camera),
            contentDescription = "Avatar Option Button",
            tint = Color.Gray,
        )
    }
    Spacer(Modifier.height(32.dp))
    forms()
    Spacer(Modifier.height(32.dp))
    CustomBerikutnyaButton(
        useBackButton = useBackButton,
        onBack = onBack,
    ) {
        onNextPage()
    }
}

@Composable
private fun CustomBerikutnyaButton(
    useBackButton: Boolean = false,
    isCentered: Boolean = false,
    onBack: () -> Unit = {},
    onNextPage: () -> Unit
) {
    Row(
        modifier = Modifier
            .fillMaxWidth(),
        horizontalArrangement = if (isCentered && !useBackButton) Arrangement.Center else Arrangement.End,
        verticalAlignment = Alignment.CenterVertically
    ) {
        if (useBackButton) {
            Button(
                modifier = Modifier.width(130.dp),
                onClick = onBack,
                colors = buttonColors(containerColor = AppToscaColor),
                elevation = ButtonDefaults.buttonElevation(
                    defaultElevation = 4.dp
                )
            ) {
                AppText.Small15(
                    text = "Kembali",
                    color = Color.White,
                    fontWeight = FontWeight.SemiBold
                )
            }
            Spacer(Modifier.width(20.dp))
        }
        Button(
            modifier = Modifier.width(130.dp),
            onClick = onNextPage,
            colors = buttonColors(containerColor = AppToscaColor),
            elevation = ButtonDefaults.buttonElevation(
                defaultElevation = 4.dp
            )
        ) {
            AppText.Small15(
                text = "Berikutnya",
                color = Color.White,
                fontWeight = FontWeight.SemiBold
            )
        }
    }
}