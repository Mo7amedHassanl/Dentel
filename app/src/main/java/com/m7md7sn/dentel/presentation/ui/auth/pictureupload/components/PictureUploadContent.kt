package com.m7md7sn.dentel.presentation.ui.auth.pictureupload.components

import android.net.Uri
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.Font
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.m7md7sn.dentel.R
import com.m7md7sn.dentel.presentation.common.components.CommonLargeButton

/**
 * Main content component for the picture upload screen
 */
@Composable
fun PictureUploadContent(
    selectedImageUri: Uri?,
    onAddButtonClick: () -> Unit,
    onConfirmClick: () -> Unit,
    onSkipClick: () -> Unit,
    isUploading: Boolean,
    userName: String?,
    uploadProgress: Int,
    modifier: Modifier = Modifier
) {
    Surface(
        color = Color.White,
        modifier = modifier.fillMaxWidth(),
        shape = RoundedCornerShape(topEnd = 32.dp, topStart = 32.dp),
    ) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(horizontal = 36.dp, vertical = 24.dp),
            horizontalAlignment = Alignment.CenterHorizontally,
        ) {
            // Welcome Header
            WelcomeHeader(userName = userName)

            Spacer(Modifier.height(24.dp))

            // Profile Picture
            ProfilePictureWithAddButton(
                imageUri = selectedImageUri,
                imageRes = R.drawable.female_avatar,
                onAddButtonClick = onAddButtonClick,
            )

            Spacer(Modifier.height(32.dp))

            // Instructions
            UploadInstructions()

            Spacer(Modifier.height(46.dp))

            // Upload progress or buttons
            if (isUploading) {
                UploadProgress(progress = uploadProgress)
                Spacer(Modifier.height(46.dp))
            } else {
                ActionButtons(
                    onConfirmClick = onConfirmClick,
                    onSkipClick = onSkipClick,
                    isUploading = isUploading
                )
            }
        }
    }
}

/**
 * Welcome header displaying the welcome message and user name
 */
@Composable
private fun WelcomeHeader(userName: String?) {
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
        text = userName ?: "No name provided",
        style = TextStyle(
            fontSize = 27.sp,
            fontFamily = FontFamily(Font(R.font.din_next_lt_bold)),
            fontWeight = FontWeight(700),
            color = Color(0xFF421882),
            textAlign = TextAlign.Center,
        )
    )
}

/**
 * Instructions text for uploading profile picture
 */
@Composable
private fun UploadInstructions() {
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
}

/**
 * Upload progress indicator with percentage text
 */
@Composable
private fun UploadProgress(progress: Int) {
    Column(horizontalAlignment = Alignment.CenterHorizontally) {
        CircularProgressIndicator()
        Spacer(Modifier.height(8.dp))
        Text(
            text = "Uploading: $progress%",
            style = TextStyle(
                fontSize = 16.sp,
                fontFamily = FontFamily(Font(R.font.din_next_lt_regular)),
                fontWeight = FontWeight(400),
                color = Color(0xFF421882),
                textAlign = TextAlign.Center,
            )
        )
    }
}

/**
 * Confirm and skip action buttons
 */
@Composable
private fun ActionButtons(
    onConfirmClick: () -> Unit,
    onSkipClick: () -> Unit,
    isUploading: Boolean
) {
    CommonLargeButton(
        text = stringResource(R.string.confirm),
        onClick = onConfirmClick,
        isLoading = isUploading
    )

    Spacer(Modifier.height(20.dp))

    TextButton(
        onClick = onSkipClick,
        modifier = Modifier.fillMaxWidth()
    ) {
        Text(
            text = stringResource(R.string.skip),
            style = TextStyle(
                fontSize = 16.sp,
                fontFamily = FontFamily(Font(R.font.din_next_lt_bold)),
                fontWeight = FontWeight(500),
                color = Color(0xFF421882),
                textAlign = TextAlign.Center,
            )
        )
    }
}
