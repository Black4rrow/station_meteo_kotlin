package com.example.station_meteo

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.runtime.Composable
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.example.station_meteo.View.BottomBar
import com.example.station_meteo.View.connection.Account
import com.example.station_meteo.View.connection.CreateAccount
import com.example.station_meteo.View.connection.Login
import com.google.firebase.FirebaseApp

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        FirebaseApp.initializeApp(this)
        setContent {
            AppNavigation()
        }
    }
}


@Composable
fun AppNavigation() {
    val navController = rememberNavController()

    NavHost(navController = navController, startDestination = "Bottom_bar") {
        composable("bottom_bar") {
            BottomBar(navController = navController)
        }
        composable("login") {
            Login(navController = navController)
        }
        composable("account") {
            Account(navController = navController)
        }
        composable("create_account") {
            CreateAccount(navController = navController)
        }
    }
}