package com.m7md7sn.dentel.presentation.ui.auth.viewmodels

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.m7md7sn.dentel.data.repository.AuthRepository
import com.m7md7sn.dentel.utils.Result
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class PasswordResetViewModel @Inject constructor(
    private val authRepository: AuthRepository
) : ViewModel() {

    var email by mutableStateOf("")
        private set

    var emailError by mutableStateOf<String?>(null)
        private set

    private val _passwordResetResult = MutableStateFlow<Result<Unit>?>(null)
    val passwordResetResult: StateFlow<Result<Unit>?> = _passwordResetResult.asStateFlow()

    fun onEmailChange(newValue: String) {
        email = newValue
        emailError = null
    }

    fun sendPasswordResetEmail() {
        // Basic validation
        if (email.isBlank()) {
            emailError = "Email cannot be empty"
            return
        }

        _passwordResetResult.value = Result.Loading
        viewModelScope.launch {
            val result = authRepository.sendPasswordResetEmail(email)
            _passwordResetResult.value = result
        }
    }

    fun resetPasswordResetResult() {
        _passwordResetResult.value = null
    }
} 