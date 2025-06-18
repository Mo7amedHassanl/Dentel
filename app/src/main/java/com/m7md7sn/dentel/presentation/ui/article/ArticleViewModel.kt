package com.m7md7sn.dentel.presentation.ui.article

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.m7md7sn.dentel.data.model.Article
import com.m7md7sn.dentel.data.repository.ArticleRepository
import com.m7md7sn.dentel.data.repository.FavoritesRepository
import com.m7md7sn.dentel.data.model.FavoriteItem
import com.m7md7sn.dentel.data.repository.FavoriteType
import com.m7md7sn.dentel.presentation.ui.section.Topic
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import kotlinx.coroutines.flow.collectLatest
import javax.inject.Inject

@HiltViewModel
class ArticleViewModel @Inject constructor(
    private val articleRepository: ArticleRepository,
    private val favoritesRepository: FavoritesRepository
) : ViewModel() {
    private val _uiState = MutableStateFlow(ArticleUiState())
    val uiState: StateFlow<ArticleUiState> = _uiState
    private var currentFavoriteItem: FavoriteItem? = null

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
}
