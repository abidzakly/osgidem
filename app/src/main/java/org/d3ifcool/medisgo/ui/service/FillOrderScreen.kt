package org.d3ifcool.medisgo.ui.service

import android.graphics.Bitmap
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.ExperimentalLayoutApi
import androidx.compose.foundation.layout.IntrinsicSize
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.heightIn
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.ButtonDefaults.buttonElevation
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Snackbar
import androidx.compose.material3.SnackbarDuration
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.material3.TextFieldDefaults
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
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavHostController
import coil.compose.AsyncImage
import com.canhub.cropper.CropImageContract
import com.canhub.cropper.CropImageContractOptions
import com.canhub.cropper.CropImageOptions
import com.google.firebase.Timestamp
import com.google.firebase.auth.FirebaseUser
import kotlinx.coroutines.launch
import org.d3ifcool.medisgo.R
import org.d3ifcool.medisgosh.component.AppCircularLoading
import org.d3ifcool.medisgosh.component.AppContainer
import org.d3ifcool.medisgosh.component.AppText
import org.d3ifcool.medisgosh.model.Order
import org.d3ifcool.medisgosh.navigation.Screen
import org.d3ifcool.medisgosh.ui.theme.AppDanger
import org.d3ifcool.medisgosh.ui.theme.AppDarkToscaColor
import org.d3ifcool.medisgosh.ui.theme.AppMediumLightBlueColor
import org.d3ifcool.medisgosh.ui.theme.AppToscaColor
import org.d3ifcool.medisgosh.ui.theme.BackButton
import org.d3ifcool.medisgosh.util.AppHelper
import org.d3ifcool.medisgosh.util.ResponseStatus
import org.d3ifcool.medisgosh.util.DateFormatter
import org.d3ifcool.medisgosh.util.FlashMessageHelper
import org.d3ifcool.medisgosh.util.FlashMessageHelper.Companion.rememberSnackbarHostState
import org.d3ifcool.medisgosh.util.MediaUtils
import java.util.Calendar
import java.util.Locale

@OptIn(ExperimentalMaterial3Api::class, ExperimentalLayoutApi::class)
@Composable
fun FillOrderScreen(
    modifier: Modifier = Modifier,
    user: FirebaseUser,
    navController: NavHostController,
    viewModel: FillOrderViewModel
) {
    val context = LocalContext.current

    val snackbarHostState = rememberSnackbarHostState()
    val coroutineScope = rememberCoroutineScope()

    var selectedDateFinal by remember { mutableStateOf<Calendar?>(null) }
    var timeString by remember { mutableStateOf("") }
    var result by remember { mutableStateOf("") }
    var bitmap: Bitmap? by remember { mutableStateOf(null) }
    val launcher = rememberLauncherForActivityResult(CropImageContract()) {
        bitmap = MediaUtils.getCroppedImage(context.contentResolver, it)
    }
    var textKeluhan by remember { mutableStateOf("") }

    val fetchedDoctor by viewModel.fetchedDoctor
    val submitStatus by viewModel.submitStatus
    val selectedDate = remember { mutableStateOf<Long?>(null) }
    val today = Calendar.getInstance()
    val dates = (0..7).map { offset ->
        Calendar.getInstance().apply {
            add(Calendar.DAY_OF_YEAR, offset)
        }.timeInMillis
    }
    LaunchedEffect(submitStatus) {
        when (submitStatus) {
            ResponseStatus.SUCCESS -> {
                FlashMessageHelper.showSuccess(
                    snackbarHostState, coroutineScope,
                    submitStatus.message ?: "Janji telah dibuat"
                )
                AppHelper.navigate(navController, Screen.Client.Orders.route)
                viewModel.reset()
            }

            ResponseStatus.FAILED -> {
                FlashMessageHelper.showError(
                    snackbarHostState, coroutineScope,
                    submitStatus.message ?: "Terjadi kesalahan"
                )
                viewModel.reset()
            }

            else -> null
        }
    }

    // Membuat scroll behavior untuk TopAppBar
    AppContainer.WithTopBar(
        modifier = modifier.padding(),
        profileImageUrl = user.photoUrl.toString(),
        onProfileClick = {
            AppHelper.navigate(navController, Screen.General.Profile.route)
        },
        snackbarHost = {
            FlashMessageHelper.FlashMessageHost(
                snackbarHostState,
            )
        },
    ) { paddingValues ->
        LazyColumn(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues),
            contentPadding = PaddingValues(bottom = 56.dp)
        ) {
            item {
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(
                            horizontal = 24.dp,
                            vertical = 45.dp
                        ),
                    horizontalArrangement = Arrangement.Start
                ) {
                    IconButton(
                        onClick = {
                            navController.popBackStack()
                        }
                    ) {
                        Icon(
                            painter = painterResource(R.drawable.back_big),
                            contentDescription = "Back Button",
                            tint = Color.Unspecified
                        )
                    }
                }
            }
            item {
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(horizontal = 36.dp),
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.spacedBy(22.dp)
                ) {
                    Box(
                        modifier = Modifier
                            .size(131.dp)
                            .clip(RoundedCornerShape(20.dp))
                    ) {
                        if (fetchedDoctor != null && fetchedDoctor!!.photoUrl != null) {
                            AsyncImage(
                                modifier = Modifier
                                    .size(140.dp)
                                    .clip(RoundedCornerShape(20.dp)),
                                model = fetchedDoctor?.photoUrl.toString(),
                                contentDescription = "User's Profile Photo",
                            )
                        } else {
                            Image(
                                painter = painterResource(R.drawable.profilephoto),
                                contentDescription = "Profile Tenaga Medis",
                                contentScale = ContentScale.Crop
                            )
                        }
                    }
                    Column(
                        modifier = Modifier
                            .width(IntrinsicSize.Max)
                            .padding(end = 8.dp)
                    ) {
                        Text(
                            text = fetchedDoctor?.name ?: "-",
                            fontSize = 24.sp,
                            color = BackButton,
                            fontWeight = FontWeight.Bold
                        )
                        HorizontalDivider(
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(vertical = 4.dp),
                            thickness = 2.dp,
                            color = BackButton
                        )
                        Text(
                            text = fetchedDoctor?.employeeData?.type ?: "-",
                            fontSize = 15.sp,
                            color = AppMediumLightBlueColor,
                            fontWeight = FontWeight.SemiBold
                        )
                    }
                }
                Spacer(Modifier.height(23.dp))
            }
            item {
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(horizontal = 36.dp),
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.SpaceBetween
                ) {
                    Column(
                        modifier = Modifier
                            .size(250.dp, 150.dp)
                            .clip(RoundedCornerShape(20.dp))
                            .background(Color.Blue.copy(alpha = .8f))
                    ) {
                        Row(
                            modifier = Modifier.padding(16.dp),
                            horizontalArrangement = Arrangement.Center
                        ) {
                            Text(
                                text = "Choose your slot",
                                color = Color.White,
                                modifier = Modifier.fillMaxWidth(),
                                textAlign = TextAlign.Center
                            )
                        }
                        LazyRow(
                            modifier = Modifier
                                .size(250.dp, 120.dp)
                                .background(color = Color.White)
                                .padding(8.dp),
                            horizontalArrangement = Arrangement.spacedBy(8.dp),
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            items(dates) { dateMillis ->
                                val date =
                                    Calendar.getInstance().apply { timeInMillis = dateMillis }
                                val isSelected = selectedDate.value == dateMillis
                                val backgroundColor =
                                    if (isSelected) Color(0xFFFFC1E3) else Color.Transparent
                                Column(
                                    horizontalAlignment = Alignment.CenterHorizontally,
                                    modifier = Modifier
                                        .clip(RoundedCornerShape(16.dp))
                                        .background(backgroundColor)
                                        .padding(horizontal = 16.dp, vertical = 16.dp)
                                        .clickable {
                                            selectedDate.value = dateMillis
                                        }
                                ) {
                                    Text(
                                        text = date.getDisplayName(
                                            Calendar.DAY_OF_WEEK,
                                            Calendar.SHORT,
                                            Locale("eng", "ENG")
                                        ) ?: "Unknown",
                                        fontWeight = FontWeight.Bold,
                                        fontSize = 12.sp,
                                    )
                                    Spacer(Modifier.height(10.dp))
                                    Text(
                                        text = date.get(Calendar.DAY_OF_MONTH).toString(),
                                        fontSize = 12.sp,
                                        fontWeight = FontWeight.Bold,
                                    )
                                }
                            }
                        }
                    }
                    Image(
                        painter = painterResource(R.drawable.doctor_illustration),
                        contentDescription = "Ilustrasi Dokter",
                        modifier = Modifier.height(200.dp)
                    )
                }
            }
            item {
                val times = listOf("9:00", "10:00", "13:00", "15:00", "17:00")
                val selectedTime = remember { mutableStateOf<String?>(null) }
                Column(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(horizontal = 36.dp),
                ) {
                    Text(
                        text = "Pilihlah Jam yang Tersedia:",
                        fontSize = 20.sp
                    )
                    Spacer(Modifier.height(8.dp))
                    LazyRow(
                        horizontalArrangement = Arrangement.spacedBy(8.dp),
                        modifier = Modifier
                            .fillMaxWidth(),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        items(times) { time ->
                            val isSelected = selectedTime.value == time
                            val backgroundColor = if (isSelected) Color.Blue else Color.Transparent
                            val textColor = if (isSelected) Color.White else Color.Black

                            Column(
                                horizontalAlignment = Alignment.CenterHorizontally,
                                modifier = Modifier
                                    .border(2.dp, Color.Blue, RoundedCornerShape(8.dp))
                                    .background(backgroundColor, RoundedCornerShape(8.dp))
                                    .clickable {
                                        selectedTime.value = time
                                        timeString = time
                                    }
                                    .padding(vertical = 8.dp, horizontal = 16.dp)
                            ) {
                                Text(
                                    text = time,
                                    fontSize = 12.sp,
                                    color = textColor
                                )
                            }
                        }
                    }
                    Spacer(Modifier.height(24.dp))
                }
            }
            item {
                Column(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(horizontal = 36.dp)
                        .height(150.dp)
                        .border(1.dp, Color.Black, RoundedCornerShape(10.dp))
                        .padding(8.dp)
                        .clip(RoundedCornerShape(9.dp))
                        .background(Color.Transparent)
                ) {
                    TextField(
                        placeholder = { Text(text = "Tuliskan Keluhanmu Secara Garis Besar Saja:") },
                        value = textKeluhan,
                        textStyle = TextStyle(fontSize = 16.sp),
                        onValueChange = {
                            textKeluhan = it
                        },
                        colors = TextFieldDefaults.colors(
                            focusedContainerColor = Color.Transparent,
                            unfocusedContainerColor = Color.Transparent,
                            focusedIndicatorColor = Color.Transparent,
                            unfocusedIndicatorColor = Color.Transparent
                        ),
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(150.dp)
                            .heightIn(max = 100.dp),
                        keyboardOptions = KeyboardOptions(
                            imeAction = ImeAction.Done
                        )
                    )
                }
                Spacer(Modifier.height(30.dp))
            }
            item {
                Column(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(horizontal = 36.dp)
                        .height(150.dp)
                        .border(1.dp, Color.Black, RoundedCornerShape(10.dp))
                        .padding(16.dp)
                        .clip(RoundedCornerShape(9.dp))
                        .background(Color.Transparent),
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    if (bitmap != null) {
                        Image(
                            bitmap = bitmap!!.asImageBitmap(),
                            contentDescription = null,
                            modifier = Modifier
                                .fillMaxWidth()
                                .clip(shape = RoundedCornerShape(20.dp))
                                .aspectRatio(1f)
                                .clickable {
                                    val options = CropImageContractOptions(
                                        null, CropImageOptions(
                                            imageSourceIncludeGallery = true,
                                            imageSourceIncludeCamera = true,
                                            fixAspectRatio = true
                                        )
                                    )
                                    launcher.launch(options)
                                }
                        )
                    } else {
                        Button(
                            onClick = {
                                val options = CropImageContractOptions(
                                    null, CropImageOptions(
                                        imageSourceIncludeGallery = true,
                                        imageSourceIncludeCamera = true,
                                        fixAspectRatio = true
                                    )
                                )
                                launcher.launch(options)
                            },
                            shape = RoundedCornerShape(10.dp),
                            colors = ButtonDefaults.buttonColors(
                                containerColor = Color.Transparent
                            ),
                            modifier = Modifier
                                .border(1.dp, Color.Black, RoundedCornerShape(10.dp))
                        ) {
                            Image(
                                painter = painterResource(R.drawable.media),
                                contentDescription = "Bukti Foto",
                                modifier = Modifier
                                    .size(75.dp)
                                    .padding(20.dp)
                            )
                        }
                        Spacer(Modifier.height(8.dp))
                        AppText.Regular16(
                            text = "Tambahkan foto sakit Anda bagaimana",
                        )
                    }
                }
                Spacer(Modifier.height(30.dp))
            }
            item {
                Column(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(horizontal = 36.dp),
                    horizontalAlignment = Alignment.End
                ) {
                    Button(
                        onClick = {
                            if (bitmap != null && textKeluhan.isNotEmpty() &&
                                timeString.isNotEmpty() && selectedDateFinal.toString()
                                    .isNotEmpty()
                            ) {
                                viewModel.submitOrder(
                                    order = Order(
                                        doctor = fetchedDoctor,
                                        description = textKeluhan,
                                        supportingImageUrl = null,
                                        status = "UNPAID",
                                        appointmentTime = DateFormatter.combineDateAndTime(
                                            selectedDate = selectedDateFinal,
                                            timeString = timeString,
                                        ),
                                        paymentReceiptUrl = "",
                                        createdAt = Timestamp.now(),
                                        rating = "",
                                        id = null
                                    ),
                                    bitmap = bitmap!!,
                                )
                            } else {
                                coroutineScope.launch {
                                    FlashMessageHelper.showWarning(
                                        snackbarHostState, coroutineScope,
                                        "Harap isi semua data terlebih dahulu!"
                                    )
                                }
                            }
                        },
                        shape = RoundedCornerShape(20.dp),
                        colors = ButtonDefaults.buttonColors(
                            containerColor = AppToscaColor
                        ),
                        elevation = buttonElevation(defaultElevation = 4.dp)
                    ) {
                        if (submitStatus == ResponseStatus.LOADING) {
                            AppCircularLoading(useSpacer = false)
                        } else {
                            AppText.Small14(
                                text = "MAKE AN APPOINMENT",
                                textAlign = TextAlign.Center,
                                color = Color.White,
                                fontWeight = FontWeight.SemiBold
                            )
                        }
                    }
                }
                Spacer(Modifier.height(23.dp))
            }
        }
    }
}
