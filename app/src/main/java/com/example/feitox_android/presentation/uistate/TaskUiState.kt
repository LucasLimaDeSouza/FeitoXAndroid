package com.example.feitox_android.presentation.uistate

import com.example.feitox_android.repository.model.TaskModel.TaskResponse

sealed class TaskUiState {
    object Loading: TaskUiState()
    data class Succses(val tasks: List<TaskResponse>): TaskUiState()
    data class Error(val msg: String): TaskUiState()
}