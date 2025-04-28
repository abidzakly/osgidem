package org.d3ifcool.medisgo.ui.order

import android.graphics.Bitmap
import android.widget.Toast
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.IntrinsicSize
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.ButtonDefaults.buttonColors
import androidx.compose.material3.CardDefaults.outlinedCardColors
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.OutlinedCard
import androidx.compose.material3.Snackbar
import androidx.compose.material3.SnackbarDuration
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.Tab
import androidx.compose.material3.TabRow
import androidx.compose.material3.TabRowDefaults
import androidx.compose.material3.TabRowDefaults.tabIndicatorOffset
import androidx.compose.material3.Text
import androidx.compose.material3.pulltorefresh.PullToRefreshBox
import androidx.compose.material3.pulltorefresh.rememberPullToRefreshState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.vectorResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.navigation.NavHostController
import coil.compose.AsyncImage
import com.canhub.cropper.CropImageContract
import com.canhub.cropper.CropImageContractOptions
import com.canhub.cropper.CropImageOptions
import com.google.firebase.auth.FirebaseUser
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import org.d3ifcool.medisgosh.R
import org.d3ifcool.medisgosh.component.AppCircularLoading
import org.d3ifcool.medisgosh.component.AppContainer
import org.d3ifcool.medisgosh.component.AppLabel
import org.d3ifcool.medisgosh.component.AppPopUp
import org.d3ifcool.medisgosh.component.AppText
import org.d3ifcool.medisgosh.model.Order
import org.d3ifcool.medisgosh.navigation.Screen
import org.d3ifcool.medisgosh.ui.theme.AppBlue2Color
import org.d3ifcool.medisgosh.ui.theme.AppBlueColor
import org.d3ifcool.medisgosh.ui.theme.AppDanger
import org.d3ifcool.medisgosh.ui.theme.AppDarkBlueColor
import org.d3ifcool.medisgosh.ui.theme.AppDarkToscaColor
import org.d3ifcool.medisgosh.ui.theme.AppGrayColor
import org.d3ifcool.medisgosh.ui.theme.AppMediumLightBlueColor
import org.d3ifcool.medisgosh.ui.theme.AppToscaColor
import org.d3ifcool.medisgosh.ui.theme.AppWarning
import org.d3ifcool.medisgosh.ui.theme.BackButton
import org.d3ifcool.medisgosh.ui.theme.PembayaranColor
import org.d3ifcool.medisgosh.util.AppHelper
import org.d3ifcool.medisgosh.util.FlashMessageHelper
import org.d3ifcool.medisgosh.util.FlashMessageHelper.Companion.rememberSnackbarHostState
import org.d3ifcool.medisgosh.util.ResponseStatus
import org.d3ifcool.medisgosh.util.MediaUtils

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun OrderScreen(
    modifier: Modifier = Modifier,
    user: FirebaseUser?,
    navController: NavHostController,
    viewModel: OrderViewModel,
) {
    val fetchedData by viewModel.fetchedOrders
    val fetchStatus by viewModel.fetchStatus
    val submissionStatus by viewModel.submissionStatus

    var showCancelDialog by remember { mutableStateOf(false) }
    var selectedTabIndex by remember { mutableIntStateOf(0) }
    var selectedOrder by remember { mutableStateOf<Order?>(null) }
    val snackbarHostState = rememberSnackbarHostState()
    val coroutineScope = rememberCoroutineScope()
    val state = rememberPullToRefreshState()
    var isRefreshing by remember { mutableStateOf(false) }
    val onRefresh: () -> Unit = {
        isRefreshing = true
        coroutineScope.launch {
            delay(1000)
            viewModel.observeOrders()
            isRefreshing = false
        }
    }

    LaunchedEffect(selectedTabIndex) {
        viewModel.filterData(selectedTabIndex)
    }

    LaunchedEffect(submissionStatus) {
        when (submissionStatus) {
            ResponseStatus.SUCCESS -> {
                FlashMessageHelper.showSuccess(
                    snackbarHostState, coroutineScope,
                    submissionStatus.message ?: "Berhasil"
                )
                if (showCancelDialog) {
                    showCancelDialog = false
                } else {
                    selectedTabIndex = 1
                }
                viewModel.reset()
            }

            ResponseStatus.FAILED -> {

                FlashMessageHelper.showError(
                    snackbarHostState, coroutineScope,
                    submissionStatus.message ?: "Gagal"
                )
                if (showCancelDialog) {
                    showCancelDialog = false
                }
                viewModel.reset()
            }

            else -> {}
        }
    }

    if (showCancelDialog) {
        AppPopUp.CustomDialog(
            label = "Apakah anda yakin\n" +
                    "ingin membatalkan pesanan anda?",
            positiveLabel = "Ya",
            negativeLabel = "Tidak",
            submissionStatus = submissionStatus,
            onDismissRequest = {
                showCancelDialog = false
            },
            onPositiveClicked = {
                if (selectedOrder != null) {
                    viewModel.cancelOrder(selectedOrder!!)
                }
            }
        )
    }

    AppContainer.WithTopBar(
        snackbarHost = {
            FlashMessageHelper.FlashMessageHost(
                snackbarHostState,
            )
        },
        modifier = modifier,
        profileImageUrl = user?.photoUrl.toString(),
        onProfileClick = {
            AppHelper.navigate(navController, Screen.General.Profile.route)
        },
    ) { innerPadding ->
        PullToRefreshBox(
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding),
            state = state,
            isRefreshing = isRefreshing,
            onRefresh = onRefresh,
        ) {
            Column(
                modifier = Modifier.fillMaxSize(),
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.Top,
            ) {
                Spacer(Modifier.height(30.dp))
                AppText.Semi20(
                    text = "Pesanan Saya",
                    fontWeight = FontWeight.Bold,
                    color = AppGrayColor
                )
                Spacer(Modifier.height(12.dp))
                TabRow(
                    selectedTabIndex = selectedTabIndex,
                    modifier = Modifier
                        .padding(top = 8.dp)
                        .padding(horizontal = 40.dp),
                    containerColor = Color.Transparent,
                    indicator =
                    { tabPositions ->
                        if (selectedTabIndex < tabPositions.size) {
                            TabRowDefaults.SecondaryIndicator(
                                Modifier.tabIndicatorOffset(
                                    tabPositions[selectedTabIndex]
                                ),
                                color = AppBlueColor
                            )
                        }
                    },
                    divider = {
                        HorizontalDivider(
                            color = Color.Transparent
                        )
                    }
                ) {
                    Tab(
                        selected = selectedTabIndex == 0,
                        onClick = { selectedTabIndex = 0 },
                        text = {
                            AppText.Regular16(
                                modifier = Modifier.fillMaxWidth(),
                                text = "Belum dibayar",
                                color = if (selectedTabIndex == 0) AppBlueColor else AppGrayColor,
                                fontWeight = FontWeight.SemiBold,
                                textAlign = TextAlign.Center
                            )
                        },
                        selectedContentColor = AppBlueColor
                    )
                    Tab(
                        selected = selectedTabIndex == 1,
                        onClick = { selectedTabIndex = 1 },
                        text = {
                            AppText.Regular16(
                                modifier = Modifier.fillMaxWidth(),
                                text = "Sedang Dilayani",
                                color = if (selectedTabIndex == 1) AppBlueColor else AppGrayColor,
                                fontWeight = FontWeight.SemiBold,
                                textAlign = TextAlign.Center
                            )
                        },
                        selectedContentColor = AppBlueColor
                    )
                    Tab(
                        selected = selectedTabIndex == 2,
                        onClick = { selectedTabIndex = 2 },
                        text = {
                            AppText.Regular16(
                                modifier = Modifier.fillMaxWidth(),
                                text = "Selesai",
                                color = if (selectedTabIndex == 2) AppBlueColor else AppGrayColor,
                                fontWeight = FontWeight.SemiBold,
                                textAlign = TextAlign.Center

                            )
                        },
                        selectedContentColor = AppBlueColor
                    )
                }
                LazyColumn(
                    modifier = Modifier.fillMaxSize(),
                    horizontalAlignment = Alignment.CenterHorizontally,
                    verticalArrangement = if (fetchStatus == ResponseStatus.SUCCESS && !fetchedData.isNullOrEmpty()) Arrangement.Top else Arrangement.Center,
                    contentPadding = PaddingValues(bottom = 86.dp, top = 16.dp)
                ) {
                    when (fetchStatus) {
                        ResponseStatus.LOADING -> {
                            item {
                                AppCircularLoading(
                                    color = AppDarkBlueColor,
                                    size = 30.dp,
                                    useSpacer = false
                                )
                            }
                        }

                        ResponseStatus.SUCCESS -> {
                            if (!fetchedData.isNullOrEmpty()) {
                                items(fetchedData!!) {
                                    Box(
                                        modifier = Modifier.padding(24.dp)
                                    ) {
                                        OrderCard(
                                            order = it,
                                            submissionStatus = submissionStatus,
                                            onCancelOrder = {
                                                selectedOrder = it
                                                showCancelDialog = true
                                            },
                                            onSubmitPayment = { bitmap ->
                                                viewModel.submitPayment(it)
                                            },
                                        )
                                    }
                                }
                            } else {
                                item {
                                    Image(
                                        painter = painterResource(R.drawable.order_empty_illustration),
                                        contentDescription = null,
                                    )
                                    Spacer(Modifier.height(52.dp))
                                    AppText.Large24(
                                        text = "Belum ada Antrian",
                                        fontWeight = FontWeight.Medium
                                    )
                                }
                            }
                        }

                        ResponseStatus.FAILED -> {
                            item {
                                Image(
                                    painter = painterResource(R.drawable.order_empty_illustration),
                                    contentDescription = null,
                                )
                                Spacer(Modifier.height(52.dp))
                                AppText.Large24(
                                    text = "Belum ada Antrian",
                                    fontWeight = FontWeight.Medium
                                )
                            }
                        }

                        else -> null
                    }
                }
            }
        }
    }
}

@Composable
private fun OrderCard(
    modifier: Modifier = Modifier,
    order: Order,
    submissionStatus: ResponseStatus,
    onCancelOrder: () -> Unit,
    onSubmitPayment: (Bitmap?) -> Unit,
) {
    val context = LocalContext.current
    var bitmap: Bitmap? by remember { mutableStateOf(null) }
    var fileName by remember { mutableStateOf<String?>(null) }
    val launcher = rememberLauncherForActivityResult(CropImageContract()) { result ->
        bitmap = MediaUtils.getCroppedImage(context.contentResolver, result)
        fileName = result.uriContent?.let { MediaUtils.getFileName(context, it) }
    }

    OutlinedCard(
        modifier = modifier.fillMaxSize(),
        onClick = {},
        colors = outlinedCardColors(containerColor = Color.Transparent),
        border = BorderStroke(2.dp, Color.Black.copy(alpha = .5f))
    ) {
        var imageLoadStatus by remember { mutableStateOf(ResponseStatus.IDLE) }
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp)
        ) {
            Row(
                verticalAlignment = Alignment.CenterVertically
            ) {
                if (order.doctor?.photoUrl != null && order.doctor?.photoUrl!!.isEmpty() || imageLoadStatus == ResponseStatus.FAILED) {
                    Image(
                        modifier = Modifier.size(100.dp),
                        imageVector = ImageVector.vectorResource(id = R.drawable.account_circle),
                        contentDescription = null
                    )
                } else {
                    if (imageLoadStatus == ResponseStatus.LOADING) {
                        AppCircularLoading(
                            color = Color.Black,
                            size = 30.dp,
                            useSpacer = false
                        )
                    }
                    AsyncImage(
                        modifier = Modifier
                            .size(180.dp)
                            .clip(RoundedCornerShape(20.dp)),
                        model = order.doctor?.photoUrl.toString(),
                        contentDescription = "Doctor's Profile Photo",
                        onLoading = {
                            imageLoadStatus = ResponseStatus.LOADING
                        },
                        onError = {
                            imageLoadStatus = ResponseStatus.FAILED
                        },
                        onSuccess = {
                            imageLoadStatus = ResponseStatus.SUCCESS
                        }
                    )
                }
                Spacer(Modifier.width(12.dp))
                Column {
                    Column(
                        modifier = Modifier.width(IntrinsicSize.Max),
                    ) {

                        AppText.Large24(
                            text = order.doctor?.name ?: "-",
                            color = BackButton,
                            fontWeight = FontWeight.SemiBold
                        )
                        HorizontalDivider(
                            modifier = Modifier.padding(vertical = 8.dp),
                            thickness = 2.dp,
                            color = BackButton
                        )
                        AppText.Semi20(
                            text = order.doctor?.employeeData?.type ?: "-",
                            color = AppMediumLightBlueColor
                        )
                    }
                }
            }
            Spacer(Modifier.height(32.dp))
            AppLabel.WithTextData(
                label = "Keluhan:",
                textData = order.description ?: "-"
            )
            AppLabel.WithTextData(
                label = "Alamat:",
                textData = order.client?.address ?: "-"
            )
            AppLabel.WithTextData(
                label = "No Telfon",
                textData = order.client?.phoneNumber ?: "-"
            )
            AppLabel.WithTextData(
                label = "Waktu Janji",
                textData = AppHelper.completeFormat(order.appointmentTime)
            )
            AppLabel.Default(
                label = "Foto Keluhan:",
            )
            Row(
                modifier = Modifier.fillMaxWidth(),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                if (imageLoadStatus == ResponseStatus.LOADING) {
                    AppCircularLoading(
                        color = Color.Black,
                        size = 30.dp,
                        useSpacer = false,
                    )
                } else if (imageLoadStatus == ResponseStatus.FAILED) {
                    Image(
                        modifier = Modifier.size(100.dp),
                        painter = painterResource(R.drawable.baseline_broken_image_24),
                        contentDescription = "Image Load Failed Placeholder",
                    )
                }
                AsyncImage(
                    modifier = Modifier
                        .size(if (imageLoadStatus == ResponseStatus.SUCCESS) 200.dp else 0.dp)
                        .clip(RoundedCornerShape(13.dp))
                        .clickable {
                            MediaUtils.previewFile(context, order.supportingImageUrl!!.toString()) {
                                Toast
                                    .makeText(
                                        context,
                                        "No app found to open this file type",
                                        Toast.LENGTH_SHORT
                                    )
                                    .show()
                            }
                        },
                    model = order.supportingImageUrl.toString(),
                    contentDescription = "Client's Supporting Image",
                    onLoading = {
                        imageLoadStatus = ResponseStatus.LOADING
                    },
                    onError = {
                        imageLoadStatus = ResponseStatus.FAILED
                    },
                    onSuccess = {
                        imageLoadStatus = ResponseStatus.SUCCESS
                    }
                )
                if (imageLoadStatus == ResponseStatus.SUCCESS) {
                    Image(
                        painter = painterResource(R.drawable.doctor_holding_stuff),
                        contentDescription = null
                    )
                }
            }
            Spacer(Modifier.height(12.dp))
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.End,
                verticalAlignment = Alignment.CenterVertically
            ) {
                if (order.paymentReceiptUrl.isNullOrEmpty() && order.status == "UNPAID") {
                    Button(
                        onClick = onCancelOrder,
                        colors = buttonColors(containerColor = AppToscaColor),
                        elevation = ButtonDefaults.buttonElevation(
                            defaultElevation = 4.dp
                        )
                    ) {
                        if (submissionStatus == ResponseStatus.LOADING) {
                            AppCircularLoading(useSpacer = false)
                        } else {
                            AppText.Small15(
                                text = "Batalkan",
                                color = Color.White,
                                fontWeight = FontWeight.SemiBold
                            )
                        }
                    }
                    Spacer(Modifier.width(24.dp))
                }
                when (order.status) {
                    "UNPAID" -> {
                        Button(
                            onClick = {
                                if (order.paymentReceiptUrl.isNullOrEmpty()) {
                                    onSubmitPayment(bitmap)
                                }
                            },
                            colors = buttonColors(containerColor = AppToscaColor),
                            elevation = ButtonDefaults.buttonElevation(
                                defaultElevation = 4.dp
                            )
                        ) {
                            if (submissionStatus == ResponseStatus.LOADING) {
                                AppCircularLoading(useSpacer = false)
                            } else {
                                AppText.Small15(
                                    text = "Administrasi",
                                    color = Color.White,
                                    fontWeight = FontWeight.SemiBold
                                )
                            }
                        }
                    }

                    "ONGOING" -> {
                        Button(
                            onClick = {},
                            colors = buttonColors(containerColor = AppToscaColor),
                            elevation = ButtonDefaults.buttonElevation(
                                defaultElevation = 4.dp
                            )
                        ) {
                            if (submissionStatus == ResponseStatus.LOADING) {
                                AppCircularLoading(useSpacer = false)
                            } else {
                                AppText.Small15(
                                    text = "Confirm",
                                    color = Color.White,
                                    fontWeight = FontWeight.SemiBold
                                )
                            }
                        }
                    }

                    else -> {
                        Spacer(Modifier.height(24.dp))
                        AppText.Regular16(
                            modifier = Modifier.fillMaxWidth(),
                            text = "Pesanan ini telah selesai.",
                            fontWeight = FontWeight.SemiBold,
                            textAlign = TextAlign.Center
                        )
                    }
                }
            }
        }
    }
}