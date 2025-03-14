package com.example.station_meteo.View

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import com.example.station_meteo.ViewModel.DatabaseViewModel
import com.example.station_meteo.model.WeatherReport
import io.github.dautovicharis.charts.LineChart
import io.github.dautovicharis.charts.model.toChartDataSet


@Composable
fun Result(navController: NavController, databaseViewModel: DatabaseViewModel = viewModel()) {
    val weatherReports = databaseViewModel.weatherReports.observeAsState(initial = emptyList())

    Column(
        Modifier
            .fillMaxSize()
            .padding(bottom = 80.dp, start = 8.dp, end = 8.dp)
            .verticalScroll(rememberScrollState()),
    ) {
        /*
        LazyColumn {
            items(weatherReports.value) { report ->
                WeatherReportItem(report)
            }
        }

         */
        Spacer(modifier = Modifier.size(16.dp))

        Text("History of measures",  fontSize = 32.sp,)

        Spacer(modifier = Modifier.size(16.dp))

        Text("Last 6 min",  fontSize = 24.sp,modifier = Modifier.padding(start = 24.dp))

        AddDefaultLineChart(weatherReports.value.map { (Math.round(it.temperature * 100) / 100f) }
            , "Temperature"," °C",40)

        AddDefaultLineChart(weatherReports.value.map { (Math.round(it.co2 * 100) / 100f) }
            , "CO2"," PPM",40)

        AddDefaultLineChart(weatherReports.value.map { (Math.round(it.humidity * 100) / 100f) }
            , "Humidity"," %",40)

        AddDefaultLineChart(weatherReports.value.map { (Math.round(it.lightLevel)).toFloat() }
            , "Light"," lux",40)

        AddDefaultLineChart(weatherReports.value.map { (Math.round(it.gas)).toFloat() }
            , "Gas"," jsp",40)

        AddDefaultLineChart(weatherReports.value.map { (Math.round(it.pressure)).toFloat() }
            , "Pressure"," jsp",40)
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

/*
val chartEntries = data.mapIndexed { index, value ->
}
 */
@Composable
private fun AddDefaultLineChart(list: List<Float>, title: String,postfix: String, nb: Int) {
    val dataSet = list.takeLast(nb).toChartDataSet(
        title = title,
        postfix = postfix
    )
    LineChart(dataSet)
}