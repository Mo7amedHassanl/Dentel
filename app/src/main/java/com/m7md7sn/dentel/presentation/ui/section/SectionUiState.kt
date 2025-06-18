package com.m7md7sn.dentel.presentation.ui.section

import androidx.compose.ui.graphics.Color
import com.m7md7sn.dentel.data.model.Section
import android.os.Parcelable
import kotlinx.parcelize.Parcelize

enum class TopicType(val value: Int) { Article(0), Video(1) }

/**
 * Represents a topic item in a section
 * Could be either an article or a video
 */
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

/**
 * Sealed interface representing the different states of the Section UI
 */
sealed interface SectionUiState {
    /**
     * Initial loading state
     */
    data object Loading : SectionUiState

    /**
     * Empty state when no topics are found for the section
     */
    data class Empty(
        val section: Section?,
        val selectedType: TopicType = TopicType.Article,
        val searchQuery: String = ""
    ) : SectionUiState

    /**
     * Success state with loaded data
     */
    data class Success(
        val section: Section?,
        val selectedType: TopicType = TopicType.Article,
        val topics: List<Topic> = emptyList(),
        val searchQuery: String = "",
        val isSearching: Boolean = false
    ) : SectionUiState

    /**
     * Error state when something went wrong
     */
    data class Error(
        val message: String,
        val section: Section? = null,
        val selectedType: TopicType = TopicType.Article,
        val searchQuery: String = ""
    ) : SectionUiState
}
