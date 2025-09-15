package com.example.feitox_android.presentation.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
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
import com.example.feitox_android.presentation.viewmodel.UserViewModel
import com.example.feitox_android.repository.model.UserModel.UserRequest
import kotlinx.coroutines.launch
import org.koin.compose.viewmodel.koinViewModel


@Composable
fun TaskCreateCountAuthScreen(
    authViewModel: AuthViewModel = koinViewModel<AuthViewModel>(),
    userViewModel: UserViewModel = koinViewModel<UserViewModel>(),
    navController: NavHostController
) {
    val authResultValue by authViewModel.authReturn.collectAsState()
    val authReturn by authViewModel.authReturn.collectAsState()
    val coroutineScope = rememberCoroutineScope()
    var email by remember { mutableStateOf("") }
    var password by remember { mutableStateOf("") }
    var passwordConfirm by remember { mutableStateOf("") }
    var circularProgressIndicator by remember { mutableStateOf(false) }
    var alertCadaster by remember { mutableStateOf(false) }
    var alertAllValues by remember { mutableStateOf(false) }
    var alertNavigate by remember { mutableStateOf(false) }


    LaunchedEffect(authResultValue) {
        when(authResultValue) {
            is AuthState.Success -> {
                try {
                    navController.navigate(
                        Screen.TaskLoginScreen.route
                    )
                } catch (e: Exception) {
                    throw e
                    alertNavigate = true
                }
            }

            is AuthState.Loading -> {
                circularProgressIndicator = true
            }else -> {
                alertNavigate = false
            }
        }
    }

    fun createACount() {
        coroutineScope.launch {
            if (email != "" && password != "" && passwordConfirm != "") {
                if (password == passwordConfirm) {
                    authViewModel.createACountWithEmailAndPassword(
                        email,
                        password
                    )

                    userViewModel.saveUser(
                        UserRequest(
                            userEmail = email
                        )
                    )

                    alertCadaster = false
                } else {
                    alertCadaster = true
                }
                alertAllValues = false
            } else {
                alertAllValues = true
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
                when (authReturn) {
                    is AuthState.Success -> {
                        Text(
                            "FeitoX",
                            color = Color.White,
                            fontSize = 25.sp,
                            fontWeight = FontWeight.Bold
                        )
                    }

                    is AuthState.Error -> {
                        val message = (authReturn as AuthState.Error).message
                        Text(
                            message,
                            color = Color.White,
                            fontSize = 25.sp,
                            fontWeight = FontWeight.Bold
                        )
                    }

                    else -> {
                        Text(
                            "null",
                            color = Color.White,
                            fontSize = 25.sp,
                            fontWeight = FontWeight.Bold
                        )
                    }
                }
            }
        }

        Column(
            modifier = Modifier
                .fillMaxWidth(),
            verticalArrangement = Arrangement.Center,
            horizontalAlignment = Alignment.CenterHorizontally
        ) {

            Text(
                "Crie uma conta com o Email e Senha",
                fontWeight = FontWeight.Medium,
                fontSize = 18.sp,
                color = Purple40
            )
            if (alertCadaster) {
                Text(
                    "AS SENHAS PRECISAM SER IGUAIS",
                    fontWeight = FontWeight.Medium,
                    fontSize = 12.sp,
                    color = Color.Red
                )
            }
            if (alertNavigate) {
                Text(
                    "ERRO AO TENTAR NAVEGAR!!!",
                    fontWeight = FontWeight.Medium,
                    fontSize = 12.sp,
                    color = Color.Red
                )
            }

            if (alertAllValues) {
                Text(
                    "TODOS OS CAMPOS DEVEM SER PREENCHIDOS",
                    fontWeight = FontWeight.Medium,
                    fontSize = 12.sp,
                    color = Color.Red
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
            Spacer(modifier = Modifier.height(10.dp))
            OutLineInputSlot(
                modifier = Modifier
                    .padding(),
                label = {
                    Text("Confirme a Senha")
                },
                value = passwordConfirm,
                onChange = { passwordConfirm = it },
                isPassword = true
            )
            Spacer(modifier = Modifier.height(10.dp))

        }

        Column {
            ButtonSlot(
                onClick = {
                    navController.navigate(
                        Screen.TaskLoginScreen.route
                    )
                },
                modifier = Modifier
                    .height(80.dp)
                    .fillMaxWidth()
                    .clickable(
                        onClick = {
                            navController.navigate(
                                Screen.TaskLoginScreen.route
                            )
                        }
                    ),
                background = Purple40,
                text = {
                    Text("Voltar", fontSize = 18.sp)
                }
            )
            Spacer(modifier = Modifier.height(10.dp))
            ButtonSlot(
                onClick = { createACount() },
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
                                "Criar conta",
                                fontSize = 18.sp,
                                fontWeight = FontWeight.Medium,
                                color = Pink40
                            )
                        }
                    }
                }
            )
        }
    }
}

