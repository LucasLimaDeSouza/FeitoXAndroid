package com.example.feitox_android.presentation.cards

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Card
import androidx.compose.material3.Checkbox
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.feitox_android.presentation.components.ButtonSlot
import com.example.feitox_android.presentation.theme.Pink40
import com.example.feitox_android.presentation.theme.Pink80
import com.example.feitox_android.presentation.theme.Purple80
import com.example.feitox_android.presentation.viewmodel.TaskViewModel
import com.example.feitox_android.repository.model.TaskModel.TaskResponse
import kotlinx.coroutines.launch
import org.koin.androidx.compose.koinViewModel


@Composable
fun CardTask(
    task: TaskResponse,
    viewModel: TaskViewModel = koinViewModel<TaskViewModel>()
) {
    val coroutineScope = rememberCoroutineScope()
    var onDismissValue by remember { mutableStateOf(false) }
    var deleteOrNo by remember { mutableStateOf(false) }


    if (onDismissValue) {
        CardCreateOrEdit(
            deleteOrNo = deleteOrNo,
            createOrNo = false,
            onDismissValue = { onDismissValue = false },
            task = task
        )
    }

    Card {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .background(Pink80)
                .padding(10.dp)
        ) {
            Text(text = task.name, color = Color.White,
                fontSize = 18.sp,
                fontWeight = FontWeight.Bold)
            Spacer(modifier = Modifier.height(10.dp))
            Text(
                text = task.description, color = Pink40,
                fontSize = 16.sp,
                fontWeight = FontWeight.Bold
            )
            Row(
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Checkbox(
                    checked = task.isDone,
                    onCheckedChange = {
                        coroutineScope.launch {
                            viewModel.updateIsDone(task)
                        }
                    }
                )
                Text(
                    text = "Feito",
                    color = Pink40,
                    fontSize = 18.sp,
                    fontWeight = FontWeight.Bold
                )
            }
            Row(
                modifier = Modifier
                    .fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                ButtonSlot(
                    modifier = Modifier
                        .padding(10.dp),
                    onClick = {
                        coroutineScope.launch {
                            deleteOrNo = false
                            onDismissValue = !onDismissValue
                        }
                    },
                    background = Purple80,
                    text = { Text("Editar", color = Pink40) }
                )
                ButtonSlot(
                    modifier = Modifier
                        .padding(10.dp),
                    onClick = {
                        coroutineScope.launch {
                            deleteOrNo = true
                            onDismissValue = !onDismissValue
                        }
                    },
                    background = Color.Red,
                    text = { Text("Apagar tarefa") }
                )
            }
        }
    }
}