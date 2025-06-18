package com.m7md7sn.dentel.presentation.ui.auth.pictureupload

import android.net.Uri
import com.m7md7sn.dentel.utils.Result

/**
 * Data class representing the UI state for the picture upload screen
 * Contains all the state needed to render the picture upload UI
 */
data class PictureUploadUiState(
    // User data
    val userName: String? = null,

    // Image selection state
    val selectedImageUri: Uri? = null,

    // Upload states
    val isUploading: Boolean = false,
    val uploadProgress: Int = 0,
    val uploadSuccess: Boolean = false,

    // Error state
    val errorMessage: String? = null
)
