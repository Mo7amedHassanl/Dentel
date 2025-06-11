package com.m7md7sn.dentel.presentation.ui.section

import androidx.compose.ui.graphics.Color
import com.m7md7sn.dentel.data.model.Section

sealed class TopicType { object Article : TopicType(); object Video : TopicType() }

data class Topic(
    val title: String,
    val subtitle: String,
    val type: TopicType
)

data class SectionUiState(
    val section: Section? = null,
    val selectedType: TopicType = TopicType.Article,
    val topics: List<Topic> = emptyList(),
    val searchQuery: String = ""
) 