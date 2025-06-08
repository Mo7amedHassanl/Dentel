package com.m7md7sn.dentel.presentation.ui.auth.viewmodels

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.m7md7sn.dentel.data.repository.AuthRepository
import com.m7md7sn.dentel.utils.Result
import com.m7md7sn.dentel.utils.Event
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.SharedFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.launch
import javax.inject.Inject
import com.m7md7sn.dentel.presentation.ui.auth.passwordreset.PasswordResetUiState

@HiltViewModel
class PasswordResetViewModel @Inject constructor(
    private val authRepository: AuthRepository
) : ViewModel() {

    private val _uiState = MutableStateFlow(PasswordResetUiState())
    val uiState: StateFlow<PasswordResetUiState> = _uiState.asStateFlow()

    private val _snackbarMessage = MutableSharedFlow<Event<String>>()
    val snackbarMessage: SharedFlow<Event<String>> = _snackbarMessage.asSharedFlow()

    fun onEmailChange(newValue: String) {
        _uiState.value = _uiState.value.copy(email = newValue, emailError = null)
    }

    fun sendPasswordResetEmail() {
        val currentUiState = _uiState.value
        // Basic validation
        if (currentUiState.email.isBlank()) {
            _uiState.value = currentUiState.copy(emailError = "Email cannot be empty")
            return
        }

        _uiState.value = currentUiState.copy(isLoading = true, passwordResetResult = Result.Loading)
        viewModelScope.launch {
            val result = authRepository.sendPasswordResetEmail(currentUiState.email)
            _uiState.value = _uiState.value.copy(isLoading = false, passwordResetResult = result)

            if (result is Result.Success) {
                _snackbarMessage.emit(Event("Password reset email sent!"))
            } else if (result is Result.Error) {
                _snackbarMessage.emit(Event(result.message))
            }
        }
    }

    fun resetPasswordResetResult() {
        _uiState.value = _uiState.value.copy(passwordResetResult = null)
    }
} 