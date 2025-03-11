package com.example.station_meteo.ViewModel

import androidx.lifecycle.ViewModel
import com.google.firebase.auth.FirebaseAuth


class UserViewModel : ViewModel() {

    private val firebaseAuth: FirebaseAuth = FirebaseAuth.getInstance()

    fun checkIfUserIsConnected(): Boolean {
        val currentUser = firebaseAuth.currentUser
        if (currentUser != null) {
            return true
        }
        return false

    }

    fun getUserMail(): String? {
        val currentUser = firebaseAuth.currentUser
        if (currentUser != null) {
            return currentUser.email
        }
        return ""
    }
    fun transformMailInName(mail: String): String {
        val atIndex = mail.indexOf('@')
        return if (atIndex != -1) {
            val name = mail.substring(0, atIndex)
            name.replaceFirstChar { it.uppercase() }
        } else {
            mail
        }
    }
}
