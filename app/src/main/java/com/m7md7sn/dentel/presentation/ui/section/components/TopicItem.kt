package com.m7md7sn.dentel.presentation.ui.section.components

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.HorizontalDivider
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
import com.m7md7sn.dentel.presentation.ui.section.TopicType

/**
 * Component for displaying a topic card (article or video)
 */
@Composable
fun TopicItem(
    title: String,
    subtitle: String,
    onCardClicked: () -> Unit,
    type: String,
    modifier: Modifier = Modifier
) {
    Card(
        modifier = modifier
            .width(332.dp)
            .padding(bottom = 16.dp),
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
