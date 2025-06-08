package com.m7md7sn.dentel.presentation.ui.auth.viewmodels

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.google.firebase.auth.FirebaseUser
import com.m7md7sn.dentel.data.repository.AuthRepository
import com.m7md7sn.dentel.utils.Result
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Visibility
import androidx.compose.material.icons.filled.VisibilityOff
import androidx.compose.ui.graphics.vector.ImageVector
import com.m7md7sn.dentel.utils.Event
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.SharedFlow
import kotlinx.coroutines.flow.asSharedFlow
import com.m7md7sn.dentel.presentation.ui.auth.login.LoginUiState

@HiltViewModel
class LoginViewModel @Inject constructor(
    private val authRepository: AuthRepository
) : ViewModel() {

    private val _uiState = MutableStateFlow(LoginUiState())
    val uiState: StateFlow<LoginUiState> = _uiState.asStateFlow()

    private val _snackbarMessage = MutableSharedFlow<Event<String>>()
    val snackbarMessage: SharedFlow<Event<String>> = _snackbarMessage.asSharedFlow()

    fun onEmailChange(newValue: String) {
        _uiState.value = _uiState.value.copy(email = newValue, emailError = null)
    }

    fun onPasswordChange(newValue: String) {
        _uiState.value = _uiState.value.copy(password = newValue, passwordError = null)
    }

    fun togglePasswordVisibility() {
        _uiState.value = _uiState.value.copy(isPasswordVisible = !_uiState.value.isPasswordVisible)
    }

    fun login() {
        val currentUiState = _uiState.value
        // Basic validation
        if (currentUiState.email.isBlank()) {
            _uiState.value = currentUiState.copy(emailError = "Email cannot be empty")
            return
        }
        if (currentUiState.password.isBlank()) {
            _uiState.value = currentUiState.copy(passwordError = "Password cannot be empty")
            return
        }

        _uiState.value = currentUiState.copy(isLoading = true, loginResult = Result.Loading)
        viewModelScope.launch {
            val result = authRepository.login(currentUiState.email, currentUiState.password)
            _uiState.value = _uiState.value.copy(isLoading = false, loginResult = result)

            if (result is Result.Error) {
                _snackbarMessage.emit(Event(result.message))
            }
        }
    }

    fun resetLoginResult() {
        _uiState.value = _uiState.value.copy(loginResult = null)
    }

    fun getPasswordVisibilityIcon(): ImageVector {
        return if (_uiState.value.isPasswordVisible) Icons.Filled.VisibilityOff else Icons.Filled.Visibility
    }
} 