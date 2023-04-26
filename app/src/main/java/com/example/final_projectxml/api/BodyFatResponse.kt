package com.example.final_projectxml.api

import com.google.gson.annotations.SerializedName

//create data class to hold the response from the API
data class BodyFatResponse (val info: BodyFatInfo)

data class BodyFatInfo(
    val bfp: Double,
    val fat_mass: Double,
    val lean_mass: Double,
    val description: String,
    val gender: String
)
