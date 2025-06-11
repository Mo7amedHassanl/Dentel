package com.m7md7sn.dentel.presentation.ui.section

import androidx.lifecycle.ViewModel
import com.m7md7sn.dentel.data.model.Section
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.update

class SectionViewModel : ViewModel() {
    private val _uiState = MutableStateFlow(SectionUiState())
    val uiState: StateFlow<SectionUiState> = _uiState

    private var allTopics: List<Topic> = emptyList()

    fun setSection(section: Section) {
        _uiState.update { it.copy(section = section) }
        loadTopics(section)
    }

    fun selectType(type: TopicType) {
        _uiState.update { it.copy(selectedType = type) }
        filterAndUpdateTopics(type = type, query = _uiState.value.searchQuery)
    }

    private fun loadTopics(section: Section) {
        // TODO: Replace with real data source
        allTopics = listOf(
            Topic(
                title = "حشوات الأسنان \nكل ما تريد معرفته عنها",
                subtitle = "شرح عن حشوات الأسنان وأنواعها ...",
                type = TopicType.Article
            ),
            Topic(
                title = "فيديو عن تنظيف الأسنان",
                subtitle = "فيديو يشرح طريقة تنظيف الأسنان الصحيحة ...",
                type = TopicType.Video
            ),
            Topic(
                title = "مقال عن تبييض الأسنان",
                subtitle = "مقال يشرح طرق تبييض الأسنان ...",
                type = TopicType.Article
            ),
            Topic(
                title = "فيديو عن زراعة الأسنان",
                subtitle = "فيديو يشرح خطوات زراعة الأسنان ...",
                type = TopicType.Video
            )
        )
        filterAndUpdateTopics(type = _uiState.value.selectedType, query = _uiState.value.searchQuery)
    }

    private fun filterAndUpdateTopics(type: TopicType, query: String) {
        val filtered = allTopics.filter { t ->
            t.type == type && (query.isBlank() || t.title.contains(query, ignoreCase = true) || t.subtitle.contains(query, ignoreCase = true))
        }
        _uiState.update { it.copy(topics = filtered) }
    }

    fun onSearchQueryChange(query: String) {
        _uiState.update { it.copy(searchQuery = query) }
        filterAndUpdateTopics(type = _uiState.value.selectedType, query = query)
    }
} 