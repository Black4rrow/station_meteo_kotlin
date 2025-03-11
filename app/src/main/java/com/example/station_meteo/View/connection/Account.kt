package com.example.station_meteo.View.connection

import androidx.compose.foundation.Image
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
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import com.example.station_meteo.R

import com.example.station_meteo.ViewModel.UserViewModel
import com.google.firebase.auth.FirebaseAuth

@Composable
fun Account(navController: NavController) {

    val  userViewModel: UserViewModel = viewModel()
    val name = userViewModel.getUserMail()?.let { userViewModel.transformMailInName(it) }

    Box(
        modifier = Modifier
            .verticalScroll(rememberScrollState())
            .fillMaxSize()
            .background(Color(0xFFF5F5F5))
    ) {
        Column(
            modifier = Modifier
                .padding(top = 16.dp, start = 16.dp, end = 16.dp, bottom = 128.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(vertical = 16.dp),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(
                    text = "Account",
                    style = TextStyle(fontSize = 32.sp),
                    color = Color.Black
                )

            }



            if (name != null) {
                Text(text = name , style = TextStyle(fontSize = 24.sp))
            }
            TextButton(onClick = {
                FirebaseAuth.getInstance().signOut()
                navController.navigate("bottom_bar")
            }) {
                Text(text = "Logout")
            }

            Spacer(modifier = Modifier.height(24.dp))

            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 2.dp)
            ) {
                userViewModel.getUserMail()
                    ?.let { AccountInfoCard(label = "Mail", value = it, showMailIcon = true) }
                Spacer(modifier = Modifier.height(8.dp))
                if (name != null) {
                    AccountInfoCard(label = "Name of account", value = name, showMailIcon = false)
                }
                Spacer(modifier = Modifier.height(8.dp))
                AccountInfoCard(label = "Mot de passe", value = "*************", showMailIcon = false)
            }
        }
    }
}


@Composable
fun AccountInfoCard(label: String, value: String, showMailIcon: Boolean) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .background(Color(0xFFE6E0E9), RoundedCornerShape(8.dp))
            .padding(8.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        if (showMailIcon) {
            Icon(
                painter = painterResource(id = R.drawable.mail),
                contentDescription = null,
                modifier = Modifier.size(24.dp)
            )
            Spacer(modifier = Modifier.width(8.dp))
        }

        Text(
            text = "$label : $value",
            style = TextStyle(fontSize = 16.sp, color = Color.Black),
            modifier = Modifier.weight(1f)
        )
        IconButton(onClick = { /* Action sur le delete */ }) {
            Icon(
                painter = painterResource(id = R.drawable.delete),
                contentDescription = null
            )
        }
    }
}