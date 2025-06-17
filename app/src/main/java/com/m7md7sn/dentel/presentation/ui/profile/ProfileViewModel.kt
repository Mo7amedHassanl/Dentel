package com.m7md7sn.dentel.presentation.ui.profile

import androidx.lifecycle.ViewModel
import com.m7md7sn.dentel.data.repository.AuthRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import javax.inject.Inject

@HiltViewModel
class ProfileViewModel @Inject constructor(
    private val authRepository: AuthRepository
) : ViewModel() {

    private val _uiState = MutableStateFlow(ProfileUiState())
    val uiState: StateFlow<ProfileUiState> = _uiState.asStateFlow()

    init {
        authRepository.currentUser?.let { user ->
            _uiState.value = _uiState.value.copy(
                userName = user.displayName ?: "",
                profilePictureUrl = user.photoUrl?.toString()
            )
        }
    }
}