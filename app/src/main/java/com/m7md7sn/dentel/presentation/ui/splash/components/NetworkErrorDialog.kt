package com.m7md7sn.dentel.presentation.ui.splash.components

import android.app.Activity
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable

/**
 * Network error dialog component for splash screen
 */
@Composable
fun NetworkErrorDialog(
    onReconnect: () -> Unit,
    onExit: () -> Unit
) {
    AlertDialog(
        onDismissRequest = { onExit() }, // Exit the application when dismissed outside
        title = { Text("No Internet Connection") },
        text = { Text("Please check your internet connection and try again.") },
        confirmButton = {
            TextButton(onClick = onReconnect) {
                Text("Reconnect")
            }
        },
        dismissButton = {
            TextButton(onClick = onExit) {
                Text("Exit")
            }
        }
    )
}
