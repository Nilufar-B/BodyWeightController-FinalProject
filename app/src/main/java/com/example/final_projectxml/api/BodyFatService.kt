package com.example.final_projectxml.api

import retrofit2.Call
import retrofit2.http.GET
import retrofit2.http.Query

interface BodyFatService {
    @GET("/get+body+fat+percentage")
     fun getBodyFatPercentage(
        @Query("weight") weight: String,
        @Query("height") height: String,
        @Query("gender") gender: String,
        @Query("age") age: String
 ):
            Call<BodyFatResponse>
}
