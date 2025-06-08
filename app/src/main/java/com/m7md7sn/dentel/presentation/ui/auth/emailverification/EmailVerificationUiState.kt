package com.m7md7sn.dentel.presentation.ui.auth.emailverification

import com.m7md7sn.dentel.utils.Result

data class EmailVerificationUiState(
    val isLoading: Boolean = false,
    val verificationResult: Result<Unit>? = null,
    val isEmailSent: Boolean = false,
    val userEmail: String = ""
) 