package com.m7md7sn.dentel.presentation.ui.profile.components

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalLayoutDirection
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.Font
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.LayoutDirection
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import coil.compose.AsyncImage
import com.m7md7sn.dentel.R
import com.m7md7sn.dentel.data.model.User
import com.m7md7sn.dentel.presentation.theme.DentelDarkPurple

/**
 * Header component for the Profile screen
 */
@Composable
fun ProfileHeader(
    modifier: Modifier = Modifier,
    user: User
) {
    Box(
        modifier = modifier
            .fillMaxWidth(),
    ) {
        CompositionLocalProvider(LocalLayoutDirection provides LayoutDirection.Ltr) {
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .align(Alignment.TopCenter),
            ) {
                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(170.dp)
                        .background(
                            color = Color(0xFF421882),
                            shape = RoundedCornerShape(
                                bottomStart = 50.dp,
                            )
                        )
                ) {
                    Image(
                        painter = painterResource(id = R.drawable.profile_tooth_bg),
                        contentDescription = null,
                        modifier = Modifier
                            .align(Alignment.BottomCenter)
                            .alpha(0.3f)
                    )
                }
                Box(
                    modifier = Modifier
                        .size(40.dp)
                        .background(
                            color = DentelDarkPurple,
                        )
                        .background(
                            color = Color.White,
                            shape = RoundedCornerShape(topEnd = 40.dp)
                        )
                        .align(Alignment.End)
                )
                Spacer(modifier = Modifier.height(50.dp))
            }
        }
        ProfilePictureWithName(
            modifier = Modifier
                .align(Alignment.BottomCenter),
            userName = user.name,
            profilePictureUrl = user.photoUrl
        )
    }
}

/**
 * Profile picture and name component
 */
@Composable
fun ProfilePictureWithName(
    modifier: Modifier = Modifier,
    userName: String,
    profilePictureUrl: String?
) {
    Column(
        modifier = modifier,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Card(
            modifier = Modifier
                .size(160.dp),
            shape = CircleShape,
            elevation = CardDefaults.cardElevation(
                defaultElevation = 4.dp
            ),
            colors = CardDefaults.cardColors(
                containerColor = Color.White
            ),
            border = BorderStroke(
                width = 4.dp,
                color = Color.White
            )
        ) {
            AsyncImage(
                model = profilePictureUrl,
                contentDescription = "Profile Picture",
                contentScale = ContentScale.Crop,
                modifier = Modifier
                    .fillMaxSize()
                    .background(
                        color = Color.White,
                        shape = CircleShape
                    ),
                fallback = painterResource(id = R.drawable.male_avatar),
                error = painterResource(id = R.drawable.male_avatar)
            )
        }
        Spacer(modifier = Modifier.height(8.dp))
        Text(
            text = userName,
            style = TextStyle(
                fontSize = 24.sp,
                fontFamily = FontFamily(Font(R.font.din_next_lt_bold)),
                fontWeight = FontWeight(500),
                color = Color(0xFF421882),
                textAlign = TextAlign.Center,
            )
        )
    }
}
