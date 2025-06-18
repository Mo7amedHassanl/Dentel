package com.m7md7sn.dentel.presentation.ui.settings

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.m7md7sn.dentel.data.repository.AuthRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

/**
 * ViewModel responsible for managing settings screen state and user interactions.
 * Follows MVVM architecture pattern with unidirectional data flow.
 */
@HiltViewModel
class SettingsViewModel @Inject constructor(
    private val authRepository: AuthRepository
) : ViewModel() {

    // Private mutable state flow that can only be modified within the ViewModel
    private val _uiState = MutableStateFlow(SettingsUiState())

    // Public immutable state flow that can be observed by the UI
    val uiState: StateFlow<SettingsUiState> = _uiState.asStateFlow()

    /**
     * Logs the user out by calling the auth repository and updating the UI state.
     */
    fun logout() {
        viewModelScope.launch {
            try {
                authRepository.logout()
                _uiState.update { currentState ->
                    currentState.copy(isLoggedOut = true)
                }
            } catch (e: Exception) {
                // In a real app, you'd want to handle errors here
                // For now, we're keeping it simple
                _uiState.update { currentState ->
                    currentState.copy(
                        errorMessage = e.message ?: "Logout failed"
                    )
                }
            }
        }
    }

    /**
     * Updates the UI state to display selected settings content.
     *
     * @param content The settings content to display
     */
    fun selectSettingsContent(content: SettingsContent) {
        _uiState.update { currentState ->
            currentState.copy(
                currentContent = content,
                errorMessage = null // Clear any previous errors
            )
        }
    }

    /**
     * Clears the currently displayed settings content and returns to the main settings screen.
     */
    fun clearSettingsContent() {
        _uiState.update { currentState ->
            currentState.copy(
                currentContent = null,
                errorMessage = null // Clear any previous errors
            )
        }
    }

    /**
     * Clears any error messages in the UI state.
     */
    fun clearError() {
        _uiState.update { currentState ->
            currentState.copy(errorMessage = null)
        }
    }
}