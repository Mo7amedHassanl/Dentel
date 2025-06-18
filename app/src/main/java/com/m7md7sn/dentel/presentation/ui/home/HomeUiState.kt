package com.m7md7sn.dentel.presentation.ui.home

import com.m7md7sn.dentel.data.model.Section
import com.m7md7sn.dentel.data.model.SuggestedTopic
import com.m7md7sn.dentel.data.repository.ReminderMessage

/**
 * Sealed interface representing different UI states for the home screen
 */
sealed interface HomeUiState {
    /**
     * Initial loading state
     */
    data object Loading : HomeUiState

    /**
     * Success state with all required data
     */
    data class Success(
        val sections: List<Section> = emptyList(),
        val suggestedTopics: List<SuggestedTopic> = emptyList(),
        val reminder: ReminderMessage? = null
    ) : HomeUiState

    /**
     * Error state when something goes wrong
     */
    data class Error(
        val message: String,
        // Keep some data if available to show partial content
        val sections: List<Section> = emptyList(),
        val suggestedTopics: List<SuggestedTopic> = emptyList(),
        val reminder: ReminderMessage? = null
    ) : HomeUiState
}
