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

@HiltViewModel
class SignupViewModel @Inject constructor(
    private val authRepository: AuthRepository
) : ViewModel() {

    var email by mutableStateOf("")
        private set

    var username by mutableStateOf("")
        private set

    var password by mutableStateOf("")
        private set

    var confirmPassword by mutableStateOf("")
        private set

    var emailError by mutableStateOf<String?>(null)
        private set

    var passwordError by mutableStateOf<String?>(null)
        private set

    var confirmPasswordError by mutableStateOf<String?>(null)
        private set

    var usernameError by mutableStateOf<String?>(null)
        private set

    var isPasswordVisible by mutableStateOf(false)
        private set

    var isConfirmPasswordVisible by mutableStateOf(false)
        private set

    private val _signupResult = MutableStateFlow<Result<FirebaseUser>?>(null)
    val signupResult: StateFlow<Result<FirebaseUser>?> = _signupResult.asStateFlow()

    fun onEmailChange(newValue: String) {
        email = newValue
        emailError = null
    }

    fun onUsernameChange(newValue: String) {
        username = newValue
        usernameError = null
    }

    fun onPasswordChange(newValue: String) {
        password = newValue
        passwordError = null
    }

    fun onConfirmPasswordChange(newValue: String) {
        confirmPassword = newValue
        confirmPasswordError = null
    }

    fun togglePasswordVisibility() {
        isPasswordVisible = !isPasswordVisible
    }

    fun toggleConfirmPasswordVisibility() {
        isConfirmPasswordVisible = !isConfirmPasswordVisible
    }

    fun signup() {
        // Basic validation
        if (username.isBlank()) {
            usernameError = "Username cannot be empty"
            return
        }
        if (email.isBlank()) {
            emailError = "Email cannot be empty"
            return
        }
        if (password.isBlank()) {
            passwordError = "Password cannot be empty"
            return
        }
        if (password.length < 6) {
            passwordError = "Password must be at least 6 characters long"
            return
        }
        if (confirmPassword.isBlank()) {
            confirmPasswordError = "Confirm Password cannot be empty"
            return
        }
        if (password != confirmPassword) {
            passwordError = "Passwords do not match"
            confirmPasswordError = "Passwords do not match"
            return
        }

        _signupResult.value = Result.Loading
        viewModelScope.launch {
            val result = authRepository.signup(email, password)
            _signupResult.value = result
        }
    }

    fun resetSignupResult() {
        _signupResult.value = null
    }
} 