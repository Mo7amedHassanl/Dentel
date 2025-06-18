package com.m7md7sn.dentel.presentation.ui.article.components

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import coil.compose.AsyncImage
import com.m7md7sn.dentel.R

@Composable
fun ArticleImage(
    imageUrl: String?,
    modifier: Modifier = Modifier
) {
    Box(
        modifier = modifier
            .fillMaxWidth()
            .height(200.dp)
            .padding(horizontal = 32.dp)
    ) {
        if (imageUrl != null) {
            // Create a loading state to track the image loading progress
            var isLoading by remember { mutableStateOf(true) }

            AsyncImage(
                model = imageUrl,
                contentDescription = "Article Image",
                contentScale = ContentScale.Crop,
                modifier = Modifier
                    .fillMaxSize()
                    .clip(RoundedCornerShape(8.dp)),
                fallback = painterResource(id = R.drawable.ic_article_image_placeholder),
                error = painterResource(id = R.drawable.ic_article_image_placeholder),
                onLoading = { isLoading = true },
                onSuccess = { isLoading = false },
                onError = { isLoading = false }
            )

            // Show loading indicator while image is loading
            if (isLoading) {
                Box(
                    modifier = Modifier
                        .fillMaxSize()
                        .background(Color.Gray.copy(alpha = 0.2f))
                ) {
                    CircularProgressIndicator(
                        modifier = Modifier.align(Alignment.Center)
                    )
                }
            }
        } else {
            // Fall back to placeholder if imageUrl is null
            Image(
                painter = painterResource(id = R.drawable.ic_article_image_placeholder),
                contentDescription = "Article Image Placeholder",
                modifier = Modifier.fillMaxSize(),
                contentScale = ContentScale.Crop
            )
        }
    }
}
