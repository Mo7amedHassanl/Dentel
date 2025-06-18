package com.m7md7sn.dentel.presentation.ui.article

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.DisposableEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import com.m7md7sn.dentel.R
import com.m7md7sn.dentel.data.model.SuggestedTopic
import com.m7md7sn.dentel.presentation.theme.DentelDarkPurple
import com.m7md7sn.dentel.presentation.theme.DentelLightPurple
import com.m7md7sn.dentel.presentation.ui.article.components.ArticleContent
import com.m7md7sn.dentel.presentation.ui.article.components.ArticleImage
import com.m7md7sn.dentel.presentation.ui.article.components.ArticleTitle
import com.m7md7sn.dentel.presentation.ui.home.SubtitleWithLogo
import com.m7md7sn.dentel.presentation.ui.home.SuggestedTopicsList
import com.m7md7sn.dentel.presentation.ui.section.Topic

@Composable
fun ArticleScreen(
    topic: Topic?,
    modifier: Modifier = Modifier,
    viewModel: ArticleViewModel = hiltViewModel()
) {
    val currentLanguage = LocalConfiguration.current.locale.language

    // Load article data from topic when screen is first displayed
    DisposableEffect(topic) {
        if (topic != null) {
            viewModel.loadArticleFromTopic(topic)
        }
        onDispose { }
    }

    // Observe UI state
    val uiState by viewModel.uiState.collectAsState()

    Surface(
        modifier = modifier.fillMaxSize(),
        color = DentelDarkPurple
    ) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .verticalScroll(enabled = true, state = rememberScrollState()),
            horizontalAlignment = Alignment.CenterHorizontally,
        ) {
            // Display title from Topic or fetched Article
            ArticleTitle(title = uiState.article?.title ?: topic?.title ?: "")

            HorizontalDivider(
                Modifier.width(330.dp),
                color = Color(0xFF444B88),
            )
            Spacer(modifier = Modifier.height(28.dp))

            // If loading, show progress indicator
            if (uiState.isLoading) {
                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(200.dp),
                    contentAlignment = Alignment.Center
                ) {
                    CircularProgressIndicator(color = Color.White)
                }
            } else {
                // Display article image
                ArticleImage(
                    imageUrl = uiState.article?.imageUrl ?: topic?.imageUrl
                )
            }

            Spacer(modifier = Modifier.height(28.dp))

            // Error state handling
            if (uiState.errorMessage != null) {
                Text(
                    text = uiState.errorMessage ?: "",
                    color = Color.White,
                    modifier = Modifier.padding(16.dp)
                )
            } else {
                // Article content with like/share buttons
                ArticleContent(
                    content = uiState.article?.content ?: topic?.content ?: "",
                    title = uiState.article?.title ?: topic?.title ?: ""
                )
            }

            Spacer(modifier = Modifier.height(32.dp))

            // Suggested topics section
            Box(modifier = Modifier.fillMaxWidth()) {
                SubtitleWithLogo(
                    titleRes = R.string.suggested_topics,
                    highlightedText = if (currentLanguage == "ar") "مواضيع" else "Topics",
                    highlightedTextColor = DentelLightPurple
                )
            }

            Spacer(Modifier.height(16.dp))

            // Suggested topics list
            SuggestedTopicsList(
                listOf(
                    SuggestedTopic(" حشوات الأسنان \nكل ما تريد معرفته عنها"),
                    SuggestedTopic("Root Canal Treatment"),
                    SuggestedTopic("Teeth Whitening"),
                    SuggestedTopic("Orthodontics"),
                    SuggestedTopic("Cosmetic Dentistry"),
                    SuggestedTopic("Oral Hygiene Tips")
                )
            )
        }
    }
}
