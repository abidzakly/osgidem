package org.d3ifcool.medisgosh.ui.profile

import android.graphics.Bitmap
import android.util.Log
import android.widget.Toast
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
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
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Image
import androidx.compose.material3.BottomSheetDefaults.DragHandle
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.ButtonDefaults.buttonColors
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.ModalBottomSheet
import androidx.compose.material3.pulltorefresh.PullToRefreshBox
import androidx.compose.material3.pulltorefresh.rememberPullToRefreshState
import androidx.compose.material3.rememberModalBottomSheetState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.vectorResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import coil.compose.AsyncImage
import com.canhub.cropper.CropImageContract
import com.canhub.cropper.CropImageContractOptions
import com.canhub.cropper.CropImageOptions
import com.firebase.ui.auth.AuthUI
import com.google.firebase.auth.FirebaseUser
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import org.d3ifcool.medisgosh.R
import org.d3ifcool.medisgosh.component.AppCircularLoading
import org.d3ifcool.medisgosh.component.AppContainer
import org.d3ifcool.medisgosh.component.AppPopUp
import org.d3ifcool.medisgosh.component.AppText
import org.d3ifcool.medisgosh.component.ImageDialog
import org.d3ifcool.medisgosh.ui.theme.AppDanger
import org.d3ifcool.medisgosh.ui.theme.AppLightBlueColor
import org.d3ifcool.medisgosh.util.AppObjectState
import org.d3ifcool.medisgosh.util.MediaUtils

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ProfilePrefixComponent(
    modifier: Modifier = Modifier,
    user: FirebaseUser?,
    onSelectedImage: (ByteArray?) -> Unit,
    onRefreshParams: () -> Unit,
    contentBody: @Composable () -> Unit
) {
    val context = LocalContext.current
    var imageLoadStatus by remember { mutableStateOf(AppObjectState.IDLE) }
    var showModalBottomSheet by remember { mutableStateOf(false) }
    var showLogoutDialog by remember { mutableStateOf(false) }
    var showImgDialog by remember { mutableStateOf(false) }
    val sheetState = rememberModalBottomSheetState(skipPartiallyExpanded = true)
    val scope = rememberCoroutineScope()
    val state = rememberPullToRefreshState()
    var isRefreshing by remember { mutableStateOf(false) }
    val onRefresh: () -> Unit = {
        isRefreshing = true
        scope.launch {
            delay(1000)
            onRefreshParams()
            isRefreshing = false
        }
    }

    var bitmap: Bitmap? by remember { mutableStateOf(null) }
    val launcher = rememberLauncherForActivityResult(CropImageContract()) {
        bitmap = MediaUtils.getCroppedImage(context.contentResolver, it)
        if (bitmap != null) {
            showImgDialog = true
        }
    }

    if (showLogoutDialog) {
        AppPopUp.CustomDialog(
            onDismissRequest = {
                showLogoutDialog = false
            },
            label = "Apakah anda yakin ingin keluar?",
            negativeLabel = "Tidak",
            positiveLabel = "Ya",
            submissionStatus = AppObjectState.IDLE,
        ) {
            AuthUI.getInstance().signOut(context)
        }
    }

    if (showImgDialog) {
        ImageDialog(bitmap = bitmap, onDismissRequest = { showImgDialog = false }) {
            onSelectedImage(MediaUtils.bitmapToByteArray(bitmap!!))
            showImgDialog = false
        }
    }

    if (showModalBottomSheet) {
        ModalBottomSheet(
            scrimColor = Color.Black.copy(alpha = .5f),
            onDismissRequest = { showModalBottomSheet = false },
            sheetState = sheetState,
            containerColor = AppLightBlueColor,
            dragHandle = {
                DragHandle(
                    color = Color.White
                )
            }
        ) {
            Column(
                modifier = Modifier
                    .height(200.dp)
                    .clip(RoundedCornerShape(topStart = 16.dp, topEnd = 16.dp))
                    .fillMaxWidth()
                    .background(
                        Brush.verticalGradient(
                            colors = listOf(
                                AppLightBlueColor,
                                Color.White,
                            )
                        )
                    )
                    .padding(16.dp)
            ) {
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(vertical = 16.dp),
                    horizontalArrangement = Arrangement.SpaceEvenly,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Column(
                        horizontalAlignment = Alignment.CenterHorizontally
                    ) {
                        IconButton(
                            modifier = Modifier
                                .size(50.dp)
                                .padding(bottom = 12.dp),
                            onClick = {
                                val options = CropImageContractOptions(
                                    null, CropImageOptions(
                                        imageSourceIncludeGallery = true,
                                        imageSourceIncludeCamera = false,
                                        fixAspectRatio = true
                                    )
                                )
                                launcher.launch(options)
                            }
                        ) {
                            Icon(
                                modifier = Modifier.size(50.dp),
                                imageVector = Icons.Default.Image,
                                contentDescription = "Gallery Button",
                                tint = Color.Black
                            )
                        }
                        AppText.Regular16(
                            text = "Gallery",
                            color = Color.Black,
                            fontWeight = FontWeight.SemiBold
                        )
                    }
                    Spacer(modifier = Modifier.width(6.dp))
                    Column(
                        horizontalAlignment = Alignment.CenterHorizontally
                    ) {
                        IconButton(
                            modifier = Modifier
                                .size(50.dp)
                                .padding(bottom = 12.dp),
                            onClick = {
                                val options = CropImageContractOptions(
                                    null, CropImageOptions(
                                        imageSourceIncludeGallery = false,
                                        imageSourceIncludeCamera = true,
                                        fixAspectRatio = true
                                    )
                                )
                                launcher.launch(options)
                            }
                        ) {
                            Icon(
                                modifier = Modifier.size(50.dp),
                                painter = painterResource(id = R.drawable.camera),
                                contentDescription = "Camera Button",
                                tint = Color.Black
                            )
                        }
                        AppText.Regular16(
                            text = "Camera",
                            color = Color.Black,
                            fontWeight = FontWeight.SemiBold
                        )
                    }
                }
            }
        }
    }

    AppContainer.Default(
        modifier = modifier
        ,
    ) { innerPadding ->
        PullToRefreshBox(
            modifier = Modifier
                .fillMaxSize()
                .padding(top = innerPadding.calculateTopPadding())
                .padding(horizontal = 24.dp),
            state = state,
            isRefreshing = isRefreshing,
            onRefresh = onRefresh,
        ) {
            LazyColumn(
                modifier = Modifier
                    .fillMaxWidth()
                ,
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.Center,
                contentPadding = PaddingValues(bottom = 36.dp)
            ) {
                item {
                    IconButton(
                        modifier = Modifier.size(100.dp),
                        onClick = {
                            if (user!!.photoUrl != null) {
                                Log.d("Profile Component", "photoUrl: ${user.photoUrl}")
                                MediaUtils.previewFile(context, user.photoUrl!!.toString()) {
                                    Toast.makeText(context, "No app found to open this file type", Toast.LENGTH_SHORT).show()
                                }
                            } else {
                                showModalBottomSheet = true
                            }
                        }
                    ) {
                        if (user!!.photoUrl == null || user.photoUrl!!.toString()
                                .isEmpty() || imageLoadStatus == AppObjectState.FAILED
                        ) {
                            Image(
                                modifier = Modifier.size(100.dp),
                                imageVector = ImageVector.vectorResource(id = R.drawable.account_circle),
                                contentDescription = null
                            )
                        } else {
                            if (imageLoadStatus == AppObjectState.LOADING) {
                                AppCircularLoading(color = Color.Black, size = 30.dp)
                            }
                            AsyncImage(
                                modifier = Modifier
                                    .size(100.dp)
                                    .clip(CircleShape),
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
                    }
                    IconButton(
                        onClick = {
                            showModalBottomSheet = true
                        }
                    ) {
                        Icon(
                            painter = painterResource(R.drawable.camera),
                            contentDescription = "Avatar Option Button",
                            tint = Color.Gray,
                        )
                    }
                    Spacer(Modifier.height(32.dp))
                }
                item {
                    contentBody()
                }
                item {
                    Spacer(Modifier.height(12.dp))
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.End
                    ) {
                        Button(
                            onClick = {
                                showLogoutDialog = true
                            }, colors = buttonColors(containerColor = AppDanger),
                            elevation = ButtonDefaults.buttonElevation(
                                defaultElevation = 4.dp
                            )
                        ) {
                            AppText.Small15(
                                text = "Log-Out",
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
