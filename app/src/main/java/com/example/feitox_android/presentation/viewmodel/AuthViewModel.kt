package com.example.feitox_android.presentation.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.feitox_android.presentation.auth.AuthenticationManager
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch

private const val TAG = "AuthViewModel"

class AuthViewModel() : ViewModel() {
    private val authManager = AuthenticationManager()

    private val _authReturn = MutableStateFlow<AuthState>(AuthState.Idle)
    val authReturn: StateFlow<AuthState> = _authReturn

    val isAuthenticatedFlow: StateFlow<EmailState> = authManager.currentUser
        .map { currentUser ->
            EmailState(
                isAuthenticated = currentUser != null,
                email = currentUser
            )
        }
        .stateIn(
            scope = viewModelScope,
            started = SharingStarted.WhileSubscribed(5000),
            initialValue = EmailState(isAuthenticated = false, email = null)
        )


    fun createACountWithEmailAndPassword(email: String, password: String) {
        viewModelScope.launch {
            _authReturn.value = AuthState.Loading
            authManager.createACountWithEmailAndPassword(email = email, password = password)
                .onSuccess {
                    _authReturn.value = AuthState.Success(it)
                }.onFailure {
                    _authReturn.value = AuthState.Error(it.message ?: "Erro desconhecido!")
                }
        }
    }

    suspend fun signIn(email: String, password: String): String {

        _authReturn.value = AuthState.Loading
        authManager.signIn(email, password)
            .onSuccess { userEmail ->
                _authReturn.value = AuthState.Success(userEmail)
                print("$TAG, $userEmail")
            }
            .onFailure {
                _authReturn.value = AuthState.Error("Erro ao tentar fazer login!")
            }
        return _authReturn.value.toString()
    }

    fun signOut() {
        authManager.signOut()
    }

}

sealed class AuthState {
    object Idle : AuthState()
    object Loading : AuthState()
    data class Success(val email: String) : AuthState()
    data class Error(val message: String) : AuthState()
}

data class EmailState(
    val isAuthenticated: Boolean,
    val email: String?,
)