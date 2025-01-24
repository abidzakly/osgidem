package org.d3ifcool.medisgo.ui.service

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavHostController
import coil.compose.AsyncImage
import com.google.firebase.auth.FirebaseUser
import org.d3ifcool.medisgo.R
import org.d3ifcool.medisgosh.component.AppContainer
import org.d3ifcool.medisgosh.component.AppTopBar
import org.d3ifcool.medisgosh.navigation.Screen
import org.d3ifcool.medisgosh.ui.theme.AppDarkGreenColor
import org.d3ifcool.medisgosh.ui.theme.AppDarkToscaColor
import org.d3ifcool.medisgosh.ui.theme.AppMediumLightBlueColor
import org.d3ifcool.medisgosh.ui.theme.AppToscaColor
import org.d3ifcool.medisgosh.ui.theme.BackButton
import org.d3ifcool.medisgosh.util.AppHelper
import org.d3ifcool.medisgosh.util.AppObjectState

@OptIn(ExperimentalMaterial3Api::class, ExperimentalLayoutApi::class)
@Composable
fun ServiceDetailScreen(
    modifier: Modifier = Modifier,
    user: FirebaseUser,
    navController: NavHostController,
    viewModel: ServiceDetailViewModel
) {
    val fetchedData by viewModel.fetchedData
    // Membuat scroll behavior untuk TopAppBar
    AppContainer.WithTopBar(
        modifier = modifier,
        profileImageUrl = user.photoUrl.toString(),
        onProfileClick = {
            AppHelper.navigate(navController, Screen.General.Profile.route)
        }
    ) { paddingValues ->
        LazyColumn(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues)
                .padding(horizontal = 32.dp),
            contentPadding = PaddingValues(top = 32.dp, bottom = 24.dp)
        ) {
            item {
                Row(
                    modifier = Modifier.fillMaxWidth(),
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
            // Bagian Profil
            item {
                Column(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    Box(modifier = Modifier.clip(RoundedCornerShape(42.dp))) {
                        if (fetchedData != null && fetchedData!!.photoUrl != null) {
                            AsyncImage(
                                modifier = Modifier
                                    .size(140.dp)
                                    .clip(RoundedCornerShape(20.dp)),
                                model = fetchedData?.photoUrl.toString(),
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
                    Spacer(Modifier.height(20.dp))
                    Text(
                        text = fetchedData?.name ?: "-",
                        fontSize = 36.sp,
                        color = BackButton,
                        fontWeight = FontWeight.Bold
                    )
                    Spacer(Modifier.height(16.dp))
                    Text(
                        text = fetchedData?.employeeData?.type ?: "-",
                        fontSize = 20.sp,
                        color = AppMediumLightBlueColor,
                        fontWeight = FontWeight.SemiBold,
                        letterSpacing = 21.sp
                    )
                }
                Spacer(Modifier.height(34.dp))
            }
            // Bagian Spesialis
            item {
                Text(
                    text = "Spesialis",
                    fontSize = 20.sp
                )
                Spacer(Modifier.height(15.dp))
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.spacedBy(10.dp)
                ) {
                    Box(
                        modifier = Modifier
                            .clip(RoundedCornerShape(9.dp))
                            .background(AppDarkToscaColor)
                            .padding(6.dp)
                    ) {
                        Text(
                            text = "Dermatologi",
                            fontSize = 15.sp,
                            color = Color.White
                        )
                    }
                    Box(
                        modifier = Modifier
                            .clip(RoundedCornerShape(9.dp))
                            .background(AppDarkToscaColor)
                            .padding(6.dp)
                    ) {
                        Text(
                            text = "General Practioner",
                            fontSize = 15.sp,
                            color = Color.White
                        )
                    }
                }
                Spacer(Modifier.height(42.dp))
            }
            // Bagian STR
            item {
                Text(
                    text = "STR (Surat Tanda Registrasi)",
                    fontSize = 20.sp
                )
                Spacer(Modifier.height(8.dp))
                Row(
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Image(
                        modifier = Modifier.size(33.dp),
                        painter = painterResource(R.drawable.garuda),
                        contentDescription = "Garuda",
                    )
                    Text(
                        text = fetchedData?.employeeData?.strNumber ?: "-",
                        fontSize = 20.sp
                    )
                }
                Spacer(Modifier.height(42.dp))
            }
            // Bagian Pengalaman
            item {
                Text(
                    text = "Pengalaman",
                    fontSize = 20.sp
                )
                Spacer(Modifier.height(16.dp))
                Column(
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(200.dp)
                        .clip(RoundedCornerShape(9.dp))
                        .background(Color.White)
                ) {

                }
                Spacer(Modifier.height(30.dp))
            }
            item {
                Text(
                    text = "Rating",
                    fontSize = 20.sp
                )
                Spacer(Modifier.height(8.dp))
                Text(
                    text = "5,0",
                    fontSize = 96.sp
                )
                Image(
                    modifier = Modifier.size(135.dp, 19.dp),
                    painter = painterResource(R.drawable.star_rating),
                    contentDescription = "Star Rating",
                )
                Spacer(Modifier.height(39.dp))
            }
            item {
                val days = listOf("Senin", "Selasa", "Rabu", "Kamis", "Jumat", "Sabtu", "Minggu")

                Text(
                    text = "Jadwal Ketersediaan",
                    fontSize = 20.sp
                )
                Spacer(Modifier.height(20.dp))
                FlowRow(
                    modifier = Modifier.fillMaxWidth(),
                    maxItemsInEachRow = 4,
                    horizontalArrangement = Arrangement.spacedBy(10.dp),
                    verticalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    days.forEach { day ->
                        // Logika menentukan warna
                        val color =
                            if (day == "Sabtu" || day == "Minggu") Color.Red.copy(alpha = .51f) else AppDarkToscaColor

                        Box(
                            modifier = Modifier
                                .size(68.dp, 23.dp)
                                .clip(RoundedCornerShape(9.dp))
                                .background(color),
                            contentAlignment = Alignment.Center
                        ) {
                            Text(
                                text = day,
                                fontSize = 16.sp,
                                color = Color.White,
                                textAlign = TextAlign.Center
                            )
                        }
                    }
                }
                Spacer(Modifier.height(35.dp))
            }
            item {
                Text(
                    text = "Biaya Layanan",
                    fontSize = 20.sp
                )
                Spacer(Modifier.height(179.dp))
            }
            item {
                Column(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    Button(
                        onClick = {},
                        shape = RoundedCornerShape(10.dp),
                        colors = ButtonDefaults.buttonColors(
                            containerColor = AppDarkToscaColor
                        )
                    ) {
                        Text(
                            text = "GET STARTED",
                            fontSize = 15.sp,
                            color = Color.White
                        )
                    }
                    Spacer(Modifier.height(16.dp))
                }
            }
        }
    }
}