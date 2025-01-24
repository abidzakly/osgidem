package org.d3ifcool.medisgo.ui.service

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
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.staggeredgrid.LazyVerticalStaggeredGrid
import androidx.compose.foundation.lazy.staggeredgrid.StaggeredGridCells
import androidx.compose.foundation.lazy.staggeredgrid.items
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults.buttonColors
import androidx.compose.material3.ButtonDefaults.buttonElevation
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults.cardColors
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Snackbar
import androidx.compose.material3.SnackbarDuration
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.Tab
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
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavHostController
import coil.compose.AsyncImage
import com.google.firebase.auth.FirebaseUser
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import org.d3ifcool.medisgo.R
import org.d3ifcool.medisgosh.component.AppCircularLoading
import org.d3ifcool.medisgosh.component.AppContainer
import org.d3ifcool.medisgosh.component.AppText
import org.d3ifcool.medisgosh.model.User
import org.d3ifcool.medisgosh.navigation.Screen
import org.d3ifcool.medisgosh.ui.theme.AppBlueColor
import org.d3ifcool.medisgosh.ui.theme.AppDarkBlue2Color
import org.d3ifcool.medisgosh.ui.theme.AppDarkBlueColor
import org.d3ifcool.medisgosh.ui.theme.AppDoctorTypeColor
import org.d3ifcool.medisgosh.ui.theme.AppLightNavyColor
import org.d3ifcool.medisgosh.ui.theme.AppToscaColor
import org.d3ifcool.medisgosh.ui.theme.AppWarning
import org.d3ifcool.medisgosh.util.AppHelper
import org.d3ifcool.medisgosh.util.AppObjectState
import java.util.Locale

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ServiceListScreen(
    modifier: Modifier = Modifier,
    user: FirebaseUser?,
    navController: NavHostController,
    viewModel: ServiceListViewModel
) {
    val fetchedData by viewModel.fetchedData
    val fetchStatus by viewModel.fetchStatus
    val userProfileData by viewModel.userProfileData
    var filteredData by remember { mutableStateOf(fetchedData) }
    var selectedTabIndex by remember { mutableIntStateOf(0) }
    var snackbarHostState = remember { SnackbarHostState() }
    val scope = rememberCoroutineScope()
    val state = rememberPullToRefreshState()
    var isRefreshing by remember { mutableStateOf(false) }
    val onRefresh: () -> Unit = {
        isRefreshing = true
        scope.launch {
            delay(1000)
            viewModel.observeList()
            isRefreshing = false
        }
    }

    LaunchedEffect(selectedTabIndex) {
        if (fetchedData != null && fetchedData!!.isNotEmpty()) {
            when (selectedTabIndex) {
                0 -> {
                    filteredData = fetchedData?.filter {
                        it.employeeData!!.type.lowercase(Locale.getDefault()).contains("dokter")
                    }
                }

                1 -> {
                    filteredData = fetchedData?.filter {
                        it.employeeData!!.type.lowercase(Locale.getDefault()).contains("perawat")
                    }
                }

                2 -> {
                    filteredData = fetchedData?.filter {
                        it.employeeData!!.type.lowercase(Locale.getDefault())
                            .contains("fisioterapi")
                    }
                }

                3 -> {
                    filteredData = fetchedData?.filter {
                        it.employeeData!!.type.lowercase(Locale.getDefault()).contains("bidan")
                    }
                }
            }
        }
    }

    AppContainer.WithTopBar(
        snackbarHost = {
            SnackbarHost(
                hostState = snackbarHostState,
            ) {
                Box(
                    modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center
                ) {
                    Snackbar(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(horizontal = 30.dp)
                            .clip(RoundedCornerShape(16.dp))
                            .clickable {
                                AppHelper.navigate(navController, Screen.General.Profile.route)
                            }
                        ,
                        containerColor = AppWarning
                    ) {
                        Box(
                            modifier = Modifier.fillMaxWidth(),
                            contentAlignment = Alignment.Center
                        ) {
                            Text(
                                text = it.visuals.message,
                                style = MaterialTheme.typography.bodyMedium,
                                color = Color.White,
                                fontWeight = FontWeight.SemiBold,
                                textAlign = TextAlign.Center
                            )
                        }
                    }
                }
            }
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
                .padding(top = innerPadding.calculateTopPadding()),
            state = state,
            isRefreshing = isRefreshing,
            onRefresh = onRefresh,
        ) {
            Column(
                modifier = Modifier
                    .fillMaxSize(),
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.Top,
            ) {
                Spacer(Modifier.height(30.dp))
                AppText.Semi20(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(start = 8.dp),
                    text = "KATEGORI TENAGA MEDIS",
                    fontWeight = FontWeight.ExtraBold,
                    color = AppDarkBlue2Color
                )
                Spacer(Modifier.height(20.dp))
                LazyRow(
                    horizontalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    items(4) { index ->
                        Box(
                            modifier = Modifier
                                .clip(RoundedCornerShape(30.dp))
                                .background(if (selectedTabIndex == index) AppToscaColor else Color.White)
                                .padding(horizontal = 16.dp, vertical = 4.dp)
                        ) {
                            Tab(
                                selected = selectedTabIndex == index,
                                onClick = { selectedTabIndex = index },
                                text = {
                                    Column(
                                        horizontalAlignment = Alignment.CenterHorizontally,
                                        verticalArrangement = Arrangement.Center
                                    ) {
                                        Icon(
                                            painter = when (index) {
                                                0 -> painterResource(R.drawable.dokter_category)
                                                1 -> painterResource(R.drawable.perawat_category)
                                                2 -> painterResource(R.drawable.fisioterapi_category)
                                                3 -> painterResource(R.drawable.bidan_category)
                                                else -> painterResource(R.drawable.perawat_category)
                                            },
                                            contentDescription = null,
                                            tint = Color.Unspecified
                                        )
                                        Spacer(Modifier.height(8.dp))
                                        AppText.Custom(
                                            fontSize = 14.sp,
                                            modifier = Modifier.fillMaxWidth(),
                                            text = when (index) {
                                                0 -> "DOKTER"
                                                1 -> "PERAWAT"
                                                2 -> "FISIOTERAPI"
                                                3 -> "BIDAN"
                                                else -> ""
                                            },
                                            color = AppDarkBlue2Color,
                                            fontWeight = FontWeight.Black,
                                            textAlign = TextAlign.Center
                                        )
                                    }
                                },
                                selectedContentColor = if (index == 0) AppToscaColor else AppBlueColor
                            )
                        }
                    }
                }
                Spacer(Modifier.height(20.dp))
                when (fetchStatus) {
                    AppObjectState.LOADING -> {
                        AppCircularLoading(
                            color = Color.Black,
                            useSpacer = false
                        )
                    }

                    AppObjectState.SUCCESS -> {
                        if (!filteredData.isNullOrEmpty()) {
                            LazyVerticalStaggeredGrid(
                                modifier = Modifier
                                    .fillMaxSize()
                                    .padding(horizontal = 12.dp),
                                columns = StaggeredGridCells.Fixed(2),
                                verticalItemSpacing = 8.dp,
                                horizontalArrangement = Arrangement.spacedBy(8.dp),
                                contentPadding = PaddingValues(8.dp, 8.dp, 8.dp, 84.dp)
                            ) {
                                items(filteredData!!) {
                                    ItemCard(user = it, onDetail = {
                                        AppHelper.navigate(
                                            navController = navController,
                                            Screen.Client.ServiceDetail.withData(id = it.id!!)
                                        )
                                    }) {
                                        if (userProfileData != null) {
                                            AppHelper.navigate(
                                                navController = navController,
                                                Screen.Client.FillOrder.withData(id = it.id!!)
                                            )
                                        } else {
                                            scope.launch {
                                                snackbarHostState.showSnackbar(
                                                    message = "Harap isi data diri kamu dulu, yaa! atau klik disini",
                                                    duration = SnackbarDuration.Short,
                                                )
                                            }
                                        }
                                    }
                                }
                            }
                        } else {
                            Column(
                                modifier = modifier.fillMaxSize().verticalScroll(rememberScrollState()),
                                horizontalAlignment = Alignment.CenterHorizontally,
                                verticalArrangement = Arrangement.Center
                            ) {
                                AppText.Regular16(
                                    text = "Belum ada data",
                                    fontWeight = FontWeight.Bold
                                )
                            }
                        }
                    }

                    AppObjectState.FAILED -> {
                        AppText.Large24(text = "Data Empty")
                    }

                    else -> null
                }
            }
        }
    }
}

@Composable
private fun ItemCard(
    modifier: Modifier = Modifier,
    user: User,
    onDetail: () -> Unit,
    onOrder: () -> Unit
) {
    var imageLoadStatus by remember { mutableStateOf(AppObjectState.IDLE) }

    Card(
        colors = cardColors(containerColor = AppLightNavyColor),
        shape = RoundedCornerShape(16.dp)
    ) {
        Column(
            modifier = Modifier
                .padding(15.dp)
        ) {
            if (user.photoUrl != null && user.photoUrl!!.isEmpty() || imageLoadStatus == AppObjectState.FAILED) {
                AppText.Small12(
                    text = "Foto tidak ditemukan"
                )
            } else {
                if (imageLoadStatus == AppObjectState.LOADING) {
                    AppCircularLoading(color = Color.White, size = 30.dp, useSpacer = false)
                }
                AsyncImage(
                    modifier = Modifier
                        .fillMaxWidth()
                        .clip(RoundedCornerShape(20.dp)),
                    model = user.photoUrl.toString(),
                    contentDescription = "User's Profile Photo",
                    onLoading = {
                        imageLoadStatus = AppObjectState.LOADING
                    },
                    onError = {
                        imageLoadStatus = AppObjectState.FAILED
                    },
                    onSuccess = {
                        imageLoadStatus = AppObjectState.SUCCESS
                    }
                )
            }
            Spacer(Modifier.height(16.dp))
            Column(
                modifier = Modifier.width(IntrinsicSize.Max)
            ) {
                AppText.Regular16(
                    text = user.name ?: "",
                    color = Color.White,
                    fontWeight = FontWeight.Bold
                )
                HorizontalDivider(
                    modifier = Modifier
                        .padding(vertical = 8.dp),
                    color = AppDarkBlueColor
                )
                AppText.Small14(
                    text = user.employeeData?.type ?: "",
                    color = AppDoctorTypeColor,
                    fontWeight = FontWeight.SemiBold
                )
            }
            Spacer(Modifier.height(10.dp))
            Row(
                modifier = Modifier
                    .fillMaxWidth(),
                horizontalArrangement = Arrangement.End,
                verticalAlignment = Alignment.CenterVertically,
            ) {
                Button(
                    contentPadding = PaddingValues(0.dp),
                    onClick = onDetail,
                    colors = buttonColors(containerColor = AppToscaColor),
                    elevation = buttonElevation(defaultElevation = 5.dp),
                    shape = RoundedCornerShape(10.dp)
                ) {
                    AppText.Small12(
                        text = "Detail",
                        color = Color.White,
                        fontWeight = FontWeight.Medium
                    )
                }
                Spacer(Modifier.width(10.dp))
                Button(
                    contentPadding = PaddingValues(0.dp),
                    onClick = onOrder,
                    colors = buttonColors(containerColor = AppToscaColor),
                    elevation = buttonElevation(defaultElevation = 5.dp),
                    shape = RoundedCornerShape(10.dp)
                ) {
                    AppText.Small12(
                        text = "Order",
                        color = Color.White,
                        fontWeight = FontWeight.Medium
                    )
                }
            }
        }
    }
}