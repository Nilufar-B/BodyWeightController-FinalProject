package com.example.final_projectxml.screens

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch


class ViewModelBMI : ViewModel() {

    private val _bmiState = MutableStateFlow(0f)
    val bmiState: StateFlow<Float> = _bmiState

     fun calculateBMI(heightValue: Float, weightValue: Float) = viewModelScope.launch{
         val heightInMeters = heightValue / 100
         val bmi = weightValue / (heightInMeters * heightInMeters)
         _bmiState.value = bmi
     }
}


