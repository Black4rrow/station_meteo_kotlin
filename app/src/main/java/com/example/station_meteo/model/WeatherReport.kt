package com.example.station_meteo.model

import com.google.firebase.Timestamp
import com.google.firebase.database.PropertyName
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

data class WeatherReport (
    val co2: Float = 0.0f,
    val gas: Float = 0.0f,
    val humidity: Float = 0.0f,
    @get:PropertyName("light_level") @set:PropertyName("light_level")
    var lightLevel: Float = 0.0f,
    val pressure: Float = 0.0f,
    val reportDate: Long = 0L,
    val temperature: Float = 0.0f,
    val userID: String = "",
){
    constructor() : this(0.0f, 0.0f, 0.0f, 0.0f, 0.0f, 0L, 0.0f, "")
}

fun formatDate(timestamp: Long): String {
    val date = Date(timestamp * 1000)

    val sdfTime = SimpleDateFormat("HH:mm", Locale.getDefault())
    val sdfDateTime = SimpleDateFormat("dd MMM yyyy HH:mm", Locale.getDefault())

    val isToday = android.text.format.DateUtils.isToday(date.time)

    return if (isToday) {
        "Today ${sdfTime.format(date)}"
    } else {
        sdfDateTime.format(date)
    }
}