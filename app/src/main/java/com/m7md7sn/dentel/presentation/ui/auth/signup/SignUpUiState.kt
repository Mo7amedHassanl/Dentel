package com.m7md7sn.dentel.presentation.ui.auth.signup

import com.google.firebase.auth.FirebaseUser
import com.m7md7sn.dentel.utils.Result

data class SignUpUiState(
    val email: String = "",
    val username: String = "",
    val password: String = "",
    val confirmPassword: String = "",
    val emailError: String? = null,
    val usernameError: String? = null,
    val passwordError: String? = null,
    val confirmPasswordError: String? = null,
    val isPasswordVisible: Boolean = false,
    val isConfirmPasswordVisible: Boolean = false,
    val isLoading: Boolean = false,
    val signupResult: Result<FirebaseUser>? = null
) 