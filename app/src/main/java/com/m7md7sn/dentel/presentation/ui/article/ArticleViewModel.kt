package com.m7md7sn.dentel.presentation.ui.article

import androidx.lifecycle.ViewModel
import com.m7md7sn.dentel.data.model.Article
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.update

class ArticleViewModel : ViewModel() {
    private val _uiState = MutableStateFlow(ArticleUiState())
    val uiState: StateFlow<ArticleUiState> = _uiState

    fun setArticle(article: Article) {
        _uiState.update { it.copy(article = article) }
    }
} 