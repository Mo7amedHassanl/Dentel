package com.m7md7sn.dentel.presentation.ui.article

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.m7md7sn.dentel.data.model.Article
import com.m7md7sn.dentel.data.model.SuggestedTopic
import com.m7md7sn.dentel.data.repository.ArticleRepository
import com.m7md7sn.dentel.data.repository.FavoritesRepository
import com.m7md7sn.dentel.data.model.FavoriteItem
import com.m7md7sn.dentel.data.repository.FavoriteType
import com.m7md7sn.dentel.data.repository.HomeRepository
import com.m7md7sn.dentel.data.repository.SectionRepository
import com.m7md7sn.dentel.presentation.ui.section.Topic
import com.m7md7sn.dentel.presentation.ui.section.TopicType
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import kotlinx.coroutines.flow.collectLatest
import javax.inject.Inject

@HiltViewModel
class ArticleViewModel @Inject constructor(
    private val articleRepository: ArticleRepository,
    private val favoritesRepository: FavoritesRepository,
    private val homeRepository: HomeRepository,
    private val sectionRepository: SectionRepository
) : ViewModel() {
    private val _uiState = MutableStateFlow(ArticleUiState())
    val uiState: StateFlow<ArticleUiState> = _uiState
    private var currentFavoriteItem: FavoriteItem? = null

    // Navigation state for handling suggested topic clicks
    private val _navigationState = MutableStateFlow<NavigationState>(NavigationState.Idle)
    val navigationState: StateFlow<NavigationState> = _navigationState

    init {
        loadSuggestedTopics()
    }

    fun loadArticle(articleId: String) {
        viewModelScope.launch {
            _uiState.update { it.copy(isLoading = true) }
            articleRepository.getArticleDetails(articleId).collect { article ->
                _uiState.update {
                    it.copy(
                        article = article,
                        isLoading = false,
                        errorMessage = if (article == null) "Unable to load article" else null
                    )
                }
                article?.let { observeFavorite(it) }
            }
        }
    }

    fun loadArticleFromTopic(topic: Topic) {
        viewModelScope.launch {
            _uiState.update { it.copy(isLoading = true) }
            articleRepository.getArticleFromTopic(topic).collect { article ->
                _uiState.update {
                    it.copy(
                        article = article,
                        isLoading = false,
                        errorMessage = if (article == null) "Unable to load article content" else null
                    )
                }
                article?.let { observeFavorite(it) }
            }
        }
    }

    private fun loadSuggestedTopics() {
        viewModelScope.launch {
            homeRepository.getSuggestedTopics()
                .catch { error ->
                    _uiState.update { it.copy(
                        suggestedTopicsError = "Failed to load suggested topics: ${error.message}"
                    ) }
                }
                .collect { topics ->
                    _uiState.update { it.copy(
                        suggestedTopics = topics
                    ) }
                }
        }
    }

    private fun observeFavorite(article: Article) {
        val favItem = FavoriteItem(
            id = article.id,
            title = article.title,
            type = FavoriteType.ARTICLE
        )
        currentFavoriteItem = favItem
        viewModelScope.launch {
            favoritesRepository.getFavoriteArticles().collectLatest { favs ->
                _uiState.update { it.copy(isFavorite = favs.any { it.id == favItem.id }) }
            }
        }
    }

    fun toggleFavorite() {
        val favItem = currentFavoriteItem ?: return
        viewModelScope.launch {
            favoritesRepository.toggleFavorite(favItem)
            favoritesRepository.getFavoriteArticles().collectLatest { favs ->
                _uiState.update { it.copy(isFavorite = favs.any { it.id == favItem.id }) }
                return@collectLatest
            }
        }
    }

    fun setArticle(article: Article) {
        _uiState.update { it.copy(article = article) }
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
