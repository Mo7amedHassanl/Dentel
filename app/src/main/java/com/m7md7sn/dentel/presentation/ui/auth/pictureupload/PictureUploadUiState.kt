package com.m7md7sn.dentel.presentation.ui.auth.pictureupload

import android.net.Uri
import com.m7md7sn.dentel.utils.Result

data class PictureUploadUiState(
    val userName: String? = null,
    val selectedImageUri: Uri? = null,
    val isUploading: Boolean = false,
    val uploadProgress: Int = 0,
    val uploadSuccess: Boolean = false,
    val errorMessage: String? = null
) 