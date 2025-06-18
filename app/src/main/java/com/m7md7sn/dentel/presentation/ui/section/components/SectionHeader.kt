package com.m7md7sn.dentel.presentation.ui.section.components

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.VerticalDivider
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.Font
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.m7md7sn.dentel.R
import com.m7md7sn.dentel.data.model.Section

/**
 * Header component for the Section screen displaying title and image
 */
@Composable
fun SectionHeader(
    section: Section,
    modifier: Modifier = Modifier
) {
    val fullTitle = stringResource(section.titleRes)
    val lines = fullTitle.split("\n")

    Row(
        modifier = modifier
            .padding(horizontal = 54.dp, vertical = 8.dp)
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
