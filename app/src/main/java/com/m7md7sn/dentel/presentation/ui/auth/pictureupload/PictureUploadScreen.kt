package com.m7md7sn.dentel.presentation.ui.auth.pictureupload

import androidx.annotation.DrawableRes
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.IconButton
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Color.Companion.White
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.Font
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.m7md7sn.dentel.R
import com.m7md7sn.dentel.presentation.common.components.CommonLargeButton
import com.m7md7sn.dentel.presentation.common.components.FullDentelHeader
import com.m7md7sn.dentel.presentation.theme.DentelDarkPurple
import com.m7md7sn.dentel.presentation.theme.DentelTheme

@Composable
fun PictureUploadScreen(modifier: Modifier = Modifier) {
    Surface(
        modifier = modifier.fillMaxSize(),
        color = DentelDarkPurple
    ) {
        Column(
            modifier = Modifier.fillMaxSize(),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center
        ) {
            FullDentelHeader(
                modifier = Modifier.weight(0.30f),
            )
            PictureUploadScreenContent(
                modifier = Modifier.weight(0.7f),
            )
        }
    }
}

@Composable
fun PictureUploadScreenContent(modifier: Modifier = Modifier) {
    Surface(
        color = White,
        modifier = modifier.fillMaxWidth(),
        shape = RoundedCornerShape(topEnd = 32.dp, topStart = 32.dp),

        ) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(horizontal = 36.dp, vertical = 36.dp),
            horizontalAlignment = Alignment.CenterHorizontally,
        ) {
            Text(
                text = stringResource(R.string.welcome),
                style = TextStyle(
                    fontSize = 27.sp,
                    fontFamily = FontFamily(Font(R.font.din_next_lt_bold)),
                    fontWeight = FontWeight(500),
                    color = Color(0xFF421882),
                    textAlign = TextAlign.Center,
                )
            )
            Spacer(Modifier.height(8.dp))
            Text(
                text = "Johnson Doe",
                style = TextStyle(
                    fontSize = 27.sp,
                    fontFamily = FontFamily(Font(R.font.din_next_lt_bold)),
                    fontWeight = FontWeight(700),
                    color = Color(0xFF421882),
                    textAlign = TextAlign.Center,
                )
            )
            Spacer(Modifier.height(24.dp))
            ProfilePictureWithAddButton (
                imageRes = R.drawable.female_avatar,
                onAddButtonClick = { /* Handle add button click */ },
            )
            Spacer(Modifier.height(32.dp))
            Text(
                text = stringResource(R.string.you_are_about_to_end),
                style = TextStyle(
                    fontSize = 25.sp,
                    lineHeight = 30.sp,
                    fontFamily = FontFamily(Font(R.font.din_next_lt_bold)),
                    fontWeight = FontWeight(500),
                    color = Color(0xFF421882),
                    textAlign = TextAlign.Center,
                )
            )
            Text(
                text = stringResource(R.string.upload_your_profile_picture),
                style = TextStyle(
                    fontSize = 16.sp,
                    lineHeight = 30.sp,
                    fontFamily = FontFamily(Font(R.font.din_next_lt_bold)),
                    fontWeight = FontWeight(500),
                    color = Color(0xFF421882),
                    textAlign = TextAlign.Center,
                )
            )
            Spacer(Modifier.height(46.dp))
            CommonLargeButton(
                text = stringResource(R.string.confirm),
                onClick = {},
            )
            Spacer(Modifier.height(8.dp))
            TextButton(
                onClick = {},
            ) {
                Text(
                    text = stringResource(R.string.skip),
                    style = TextStyle(
                        fontSize = 16.sp,
                        lineHeight = 20.sp,
                        fontFamily = FontFamily(Font(R.font.din_next_lt_regular)),
                        fontWeight = FontWeight(400),
                        color = Color(0xFF421882),
                        textAlign = TextAlign.Center,
                        letterSpacing = 0.33.sp,
                    )
                )
            }
        }
    }
}

@Composable
fun ProfilePictureWithAddButton(
    @DrawableRes imageRes: Int,
    onAddButtonClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    Box(
        modifier = modifier
    ) {
        Image(
            painter = painterResource(imageRes),
            contentDescription = null,
            modifier = Modifier
                .align(Alignment.Center)
                .size(160.dp),
            contentScale = ContentScale.Crop
        )
        IconButton(
            onClick = onAddButtonClick,
            modifier = Modifier
                .align(Alignment.BottomEnd)
                .size(64.dp),
        ) {
            Image(
                painter = painterResource(R.drawable.ic_add),
                contentDescription = null,
            )
        }
    }
}

@Preview
@Composable
private fun PictureUploadScreenPreviewEn() {
    DentelTheme {
        PictureUploadScreen()
    }
}

@Preview(locale = "ar")
@Composable
private fun PictureUploadScreenPreviewAr() {
    DentelTheme {
        PictureUploadScreen()
    }
}