package org.d3ifcool.medisgosh.component

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.FabPosition
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import androidx.navigation.NavHostController
import org.d3ifcool.medisgosh.R
import org.d3ifcool.medisgosh.ui.theme.AppLightBlueColor
import org.d3ifcool.medisgosh.util.AppHelper
import org.d3ifcool.medisgosh.util.ResponseStatus

class AppContainer {
    companion object {
        @Composable
        fun Default(
            modifier: Modifier = Modifier,
            topBar: @Composable () -> Unit = {},
            bottomBar: @Composable () -> Unit = {},
            snackbarHost: @Composable () -> Unit = {},
            floatingActionButton: @Composable () -> Unit = {},
            floatingActionButtonPosition: FabPosition = FabPosition.End,
            content: @Composable (PaddingValues) -> Unit
        ) {
            Box(
                modifier = Modifier
                    .background(
                        Brush.verticalGradient(
                            colors = listOf(
                                Color.White,
                                AppLightBlueColor
                            )
                        )
                    )
            ) {
                Scaffold(
                    modifier = modifier,
                    topBar = topBar,
                    bottomBar = bottomBar,
                    snackbarHost = snackbarHost,
                    floatingActionButton = floatingActionButton,
                    floatingActionButtonPosition = floatingActionButtonPosition,
                    containerColor = Color.Transparent,
                    content = content
                )
            }
        }

        @Composable
        fun WithTopBar(
            profileImageUrl: String,
            modifier: Modifier = Modifier,
            topBarModifier: Modifier = Modifier,
            bottomBar: @Composable () -> Unit = {},
            snackbarHost: @Composable () -> Unit = {},
            floatingActionButton: @Composable () -> Unit = {},
            floatingActionButtonPosition: FabPosition = FabPosition.End,
            onProfileClick: () -> Unit,
            content: @Composable (PaddingValues) -> Unit
        ) {
            var imageLoadStatus by remember { mutableStateOf(ResponseStatus.IDLE) }

            Box(
                modifier = Modifier
                    .background(
                        Brush.verticalGradient(
                            colors = listOf(
                                Color.White,
                                AppLightBlueColor
                            )
                        )
                    )
            ) {
                Scaffold(
                    modifier = modifier,
                    topBar = {
                        AppTopBar(
                            modifier = topBarModifier.padding(horizontal = 12.dp),
                            imageUrl = profileImageUrl,
                            state = imageLoadStatus,
                            onStateChange = { imageLoadStatus = it }
                        ) {
                            onProfileClick()
                        }
                    },
                    bottomBar = bottomBar,
                    snackbarHost = snackbarHost,
                    floatingActionButton = floatingActionButton,
                    floatingActionButtonPosition = floatingActionButtonPosition,
                    containerColor = Color.Transparent,
                    content = content
                )
            }
        }

        @Composable
        fun WithBottomBar(
            isEmployee: Boolean,
            isHideBottomBar: Boolean = false,
            navController: NavHostController,
            modifier: Modifier = Modifier,
            bottomBarModifier: Modifier = Modifier,
            topBar: @Composable () -> Unit = {},
            snackbarHost: @Composable () -> Unit = {},
            useFab: Boolean = true,
            floatingActionButtonPosition: FabPosition = FabPosition.End,
            content: @Composable (PaddingValues) -> Unit
        ) {
            val test = LocalContext.current

            Box(
                modifier = Modifier
                    .background(
                        Brush.verticalGradient(
                            colors = listOf(
                                Color.White,
                                AppLightBlueColor
                            )
                        )
                    )
            ) {
                Scaffold(
                    modifier = modifier,
                    topBar = topBar,
                    bottomBar = {
                        if (!isHideBottomBar) {
                            AppBottomBar(navController, isEmployee)
                        }
                    },
                    snackbarHost = snackbarHost,
                    floatingActionButton = {
                        if (useFab && !isEmployee) {
                            FloatingActionButton(
                                onClick = {
                                    AppHelper.openWhatsAppWithTemplate(
                                        context = test,
                                        phoneNumber = "+6285723375324",
                                        message = "Hello, I would like to inquire about your services!"
                                    )
                                },
                                containerColor = Color.Green,
                                content = {
                                    Icon(
                                        painter = painterResource(R.drawable.whatsapp),
                                        contentDescription = "Open WhatsApp",
                                        tint = Color.White
                                    )
                                }
                            )
                        }
                    },
                    floatingActionButtonPosition = floatingActionButtonPosition,
                    containerColor = Color.Transparent,
                    content = content
                )
            }
        }

        @Composable
        fun WithTopAndBottomBar(
            isHideTopBar: Boolean = false,
            isHideBottomBar: Boolean = false,
            isEmployee: Boolean,
            navController: NavHostController,
            profileImageUrl: String,
            modifier: Modifier = Modifier,
            topBarModifier: Modifier = Modifier,
            snackbarHost: @Composable () -> Unit = {},
            floatingActionButton: @Composable () -> Unit = {},
            floatingActionButtonPosition: FabPosition = FabPosition.End,
            onProfileClick: () -> Unit,
            content: @Composable (PaddingValues) -> Unit
        ) {
            var imageLoadStatus by remember { mutableStateOf(ResponseStatus.IDLE) }

            Box(
                modifier = Modifier
                    .background(
                        Brush.verticalGradient(
                            colors = listOf(
                                Color.White,
                                AppLightBlueColor
                            )
                        )
                    )
            ) {
                Scaffold(
                    modifier = modifier,
                    topBar = {
                        if (!isHideTopBar) {
                            AppTopBar(
                                modifier = topBarModifier,
                                imageUrl = profileImageUrl,
                                state = imageLoadStatus,
                                onStateChange = { imageLoadStatus = it }
                            ) {
                                onProfileClick()
                            }
                        }
                    },
                    bottomBar = {
                        if (!isHideBottomBar) {
                            AppBottomBar(navController, isEmployee)
                        }
                    },
                    snackbarHost = snackbarHost,
                    floatingActionButton = floatingActionButton,
                    floatingActionButtonPosition = floatingActionButtonPosition,
                    containerColor = Color.Transparent,
                    content = content
                )
            }
        }
    }
}