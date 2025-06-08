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
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharedFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject
import com.m7md7sn.dentel.presentation.ui.auth.pictureupload.PictureUploadUiState
import com.m7md7sn.dentel.utils.Event

@HiltViewModel
class PictureUploadViewModel @Inject constructor(
    private val auth: FirebaseAuth,
    @ApplicationContext private val context: Context
) : ViewModel() {

    private val _uiState = MutableStateFlow(PictureUploadUiState(userName = auth.currentUser?.displayName))
    val uiState: StateFlow<PictureUploadUiState> = _uiState.asStateFlow()

    private val _snackbarMessage = MutableSharedFlow<Event<String>>()
    val snackbarMessage: SharedFlow<Event<String>> = _snackbarMessage.asSharedFlow()

    fun setSelectedImageUri(uri: Uri?) {
        _uiState.value = _uiState.value.copy(selectedImageUri = uri)
    }

    fun uploadProfilePicture() {
        _uiState.value.selectedImageUri?.let { uri ->
            _uiState.value = _uiState.value.copy(isUploading = true, errorMessage = null, uploadProgress = 0)
            viewModelScope.launch {
                try {
                    val userId = auth.currentUser?.uid
                    if (userId == null) {
                        _snackbarMessage.emit(Event("User not logged in."))
                        _uiState.value = _uiState.value.copy(isUploading = false)
                        return@launch
                    }

                    MediaManager.get().upload(uri)
                        .option("folder", "profile_pictures")
                        .option("public_id", userId)
                        .unsigned("dentel_profile_pictures")
                        .callback(object : UploadCallback {
                            override fun onStart(requestId: String) {
                                // Nothing specific to update in UI state on start that isn't already handled
                            }

                            override fun onProgress(requestId: String, bytes: Long, totalBytes: Long) {
                                val progress = ((bytes * 100) / totalBytes).toInt()
                                _uiState.value = _uiState.value.copy(uploadProgress = progress)
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
                                                _uiState.value = _uiState.value.copy(uploadSuccess = true, isUploading = false)
                                                viewModelScope.launch { _snackbarMessage.emit(Event("Profile picture uploaded successfully!")) }
                                            } else {
                                                viewModelScope.launch { _snackbarMessage.emit(Event(task.exception?.localizedMessage ?: "Failed to update profile.")) }
                                                _uiState.value = _uiState.value.copy(isUploading = false)
                                            }
                                        }
                                } else {
                                    viewModelScope.launch { _snackbarMessage.emit(Event("Failed to get image URL from Cloudinary.")) }
                                    _uiState.value = _uiState.value.copy(isUploading = false)
                                }
                            }

                            override fun onError(requestId: String, error: ErrorInfo?) {
                                viewModelScope.launch { _snackbarMessage.emit(Event(error?.description ?: "Cloudinary upload failed.")) }
                                _uiState.value = _uiState.value.copy(isUploading = false)
                            }

                            override fun onReschedule(requestId: String, error: ErrorInfo?) {
                                // Upload rescheduled, no direct UI state update for this in current scope
                            }
                        })
                        .dispatch()

                } catch (e: Exception) {
                    viewModelScope.launch { _snackbarMessage.emit(Event(e.localizedMessage ?: "An unexpected error occurred.")) }
                    _uiState.value = _uiState.value.copy(isUploading = false)
                }
            }
        } ?: run {
            viewModelScope.launch { _snackbarMessage.emit(Event("No image selected.")) }
        }
    }
}