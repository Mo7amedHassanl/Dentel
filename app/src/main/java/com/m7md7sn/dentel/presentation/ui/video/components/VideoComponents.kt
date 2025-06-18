package com.m7md7sn.dentel.presentation.ui.video.components

import androidx.annotation.StringRes
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.Favorite
import androidx.compose.material.icons.outlined.Share
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.Font
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import android.content.Intent
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.material.icons.outlined.FavoriteBorder
import com.m7md7sn.dentel.R
import com.m7md7sn.dentel.presentation.theme.DentelDarkPurple

@Composable
fun ArticleVideoTitle(title: String, modifier: Modifier = Modifier) {
    Text(
        text = title,
        style = TextStyle(
            fontSize = 24.sp,
            fontWeight = FontWeight.Bold,
            fontFamily = FontFamily(Font(R.font.din_next_lt_bold)),
            color = Color.White
        ),
        modifier = modifier.padding(16.dp)
    )
}

@Composable
fun LikeAndShareButtons(
    modifier: Modifier = Modifier,
    title: String = "",
    url: String = "",
    isArticle: Boolean = false
) {
    val context = LocalContext.current

    Row(
        modifier = modifier,
        verticalAlignment = Alignment.CenterVertically
    ) {
        VideoButton(
            text = R.string.like,
            onClick = { /* Handle like action */ },
            icon = Icons.Outlined.FavoriteBorder,
            tint = Color(0xFFE63E3E),
        )
        Spacer(modifier = Modifier.width(16.dp))
        VideoButton(
            text = R.string.share,
            onClick = {
                val shareIntent = Intent(Intent.ACTION_SEND).apply {
                    type = "text/plain"
                    if (isArticle) {
                        // Article share message
                        putExtra(Intent.EXTRA_TEXT,
                            "Check this article: $title\n" +
                            "App link in store: https://play.google.com/store/apps/details?id=${context.packageName}")
                    } else {
                        // Video share message
                        putExtra(Intent.EXTRA_TEXT,
                            "Check this video: $title\n" +
                            "YouTube link: $url\n" +
                            "App link in store: https://play.google.com/store/apps/details?id=${context.packageName}")
                    }
                }
                context.startActivity(Intent.createChooser(shareIntent, "Share via"))
            },
            icon = Icons.Outlined.Share,
            tint = Color.Black,
        )
    }
}

@Composable
fun VideoButton(
    @StringRes text: Int,
    onClick: () -> Unit,
    icon: ImageVector,
    tint: Color,
    modifier: Modifier = Modifier
) {
    OutlinedButton(
        onClick = onClick,
        modifier = modifier
            .height(40.dp)
            .width(94.dp),
        shape = RoundedCornerShape(10.dp),
        colors = ButtonDefaults.outlinedButtonColors(
            containerColor = Color.White,
            contentColor = DentelDarkPurple,
        ),
        border = BorderStroke(
            width = 0.5.dp,
            color = Color(0xFFAE9FC6)
        ),
        contentPadding = PaddingValues(8.dp)
    ) {
        Text(
            text = stringResource(text),
            style = TextStyle(
                fontSize = 16.sp,
                fontFamily = FontFamily(Font(R.font.din_next_lt_regular)),
                fontWeight = FontWeight(500),
                color = Color(0xFF707070),
            )
        )
        Spacer(modifier = Modifier.width(12.dp))
        Icon(
            imageVector = icon,
            contentDescription = null,
            tint = tint,
            modifier = Modifier.size(24.dp)
        )
    }
}
