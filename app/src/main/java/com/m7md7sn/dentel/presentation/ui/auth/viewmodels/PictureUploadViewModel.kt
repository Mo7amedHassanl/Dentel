package com.m7md7sn.dentel.presentation.ui.auth.viewmodels

import android.content.Context
import android.net.Uri
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.cloudinary.android.MediaManager
import com.cloudinary.android.callback.ErrorInfo
import com.cloudinary.android.callback.UploadCallback
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.UserProfileChangeRequest
import dagger.hilt.android.lifecycle.HiltViewModel
import dagger.hilt.android.qualifiers.ApplicationContext
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class PictureUploadViewModel @Inject constructor(
    private val auth: FirebaseAuth,
    @ApplicationContext private val context: Context
) : ViewModel() {

    private val _selectedImageUri = MutableStateFlow<Uri?>(null)
    val selectedImageUri: StateFlow<Uri?> = _selectedImageUri

    private val _isUploading = MutableStateFlow(false)
    val isUploading: StateFlow<Boolean> = _isUploading

    private val _uploadSuccess = MutableStateFlow(false)
    val uploadSuccess: StateFlow<Boolean> = _uploadSuccess

    private val _uploadProgress = MutableStateFlow(0)
    val uploadProgress: StateFlow<Int> = _uploadProgress

    private val _errorMessage = MutableStateFlow<String?>(null)
    val errorMessage: StateFlow<String?> = _errorMessage

    private val _userName = MutableStateFlow<String?>(null)
    val userName: StateFlow<String?> = _userName

    init {
        auth.currentUser?.displayName?.let {
            _userName.value = it
        }
    }

    fun setSelectedImageUri(uri: Uri?) {
        _selectedImageUri.value = uri
    }

    fun uploadProfilePicture() {
        _selectedImageUri.value?.let { uri ->
            _isUploading.value = true
            _errorMessage.value = null
            viewModelScope.launch {
                try {
                    val userId = auth.currentUser?.uid
                    if (userId == null) {
                        _errorMessage.value = "User not logged in."
                        _isUploading.value = false
                        return@launch
                    }

                    MediaManager.get().upload(uri)
                        .option("folder", "profile_pictures") // Optional: specify a folder
                        .option("public_id", userId) // Optional: use user ID as public ID
                        .unsigned("dentel_profile_pictures") // Use your unsigned upload preset name
                        .callback(object : UploadCallback {
                            override fun onStart(requestId: String) {
                                // Upload started
                            }

                            override fun onProgress(requestId: String, bytes: Long, totalBytes: Long) {
                                val progress = ((bytes * 100) / totalBytes).toInt()
                                _uploadProgress.value = progress
                            }

                            override fun onSuccess(requestId: String, resultData: Map<*, *>?) {
                                val imageUrl = resultData?.get("secure_url") as? String
                                if (imageUrl != null) {
                                    val profileUpdates = UserProfileChangeRequest.Builder()
                                        .setPhotoUri(Uri.parse(imageUrl))
                                        .build()

                                    auth.currentUser?.updateProfile(profileUpdates)
                                        ?.addOnCompleteListener { task ->
                                            if (task.isSuccessful) {
                                                _uploadSuccess.value = true
                                                _isUploading.value = false
                                            } else {
                                                _errorMessage.value = task.exception?.localizedMessage
                                                _isUploading.value = false
                                            }
                                        }
                                } else {
                                    _errorMessage.value = "Failed to get image URL from Cloudinary."
                                    _isUploading.value = false
                                }
                            }

                            override fun onError(requestId: String, error: ErrorInfo?) {
                                _errorMessage.value = error?.description ?: "Cloudinary upload failed."
                                _isUploading.value = false
                            }

                            override fun onReschedule(requestId: String, error: ErrorInfo?) {
                                // Upload rescheduled
                            }
                        })
                        .dispatch()

                } catch (e: Exception) {
                    _errorMessage.value = e.localizedMessage
                    _isUploading.value = false
                }
            }
        } ?: run {
            _errorMessage.value = "No image selected."
        }
    }
}