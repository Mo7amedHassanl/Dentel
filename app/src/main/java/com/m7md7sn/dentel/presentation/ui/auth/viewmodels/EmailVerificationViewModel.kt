package com.m7md7sn.dentel.presentation.ui.auth.viewmodels

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
class EmailVerificationViewModel @Inject constructor(
    private val authRepository: AuthRepository
) : ViewModel() {

    private val _sendVerificationResult = MutableStateFlow<Result<Unit>?>(null)
    val sendVerificationResult: StateFlow<Result<Unit>?> = _sendVerificationResult.asStateFlow()

    private val _reloadUserResult = MutableStateFlow<Result<Unit>?>(null)
    val reloadUserResult: StateFlow<Result<Unit>?> = _reloadUserResult.asStateFlow()

    val currentUser: FirebaseUser? = authRepository.currentUser

    init {
        sendEmailVerification()
    }

    fun sendEmailVerification() {
        _sendVerificationResult.value = Result.Loading
        viewModelScope.launch {
            val result = authRepository.sendEmailVerification()
            _sendVerificationResult.value = result
        }
    }

    fun reloadUser() {
        _reloadUserResult.value = Result.Loading
        viewModelScope.launch {
            val result = authRepository.reloadUser()
            _reloadUserResult.value = result
        }
    }

    fun resetSendVerificationResult() {
        _sendVerificationResult.value = null
    }

    fun resetReloadUserResult() {
        _reloadUserResult.value = null
    }

    fun logout() {
        viewModelScope.launch {
            authRepository.logout()
        }
    }
} 