package com.m7md7sn.dentel.presentation.ui.auth.pictureupload.components

import android.net.Uri
import androidx.annotation.DrawableRes
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material3.IconButton
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import coil.compose.rememberAsyncImagePainter

/**
 * Profile picture component with an add button overlay
 */
@Composable
fun ProfilePictureWithAddButton(
    imageUri: Uri?,
    @DrawableRes imageRes: Int,
    onAddButtonClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    Box(modifier = modifier.size(160.dp)) {
        Image(
            painter = if (imageUri != null)
                rememberAsyncImagePainter(imageUri)
            else
                painterResource(id = imageRes),
            contentDescription = "Profile Picture",
            modifier = Modifier.fillMaxSize(),
            contentScale = ContentScale.Crop
        )

        IconButton(
            onClick = onAddButtonClick,
            modifier = Modifier
                .align(Alignment.BottomEnd)
                .padding(8.dp)
        ) {
            Image(
                painter = painterResource(id = com.m7md7sn.dentel.R.drawable.ic_add),
                contentDescription = "Add Image"
            )
        }
    }
}
