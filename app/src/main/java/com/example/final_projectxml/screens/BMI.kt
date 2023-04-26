package com.example.final_projectxml.screens


import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.viewModels
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.lifecycleScope
import com.example.final_projectxml.databinding.FragmentBmiBinding
import kotlinx.coroutines.launch
import java.math.BigDecimal
import java.math.RoundingMode


class BMI : Fragment() {

    private var _binding: FragmentBmiBinding? = null
    private val binding get() = _binding!!

    private  val viewModelBMI: ViewModelBMI by viewModels()

    private var selectedItemId: Int = 0



    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentBmiBinding.inflate(layoutInflater, container, false)
        val view = binding.root

        savedInstanceState?.let {
            selectedItemId = it.getInt("selectedItemId", 0)
        }


        
        binding.btnCalculateBMI.setOnClickListener{
            if (validateMetricUnits()){

            //    val heightValue: Float = binding?.etHeight?.text.toString().toFloat() / 100 //to get height value in meters

                val heightValue: Float = binding.etHeight.text.toString().toFloat()
                val weightValue: Float = binding.etWeight.text.toString().toFloat()

              //  val bmi = weightValue / (heightValue*heightValue)
               viewModelBMI.calculateBMI(heightValue, weightValue)

                lifecycleScope.launch{
                    viewModelBMI.bmiState.collect{bmi ->
                        displayBMIResults(bmi)
                    }
                }

             //  displayBMIResults(bmi)


            } else{
                Toast.makeText(requireContext(), "Please enter valid values.", Toast.LENGTH_SHORT)
                    .show()

            }
        }

        return view
    }

    private fun displayBMIResults(bmi:Float){

        val bmiLable:String
        val bmiDescription: String

        when {
            bmi <= 15f -> {
                bmiLable = "Very severaly Underweight"
                bmiDescription = "Underweight, you need to eat more!Take care of yourself!"
            }
            bmi > 15f && bmi <= 16f -> {
                bmiLable = "Severely underweight"
                bmiDescription = "Underweight, you need to eat more!Take care of yourself!"
            }
            bmi > 16f && bmi <= 18.5f -> {
                bmiLable = "Underweight"
                bmiDescription = "Underweight, you need to eat more!Take care of yourself!"
            }
            bmi > 18.5f && bmi <= 25f -> {
                bmiLable = "Normal"
                bmiDescription = "You are in a good shape!"
            }
            bmi > 25f && bmi <= 30f -> {
                bmiLable = "Overweight"
                bmiDescription = "You need to take care of yourself!Track calories and activity"
            }
            bmi > 30f && bmi <= 35f -> {
                bmiLable = "Obesity"
                bmiDescription = "Act now! Take care of yourself!"
            }
            else -> {
                bmiLable = "Obese Class ||| (Very Severely obese)"
                bmiDescription = "OMG! Pull yourself together before it's too late!"
            }
        }

        val bmiValue = BigDecimal(bmi.toDouble()).setScale(2, RoundingMode.HALF_EVEN).toString() //cat value to display

        binding.bmiResult.visibility = View.VISIBLE
        binding.tvBMIValue.text = bmiValue
        binding.tvBMIType.text = bmiLable
        binding.tvBMIDescription.text = bmiDescription

    }

    //function to check if user entered values for both weight and height before doing any calculation
    private fun validateMetricUnits(): Boolean{
       var isValid = true

        if(binding.etWeight.text.toString().isEmpty()){
            isValid = false
        } else if(binding.etHeight.text.toString().isEmpty()){
            isValid = false
        }
        return isValid
    }


    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}