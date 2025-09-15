package com.example.feitox_android.presentation.viewmodel

import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.feitox_android.presentation.auth.AuthenticationManager
import com.example.feitox_android.presentation.uistate.TaskUiState
import com.example.feitox_android.repository.model.TaskModel.TaskRequest
import com.example.feitox_android.repository.model.TaskModel.TaskResponse
import com.example.feitox_android.repository.service.TaskAPIService
import kotlinx.coroutines.FlowPreview
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.debounce
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.launch


@OptIn(FlowPreview::class)
class TaskViewModel(
    private val repository: TaskAPIService
) : ViewModel() {

    private val authManager = AuthenticationManager()

    private val _tasks = MutableStateFlow<TaskUiState>(TaskUiState.Loading)
    val tasks = _tasks.asStateFlow()

    private val _searchLetter = MutableStateFlow("")
    val searchLetter: StateFlow<String> = _searchLetter

    private var email: String = ""

    val isUserEmailAuthenticatedFlow: StateFlow<String?> = authManager.currentUser

    init {
        viewModelScope.launch {
            searchLetter
                .debounce(300)
                .collect { letter ->
                    loadTask(email, letter)
                }
            getTasksByEmail(isUserEmailAuthenticatedFlow.toString())
        }
    }

    fun updateSearchLetter(userEmail: String,letter: String) {
        _searchLetter.value = letter
        email = userEmail
    }

    fun loadTask(email: String, name: String) {
        viewModelScope.launch {
            _tasks.value = TaskUiState.Loading
            try {
                if (name.isBlank()) {
                    val task = repository.getTasksByEmail(email)
                    _tasks.value = TaskUiState.Succses(task)
                } else {
                    val task = repository.getTasksByName(email, name)
                    _tasks.value = TaskUiState.Succses(task)
                }
            } catch (e: Exception) {
                _tasks.value = TaskUiState.Error("Erro: ${e.message}")
            }
        }
    }

    fun getTasksByEmail(email: String) {
        viewModelScope.launch {
            _tasks.value = TaskUiState.Loading
            try {
                val tasks = repository.getTasksByEmail(email)
                _tasks.value = TaskUiState.Succses(tasks)
            } catch (e: Exception) {
                _tasks.value = TaskUiState.Error("Não há tarefas")
            }
        }
    }

    fun createTask(task: TaskRequest) {
        viewModelScope.launch {
            try {
                repository.createTask(task)
                getTasksByEmail(task.userEmail)
            } catch (e: Exception) {
                e.message
            }
        }
    }

    fun updateTask(task: TaskResponse) {
        viewModelScope.launch {
            try {
                repository.updateTask(task)
            } catch (e: Exception) {
                _tasks.value = TaskUiState.Error("Falha ao tentar atualizar tarefa: ${e.message}")
            }
            getTasksByEmail(task.userEmail)
        }
    }

    fun updateIsDone(task: TaskResponse) {
        viewModelScope.launch {

            val current = _tasks.value
            if (current is TaskUiState.Succses) {
                try {
                    val taskUpdated = current.tasks.map {
                        if (it.id == task.id) {
                            val taskUpdated = it.copy(isDone = !it.isDone)
                            repository.updateTask(taskUpdated)
                            taskUpdated
                        } else it
                    }
                    _tasks.value = TaskUiState.Succses(taskUpdated)

                } catch (e: Exception) {
                    _tasks.value =
                        TaskUiState.Error("Falha ao tentar atualizar o status: ${e.message}")
                }
            }
        }

    }

    fun deleteTask(idTask: String, userEmail: String) {
        viewModelScope.launch {
            val current = _tasks.value
            if (current is TaskUiState.Succses) {
                try {
                    current.tasks.map {
                        if (it.id == idTask) repository.deleteTask(idTask)
                    }
                    getTasksByEmail(userEmail)
                } catch (e: Exception) {
                    e.message
                }
            }
        }
    }
}