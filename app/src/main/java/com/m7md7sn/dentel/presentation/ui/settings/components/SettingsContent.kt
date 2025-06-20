package com.m7md7sn.dentel.presentation.ui.settings.components

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.selection.selectable
import androidx.compose.foundation.selection.selectableGroup
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Message
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.RadioButton
import androidx.compose.material3.RadioButtonDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.focus.FocusDirection
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.rememberVectorPainter
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.semantics.Role
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.Font
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.m7md7sn.dentel.R
import com.m7md7sn.dentel.presentation.common.components.CommonLargeButton
import com.m7md7sn.dentel.presentation.common.components.CommonTextField
import com.m7md7sn.dentel.presentation.theme.DentelDarkPurple

/**
 * Content screen for Account settings
 */
@Composable
fun AccountContent() {
    Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
        Text("Account Settings Content")
    }
}

/**
 * Content screen for Notifications settings
 */
@Composable
fun NotificationsContent() {
    Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
        Text("Notifications Settings Content")
    }
}

/**
 * Content screen for Language settings
 */
@Composable
fun LanguageContent(
    modifier: Modifier = Modifier,
    selectedLanguage: String,
    onLanguageSelected: (String) -> Unit,
) {
    val languageOptions = listOf("Arabic", "English")
    Column(modifier.selectableGroup()) {
        languageOptions.forEach { text ->
            Row(
                Modifier
                    .fillMaxWidth()
                    .height(60.dp)
                    .selectable(
                        selected = (text == selectedLanguage),
                        onClick = { onLanguageSelected(text) },
                        role = Role.RadioButton
                    )
                    .padding(horizontal = 32.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
                RadioButton(
                    selected = (text == selectedLanguage),
                    onClick = null,
                    colors = RadioButtonDefaults.colors(
                        selectedColor = DentelDarkPurple,
                        unselectedColor = Color(0xFFBDBDBD)
                    )
                )
                Spacer(modifier = Modifier.width(16.dp))
                Text(
                    text = text,
                    style = TextStyle(
                        fontSize = 20.sp,
                        fontFamily = FontFamily(Font(R.font.din_next_lt_bold)),
                        fontWeight = FontWeight(500),
                        color = Color(0xFF421882),
                        textAlign = TextAlign.Right,
                    )
                )
            }
        }
    }
}

/**
 * Content screen for Support settings - Pure UI component
 */
@Composable
fun SupportContent(
    email: String,
    message: String,
    isEmailError: Boolean,
    isMessageError: Boolean,
    isLoading: Boolean,
    onEmailChange: (String) -> Unit,
    onMessageChange: (String) -> Unit,
    onSendClick: () -> Unit
) {
    val focusManager = LocalFocusManager.current

    Column(
        modifier = Modifier
            .fillMaxWidth()
            .padding(16.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Text(
            text = stringResource(R.string.contact_developer),
            style = TextStyle(
                fontSize = 22.sp,
                fontFamily = FontFamily(Font(R.font.din_next_lt_bold)),
                fontWeight = FontWeight(600),
                color = DentelDarkPurple,
                textAlign = TextAlign.Center
            ),
        )

        Spacer(modifier = Modifier.height(16.dp))

        // Email field
        CommonTextField(
            value = email,
            onValueChange = onEmailChange,
            label = "Your Email",
            trailingIcon = painterResource(id = R.drawable.ic_user),
            isError = isEmailError,
            errorMessage = if (isEmailError) "Invalid email format" else null,
            modifier = Modifier.fillMaxWidth(),
            keyboardOptions = KeyboardOptions(
                keyboardType = KeyboardType.Email,
                imeAction = ImeAction.Next
            ),
            keyboardActions = KeyboardActions(
                onNext = { focusManager.moveFocus(FocusDirection.Down) }
            )
        )

        Spacer(modifier = Modifier.height(16.dp))

        // Message field
        CommonTextField(
            value = message,
            onValueChange = onMessageChange,
            label = "Message",
            isError = isMessageError,
            errorMessage = if (isMessageError) "Message cannot be empty" else null,
            modifier = Modifier
                .fillMaxWidth()
                .height(200.dp),
            keyboardOptions = KeyboardOptions(
                keyboardType = KeyboardType.Text,
                imeAction = ImeAction.Done
            ),
            keyboardActions = KeyboardActions(
                onDone = { focusManager.clearFocus() }
            ),
            trailingIcon = rememberVectorPainter(Icons.Filled.Message)
        )

        Spacer(modifier = Modifier.height(24.dp))

        // Send button
        CommonLargeButton(
            text = "Send Message",
            onClick = onSendClick,
            modifier = Modifier.fillMaxWidth(),
            isLoading = isLoading
        )
    }
}

/**
 * Loading indicator for settings operations
 */
@Composable
fun LoadingIndicator() {
    Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
        CircularProgressIndicator(color = Color(0xFF421882))
    }
}

/**
 * Error message display
 */
@Composable
fun ErrorMessage(message: String) {
    Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
        Text(text = message, color = Color.Red)
    }
}
