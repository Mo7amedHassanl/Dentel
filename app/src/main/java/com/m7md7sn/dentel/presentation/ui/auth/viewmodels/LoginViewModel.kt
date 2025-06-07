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
class LoginViewModel @Inject constructor(
    private val authRepository: AuthRepository
) : ViewModel() {

    var email by mutableStateOf("")
        private set

    var password by mutableStateOf("")
        private set

    var emailError by mutableStateOf<String?>(null)
        private set

    var passwordError by mutableStateOf<String?>(null)
        private set

    var isPasswordVisible by mutableStateOf(false)
        private set

    private val _loginResult = MutableStateFlow<Result<FirebaseUser>?>(null)
    val loginResult: StateFlow<Result<FirebaseUser>?> = _loginResult.asStateFlow()

    fun onEmailChange(newValue: String) {
        email = newValue
        emailError = null
    }

    fun onPasswordChange(newValue: String) {
        password = newValue
        passwordError = null
    }

    fun togglePasswordVisibility() {
        isPasswordVisible = !isPasswordVisible
    }

    fun login() {
        // Basic validation
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

        _loginResult.value = Result.Loading
        viewModelScope.launch {
            val result = authRepository.login(email, password)
            _loginResult.value = result
        }
    }

    fun resetLoginResult() {
        _loginResult.value = null
    }
} 