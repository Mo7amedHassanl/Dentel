package com.m7md7sn.dentel.presentation.ui.auth.passwordreset

import com.m7md7sn.dentel.utils.Result

/**
 * Data class representing the UI state for the password reset screen
 * Contains all the state needed to render the password reset UI
 */
data class PasswordResetUiState(
    // Form fields
    val email: String = "",

    // Error states
    val emailError: String? = null,

    // UI states
    val isLoading: Boolean = false,

    // Operation result
    val passwordResetResult: Result<Unit>? = null
)
