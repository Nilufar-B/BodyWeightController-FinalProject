package com.example.final_projectxml.weatherApi

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel

class ViewModel : ViewModel() {
    val liveDataCurrent = MutableLiveData<WeatherModel>()

}