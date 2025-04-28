package org.d3ifcool.medisgotm.ui.order

import android.widget.Toast
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
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.ButtonDefaults.buttonColors
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults.cardColors
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.SnackbarDuration
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
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.vectorResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.Dialog
import androidx.navigation.NavHostController
import coil.compose.AsyncImage
import com.google.firebase.auth.FirebaseUser
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import org.d3ifcool.medisgosh.R
import org.d3ifcool.medisgosh.component.AppCircularLoading
import org.d3ifcool.medisgosh.component.AppContainer
import org.d3ifcool.medisgosh.component.AppLabel
import org.d3ifcool.medisgosh.component.AppText
import org.d3ifcool.medisgosh.model.Order
import org.d3ifcool.medisgosh.navigation.Screen
import org.d3ifcool.medisgosh.ui.theme.AppDarkBlueColor
import org.d3ifcool.medisgosh.ui.theme.AppLightNavyColor
import org.d3ifcool.medisgosh.ui.theme.AppMediumLightBlueColor
import org.d3ifcool.medisgosh.ui.theme.AppToscaColor
import org.d3ifcool.medisgosh.ui.theme.BackButton
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
    var selectedOrder by remember { mutableStateOf<Order?>(null) }

    var showOrderDialog by remember { mutableStateOf(false) }
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

    if (showOrderDialog && selectedOrder != null) {
        OrderDialog(
            onDismissRequest = {
                showOrderDialog = false
            },
            order = selectedOrder!!,
            submissionStatus = submissionStatus
        ) {
            viewModel.markAsDone(selectedOrder!!)
        }
    }

    LaunchedEffect(submissionStatus) {
        when (submissionStatus) {
            ResponseStatus.SUCCESS -> {
                FlashMessageHelper.showSuccess(
                    snackbarHostState, coroutineScope,
                    submissionStatus.message ?: "Berhasil",
                )
                if (showOrderDialog) {
                    showOrderDialog = false
                }
                viewModel.reset()
            }

            ResponseStatus.FAILED -> {
                FlashMessageHelper.showSuccess(
                    snackbarHostState, coroutineScope,
                    submissionStatus.message ?: "Gagal",
                )
                if (showOrderDialog) {
                    showOrderDialog = false
                }
                viewModel.reset()
            }

            else -> {}
        }
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

            LazyColumn(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(innerPadding),
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = if (fetchStatus == ResponseStatus.SUCCESS) Arrangement.Top else Arrangement.Center,
                contentPadding = PaddingValues(vertical = 36.dp)
            ) {
                when (fetchStatus) {
                    ResponseStatus.LOADING -> {
                        item {
                            AppCircularLoading(color = AppDarkBlueColor, useSpacer = false)
                        }
                    }

                    ResponseStatus.SUCCESS -> {
                        if (!fetchedData.isNullOrEmpty())
                            items(fetchedData!!) {
                                OrderCard(
                                    order = it
                                ) {
                                    selectedOrder = it
                                    showOrderDialog = true
                                }
                            }
                        else
                            item {
                                Image(
                                    modifier = Modifier.size(60.dp),
                                    painter = painterResource(org.d3ifcool.medisgotm.R.drawable.order_empty_illustration),
                                    contentDescription = null,
                                )
                                Spacer(Modifier.height(52.dp))
                                AppText.Regular16(
                                    text = "Belum ada Antrian",
                                    fontWeight = FontWeight.Medium
                                )
                            }
                    }

                    ResponseStatus.FAILED -> {
                        item {
                            Image(
                                modifier = Modifier.size(60.dp),
                                painter = painterResource(org.d3ifcool.medisgotm.R.drawable.order_empty_illustration),
                                contentDescription = null,
                            )
                            Spacer(Modifier.height(52.dp))
                            AppText.Regular16(
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

@Composable
private fun OrderCard(
    modifier: Modifier = Modifier,
    order: Order,
    onClick: () -> Unit
) {
    Column(
        modifier = Modifier.fillMaxWidth(),
        horizontalAlignment = Alignment.CenterHorizontally,
    ) {
        Card(
            modifier = modifier
                .fillMaxWidth()
                .padding(horizontal = 15.dp),
            onClick = onClick,
            colors = cardColors(
                containerColor = AppLightNavyColor
            ),
            shape = RoundedCornerShape(15.dp)
        ) {
            var imageLoadStatus by remember { mutableStateOf(ResponseStatus.IDLE) }
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(12.dp),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                ) {
                    if (order.client?.photoUrl != null && order.client?.photoUrl!!.isEmpty() || imageLoadStatus == ResponseStatus.FAILED) {
                        Image(
                            imageVector = ImageVector.vectorResource(id = R.drawable.account_circle),
                            contentDescription = null
                        )
                    } else {
                        if (imageLoadStatus == ResponseStatus.LOADING) {
                            AppCircularLoading(color = Color.White, size = 30.dp)
                        }
                        AsyncImage(
                            modifier = Modifier
                                .size(56.dp)
                                .clip(CircleShape),
                            model = order.client?.photoUrl.toString(),
                            contentDescription = "User's Profile Photo",
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
                    Spacer(Modifier.width(14.dp))
                    Column {
                        AppText.Small15(
                            text = order.client?.name ?: "",
                            fontWeight = FontWeight.SemiBold,
                            color = Color.White,
                        )
                        Spacer(Modifier.width(4.dp))
                        AppText.Small15(
                            text = AppHelper.hhmmFormat(order.appointmentTime) ?: "",
                            fontWeight = FontWeight.SemiBold,
                            color = Color.White,
                        )
                        Spacer(Modifier.width(4.dp))
                        AppText.Small15(
                            text = AppHelper.stripeFormat(order.appointmentTime) ?: "",
                            fontWeight = FontWeight.SemiBold,
                            color = Color.White,
                        )
                    }
                }
                Icon(
                    modifier = Modifier.size(30.dp),
                    painter = painterResource(org.d3ifcool.medisgotm.R.drawable.order_illustration),
                    contentDescription = null,
                    tint = Color.Unspecified,
                )
            }
        }
        Spacer(Modifier.height(20.dp))
    }
}

@Composable
private fun OrderDialog(
    modifier: Modifier = Modifier,
    onDismissRequest: () -> Unit,
    order: Order,
    submissionStatus: ResponseStatus,
    onMarkAsDone: () -> Unit,
) {
    val context = LocalContext.current
    Dialog(
        onDismissRequest = onDismissRequest
    ) {
        Box(
            modifier = Modifier
                .fillMaxSize()
                .background(Color.White) // Optional: For background dimming
                .clickable(onClick = onDismissRequest) // Dismiss when clicking outside the dialog
        ) {
            Card(
                modifier = Modifier.fillMaxSize(),
                onClick = {},
                colors = cardColors(containerColor = Color.White),
            ) {
                var imageLoadStatus by remember { mutableStateOf(ResponseStatus.IDLE) }
                Column(
                    modifier = Modifier
                        .fillMaxSize()
                        .padding(16.dp)
                ) {
                    Row(
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        if (order.client?.photoUrl != null && order.client?.photoUrl!!.isEmpty() || imageLoadStatus == ResponseStatus.FAILED) {
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
                                model = order.client?.photoUrl.toString(),
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
                                    text = order.client?.name ?: "-",
                                    color = BackButton,
                                    fontWeight = FontWeight.SemiBold
                                )
                                HorizontalDivider(
                                    modifier = Modifier.padding(vertical = 8.dp),
                                    thickness = 2.dp,
                                    color = BackButton
                                )
                                AppText.Semi20(
                                    text = (if (order.client?.dateOfBirth != null) AppHelper.calculateAge(
                                        order.client?.dateOfBirth!!
                                    ) else "-").toString() + " Tahun",
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
                                    MediaUtils.previewFile(
                                        context,
                                        order.supportingImageUrl!!.toString()
                                    ) {
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
                        Button(
                            onClick = onDismissRequest,
                            colors = buttonColors(containerColor = AppToscaColor),
                            elevation = ButtonDefaults.buttonElevation(
                                defaultElevation = 4.dp
                            )
                        ) {
                            if (submissionStatus == ResponseStatus.LOADING) {
                                AppCircularLoading(useSpacer = false)
                            } else {
                                AppText.Small15(
                                    text = "Tutup",
                                    color = Color.White,
                                    fontWeight = FontWeight.SemiBold
                                )
                            }
                        }
                        Spacer(Modifier.width(24.dp))
                        Button(
                            onClick = onMarkAsDone,
                            colors = buttonColors(containerColor = AppToscaColor),
                            elevation = ButtonDefaults.buttonElevation(
                                defaultElevation = 4.dp
                            )
                        ) {
                            if (submissionStatus == ResponseStatus.LOADING) {
                                AppCircularLoading(useSpacer = false)
                            } else {
                                AppText.Small15(
                                    text = "Selesai",
                                    color = Color.White,
                                    fontWeight = FontWeight.SemiBold
                                )
                            }
                        }
                    }
                }
            }
        }
    }
}