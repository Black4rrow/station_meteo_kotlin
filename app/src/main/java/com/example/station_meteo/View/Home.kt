package com.example.station_meteo.View

import android.Manifest
import android.annotation.SuppressLint
import android.os.Build
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.annotation.RequiresApi
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.Image
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
import androidx.compose.material3.Button
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.draw.clip
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.RectangleShape
import androidx.compose.ui.graphics.StrokeCap
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.graphics.painter.Painter
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import com.example.station_meteo.R
import com.example.station_meteo.ViewModel.DatabaseViewModel
import com.example.station_meteo.ViewModel.NotificationViewModel
import kotlinx.coroutines.delay
import kotlin.math.cos
import kotlin.math.sin

@SuppressLint("NewApi")
@Composable
fun Home(navController: NavController, databaseViewModel: DatabaseViewModel = viewModel(),notificationViewModel: NotificationViewModel = viewModel()) {
    val progressColors = listOf(
        Color(0xFFADD8E6),
        Color(0xFF87CEEB),
        Color(0xFF4682B4),
        Color(0xFF0000FF),
        Color(0xFF00008B),
    )
    val weatherReports = databaseViewModel.weatherReports.observeAsState(initial = emptyList())

    val latestHumidity = weatherReports.value.maxByOrNull { it.reportDate }?.humidity ?: 0.0f
    val latestTemperature = weatherReports.value.maxByOrNull { it.reportDate }?.temperature ?: 0.0f
    val latestCo2 = weatherReports.value.maxByOrNull { it.reportDate }?.co2 ?: 0.0f


    Column(
        Modifier
            .fillMaxSize()
            .padding(top = 16.dp, bottom = 80.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Spacer(modifier = Modifier.size(100.dp))
        Box(
            modifier = Modifier.size(200.dp)
        ) {
            Gauge(
                value = latestHumidity,
                trackColor = Color.Gray,
                progressColors = progressColors,
                Color.Black
            )

        }
        Row(Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceBetween) {

            Column(
                modifier = Modifier
                    .padding(start = 16.dp),
                horizontalAlignment = Alignment.CenterHorizontally
            )
            {
                Image(
                    painter = painterResource(id = R.drawable.co2cloud),
                    contentDescription = "Co2 Cloud Image",
                    modifier = Modifier.size(128.dp)
                )
                Text(
                    text = "${latestCo2.toInt()} PPM",
                    fontSize = 32.sp,
                    lineHeight = 24.sp,
                    color = Color.Black
                )
            }

            Row(
                modifier = Modifier
                    .padding(top = 32.dp, end = 16.dp),
                verticalAlignment = Alignment.CenterVertically
            )
            {
                Image(
                    painter = painterResource(id = R.drawable.thermometer),
                    contentDescription = "thermometer Image",
                    contentScale = ContentScale.Fit,
                    modifier = Modifier
                        .width(60.dp)
                        .height(128.dp)
                        .clip(RectangleShape)
                )
                Text(
                    text = "${"%.1f".format(latestTemperature)} °C",
                    fontSize = 32.sp,
                    lineHeight = 24.sp,
                    color = Color.Black
                )

            }

        }
        Box() {
            if (latestCo2 > 6000)
                BlinkingImage(painterResource(id = R.drawable.alert))
            MonitorCO2(latestCo2.toInt(),notificationViewModel,"Alert !","Warning, please leave the room.")
        }
    }
}


@Composable
fun Gauge(value: Float, trackColor: Color, progressColors: List<Color>, needle: Color) {
    Box(
        modifier = Modifier
            .padding(bottom = 5.dp),
        Alignment.BottomCenter
    )
    {
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
        Column(
            modifier = Modifier
                .padding(bottom = 5.dp),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center
        ) {
            Text(
                text = "${"%.1f".format(value)} %",
                fontSize = 32.sp,
                lineHeight = 28.sp,
                color = needle
            )
            Text(
                text = "Humidity",
                fontSize = 16.sp,
                lineHeight = 24.sp,
                color = Color.Black
            )
        }
    }
}

@Composable
fun BlinkingImage(painter: Painter) {
    var alpha by remember { mutableStateOf(1f) }

    LaunchedEffect(Unit) {
        while (true) {
            alpha = 0.2f
            delay(500)
            alpha = 1f
            delay(500)
        }
    }
    Column(horizontalAlignment = Alignment.CenterHorizontally) {
        Image(
            painter = painter,
            contentDescription = "Blinking Image",
            modifier = Modifier
                .size(128.dp)
                .alpha(alpha)
        )
        Text(
            text = "Alert !",
            fontSize = 32.sp,
            lineHeight = 24.sp,
            color = Color.Red.copy(alpha = alpha)
        )
    }

}


@RequiresApi(Build.VERSION_CODES.TIRAMISU)
@Composable
fun MonitorCO2(co2Value: Int,notificationViewModel : NotificationViewModel,title: String,message: String) {
    val context = LocalContext.current
    var hasPermission by remember { mutableStateOf(notificationViewModel.checkNotificationPermission(context)) }
    var notificationSent by remember { mutableStateOf(false) }

    val requestPermissionLauncher =
        rememberLauncherForActivityResult(ActivityResultContracts.RequestPermission()) { isGranted ->
            hasPermission = isGranted
        }

    LaunchedEffect(co2Value) {
        if (co2Value > 6000 && !notificationSent) {
            if (hasPermission) {
                notificationViewModel.showNotification(context,title,message)
                notificationSent = true
            } else {
                requestPermissionLauncher.launch(Manifest.permission.POST_NOTIFICATIONS)
            }
        } else if (co2Value <= 6000) {
            notificationSent = false
        }
    }
}
