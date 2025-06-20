package com.m7md7sn.dentel.presentation.ui.home

import androidx.compose.ui.graphics.Color
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.m7md7sn.dentel.data.model.SuggestedTopic
import com.m7md7sn.dentel.data.repository.HomeRepository
import com.m7md7sn.dentel.data.repository.SectionRepository
import com.m7md7sn.dentel.presentation.ui.section.Topic
import com.m7md7sn.dentel.presentation.ui.section.TopicType
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

/**
 * ViewModel for the Home screen
 * Follows MVVM architecture pattern for separation of concerns
 */
@HiltViewModel
class HomeViewModel @Inject constructor(
    private val homeRepository: HomeRepository,
    private val sectionRepository: SectionRepository
) : ViewModel() {

    // Private mutable state flow that can only be modified within the ViewModel
    private val _uiState = MutableStateFlow<HomeUiState>(HomeUiState.Loading)

    // Public immutable state flow that can be observed by the UI
    val uiState: StateFlow<HomeUiState> = _uiState

    // Navigation state for handling suggested topic clicks
    private val _navigationState = MutableStateFlow<NavigationState>(NavigationState.Idle)
    val navigationState: StateFlow<NavigationState> = _navigationState

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
     * Handle click on a suggested topic
     */
    fun onSuggestedTopicClicked(suggestedTopic: SuggestedTopic) {
        viewModelScope.launch {
            // Update navigation state to loading
            _navigationState.update { NavigationState.Loading }

            try {
                // Convert the string type to TopicType enum
                val topicType = when (suggestedTopic.type.lowercase()) {
                    "video" -> TopicType.Video
                    "article" -> TopicType.Article
                    else -> {
                        // If type is not specified clearly, try to determine from the id format or default to Article
                        if (suggestedTopic.id.contains("video", ignoreCase = true)) {
                            TopicType.Video
                        } else {
                            TopicType.Article
                        }
                    }
                }

                // Get topic details from the repository
                val topic = sectionRepository.getTopicById(suggestedTopic.id, topicType)

                if (topic != null) {
                    // Update navigation state with the topic
                    _navigationState.update { NavigationState.NavigateToContent(topic) }
                } else {
                    _navigationState.update { NavigationState.Error("Could not find topic with ID: ${suggestedTopic.id}") }
                }
            } catch (e: Exception) {
                _navigationState.update { NavigationState.Error("Error loading topic: ${e.message}") }
            }
        }
    }

    /**
     * Reset navigation state after navigation is handled
     */
    fun resetNavigationState() {
        _navigationState.update { NavigationState.Idle }
    }

    /**
     * Reload data when user requests a refresh
     */
    fun refreshHomeData() {
        loadHomeData()
    }
}

/**
 * Navigation state for the suggested topic navigation
 */
sealed class NavigationState {
    data object Idle : NavigationState()
    data object Loading : NavigationState()
    data class NavigateToContent(val topic: Topic) : NavigationState()
    data class Error(val message: String) : NavigationState()
}
