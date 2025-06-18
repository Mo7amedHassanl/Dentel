package com.m7md7sn.dentel.presentation.ui.auth.passwordreset

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
 * ViewModel for password reset screen that handles reset logic and state management
 * Follows MVVM architecture pattern for separation of concerns
 */
@HiltViewModel
class PasswordResetViewModel @Inject constructor(
    private val authRepository: AuthRepository
) : ViewModel() {

    // Private mutable state flow that can only be modified within the ViewModel
    private val _uiState = MutableStateFlow(PasswordResetUiState())

    // Public immutable state flow that can be observed by the UI
    val uiState: StateFlow<PasswordResetUiState> = _uiState.asStateFlow()

    // One-time events like snackbar messages
    private val _snackbarMessage = MutableSharedFlow<Event<String>>()
    val snackbarMessage: SharedFlow<Event<String>> = _snackbarMessage.asSharedFlow()

    /**
     * Updates the email field and clears any previous error
     */
    fun onEmailChange(newValue: String) {
        _uiState.value = _uiState.value.copy(email = newValue, emailError = null)
    }

    /**
     * Sends password reset email to the provided email address
     * Validates email and updates UI state accordingly
     */
    fun sendPasswordResetEmail() {
        val currentUiState = _uiState.value

        // Basic validation
        if (currentUiState.email.isBlank()) {
            _uiState.value = currentUiState.copy(emailError = "Email cannot be empty")
            return
        }

        // Show loading state and attempt to send reset email
        _uiState.value = currentUiState.copy(isLoading = true, passwordResetResult = Result.Loading)
        viewModelScope.launch {
            val result = authRepository.sendPasswordResetEmail(currentUiState.email)
            _uiState.value = _uiState.value.copy(isLoading = false, passwordResetResult = result)

            // Show success or error message
            if (result is Result.Success) {
                _snackbarMessage.emit(Event("Password reset email sent!"))
            } else if (result is Result.Error) {
                _snackbarMessage.emit(Event(result.message))
            }
        }
    }

    /**
     * Resets the password reset result to null
     * Used after handling success or failure
     */
    fun resetPasswordResetResult() {
        _uiState.value = _uiState.value.copy(passwordResetResult = null)
    }
}