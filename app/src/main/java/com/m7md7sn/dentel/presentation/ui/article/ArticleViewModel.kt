package com.m7md7sn.dentel.presentation.ui.article

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.m7md7sn.dentel.data.model.Article
import com.m7md7sn.dentel.data.repository.ArticleRepository
import com.m7md7sn.dentel.presentation.ui.section.Topic
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class ArticleViewModel @Inject constructor(
    private val articleRepository: ArticleRepository
) : ViewModel() {
    private val _uiState = MutableStateFlow(ArticleUiState())
    val uiState: StateFlow<ArticleUiState> = _uiState

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
            }
        }
    }

    fun setArticle(article: Article) {
        _uiState.update { it.copy(article = article) }
    }
}
