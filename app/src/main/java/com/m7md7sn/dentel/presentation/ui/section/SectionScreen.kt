package com.m7md7sn.dentel.presentation.ui.section

import androidx.annotation.DrawableRes
import androidx.annotation.StringRes
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.SearchBar
import androidx.compose.material3.SearchBarDefaults
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.VerticalDivider
import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalLayoutDirection
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.Font
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.LayoutDirection
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.m7md7sn.dentel.R
import com.m7md7sn.dentel.data.model.Section
import com.m7md7sn.dentel.presentation.theme.DentelDarkPurple
import com.m7md7sn.dentel.presentation.theme.DentelTheme
import com.m7md7sn.dentel.presentation.theme.DentelBrightBlue
import com.m7md7sn.dentel.presentation.theme.DentelLightPurple
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.viewmodel.compose.viewModel
import com.m7md7sn.dentel.presentation.ui.section.TopicType
import com.m7md7sn.dentel.presentation.ui.section.SectionViewModel
import com.m7md7sn.dentel.presentation.ui.section.Topic
import com.m7md7sn.dentel.presentation.ui.section.SectionUiState

@Composable
fun SectionScreen(
    section: Section,
    modifier: Modifier = Modifier,
    viewModel: SectionViewModel = hiltViewModel(),
    onTopicClick: (Topic) -> Unit = {}
) {
    val uiState by viewModel.uiState.collectAsState()
    val scrollState = rememberScrollState()

    // Set section only once
    LaunchedEffect(section) {
        viewModel.setSection(section)
    }

    Surface(
        modifier = modifier.fillMaxSize(),
        color = DentelDarkPurple
    ) {
        Box(
            modifier = Modifier
                .fillMaxSize()
        ) {
            CompositionLocalProvider(LocalLayoutDirection provides LayoutDirection.Ltr) {
                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                ) {
                    Image(
                        painter = painterResource(id = R.drawable.section_tooth_bg),
                        contentDescription = null,
                        modifier = Modifier
                            .padding(top = 48.dp)
                            .align(Alignment.TopEnd)
                            .alpha(0.3f),
                    )
                }
            }
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .verticalScroll(
                        state = scrollState
                    ),
                horizontalAlignment = Alignment.CenterHorizontally,
            ) {
                uiState.section?.let {
                    SectionHeader(section = it)
                }
                SectionSearchBar(
                    query = uiState.searchQuery,
                    onQueryChange = viewModel::onSearchQueryChange
                )
                Spacer(modifier = Modifier.height(32.dp))
                ContentTypeButtons(
                    selectedType = uiState.selectedType,
                    onTypeSelected = viewModel::selectType
                )
                Spacer(modifier = Modifier.height(10.dp))
                TopicsList(
                    topics = uiState.topics,
                    onTopicClick = onTopicClick
                )
            }
        }
    }
}

@Composable
fun TopicsList(
    topics: List<Topic>,
    onTopicClick: (Topic) -> Unit,
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
            contentDescription = null
        )
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .height(800.dp)
                .clip(RoundedCornerShape(32.dp))
                .background(Color.White)
                .padding(vertical = 16.dp, horizontal = 22.dp)
        ) {
            LazyColumn() {
                items(topics.size) { idx ->
                    val topic = topics[idx]
                    TopicItem(
                        title = topic.title,
                        subtitle = topic.subtitle,
                        type = when (topic.type) {
                            is TopicType.Article -> "article"
                            is TopicType.Video -> "video"
                        },
                        onCardClicked = { onTopicClick(topic) }
                    )
                }
            }
        }
    }
}

@Composable
fun TopicItem(
    title: String = " حشوات الأسنان \n" +
            "كل ما تريد معرفته عنها",
    subtitle: String = "في الفيديو دا من الدنتو اتكلمنا عن حشوات الاسنان ، واشهر انواعها ، والسبب اللي بيخلينا نلجأ ليها ، وعيوب ومزايا كل نوع .",
    onCardClicked: () -> Unit = {},
    type: String = "video",
    modifier: Modifier = Modifier
) {
    Card(
        modifier = modifier
            .width(332.dp).padding(bottom = 16.dp),
        colors = CardDefaults.cardColors(
            containerColor = if (type == "video") Color(0xFFE1F0FF) else Color(0xFFE5E1FF),
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
            contentAlignment = Alignment.Center,

        ) {
            Column(
                horizontalAlignment = Alignment.CenterHorizontally,
                modifier = Modifier
                    .padding(horizontal = 28.dp, vertical = 8.dp)
                    .fillMaxWidth()
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
                HorizontalDivider(
                    modifier = Modifier
                        .width(270.dp)
                        .padding(vertical = 8.dp),
                    color = Color(0xFFB3CDD7),
                    thickness = 0.5.dp
                )
                Text(
                    text = subtitle,
                    style = TextStyle(
                        fontSize = 10.sp,
                        lineHeight = 16.sp,
                        fontFamily = FontFamily(Font(R.font.din_next_lt_regular)),
                        fontWeight = FontWeight(400),
                        color = Color(0xFF421882),
                        textAlign = TextAlign.Center,
                    )
                )
            }

        }
    }
}

@Composable
fun ContentTypeButtons(
    selectedType: TopicType,
    onTypeSelected: (TopicType) -> Unit,
    modifier: Modifier = Modifier
) {
    Row(
        modifier = modifier
            .padding(horizontal = 32.dp)
            .fillMaxWidth(),
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically
    ) {
        ContentTypeButton(
            text = R.string.videos,
            iconRes = R.drawable.ic_video,
            clickedIconRes = R.drawable.ic_video_selected,
            clicked = selectedType is TopicType.Video,
            onClick = { onTypeSelected(TopicType.Video) },
            color = DentelBrightBlue
        )
        ContentTypeButton(
            text = R.string.articles,
            iconRes = R.drawable.ic_article,
            clickedIconRes = R.drawable.ic_articles_selected,
            clicked = selectedType is TopicType.Article,
            onClick = { onTypeSelected(TopicType.Article) },
            color = DentelLightPurple
        )
    }
}

@Composable
fun ContentTypeButton(
    @StringRes text: Int,
    @DrawableRes iconRes: Int,
    @DrawableRes clickedIconRes: Int,
    clicked: Boolean,
    color: Color,
    onClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    Button(
        onClick = onClick,
        modifier = modifier
            .shadow(
                elevation = 5.dp,
                spotColor = Color(0x29000000),
                ambientColor = Color(0x29000000)
            )
            .width(156.dp)
            .height(50.dp),
        shape = RoundedCornerShape(size = 18.dp),
        colors = ButtonDefaults.buttonColors(
            containerColor = if (clicked) color else Color(0xFFFFFFFF),
            contentColor = DentelBrightBlue
        ),
        contentPadding = PaddingValues(horizontal = 24.dp, vertical = 10.dp)
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth(),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Image(
                painter = painterResource(id = if (clicked) clickedIconRes else iconRes),
                contentDescription = null,
                modifier = Modifier.size(24.dp)
            )
            Spacer(modifier = Modifier.width(16.dp))
            Text(
                text = stringResource(text),
                style = TextStyle(
                    fontSize = 15.sp,
                    lineHeight = 20.sp,
                    fontFamily = FontFamily(Font(R.font.din_next_lt_bold)),
                    fontWeight = FontWeight(400),
                    color = if (!clicked) Color(0xFF421882) else Color.White,
                    textAlign = TextAlign.Center,
                    letterSpacing = 0.38.sp,
                )
            )
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun SectionSearchBar(
    query: String,
    onQueryChange: (String) -> Unit,
    modifier: Modifier = Modifier
) {
    SearchBar(
        query = query,
        onQueryChange = onQueryChange,
        onSearch = {},
        active = false,
        onActiveChange = {},
        modifier = modifier
            .fillMaxWidth()
            .padding(horizontal = 30.dp)
            .height(64.dp),
        placeholder = {
            Text(
                text = stringResource(R.string.search),
                style = TextStyle(
                    fontSize = 18.sp,
                    lineHeight = 16.sp,
                    fontFamily = FontFamily(Font(R.font.din_next_lt_regular)),
                    fontWeight = FontWeight(400),
                    color = Color(0xFF421882),
                    textAlign = TextAlign.Center,
                )
            )
        },
        leadingIcon = {
            Image(
                painter = painterResource(id = R.drawable.ic_search),
                contentDescription = null,
                modifier = Modifier.size(24.dp)
            )
        },
        shape = RoundedCornerShape(20.dp),
        colors = SearchBarDefaults.colors(
            containerColor = Color.White,
            dividerColor = Color.Transparent
        ),
        tonalElevation = 0.dp,
        shadowElevation = 0.dp
    ) {}
}

@Composable
fun SectionHeader(
    section: Section,
    modifier: Modifier = Modifier
) {
    val fullTitle = stringResource(section.titleRes)
    val lines = fullTitle.split("\n")

    Row(
        modifier = modifier
            .padding(horizontal = 54.dp, vertical = 28.dp)
            .fillMaxWidth(),
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically
    ) {
        VerticalDivider(
            modifier = Modifier
                .padding(1.dp)
                .width(6.dp)
                .height(110.dp)
                .clip(RoundedCornerShape(6.dp)),
            color = section.color,
            thickness = 6.dp
        )

        Column {
            if (lines.isNotEmpty()) {
                Text(
                    text = lines[0],
                    style = TextStyle(
                        fontSize = 34.sp,
                        lineHeight = 40.sp,
                        fontFamily = FontFamily(Font(R.font.din_next_lt_bold)),
                        fontWeight = FontWeight(700),
                        color = Color(0xFFFFFFFF),
                        textAlign = TextAlign.Right,
                        letterSpacing = 0.85.sp,
                    )
                )
            }
            if (lines.size > 1) {
                Text(
                    text = lines[1],
                    style = TextStyle(
                        fontSize = 34.sp,
                        lineHeight = 40.sp,
                        fontFamily = FontFamily(Font(R.font.din_next_lt_regular)),
                        fontWeight = FontWeight(400),
                        color = Color(0xFFFFFFFF),
                        textAlign = TextAlign.Right,
                        letterSpacing = 0.85.sp,
                    )
                )
            }
        }

        Image(
            painter = painterResource(id = section.imageRes),
            contentDescription = "null",
            modifier = Modifier
                .size(76.dp)
        )
    }
}

@Preview
@Composable
private fun SectionScreenPreviewEn() {
    DentelTheme {
        SectionScreen(Section(
            imageRes = R.drawable.ic_mobile_denture,
            titleRes = R.string.mobile_denture,
            color = Color(0xFF6A89E0)
        ))
    }
}

@Preview(locale = "ar")
@Composable
private fun SectionScreenPreviewAr() {
    DentelTheme {
        SectionScreen(Section(
            imageRes = R.drawable.ic_mobile_denture,
            titleRes = R.string.mobile_denture,
            color = Color(0xFF6A89E0)
        ))
    }
}