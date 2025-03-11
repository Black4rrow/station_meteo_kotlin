package com.example.station_meteo.View

import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.livedata.observeAsState
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import com.example.station_meteo.ViewModel.DatabaseViewModel
import com.example.station_meteo.model.WeatherReport

@Composable
fun Home(navController: NavController, databaseViewModel: DatabaseViewModel = viewModel()) {

    val weatherReports = databaseViewModel.weatherReports.observeAsState(initial = emptyList())

    LazyColumn {
        items(weatherReports.value) { report ->
            WeatherReportItem(report)
        }
    }
}

@Composable
fun WeatherReportItem(report: WeatherReport) {
    Text(
        text = "Température : ${report.temperature}°C\n" +
                "Humidité : ${report.humidity}%\n" +
                "CO2 : ${report.co2} ppm\n" +
                "Date : ${report.reportDate}"
    )
}
