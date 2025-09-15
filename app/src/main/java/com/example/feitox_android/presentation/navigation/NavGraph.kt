package com.example.feitox_android.presentation.navigation

import androidx.compose.runtime.Composable
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import com.example.feitox_android.presentation.screens.TaskCreateCountAuthScreen
import com.example.feitox_android.presentation.screens.TaskForgetPasswordScreen
import com.example.feitox_android.presentation.screens.TaskLoginAuthScreen
import com.example.feitox_android.presentation.screens.TasksScreen


@Composable
fun NavGraph(navController: NavHostController) {
    NavHost(
        navController = navController,
        startDestination = Screen.TaskLoginScreen.route
    ) {
        composable(route = Screen.TaskLoginScreen.route) {
            TaskLoginAuthScreen(navController = navController)
        }

        composable(route = Screen.TaskCreateCountScreen.route) {
            TaskCreateCountAuthScreen(
                navController = navController
            )
        }
        composable(route = Screen.TaskForgetPasswordScreen.route) {
            TaskForgetPasswordScreen(
                navController = navController
            )
        }

        composable(
            route = Screen.TaskScreen.route,
            arguments = Screen.TaskScreen.arguments
        ) { findArguments ->
            val userEmail = findArguments.arguments?.getString("email").orEmpty()
            TasksScreen(
                userEmail = userEmail,
                navController = navController,
            )
        }
    }
}