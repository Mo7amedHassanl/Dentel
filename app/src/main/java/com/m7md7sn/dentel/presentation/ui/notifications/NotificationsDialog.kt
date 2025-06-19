package com.m7md7sn.dentel.presentation.ui.notifications

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Notifications
import androidx.compose.material3.Icon
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
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.window.Dialog
import com.m7md7sn.dentel.R

@Composable
fun NotificationsDialog(
    visible: Boolean,
    modifier: Modifier = Modifier,
    onDismiss: () -> Unit,
    notifications: List<String> = emptyList()
) {
    if (visible) {
        Dialog(onDismissRequest = onDismiss) {
            Box(
                modifier = Modifier
                    .shadow(
                        elevation = 5.dp,
                        spotColor = Color(0x910CA0E2),
                        ambientColor = Color(0x910CA0E2)
                    )
                    .width(314.dp)
                    .height(133.dp)
                    .background(
                        color = Color(0xFFFFFFFF),
                        shape = RoundedCornerShape(23.dp)
                    ),
                contentAlignment = Alignment.Center
            ) {
                if (notifications.isEmpty()) {
                    Row(
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.Center,
                        modifier = Modifier.fillMaxWidth()
                    ) {
                        Image(
                            painter = painterResource(R.drawable.ic_notifications),
                            contentDescription = null,
                        )
                        Spacer(Modifier.width(8.dp))
                        Text(
                            text = stringResource(R.string.dont_have_notifications),
                            style = TextStyle(
                                fontSize = 15.sp,
                                lineHeight = 20.sp,
                                fontFamily = FontFamily(Font(R.font.din_next_lt_regular)),
                                fontWeight = FontWeight(400),
                                color = Color(0xFF05B3EF),
                                textAlign = TextAlign.Center,
                                letterSpacing = 2.5.sp,
                            )
                        )
                    }
                } else {
                    // List notifications
                }
            }
        }
    }

}

@Preview
@Composable
fun NotificationsDialogPreview() {
    NotificationsDialog(
        visible = true,
        onDismiss = {},
    )
}