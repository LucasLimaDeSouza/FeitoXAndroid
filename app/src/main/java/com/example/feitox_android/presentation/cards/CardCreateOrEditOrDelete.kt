package com.example.feitox_android.presentation.cards

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
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
import androidx.compose.ui.window.Dialog
import com.example.feitox_android.presentation.components.ButtonSlot
import com.example.feitox_android.presentation.components.OutLineInputSlot
import com.example.feitox_android.presentation.theme.Pink40
import com.example.feitox_android.presentation.theme.Purple80
import com.example.feitox_android.presentation.viewmodel.TaskViewModel
import com.example.feitox_android.repository.model.TaskModel.TaskRequest
import com.example.feitox_android.repository.model.TaskModel.TaskResponse
import kotlinx.coroutines.launch
import org.koin.androidx.compose.koinViewModel

@Composable
fun CardCreateOrEdit(
    deleteOrNo: Boolean = false,
    userEmail: String = "",
    createOrNo: Boolean,
    onDismissValue: () -> Unit = {},
    task: TaskResponse = TaskResponse(
        id = "",
        name = "",
        description = "",
        isDone = false,
        userEmail = ""
    ),
    viewModel: TaskViewModel = koinViewModel<TaskViewModel>()
) {
    val coroutineScope = rememberCoroutineScope()

    var newNameValue by remember { mutableStateOf("") }
    var newDescriptionValue by remember { mutableStateOf("") }

    var nameValue by remember { mutableStateOf(task.name) }
    var descriptionValue by remember { mutableStateOf(task.description) }


    fun createOrUpdate() {

        when (createOrNo) {
            true -> {
                coroutineScope.launch {
                    viewModel.createTask(
                        TaskRequest(
                            name = newNameValue,
                            description = newDescriptionValue,
                            userEmail = userEmail
                        )
                    )
                    onDismissValue()
                }
            }

            else -> {
                coroutineScope.launch {
                    viewModel.updateTask(
                        TaskResponse(
                            id = task.id,
                            name = nameValue,
                            description = descriptionValue,
                            isDone = task.isDone,
                            userEmail = task.userEmail,
                        )
                    )
                    onDismissValue()
                }
            }
        }
    }



    Dialog(onDismissRequest = { onDismissValue() }) {
        Card(
            elevation = CardDefaults.cardElevation(
                defaultElevation = 10.dp
            )
        ) {
            when (deleteOrNo) {
                true -> {
                    Column(
                        modifier = Modifier
                            .padding(10.dp),
                        verticalArrangement = Arrangement.Center,
                        horizontalAlignment = Alignment.CenterHorizontally

                    ) {
                        Text(
                            text = "EXLUIR TAREFA?",
                            color = Pink40,
                            fontSize = 18.sp,
                            fontWeight = FontWeight.Bold
                        )
                        Row {
                            ButtonSlot(
                                modifier = Modifier
                                    .padding(10.dp),
                                onClick = {
                                    coroutineScope.launch {
                                        viewModel.deleteTask(task.id, task.userEmail)
                                    }
                                },
                                background = Purple80,
                                text = { Text("Sim", color = Pink40) }
                            )
                            ButtonSlot(
                                modifier = Modifier
                                    .padding(10.dp),
                                onClick = { onDismissValue() },
                                background = Pink40,
                                text = { Text("Não", color = Color.White) }
                            )
                        }
                    }
                }

                else -> {
                    Column(
                        modifier = Modifier
                            .padding(10.dp),
                        verticalArrangement = Arrangement.Top,
                        horizontalAlignment = Alignment.CenterHorizontally

                    ) {
                        OutLineInputSlot(
                            modifier = Modifier
                                .padding(),
                            label = {
                                Text("Nome da Tarefa")
                            },
                            value = if (createOrNo) newNameValue else nameValue,
                            onChange = {
                                if (createOrNo) {
                                    newNameValue = it
                                } else {
                                    nameValue = it
                                }
                            },
                        )
                        Spacer(modifier = Modifier.height(10.dp))
                        OutLineInputSlot(
                            modifier = Modifier
                                .padding(),
                            value = if (createOrNo) newDescriptionValue else descriptionValue,
                            label = {
                                Text("Descrição da Tarefa")
                            },
                            onChange = {
                                if (createOrNo) {
                                    newDescriptionValue = it
                                } else {
                                    descriptionValue = it
                                }
                            },
                        )
                        Spacer(modifier = Modifier.height(10.dp))

                        Row(
                            modifier = Modifier
                                .fillMaxWidth(),
                            horizontalArrangement = Arrangement.Center
                        ) {
                            ButtonSlot(
                                modifier = Modifier
                                    .width(150.dp),
                                onClick = { createOrUpdate() },
                                text = {
                                    Text("Salvar", color = Pink40)
                                },
                                background = Purple80
                            )
                            ButtonSlot(
                                modifier = Modifier
                                    .width(150.dp),
                                onClick = { onDismissValue() },
                                text = {
                                    Text("Cancelar", color = Color.White)
                                },
                                background = Color.Red
                            )
                        }
                    }

                }
            }
        }
    }
}