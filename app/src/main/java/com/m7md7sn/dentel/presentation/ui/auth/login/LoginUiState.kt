package com.m7md7sn.dentel.presentation.ui.auth.login

import com.google.firebase.auth.FirebaseUser
import com.m7md7sn.dentel.utils.Result

/**
 * Data class representing the UI state for the login screen
 * Contains all the state needed to render the login UI
 */
data class LoginUiState(
    // Form fields
    val email: String = "",
    val password: String = "",

    // Error states
    val emailError: String? = null,
    val passwordError: String? = null,

    // UI states
    val isPasswordVisible: Boolean = false,
    val isLoading: Boolean = false,

    // Login operation result
    val loginResult: Result<FirebaseUser>? = null
)
