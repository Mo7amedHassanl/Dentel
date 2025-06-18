package com.m7md7sn.dentel.presentation.ui.home

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import com.m7md7sn.dentel.R
import com.m7md7sn.dentel.presentation.theme.DentelDarkPurple
import com.m7md7sn.dentel.presentation.theme.DentelTheme
import com.m7md7sn.dentel.presentation.ui.home.components.HomeHeader
import com.m7md7sn.dentel.presentation.ui.home.components.ReminderCard
import com.m7md7sn.dentel.presentation.ui.home.components.SectionTitleWithLogo
import com.m7md7sn.dentel.presentation.ui.home.components.SectionsGrid
import com.m7md7sn.dentel.presentation.ui.home.components.SubtitleWithLogo
import com.m7md7sn.dentel.presentation.ui.home.components.SuggestedTopicsList

/**
 * Main Home screen that displays sections, suggested topics, and daily reminders
 */
@Composable
fun HomeScreen(
    modifier: Modifier = Modifier,
    viewModel: HomeViewModel = hiltViewModel(),
    onSectionClick: (String) -> Unit = {}
) {
    val uiState by viewModel.uiState.collectAsState()
    val currentLanguage = LocalConfiguration.current.locale.language
    val scrollState = rememberScrollState()

    Surface(
        modifier = modifier.fillMaxSize(),
        color = DentelDarkPurple
    ) {
        when (uiState) {
            is HomeUiState.Loading -> {
                // Display loading indicator
                Box(
                    modifier = Modifier.fillMaxSize(),
                    contentAlignment = Alignment.Center
                ) {
                    CircularProgressIndicator(color = Color.White)
                }
            }

            is HomeUiState.Error -> {
                val errorState = uiState as HomeUiState.Error

                if (errorState.sections.isEmpty()) {
                    // Show error message if we have no data to display
                    Box(
                        modifier = Modifier.fillMaxSize(),
                        contentAlignment = Alignment.Center
                    ) {
                        Text(
                            text = errorState.message,
                            color = Color.White,
                            modifier = Modifier.padding(16.dp)
                        )
                    }
                } else {
                    // Display content with available data even if there was an error
                    HomeContent(
                        sections = errorState.sections,
                        suggestedTopics = errorState.suggestedTopics,
                        reminder = errorState.reminder,
                        currentLanguage = currentLanguage,
                        scrollState = scrollState,
                        onSectionClick = onSectionClick
                    )
                }
            }

            is HomeUiState.Success -> {
                val successState = uiState as HomeUiState.Success

                // Display full content
                HomeContent(
                    sections = successState.sections,
                    suggestedTopics = successState.suggestedTopics,
                    reminder = successState.reminder,
                    currentLanguage = currentLanguage,
                    scrollState = scrollState,
                    onSectionClick = onSectionClick
                )
            }
        }
    }
}

/**
 * Content of the home screen when data is available
 */
@Composable
private fun HomeContent(
    sections: List<com.m7md7sn.dentel.data.model.Section>,
    suggestedTopics: List<com.m7md7sn.dentel.data.model.SuggestedTopic>,
    reminder: com.m7md7sn.dentel.data.repository.ReminderMessage?,
    currentLanguage: String,
    scrollState: androidx.compose.foundation.ScrollState,
    onSectionClick: (String) -> Unit
) {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .verticalScroll(scrollState),
        horizontalAlignment = Alignment.CenterHorizontally,
    ) {
        // Header with app title and description
        HomeHeader(
            modifier = Modifier.fillMaxSize()
        )

        // Title for sections area
        SectionTitleWithLogo(
            titleRes = R.string.choose_part,
            highlightedText = if (currentLanguage == "ar") "القسم" else "the section"
        )

        Spacer(Modifier.height(16.dp))

        // Grid of dental sections
        SectionsGrid(
            sections = sections,
            onSectionClick = { section ->
                onSectionClick(section.id)
            }
        )

        Spacer(Modifier.height(24.dp))

        // Suggested topics title and list
        Box(modifier = Modifier.fillMaxSize()) {
            SubtitleWithLogo(
                titleRes = R.string.suggested_topics,
                highlightedText = if (currentLanguage == "ar") "مواضيع" else "Topics",
                highlightedTextColor = com.m7md7sn.dentel.presentation.theme.DentelLightPurple
            )
        }

        Spacer(Modifier.height(16.dp))

        // Suggested topics carousel
        SuggestedTopicsList(
            topics = suggestedTopics
        )

        Spacer(Modifier.height(24.dp))

        // Daily reminder card
        ReminderCard(reminder = reminder)

        Spacer(Modifier.height(64.dp))
    }
}

@Preview
@Composable
private fun HomeScreenPreviewEn() {
    DentelTheme {
        HomeScreen(onSectionClick = {})
    }
}

@Preview(locale = "ar")
@Composable
private fun HomeScreenPreviewAr() {
    DentelTheme {
        HomeScreen(onSectionClick = {})
    }
}