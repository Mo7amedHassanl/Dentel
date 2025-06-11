package com.m7md7sn.dentel.presentation.ui.settings

import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import com.m7md7sn.dentel.presentation.theme.DentelTheme

@Composable
fun SettingsScreen(modifier: Modifier = Modifier) {

}

@Preview
@Composable
private fun SettingsScreenPreviewEn() {
    DentelTheme {
        SettingsScreen()
    }
}

@Preview(locale = "ar")
@Composable
private fun SettingsScreenPreviewAr() {
    DentelTheme {
        SettingsScreen()
    }
}