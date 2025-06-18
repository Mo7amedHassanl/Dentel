package com.m7md7sn.dentel.presentation.ui.profile.components

import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Icon
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.semantics.contentDescription
import androidx.compose.ui.semantics.semantics
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import com.m7md7sn.dentel.R
import com.m7md7sn.dentel.presentation.theme.DentelBrightBlue

/**
 * Button component for showing more items in a list
 *
 * @param onClick Callback invoked when the button is clicked
 * @param modifier Modifier to be applied to the button
 * @param icon Icon to be displayed in the button (default is Add icon)
 * @param backgroundColor Background color of the button (default is DentelBrightBlue)
 * @param contentColor Color for the button content (default is White)
 * @param width Width of the button (default is 68.dp)
 * @param height Height of the button (default is 28.dp)
 * @param cornerRadius Corner radius of the button (default is 12.dp)
 * @param enabled Whether the button is enabled or not (default is true)
 */
@Composable
fun ShowMoreButton(
    onClick: () -> Unit,
    modifier: Modifier = Modifier,
    icon: ImageVector = Icons.Filled.Add,
    backgroundColor: Color = DentelBrightBlue,
    contentColor: Color = Color.White,
    width: Dp = 68.dp,
    height: Dp = 28.dp,
    cornerRadius: Dp = 12.dp,
    enabled: Boolean = true
) {
    val context = LocalContext.current
    val contentDesc = "Show more"

    Button(
        onClick = onClick,
        modifier = modifier
            .width(width)
            .height(height)
            .semantics {
                contentDescription = contentDesc
            },
        enabled = enabled,
        colors = ButtonDefaults.buttonColors(
            containerColor = backgroundColor,
            contentColor = contentColor,
            disabledContainerColor = backgroundColor.copy(alpha = 0.5f),
            disabledContentColor = contentColor.copy(alpha = 0.5f)
        ),
        contentPadding = PaddingValues(0.dp),
        shape = RoundedCornerShape(cornerRadius),
        elevation = ButtonDefaults.buttonElevation(
            defaultElevation = 2.dp,
            pressedElevation = 4.dp,
            disabledElevation = 0.dp
        )
    ) {
        Icon(
            imageVector = icon,
            contentDescription = null, // Content description is set at the button level
            modifier = Modifier.size(24.dp),
        )
    }
}
