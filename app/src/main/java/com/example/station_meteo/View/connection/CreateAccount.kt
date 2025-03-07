package com.example.station_meteo.View.connection

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
import androidx.navigation.NavController
import com.example.football4u.ViewModel.Login.CreateAccountState
import com.example.football4u.ViewModel.Login.CreateAccountViewModel

@Composable
fun CreateAccount(
    navController: NavController,
    viewModel: CreateAccountViewModel = viewModel()
) {
    val uiState by viewModel.uiState.collectAsState()
    var email by remember { mutableStateOf("") }
    var password by remember { mutableStateOf("") }
    var confirmPassword by remember { mutableStateOf("") }

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
                text = "Create Account",
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
                value = email,
                onValueChange = { email = it },
                placeholder = { Text(text = "Email") },
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

            OutlinedTextField(
                value = confirmPassword,
                onValueChange = { confirmPassword = it },
                placeholder = { Text(text = "Confirm password") },
                modifier = Modifier
                    .fillMaxWidth()
                    .background(Color(0xFFEEF2FF), shape = MaterialTheme.shapes.medium),
                keyboardOptions = KeyboardOptions.Default.copy(keyboardType = KeyboardType.Password),
                visualTransformation = PasswordVisualTransformation()
            )

            Button(
                onClick = {
                    if (password == confirmPassword) {
                        viewModel.createAccount(email, password)
                    }
                },
                colors = ButtonDefaults.buttonColors(containerColor = Color(0xFFF8B4B4)),
                modifier = Modifier.fillMaxWidth()
            ) {
                Text(text = "Create account", color = Color.Black)
            }

            TextButton(onClick = { navController.popBackStack() }) {
                Text(
                    text = "Back to login",
                    style = MaterialTheme.typography.bodyMedium.copy(
                        textDecoration = TextDecoration.Underline
                    ),
                    color = Color.Black
                )
            }

            when (uiState) {
                is CreateAccountState.Loading -> CircularProgressIndicator(
                    modifier = Modifier.padding(top = 16.dp)
                )

                is CreateAccountState.Success -> {
                    LaunchedEffect(Unit) {
                        navController.navigate("bottom_bar") {
                            popUpTo("create_account") { inclusive = true }
                        }
                    }
                }

                is CreateAccountState.Error -> Text(
                    text = (uiState as CreateAccountState.Error).message,
                    color = Color.Red,
                    modifier = Modifier.padding(top = 16.dp)
                )

                else -> {}
            }

        }
    }
}

