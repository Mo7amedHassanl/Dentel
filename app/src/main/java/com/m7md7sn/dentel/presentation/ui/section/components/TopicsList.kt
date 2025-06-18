package com.m7md7sn.dentel.presentation.ui.section.components

import androidx.compose.animation.AnimatedContent
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.slideInHorizontally
import androidx.compose.animation.slideOutHorizontally
import androidx.compose.animation.togetherWith
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.Font
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.m7md7sn.dentel.R
import com.m7md7sn.dentel.presentation.ui.section.Topic
import com.m7md7sn.dentel.presentation.ui.section.TopicType
import androidx.compose.animation.core.tween

/**
 * Component for displaying the topics list with animations between content types
 */
@Composable
fun TopicsOrEmptyMessage(
    topics: List<Topic>,
    onTopicClick: (Topic) -> Unit,
    isLoading: Boolean = false,
    modifier: Modifier = Modifier
) {
    Column(
        modifier = modifier
            .fillMaxWidth()
            .padding(horizontal = 8.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
    ) {
        Image(
            painter = painterResource(id = R.drawable.section_topic_list_upper_curve),
            contentDescription = null,
            contentScale = ContentScale.FillHeight,
        )
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .clip(RoundedCornerShape(32.dp))
                .background(Color.White)
                .padding(vertical = 16.dp, horizontal = 22.dp),
            contentAlignment = Alignment.TopCenter
        ) {
            AnimatedContent(
                targetState = Triple(isLoading, topics.isEmpty(), topics),
                transitionSpec = {
                    // Get the current and target content types
                    val initialType = initialState.third.firstOrNull()?.type
                    val targetType = targetState.third.firstOrNull()?.type

                    // Determine slide direction based on transition between content types
                    // For Articles -> Videos: slide to the left (negative)
                    // For Videos -> Articles: slide to the right (positive)
                    val slideDirection = if (initialType == targetType) {
                        0 // No direction change if same type
                    } else if (targetType == TopicType.Video) {
                        -1 // Sliding left for Articles -> Videos
                    } else {
                        1 // Sliding right for Videos -> Articles
                    }

                    // Create slide and fade animations with the determined direction
                    (slideInHorizontally(animationSpec = tween(400)) { width -> slideDirection * width } +
                            fadeIn(animationSpec = tween(400)))
                        .togetherWith(
                            slideOutHorizontally(animationSpec = tween(400)) { width -> -slideDirection * width } +
                                    fadeOut(animationSpec = tween(300))
                        )
                },
                label = "topics_content_animation"
            ) { (loading, isEmpty, topicsList) ->
                when {
                    loading -> {
                        CircularProgressIndicator(color = Color(0xFF421882))
                    }

                    isEmpty -> {
                        Text(
                            text = "No topics found for this section.",
                            color = Color(0xFF421882),
                            fontFamily = FontFamily(Font(R.font.din_next_lt_regular)),
                            fontWeight = FontWeight(400),
                            fontSize = 16.sp,
                            textAlign = TextAlign.Center,
                            modifier = Modifier.padding(16.dp)
                        )
                    }

                    else -> {
                        LazyColumn() {
                            items(topicsList.size) { idx ->
                                val topic = topicsList[idx]
                                TopicItem(
                                    title = topic.title,
                                    subtitle = topic.subtitle,
                                    type = when (topic.type) {
                                        TopicType.Article -> "article"
                                        TopicType.Video -> "video"
                                    },
                                    onCardClicked = { onTopicClick(topic) }
                                )
                            }
                        }
                    }
                }
            }
        }
    }
}
