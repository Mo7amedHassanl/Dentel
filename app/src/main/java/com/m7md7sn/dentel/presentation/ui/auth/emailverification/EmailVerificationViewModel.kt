package com.m7md7sn.dentel.presentation.ui.auth.emailverification

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.m7md7sn.dentel.data.repository.AuthRepository
import com.m7md7sn.dentel.utils.Event
import com.m7md7sn.dentel.utils.Result
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharedFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

/**
 * ViewModel for email verification screen that handles verification logic and state management
 * Follows MVVM architecture pattern for separation of concerns
 */
@HiltViewModel
class EmailVerificationViewModel @Inject constructor(
    private val authRepository: AuthRepository
) : ViewModel() {

    // Private mutable state flow that can only be modified within the ViewModel
    private val _uiState = MutableStateFlow(
        EmailVerificationUiState(
            userEmail = authRepository.currentUser?.email ?: ""
        )
    )

    // Public immutable state flow that can be observed by the UI
    val uiState: StateFlow<EmailVerificationUiState> = _uiState.asStateFlow()

    // One-time events like snackbar messages
    private val _snackbarMessage = MutableSharedFlow<Event<String>>()
    val snackbarMessage: SharedFlow<Event<String>> = _snackbarMessage.asSharedFlow()

    /**
     * Sends email verification to current user's email address
     * Updates UI state with loading state and result
     */
    fun sendEmailVerification() {
        _uiState.value = _uiState.value.copy(isLoading = true, verificationResult = Result.Loading)
        viewModelScope.launch {
            val result = authRepository.sendEmailVerification()
            _uiState.value = _uiState.value.copy(isLoading = false, verificationResult = result)

            if (result is Result.Success) {
                _snackbarMessage.emit(Event("Verification email sent!"))
                _uiState.value = _uiState.value.copy(isEmailSent = true)
            } else if (result is Result.Error) {
                _snackbarMessage.emit(Event(result.message))
            }
        }
    }

    /**
     * Checks if the email has been verified by reloading the user
     * Updates UI state based on verification status
     */
    fun checkEmailVerificationStatus() {
        _uiState.value = _uiState.value.copy(isLoading = true, verificationResult = Result.Loading)
        viewModelScope.launch {
            val reloadResult = authRepository.reloadUser()
            if (reloadResult is Result.Success) {
                if (authRepository.currentUser?.isEmailVerified == true) {
                    _snackbarMessage.emit(Event("Email verified successfully!"))
                    _uiState.value = _uiState.value.copy(isLoading = false, verificationResult = Result.Success(Unit))
                } else {
                    _snackbarMessage.emit(Event("Email not yet verified. Please check your inbox."))
                    _uiState.value = _uiState.value.copy(isLoading = false, verificationResult = null)
                }
            } else if (reloadResult is Result.Error) {
                _snackbarMessage.emit(Event(reloadResult.message))
                _uiState.value = _uiState.value.copy(isLoading = false, verificationResult = reloadResult)
            }
        }
    }

    /**
     * Resets the verification result to null
     * Used after handling verification success or failure
     */
    fun resetVerificationResult() {
        _uiState.value = _uiState.value.copy(verificationResult = null)
    }
}