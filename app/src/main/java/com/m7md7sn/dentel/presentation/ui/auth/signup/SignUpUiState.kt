package com.m7md7sn.dentel.presentation.ui.auth.signup

import com.google.firebase.auth.FirebaseUser
import com.m7md7sn.dentel.utils.Result

/**
 * Data class representing the UI state for the signup screen
 * Contains all the state needed to render the signup UI
 */
data class SignUpUiState(
    // Form fields
    val email: String = "",
    val username: String = "",
    val password: String = "",
    val confirmPassword: String = "",

    // Error states
    val emailError: String? = null,
    val usernameError: String? = null,
    val passwordError: String? = null,
    val confirmPasswordError: String? = null,

    // UI states
    val isPasswordVisible: Boolean = false,
    val isConfirmPasswordVisible: Boolean = false,
    val isLoading: Boolean = false,

    // Signup operation result
    val signupResult: Result<FirebaseUser>? = null
)
