package com.m7md7sn.dentel.presentation.ui.video

import com.m7md7sn.dentel.data.model.SuggestedTopic
import com.m7md7sn.dentel.data.model.Video
 
data class VideoUiState(
    val video: Video? = null,
    val isLoading: Boolean = false,
    val errorMessage: String? = null,
    val isFavorite: Boolean = false,
    val suggestedTopics: List<SuggestedTopic> = emptyList(),
    val suggestedTopicsError: String? = null
)
