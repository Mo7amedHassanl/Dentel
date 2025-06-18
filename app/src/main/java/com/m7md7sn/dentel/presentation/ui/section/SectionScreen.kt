package com.m7md7sn.dentel.presentation.ui.section

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalLayoutDirection
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.LayoutDirection
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import com.m7md7sn.dentel.R
import com.m7md7sn.dentel.presentation.theme.DentelDarkPurple
import com.m7md7sn.dentel.presentation.theme.DentelTheme
import com.m7md7sn.dentel.presentation.ui.section.components.ContentTypeButtons
import com.m7md7sn.dentel.presentation.ui.section.components.SectionHeader
import com.m7md7sn.dentel.presentation.ui.section.components.SectionSearchBar
import com.m7md7sn.dentel.presentation.ui.section.components.TopicsOrEmptyMessage

/**
 * Main screen for displaying a section and its topics
 */
@Composable
fun SectionScreen(
    sectionId: String,
    modifier: Modifier = Modifier,
    viewModel: SectionViewModel = hiltViewModel(),
    onTopicClick: (Topic) -> Unit = {}
) {
    // Collect UI state
    val uiState by viewModel.uiState.collectAsState()
    val favoriteTopicIds by viewModel.favoriteTopicIds.collectAsState()

    // Set section ID only once
    LaunchedEffect(sectionId) {
        viewModel.setSectionById(sectionId)
    }

    Surface(
        modifier = modifier.fillMaxSize(),
        color = DentelDarkPurple
    ) {
        Box(modifier = Modifier.fillMaxSize()) {
            // Background decoration
            CompositionLocalProvider(LocalLayoutDirection provides LayoutDirection.Ltr) {
                Box(modifier = Modifier.fillMaxWidth()) {
                    Image(
                        painter = painterResource(id = R.drawable.section_tooth_bg),
                        contentDescription = null,
                        modifier = Modifier
                            .padding(top = 48.dp)
                            .align(Alignment.TopEnd)
                            .alpha(0.3f)
                    )
                }
            }

            // Main content
            Column(
                modifier = Modifier.fillMaxSize(),
                horizontalAlignment = Alignment.CenterHorizontally,
            ) {
                // Extract common state data regardless of state type
                val section = when (uiState) {
                    is SectionUiState.Success -> (uiState as SectionUiState.Success).section
                    is SectionUiState.Empty -> (uiState as SectionUiState.Empty).section
                    is SectionUiState.Error -> (uiState as SectionUiState.Error).section
                    else -> null
                }

                val searchQuery = when (uiState) {
                    is SectionUiState.Success -> (uiState as SectionUiState.Success).searchQuery
                    is SectionUiState.Empty -> (uiState as SectionUiState.Empty).searchQuery
                    is SectionUiState.Error -> (uiState as SectionUiState.Error).searchQuery
                    else -> ""
                }

                val selectedType = when (uiState) {
                    is SectionUiState.Success -> (uiState as SectionUiState.Success).selectedType
                    is SectionUiState.Empty -> (uiState as SectionUiState.Empty).selectedType
                    is SectionUiState.Error -> (uiState as SectionUiState.Error).selectedType
                    else -> TopicType.Article
                }

                // Display section header if section data is available
                section?.let {
                    SectionHeader(section = it)
                }

                // Search bar
                SectionSearchBar(
                    query = searchQuery,
                    onQueryChange = viewModel::onSearchQueryChange
                )

                Spacer(modifier = Modifier.height(32.dp))

                // Content type toggle buttons
                ContentTypeButtons(
                    selectedType = selectedType,
                    onTypeSelected = viewModel::selectType,
                )

                Spacer(modifier = Modifier.height(10.dp))

                // Display appropriate content based on UI state
                when (uiState) {
                    is SectionUiState.Loading -> {
                        TopicsOrEmptyMessage(
                            topics = emptyList(), // Show empty list while loading
                            onTopicClick = onTopicClick,
                            isLoading = true,
                            modifier = Modifier.weight(1f)
                        )
                    }

                    is SectionUiState.Error -> {
                        Text(
                            text = "Error: ${(uiState as SectionUiState.Error).message}",
                            color = Color.Red,
                            modifier = Modifier.padding(16.dp)
                        )
                    }

                    is SectionUiState.Success -> {
                        val successState = uiState as SectionUiState.Success
                        TopicsOrEmptyMessage(
                            topics = successState.topics,
                            onTopicClick = onTopicClick,
                            favoriteTopicIds = favoriteTopicIds,
                            isLoading = successState.isSearching,
                            modifier = Modifier.weight(1f)
                        )
                    }

                    is SectionUiState.Empty -> {
                        TopicsOrEmptyMessage(
                            topics = emptyList(), // Empty list for empty state
                            onTopicClick = onTopicClick,
                            isLoading = false,
                            modifier = Modifier.weight(1f)
                        )
                    }
                }
            }
        }
    }
}

@Preview
@Composable
private fun SectionScreenPreviewEn() {
    DentelTheme {
        SectionScreen("mobile_denture")
    }
}

@Preview(locale = "ar")
@Composable
private fun SectionScreenPreviewAr() {
    DentelTheme {
        SectionScreen("mobile_denture")
    }
}
