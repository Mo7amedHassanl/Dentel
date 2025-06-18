package com.m7md7sn.dentel.presentation.ui.video.components

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
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalLayoutDirection
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.Font
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.LayoutDirection
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.m7md7sn.dentel.R
import com.m7md7sn.dentel.presentation.theme.DentelDarkPurple

@Composable
fun VideoDescriptionWithLikeAndShareButtons(
    description: String,
    modifier: Modifier = Modifier,
    title: String = "",
    videoUrl: String = ""
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
                url = videoUrl,
                isArticle = false
            )
        }
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .background(color = Color.White, shape = RoundedCornerShape(bottomEnd = 30.dp, bottomStart = 30.dp))
                .padding(horizontal = 32.dp, vertical = 16.dp)
        ) {
            Text(
                text = description,
                style = TextStyle(
                    fontSize = 16.sp,
                    lineHeight = 25.sp,
                    fontFamily = FontFamily(Font(R.font.din_next_lt_regular)),
                    fontWeight = FontWeight(500),
                    color = Color(0xFF421882),
                    textAlign = TextAlign.Start,
                    letterSpacing = 0.38.sp,
                )
            )
        }
    }
}
