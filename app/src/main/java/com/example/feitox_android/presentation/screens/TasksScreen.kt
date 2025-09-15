package com.example.feitox_android.presentation.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavHostController
import com.example.feitox_android.R
import com.example.feitox_android.presentation.cards.CardCreateOrEdit
import com.example.feitox_android.presentation.cards.CardTask
import com.example.feitox_android.presentation.components.ButtonSlot
import com.example.feitox_android.presentation.components.OutLineSearchSlot
import com.example.feitox_android.presentation.navigation.Screen
import com.example.feitox_android.presentation.theme.Pink40
import com.example.feitox_android.presentation.theme.Pink80
import com.example.feitox_android.presentation.theme.PurpleGrey80
import com.example.feitox_android.presentation.uistate.TaskUiState
import com.example.feitox_android.presentation.viewmodel.AuthViewModel
import com.example.feitox_android.presentation.viewmodel.TaskViewModel
import com.example.feitox_android.repository.model.TaskModel.TaskResponse
import kotlinx.coroutines.launch
import org.koin.androidx.compose.koinViewModel

@Composable
fun TasksScreen(
    userEmail: String,
    navController: NavHostController,
    taskViewModel: TaskViewModel = koinViewModel<TaskViewModel>(),
    authViewModel: AuthViewModel = koinViewModel<AuthViewModel>(),
) {
    var onDismissValue by remember { mutableStateOf(false) }
    val coroutineScope = rememberCoroutineScope()
    val state by taskViewModel.tasks.collectAsState()

    LaunchedEffect(true) {
        taskViewModel.getTasksByEmail(userEmail)
    }

    if (onDismissValue) {
        CardCreateOrEdit(
            userEmail = userEmail,
            createOrNo = true,
            onDismissValue = { onDismissValue = false },
        )
    }

    Scaffold(
        content = {

            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .background(PurpleGrey80)
                    .padding(10.dp, 80.dp, 10.dp, 10.dp),
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.Top

            ) {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceAround,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Text(
                        text = userEmail,
                        color = Color.White,
                        fontSize = 25.sp,
                        fontWeight = FontWeight.Bold
                    )
                    ButtonSlot(
                        onClick = {
                            coroutineScope.launch {
                                authViewModel.signOut()
                                navController.navigate(
                                    Screen.TaskLoginScreen.route
                                )
                            }
                        },
                        modifier = Modifier
                            .height(80.dp)
                            .width(90.dp),
                        background = Pink80,
                        text = {
                            Text(
                                "Sair",
                                fontSize = 18.sp,
                                fontWeight = FontWeight.Medium,
                                color = Pink40
                            )
                        }
                    )
                }
                Spacer(modifier = Modifier.height(25.dp))
                OutLineSearchSlot(userEmail = userEmail)

                when (state) {

                    is TaskUiState.Loading -> {
                        Column(
                            modifier = Modifier
                                .fillMaxSize()
                                .padding(10.dp),
                            horizontalAlignment = Alignment.CenterHorizontally,
                            verticalArrangement = Arrangement.Center

                        ) {
                            CircularProgressIndicator()
                        }
                    }

                    is TaskUiState.Error -> {
                        val error = (state as TaskUiState.Error).msg
                        Column(
                            modifier = Modifier
                                .fillMaxSize()
                                .padding(10.dp),
                            horizontalAlignment = Alignment.CenterHorizontally,
                            verticalArrangement = Arrangement.Center

                        ) {
                            Icon(
                                modifier = Modifier
                                    .size(200.dp),
                                painter = painterResource(id = R.drawable.outline_add_task_24),
                                contentDescription = "logo_google",
                                tint = Color.White
                            )
                            Text(
                                error,
                                color = Color.White,
                                fontSize = 25.sp,
                                fontWeight = FontWeight.Bold
                            )
                        }
                    }

                    is TaskUiState.Succses -> {
                        val tasks = (state as TaskUiState.Succses).tasks

                        LazyColumn(
                            modifier = Modifier
                                .padding(it),
                            verticalArrangement = Arrangement.spacedBy(16.dp)
                        ) {

                            items(items = tasks, key = { it.id }) { task ->

                                CardTask(
                                    TaskResponse(
                                        id = task.id,
                                        name = task.name,
                                        description = task.description,
                                        isDone = task.isDone,
                                        userEmail = task.userEmail
                                    )
                                )
                            }
                        }
                    }
                }
            }
        },
        floatingActionButton = {
            FloatingActionButton(
                onClick = { onDismissValue = !onDismissValue },
                containerColor = Pink80
            ) {
                Icon(
                    painter = painterResource(id = R.drawable.add_circle),
                    contentDescription = "Adcionar tarefa"
                )
            }
        }
    )
}