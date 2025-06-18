package com.m7md7sn.dentel.presentation.ui.auth.login.components

import androidx.annotation.DrawableRes
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.OutlinedButton
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
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.m7md7sn.dentel.R

/**
 * Component that displays social login buttons (Facebook and Google)
 */
@Composable
fun SocialLoginButtons(
    onFacebookLoginClick: () -> Unit = {},
    onGoogleLoginClick: () -> Unit = {},
    modifier: Modifier = Modifier
) {
    Column(
        modifier = modifier,
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
        Text(
            text = stringResource(R.string.login_with),
            style = TextStyle(
                fontSize = 12.sp,
                lineHeight = 20.sp,
                fontFamily = FontFamily(Font(R.font.din_next_lt_regular)),
                fontWeight = FontWeight(400),
                color = Color(0x99200A4D),
            ),
            modifier = Modifier.padding(bottom = 4.dp)
        )
        Row {
            SocialLoginButton(
                icon = R.drawable.ic_google,
                onClick = onGoogleLoginClick
            )
            Spacer(Modifier.width(22.dp))
            SocialLoginButton(
                icon = R.drawable.ic_facebook,
                onClick = onFacebookLoginClick
            )
        }
    }
}

/**
 * Circular button for social login options
 */
@Composable
fun SocialLoginButton(
    @DrawableRes icon: Int,
    onClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    OutlinedButton(
        onClick = onClick,
        modifier = modifier.size(64.dp),
        shape = CircleShape,
        border = BorderStroke(1.dp, Color.Gray),
        contentPadding = PaddingValues(0.dp)
    ) {
        Box(
            Modifier.fillMaxSize(),
            contentAlignment = Alignment.Center
        ) {
            Image(
                painter = painterResource(icon),
                contentDescription = null,
                modifier = Modifier.size(40.dp),
            )
        }
    }
}
