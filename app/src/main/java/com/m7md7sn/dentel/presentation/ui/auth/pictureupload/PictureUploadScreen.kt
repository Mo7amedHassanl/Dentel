package com.m7md7sn.dentel.presentation.ui.auth.pictureupload

import android.net.Uri
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.Surface
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.tooling.preview.Preview
import androidx.hilt.navigation.compose.hiltViewModel
import com.m7md7sn.dentel.presentation.common.components.FullDentelHeader
import com.m7md7sn.dentel.presentation.theme.DentelDarkPurple
import com.m7md7sn.dentel.presentation.theme.DentelTheme
import com.m7md7sn.dentel.presentation.ui.auth.pictureupload.components.PictureUploadContent
import com.m7md7sn.dentel.utils.Event

/**
 * Main picture upload screen composable that handles the profile picture upload UI flow
 */
@Composable
fun PictureUploadScreen(
    onNavigateToHome: () -> Unit,
    modifier: Modifier = Modifier,
    viewModel: PictureUploadViewModel = hiltViewModel()
) {
    val uiState by viewModel.uiState.collectAsState()
    val snackbarHostState = remember { SnackbarHostState() }
    val context = LocalContext.current

    // Image picker launcher
    val pickImageLauncher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.GetContent()
    ) { uri: Uri? ->
        viewModel.setSelectedImageUri(uri)
    }

    // Collect snackbar messages
    LaunchedEffect(Unit) {
        viewModel.snackbarMessage.collect { event: Event<String> ->
            event.getContentIfNotHandled()?.let { message ->
                snackbarHostState.showSnackbar(message)
            }
        }
    }

    // Handle upload success
    LaunchedEffect(uiState.uploadSuccess) {
        if (uiState.uploadSuccess) {
            onNavigateToHome()
        }
    }

    // Main UI
    Surface(
        modifier = modifier.fillMaxSize(),
        color = DentelDarkPurple
    ) {
        Box(modifier = Modifier.fillMaxSize()) {
            Column(
                modifier = Modifier.fillMaxSize(),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                // App header (logo)
                FullDentelHeader(
                    modifier = Modifier.weight(0.30f)
                )

                // Picture upload content
                PictureUploadContent(
                    modifier = Modifier.weight(0.7f),
                    selectedImageUri = uiState.selectedImageUri,
                    onAddButtonClick = { pickImageLauncher.launch("image/*") },
                    onConfirmClick = { viewModel.uploadProfilePicture() },
                    onSkipClick = onNavigateToHome,
                    isUploading = uiState.isUploading,
                    userName = uiState.userName,
                    uploadProgress = uiState.uploadProgress
                )
            }

            // Snackbar for showing messages
            SnackbarHost(
                hostState = snackbarHostState,
                modifier = Modifier.align(Alignment.BottomCenter)
            )
        }
    }
}

@Preview
@Composable
private fun PreviewPictureUploadScreen() {
    DentelTheme {
        PictureUploadScreen(onNavigateToHome = {})
    }
}