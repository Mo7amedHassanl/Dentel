package com.m7md7sn.dentel.app.host

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import com.m7md7sn.dentel.presentation.navigation.DentelApp
import com.m7md7sn.dentel.presentation.theme.DentelTheme
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            DentelTheme {
                DentelApp()
            }
        }
    }
}