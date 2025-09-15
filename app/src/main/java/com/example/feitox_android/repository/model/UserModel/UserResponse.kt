package com.example.feitox_android.repository.model.UserModel

import kotlinx.serialization.Serializable

@Serializable
data class UserResponse(
    val idUser: String,
    val userEmail: String,
)