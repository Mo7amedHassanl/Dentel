package com.m7md7sn.dentel.presentation.ui.auth.pictureupload

import android.net.Uri
import android.widget.Toast
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
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
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.IconButton
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Color.Companion.White
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
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
import androidx.hilt.navigation.compose.hiltViewModel
import coil.compose.rememberAsyncImagePainter
import com.m7md7sn.dentel.R
import com.m7md7sn.dentel.presentation.common.components.CommonLargeButton
import com.m7md7sn.dentel.presentation.common.components.FullDentelHeader
import com.m7md7sn.dentel.presentation.theme.DentelDarkPurple
import com.m7md7sn.dentel.presentation.theme.DentelTheme
import com.m7md7sn.dentel.presentation.ui.auth.viewmodels.PictureUploadViewModel

@Composable
fun PictureUploadScreen(
    modifier: Modifier = Modifier,
    onNavigateToHome: () -> Unit,
    viewModel: PictureUploadViewModel = hiltViewModel()
) {
    val selectedImageUri by viewModel.selectedImageUri.collectAsState()
    val isUploading by viewModel.isUploading.collectAsState()
    val uploadSuccess by viewModel.uploadSuccess.collectAsState()
    val errorMessage by viewModel.errorMessage.collectAsState()
    val userName by viewModel.userName.collectAsState()
    val uploadProgress by viewModel.uploadProgress.collectAsState()
    val context = LocalContext.current

    val pickImageLauncher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.GetContent()
    ) { uri: Uri? ->
        viewModel.setSelectedImageUri(uri)
    }

    LaunchedEffect(uploadSuccess) {
        if (uploadSuccess) {
            Toast.makeText(context, "Profile picture uploaded successfully!", Toast.LENGTH_SHORT).show()
            onNavigateToHome()
        }
    }

    LaunchedEffect(errorMessage) {
        errorMessage?.let {
            Toast.makeText(context, it, Toast.LENGTH_LONG).show()
        }
    }

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
                selectedImageUri = selectedImageUri,
                onAddButtonClick = { pickImageLauncher.launch("image/*") },
                onConfirmClick = { viewModel.uploadProfilePicture() },
                onSkipClick = onNavigateToHome,
                isUploading = isUploading,
                userName = userName,
                uploadProgress = uploadProgress
            )
        }
    }
}

@Composable
fun PictureUploadScreenContent(
    modifier: Modifier = Modifier,
    selectedImageUri: Uri?,
    onAddButtonClick: () -> Unit,
    onConfirmClick: () -> Unit,
    onSkipClick: () -> Unit,
    isUploading: Boolean,
    userName: String?,
    uploadProgress: Int
) {
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
                text = userName ?: "No name provided",
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
                imageUri = selectedImageUri,
                imageRes = R.drawable.female_avatar,
                onAddButtonClick = onAddButtonClick,
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
            if (isUploading) {
                Column(horizontalAlignment = Alignment.CenterHorizontally) {
                    CircularProgressIndicator()
                    Spacer(Modifier.height(8.dp))
                    Text(
                        text = "Uploading: $uploadProgress%",
                        style = TextStyle(
                            fontSize = 16.sp,
                            fontFamily = FontFamily(Font(R.font.din_next_lt_regular)),
                            fontWeight = FontWeight(400),
                            color = Color(0xFF421882),
                            textAlign = TextAlign.Center,
                        )
                    )
                }
            } else {
            CommonLargeButton(
                text = stringResource(R.string.confirm),
                    onClick = onConfirmClick,
            )
            }
            Spacer(Modifier.height(8.dp))
            TextButton(
                onClick = onSkipClick,
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
    imageUri: Uri?,
    @DrawableRes imageRes: Int,
    onAddButtonClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    Box(
        modifier = modifier
    ) {
        val painter = if (imageUri != null) {
            rememberAsyncImagePainter(imageUri)
        } else {
            painterResource(imageRes)
        }
        Image(
            painter = painter,
            contentDescription = null,
            modifier = Modifier
                .align(Alignment.Center)
                .size(160.dp),
            contentScale = ContentScale.Crop
        )
        IconButton(
            onClick = onAddButtonClick,
            modifier = Modifier
                .align(Alignment.BottomEnd) .size(64.dp),
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
        PictureUploadScreen(onNavigateToHome = {})
    }
}

@Preview(locale = "ar")
@Composable
private fun PictureUploadScreenPreviewAr() {
    DentelTheme {
        PictureUploadScreen(onNavigateToHome = {})
    }
}