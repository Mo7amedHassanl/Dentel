package com.m7md7sn.dentel.presentation.ui.section.components

import androidx.annotation.DrawableRes
import androidx.annotation.StringRes
import androidx.compose.animation.AnimatedContent
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.togetherWith
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
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
import com.m7md7sn.dentel.presentation.ui.section.TopicType
import androidx.compose.animation.core.tween
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.ui.draw.shadow
import com.m7md7sn.dentel.presentation.theme.DentelBrightBlue
import com.m7md7sn.dentel.presentation.theme.DentelLightPurple

/**
 * Buttons to toggle between different content types (Articles/Videos)
 */
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
            clicked = selectedType == TopicType.Video,
            onClick = { onTypeSelected(TopicType.Video) },
            color = DentelBrightBlue
        )
        ContentTypeButton(
            text = R.string.articles,
            iconRes = R.drawable.ic_article,
            clickedIconRes = R.drawable.ic_articles_selected,
            clicked = selectedType == TopicType.Article,
            onClick = { onTypeSelected(TopicType.Article) },
            color = DentelLightPurple
        )
    }
}

/**
 * Individual content type button
 */
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
            // Animate the icon with crossfade effect
            AnimatedContent(
                targetState = clicked,
                transitionSpec = {
                    fadeIn(animationSpec = tween(300)) togetherWith
                            fadeOut(animationSpec = tween(300))
                },
                label = "button_icon_animation"
            ) { isSelected ->
                Image(
                    painter = painterResource(id = if (isSelected) clickedIconRes else iconRes),
                    contentDescription = null,
                    modifier = Modifier.size(24.dp)
                )
            }

            Spacer(modifier = Modifier.width(16.dp))

            // Animate the text color change
            AnimatedContent(
                targetState = clicked,
                transitionSpec = {
                    fadeIn(animationSpec = tween(300)) togetherWith
                            fadeOut(animationSpec = tween(300))
                },
                label = "button_text_animation"
            ) { isSelected ->
                Text(
                    text = stringResource(text),
                    style = TextStyle(
                        fontSize = 15.sp,
                        lineHeight = 20.sp,
                        fontFamily = FontFamily(Font(R.font.din_next_lt_bold)),
                        fontWeight = FontWeight(400),
                        color = if (!isSelected) Color(0xFF421882) else Color.White,
                        textAlign = TextAlign.Center,
                        letterSpacing = 0.38.sp,
                    )
                )
            }
        }
    }
}
