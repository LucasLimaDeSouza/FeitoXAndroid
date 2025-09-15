package com.example.feitox_android.presentation.components

import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.tooling.preview.Preview

@Preview(showBackground = true)
@Composable
fun ButtonSlotPreview(){
    ButtonSlot(
        modifier = TODO(),
        onClick = TODO(),
        text = TODO(),
        background = TODO()
    )
}

@Composable
fun ButtonSlot(
    modifier: Modifier,
    onClick: () -> Unit,
    background: Color,
    text: @Composable () -> Unit
){
    Button(
        modifier = modifier,
        colors = ButtonDefaults.buttonColors(
            containerColor = background
        ),
        onClick = { onClick() }
    ) {
        text()
    }
}