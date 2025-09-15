package com.example.feitox_android.presentation.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.feitox_android.presentation.auth.AuthenticationManager
import com.example.feitox_android.presentation.uistate.UserUiState
import com.example.feitox_android.repository.model.UserModel.UserRequest
import com.example.feitox_android.repository.model.UserModel.UserResponse
import com.example.feitox_android.repository.service.UserAPIService
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
// Parte ADM (Ainda Em Desenvolvimento)
class UserViewModel(
    private val repository: UserAPIService
) : ViewModel() {

    private val authManager = AuthenticationManager()

    private val _users = MutableStateFlow<UserUiState>(UserUiState.Loading)
    val users = _users.asStateFlow()

    private val _passwordReset = MutableStateFlow<PasswordResetUiState>(PasswordResetUiState.Loading)
    val passwordReset = _passwordReset.asStateFlow()

    fun requestPasswordWithEmail(email: String) {
        viewModelScope.launch {
            try {
                _users.value = UserUiState.Loading
                val result = authManager.sendPasswordResetEmail(email)
                result.fold(
                    onSuccess = {
                        _passwordReset.value = PasswordResetUiState.Success
                    }, onFailure = {
                        _passwordReset.value = PasswordResetUiState.Error("Email inválido!!")
                    }
                )
            } catch (e: Exception) {
                _users.value = UserUiState.Error("Erro ao tentar recuperar pelo email")
            }
        }
    }

    fun saveUser(user: UserRequest) {
        viewModelScope.launch {
            _users.value = UserUiState.Loading
            try {
                repository.saveUser(user)
            } catch (e: Exception) {
                _users.value = UserUiState.Error("Erro ao salvar usuario: ${e.message}")
            }
        }
    }
//
//    fun getUsers() {
//        viewModelScope.launch {
//            _users.value = UserUiState.Loading
//            try {
//                val users = repository.users()
//                _users.value = UserUiState.Succses(users)
//            } catch (e: Exception) {
//                _users.value = UserUiState.Error("Erro ao buscar usuario: ${e.message}")
//            }
//        }
//    }
//
//    fun getUserByEmail(userEmail: String) {
//        viewModelScope.launch {
//            _users.value = UserUiState.Loading
//            try {
//                val users = repository.getUserByEmail(userEmail)
//                _users.value = UserUiState.Succses(users)
//            } catch (e: Exception) {
//                _users.value = UserUiState.Error("Erro ao buscar usuario pelo email")
//            }
//        }
//    }
//
//    fun deteleUser(user: UserResponse) {
//        viewModelScope.launch {
//            _users.value = UserUiState.Loading
//            try {
//                repository.deleteUser(user)
//            } catch (e: Exception) {
//                _users.value = UserUiState.Error("Erro ao tentar excluir o usuario: ${e.message}")
//            }
//        }
//    }
}

sealed class PasswordResetUiState {
    object Idle : PasswordResetUiState()
    object Loading : PasswordResetUiState()
    object Success : PasswordResetUiState()
    data class Error(val message: String) : PasswordResetUiState()
}