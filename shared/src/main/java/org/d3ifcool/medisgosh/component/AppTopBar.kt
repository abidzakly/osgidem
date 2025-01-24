package org.d3ifcool.medisgosh.component

import androidx.compose.foundation.Image
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.IconButton
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.res.vectorResource
import androidx.compose.ui.unit.dp
import coil.compose.AsyncImage
import org.d3ifcool.medisgosh.R
import org.d3ifcool.medisgosh.util.AppObjectState

@Composable
fun AppTopBar(
    modifier: Modifier = Modifier,
    imageUrl: String,
    state: AppObjectState,
    onStateChange: (AppObjectState) -> Unit,
    onProfileClicked: () -> Unit,
) {
    Row(
        modifier = modifier.fillMaxWidth(),
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.SpaceBetween
    ) {
        Image(
            modifier = Modifier.size(74.dp, 81.dp),
            imageVector = ImageVector.vectorResource(id = R.drawable.app_logo_default),
            contentDescription = null
        )
        Box(
            modifier = Modifier
                .size(56.dp)
                .clip(CircleShape)
                .clickable {
                    onProfileClicked()
                },
            contentAlignment = Alignment.Center
        ) {
            if (imageUrl.isEmpty() || state == AppObjectState.FAILED) {
                Image(
                    modifier = Modifier
                        .size(56.dp)
                        .clip(CircleShape),
                    imageVector = ImageVector.vectorResource(id = R.drawable.account_circle),
                    contentDescription = null
                )
            } else {
                if (state == AppObjectState.LOADING) {
                    AppCircularLoading(color = Color.Black, size = 30.dp)
                }
                AsyncImage(
                    modifier = Modifier
                        .size(56.dp)
                        .clip(CircleShape),
                    model = imageUrl,
                    contentDescription = "User's Profile Photo",
                    onLoading = {
                        onStateChange(AppObjectState.LOADING)
                    },
                    onError = {
                        onStateChange(AppObjectState.FAILED)
                    },
                    onSuccess = {
                        onStateChange(AppObjectState.SUCCESS)
                    }
                )
            }
        }
    }
}