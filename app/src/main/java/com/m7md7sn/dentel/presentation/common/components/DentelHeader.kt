package com.m7md7sn.dentel.presentation.common.components

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalLayoutDirection
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.Font
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.LayoutDirection
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.m7md7sn.dentel.R

@Preview
@Composable
fun FullDentelHeader(
    modifier: Modifier = Modifier
) {
    CompositionLocalProvider(LocalLayoutDirection provides LayoutDirection.Ltr) {
        Row(
            horizontalArrangement = Arrangement.Center,
            verticalAlignment = Alignment.CenterVertically,
            modifier = modifier
        ) {
            Image(
                painter = painterResource(id = R.drawable.dentel_logo),
                contentDescription = "Dentel Logo",
                modifier = Modifier.size(width = 86.dp, height = 96.dp)
            )
            Spacer(modifier = Modifier.width(16.dp))
            Column(
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.Center
            ) {
                Text(
                    text = stringResource(R.string.dentel_title),
                    style = TextStyle(
                        fontSize = 64.sp,
                        lineHeight = 76.sp,
                        fontFamily = FontFamily(Font(R.font.myriad_arabic_bold)),
                        fontWeight = FontWeight(700),
                        color = Color(0xFFFFFFFF),
                    )
                )
                Text(
                    text = stringResource(R.string.slogan),
                    style = TextStyle(
                        fontSize = 17.5.sp,
                        fontFamily = FontFamily(Font(R.font.myriad_arabic_bold)),
                        fontWeight = FontWeight(700),
                        color = Color(0xFFFFFFFF),
                        letterSpacing = 0.44.sp,
                    )
                )
            }
        }
    }
}

@Preview
@Composable
fun MinimizedDentelHeader(
    modifier: Modifier = Modifier
) {
    CompositionLocalProvider(LocalLayoutDirection provides LayoutDirection.Ltr) {
        Row(
            horizontalArrangement = Arrangement.Center,
            verticalAlignment = Alignment.CenterVertically,
            modifier = modifier
        ) {
            Image(
                painter = painterResource(id = R.drawable.dentel_logo),
                contentDescription = "Dentel Logo",
                modifier = Modifier.size(width = 54.dp, height = 60.dp)
            )
            Spacer(modifier = Modifier.width(12.dp))
            Column(
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.Center
            ) {
                Text(
                    text = stringResource(R.string.dentel_title),
                    style = TextStyle(
                        fontSize = 46.sp,
                        lineHeight = 55.sp,
                        fontFamily = FontFamily(Font(R.font.myriad_arabic_bold)),
                        fontWeight = FontWeight(700),
                        color = Color(0xFFFFFFFF),
                    )
                )
            }
        }
    }
}