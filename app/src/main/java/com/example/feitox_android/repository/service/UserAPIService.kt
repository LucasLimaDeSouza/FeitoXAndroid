package com.example.feitox_android.repository.service

import com.example.feitox_android.repository.model.TaskModel.TaskRequest
import com.example.feitox_android.repository.model.TaskModel.TaskResponse
import com.example.feitox_android.repository.model.UserModel.UserRequest
import com.example.feitox_android.repository.model.UserModel.UserResponse
import io.ktor.client.HttpClient
import io.ktor.client.call.NoTransformationFoundException
import io.ktor.client.call.body
import io.ktor.client.request.accept
import io.ktor.client.request.delete
import io.ktor.client.request.get
import io.ktor.client.request.post
import io.ktor.client.request.setBody
import io.ktor.http.ContentType
import io.ktor.http.HttpStatusCode
import io.ktor.http.contentType
import io.ktor.http.encodeURLPath

class UserAPIService(
    private val baseUrl: String = "http://10.0.2.2:8080",
    private val httpClient: HttpClient
) {

    suspend fun users(): List<UserResponse> {
        return try {
            httpClient.get("$baseUrl/users").body()
        } catch (e: Exception) {
            throw e
            emptyList()
        }
    }

    suspend fun getUserByEmail(email: String): List<UserResponse> {
        return try {
            val response = httpClient.get("$baseUrl/users/${email.encodeURLPath()}") {
                accept(ContentType.Application.Json)
            }

            when (response.status) {
                HttpStatusCode.OK -> response.body()
                HttpStatusCode.NotFound -> emptyList()
                else -> throw RuntimeException("Falha na requisição: ${response.status}")
            }

        } catch (e: Exception) {

            if (e is NoTransformationFoundException) {
                throw IllegalArgumentException("Resposta inválida do servidor")
            }
            throw e
        }
    }

    suspend fun saveUser(user: UserRequest) {
        try {
            val response = httpClient.post("$baseUrl/user") {
                contentType(ContentType.Application.Json)
                setBody(user)
            }
            return response.body()
        } catch (e: Exception) {
            throw e
        }

    }

    suspend fun deleteUser(user: UserResponse) {
        try {
            val response = httpClient.delete("$baseUrl/user"){
                contentType(ContentType.Application.Json)
                setBody(user)
            }
        } catch (e: Exception) {
            throw e
        }
    }

}