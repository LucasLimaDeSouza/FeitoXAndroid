package com.example.feitox_android.presentation.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.navigation.NavHostController
import com.example.feitox_android.R
import com.example.feitox_android.presentation.components.ButtonSlot
import com.example.feitox_android.presentation.components.OutLineInputSlot
import com.example.feitox_android.presentation.navigation.Screen
import com.example.feitox_android.presentation.theme.Pink40
import com.example.feitox_android.presentation.theme.Pink80
import com.example.feitox_android.presentation.theme.Purple40
import com.example.feitox_android.presentation.theme.Purple80
import com.example.feitox_android.presentation.viewmodel.AuthState
import com.example.feitox_android.presentation.viewmodel.AuthViewModel
import kotlinx.coroutines.launch
import org.koin.androidx.compose.koinViewModel


@Composable
fun TaskLoginAuthScreen(
    authViewModel: AuthViewModel = koinViewModel<AuthViewModel>(),
    navController: NavHostController
) {

    val isAuthenticated by authViewModel.isAuthenticatedFlow.collectAsStateWithLifecycle()
    val authReturn by authViewModel.authReturn.collectAsState()
    var circularProgressIndicator by remember { mutableStateOf(false) }
    var userEmail by remember { mutableStateOf("") }
    var email by remember { mutableStateOf("") }
    var password by remember { mutableStateOf("") }
    var alert by remember { mutableStateOf(false) }
    val coroutineScope = rememberCoroutineScope()

    LaunchedEffect(isAuthenticated) {
        val email = isAuthenticated.email
        if (isAuthenticated.isAuthenticated) {
            navController.navigate(
                Screen.TaskScreen.withArgs(
                    email = email
                )
            )
        }
    }


    LaunchedEffect(authReturn) {
        when (authReturn) {
            is AuthState.Idle -> {
                circularProgressIndicator = true
                userEmail = "Email do usuario"
                circularProgressIndicator = false
            }

            is AuthState.Loading -> {
                circularProgressIndicator = true
                userEmail = "Loading.."
            }

            is AuthState.Success -> {
                val email = (authReturn as AuthState.Success).email
                if (email.isEmpty()) { alert = true } else {
                    navController.navigate(
                        Screen.TaskScreen.withArgs(
                            email
                        )
                    )

                }
            }

            is AuthState.Error -> {
                val erro = (authReturn as AuthState.Error).message
                userEmail = erro
                alert = true
                circularProgressIndicator = false
            }
        }
    }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(Purple80)
            .padding(15.dp, 35.dp),
        verticalArrangement = Arrangement.SpaceBetween,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {

        Spacer(modifier = Modifier.width(10.dp))

        Box(
            modifier = Modifier
                .fillMaxWidth(),
            contentAlignment = Alignment.Center
        ) {
            Column(
                modifier = Modifier
                    .fillMaxWidth(),
                verticalArrangement = Arrangement.Center,
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                Icon(
                    modifier = Modifier
                        .size(200.dp),
                    painter = painterResource(id = R.drawable.outline_add_task_24),
                    contentDescription = "logo_google",
                    tint = Color.White
                )
                Text(
                    "FeitoX",
                    color = Color.White,
                    fontSize = 25.sp,
                    fontWeight = FontWeight.Bold
                )

            }
        }

        Column(
            modifier = Modifier
                .fillMaxWidth(),
            verticalArrangement = Arrangement.Center,
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            if (alert == true) {
                Text(
                    "Erro ao tentar fazer login",
                    fontSize = 18.sp,
                    fontWeight = FontWeight.Medium,
                    color = Pink40
                )
            } else {

                Text(
                    "Faça login com o E-mail e Senha",
                    fontSize = 18.sp,
                    fontWeight = FontWeight.Medium,
                    color = Color.White
                )
            }


            Spacer(modifier = Modifier.height(10.dp))
            OutLineInputSlot(
                modifier = Modifier
                    .padding(),
                label = {
                    Text("Email")
                },
                value = email,
                onChange = { email = it },
                isPassword = false
            )
            Spacer(modifier = Modifier.height(10.dp))
            OutLineInputSlot(
                modifier = Modifier
                    .padding(),
                label = {
                    Text("Senha")
                },
                value = password,
                onChange = { password = it },
                isPassword = true
            )
            Spacer(modifier = Modifier.height(20.dp))
            Row(
                modifier = Modifier
                    .fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceEvenly
            ) {

                Text(
                    "recuperar senha",
                    modifier = Modifier
                        .clickable(
                            onClick = {
                                navController.navigate(
                                    Screen.TaskForgetPasswordScreen.route
                                )
                            }
                        ),
                    fontSize = 18.sp,
                    fontWeight = FontWeight.Medium,
                    color = Purple40
                )

                Text(
                    "criar conta",
                    modifier = Modifier
                        .clickable(
                            onClick = {
                                navController.navigate(
                                    Screen.TaskCreateCountScreen.route
                                )
                            }
                        ),
                    fontSize = 18.sp,
                    fontWeight = FontWeight.Medium,
                    color = Purple40
                )
            }
        }

        ButtonSlot(
            onClick = {
                coroutineScope.launch {
                    if (email != "" || password != "") {
                        alert = false
                        authViewModel.signIn(email, password)
                    } else {
                        alert = true
                    }
                }
            },
            modifier = Modifier
                .height(80.dp)
                .fillMaxWidth(),
            background = Pink80,
            text = {
                when (circularProgressIndicator == true) {
                    true -> {
                        CircularProgressIndicator()
                    }

                    false -> {
                        Text(
                            "Fazer Login",
                            fontSize = 18.sp,
                            fontWeight = FontWeight.Medium,
                            color = Color.White
                        )
                    }
                }
            }
        )
    }
}