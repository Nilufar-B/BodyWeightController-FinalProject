package com.example.final_projectxml.api

import androidx.lifecycle.ViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import retrofit2.*
import retrofit2.converter.gson.GsonConverterFactory


class AdviceViewModel : ViewModel() {

    private val _adviceState = MutableStateFlow<String?>(null)
    val randomAdvice: StateFlow<String?> = _adviceState.asStateFlow()

       fun loadRandomAdvice() {

            val retrofit = Retrofit.Builder()
                  .baseUrl("https://api.adviceslip.com/")
                  .addConverterFactory(GsonConverterFactory.create())
                  .build()


         val randomAdviceApi = retrofit.create<AdviceApi>().getRandomAdvice()

             randomAdviceApi.enqueue(object : Callback<AdviceResponse>{
                 override fun onResponse(
                     call: Call<AdviceResponse>,
                     response: Response<AdviceResponse>
                 ) {
                     if (response.isSuccessful){
                         val advice = response.body()?.slip?.advice
                         if (advice != null){
                             _adviceState.value = randomAdvice.value.toString()
                         }
                     }else{
                         println("Error")
                     }
                 }

                 override fun onFailure(call: Call<AdviceResponse>, t: Throwable) {
                     println(t.printStackTrace())
                 }

             })

    }



}