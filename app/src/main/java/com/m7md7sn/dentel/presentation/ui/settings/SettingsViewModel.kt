package com.m7md7sn.dentel.presentation.ui.settings

import android.app.Application
import android.content.ActivityNotFoundException
import android.content.Intent
import android.net.Uri
import android.widget.Toast
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.cloudinary.android.MediaManager
import com.cloudinary.android.callback.ErrorInfo
import com.cloudinary.android.callback.UploadCallback
import com.google.firebase.auth.UserProfileChangeRequest
import com.m7md7sn.dentel.data.model.User
import com.m7md7sn.dentel.data.repository.AuthRepository
import com.m7md7sn.dentel.data.repository.ProfileRepository
import com.m7md7sn.dentel.utils.LocaleUtils
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.flow.receiveAsFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import java.io.File
import java.io.IOException
import java.util.Locale
import javax.inject.Inject
import androidx.core.net.toUri
import kotlinx.coroutines.flow.MutableSharedFlow

/**
 * ViewModel responsible for managing settings screen state and user interactions.
 * Follows MVVM architecture pattern with unidirectional data flow.
 */
@HiltViewModel
class SettingsViewModel @Inject constructor(
    private val authRepository: AuthRepository,
    private val profileRepository: ProfileRepository,
    private val application: Application
) : ViewModel() {

    // Private mutable state flow that can only be modified within the ViewModel
    private val _uiState = MutableStateFlow(SettingsUiState())

    // Public immutable state flow that can be observed by the UI
    val uiState: StateFlow<SettingsUiState> = _uiState.asStateFlow()

    private val _eventChannel = Channel<Event>()
    val eventFlow = _eventChannel.receiveAsFlow()

    // One-time events like snackbar messages
    private val _snackbarMessage = MutableSharedFlow<String>()
    val snackbarMessage = _snackbarMessage

    // Dialog state for account management
    var showChangePasswordDialog = MutableStateFlow(false)
    var changePasswordCurrent = MutableStateFlow("")
    var changePasswordNew = MutableStateFlow("")
    var showResetPasswordDialog = MutableStateFlow(false)
    var showDeleteAccountDialog = MutableStateFlow(false)
    var deleteAccountPassword = MutableStateFlow("")

    init {
        // Observe user profile and update state
        viewModelScope.launch {
            profileRepository.getUserProfile().collectLatest { user ->
                _uiState.update {
                    it.copy(
                        profileName = user.name,
                        profileEmail = user.email,
                        profilePhotoUrl = user.photoUrl
                    )
                }
            }
        }
        // Initialize with saved language preference or current locale if no preference
        val savedLanguage = LocaleUtils.getSavedLanguage(application)
        val currentLanguage = if (savedLanguage != null) {
            if (savedLanguage == "ar") "Arabic" else "English"
        } else {
            LocaleUtils.getDisplayLanguage(Locale.getDefault())
        }

        _uiState.update { it.copy(selectedLanguage = currentLanguage) }
    }

    /**
     * Logs the user out by calling the auth repository and updating the UI state.
     */
    fun logout() {
        viewModelScope.launch {
            try {
                authRepository.logout()
                _uiState.update { currentState ->
                    currentState.copy(isLoggedOut = true)
                }
            } catch (e: Exception) {
                // In a real app, you'd want to handle errors here
                // For now, we're keeping it simple
                _uiState.update { currentState ->
                    currentState.copy(
                        errorMessage = e.message ?: "Logout failed"
                    )
                }
            }
        }
    }

    /**
     * Updates the selected language and app locale.
     */
    fun onLanguageSelected(language: String) {
        viewModelScope.launch {
            _uiState.update { it.copy(selectedLanguage = language) }
            val langCode = if (language == "Arabic") "ar" else "en"

            // Update the app locale using our enhanced utility
            LocaleUtils.setLocale(application, langCode)

            // Notify UI that we need to recreate the activity to apply changes
            _eventChannel.send(Event.RecreateActivity)
        }
    }

    /**
     * Updates the support email field and validates the format
     *
     * @param email The email input from user
     */
    fun updateSupportEmail(email: String) {
        _uiState.update {
            it.copy(
                supportEmail = email,
                isEmailError = email.isNotEmpty() && !isValidEmail(email)
            )
        }
    }

    /**
     * Updates the support message field and validates it's not empty
     *
     * @param message The message input from user
     */
    fun updateSupportMessage(message: String) {
        _uiState.update {
            it.copy(
                supportMessage = message,
                isMessageError = message.isEmpty()
            )
        }
    }

    /**
     * Validates and sends a support email if inputs are valid
     */
    fun validateAndSendSupportEmail() {
        val currentState = _uiState.value
        val isEmailValid = isValidEmail(currentState.supportEmail)
        val isMessageValid = currentState.supportMessage.isNotEmpty()

        _uiState.update {
            it.copy(
                isEmailError = !isEmailValid,
                isMessageError = !isMessageValid
            )
        }

        if (isEmailValid && isMessageValid) {
            sendSupportEmail(currentState.supportEmail, currentState.supportMessage)
        }
    }

    /**
     * Clear support form fields after successful submission
     */
    private fun clearSupportForm() {
        _uiState.update {
            it.copy(
                supportEmail = "",
                supportMessage = "",
                isEmailError = false,
                isMessageError = false
            )
        }
    }

    /**
     * Validates email format
     */
    private fun isValidEmail(email: String): Boolean {
        val emailRegex = "^[A-Za-z0-9+_.-]+@[A-Za-z0-9.-]+\\.[A-Za-z]{2,6}\$"
        return email.matches(emailRegex.toRegex())
    }

    /**
     * Updates the UI state to display selected settings content.
     *
     * @param content The settings content to display
     */
    fun selectSettingsContent(content: SettingsContent) {
        _uiState.update { currentState ->
            currentState.copy(
                currentContent = content,
                errorMessage = null // Clear any previous errors
            )
        }
    }

    /**
     * Clears the currently displayed settings content and returns to the main settings screen.
     */
    fun clearSettingsContent() {
        _uiState.update { currentState ->
            currentState.copy(
                currentContent = null,
                errorMessage = null // Clear any previous errors
            )
        }
    }

    /**
     * Clears any error messages in the UI state.
     */
    fun clearError() {
        _uiState.update { currentState ->
            currentState.copy(errorMessage = null)
        }
    }

    /**
     * Sends a support email to the developer
     *
     * @param email User's email address
     * @param message Support message content
     */
    fun sendSupportEmail(email: String, message: String) {
        viewModelScope.launch {
            _uiState.update { it.copy(isLoading = true) }

            try {
                // Constants
                val developerEmail = "dev.m7md7asn@gmail.com"// Replace with actual developer email
                val subject = "Dentel App Support Request"

                // Format the message to include the sender's email
                val formattedMessage = """
                    From: $email

                    $message
                """.trimIndent()

                val intent = Intent(Intent.ACTION_SEND).apply {
                    type = "message/rfc822"
                    putExtra(Intent.EXTRA_EMAIL, arrayOf(developerEmail))
                    putExtra(Intent.EXTRA_SUBJECT, subject)
                    putExtra(Intent.EXTRA_TEXT, formattedMessage)
                    addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
                }

                try {
                    application.startActivity(Intent.createChooser(intent, "Send email using...").apply {
                        addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
                    })

                    // Clear the form after successful send
                    clearSupportForm()

                    // Send successful event
                    _eventChannel.send(Event.EmailSendSuccess)

                } catch (e: ActivityNotFoundException) {
                    // No email app found
                    _eventChannel.send(Event.EmailSendError("No email app found on this device"))
                }
            } catch (e: Exception) {
                // General error
                _eventChannel.send(Event.EmailSendError("Failed to send email: ${e.message}"))
            } finally {
                _uiState.update { it.copy(isLoading = false) }
            }
        }
    }

    // --- Account/Profile Section Logic ---

    fun onNameChange(newName: String) {
        _uiState.update { it.copy(profileName = newName, profileNameError = null) }
    }

    fun onUpdateProfileClick() {
        val name = _uiState.value.profileName.trim()
        if (name.isBlank()) {
            _uiState.update { it.copy(profileNameError = "Name cannot be empty") }
            return
        }
        viewModelScope.launch {
            _uiState.update { it.copy(isLoading = true, profileNameError = null) }
            val result = authRepository.updateUserProfile(name)
            _uiState.update {
                it.copy(
                    isLoading = false,
                    profileNameError = if (result is com.m7md7sn.dentel.utils.Result.Error) result.message else null
                )
            }
        }
    }

    fun onPhotoClick() {
        // This should trigger the image picker in the UI
        // UI should call setSelectedImageUri(uri) after picking
        // No-op here, but method provided for composable callback
    }

    fun setSelectedImageUri(uri: Uri?) {
        _uiState.update { it.copy(selectedImageUri = uri) }
    }

    private fun getFileFromUri(uri: Uri): File? {
        return try {
            val inputStream = application.contentResolver.openInputStream(uri) ?: return null
            val tempFile = File.createTempFile("upload", ".jpg", application.cacheDir)
            tempFile.outputStream().use { output ->
                inputStream.copyTo(output)
            }
            tempFile
        } catch (e: IOException) {
            e.printStackTrace()
            null
        }
    }

    fun uploadProfilePicture() {
        val uri = _uiState.value.selectedImageUri
        if (uri == null) {
            _uiState.update { it.copy(errorMessage = "No image selected.") }
            viewModelScope.launch { _snackbarMessage.emit("No image selected.") }
            return
        }
        _uiState.update { it.copy(isUploading = true, uploadProgress = 0, errorMessage = null, uploadSuccess = false) }
        viewModelScope.launch {
            try {
                val userId = authRepository.currentUser?.uid
                if (userId == null) {
                    _uiState.update { it.copy(isUploading = false, errorMessage = "User not logged in.") }
                    _snackbarMessage.emit("User not logged in.")
                    return@launch
                }
                val file = getFileFromUri(uri)
                if (file == null) {
                    _uiState.update { it.copy(isUploading = false, errorMessage = "Failed to read image file.") }
                    _snackbarMessage.emit("Failed to read image file.")
                    return@launch
                }
                MediaManager.get().upload(file.absolutePath)
                    .option("folder", "profile_pictures")
                    .option("public_id", userId)
                    .unsigned("dentel_profile_pictures")
                    .callback(object : UploadCallback {
                        override fun onStart(requestId: String) {}
                        override fun onProgress(requestId: String, bytes: Long, totalBytes: Long) {
                            val progress = ((bytes * 100) / totalBytes).toInt()
                            _uiState.update { it.copy(uploadProgress = progress) }
                        }
                        override fun onSuccess(requestId: String, resultData: Map<*, *>?) {
                            val imageUrl = resultData?.get("secure_url") as? String
                            if (imageUrl != null) {
                                updateProfilePictureUrl(imageUrl)
                            } else {
                                _uiState.update { it.copy(isUploading = false, errorMessage = "Failed to get image URL from Cloudinary.") }
                                viewModelScope.launch { _snackbarMessage.emit("Failed to get image URL from Cloudinary.") }
                            }
                        }
                        override fun onError(requestId: String, error: ErrorInfo?) {
                            _uiState.update { it.copy(isUploading = false, errorMessage = error?.description ?: "Cloudinary upload failed.") }
                            android.util.Log.e("CloudinaryUpload", "Upload error: ${error?.description}")
                            viewModelScope.launch { _snackbarMessage.emit(error?.description ?: "Cloudinary upload failed.") }
                        }
                        override fun onReschedule(requestId: String, error: ErrorInfo?) {}
                    })
                    .dispatch()
            } catch (e: Exception) {
                _uiState.update { it.copy(isUploading = false, errorMessage = e.localizedMessage ?: "An unexpected error occurred.") }
                _snackbarMessage.emit(e.localizedMessage ?: "An unexpected error occurred.")
            }
        }
    }

    private fun updateProfilePictureUrl(imageUrl: String) {
        val user = authRepository.currentUser
        if (user == null) {
            _uiState.update { it.copy(isUploading = false, errorMessage = "User not logged in.") }
            viewModelScope.launch { _snackbarMessage.emit("User not logged in.") }
            return
        }
        val profileUpdates = UserProfileChangeRequest.Builder()
            .setPhotoUri(imageUrl.toUri())
            .build()
        user.updateProfile(profileUpdates)
            .addOnCompleteListener { task ->
                if (task.isSuccessful) {
                    user.reload().addOnCompleteListener {
                        _uiState.update { it.copy(isUploading = false, uploadSuccess = true, profilePhotoUrl = imageUrl) }
                        viewModelScope.launch { _snackbarMessage.emit("Profile picture updated successfully!") }
                    }
                } else {
                    _uiState.update { it.copy(isUploading = false, errorMessage = task.exception?.localizedMessage ?: "Failed to update profile.") }
                    viewModelScope.launch { _snackbarMessage.emit(task.exception?.localizedMessage ?: "Failed to update profile.") }
                }
            }
    }

    fun onChangePasswordClick() {
        showChangePasswordDialog.value = true
    }
    fun onResetPasswordClick() {
        showResetPasswordDialog.value = true
    }
    fun onDeleteAccountClick() {
        showDeleteAccountDialog.value = true
    }
    fun dismissDialogs() {
        showChangePasswordDialog.value = false
        showResetPasswordDialog.value = false
        showDeleteAccountDialog.value = false
        changePasswordCurrent.value = ""
        changePasswordNew.value = ""
        deleteAccountPassword.value = ""
    }

    fun submitChangePassword() {
        val currentEmail = authRepository.currentUser?.email ?: ""
        val currentPassword = changePasswordCurrent.value
        val newPassword = changePasswordNew.value
        if (currentPassword.isBlank() || newPassword.isBlank()) {
            viewModelScope.launch { _snackbarMessage.emit("Please fill all fields.") }
            return
        }
        viewModelScope.launch {
            _uiState.update { it.copy(isLoading = true) }
            val reauth = authRepository.reauthenticate(currentEmail, currentPassword)
            if (reauth is com.m7md7sn.dentel.utils.Result.Error) {
                _uiState.update { it.copy(isLoading = false) }
                _snackbarMessage.emit(reauth.message ?: "Re-authentication failed.")
                return@launch
            }
            val result = authRepository.updatePassword(newPassword)
            _uiState.update { it.copy(isLoading = false) }
            if (result is com.m7md7sn.dentel.utils.Result.Success) {
                _snackbarMessage.emit("Password updated successfully.")
                dismissDialogs()
            } else {
                _snackbarMessage.emit((result as com.m7md7sn.dentel.utils.Result.Error).message ?: "Failed to update password.")
            }
        }
    }

    fun submitResetPassword() {
        val email = authRepository.currentUser?.email ?: ""
        if (email.isBlank()) {
            viewModelScope.launch { _snackbarMessage.emit("No email found for this account.") }
            return
        }
        viewModelScope.launch {
            _uiState.update { it.copy(isLoading = true) }
            val result = authRepository.sendPasswordResetEmail(email)
            _uiState.update { it.copy(isLoading = false) }
            if (result is com.m7md7sn.dentel.utils.Result.Success) {
                _snackbarMessage.emit("Password reset email sent.")
                dismissDialogs()
            } else {
                _snackbarMessage.emit((result as com.m7md7sn.dentel.utils.Result.Error).message ?: "Failed to send reset email.")
            }
        }
    }

    fun submitDeleteAccount() {
        val currentEmail = authRepository.currentUser?.email ?: ""
        val password = deleteAccountPassword.value
        val userId = authRepository.currentUser?.uid
        if (password.isBlank()) {
            viewModelScope.launch { _snackbarMessage.emit("Please enter your password.") }
            return
        }
        if (userId == null) {
            viewModelScope.launch { _snackbarMessage.emit("User not logged in.") }
            return
        }
        viewModelScope.launch {
            _uiState.update { it.copy(isLoading = true) }
            // Re-authenticate
            val reauth = authRepository.reauthenticate(currentEmail, password)
            if (reauth is com.m7md7sn.dentel.utils.Result.Error) {
                _uiState.update { it.copy(isLoading = false) }
                _snackbarMessage.emit(reauth.message ?: "Re-authentication failed.")
                return@launch
            }
            // Delete user data from Firestore
            try {
                profileRepository.deleteUserProfileData(userId)
            } catch (e: Exception) {
                // Log or handle error, but continue
            }
            // Delete profile image from Cloudinary
            try {
                com.cloudinary.android.MediaManager.get().cloudinary.uploader().destroy("profile_pictures/$userId", mapOf("resource_type" to "image"))
            } catch (e: Exception) {
                // Log or handle error, but continue
            }
            // Delete Firebase user
            val result = authRepository.deleteAccount()
            _uiState.update { it.copy(isLoading = false) }
            if (result is com.m7md7sn.dentel.utils.Result.Success) {
                _snackbarMessage.emit("Account deleted successfully.")
                _uiState.update { it.copy(isLoggedOut = true) }
            } else {
                _snackbarMessage.emit((result as com.m7md7sn.dentel.utils.Result.Error).message ?: "Failed to delete account.")
            }
            dismissDialogs()
        }
    }

    sealed class Event {
        object RecreateActivity : Event()
        object EmailSendSuccess : Event()
        data class EmailSendError(val message: String) : Event()
    }
}