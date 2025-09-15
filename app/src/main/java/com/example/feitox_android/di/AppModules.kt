package com.example.feitox_android.di

import com.example.feitox_android.presentation.viewmodel.AuthViewModel
import com.example.feitox_android.repository.service.TaskAPIService
import com.example.feitox_android.presentation.viewmodel.TaskViewModel
import com.example.feitox_android.presentation.viewmodel.UserViewModel
import com.example.feitox_android.repository.service.UserAPIService
import io.ktor.client.HttpClient
import io.ktor.client.engine.android.Android
import io.ktor.client.plugins.contentnegotiation.ContentNegotiation
import io.ktor.client.plugins.logging.LogLevel
import io.ktor.client.plugins.logging.Logging
import io.ktor.serialization.kotlinx.json.json
import kotlinx.serialization.json.Json
import org.koin.core.module.dsl.viewModelOf
import org.koin.dsl.module

val appModule = module {
    viewModelOf(::TaskViewModel)
    viewModelOf(::AuthViewModel)
    viewModelOf(::UserViewModel)
}

val networkModule = module {
    single {
        HttpClient(Android) {
            install(Logging) {
                level = LogLevel.ALL
            }
            install(ContentNegotiation) {
                json(Json {
                    ignoreUnknownKeys = true
                })
            }
            engine { // tratar o erro na UX
                connectTimeout = 30_000 //min
                socketTimeout = 30_000 // max
            }
        }
    }

    single<TaskAPIService> {
        TaskAPIService(
            baseUrl = "http://10.0.2.2:8080",
            httpClient = get()
        )
    }
    single<UserAPIService> {
        UserAPIService(
            baseUrl = "http://10.0.2.2:8080",
            httpClient = get()
        )
    }
}

