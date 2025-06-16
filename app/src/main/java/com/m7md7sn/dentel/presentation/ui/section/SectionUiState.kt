package com.m7md7sn.dentel.presentation.ui.section

import androidx.compose.ui.graphics.Color
import com.m7md7sn.dentel.data.model.Section
import android.os.Parcelable
import kotlinx.parcelize.Parcelize

enum class TopicType(val value: Int) { Article(0), Video(1) }

@Parcelize
data class Topic(
    val id: String = "",
    val title: String = "",
    val subtitle: String = "",
    val type: TopicType,
    val videoUrl: String? = null,
    val content: String? = null,
    val imageUrl: String? = null,
    val thumbnailUrl: String? = null
) : Parcelable

sealed interface SectionUiState {
    data object Loading : SectionUiState
    data class Success(
    val section: Section? = null,
    val selectedType: TopicType = TopicType.Article,
    val topics: List<Topic> = emptyList(),
        val searchQuery: String = ""
    ) : SectionUiState
    data class Error(val message: String) : SectionUiState
} 