package org.d3ifcool.medisgosh.ui.home

import androidx.compose.animation.AnimatedContent
import androidx.compose.animation.ExperimentalAnimationApi
import androidx.compose.animation.slideInHorizontally
import androidx.compose.animation.slideOutHorizontally
import androidx.compose.animation.with
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
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.ButtonDefaults.buttonElevation
import androidx.compose.material3.HorizontalDivider
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.navigation.NavHostController
import com.google.firebase.auth.FirebaseUser
import kotlinx.coroutines.delay
import org.d3ifcool.medisgosh.R
import org.d3ifcool.medisgosh.component.AppContainer
import org.d3ifcool.medisgosh.component.AppText
import org.d3ifcool.medisgosh.navigation.Screen
import org.d3ifcool.medisgosh.ui.theme.AppDarkBlueColor
import org.d3ifcool.medisgosh.ui.theme.AppToscaColor
import org.d3ifcool.medisgosh.util.AppHelper

@Composable
fun HomeScreen(
    modifier: Modifier = Modifier,
    isEmployee: Boolean,
    user: FirebaseUser?,
    navController: NavHostController,
    onClicked: () -> Unit
) {
    val images = listOf(
        R.drawable.default_slide,
        R.drawable.slide_2,
        R.drawable.slide_3,
        R.drawable.slide_4,
    )
    var currentIndex by remember { mutableIntStateOf(0) }

    AppContainer.WithTopBar(
        modifier = modifier
        ,
        profileImageUrl = user?.photoUrl.toString(),
        onProfileClick = {
            AppHelper.navigate(navController, Screen.General.Profile.route)
        },
    ) { innerPadding ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding)
                .padding(horizontal = 24.dp)
            ,
            verticalArrangement = Arrangement.Center,
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            if (!isEmployee) {
                AppText.Large24(
                    text = "Tenaga Medis Home Care",
                    color = AppDarkBlueColor,
                    fontWeight = FontWeight.SemiBold
                )
                Spacer(Modifier.height(21.dp))
                HorizontalDivider(
                    modifier = Modifier.fillMaxWidth(.75f),
                    color = AppDarkBlueColor
                )
                Spacer(Modifier.height(64.dp))
            }
            AutoScrollingBanner(
                contentImage = images
            ) {
                currentIndex = it
            }
            Spacer(Modifier.height(16.dp))
            PagerIndicator(
                currentPage = currentIndex,
                pageCount = images.size

            )
            Spacer(Modifier.height(16.dp))
            AppText.Small14(
                modifier = Modifier.fillMaxWidth(),
                text = if (isEmployee) stringResource(R.string.home_text_tm) else stringResource(
                    R.string.home_text_client
                ),
                color = AppDarkBlueColor,
                fontWeight = FontWeight.Medium,
                textAlign = TextAlign.Center
            )
            Spacer(Modifier.height(24.dp))
            Button(
                onClick = onClicked,
                colors = ButtonDefaults.buttonColors(
                    containerColor = AppToscaColor,
                    contentColor = Color.White
                ),
                elevation = buttonElevation(
                    defaultElevation = 4.dp
                )
            ) {
                AppText.Small14(
                    text = if (isEmployee) stringResource(R.string.button_lihat_antrian) else stringResource(
                        R.string.button_buat_janji
                    ),
                    color = Color.White,
                    fontWeight = FontWeight.SemiBold,
                )
            }
        }
    }
}

@OptIn(ExperimentalAnimationApi::class)
@Composable
fun AutoScrollingBanner(
    contentImage: List<Int>,
    onIndexChange: (Int) -> Unit
) {

    var currentIndex by remember { mutableStateOf(0) }

    LaunchedEffect(Unit) {
        while (true) {
            delay(3000)
            currentIndex = (currentIndex + 1) % contentImage.size
            onIndexChange(currentIndex)
        }
    }

    AnimatedContent(
        targetState = contentImage[currentIndex],
        transitionSpec = {
            slideInHorizontally(initialOffsetX = { it }) with slideOutHorizontally(targetOffsetX = { -it })
        }
    ) { image ->
        Image(
            painter = painterResource(id = image),
            contentDescription = "Motivational Image",
            modifier = Modifier
                .fillMaxWidth()
                .height(200.dp),
            contentScale = ContentScale.Crop
        )
    }
}

@Composable
fun PagerIndicator(modifier: Modifier = Modifier, currentPage: Int, pageCount: Int) {
    Row(modifier = modifier, horizontalArrangement = Arrangement.spacedBy(8.dp)) {
        repeat(pageCount) { index ->
            Box(
                modifier = Modifier
                    .size(10.dp)
                    .background(
                        color = if (index == currentPage) Color.Black else Color.Gray,
                        shape = CircleShape
                    )
            )
        }
    }
}