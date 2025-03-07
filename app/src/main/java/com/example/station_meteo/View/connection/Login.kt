package com.example.station_meteo.View.connection

import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.navigation.NavController
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.style.TextDecoration
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.football4u.ViewModel.Login.LoginState
import com.example.football4u.ViewModel.Login.LoginViewModel


@Composable
fun Login(navController: NavController, viewModel: LoginViewModel = viewModel()) {
    val uiState by viewModel.uiState.collectAsState()
    var username by remember { mutableStateOf("") }
    var password by remember { mutableStateOf("") }

    Box(
        modifier = Modifier
            .fillMaxSize(),
        contentAlignment = Alignment.Center
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            Text(
                text = "Login",
                style = MaterialTheme.typography.displaySmall.copy(
                    fontWeight = FontWeight.Bold,
                    fontSize = 30.sp,
                )
            )

            Text(
                text = "Who are you ?",
                style = MaterialTheme.typography.headlineSmall.copy(
                    fontWeight = FontWeight.Bold,
                    fontSize = 20.sp
                )
            )

            OutlinedTextField(
                value = username,
                onValueChange = { username = it },
                placeholder = { Text(text = "Mail of your account") },
                modifier = Modifier
                    .fillMaxWidth()
                    .background(Color(0xFFEEF2FF), shape = MaterialTheme.shapes.medium),
                keyboardOptions = KeyboardOptions.Default.copy(keyboardType = KeyboardType.Email)
            )

            OutlinedTextField(
                value = password,
                onValueChange = { password = it },
                placeholder = { Text(text = "Password") },
                modifier = Modifier
                    .fillMaxWidth()
                    .background(Color(0xFFEEF2FF), shape = MaterialTheme.shapes.medium),
                keyboardOptions = KeyboardOptions.Default.copy(keyboardType = KeyboardType.Password),
                visualTransformation = PasswordVisualTransformation()
            )

            Button(
                onClick = { viewModel.login(username, password) },
                colors = ButtonDefaults.buttonColors(containerColor = Color(0xFFF8B4B4)),
                modifier = Modifier.fillMaxWidth()
            ) {
                Text(text = "Validate", color = Color.Black)
            }

            TextButton(onClick = { navController.navigate("create_account") }) {
                Text(
                    text = "Create account",
                    style = MaterialTheme.typography.bodyMedium.copy(
                        textDecoration = TextDecoration.Underline
                    ),
                    color = Color.Black
                )
            }

            when (uiState) {
                is LoginState.Loading -> {
                    CircularProgressIndicator()
                }

                is LoginState.Error -> {
                    Text(
                        text = (uiState as LoginState.Error).message,
                        color = Color.Red,
                        style = MaterialTheme.typography.bodyMedium
                    )
                }

                is LoginState.Success -> {
                    LaunchedEffect(Unit) {
                        navController.navigate("bottom_bar") {
                            popUpTo("login") { inclusive = true }
                        }
                    }
                    Text(
                        text = "Successful connection !",
                        color = Color.Green,
                        style = MaterialTheme.typography.bodyMedium
                    )
                }

                else -> Unit
            }
        }
    }
}