package com.example.feitox_android.presentation.components

import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.feitox_android.presentation.theme.Purple80
import com.example.feitox_android.presentation.viewmodel.TaskViewModel
import org.koin.androidx.compose.koinViewModel

@Preview
@Composable
fun OutLineSearchSlotPreview(){
    OutLineSearchSlot(
        userEmail = TODO()
    )
}


@Composable
fun OutLineSearchSlot(
    userEmail: String,
    viewModel: TaskViewModel = koinViewModel<TaskViewModel>()
){
    var valueInput by remember { mutableStateOf("") }
    val searchLetter by viewModel.searchLetter.collectAsState()

    OutlinedTextField(
        colors = TextFieldDefaults.colors(
            focusedTextColor = Purple80,
            unfocusedTextColor = Purple80,
            focusedContainerColor = Purple80,
            unfocusedContainerColor = Purple80,
        ),
        value = searchLetter,
        onValueChange = { newValueINput ->
            viewModel.updateSearchLetter(userEmail, newValueINput)
        },
        label = {
            Text(
                text = "Procurar tarefa",
                color = Color.Black,
                fontSize = 12.sp,
                fontWeight = FontWeight.SemiBold
            )
        },
        modifier = Modifier
            .fillMaxWidth()
            .height(60.dp),
        textStyle = TextStyle(
            fontSize = 18.sp,
            letterSpacing = 1.sp,
            lineHeight = 25.sp,
            fontWeight = FontWeight.Bold,
            color = Color.Black
        ),
        shape = RoundedCornerShape(10.dp)
    )
}