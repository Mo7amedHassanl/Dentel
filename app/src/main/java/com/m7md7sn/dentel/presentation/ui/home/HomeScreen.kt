package com.m7md7sn.dentel.presentation.ui.home

import androidx.annotation.DrawableRes
import androidx.annotation.StringRes
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.gestures.Orientation
import androidx.compose.foundation.gestures.scrollable
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
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyHorizontalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Color.Companion.White
import androidx.compose.ui.layout.ContentScale
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
import androidx.hilt.navigation.compose.hiltViewModel
import com.m7md7sn.dentel.R
import com.m7md7sn.dentel.data.model.Section
import com.m7md7sn.dentel.presentation.theme.DentelDarkPurple
import com.m7md7sn.dentel.presentation.theme.DentelTheme

@Composable
fun HomeScreen(modifier: Modifier = Modifier, viewModel: HomeViewModel = hiltViewModel()) {
    val scrollState = rememberScrollState()
    val uiState by viewModel.uiState.collectAsState()
    Surface(
        modifier = modifier.fillMaxSize(),
        color = DentelDarkPurple
    ) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .scrollable(
                    orientation = Orientation.Vertical,
                    enabled = true,
                    state = scrollState
                ),
            horizontalAlignment = Alignment.CenterHorizontally,
        ) {
            HomeHeader(
                modifier = Modifier.fillMaxWidth()
            )
            SectionTitleWithLogo()
            SectionsGrid(
                uiState.sections
            )
        }
    }
}

@Composable
fun SectionsGrid(
    sections: List<Section>,
    modifier: Modifier = Modifier
) {
    Box(
        modifier = modifier
            .fillMaxWidth()
            .height(350.dp)
            .padding(horizontal = 34.dp)
    ) {
        LazyHorizontalGrid(
            rows = GridCells.Fixed(2),
            contentPadding = PaddingValues(12.dp)
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
                        .align(Alignment.TopEnd )
                )
                Text(
                    text = stringResource(titleRes),
                    style = TextStyle(
                        fontSize = 20.sp,
                        lineHeight = 28.sp,
                        fontFamily = FontFamily(Font(R.font.din_next_lt_bold)),
                        fontWeight = FontWeight(700),
                        color = Color(0xFF421882),
                        letterSpacing = 0.63.sp,
                    ),
                    modifier = Modifier.align(Alignment.BottomStart)
                )
            }
        }
    }
}

@Composable
fun SectionTitleWithLogo(
    modifier: Modifier = Modifier
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
            Text(
                text = stringResource(R.string.choose_part),
                style = TextStyle(
                    fontSize = 15.sp,
                    lineHeight = 20.sp,
                    fontFamily = FontFamily(Font(R.font.din_next_lt_bold)),
                    fontWeight = FontWeight(500),
                    color = Color(0xFFFFFFFF),
                    textAlign = TextAlign.Center,
                    letterSpacing = 0.38.sp,
                )
            )
        }
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
                    text = stringResource(R.string.home_header_description),
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