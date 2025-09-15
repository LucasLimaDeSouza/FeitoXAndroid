package com.example.feitox_android.repository.model.TaskModel

import kotlinx.serialization.Serializable

@Serializable
data class TaskRequest(
    val name: String,
    val description: String,
    val userEmail: String
)