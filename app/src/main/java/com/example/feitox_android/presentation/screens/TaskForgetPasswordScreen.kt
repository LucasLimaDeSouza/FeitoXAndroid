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
import com.example.feitox_android.presentation.uistate.UserUiState
import com.example.feitox_android.presentation.viewmodel.AuthState
import com.example.feitox_android.presentation.viewmodel.AuthViewModel
import com.example.feitox_android.presentation.viewmodel.UserViewModel
import com.example.feitox_android.repository.model.UserModel.UserRequest
import kotlinx.coroutines.launch
import org.koin.compose.viewmodel.koinViewModel

@Composable
fun TaskForgetPasswordScreen(
    authViewModel: AuthViewModel = koinViewModel<AuthViewModel>(),
    userViewModel: UserViewModel = koinViewModel<UserViewModel>(),
    navController: NavHostController
) {
    val authResultValue by authViewModel.authReturn.collectAsState()
    val authReturnUsers by userViewModel.users.collectAsState()
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
                when (authReturnUsers) {
                    is UserUiState.Succses -> {
                        val message = (authReturnUsers as UserUiState.Send).msg
                        Text(
                            message,
                            color = Color.White,
                            fontSize = 25.sp,
                            fontWeight = FontWeight.Bold
                        )
                    }

                    is UserUiState.Error -> {
                        val message = (authReturnUsers as UserUiState.Send).msg
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
                "Digite o email para receber o link",
                fontWeight = FontWeight.Medium,
                fontSize = 18.sp,
                color = Purple40
            )
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
                onClick = {
                    coroutineScope.launch {
                        userViewModel.requestPasswordWithEmail(email)
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
                                "Enviar",
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