package com.example.station_meteo.ViewModel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.station_meteo.model.WeatherReport
import com.google.firebase.database.*

class DatabaseViewModel : ViewModel() {

    private val _weatherReports = MutableLiveData<List<WeatherReport>>()
    val weatherReports: LiveData<List<WeatherReport>> get() = _weatherReports

    private val databaseReference: DatabaseReference =
        FirebaseDatabase.getInstance().getReference("measure")

    init {
        databaseReference.addValueEventListener(object : ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                val reports = mutableListOf<WeatherReport>()
                for (child in snapshot.children) {
                    val report = child.getValue(WeatherReport::class.java)
                    if (report != null) {
                        reports.add(report)
                    }
                }
                _weatherReports.postValue(reports)
            }

            override fun onCancelled(error: DatabaseError) {
                _weatherReports.postValue(emptyList())
            }
        })
    }
}
