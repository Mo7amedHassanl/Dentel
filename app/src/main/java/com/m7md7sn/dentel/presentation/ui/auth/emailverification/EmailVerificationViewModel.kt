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

@HiltViewModel
class EmailVerificationViewModel @Inject constructor(
    private val authRepository: AuthRepository
) : ViewModel() {

    private val _uiState = MutableStateFlow(
        EmailVerificationUiState(
            userEmail = authRepository.currentUser?.email ?: ""
        )
    )
    val uiState: StateFlow<EmailVerificationUiState> = _uiState.asStateFlow()

    private val _snackbarMessage = MutableSharedFlow<Event<String>>()
    val snackbarMessage: SharedFlow<Event<String>> = _snackbarMessage.asSharedFlow()

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

    fun resetVerificationResult() {
        _uiState.value = _uiState.value.copy(verificationResult = null)
    }
}