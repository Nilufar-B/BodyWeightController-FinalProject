package com.example.final_projectxml.api

//create data class to hold the response from the API
data class AdviceResponse(
val slip: Slip
)

data class Slip(
  val id: Int,
  val advice: String
)