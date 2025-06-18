package com.m7md7sn.dentel.presentation.ui.notifications

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
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.Dialog

@Composable
fun NotificationsDialog(
    visible: Boolean,
    onDismiss: () -> Unit,
    notifications: List<String> // or your Notification model
) {
    if (visible) {
        Dialog(onDismissRequest = onDismiss) {
            // Your custom dialog UI here, matching your design
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
                        shape = RoundedCornerShape(
                            topStart = 23.dp,
                            topEnd = 10.dp,
                            bottomStart = 23.dp,
                            bottomEnd = 23.dp
                        )
                    )
            ) {
                if (notifications.isEmpty()) {
                    Row(
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.Center,
                        modifier = Modifier.fillMaxWidth()
                    ) {
                        Icon(
                            Icons.Default.Notifications,
                            contentDescription = null,
                            tint = Color(0xFF6C63FF)
                        )
                        Spacer(Modifier.width(8.dp))
                        Text("لا يوجد لديك اشعارات", color = Color(0xFF6C63FF))
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
        notifications = listOf("Notification 1", "Notification 2")
    )
}