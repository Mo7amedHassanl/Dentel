package com.m7md7sn.dentel.presentation.ui.auth.passwordreset.components

import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import com.m7md7sn.dentel.R
import com.m7md7sn.dentel.presentation.common.components.CommonTextField

/**
 * Email input field component for password reset
 */
@Composable
fun EmailInputField(
    email: String,
    onEmailValueChange: (String) -> Unit,
    onSendPasswordResetClick: () -> Unit,
    isEmailError: Boolean = false,
    emailErrorMessage: String? = null,
    modifier: Modifier = Modifier
) {
    CommonTextField(
        value = email,
        onValueChange = onEmailValueChange,
        label = stringResource(R.string.email),
        trailingIcon = painterResource(id = R.drawable.ic_user),
        modifier = modifier.fillMaxWidth(),
        keyboardOptions = KeyboardOptions(
            imeAction = ImeAction.Done,
            keyboardType = KeyboardType.Email
        ),
        keyboardActions = KeyboardActions(
            onDone = {
                onSendPasswordResetClick()
            }
        ),
        isError = isEmailError,
        errorMessage = emailErrorMessage
    )
}
