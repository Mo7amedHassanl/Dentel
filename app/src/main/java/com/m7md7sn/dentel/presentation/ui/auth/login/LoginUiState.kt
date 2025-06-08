package com.m7md7sn.dentel.presentation.ui.auth.login

import com.google.firebase.auth.FirebaseUser
import com.m7md7sn.dentel.utils.Result

data class LoginUiState(
    val email: String = "",
    val password: String = "",
    val emailError: String? = null,
    val passwordError: String? = null,
    val isPasswordVisible: Boolean = false,
    val isLoading: Boolean = false,
    val loginResult: Result<FirebaseUser>? = null
) 