package com.example.final_projectxml.screens

import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import com.example.final_projectxml.api.BodyFatResponse
import com.example.final_projectxml.api.BodyFatService
import com.example.final_projectxml.databinding.FragmentNavycalcBinding
import retrofit2.*
import retrofit2.converter.gson.GsonConverterFactory

class NavyCalc : Fragment() {

    private var _binding: FragmentNavycalcBinding? = null
    private val binding get() = _binding!!

    private val apiBaseUrl = "https://zylalabs.com/api/428/fitness+calculator+api/330/"


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        _binding = FragmentNavycalcBinding.inflate(inflater, container, false)
       val view = binding.root


        binding.btnCalculate.setOnClickListener{

            val weight = binding.etWeight.text.toString()
            val height = binding.etHeight.text.toString()
            val gender = if (binding.maleRadioBtn.isChecked) "male" else "female"
            val age = binding.etAge.text.toString()


            val retrofit = Retrofit.Builder()
                .baseUrl(apiBaseUrl)
                .addConverterFactory(GsonConverterFactory.create())
                .build()


          val api = retrofit.create(BodyFatService::class.java)
            val call = api.getBodyFatPercentage(weight, height,gender, age)
           call.enqueue(object : Callback<BodyFatResponse>{
                override fun onResponse(
                    call: Call<BodyFatResponse>,
                    response: Response<BodyFatResponse>
                ) {
                    if (response.isSuccessful){
                        val bodyFatPercentage = response.body()?.info?.description?: "Error"
                        binding.NavyResult.visibility = View.VISIBLE
                        binding.tvNavyValue.text = "$bodyFatPercentage"
                        println("Fat$bodyFatPercentage")
                        Toast.makeText(requireContext(), "Body fat percentage is $bodyFatPercentage",
                        Toast.LENGTH_LONG).show()
                    }else{
                        Toast.makeText(requireContext(), "Failed to calculate body fat percentage",
                            Toast.LENGTH_LONG).show()
                    }
                }

                override fun onFailure(call: Call<BodyFatResponse>, t: Throwable) {
                    Toast.makeText(requireContext(), "Something went wrong!",
                    Toast.LENGTH_LONG).show()
                    Log.e("Body Fat Percentage", "Error: ${t.message}")
                }
            })
        }

        return view
    }

   /* override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.btnCalculate.setOnClickListener{
           val height = binding.etHeight.text.toString().toDouble()
            val neck = binding.etNeck.text.toString().toDouble()
            val waist = binding.etWaist.text.toString().toDouble()
            val hip = binding.etHips.text.toString().toDouble()

            val bodyFatPercentage = calculatePercentage(height, neck, waist, hip)

            binding.NavyResult.visibility = View.VISIBLE
            binding.tvNavyValue.text = "$bodyFatPercentage%"
           // binding.tvNavyDescription.text = "Your body fat is normal."
        }
    }*/

  /*  private fun calculatePercentage(height: Double, neck: Double, waist: Double, hip: Double): BigDecimal? {

        val heightValue = height
        val neckValue = neck
        val waistValue = waist
        val hipValue = hip

        val bodyFat = 495 / (1.0324 - 0.19077 *
                (log10(waistValue+hipValue-neckValue))+0.22100*(log10(heightValue))) - 450


        val result = BigDecimal(bodyFat).setScale(2, RoundingMode.HALF_EVEN)

        return  result

    }*/


}

