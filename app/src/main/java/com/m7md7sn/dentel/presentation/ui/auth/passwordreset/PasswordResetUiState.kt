package com.m7md7sn.dentel.presentation.ui.auth.passwordreset

import com.m7md7sn.dentel.utils.Result

data class PasswordResetUiState(
    val email: String = "",
    val emailError: String? = null,
    val isLoading: Boolean = false,
    val passwordResetResult: Result<Unit>? = null
) 