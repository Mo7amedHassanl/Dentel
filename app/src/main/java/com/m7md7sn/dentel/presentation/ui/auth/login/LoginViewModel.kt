package com.m7md7sn.dentel.presentation.ui.auth.login

import android.content.Intent
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
 * ViewModel for login screen that handles login logic and state management
 * Follows MVVM architecture pattern for separation of concerns
 */
@HiltViewModel
class LoginViewModel @Inject constructor(
    private val authRepository: AuthRepository
) : ViewModel() {

    // Private mutable state flow that can only be modified within the ViewModel
    private val _uiState = MutableStateFlow(LoginUiState())

    // Public immutable state flow that can be observed by the UI
    val uiState: StateFlow<LoginUiState> = _uiState.asStateFlow()

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
     * Updates the password field and clears any previous error
     */
    fun onPasswordChange(newValue: String) {
        _uiState.value = _uiState.value.copy(password = newValue, passwordError = null)
    }

    /**
     * Toggles password visibility between visible and hidden
     */
    fun togglePasswordVisibility() {
        _uiState.value = _uiState.value.copy(isPasswordVisible = !_uiState.value.isPasswordVisible)
    }

    /**
     * Attempts to log in the user with the provided credentials
     * Validates inputs and updates UI state accordingly
     */
    fun login() {
        val currentUiState = _uiState.value

        // Validate email
        if (currentUiState.email.isBlank()) {
            _uiState.value = currentUiState.copy(emailError = "Email cannot be empty")
            return
        }

        // Validate password
        if (currentUiState.password.isBlank()) {
            _uiState.value = currentUiState.copy(passwordError = "Password cannot be empty")
            return
        }

        // Show loading state and attempt login
        _uiState.value = currentUiState.copy(isLoading = true, loginResult = Result.Loading)
        viewModelScope.launch {
            val result = authRepository.login(currentUiState.email, currentUiState.password)
            _uiState.value = _uiState.value.copy(isLoading = false, loginResult = result)

            // Show error message if login failed
            if (result is Result.Error) {
                _snackbarMessage.emit(Event(result.message))
            }
        }
    }

    /**
     * Returns an intent that can be used to start Google sign-in
     */
    fun getGoogleSignInIntent(): Intent {
        return authRepository.getGoogleSignInIntent()
    }

    /**
     * Handles the result of Google sign-in process
     * @param idToken The ID token from Google sign-in
     */
    fun signInWithGoogle(idToken: String) {
        _uiState.value = _uiState.value.copy(isLoading = true, loginResult = Result.Loading)
        viewModelScope.launch {
            val result = authRepository.signInWithGoogle(idToken)
            _uiState.value = _uiState.value.copy(isLoading = false, loginResult = result)

            if (result is Result.Error) {
                _snackbarMessage.emit(Event(result.message))
            }
        }
    }

    /**
     * Resets the login result to null
     * Used after handling login success or failure
     */
    fun resetLoginResult() {
        _uiState.value = _uiState.value.copy(loginResult = null)
    }
}