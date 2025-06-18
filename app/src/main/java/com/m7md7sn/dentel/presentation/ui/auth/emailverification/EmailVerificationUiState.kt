package com.m7md7sn.dentel.presentation.ui.auth.emailverification

import com.m7md7sn.dentel.utils.Result

/**
 * Data class representing the UI state for the email verification screen
 * Contains all the state needed to render the email verification UI
 */
data class EmailVerificationUiState(
    // UI states
    val isLoading: Boolean = false,

    // Email verification states
    val verificationResult: Result<Unit>? = null,
    val isEmailSent: Boolean = false,

    // User data
    val userEmail: String = ""
)
