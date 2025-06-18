package com.m7md7sn.dentel.presentation.ui.auth.signup

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.google.firebase.auth.FirebaseUser
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
 * ViewModel for signup screen that handles registration logic and state management
 * Follows MVVM architecture pattern for separation of concerns
 */
@HiltViewModel
class SignupViewModel @Inject constructor(
    private val authRepository: AuthRepository
) : ViewModel() {

    // Private mutable state flow that can only be modified within the ViewModel
    private val _uiState = MutableStateFlow(SignUpUiState())

    // Public immutable state flow that can be observed by the UI
    val uiState: StateFlow<SignUpUiState> = _uiState.asStateFlow()

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
     * Updates the username field and clears any previous error
     */
    fun onUsernameChange(newValue: String) {
        _uiState.value = _uiState.value.copy(username = newValue, usernameError = null)
    }

    /**
     * Updates the password field and clears any previous error
     */
    fun onPasswordChange(newValue: String) {
        _uiState.value = _uiState.value.copy(password = newValue, passwordError = null)
    }

    /**
     * Updates the confirm password field and clears any previous error
     */
    fun onConfirmPasswordChange(newValue: String) {
        _uiState.value = _uiState.value.copy(confirmPassword = newValue, confirmPasswordError = null)
    }

    /**
     * Toggles password visibility between visible and hidden
     */
    fun togglePasswordVisibility() {
        _uiState.value = _uiState.value.copy(isPasswordVisible = !_uiState.value.isPasswordVisible)
    }

    /**
     * Toggles confirm password visibility between visible and hidden
     */
    fun toggleConfirmPasswordVisibility() {
        _uiState.value = _uiState.value.copy(isConfirmPasswordVisible = !_uiState.value.isConfirmPasswordVisible)
    }

    /**
     * Attempts to register the user with the provided credentials
     * Validates inputs and updates UI state accordingly
     */
    fun signup() {
        val currentState = _uiState.value

        // Basic validation
        if (currentState.username.isBlank()) {
            _uiState.value = currentState.copy(usernameError = "Username cannot be empty")
            return
        }
        if (currentState.email.isBlank()) {
            _uiState.value = currentState.copy(emailError = "Email cannot be empty")
            return
        }
        if (currentState.password.isBlank()) {
            _uiState.value = currentState.copy(passwordError = "Password cannot be empty")
            return
        }
        if (currentState.password.length < 6) {
            _uiState.value = currentState.copy(passwordError = "Password must be at least 6 characters long")
            return
        }
        if (currentState.confirmPassword.isBlank()) {
            _uiState.value = currentState.copy(confirmPasswordError = "Confirm Password cannot be empty")
            return
        }
        if (currentState.password != currentState.confirmPassword) {
            _uiState.value = currentState.copy(
                passwordError = "Passwords do not match",
                confirmPasswordError = "Passwords do not match"
            )
            return
        }

        // Show loading state and attempt signup
        _uiState.value = currentState.copy(isLoading = true, signupResult = Result.Loading)
        viewModelScope.launch {
            val result = authRepository.signup(currentState.email, currentState.password)
            if (result is Result.Success) {
                // Update user profile with display name
                val updateProfileResult = authRepository.updateUserProfile(currentState.username)
                if (updateProfileResult is Result.Success) {
                    _uiState.value = _uiState.value.copy(isLoading = false, signupResult = result)
                } else if (updateProfileResult is Result.Error) {
                    val message = updateProfileResult.message + " but signup was successful."
                    _snackbarMessage.emit(Event(message))
                    _uiState.value = _uiState.value.copy(
                        isLoading = false,
                        signupResult = Result.Error(message, updateProfileResult.throwable)
                    )
                }
            } else if (result is Result.Error) {
                _snackbarMessage.emit(Event(result.message))
                _uiState.value = _uiState.value.copy(isLoading = false, signupResult = result)
            }
        }
    }

    /**
     * Resets the signup result to null
     * Used after handling signup success or failure
     */
    fun resetSignupResult() {
        _uiState.value = _uiState.value.copy(signupResult = null)
    }
}