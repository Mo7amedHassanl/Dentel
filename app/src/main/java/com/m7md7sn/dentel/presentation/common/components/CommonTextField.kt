package com.m7md7sn.dentel.presentation.common.components

import androidx.compose.foundation.Image
import androidx.compose.foundation.clickable
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.OutlinedTextFieldDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.ColorFilter
import androidx.compose.ui.graphics.painter.Painter
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.VisualTransformation
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.sp
import com.m7md7sn.dentel.R
import com.m7md7sn.dentel.presentation.theme.DentelDarkPurple

@Composable
fun CommonTextField(
    value: String = "Johnsondoe@nomail.com",
    onValueChange: (String) -> Unit = {},
    label: String = "البريد الإلكتروني",
    trailingIcon: Painter? = painterResource(id = R.drawable.ic_user),
    keyboardActions: KeyboardActions = KeyboardActions.Default,
    keyboardOptions: KeyboardOptions = KeyboardOptions.Default,
    isError: Boolean = false,
    errorMessage: String? = null,
    modifier: Modifier = Modifier,
    visualTransformation: VisualTransformation = VisualTransformation.None,
    onTrailingIconClick: (() -> Unit)? = null,
    enabled: Boolean = true
) {
    OutlinedTextField(
        value = value,
        onValueChange = onValueChange,
        modifier = modifier,
        colors = OutlinedTextFieldDefaults.colors(
            unfocusedBorderColor = if (isError) Color.Red else DentelDarkPurple.copy(alpha = 0.3f),
            focusedBorderColor = if (isError) Color.Red else DentelDarkPurple,
            cursorColor = DentelDarkPurple,
            focusedTextColor = DentelDarkPurple,
            unfocusedTextColor = DentelDarkPurple,
            disabledTextColor = DentelDarkPurple.copy(alpha = 0.8f),
        ),
        label = {
            Text(
                text = label,
                color = if (isError) Color.Red else DentelDarkPurple,
                fontSize = 14.sp,
                fontWeight = FontWeight.Medium
            )
        },
        trailingIcon = {
            trailingIcon?.let {
                Image(
                    painter = it,
                    contentDescription = null,
                    modifier = if (onTrailingIconClick != null) Modifier.clickable(onClick = onTrailingIconClick) else Modifier,
                    colorFilter = ColorFilter.tint(DentelDarkPurple),
                )
            }
        },
        keyboardActions = keyboardActions,
        keyboardOptions = keyboardOptions,
        isError = isError,
        supportingText = if (isError && !errorMessage.isNullOrBlank()) {
            {
                Text(
                    text = errorMessage,
                    color = Color.Red,
                    fontSize = 12.sp
                )
            }
        } else null,
        visualTransformation = visualTransformation,
        enabled = enabled
    )
}