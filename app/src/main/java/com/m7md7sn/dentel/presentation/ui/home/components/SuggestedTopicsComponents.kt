package com.m7md7sn.dentel.presentation.ui.home.components

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.Font
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.m7md7sn.dentel.R
import com.m7md7sn.dentel.data.model.SuggestedTopic
import androidx.compose.foundation.pager.HorizontalPager
import androidx.compose.foundation.pager.rememberPagerState
import com.m7md7sn.dentel.presentation.theme.DentelLightPurple
import com.google.accompanist.pager.HorizontalPagerIndicator

/**
 * Component that displays a list of suggested topics in a horizontal pager
 */
@Composable
fun SuggestedTopicsList(
    topics: List<SuggestedTopic>,
    modifier: Modifier = Modifier,
    onTopicClick: (SuggestedTopic) -> Unit = {}
) {
    val pagerState = rememberPagerState(initialPage = 0, pageCount = { topics.size })

    Column(
        modifier = modifier.fillMaxWidth(),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        HorizontalPager(
            state = pagerState,
            modifier = Modifier
                .fillMaxWidth()
                .height(83.dp)
                .padding(horizontal = 42.dp),
            pageSpacing = 8.dp
        ) { page ->
            SuggestedTopicItem(
                title = topics[page].title,
                onCardClicked = { onTopicClick(topics[page]) },
                modifier = Modifier.fillMaxWidth()
            )
        }

        Spacer(Modifier.height(16.dp))

        HorizontalPagerIndicator(
            pagerState = pagerState,
            pageCount = topics.size,
            activeColor = DentelLightPurple,
            inactiveColor = Color.White,
            indicatorWidth = 10.dp,
            indicatorHeight = 10.dp,
            spacing = 8.dp
        )
    }
}

/**
 * Individual item in the suggested topics list
 */
@Composable
fun SuggestedTopicItem(
    title: String,
    onCardClicked: () -> Unit,
    modifier: Modifier = Modifier
){
    Card(
        modifier = modifier
            .width(307.dp)
            .height(83.dp),
        colors = CardDefaults.cardColors(
            containerColor = Color(0xFFE5E1FF),
        ),
        shape = RoundedCornerShape(20.dp),
        onClick = onCardClicked,
        elevation = CardDefaults.cardElevation(
            defaultElevation = 2.5.dp
        ),
    ) {
        Box(
            modifier = Modifier
                .fillMaxSize(),
            contentAlignment = Alignment.Center
        ) {
            Text(
                text = title,
                style = TextStyle(
                    fontSize = 25.sp,
                    lineHeight = 30.sp,
                    fontFamily = FontFamily(Font(R.font.din_next_lt_bold)),
                    fontWeight = FontWeight(700),
                    color = Color(0xFF421882),
                    textAlign = TextAlign.Center,
                )
            )
        }
    }
}
