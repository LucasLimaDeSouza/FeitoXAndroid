package com.example.feitox_android.presentation.auth

import android.util.Log
import com.google.firebase.Firebase
import com.google.firebase.auth.auth
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.tasks.await
import kotlin.coroutines.resume
import kotlin.coroutines.suspendCoroutine

private const val TAG = "AuthenticationManager"

class AuthenticationManager() {
    private val auth = Firebase.auth
    private val _currentUser = MutableStateFlow<String?>(null)
    val currentUser = _currentUser.asStateFlow()

    init {
        auth.addAuthStateListener { user ->
            _currentUser.value = user.currentUser?.email
        }
    }

    suspend fun sendPasswordResetEmail(email: String): Result<Unit> {
        return try {
            auth.sendPasswordResetEmail(email).await() // .await() para usar com coroutines
            Result.success(Unit)
        } catch (e: Exception) {
            Log.e("PasswordReset", "Erro ao enviar email de redefinição", e)
            Result.failure(e)
        }
    }

    suspend fun createACountWithEmailAndPassword(
        email: String,
        password: String
    ): Result<String> = suspendCoroutine { continuation ->
        auth.createUserWithEmailAndPassword(email, password)
            .addOnCompleteListener { task ->
                if (task.isSuccessful) {
                    val email = task.result?.user?.email ?: "null"
                    Log.i(TAG, "create a user: sucesso: $email")
                    continuation.resume(Result.success(email))
                } else {
                    val error = task.exception ?: Exception("Unknown error")
                    Log.i(TAG, "fail to create a user: falha -> $error")
                    continuation.resume(Result.failure(error))
                }
            }
    }

    suspend fun signIn(email: String, password: String): Result<String> =
        suspendCoroutine { continuation ->
            auth.signInWithEmailAndPassword(email, password)
                .addOnCompleteListener { task ->
                    if (task.isSuccessful) {
                        Log.i(TAG, "success to login: Email -> $email")
                        continuation.resume(Result.success(task.result.user?.email.toString()))
                    } else {
                        val error = task.exception ?: Exception("Login error")
                        Log.i(TAG, "fail to login: falha -> $error")
                        continuation.resume(Result.failure(error))
                    }
                }
        }

    // TESTE !!! -> PODENDO LOGAR COM QUALQUER CARACTER PARA FINS DE TESTEs
//    suspend fun signIn(email: String, password: String): Result<String> =
//        suspendCoroutine { continuation ->
//
//            if (!email.isEmpty() && !password.isEmpty()) {
//                Log.i(TAG, "success to login: Email -> $email")
//                continuation.resume(Result.success(email))
//            } else {
//                val error = Exception("Login error")
//                Log.i(TAG, "fail to login: falha -> $error")
//                continuation.resume(Result.failure(error))
//            }
//        }

    fun signOut() {
        auth.signOut()
    }

}

sealed interface AuthResponse {
    data class Success(val userName: String) : AuthResponse
    data class Error(val msg: String) : AuthResponse
}