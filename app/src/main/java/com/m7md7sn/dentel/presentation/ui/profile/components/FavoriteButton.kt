package com.m7md7sn.dentel.presentation.ui.profile.components

import androidx.annotation.DrawableRes
import androidx.annotation.StringRes
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
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
import androidx.compose.ui.draw.shadow
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
import com.m7md7sn.dentel.presentation.theme.DentelBrightBlue
import com.m7md7sn.dentel.presentation.theme.DentelDarkPurple

/**
 * Button for favorite section selection
 */
@Composable
fun FavoriteButton(
    onClick: () -> Unit,
    clicked: Boolean,
    @DrawableRes iconRes: Int,
    @DrawableRes clickedIconRes: Int,
    @StringRes text: Int,
    highlightedTextColor: Color,
    color: Color,
    modifier: Modifier = Modifier
) {
    val fullTitle = stringResource(text)
    val lines = fullTitle.split("\n")

    Button(
        onClick = onClick,
        modifier = modifier
            .width(140.dp)
            .height(120.dp)
            .shadow(
                elevation = 4.dp,
                shape = RoundedCornerShape(size = 18.dp)
            ),
        shape = RoundedCornerShape(size = 18.dp),
        colors = ButtonDefaults.buttonColors(
            containerColor = if (clicked) color else Color(0xFFFFFFFF),
            contentColor = DentelBrightBlue
        ),
        contentPadding = PaddingValues(horizontal = 24.dp, vertical = 10.dp)
    ) {
        Column(
            modifier = Modifier
                .fillMaxSize(),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Image(
                painter = painterResource(id = if (clicked) clickedIconRes else iconRes),
                contentDescription = null,
                modifier = Modifier.size(42.dp)
            )
            Spacer(modifier = Modifier.width(16.dp))

            if (lines.isNotEmpty()) {
                Text(
                    text = lines[0],
                    style = TextStyle(
                        fontSize = 15.sp,
                        lineHeight = 20.sp,
                        fontFamily = FontFamily(Font(R.font.din_next_lt_bold)),
                        fontWeight = FontWeight(700),
                        color = if (clicked) Color.White else highlightedTextColor,
                        textAlign = TextAlign.Center,
                    )
                )
            }

            if (lines.size > 1) {
                Text(
                    text = lines[1],
                    style = TextStyle(
                        fontSize = 15.sp,
                        lineHeight = 20.sp,
                        fontFamily = FontFamily(Font(R.font.din_next_lt_bold)),
                        fontWeight = FontWeight(700),
                        color = DentelDarkPurple,
                        textAlign = TextAlign.Center,
                    )
                )
            }
        }
    }
}
