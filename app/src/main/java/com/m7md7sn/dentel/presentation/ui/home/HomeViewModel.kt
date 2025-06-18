package com.m7md7sn.dentel.presentation.ui.home

import androidx.compose.ui.graphics.Color
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.m7md7sn.dentel.data.repository.HomeRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.launch
import javax.inject.Inject

/**
 * ViewModel for the Home screen
 * Follows MVVM architecture pattern for separation of concerns
 */
@HiltViewModel
class HomeViewModel @Inject constructor(
    private val homeRepository: HomeRepository
) : ViewModel() {

    // Private mutable state flow that can only be modified within the ViewModel
    private val _uiState = MutableStateFlow<HomeUiState>(HomeUiState.Loading)

    // Public immutable state flow that can be observed by the UI
    val uiState: StateFlow<HomeUiState> = _uiState

    init {
        loadHomeData()
    }

    /**
     * Loads all required data for the home screen
     * Combines multiple data streams and handles errors
     */
    private fun loadHomeData() {
        viewModelScope.launch {
            _uiState.value = HomeUiState.Loading

            // Combine all three data sources into a single flow
            combine(
                homeRepository.getSections(),
                homeRepository.getSuggestedTopics(),
                homeRepository.getDailyReminder()
            ) { sections, suggestedTopics, reminder ->
                HomeUiState.Success(
                    sections = sections,
                    suggestedTopics = suggestedTopics,
                    reminder = reminder
                )
            }.catch { error ->
                // Handle errors but try to show any data we might have
                val currentState = _uiState.value
                val partialData = if (currentState is HomeUiState.Success) {
                    HomeUiState.Error(
                        message = error.message ?: "Unknown error occurred",
                        sections = currentState.sections,
                        suggestedTopics = currentState.suggestedTopics,
                        reminder = currentState.reminder
                    )
                } else {
                    HomeUiState.Error(message = error.message ?: "Unknown error occurred")
                }
                _uiState.value = partialData
            }.collect { state ->
                _uiState.value = state
            }
        }
    }

    /**
     * Reload data when user requests a refresh
     */
    fun refreshHomeData() {
        loadHomeData()
    }
}
