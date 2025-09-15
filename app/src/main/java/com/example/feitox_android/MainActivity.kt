package com.example.feitox_android

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.navigation.compose.rememberNavController
import com.example.feitox_android.presentation.navigation.NavGraph
import com.example.feitox_android.presentation.theme.Minhastarefas_androidTheme


class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            Minhastarefas_androidTheme {
                val navController = rememberNavController()
                NavGraph(navController = navController)
            }
        }
    }
}



