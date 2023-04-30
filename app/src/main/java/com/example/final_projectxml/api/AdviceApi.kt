package com.example.final_projectxml.api

import retrofit2.Call
import retrofit2.http.GET

interface AdviceApi {
    @GET("advice")
    fun getRandomAdvice(): Call<AdviceResponse>
}
