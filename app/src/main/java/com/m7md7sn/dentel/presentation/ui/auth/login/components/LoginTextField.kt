package com.m7md7sn.dentel.presentation.ui.auth.login.components

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Visibility
import androidx.compose.material.icons.filled.VisibilityOff
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.focus.FocusDirection
import androidx.compose.ui.focus.FocusManager
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.input.VisualTransformation
import androidx.compose.ui.unit.dp
import com.m7md7sn.dentel.R
import com.m7md7sn.dentel.presentation.common.components.CommonTextField
import androidx.compose.ui.graphics.vector.rememberVectorPainter

/**
 * Login form text fields component that contains email and password fields
 */
@Composable
fun LoginTextFields(
    email: String,
    onEmailValueChange: (String) -> Unit,
    password: String,
    onPasswordValueChange: (String) -> Unit,
    onLoginClick: () -> Unit,
    focusManager: FocusManager = LocalFocusManager.current,
    isEmailError: Boolean = false,
    emailErrorMessage: String? = null,
    isPasswordError: Boolean = false,
    passwordErrorMessage: String? = null,
    isPasswordVisible: Boolean = false,
    onTogglePasswordVisibility: () -> Unit
) {
    Column(
        modifier = Modifier.fillMaxWidth(),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        CommonTextField(
            value = email,
            onValueChange = onEmailValueChange,
            label = stringResource(R.string.email),
            trailingIcon = painterResource(R.drawable.ic_user),
            keyboardOptions = KeyboardOptions(
                keyboardType = KeyboardType.Email,
                imeAction = ImeAction.Next
            ),
            keyboardActions = KeyboardActions(
                onNext = { focusManager.moveFocus(FocusDirection.Down) }
            ),
            modifier = Modifier.fillMaxWidth(),
            isError = isEmailError,
            errorMessage = emailErrorMessage
        )
        Spacer(Modifier.height(32.dp))
        CommonTextField(
            value = password,
            onValueChange = onPasswordValueChange,
            label = stringResource(R.string.password),
            trailingIcon = rememberVectorPainter(
                image = if (isPasswordVisible) Icons.Filled.VisibilityOff else Icons.Filled.Visibility
            ),
            keyboardOptions = KeyboardOptions(
                keyboardType = KeyboardType.Password,
                imeAction = ImeAction.Done
            ),
            keyboardActions = KeyboardActions(
                onDone = {
                    focusManager.clearFocus()
                    onLoginClick()
                }
            ),
            modifier = Modifier.fillMaxWidth(),
            isError = isPasswordError,
            errorMessage = passwordErrorMessage,
            visualTransformation = if (isPasswordVisible) VisualTransformation.None else PasswordVisualTransformation(),
            onTrailingIconClick = onTogglePasswordVisibility
        )
    }
}
