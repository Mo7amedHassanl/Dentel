package com.m7md7sn.dentel.presentation.ui.home.components

import androidx.annotation.DrawableRes
import androidx.annotation.StringRes
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyHorizontalGrid
import androidx.compose.foundation.lazy.grid.rememberLazyGridState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.derivedStateOf
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.Font
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.m7md7sn.dentel.R
import com.m7md7sn.dentel.data.model.Section
import com.m7md7sn.dentel.presentation.theme.DentelBrightBlue
import com.m7md7sn.dentel.presentation.theme.DentelDarkPurple
import kotlin.math.ceil

/**
 * Grid to display all available sections
 */
@Composable
fun SectionsGrid(
    sections: List<Section>,
    onSectionClick: (Section) -> Unit,
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
            items(sections.size) { idx ->
                val section = sections[idx]
                SectionGridItem(
                    imageRes = section.imageRes,
                    titleRes = section.titleRes,
                    color = section.color,
                    onCardClicked = { onSectionClick(section) }
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

/**
 * Individual section card item
 */
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

/**
 * Page indicator dots for the sections grid
 */
@Composable
fun PageIndicator(
    pageCount: Int,
    currentPage: Int,
    modifier: Modifier = Modifier
) {
    Row(
        modifier = modifier,
        verticalAlignment = Alignment.CenterVertically
    ) {
        repeat(pageCount) { index ->
            Box(
                modifier = Modifier
                    .padding(horizontal = 4.dp)
                    .size(10.dp)
                    .clip(CircleShape)
                    .background(if (index == currentPage) DentelBrightBlue else Color.White)
            )
        }
    }
}
