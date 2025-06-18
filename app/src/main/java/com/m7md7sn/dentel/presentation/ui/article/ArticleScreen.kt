package com.m7md7sn.dentel.presentation.ui.article

import android.content.Context
import android.content.Intent
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalLayoutDirection
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.Font
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.LayoutDirection
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import coil.compose.AsyncImage
import com.m7md7sn.dentel.R
import com.m7md7sn.dentel.data.model.Article
import com.m7md7sn.dentel.data.model.SuggestedTopic
import com.m7md7sn.dentel.presentation.theme.DentelDarkPurple
import com.m7md7sn.dentel.presentation.theme.DentelLightPurple
import com.m7md7sn.dentel.presentation.theme.DentelTheme
import com.m7md7sn.dentel.presentation.ui.home.SubtitleWithLogo
import com.m7md7sn.dentel.presentation.ui.home.SuggestedTopicsList
import androidx.lifecycle.ViewModel
import androidx.hilt.navigation.compose.hiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.update
import com.m7md7sn.dentel.presentation.ui.article.ArticleUiState
import com.m7md7sn.dentel.presentation.ui.section.Topic
import com.m7md7sn.dentel.presentation.ui.video.components.LikeAndShareButtons

@Composable
fun ArticleScreen(topic: Topic?, modifier: Modifier = Modifier) {
    val currentLanguage = LocalConfiguration.current.locale.language

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
            ArticleVideoTitle(title = topic?.title ?: "")
            HorizontalDivider(
                Modifier
                    .width(330.dp),
                color = Color(0xFF444B88),
            )
            Spacer(modifier = Modifier.height(28.dp))

            // Use AsyncImage from Coil to load the article image from URL
            if (topic?.imageUrl != null) {
                // Create a loading state to track the image loading progress
                var isLoading by remember { mutableStateOf(true) }

                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(200.dp)
                        .padding(horizontal = 32.dp)
                ) {
                    AsyncImage(
                        model = topic.imageUrl,
                        contentDescription = "Article Image",
                        contentScale = ContentScale.Crop,
                        modifier = Modifier
                            .fillMaxSize()
                            .clip(RoundedCornerShape(8.dp)),
                        fallback = painterResource(id = R.drawable.ic_article_image_placeholder),
                        error = painterResource(id = R.drawable.ic_article_image_placeholder),
                        onLoading = { isLoading = true },
                        onSuccess = { isLoading = false },
                        onError = { isLoading = false }
                    )

                    // Show loading indicator while image is loading
                    if (isLoading) {
                        Box(
                            modifier = Modifier
                                .fillMaxSize()
                                .background(Color.Gray.copy(alpha = 0.2f))
                        ) {
                            CircularProgressIndicator(
                                modifier = Modifier.align(Alignment.Center)
                            )
                        }
                    }
                }
            } else {
                // Fall back to placeholder if imageUrl is null
                Image(
                    painter = painterResource(id = R.drawable.ic_article_image_placeholder),
                    contentDescription = "Article Image Placeholder",
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(200.dp)
                        .padding(horizontal = 32.dp)
                )
            }

            Spacer(modifier = Modifier.height(28.dp))
            ArticleContent(
                content = topic?.content ?: "",
                title = topic?.title ?: "",
            )
            Spacer(modifier = Modifier.height(32.dp))
            Box(
                modifier = Modifier.fillMaxWidth()
            ) {
                SubtitleWithLogo(
                    titleRes = R.string.suggested_topics,
                    highlightedText = if (currentLanguage == "ar") "مواضيع" else "Topics",
                    highlightedTextColor = DentelLightPurple
                )
            }
            Spacer(Modifier.height(16.dp))
            SuggestedTopicsList(
                listOf(
                    SuggestedTopic(
                        " حشوات الأسنان \n" +
                                "كل ما تريد معرفته عنها"
                    ),
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

@Composable
fun ArticleContent(
    content: String,
    modifier: Modifier = Modifier,
    title: String = ""
) {
    Column(
        modifier = modifier
            .fillMaxWidth(),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .background(Color.White),
        ) {
            CompositionLocalProvider(LocalLayoutDirection provides LayoutDirection.Ltr) {
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(60.dp),
                ) {
                    Surface(
                        modifier = Modifier.size(60.dp),
                        color = DentelDarkPurple,
                        shape = RoundedCornerShape(bottomEnd = 30.dp)
                    ) {}
                    Surface(
                        modifier = Modifier.size(30.dp),
                        color = DentelDarkPurple,
                    ) {
                        Surface(
                            modifier = Modifier.fillMaxSize(),
                            color = Color.White,
                            shape = RoundedCornerShape(topStart = 30.dp)
                        ) {}
                    }
                    Spacer(modifier = Modifier.weight(1f))
                    Surface(
                        modifier = Modifier.size(30.dp),
                        color = DentelDarkPurple,
                    ) {
                        Surface(
                            modifier = Modifier.fillMaxSize(),
                            color = Color.White,
                            shape = RoundedCornerShape(topEnd = 30.dp)
                        ) {}
                    }
                    Surface(
                        modifier = Modifier.size(60.dp),
                        color = DentelDarkPurple,
                        shape = RoundedCornerShape(bottomStart = 30.dp)
                    ) {}
                }
            }
            LikeAndShareButtons(
                modifier = Modifier.align(Alignment.Center),
                title = title,
                isArticle = true,
            )
        }
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .background(
                    color = Color.White,
                    shape = RoundedCornerShape(bottomEnd = 30.dp, bottomStart = 30.dp)
                )
                .padding(horizontal = 32.dp, vertical = 16.dp)
        ) {
            // Display the article content instead of subtitle
            Text(
                text = content,
                style = TextStyle(
                    fontSize = 16.sp,
                    lineHeight = 25.sp,
                    fontFamily = FontFamily(Font(R.font.din_next_lt_regular)),
                    fontWeight = FontWeight(500),
                    color = Color(0xFF421882),
                    textAlign = TextAlign.Right,
                    letterSpacing = 0.38.sp,
                )
            )
        }
    }
}

@Composable
fun ArticleVideoTitle(title: String, modifier: Modifier = Modifier) {
    Text(
        text = title,
        style = TextStyle(
            fontSize = 24.sp,
            fontWeight = FontWeight.Bold,
            color = Color.White
        ),
        modifier = modifier.padding(16.dp)
    )
}

@Preview
@Composable
private fun ArticleScreenPreviewEn() {
    DentelTheme {
        ArticleScreen(null)
    }
}

@Preview(locale = "ar")
@Composable
private fun ArticleScreenPreviewAr() {
    DentelTheme {
        ArticleScreen(null)
    }
}