package com.m7md7sn.dentel.app.host

import android.content.Context
import android.os.Bundle
import android.view.View
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.ui.platform.LocalLayoutDirection
import androidx.compose.ui.unit.LayoutDirection
import androidx.core.view.ViewCompat
import com.m7md7sn.dentel.presentation.navigation.DentelApp
import com.m7md7sn.dentel.presentation.theme.DentelTheme
import com.m7md7sn.dentel.utils.LocaleUtils
import dagger.hilt.android.AndroidEntryPoint
import java.util.Locale

@AndroidEntryPoint
class MainActivity : ComponentActivity() {

    override fun attachBaseContext(newBase: Context) {
        // Apply saved language settings before attaching base context
        val context = LocaleUtils.applyLocale(newBase)
        super.attachBaseContext(context)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()

        // Ensure window-level layout direction is set based on current locale
        val isRtl = Locale.getDefault().language == "ar"
        window.decorView.layoutDirection = if (isRtl) View.LAYOUT_DIRECTION_RTL else View.LAYOUT_DIRECTION_LTR

        setContent {
            // Get the appropriate layout direction based on the current locale
            val layoutDirection = if (isRtl) LayoutDirection.Rtl else LayoutDirection.Ltr

            // Apply the layout direction to the entire Compose hierarchy
            CompositionLocalProvider(LocalLayoutDirection provides layoutDirection) {
                DentelTheme {
                    DentelApp()
                }
            }
        }
    }
}
