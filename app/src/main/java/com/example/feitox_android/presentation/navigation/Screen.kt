package com.example.feitox_android.presentation.navigation

import androidx.navigation.NamedNavArgument
import androidx.navigation.NavType
import androidx.navigation.navArgument

sealed class Screen(val route: String, val arguments: List<NamedNavArgument> = emptyList()) {

    object TaskLoginScreen : Screen(route = "task_login_screen")

    object TaskCreateCountScreen : Screen(route = "task_create_count_screen")

    object TaskForgetPasswordScreen : Screen(route = "task_forget_password_screen")

    object TaskScreen : Screen(
        route = "task_screen/{email}",
        arguments = listOf(
            navArgument("email") { type = NavType.StringType }
        )
    ) {
        fun withArgs(
            email: String?
        ): String {
            return "task_screen/${email}"
        }
    }
}