package com.m7md7sn.dentel.presentation.ui.auth.signup.components

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
import androidx.compose.ui.graphics.vector.rememberVectorPainter
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

/**
 * Signup form text fields component that contains username, email, password and confirm password fields
 */
@Composable
fun SignUpTextFields(
    username: String,
    onUsernameValueChange: (String) -> Unit,
    email: String,
    onEmailValueChange: (String) -> Unit,
    password: String,
    onPasswordValueChange: (String) -> Unit,
    confirmPassword: String,
    onConfirmPasswordValueChange: (String) -> Unit,
    onSignupClick: () -> Unit = {},
    focusManager: FocusManager = LocalFocusManager.current,
    isUsernameError: Boolean = false,
    usernameErrorMessage: String? = null,
    isEmailError: Boolean = false,
    emailErrorMessage: String? = null,
    isPasswordError: Boolean = false,
    passwordErrorMessage: String? = null,
    isConfirmPasswordError: Boolean = false,
    confirmPasswordErrorMessage: String? = null,
    isPasswordVisible: Boolean = false,
    onTogglePasswordVisibility: () -> Unit,
    isConfirmPasswordVisible: Boolean = false,
    onToggleConfirmPasswordVisibility: () -> Unit
) {
    Column(
        modifier = Modifier.fillMaxWidth(),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        CommonTextField(
            value = username,
            onValueChange = onUsernameValueChange,
            label = stringResource(R.string.username),
            trailingIcon = painterResource(R.drawable.ic_user),
            modifier = Modifier.fillMaxWidth(),
            keyboardOptions = KeyboardOptions(
                imeAction = ImeAction.Next,
                keyboardType = KeyboardType.Text
            ),
            keyboardActions = KeyboardActions(
                onNext = {
                    focusManager.moveFocus(FocusDirection.Down)
                }
            ),
            isError = isUsernameError,
            errorMessage = usernameErrorMessage
        )
        Spacer(Modifier.height(12.dp))
        CommonTextField(
            value = email,
            onValueChange = onEmailValueChange,
            label = stringResource(R.string.email),
            trailingIcon = painterResource(R.drawable.ic_email),
            modifier = Modifier.fillMaxWidth(),
            keyboardOptions = KeyboardOptions(
                imeAction = ImeAction.Next,
                keyboardType = KeyboardType.Email
            ),
            keyboardActions = KeyboardActions(
                onNext = {
                    focusManager.moveFocus(FocusDirection.Down)
                }
            ),
            isError = isEmailError,
            errorMessage = emailErrorMessage
        )
        Spacer(Modifier.height(12.dp))
        CommonTextField(
            value = password,
            onValueChange = onPasswordValueChange,
            label = stringResource(R.string.password),
            trailingIcon = rememberVectorPainter(
                image = if (isPasswordVisible) Icons.Filled.VisibilityOff else Icons.Filled.Visibility
            ),
            modifier = Modifier.fillMaxWidth(),
            keyboardOptions = KeyboardOptions(
                imeAction = ImeAction.Next,
                keyboardType = KeyboardType.Password
            ),
            keyboardActions = KeyboardActions(
                onNext = {
                    focusManager.moveFocus(FocusDirection.Down)
                }
            ),
            isError = isPasswordError,
            errorMessage = passwordErrorMessage,
            visualTransformation = if (isPasswordVisible) VisualTransformation.None else PasswordVisualTransformation(),
            onTrailingIconClick = onTogglePasswordVisibility
        )
        Spacer(Modifier.height(12.dp))
        CommonTextField(
            value = confirmPassword,
            onValueChange = onConfirmPasswordValueChange,
            label = stringResource(R.string.confirm_password),
            trailingIcon = rememberVectorPainter(
                image = if (isConfirmPasswordVisible) Icons.Filled.VisibilityOff else Icons.Filled.Visibility
            ),
            modifier = Modifier.fillMaxWidth(),
            keyboardOptions = KeyboardOptions(
                imeAction = ImeAction.Done,
                keyboardType = KeyboardType.Password
            ),
            keyboardActions = KeyboardActions(
                onDone = {
                    focusManager.clearFocus()
                    onSignupClick()
                }
            ),
            isError = isConfirmPasswordError,
            errorMessage = confirmPasswordErrorMessage,
            visualTransformation = if (isConfirmPasswordVisible) VisualTransformation.None else PasswordVisualTransformation(),
            onTrailingIconClick = onToggleConfirmPasswordVisibility
        )
    }
}
