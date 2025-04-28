package org.d3ifcool.medisgotm.ui.profile

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.ButtonDefaults.buttonColors
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import coil.compose.AsyncImage
import com.google.firebase.auth.FirebaseUser
import org.d3ifcool.medisgosh.component.AppPopUp
import org.d3ifcool.medisgosh.component.AppText
import org.d3ifcool.medisgosh.ui.theme.AppDanger
import org.d3ifcool.medisgosh.ui.theme.AppDarkToscaColor
import org.d3ifcool.medisgosh.ui.theme.AppMediumLightBlueColor
import org.d3ifcool.medisgosh.ui.theme.BackButton
import org.d3ifcool.medisgosh.util.ResponseStatus

@Composable
fun ProfileScreen(modifier: Modifier = Modifier, user: FirebaseUser?, viewModel: ProfileViewModel) {
    val fetchedData by viewModel.profileData
    var showLogoutDialog by remember { mutableStateOf(false) }
    if (showLogoutDialog) {
        AppPopUp.CustomDialog(
            onDismissRequest = {
                showLogoutDialog = false
            },
            label = "Apakah anda yakin ingin keluar?",
            negativeLabel = "Tidak",
            positiveLabel = "Ya",
            submissionStatus = ResponseStatus.IDLE,
        ) {
            viewModel.logout()
        }
    }

    LazyColumn(
        modifier = modifier
            .fillMaxSize()
            .padding(horizontal = 32.dp),
        contentPadding = PaddingValues(top = 32.dp, bottom = 24.dp)
    ) {
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
                            painter = painterResource(org.d3ifcool.medisgosh.R.drawable.profilephoto),
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
                    painter = painterResource(org.d3ifcool.medisgosh.R.drawable.garuda),
                    contentDescription = "Garuda",
                )
                Text(
                    text = fetchedData?.employeeData?.strNumber ?: "-",
                    fontSize = 20.sp
                )
            }
            Spacer(Modifier.height(42.dp))
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