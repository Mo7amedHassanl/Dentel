package com.m7md7sn.dentel.presentation.ui.splash

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.m7md7sn.dentel.data.repository.AuthRepository
import com.m7md7sn.dentel.utils.NetworkMonitor
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

/**
 * ViewModel for handling splash screen logic
 * Manages authentication state and network connectivity
 */
@HiltViewModel
class SplashViewModel @Inject constructor(
    private val authRepository: AuthRepository,
    private val networkMonitor: NetworkMonitor
) : ViewModel() {

    // Create a UI state to represent all state variables in one object
    private val _uiState = MutableStateFlow(SplashUiState())
    val uiState: StateFlow<SplashUiState> = _uiState.asStateFlow()

    // Network connectivity state
    val isOnline: StateFlow<Boolean> = networkMonitor.isOnline
        .map { it }
        .stateIn(
            scope = viewModelScope,
            started = SharingStarted.WhileSubscribed(5000L),
            initialValue = isInternetConnected()
        )

    init {
        // Initialize state when ViewModel is created
        viewModelScope.launch {
            _uiState.update { currentState ->
                currentState.copy(
                    isUserSignedIn = isUserSignedIn(),
                    isOnline = isInternetConnected()
                )
            }
        }

        // Listen for network changes
        viewModelScope.launch {
            networkMonitor.isOnline.collect { online ->
                _uiState.update { it.copy(isOnline = online) }
            }
        }
    }

    /**
     * Check if user is currently signed in
     */
    fun isUserSignedIn(): Boolean {
        val isSignedIn = authRepository.currentUser != null
        // Update state
        _uiState.update { it.copy(isUserSignedIn = isSignedIn) }
        return isSignedIn
    }

    /**
     * Check if device has an internet connection
     */
    fun isInternetConnected(): Boolean {
        val isConnected = networkMonitor.isInternetConnected()
        // Update state
        _uiState.update { it.copy(isOnline = isConnected) }
        return isConnected
    }

    /**
     * Handle navigation based on authentication state
     */
    fun checkAuthAndConnectivityState(): NavigationState {
        val isSignedIn = isUserSignedIn()
        val isConnected = isInternetConnected()

        return when {
            !isConnected -> NavigationState.NoInternet
            isSignedIn -> NavigationState.NavigateToHome
            else -> NavigationState.NavigateToLogin
        }
    }
}

/**
 * Represents the UI state for Splash Screen
 */
data class SplashUiState(
    val isLoading: Boolean = true,
    val isUserSignedIn: Boolean = false,
    val isOnline: Boolean = true,
    val showNoInternetDialog: Boolean = false
)

/**
 * Possible navigation destinations from Splash Screen
 */
sealed class NavigationState {
    object NavigateToHome : NavigationState()
    object NavigateToLogin : NavigationState()
    object NoInternet : NavigationState()
}
