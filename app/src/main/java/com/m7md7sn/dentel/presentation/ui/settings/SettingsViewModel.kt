package com.m7md7sn.dentel.presentation.ui.settings

import android.app.Application
import android.content.ActivityNotFoundException
import android.content.Intent
import android.widget.Toast
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.m7md7sn.dentel.data.repository.AuthRepository
import com.m7md7sn.dentel.utils.LocaleUtils
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.receiveAsFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import java.util.Locale
import javax.inject.Inject

/**
 * ViewModel responsible for managing settings screen state and user interactions.
 * Follows MVVM architecture pattern with unidirectional data flow.
 */
@HiltViewModel
class SettingsViewModel @Inject constructor(
    private val authRepository: AuthRepository,
    private val application: Application
) : ViewModel() {

    // Private mutable state flow that can only be modified within the ViewModel
    private val _uiState = MutableStateFlow(SettingsUiState())

    // Public immutable state flow that can be observed by the UI
    val uiState: StateFlow<SettingsUiState> = _uiState.asStateFlow()

    private val _eventChannel = Channel<Event>()
    val eventFlow = _eventChannel.receiveAsFlow()

    init {
        // Initialize with saved language preference or current locale if no preference
        val savedLanguage = LocaleUtils.getSavedLanguage(application)
        val currentLanguage = if (savedLanguage != null) {
            if (savedLanguage == "ar") "Arabic" else "English"
        } else {
            LocaleUtils.getDisplayLanguage(Locale.getDefault())
        }

        _uiState.update { it.copy(selectedLanguage = currentLanguage) }
    }

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
     * Updates the selected language and app locale.
     */
    fun onLanguageSelected(language: String) {
        viewModelScope.launch {
            _uiState.update { it.copy(selectedLanguage = language) }
            val langCode = if (language == "Arabic") "ar" else "en"

            // Update the app locale using our enhanced utility
            LocaleUtils.setLocale(application, langCode)

            // Notify UI that we need to recreate the activity to apply changes
            _eventChannel.send(Event.RecreateActivity)
        }
    }

    /**
     * Updates the support email field and validates the format
     *
     * @param email The email input from user
     */
    fun updateSupportEmail(email: String) {
        _uiState.update {
            it.copy(
                supportEmail = email,
                isEmailError = email.isNotEmpty() && !isValidEmail(email)
            )
        }
    }

    /**
     * Updates the support message field and validates it's not empty
     *
     * @param message The message input from user
     */
    fun updateSupportMessage(message: String) {
        _uiState.update {
            it.copy(
                supportMessage = message,
                isMessageError = message.isEmpty()
            )
        }
    }

    /**
     * Validates and sends a support email if inputs are valid
     */
    fun validateAndSendSupportEmail() {
        val currentState = _uiState.value
        val isEmailValid = isValidEmail(currentState.supportEmail)
        val isMessageValid = currentState.supportMessage.isNotEmpty()

        _uiState.update {
            it.copy(
                isEmailError = !isEmailValid,
                isMessageError = !isMessageValid
            )
        }

        if (isEmailValid && isMessageValid) {
            sendSupportEmail(currentState.supportEmail, currentState.supportMessage)
        }
    }

    /**
     * Clear support form fields after successful submission
     */
    private fun clearSupportForm() {
        _uiState.update {
            it.copy(
                supportEmail = "",
                supportMessage = "",
                isEmailError = false,
                isMessageError = false
            )
        }
    }

    /**
     * Validates email format
     */
    private fun isValidEmail(email: String): Boolean {
        val emailRegex = "^[A-Za-z0-9+_.-]+@[A-Za-z0-9.-]+\\.[A-Za-z]{2,6}\$"
        return email.matches(emailRegex.toRegex())
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

    /**
     * Sends a support email to the developer
     *
     * @param email User's email address
     * @param message Support message content
     */
    fun sendSupportEmail(email: String, message: String) {
        viewModelScope.launch {
            _uiState.update { it.copy(isLoading = true) }

            try {
                // Constants
                val developerEmail = "dev.m7md7asn@gmail.com"// Replace with actual developer email
                val subject = "Dentel App Support Request"

                // Format the message to include the sender's email
                val formattedMessage = """
                    From: $email

                    $message
                """.trimIndent()

                val intent = Intent(Intent.ACTION_SEND).apply {
                    type = "message/rfc822"
                    putExtra(Intent.EXTRA_EMAIL, arrayOf(developerEmail))
                    putExtra(Intent.EXTRA_SUBJECT, subject)
                    putExtra(Intent.EXTRA_TEXT, formattedMessage)
                    addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
                }

                try {
                    application.startActivity(Intent.createChooser(intent, "Send email using...").apply {
                        addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
                    })

                    // Clear the form after successful send
                    clearSupportForm()

                    // Send successful event
                    _eventChannel.send(Event.EmailSendSuccess)

                } catch (e: ActivityNotFoundException) {
                    // No email app found
                    _eventChannel.send(Event.EmailSendError("No email app found on this device"))
                }
            } catch (e: Exception) {
                // General error
                _eventChannel.send(Event.EmailSendError("Failed to send email: ${e.message}"))
            } finally {
                _uiState.update { it.copy(isLoading = false) }
            }
        }
    }

    sealed class Event {
        object RecreateActivity : Event()
        object EmailSendSuccess : Event()
        data class EmailSendError(val message: String) : Event()
    }
}