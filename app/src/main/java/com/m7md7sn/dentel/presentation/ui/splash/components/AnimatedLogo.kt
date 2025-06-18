package com.m7md7sn.dentel.presentation.ui.splash.components

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.res.painterResource
import com.m7md7sn.dentel.R
import com.m7md7sn.dentel.presentation.theme.DentelDarkPurple

/**
 * Logo animation component for splash screen
 */
@Composable
fun AnimatedLogo(
    scale: Float,
    contentAlpha: Float,
    modifier: Modifier = Modifier
) {
    Box(
        modifier = modifier
            .fillMaxSize()
            .background(color = DentelDarkPurple),
        contentAlignment = Alignment.Center
    ) {
        Image(
            painter = painterResource(id = R.drawable.dentel_logo),
            contentDescription = "Dentel Logo",
            modifier = Modifier
                .graphicsLayer(
                    alpha = contentAlpha,
                    scaleX = scale,
                    scaleY = scale
                )
        )
    }
}
