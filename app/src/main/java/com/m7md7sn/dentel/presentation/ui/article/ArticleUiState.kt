package com.m7md7sn.dentel.presentation.ui.article

import com.m7md7sn.dentel.data.model.Article
import com.m7md7sn.dentel.data.model.SuggestedTopic

data class ArticleUiState(
    val article: Article? = null,
    val isLoading: Boolean = false,
    val errorMessage: String? = null,
    val isFavorite: Boolean = false,
    val suggestedTopics: List<SuggestedTopic> = emptyList(),
    val suggestedTopicsError: String? = null,
    val isLoadingSuggestedTopics: Boolean = false
)
