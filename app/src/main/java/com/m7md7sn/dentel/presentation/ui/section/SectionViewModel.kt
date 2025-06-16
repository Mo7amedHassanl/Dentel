package com.m7md7sn.dentel.presentation.ui.section

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.google.firebase.firestore.FirebaseFirestore
import com.m7md7sn.dentel.data.model.Section
import com.m7md7sn.dentel.data.model.Video
import com.m7md7sn.dentel.data.model.Article
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import com.m7md7sn.dentel.presentation.ui.section.Topic
import com.m7md7sn.dentel.presentation.ui.section.TopicType
import com.m7md7sn.dentel.presentation.ui.section.SectionUiState
import androidx.compose.ui.graphics.Color
import com.m7md7sn.dentel.R
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.tasks.await
import javax.inject.Inject

@HiltViewModel
class SectionViewModel @Inject constructor(
    private val db: FirebaseFirestore // Injected Firestore instance
) : ViewModel() {
    private val _uiState = MutableStateFlow<SectionUiState>(SectionUiState.Loading)
    val uiState: StateFlow<SectionUiState> = _uiState

    private var allTopics: List<Topic> = emptyList()

    private val allSections = listOf(
        Section(
            id = "regular_filling",
            imageRes = R.drawable.ic_regular_filling,
            titleRes = R.string.regular_filling,
            color = Color(0xFFA6A4ED)
        ),
        Section(
            id = "mobile_denture",
            imageRes = R.drawable.ic_mobile_denture,
            titleRes = R.string.mobile_denture,
            color = Color(0xFF6A89E0)
        ),
        Section(
            id = "nerve_filling",
            imageRes = R.drawable.ic_nerve_filling,
            titleRes = R.string.nerve_filling,
            color = Color(0xFF43B4DB)
        ),
        Section(
            id = "fixed_denture",
            imageRes = R.drawable.ic_fixed_denture,
            titleRes = R.string.fixed_denture,
            color = Color(0xFF96CCBC)
        ),
        Section(
            id = "bedo",
            imageRes = R.drawable.ic_bedo_teeth,
            titleRes = R.string.bedo,
            color = Color(0xFFD37C9C)
        ),
        Section(
            id = "diagnosis",
            imageRes = R.drawable.ic_diagnosis,
            titleRes = R.string.diagnosis,
            color = Color(0xFFCAAD95)
        ),
    )

    fun setSection(section: Section) {
        _uiState.update {
            if (it is SectionUiState.Success) {
                it.copy(section = section)
            } else {
                SectionUiState.Success(section = section) // Default to success with the new section
            }
        }
        loadTopics(section.id)
    }

    fun setSectionById(sectionId: String) {
        val section = allSections.find { it.id == sectionId }
        if (section != null) {
            _uiState.value = SectionUiState.Loading
            loadTopics(section.id)
        } else {
            _uiState.value = SectionUiState.Error("Section not found")
        }
    }

    fun selectType(type: TopicType) {
        _uiState.update {
            if (it is SectionUiState.Success) {
                it.copy(selectedType = type)
            } else {
                it // Do not change state if not in Success
            }
        }
        if (_uiState.value is SectionUiState.Success) {
            filterAndUpdateTopics(type = type, query = (_uiState.value as SectionUiState.Success).searchQuery)
        }
    }

    private fun loadTopics(sectionId: String) {
        viewModelScope.launch {
            _uiState.value = SectionUiState.Loading
            try {
            val topicsList = mutableListOf<Topic>()

                val articlesSnapshot = db.collection("sections").document(sectionId).collection("articles").get().await()
                for (doc in articlesSnapshot) {
                        val article = doc.toObject(Article::class.java).copy(id = doc.id)
                        topicsList.add(
                            Topic(
                                id = article.id,
                                title = article.title,
                                subtitle = article.subtitle,
                                type = TopicType.Article,
                                content = article.content,
                                imageUrl = article.imageUrl
                            )
                        )
                    }

                val videosSnapshot = db.collection("sections").document(sectionId).collection("videos").get().await()
                for (doc in videosSnapshot) {
                                val video = doc.toObject(Video::class.java).copy(id = doc.id)
                                topicsList.add(
                                    Topic(
                                        id = video.id,
                                        title = video.title,
                                        subtitle = video.subtitle,
                                        type = TopicType.Video,
                                        videoUrl = video.videoUrl,
                            thumbnailUrl = video.thumbnailUrl,
                            content = video.description
                                    )
                                )
                            }
                            allTopics = topicsList
                _uiState.update { currentState ->
                    SectionUiState.Success(
                        section = if (currentState is SectionUiState.Success) currentState.section else allSections.find { it.id == sectionId },
                        topics = filterTopics(
                            (currentState as? SectionUiState.Success)?.selectedType ?: TopicType.Article,
                            (currentState as? SectionUiState.Success)?.searchQuery ?: ""
                        ),
                        selectedType = (currentState as? SectionUiState.Success)?.selectedType ?: TopicType.Article,
                        searchQuery = (currentState as? SectionUiState.Success)?.searchQuery ?: ""
                    )
                }
            } catch (e: Exception) {
                _uiState.value = SectionUiState.Error("Failed to load topics: ${e.message}")
            }
        }
    }

    private fun filterTopics(type: TopicType, query: String): List<Topic> {
        return allTopics.filter { t ->
            t.type == type && (query.isBlank() || t.title.contains(query, ignoreCase = true) || t.subtitle.contains(query, ignoreCase = true))
        }
    }

    private fun filterAndUpdateTopics(type: TopicType, query: String) {
        _uiState.update {
            if (it is SectionUiState.Success) {
                it.copy(topics = filterTopics(type, query))
            } else {
                it // Do not change state if not in Success
            }
        }
    }

    fun onSearchQueryChange(query: String) {
        _uiState.update { currentState ->
            if (currentState is SectionUiState.Success) {
                currentState.copy(searchQuery = query)
            } else {
                currentState // Do not change state if not in Success
            }
        }
        if (_uiState.value is SectionUiState.Success) {
            filterAndUpdateTopics(type = (_uiState.value as SectionUiState.Success).selectedType, query = query)
        }
    }
} 