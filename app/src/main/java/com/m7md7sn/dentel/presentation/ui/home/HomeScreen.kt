package com.m7md7sn.dentel.presentation.ui.home

import androidx.annotation.DrawableRes
import androidx.annotation.StringRes
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.gestures.Orientation
import androidx.compose.foundation.gestures.scrollable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.BoxScope
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
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyHorizontalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.foundation.lazy.grid.rememberLazyGridState
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.pager.HorizontalPager
import androidx.compose.foundation.pager.rememberPagerState
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.CardElevation
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.derivedStateOf
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Color.Companion.White
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.platform.LocalLayoutDirection
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.SpanStyle
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.buildAnnotatedString
import androidx.compose.ui.text.font.Font
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.withStyle
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.LayoutDirection
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import com.google.accompanist.pager.HorizontalPagerIndicator
import com.m7md7sn.dentel.R
import com.m7md7sn.dentel.data.model.Section
import com.m7md7sn.dentel.data.model.SuggestedTopic
import com.m7md7sn.dentel.presentation.theme.DentelBrightBlue
import com.m7md7sn.dentel.presentation.theme.DentelDarkPurple
import com.m7md7sn.dentel.presentation.theme.DentelLightPurple
import com.m7md7sn.dentel.presentation.theme.DentelTheme
import kotlin.math.ceil
import java.util.Locale

@Composable
fun HomeScreen(modifier: Modifier = Modifier, viewModel: HomeViewModel = hiltViewModel()) {
    val scrollState = rememberScrollState()
    val uiState by viewModel.uiState.collectAsState()
    val currentLanguage = LocalConfiguration.current.locale.language

    Surface(
        modifier = modifier.fillMaxSize(),
        color = DentelDarkPurple
    ) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .verticalScroll(
                    state = scrollState
                ),
            horizontalAlignment = Alignment.CenterHorizontally,
        ) {
            HomeHeader(
                modifier = Modifier.fillMaxWidth()
            )
            SectionTitleWithLogo(
                titleRes = R.string.choose_part,
                highlightedText = if (currentLanguage == "ar") "القسم" else "the section"
            )
            Spacer(Modifier.height(16.dp))
            SectionsGrid(
                uiState.sections
            )
            Spacer(Modifier.height(24.dp))
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
            Spacer(Modifier.height(24.dp))
            ReminderCard() //Temp
            Spacer(Modifier.height(64.dp))
        }
    }
}

@Composable
fun ReminderCard(
    modifier: Modifier = Modifier
) {
    Card(
        modifier = modifier
            .border(
                width = 1.dp,
                color = Color(0xFF707070),
                shape = RoundedCornerShape(size = 27.dp)
            )
            .width(307.dp)
            .height(105.dp),
        colors = CardDefaults.cardColors(
            containerColor = Color(0xFFFFFFFF),
        ),
        shape = RoundedCornerShape(28.dp),
        elevation = CardDefaults.cardElevation(
            defaultElevation = 2.5.dp
        ),
    ) {
        Row(
            modifier = Modifier
                .fillMaxSize()
                .padding(horizontal = 24.dp, vertical = 12.dp),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            Column(
                modifier = Modifier
                    .width(160.dp)
            ) {
                Text(
                    text = "تنظيف الأسنان بانتظام وبطريقة صحيحة",
                    style = TextStyle(
                        fontSize = 20.sp,
                        lineHeight = 23.sp,
                        fontFamily = FontFamily(Font(R.font.din_next_lt_bold)),
                        fontWeight = FontWeight(700),
                        color = Color(0xFF421882),
                        textAlign = TextAlign.Right,
                    )
                )
                Text(
                    text = "يضمن لك أسنان سليمة ويحفظها من كافة الأمراض والمشاكل ",
                    style = TextStyle(
                        fontSize = 12.sp,
                        fontFamily = FontFamily(Font(R.font.din_next_lt_regular)),
                        fontWeight = FontWeight(500),
                        color = Color(0xFF421882),
                        textAlign = TextAlign.Right,
                    )
                )

            }
            Image(
                painter = painterResource(id = R.drawable.ic_tooth_cleaning),
                contentDescription = null,
            )
        }
    }
}

@Composable
fun SuggestedTopicsList(
    topics: List<SuggestedTopic>,
    modifier: Modifier = Modifier
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
                onCardClicked = { /* TODO: Handle card click */ },
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

@Composable
fun SuggestedTopicItem(
    title: String,
    onCardClicked: () -> Unit,
    modifier: Modifier = Modifier
) {
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


@Composable
fun SectionsGrid(
    sections: List<Section>,
    modifier: Modifier = Modifier
) {
    val gridState = rememberLazyGridState()
    val pageCount = ceil((sections.size + 1) / 4.0).toInt() // Each page has 4 items
    val currentPage by remember {
        derivedStateOf {
            val firstVisibleItem = gridState.firstVisibleItemIndex
            firstVisibleItem / 2 // Assuming 2 items per row
        }
    }

    Column(
        modifier = modifier
            .fillMaxWidth()
            .padding(horizontal = 34.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
    ) {
        LazyHorizontalGrid(
            rows = GridCells.Fixed(2),
            state = gridState,
            modifier = Modifier
                .height(330.dp)
                .width(312.dp),

            ) {
            items(sections) {
                SectionGridItem(
                    imageRes = it.imageRes,
                    titleRes = it.titleRes,
                    color = it.color,
                    onCardClicked = { /* TODO: Handle card click */ }
                )
            }
        }
        PageIndicator(
            pageCount = pageCount,
            currentPage = currentPage,
            modifier = Modifier
                .padding(bottom = 16.dp)
        )
    }
}

@Composable
fun PageIndicator(
    pageCount: Int,
    currentPage: Int,
    modifier: Modifier = Modifier
) {
    Row(
        modifier = modifier,
        horizontalArrangement = Arrangement.Center
    ) {
        repeat(pageCount) {
            Box(
                modifier = Modifier
                    .padding(horizontal = 4.dp)
                    .size(10.dp)
                    .clip(CircleShape)
                    .background(if (it == currentPage) DentelBrightBlue else Color.White)
            )
        }
    }
}

@Composable
fun SectionGridItem(
    @DrawableRes imageRes: Int,
    @StringRes titleRes: Int,
    color: Color,
    onCardClicked: () -> Unit,
    modifier: Modifier = Modifier
) {
    Card(
        modifier = modifier
            .padding(end = 24.dp, bottom = 24.dp)
            .size(142.dp),
        colors = CardDefaults.cardColors(
            containerColor = color
        ),
        shape = RoundedCornerShape(24.dp),
        onClick = onCardClicked
    ) {
        Box(
            modifier = Modifier
                .fillMaxSize()
        ) {
            Image(
                painter = painterResource(id = R.drawable.section_card_bg),
                contentDescription = null,
                modifier = Modifier
                    .alpha(0.1f)
                    .align(Alignment.BottomCenter)
            )
            Box(
                modifier = Modifier
                    .padding(16.dp)
                    .fillMaxSize()
            ) {
                Image(
                    painter = painterResource(imageRes),
                    contentDescription = null,
                    modifier = Modifier
                        .size(55.dp)
                        .align(Alignment.TopEnd)
                )
                Column(
                    modifier = Modifier.align(Alignment.BottomStart),
                    horizontalAlignment = Alignment.Start
                ) {
                    val fullTitle = stringResource(titleRes)
                    val lines = fullTitle.split("\n")

                    if (lines.isNotEmpty()) {
                        Text(
                            text = lines[0],
                            style = TextStyle(
                                fontSize = 21.sp,
                                lineHeight = 28.sp,
                                fontFamily = FontFamily(Font(R.font.din_next_lt_bold)),
                                fontWeight = FontWeight(700),
                                color = DentelDarkPurple,
                                letterSpacing = 0.63.sp,
                            )
                        )
                    }

                    if (lines.size > 1) {
                        Text(
                            text = lines[1],
                            style = TextStyle(
                                fontSize = 21.sp,
                                lineHeight = 28.sp,
                                fontFamily = FontFamily(Font(R.font.din_next_lt_regular)),
                                fontWeight = FontWeight(400),
                                color = Color.White,
                                letterSpacing = 0.63.sp,
                            )
                        )
                    }
                }
            }
        }
    }
}

@Composable
fun SectionTitleWithLogo(
    modifier: Modifier = Modifier,
    @StringRes titleRes: Int,
    highlightedText: String
) {
    Box(
        modifier = modifier.fillMaxWidth()
    ) {
        CompositionLocalProvider(LocalLayoutDirection provides LayoutDirection.Ltr) {
            Box(
                modifier = Modifier
                    .fillMaxWidth()
            ) {
                Image(
                    painter = painterResource(R.drawable.home_lower_path),
                    contentDescription = null,
                    modifier = Modifier
                        .align(Alignment.TopEnd)
                        .padding(bottom = 8.dp)
                )
            }
        }
        SubtitleWithLogo(titleRes, highlightedText, DentelBrightBlue)
    }
}

@Composable
private fun BoxScope.SubtitleWithLogo(
    titleRes: Int,
    highlightedText: String,
    highlightedTextColor: Color
) {
    Row(
        modifier = Modifier
            .padding(start = 34.dp)
            .align(Alignment.BottomStart),
        verticalAlignment = Alignment.CenterVertically,
    ) {
        Image(
            painter = painterResource(R.drawable.ic_mini_logo),
            contentDescription = null,
        )
        Spacer(Modifier.width(22.dp))

        val fullString = stringResource(titleRes)

        val startIndex = fullString.indexOf(highlightedText)
        val endIndex = startIndex + highlightedText.length

        Text(
            text = buildAnnotatedString {
                if (startIndex != -1) {
                    withStyle(style = SpanStyle(color = White)) {
                        append(fullString.substring(0, startIndex))
                    }
                    withStyle(style = SpanStyle(color = highlightedTextColor)) {
                        append(fullString.substring(startIndex, endIndex))
                    }
                    withStyle(style = SpanStyle(color = White)) {
                        append(fullString.substring(endIndex))
                    }
                } else {
                    append(fullString)
                }
            },
            style = TextStyle(
                fontSize = 15.sp,
                lineHeight = 20.sp,
                fontFamily = FontFamily(Font(R.font.din_next_lt_bold)),
                fontWeight = FontWeight(500),
                color = White,
                textAlign = TextAlign.Center,
                letterSpacing = 0.38.sp,
            )
        )
    }
}

@Composable
fun HomeHeader(modifier: Modifier = Modifier) {
    CompositionLocalProvider(LocalLayoutDirection provides LayoutDirection.Ltr) {
        Box(
            modifier = modifier
                .background(White)
                .height(300.dp),
        ) {
            Box(
                modifier = Modifier.align(Alignment.TopEnd)
            ) {
                Image(
                    painter = painterResource(R.drawable.header_upper_path),
                    contentDescription = null
                )
                Image(
                    painter = painterResource(R.drawable.dentel_logo),
                    contentDescription = null,
                    modifier = Modifier
                        .padding(top = 20.dp, end = 38.dp)
                        .width(40.dp)
                        .height(45.dp)
                        .align(Alignment.TopEnd)
                )
            }
            Image(
                painter = painterResource(R.drawable.doctor_illustration),
                contentDescription = null,
                modifier = Modifier
                    .align(Alignment.BottomEnd)
                    .padding(end = 22.dp)
            )
            Column(
                modifier = Modifier
                    .padding(start = 28.dp, top = 12.dp)
                    .align(Alignment.TopStart),
                horizontalAlignment = Alignment.Start,
            ) {
                Box {
                    Text(
                        text = stringResource(R.string.dentel_title),
                        style = TextStyle(
                            fontSize = 50.sp,
                            lineHeight = 68.sp,
                            fontFamily = FontFamily(Font(R.font.myriad_arabic_bold)),
                            fontWeight = FontWeight(700),
                            color = Color(0xFF421882),
                            letterSpacing = 1.25.sp,
                        ),
                        modifier = Modifier
                            .padding(bottom = 8.dp)
                    )
                    Text(
                        text = stringResource(R.string.slogan),
                        style = TextStyle(
                            fontSize = 15.sp,
                            lineHeight = 18.sp,
                            fontFamily = FontFamily(Font(R.font.myriad_arabic_bold)),
                            fontWeight = FontWeight(700),
                            color = Color(0xFF0F1A36),
                            letterSpacing = 0.09.sp,
                        ),
                        modifier = Modifier
                            .align(Alignment.BottomCenter)
                    )
                }
                Spacer(Modifier.height(12.dp))
                HorizontalDivider(
                    modifier = Modifier
                        .padding(1.dp)
                        .width(19.4404.dp)
                        .height(5.24545.dp)
                        .clip(RoundedCornerShape(2.dp))
                        .background(color = Color(0xFF05B3EF))
                )
                Spacer(Modifier.height(8.dp))
                Text(
                    text = buildAnnotatedString {
                        val currentLanguage = LocalConfiguration.current.locale.language
                        val fullDescription = stringResource(R.string.home_header_description)

                        if (currentLanguage == "ar") {
                            val highlightedText = "الفيديوهات\nالتوضيــحــيـــــــــــة"
                            val startIndex = fullDescription.indexOf("الفيديوهات")

                            if (startIndex != -1) {
                                withStyle(style = SpanStyle(fontFamily = FontFamily(Font(R.font.din_next_lt_regular)))) {
                                    append(fullDescription.substring(0, startIndex))
                                }
                                withStyle(style = SpanStyle(fontFamily = FontFamily(Font(R.font.din_next_lt_bold)))) {
                                    append(highlightedText)
                                }
                                withStyle(style = SpanStyle(fontFamily = FontFamily(Font(R.font.din_next_lt_regular)))) {
                                    append(fullDescription.substring(startIndex + highlightedText.length))
                                }
                            } else {
                                append(fullDescription)
                            }
                        } else {
                            append(fullDescription)
                        }
                    },
                    style = TextStyle(
                        fontSize = 15.sp,
                        lineHeight = 20.sp,
                        fontFamily = FontFamily(Font(R.font.din_next_lt_regular)),
                        fontWeight = FontWeight(400),
                        color = Color(0xFF421882),
                    )
                )
            }
        }
    }
}

@Preview
@Composable
private fun HomeScreenPreviewEn() {
    DentelTheme {
        HomeScreen()
    }
}

@Preview(locale = "ar")
@Composable
private fun HomeScreenPreviewAr() {
    DentelTheme {
        HomeScreen()
    }
}