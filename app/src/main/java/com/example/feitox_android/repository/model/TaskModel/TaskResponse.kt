package com.example.feitox_android.repository.model.TaskModel

import kotlinx.serialization.Serializable

@Serializable
data class TaskResponse(
    val id: String,
    val name: String,
    val description: String,
    val isDone: Boolean,
    val userEmail: String
)
