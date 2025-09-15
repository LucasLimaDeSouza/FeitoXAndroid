package com.example.feitox_android.presentation.components

import androidx.compose.material3.OutlinedTextField
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.input.VisualTransformation


@Composable
fun OutLineInputSlot(
    modifier: Modifier,
    label: @Composable () -> Unit,
    value: String,
    onChange: (String) -> Unit,
    isPassword: Boolean = false
) {
    OutlinedTextField(
        label = { label() },
        modifier = modifier,
        value = value,
        onValueChange = onChange,
        visualTransformation = if (isPassword) PasswordVisualTransformation() else VisualTransformation.None
    )
}