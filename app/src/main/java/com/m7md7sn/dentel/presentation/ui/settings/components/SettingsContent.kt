package com.m7md7sn.dentel.presentation.ui.settings.components

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.selection.selectable
import androidx.compose.foundation.selection.selectableGroup
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.Button
import androidx.compose.material.ButtonDefaults
import androidx.compose.material.Divider
import androidx.compose.material.Icon
import androidx.compose.material.OutlinedButton
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.rounded.KeyboardArrowRight
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material.icons.filled.Edit
import androidx.compose.material.icons.filled.Email
import androidx.compose.material.icons.filled.KeyboardArrowRight
import androidx.compose.material.icons.filled.Lock
import androidx.compose.material.icons.filled.Message
import androidx.compose.material.icons.filled.Refresh
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.RadioButton
import androidx.compose.material3.RadioButtonDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.focus.FocusDirection
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.graphics.vector.rememberVectorPainter
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
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
import coil.compose.AsyncImage
import coil.request.ImageRequest
import com.m7md7sn.dentel.R
import com.m7md7sn.dentel.presentation.common.components.CommonLargeButton
import com.m7md7sn.dentel.presentation.common.components.CommonTextField
import com.m7md7sn.dentel.presentation.theme.DentelDarkPurple

/**
 * Content screen for Account settings
 */
@Composable
fun AccountContent(
    modifier: Modifier = Modifier,
    profileName: String = "",
    profileEmail: String = "",
    profilePhotoUrl: String? = null,
    isLoading: Boolean = false,
    uploadProgress: Int = 0,
    onNameChange: (String) -> Unit = {},
    onUpdateProfileClick: () -> Unit = {},
    onChangePasswordClick: () -> Unit = {},
    onChangeEmailClick: () -> Unit = {},
    onResetPasswordClick: () -> Unit = {},
    onDeleteAccountClick: () -> Unit = {},
    onPhotoClick: () -> Unit = {},
) {
    val scrollState = rememberScrollState()

    Box(modifier = modifier.fillMaxSize()) {
        if (isLoading) {
            LoadingIndicator()
        } else {
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .verticalScroll(scrollState)
                    .padding(16.dp),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                // Profile Photo Section
                ProfilePhotoSection(
                    photoUrl = profilePhotoUrl,
                    uploadProgress = uploadProgress,
                    modifier = Modifier.size(120.dp),
                    onClick = onPhotoClick
                )

                Spacer(modifier = Modifier.height(16.dp))

                // Profile Information Section
                Text(
                    text = stringResource(R.string.profile_information),
                    style = TextStyle(
                        fontSize = 22.sp,
                        fontFamily = FontFamily(Font(R.font.din_next_lt_bold)),
                        fontWeight = FontWeight.Bold,
                        color = DentelDarkPurple,
                    )
                )

                Spacer(modifier = Modifier.height(8.dp))

                // Name Field
                CommonTextField(
                    value = profileName,
                    onValueChange = onNameChange,
                    label = stringResource(R.string.username),
                    trailingIcon = painterResource(id = R.drawable.ic_user),
                    modifier = Modifier.fillMaxWidth(),
                )

                Spacer(modifier = Modifier.height(8.dp))

                // Email Field (read-only)
                CommonTextField(
                    value = profileEmail,
                    onValueChange = {},
                    label = stringResource(R.string.email),
                    trailingIcon = painterResource(id = R.drawable.ic_email),
                    modifier = Modifier.fillMaxWidth(),
                    enabled = false
                )

                Spacer(modifier = Modifier.height(16.dp))

                // Update Profile Button
                CommonLargeButton(
                    text = stringResource(R.string.update_profile),
                    onClick = onUpdateProfileClick,
                    modifier = Modifier.fillMaxWidth()
                )

                Spacer(modifier = Modifier.height(24.dp))
                Divider(color = Color.LightGray.copy(alpha = 0.5f), thickness = 1.dp)
                Spacer(modifier = Modifier.height(24.dp))

                // Account Management Section
                Text(
                    text = stringResource(R.string.account_management),
                    style = TextStyle(
                        fontSize = 22.sp,
                        fontFamily = FontFamily(Font(R.font.din_next_lt_bold)),
                        fontWeight = FontWeight.Bold,
                        color = DentelDarkPurple,
                    )
                )

                Spacer(modifier = Modifier.height(16.dp))

                // Account Management Options
                AccountOptionButton(
                    text = stringResource(R.string.change_password),
                    icon = Icons.Default.Lock,
                    onClick = onChangePasswordClick,
                    modifier = Modifier.fillMaxWidth()
                )

                Spacer(modifier = Modifier.height(8.dp))

                AccountOptionButton(
                    text = stringResource(R.string.reset_password),
                    icon = Icons.Default.Refresh,
                    onClick = onResetPasswordClick,
                    modifier = Modifier.fillMaxWidth()
                )

                Spacer(modifier = Modifier.height(24.dp))
                Divider(color = Color.LightGray.copy(alpha = 0.5f), thickness = 1.dp)
                Spacer(modifier = Modifier.height(24.dp))

                // Danger Zone
                Text(
                    text = stringResource(R.string.danger_zone),
                    style = TextStyle(
                        fontSize = 22.sp,
                        fontFamily = FontFamily(Font(R.font.din_next_lt_bold)),
                        fontWeight = FontWeight.Bold,
                        color = Color.Red,
                    )
                )

                Spacer(modifier = Modifier.height(16.dp))

                // Delete Account Button
                Button(
                    onClick = onDeleteAccountClick,
                    modifier = Modifier.fillMaxWidth(),
                    colors = ButtonDefaults.buttonColors(
                        backgroundColor = Color.Red.copy(alpha = 0.8f),
                        contentColor = Color.White
                    )
                ) {
                    Icon(
                        imageVector = Icons.Default.Delete,
                        contentDescription = stringResource(R.string.delete_account),
                        tint = Color.White
                    )
                    Spacer(modifier = Modifier.width(8.dp))
                    Text(
                        stringResource(R.string.delete_account),
                        color = Color.White,
                        style = TextStyle(
                            fontWeight = FontWeight.Bold
                        )
                    )
                }

                Spacer(modifier = Modifier.height(16.dp))
            }
        }
    }
}

@Composable
private fun ProfilePhotoSection(
    photoUrl: String?,
    uploadProgress: Int,
    modifier: Modifier = Modifier,
    onClick: () -> Unit = {},
) {
    Box(
        modifier = modifier
            .clickable(onClick = onClick),
    ) {
        Box(
            modifier = modifier
                .clip(CircleShape)
                .border(2.dp, DentelDarkPurple, CircleShape)
        ) {
            // Profile image
            AsyncImage(
                model = ImageRequest.Builder(LocalContext.current)
                    .data(photoUrl ?: R.drawable.male_avatar)
                    .crossfade(true)
                    .build(),
                contentDescription = "Profile Photo",
                contentScale = ContentScale.Crop,
                modifier = Modifier
                    .fillMaxSize()
            )
            // Upload progress indicator
            if (uploadProgress > 0) {
                androidx.compose.material.LinearProgressIndicator(
                    progress = uploadProgress / 100f,
                    modifier = Modifier
                        .fillMaxWidth()
                        .align(Alignment.BottomCenter)
                        .height(4.dp),
                    backgroundColor = Color.Transparent,
                    color = DentelDarkPurple
                )
            }
        }
        Box(
            modifier = Modifier
                .align(Alignment.BottomEnd)
                .size(36.dp)
                .clip(CircleShape)
                .background(DentelDarkPurple)
                .padding(8.dp)
        ) {
            Icon(
                imageVector = Icons.Default.Edit,
                contentDescription = "Edit Profile Photo",
                tint = Color.White,
                modifier = Modifier.align(Alignment.Center)
            )
        }
    }

}

@Composable
private fun AccountOptionButton(
    text: String,
    icon: ImageVector,
    onClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    Card(
        modifier = modifier
            .padding(vertical = 4.dp)
            .fillMaxWidth()
            .height(60.dp),
        shape = RoundedCornerShape(16.dp),
        elevation = CardDefaults.cardElevation(
            defaultElevation = 2.dp
        ),
        colors = CardDefaults.cardColors(containerColor = Color(0xFFE5E1FF)),
        onClick = onClick
    ) {
        Row(
            modifier = Modifier
                .fillMaxSize()
                .padding(horizontal = 16.dp),
            verticalAlignment = Alignment.CenterVertically,
        ) {
            Icon(
                imageVector = icon,
                contentDescription = null,
                tint = DentelDarkPurple
            )
            Spacer(modifier = Modifier.width(8.dp))
            Text(
                text = text,
                style = TextStyle(
                    fontSize = 16.sp,
                    fontWeight = FontWeight.Normal
                )
            )
            Spacer(Modifier.weight(1f))
            Icon(
                imageVector = Icons.AutoMirrored.Rounded.KeyboardArrowRight,
                contentDescription = null,
                tint = DentelDarkPurple
            )
        }
    }
//    OutlinedButton(
//        onClick = onClick,
//        modifier = modifier,
//        border = BorderStroke(1.dp, DentelDarkPurple),
//        colors = ButtonDefaults.outlinedButtonColors(
//            contentColor = DentelDarkPurple
//        )
//    ) {
//        Icon(
//            imageVector = icon,
//            contentDescription = null,
//            tint = DentelDarkPurple
//        )
//        Spacer(modifier = Modifier.width(8.dp))
//        Text(
//            text = text,
//            style = TextStyle(
//                fontSize = 16.sp,
//                fontWeight = FontWeight.Normal
//            )
//        )
//        Spacer(Modifier.weight(1f))
//        Icon(
//            imageVector = Icons.Default.KeyboardArrowRight,
//            contentDescription = null,
//            tint = DentelDarkPurple
//        )
//    }
}

/**
 * Content screen for Notifications settings
 */
@Composable
fun NotificationsContent() {
    Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
        Text("Coming Soon...")
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
