package com.example.football4u.ViewModel.Login

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.google.firebase.auth.FirebaseAuth
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch

class CreateAccountViewModel : ViewModel() {
    private val auth: FirebaseAuth = FirebaseAuth.getInstance()

    private val _uiState = MutableStateFlow<CreateAccountState>(CreateAccountState.Idle)
    val uiState: StateFlow<CreateAccountState> = _uiState

    fun createAccount(email: String, password: String) {
        _uiState.value = CreateAccountState.Loading

        viewModelScope.launch {
            try {
                auth.createUserWithEmailAndPassword(email, password)
                    .addOnCompleteListener { task ->
                        if (task.isSuccessful) {
                            _uiState.value = CreateAccountState.Success
                        } else {
                            _uiState.value =
                                CreateAccountState.Error(task.exception?.message ?: "Unknown error")
                        }
                    }
            } catch (e: Exception) {
                _uiState.value = CreateAccountState.Error(e.message ?: "Unknown error")
            }
        }
    }
}

sealed class CreateAccountState {
    object Idle : CreateAccountState()
    object Loading : CreateAccountState()
    object Success : CreateAccountState()
    data class Error(val message: String) : CreateAccountState()
}
