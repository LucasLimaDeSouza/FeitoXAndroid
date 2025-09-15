package com.example.feitox_android.repository.service

import com.example.feitox_android.repository.model.TaskModel.TaskRequest
import com.example.feitox_android.repository.model.TaskModel.TaskResponse
import io.ktor.client.HttpClient
import io.ktor.client.call.NoTransformationFoundException
import io.ktor.client.call.body
import io.ktor.client.request.accept
import io.ktor.client.request.delete
import io.ktor.client.request.get
import io.ktor.client.request.post
import io.ktor.client.request.put
import io.ktor.client.request.setBody
import io.ktor.http.ContentType
import io.ktor.http.HttpStatusCode
import io.ktor.http.contentType
import io.ktor.http.encodeURLPath

class TaskAPIService(
    private val baseUrl: String = "http://10.0.2.2:8080",
    private val httpClient: HttpClient
) {

    suspend fun loadTask(name: String): List<TaskResponse> {
        return try {
            httpClient.get("$baseUrl/load_tasks_by_name/{$name}").body()
        } catch (e: Exception) {
            throw e
            emptyList()
        }
    }

    suspend fun getTasksByEmail(email: String): List<TaskResponse> {
        return try {
            val response = httpClient.get("$baseUrl/tasks_of_user/$email"){
                accept(ContentType.Application.Json)
            }
            when(response.status) {
                HttpStatusCode.OK -> response.body()
                HttpStatusCode.NotFound -> emptyList()
                else -> throw RuntimeException("Falha na requisição: ${response.status}")
            }
        } catch (e: Exception) {
            throw e
            emptyList()
        }
    }

    suspend fun getTasksByName(email: String, name: String): List<TaskResponse> {
        return try {
            val response = httpClient.get("$baseUrl/tasks/${email.encodeURLPath()}/${name.encodeURLPath()}") {
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

    suspend fun createTask(task: TaskRequest) {
        try {
            val response = httpClient.post("$baseUrl/task") {
                contentType(ContentType.Application.Json)
                setBody(task)
            }
            return response.body()
        } catch (e: Exception) {
            throw e
        }

    }

    suspend fun updateTask(task: TaskResponse) {
        try {
            val response = httpClient.put("$baseUrl/task") {
                contentType(ContentType.Application.Json)
                setBody(task)
            }
        } catch (e: Exception) {
            throw e
        }
    }

    suspend fun deleteTask(id: String) {
        try {
            httpClient.delete("$baseUrl/task/$id")
        } catch (e: Exception) {
            throw IllegalArgumentException("Erro ao tentar excluir tarefa:{ $id }" +
                    "erro: $e")

        }
    }

}