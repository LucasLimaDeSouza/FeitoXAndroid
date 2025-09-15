package com.example.feitox_android.presentation.uistate

import com.example.feitox_android.repository.model.UserModel.UserResponse

sealed class UserUiState {
    object Loading: UserUiState()
    object Succses : UserUiState()
    data class Send(val msg: String): UserUiState()
    data class Error(val msg: String): UserUiState()
}
