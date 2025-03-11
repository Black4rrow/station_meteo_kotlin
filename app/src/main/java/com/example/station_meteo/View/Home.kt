package com.example.station_meteo.View

import androidx.compose.foundation.Canvas
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.StrokeCap
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import com.example.station_meteo.ViewModel.DatabaseViewModel
import com.example.station_meteo.model.WeatherReport
import kotlin.math.cos
import kotlin.math.sin

@Composable
fun Home(navController: NavController, databaseViewModel: DatabaseViewModel = viewModel()) {
    val progressColors = listOf(
        Color(0xFFFF0000),
        Color(0xFFFFA500),
        Color(0xFFFFFF00),
        Color(0xFF00FF00),
    )
    val weatherReports = databaseViewModel.weatherReports.observeAsState(initial = emptyList())
    Text("Home")
    LazyColumn {
        items(weatherReports.value) { report ->
            WeatherReportItem(report)
        }
    }
    Column(
        Modifier.fillMaxSize(),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Box(
            modifier = Modifier.size(200.dp)
        ) {
            Gauge(
                value = 50f,
                trackColor = Color.Gray,
                progressColors = progressColors,
                Color.Black
            )

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

@Composable
fun Gauge(value: Float, trackColor: Color, progressColors: List<Color>, needle: Color) {
    Canvas(modifier = Modifier.fillMaxSize()) {
        val sweepAngle = 240f
        val fillSwipeAngle = (value / 100f) * sweepAngle
        val height = size.height
        val width = size.width
        val startAngle = 150f
        val arcHeight = height - 20.dp.toPx()

        drawArc(
            color = trackColor,
            startAngle = startAngle,
            sweepAngle = sweepAngle,
            useCenter = false,
            topLeft = Offset((width - height + 60f) / 2f, (height - arcHeight) / 2f),
            size = Size(arcHeight, arcHeight),
            style = Stroke(width = 50f, cap = StrokeCap.Round)
        )

        drawArc(
            brush = Brush.horizontalGradient(progressColors),
            startAngle = startAngle,
            sweepAngle = fillSwipeAngle,
            useCenter = false,
            topLeft = Offset((width - height + 60f) / 2f, (height - arcHeight) / 2),
            size = Size(arcHeight, arcHeight),
            style = Stroke(width = 50f, cap = StrokeCap.Round)
        )
        val centerOffset = Offset(width / 2f, height / 2.09f)
        drawCircle(needle, 24f, centerOffset)

        val needleAngle = (value / 100f) * sweepAngle + startAngle
        val needleLength = 160f
        val needleBaseWidth = 10f

        val needlePath = androidx.compose.ui.graphics.Path().apply {
            val topX = centerOffset.x + needleLength * cos(
                Math.toRadians(needleAngle.toDouble()).toFloat()
            )
            val topY = centerOffset.y + needleLength * sin(
                Math.toRadians(needleAngle.toDouble()).toFloat()
            )

            val baseLeftX = centerOffset.x + needleBaseWidth * cos(
                Math.toRadians((needleAngle - 90).toDouble()).toFloat()
            )
            val baseLeftY = centerOffset.y + needleBaseWidth * sin(
                Math.toRadians((needleAngle - 90).toDouble()).toFloat()
            )
            val baseRightX = centerOffset.x + needleBaseWidth * cos(
                Math.toRadians((needleAngle + 90).toDouble()).toFloat()
            )
            val baseRightY = centerOffset.y + needleBaseWidth * sin(
                Math.toRadians((needleAngle + 90).toDouble()).toFloat()
            )

            moveTo(topX, topY)
            lineTo(baseLeftX, baseLeftY)
            lineTo(baseRightX, baseRightY)
            close()
        }

        drawPath(
            color = needle,
            path = needlePath
        )
    }
}
